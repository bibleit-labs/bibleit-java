package app.bibleit;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

class SshClientTest {
    static SshClient client(String fixture) throws IOException {
        var fixtures = Json.MAPPER.readTree(Files.readString(Path.of("contract/fixtures/responses-v1.json")));
        for (var f : fixtures) if (f.path("name").asText().equals(fixture))
            return new SshClient(command -> Protocol.decodeCommandResponse(command,ProtocolTest.wire(f.path("wire").asText())));
        throw new IllegalArgumentException(fixture);
    }
    @Test void typedIdentityAndReading() throws Exception {
        var identity = client("typed-identity").getIdentity();
        assertTrue(identity.authenticated()); assertFalse(identity.actor().isEmpty());
        var reading = client("typed-single-verse").readVerses(new Reference("web","43",3,16));
        assertEquals(1,reading.verses().size()); assertFalse(reading.verses().getFirst().text().isEmpty());
        assertNotNull(reading.raw());
    }
    @Test void sparseCatalogRetainsSourceIdentifiers() throws Exception {
        var catalog = client("translation-catalog-sparse").getTranslationCatalog("web");
        assertEquals(1,catalog.books().size());
        assertEquals(19,catalog.books().getFirst().id());
        assertTrue(catalog.books().getFirst().chapters().getFirst().verses()>0);
    }
    @Test void helpQuotasTokensAndOrganizations() throws Exception {
        assertFalse(client("help-account").help("ACCOUNT").commands().isEmpty());
        assertFalse(client("account-quotas").listQuotas().quotas().isEmpty());
        assertTrue(client("token-empty").listTokens().tokens().isEmpty());
        assertFalse(client("token-metadata-client-contract").listTokens().tokens().isEmpty());
        assertEquals(2,client("organization-directory-mixed").listOrganizations("").entries().size());
        assertEquals("organization_deleted",client("organization-application-deleted-status").getOrganizationApplicationStatus("id").application().state());
    }
    @Test void typedLiveRepliesAndControlValidation() throws Exception {
        assertNotNull(client("typed-live-stack").getLiveStack("live").entries());
        var ssh = new SshClient(c -> { throw new AssertionError("Invalid control must not execute"); });
        assertThrows(IllegalArgumentException.class, () -> ssh.controlLive("live",Commands.LiveAction.INFO));
    }
    @Test void rejectsCountAndCoordinateMismatches() {
        var ssh = new SshClient(c -> Protocol.decodeResponse(ProtocolTest.wire("OK translation=web book=43 verses=2\nVERSE text=one\nEND\n")));
        assertThrows(IOException.class, () -> ssh.readVerses(new Reference("web","43",3,0)));
        var bad = new SshClient(c -> Protocol.decodeResponse(ProtocolTest.wire("OK translation=web book=43 verses=1\nVERSE text=one book=43\nEND\n")));
        assertThrows(IOException.class, () -> bad.readVerses(new Reference("web","43",3,0)));
    }
}
