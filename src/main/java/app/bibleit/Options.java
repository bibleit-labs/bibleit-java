package app.bibleit;

import java.io.InputStream;
import java.time.Duration;
import java.util.*;

/** Immutable per-request options. Raw streams are consumed once and never replayed. */
public final class Options {
    final Map<String, List<String>> query;
    final Object json;
    final InputStream body;
    final long contentLength;
    final String contentType, ifMatch, idempotencyKey, destinationToken, range;
    final Duration timeout;
    private Options(Builder b) {
        var copy = new LinkedHashMap<String, List<String>>();
        b.query.forEach((k, v) -> copy.put(k, List.copyOf(v)));
        query = Collections.unmodifiableMap(copy);
        json = b.json; body = b.body; contentLength = b.contentLength;
        contentType = b.contentType; ifMatch = b.ifMatch; idempotencyKey = b.idempotencyKey;
        destinationToken = b.destinationToken; range = b.range; timeout = b.timeout;
        if (json != null && body != null) throw new IllegalArgumentException("JSON and raw body are mutually exclusive");
        if (body != null && contentLength < 0) throw new IllegalArgumentException("Raw body requires exact content length");
        if (timeout.isNegative() || timeout.isZero()) throw new IllegalArgumentException("Timeout must be positive");
    }
    public static Options defaults() { return builder().build(); }
    public static Builder builder() { return new Builder(); }
    public Builder toBuilder() {
        var b = new Builder(); b.query.putAll(query); b.json = json; b.body = body;
        b.contentLength = contentLength; b.contentType = contentType; b.ifMatch = ifMatch;
        b.idempotencyKey = idempotencyKey; b.destinationToken = destinationToken;
        b.range = range; b.timeout = timeout; return b;
    }
    public static final class Builder {
        private final Map<String, List<String>> query = new LinkedHashMap<>();
        private Object json;
        private InputStream body;
        private long contentLength = -1;
        private String contentType = "", ifMatch = "", idempotencyKey = "", destinationToken = "", range = "";
        private Duration timeout = Duration.ofSeconds(40);
        public Builder query(String key, String... values) { query.put(key, List.of(values)); return this; }
        public Builder json(Object value) { json = Objects.requireNonNull(value); return this; }
        public Builder body(InputStream value, long length, String type) { body = Objects.requireNonNull(value); contentLength = length; contentType = Objects.requireNonNull(type); return this; }
        public Builder ifMatch(String value) { ifMatch = Objects.requireNonNull(value); return this; }
        public Builder idempotencyKey(String value) { idempotencyKey = Objects.requireNonNull(value); return this; }
        public Builder destinationToken(String value) { destinationToken = Objects.requireNonNull(value); return this; }
        public Builder range(String value) { range = Objects.requireNonNull(value); return this; }
        public Builder timeout(Duration value) { timeout = Objects.requireNonNull(value); return this; }
        public Options build() { return new Options(this); }
    }
}
