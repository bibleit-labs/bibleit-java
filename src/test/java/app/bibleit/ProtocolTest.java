package app.bibleit;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DynamicTest;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

class ProtocolTest {
    static InputStream wire(String value) { return new ByteArrayInputStream(value.getBytes(StandardCharsets.UTF_8)); }
    @TestFactory Stream<DynamicTest> sharedResponseFixtures() throws IOException {
        var fixtures = Json.MAPPER.readTree(Files.readString(Path.of("contract/fixtures/responses-v1.json")));
        var tests = new ArrayList<DynamicTest>();
        for (var fixture : fixtures) tests.add(DynamicTest.dynamicTest(fixture.path("name").asText(), () -> {
            String value = fixture.path("wire").asText();
            if (fixture.path("invalid").asBoolean()) { assertThrows(IOException.class, () -> Protocol.decodeResponse(wire(value))); return; }
            if (fixture.has("error_code")) {
                var error = assertThrows(ServerException.class, () -> Protocol.decodeResponse(wire(value)));
                assertEquals(fixture.path("error_code").asText(), error.code());
                fixture.path("error_fields").properties().forEach(entry -> assertEquals(entry.getValue().asText(), error.fields().get(entry.getKey())));
                return;
            }
            var result = Protocol.decodeResponse(wire(value));
            assertEquals(fixture.path("records").size(), result.records().size());
            for (int i = 0; i < result.records().size(); i++) {
                var expected = fixture.path("records").get(i); var record = result.records().get(i);
                assertEquals(expected.path("type").asText(), record.type());
                assertEquals(Json.MAPPER.convertValue(expected.path("fields"), Map.class), record.fields());
            }
        }));
        return tests.stream();
    }
    @Test void commandsAndFraming() throws Exception {
        assertEquals("READ web \"Song of Songs\" 2 1", Commands.readCommand(new Reference("web", "Song of Songs", 2, 1)).wire());
        assertEquals("SEARCH web \"love \\ peace\"", Commands.searchCommand("web", "love \\ peace").wire());
        assertThrows(IllegalArgumentException.class, () -> Commands.createLiveCommand("bad\"name"));
        assertThrows(IllegalArgumentException.class, () -> Commands.createLiveCommand("bad\nPING"));
        assertEquals("LIVE id STACK POP", Commands.popLiveCommand("id", 0).wire());
        assertThrows(IllegalArgumentException.class, () -> Commands.popLiveCommand("id", -1));
        assertThrows(IllegalArgumentException.class, () -> Commands.liveSecretCommand("id", Commands.SecretAction.AUTHENTICATE, "rotate"));
        assertThrows(IllegalArgumentException.class, () -> new SshClient(c -> null).execute(Commands.liveCommand("id", Commands.LiveAction.SUBSCRIBE)));
        assertEquals(1, Protocol.decodeCommandResponse(Commands.translationListCommand(), wire("OK count=0\n")).records().size());
        assertThrows(IOException.class, () -> Protocol.decodeResponse(wire("OK count=0\n")));
        assertEquals("Command[redacted]", Commands.liveSecretCommand("id", Commands.SecretAction.AUTHENTICATE, "secret").toString());
    }
    @Test void rejectsInvalidUtf8AndScalars() {
        assertThrows(IOException.class, () -> Protocol.decodeResponse(new ByteArrayInputStream(new byte[]{79,75,32,(byte)0xff,10})));
        assertThrows(IOException.class, () -> Protocol.parseRecord("OK x=\"\\ud800\""));
    }
}
