package app.bibleit;

import com.fasterxml.jackson.databind.JsonNode;
import java.net.http.HttpHeaders;

/** OAuth error with status, original JSON and response headers. */
public final class OAuthException extends ProblemException {
    public OAuthException(int status, HttpHeaders headers, JsonNode fields) { super(status, headers, fields); }
    public String description() { return fields().path("error_description").asText(""); }
}
