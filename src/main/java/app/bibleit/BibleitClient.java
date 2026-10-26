package app.bibleit;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Bibleit resource client. Credentials are explicit and service origins are fixed.
 */
public final class BibleitClient {
    /**
     * The fixed REST API origin and version prefix.
     */
    public static final String ENDPOINT = "https://api.bibleit.app/v1";
    /**
     * The fixed website origin for browser authentication and OAuth.
     */
    public static final String SERVER_ENDPOINT = "https://bibleit.app";
    private final String token;
    private final Transport transport;
    /**
     * Creates a resource client with explicit credentials and a fixed service origin.
     * @param token the explicit bearer credential, or an empty string for public discovery
     */
    public BibleitClient(String token) { this(token, Transport.standard()); }
    /**
     * Creates a resource client with explicit credentials and a fixed service origin.
     * @param token the explicit bearer credential, or an empty string for public discovery
     * @param transport the trusted application transport
     */
    public BibleitClient(String token, Transport transport) {
        this.token = Objects.requireNonNull(token);
        if (!token.equals(token.strip()) || token.chars().anyMatch(c -> c < 32 || c == 127))
            throw new IllegalArgumentException("Invalid bearer token");
        this.transport = Objects.requireNonNull(transport);
    }
    /**
     * Returns the immutable pinned REST operation directory.
     * @return the immutable operation map
     */
    public static Map<String, Operation> operations() { return Operations.ALL; }
    /**
     * Returns the sorted REST operation IDs.
     * @return the sorted immutable operation names
     */
    public static List<String> operationNames() { return Operations.ALL.keySet().stream().sorted().toList(); }
    /**
     * Invokes one REST operation from the pinned contract.
     * @param operation the operation ID from the pinned contract
     * @param parameters the named path identifiers
     * @param options immutable request options
     * @return the streaming response; the caller must close it
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public Response call(String operation, Map<String, String> parameters, Options options) throws IOException, InterruptedException {
        return call(operation, parameters, QueryParameters.empty(), options);
    }

    /**
     * Invokes a named REST operation with typed query parameters.
     * @param operation the pinned operation ID
     * @param parameters the named path identifiers
     * @param query the operation-specific query object
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public Response call(String operation, Map<String, String> parameters, QueryParameters query, Options options) throws IOException, InterruptedException {
        var op = Operations.ALL.get(operation);
        if (op == null) throw new IllegalArgumentException("Unknown REST operation");
        String path = op.path();
        for (var entry : parameters.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            String value = Objects.requireNonNull(entry.getValue());
            if (!path.contains(placeholder) || value.isEmpty() || value.equals(".") || value.equals("..")
                || value.chars().anyMatch(c -> c < 32 || c == 127 || c == '/' || c == '\\'))
                throw new IllegalArgumentException("Invalid path parameter");
            path = path.replace(placeholder, encode(value));
        }
        if (path.contains("{") || path.contains("}")) throw new IllegalArgumentException("Missing path parameter");
        var o = Objects.requireNonNull(options);
        String encodedQuery = encodeQuery(Objects.requireNonNull(query).parameters());
        var request = HttpRequest.newBuilder(URI.create(ENDPOINT + path + (encodedQuery.isEmpty() ? "" : "?" + encodedQuery)))
            .timeout(o.timeout).header("Accept", "application/json");
        if (!token.isEmpty()) request.header("Authorization", "Bearer " + token);
        if (!o.ifMatch.isEmpty()) request.header("If-Match", o.ifMatch);
        if (!o.idempotencyKey.isEmpty()) request.header("Idempotency-Key", o.idempotencyKey);
        if (!o.destinationToken.isEmpty()) request.header("X-Destination-Service-Token", o.destinationToken);
        if (!o.range.isEmpty()) request.header("Range", o.range);
        var publisher = HttpRequest.BodyPublishers.noBody();
        if (o.json != null) {
            publisher = HttpRequest.BodyPublishers.ofByteArray(Json.MAPPER.writeValueAsBytes(o.json));
            request.header("Content-Type", o.contentType.isEmpty() ? "application/json" : o.contentType);
        } else if (o.body != null) {
            publisher = o.contentLength == 0 ? HttpRequest.BodyPublishers.noBody()
                : HttpRequest.BodyPublishers.fromPublisher(HttpRequest.BodyPublishers.ofInputStream(() -> o.body), o.contentLength);
            request.header("Content-Type", o.contentType);
        }
        return checked(transport.send(request.method(op.method(), publisher).build()));
    }
    static String encode(String value) { return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20"); }
    static String encodeQuery(List<QueryParameter> query) {
        var pairs = new ArrayList<String>();
        for (var parameter : query) pairs.add(encode(parameter.name()) + "=" + encode(parameter.value()));
        return String.join("&", pairs);
    }
    static Response checked(Response response) throws IOException {
        if (response.statusCode() >= 200 && response.statusCode() < 300) return response;
        try (response) {
            JsonNode fields;
            try { fields = response.decode(); }
            catch (IOException e) { fields = Json.MAPPER.createObjectNode(); }
            throw new ProblemException(response.statusCode(), response.headers(), fields);
        }
    }
    /**
     * Revokes this client’s official CLI browser credential; only HTTP 204 acknowledges success.
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public void revokeCredential() throws IOException, InterruptedException {
        if (token.isEmpty()) throw new IllegalStateException("Browser credential required");
        var request = HttpRequest.newBuilder(URI.create(SERVER_ENDPOINT + "/api/cli/logout"))
            .timeout(java.time.Duration.ofSeconds(40)).header("Authorization", "Bearer " + token)
            .POST(HttpRequest.BodyPublishers.noBody()).build();
        try (var response = checked(transport.send(request))) {
            if (response.statusCode() != 204) throw new IOException("Invalid revocation acknowledgement");
        }
    }
    /**
     * Performs one bounded Live event poll and validates its version, resource and cursor.
     * @param liveId the Live resource identifier
     * @param after the opaque continuation cursor, or an empty string to start
     * @param wait the bounded long-poll wait in seconds, from 0 through 25
     * @return the validated event envelope
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public JsonNode pollLiveEvents(String liveId, String after, int wait) throws IOException, InterruptedException {
        if (wait < 0 || wait > 25) throw new IllegalArgumentException("Wait must be 0..25 seconds");
        var query = Queries.GetLiveEventsQuery.builder().waitSeconds(wait);
        if (after != null && !after.isEmpty()) query.after(after);
        try (var response = getLiveEvents(liveId, query.build())) {
            var event = response.decode(); String type = event.path("type").asText();
            boolean heartbeat = type.equals("stream.heartbeat");
            boolean noData = !event.has("data") || event.path("data").isNull();
            if (event.path("schema_version").asInt() != 1 || !Set.of("live.snapshot", "live.changed", "stream.heartbeat").contains(type)
                || !event.path("resource_type").asText().equals("live") || !event.path("resource_id").asText().equals(liveId)
                || !event.path("cursor").isTextual() || event.path("cursor").asText().isEmpty()
                || heartbeat != noData || !heartbeat && !event.path("data").isObject())
                throw new IOException("Invalid Live event");
            return event;
        }
    }
    /**
     * Creates a lazy iterable of personal Scripture book pages.
     * @param accountId the personal account or organization identifier
     * @param slug the translation slug
     * @param bookId the source book identifier
     * @param options immutable HTTP request options
     * @return a lazy, repeatable iterable of typed Scripture pages
     */
    public BookPages readPersonalBookPages(String accountId, String slug, String bookId, Options options) {
        return readPersonalBookPages(accountId, slug, bookId, Queries.ReadPersonalBookQuery.builder().build(), options);
    }
    /**
     * Creates a lazy iterable of personal Scripture book pages.
     * @param accountId the personal account or organization identifier
     * @param slug the translation slug
     * @param bookId the source book identifier
     * @param query the typed page limit and continuation cursor
     * @return a lazy, repeatable iterable of typed Scripture pages
     */
    public BookPages readPersonalBookPages(String accountId, String slug, String bookId, Queries.ReadPersonalBookQuery query) {
        return readPersonalBookPages(accountId, slug, bookId, query, Options.defaults());
    }
    /**
     * Creates a lazy iterable of personal Scripture book pages.
     * @param accountId the personal account or organization identifier
     * @param slug the translation slug
     * @param bookId the source book identifier
     * @param query the typed page limit and continuation cursor
     * @param options immutable HTTP request options
     * @return a lazy, repeatable iterable of typed Scripture pages
     */
    public BookPages readPersonalBookPages(String accountId, String slug, String bookId, Queries.ReadPersonalBookQuery query, Options options) {
        Objects.requireNonNull(query);
        Objects.requireNonNull(options);
        return new BookPages((limit, after) -> readPersonalBook(accountId, slug, bookId, query.toBuilder().limit(limit).after(after).build(), options), query.getLimit(), query.getAfter());
    }
    /**
     * Creates a lazy iterable of organization Scripture book pages.
     * @param organizationId the organization account or organization identifier
     * @param slug the translation slug
     * @param bookId the source book identifier
     * @param options immutable HTTP request options
     * @return a lazy, repeatable iterable of typed Scripture pages
     */
    public BookPages readBookPages(String organizationId, String slug, String bookId, Options options) {
        return readBookPages(organizationId, slug, bookId, Queries.ReadBookQuery.builder().build(), options);
    }
    /**
     * Creates a lazy iterable of organization Scripture book pages.
     * @param organizationId the organization account or organization identifier
     * @param slug the translation slug
     * @param bookId the source book identifier
     * @param query the typed page limit and continuation cursor
     * @return a lazy, repeatable iterable of typed Scripture pages
     */
    public BookPages readBookPages(String organizationId, String slug, String bookId, Queries.ReadBookQuery query) {
        return readBookPages(organizationId, slug, bookId, query, Options.defaults());
    }
    /**
     * Creates a lazy iterable of organization Scripture book pages.
     * @param organizationId the organization account or organization identifier
     * @param slug the translation slug
     * @param bookId the source book identifier
     * @param query the typed page limit and continuation cursor
     * @param options immutable HTTP request options
     * @return a lazy, repeatable iterable of typed Scripture pages
     */
    public BookPages readBookPages(String organizationId, String slug, String bookId, Queries.ReadBookQuery query, Options options) {
        Objects.requireNonNull(query);
        Objects.requireNonNull(options);
        return new BookPages((limit, after) -> readBook(organizationId, slug, bookId, query.toBuilder().limit(limit).after(after).build(), options), query.getLimit(), query.getAfter());
    }

