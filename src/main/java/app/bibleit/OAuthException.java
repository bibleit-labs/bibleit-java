package app.bibleit;

import com.fasterxml.jackson.databind.JsonNode;
import java.net.http.HttpHeaders;

/**
 * OAuth error with status, original JSON and response headers.
 */
public final class OAuthException extends ProblemException {
    /**
     * Creates an OAuth protocol failure retaining response metadata.
     * @param status the HTTP failure status
     * @param headers the response headers
     * @param fields the original server fields
     */
    public OAuthException(int status, HttpHeaders headers, JsonNode fields) { super(status, headers, fields); }
    /**
     * Returns the untrusted OAuth error description for deliberate application handling.
     * @return the description result
     */
    public String description() { return fields().path("error_description").asText(""); }
}
