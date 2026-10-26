package app.bibleit;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.*;
import java.util.*;

/** Strict finite protocol v1 response decoding. Never evaluates opaque compound terms. */
public final class Protocol {
    private Protocol() {}
    private static final class IncompleteTerm extends IOException {}
    public static ProtocolRecord parseRecord(String line) throws IOException {
        if (line.indexOf(0) >= 0) throw new IOException("Invalid protocol record");
        var words = new ArrayList<String>(); var word = new StringBuilder();
        var compound = new ArrayDeque<Character>();
        boolean quoted = false, active = false, escaped = false, termEscaped = false;
        char termQuote = 0;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (!compound.isEmpty()) {
                word.append(c);
                if (termEscaped) { termEscaped = false; continue; }
                if (termQuote != 0) {
                    if (c == '\\') termEscaped = true;
                    else if (c == termQuote) termQuote = 0;
                    continue;
                }
                if (c == '"' || c == '\'') termQuote = c;
                else if (c == '{' || c == '[' || c == '(') compound.push(close(c));
                else if (c == '}' || c == ']' || c == ')') {
                    if (compound.pop() != c) throw new IOException("Mismatched protocol term");
                }
                continue;
            }
            if (c == '\r' || c == '\n') throw new IOException("Newline outside protocol term");
            if (escaped) {
                switch (c) {
                    case '\\', '"' -> word.append(c);
                    case 'n' -> word.append('\n');
                    case 'r' -> word.append('\r');
                    case 't' -> word.append('\t');
                    case 'u' -> {
                        if (i + 4 >= line.length()) throw new IOException("Truncated Unicode escape");
                        String digits = line.substring(i + 1, i + 5);
                        if (!digits.matches("[0-9a-fA-F]{4}")) throw new IOException("Invalid Unicode escape");
                        char scalar = (char) Integer.parseInt(digits, 16);
                        if (Character.isSurrogate(scalar)) throw new IOException("Invalid Unicode scalar");
                        word.append(scalar); i += 4;
                    }
                    default -> throw new IOException("Invalid protocol escape");
                }
                escaped = false;
            } else if (c == '\\' && quoted) { escaped = true; active = true; }
            else if (c == '"') { quoted = !quoted; active = true; }
            else if (!quoted && (c == '{' || c == '[' || c == '(') && word.indexOf("=") >= 0) {
                compound.push(close(c)); word.append(c); active = true;
            } else if (!quoted && (c == ' ' || c == '\t')) {
                if (active) { words.add(word.toString()); word.setLength(0); active = false; }
            } else { word.append(c); active = true; }
        }
        if (!compound.isEmpty()) throw new IncompleteTerm();
        if (quoted || escaped) throw new IOException("Unterminated protocol string");
        if (active) words.add(word.toString());
        if (words.isEmpty()) throw new IOException("Empty protocol record");
        var fields = new LinkedHashMap<String, String>();
        for (String part : words.subList(1, words.size())) {
            int separator = part.indexOf('=');
            if (separator < 0) continue;
            if (separator == 0) throw new IOException("Empty protocol field");
            String key = part.substring(0, separator), value = part.substring(separator + 1);
            String previous = fields.putIfAbsent(key, value);
            if (previous != null && !previous.equals(value)) throw new IOException("Conflicting protocol field");
        }
        return new ProtocolRecord(words.getFirst().toLowerCase(Locale.ROOT), fields, new ArrayList<>(fields.keySet()));
    }
    private static char close(char open) { return switch (open) { case '{' -> '}'; case '[' -> ']'; default -> ')'; }; }
    public static CommandResponse decodeResponse(InputStream input) throws IOException { return decode(input, false); }
    public static CommandResponse decodeCommandResponse(Command command, InputStream input) throws IOException {
        if (command == null || command.streaming()) throw new IllegalArgumentException("Finite command required");
        return decode(input, command.wire().startsWith("ACCOUNT TRANSLATION "));
    }
    private static CommandResponse decode(InputStream input, boolean singleRecord) throws IOException {
        byte[] bytes = input.readNBytes(2 * 1024 * 1024 + 1);
        if (bytes.length > 2 * 1024 * 1024) throw new IOException("Protocol response exceeds 2 MiB");
        if (bytes.length == 0 || bytes[bytes.length - 1] != '\n') throw new EOFException("Missing final newline");
        String body = StandardCharsets.UTF_8.newDecoder().onMalformedInput(CodingErrorAction.REPORT).decode(ByteBuffer.wrap(bytes)).toString();
        var lines = new ArrayList<String>(); var records = new ArrayList<ProtocolRecord>();
        boolean ended = false; ServerException error = null; String pending = "";
        for (String physical : body.substring(0, body.length() - 1).split("\n", -1)) {
            String line = physical.endsWith("\r") ? physical.substring(0, physical.length() - 1) : physical;
            if (ended || error != null) throw new IOException("Unexpected trailing data");
            if (line.equals("END") && pending.isEmpty()) {
                if (records.isEmpty()) throw new IOException("Unexpected END");
                ended = true; continue;
            }
            line = pending.isEmpty() ? line : pending + "\n" + line;
            ProtocolRecord record;
            try { record = parseRecord(line); }
            catch (IncompleteTerm e) { pending = line; continue; }
            pending = "";
            if (records.isEmpty()) {
                if (record.type().equals("err")) {
                    String[] words = line.split("\\s+");
                    if (words.length < 2 || words[1].contains("=")) throw new IOException("Missing error code");
                    error = new ServerException(words[1], record.fields()); continue;
                }
                if (!record.type().equals("ok")) throw new IOException("Expected OK or ERR");
            } else if (Set.of("ok", "err", "event").contains(record.type())) throw new IOException("Unexpected envelope");
            records.add(record); lines.add(line);
        }
        if (!pending.isEmpty()) throw new EOFException("Incomplete protocol term");
        if (error != null) throw error;
        if (records.isEmpty() || !ended && (records.size() > 1 || !singleRecord && multiRecord(records.getFirst()))) throw new EOFException("Missing END");
        return new CommandResponse(lines, records);
    }
    private static boolean multiRecord(ProtocolRecord header) {
        return Set.of("count", "verses", "results", "books", "commands", "connections").stream().anyMatch(header.fields()::containsKey)
            || header.fields().getOrDefault("event", "").equals("verse") && header.fields().containsKey("translations");
    }
}
