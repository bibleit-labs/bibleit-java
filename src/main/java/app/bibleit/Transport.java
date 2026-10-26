package app.bibleit;

import java.io.IOException;
import java.net.http.HttpRequest;

/** Trusted application transport. Implementations own TLS, cancellation and replay policy. */
@FunctionalInterface
public interface Transport {
    Response send(HttpRequest request) throws IOException, InterruptedException;
    /** TLS-verifying JDK transport with no cookies or redirects. */
    static Transport standard() { return new JdkTransport(); }
}