    /**
     * Download the OpenAPI contract.
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getOpenApi(Options options) throws IOException, InterruptedException {
        return call("getOpenApi", Map.of(), options);
    }
    /**
     * Download the OpenAPI contract. Uses default request options.
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getOpenApi() throws IOException, InterruptedException {
        return getOpenApi(Options.defaults());
    }

    /**
     * Clear the retained scripture stack.
     * @param liveId the live_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response clearLiveStack(String liveId, Options options) throws IOException, InterruptedException {
        return call("clearLiveStack", Map.of("live_id", liveId), options);
    }
    /**
     * Clear the retained scripture stack. Uses default request options.
     * @param liveId the live_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response clearLiveStack(String liveId) throws IOException, InterruptedException {
        return clearLiveStack(liveId, Options.defaults());
    }

    /**
     * Create a one-time audience secret.
     * @param liveId the live_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response createLiveAudienceSecret(String liveId, Options options) throws IOException, InterruptedException {
        return call("createLiveAudienceSecret", Map.of("live_id", liveId), options);
    }
    /**
     * Create a one-time audience secret. Uses default request options.
     * @param liveId the live_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response createLiveAudienceSecret(String liveId) throws IOException, InterruptedException {
        return createLiveAudienceSecret(liveId, Options.defaults());
    }

    /**
     * Remove audience-secret protection.
     * @param liveId the live_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response deleteLiveAudienceSecret(String liveId, Options options) throws IOException, InterruptedException {
        return call("deleteLiveAudienceSecret", Map.of("live_id", liveId), options);
    }
    /**
     * Remove audience-secret protection. Uses default request options.
     * @param liveId the live_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response deleteLiveAudienceSecret(String liveId) throws IOException, InterruptedException {
        return deleteLiveAudienceSecret(liveId, Options.defaults());
    }

    /**
     * Read the Live display.
     * @param liveId the live_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getLiveDisplay(String liveId, Options options) throws IOException, InterruptedException {
        return call("getLiveDisplay", Map.of("live_id", liveId), options);
    }
    /**
     * Read the Live display. Uses default request options.
     * @param liveId the live_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getLiveDisplay(String liveId) throws IOException, InterruptedException {
        return getLiveDisplay(liveId, Options.defaults());
    }

    /**
     * Read Live playback state.
     * @param liveId the live_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getLivePlayback(String liveId, Options options) throws IOException, InterruptedException {
        return call("getLivePlayback", Map.of("live_id", liveId), options);
    }
    /**
     * Read Live playback state. Uses default request options.
     * @param liveId the live_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getLivePlayback(String liveId) throws IOException, InterruptedException {
        return getLivePlayback(liveId, Options.defaults());
    }

    /**
     * Read retained reference and selected translations.
     * @param liveId the live_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getLiveSettings(String liveId, Options options) throws IOException, InterruptedException {
        return call("getLiveSettings", Map.of("live_id", liveId), options);
    }
    /**
     * Read retained reference and selected translations. Uses default request options.
     * @param liveId the live_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getLiveSettings(String liveId) throws IOException, InterruptedException {
        return getLiveSettings(liveId, Options.defaults());
    }

    /**
     * Read retained scripture stack.
     * @param liveId the live_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getLiveStack(String liveId, Options options) throws IOException, InterruptedException {
        return call("getLiveStack", Map.of("live_id", liveId), options);
    }
    /**
     * Read retained scripture stack. Uses default request options.
     * @param liveId the live_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getLiveStack(String liveId) throws IOException, InterruptedException {
        return getLiveStack(liveId, Options.defaults());
    }

    /**
     * Read Live connection and retained-stack statistics.
     * @param liveId the live_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getLiveStatistics(String liveId, Options options) throws IOException, InterruptedException {
        return call("getLiveStatistics", Map.of("live_id", liveId), options);
    }
    /**
     * Read Live connection and retained-stack statistics. Uses default request options.
     * @param liveId the live_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getLiveStatistics(String liveId) throws IOException, InterruptedException {
        return getLiveStatistics(liveId, Options.defaults());
    }

    /**
     * Remove a positive count from the retained stack tail.
     * @param liveId the live_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response popLiveStack(String liveId, Options options) throws IOException, InterruptedException {
        return call("popLiveStack", Map.of("live_id", liveId), options);
    }
    /**
     * Remove a positive count from the retained stack tail. Uses default request options.
     * @param liveId the live_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response popLiveStack(String liveId) throws IOException, InterruptedException {
        return popLiveStack(liveId, Options.defaults());
    }

    /**
     * Push retained scripture with optional selected translations.
     * @param liveId the live_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response pushLiveStack(String liveId, Options options) throws IOException, InterruptedException {
        return call("pushLiveStack", Map.of("live_id", liveId), options);
    }
    /**
     * Push retained scripture with optional selected translations. Uses default request options.
     * @param liveId the live_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response pushLiveStack(String liveId) throws IOException, InterruptedException {
        return pushLiveStack(liveId, Options.defaults());
    }

    /**
     * Replace or clear the Live display.
     * @param liveId the live_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response replaceLiveDisplay(String liveId, Options options) throws IOException, InterruptedException {
        return call("replaceLiveDisplay", Map.of("live_id", liveId), options);
    }
    /**
     * Replace or clear the Live display. Uses default request options.
     * @param liveId the live_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response replaceLiveDisplay(String liveId) throws IOException, InterruptedException {
        return replaceLiveDisplay(liveId, Options.defaults());
    }

    /**
     * Rotate a one-time audience secret.
     * @param liveId the live_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response rotateLiveAudienceSecret(String liveId, Options options) throws IOException, InterruptedException {
        return call("rotateLiveAudienceSecret", Map.of("live_id", liveId), options);
    }
    /**
     * Rotate a one-time audience secret. Uses default request options.
     * @param liveId the live_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response rotateLiveAudienceSecret(String liveId) throws IOException, InterruptedException {
        return rotateLiveAudienceSecret(liveId, Options.defaults());
    }

    /**
     * Update Live playback state.
     * @param liveId the live_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response updateLivePlayback(String liveId, Options options) throws IOException, InterruptedException {
        return call("updateLivePlayback", Map.of("live_id", liveId), options);
    }
    /**
     * Update Live playback state. Uses default request options.
     * @param liveId the live_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response updateLivePlayback(String liveId) throws IOException, InterruptedException {
        return updateLivePlayback(liveId, Options.defaults());
    }

    /**
     * Update retained reference and translations.
     * @param liveId the live_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response updateLiveSettings(String liveId, Options options) throws IOException, InterruptedException {
        return call("updateLiveSettings", Map.of("live_id", liveId), options);
    }
    /**
     * Update retained reference and translations. Uses default request options.
     * @param liveId the live_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response updateLiveSettings(String liveId) throws IOException, InterruptedException {
        return updateLiveSettings(liveId, Options.defaults());
    }

    /**
     * Receive versioned Live events.
     * @param liveId the live_id path identifier

     * @param query the operation-specific query values
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getLiveEvents(String liveId, Queries.GetLiveEventsQuery query, Options options) throws IOException, InterruptedException {
        return call("getLiveEvents", Map.of("live_id", liveId), query, options);
    }
    /**
     * Receive versioned Live events. Leaves query values unset for server defaults.
     * @param liveId the live_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getLiveEvents(String liveId, Options options) throws IOException, InterruptedException {
        return getLiveEvents(liveId, Queries.GetLiveEventsQuery.builder().build(), options);
    }

    /**
     * Receive versioned Live events. Uses default HTTP request options.
     * @param liveId the live_id path identifier
     * @param query the operation-specific query values
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getLiveEvents(String liveId, Queries.GetLiveEventsQuery query) throws IOException, InterruptedException {
        return getLiveEvents(liveId, query, Options.defaults());
    }
    /**
     * Receive versioned Live events. Uses default request options.
     * @param liveId the live_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getLiveEvents(String liveId) throws IOException, InterruptedException {
        return getLiveEvents(liveId, Options.defaults());
    }

    /**
     * Create an organization Live.
     * @param organizationId the organization_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response createLive(String organizationId, Options options) throws IOException, InterruptedException {
        return call("createLive", Map.of("organization_id", organizationId), options);
    }
    /**
     * Create an organization Live. Uses default request options.
     * @param organizationId the organization_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response createLive(String organizationId) throws IOException, InterruptedException {
        return createLive(organizationId, Options.defaults());
    }

    /**
     * Insert a scripture queue item.
     * @param liveId the live_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response createLiveQueueItem(String liveId, Options options) throws IOException, InterruptedException {
        return call("createLiveQueueItem", Map.of("live_id", liveId), options);
    }
    /**
     * Insert a scripture queue item. Uses default request options.
     * @param liveId the live_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response createLiveQueueItem(String liveId) throws IOException, InterruptedException {
        return createLiveQueueItem(liveId, Options.defaults());
    }

    /**
     * Delete a Live.
     * @param liveId the live_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response deleteLive(String liveId, Options options) throws IOException, InterruptedException {
        return call("deleteLive", Map.of("live_id", liveId), options);
    }
    /**
     * Delete a Live. Uses default request options.
     * @param liveId the live_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response deleteLive(String liveId) throws IOException, InterruptedException {
        return deleteLive(liveId, Options.defaults());
    }

    /**
     * Remove a queue item.
     * @param liveId the live_id path identifier
     * @param itemId the item_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response deleteLiveQueueItem(String liveId, String itemId, Options options) throws IOException, InterruptedException {
        return call("deleteLiveQueueItem", Map.of("live_id", liveId, "item_id", itemId), options);
    }
    /**
     * Remove a queue item. Uses default request options.
     * @param liveId the live_id path identifier
     * @param itemId the item_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response deleteLiveQueueItem(String liveId, String itemId) throws IOException, InterruptedException {
        return deleteLiveQueueItem(liveId, itemId, Options.defaults());
    }

    /**
     * Read Live state.
     * @param liveId the live_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getLive(String liveId, Options options) throws IOException, InterruptedException {
        return call("getLive", Map.of("live_id", liveId), options);
    }
    /**
     * Read Live state. Uses default request options.
     * @param liveId the live_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getLive(String liveId) throws IOException, InterruptedException {
        return getLive(liveId, Options.defaults());
    }

    /**
     * Read a stable queue item.
     * @param liveId the live_id path identifier
     * @param itemId the item_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getLiveQueueItem(String liveId, String itemId, Options options) throws IOException, InterruptedException {
        return call("getLiveQueueItem", Map.of("live_id", liveId, "item_id", itemId), options);
    }
    /**
     * Read a stable queue item. Uses default request options.
     * @param liveId the live_id path identifier
     * @param itemId the item_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getLiveQueueItem(String liveId, String itemId) throws IOException, InterruptedException {
        return getLiveQueueItem(liveId, itemId, Options.defaults());
    }

    /**
     * List ordered Live queue items.
     * @param liveId the live_id path identifier

     * @param query the operation-specific query values
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listLiveQueueItems(String liveId, Queries.ListLiveQueueItemsQuery query, Options options) throws IOException, InterruptedException {
        return call("listLiveQueueItems", Map.of("live_id", liveId), query, options);
    }
    /**
     * List ordered Live queue items. Leaves query values unset for server defaults.
     * @param liveId the live_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listLiveQueueItems(String liveId, Options options) throws IOException, InterruptedException {
        return listLiveQueueItems(liveId, Queries.ListLiveQueueItemsQuery.builder().build(), options);
    }

    /**
     * List ordered Live queue items. Uses default HTTP request options.
     * @param liveId the live_id path identifier
     * @param query the operation-specific query values
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listLiveQueueItems(String liveId, Queries.ListLiveQueueItemsQuery query) throws IOException, InterruptedException {
        return listLiveQueueItems(liveId, query, Options.defaults());
    }
    /**
     * List ordered Live queue items. Uses default request options.
     * @param liveId the live_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listLiveQueueItems(String liveId) throws IOException, InterruptedException {
        return listLiveQueueItems(liveId, Options.defaults());
    }

    /**
     * List organization Lives.
     * @param organizationId the organization_id path identifier

     * @param query the operation-specific query values
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listLives(String organizationId, Queries.ListLivesQuery query, Options options) throws IOException, InterruptedException {
        return call("listLives", Map.of("organization_id", organizationId), query, options);
    }
    /**
     * List organization Lives. Leaves query values unset for server defaults.
     * @param organizationId the organization_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listLives(String organizationId, Options options) throws IOException, InterruptedException {
        return listLives(organizationId, Queries.ListLivesQuery.builder().build(), options);
    }

    /**
     * List organization Lives. Uses default HTTP request options.
     * @param organizationId the organization_id path identifier
     * @param query the operation-specific query values
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listLives(String organizationId, Queries.ListLivesQuery query) throws IOException, InterruptedException {
        return listLives(organizationId, query, Options.defaults());
    }
    /**
     * List organization Lives. Uses default request options.
     * @param organizationId the organization_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listLives(String organizationId) throws IOException, InterruptedException {
        return listLives(organizationId, Options.defaults());
    }

    /**
     * Move a Live to another organization.
     * @param liveId the live_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response moveLiveWorkspace(String liveId, Options options) throws IOException, InterruptedException {
        return call("moveLiveWorkspace", Map.of("live_id", liveId), options);
    }
    /**
     * Move a Live to another organization. Uses default request options.
     * @param liveId the live_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response moveLiveWorkspace(String liveId) throws IOException, InterruptedException {
        return moveLiveWorkspace(liveId, Options.defaults());
    }

    /**
     * Replace queue ordering.
     * @param liveId the live_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response replaceLiveQueueOrder(String liveId, Options options) throws IOException, InterruptedException {
        return call("replaceLiveQueueOrder", Map.of("live_id", liveId), options);
    }
    /**
     * Replace queue ordering. Uses default request options.
     * @param liveId the live_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response replaceLiveQueueOrder(String liveId) throws IOException, InterruptedException {
        return replaceLiveQueueOrder(liveId, Options.defaults());
    }

    /**
     * Edit Live metadata.
     * @param liveId the live_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response updateLiveMetadata(String liveId, Options options) throws IOException, InterruptedException {
        return call("updateLiveMetadata", Map.of("live_id", liveId), options);
    }
    /**
     * Edit Live metadata. Uses default request options.
     * @param liveId the live_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response updateLiveMetadata(String liveId) throws IOException, InterruptedException {
        return updateLiveMetadata(liveId, Options.defaults());
    }

    /**
     * Start registered-client authorization.

     * @param query the operation-specific query values
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response authorizeOAuth(Queries.AuthorizeOAuthQuery query, Options options) throws IOException, InterruptedException {
        return call("authorizeOAuth", Map.of(), query, options);
    }
    /**
     * Start registered-client authorization. Leaves query values unset for server defaults.
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response authorizeOAuth(Options options) throws IOException, InterruptedException {
        return authorizeOAuth(Queries.AuthorizeOAuthQuery.builder().build(), options);
    }

    /**
     * Start registered-client authorization. Uses default HTTP request options.
     * @param query the operation-specific query values
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response authorizeOAuth(Queries.AuthorizeOAuthQuery query) throws IOException, InterruptedException {
        return authorizeOAuth(query, Options.defaults());
    }
    /**
     * Start registered-client authorization. Uses default request options.
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response authorizeOAuth() throws IOException, InterruptedException {
        return authorizeOAuth(Options.defaults());
    }

    /**
     * Approve or deny OAuth consent.
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response decideOAuthConsent(Options options) throws IOException, InterruptedException {
        return call("decideOAuthConsent", Map.of(), options);
    }
    /**
     * Approve or deny OAuth consent. Uses default request options.
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response decideOAuthConsent() throws IOException, InterruptedException {
        return decideOAuthConsent(Options.defaults());
    }

    /**
     * Disconnect an application.
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response disconnectOAuthApp(Options options) throws IOException, InterruptedException {
        return call("disconnectOAuthApp", Map.of(), options);
    }
    /**
     * Disconnect an application. Uses default request options.
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response disconnectOAuthApp() throws IOException, InterruptedException {
        return disconnectOAuthApp(Options.defaults());
    }

    /**
     * Exchange a code or rotate a refresh token.
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response exchangeOAuthToken(Options options) throws IOException, InterruptedException {
        return call("exchangeOAuthToken", Map.of(), options);
    }
    /**
     * Exchange a code or rotate a refresh token. Uses default request options.
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response exchangeOAuthToken() throws IOException, InterruptedException {
        return exchangeOAuthToken(Options.defaults());
    }

    /**
     * View connected applications.
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getConnectedAppsPage(Options options) throws IOException, InterruptedException {
        return call("getConnectedAppsPage", Map.of(), options);
    }
    /**
     * View connected applications. Uses default request options.
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getConnectedAppsPage() throws IOException, InterruptedException {
        return getConnectedAppsPage(Options.defaults());
    }

    /**
     * Read OAuth authorization-server metadata.
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getOAuthMetadata(Options options) throws IOException, InterruptedException {
        return call("getOAuthMetadata", Map.of(), options);
    }
    /**
     * Read OAuth authorization-server metadata. Uses default request options.
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getOAuthMetadata() throws IOException, InterruptedException {
        return getOAuthMetadata(Options.defaults());
    }

    /**
     * Revoke delegated access and refresh tokens.
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response revokeOAuthToken(Options options) throws IOException, InterruptedException {
        return call("revokeOAuthToken", Map.of(), options);
    }
    /**
     * Revoke delegated access and refresh tokens. Uses default request options.
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response revokeOAuthToken() throws IOException, InterruptedException {
        return revokeOAuthToken(Options.defaults());
    }

    /**
     * Atomically delete all Lives in this workspace.
     * @param organizationId the organization_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response deleteOrganizationLives(String organizationId, Options options) throws IOException, InterruptedException {
        return call("deleteOrganizationLives", Map.of("organization_id", organizationId), options);
    }
    /**
     * Atomically delete all Lives in this workspace. Uses default request options.
     * @param organizationId the organization_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response deleteOrganizationLives(String organizationId) throws IOException, InterruptedException {
        return deleteOrganizationLives(organizationId, Options.defaults());
    }

    /**
     * Remove a member.
     * @param organizationId the organization_id path identifier
     * @param memberId the member_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response deleteOrganizationMember(String organizationId, String memberId, Options options) throws IOException, InterruptedException {
        return call("deleteOrganizationMember", Map.of("organization_id", organizationId, "member_id", memberId), options);
    }
    /**
     * Remove a member. Uses default request options.
     * @param organizationId the organization_id path identifier
     * @param memberId the member_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response deleteOrganizationMember(String organizationId, String memberId) throws IOException, InterruptedException {
        return deleteOrganizationMember(organizationId, memberId, Options.defaults());
    }

    /**
     * Read organization identity.
     * @param organizationId the organization_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getOrganization(String organizationId, Options options) throws IOException, InterruptedException {
        return call("getOrganization", Map.of("organization_id", organizationId), options);
    }
    /**
     * Read organization identity. Uses default request options.
     * @param organizationId the organization_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getOrganization(String organizationId) throws IOException, InterruptedException {
        return getOrganization(organizationId, Options.defaults());
    }

    /**
     * Read organization Live Studio settings.
     * @param organizationId the organization_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getOrganizationLiveStudio(String organizationId, Options options) throws IOException, InterruptedException {
        return call("getOrganizationLiveStudio", Map.of("organization_id", organizationId), options);
    }
    /**
     * Read organization Live Studio settings. Uses default request options.
     * @param organizationId the organization_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getOrganizationLiveStudio(String organizationId) throws IOException, InterruptedException {
        return getOrganizationLiveStudio(organizationId, Options.defaults());
    }

    /**
     * Read an active membership.
     * @param organizationId the organization_id path identifier
     * @param memberId the member_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getOrganizationMember(String organizationId, String memberId, Options options) throws IOException, InterruptedException {
        return call("getOrganizationMember", Map.of("organization_id", organizationId, "member_id", memberId), options);
    }
    /**
     * Read an active membership. Uses default request options.
     * @param organizationId the organization_id path identifier
     * @param memberId the member_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getOrganizationMember(String organizationId, String memberId) throws IOException, InterruptedException {
        return getOrganizationMember(organizationId, memberId, Options.defaults());
    }

    /**
     * Read organization limits and usage.
     * @param organizationId the organization_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getOrganizationQuotas(String organizationId, Options options) throws IOException, InterruptedException {
        return call("getOrganizationQuotas", Map.of("organization_id", organizationId), options);
    }
    /**
     * Read organization limits and usage. Uses default request options.
     * @param organizationId the organization_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getOrganizationQuotas(String organizationId) throws IOException, InterruptedException {
        return getOrganizationQuotas(organizationId, Options.defaults());
    }

    /**
     * Read your organization application status.
     * @param applicationId the application_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getPersonalOrganizationApplication(String applicationId, Options options) throws IOException, InterruptedException {
        return call("getPersonalOrganizationApplication", Map.of("application_id", applicationId), options);
    }
    /**
     * Read your organization application status. Uses default request options.
     * @param applicationId the application_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getPersonalOrganizationApplication(String applicationId) throws IOException, InterruptedException {
        return getPersonalOrganizationApplication(applicationId, Options.defaults());
    }

    /**
     * Discover the service token organization.
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getServiceOrganization(Options options) throws IOException, InterruptedException {
        return call("getServiceOrganization", Map.of(), options);
    }
    /**
     * Discover the service token organization. Uses default request options.
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getServiceOrganization() throws IOException, InterruptedException {
        return getServiceOrganization(Options.defaults());
    }

    /**
     * List active organization memberships.
     * @param organizationId the organization_id path identifier

     * @param query the operation-specific query values
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listOrganizationMembers(String organizationId, Queries.ListOrganizationMembersQuery query, Options options) throws IOException, InterruptedException {
        return call("listOrganizationMembers", Map.of("organization_id", organizationId), query, options);
    }
    /**
     * List active organization memberships. Leaves query values unset for server defaults.
     * @param organizationId the organization_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listOrganizationMembers(String organizationId, Options options) throws IOException, InterruptedException {
        return listOrganizationMembers(organizationId, Queries.ListOrganizationMembersQuery.builder().build(), options);
    }

    /**
     * List active organization memberships. Uses default HTTP request options.
     * @param organizationId the organization_id path identifier
     * @param query the operation-specific query values
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listOrganizationMembers(String organizationId, Queries.ListOrganizationMembersQuery query) throws IOException, InterruptedException {
        return listOrganizationMembers(organizationId, query, Options.defaults());
    }
    /**
     * List active organization memberships. Uses default request options.
     * @param organizationId the organization_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listOrganizationMembers(String organizationId) throws IOException, InterruptedException {
        return listOrganizationMembers(organizationId, Options.defaults());
    }

    /**
     * List your memberships and applications.

     * @param query the operation-specific query values
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listPersonalOrganizations(Queries.ListPersonalOrganizationsQuery query, Options options) throws IOException, InterruptedException {
        return call("listPersonalOrganizations", Map.of(), query, options);
    }
    /**
     * List your memberships and applications. Leaves query values unset for server defaults.
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listPersonalOrganizations(Options options) throws IOException, InterruptedException {
        return listPersonalOrganizations(Queries.ListPersonalOrganizationsQuery.builder().build(), options);
    }

    /**
     * List your memberships and applications. Uses default HTTP request options.
     * @param query the operation-specific query values
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listPersonalOrganizations(Queries.ListPersonalOrganizationsQuery query) throws IOException, InterruptedException {
        return listPersonalOrganizations(query, Options.defaults());
    }
    /**
     * List your memberships and applications. Uses default request options.
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listPersonalOrganizations() throws IOException, InterruptedException {
        return listPersonalOrganizations(Options.defaults());
    }

    /**
     * Read a book page with source verse identifiers.
     * @param organizationId the organization_id path identifier
     * @param slug the slug path identifier
     * @param bookId the book_id path identifier

     * @param query the operation-specific query values
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response readBook(String organizationId, String slug, String bookId, Queries.ReadBookQuery query, Options options) throws IOException, InterruptedException {
        return call("readBook", Map.of("organization_id", organizationId, "slug", slug, "book_id", bookId), query, options);
    }
    /**
     * Read a book page with source verse identifiers. Leaves query values unset for server defaults.
     * @param organizationId the organization_id path identifier
     * @param slug the slug path identifier
     * @param bookId the book_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response readBook(String organizationId, String slug, String bookId, Options options) throws IOException, InterruptedException {
        return readBook(organizationId, slug, bookId, Queries.ReadBookQuery.builder().build(), options);
    }

    /**
     * Read a book page with source verse identifiers. Uses default HTTP request options.
     * @param organizationId the organization_id path identifier
     * @param slug the slug path identifier
     * @param bookId the book_id path identifier
     * @param query the operation-specific query values
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response readBook(String organizationId, String slug, String bookId, Queries.ReadBookQuery query) throws IOException, InterruptedException {
        return readBook(organizationId, slug, bookId, query, Options.defaults());
    }
    /**
     * Read a book page with source verse identifiers. Uses default request options.
     * @param organizationId the organization_id path identifier
     * @param slug the slug path identifier
     * @param bookId the book_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response readBook(String organizationId, String slug, String bookId) throws IOException, InterruptedException {
        return readBook(organizationId, slug, bookId, Options.defaults());
    }

    /**
     * Replace organization Live Studio settings.
     * @param organizationId the organization_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response replaceOrganizationLiveStudio(String organizationId, Options options) throws IOException, InterruptedException {
        return call("replaceOrganizationLiveStudio", Map.of("organization_id", organizationId), options);
    }
    /**
     * Replace organization Live Studio settings. Uses default request options.
     * @param organizationId the organization_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response replaceOrganizationLiveStudio(String organizationId) throws IOException, InterruptedException {
        return replaceOrganizationLiveStudio(organizationId, Options.defaults());
    }

    /**
     * Change a member role.
     * @param organizationId the organization_id path identifier
     * @param memberId the member_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response updateOrganizationMember(String organizationId, String memberId, Options options) throws IOException, InterruptedException {
        return call("updateOrganizationMember", Map.of("organization_id", organizationId, "member_id", memberId), options);
    }
    /**
     * Change a member role. Uses default request options.
     * @param organizationId the organization_id path identifier
     * @param memberId the member_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response updateOrganizationMember(String organizationId, String memberId) throws IOException, InterruptedException {
        return updateOrganizationMember(organizationId, memberId, Options.defaults());
    }

    /**
     * Create a personal Reader Live.
     * @param accountId the account_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response createPersonalLive(String accountId, Options options) throws IOException, InterruptedException {
        return call("createPersonalLive", Map.of("account_id", accountId), options);
    }
    /**
     * Create a personal Reader Live. Uses default request options.
     * @param accountId the account_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response createPersonalLive(String accountId) throws IOException, InterruptedException {
        return createPersonalLive(accountId, Options.defaults());
    }

    /**
     * Atomically delete all Lives in this workspace.
     * @param accountId the account_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response deletePersonalLives(String accountId, Options options) throws IOException, InterruptedException {
        return call("deletePersonalLives", Map.of("account_id", accountId), options);
    }
    /**
     * Atomically delete all Lives in this workspace. Uses default request options.
     * @param accountId the account_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response deletePersonalLives(String accountId) throws IOException, InterruptedException {
        return deletePersonalLives(accountId, Options.defaults());
    }

    /**
     * Disable a personal translation.
     * @param accountId the account_id path identifier
     * @param slug the slug path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response disablePersonalTranslation(String accountId, String slug, Options options) throws IOException, InterruptedException {
        return call("disablePersonalTranslation", Map.of("account_id", accountId, "slug", slug), options);
    }
    /**
     * Disable a personal translation. Uses default request options.
     * @param accountId the account_id path identifier
     * @param slug the slug path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response disablePersonalTranslation(String accountId, String slug) throws IOException, InterruptedException {
        return disablePersonalTranslation(accountId, slug, Options.defaults());
    }

    /**
     * Enable an installed personal translation.
     * @param accountId the account_id path identifier
     * @param slug the slug path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response enablePersonalTranslation(String accountId, String slug, Options options) throws IOException, InterruptedException {
        return call("enablePersonalTranslation", Map.of("account_id", accountId, "slug", slug), options);
    }
    /**
     * Enable an installed personal translation. Uses default request options.
     * @param accountId the account_id path identifier
     * @param slug the slug path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response enablePersonalTranslation(String accountId, String slug) throws IOException, InterruptedException {
        return enablePersonalTranslation(accountId, slug, Options.defaults());
    }

    /**
     * Read your personal account.
     * @param accountId the account_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getPersonalAccount(String accountId, Options options) throws IOException, InterruptedException {
        return call("getPersonalAccount", Map.of("account_id", accountId), options);
    }
    /**
     * Read your personal account. Uses default request options.
     * @param accountId the account_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getPersonalAccount(String accountId) throws IOException, InterruptedException {
        return getPersonalAccount(accountId, Options.defaults());
    }

    /**
     * Read your personal identity.
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getPersonalIdentity(Options options) throws IOException, InterruptedException {
        return call("getPersonalIdentity", Map.of(), options);
    }
    /**
     * Read your personal identity. Uses default request options.
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getPersonalIdentity() throws IOException, InterruptedException {
        return getPersonalIdentity(Options.defaults());
    }

    /**
     * Read your effective personal quotas and current usage.
     * @param accountId the account_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getPersonalQuotas(String accountId, Options options) throws IOException, InterruptedException {
        return call("getPersonalQuotas", Map.of("account_id", accountId), options);
    }
    /**
     * Read your effective personal quotas and current usage. Uses default request options.
     * @param accountId the account_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getPersonalQuotas(String accountId) throws IOException, InterruptedException {
        return getPersonalQuotas(accountId, Options.defaults());
    }

    /**
     * Read an personal account library membership.
     * @param accountId the account_id path identifier
     * @param slug the slug path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getPersonalTranslation(String accountId, String slug, Options options) throws IOException, InterruptedException {
        return call("getPersonalTranslation", Map.of("account_id", accountId, "slug", slug), options);
    }
    /**
     * Read an personal account library membership. Uses default request options.
     * @param accountId the account_id path identifier
     * @param slug the slug path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getPersonalTranslation(String accountId, String slug) throws IOException, InterruptedException {
        return getPersonalTranslation(accountId, slug, Options.defaults());
    }

    /**
     * List your connected OAuth applications.
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listOAuthGrants(Options options) throws IOException, InterruptedException {
        return call("listOAuthGrants", Map.of(), options);
    }
    /**
     * List your connected OAuth applications. Uses default request options.
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listOAuthGrants() throws IOException, InterruptedException {
        return listOAuthGrants(Options.defaults());
    }

    /**
     * List your personal account.

     * @param query the operation-specific query values
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listPersonalAccounts(Queries.ListPersonalAccountsQuery query, Options options) throws IOException, InterruptedException {
        return call("listPersonalAccounts", Map.of(), query, options);
    }
    /**
     * List your personal account. Leaves query values unset for server defaults.
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listPersonalAccounts(Options options) throws IOException, InterruptedException {
        return listPersonalAccounts(Queries.ListPersonalAccountsQuery.builder().build(), options);
    }

    /**
     * List your personal account. Uses default HTTP request options.
     * @param query the operation-specific query values
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listPersonalAccounts(Queries.ListPersonalAccountsQuery query) throws IOException, InterruptedException {
        return listPersonalAccounts(query, Options.defaults());
    }
    /**
     * List your personal account. Uses default request options.
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listPersonalAccounts() throws IOException, InterruptedException {
        return listPersonalAccounts(Options.defaults());
    }

    /**
     * List your personal Lives.
     * @param accountId the account_id path identifier

     * @param query the operation-specific query values
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listPersonalLives(String accountId, Queries.ListPersonalLivesQuery query, Options options) throws IOException, InterruptedException {
        return call("listPersonalLives", Map.of("account_id", accountId), query, options);
    }
    /**
     * List your personal Lives. Leaves query values unset for server defaults.
     * @param accountId the account_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listPersonalLives(String accountId, Options options) throws IOException, InterruptedException {
        return listPersonalLives(accountId, Queries.ListPersonalLivesQuery.builder().build(), options);
    }

    /**
     * List your personal Lives. Uses default HTTP request options.
     * @param accountId the account_id path identifier
     * @param query the operation-specific query values
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listPersonalLives(String accountId, Queries.ListPersonalLivesQuery query) throws IOException, InterruptedException {
        return listPersonalLives(accountId, query, Options.defaults());
    }
    /**
     * List your personal Lives. Uses default request options.
     * @param accountId the account_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listPersonalLives(String accountId) throws IOException, InterruptedException {
        return listPersonalLives(accountId, Options.defaults());
    }

    /**
     * List personal credential metadata.
     * @param accountId the account_id path identifier

     * @param query the operation-specific query values
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listPersonalTokens(String accountId, Queries.ListPersonalTokensQuery query, Options options) throws IOException, InterruptedException {
        return call("listPersonalTokens", Map.of("account_id", accountId), query, options);
    }
    /**
     * List personal credential metadata. Leaves query values unset for server defaults.
     * @param accountId the account_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listPersonalTokens(String accountId, Options options) throws IOException, InterruptedException {
        return listPersonalTokens(accountId, Queries.ListPersonalTokensQuery.builder().build(), options);
    }

    /**
     * List personal credential metadata. Uses default HTTP request options.
     * @param accountId the account_id path identifier
     * @param query the operation-specific query values
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listPersonalTokens(String accountId, Queries.ListPersonalTokensQuery query) throws IOException, InterruptedException {
        return listPersonalTokens(accountId, query, Options.defaults());
    }
    /**
     * List personal credential metadata. Uses default request options.
     * @param accountId the account_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listPersonalTokens(String accountId) throws IOException, InterruptedException {
        return listPersonalTokens(accountId, Options.defaults());
    }

    /**
     * List the personal account translation library.
     * @param accountId the account_id path identifier

     * @param query the operation-specific query values
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listPersonalTranslations(String accountId, Queries.ListPersonalTranslationsQuery query, Options options) throws IOException, InterruptedException {
        return call("listPersonalTranslations", Map.of("account_id", accountId), query, options);
    }
    /**
     * List the personal account translation library. Leaves query values unset for server defaults.
     * @param accountId the account_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listPersonalTranslations(String accountId, Options options) throws IOException, InterruptedException {
        return listPersonalTranslations(accountId, Queries.ListPersonalTranslationsQuery.builder().build(), options);
    }

    /**
     * List the personal account translation library. Uses default HTTP request options.
     * @param accountId the account_id path identifier
     * @param query the operation-specific query values
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listPersonalTranslations(String accountId, Queries.ListPersonalTranslationsQuery query) throws IOException, InterruptedException {
        return listPersonalTranslations(accountId, query, Options.defaults());
    }
    /**
     * List the personal account translation library. Uses default request options.
     * @param accountId the account_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listPersonalTranslations(String accountId) throws IOException, InterruptedException {
        return listPersonalTranslations(accountId, Options.defaults());
    }

    /**
     * Read a book page with source verse identifiers.
     * @param accountId the account_id path identifier
     * @param slug the slug path identifier
     * @param bookId the book_id path identifier

     * @param query the operation-specific query values
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response readPersonalBook(String accountId, String slug, String bookId, Queries.ReadPersonalBookQuery query, Options options) throws IOException, InterruptedException {
        return call("readPersonalBook", Map.of("account_id", accountId, "slug", slug, "book_id", bookId), query, options);
    }
    /**
     * Read a book page with source verse identifiers. Leaves query values unset for server defaults.
     * @param accountId the account_id path identifier
     * @param slug the slug path identifier
     * @param bookId the book_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response readPersonalBook(String accountId, String slug, String bookId, Options options) throws IOException, InterruptedException {
        return readPersonalBook(accountId, slug, bookId, Queries.ReadPersonalBookQuery.builder().build(), options);
    }

    /**
     * Read a book page with source verse identifiers. Uses default HTTP request options.
     * @param accountId the account_id path identifier
     * @param slug the slug path identifier
     * @param bookId the book_id path identifier
     * @param query the operation-specific query values
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response readPersonalBook(String accountId, String slug, String bookId, Queries.ReadPersonalBookQuery query) throws IOException, InterruptedException {
        return readPersonalBook(accountId, slug, bookId, query, Options.defaults());
    }
    /**
     * Read a book page with source verse identifiers. Uses default request options.
     * @param accountId the account_id path identifier
     * @param slug the slug path identifier
     * @param bookId the book_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response readPersonalBook(String accountId, String slug, String bookId) throws IOException, InterruptedException {
        return readPersonalBook(accountId, slug, bookId, Options.defaults());
    }

    /**
     * Read a chapter.
     * @param accountId the account_id path identifier
     * @param slug the slug path identifier
     * @param bookId the book_id path identifier
     * @param chapter the chapter path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response readPersonalChapter(String accountId, String slug, String bookId, String chapter, Options options) throws IOException, InterruptedException {
        return call("readPersonalChapter", Map.of("account_id", accountId, "slug", slug, "book_id", bookId, "chapter", chapter), options);
    }
    /**
     * Read a chapter. Uses default request options.
     * @param accountId the account_id path identifier
     * @param slug the slug path identifier
     * @param bookId the book_id path identifier
     * @param chapter the chapter path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response readPersonalChapter(String accountId, String slug, String bookId, String chapter) throws IOException, InterruptedException {
        return readPersonalChapter(accountId, slug, bookId, chapter, Options.defaults());
    }

    /**
     * Read a verse.
     * @param accountId the account_id path identifier
     * @param slug the slug path identifier
     * @param bookId the book_id path identifier
     * @param chapter the chapter path identifier
     * @param verse the verse path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response readPersonalVerse(String accountId, String slug, String bookId, String chapter, String verse, Options options) throws IOException, InterruptedException {
        return call("readPersonalVerse", Map.of("account_id", accountId, "slug", slug, "book_id", bookId, "chapter", chapter, "verse", verse), options);
    }
    /**
     * Read a verse. Uses default request options.
     * @param accountId the account_id path identifier
     * @param slug the slug path identifier
     * @param bookId the book_id path identifier
     * @param chapter the chapter path identifier
     * @param verse the verse path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response readPersonalVerse(String accountId, String slug, String bookId, String chapter, String verse) throws IOException, InterruptedException {
        return readPersonalVerse(accountId, slug, bookId, chapter, verse, Options.defaults());
    }

    /**
     * Disconnect your OAuth grant.
     * @param grantId the grant_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response revokeOAuthGrant(String grantId, Options options) throws IOException, InterruptedException {
        return call("revokeOAuthGrant", Map.of("grant_id", grantId), options);
    }
    /**
     * Disconnect your OAuth grant. Uses default request options.
     * @param grantId the grant_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response revokeOAuthGrant(String grantId) throws IOException, InterruptedException {
        return revokeOAuthGrant(grantId, Options.defaults());
    }

    /**
     * Search personal account scripture.
     * @param accountId the account_id path identifier
     * @param slug the slug path identifier

     * @param query the operation-specific query values
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response searchPersonalScripture(String accountId, String slug, Queries.SearchPersonalScriptureQuery query, Options options) throws IOException, InterruptedException {
        return call("searchPersonalScripture", Map.of("account_id", accountId, "slug", slug), query, options);
    }
    /**
     * Search personal account scripture. Leaves query values unset for server defaults.
     * @param accountId the account_id path identifier
     * @param slug the slug path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response searchPersonalScripture(String accountId, String slug, Options options) throws IOException, InterruptedException {
        return searchPersonalScripture(accountId, slug, Queries.SearchPersonalScriptureQuery.builder().build(), options);
    }

    /**
     * Search personal account scripture. Uses default HTTP request options.
     * @param accountId the account_id path identifier
     * @param slug the slug path identifier
     * @param query the operation-specific query values
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response searchPersonalScripture(String accountId, String slug, Queries.SearchPersonalScriptureQuery query) throws IOException, InterruptedException {
        return searchPersonalScripture(accountId, slug, query, Options.defaults());
    }
    /**
     * Search personal account scripture. Uses default request options.
     * @param accountId the account_id path identifier
     * @param slug the slug path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response searchPersonalScripture(String accountId, String slug) throws IOException, InterruptedException {
        return searchPersonalScripture(accountId, slug, Options.defaults());
    }

    /**
     * Read protected presentation asset bytes.
     * @param presentationId the presentation_id path identifier
     * @param assetId the asset_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getPresentationAsset(String presentationId, String assetId, Options options) throws IOException, InterruptedException {
        return call("getPresentationAsset", Map.of("presentation_id", presentationId, "asset_id", assetId), options);
    }
    /**
     * Read protected presentation asset bytes. Uses default request options.
     * @param presentationId the presentation_id path identifier
     * @param assetId the asset_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getPresentationAsset(String presentationId, String assetId) throws IOException, InterruptedException {
        return getPresentationAsset(presentationId, assetId, Options.defaults());
    }

    /**
     * Upload a staged presentation asset.
     * @param presentationId the presentation_id path identifier

     * @param query the operation-specific query values
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response uploadPresentationAsset(String presentationId, Queries.UploadPresentationAssetQuery query, Options options) throws IOException, InterruptedException {
        return call("uploadPresentationAsset", Map.of("presentation_id", presentationId), query, options);
    }
    /**
     * Upload a staged presentation asset. Leaves query values unset for server defaults.
     * @param presentationId the presentation_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response uploadPresentationAsset(String presentationId, Options options) throws IOException, InterruptedException {
        return uploadPresentationAsset(presentationId, Queries.UploadPresentationAssetQuery.builder().build(), options);
    }

    /**
     * Upload a staged presentation asset. Uses default HTTP request options.
     * @param presentationId the presentation_id path identifier
     * @param query the operation-specific query values
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response uploadPresentationAsset(String presentationId, Queries.UploadPresentationAssetQuery query) throws IOException, InterruptedException {
        return uploadPresentationAsset(presentationId, query, Options.defaults());
    }
    /**
     * Upload a staged presentation asset. Uses default request options.
     * @param presentationId the presentation_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response uploadPresentationAsset(String presentationId) throws IOException, InterruptedException {
        return uploadPresentationAsset(presentationId, Options.defaults());
    }

    /**
     * Queue a saved-revision presentation export.
     * @param presentationId the presentation_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response createPresentationExport(String presentationId, Options options) throws IOException, InterruptedException {
        return call("createPresentationExport", Map.of("presentation_id", presentationId), options);
    }
    /**
     * Queue a saved-revision presentation export. Uses default request options.
     * @param presentationId the presentation_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response createPresentationExport(String presentationId) throws IOException, InterruptedException {
        return createPresentationExport(presentationId, Options.defaults());
    }

    /**
     * Consume and download a completed export once.
     * @param presentationId the presentation_id path identifier
     * @param exportId the export_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response downloadPresentationExport(String presentationId, String exportId, Options options) throws IOException, InterruptedException {
        return call("downloadPresentationExport", Map.of("presentation_id", presentationId, "export_id", exportId), options);
    }
    /**
     * Consume and download a completed export once. Uses default request options.
     * @param presentationId the presentation_id path identifier
     * @param exportId the export_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response downloadPresentationExport(String presentationId, String exportId) throws IOException, InterruptedException {
        return downloadPresentationExport(presentationId, exportId, Options.defaults());
    }

    /**
     * Read export job status.
     * @param presentationId the presentation_id path identifier
     * @param exportId the export_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getPresentationExport(String presentationId, String exportId, Options options) throws IOException, InterruptedException {
        return call("getPresentationExport", Map.of("presentation_id", presentationId, "export_id", exportId), options);
    }
    /**
     * Read export job status. Uses default request options.
     * @param presentationId the presentation_id path identifier
     * @param exportId the export_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getPresentationExport(String presentationId, String exportId) throws IOException, InterruptedException {
        return getPresentationExport(presentationId, exportId, Options.defaults());
    }

    /**
     * List the requesting service credential’s export jobs.
     * @param presentationId the presentation_id path identifier

     * @param query the operation-specific query values
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listPresentationExports(String presentationId, Queries.ListPresentationExportsQuery query, Options options) throws IOException, InterruptedException {
        return call("listPresentationExports", Map.of("presentation_id", presentationId), query, options);
    }
    /**
     * List the requesting service credential’s export jobs. Leaves query values unset for server defaults.
     * @param presentationId the presentation_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listPresentationExports(String presentationId, Options options) throws IOException, InterruptedException {
        return listPresentationExports(presentationId, Queries.ListPresentationExportsQuery.builder().build(), options);
    }

    /**
     * List the requesting service credential’s export jobs. Uses default HTTP request options.
     * @param presentationId the presentation_id path identifier
     * @param query the operation-specific query values
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listPresentationExports(String presentationId, Queries.ListPresentationExportsQuery query) throws IOException, InterruptedException {
        return listPresentationExports(presentationId, query, Options.defaults());
    }
    /**
     * List the requesting service credential’s export jobs. Uses default request options.
     * @param presentationId the presentation_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listPresentationExports(String presentationId) throws IOException, InterruptedException {
        return listPresentationExports(presentationId, Options.defaults());
    }

    /**
     * Create an organization presentation.
     * @param organizationId the organization_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response createPresentation(String organizationId, Options options) throws IOException, InterruptedException {
        return call("createPresentation", Map.of("organization_id", organizationId), options);
    }
    /**
     * Create an organization presentation. Uses default request options.
     * @param organizationId the organization_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response createPresentation(String organizationId) throws IOException, InterruptedException {
        return createPresentation(organizationId, Options.defaults());
    }

    /**
     * Delete a saved presentation.
     * @param presentationId the presentation_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response deletePresentation(String presentationId, Options options) throws IOException, InterruptedException {
        return call("deletePresentation", Map.of("presentation_id", presentationId), options);
    }
    /**
     * Delete a saved presentation. Uses default request options.
     * @param presentationId the presentation_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response deletePresentation(String presentationId) throws IOException, InterruptedException {
        return deletePresentation(presentationId, Options.defaults());
    }

    /**
     * Read a saved presentation.
     * @param presentationId the presentation_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getPresentation(String presentationId, Options options) throws IOException, InterruptedException {
        return call("getPresentation", Map.of("presentation_id", presentationId), options);
    }
    /**
     * Read a saved presentation. Uses default request options.
     * @param presentationId the presentation_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getPresentation(String presentationId) throws IOException, InterruptedException {
        return getPresentation(presentationId, Options.defaults());
    }

    /**
     * Import a portable presentation bundle.
     * @param organizationId the organization_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response importPresentation(String organizationId, Options options) throws IOException, InterruptedException {
        return call("importPresentation", Map.of("organization_id", organizationId), options);
    }
    /**
     * Import a portable presentation bundle. Uses default request options.
     * @param organizationId the organization_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response importPresentation(String organizationId) throws IOException, InterruptedException {
        return importPresentation(organizationId, Options.defaults());
    }

    /**
     * List organization presentations.
     * @param organizationId the organization_id path identifier

     * @param query the operation-specific query values
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listPresentations(String organizationId, Queries.ListPresentationsQuery query, Options options) throws IOException, InterruptedException {
        return call("listPresentations", Map.of("organization_id", organizationId), query, options);
    }
    /**
     * List organization presentations. Leaves query values unset for server defaults.
     * @param organizationId the organization_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listPresentations(String organizationId, Options options) throws IOException, InterruptedException {
        return listPresentations(organizationId, Queries.ListPresentationsQuery.builder().build(), options);
    }

    /**
     * List organization presentations. Uses default HTTP request options.
     * @param organizationId the organization_id path identifier
     * @param query the operation-specific query values
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listPresentations(String organizationId, Queries.ListPresentationsQuery query) throws IOException, InterruptedException {
        return listPresentations(organizationId, query, Options.defaults());
    }
    /**
     * List organization presentations. Uses default request options.
     * @param organizationId the organization_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listPresentations(String organizationId) throws IOException, InterruptedException {
        return listPresentations(organizationId, Options.defaults());
    }

    /**
     * Replace a saved presentation document.
     * @param presentationId the presentation_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response replacePresentation(String presentationId, Options options) throws IOException, InterruptedException {
        return call("replacePresentation", Map.of("presentation_id", presentationId), options);
    }
    /**
     * Replace a saved presentation document. Uses default request options.
     * @param presentationId the presentation_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response replacePresentation(String presentationId) throws IOException, InterruptedException {
        return replacePresentation(presentationId, Options.defaults());
    }

    /**
     * Read a chapter.
     * @param organizationId the organization_id path identifier
     * @param slug the slug path identifier
     * @param bookId the book_id path identifier
     * @param chapter the chapter path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response readChapter(String organizationId, String slug, String bookId, String chapter, Options options) throws IOException, InterruptedException {
        return call("readChapter", Map.of("organization_id", organizationId, "slug", slug, "book_id", bookId, "chapter", chapter), options);
    }
    /**
     * Read a chapter. Uses default request options.
     * @param organizationId the organization_id path identifier
     * @param slug the slug path identifier
     * @param bookId the book_id path identifier
     * @param chapter the chapter path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response readChapter(String organizationId, String slug, String bookId, String chapter) throws IOException, InterruptedException {
        return readChapter(organizationId, slug, bookId, chapter, Options.defaults());
    }

    /**
     * Read a verse.
     * @param organizationId the organization_id path identifier
     * @param slug the slug path identifier
     * @param bookId the book_id path identifier
     * @param chapter the chapter path identifier
     * @param verse the verse path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response readVerse(String organizationId, String slug, String bookId, String chapter, String verse, Options options) throws IOException, InterruptedException {
        return call("readVerse", Map.of("organization_id", organizationId, "slug", slug, "book_id", bookId, "chapter", chapter, "verse", verse), options);
    }
    /**
     * Read a verse. Uses default request options.
     * @param organizationId the organization_id path identifier
     * @param slug the slug path identifier
     * @param bookId the book_id path identifier
     * @param chapter the chapter path identifier
     * @param verse the verse path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response readVerse(String organizationId, String slug, String bookId, String chapter, String verse) throws IOException, InterruptedException {
        return readVerse(organizationId, slug, bookId, chapter, verse, Options.defaults());
    }

    /**
     * Search organization scripture.
     * @param organizationId the organization_id path identifier
     * @param slug the slug path identifier

     * @param query the operation-specific query values
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response searchScripture(String organizationId, String slug, Queries.SearchScriptureQuery query, Options options) throws IOException, InterruptedException {
        return call("searchScripture", Map.of("organization_id", organizationId, "slug", slug), query, options);
    }
    /**
     * Search organization scripture. Leaves query values unset for server defaults.
     * @param organizationId the organization_id path identifier
     * @param slug the slug path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response searchScripture(String organizationId, String slug, Options options) throws IOException, InterruptedException {
        return searchScripture(organizationId, slug, Queries.SearchScriptureQuery.builder().build(), options);
    }

    /**
     * Search organization scripture. Uses default HTTP request options.
     * @param organizationId the organization_id path identifier
     * @param slug the slug path identifier
     * @param query the operation-specific query values
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response searchScripture(String organizationId, String slug, Queries.SearchScriptureQuery query) throws IOException, InterruptedException {
        return searchScripture(organizationId, slug, query, Options.defaults());
    }
    /**
     * Search organization scripture. Uses default request options.
     * @param organizationId the organization_id path identifier
     * @param slug the slug path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response searchScripture(String organizationId, String slug) throws IOException, InterruptedException {
        return searchScripture(organizationId, slug, Options.defaults());
    }

    /**
     * Create a service token.
     * @param organizationId the organization_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response createServiceToken(String organizationId, Options options) throws IOException, InterruptedException {
        return call("createServiceToken", Map.of("organization_id", organizationId), options);
    }
    /**
     * Create a service token. Uses default request options.
     * @param organizationId the organization_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response createServiceToken(String organizationId) throws IOException, InterruptedException {
        return createServiceToken(organizationId, Options.defaults());
    }

    /**
     * Read service-token configuration.
     * @param organizationId the organization_id path identifier
     * @param credentialId the credential_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getServiceToken(String organizationId, String credentialId, Options options) throws IOException, InterruptedException {
        return call("getServiceToken", Map.of("organization_id", organizationId, "credential_id", credentialId), options);
    }
    /**
     * Read service-token configuration. Uses default request options.
     * @param organizationId the organization_id path identifier
     * @param credentialId the credential_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getServiceToken(String organizationId, String credentialId) throws IOException, InterruptedException {
        return getServiceToken(organizationId, credentialId, Options.defaults());
    }

    /**
     * List service tokens.
     * @param organizationId the organization_id path identifier

     * @param query the operation-specific query values
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listServiceTokens(String organizationId, Queries.ListServiceTokensQuery query, Options options) throws IOException, InterruptedException {
        return call("listServiceTokens", Map.of("organization_id", organizationId), query, options);
    }
    /**
     * List service tokens. Leaves query values unset for server defaults.
     * @param organizationId the organization_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listServiceTokens(String organizationId, Options options) throws IOException, InterruptedException {
        return listServiceTokens(organizationId, Queries.ListServiceTokensQuery.builder().build(), options);
    }

    /**
     * List service tokens. Uses default HTTP request options.
     * @param organizationId the organization_id path identifier
     * @param query the operation-specific query values
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listServiceTokens(String organizationId, Queries.ListServiceTokensQuery query) throws IOException, InterruptedException {
        return listServiceTokens(organizationId, query, Options.defaults());
    }
    /**
     * List service tokens. Uses default request options.
     * @param organizationId the organization_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listServiceTokens(String organizationId) throws IOException, InterruptedException {
        return listServiceTokens(organizationId, Options.defaults());
    }

    /**
     * Revoke a service token.
     * @param organizationId the organization_id path identifier
     * @param credentialId the credential_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response revokeServiceToken(String organizationId, String credentialId, Options options) throws IOException, InterruptedException {
        return call("revokeServiceToken", Map.of("organization_id", organizationId, "credential_id", credentialId), options);
    }
    /**
     * Revoke a service token. Uses default request options.
     * @param organizationId the organization_id path identifier
     * @param credentialId the credential_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response revokeServiceToken(String organizationId, String credentialId) throws IOException, InterruptedException {
        return revokeServiceToken(organizationId, credentialId, Options.defaults());
    }

    /**
     * Rotate a service-token secret.
     * @param organizationId the organization_id path identifier
     * @param credentialId the credential_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response rotateServiceToken(String organizationId, String credentialId, Options options) throws IOException, InterruptedException {
        return call("rotateServiceToken", Map.of("organization_id", organizationId, "credential_id", credentialId), options);
    }
    /**
     * Rotate a service-token secret. Uses default request options.
     * @param organizationId the organization_id path identifier
     * @param credentialId the credential_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response rotateServiceToken(String organizationId, String credentialId) throws IOException, InterruptedException {
        return rotateServiceToken(organizationId, credentialId, Options.defaults());
    }

    /**
     * Edit service-token configuration.
     * @param organizationId the organization_id path identifier
     * @param credentialId the credential_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response updateServiceToken(String organizationId, String credentialId, Options options) throws IOException, InterruptedException {
        return call("updateServiceToken", Map.of("organization_id", organizationId, "credential_id", credentialId), options);
    }
    /**
     * Edit service-token configuration. Uses default request options.
     * @param organizationId the organization_id path identifier
     * @param credentialId the credential_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response updateServiceToken(String organizationId, String credentialId) throws IOException, InterruptedException {
        return updateServiceToken(organizationId, credentialId, Options.defaults());
    }

    /**
     * Remove a translation from the organization library.
     * @param organizationId the organization_id path identifier
     * @param slug the slug path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response disableOrganizationTranslation(String organizationId, String slug, Options options) throws IOException, InterruptedException {
        return call("disableOrganizationTranslation", Map.of("organization_id", organizationId, "slug", slug), options);
    }
    /**
     * Remove a translation from the organization library. Uses default request options.
     * @param organizationId the organization_id path identifier
     * @param slug the slug path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response disableOrganizationTranslation(String organizationId, String slug) throws IOException, InterruptedException {
        return disableOrganizationTranslation(organizationId, slug, Options.defaults());
    }

    /**
     * Enable an installed translation in the organization library.
     * @param organizationId the organization_id path identifier
     * @param slug the slug path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response enableOrganizationTranslation(String organizationId, String slug, Options options) throws IOException, InterruptedException {
        return call("enableOrganizationTranslation", Map.of("organization_id", organizationId, "slug", slug), options);
    }
    /**
     * Enable an installed translation in the organization library. Uses default request options.
     * @param organizationId the organization_id path identifier
     * @param slug the slug path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response enableOrganizationTranslation(String organizationId, String slug) throws IOException, InterruptedException {
        return enableOrganizationTranslation(organizationId, slug, Options.defaults());
    }

    /**
     * Read an organization library membership.
     * @param organizationId the organization_id path identifier
     * @param slug the slug path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getOrganizationTranslation(String organizationId, String slug, Options options) throws IOException, InterruptedException {
        return call("getOrganizationTranslation", Map.of("organization_id", organizationId, "slug", slug), options);
    }
    /**
     * Read an organization library membership. Uses default request options.
     * @param organizationId the organization_id path identifier
     * @param slug the slug path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getOrganizationTranslation(String organizationId, String slug) throws IOException, InterruptedException {
        return getOrganizationTranslation(organizationId, slug, Options.defaults());
    }

    /**
     * Read translation edition metadata.
     * @param slug the slug path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getTranslation(String slug, Options options) throws IOException, InterruptedException {
        return call("getTranslation", Map.of("slug", slug), options);
    }
    /**
     * Read translation edition metadata. Uses default request options.
     * @param slug the slug path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getTranslation(String slug) throws IOException, InterruptedException {
        return getTranslation(slug, Options.defaults());
    }

    /**
     * Read installed chapter and verse catalogue.
     * @param slug the slug path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getTranslationCatalogueDetails(String slug, Options options) throws IOException, InterruptedException {
        return call("getTranslationCatalogueDetails", Map.of("slug", slug), options);
    }
    /**
     * Read installed chapter and verse catalogue. Uses default request options.
     * @param slug the slug path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response getTranslationCatalogueDetails(String slug) throws IOException, InterruptedException {
        return getTranslationCatalogueDetails(slug, Options.defaults());
    }

    /**
     * List canonical book definitions.

     * @param query the operation-specific query values
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listBooks(Queries.ListBooksQuery query, Options options) throws IOException, InterruptedException {
        return call("listBooks", Map.of(), query, options);
    }
    /**
     * List canonical book definitions. Leaves query values unset for server defaults.
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listBooks(Options options) throws IOException, InterruptedException {
        return listBooks(Queries.ListBooksQuery.builder().build(), options);
    }

    /**
     * List canonical book definitions. Uses default HTTP request options.
     * @param query the operation-specific query values
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listBooks(Queries.ListBooksQuery query) throws IOException, InterruptedException {
        return listBooks(query, Options.defaults());
    }
    /**
     * List canonical book definitions. Uses default request options.
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listBooks() throws IOException, InterruptedException {
        return listBooks(Options.defaults());
    }

    /**
     * List the organization translation library.
     * @param organizationId the organization_id path identifier

     * @param query the operation-specific query values
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listOrganizationTranslations(String organizationId, Queries.ListOrganizationTranslationsQuery query, Options options) throws IOException, InterruptedException {
        return call("listOrganizationTranslations", Map.of("organization_id", organizationId), query, options);
    }
    /**
     * List the organization translation library. Leaves query values unset for server defaults.
     * @param organizationId the organization_id path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listOrganizationTranslations(String organizationId, Options options) throws IOException, InterruptedException {
        return listOrganizationTranslations(organizationId, Queries.ListOrganizationTranslationsQuery.builder().build(), options);
    }

    /**
     * List the organization translation library. Uses default HTTP request options.
     * @param organizationId the organization_id path identifier
     * @param query the operation-specific query values
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listOrganizationTranslations(String organizationId, Queries.ListOrganizationTranslationsQuery query) throws IOException, InterruptedException {
        return listOrganizationTranslations(organizationId, query, Options.defaults());
    }
    /**
     * List the organization translation library. Uses default request options.
     * @param organizationId the organization_id path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listOrganizationTranslations(String organizationId) throws IOException, InterruptedException {
        return listOrganizationTranslations(organizationId, Options.defaults());
    }

    /**
     * List a translation edition book coverage.
     * @param slug the slug path identifier

     * @param query the operation-specific query values
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listTranslationBooks(String slug, Queries.ListTranslationBooksQuery query, Options options) throws IOException, InterruptedException {
        return call("listTranslationBooks", Map.of("slug", slug), query, options);
    }
    /**
     * List a translation edition book coverage. Leaves query values unset for server defaults.
     * @param slug the slug path identifier
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listTranslationBooks(String slug, Options options) throws IOException, InterruptedException {
        return listTranslationBooks(slug, Queries.ListTranslationBooksQuery.builder().build(), options);
    }

    /**
     * List a translation edition book coverage. Uses default HTTP request options.
     * @param slug the slug path identifier
     * @param query the operation-specific query values
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listTranslationBooks(String slug, Queries.ListTranslationBooksQuery query) throws IOException, InterruptedException {
        return listTranslationBooks(slug, query, Options.defaults());
    }
    /**
     * List a translation edition book coverage. Uses default request options.
     * @param slug the slug path identifier
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listTranslationBooks(String slug) throws IOException, InterruptedException {
        return listTranslationBooks(slug, Options.defaults());
    }

    /**
     * List reviewed translation editions.

     * @param query the operation-specific query values
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listTranslationCatalogue(Queries.ListTranslationCatalogueQuery query, Options options) throws IOException, InterruptedException {
        return call("listTranslationCatalogue", Map.of(), query, options);
    }
    /**
     * List reviewed translation editions. Leaves query values unset for server defaults.
     * @param options immutable HTTP request options
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listTranslationCatalogue(Options options) throws IOException, InterruptedException {
        return listTranslationCatalogue(Queries.ListTranslationCatalogueQuery.builder().build(), options);
    }

    /**
     * List reviewed translation editions. Uses default HTTP request options.
     * @param query the operation-specific query values
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listTranslationCatalogue(Queries.ListTranslationCatalogueQuery query) throws IOException, InterruptedException {
        return listTranslationCatalogue(query, Options.defaults());
    }
    /**
     * List reviewed translation editions. Uses default request options.
     * @return the streaming response; the caller must close it
     * @throws IOException if transport or HTTP processing fails
     * @throws InterruptedException if the request is interrupted
     */
    public Response listTranslationCatalogue() throws IOException, InterruptedException {
        return listTranslationCatalogue(Options.defaults());
    }

}
