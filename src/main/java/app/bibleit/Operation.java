package app.bibleit;

/**
 * A pinned REST operation.
 * @param method the method
 * @param path the path
 * @param summary the summary
 */
public record Operation(String method, String path, String summary) {}
