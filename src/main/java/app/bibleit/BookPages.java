package app.bibleit;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.*;

/** Lazy book traversal. Call nextPage until it returns null. A failed traversal cannot be resumed. */
public final class BookPages {
    private final BibleitClient client;
    private final String operation;
    private final Map<String, String> parameters;
    private Options options;
    private final Set<String> seen = new HashSet<>();
    private final int limit;
    private boolean finished;
    private String translation;
    private JsonNode rights;
    private int book, chapter, verse;
    BookPages(BibleitClient client, String operation, Map<String, String> parameters, Options options) {
        this.client = client; this.operation = operation; this.parameters = Map.copyOf(parameters);
        var limits = options.query.getOrDefault("limit", List.of("50"));
        if (limits.size() != 1 || !limits.getFirst().matches("[0-9]{1,3}")) throw new IllegalArgumentException("Invalid book page limit");
        limit = Integer.parseInt(limits.getFirst());
        if (limit < 1 || limit > 500) throw new IllegalArgumentException("Book page limit must be 1..500");
        var after = options.query.get("after");
        if (after != null) {
            if (after.size() != 1 || after.getFirst().isEmpty()) throw new IllegalArgumentException("Invalid cursor");
            seen.add(after.getFirst());
        }
        this.options = options.toBuilder().query("limit", Integer.toString(limit)).build();
    }
    public JsonNode nextPage() throws IOException, InterruptedException {
        if (finished) return null;
        try {
            if (Thread.currentThread().isInterrupted()) throw new InterruptedException("Book traversal interrupted");
            JsonNode page;
            try (var response = client.call(operation, parameters, options)) { page = response.decode(); }
            var next = page.get("next_cursor"); var verses = page.path("verses");
            if (!page.path("translation").isTextual() || page.path("translation").asText().isEmpty()
                || !page.path("rights").isObject() || !verses.isArray() || verses.size() > limit || next == null
                || !(next.isNull() || next.isTextual() && !next.asText().isEmpty())
                || !next.isNull() && (verses.isEmpty() || !seen.add(next.asText()))) throw invalid();
            if (translation == null) { translation = page.path("translation").asText(); rights = page.get("rights").deepCopy(); }
            else if (!translation.equals(page.path("translation").asText()) || !rights.equals(page.get("rights"))) throw invalid();
            for (var v : verses) {
                if (!v.path("book_id").canConvertToInt() || !v.path("chapter").canConvertToInt() || !v.path("verse").canConvertToInt()
                    || !v.path("book_id").isIntegralNumber() || !v.path("chapter").isIntegralNumber() || !v.path("verse").isIntegralNumber()) throw invalid();
                int b = v.path("book_id").asInt(), c = v.path("chapter").asInt(), n = v.path("verse").asInt();
                if (b < 1 || b > 255 || c < 1 || c > 152 || n < 1 || n > 255
                    || book != 0 && (b != book || c < chapter || c == chapter && n <= verse)) throw invalid();
                book = b; chapter = c; verse = n;
            }
            if (next.isNull()) finished = true;
            else options = options.toBuilder().query("after", next.asText()).build();
            return page;
        } catch (IOException | InterruptedException | RuntimeException e) { finished = true; throw e; }
    }
    private static IOException invalid() { return new IOException("Invalid or inconsistent REST book page"); }
}
