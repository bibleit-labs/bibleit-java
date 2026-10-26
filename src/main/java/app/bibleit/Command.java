package app.bibleit;

/**
 * Validated SSH wire command. wire() can contain secrets; toString() is redacted.
 */
public final class Command {
    private final String wire;
    private final boolean streaming;
    Command(String wire, boolean streaming) {
        if (wire.getBytes(java.nio.charset.StandardCharsets.UTF_8).length > 8192)
            throw new IllegalArgumentException("Command exceeds 8 KiB");
        this.wire = wire; this.streaming = streaming;
    }
    /**
     * Returns the wire command explicitly; it may contain credentials.
     * @return the command text for explicit transport use
     */
    public String wire() { return wire; }
    /**
     * Reports whether this command requires a persistent streaming connection.
     * @return true for a streaming subscription command
     */
    public boolean streaming() { return streaming; }
    @Override public String toString() { return "Command[redacted]"; }
}
