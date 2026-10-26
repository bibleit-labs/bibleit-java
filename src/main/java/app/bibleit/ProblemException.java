package app.bibleit;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.net.http.HttpHeaders;
import java.time.Duration;

/**
 * HTTP failure retaining server fields. The message excludes untrusted server detail.
 */
public class ProblemException extends IOException {
    /** HTTP failure status.
     * @serial HTTP failure status.
     */
    private final int status;
    /** Original immutable response headers.
     * @serial Original immutable response headers.
     */
    private final HttpHeaders headers;
    /** Original structured problem fields.
     * @serial Original structured problem fields.
     */
    private final JsonNode fields;
    /**
     * Creates an HTTP problem retaining the original status, headers and server fields.
     * @param status the HTTP failure status
     * @param headers the response headers
     * @param fields the original server fields
     */
    public ProblemException(int status, HttpHeaders headers, JsonNode fields) {
        super("Bibleit HTTP " + status + ": " + fields.path("code").asText(fields.path("error").asText("unknown")));
        this.status = status; this.headers = headers; this.fields = fields;
    }
    /**
     * Returns the HTTP failure status.
     * @return the status result
     */
    public int status() { return status; }
    /**
     * Returns the stable server error code, including unknown codes.
     * @return the code result
     */
    public String code() { return fields.path("code").asText(fields.path("error").asText("")); }
    /**
     * Returns the problem request ID, falling back to the HTTP response header.
     * @return the request id result
     */
    public String requestId() { return fields.path("request_id").asText(headers.firstValue("X-Request-Id").orElse("")); }
    /**
     * Returns the original server metadata.
     * @return a defensive copy of the original JSON problem fields
     */
    public JsonNode fields() { return fields.deepCopy(); }
    /**
     * Returns the immutable HTTP response headers.
     * @return the response headers
     */
    public HttpHeaders headers() { return headers; }
    /**
     * Returns the nonnegative server retry delay, or zero when absent or invalid.
     * @return the retry after result
     */
    public Duration retryAfter() {
        try { return Duration.ofSeconds(Math.max(0, Long.parseLong(headers.firstValue("Retry-After").orElse("0")))); }
        catch (NumberFormatException e) { return Duration.ZERO; }
    }
}
