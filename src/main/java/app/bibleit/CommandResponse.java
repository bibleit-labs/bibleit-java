package app.bibleit;

import java.util.List;

/** A finite SSH response retaining original logical lines, excluding END. */
public record CommandResponse(List<String> lines, List<ProtocolRecord> records) {
    public CommandResponse { lines = List.copyOf(lines); records = List.copyOf(records); }
    @Override public String toString() { return "CommandResponse[redacted]"; }
}
