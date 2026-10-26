package app.bibleit;

/** A pinned REST operation. */
public record Operation(String method, String path, String summary) {}
