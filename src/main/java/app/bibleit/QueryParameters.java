package app.bibleit;

import java.util.List;

/** Typed query values generated for a specific REST operation. */
public interface QueryParameters {
    /**
     * Returns the immutable ordered wire parameters, omitting unset values.
     * @return the unencoded parameter list
     */
    List<QueryParameter> parameters();

    /**
     * Creates a query without parameters for the generic REST entry point.
     * @return an empty query
     */
    static QueryParameters empty() { return List::of; }
}
