package app.bibleit;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Typed broadcast SSH operations over one caller-owned connection. The application
 * negotiates server capabilities and retains original write tickets and payloads.
 * No method retries, opens capture, performs Google consent or restarts publishing.
 */
public final class BroadcastSshClient {
    private final SshClient client;
    /**
     * Binds helpers to the existing caller-owned SSH executor.
     * @param client the finite SSH command client; upload calls must use the same persistent shell
     */
    public BroadcastSshClient(SshClient client) { this.client = Objects.requireNonNull(client); }
    /**
     * Typed projection retaining the original finite response and future fields.
     * @param <T> the contract model type
     * @param value the projection, possibly null for a disconnected channel
     * @param raw the original finite response
     */
    public record Result<T>(T value, CommandResponse raw) {
        @Override public String toString() { return "BroadcastResult[redacted]"; }
    }
    private static Map<String,Object> scalarFields(Map<String,String> fields) throws IOException {
        var result = new LinkedHashMap<String,Object>();
        for (var entry : fields.entrySet()) {
            String key = entry.getKey(), value = entry.getValue();
            if (List.of("revision","deleted_revision","expires_at","chunk_bytes","payload_bytes","expected_revision","count","limit","width","height","fps","sample_rate","sampleRate","version","channels","video_bitrate","audio_bitrate").contains(key)) {
                result.put(key,SshDecoders.number(fields,key));
            } else if (List.of("terminal","applied","replay","eof").contains(key)) {
                result.put(key,SshDecoders.bool(fields,key));
            } else result.put(key,value);
        }
        return result;
    }
    private static <T> T convert(Object value,Class<T> type) throws IOException {
        try { return Json.MAPPER.convertValue(value,type); }
        catch (IllegalArgumentException error) { throw new IOException("Invalid broadcast SSH model"); }
    }
    private static <T> T lifecycle(Map<String,String> fields,String kind,Class<T> type) throws IOException {
        if (!"broadcast-lifecycle-json-v1".equals(fields.get("document_format")) || !kind.equals(fields.get("record_type"))) throw SshDecoders.invalid();
        long size = SshDecoders.number(fields,"bytes");
        String encoded = SshDecoders.required(fields,"data_base64");
        if (size > 1048576 || encoded.length() > 1398104) throw SshDecoders.invalid();
        byte[] bytes;
        try { bytes = Base64.getDecoder().decode(encoded); }
        catch (IllegalArgumentException error) { throw SshDecoders.invalid(); }
        if (bytes.length != size || !Base64.getEncoder().encodeToString(bytes).equals(encoded)) throw SshDecoders.invalid();
        var node = Json.MAPPER.readTree(bytes);
        if (node == null || !node.isObject() || !node.has("value")) throw SshDecoders.invalid();
        if (node.get("value").isNull()) {
            if (!kind.equals("channel")) throw SshDecoders.invalid();
            return null;
        }
        if (!node.get("value").isObject()) throw SshDecoders.invalid();
        return convert(node.get("value"),type);
    }
    private <T> Result<T> one(Command command,String kind,Class<T> type) throws IOException,InterruptedException {
        var raw = client.execute(command);
        return new Result<>(lifecycle(SshDecoders.single(raw),kind,type),raw);
    }
    private <T> Result<List<T>> many(Command command,String kind,String row,Class<T> type) throws IOException,InterruptedException {
        var raw = client.execute(command); SshDecoders.counted(raw,"count",row);
        var values = new ArrayList<T>();
        for (var record : raw.records().subList(1,raw.records().size())) values.add(lifecycle(record.fields(),kind,type));
        return new Result<>(List.copyOf(values),raw);
    }
    private <T> Result<T> fields(Command command,Class<T> type) throws IOException,InterruptedException {
        var raw = client.execute(command);
        return new Result<>(convert(scalarFields(SshDecoders.single(raw)),type),raw);
    }
    /**
     * Reads the current channel projection.
     * @param organization the organization value required by the broadcast contract
     * @return the typed projection retaining the original response
     * @throws IOException if execution or bounded response decoding fails
     * @throws InterruptedException if execution is interrupted
     */
    public Result<Models.BroadcastChannel> channel(String organization) throws IOException,InterruptedException {
        return one(BroadcastCommands.channel(organization),"channel",Models.BroadcastChannel.class);
    }

