package app.bibleit;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UncheckedIOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Lazy, repeatable whole-book traversal. Each iterator fetches and validates its own pages.
 */
public final class BookPages implements Iterable<Models.ScriptureBookRead> {
    /** Fetches one page using typed query values supplied by the client. */
    @FunctionalInterface
    interface PageFetcher {
        Response fetch(int limit, String after) throws IOException, InterruptedException;
    }

    private final PageFetcher fetcher;
    private final int limit;
    private final String initialAfter;

    BookPages(PageFetcher fetcher, Integer limit, String after) {
        this.fetcher = fetcher;
        this.limit = limit == null ? 50 : limit;
        if (this.limit < 1 || this.limit > 500) throw new IllegalArgumentException("Book page limit must be 1..500");
        if (after != null && after.isEmpty()) throw new IllegalArgumentException("Invalid cursor");
        initialAfter = after;
    }

    /** I/O failures throw UncheckedIOException; interruption restores the thread interrupt flag. */
    @Override public Iterator<Models.ScriptureBookRead> iterator() { return new PageIterator(); }

    private final class PageIterator implements Iterator<Models.ScriptureBookRead> {
        private final Set<String> seen = new HashSet<>();
        private String after = initialAfter;
        private boolean finished;
        private Models.ScriptureBookRead next;
        private String translation;
        private JsonNode rights;
        private int book, chapter, verse;

        private PageIterator() {
            if (after != null) seen.add(after);
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

        @Override public Models.ScriptureBookRead next() {
            if (!hasNext()) throw new NoSuchElementException("Book traversal complete");
            var page = next;
            next = null;
            return page;
        }

        private Models.ScriptureBookRead fetch() throws IOException, InterruptedException {
            if (Thread.currentThread().isInterrupted()) throw new InterruptedException("Book traversal interrupted");
            Models.ScriptureBookRead result;
            try (var response = fetcher.fetch(limit, after)) {
                result = response.decode(Models.ScriptureBookRead.class);
            }
            if (result == null) throw invalid();
            JsonNode page = result.toJson();
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
            else after = cursor.asText();
            return result;
        }
    }

    private static IOException invalid() { return new IOException("Invalid or inconsistent REST book page"); }
}
