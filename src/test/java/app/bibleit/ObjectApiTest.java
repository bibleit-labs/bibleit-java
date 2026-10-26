package app.bibleit;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.http.HttpRequest;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObjectApiTest {
    static final String HEARTBEAT = "{\"schema_version\":1,\"type\":\"stream.heartbeat\",\"resource_type\":\"live\",\"resource_id\":\"live\",\"cursor\":\"opaque\",\"sequence\":2,\"data\":null}";

    static String requestBody(HttpRequest request) throws Exception {
        var bytes = new ByteArrayOutputStream();
        var done = new CompletableFuture<Void>();
        request.bodyPublisher().orElseThrow().subscribe(new Flow.Subscriber<ByteBuffer>() {
            public void onSubscribe(Flow.Subscription subscription) { subscription.request(Long.MAX_VALUE); }
            public void onNext(ByteBuffer buffer) {
                byte[] chunk = new byte[buffer.remaining()]; buffer.get(chunk); bytes.writeBytes(chunk);
            }
            public void onError(Throwable error) { done.completeExceptionally(error); }
            public void onComplete() { done.complete(null); }
        });
        done.get(5, TimeUnit.SECONDS);
        return bytes.toString(StandardCharsets.UTF_8);
    }

    @Test void typedModelsKeepOrdinaryObjectReferences() throws Exception {
        var json = ClientTest.page("null",16);
        var page = ClientTest.response(200,json,Map.of()).decode(Models.ScriptureBookRead.class);
        var verse = page.getVerses().getFirst();
        verse.setText("Updated text");
        assertSame(verse,page.getVerses().getFirst());
        var decoded = Json.MAPPER.readValue(Json.MAPPER.writeValueAsBytes(page),Models.ScriptureBookRead.class);
        assertEquals("Updated text",decoded.getVerses().getFirst().getText());
        assertEquals(16,decoded.getVerses().getFirst().getVerse());
    }

    @Test void genericResponseDecodingReturnsTypedCollections() throws Exception {
        try (var response = ClientTest.response(200,"[{\"text\":\"hello\",\"verse\":16}]",Map.of())) {
            var verses = response.decode(new TypeReference<List<Models.Verse>>() {});
            assertEquals("hello",verses.getFirst().getText());
            assertEquals(16,verses.getFirst().getVerse());
        }
    }

    @Test void fieldPresenceAndUnknownValuesRemainLossless() throws Exception {
        var live = new Models.CreateLive();
        live.setName(null); assertTrue(live.hasField("name"));
        assertEquals("{\"name\":null}",Json.MAPPER.writeValueAsString(live));
        live.removeField("name"); assertFalse(live.hasField("name")); assertNull(live.getName());
        live.setField("name","Updated"); assertEquals("Updated",live.getName());
        live.setField("future",null); assertTrue(live.hasField("future"));
        var copy = Json.MAPPER.readValue(Json.MAPPER.writeValueAsBytes(live),Models.CreateLive.class);
        assertEquals("Updated",copy.getName()); assertTrue(copy.hasField("future"));
        assertNull(copy.additionalProperties().get("future"));
        assertThrows(UnsupportedOperationException.class, () -> copy.additionalProperties().put("x",1));
        assertEquals("\"live.get\"",Json.MAPPER.writeValueAsString(new Models.Scope("live.get")));
    }

    @Test void pollOverloadsOmitCursorAndUseDefaultWait() throws Exception {
        var requests = new ArrayList<HttpRequest>();
        var client = new BibleitClient("",request -> { requests.add(request); return ClientTest.response(200,HEARTBEAT,Map.of()); });
        var event = client.pollLiveEvents("live");
        assertEquals("stream.heartbeat",event.getType()); assertNull(event.getData());
        assertEquals(2,event.getSequence());
        assertEquals("wait=25",requests.getLast().uri().getRawQuery());
        client.pollLiveEvents("live",0);
        assertEquals("wait=0",requests.getLast().uri().getRawQuery());
        String cursor = "v1:"+"a".repeat(64);
        client.pollLiveEvents("live",cursor);
        assertEquals("after=v1%3A"+"a".repeat(64)+"&wait=25",requests.getLast().uri().getRawQuery());
        client.pollLiveEvents("live",cursor,3);
        assertTrue(requests.getLast().uri().getRawQuery().endsWith("&wait=3"));
    }

    @Test void liveSnapshotsHaveTypedNestedObjects() throws Exception {
        String json = "{\"schema_version\":1,\"type\":\"live.changed\",\"resource_type\":\"live\",\"resource_id\":\"live\",\"cursor\":\"opaque\",\"data\":{"
            + "\"live\":{\"id\":\"live\",\"account_id\":\"account\",\"sequence\":3},"
            + "\"display\":{\"live_id\":\"live\",\"type\":\"empty\",\"snapshot\":null,\"verses\":[]},"
            + "\"queue\":null,\"viewer_settings\":{\"theme\":\"dark\"},\"design\":{\"revision\":4}}}";
        var client = new BibleitClient("",request -> ClientTest.response(200,json,Map.of()));
        var event = client.pollLiveEvents("live");
        assertEquals("account",event.getData().getLive().getAccountId());
        assertEquals("live",event.getData().getDisplay().getLiveId());
        assertEquals("dark",event.getData().getViewerSettings().getTheme());
        assertEquals(4,event.getData().getDesign().getRevision());
        assertNull(event.getData().getQueue());
        var roundtrip = Json.MAPPER.readValue(Json.MAPPER.writeValueAsBytes(event),Models.LiveEvent.class);
        assertEquals("account",roundtrip.getData().getLive().getAccountId());
    }

    @Test void unionRequestsSerializeWithoutInventedWrappers() throws Exception {
        var reference = new Models.ScriptureReference(); reference.setTranslation("web"); reference.setChapter(3L);
        var display = new Models.ReplaceDisplay(); display.setType("scripture"); display.setReference(reference);
        var result = Json.MAPPER.readValue(Json.MAPPER.writeValueAsBytes(display),Models.ReplaceDisplay.class);
        assertEquals("scripture",result.getType()); assertEquals(3,result.getReference().getChapter());
        assertFalse(result.hasField("value"));
    }

    @Test void malformedTypedEventFieldsFailWithoutCoercion() {
        for (String json : List.of("null",HEARTBEAT.replace("\"schema_version\":1","\"schema_version\":\"1\""),
            HEARTBEAT.replace("\"cursor\":\"opaque\"","\"cursor\":123"),
            HEARTBEAT.replace("\"data\":null","\"data\":[]"))) {
            var client = new BibleitClient("",request -> ClientTest.response(200,json,Map.of()));
            assertThrows(IOException.class, () -> client.pollLiveEvents("live"));
        }
    }

    @Test void authorizationReturnsTypedSecretsAndOmittedDeviceName() throws Exception {
        var requests = new ArrayList<HttpRequest>();
        var auth = new AuthClient(request -> {
            requests.add(request);
            return ClientTest.response(200,"{\"actor\":\"actor\",\"access_token\":\"bt_fixture\",\"identity_name\":\"name\",\"future\":true}",Map.of());
        });
        var result = auth.exchangeCode("code","verifier");
        assertEquals("bt_fixture",result.getAccessToken()); assertEquals("name",result.getIdentityName());
        assertEquals(Boolean.TRUE,result.additionalProperties().get("future"));
        assertFalse(result.toString().contains("bt_fixture"));
        assertEquals(List.of("code=code", "code_verifier=verifier"),
            java.util.Arrays.stream(requestBody(requests.getFirst()).split("&")).sorted().toList());
    }

    @Test void explicitJsonNullDoesNotNeedATreeNode() throws Exception {
        var requests = new ArrayList<HttpRequest>();
        var client = new BibleitClient("",request -> { requests.add(request); return ClientTest.response(200,"{}",Map.of()); });
        try (var ignored = client.createLive("organization",Options.builder().json(null).build())) {
            assertEquals("null",requestBody(requests.getFirst()));
            assertEquals("application/json",requests.getFirst().headers().firstValue("Content-Type").orElseThrow());
        }
        assertThrows(IllegalArgumentException.class, () -> Options.builder().json(null).body(ProtocolTest.wire("x"),1,"text/plain").build());
    }

    @Test void errorsExposeTypedDefensivelyCopiedDetails() {
        var client = new BibleitClient("",request -> ClientTest.response(403,"{\"code\":\"forbidden\",\"detail\":\"private\",\"future\":true}",Map.of()));
        var error = assertThrows(ProblemException.class,client::getPersonalIdentity);
        assertEquals("private",error.fields().getDetail());
        error.fields().setCode("changed"); assertEquals("forbidden",error.code());
        assertFalse(error.toString().contains("private"));
    }

    @Test void pageValidationUsesTypedCoordinatesAndAnIndependentRightsSnapshot() throws Exception {
        for (String verse : List.of("1.5","2147483648","null")) {
            String json = ClientTest.page("null",1).replace("\"verse\":1","\"verse\":"+verse);
            var client = new BibleitClient("",request -> ClientTest.response(200,json,Map.of()));
            assertThrows(UncheckedIOException.class, () -> client.readPersonalBookPages("account","web","43").iterator().next());
        }
        var calls = new AtomicInteger();
        var client = new BibleitClient("",request -> ClientTest.response(200,ClientTest.page(calls.getAndIncrement()==0?"\"next\"":"null",calls.get()),Map.of()));
        var iterator = client.readPersonalBookPages("account","web","43").iterator();
        iterator.next().getRights().setField("local_change",true);
        assertNotNull(iterator.next()); assertFalse(iterator.hasNext());
    }

    @Test void optionalSshArgumentsHaveConvenienceOverloads() {
        assertEquals("HELP",Commands.helpCommand().wire());
        assertEquals("LIVE CREATE",Commands.createLiveCommand().wire());
        assertEquals("ORGANIZATION LIST",Commands.organizationListCommand().wire());
        assertEquals("LIVE live STACK POP",Commands.popLiveCommand("live").wire());
        assertEquals("LIVE live SECRET CREATE",Commands.liveSecretCommand("live",Commands.SecretAction.CREATE).wire());
        assertThrows(IllegalArgumentException.class, () -> Commands.liveSecretCommand("live",Commands.SecretAction.AUTHENTICATE));
        assertEquals("READ web 43",Commands.readCommand(new Reference("web","43")).wire());
        assertEquals("READ web 43 3",Commands.readCommand(new Reference("web","43",3)).wire());
    }
}