    /**
     * Reads the current preflight projection.
     * @param organization the organization value required by the broadcast contract
     * @return the typed projection retaining the original response
     * @throws IOException if execution or bounded response decoding fails
     * @throws InterruptedException if execution is interrupted
     */
    public Result<Models.BroadcastPreflight> preflight(String organization) throws IOException,InterruptedException {
        return one(BroadcastCommands.preflight(organization),"preflight",Models.BroadcastPreflight.class);
    }

    /**
     * Lists current events.
     * @param organization the organization value required by the broadcast contract
     * @return the typed projection retaining the original response
     * @throws IOException if execution or bounded response decoding fails
     * @throws InterruptedException if execution is interrupted
     */
    public Result<List<Models.BroadcastEvent>> events(String organization) throws IOException,InterruptedException {
        return many(BroadcastCommands.events(organization),"event","youtube_event",Models.BroadcastEvent.class);
    }

    /**
     * Lists current publishers.
     * @param organization the organization value required by the broadcast contract
     * @return the typed projection retaining the original response
     * @throws IOException if execution or bounded response decoding fails
     * @throws InterruptedException if execution is interrupted
     */
    public Result<List<Models.BroadcastPublisher>> publishers(String organization) throws IOException,InterruptedException {
        return many(BroadcastCommands.publishers(organization),"publisher","publisher",Models.BroadcastPublisher.class);
    }

    /**
     * Reads one event.
     * @param organization the organization value required by the broadcast contract
     * @param id the id value required by the broadcast contract
     * @return the typed projection retaining the original response
     * @throws IOException if execution or bounded response decoding fails
     * @throws InterruptedException if execution is interrupted
     */
    public Result<Models.BroadcastEvent> getEvent(String organization, String id) throws IOException,InterruptedException {
        return one(BroadcastCommands.getEvent(organization,id),"event",Models.BroadcastEvent.class);
    }

    /**
     * Reads one operation.
     * @param organization the organization value required by the broadcast contract
     * @param id the id value required by the broadcast contract
     * @return the typed projection retaining the original response
     * @throws IOException if execution or bounded response decoding fails
     * @throws InterruptedException if execution is interrupted
     */
    public Result<Models.BroadcastOperation> getOperation(String organization, String id) throws IOException,InterruptedException {
        return one(BroadcastCommands.getOperation(organization,id),"operation",Models.BroadcastOperation.class);
    }

    /**
     * Reads one publisher.
     * @param organization the organization value required by the broadcast contract
     * @param id the id value required by the broadcast contract
     * @return the typed projection retaining the original response
     * @throws IOException if execution or bounded response decoding fails
     * @throws InterruptedException if execution is interrupted
     */
    public Result<Models.BroadcastPublisher> getPublisher(String organization, String id) throws IOException,InterruptedException {
        return one(BroadcastCommands.getPublisher(organization,id),"publisher",Models.BroadcastPublisher.class);
    }

    /**
     * Requests a durable control operation; poll its resulting state to verify completion.
     * @param organization the organization value required by the broadcast contract
     * @param requestId the requestId value required by the broadcast contract
     * @param metadata the metadata value required by the broadcast contract
     * @return the typed projection retaining the original response
     * @throws IOException if execution or bounded response decoding fails
     * @throws InterruptedException if execution is interrupted
     */
    public Result<Models.BroadcastOperation> createEvent(String organization, String requestId, byte[] metadata) throws IOException,InterruptedException {
        return one(BroadcastCommands.createEvent(organization,requestId,metadata),"operation",Models.BroadcastOperation.class);
    }

    /**
     * Requests a durable control operation; poll its resulting state to verify completion.
     * @param organization the organization value required by the broadcast contract
     * @param requestId the requestId value required by the broadcast contract
     * @param providerId the providerId value required by the broadcast contract
     * @return the typed projection retaining the original response
     * @throws IOException if execution or bounded response decoding fails
     * @throws InterruptedException if execution is interrupted
     */
    public Result<Models.BroadcastOperation> attachEvent(String organization, String requestId, String providerId) throws IOException,InterruptedException {
        return one(BroadcastCommands.attachEvent(organization,requestId,providerId),"operation",Models.BroadcastOperation.class);
    }

