package app.bibleit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Scripture reference. Zero chapter or verse means omitted.
 * @param translation the translation
 * @param book the book
 * @param chapter the chapter
 * @param verse the verse
 */
public record Reference(String translation, String book, int chapter, int verse) {
    /**
     * Copies and validates the reference components.
     * @param translation the translation
     * @param book the book
     * @param chapter the chapter
     * @param verse the verse
     */
    public Reference {
        Objects.requireNonNull(translation); Objects.requireNonNull(book);
        if (chapter < 0 || chapter > 255 || verse < 0 || verse > 255 || verse != 0 && chapter == 0)
            throw new IllegalArgumentException("Invalid chapter or verse");
    }
    List<String> arguments(boolean requireTranslation) {
        var args = new ArrayList<String>();
        if (requireTranslation || !translation.isEmpty()) args.add(translation);
        args.add(book);
        if (chapter != 0) args.add(Integer.toString(chapter));
        if (verse != 0) args.add(Integer.toString(verse));
        return args;
    }
    /**
     * Creates a whole-book Scripture reference.
     * @param translation the translation slug
     * @param book the source book identifier or name
     */
    public Reference(String translation, String book) { this(translation, book, 0, 0); }
    /**
     * Creates a whole-chapter Scripture reference.
     * @param translation the translation slug
     * @param book the source book identifier or name
     * @param chapter the source chapter identifier
     */
    public Reference(String translation, String book, int chapter) { this(translation, book, chapter, 0); }

}
