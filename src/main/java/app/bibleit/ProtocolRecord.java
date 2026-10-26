package app.bibleit;

import java.util.*;

/** Parsed record preserving unknown fields and their original order. */
public record ProtocolRecord(String type, Map<String, String> fields, List<String> order) {
    public ProtocolRecord { fields = Collections.unmodifiableMap(new LinkedHashMap<>(fields)); order = List.copyOf(order); }
    @Override public String toString() { return "ProtocolRecord[type=" + type + ", fields=redacted]"; }
}
