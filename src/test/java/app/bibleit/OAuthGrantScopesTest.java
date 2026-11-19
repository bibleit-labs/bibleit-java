package app.bibleit;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OAuthGrantScopesTest {
    @Test void grantResponseUsesItsOwnScopeTypeAndPreservesWireValues() throws Exception {
        String wire = "{\"id\":\"00000000000000000000000000000000\",\"client_id\":\"synthetic-response-client\",\"client_name\":\"Synthetic response only\",\"scopes\":[\"identity.read\",\"live.queue.stage\",\"live.rename\"],\"expires_at\":1}";
        try (var response = ClientTest.response(200, wire, Map.of())) {
            var grant = response.decode(Models.OAuthGrant.class);
            List<Models.OAuthGrantedScope> scopes = grant.getScopes();
            assertEquals(List.of("identity.read", "live.queue.stage", "live.rename"), scopes.stream().map(Models.OAuthGrantedScope::value).toList());
            assertInstanceOf(Models.OAuthGrantedScope.class, scopes.getFirst());
            assertEquals(Json.MAPPER.readTree(wire), Json.MAPPER.valueToTree(grant));
        }
        var future = Json.MAPPER.readValue("\"future.unknown\"", Models.OAuthGrantedScope.class);
        assertEquals("future.unknown", future.value());
    }

    @Test void pinnedContractKeepsOrdinaryAndGrantedScopeValidationSeparate() throws Exception {
        var schemas = Json.MAPPER.readTree(Files.readString(Path.of("contract/openapi.json"))).path("components").path("schemas");
        assertEquals("#/components/schemas/OAuthGrantedScope", schemas.path("OAuthGrant").path("properties").path("scopes").path("items").path("$ref").asText());
        for (var scope : List.of("live.queue.stage", "live.rename")) {
            var wire = Json.MAPPER.valueToTree(scope);
            boolean inResponse = false, inRequest = false;
            for (var value : schemas.path("OAuthGrantedScope").path("enum")) inResponse |= value.equals(wire);
            for (var value : schemas.path("OAuthScope").path("enum")) inRequest |= value.equals(wire);
            assertTrue(inResponse, scope);
            assertFalse(inRequest, scope);
        }
    }
}
