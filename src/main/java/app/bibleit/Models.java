package app.bibleit;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.math.BigDecimal;
import java.util.*;

/** Generated Java beans. Missing fields, explicit nulls and unknown fields are retained. */
public final class Models {
    private Models() {}
    public abstract static class Model {
        private final ObjectNode fields;
        protected Model() { fields = Json.MAPPER.createObjectNode(); }
        protected Model(JsonNode value) {
            if (!value.isObject()) throw new IllegalArgumentException("Expected a JSON object");
            fields = (ObjectNode) value.deepCopy();
        }
        @JsonValue public JsonNode toJson() { return fields.deepCopy(); }
        public boolean hasField(String name) { return fields.has(name); }
        public void removeField(String name) { fields.remove(name); }
        public void setField(String name, JsonNode value) { fields.set(name, value == null ? Json.MAPPER.nullNode() : value.deepCopy()); }
        protected <T> T get(String name, TypeReference<T> type) { return Json.MAPPER.convertValue(fields.get(name), type); }
        protected void set(String name, Object value) { fields.set(name, Json.MAPPER.valueToTree(value)); }
        @Override public String toString() { return getClass().getSimpleName() + "[redacted]"; }
    }
    public static final class Problem extends Model {
        public Problem() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public Problem(JsonNode value) { super(value); }
        public JsonNode getType() { return get("type", new TypeReference<>() {}); }
        public void setType(JsonNode value) { set("type", value); }
        public String getTitle() { return get("title", new TypeReference<>() {}); }
        public void setTitle(String value) { set("title", value); }
        public Long getStatus() { return get("status", new TypeReference<>() {}); }
        public void setStatus(Long value) { set("status", value); }
        public String getCode() { return get("code", new TypeReference<>() {}); }
        public void setCode(String value) { set("code", value); }
        public String getRequestId() { return get("request_id", new TypeReference<>() {}); }
        public void setRequestId(String value) { set("request_id", value); }
        public String getDetail() { return get("detail", new TypeReference<>() {}); }
        public void setDetail(String value) { set("detail", value); }
    }
    public static final class Scope {
        private final JsonNode value;
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public Scope(JsonNode value) { this.value = value.deepCopy(); }
        @JsonValue public JsonNode value() { return value.deepCopy(); }
        @Override public String toString() { return "Scope[redacted]"; }
    }
    public static final class ServiceToken extends Model {
        public ServiceToken() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public ServiceToken(JsonNode value) { super(value); }
        public String getId() { return get("id", new TypeReference<>() {}); }
        public void setId(String value) { set("id", value); }
        public String getOrganizationId() { return get("organization_id", new TypeReference<>() {}); }
        public void setOrganizationId(String value) { set("organization_id", value); }
        public String getLabel() { return get("label", new TypeReference<>() {}); }
        public void setLabel(String value) { set("label", value); }
        public List<Scope> getScopes() { return get("scopes", new TypeReference<>() {}); }
        public void setScopes(List<Scope> value) { set("scopes", value); }
        public Long getIssuedAt() { return get("issued_at", new TypeReference<>() {}); }
        public void setIssuedAt(Long value) { set("issued_at", value); }
        public Long getExpiresAt() { return get("expires_at", new TypeReference<>() {}); }
        public void setExpiresAt(Long value) { set("expires_at", value); }
        public Long getLastUsedAt() { return get("last_used_at", new TypeReference<>() {}); }
        public void setLastUsedAt(Long value) { set("last_used_at", value); }
    }
    public static final class CreateServiceToken extends Model {
        public CreateServiceToken() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public CreateServiceToken(JsonNode value) { super(value); }
        public String getLabel() { return get("label", new TypeReference<>() {}); }
        public void setLabel(String value) { set("label", value); }
        public List<Scope> getScopes() { return get("scopes", new TypeReference<>() {}); }
        public void setScopes(List<Scope> value) { set("scopes", value); }
        public Long getExpiresAt() { return get("expires_at", new TypeReference<>() {}); }
        public void setExpiresAt(Long value) { set("expires_at", value); }
    }
    public static final class IssuedServiceToken extends Model {
        public IssuedServiceToken() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public IssuedServiceToken(JsonNode value) { super(value); }
        public String getId() { return get("id", new TypeReference<>() {}); }
        public void setId(String value) { set("id", value); }
        public String getOrganizationId() { return get("organization_id", new TypeReference<>() {}); }
        public void setOrganizationId(String value) { set("organization_id", value); }
        public String getLabel() { return get("label", new TypeReference<>() {}); }
        public void setLabel(String value) { set("label", value); }
        public List<Scope> getScopes() { return get("scopes", new TypeReference<>() {}); }
        public void setScopes(List<Scope> value) { set("scopes", value); }
        public Long getExpiresAt() { return get("expires_at", new TypeReference<>() {}); }
        public void setExpiresAt(Long value) { set("expires_at", value); }
        public String getSecret() { return get("secret", new TypeReference<>() {}); }
        public void setSecret(String value) { set("secret", value); }
    }
    public static final class LiveSummary extends Model {
        public LiveSummary() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveSummary(JsonNode value) { super(value); }
        public String getId() { return get("id", new TypeReference<>() {}); }
        public void setId(String value) { set("id", value); }
        public String getOrganizationId() { return get("organization_id", new TypeReference<>() {}); }
        public void setOrganizationId(String value) { set("organization_id", value); }
        public String getName() { return get("name", new TypeReference<>() {}); }
        public void setName(String value) { set("name", value); }
    }
    public static final class CreateLive extends Model {
        public CreateLive() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public CreateLive(JsonNode value) { super(value); }
        public String getName() { return get("name", new TypeReference<>() {}); }
        public void setName(String value) { set("name", value); }
    }
    public static final class Live extends Model {
        public Live() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public Live(JsonNode value) { super(value); }
        public String getId() { return get("id", new TypeReference<>() {}); }
        public void setId(String value) { set("id", value); }
        public String getOrganizationId() { return get("organization_id", new TypeReference<>() {}); }
        public void setOrganizationId(String value) { set("organization_id", value); }
        public String getName() { return get("name", new TypeReference<>() {}); }
        public void setName(String value) { set("name", value); }
        public String getStatus() { return get("status", new TypeReference<>() {}); }
        public void setStatus(String value) { set("status", value); }
        public String getMode() { return get("mode", new TypeReference<>() {}); }
        public void setMode(String value) { set("mode", value); }
        public Boolean getPaused() { return get("paused", new TypeReference<>() {}); }
        public void setPaused(Boolean value) { set("paused", value); }
        public Long getSequence() { return get("sequence", new TypeReference<>() {}); }
        public void setSequence(Long value) { set("sequence", value); }
    }
    public static final class ServiceTokenList extends Model {
        public ServiceTokenList() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public ServiceTokenList(JsonNode value) { super(value); }
        public List<ServiceToken> getData() { return get("data", new TypeReference<>() {}); }
        public void setData(List<ServiceToken> value) { set("data", value); }
        public String getNextCursor() { return get("next_cursor", new TypeReference<>() {}); }
        public void setNextCursor(String value) { set("next_cursor", value); }
    }
    public static final class LiveList extends Model {
        public LiveList() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveList(JsonNode value) { super(value); }
        public List<LiveSummary> getData() { return get("data", new TypeReference<>() {}); }
        public void setData(List<LiveSummary> value) { set("data", value); }
        public String getNextCursor() { return get("next_cursor", new TypeReference<>() {}); }
        public void setNextCursor(String value) { set("next_cursor", value); }
    }
    public static final class TranslationRights extends Model {
        public TranslationRights() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public TranslationRights(JsonNode value) { super(value); }
        public String getEdition() { return get("edition", new TypeReference<>() {}); }
        public void setEdition(String value) { set("edition", value); }
        public String getRightsStatus() { return get("rights_status", new TypeReference<>() {}); }
        public void setRightsStatus(String value) { set("rights_status", value); }
        public String getLicenseId() { return get("license_id", new TypeReference<>() {}); }
        public void setLicenseId(String value) { set("license_id", value); }
        public String getLicenseUrl() { return get("license_url", new TypeReference<>() {}); }
        public void setLicenseUrl(String value) { set("license_url", value); }
        public String getSourceUrl() { return get("source_url", new TypeReference<>() {}); }
        public void setSourceUrl(String value) { set("source_url", value); }
        public String getCopyright() { return get("copyright", new TypeReference<>() {}); }
        public void setCopyright(String value) { set("copyright", value); }
        public String getChangeNotice() { return get("change_notice", new TypeReference<>() {}); }
        public void setChangeNotice(String value) { set("change_notice", value); }
        public String getAttribution() { return get("attribution", new TypeReference<>() {}); }
        public void setAttribution(String value) { set("attribution", value); }
    }
    public static final class Verse extends Model {
        public Verse() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public Verse(JsonNode value) { super(value); }
        public Long getBookId() { return get("book_id", new TypeReference<>() {}); }
        public void setBookId(Long value) { set("book_id", value); }
        public String getBook() { return get("book", new TypeReference<>() {}); }
        public void setBook(String value) { set("book", value); }
        public Long getChapter() { return get("chapter", new TypeReference<>() {}); }
        public void setChapter(Long value) { set("chapter", value); }
        public Long getVerse() { return get("verse", new TypeReference<>() {}); }
        public void setVerse(Long value) { set("verse", value); }
        public String getText() { return get("text", new TypeReference<>() {}); }
        public void setText(String value) { set("text", value); }
    }
    public static final class ScriptureRead extends Model {
        public ScriptureRead() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public ScriptureRead(JsonNode value) { super(value); }
        public String getTranslation() { return get("translation", new TypeReference<>() {}); }
        public void setTranslation(String value) { set("translation", value); }
        public TranslationRights getRights() { return get("rights", new TypeReference<>() {}); }
        public void setRights(TranslationRights value) { set("rights", value); }
        public List<Verse> getVerses() { return get("verses", new TypeReference<>() {}); }
        public void setVerses(List<Verse> value) { set("verses", value); }
    }
    public static final class ScriptureSearch extends Model {
        public ScriptureSearch() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public ScriptureSearch(JsonNode value) { super(value); }
        public String getTranslation() { return get("translation", new TypeReference<>() {}); }
        public void setTranslation(String value) { set("translation", value); }
        public TranslationRights getRights() { return get("rights", new TypeReference<>() {}); }
        public void setRights(TranslationRights value) { set("rights", value); }
        public List<Verse> getVerses() { return get("verses", new TypeReference<>() {}); }
        public void setVerses(List<Verse> value) { set("verses", value); }
        public Boolean getHasMore() { return get("has_more", new TypeReference<>() {}); }
        public void setHasMore(Boolean value) { set("has_more", value); }
    }
    public static final class DisplayVerse extends Model {
        public DisplayVerse() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public DisplayVerse(JsonNode value) { super(value); }
        public Long getBookId() { return get("book_id", new TypeReference<>() {}); }
        public void setBookId(Long value) { set("book_id", value); }
        public String getBook() { return get("book", new TypeReference<>() {}); }
        public void setBook(String value) { set("book", value); }
        public Long getChapter() { return get("chapter", new TypeReference<>() {}); }
        public void setChapter(Long value) { set("chapter", value); }
        public Long getVerse() { return get("verse", new TypeReference<>() {}); }
        public void setVerse(Long value) { set("verse", value); }
        public String getText() { return get("text", new TypeReference<>() {}); }
        public void setText(String value) { set("text", value); }
        public String getTranslation() { return get("translation", new TypeReference<>() {}); }
        public void setTranslation(String value) { set("translation", value); }
        public TranslationRights getRights() { return get("rights", new TypeReference<>() {}); }
        public void setRights(TranslationRights value) { set("rights", value); }
        public String getAttribution() { return get("attribution", new TypeReference<>() {}); }
        public void setAttribution(String value) { set("attribution", value); }
    }
    public static final class ScriptureReference extends Model {
        public ScriptureReference() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public ScriptureReference(JsonNode value) { super(value); }
        public String getTranslation() { return get("translation", new TypeReference<>() {}); }
        public void setTranslation(String value) { set("translation", value); }
        public Long getBookId() { return get("book_id", new TypeReference<>() {}); }
        public void setBookId(Long value) { set("book_id", value); }
        public Long getChapter() { return get("chapter", new TypeReference<>() {}); }
        public void setChapter(Long value) { set("chapter", value); }
        public Long getVerseStart() { return get("verse_start", new TypeReference<>() {}); }
        public void setVerseStart(Long value) { set("verse_start", value); }
        public Long getVerseEnd() { return get("verse_end", new TypeReference<>() {}); }
        public void setVerseEnd(Long value) { set("verse_end", value); }
    }
    public static final class ReplaceDisplay {
        private final JsonNode value;
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public ReplaceDisplay(JsonNode value) { this.value = value.deepCopy(); }
        @JsonValue public JsonNode value() { return value.deepCopy(); }
        @Override public String toString() { return "ReplaceDisplay[redacted]"; }
    }
    public static final class PlaybackPatch extends Model {
        public PlaybackPatch() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PlaybackPatch(JsonNode value) { super(value); }
        public JsonNode getStatus() { return get("status", new TypeReference<>() {}); }
        public void setStatus(JsonNode value) { set("status", value); }
        public Boolean getPaused() { return get("paused", new TypeReference<>() {}); }
        public void setPaused(Boolean value) { set("paused", value); }
    }
    public static final class Playback extends Model {
        public Playback() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public Playback(JsonNode value) { super(value); }
        public String getLiveId() { return get("live_id", new TypeReference<>() {}); }
        public void setLiveId(String value) { set("live_id", value); }
        public JsonNode getStatus() { return get("status", new TypeReference<>() {}); }
        public void setStatus(JsonNode value) { set("status", value); }
        public Boolean getPaused() { return get("paused", new TypeReference<>() {}); }
        public void setPaused(Boolean value) { set("paused", value); }
        public Boolean getShowing() { return get("showing", new TypeReference<>() {}); }
        public void setShowing(Boolean value) { set("showing", value); }
        public Long getSequence() { return get("sequence", new TypeReference<>() {}); }
        public void setSequence(Long value) { set("sequence", value); }
    }
    public static final class SnapshotSummary extends Model {
        public SnapshotSummary() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public SnapshotSummary(JsonNode value) { super(value); }
        public String getId() { return get("id", new TypeReference<>() {}); }
        public void setId(String value) { set("id", value); }
        public String getDeck() { return get("deck", new TypeReference<>() {}); }
        public void setDeck(String value) { set("deck", value); }
        public Long getRevision() { return get("revision", new TypeReference<>() {}); }
        public void setRevision(Long value) { set("revision", value); }
        public Long getIndex() { return get("index", new TypeReference<>() {}); }
        public void setIndex(Long value) { set("index", value); }
        public Boolean getBlank() { return get("blank", new TypeReference<>() {}); }
        public void setBlank(Boolean value) { set("blank", value); }
        public String getSlideId() { return get("slide_id", new TypeReference<>() {}); }
        public void setSlideId(String value) { set("slide_id", value); }
    }
    public static final class LiveDisplay extends Model {
        public LiveDisplay() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveDisplay(JsonNode value) { super(value); }
        public String getLiveId() { return get("live_id", new TypeReference<>() {}); }
        public void setLiveId(String value) { set("live_id", value); }
        public JsonNode getStatus() { return get("status", new TypeReference<>() {}); }
        public void setStatus(JsonNode value) { set("status", value); }
        public Boolean getPaused() { return get("paused", new TypeReference<>() {}); }
        public void setPaused(Boolean value) { set("paused", value); }
        public Boolean getShowing() { return get("showing", new TypeReference<>() {}); }
        public void setShowing(Boolean value) { set("showing", value); }
        public Long getSequence() { return get("sequence", new TypeReference<>() {}); }
        public void setSequence(Long value) { set("sequence", value); }
        public String getOrganizationId() { return get("organization_id", new TypeReference<>() {}); }
        public void setOrganizationId(String value) { set("organization_id", value); }
        public JsonNode getType() { return get("type", new TypeReference<>() {}); }
        public void setType(JsonNode value) { set("type", value); }
        public List<DisplayVerse> getVerses() { return get("verses", new TypeReference<>() {}); }
        public void setVerses(List<DisplayVerse> value) { set("verses", value); }
        public JsonNode getSnapshot() { return get("snapshot", new TypeReference<>() {}); }
        public void setSnapshot(JsonNode value) { set("snapshot", value); }
    }
    public static final class ServiceTokenConfiguration extends Model {
        public ServiceTokenConfiguration() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public ServiceTokenConfiguration(JsonNode value) { super(value); }
        public String getId() { return get("id", new TypeReference<>() {}); }
        public void setId(String value) { set("id", value); }
        public String getOrganizationId() { return get("organization_id", new TypeReference<>() {}); }
        public void setOrganizationId(String value) { set("organization_id", value); }
        public String getLabel() { return get("label", new TypeReference<>() {}); }
        public void setLabel(String value) { set("label", value); }
        public List<Scope> getScopes() { return get("scopes", new TypeReference<>() {}); }
        public void setScopes(List<Scope> value) { set("scopes", value); }
        public Long getIssuedAt() { return get("issued_at", new TypeReference<>() {}); }
        public void setIssuedAt(Long value) { set("issued_at", value); }
        public Long getExpiresAt() { return get("expires_at", new TypeReference<>() {}); }
        public void setExpiresAt(Long value) { set("expires_at", value); }
    }
    public static final class PatchServiceToken extends Model {
        public PatchServiceToken() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PatchServiceToken(JsonNode value) { super(value); }
        public String getLabel() { return get("label", new TypeReference<>() {}); }
        public void setLabel(String value) { set("label", value); }
        public List<Scope> getScopes() { return get("scopes", new TypeReference<>() {}); }
        public void setScopes(List<Scope> value) { set("scopes", value); }
        public Long getExpiresAt() { return get("expires_at", new TypeReference<>() {}); }
        public void setExpiresAt(Long value) { set("expires_at", value); }
    }
    public static final class EmptyObject extends Model {
        public EmptyObject() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public EmptyObject(JsonNode value) { super(value); }
    }
    public static final class TranslationProvenance extends Model {
        public TranslationProvenance() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public TranslationProvenance(JsonNode value) { super(value); }
        public String getArchiveSha256() { return get("archive_sha256", new TypeReference<>() {}); }
        public void setArchiveSha256(String value) { set("archive_sha256", value); }
        public String getReviewedOn() { return get("reviewed_on", new TypeReference<>() {}); }
        public void setReviewedOn(String value) { set("reviewed_on", value); }
        public String getSourceId() { return get("source_id", new TypeReference<>() {}); }
        public void setSourceId(String value) { set("source_id", value); }
    }
    public static final class CatalogueTranslation extends Model {
        public CatalogueTranslation() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public CatalogueTranslation(JsonNode value) { super(value); }
        public String getSlug() { return get("slug", new TypeReference<>() {}); }
        public void setSlug(String value) { set("slug", value); }
        public String getName() { return get("name", new TypeReference<>() {}); }
        public void setName(String value) { set("name", value); }
        public JsonNode getLanguage() { return get("language", new TypeReference<>() {}); }
        public void setLanguage(JsonNode value) { set("language", value); }
        public List<Long> getBookIds() { return get("book_ids", new TypeReference<>() {}); }
        public void setBookIds(List<Long> value) { set("book_ids", value); }
        public TranslationRights getRights() { return get("rights", new TypeReference<>() {}); }
        public void setRights(TranslationRights value) { set("rights", value); }
        public Boolean getInstalled() { return get("installed", new TypeReference<>() {}); }
        public void setInstalled(Boolean value) { set("installed", value); }
        public TranslationProvenance getProvenance() { return get("provenance", new TypeReference<>() {}); }
        public void setProvenance(TranslationProvenance value) { set("provenance", value); }
    }
    public static final class BookDefinition extends Model {
        public BookDefinition() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public BookDefinition(JsonNode value) { super(value); }
        public Long getId() { return get("id", new TypeReference<>() {}); }
        public void setId(Long value) { set("id", value); }
        public String getCode() { return get("code", new TypeReference<>() {}); }
        public void setCode(String value) { set("code", value); }
        public List<String> getAliases() { return get("aliases", new TypeReference<>() {}); }
        public void setAliases(List<String> value) { set("aliases", value); }
        public JsonNode getNames() { return get("names", new TypeReference<>() {}); }
        public void setNames(JsonNode value) { set("names", value); }
    }
    public static final class TranslationCollection extends Model {
        public TranslationCollection() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public TranslationCollection(JsonNode value) { super(value); }
        public List<CatalogueTranslation> getData() { return get("data", new TypeReference<>() {}); }
        public void setData(List<CatalogueTranslation> value) { set("data", value); }
        public String getNextCursor() { return get("next_cursor", new TypeReference<>() {}); }
        public void setNextCursor(String value) { set("next_cursor", value); }
    }
    public static final class BookCollection extends Model {
        public BookCollection() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public BookCollection(JsonNode value) { super(value); }
        public List<BookDefinition> getData() { return get("data", new TypeReference<>() {}); }
        public void setData(List<BookDefinition> value) { set("data", value); }
        public String getNextCursor() { return get("next_cursor", new TypeReference<>() {}); }
        public void setNextCursor(String value) { set("next_cursor", value); }
    }
    public static final class Organization extends Model {
        public Organization() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public Organization(JsonNode value) { super(value); }
        public String getId() { return get("id", new TypeReference<>() {}); }
        public void setId(String value) { set("id", value); }
        public String getName() { return get("name", new TypeReference<>() {}); }
        public void setName(String value) { set("name", value); }
        public String getHandle() { return get("handle", new TypeReference<>() {}); }
        public void setHandle(String value) { set("handle", value); }
        public String getLogoUrl() { return get("logo_url", new TypeReference<>() {}); }
        public void setLogoUrl(String value) { set("logo_url", value); }
        public String getWebsite() { return get("website", new TypeReference<>() {}); }
        public void setWebsite(String value) { set("website", value); }
    }
    public static final class Membership extends Model {
        public Membership() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public Membership(JsonNode value) { super(value); }
        public String getId() { return get("id", new TypeReference<>() {}); }
        public void setId(String value) { set("id", value); }
        public String getOrganizationId() { return get("organization_id", new TypeReference<>() {}); }
        public void setOrganizationId(String value) { set("organization_id", value); }
        public String getDisplayName() { return get("display_name", new TypeReference<>() {}); }
        public void setDisplayName(String value) { set("display_name", value); }
        public String getHandle() { return get("handle", new TypeReference<>() {}); }
        public void setHandle(String value) { set("handle", value); }
        public JsonNode getRole() { return get("role", new TypeReference<>() {}); }
        public void setRole(JsonNode value) { set("role", value); }
    }
    public static final class MembershipPage extends Model {
        public MembershipPage() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public MembershipPage(JsonNode value) { super(value); }
        public List<Membership> getData() { return get("data", new TypeReference<>() {}); }
        public void setData(List<Membership> value) { set("data", value); }
        public String getNextCursor() { return get("next_cursor", new TypeReference<>() {}); }
        public void setNextCursor(String value) { set("next_cursor", value); }
    }
    public static final class MembershipPatch extends Model {
        public MembershipPatch() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public MembershipPatch(JsonNode value) { super(value); }
        public JsonNode getRole() { return get("role", new TypeReference<>() {}); }
        public void setRole(JsonNode value) { set("role", value); }
    }
    public static final class LiveMetadataPatch extends Model {
        public LiveMetadataPatch() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveMetadataPatch(JsonNode value) { super(value); }
        public String getName() { return get("name", new TypeReference<>() {}); }
        public void setName(String value) { set("name", value); }
    }
    public static final class LiveWorkspacePut extends Model {
        public LiveWorkspacePut() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveWorkspacePut(JsonNode value) { super(value); }
        public String getOrganizationId() { return get("organization_id", new TypeReference<>() {}); }
        public void setOrganizationId(String value) { set("organization_id", value); }
    }
    public static final class LiveQueueItem extends Model {
        public LiveQueueItem() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveQueueItem(JsonNode value) { super(value); }
        public String getId() { return get("id", new TypeReference<>() {}); }
        public void setId(String value) { set("id", value); }
        public Long getPosition() { return get("position", new TypeReference<>() {}); }
        public void setPosition(Long value) { set("position", value); }
        public DisplayVerse getVerse() { return get("verse", new TypeReference<>() {}); }
        public void setVerse(DisplayVerse value) { set("verse", value); }
    }
    public static final class LiveQueuePage extends Model {
        public LiveQueuePage() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveQueuePage(JsonNode value) { super(value); }
        public String getLiveId() { return get("live_id", new TypeReference<>() {}); }
        public void setLiveId(String value) { set("live_id", value); }
        public String getOrganizationId() { return get("organization_id", new TypeReference<>() {}); }
        public void setOrganizationId(String value) { set("organization_id", value); }
        public Long getSequence() { return get("sequence", new TypeReference<>() {}); }
        public void setSequence(Long value) { set("sequence", value); }
        public List<LiveQueueItem> getData() { return get("data", new TypeReference<>() {}); }
        public void setData(List<LiveQueueItem> value) { set("data", value); }
        public String getNextCursor() { return get("next_cursor", new TypeReference<>() {}); }
        public void setNextCursor(String value) { set("next_cursor", value); }
    }
    public static final class CreateLiveQueueItem extends Model {
        public CreateLiveQueueItem() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public CreateLiveQueueItem(JsonNode value) { super(value); }
        public JsonNode getReference() { return get("reference", new TypeReference<>() {}); }
        public void setReference(JsonNode value) { set("reference", value); }
        public String getBeforeItemId() { return get("before_item_id", new TypeReference<>() {}); }
        public void setBeforeItemId(String value) { set("before_item_id", value); }
    }
    public static final class LiveQueueOrder extends Model {
        public LiveQueueOrder() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveQueueOrder(JsonNode value) { super(value); }
        public List<String> getItemIds() { return get("item_ids", new TypeReference<>() {}); }
        public void setItemIds(List<String> value) { set("item_ids", value); }
    }
    public static final class LiveStudioSettings extends Model {
        public LiveStudioSettings() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveStudioSettings(JsonNode value) { super(value); }
        public Long getVersion() { return get("version", new TypeReference<>() {}); }
        public void setVersion(Long value) { set("version", value); }
        public Boolean getShowLogo() { return get("showLogo", new TypeReference<>() {}); }
        public void setShowLogo(Boolean value) { set("showLogo", value); }
        public JsonNode getPreset() { return get("preset", new TypeReference<>() {}); }
        public void setPreset(JsonNode value) { set("preset", value); }
        public JsonNode getTransition() { return get("transition", new TypeReference<>() {}); }
        public void setTransition(JsonNode value) { set("transition", value); }
        public JsonNode getControls() { return get("controls", new TypeReference<>() {}); }
        public void setControls(JsonNode value) { set("controls", value); }
        public JsonNode getPalettes() { return get("palettes", new TypeReference<>() {}); }
        public void setPalettes(JsonNode value) { set("palettes", value); }
        public JsonNode getContainer() { return get("container", new TypeReference<>() {}); }
        public void setContainer(JsonNode value) { set("container", value); }
        public JsonNode getElements() { return get("elements", new TypeReference<>() {}); }
        public void setElements(JsonNode value) { set("elements", value); }
        public List<JsonNode> getOrder() { return get("order", new TypeReference<>() {}); }
        public void setOrder(List<JsonNode> value) { set("order", value); }
    }
    public static final class LiveStudioSettingsInput extends Model {
        public LiveStudioSettingsInput() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveStudioSettingsInput(JsonNode value) { super(value); }
        public Long getVersion() { return get("version", new TypeReference<>() {}); }
        public void setVersion(Long value) { set("version", value); }
        public Boolean getShowLogo() { return get("showLogo", new TypeReference<>() {}); }
        public void setShowLogo(Boolean value) { set("showLogo", value); }
        public JsonNode getPreset() { return get("preset", new TypeReference<>() {}); }
        public void setPreset(JsonNode value) { set("preset", value); }
        public JsonNode getTransition() { return get("transition", new TypeReference<>() {}); }
        public void setTransition(JsonNode value) { set("transition", value); }
        public JsonNode getControls() { return get("controls", new TypeReference<>() {}); }
        public void setControls(JsonNode value) { set("controls", value); }
        public JsonNode getPalettes() { return get("palettes", new TypeReference<>() {}); }
        public void setPalettes(JsonNode value) { set("palettes", value); }
        public JsonNode getContainer() { return get("container", new TypeReference<>() {}); }
        public void setContainer(JsonNode value) { set("container", value); }
        public JsonNode getElements() { return get("elements", new TypeReference<>() {}); }
        public void setElements(JsonNode value) { set("elements", value); }
        public List<JsonNode> getOrder() { return get("order", new TypeReference<>() {}); }
        public void setOrder(List<JsonNode> value) { set("order", value); }
    }
    public static final class OrganizationLiveStudio extends Model {
        public OrganizationLiveStudio() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public OrganizationLiveStudio(JsonNode value) { super(value); }
        public String getOrganizationId() { return get("organization_id", new TypeReference<>() {}); }
        public void setOrganizationId(String value) { set("organization_id", value); }
        public Long getRevision() { return get("revision", new TypeReference<>() {}); }
        public void setRevision(Long value) { set("revision", value); }
        public LiveStudioSettings getSettings() { return get("settings", new TypeReference<>() {}); }
        public void setSettings(LiveStudioSettings value) { set("settings", value); }
    }
    public static final class PutOrganizationLiveStudio extends Model {
        public PutOrganizationLiveStudio() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PutOrganizationLiveStudio(JsonNode value) { super(value); }
        public LiveStudioSettingsInput getSettings() { return get("settings", new TypeReference<>() {}); }
        public void setSettings(LiveStudioSettingsInput value) { set("settings", value); }
    }
    public static final class PresentationElementStyle extends Model {
        public PresentationElementStyle() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PresentationElementStyle(JsonNode value) { super(value); }
        public BigDecimal getFontSize() { return get("font_size", new TypeReference<>() {}); }
        public void setFontSize(BigDecimal value) { set("font_size", value); }
        public JsonNode getFontFamily() { return get("font_family", new TypeReference<>() {}); }
        public void setFontFamily(JsonNode value) { set("font_family", value); }
        public String getColor() { return get("color", new TypeReference<>() {}); }
        public void setColor(String value) { set("color", value); }
        public Boolean getBold() { return get("bold", new TypeReference<>() {}); }
        public void setBold(Boolean value) { set("bold", value); }
        public Boolean getItalic() { return get("italic", new TypeReference<>() {}); }
        public void setItalic(Boolean value) { set("italic", value); }
        public JsonNode getAlign() { return get("align", new TypeReference<>() {}); }
        public void setAlign(JsonNode value) { set("align", value); }
        public BigDecimal getLineHeight() { return get("line_height", new TypeReference<>() {}); }
        public void setLineHeight(BigDecimal value) { set("line_height", value); }
        public String getFill() { return get("fill", new TypeReference<>() {}); }
        public void setFill(String value) { set("fill", value); }
        public String getStroke() { return get("stroke", new TypeReference<>() {}); }
        public void setStroke(String value) { set("stroke", value); }
        public BigDecimal getStrokeWidth() { return get("stroke_width", new TypeReference<>() {}); }
        public void setStrokeWidth(BigDecimal value) { set("stroke_width", value); }
        public JsonNode getStrokeStyle() { return get("stroke_style", new TypeReference<>() {}); }
        public void setStrokeStyle(JsonNode value) { set("stroke_style", value); }
        public BigDecimal getOpacity() { return get("opacity", new TypeReference<>() {}); }
        public void setOpacity(BigDecimal value) { set("opacity", value); }
    }
    public static final class PresentationTextElement extends Model {
        public PresentationTextElement() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PresentationTextElement(JsonNode value) { super(value); }
        public String getId() { return get("id", new TypeReference<>() {}); }
        public void setId(String value) { set("id", value); }
        public JsonNode getType() { return get("type", new TypeReference<>() {}); }
        public void setType(JsonNode value) { set("type", value); }
        public Long getX() { return get("x", new TypeReference<>() {}); }
        public void setX(Long value) { set("x", value); }
        public Long getY() { return get("y", new TypeReference<>() {}); }
        public void setY(Long value) { set("y", value); }
        public Long getWidth() { return get("width", new TypeReference<>() {}); }
        public void setWidth(Long value) { set("width", value); }
        public Long getHeight() { return get("height", new TypeReference<>() {}); }
        public void setHeight(Long value) { set("height", value); }
        public PresentationElementStyle getStyle() { return get("style", new TypeReference<>() {}); }
        public void setStyle(PresentationElementStyle value) { set("style", value); }
        public Long getRotation() { return get("rotation", new TypeReference<>() {}); }
        public void setRotation(Long value) { set("rotation", value); }
        public String getText() { return get("text", new TypeReference<>() {}); }
        public void setText(String value) { set("text", value); }
    }
    public static final class PresentationVerseElement extends Model {
        public PresentationVerseElement() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PresentationVerseElement(JsonNode value) { super(value); }
        public String getId() { return get("id", new TypeReference<>() {}); }
        public void setId(String value) { set("id", value); }
        public JsonNode getType() { return get("type", new TypeReference<>() {}); }
        public void setType(JsonNode value) { set("type", value); }
        public Long getX() { return get("x", new TypeReference<>() {}); }
        public void setX(Long value) { set("x", value); }
        public Long getY() { return get("y", new TypeReference<>() {}); }
        public void setY(Long value) { set("y", value); }
        public Long getWidth() { return get("width", new TypeReference<>() {}); }
        public void setWidth(Long value) { set("width", value); }
        public Long getHeight() { return get("height", new TypeReference<>() {}); }
        public void setHeight(Long value) { set("height", value); }
        public PresentationElementStyle getStyle() { return get("style", new TypeReference<>() {}); }
        public void setStyle(PresentationElementStyle value) { set("style", value); }
        public Long getRotation() { return get("rotation", new TypeReference<>() {}); }
        public void setRotation(Long value) { set("rotation", value); }
        public String getText() { return get("text", new TypeReference<>() {}); }
        public void setText(String value) { set("text", value); }
        public String getTranslation() { return get("translation", new TypeReference<>() {}); }
        public void setTranslation(String value) { set("translation", value); }
        public String getEdition() { return get("edition", new TypeReference<>() {}); }
        public void setEdition(String value) { set("edition", value); }
        public String getReference() { return get("reference", new TypeReference<>() {}); }
        public void setReference(String value) { set("reference", value); }
        public String getAttribution() { return get("attribution", new TypeReference<>() {}); }
        public void setAttribution(String value) { set("attribution", value); }
    }
    public static final class PresentationShapeElement extends Model {
        public PresentationShapeElement() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PresentationShapeElement(JsonNode value) { super(value); }
        public String getId() { return get("id", new TypeReference<>() {}); }
        public void setId(String value) { set("id", value); }
        public JsonNode getType() { return get("type", new TypeReference<>() {}); }
        public void setType(JsonNode value) { set("type", value); }
        public Long getX() { return get("x", new TypeReference<>() {}); }
        public void setX(Long value) { set("x", value); }
        public Long getY() { return get("y", new TypeReference<>() {}); }
        public void setY(Long value) { set("y", value); }
        public Long getWidth() { return get("width", new TypeReference<>() {}); }
        public void setWidth(Long value) { set("width", value); }
        public Long getHeight() { return get("height", new TypeReference<>() {}); }
        public void setHeight(Long value) { set("height", value); }
        public PresentationElementStyle getStyle() { return get("style", new TypeReference<>() {}); }
        public void setStyle(PresentationElementStyle value) { set("style", value); }
        public Long getRotation() { return get("rotation", new TypeReference<>() {}); }
        public void setRotation(Long value) { set("rotation", value); }
        public JsonNode getShape() { return get("shape", new TypeReference<>() {}); }
        public void setShape(JsonNode value) { set("shape", value); }
    }
    public static final class PresentationImageElement extends Model {
        public PresentationImageElement() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PresentationImageElement(JsonNode value) { super(value); }
        public String getId() { return get("id", new TypeReference<>() {}); }
        public void setId(String value) { set("id", value); }
        public JsonNode getType() { return get("type", new TypeReference<>() {}); }
        public void setType(JsonNode value) { set("type", value); }
        public Long getX() { return get("x", new TypeReference<>() {}); }
        public void setX(Long value) { set("x", value); }
        public Long getY() { return get("y", new TypeReference<>() {}); }
        public void setY(Long value) { set("y", value); }
        public Long getWidth() { return get("width", new TypeReference<>() {}); }
        public void setWidth(Long value) { set("width", value); }
        public Long getHeight() { return get("height", new TypeReference<>() {}); }
        public void setHeight(Long value) { set("height", value); }
        public PresentationElementStyle getStyle() { return get("style", new TypeReference<>() {}); }
        public void setStyle(PresentationElementStyle value) { set("style", value); }
        public Long getRotation() { return get("rotation", new TypeReference<>() {}); }
        public void setRotation(Long value) { set("rotation", value); }
        public String getAsset() { return get("asset", new TypeReference<>() {}); }
        public void setAsset(String value) { set("asset", value); }
        public String getAlt() { return get("alt", new TypeReference<>() {}); }
        public void setAlt(String value) { set("alt", value); }
        public JsonNode getFit() { return get("fit", new TypeReference<>() {}); }
        public void setFit(JsonNode value) { set("fit", value); }
    }
    public static final class PresentationVideoElement extends Model {
        public PresentationVideoElement() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PresentationVideoElement(JsonNode value) { super(value); }
        public String getId() { return get("id", new TypeReference<>() {}); }
        public void setId(String value) { set("id", value); }
        public JsonNode getType() { return get("type", new TypeReference<>() {}); }
        public void setType(JsonNode value) { set("type", value); }
        public Long getX() { return get("x", new TypeReference<>() {}); }
        public void setX(Long value) { set("x", value); }
        public Long getY() { return get("y", new TypeReference<>() {}); }
        public void setY(Long value) { set("y", value); }
        public Long getWidth() { return get("width", new TypeReference<>() {}); }
        public void setWidth(Long value) { set("width", value); }
        public Long getHeight() { return get("height", new TypeReference<>() {}); }
        public void setHeight(Long value) { set("height", value); }
        public PresentationElementStyle getStyle() { return get("style", new TypeReference<>() {}); }
        public void setStyle(PresentationElementStyle value) { set("style", value); }
        public Long getRotation() { return get("rotation", new TypeReference<>() {}); }
        public void setRotation(Long value) { set("rotation", value); }
        public String getAsset() { return get("asset", new TypeReference<>() {}); }
        public void setAsset(String value) { set("asset", value); }
        public JsonNode getFit() { return get("fit", new TypeReference<>() {}); }
        public void setFit(JsonNode value) { set("fit", value); }
        public String getPoster() { return get("poster", new TypeReference<>() {}); }
        public void setPoster(String value) { set("poster", value); }
        public Boolean getMuted() { return get("muted", new TypeReference<>() {}); }
        public void setMuted(Boolean value) { set("muted", value); }
        public Boolean getLoop() { return get("loop", new TypeReference<>() {}); }
        public void setLoop(Boolean value) { set("loop", value); }
    }
    public static final class PresentationYouTubeElement extends Model {
        public PresentationYouTubeElement() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PresentationYouTubeElement(JsonNode value) { super(value); }
        public String getId() { return get("id", new TypeReference<>() {}); }
        public void setId(String value) { set("id", value); }
        public JsonNode getType() { return get("type", new TypeReference<>() {}); }
        public void setType(JsonNode value) { set("type", value); }
        public Long getX() { return get("x", new TypeReference<>() {}); }
        public void setX(Long value) { set("x", value); }
        public Long getY() { return get("y", new TypeReference<>() {}); }
        public void setY(Long value) { set("y", value); }
        public Long getWidth() { return get("width", new TypeReference<>() {}); }
        public void setWidth(Long value) { set("width", value); }
        public Long getHeight() { return get("height", new TypeReference<>() {}); }
        public void setHeight(Long value) { set("height", value); }
        public PresentationElementStyle getStyle() { return get("style", new TypeReference<>() {}); }
        public void setStyle(PresentationElementStyle value) { set("style", value); }
        public Long getRotation() { return get("rotation", new TypeReference<>() {}); }
        public void setRotation(Long value) { set("rotation", value); }
        public String getVideoId() { return get("video_id", new TypeReference<>() {}); }
        public void setVideoId(String value) { set("video_id", value); }
    }
    public static final class PresentationElement {
        private final JsonNode value;
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PresentationElement(JsonNode value) { this.value = value.deepCopy(); }
        @JsonValue public JsonNode value() { return value.deepCopy(); }
        @Override public String toString() { return "PresentationElement[redacted]"; }
    }
    public static final class PresentationSlide extends Model {
        public PresentationSlide() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PresentationSlide(JsonNode value) { super(value); }
        public String getId() { return get("id", new TypeReference<>() {}); }
        public void setId(String value) { set("id", value); }
        public List<PresentationElement> getElements() { return get("elements", new TypeReference<>() {}); }
        public void setElements(List<PresentationElement> value) { set("elements", value); }
        public String getBackground() { return get("background", new TypeReference<>() {}); }
        public void setBackground(String value) { set("background", value); }
    }
    public static final class PresentationDocument extends Model {
        public PresentationDocument() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PresentationDocument(JsonNode value) { super(value); }
        public Long getVersion() { return get("version", new TypeReference<>() {}); }
        public void setVersion(Long value) { set("version", value); }
        public String getName() { return get("name", new TypeReference<>() {}); }
        public void setName(String value) { set("name", value); }
        public List<PresentationSlide> getSlides() { return get("slides", new TypeReference<>() {}); }
        public void setSlides(List<PresentationSlide> value) { set("slides", value); }
    }
    public static final class Presentation extends Model {
        public Presentation() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public Presentation(JsonNode value) { super(value); }
        public String getId() { return get("id", new TypeReference<>() {}); }
        public void setId(String value) { set("id", value); }
        public String getOrganizationId() { return get("organization_id", new TypeReference<>() {}); }
        public void setOrganizationId(String value) { set("organization_id", value); }
        public Long getRevision() { return get("revision", new TypeReference<>() {}); }
        public void setRevision(Long value) { set("revision", value); }
        public PresentationDocument getDocument() { return get("document", new TypeReference<>() {}); }
        public void setDocument(PresentationDocument value) { set("document", value); }
    }
    public static final class PresentationSummary extends Model {
        public PresentationSummary() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PresentationSummary(JsonNode value) { super(value); }
        public String getId() { return get("id", new TypeReference<>() {}); }
        public void setId(String value) { set("id", value); }
        public String getOrganizationId() { return get("organization_id", new TypeReference<>() {}); }
        public void setOrganizationId(String value) { set("organization_id", value); }
        public String getName() { return get("name", new TypeReference<>() {}); }
        public void setName(String value) { set("name", value); }
        public Long getRevision() { return get("revision", new TypeReference<>() {}); }
        public void setRevision(Long value) { set("revision", value); }
        public Long getSlideCount() { return get("slide_count", new TypeReference<>() {}); }
        public void setSlideCount(Long value) { set("slide_count", value); }
    }
    public static final class PresentationPage extends Model {
        public PresentationPage() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PresentationPage(JsonNode value) { super(value); }
        public List<PresentationSummary> getData() { return get("data", new TypeReference<>() {}); }
        public void setData(List<PresentationSummary> value) { set("data", value); }
        public String getNextCursor() { return get("next_cursor", new TypeReference<>() {}); }
        public void setNextCursor(String value) { set("next_cursor", value); }
    }
    public static final class CreatePresentation extends Model {
        public CreatePresentation() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public CreatePresentation(JsonNode value) { super(value); }
        public String getName() { return get("name", new TypeReference<>() {}); }
        public void setName(String value) { set("name", value); }
    }
    public static final class PutPresentation extends Model {
        public PutPresentation() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PutPresentation(JsonNode value) { super(value); }
        public PresentationDocument getDocument() { return get("document", new TypeReference<>() {}); }
        public void setDocument(PresentationDocument value) { set("document", value); }
    }
    public static final class PresentationDisplayReference extends Model {
        public PresentationDisplayReference() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PresentationDisplayReference(JsonNode value) { super(value); }
        public JsonNode getType() { return get("type", new TypeReference<>() {}); }
        public void setType(JsonNode value) { set("type", value); }
        public String getPresentationId() { return get("presentation_id", new TypeReference<>() {}); }
        public void setPresentationId(String value) { set("presentation_id", value); }
        public Long getRevision() { return get("revision", new TypeReference<>() {}); }
        public void setRevision(Long value) { set("revision", value); }
        public String getSlideId() { return get("slide_id", new TypeReference<>() {}); }
        public void setSlideId(String value) { set("slide_id", value); }
    }
    public static final class PresentationAsset extends Model {
        public PresentationAsset() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PresentationAsset(JsonNode value) { super(value); }
        public String getId() { return get("id", new TypeReference<>() {}); }
        public void setId(String value) { set("id", value); }
        public String getOrganizationId() { return get("organization_id", new TypeReference<>() {}); }
        public void setOrganizationId(String value) { set("organization_id", value); }
        public String getPresentationId() { return get("presentation_id", new TypeReference<>() {}); }
        public void setPresentationId(String value) { set("presentation_id", value); }
        public String getKind() { return get("kind", new TypeReference<>() {}); }
        public void setKind(String value) { set("kind", value); }
        public String getMime() { return get("mime", new TypeReference<>() {}); }
        public void setMime(String value) { set("mime", value); }
        public Long getWidth() { return get("width", new TypeReference<>() {}); }
        public void setWidth(Long value) { set("width", value); }
        public Long getHeight() { return get("height", new TypeReference<>() {}); }
        public void setHeight(Long value) { set("height", value); }
        public Long getBytes() { return get("bytes", new TypeReference<>() {}); }
        public void setBytes(Long value) { set("bytes", value); }
    }
    public static final class PresentationExport extends Model {
        public PresentationExport() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PresentationExport(JsonNode value) { super(value); }
        public String getId() { return get("id", new TypeReference<>() {}); }
        public void setId(String value) { set("id", value); }
        public String getPresentationId() { return get("presentation_id", new TypeReference<>() {}); }
        public void setPresentationId(String value) { set("presentation_id", value); }
        public String getOrganizationId() { return get("organization_id", new TypeReference<>() {}); }
        public void setOrganizationId(String value) { set("organization_id", value); }
        public Long getRevision() { return get("revision", new TypeReference<>() {}); }
        public void setRevision(Long value) { set("revision", value); }
        public String getFormat() { return get("format", new TypeReference<>() {}); }
        public void setFormat(String value) { set("format", value); }
        public String getStatus() { return get("status", new TypeReference<>() {}); }
        public void setStatus(String value) { set("status", value); }
        public String getError() { return get("error", new TypeReference<>() {}); }
        public void setError(String value) { set("error", value); }
        public Long getBytes() { return get("bytes", new TypeReference<>() {}); }
        public void setBytes(Long value) { set("bytes", value); }
        public Long getCreatedAt() { return get("created_at", new TypeReference<>() {}); }
        public void setCreatedAt(Long value) { set("created_at", value); }
        public Long getExpiresAt() { return get("expires_at", new TypeReference<>() {}); }
        public void setExpiresAt(Long value) { set("expires_at", value); }
        public String getDownloadUrl() { return get("download_url", new TypeReference<>() {}); }
        public void setDownloadUrl(String value) { set("download_url", value); }
    }
    public static final class PresentationExportList extends Model {
        public PresentationExportList() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PresentationExportList(JsonNode value) { super(value); }
        public List<PresentationExport> getData() { return get("data", new TypeReference<>() {}); }
        public void setData(List<PresentationExport> value) { set("data", value); }
        public String getNextCursor() { return get("next_cursor", new TypeReference<>() {}); }
        public void setNextCursor(String value) { set("next_cursor", value); }
    }
    public static final class CreatePresentationExport {
        private final JsonNode value;
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public CreatePresentationExport(JsonNode value) { this.value = value.deepCopy(); }
        @JsonValue public JsonNode value() { return value.deepCopy(); }
        @Override public String toString() { return "CreatePresentationExport[redacted]"; }
    }
    public static final class PersonalLiveSummary extends Model {
        public PersonalLiveSummary() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PersonalLiveSummary(JsonNode value) { super(value); }
        public String getId() { return get("id", new TypeReference<>() {}); }
        public void setId(String value) { set("id", value); }
        public String getName() { return get("name", new TypeReference<>() {}); }
        public void setName(String value) { set("name", value); }
        public String getAccountId() { return get("account_id", new TypeReference<>() {}); }
        public void setAccountId(String value) { set("account_id", value); }
    }
    public static final class PersonalLive extends Model {
        public PersonalLive() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PersonalLive(JsonNode value) { super(value); }
        public String getId() { return get("id", new TypeReference<>() {}); }
        public void setId(String value) { set("id", value); }
        public String getName() { return get("name", new TypeReference<>() {}); }
        public void setName(String value) { set("name", value); }
        public String getStatus() { return get("status", new TypeReference<>() {}); }
        public void setStatus(String value) { set("status", value); }
        public String getMode() { return get("mode", new TypeReference<>() {}); }
        public void setMode(String value) { set("mode", value); }
        public Boolean getPaused() { return get("paused", new TypeReference<>() {}); }
        public void setPaused(Boolean value) { set("paused", value); }
        public Long getSequence() { return get("sequence", new TypeReference<>() {}); }
        public void setSequence(Long value) { set("sequence", value); }
        public String getAccountId() { return get("account_id", new TypeReference<>() {}); }
        public void setAccountId(String value) { set("account_id", value); }
    }
    public static final class PersonalLiveDisplay extends Model {
        public PersonalLiveDisplay() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PersonalLiveDisplay(JsonNode value) { super(value); }
        public String getLiveId() { return get("live_id", new TypeReference<>() {}); }
        public void setLiveId(String value) { set("live_id", value); }
        public JsonNode getStatus() { return get("status", new TypeReference<>() {}); }
        public void setStatus(JsonNode value) { set("status", value); }
        public Boolean getPaused() { return get("paused", new TypeReference<>() {}); }
        public void setPaused(Boolean value) { set("paused", value); }
        public Boolean getShowing() { return get("showing", new TypeReference<>() {}); }
        public void setShowing(Boolean value) { set("showing", value); }
        public Long getSequence() { return get("sequence", new TypeReference<>() {}); }
        public void setSequence(Long value) { set("sequence", value); }
        public String getType() { return get("type", new TypeReference<>() {}); }
        public void setType(String value) { set("type", value); }
        public List<DisplayVerse> getVerses() { return get("verses", new TypeReference<>() {}); }
        public void setVerses(List<DisplayVerse> value) { set("verses", value); }
        public JsonNode getSnapshot() { return get("snapshot", new TypeReference<>() {}); }
        public void setSnapshot(JsonNode value) { set("snapshot", value); }
        public String getAccountId() { return get("account_id", new TypeReference<>() {}); }
        public void setAccountId(String value) { set("account_id", value); }
    }
    public static final class PersonalLiveQueuePage extends Model {
        public PersonalLiveQueuePage() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PersonalLiveQueuePage(JsonNode value) { super(value); }
        public String getLiveId() { return get("live_id", new TypeReference<>() {}); }
        public void setLiveId(String value) { set("live_id", value); }
        public Long getSequence() { return get("sequence", new TypeReference<>() {}); }
        public void setSequence(Long value) { set("sequence", value); }
        public List<LiveQueueItem> getData() { return get("data", new TypeReference<>() {}); }
        public void setData(List<LiveQueueItem> value) { set("data", value); }
        public String getNextCursor() { return get("next_cursor", new TypeReference<>() {}); }
        public void setNextCursor(String value) { set("next_cursor", value); }
        public String getAccountId() { return get("account_id", new TypeReference<>() {}); }
        public void setAccountId(String value) { set("account_id", value); }
    }
    public static final class PersonalLiveList extends Model {
        public PersonalLiveList() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PersonalLiveList(JsonNode value) { super(value); }
        public List<PersonalLiveSummary> getData() { return get("data", new TypeReference<>() {}); }
        public void setData(List<PersonalLiveSummary> value) { set("data", value); }
        public String getNextCursor() { return get("next_cursor", new TypeReference<>() {}); }
        public void setNextCursor(String value) { set("next_cursor", value); }
    }
    public static final class PersonalIdentity extends Model {
        public PersonalIdentity() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PersonalIdentity(JsonNode value) { super(value); }
        public String getId() { return get("id", new TypeReference<>() {}); }
        public void setId(String value) { set("id", value); }
        public String getDisplayName() { return get("display_name", new TypeReference<>() {}); }
        public void setDisplayName(String value) { set("display_name", value); }
        public String getHandle() { return get("handle", new TypeReference<>() {}); }
        public void setHandle(String value) { set("handle", value); }
        public String getPersonalAccountId() { return get("personal_account_id", new TypeReference<>() {}); }
        public void setPersonalAccountId(String value) { set("personal_account_id", value); }
    }
    public static final class PersonalAccount extends Model {
        public PersonalAccount() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PersonalAccount(JsonNode value) { super(value); }
        public String getId() { return get("id", new TypeReference<>() {}); }
        public void setId(String value) { set("id", value); }
        public String getKind() { return get("kind", new TypeReference<>() {}); }
        public void setKind(String value) { set("kind", value); }
        public String getName() { return get("name", new TypeReference<>() {}); }
        public void setName(String value) { set("name", value); }
        public String getHandle() { return get("handle", new TypeReference<>() {}); }
        public void setHandle(String value) { set("handle", value); }
    }
    public static final class PersonalAccountList extends Model {
        public PersonalAccountList() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PersonalAccountList(JsonNode value) { super(value); }
        public List<PersonalAccount> getData() { return get("data", new TypeReference<>() {}); }
        public void setData(List<PersonalAccount> value) { set("data", value); }
        public JsonNode getNextCursor() { return get("next_cursor", new TypeReference<>() {}); }
        public void setNextCursor(JsonNode value) { set("next_cursor", value); }
    }
    public static final class OAuthScope {
        private final JsonNode value;
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public OAuthScope(JsonNode value) { this.value = value.deepCopy(); }
        @JsonValue public JsonNode value() { return value.deepCopy(); }
        @Override public String toString() { return "OAuthScope[redacted]"; }
    }
    public static final class OAuthError extends Model {
        public OAuthError() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public OAuthError(JsonNode value) { super(value); }
        public String getError() { return get("error", new TypeReference<>() {}); }
        public void setError(String value) { set("error", value); }
    }
    public static final class OAuthToken extends Model {
        public OAuthToken() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public OAuthToken(JsonNode value) { super(value); }
        public String getAccessToken() { return get("access_token", new TypeReference<>() {}); }
        public void setAccessToken(String value) { set("access_token", value); }
        public String getTokenType() { return get("token_type", new TypeReference<>() {}); }
        public void setTokenType(String value) { set("token_type", value); }
        public Long getExpiresIn() { return get("expires_in", new TypeReference<>() {}); }
        public void setExpiresIn(Long value) { set("expires_in", value); }
        public String getScope() { return get("scope", new TypeReference<>() {}); }
        public void setScope(String value) { set("scope", value); }
        public String getRefreshToken() { return get("refresh_token", new TypeReference<>() {}); }
        public void setRefreshToken(String value) { set("refresh_token", value); }
    }
    public static final class OAuthMetadata extends Model {
        public OAuthMetadata() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public OAuthMetadata(JsonNode value) { super(value); }
        public String getIssuer() { return get("issuer", new TypeReference<>() {}); }
        public void setIssuer(String value) { set("issuer", value); }
        public String getAuthorizationEndpoint() { return get("authorization_endpoint", new TypeReference<>() {}); }
        public void setAuthorizationEndpoint(String value) { set("authorization_endpoint", value); }
        public String getTokenEndpoint() { return get("token_endpoint", new TypeReference<>() {}); }
        public void setTokenEndpoint(String value) { set("token_endpoint", value); }
        public String getRevocationEndpoint() { return get("revocation_endpoint", new TypeReference<>() {}); }
        public void setRevocationEndpoint(String value) { set("revocation_endpoint", value); }
        public List<String> getResponseTypesSupported() { return get("response_types_supported", new TypeReference<>() {}); }
        public void setResponseTypesSupported(List<String> value) { set("response_types_supported", value); }
        public List<String> getGrantTypesSupported() { return get("grant_types_supported", new TypeReference<>() {}); }
        public void setGrantTypesSupported(List<String> value) { set("grant_types_supported", value); }
        public List<String> getTokenEndpointAuthMethodsSupported() { return get("token_endpoint_auth_methods_supported", new TypeReference<>() {}); }
        public void setTokenEndpointAuthMethodsSupported(List<String> value) { set("token_endpoint_auth_methods_supported", value); }
        public List<String> getRevocationEndpointAuthMethodsSupported() { return get("revocation_endpoint_auth_methods_supported", new TypeReference<>() {}); }
        public void setRevocationEndpointAuthMethodsSupported(List<String> value) { set("revocation_endpoint_auth_methods_supported", value); }
        public List<String> getCodeChallengeMethodsSupported() { return get("code_challenge_methods_supported", new TypeReference<>() {}); }
        public void setCodeChallengeMethodsSupported(List<String> value) { set("code_challenge_methods_supported", value); }
        public List<String> getScopesSupported() { return get("scopes_supported", new TypeReference<>() {}); }
        public void setScopesSupported(List<String> value) { set("scopes_supported", value); }
        public Boolean getAuthorizationResponseIssParameterSupported() { return get("authorization_response_iss_parameter_supported", new TypeReference<>() {}); }
        public void setAuthorizationResponseIssParameterSupported(Boolean value) { set("authorization_response_iss_parameter_supported", value); }
    }
    public static final class OAuthGrant extends Model {
        public OAuthGrant() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public OAuthGrant(JsonNode value) { super(value); }
        public String getId() { return get("id", new TypeReference<>() {}); }
        public void setId(String value) { set("id", value); }
        public String getClientId() { return get("client_id", new TypeReference<>() {}); }
        public void setClientId(String value) { set("client_id", value); }
        public String getClientName() { return get("client_name", new TypeReference<>() {}); }
        public void setClientName(String value) { set("client_name", value); }
        public List<OAuthScope> getScopes() { return get("scopes", new TypeReference<>() {}); }
        public void setScopes(List<OAuthScope> value) { set("scopes", value); }
        public Long getExpiresAt() { return get("expires_at", new TypeReference<>() {}); }
        public void setExpiresAt(Long value) { set("expires_at", value); }
    }
    public static final class OAuthGrantList extends Model {
        public OAuthGrantList() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public OAuthGrantList(JsonNode value) { super(value); }
        public List<OAuthGrant> getData() { return get("data", new TypeReference<>() {}); }
        public void setData(List<OAuthGrant> value) { set("data", value); }
    }
    public static final class LiveEventViewerSettings extends Model {
        public LiveEventViewerSettings() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveEventViewerSettings(JsonNode value) { super(value); }
        public JsonNode getTheme() { return get("theme", new TypeReference<>() {}); }
        public void setTheme(JsonNode value) { set("theme", value); }
        public Long getScale() { return get("scale", new TypeReference<>() {}); }
        public void setScale(Long value) { set("scale", value); }
        public Boolean getPresentation() { return get("presentation", new TypeReference<>() {}); }
        public void setPresentation(Boolean value) { set("presentation", value); }
        public JsonNode getPreset() { return get("preset", new TypeReference<>() {}); }
        public void setPreset(JsonNode value) { set("preset", value); }
        public JsonNode getTransition() { return get("transition", new TypeReference<>() {}); }
        public void setTransition(JsonNode value) { set("transition", value); }
    }
    public static final class LiveEventDesign extends Model {
        public LiveEventDesign() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveEventDesign(JsonNode value) { super(value); }
        public LiveStudioSettings getSettings() { return get("settings", new TypeReference<>() {}); }
        public void setSettings(LiveStudioSettings value) { set("settings", value); }
        public Long getRevision() { return get("revision", new TypeReference<>() {}); }
        public void setRevision(Long value) { set("revision", value); }
        public String getOrganization() { return get("organization", new TypeReference<>() {}); }
        public void setOrganization(String value) { set("organization", value); }
    }
    public static final class LiveEventSnapshot extends Model {
        public LiveEventSnapshot() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveEventSnapshot(JsonNode value) { super(value); }
        public JsonNode getLive() { return get("live", new TypeReference<>() {}); }
        public void setLive(JsonNode value) { set("live", value); }
        public JsonNode getDisplay() { return get("display", new TypeReference<>() {}); }
        public void setDisplay(JsonNode value) { set("display", value); }
        public JsonNode getQueue() { return get("queue", new TypeReference<>() {}); }
        public void setQueue(JsonNode value) { set("queue", value); }
        public LiveEventViewerSettings getViewerSettings() { return get("viewer_settings", new TypeReference<>() {}); }
        public void setViewerSettings(LiveEventViewerSettings value) { set("viewer_settings", value); }
        public LiveEventDesign getDesign() { return get("design", new TypeReference<>() {}); }
        public void setDesign(LiveEventDesign value) { set("design", value); }
    }
    public static final class LiveSnapshotEvent extends Model {
        public LiveSnapshotEvent() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveSnapshotEvent(JsonNode value) { super(value); }
        public Long getSchemaVersion() { return get("schema_version", new TypeReference<>() {}); }
        public void setSchemaVersion(Long value) { set("schema_version", value); }
        public String getType() { return get("type", new TypeReference<>() {}); }
        public void setType(String value) { set("type", value); }
        public String getResourceType() { return get("resource_type", new TypeReference<>() {}); }
        public void setResourceType(String value) { set("resource_type", value); }
        public String getResourceId() { return get("resource_id", new TypeReference<>() {}); }
        public void setResourceId(String value) { set("resource_id", value); }
        public Long getSequence() { return get("sequence", new TypeReference<>() {}); }
        public void setSequence(Long value) { set("sequence", value); }
        public String getCursor() { return get("cursor", new TypeReference<>() {}); }
        public void setCursor(String value) { set("cursor", value); }
        public LiveEventSnapshot getData() { return get("data", new TypeReference<>() {}); }
        public void setData(LiveEventSnapshot value) { set("data", value); }
    }
    public static final class LiveHeartbeatEvent extends Model {
        public LiveHeartbeatEvent() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveHeartbeatEvent(JsonNode value) { super(value); }
        public Long getSchemaVersion() { return get("schema_version", new TypeReference<>() {}); }
        public void setSchemaVersion(Long value) { set("schema_version", value); }
        public String getType() { return get("type", new TypeReference<>() {}); }
        public void setType(String value) { set("type", value); }
        public String getResourceType() { return get("resource_type", new TypeReference<>() {}); }
        public void setResourceType(String value) { set("resource_type", value); }
        public String getResourceId() { return get("resource_id", new TypeReference<>() {}); }
        public void setResourceId(String value) { set("resource_id", value); }
        public Long getSequence() { return get("sequence", new TypeReference<>() {}); }
        public void setSequence(Long value) { set("sequence", value); }
        public String getCursor() { return get("cursor", new TypeReference<>() {}); }
        public void setCursor(String value) { set("cursor", value); }
        public JsonNode getData() { return get("data", new TypeReference<>() {}); }
        public void setData(JsonNode value) { set("data", value); }
    }
    public static final class LiveEvent {
        private final JsonNode value;
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveEvent(JsonNode value) { this.value = value.deepCopy(); }
        @JsonValue public JsonNode value() { return value.deepCopy(); }
        @Override public String toString() { return "LiveEvent[redacted]"; }
    }
    public static final class AccountQuota extends Model {
        public AccountQuota() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public AccountQuota(JsonNode value) { super(value); }
        public String getResource() { return get("resource", new TypeReference<>() {}); }
        public void setResource(String value) { set("resource", value); }
        public String getAction() { return get("action", new TypeReference<>() {}); }
        public void setAction(String value) { set("action", value); }
        public Long getLimit() { return get("limit", new TypeReference<>() {}); }
        public void setLimit(Long value) { set("limit", value); }
        public Long getUsed() { return get("used", new TypeReference<>() {}); }
        public void setUsed(Long value) { set("used", value); }
    }
    public static final class AccountQuotaCollection extends Model {
        public AccountQuotaCollection() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public AccountQuotaCollection(JsonNode value) { super(value); }
        public List<AccountQuota> getData() { return get("data", new TypeReference<>() {}); }
        public void setData(List<AccountQuota> value) { set("data", value); }
    }
    public static final class PersonalTokenMetadata extends Model {
        public PersonalTokenMetadata() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PersonalTokenMetadata(JsonNode value) { super(value); }
        public String getId() { return get("id", new TypeReference<>() {}); }
        public void setId(String value) { set("id", value); }
        public String getLabel() { return get("label", new TypeReference<>() {}); }
        public void setLabel(String value) { set("label", value); }
        public String getSource() { return get("source", new TypeReference<>() {}); }
        public void setSource(String value) { set("source", value); }
        public List<String> getScopes() { return get("scopes", new TypeReference<>() {}); }
        public void setScopes(List<String> value) { set("scopes", value); }
        public Long getIssuedAt() { return get("issued_at", new TypeReference<>() {}); }
        public void setIssuedAt(Long value) { set("issued_at", value); }
        public Long getExpiresAt() { return get("expires_at", new TypeReference<>() {}); }
        public void setExpiresAt(Long value) { set("expires_at", value); }
        public Long getLastUsedAt() { return get("last_used_at", new TypeReference<>() {}); }
        public void setLastUsedAt(Long value) { set("last_used_at", value); }
        public Boolean getRetiring() { return get("retiring", new TypeReference<>() {}); }
        public void setRetiring(Boolean value) { set("retiring", value); }
    }
    public static final class PersonalTokenCollection extends Model {
        public PersonalTokenCollection() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PersonalTokenCollection(JsonNode value) { super(value); }
        public List<PersonalTokenMetadata> getData() { return get("data", new TypeReference<>() {}); }
        public void setData(List<PersonalTokenMetadata> value) { set("data", value); }
        public String getNextCursor() { return get("next_cursor", new TypeReference<>() {}); }
        public void setNextCursor(String value) { set("next_cursor", value); }
    }
    public static final class PersonalDirectoryEntry extends Model {
        public PersonalDirectoryEntry() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PersonalDirectoryEntry(JsonNode value) { super(value); }
        public String getRecordValue() { return get("record", new TypeReference<>() {}); }
        public void setRecordValue(String value) { set("record", value); }
        public String getId() { return get("id", new TypeReference<>() {}); }
        public void setId(String value) { set("id", value); }
        public String getName() { return get("name", new TypeReference<>() {}); }
        public void setName(String value) { set("name", value); }
        public String getHandle() { return get("handle", new TypeReference<>() {}); }
        public void setHandle(String value) { set("handle", value); }
        public String getRole() { return get("role", new TypeReference<>() {}); }
        public void setRole(String value) { set("role", value); }
        public String getVerification() { return get("verification", new TypeReference<>() {}); }
        public void setVerification(String value) { set("verification", value); }
        public String getStatus() { return get("status", new TypeReference<>() {}); }
        public void setStatus(String value) { set("status", value); }
        public String getApplicationId() { return get("application_id", new TypeReference<>() {}); }
        public void setApplicationId(String value) { set("application_id", value); }
        public String getDomain() { return get("domain", new TypeReference<>() {}); }
        public void setDomain(String value) { set("domain", value); }
        public String getState() { return get("state", new TypeReference<>() {}); }
        public void setState(String value) { set("state", value); }
        public String getOrganizationId() { return get("organization_id", new TypeReference<>() {}); }
        public void setOrganizationId(String value) { set("organization_id", value); }
        public String getVerificationUrl() { return get("verification_url", new TypeReference<>() {}); }
        public void setVerificationUrl(String value) { set("verification_url", value); }
        public String getNextAction() { return get("next_action", new TypeReference<>() {}); }
        public void setNextAction(String value) { set("next_action", value); }
    }
    public static final class PersonalDirectory extends Model {
        public PersonalDirectory() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PersonalDirectory(JsonNode value) { super(value); }
        public List<PersonalDirectoryEntry> getData() { return get("data", new TypeReference<>() {}); }
        public void setData(List<PersonalDirectoryEntry> value) { set("data", value); }
        public String getNextCursor() { return get("next_cursor", new TypeReference<>() {}); }
        public void setNextCursor(String value) { set("next_cursor", value); }
    }
    public static final class PersonalOrganizationApplication extends Model {
        public PersonalOrganizationApplication() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PersonalOrganizationApplication(JsonNode value) { super(value); }
        public String getApplicationId() { return get("application_id", new TypeReference<>() {}); }
        public void setApplicationId(String value) { set("application_id", value); }
        public String getName() { return get("name", new TypeReference<>() {}); }
        public void setName(String value) { set("name", value); }
        public String getDomain() { return get("domain", new TypeReference<>() {}); }
        public void setDomain(String value) { set("domain", value); }
        public String getState() { return get("state", new TypeReference<>() {}); }
        public void setState(String value) { set("state", value); }
        public String getOrganizationId() { return get("organization_id", new TypeReference<>() {}); }
        public void setOrganizationId(String value) { set("organization_id", value); }
        public String getVerificationUrl() { return get("verification_url", new TypeReference<>() {}); }
        public void setVerificationUrl(String value) { set("verification_url", value); }
        public String getNextAction() { return get("next_action", new TypeReference<>() {}); }
        public void setNextAction(String value) { set("next_action", value); }
    }
    public static final class TranslationChapterMetadata extends Model {
        public TranslationChapterMetadata() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public TranslationChapterMetadata(JsonNode value) { super(value); }
        public Long getChapter() { return get("chapter", new TypeReference<>() {}); }
        public void setChapter(Long value) { set("chapter", value); }
        public Long getVerses() { return get("verses", new TypeReference<>() {}); }
        public void setVerses(Long value) { set("verses", value); }
    }
    public static final class TranslationBookMetadata extends Model {
        public TranslationBookMetadata() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public TranslationBookMetadata(JsonNode value) { super(value); }
        public Long getBook() { return get("book", new TypeReference<>() {}); }
        public void setBook(Long value) { set("book", value); }
        public String getName() { return get("name", new TypeReference<>() {}); }
        public void setName(String value) { set("name", value); }
        public List<TranslationChapterMetadata> getChapters() { return get("chapters", new TypeReference<>() {}); }
        public void setChapters(List<TranslationChapterMetadata> value) { set("chapters", value); }
    }
    public static final class TranslationCatalogueDetails extends Model {
        public TranslationCatalogueDetails() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public TranslationCatalogueDetails(JsonNode value) { super(value); }
        public String getTranslation() { return get("translation", new TypeReference<>() {}); }
        public void setTranslation(String value) { set("translation", value); }
        public List<TranslationBookMetadata> getData() { return get("data", new TypeReference<>() {}); }
        public void setData(List<TranslationBookMetadata> value) { set("data", value); }
    }
    public static final class LiveStackReference extends Model {
        public LiveStackReference() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveStackReference(JsonNode value) { super(value); }
        public String getTranslation() { return get("translation", new TypeReference<>() {}); }
        public void setTranslation(String value) { set("translation", value); }
        public Long getBookId() { return get("book_id", new TypeReference<>() {}); }
        public void setBookId(Long value) { set("book_id", value); }
        public Long getChapter() { return get("chapter", new TypeReference<>() {}); }
        public void setChapter(Long value) { set("chapter", value); }
        public Long getVerse() { return get("verse", new TypeReference<>() {}); }
        public void setVerse(Long value) { set("verse", value); }
    }
    public static final class LiveStackPush extends Model {
        public LiveStackPush() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveStackPush(JsonNode value) { super(value); }
        public LiveStackReference getReference() { return get("reference", new TypeReference<>() {}); }
        public void setReference(LiveStackReference value) { set("reference", value); }
    }
    public static final class LiveStackRemoval extends Model {
        public LiveStackRemoval() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveStackRemoval(JsonNode value) { super(value); }
        public Long getCount() { return get("count", new TypeReference<>() {}); }
        public void setCount(Long value) { set("count", value); }
    }
    public static final class LiveStackVerse extends Model {
        public LiveStackVerse() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveStackVerse(JsonNode value) { super(value); }
        public Long getBookId() { return get("book_id", new TypeReference<>() {}); }
        public void setBookId(Long value) { set("book_id", value); }
        public String getBook() { return get("book", new TypeReference<>() {}); }
        public void setBook(String value) { set("book", value); }
        public Long getChapter() { return get("chapter", new TypeReference<>() {}); }
        public void setChapter(Long value) { set("chapter", value); }
        public Long getVerse() { return get("verse", new TypeReference<>() {}); }
        public void setVerse(Long value) { set("verse", value); }
        public String getText() { return get("text", new TypeReference<>() {}); }
        public void setText(String value) { set("text", value); }
        public String getTranslation() { return get("translation", new TypeReference<>() {}); }
        public void setTranslation(String value) { set("translation", value); }
        public TranslationRights getRights() { return get("rights", new TypeReference<>() {}); }
        public void setRights(TranslationRights value) { set("rights", value); }
        public String getAttribution() { return get("attribution", new TypeReference<>() {}); }
        public void setAttribution(String value) { set("attribution", value); }
        public Long getPosition() { return get("position", new TypeReference<>() {}); }
        public void setPosition(Long value) { set("position", value); }
    }
    public static final class LiveStack extends Model {
        public LiveStack() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveStack(JsonNode value) { super(value); }
        public String getLiveId() { return get("live_id", new TypeReference<>() {}); }
        public void setLiveId(String value) { set("live_id", value); }
        public Long getSequence() { return get("sequence", new TypeReference<>() {}); }
        public void setSequence(Long value) { set("sequence", value); }
        public Long getAffected() { return get("affected", new TypeReference<>() {}); }
        public void setAffected(Long value) { set("affected", value); }
        public List<LiveStackVerse> getData() { return get("data", new TypeReference<>() {}); }
        public void setData(List<LiveStackVerse> value) { set("data", value); }
    }
    public static final class LiveSettings extends Model {
        public LiveSettings() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveSettings(JsonNode value) { super(value); }
        public String getLiveId() { return get("live_id", new TypeReference<>() {}); }
        public void setLiveId(String value) { set("live_id", value); }
        public Long getSequence() { return get("sequence", new TypeReference<>() {}); }
        public void setSequence(Long value) { set("sequence", value); }
        public String getReference() { return get("reference", new TypeReference<>() {}); }
        public void setReference(String value) { set("reference", value); }
        public List<String> getTranslations() { return get("translations", new TypeReference<>() {}); }
        public void setTranslations(List<String> value) { set("translations", value); }
    }
    public static final class LiveSettingsPatch extends Model {
        public LiveSettingsPatch() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveSettingsPatch(JsonNode value) { super(value); }
        public String getReference() { return get("reference", new TypeReference<>() {}); }
        public void setReference(String value) { set("reference", value); }
        public List<String> getTranslations() { return get("translations", new TypeReference<>() {}); }
        public void setTranslations(List<String> value) { set("translations", value); }
    }
    public static final class LiveSubscriber extends Model {
        public LiveSubscriber() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveSubscriber(JsonNode value) { super(value); }
        public String getUser() { return get("user", new TypeReference<>() {}); }
        public void setUser(String value) { set("user", value); }
        public String getAccess() { return get("access", new TypeReference<>() {}); }
        public void setAccess(String value) { set("access", value); }
        public Long getConnectedAt() { return get("connected_at", new TypeReference<>() {}); }
        public void setConnectedAt(Long value) { set("connected_at", value); }
        public Long getConnectedForSeconds() { return get("connected_for_seconds", new TypeReference<>() {}); }
        public void setConnectedForSeconds(Long value) { set("connected_for_seconds", value); }
    }
    public static final class LiveStatistics extends Model {
        public LiveStatistics() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveStatistics(JsonNode value) { super(value); }
        public String getId() { return get("id", new TypeReference<>() {}); }
        public void setId(String value) { set("id", value); }
        public Long getSequence() { return get("sequence", new TypeReference<>() {}); }
        public void setSequence(Long value) { set("sequence", value); }
        public Long getRunningForSeconds() { return get("running_for_seconds", new TypeReference<>() {}); }
        public void setRunningForSeconds(Long value) { set("running_for_seconds", value); }
        public Long getStackEntries() { return get("stack_entries", new TypeReference<>() {}); }
        public void setStackEntries(Long value) { set("stack_entries", value); }
        public Long getConnections() { return get("connections", new TypeReference<>() {}); }
        public void setConnections(Long value) { set("connections", value); }
        public Long getUserConnections() { return get("user_connections", new TypeReference<>() {}); }
        public void setUserConnections(Long value) { set("user_connections", value); }
        public Long getAnonymousConnections() { return get("anonymous_connections", new TypeReference<>() {}); }
        public void setAnonymousConnections(Long value) { set("anonymous_connections", value); }
        public List<LiveSubscriber> getSubscribers() { return get("subscribers", new TypeReference<>() {}); }
        public void setSubscribers(List<LiveSubscriber> value) { set("subscribers", value); }
    }
    public static final class LiveAudienceSecret extends Model {
        public LiveAudienceSecret() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveAudienceSecret(JsonNode value) { super(value); }
        public String getLiveId() { return get("live_id", new TypeReference<>() {}); }
        public void setLiveId(String value) { set("live_id", value); }
        public Long getSequence() { return get("sequence", new TypeReference<>() {}); }
        public void setSequence(Long value) { set("sequence", value); }
        public Boolean getProtectedValue() { return get("protected", new TypeReference<>() {}); }
        public void setProtectedValue(Boolean value) { set("protected", value); }
        public String getSecret() { return get("secret", new TypeReference<>() {}); }
        public void setSecret(String value) { set("secret", value); }
    }
    public static final class LiveBulkDeletion extends Model {
        public LiveBulkDeletion() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveBulkDeletion(JsonNode value) { super(value); }
        public Long getDeleted() { return get("deleted", new TypeReference<>() {}); }
        public void setDeleted(Long value) { set("deleted", value); }
    }
    public static final class ScriptureBookRead extends Model {
        public ScriptureBookRead() {}
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public ScriptureBookRead(JsonNode value) { super(value); }
        public String getTranslation() { return get("translation", new TypeReference<>() {}); }
        public void setTranslation(String value) { set("translation", value); }
        public TranslationRights getRights() { return get("rights", new TypeReference<>() {}); }
        public void setRights(TranslationRights value) { set("rights", value); }
        public List<Verse> getVerses() { return get("verses", new TypeReference<>() {}); }
        public void setVerses(List<Verse> value) { set("verses", value); }
        public String getNextCursor() { return get("next_cursor", new TypeReference<>() {}); }
        public void setNextCursor(String value) { set("next_cursor", value); }
    }
}
