package app.bibleit;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.*;
import java.util.*;

/** Lazy, repeatable whole-book traversal. Each iterator fetches and validates its own pages. */
public final class BookPages implements Iterable<JsonNode> {
    private final BibleitClient client;
    private final String operation;
    private final Map<String, String> parameters;
    private final Options snapshot;
    private final int limit;

    BookPages(BibleitClient client, String operation, Map<String, String> parameters, Options options) {
        this.client = client;
        this.operation = operation;
        this.parameters = Map.copyOf(parameters);
        var limits = options.query.getOrDefault("limit", List.of("50"));
        if (limits.size() != 1 || !limits.getFirst().matches("[0-9]{1,3}"))
            throw new IllegalArgumentException("Invalid book page limit");
        limit = Integer.parseInt(limits.getFirst());
        if (limit < 1 || limit > 500) throw new IllegalArgumentException("Book page limit must be 1..500");
        var after = options.query.get("after");
        if (after != null && (after.size() != 1 || after.getFirst().isEmpty()))
            throw new IllegalArgumentException("Invalid cursor");
        snapshot = options.toBuilder().query("limit", Integer.toString(limit)).build();
    }

    /** I/O failures throw UncheckedIOException; interruption restores the thread interrupt flag. */
    @Override public Iterator<JsonNode> iterator() { return new PageIterator(); }

    private final class PageIterator implements Iterator<JsonNode> {
        private final Set<String> seen = new HashSet<>();
        private Options options = snapshot;
        private boolean finished;
        private JsonNode next;
        private String translation;
        private JsonNode rights;
        private int book, chapter, verse;

        private PageIterator() {
            var after = snapshot.query.get("after");
            if (after != null) seen.add(after.getFirst());
        }

        @Override public boolean hasNext() {
            if (next != null) return true;
            if (finished) return false;
            try {
                next = fetch();
                return true;
            } catch (InterruptedException e) {
                finished = true;
                Thread.currentThread().interrupt();
                var failure = new InterruptedIOException("Book traversal interrupted");
                failure.initCause(e);
                throw new UncheckedIOException(failure);
            } catch (IOException e) {
                finished = true;
                throw new UncheckedIOException(e);
            } catch (RuntimeException e) {
                finished = true;
                throw e;
            }
        }

        @Override public JsonNode next() {
            if (!hasNext()) throw new NoSuchElementException("Book traversal complete");
            var page = next;
            next = null;
            return page;
        }

        private JsonNode fetch() throws IOException, InterruptedException {
            if (Thread.currentThread().isInterrupted()) throw new InterruptedException("Book traversal interrupted");
            JsonNode page;
            try (var response = client.call(operation, parameters, options)) { page = response.decode(); }
            var cursor = page.get("next_cursor");
            var verses = page.path("verses");
            if (!page.path("translation").isTextual() || page.path("translation").asText().isEmpty()
                || !page.path("rights").isObject() || !verses.isArray() || verses.size() > limit || cursor == null
                || !(cursor.isNull() || cursor.isTextual() && !cursor.asText().isEmpty())
                || !cursor.isNull() && (verses.isEmpty() || !seen.add(cursor.asText()))) throw invalid();
            if (translation == null) {
                translation = page.path("translation").asText();
                rights = page.get("rights").deepCopy();
            } else if (!translation.equals(page.path("translation").asText()) || !rights.equals(page.get("rights"))) throw invalid();
            for (var v : verses) {
                if (!v.path("book_id").canConvertToInt() || !v.path("chapter").canConvertToInt() || !v.path("verse").canConvertToInt()
                    || !v.path("book_id").isIntegralNumber() || !v.path("chapter").isIntegralNumber() || !v.path("verse").isIntegralNumber()) throw invalid();
                int b = v.path("book_id").asInt(), c = v.path("chapter").asInt(), n = v.path("verse").asInt();
                if (b < 1 || b > 255 || c < 1 || c > 152 || n < 1 || n > 255
                    || book != 0 && (b != book || c < chapter || c == chapter && n <= verse)) throw invalid();
                book = b; chapter = c; verse = n;
            }
            if (cursor.isNull()) finished = true;
            else options = options.toBuilder().query("after", cursor.asText()).build();
            return page;
        }
    }

    private static IOException invalid() { return new IOException("Invalid or inconsistent REST book page"); }
}
