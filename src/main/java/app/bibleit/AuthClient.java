package app.bibleit;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Official CLI browser-code exchange. Does not open a browser or store credentials.
 */
public final class AuthClient {
    private final Transport transport;
    /**
     * Creates a browser-code exchange client on the fixed website origin.
     */
    public AuthClient() { this(Transport.standard()); }
    /**
     * Creates a browser-code exchange client on the fixed website origin.
     * @param transport the trusted application transport
     */
    public AuthClient(Transport transport) { this.transport = Objects.requireNonNull(transport); }
    /**
     * Exchanges a single-use authorization code without an SDK retry loop.
     * @param code the single-use authorization or server error code
     * @param verifier the private PKCE verifier
     * @param deviceName an optional display label for the device
     * @return the original authorization object, including the secret access token
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public Models.AuthorizationResult exchangeCode(String code, String verifier, String deviceName) throws IOException, InterruptedException {
        OAuthClient.required(code, verifier);
        var form = new LinkedHashMap<>(Map.of("code", code, "code_verifier", verifier));
        if (deviceName != null && !deviceName.isEmpty()) form.put("device_name", deviceName);
        try (var response = BibleitClient.checked(transport.send(OAuthClient.formRequest("POST", "/api/cli/token", form)))) {
            if (response.statusCode() != 200) throw new IOException("Invalid authorization status");
            var fields = response.decode(Models.AuthorizationResult.class);
            if (fields == null || fields.getActor() == null || fields.getActor().isEmpty()
                || fields.getAccessToken() == null || fields.getAccessToken().isEmpty()
                || !fields.getAccessToken().equals(fields.getAccessToken().strip())
                || fields.getAccessToken().chars().anyMatch(c -> c < 32 || c == 127))
                throw new IOException("Invalid authorization response");
            return fields;
        }
    }
    /**
     * Exchanges a browser code without assigning an optional device label.
     * @param code the approved single-use code
     * @param verifier the private PKCE verifier
     * @return the typed authorization result containing the secret access token
     * @throws IOException if the request or response fails
     * @throws InterruptedException if execution is interrupted
     */
    public Models.AuthorizationResult exchangeCode(String code, String verifier) throws IOException, InterruptedException {
        return exchangeCode(code, verifier, null);
    }
}
