package app.bibleit;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;
class LiveOverlayTest {
 private static java.io.InputStream reply(String text) { return new java.io.ByteArrayInputStream(text.getBytes(java.nio.charset.StandardCharsets.UTF_8)); }
 @Test void validatedConditionalCommands() {
  assertEquals("LIVE L1 OVERLAY GET",Commands.getLiveOverlayCommand("L1").wire());
  assertEquals("LIVE L1 OVERLAY SET builtin-youtube-stream-link 7",Commands.setLiveOverlayCommand("L1","builtin-youtube-stream-link",7).wire());
  assertEquals("LIVE L1 OVERLAY CLEAR 9223372036854775807",Commands.clearLiveOverlayCommand("L1",Long.MAX_VALUE).wire());
  assertThrows(IllegalArgumentException.class,()->Commands.clearLiveOverlayCommand("L1",-1));
  assertThrows(IllegalArgumentException.class,()->Commands.setLiveOverlayCommand("L1","bad\ncommand",0));
 }
 @Test void typedReplyPreservesOptionalAndFutureFields() throws Exception {
  var calls=new AtomicInteger();
  var client=new SshClient(cmd->{calls.incrementAndGet();return Protocol.decodeCommandResponse(cmd,reply(cmd.wire().endsWith("GET") ? "OK live_id=L1 sequence=7 active=true design_id=builtin-translation design_revision=1 name=\"Translation\" future=kept\n" : "OK live_id=L1 sequence=8 active=false\n"));});
  var selected=client.getLiveOverlay("L1");assertEquals(7,selected.sequence());assertNull(selected.overlay().youtubeUrl());assertEquals("kept",selected.fields().get("future"));
  assertNull(client.clearLiveOverlay("L1",7).overlay());assertEquals(2,calls.get());
 }
 @Test void malformedReplyFails() {
  var client=new SshClient(cmd->Protocol.decodeCommandResponse(cmd,reply("OK live_id=L1 sequence=7 active=true\n")));
  assertThrows(IOException.class,()->client.getLiveOverlay("L1"));
 }
 @Test void restMutationUsesExplicitSequenceAndNeverRetries() throws Exception {
  var calls=new AtomicInteger();
  var client=new BibleitClient("svc_fixture",request->{calls.incrementAndGet();assertEquals("PUT",request.method());assertEquals("/v1/lives/L1/overlay",request.uri().getPath());assertEquals("\"live-7\"",request.headers().firstValue("If-Match").orElseThrow());return ClientTest.response(412,"{\"error\":\"precondition_failed\"}",java.util.Map.of());});
  assertThrows(ProblemException.class,()->client.setLiveOverlay("L1",Options.builder().ifMatch("\"live-7\"").json(java.util.Collections.singletonMap("design_id",null)).build()));
  assertEquals(1,calls.get());
 }

 @Test void generatedModelsPreserveExplicitNull() throws Exception {
  var input=new Models.LiveOverlayInput();assertFalse(input.hasField("design_id"));input.setDesignId(null);assertTrue(input.hasField("design_id"));
  assertEquals("{\"design_id\":null}",Json.MAPPER.writeValueAsString(input));
  var state=Json.MAPPER.readValue("{\"live_id\":\"L1\",\"sequence\":7,\"overlay\":null}",Models.LiveOverlayState.class);
  assertTrue(state.hasField("overlay"));assertNull(state.getOverlay());assertEquals(7L,state.getSequence());
 }

}
