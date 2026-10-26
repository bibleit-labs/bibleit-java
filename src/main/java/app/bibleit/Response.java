package app.bibleit;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpHeaders;
import java.util.Objects;

/**
 * Streaming response. Always use try-with-resources, including after decode.
 */
public final class Response implements AutoCloseable {
    static final int MAX_JSON_BYTES = 4 * 1024 * 1024;
    private final int statusCode;
    private final HttpHeaders headers;
    private final InputStream body;
    /**
     * Creates a streaming response whose body is closed by this response.
     * @param statusCode the HTTP response status
     * @param headers the response headers
     * @param body the streaming body
     */
    public Response(int statusCode, HttpHeaders headers, InputStream body) {
        this.statusCode = statusCode; this.headers = Objects.requireNonNull(headers); this.body = Objects.requireNonNull(body);
    }
    /**
     * Returns the HTTP response status.
     * @return the status code result
     */
    public int statusCode() { return statusCode; }
    /**
     * Returns the immutable HTTP response headers.
     * @return the response headers
     */
    public HttpHeaders headers() { return headers; }
    /**
     * Returns the response stream for binary or application-selected decoding.
     * @return the response body stream
     */
    public InputStream body() { return body; }
    /**
     * Deserializes one bounded JSON response into a Java object.
     * @param <T> the decoded Java type
     * @param type the Jackson target class
     * @return the decoded Java value
     * @throws IOException if the response is invalid or exceeds the JSON size limit
     */
    public <T> T decode(Class<T> type) throws IOException {
        return Json.MAPPER.readValue(readJsonBytes(), type);
    }

    /**
     * Deserializes a parameterized Java type, such as a list of schema objects.
     * @param <T> the decoded Java type
     * @param type the Jackson generic type reference
     * @return the decoded Java value
     * @throws IOException if the response is invalid or exceeds the JSON size limit
     */
    public <T> T decode(TypeReference<T> type) throws IOException {
        return Json.MAPPER.readValue(readJsonBytes(), type);
    }

    private byte[] readJsonBytes() throws IOException {
        byte[] bytes = body.readNBytes(MAX_JSON_BYTES + 1);
        if (bytes.length > MAX_JSON_BYTES) throw new IOException("JSON response exceeds 4 MiB");
        return bytes;
    }
    @Override public void close() throws IOException { body.close(); }
}
