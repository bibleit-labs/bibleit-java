package app.bibleit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Arrays;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BroadcastParityTest {
    private static final String ORG = "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee";
    private static String lifecycle(String kind,String json) {
        byte[] bytes = ("{\"value\":" + json + "}").getBytes(StandardCharsets.UTF_8);
        return "document_format=broadcast-lifecycle-json-v1 record_type=" + kind + " bytes=" + bytes.length + " data_base64=" + Base64.getEncoder().encodeToString(bytes);
    }
    private static SshClient ssh(String wire) {
        return new SshClient(command -> Protocol.decodeCommandResponse(command,ProtocolTest.wire(wire)));
    }
    @Test void sameChannelModelAndNullDisconnectedChannel() throws Exception {
        String json = "{\"id\":\"connection\",\"channel_id\":\"channel\",\"state\":\"connected\",\"revision\":4,\"future\":\"retained\"}";
        var sshResult = ssh("OK " + lifecycle("channel",json) + "\n").broadcast().channel(ORG);
        try (var response = ClientTest.response(200,json,Map.of())) {
            assertEquals(response.decode(Models.BroadcastChannel.class),sshResult.value());
        }
        assertEquals("retained",sshResult.value().additionalProperties().get("future"));
        assertNull(ssh("OK " + lifecycle("channel","null") + "\n").broadcast().channel(ORG).value());
        try (var response = ClientTest.response(200,"null",Map.of())) { assertNull(response.decode(Models.BroadcastChannel.class)); }
    }
    @Test void typedListsProfilesAndReceiptsKeepKnownScalarsAndUnknownFields() throws Exception {
        var page = ssh("OK count=1 limit=25 next=11111111-2222-3333-4444-555555555555\nSETUP id=11111111-2222-3333-4444-555555555555 organization_id=" + ORG + " name=Demo revision=4 future=value\nEND\n").broadcast().listSetups(ORG,25,"");
        assertEquals(4L,page.value().getData().getFirst().getRevision());
        assertEquals("value",page.value().getData().getFirst().additionalProperties().get("future"));
        assertEquals("11111111-2222-3333-4444-555555555555",page.value().getNextCursor());
        var profiles = ssh("OK count=1\nOUTPUT_PROFILE id=1080p30 version=1 width=1920 height=1080 fps=30 sampleRate=48000 channels=2\nEND\n").broadcast().profiles(ORG);
        assertEquals(48000L,profiles.value().getFirst().getSampleRate());
        var receipt = ssh("OK request_id=private-ticket result_state=applied terminal=true applied=true operation=save revision=5 replay=true future=retained\n").broadcast().writeStatus(ORG,"private-ticket");
        assertEquals(5L,receipt.value().getRevision());
        assertTrue(receipt.value().getReplay());
        assertEquals("retained",receipt.value().additionalProperties().get("future"));
        assertTrue(receipt.toString().equals("BroadcastResult[redacted]"));
    }
    @Test void rejectsMalformedEnvelopeCountsAndTypes() {
        assertThrows(IOException.class, () -> ssh("OK " + lifecycle("event","{}") + "\n").broadcast().channel(ORG));
        assertThrows(IOException.class, () -> ssh("OK " + lifecycle("event","null") + "\n").broadcast().getEvent(ORG,ORG));
        assertThrows(IOException.class, () -> ssh("OK document_format=broadcast-lifecycle-json-v1 record_type=channel bytes=1 data_base64=e30=\n").broadcast().channel(ORG));
        assertThrows(IOException.class, () -> ssh("OK " + lifecycle("channel","[]") + "\n").broadcast().channel(ORG));
        assertThrows(IOException.class, () -> ssh("OK count=2\nSETUP revision=1\nEND\n").broadcast().listSetups(ORG,25,""));
        assertThrows(IOException.class, () -> ssh("OK result_state=applied revision=invalid\n").broadcast().writeStatus(ORG,"ticket"));
    }
    @Test void browserRequiredAndConflictsRemainErrorsWithoutRetries() {
        var calls = new AtomicInteger();
        var ssh = new SshClient(command -> { calls.incrementAndGet(); return Protocol.decodeCommandResponse(command,ProtocolTest.wire("ERR browser_required\n")); });
        var error = assertThrows(ServerException.class, () -> ssh.broadcast().takeover(ORG));
        assertEquals("browser_required",error.code()); assertEquals(1,calls.get());
        var rest = new BibleitClient("bt_test", request -> { calls.incrementAndGet(); return ClientTest.response(409,"{\"code\":\"browser_required\"}",Map.of()); });
        var restError = assertThrows(ProblemException.class, () -> rest.requestBroadcastTakeover(ORG,Options.builder().json(Map.of()).build()));
        assertEquals("browser_required",restError.code()); assertEquals(2,calls.get());
    }
    @Test void signedSetupUploadRetainsExactBytesAndConditionalHeaders() throws Exception {
        byte[] body = "{ \n  \"name\" : \"Keep exact bytes\", \"version\" : 1\n}\n".getBytes(StandardCharsets.UTF_8);
        var calls = new AtomicInteger();
        var client = new BibleitClient("bt_test", request -> {
            calls.incrementAndGet();
            assertEquals("PUT",request.method());
            assertEquals("application/json",request.headers().firstValue("Content-Type").orElseThrow());
            assertEquals("private-ticket",request.headers().firstValue("Idempotency-Key").orElseThrow());
            assertEquals("\"setup-4\"",request.headers().firstValue("If-Match").orElseThrow());
            assertEquals(body.length,request.bodyPublisher().orElseThrow().contentLength());
            var bytes = new ByteArrayOutputStream(); var finished = new CompletableFuture<byte[]>();
            request.bodyPublisher().orElseThrow().subscribe(new Flow.Subscriber<ByteBuffer>() {
                @Override public void onSubscribe(Flow.Subscription subscription) { subscription.request(Long.MAX_VALUE); }
                @Override public void onNext(ByteBuffer buffer) { byte[] next = new byte[buffer.remaining()]; buffer.get(next); bytes.writeBytes(next); }
                @Override public void onError(Throwable error) { finished.completeExceptionally(error); }
                @Override public void onComplete() { finished.complete(bytes.toByteArray()); }
            });
            assertArrayEquals(body,finished.join());
            return ClientTest.response(200,"{\"result_state\":\"applied\",\"terminal\":true,\"applied\":true,\"revision\":5}",Map.of());
        });
        try (var response = client.replaceBroadcastSetup(ORG,ORG,Options.builder()
                .body(new ByteArrayInputStream(body),body.length,"application/json")
                .ifMatch("\"setup-4\"").idempotencyKey("private-ticket").build())) {
            assertEquals(5L,response.decode(Models.BroadcastWriteReceipt.class).getRevision());
        }
        assertEquals(1,calls.get());
    }
    @Test void setupExportValidatesExactRevisionHashAndContiguousChunks() throws Exception {
        byte[] document = ("{\"version\":1,\"name\":\"" + "x".repeat(3200) + "\"}").getBytes(StandardCharsets.UTF_8);
        String hash = HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(document));
        String meta = "organization_id=" + ORG + " id=" + ORG + " revision=4 document_format=studio-setup-json-v1 document_bytes=" + document.length + " document_sha256=" + hash + " chunk_bytes=3072";
        var calls = new AtomicInteger();
        var ssh = new SshClient(command -> {
            int call = calls.getAndIncrement();
            String wire;
            if (call == 0) wire = "OK name=Demo " + meta + "\n";
            else {
                int offset = (call - 1) * 3072, next = Math.min(document.length,offset+3072);
                assertTrue(command.wire().endsWith(" DOCUMENT 4 " + offset));
                byte[] bytes = Arrays.copyOfRange(document,offset,next);
                wire = "OK " + meta + " offset=" + offset + " bytes=" + bytes.length + " next_offset=" + next + " eof=" + (next == document.length) + " data_base64=" + Base64.getEncoder().encodeToString(bytes) + "\n";
            }
            return Protocol.decodeCommandResponse(command,ProtocolTest.wire(wire));
        });
        var setup = ssh.broadcast().getSetup(ORG,ORG).value();
        assertEquals(4L,setup.getRevision()); assertEquals("x".repeat(3200),setup.getDocument().getName());
        assertEquals(3,calls.get());
        var corrupt = new SshClient(command -> Protocol.decodeCommandResponse(command,ProtocolTest.wire(
            "OK " + meta + (command.wire().endsWith(" GET") ? "" : " offset=0 bytes=1 next_offset=1 eof=false data_base64=eA==") + "\n")));
        assertThrows(IOException.class, () -> corrupt.broadcast().getSetup(ORG,ORG));
    }

}
