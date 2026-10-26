package app.bibleit;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.*;
import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/** Bibleit resource client. Credentials are explicit and service origins are fixed. */
public final class BibleitClient {
    public static final String ENDPOINT = "https://api.bibleit.app/v1";
    public static final String SERVER_ENDPOINT = "https://bibleit.app";
    private final String token;
    private final Transport transport;
    public BibleitClient(String token) { this(token, Transport.standard()); }
    public BibleitClient(String token, Transport transport) {
        this.token = Objects.requireNonNull(token);
        if (!token.equals(token.strip()) || token.chars().anyMatch(c -> c < 32 || c == 127))
            throw new IllegalArgumentException("Invalid bearer token");
        this.transport = Objects.requireNonNull(transport);
    }
    public static Map<String, Operation> operations() { return Operations.ALL; }
    public static List<String> operationNames() { return Operations.ALL.keySet().stream().sorted().toList(); }
    public Response call(String operation, Map<String, String> parameters, Options options) throws IOException, InterruptedException {
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
        String query = encodeQuery(o.query);
        var request = HttpRequest.newBuilder(URI.create(ENDPOINT + path + (query.isEmpty() ? "" : "?" + query)))
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
    static String encodeQuery(Map<String, List<String>> query) {
        var pairs = new ArrayList<String>();
        query.forEach((key, values) -> values.forEach(value -> pairs.add(encode(key) + "=" + encode(value))));
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
    public void revokeCredential() throws IOException, InterruptedException {
        if (token.isEmpty()) throw new IllegalStateException("Browser credential required");
        var request = HttpRequest.newBuilder(URI.create(SERVER_ENDPOINT + "/api/cli/logout"))
            .timeout(java.time.Duration.ofSeconds(40)).header("Authorization", "Bearer " + token)
            .POST(HttpRequest.BodyPublishers.noBody()).build();
        try (var response = checked(transport.send(request))) {
            if (response.statusCode() != 204) throw new IOException("Invalid revocation acknowledgement");
        }
    }
    public JsonNode pollLiveEvents(String liveId, String after, int wait) throws IOException, InterruptedException {
        if (wait < 0 || wait > 25) throw new IllegalArgumentException("Wait must be 0..25 seconds");
        var options = Options.builder().query("wait", Integer.toString(wait));
        if (after != null && !after.isEmpty()) options.query("after", after);
        try (var response = getLiveEvents(liveId, options.build())) {
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
    public BookPages readPersonalBookPages(String accountId, String slug, String bookId, Options options) {
        return new BookPages(this, "readPersonalBook", Map.of("account_id", accountId, "slug", slug, "book_id", bookId), options);
    }
    public BookPages readBookPages(String organizationId, String slug, String bookId, Options options) {
        return new BookPages(this, "readBook", Map.of("organization_id", organizationId, "slug", slug, "book_id", bookId), options);
    }
    /** List service tokens. */
    public Response listServiceTokens(String organizationId, Options options) throws IOException, InterruptedException {
        return call("listServiceTokens", Map.of("organization_id", organizationId), options);
    }
    public Response listServiceTokens(String organizationId) throws IOException, InterruptedException {
        return listServiceTokens(organizationId, Options.defaults());
    }
    /** Create a service token. */
    public Response createServiceToken(String organizationId, Options options) throws IOException, InterruptedException {
        return call("createServiceToken", Map.of("organization_id", organizationId), options);
    }
    public Response createServiceToken(String organizationId) throws IOException, InterruptedException {
        return createServiceToken(organizationId, Options.defaults());
    }
    /** Revoke a service token. */
    public Response revokeServiceToken(String organizationId, String credentialId, Options options) throws IOException, InterruptedException {
        return call("revokeServiceToken", Map.of("organization_id", organizationId, "credential_id", credentialId), options);
    }
    public Response revokeServiceToken(String organizationId, String credentialId) throws IOException, InterruptedException {
        return revokeServiceToken(organizationId, credentialId, Options.defaults());
    }
    /** Read service-token configuration. */
    public Response getServiceToken(String organizationId, String credentialId, Options options) throws IOException, InterruptedException {
        return call("getServiceToken", Map.of("organization_id", organizationId, "credential_id", credentialId), options);
    }
    public Response getServiceToken(String organizationId, String credentialId) throws IOException, InterruptedException {
        return getServiceToken(organizationId, credentialId, Options.defaults());
    }
    /** Edit service-token configuration. */
    public Response updateServiceToken(String organizationId, String credentialId, Options options) throws IOException, InterruptedException {
        return call("updateServiceToken", Map.of("organization_id", organizationId, "credential_id", credentialId), options);
    }
    public Response updateServiceToken(String organizationId, String credentialId) throws IOException, InterruptedException {
        return updateServiceToken(organizationId, credentialId, Options.defaults());
    }
    /** List organization Lives. */
    public Response listLives(String organizationId, Options options) throws IOException, InterruptedException {
        return call("listLives", Map.of("organization_id", organizationId), options);
    }
    public Response listLives(String organizationId) throws IOException, InterruptedException {
        return listLives(organizationId, Options.defaults());
    }
    /** Create an organization Live. */
    public Response createLive(String organizationId, Options options) throws IOException, InterruptedException {
        return call("createLive", Map.of("organization_id", organizationId), options);
    }
    public Response createLive(String organizationId) throws IOException, InterruptedException {
        return createLive(organizationId, Options.defaults());
    }
    /** Atomically delete all Lives in this workspace. */
    public Response deleteOrganizationLives(String organizationId, Options options) throws IOException, InterruptedException {
        return call("deleteOrganizationLives", Map.of("organization_id", organizationId), options);
    }
    public Response deleteOrganizationLives(String organizationId) throws IOException, InterruptedException {
        return deleteOrganizationLives(organizationId, Options.defaults());
    }
    /** Read Live state. */
    public Response getLive(String liveId, Options options) throws IOException, InterruptedException {
        return call("getLive", Map.of("live_id", liveId), options);
    }
    public Response getLive(String liveId) throws IOException, InterruptedException {
        return getLive(liveId, Options.defaults());
    }
    /** Delete a Live. */
    public Response deleteLive(String liveId, Options options) throws IOException, InterruptedException {
        return call("deleteLive", Map.of("live_id", liveId), options);
    }
    public Response deleteLive(String liveId) throws IOException, InterruptedException {
        return deleteLive(liveId, Options.defaults());
    }
    /** Edit Live metadata. */
    public Response updateLiveMetadata(String liveId, Options options) throws IOException, InterruptedException {
        return call("updateLiveMetadata", Map.of("live_id", liveId), options);
    }
    public Response updateLiveMetadata(String liveId) throws IOException, InterruptedException {
        return updateLiveMetadata(liveId, Options.defaults());
    }
    /** Download the OpenAPI contract. */
    public Response getOpenApi(Options options) throws IOException, InterruptedException {
        return call("getOpenApi", Map.of(), options);
    }
    public Response getOpenApi() throws IOException, InterruptedException {
        return getOpenApi(Options.defaults());
    }
    /** Read a chapter. */
    public Response readChapter(String organizationId, String slug, String bookId, String chapter, Options options) throws IOException, InterruptedException {
        return call("readChapter", Map.of("organization_id", organizationId, "slug", slug, "book_id", bookId, "chapter", chapter), options);
    }
    public Response readChapter(String organizationId, String slug, String bookId, String chapter) throws IOException, InterruptedException {
        return readChapter(organizationId, slug, bookId, chapter, Options.defaults());
    }
    /** Read a verse. */
    public Response readVerse(String organizationId, String slug, String bookId, String chapter, String verse, Options options) throws IOException, InterruptedException {
        return call("readVerse", Map.of("organization_id", organizationId, "slug", slug, "book_id", bookId, "chapter", chapter, "verse", verse), options);
    }
    public Response readVerse(String organizationId, String slug, String bookId, String chapter, String verse) throws IOException, InterruptedException {
        return readVerse(organizationId, slug, bookId, chapter, verse, Options.defaults());
    }
    /** Search organization scripture. */
    public Response searchScripture(String organizationId, String slug, Options options) throws IOException, InterruptedException {
        return call("searchScripture", Map.of("organization_id", organizationId, "slug", slug), options);
    }
    public Response searchScripture(String organizationId, String slug) throws IOException, InterruptedException {
        return searchScripture(organizationId, slug, Options.defaults());
    }
    /** Read the Live display. */
    public Response getLiveDisplay(String liveId, Options options) throws IOException, InterruptedException {
        return call("getLiveDisplay", Map.of("live_id", liveId), options);
    }
    public Response getLiveDisplay(String liveId) throws IOException, InterruptedException {
        return getLiveDisplay(liveId, Options.defaults());
    }
    /** Replace or clear the Live display. */
    public Response replaceLiveDisplay(String liveId, Options options) throws IOException, InterruptedException {
        return call("replaceLiveDisplay", Map.of("live_id", liveId), options);
    }
    public Response replaceLiveDisplay(String liveId) throws IOException, InterruptedException {
        return replaceLiveDisplay(liveId, Options.defaults());
    }
    /** Read Live playback state. */
    public Response getLivePlayback(String liveId, Options options) throws IOException, InterruptedException {
        return call("getLivePlayback", Map.of("live_id", liveId), options);
    }
    public Response getLivePlayback(String liveId) throws IOException, InterruptedException {
        return getLivePlayback(liveId, Options.defaults());
    }
    /** Update Live playback state. */
    public Response updateLivePlayback(String liveId, Options options) throws IOException, InterruptedException {
        return call("updateLivePlayback", Map.of("live_id", liveId), options);
    }
    public Response updateLivePlayback(String liveId) throws IOException, InterruptedException {
        return updateLivePlayback(liveId, Options.defaults());
    }
    /** Rotate a service-token secret. */
    public Response rotateServiceToken(String organizationId, String credentialId, Options options) throws IOException, InterruptedException {
        return call("rotateServiceToken", Map.of("organization_id", organizationId, "credential_id", credentialId), options);
    }
    public Response rotateServiceToken(String organizationId, String credentialId) throws IOException, InterruptedException {
        return rotateServiceToken(organizationId, credentialId, Options.defaults());
    }
    /** List reviewed translation editions. */
    public Response listTranslationCatalogue(Options options) throws IOException, InterruptedException {
        return call("listTranslationCatalogue", Map.of(), options);
    }
    public Response listTranslationCatalogue() throws IOException, InterruptedException {
        return listTranslationCatalogue(Options.defaults());
    }
    /** Read translation edition metadata. */
    public Response getTranslation(String slug, Options options) throws IOException, InterruptedException {
        return call("getTranslation", Map.of("slug", slug), options);
    }
    public Response getTranslation(String slug) throws IOException, InterruptedException {
        return getTranslation(slug, Options.defaults());
    }
    /** List canonical book definitions. */
    public Response listBooks(Options options) throws IOException, InterruptedException {
        return call("listBooks", Map.of(), options);
    }
    public Response listBooks() throws IOException, InterruptedException {
        return listBooks(Options.defaults());
    }
    /** List a translation edition book coverage. */
    public Response listTranslationBooks(String slug, Options options) throws IOException, InterruptedException {
        return call("listTranslationBooks", Map.of("slug", slug), options);
    }
    public Response listTranslationBooks(String slug) throws IOException, InterruptedException {
        return listTranslationBooks(slug, Options.defaults());
    }
    /** List the organization translation library. */
    public Response listOrganizationTranslations(String organizationId, Options options) throws IOException, InterruptedException {
        return call("listOrganizationTranslations", Map.of("organization_id", organizationId), options);
    }
    public Response listOrganizationTranslations(String organizationId) throws IOException, InterruptedException {
        return listOrganizationTranslations(organizationId, Options.defaults());
    }
    /** Enable an installed translation in the organization library. */
    public Response enableOrganizationTranslation(String organizationId, String slug, Options options) throws IOException, InterruptedException {
        return call("enableOrganizationTranslation", Map.of("organization_id", organizationId, "slug", slug), options);
    }
    public Response enableOrganizationTranslation(String organizationId, String slug) throws IOException, InterruptedException {
        return enableOrganizationTranslation(organizationId, slug, Options.defaults());
    }
    /** Remove a translation from the organization library. */
    public Response disableOrganizationTranslation(String organizationId, String slug, Options options) throws IOException, InterruptedException {
        return call("disableOrganizationTranslation", Map.of("organization_id", organizationId, "slug", slug), options);
    }
    public Response disableOrganizationTranslation(String organizationId, String slug) throws IOException, InterruptedException {
        return disableOrganizationTranslation(organizationId, slug, Options.defaults());
    }
    /** Read an organization library membership. */
    public Response getOrganizationTranslation(String organizationId, String slug, Options options) throws IOException, InterruptedException {
        return call("getOrganizationTranslation", Map.of("organization_id", organizationId, "slug", slug), options);
    }
    public Response getOrganizationTranslation(String organizationId, String slug) throws IOException, InterruptedException {
        return getOrganizationTranslation(organizationId, slug, Options.defaults());
    }
    /** Discover the service token organization. */
    public Response getServiceOrganization(Options options) throws IOException, InterruptedException {
        return call("getServiceOrganization", Map.of(), options);
    }
    public Response getServiceOrganization() throws IOException, InterruptedException {
        return getServiceOrganization(Options.defaults());
    }
    /** Read organization identity. */
    public Response getOrganization(String organizationId, Options options) throws IOException, InterruptedException {
        return call("getOrganization", Map.of("organization_id", organizationId), options);
    }
    public Response getOrganization(String organizationId) throws IOException, InterruptedException {
        return getOrganization(organizationId, Options.defaults());
    }
    /** List active organization memberships. */
    public Response listOrganizationMembers(String organizationId, Options options) throws IOException, InterruptedException {
        return call("listOrganizationMembers", Map.of("organization_id", organizationId), options);
    }
    public Response listOrganizationMembers(String organizationId) throws IOException, InterruptedException {
        return listOrganizationMembers(organizationId, Options.defaults());
    }
    /** Read an active membership. */
    public Response getOrganizationMember(String organizationId, String memberId, Options options) throws IOException, InterruptedException {
        return call("getOrganizationMember", Map.of("organization_id", organizationId, "member_id", memberId), options);
    }
    public Response getOrganizationMember(String organizationId, String memberId) throws IOException, InterruptedException {
        return getOrganizationMember(organizationId, memberId, Options.defaults());
    }
    /** Change a member role. */
    public Response updateOrganizationMember(String organizationId, String memberId, Options options) throws IOException, InterruptedException {
        return call("updateOrganizationMember", Map.of("organization_id", organizationId, "member_id", memberId), options);
    }
    public Response updateOrganizationMember(String organizationId, String memberId) throws IOException, InterruptedException {
        return updateOrganizationMember(organizationId, memberId, Options.defaults());
    }
    /** Remove a member. */
    public Response deleteOrganizationMember(String organizationId, String memberId, Options options) throws IOException, InterruptedException {
        return call("deleteOrganizationMember", Map.of("organization_id", organizationId, "member_id", memberId), options);
    }
    public Response deleteOrganizationMember(String organizationId, String memberId) throws IOException, InterruptedException {
        return deleteOrganizationMember(organizationId, memberId, Options.defaults());
    }
    /** Move a Live to another organization. */
    public Response moveLiveWorkspace(String liveId, Options options) throws IOException, InterruptedException {
        return call("moveLiveWorkspace", Map.of("live_id", liveId), options);
    }
    public Response moveLiveWorkspace(String liveId) throws IOException, InterruptedException {
        return moveLiveWorkspace(liveId, Options.defaults());
    }
    /** List ordered Live queue items. */
    public Response listLiveQueueItems(String liveId, Options options) throws IOException, InterruptedException {
        return call("listLiveQueueItems", Map.of("live_id", liveId), options);
    }
    public Response listLiveQueueItems(String liveId) throws IOException, InterruptedException {
        return listLiveQueueItems(liveId, Options.defaults());
    }
    /** Insert a scripture queue item. */
    public Response createLiveQueueItem(String liveId, Options options) throws IOException, InterruptedException {
        return call("createLiveQueueItem", Map.of("live_id", liveId), options);
    }
    public Response createLiveQueueItem(String liveId) throws IOException, InterruptedException {
        return createLiveQueueItem(liveId, Options.defaults());
    }
    /** Read a stable queue item. */
    public Response getLiveQueueItem(String liveId, String itemId, Options options) throws IOException, InterruptedException {
        return call("getLiveQueueItem", Map.of("live_id", liveId, "item_id", itemId), options);
    }
    public Response getLiveQueueItem(String liveId, String itemId) throws IOException, InterruptedException {
        return getLiveQueueItem(liveId, itemId, Options.defaults());
    }
    /** Remove a queue item. */
    public Response deleteLiveQueueItem(String liveId, String itemId, Options options) throws IOException, InterruptedException {
        return call("deleteLiveQueueItem", Map.of("live_id", liveId, "item_id", itemId), options);
    }
    public Response deleteLiveQueueItem(String liveId, String itemId) throws IOException, InterruptedException {
        return deleteLiveQueueItem(liveId, itemId, Options.defaults());
    }
    /** Replace queue ordering. */
    public Response replaceLiveQueueOrder(String liveId, Options options) throws IOException, InterruptedException {
        return call("replaceLiveQueueOrder", Map.of("live_id", liveId), options);
    }
    public Response replaceLiveQueueOrder(String liveId) throws IOException, InterruptedException {
        return replaceLiveQueueOrder(liveId, Options.defaults());
    }
    /** Read organization Live Studio settings. */
    public Response getOrganizationLiveStudio(String organizationId, Options options) throws IOException, InterruptedException {
        return call("getOrganizationLiveStudio", Map.of("organization_id", organizationId), options);
    }
    public Response getOrganizationLiveStudio(String organizationId) throws IOException, InterruptedException {
        return getOrganizationLiveStudio(organizationId, Options.defaults());
    }
    /** Replace organization Live Studio settings. */
    public Response replaceOrganizationLiveStudio(String organizationId, Options options) throws IOException, InterruptedException {
        return call("replaceOrganizationLiveStudio", Map.of("organization_id", organizationId), options);
    }
    public Response replaceOrganizationLiveStudio(String organizationId) throws IOException, InterruptedException {
        return replaceOrganizationLiveStudio(organizationId, Options.defaults());
    }
    /** List organization presentations. */
    public Response listPresentations(String organizationId, Options options) throws IOException, InterruptedException {
        return call("listPresentations", Map.of("organization_id", organizationId), options);
    }
    public Response listPresentations(String organizationId) throws IOException, InterruptedException {
        return listPresentations(organizationId, Options.defaults());
    }
    /** Create an organization presentation. */
    public Response createPresentation(String organizationId, Options options) throws IOException, InterruptedException {
        return call("createPresentation", Map.of("organization_id", organizationId), options);
    }
    public Response createPresentation(String organizationId) throws IOException, InterruptedException {
        return createPresentation(organizationId, Options.defaults());
    }
    /** Read a saved presentation. */
    public Response getPresentation(String presentationId, Options options) throws IOException, InterruptedException {
        return call("getPresentation", Map.of("presentation_id", presentationId), options);
    }
    public Response getPresentation(String presentationId) throws IOException, InterruptedException {
        return getPresentation(presentationId, Options.defaults());
    }
    /** Replace a saved presentation document. */
    public Response replacePresentation(String presentationId, Options options) throws IOException, InterruptedException {
        return call("replacePresentation", Map.of("presentation_id", presentationId), options);
    }
    public Response replacePresentation(String presentationId) throws IOException, InterruptedException {
        return replacePresentation(presentationId, Options.defaults());
    }
    /** Delete a saved presentation. */
    public Response deletePresentation(String presentationId, Options options) throws IOException, InterruptedException {
        return call("deletePresentation", Map.of("presentation_id", presentationId), options);
    }
    public Response deletePresentation(String presentationId) throws IOException, InterruptedException {
        return deletePresentation(presentationId, Options.defaults());
    }
    /** Upload a staged presentation asset. */
    public Response uploadPresentationAsset(String presentationId, Options options) throws IOException, InterruptedException {
        return call("uploadPresentationAsset", Map.of("presentation_id", presentationId), options);
    }
    public Response uploadPresentationAsset(String presentationId) throws IOException, InterruptedException {
        return uploadPresentationAsset(presentationId, Options.defaults());
    }
    /** Read protected presentation asset bytes. */
    public Response getPresentationAsset(String presentationId, String assetId, Options options) throws IOException, InterruptedException {
        return call("getPresentationAsset", Map.of("presentation_id", presentationId, "asset_id", assetId), options);
    }
    public Response getPresentationAsset(String presentationId, String assetId) throws IOException, InterruptedException {
        return getPresentationAsset(presentationId, assetId, Options.defaults());
    }
    /** Import a portable presentation bundle. */
    public Response importPresentation(String organizationId, Options options) throws IOException, InterruptedException {
        return call("importPresentation", Map.of("organization_id", organizationId), options);
    }
    public Response importPresentation(String organizationId) throws IOException, InterruptedException {
        return importPresentation(organizationId, Options.defaults());
    }
    /** Queue a saved-revision presentation export. */
    public Response createPresentationExport(String presentationId, Options options) throws IOException, InterruptedException {
        return call("createPresentationExport", Map.of("presentation_id", presentationId), options);
    }
    public Response createPresentationExport(String presentationId) throws IOException, InterruptedException {
        return createPresentationExport(presentationId, Options.defaults());
    }
    /** List the requesting service credential’s export jobs. */
    public Response listPresentationExports(String presentationId, Options options) throws IOException, InterruptedException {
        return call("listPresentationExports", Map.of("presentation_id", presentationId), options);
    }
    public Response listPresentationExports(String presentationId) throws IOException, InterruptedException {
        return listPresentationExports(presentationId, Options.defaults());
    }
    /** Read export job status. */
    public Response getPresentationExport(String presentationId, String exportId, Options options) throws IOException, InterruptedException {
        return call("getPresentationExport", Map.of("presentation_id", presentationId, "export_id", exportId), options);
    }
    public Response getPresentationExport(String presentationId, String exportId) throws IOException, InterruptedException {
        return getPresentationExport(presentationId, exportId, Options.defaults());
    }
    /** Consume and download a completed export once. */
    public Response downloadPresentationExport(String presentationId, String exportId, Options options) throws IOException, InterruptedException {
        return call("downloadPresentationExport", Map.of("presentation_id", presentationId, "export_id", exportId), options);
    }
    public Response downloadPresentationExport(String presentationId, String exportId) throws IOException, InterruptedException {
        return downloadPresentationExport(presentationId, exportId, Options.defaults());
    }
    /** Read your personal identity. */
    public Response getPersonalIdentity(Options options) throws IOException, InterruptedException {
        return call("getPersonalIdentity", Map.of(), options);
    }
    public Response getPersonalIdentity() throws IOException, InterruptedException {
        return getPersonalIdentity(Options.defaults());
    }
    /** List your personal account. */
    public Response listPersonalAccounts(Options options) throws IOException, InterruptedException {
        return call("listPersonalAccounts", Map.of(), options);
    }
    public Response listPersonalAccounts() throws IOException, InterruptedException {
        return listPersonalAccounts(Options.defaults());
    }
    /** Read your personal account. */
    public Response getPersonalAccount(String accountId, Options options) throws IOException, InterruptedException {
        return call("getPersonalAccount", Map.of("account_id", accountId), options);
    }
    public Response getPersonalAccount(String accountId) throws IOException, InterruptedException {
        return getPersonalAccount(accountId, Options.defaults());
    }
    /** List the personal account translation library. */
    public Response listPersonalTranslations(String accountId, Options options) throws IOException, InterruptedException {
        return call("listPersonalTranslations", Map.of("account_id", accountId), options);
    }
    public Response listPersonalTranslations(String accountId) throws IOException, InterruptedException {
        return listPersonalTranslations(accountId, Options.defaults());
    }
    /** Read an personal account library membership. */
    public Response getPersonalTranslation(String accountId, String slug, Options options) throws IOException, InterruptedException {
        return call("getPersonalTranslation", Map.of("account_id", accountId, "slug", slug), options);
    }
    public Response getPersonalTranslation(String accountId, String slug) throws IOException, InterruptedException {
        return getPersonalTranslation(accountId, slug, Options.defaults());
    }
    /** Enable an installed personal translation. */
    public Response enablePersonalTranslation(String accountId, String slug, Options options) throws IOException, InterruptedException {
        return call("enablePersonalTranslation", Map.of("account_id", accountId, "slug", slug), options);
    }
    public Response enablePersonalTranslation(String accountId, String slug) throws IOException, InterruptedException {
        return enablePersonalTranslation(accountId, slug, Options.defaults());
    }
    /** Disable a personal translation. */
    public Response disablePersonalTranslation(String accountId, String slug, Options options) throws IOException, InterruptedException {
        return call("disablePersonalTranslation", Map.of("account_id", accountId, "slug", slug), options);
    }
    public Response disablePersonalTranslation(String accountId, String slug) throws IOException, InterruptedException {
        return disablePersonalTranslation(accountId, slug, Options.defaults());
    }
    /** Read a chapter. */
    public Response readPersonalChapter(String accountId, String slug, String bookId, String chapter, Options options) throws IOException, InterruptedException {
        return call("readPersonalChapter", Map.of("account_id", accountId, "slug", slug, "book_id", bookId, "chapter", chapter), options);
    }
    public Response readPersonalChapter(String accountId, String slug, String bookId, String chapter) throws IOException, InterruptedException {
        return readPersonalChapter(accountId, slug, bookId, chapter, Options.defaults());
    }
    /** Read a verse. */
    public Response readPersonalVerse(String accountId, String slug, String bookId, String chapter, String verse, Options options) throws IOException, InterruptedException {
        return call("readPersonalVerse", Map.of("account_id", accountId, "slug", slug, "book_id", bookId, "chapter", chapter, "verse", verse), options);
    }
    public Response readPersonalVerse(String accountId, String slug, String bookId, String chapter, String verse) throws IOException, InterruptedException {
        return readPersonalVerse(accountId, slug, bookId, chapter, verse, Options.defaults());
    }
    /** Search personal account scripture. */
    public Response searchPersonalScripture(String accountId, String slug, Options options) throws IOException, InterruptedException {
        return call("searchPersonalScripture", Map.of("account_id", accountId, "slug", slug), options);
    }
    public Response searchPersonalScripture(String accountId, String slug) throws IOException, InterruptedException {
        return searchPersonalScripture(accountId, slug, Options.defaults());
    }
    /** List your personal Lives. */
    public Response listPersonalLives(String accountId, Options options) throws IOException, InterruptedException {
        return call("listPersonalLives", Map.of("account_id", accountId), options);
    }
    public Response listPersonalLives(String accountId) throws IOException, InterruptedException {
        return listPersonalLives(accountId, Options.defaults());
    }
    /** Create a personal Reader Live. */
    public Response createPersonalLive(String accountId, Options options) throws IOException, InterruptedException {
        return call("createPersonalLive", Map.of("account_id", accountId), options);
    }
    public Response createPersonalLive(String accountId) throws IOException, InterruptedException {
        return createPersonalLive(accountId, Options.defaults());
    }
    /** Atomically delete all Lives in this workspace. */
    public Response deletePersonalLives(String accountId, Options options) throws IOException, InterruptedException {
        return call("deletePersonalLives", Map.of("account_id", accountId), options);
    }
    public Response deletePersonalLives(String accountId) throws IOException, InterruptedException {
        return deletePersonalLives(accountId, Options.defaults());
    }
    /** Read OAuth authorization-server metadata. */
    public Response getOAuthMetadata(Options options) throws IOException, InterruptedException {
        return call("getOAuthMetadata", Map.of(), options);
    }
    public Response getOAuthMetadata() throws IOException, InterruptedException {
        return getOAuthMetadata(Options.defaults());
    }
    /** Start registered-client authorization. */
    public Response authorizeOAuth(Options options) throws IOException, InterruptedException {
        return call("authorizeOAuth", Map.of(), options);
    }
    public Response authorizeOAuth() throws IOException, InterruptedException {
        return authorizeOAuth(Options.defaults());
    }
    /** Approve or deny OAuth consent. */
    public Response decideOAuthConsent(Options options) throws IOException, InterruptedException {
        return call("decideOAuthConsent", Map.of(), options);
    }
    public Response decideOAuthConsent() throws IOException, InterruptedException {
        return decideOAuthConsent(Options.defaults());
    }
    /** Exchange a code or rotate a refresh token. */
    public Response exchangeOAuthToken(Options options) throws IOException, InterruptedException {
        return call("exchangeOAuthToken", Map.of(), options);
    }
    public Response exchangeOAuthToken() throws IOException, InterruptedException {
        return exchangeOAuthToken(Options.defaults());
    }
    /** Revoke delegated access and refresh tokens. */
    public Response revokeOAuthToken(Options options) throws IOException, InterruptedException {
        return call("revokeOAuthToken", Map.of(), options);
    }
    public Response revokeOAuthToken() throws IOException, InterruptedException {
        return revokeOAuthToken(Options.defaults());
    }
    /** View connected applications. */
    public Response getConnectedAppsPage(Options options) throws IOException, InterruptedException {
        return call("getConnectedAppsPage", Map.of(), options);
    }
    public Response getConnectedAppsPage() throws IOException, InterruptedException {
        return getConnectedAppsPage(Options.defaults());
    }
    /** Disconnect an application. */
    public Response disconnectOAuthApp(Options options) throws IOException, InterruptedException {
        return call("disconnectOAuthApp", Map.of(), options);
    }
    public Response disconnectOAuthApp() throws IOException, InterruptedException {
        return disconnectOAuthApp(Options.defaults());
    }
    /** List your connected OAuth applications. */
    public Response listOAuthGrants(Options options) throws IOException, InterruptedException {
        return call("listOAuthGrants", Map.of(), options);
    }
    public Response listOAuthGrants() throws IOException, InterruptedException {
        return listOAuthGrants(Options.defaults());
    }
    /** Disconnect your OAuth grant. */
    public Response revokeOAuthGrant(String grantId, Options options) throws IOException, InterruptedException {
        return call("revokeOAuthGrant", Map.of("grant_id", grantId), options);
    }
    public Response revokeOAuthGrant(String grantId) throws IOException, InterruptedException {
        return revokeOAuthGrant(grantId, Options.defaults());
    }
    /** Receive versioned Live events. */
    public Response getLiveEvents(String liveId, Options options) throws IOException, InterruptedException {
        return call("getLiveEvents", Map.of("live_id", liveId), options);
    }
    public Response getLiveEvents(String liveId) throws IOException, InterruptedException {
        return getLiveEvents(liveId, Options.defaults());
    }
    /** Read your effective personal quotas and current usage. */
    public Response getPersonalQuotas(String accountId, Options options) throws IOException, InterruptedException {
        return call("getPersonalQuotas", Map.of("account_id", accountId), options);
    }
    public Response getPersonalQuotas(String accountId) throws IOException, InterruptedException {
        return getPersonalQuotas(accountId, Options.defaults());
    }
    /** List personal credential metadata. */
    public Response listPersonalTokens(String accountId, Options options) throws IOException, InterruptedException {
        return call("listPersonalTokens", Map.of("account_id", accountId), options);
    }
    public Response listPersonalTokens(String accountId) throws IOException, InterruptedException {
        return listPersonalTokens(accountId, Options.defaults());
    }
    /** Read organization limits and usage. */
    public Response getOrganizationQuotas(String organizationId, Options options) throws IOException, InterruptedException {
        return call("getOrganizationQuotas", Map.of("organization_id", organizationId), options);
    }
    public Response getOrganizationQuotas(String organizationId) throws IOException, InterruptedException {
        return getOrganizationQuotas(organizationId, Options.defaults());
    }
    /** List your memberships and applications. */
    public Response listPersonalOrganizations(Options options) throws IOException, InterruptedException {
        return call("listPersonalOrganizations", Map.of(), options);
    }
    public Response listPersonalOrganizations() throws IOException, InterruptedException {
        return listPersonalOrganizations(Options.defaults());
    }
    /** Read your organization application status. */
    public Response getPersonalOrganizationApplication(String applicationId, Options options) throws IOException, InterruptedException {
        return call("getPersonalOrganizationApplication", Map.of("application_id", applicationId), options);
    }
    public Response getPersonalOrganizationApplication(String applicationId) throws IOException, InterruptedException {
        return getPersonalOrganizationApplication(applicationId, Options.defaults());
    }
    /** Read a book page with source verse identifiers. */
    public Response readPersonalBook(String accountId, String slug, String bookId, Options options) throws IOException, InterruptedException {
        return call("readPersonalBook", Map.of("account_id", accountId, "slug", slug, "book_id", bookId), options);
    }
    public Response readPersonalBook(String accountId, String slug, String bookId) throws IOException, InterruptedException {
        return readPersonalBook(accountId, slug, bookId, Options.defaults());
    }
    /** Read a book page with source verse identifiers. */
    public Response readBook(String organizationId, String slug, String bookId, Options options) throws IOException, InterruptedException {
        return call("readBook", Map.of("organization_id", organizationId, "slug", slug, "book_id", bookId), options);
    }
    public Response readBook(String organizationId, String slug, String bookId) throws IOException, InterruptedException {
        return readBook(organizationId, slug, bookId, Options.defaults());
    }
    /** Read installed chapter and verse catalogue. */
    public Response getTranslationCatalogueDetails(String slug, Options options) throws IOException, InterruptedException {
        return call("getTranslationCatalogueDetails", Map.of("slug", slug), options);
    }
    public Response getTranslationCatalogueDetails(String slug) throws IOException, InterruptedException {
        return getTranslationCatalogueDetails(slug, Options.defaults());
    }
    /** Read Live connection and retained-stack statistics. */
    public Response getLiveStatistics(String liveId, Options options) throws IOException, InterruptedException {
        return call("getLiveStatistics", Map.of("live_id", liveId), options);
    }
    public Response getLiveStatistics(String liveId) throws IOException, InterruptedException {
        return getLiveStatistics(liveId, Options.defaults());
    }
    /** Read retained reference and selected translations. */
    public Response getLiveSettings(String liveId, Options options) throws IOException, InterruptedException {
        return call("getLiveSettings", Map.of("live_id", liveId), options);
    }
    public Response getLiveSettings(String liveId) throws IOException, InterruptedException {
        return getLiveSettings(liveId, Options.defaults());
    }
    /** Update retained reference and translations. */
    public Response updateLiveSettings(String liveId, Options options) throws IOException, InterruptedException {
        return call("updateLiveSettings", Map.of("live_id", liveId), options);
    }
    public Response updateLiveSettings(String liveId) throws IOException, InterruptedException {
        return updateLiveSettings(liveId, Options.defaults());
    }
    /** Read retained scripture stack. */
    public Response getLiveStack(String liveId, Options options) throws IOException, InterruptedException {
        return call("getLiveStack", Map.of("live_id", liveId), options);
    }
    public Response getLiveStack(String liveId) throws IOException, InterruptedException {
        return getLiveStack(liveId, Options.defaults());
    }
    /** Push retained scripture with optional selected translations. */
    public Response pushLiveStack(String liveId, Options options) throws IOException, InterruptedException {
        return call("pushLiveStack", Map.of("live_id", liveId), options);
    }
    public Response pushLiveStack(String liveId) throws IOException, InterruptedException {
        return pushLiveStack(liveId, Options.defaults());
    }
    /** Clear the retained scripture stack. */
    public Response clearLiveStack(String liveId, Options options) throws IOException, InterruptedException {
        return call("clearLiveStack", Map.of("live_id", liveId), options);
    }
    public Response clearLiveStack(String liveId) throws IOException, InterruptedException {
        return clearLiveStack(liveId, Options.defaults());
    }
    /** Remove a positive count from the retained stack tail. */
    public Response popLiveStack(String liveId, Options options) throws IOException, InterruptedException {
        return call("popLiveStack", Map.of("live_id", liveId), options);
    }
    public Response popLiveStack(String liveId) throws IOException, InterruptedException {
        return popLiveStack(liveId, Options.defaults());
    }
    /** Create a one-time audience secret. */
    public Response createLiveAudienceSecret(String liveId, Options options) throws IOException, InterruptedException {
        return call("createLiveAudienceSecret", Map.of("live_id", liveId), options);
    }
    public Response createLiveAudienceSecret(String liveId) throws IOException, InterruptedException {
        return createLiveAudienceSecret(liveId, Options.defaults());
    }
    /** Rotate a one-time audience secret. */
    public Response rotateLiveAudienceSecret(String liveId, Options options) throws IOException, InterruptedException {
        return call("rotateLiveAudienceSecret", Map.of("live_id", liveId), options);
    }
    public Response rotateLiveAudienceSecret(String liveId) throws IOException, InterruptedException {
        return rotateLiveAudienceSecret(liveId, Options.defaults());
    }
    /** Remove audience-secret protection. */
    public Response deleteLiveAudienceSecret(String liveId, Options options) throws IOException, InterruptedException {
        return call("deleteLiveAudienceSecret", Map.of("live_id", liveId), options);
    }
    public Response deleteLiveAudienceSecret(String liveId) throws IOException, InterruptedException {
        return deleteLiveAudienceSecret(liveId, Options.defaults());
    }
}
