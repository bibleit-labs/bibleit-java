package app.bibleit;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BroadcastCommandsTest {
    private static final String ORG = "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee";
    private static final String ID = "11111111-2222-3333-4444-555555555555";
    private static final String HASH = "ab".repeat(32);
    private static String wire(String suffix) { return "BROADCAST " + ORG + " " + suffix; }

    @Test void discoveryAndRevisionBoundReads() {
        assertEquals(wire("PROFILES"), BroadcastCommands.profiles(ORG).wire());
        assertEquals(wire("CHANNEL"), BroadcastCommands.channel(ORG).wire());
        assertEquals(wire("PREFLIGHT"), BroadcastCommands.preflight(ORG).wire());
        assertEquals(wire("EVENTS"), BroadcastCommands.events(ORG).wire());
        assertEquals(wire("PUBLISHERS"), BroadcastCommands.publishers(ORG).wire());
        assertEquals(wire("TAKEOVER"), BroadcastCommands.takeover(ORG).wire());
        assertEquals(wire("SETUP LIST 25"), BroadcastCommands.listSetups(ORG,25,"").wire());
        assertEquals(wire("SETUP LIST 99 " + ID), BroadcastCommands.listSetups(ORG,99,ID).wire());
        assertEquals(wire("SETUP " + ID + " GET"), BroadcastCommands.getSetup(ORG,ID).wire());
        assertEquals(wire("SETUP " + ID + " DOCUMENT 5 3072"), BroadcastCommands.setupDocument(ORG,ID,5,3072).wire());
        // The exact document end may be unaligned and is a valid EOF request.
        assertEquals(wire("SETUP " + ID + " DOCUMENT 5 9"), BroadcastCommands.setupDocument(ORG,ID,5,9).wire());
        assertEquals("HELP BROADCAST", Commands.helpCommand("broadcast").wire());
    }

    @Test void immutableIntentsAndPersistentUploadCommands() {
        assertEquals(wire("WRITE INTENT CREATE new 0 2 " + HASH), BroadcastCommands.writeIntent(ORG,BroadcastCommands.WriteOperation.CREATE,"new",0,2,HASH).wire());
        for (var op : new BroadcastCommands.WriteOperation[]{BroadcastCommands.WriteOperation.SAVE,BroadcastCommands.WriteOperation.DUPLICATE,BroadcastCommands.WriteOperation.DELETE})
            assertEquals(wire("WRITE INTENT " + op + " " + ID + " 7 1048576 " + HASH), BroadcastCommands.writeIntent(ORG,op,ID,7,1048576,HASH).wire());
        assertEquals(wire("WRITE BEGIN ticket"), BroadcastCommands.beginWrite(ORG,"ticket").wire());
        assertEquals(wire("WRITE STATUS ticket"), BroadcastCommands.writeStatus(ORG,"ticket").wire());
        assertEquals(wire("WRITE INSPECT " + ID), BroadcastCommands.inspectWrite(ORG,ID).wire());
        assertEquals(wire("WRITE ABORT " + ID), BroadcastCommands.abortWrite(ORG,ID).wire());
        assertEquals(wire("WRITE COMMIT ticket " + ID), BroadcastCommands.commitWrite(ORG,"ticket",ID).wire());
        var data = new byte[3072];
        String chunk = BroadcastCommands.writeChunk(ORG,ID,3072,data).wire();
        assertEquals(wire("WRITE CHUNK " + ID + " 3072 " + Base64.getEncoder().encodeToString(data)),chunk);
        assertTrue(chunk.getBytes(StandardCharsets.UTF_8).length <= 8192);
        assertFalse(BroadcastCommands.beginWrite(ORG,"ticket").streaming());
        assertEquals(wire("WRITE STATUS " + "a".repeat(768)),BroadcastCommands.writeStatus(ORG,"a".repeat(768)).wire());
        assertFalse(BroadcastCommands.beginWrite(ORG,"private-ticket").toString().contains("private-ticket"));
    }

    @Test void lifecycleDoesNotStartBrowserCaptureAndRetainsRequestIdentity() {
        byte[] metadata = "{ \"title\": \"Event\" }".getBytes(StandardCharsets.UTF_8);
        assertEquals(wire("EVENT CREATE " + ID + " " + Base64.getEncoder().encodeToString(metadata)), BroadcastCommands.createEvent(ORG,ID,metadata).wire());
        assertEquals(wire("EVENT ATTACH " + ID + " Provider_1-"), BroadcastCommands.attachEvent(ORG,ID,"Provider_1-").wire());
        assertEquals(wire("EVENT " + ID + " GO-LIVE 2 " + ID), BroadcastCommands.transitionEvent(ORG,ID,BroadcastCommands.EventAction.GO_LIVE,2,ID).wire());
        assertEquals(wire("EVENT " + ID + " TEST 2 " + ID), BroadcastCommands.transitionEvent(ORG,ID,BroadcastCommands.EventAction.TEST,2,ID).wire());
        assertEquals(wire("EVENT " + ID + " END 2 " + ID), BroadcastCommands.transitionEvent(ORG,ID,BroadcastCommands.EventAction.END,2,ID).wire());
        assertEquals(wire("PUBLISHER " + ID + " STOP 3"), BroadcastCommands.stopPublisher(ORG,ID,3).wire());
        assertEquals(wire("EVENT " + ID + " GET"), BroadcastCommands.getEvent(ORG,ID).wire());
        assertEquals(wire("OPERATION " + ID + " GET"), BroadcastCommands.getOperation(ORG,ID).wire());
        assertEquals(wire("PUBLISHER " + ID + " GET"), BroadcastCommands.getPublisher(ORG,ID).wire());
    }

    @Test void rejectsMalformedAndOutOfBoundsBeforeTransport() throws Exception {
        var count = new AtomicInteger();
        var client = new SshClient(command -> { count.incrementAndGet(); return new CommandResponse(java.util.List.of(), java.util.List.of()); });
        assertThrows(IllegalArgumentException.class, () -> client.execute(BroadcastCommands.profiles(ORG.toUpperCase())));
        assertThrows(IllegalArgumentException.class, () -> BroadcastCommands.getEvent(ORG,ID + "\nPING"));
        assertThrows(IllegalArgumentException.class, () -> BroadcastCommands.listSetups(ORG,100,""));
        assertThrows(IllegalArgumentException.class, () -> BroadcastCommands.listSetups(ORG,0,""));
        assertThrows(IllegalArgumentException.class, () -> BroadcastCommands.writeIntent(ORG,BroadcastCommands.WriteOperation.CREATE,ID,1,2,HASH));
        assertThrows(IllegalArgumentException.class, () -> BroadcastCommands.writeIntent(ORG,BroadcastCommands.WriteOperation.SAVE,ID,0,2,HASH));
        assertThrows(IllegalArgumentException.class, () -> BroadcastCommands.writeIntent(ORG,BroadcastCommands.WriteOperation.CREATE,"new",0,1048577,HASH));
        assertThrows(IllegalArgumentException.class, () -> BroadcastCommands.writeIntent(ORG,BroadcastCommands.WriteOperation.CREATE,"new",0,2,HASH.toUpperCase()));
        assertThrows(IllegalArgumentException.class, () -> BroadcastCommands.beginWrite(ORG,"a".repeat(769)));
        assertThrows(IllegalArgumentException.class, () -> BroadcastCommands.beginWrite(ORG,"ticket\nPING"));
        for (String ticket : new String[]{"", "$(command)", "`command`", "ticket;command", "ticket=value", "ticket/value", "ticket+value", "tïcket"}) {
            assertThrows(IllegalArgumentException.class, () -> BroadcastCommands.beginWrite(ORG,ticket));
            assertThrows(IllegalArgumentException.class, () -> BroadcastCommands.writeStatus(ORG,ticket));
            assertThrows(IllegalArgumentException.class, () -> BroadcastCommands.commitWrite(ORG,ticket,ID));
        }
        assertThrows(IllegalArgumentException.class, () -> BroadcastCommands.writeChunk(ORG,ID,0,new byte[3073]));
        assertThrows(IllegalArgumentException.class, () -> BroadcastCommands.writeChunk(ORG,ID,1048576,new byte[1]));
        assertThrows(IllegalArgumentException.class, () -> BroadcastCommands.setupDocument(ORG,ID,1,-1));
        assertThrows(IllegalArgumentException.class, () -> BroadcastCommands.createEvent(ORG,ID,new byte[3073]));
        assertThrows(IllegalArgumentException.class, () -> BroadcastCommands.attachEvent(ORG,ID,"https://youtube.com/"));
        assertThrows(IllegalArgumentException.class, () -> BroadcastCommands.stopPublisher(ORG,ID,0));
        assertEquals(0,count.get());
    }
}
