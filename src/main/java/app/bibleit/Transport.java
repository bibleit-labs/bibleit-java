package app.bibleit;

import java.io.IOException;
import java.net.http.HttpRequest;

/**
 * Trusted application transport. Implementations own TLS, cancellation and replay policy.
 */
@FunctionalInterface
public interface Transport {
    /**
     * Sends one request through this trusted transport.
     * @param request the fixed-origin HTTP request
     * @return the streaming response; the caller must close it
     * @throws IOException if transport processing fails
     * @throws InterruptedException if execution is interrupted
     */
    Response send(HttpRequest request) throws IOException, InterruptedException;
    /**
     * Creates a TLS-verifying JDK transport with no cookies or redirects.
     * @return the default transport
     */
    static Transport standard() { return new JdkTransport(); }
}