    /**
     * Requests a durable control operation; poll its resulting state to verify completion.
     * @param organization the organization value required by the broadcast contract
     * @param id the id value required by the broadcast contract
     * @param action the action value required by the broadcast contract
     * @param expectedRevision the expectedRevision value required by the broadcast contract
     * @param requestId the requestId value required by the broadcast contract
     * @return the typed projection retaining the original response
     * @throws IOException if execution or bounded response decoding fails
     * @throws InterruptedException if execution is interrupted
     */
    public Result<Models.BroadcastOperation> transitionEvent(String organization, String id, BroadcastCommands.EventAction action, long expectedRevision, String requestId) throws IOException,InterruptedException {
        return one(BroadcastCommands.transitionEvent(organization,id,action,expectedRevision,requestId),"operation",Models.BroadcastOperation.class);
    }

    /**
     * Requests a durable control operation; poll its resulting state to verify completion.
     * @param organization the organization value required by the broadcast contract
     * @param id the id value required by the broadcast contract
     * @param expectedRevision the expectedRevision value required by the broadcast contract
     * @return the typed projection retaining the original response
     * @throws IOException if execution or bounded response decoding fails
     * @throws InterruptedException if execution is interrupted
     */
    public Result<Models.BroadcastPublisher> stopPublisher(String organization, String id, long expectedRevision) throws IOException,InterruptedException {
        return one(BroadcastCommands.stopPublisher(organization,id,expectedRevision),"publisher",Models.BroadcastPublisher.class);
    }

    /**
     * Requests a signed immutable intent without allocating upload storage.
     * @param organization the organization value required by the broadcast contract
     * @param operation the operation value required by the broadcast contract
     * @param target the target value required by the broadcast contract
     * @param expectedRevision the expectedRevision value required by the broadcast contract
     * @param payloadBytes the payloadBytes value required by the broadcast contract
     * @param sha256 the sha256 value required by the broadcast contract
     * @return the typed projection retaining the original response
     * @throws IOException if execution or bounded response decoding fails
     * @throws InterruptedException if execution is interrupted
     */
    public Result<Models.BroadcastWriteIntent> writeIntent(String organization, BroadcastCommands.WriteOperation operation, String target, long expectedRevision, int payloadBytes, String sha256) throws IOException,InterruptedException {
        return fields(BroadcastCommands.writeIntent(organization,operation,target,expectedRevision,payloadBytes,sha256),Models.BroadcastWriteIntent.class);
    }

    /**
     * Reconciles the original ticket; an uncertain outcome never creates a replacement.
     * @param organization the organization value required by the broadcast contract
     * @param requestTicket the requestTicket value required by the broadcast contract
     * @return the typed projection retaining the original response
     * @throws IOException if execution or bounded response decoding fails
     * @throws InterruptedException if execution is interrupted
     */
    public Result<Models.BroadcastWriteReceipt> writeStatus(String organization, String requestTicket) throws IOException,InterruptedException {
        return fields(BroadcastCommands.writeStatus(organization,requestTicket),Models.BroadcastWriteReceipt.class);
    }

    /**
     * Commits a retained ticket and upload on the same persistent shell.
     * @param organization the organization value required by the broadcast contract
     * @param requestTicket the requestTicket value required by the broadcast contract
     * @param transferId the transferId value required by the broadcast contract
     * @return the typed projection retaining the original response
     * @throws IOException if execution or bounded response decoding fails
     * @throws InterruptedException if execution is interrupted
     */
    public Result<Models.BroadcastWriteReceipt> commitWrite(String organization, String requestTicket, String transferId) throws IOException,InterruptedException {
        return fields(BroadcastCommands.commitWrite(organization,requestTicket,transferId),Models.BroadcastWriteReceipt.class);
    }

