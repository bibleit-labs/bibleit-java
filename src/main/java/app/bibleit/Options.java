package app.bibleit;

import java.io.InputStream;
import java.time.Duration;
import java.util.Objects;

/**
 * Immutable per-request options. Raw streams are consumed once and never replayed.
 */
public final class Options {
    final Object json;
    final boolean hasJson;
    final InputStream body;
    final long contentLength;
    final String contentType, ifMatch, idempotencyKey, destinationToken, range;
    final Duration timeout;
    private Options(Builder b) {
        json = b.json; hasJson = b.hasJson; body = b.body; contentLength = b.contentLength;
        contentType = b.contentType; ifMatch = b.ifMatch; idempotencyKey = b.idempotencyKey;
        destinationToken = b.destinationToken; range = b.range; timeout = b.timeout;
        if (hasJson && body != null) throw new IllegalArgumentException("JSON and raw body are mutually exclusive");
        if (body != null && contentLength < 0) throw new IllegalArgumentException("Raw body requires exact content length");
        if (timeout.isNegative() || timeout.isZero()) throw new IllegalArgumentException("Timeout must be positive");
    }
    /**
     * Creates the default request options.
     * @return immutable default options
     */
    public static Options defaults() { return builder().build(); }
    /**
     * Creates an empty request-options builder.
     * @return a new empty builder
     */
    public static Builder builder() { return new Builder(); }
    /**
     * Copies these options into a new builder.
     * @return a new builder containing these option values
     */
    public Builder toBuilder() {
        var b = new Builder(); b.json = json; b.hasJson = hasJson; b.body = body;
        b.contentLength = contentLength; b.contentType = contentType; b.ifMatch = ifMatch;
        b.idempotencyKey = idempotencyKey; b.destinationToken = destinationToken;
        b.range = range; b.timeout = timeout; return b;
    }
    /**
     * Creates an empty request-options builder.
     */
    public static final class Builder {
        /**
         * Creates an empty request-options builder.
         */
        public Builder() {}
        private Object json;
        private boolean hasJson;
        private InputStream body;
        private long contentLength = -1;
        private String contentType = "", ifMatch = "", idempotencyKey = "", destinationToken = "", range = "";
        private Duration timeout = Duration.ofSeconds(40);
        /**
         * Sets a Java request body for Jackson serialization.
         * @param value the Java body; null writes an explicit JSON null
         * @return this builder
         */
        public Builder json(Object value) { json = value; hasJson = true; return this; }
        /**
         * Sets a one-use raw upload with its exact length and media type.
         * @param value the value to set
         * @param length the exact upload byte length
         * @param type the Jackson target class
         * @return this builder
         */
        public Builder body(InputStream value, long length, String type) { body = Objects.requireNonNull(value); contentLength = length; contentType = Objects.requireNonNull(type); return this; }
        /**
         * Sets the exact ETag required for a conditional write.
         * @param value the value to set
         * @return this builder
         */
        public Builder ifMatch(String value) { ifMatch = Objects.requireNonNull(value); return this; }
        /**
         * Sets an explicit operation-supported idempotency key.
         * @param value the value to set
         * @return this builder
         */
        public Builder idempotencyKey(String value) { idempotencyKey = Objects.requireNonNull(value); return this; }
        /**
         * Sets the destination service token for a workspace move.
         * @param value the value to set
         * @return this builder
         */
        public Builder destinationToken(String value) { destinationToken = Objects.requireNonNull(value); return this; }
        /**
         * Sets the requested binary byte range.
         * @param value the value to set
         * @return this builder
         */
        public Builder range(String value) { range = Objects.requireNonNull(value); return this; }
        /**
         * Sets a positive request timeout.
         * @param value the value to set
         * @return this builder
         */
        public Builder timeout(Duration value) { timeout = Objects.requireNonNull(value); return this; }
        /**
         * Copies and validates these options.
         * @return the validated immutable options
         */
        public Options build() { return new Options(this); }
    }
}
