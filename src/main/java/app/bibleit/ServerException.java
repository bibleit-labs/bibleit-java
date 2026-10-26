package app.bibleit;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

/** SSH protocol error. Unknown server codes and fields are preserved. */
public final class ServerException extends IOException {
    private final String code;
    private final Map<String, String> fields;
    public ServerException(String code, Map<String, String> fields) { super("Bibleit SSH: " + code); this.code = code; this.fields = Map.copyOf(fields); }
    public String code() { return code; }
    public Map<String, String> fields() { return fields; }
    public Duration retryAfter() {
        try { return Duration.ofMillis(Math.max(0, Long.parseLong(fields.getOrDefault("retry_after_ms", "0")))); }
        catch (NumberFormatException e) { return Duration.ZERO; }
    }
}
