package app.bibleit;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Parsed record preserving unknown fields and their original order.
 * @param type the type
 * @param fields the original wire fields
 * @param order the order
 */
public record ProtocolRecord(String type, Map<String, String> fields, List<String> order) {
    /**
     * Copies and validates the protocol record components.
     * @param type the type
     * @param fields the fields
     * @param order the order
     */
    public ProtocolRecord { fields = Collections.unmodifiableMap(new LinkedHashMap<>(fields)); order = List.copyOf(order); }
    @Override public String toString() { return "ProtocolRecord[type=" + type + ", fields=redacted]"; }
}
