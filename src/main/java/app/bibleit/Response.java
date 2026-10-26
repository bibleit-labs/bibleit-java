package app.bibleit;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.*;
import java.net.http.HttpHeaders;
import java.util.Objects;

/** Streaming response. Always use try-with-resources, including after decode. */
public final class Response implements AutoCloseable {
    static final int MAX_JSON_BYTES = 4 * 1024 * 1024;
    private final int statusCode;
    private final HttpHeaders headers;
    private final InputStream body;
    public Response(int statusCode, HttpHeaders headers, InputStream body) {
        this.statusCode = statusCode; this.headers = Objects.requireNonNull(headers); this.body = Objects.requireNonNull(body);
    }
    public int statusCode() { return statusCode; }
    public HttpHeaders headers() { return headers; }
    public InputStream body() { return body; }
    public JsonNode decode() throws IOException { return decode(JsonNode.class); }
    public <T> T decode(Class<T> type) throws IOException {
        byte[] bytes = body.readNBytes(MAX_JSON_BYTES + 1);
        if (bytes.length > MAX_JSON_BYTES) throw new IOException("JSON response exceeds 4 MiB");
        return Json.MAPPER.readValue(bytes, type);
    }
    @Override public void close() throws IOException { body.close(); }
}
