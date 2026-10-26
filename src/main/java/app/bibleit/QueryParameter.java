package app.bibleit;

import java.util.Objects;

/**
 * One immutable wire parameter produced by a typed query object.
 * @param name the wire parameter name
 * @param value the unencoded wire value
 */
public record QueryParameter(String name, String value) {
    /**
     * Creates a non-null parameter; encoding happens at request construction.
     * @param name the wire parameter name
     * @param value the unencoded wire value
     */
    public QueryParameter {
        Objects.requireNonNull(name);
        Objects.requireNonNull(value);
    }
}
