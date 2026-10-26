package app.bibleit;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.net.http.HttpHeaders;
import java.time.Duration;

/** HTTP failure retaining server fields. The message excludes untrusted server detail. */
public class ProblemException extends IOException {
    private final int status;
    private final HttpHeaders headers;
    private final JsonNode fields;
    public ProblemException(int status, HttpHeaders headers, JsonNode fields) {
        super("Bibleit HTTP " + status + ": " + fields.path("code").asText(fields.path("error").asText("unknown")));
        this.status = status; this.headers = headers; this.fields = fields;
    }
    public int status() { return status; }
    public String code() { return fields.path("code").asText(fields.path("error").asText("")); }
    public String requestId() { return fields.path("request_id").asText(headers.firstValue("X-Request-Id").orElse("")); }
    public JsonNode fields() { return fields.deepCopy(); }
    public HttpHeaders headers() { return headers; }
    public Duration retryAfter() {
        try { return Duration.ofSeconds(Math.max(0, Long.parseLong(headers.firstValue("Retry-After").orElse("0")))); }
        catch (NumberFormatException e) { return Duration.ZERO; }
    }
}
