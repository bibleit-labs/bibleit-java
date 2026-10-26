package app.bibleit;

import java.util.List;
import java.util.Map;

/** Typed SSH results. Optional wire fields use nullable boxed values; raw records retain future fields. */
public final class SshModels {
    private SshModels() {}
    public record IdentityInfo(String actor, String displayName, String identityName, String authProvider, String keyFingerprint, boolean authenticated, List<String> roles, List<String> permissions, CommandResponse raw) {
        @Override public String toString() { return "IdentityInfo[redacted]"; }
    }
    public record VerseText(String text, String content, String translation, String bookName, String reference, Long bookId, Long chapter, Long verse, Map<String, String> fields) {
        @Override public String toString() { return "VerseText[redacted]"; }
    }
    public record Reading(String translation, Long book, Long chapter, Long verse, List<VerseText> verses, CommandResponse raw) {
        @Override public String toString() { return "Reading[redacted]"; }
    }
    public record ServerInfo(long protocolVersion, String version, List<String> capabilities, CommandResponse raw) {
        @Override public String toString() { return "ServerInfo[redacted]"; }
    }
    public record HelpEntry(String usage, String summary, String auth, Map<String, String> fields) {
        @Override public String toString() { return "HelpEntry[redacted]"; }
    }
    public record HelpResult(boolean authenticated, List<HelpEntry> commands, CommandResponse raw) {
        @Override public String toString() { return "HelpResult[redacted]"; }
    }
    public record AccountInfo(String actor, String displayName, String handle, String avatarUrl, String locale, String theme, String plan, String planName, String planSource, long planActivatedAt, long lives, long translations, long tokens, long keys, CommandResponse raw) {
        @Override public String toString() { return "AccountInfo[redacted]"; }
    }
    public record QuotaLimit(boolean unlimited, long value) {
        @Override public String toString() { return "QuotaLimit[redacted]"; }
    }
    public record Quota(String permission, QuotaLimit limit, long used, Map<String, String> fields) {
        @Override public String toString() { return "Quota[redacted]"; }
    }
    public record QuotaResult(List<Quota> quotas, CommandResponse raw) {
        @Override public String toString() { return "QuotaResult[redacted]"; }
    }
    public record TranslationInfo(String shortName, String fullName, String edition, String updated, String rightsStatus, String rightsLabel, String rightsUrl, String sourceUrl, String attribution, String trademarkNotice, CommandResponse raw) {
        @Override public String toString() { return "TranslationInfo[redacted]"; }
    }
    public record CatalogChapter(long number, long verses, Map<String, String> fields) {
        @Override public String toString() { return "CatalogChapter[redacted]"; }
    }
    public record CatalogBook(long id, String name, List<CatalogChapter> chapters, Map<String, String> fields) {
        @Override public String toString() { return "CatalogBook[redacted]"; }
    }
    public record TranslationCatalog(String translation, List<CatalogBook> books, CommandResponse raw) {
        @Override public String toString() { return "TranslationCatalog[redacted]"; }
    }
    public record TokenMetadata(String id, String source, long issuedAt, String issuedBy, String label, List<String> scopes, Long lastUsedAt, Long expiresAt, Boolean retiring, Map<String, String> fields) {
        @Override public String toString() { return "TokenMetadata[redacted]"; }
    }
    public record TokenList(String actor, List<TokenMetadata> tokens, CommandResponse raw) {
        @Override public String toString() { return "TokenList[redacted]"; }
    }
    public record LiveState(String id, String name, String status, boolean paused, String reference, String owner, String createdBy, String organizationId, Boolean owned, Boolean protectedValue, Long createdAt, List<String> translations, Map<String, String> fields) {
        @Override public String toString() { return "LiveState[redacted]"; }
    }
    public record LiveResult(LiveState live, CommandResponse raw) {
        @Override public String toString() { return "LiveResult[redacted]"; }
    }
    public record LiveList(List<LiveState> lives, CommandResponse raw) {
        @Override public String toString() { return "LiveList[redacted]"; }
    }
    public record LiveConnection(String actor, String access, long connectedAt, long connectedForSeconds, Map<String, String> fields) {
        @Override public String toString() { return "LiveConnection[redacted]"; }
    }
    public record LiveStatistics(String id, long runningForSeconds, long revision, long stackEntries, long actorConnections, long anonymousConnections, List<LiveConnection> connections, CommandResponse raw) {
        @Override public String toString() { return "LiveStatistics[redacted]"; }
    }
    public record LiveVerse(String translation, String reference, String text, Map<String, String> fields) {
        @Override public String toString() { return "LiveVerse[redacted]"; }
    }
    public record StackEntry(long position, LiveVerse liveVerse) {
        @Override public String toString() { return "StackEntry[redacted]"; }
    }
    public record LiveStack(String id, List<StackEntry> entries, CommandResponse raw) {
        @Override public String toString() { return "LiveStack[redacted]"; }
    }
    public record LiveVerseResult(String event, List<LiveVerse> verses, CommandResponse raw) {
        @Override public String toString() { return "LiveVerseResult[redacted]"; }
    }
    public record LiveActionResult(LiveState state, Boolean paused, String event, CommandResponse raw) {
        @Override public String toString() { return "LiveActionResult[redacted]"; }
    }
    public record OrganizationDirectoryEntry(String kind, String id, String applicationId, String handle, String name, String domain, String role, String verification, String status, String state, String nextAction, String verificationUrl, String organizationId, Map<String, String> fields) {
        @Override public String toString() { return "OrganizationDirectoryEntry[redacted]"; }
    }
    public record OrganizationDirectory(List<OrganizationDirectoryEntry> entries, String next, CommandResponse raw) {
        @Override public String toString() { return "OrganizationDirectory[redacted]"; }
    }
    public record OrganizationApplicationResult(OrganizationDirectoryEntry application, CommandResponse raw) {
        @Override public String toString() { return "OrganizationApplicationResult[redacted]"; }
    }
}
