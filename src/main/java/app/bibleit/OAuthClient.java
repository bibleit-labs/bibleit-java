package app.bibleit;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Registered delegated OAuth. Applications validate state and securely rotate stored tokens.
 */
public final class OAuthClient {
    private final Transport transport;
    /**
     * Creates a registered OAuth client on the fixed website origin.
     */
    public OAuthClient() { this(Transport.standard()); }
    /**
     * Creates a registered OAuth client on the fixed website origin.
     * @param transport the trusted application transport
     */
    public OAuthClient(Transport transport) { this.transport = Objects.requireNonNull(transport); }
    /**
     * Builds a consent URI with S256 PKCE and explicit scopes; the application validates callback state.
     * @param clientId the registered OAuth client identifier
     * @param redirectUri the registered callback URI
     * @param state the caller-generated callback state
     * @param challenge the S256 PKCE challenge
     * @param scopes the requested scope names
     * @return the website consent URI
     */
    public static URI authorizationUrl(String clientId, String redirectUri, String state, String challenge, List<String> scopes) {
        required(clientId, redirectUri, state);
        if (!challenge.matches("[A-Za-z0-9_-]{43}") || scopes.isEmpty() || scopes.stream().anyMatch(String::isBlank))
            throw new IllegalArgumentException("S256 challenge and explicit scopes required");
        return URI.create(BibleitClient.SERVER_ENDPOINT + "/oauth/authorize?" + form(Map.of("response_type", "code", "client_id", clientId,
            "redirect_uri", redirectUri, "state", state, "code_challenge", challenge, "code_challenge_method", "S256", "scope", String.join(" ", scopes))));
    }
    /**
     * Reads the website’s OAuth authorization-server metadata.
     * @return the authorization-server metadata
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public Models.OAuthMetadata metadata() throws IOException, InterruptedException {
        try (var response = request("GET", "/.well-known/oauth-authorization-server", Map.of())) { return response.decode(Models.OAuthMetadata.class); }
    }
    /**
     * Exchanges a single-use authorization code without an SDK retry loop.
     * @param clientId the registered OAuth client identifier
     * @param code the single-use authorization or server error code
     * @param redirectUri the registered callback URI
     * @param verifier the private PKCE verifier
     * @return the issued token values; the caller owns secure storage
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public Models.OAuthToken exchangeCode(String clientId, String code, String redirectUri, String verifier) throws IOException, InterruptedException {
        required(clientId, code, redirectUri);
        if (!verifier.matches("[A-Za-z0-9._~-]{43,128}")) throw new IllegalArgumentException("Invalid PKCE verifier");
        return token(Map.of("grant_type", "authorization_code", "client_id", clientId, "code", code, "redirect_uri", redirectUri, "code_verifier", verifier));
    }
    /**
     * Rotates a single-use refresh token; the application must replace stored credentials atomically.
     * @param clientId the registered OAuth client identifier
     * @param refreshToken the single-use refresh token
     * @param scopes the requested scope names
     * @return the rotated tokens; replace stored credentials atomically
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public Models.OAuthToken refresh(String clientId, String refreshToken, List<String> scopes) throws IOException, InterruptedException {
        required(clientId, refreshToken);
        var values = new LinkedHashMap<>(Map.of("grant_type", "refresh_token", "client_id", clientId, "refresh_token", refreshToken));
        if (!scopes.isEmpty()) values.put("scope", String.join(" ", scopes));
        return token(values);
    }
    /**
     * Rotates a refresh token without narrowing its existing scopes.
     * @param clientId the registered OAuth client identifier
     * @param refreshToken the single-use refresh token
     * @return the rotated token values
     * @throws IOException if the request or response fails
     * @throws InterruptedException if execution is interrupted
     */
    public Models.OAuthToken refresh(String clientId, String refreshToken) throws IOException, InterruptedException {
        return refresh(clientId, refreshToken, List.of());
    }
    /**
     * Revokes the supplied OAuth token.
     * @param clientId the registered OAuth client identifier
     * @param token the explicit bearer credential, or an empty string for public discovery
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
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
            Models.OAuthError fields;
            try { fields = response.decode(Models.OAuthError.class); } catch (IOException e) { fields = new Models.OAuthError(); }
            if (fields == null) fields = new Models.OAuthError();
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
        var query = new ArrayList<QueryParameter>(); values.forEach((k, v) -> query.add(new QueryParameter(k, v)));
        return BibleitClient.encodeQuery(query);
    }
    static void required(String... values) {
        for (String value : values) if (value == null || value.isEmpty()) throw new IllegalArgumentException("Required authorization value missing");
    }
}
