package app.bibleit;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

/**
 * SSH protocol error. Unknown server codes and fields are preserved.
 */
public final class ServerException extends IOException {
    /** Stable SSH domain error code.
     * @serial Stable SSH domain error code.
     */
    private final String code;
    /** Original immutable SSH error fields.
     * @serial Original immutable SSH error fields.
     */
    private final Map<String, String> fields;
    /**
     * Creates an SSH domain failure retaining unknown codes and metadata.
     * @param code the single-use authorization or server error code
     * @param fields the original server fields
     */
    public ServerException(String code, Map<String, String> fields) { super("Bibleit SSH: " + code); this.code = code; this.fields = Map.copyOf(fields); }
    /**
     * Returns the stable server error code, including unknown codes.
     * @return the code result
     */
    public String code() { return code; }
    /**
     * Returns the original server metadata.
     * @return the immutable SSH error fields
     */
    public Map<String, String> fields() { return fields; }
    /**
     * Returns the nonnegative server retry delay, or zero when absent or invalid.
     * @return the retry after result
     */
    public Duration retryAfter() {
        try { return Duration.ofMillis(Math.max(0, Long.parseLong(fields.getOrDefault("retry_after_ms", "0")))); }
        catch (NumberFormatException e) { return Duration.ZERO; }
    }
}