    /**
     * Requests takeover; authorized callers receive browser_required rather than a capture secret.
     * @param organization the organization value required by the broadcast contract
     * @return the typed projection retaining the original response
     * @throws IOException if execution or bounded response decoding fails
     * @throws InterruptedException if execution is interrupted
     */
    public CommandResponse takeover(String organization) throws IOException,InterruptedException {
        return client.execute(BroadcastCommands.takeover(organization));
    }
    /**
     * Lists revisioned Setup summaries using the shared REST model.
     * @param organization the workspace UUID
     * @param limit the page limit from 1 through 99
     * @param after the previous cursor or empty for the first page
     * @return a typed page retaining wire fields
     * @throws IOException if execution or decoding fails
     * @throws InterruptedException if execution is interrupted
     */
    public Result<Models.BroadcastSetupPage> listSetups(String organization,int limit,String after) throws IOException,InterruptedException {
        var raw = client.execute(BroadcastCommands.listSetups(organization,limit,after));
        var header = SshDecoders.counted(raw,"count","setup");
        var rows = new ArrayList<Models.BroadcastSetupSummary>();
        for (var record : raw.records().subList(1,raw.records().size())) rows.add(convert(scalarFields(record.fields()),Models.BroadcastSetupSummary.class));
        var page = new Models.BroadcastSetupPage(); page.setData(List.copyOf(rows));
        page.setNextCursor(header.containsKey("next") ? header.get("next") : null);
        return new Result<>(page,raw);
    }
    /**
     * Lists output profiles using the same technical model as REST preflight.
     * @param organization the workspace UUID
     * @return the typed profiles retaining the wire response
     * @throws IOException if execution or decoding fails
     * @throws InterruptedException if execution is interrupted
     */
    public Result<List<Models.BroadcastOutputProfile>> profiles(String organization) throws IOException,InterruptedException {
        var raw = client.execute(BroadcastCommands.profiles(organization));
        SshDecoders.counted(raw,"count","output_profile");
        var profiles = new ArrayList<Models.BroadcastOutputProfile>();
        for (var record : raw.records().subList(1,raw.records().size())) profiles.add(convert(scalarFields(record.fields()),Models.BroadcastOutputProfile.class));
        return new Result<>(List.copyOf(profiles),raw);
    }

    /**
     * Exports one revision-bound Setup with bounded sequential reads and hash verification.
     * No failed chunk is retried and no changed revision is silently accepted.
     * @param organization the canonical workspace UUID
     * @param id the canonical Setup UUID
     * @return the same Setup document model as REST, retaining initial export metadata
     * @throws IOException if execution, framing, identity, revision or checksum validation fails
     * @throws InterruptedException if execution is interrupted
     */
    public Result<Models.BroadcastSetup> getSetup(String organization,String id) throws IOException,InterruptedException {
        var raw = client.execute(BroadcastCommands.getSetup(organization,id));
        var meta = SshDecoders.single(raw);
        long revision = SshDecoders.positive(meta,"revision");
        long size = SshDecoders.number(meta,"document_bytes");
        String hash = SshDecoders.required(meta,"document_sha256");
        if (size < 2 || size > 1048576 || !hash.matches("[a-f0-9]{64}") ||
            SshDecoders.number(meta,"chunk_bytes") != 3072 ||
            !organization.equals(meta.get("organization_id")) || !id.equals(meta.get("id")) ||
            !"studio-setup-json-v1".equals(meta.get("document_format"))) throw SshDecoders.invalid();
        var bytes = new ByteArrayOutputStream((int)size);
        while (bytes.size() < size) {
            long offset = bytes.size();
            var chunk = SshDecoders.single(client.execute(BroadcastCommands.setupDocument(organization,id,revision,offset)));
            for (String key : List.of("organization_id","id","revision","document_format","document_bytes","document_sha256","chunk_bytes"))
                if (!Objects.equals(meta.get(key),chunk.get(key))) throw SshDecoders.invalid();
            if (SshDecoders.number(chunk,"offset") != offset) throw SshDecoders.invalid();
            String encoded = SshDecoders.required(chunk,"data_base64");
            if (encoded.length() > 4096) throw SshDecoders.invalid();
            byte[] data;
            try { data = Base64.getDecoder().decode(encoded); }
            catch (IllegalArgumentException error) { throw SshDecoders.invalid(); }
            long next = offset + data.length;
            if (data.length < 1 || data.length > 3072 || next > size ||
                !Base64.getEncoder().encodeToString(data).equals(encoded) ||
                SshDecoders.number(chunk,"bytes") != data.length ||
                SshDecoders.number(chunk,"next_offset") != next ||
                SshDecoders.bool(chunk,"eof") != (next == size)) throw SshDecoders.invalid();
            bytes.writeBytes(data);
        }
        byte[] document = bytes.toByteArray();
        try {
            if (!hash.equals(HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(document)))) throw SshDecoders.invalid();
        } catch (NoSuchAlgorithmException impossible) { throw new IOException("SHA-256 unavailable"); }
        var tree = Json.MAPPER.readTree(document);
        if (tree == null || !tree.isObject()) throw SshDecoders.invalid();
        var setup = new Models.BroadcastSetup(); setup.setId(id); setup.setOrganizationId(organization);
        setup.setRevision(revision); setup.setDocument(convert(tree,Models.BroadcastSetupDocument.class));
        return new Result<>(setup,raw);
    }

}
