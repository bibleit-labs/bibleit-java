package app.bibleit;

import com.fasterxml.jackson.databind.JsonNode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorkspaceConsumerTest {
    private static final String WORKSPACE = "00000000-0000-4000-8000-000000000001";
    private static final String PROFILE = "{\"organization_id\":\"" + WORKSPACE
        + "\",\"name\":\"Synthetic workspace\",\"scopes\":[\"broadcast.get\"]}";

    @Test void responsesDecodeWorkspaceReadScopesWithoutClosingFutureWireValues() throws Exception {
        try (var response = ClientTest.response(200,
                "{\"scopes_supported\":[\"identity.read\",\"broadcast.get\"]}", Map.of())) {
            var metadata = response.decode(Models.McpOAuthMetadata.class);
            assertEquals(List.of("identity.read", "broadcast.get"), metadata.getScopesSupported()
                .stream().map(Models.McpOAuthScope::value).toList());
        }
        try (var response = ClientTest.response(200,
                "{\"data\":[{\"scopes\":[\"identity.read\",\"broadcast.get\"]}]}", Map.of())) {
            var grants = response.decode(Models.OAuthGrantList.class);
            assertEquals(List.of("identity.read", "broadcast.get"), grants.getData().getFirst()
                .getScopes().stream().map(Models.OAuthGrantedScope::value).toList());
        }
        assertEquals("future.scope", Json.MAPPER.readValue("\"future.scope\"",
            Models.McpOAuthScope.class).value());
        assertEquals("future.scope", Json.MAPPER.readValue("\"future.scope\"",
            Models.OAuthGrantedScope.class).value());
    }

    @Test void typedWorkspaceProfileRoundTripsAndRetainsUnknownFields() throws Exception {
        String wire = "{\"scopes\":[\"identity.read\",\"broadcast.get\"],"
            + "\"workspace_permissions\":[" + PROFILE + "],\"future\":\"retained\"}";
        try (var response = ClientTest.response(200, wire, Map.of())) {
            var probe = response.decode(Models.McpOAuthProbe.class);
            assertTrue(probe.hasField("workspace_permissions"));
            var permission = probe.getWorkspacePermissions().getFirst();
            assertEquals(WORKSPACE, permission.getOrganizationId());
            assertEquals("Synthetic workspace", permission.getName());
            assertEquals(List.of("broadcast.get"), permission.getScopes());
            assertFalse(probe.additionalProperties().containsKey("workspace_permissions"));
            assertEquals("retained", probe.additionalProperties().get("future"));
            assertEquals(Json.MAPPER.readTree(wire), Json.MAPPER.valueToTree(probe));
        }
        var oldProbe = Json.MAPPER.readValue("{\"scopes\":[\"identity.read\"]}",
            Models.McpOAuthProbe.class);
        assertNull(oldProbe.getWorkspacePermissions());
        assertFalse(oldProbe.hasField("workspace_permissions"));
        assertFalse(Json.MAPPER.valueToTree(oldProbe).has("workspace_permissions"));
    }

    @Test void packagedContractCarriesOnlyTheReviewedResponseVocabulary() throws Exception {
        byte[] resource;
        try (var stream = Models.class.getResourceAsStream("/app/bibleit/openapi.json")) {
            assertNotNull(stream);
            resource = stream.readAllBytes();
        }
        assertEquals("a4f30711e0b588a5d4f3344daefbbbc470fe5f8dd71c4698c93aa12101f01692",
            HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(resource)));
        assertEquals(Json.MAPPER.readTree(Files.readAllBytes(Path.of("contract/openapi.json"))),
            Json.MAPPER.readTree(resource));
        var schemas = Json.MAPPER.readTree(resource).path("components").path("schemas");
        assertTrue(contains(schemas.path("McpOAuthScope").path("enum"), "broadcast.get"));
        assertTrue(contains(schemas.path("OAuthGrantedScope").path("enum"), "broadcast.get"));
        assertFalse(contains(schemas.path("OAuthScope").path("enum"), "broadcast.get"));
        assertFalse(contains(schemas.path("OAuthMetadata").path("properties")
            .path("scopes_supported").path("items").path("enum"), "broadcast.get"));
        var profile = schemas.path("McpWorkspacePermission");
        assertFalse(profile.path("additionalProperties").asBoolean(true));
        assertEquals("broadcast.get", profile.path("properties").path("scopes")
            .path("items").path("const").asText());
        assertEquals(1, profile.path("properties").path("scopes").path("minItems").asInt());
        assertEquals(1, profile.path("properties").path("scopes").path("maxItems").asInt());
        assertEquals(1, schemas.path("McpOAuthProbe").path("properties")
            .path("workspace_permissions").path("maxItems").asInt());
        assertFalse(contains(schemas.path("OAuthResourceError").path("properties")
            .path("error").path("enum"), "not_found"));
        assertTrue(contains(schemas.path("McpWorkspaceConsentError").path("properties")
            .path("error").path("enum"), "not_found"));
    }

    private static boolean contains(JsonNode values, String expected) {
        for (var value : values) if (value.asText().equals(expected)) return true;
        return false;
    }
}
