package app.bibleit;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.*;

/** Official CLI browser-code exchange. Does not open a browser or store credentials. */
public final class AuthClient {
    private final Transport transport;
    public AuthClient() { this(Transport.standard()); }
    public AuthClient(Transport transport) { this.transport = Objects.requireNonNull(transport); }
    public JsonNode exchangeCode(String code, String verifier, String deviceName) throws IOException, InterruptedException {
        OAuthClient.required(code, verifier);
        var form = new LinkedHashMap<>(Map.of("code", code, "code_verifier", verifier));
        if (deviceName != null && !deviceName.isEmpty()) form.put("device_name", deviceName);
        try (var response = BibleitClient.checked(transport.send(OAuthClient.formRequest("POST", "/api/cli/token", form)))) {
            if (response.statusCode() != 200) throw new IOException("Invalid authorization status");
            var fields = response.decode();
            if (!fields.path("actor").isTextual() || fields.path("actor").asText().isEmpty()
                || !fields.path("access_token").isTextual() || fields.path("access_token").asText().isEmpty()
                || fields.path("access_token").asText().chars().anyMatch(c -> c < 32 || c == 127)) throw new IOException("Invalid authorization response");
            return fields;
        }
    }
}
