package app.bibleit;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;

/** Registered delegated OAuth. Applications validate state and securely rotate stored tokens. */
public final class OAuthClient {
    private final Transport transport;
    public OAuthClient() { this(Transport.standard()); }
    public OAuthClient(Transport transport) { this.transport = Objects.requireNonNull(transport); }
    public static URI authorizationUrl(String clientId, String redirectUri, String state, String challenge, List<String> scopes) {
        required(clientId, redirectUri, state);
        if (!challenge.matches("[A-Za-z0-9_-]{43}") || scopes.isEmpty() || scopes.stream().anyMatch(String::isBlank))
            throw new IllegalArgumentException("S256 challenge and explicit scopes required");
        return URI.create(BibleitClient.SERVER_ENDPOINT + "/oauth/authorize?" + form(Map.of("response_type", "code", "client_id", clientId,
            "redirect_uri", redirectUri, "state", state, "code_challenge", challenge, "code_challenge_method", "S256", "scope", String.join(" ", scopes))));
    }
    public Models.OAuthMetadata metadata() throws IOException, InterruptedException {
        try (var response = request("GET", "/.well-known/oauth-authorization-server", Map.of())) { return response.decode(Models.OAuthMetadata.class); }
    }
    public Models.OAuthToken exchangeCode(String clientId, String code, String redirectUri, String verifier) throws IOException, InterruptedException {
        required(clientId, code, redirectUri);
        if (!verifier.matches("[A-Za-z0-9._~-]{43,128}")) throw new IllegalArgumentException("Invalid PKCE verifier");
        return token(Map.of("grant_type", "authorization_code", "client_id", clientId, "code", code, "redirect_uri", redirectUri, "code_verifier", verifier));
    }
    public Models.OAuthToken refresh(String clientId, String refreshToken, List<String> scopes) throws IOException, InterruptedException {
        required(clientId, refreshToken);
        var values = new LinkedHashMap<>(Map.of("grant_type", "refresh_token", "client_id", clientId, "refresh_token", refreshToken));
        if (!scopes.isEmpty()) values.put("scope", String.join(" ", scopes));
        return token(values);
    }
    public void revoke(String clientId, String token) throws IOException, InterruptedException {
        required(clientId, token);
        try (var response = request("POST", "/oauth/revoke", Map.of("client_id", clientId, "token", token))) { /* closed */ }
    }
    private Models.OAuthToken token(Map<String, String> values) throws IOException, InterruptedException {
        try (var response = request("POST", "/oauth/token", values)) { return response.decode(Models.OAuthToken.class); }
    }
    private Response request(String method, String path, Map<String, String> values) throws IOException, InterruptedException {
        var response = transport.send(formRequest(method, path, values));
        if (response.statusCode() >= 200 && response.statusCode() < 300) return response;
        try (response) {
            com.fasterxml.jackson.databind.JsonNode fields;
            try { fields = response.decode(); } catch (IOException e) { fields = Json.MAPPER.createObjectNode(); }
            throw new OAuthException(response.statusCode(), response.headers(), fields);
        }
    }
    static HttpRequest formRequest(String method, String path, Map<String, String> values) {
        String encoded = form(values);
        if (encoded.getBytes(StandardCharsets.UTF_8).length > 16384) throw new IllegalArgumentException("Authorization form exceeds 16 KiB");
        var request = HttpRequest.newBuilder(URI.create(BibleitClient.SERVER_ENDPOINT + path)).timeout(Duration.ofSeconds(40)).header("Accept", "application/json");
        if (method.equals("POST")) request.header("Content-Type", "application/x-www-form-urlencoded");
        return request.method(method, method.equals("GET") ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(encoded)).build();
    }
    static String form(Map<String, String> values) {
        var query = new LinkedHashMap<String, List<String>>(); values.forEach((k, v) -> query.put(k, List.of(v)));
        return BibleitClient.encodeQuery(query);
    }
    static void required(String... values) {
        for (String value : values) if (value == null || value.isEmpty()) throw new IllegalArgumentException("Required authorization value missing");
    }
}
