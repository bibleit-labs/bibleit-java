package app.bibleit;

import java.net.http.HttpHeaders;

/**
 * OAuth error with status, original JSON and response headers.
 */
public final class OAuthException extends ProblemException {
    /**
     * Creates an OAuth protocol failure retaining response metadata.
     * @param status the HTTP failure status
     * @param headers the response headers
     * @param fields the original server fields
     */
    public OAuthException(int status, HttpHeaders headers, Models.OAuthError fields) { super(status, headers, Json.MAPPER.convertValue(fields, Models.Problem.class)); }
    /**
     * Returns the untrusted OAuth error description for deliberate application handling.
     * @return the description result
     */
    public String description() {
        var description = error().getErrorDescription();
        return description == null ? "" : description;
    }
    /**
     * Returns the typed OAuth protocol error.
     * @return a defensive copy of the OAuth error
     */
    public Models.OAuthError error() { return Json.MAPPER.convertValue(fields(), Models.OAuthError.class); }
}
