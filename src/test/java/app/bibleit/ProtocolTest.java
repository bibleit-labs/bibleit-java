package app.bibleit;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProtocolTest {
    static InputStream wire(String value) { return new ByteArrayInputStream(value.getBytes(StandardCharsets.UTF_8)); }
    @TestFactory Stream<DynamicTest> sharedResponseFixtures() throws IOException {
        var fixtures = TestFixtures.responses();
        var tests = new ArrayList<DynamicTest>();
        for (var fixture : fixtures) tests.add(DynamicTest.dynamicTest(fixture.name(), () -> {
            String value = fixture.wire();
            if (fixture.invalid()) { assertThrows(IOException.class, () -> Protocol.decodeResponse(wire(value))); return; }
            if (fixture.errorCode() != null) {
                var error = assertThrows(ServerException.class, () -> Protocol.decodeResponse(wire(value)));
                assertEquals(fixture.errorCode(), error.code());
                if (fixture.errorFields() != null) assertEquals(fixture.errorFields(), error.fields());
                return;
            }
            var result = Protocol.decodeResponse(wire(value));
            assertEquals(fixture.records().size(), result.records().size());
            for (int i = 0; i < result.records().size(); i++) {
                var expected = fixture.records().get(i); var record = result.records().get(i);
                assertEquals(expected.type(), record.type());
                assertEquals(expected.fields(), record.fields());
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
