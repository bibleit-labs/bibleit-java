package app.bibleit;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

/**
 * Broadcast control-plane SSH commands. No command opens capture or grants admission.
 * Callers negotiate broadcast.read.v1, broadcast.write.v1 and broadcast.lifecycle.v1.
 * Upload commands require one caller-owned persistent shell without a PTY.
 * Invalid identifiers, byte bounds and revisions fail before transport execution.
 */
public final class BroadcastCommands {
    private BroadcastCommands() {}
    /** Setup mutation bound into a signed, server-issued write ticket. */
    public enum WriteOperation {
        /** Creates a Setup. */ CREATE,
        /** Replaces a Setup at an expected revision. */ SAVE,
        /** Copies a Setup at an expected revision. */ DUPLICATE,
        /** Deletes a Setup at an expected revision. */ DELETE
    }
    /** Browser-backed event transitions, each requiring a durable request identifier. */
    public enum EventAction {
        /** Requests testing for an existing browser publisher. */ TEST,
        /** Requests live state for an existing browser publisher. */ GO_LIVE,
        /** Requests completion. */ END
    }
    private static String uuid(String value) {
        if (value == null || !value.matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"))
            throw new IllegalArgumentException("Canonical workspace or resource UUID required");
        return value;
    }
    private static long revision(long value) {
        if (value < 1) throw new IllegalArgumentException("Positive revision required");
        return value;
    }
    private static String ticket(String value) {
        if (value == null || value.isEmpty() || value.getBytes(StandardCharsets.UTF_8).length > 768 ||
            value.chars().anyMatch(c -> c <= 32 || c == 127 || c == '"'))
            throw new IllegalArgumentException("Bounded single-token server ticket required");
        return value;
    }
    private static Command wire(String organization, String suffix) {
        return new Command("BROADCAST " + uuid(organization) + " " + suffix, false);
    }
    /**
     * Builds the profiles command. Takeover requires browser interaction.
     * @param organization the canonical workspace UUID
     * @return the validated command; explicit wire values may contain private tickets
     * @throws IllegalArgumentException if an argument violates the wire contract
     */
    public static Command profiles(String organization) {
        return wire(organization, "PROFILES");
    }

    /**
     * Builds the channel command. Takeover requires browser interaction.
     * @param organization the canonical workspace UUID
     * @return the validated command; explicit wire values may contain private tickets
     * @throws IllegalArgumentException if an argument violates the wire contract
     */
    public static Command channel(String organization) {
        return wire(organization, "CHANNEL");
    }

    /**
     * Builds the preflight command. Takeover requires browser interaction.
     * @param organization the canonical workspace UUID
     * @return the validated command; explicit wire values may contain private tickets
     * @throws IllegalArgumentException if an argument violates the wire contract
     */
    public static Command preflight(String organization) {
        return wire(organization, "PREFLIGHT");
    }

    /**
     * Builds the events command. Takeover requires browser interaction.
     * @param organization the canonical workspace UUID
     * @return the validated command; explicit wire values may contain private tickets
     * @throws IllegalArgumentException if an argument violates the wire contract
     */
    public static Command events(String organization) {
        return wire(organization, "EVENTS");
    }

    /**
     * Builds the publishers command. Takeover requires browser interaction.
     * @param organization the canonical workspace UUID
     * @return the validated command; explicit wire values may contain private tickets
     * @throws IllegalArgumentException if an argument violates the wire contract
     */
    public static Command publishers(String organization) {
        return wire(organization, "PUBLISHERS");
    }

    /**
     * Builds the takeover command. Takeover requires browser interaction.
     * @param organization the canonical workspace UUID
     * @return the validated command; explicit wire values may contain private tickets
     * @throws IllegalArgumentException if an argument violates the wire contract
     */
    public static Command takeover(String organization) {
        return wire(organization, "TAKEOVER");
    }

    /**
     * Lists a bounded page of saved Setups.
     * @param organization the canonical workspace UUID
     * @param limit the page limit from 1 through 99
     * @param after the canonical cursor UUID, or empty for the first page
     * @return the validated command; explicit wire values may contain private tickets
     * @throws IllegalArgumentException if an argument violates the wire contract
     */
    public static Command listSetups(String organization, int limit, String after) {
        if (limit < 1 || limit > 99) throw new IllegalArgumentException("Page limit must be 1 through 99");
        Objects.requireNonNull(after, "after");
        return wire(organization, "SETUP LIST " + limit + (after.isEmpty() ? "" : " " + uuid(after)));
    }

    /**
     * Reads Setup export metadata.
     * @param organization the canonical workspace UUID
     * @param id the canonical resource UUID
     * @return the validated command; explicit wire values may contain private tickets
     * @throws IllegalArgumentException if an argument violates the wire contract
     */
    public static Command getSetup(String organization, String id) {
        return wire(organization, "SETUP " + uuid(id) + " GET");
    }

    /**
     * Reads a revision-bound Setup document chunk.
     * @param organization the canonical workspace UUID
     * @param id the canonical resource UUID
     * @param expectedRevision the positive expected resource revision
     * @param offset the contiguous raw byte offset
     * @return the validated command; explicit wire values may contain private tickets
     * @throws IllegalArgumentException if an argument violates the wire contract
     */
    public static Command setupDocument(String organization, String id, long expectedRevision, long offset) {
        if (offset < 0 || offset > 1048576) throw new IllegalArgumentException("Bounded offset required");
        return wire(organization, "SETUP " + uuid(id) + " DOCUMENT " + revision(expectedRevision) + " " + offset);
    }

    /**
     * Requests a signed immutable intent without allocating or mutating a Setup.
     * @param organization the canonical workspace UUID
     * @param operation the Setup write operation
     * @param target the canonical Setup UUID, or new for create
     * @param expectedRevision the positive expected resource revision
     * @param payloadBytes the exact JSON byte length
     * @param sha256 the lowercase SHA-256 of the exact payload bytes
     * @return the validated command; explicit wire values may contain private tickets
     * @throws IllegalArgumentException if an argument violates the wire contract
     */
    public static Command writeIntent(String organization, WriteOperation operation, String target, long expectedRevision, int payloadBytes, String sha256) {
        Objects.requireNonNull(operation, "operation");
        if (payloadBytes < 2 || payloadBytes > 1048576 || sha256 == null || !sha256.matches("[a-f0-9]{64}")) throw new IllegalArgumentException("Bounded payload and lowercase SHA-256 required");
        if (operation == WriteOperation.CREATE) {
            if (!"new".equals(target) || expectedRevision != 0) throw new IllegalArgumentException("Create requires new and revision zero");
        } else { uuid(target); revision(expectedRevision); }
        return wire(organization, "WRITE INTENT " + operation.name() + " " + target + " " + expectedRevision + " " + payloadBytes + " " + sha256);
    }

    /**
     * Builds a signed-ticket begin request. Never replace an uncertain ticket automatically.
     * @param organization the canonical workspace UUID
     * @param requestTicket the original signed server-issued write ticket
     * @return the validated command; explicit wire values may contain private tickets
     * @throws IllegalArgumentException if an argument violates the wire contract
     */
    public static Command beginWrite(String organization, String requestTicket) {
        return wire(organization, "WRITE BEGIN " + ticket(requestTicket));
    }

    /**
     * Builds a signed-ticket status request. Never replace an uncertain ticket automatically.
     * @param organization the canonical workspace UUID
     * @param requestTicket the original signed server-issued write ticket
     * @return the validated command; explicit wire values may contain private tickets
     * @throws IllegalArgumentException if an argument violates the wire contract
     */
    public static Command writeStatus(String organization, String requestTicket) {
        return wire(organization, "WRITE STATUS " + ticket(requestTicket));
    }

    /**
     * Builds the bounded upload inspect request.
     * @param organization the canonical workspace UUID
     * @param transferId the canonical upload transfer UUID
     * @return the validated command; explicit wire values may contain private tickets
     * @throws IllegalArgumentException if an argument violates the wire contract
     */
    public static Command inspectWrite(String organization, String transferId) {
        return wire(organization, "WRITE INSPECT " + uuid(transferId));
    }

    /**
     * Builds the bounded upload abort request.
     * @param organization the canonical workspace UUID
     * @param transferId the canonical upload transfer UUID
     * @return the validated command; explicit wire values may contain private tickets
     * @throws IllegalArgumentException if an argument violates the wire contract
     */
    public static Command abortWrite(String organization, String transferId) {
        return wire(organization, "WRITE ABORT " + uuid(transferId));
    }

    /**
     * Uploads at most 3072 raw bytes at a contiguous offset.
     * @param organization the canonical workspace UUID
     * @param transferId the canonical upload transfer UUID
     * @param offset the contiguous raw byte offset
     * @param data the raw upload chunk, copied into base64
     * @return the validated command; explicit wire values may contain private tickets
     * @throws IllegalArgumentException if an argument violates the wire contract
     */
    public static Command writeChunk(String organization, String transferId, long offset, byte[] data) {
        Objects.requireNonNull(data, "data");
        if (data.length < 1 || data.length > 3072 || offset < 0 || offset > 1048576 || offset + data.length > 1048576) throw new IllegalArgumentException("Bounded contiguous chunk required");
        return wire(organization, "WRITE CHUNK " + uuid(transferId) + " " + offset + " " + Base64.getEncoder().encodeToString(data));
    }

    /**
     * Commits the original ticket and upload. On lost replies, query this same ticket.
     * @param organization the canonical workspace UUID
     * @param requestTicket the original signed server-issued write ticket
     * @param transferId the canonical upload transfer UUID
     * @return the validated command; explicit wire values may contain private tickets
     * @throws IllegalArgumentException if an argument violates the wire contract
     */
    public static Command commitWrite(String organization, String requestTicket, String transferId) {
        return wire(organization, "WRITE COMMIT " + ticket(requestTicket) + " " + uuid(transferId));
    }

    /**
     * Reads the current event projection.
     * @param organization the canonical workspace UUID
     * @param id the canonical resource UUID
     * @return the validated command; explicit wire values may contain private tickets
     * @throws IllegalArgumentException if an argument violates the wire contract
     */
    public static Command getEvent(String organization, String id) {
        return wire(organization, "EVENT " + uuid(id) + " GET");
    }

    /**
     * Reads the current operation projection.
     * @param organization the canonical workspace UUID
     * @param id the canonical resource UUID
     * @return the validated command; explicit wire values may contain private tickets
     * @throws IllegalArgumentException if an argument violates the wire contract
     */
    public static Command getOperation(String organization, String id) {
        return wire(organization, "OPERATION " + uuid(id) + " GET");
    }

    /**
     * Reads the current publisher projection.
     * @param organization the canonical workspace UUID
     * @param id the canonical resource UUID
     * @return the validated command; explicit wire values may contain private tickets
     * @throws IllegalArgumentException if an argument violates the wire contract
     */
    public static Command getPublisher(String organization, String id) {
        return wire(organization, "PUBLISHER " + uuid(id) + " GET");
    }

    /**
     * Enqueues creation using exact bounded JSON metadata and a durable request identifier.
     * @param organization the canonical workspace UUID
     * @param requestId the original canonical durable request UUID
     * @param metadata the exact UTF-8 JSON event metadata bytes
     * @return the validated command; explicit wire values may contain private tickets
     * @throws IllegalArgumentException if an argument violates the wire contract
     */
    public static Command createEvent(String organization, String requestId, byte[] metadata) {
        Objects.requireNonNull(metadata, "metadata");
        if (metadata.length < 1 || metadata.length > 3072) throw new IllegalArgumentException("Event metadata must be 1 through 3072 bytes");
        return wire(organization, "EVENT CREATE " + uuid(requestId) + " " + Base64.getEncoder().encodeToString(metadata));
    }

    /**
     * Enqueues attaching an existing provider event.
     * @param organization the canonical workspace UUID
     * @param requestId the original canonical durable request UUID
     * @param providerId the YouTube broadcast identifier
     * @return the validated command; explicit wire values may contain private tickets
     * @throws IllegalArgumentException if an argument violates the wire contract
     */
    public static Command attachEvent(String organization, String requestId, String providerId) {
        if (providerId == null || !providerId.matches("[A-Za-z0-9_-]{1,128}")) throw new IllegalArgumentException("Invalid provider event identifier");
        return wire(organization, "EVENT ATTACH " + uuid(requestId) + " " + providerId);
    }

    /**
     * Enqueues a revision-aware transition; acknowledgement is not provider completion.
     * @param organization the canonical workspace UUID
     * @param id the canonical resource UUID
     * @param action the browser-backed event transition
     * @param expectedRevision the positive expected resource revision
     * @param requestId the original canonical durable request UUID
     * @return the validated command; explicit wire values may contain private tickets
     * @throws IllegalArgumentException if an argument violates the wire contract
     */
    public static Command transitionEvent(String organization, String id, EventAction action, long expectedRevision, String requestId) {
        Objects.requireNonNull(action, "action");
        String keyword = action == EventAction.GO_LIVE ? "GO-LIVE" : action.name();
        return wire(organization, "EVENT " + uuid(id) + " " + keyword + " " + revision(expectedRevision) + " " + uuid(requestId));
    }

    /**
     * Requests publisher stop; poll status to verify worker exit and cleanup.
     * @param organization the canonical workspace UUID
     * @param id the canonical resource UUID
     * @param expectedRevision the positive expected resource revision
     * @return the validated command; explicit wire values may contain private tickets
     * @throws IllegalArgumentException if an argument violates the wire contract
     */
    public static Command stopPublisher(String organization, String id, long expectedRevision) {
        return wire(organization, "PUBLISHER " + uuid(id) + " STOP " + revision(expectedRevision));
    }
}
