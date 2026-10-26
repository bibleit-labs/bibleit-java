package app.bibleit;

/** Validated SSH wire command. wire() can contain secrets; toString() is redacted. */
public final class Command {
    private final String wire;
    private final boolean streaming;
    Command(String wire, boolean streaming) {
        if (wire.getBytes(java.nio.charset.StandardCharsets.UTF_8).length > 8192)
            throw new IllegalArgumentException("Command exceeds 8 KiB");
        this.wire = wire; this.streaming = streaming;
    }
    public String wire() { return wire; }
    public boolean streaming() { return streaming; }
    @Override public String toString() { return "Command[redacted]"; }
}
