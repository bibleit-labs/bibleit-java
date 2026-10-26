package app.bibleit;

import java.util.List;
import java.util.Map;

/**
 * Typed SSH results. Optional wire fields use nullable boxed values; raw records retain future fields.
 */
public final class SshModels {
    private SshModels() {}
    /**
     * Typed identity info result retaining wire metadata.
     * @param actor the actor
     * @param displayName the display name
     * @param identityName the identity name
     * @param authProvider the auth provider
     * @param keyFingerprint the key fingerprint
     * @param authenticated the authenticated
     * @param roles the roles
     * @param permissions the permissions
     * @param raw the original finite response
     */
    public record IdentityInfo(String actor, String displayName, String identityName, String authProvider, String keyFingerprint, boolean authenticated, List<String> roles, List<String> permissions, CommandResponse raw) {
        @Override public String toString() { return "IdentityInfo[redacted]"; }
    }
    /**
     * Typed verse text result retaining wire metadata.
     * @param text the text
     * @param content the content
     * @param translation the translation
     * @param bookName the book name
     * @param reference the reference
     * @param bookId the book id
     * @param chapter the chapter
     * @param verse the verse
     * @param fields the original wire fields
     */
    public record VerseText(String text, String content, String translation, String bookName, String reference, Long bookId, Long chapter, Long verse, Map<String, String> fields) {
        @Override public String toString() { return "VerseText[redacted]"; }
    }
    /**
     * Typed reading result retaining wire metadata.
     * @param translation the translation
     * @param book the book
     * @param chapter the chapter
     * @param verse the verse
     * @param verses the verses
     * @param raw the original finite response
     */
    public record Reading(String translation, Long book, Long chapter, Long verse, List<VerseText> verses, CommandResponse raw) {
        @Override public String toString() { return "Reading[redacted]"; }
    }
    /**
     * Typed server info result retaining wire metadata.
     * @param protocolVersion the protocol version
     * @param version the version
     * @param capabilities the capabilities
     * @param raw the original finite response
     */
    public record ServerInfo(long protocolVersion, String version, List<String> capabilities, CommandResponse raw) {
        @Override public String toString() { return "ServerInfo[redacted]"; }
    }
    /**
     * Typed help entry result retaining wire metadata.
     * @param usage the usage
     * @param summary the summary
     * @param auth the auth
     * @param fields the original wire fields
     */
    public record HelpEntry(String usage, String summary, String auth, Map<String, String> fields) {
        @Override public String toString() { return "HelpEntry[redacted]"; }
    }
    /**
     * Typed help result result retaining wire metadata.
     * @param authenticated the authenticated
     * @param commands the commands
     * @param raw the original finite response
     */
    public record HelpResult(boolean authenticated, List<HelpEntry> commands, CommandResponse raw) {
        @Override public String toString() { return "HelpResult[redacted]"; }
    }
    /**
     * Typed account info result retaining wire metadata.
     * @param actor the actor
     * @param displayName the display name
     * @param handle the handle
     * @param avatarUrl the avatar url
     * @param locale the locale
     * @param theme the theme
     * @param plan the plan
     * @param planName the plan name
     * @param planSource the plan source
     * @param planActivatedAt the plan activated at
     * @param lives the lives
     * @param translations the translations
     * @param tokens the tokens
     * @param keys the keys
     * @param raw the original finite response
     */
    public record AccountInfo(String actor, String displayName, String handle, String avatarUrl, String locale, String theme, String plan, String planName, String planSource, long planActivatedAt, long lives, long translations, long tokens, long keys, CommandResponse raw) {
        @Override public String toString() { return "AccountInfo[redacted]"; }
    }
    /**
     * A finite quota bound or an explicit unlimited allowance.
     * @param unlimited the unlimited
     * @param value the finite bound; meaningful only when unlimited is false
     */
    public record QuotaLimit(boolean unlimited, long value) {
        @Override public String toString() { return "QuotaLimit[redacted]"; }
    }
    /**
     * Typed quota result retaining wire metadata.
     * @param permission the permission
     * @param limit the limit
     * @param used the used
     * @param fields the original wire fields
     */
    public record Quota(String permission, QuotaLimit limit, long used, Map<String, String> fields) {
        @Override public String toString() { return "Quota[redacted]"; }
    }
    /**
     * Typed quota result result retaining wire metadata.
     * @param quotas the quotas
     * @param raw the original finite response
     */
    public record QuotaResult(List<Quota> quotas, CommandResponse raw) {
        @Override public String toString() { return "QuotaResult[redacted]"; }
    }
    /**
     * Typed translation info result retaining wire metadata.
     * @param shortName the short name
     * @param fullName the full name
     * @param edition the edition
     * @param updated the updated
     * @param rightsStatus the rights status
     * @param rightsLabel the rights label
     * @param rightsUrl the rights url
     * @param sourceUrl the source url
     * @param attribution the attribution
     * @param trademarkNotice the trademark notice
     * @param raw the original finite response
     */
    public record TranslationInfo(String shortName, String fullName, String edition, String updated, String rightsStatus, String rightsLabel, String rightsUrl, String sourceUrl, String attribution, String trademarkNotice, CommandResponse raw) {
        @Override public String toString() { return "TranslationInfo[redacted]"; }
    }
    /**
     * A source chapter descriptor; verses is the highest source verse ID, not a row count.
     * @param number the number
     * @param verses the verses
     * @param fields the original wire fields
     */
    public record CatalogChapter(long number, long verses, Map<String, String> fields) {
        @Override public String toString() { return "CatalogChapter[redacted]"; }
    }
    /**
     * A source book descriptor; its ID is not its position in the book list.
     * @param id the id
     * @param name the name
     * @param chapters the chapters
     * @param fields the original wire fields
     */
    public record CatalogBook(long id, String name, List<CatalogChapter> chapters, Map<String, String> fields) {
        @Override public String toString() { return "CatalogBook[redacted]"; }
    }
    /**
     * Typed translation catalog result retaining wire metadata.
     * @param translation the translation
     * @param books the books
     * @param raw the original finite response
     */
    public record TranslationCatalog(String translation, List<CatalogBook> books, CommandResponse raw) {
        @Override public String toString() { return "TranslationCatalog[redacted]"; }
    }
    /**
     * Active credential metadata; bearer token values are never included.
     * @param id the id
     * @param source the source
     * @param issuedAt the issued at
     * @param issuedBy the issued by
     * @param label the label
     * @param scopes the scopes
     * @param lastUsedAt the last used at
     * @param expiresAt the expires at
     * @param retiring the retiring
     * @param fields the original wire fields
     */
    public record TokenMetadata(String id, String source, long issuedAt, String issuedBy, String label, List<String> scopes, Long lastUsedAt, Long expiresAt, Boolean retiring, Map<String, String> fields) {
        @Override public String toString() { return "TokenMetadata[redacted]"; }
    }
    /**
     * Typed token list result retaining wire metadata.
     * @param actor the actor
     * @param tokens the tokens
     * @param raw the original finite response
     */
    public record TokenList(String actor, List<TokenMetadata> tokens, CommandResponse raw) {
        @Override public String toString() { return "TokenList[redacted]"; }
    }
    /**
     * Typed live state result retaining wire metadata.
     * @param id the id
     * @param name the name
     * @param status the status
     * @param paused the paused
     * @param reference the reference
     * @param owner the owner
     * @param createdBy the created by
     * @param organizationId the organization id
     * @param owned the owned
     * @param protectedValue the protected value
     * @param createdAt the created at
     * @param translations the translations
     * @param fields the original wire fields
     */
    public record LiveState(String id, String name, String status, boolean paused, String reference, String owner, String createdBy, String organizationId, Boolean owned, Boolean protectedValue, Long createdAt, List<String> translations, Map<String, String> fields) {
        @Override public String toString() { return "LiveState[redacted]"; }
    }
    /**
     * Typed live result result retaining wire metadata.
     * @param live the live
     * @param raw the original finite response
     */
    public record LiveResult(LiveState live, CommandResponse raw) {
        @Override public String toString() { return "LiveResult[redacted]"; }
    }
    /**
     * Typed live list result retaining wire metadata.
     * @param lives the lives
     * @param raw the original finite response
     */
    public record LiveList(List<LiveState> lives, CommandResponse raw) {
        @Override public String toString() { return "LiveList[redacted]"; }
    }
    /**
     * Typed live connection result retaining wire metadata.
     * @param actor the actor
     * @param access the access
     * @param connectedAt the connected at
     * @param connectedForSeconds the connected for seconds
     * @param fields the original wire fields
     */
    public record LiveConnection(String actor, String access, long connectedAt, long connectedForSeconds, Map<String, String> fields) {
        @Override public String toString() { return "LiveConnection[redacted]"; }
    }
    /**
     * Typed live statistics result retaining wire metadata.
     * @param id the id
     * @param runningForSeconds the running for seconds
     * @param revision the revision
     * @param stackEntries the stack entries
     * @param actorConnections the actor connections
     * @param anonymousConnections the anonymous connections
     * @param connections the connections
     * @param raw the original finite response
     */
    public record LiveStatistics(String id, long runningForSeconds, long revision, long stackEntries, long actorConnections, long anonymousConnections, List<LiveConnection> connections, CommandResponse raw) {
        @Override public String toString() { return "LiveStatistics[redacted]"; }
    }
    /**
     * Typed live verse result retaining wire metadata.
     * @param translation the translation
     * @param reference the reference
     * @param text the text
     * @param fields the original wire fields
     */
    public record LiveVerse(String translation, String reference, String text, Map<String, String> fields) {
        @Override public String toString() { return "LiveVerse[redacted]"; }
    }
    /**
     * Typed stack entry result retaining wire metadata.
     * @param position the position
     * @param liveVerse the live verse
     */
    public record StackEntry(long position, LiveVerse liveVerse) {
        @Override public String toString() { return "StackEntry[redacted]"; }
    }
    /**
     * Typed live stack result retaining wire metadata.
     * @param id the id
     * @param entries the entries
     * @param raw the original finite response
     */
    public record LiveStack(String id, List<StackEntry> entries, CommandResponse raw) {
        @Override public String toString() { return "LiveStack[redacted]"; }
    }
    /**
     * Typed live verse result result retaining wire metadata.
     * @param event the event
     * @param verses the verses
     * @param raw the original finite response
     */
    public record LiveVerseResult(String event, List<LiveVerse> verses, CommandResponse raw) {
        @Override public String toString() { return "LiveVerseResult[redacted]"; }
    }
    /**
     * Typed live action result result retaining wire metadata.
     * @param state the state
     * @param paused the paused
     * @param event the event
     * @param raw the original finite response
     */
    public record LiveActionResult(LiveState state, Boolean paused, String event, CommandResponse raw) {
        @Override public String toString() { return "LiveActionResult[redacted]"; }
    }
    /**
     * Typed organization directory entry result retaining wire metadata.
     * @param kind the kind
     * @param id the id
     * @param applicationId the application id
     * @param handle the handle
     * @param name the name
     * @param domain the domain
     * @param role the role
     * @param verification the verification
     * @param status the status
     * @param state the state
     * @param nextAction the next action
     * @param verificationUrl the verification url
     * @param organizationId the organization id
     * @param fields the original wire fields
     */
    public record OrganizationDirectoryEntry(String kind, String id, String applicationId, String handle, String name, String domain, String role, String verification, String status, String state, String nextAction, String verificationUrl, String organizationId, Map<String, String> fields) {
        @Override public String toString() { return "OrganizationDirectoryEntry[redacted]"; }
    }
    /**
     * Typed organization directory result retaining wire metadata.
     * @param entries the entries
     * @param next the next
     * @param raw the original finite response
     */
    public record OrganizationDirectory(List<OrganizationDirectoryEntry> entries, String next, CommandResponse raw) {
        @Override public String toString() { return "OrganizationDirectory[redacted]"; }
    }
    /**
     * Typed organization application result result retaining wire metadata.
     * @param application the application
     * @param raw the original finite response
     */
    public record OrganizationApplicationResult(OrganizationDirectoryEntry application, CommandResponse raw) {
        @Override public String toString() { return "OrganizationApplicationResult[redacted]"; }
    }
}
