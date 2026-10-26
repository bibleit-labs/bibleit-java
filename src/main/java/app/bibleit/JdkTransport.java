package app.bibleit;

import java.io.IOException;
import java.net.http.*;
import java.time.Duration;

/** JDK transport. The SDK calls send once; the JDK may recover connection establishment failures. */
final class JdkTransport implements Transport {
    private final HttpClient client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(10)).followRedirects(HttpClient.Redirect.NEVER)
        .version(HttpClient.Version.HTTP_1_1).build();
    public Response send(HttpRequest request) throws IOException, InterruptedException {
        var response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
        return new Response(response.statusCode(), response.headers(), response.body());
    }
}
