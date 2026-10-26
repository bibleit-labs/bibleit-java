package app.bibleit;

import java.util.List;

/**
 * A finite SSH response retaining original logical lines, excluding END.
 * @param lines the lines
 * @param records the records
 */
public record CommandResponse(List<String> lines, List<ProtocolRecord> records) {
    /**
     * Copies and validates the command response components.
     * @param lines the lines
     * @param records the records
     */
    public CommandResponse { lines = List.copyOf(lines); records = List.copyOf(records); }
    @Override public String toString() { return "CommandResponse[redacted]"; }
}
