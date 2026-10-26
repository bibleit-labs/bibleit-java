package app.bibleit;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.math.BigDecimal;
import java.util.List;

/** Java schema beans preserving missing fields, explicit nulls and unknown fields. */
public final class Models {
    private Models() {}
    /**
     * Base object model retaining the complete wire representation.
     */
    public abstract static class Model {
        private final ObjectNode fields;
        /**
         * Creates an empty JSON object.
         */
        protected Model() { fields = Json.MAPPER.createObjectNode(); }
        /**
         * Copies a JSON object into this model.
         * @param value the object to copy
         * @throws IllegalArgumentException if the value is not an object
         */
        protected Model(JsonNode value) {
            if (!value.isObject()) throw new IllegalArgumentException("Expected a JSON object");
            fields = (ObjectNode) value.deepCopy();
        }
        /**
         * Returns a defensive copy of all wire fields.
         * @return the copied JSON object
         */
        @JsonValue public JsonNode toJson() { return fields.deepCopy(); }
        /**
         * Checks whether a field is present, including explicit null.
         * @param name the wire field name
         * @return true when the field exists
         */
        public boolean hasField(String name) { return fields.has(name); }
        /**
         * Removes a field so it is omitted when serialized.
         * @param name the wire field name
         */
        public void removeField(String name) { fields.remove(name); }
        /**
         * Sets an arbitrary wire field while preserving future server fields.
         * @param name the wire field name
         * @param value the copied value; null stores an explicit JSON null
         */
        public void setField(String name, JsonNode value) { fields.set(name, value == null ? Json.MAPPER.nullNode() : value.deepCopy()); }
        /**
         * Decodes one field into its Java representation.
         * @param <T> the field type
         * @param name the wire field name
         * @param type the Jackson target type
         * @return the decoded field value
         */
        protected <T> T get(String name, TypeReference<T> type) { return Json.MAPPER.convertValue(fields.get(name), type); }
        /**
         * Serializes a Java field into the wire representation.
         * @param name the wire field name
         * @param value the field value; null stores an explicit JSON null
         */
        protected void set(String name, Object value) { fields.set(name, Json.MAPPER.valueToTree(value)); }
        @Override public String toString() { return getClass().getSimpleName() + "[redacted]"; }
    }
    /**
     * Wire representation of the problem schema.
     */
    public static final class Problem extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public Problem() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public Problem(JsonNode value) { super(value); }
        /**
         * Returns type. The type value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getType() { return get("type", new TypeReference<>() {}); }
        /**
         * Sets type.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setType(JsonNode value) { set("type", value); }
        /**
         * Returns title. The title value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getTitle() { return get("title", new TypeReference<>() {}); }
        /**
         * Sets title.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setTitle(String value) { set("title", value); }
        /**
         * Returns status. The status value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getStatus() { return get("status", new TypeReference<>() {}); }
        /**
         * Sets status.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setStatus(Long value) { set("status", value); }
        /**
         * Returns code. The code value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getCode() { return get("code", new TypeReference<>() {}); }
        /**
         * Sets code.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setCode(String value) { set("code", value); }
        /**
         * Returns request id. The request id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getRequestId() { return get("request_id", new TypeReference<>() {}); }
        /**
         * Sets request id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setRequestId(String value) { set("request_id", value); }
        /**
         * Returns detail. Safe explanation and recovery guidance, including invalid, mismatched and stale Scripture cursors.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getDetail() { return get("detail", new TypeReference<>() {}); }
        /**
         * Sets detail.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setDetail(String value) { set("detail", value); }
    }
    /**
     * Wire representation of the scope schema.
     */
    public static final class Scope {
        private final JsonNode value;
        /**
         * Copies a scalar or union wire value without adding wrapper fields.
         * @param value the non-null JSON value
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public Scope(JsonNode value) { this.value = value.deepCopy(); }
        /**
         * Returns the underlying wire representation.
         * @return a defensive copy of the JSON value
         */
        @JsonValue public JsonNode value() { return value.deepCopy(); }
        @Override public String toString() { return "Scope[redacted]"; }
    }
    /**
     * Wire representation of the service token schema.
     */
    public static final class ServiceToken extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public ServiceToken() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public ServiceToken(JsonNode value) { super(value); }
        /**
         * Returns id. The id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getId() { return get("id", new TypeReference<>() {}); }
        /**
         * Sets id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setId(String value) { set("id", value); }
        /**
         * Returns organization id. The organization id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getOrganizationId() { return get("organization_id", new TypeReference<>() {}); }
        /**
         * Sets organization id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setOrganizationId(String value) { set("organization_id", value); }
        /**
         * Returns label. The label value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getLabel() { return get("label", new TypeReference<>() {}); }
        /**
         * Sets label.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setLabel(String value) { set("label", value); }
        /**
         * Returns scopes. The scopes value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<Scope> getScopes() { return get("scopes", new TypeReference<>() {}); }
        /**
         * Sets scopes.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setScopes(List<Scope> value) { set("scopes", value); }
        /**
         * Returns issued at. The issued at value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getIssuedAt() { return get("issued_at", new TypeReference<>() {}); }
        /**
         * Sets issued at.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setIssuedAt(Long value) { set("issued_at", value); }
        /**
         * Returns expires at. Unix timestamp in seconds; null means no deadline or no recorded use.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getExpiresAt() { return get("expires_at", new TypeReference<>() {}); }
        /**
         * Sets expires at.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setExpiresAt(Long value) { set("expires_at", value); }
        /**
         * Returns last used at. Unix timestamp in seconds; null means no deadline or no recorded use.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getLastUsedAt() { return get("last_used_at", new TypeReference<>() {}); }
        /**
         * Sets last used at.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setLastUsedAt(Long value) { set("last_used_at", value); }
    }
    /**
     * Wire representation of the create service token schema.
     */
    public static final class CreateServiceToken extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public CreateServiceToken() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public CreateServiceToken(JsonNode value) { super(value); }
        /**
         * Returns label. At most 128 UTF-8 bytes.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getLabel() { return get("label", new TypeReference<>() {}); }
        /**
         * Sets label.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setLabel(String value) { set("label", value); }
        /**
         * Returns scopes. Every scope must be held by the issuer; duplicate entries are normalized.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<Scope> getScopes() { return get("scopes", new TypeReference<>() {}); }
        /**
         * Sets scopes.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setScopes(List<Scope> value) { set("scopes", value); }
        /**
         * Returns expires at. Future Unix seconds, at most 90 days ahead. Omit for 30 days; null explicitly permits no expiration.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getExpiresAt() { return get("expires_at", new TypeReference<>() {}); }
        /**
         * Sets expires at.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setExpiresAt(Long value) { set("expires_at", value); }
    }
    /**
     * Wire representation of the issued service token schema.
     */
    public static final class IssuedServiceToken extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public IssuedServiceToken() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public IssuedServiceToken(JsonNode value) { super(value); }
        /**
         * Returns id. The id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getId() { return get("id", new TypeReference<>() {}); }
        /**
         * Sets id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setId(String value) { set("id", value); }
        /**
         * Returns organization id. The organization id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getOrganizationId() { return get("organization_id", new TypeReference<>() {}); }
        /**
         * Sets organization id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setOrganizationId(String value) { set("organization_id", value); }
        /**
         * Returns label. The label value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getLabel() { return get("label", new TypeReference<>() {}); }
        /**
         * Sets label.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setLabel(String value) { set("label", value); }
        /**
         * Returns scopes. The scopes value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<Scope> getScopes() { return get("scopes", new TypeReference<>() {}); }
        /**
         * Sets scopes.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setScopes(List<Scope> value) { set("scopes", value); }
        /**
         * Returns expires at. Unix timestamp in seconds; null means no deadline or no recorded use.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getExpiresAt() { return get("expires_at", new TypeReference<>() {}); }
        /**
         * Sets expires at.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setExpiresAt(Long value) { set("expires_at", value); }
        /**
         * Returns secret. Returned once. Store in a confidential backend.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getSecret() { return get("secret", new TypeReference<>() {}); }
        /**
         * Sets secret.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setSecret(String value) { set("secret", value); }
    }
    /**
     * Wire representation of the live summary schema.
     */
    public static final class LiveSummary extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public LiveSummary() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveSummary(JsonNode value) { super(value); }
        /**
         * Returns id. The id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getId() { return get("id", new TypeReference<>() {}); }
        /**
         * Sets id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setId(String value) { set("id", value); }
        /**
         * Returns organization id. The organization id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getOrganizationId() { return get("organization_id", new TypeReference<>() {}); }
        /**
         * Sets organization id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setOrganizationId(String value) { set("organization_id", value); }
        /**
         * Returns name. The name value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getName() { return get("name", new TypeReference<>() {}); }
        /**
         * Sets name.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setName(String value) { set("name", value); }
    }
    /**
     * Wire representation of the create live schema.
     */
    public static final class CreateLive extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public CreateLive() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public CreateLive(JsonNode value) { super(value); }
        /**
         * Returns name. At most 128 UTF-8 bytes.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getName() { return get("name", new TypeReference<>() {}); }
        /**
         * Sets name.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setName(String value) { set("name", value); }
    }
    /**
     * Wire representation of the live schema.
     */
    public static final class Live extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public Live() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public Live(JsonNode value) { super(value); }
        /**
         * Returns id. The id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getId() { return get("id", new TypeReference<>() {}); }
        /**
         * Sets id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setId(String value) { set("id", value); }
        /**
         * Returns organization id. The organization id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getOrganizationId() { return get("organization_id", new TypeReference<>() {}); }
        /**
         * Sets organization id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setOrganizationId(String value) { set("organization_id", value); }
        /**
         * Returns name. The name value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getName() { return get("name", new TypeReference<>() {}); }
        /**
         * Sets name.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setName(String value) { set("name", value); }
        /**
         * Returns status. The status value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getStatus() { return get("status", new TypeReference<>() {}); }
        /**
         * Sets status.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setStatus(String value) { set("status", value); }
        /**
         * Returns mode. The mode value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getMode() { return get("mode", new TypeReference<>() {}); }
        /**
         * Sets mode.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setMode(String value) { set("mode", value); }
        /**
         * Returns paused. The paused value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Boolean getPaused() { return get("paused", new TypeReference<>() {}); }
        /**
         * Sets paused.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setPaused(Boolean value) { set("paused", value); }
        /**
         * Returns sequence. The sequence value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getSequence() { return get("sequence", new TypeReference<>() {}); }
        /**
         * Sets sequence.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setSequence(Long value) { set("sequence", value); }
    }
    /**
     * Wire representation of the service token list schema.
     */
    public static final class ServiceTokenList extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public ServiceTokenList() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public ServiceTokenList(JsonNode value) { super(value); }
        /**
         * Returns data. The data value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<ServiceToken> getData() { return get("data", new TypeReference<>() {}); }
        /**
         * Sets data.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setData(List<ServiceToken> value) { set("data", value); }
        /**
         * Returns next cursor. Pass this value unchanged as after; null ends the collection.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getNextCursor() { return get("next_cursor", new TypeReference<>() {}); }
        /**
         * Sets next cursor.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setNextCursor(String value) { set("next_cursor", value); }
    }
    /**
     * Wire representation of the live list schema.
     */
    public static final class LiveList extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public LiveList() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveList(JsonNode value) { super(value); }
        /**
         * Returns data. The data value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<LiveSummary> getData() { return get("data", new TypeReference<>() {}); }
        /**
         * Sets data.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setData(List<LiveSummary> value) { set("data", value); }
        /**
         * Returns next cursor. Pass this value unchanged as after; null ends the collection.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getNextCursor() { return get("next_cursor", new TypeReference<>() {}); }
        /**
         * Sets next cursor.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setNextCursor(String value) { set("next_cursor", value); }
    }
    /**
     * Wire representation of the translation rights schema.
     */
    public static final class TranslationRights extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public TranslationRights() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public TranslationRights(JsonNode value) { super(value); }
        /**
         * Returns edition. The edition value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getEdition() { return get("edition", new TypeReference<>() {}); }
        /**
         * Sets edition.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setEdition(String value) { set("edition", value); }
        /**
         * Returns rights status. The rights status value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getRightsStatus() { return get("rights_status", new TypeReference<>() {}); }
        /**
         * Sets rights status.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setRightsStatus(String value) { set("rights_status", value); }
        /**
         * Returns license id. The license id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getLicenseId() { return get("license_id", new TypeReference<>() {}); }
        /**
         * Sets license id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setLicenseId(String value) { set("license_id", value); }
        /**
         * Returns license url. The license url value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getLicenseUrl() { return get("license_url", new TypeReference<>() {}); }
        /**
         * Sets license url.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setLicenseUrl(String value) { set("license_url", value); }
        /**
         * Returns source url. The source url value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getSourceUrl() { return get("source_url", new TypeReference<>() {}); }
        /**
         * Sets source url.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setSourceUrl(String value) { set("source_url", value); }
        /**
         * Returns copyright. The copyright value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getCopyright() { return get("copyright", new TypeReference<>() {}); }
        /**
         * Sets copyright.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setCopyright(String value) { set("copyright", value); }
        /**
         * Returns change notice. The change notice value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getChangeNotice() { return get("change_notice", new TypeReference<>() {}); }
        /**
         * Sets change notice.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setChangeNotice(String value) { set("change_notice", value); }
        /**
         * Returns attribution. The attribution value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getAttribution() { return get("attribution", new TypeReference<>() {}); }
        /**
         * Sets attribution.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setAttribution(String value) { set("attribution", value); }
    }
    /**
     * Wire representation of the verse schema.
     */
    public static final class Verse extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public Verse() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public Verse(JsonNode value) { super(value); }
        /**
         * Returns book id. The book id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getBookId() { return get("book_id", new TypeReference<>() {}); }
        /**
         * Sets book id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setBookId(Long value) { set("book_id", value); }
        /**
         * Returns book. The book value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getBook() { return get("book", new TypeReference<>() {}); }
        /**
         * Sets book.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setBook(String value) { set("book", value); }
        /**
         * Returns chapter. The chapter value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getChapter() { return get("chapter", new TypeReference<>() {}); }
        /**
         * Sets chapter.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setChapter(Long value) { set("chapter", value); }
        /**
         * Returns verse. The verse value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getVerse() { return get("verse", new TypeReference<>() {}); }
        /**
         * Sets verse.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setVerse(Long value) { set("verse", value); }
        /**
         * Returns text. The text value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getText() { return get("text", new TypeReference<>() {}); }
        /**
         * Sets text.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setText(String value) { set("text", value); }
    }
    /**
     * Wire representation of the scripture read schema.
     */
    public static final class ScriptureRead extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public ScriptureRead() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public ScriptureRead(JsonNode value) { super(value); }
        /**
         * Returns translation. The translation value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getTranslation() { return get("translation", new TypeReference<>() {}); }
        /**
         * Sets translation.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setTranslation(String value) { set("translation", value); }
        /**
         * Returns rights. The rights value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public TranslationRights getRights() { return get("rights", new TypeReference<>() {}); }
        /**
         * Sets rights.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setRights(TranslationRights value) { set("rights", value); }
        /**
         * Returns verses. The verses value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<Verse> getVerses() { return get("verses", new TypeReference<>() {}); }
        /**
         * Sets verses.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setVerses(List<Verse> value) { set("verses", value); }
    }
    /**
     * Wire representation of the scripture search schema.
     */
    public static final class ScriptureSearch extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public ScriptureSearch() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public ScriptureSearch(JsonNode value) { super(value); }
        /**
         * Returns translation. The translation value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getTranslation() { return get("translation", new TypeReference<>() {}); }
        /**
         * Sets translation.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setTranslation(String value) { set("translation", value); }
        /**
         * Returns rights. The rights value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public TranslationRights getRights() { return get("rights", new TypeReference<>() {}); }
        /**
         * Sets rights.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setRights(TranslationRights value) { set("rights", value); }
        /**
         * Returns verses. The verses value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<Verse> getVerses() { return get("verses", new TypeReference<>() {}); }
        /**
         * Sets verses.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setVerses(List<Verse> value) { set("verses", value); }
        /**
         * Returns has more. The has more value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Boolean getHasMore() { return get("has_more", new TypeReference<>() {}); }
        /**
         * Sets has more.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setHasMore(Boolean value) { set("has_more", value); }
    }
    /**
     * Wire representation of the display verse schema.
     */
    public static final class DisplayVerse extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public DisplayVerse() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public DisplayVerse(JsonNode value) { super(value); }
        /**
         * Returns book id. The book id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getBookId() { return get("book_id", new TypeReference<>() {}); }
        /**
         * Sets book id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setBookId(Long value) { set("book_id", value); }
        /**
         * Returns book. The book value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getBook() { return get("book", new TypeReference<>() {}); }
        /**
         * Sets book.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setBook(String value) { set("book", value); }
        /**
         * Returns chapter. The chapter value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getChapter() { return get("chapter", new TypeReference<>() {}); }
        /**
         * Sets chapter.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setChapter(Long value) { set("chapter", value); }
        /**
         * Returns verse. The verse value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getVerse() { return get("verse", new TypeReference<>() {}); }
        /**
         * Sets verse.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setVerse(Long value) { set("verse", value); }
        /**
         * Returns text. The text value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getText() { return get("text", new TypeReference<>() {}); }
        /**
         * Sets text.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setText(String value) { set("text", value); }
        /**
         * Returns translation. The translation value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getTranslation() { return get("translation", new TypeReference<>() {}); }
        /**
         * Sets translation.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setTranslation(String value) { set("translation", value); }
        /**
         * Returns rights. The rights value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public TranslationRights getRights() { return get("rights", new TypeReference<>() {}); }
        /**
         * Sets rights.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setRights(TranslationRights value) { set("rights", value); }
        /**
         * Returns attribution. The attribution value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getAttribution() { return get("attribution", new TypeReference<>() {}); }
        /**
         * Sets attribution.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setAttribution(String value) { set("attribution", value); }
    }
    /**
     * Wire representation of the scripture reference schema.
     */
    public static final class ScriptureReference extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public ScriptureReference() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public ScriptureReference(JsonNode value) { super(value); }
        /**
         * Returns translation. The translation value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getTranslation() { return get("translation", new TypeReference<>() {}); }
        /**
         * Sets translation.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setTranslation(String value) { set("translation", value); }
        /**
         * Returns book id. The book id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getBookId() { return get("book_id", new TypeReference<>() {}); }
        /**
         * Sets book id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setBookId(Long value) { set("book_id", value); }
        /**
         * Returns chapter. The chapter value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getChapter() { return get("chapter", new TypeReference<>() {}); }
        /**
         * Sets chapter.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setChapter(Long value) { set("chapter", value); }
        /**
         * Returns verse start. The verse start value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getVerseStart() { return get("verse_start", new TypeReference<>() {}); }
        /**
         * Sets verse start.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setVerseStart(Long value) { set("verse_start", value); }
        /**
         * Returns verse end. The verse end value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getVerseEnd() { return get("verse_end", new TypeReference<>() {}); }
        /**
         * Sets verse end.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setVerseEnd(Long value) { set("verse_end", value); }
    }
    /**
     * Replace with credited scripture, an explicitly selected saved presentation revision, or clear. Scripture needs translation.read; presentation needs presentation.present in addition to live.update. Saving a presentation never publishes it. Scripture ranges contain 1..100 existing verses in one chapter.
     */
    public static final class ReplaceDisplay {
        private final JsonNode value;
        /**
         * Copies a scalar or union wire value without adding wrapper fields.
         * @param value the non-null JSON value
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public ReplaceDisplay(JsonNode value) { this.value = value.deepCopy(); }
        /**
         * Returns the underlying wire representation.
         * @return a defensive copy of the JSON value
         */
        @JsonValue public JsonNode value() { return value.deepCopy(); }
        @Override public String toString() { return "ReplaceDisplay[redacted]"; }
    }
    /**
     * Change lifecycle status and/or pause state atomically. Pausing a stopped Live returns 409. Stopping resets pause and visibility; starting alone does not redisplay content. paused:false resumes retained content on a running Live.
     */
    public static final class PlaybackPatch extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public PlaybackPatch() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PlaybackPatch(JsonNode value) { super(value); }
        /**
         * Returns status. The status value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getStatus() { return get("status", new TypeReference<>() {}); }
        /**
         * Sets status.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setStatus(JsonNode value) { set("status", value); }
        /**
         * Returns paused. The paused value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Boolean getPaused() { return get("paused", new TypeReference<>() {}); }
        /**
         * Sets paused.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setPaused(Boolean value) { set("paused", value); }
    }
    /**
     * Wire representation of the playback schema.
     */
    public static final class Playback extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public Playback() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public Playback(JsonNode value) { super(value); }
        /**
         * Returns live id. The live id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getLiveId() { return get("live_id", new TypeReference<>() {}); }
        /**
         * Sets live id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setLiveId(String value) { set("live_id", value); }
        /**
         * Returns status. The status value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getStatus() { return get("status", new TypeReference<>() {}); }
        /**
         * Sets status.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setStatus(JsonNode value) { set("status", value); }
        /**
         * Returns paused. The paused value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Boolean getPaused() { return get("paused", new TypeReference<>() {}); }
        /**
         * Sets paused.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setPaused(Boolean value) { set("paused", value); }
        /**
         * Returns showing. The showing value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Boolean getShowing() { return get("showing", new TypeReference<>() {}); }
        /**
         * Sets showing.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setShowing(Boolean value) { set("showing", value); }
        /**
         * Returns sequence. The sequence value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getSequence() { return get("sequence", new TypeReference<>() {}); }
        /**
         * Sets sequence.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setSequence(Long value) { set("sequence", value); }
    }
    /**
     * Wire representation of the snapshot summary schema.
     */
    public static final class SnapshotSummary extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public SnapshotSummary() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public SnapshotSummary(JsonNode value) { super(value); }
        /**
         * Returns id. The id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getId() { return get("id", new TypeReference<>() {}); }
        /**
         * Sets id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setId(String value) { set("id", value); }
        /**
         * Returns deck. The deck value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getDeck() { return get("deck", new TypeReference<>() {}); }
        /**
         * Sets deck.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setDeck(String value) { set("deck", value); }
        /**
         * Returns revision. The revision value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getRevision() { return get("revision", new TypeReference<>() {}); }
        /**
         * Sets revision.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setRevision(Long value) { set("revision", value); }
        /**
         * Returns index. The index value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getIndex() { return get("index", new TypeReference<>() {}); }
        /**
         * Sets index.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setIndex(Long value) { set("index", value); }
        /**
         * Returns blank. The blank value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Boolean getBlank() { return get("blank", new TypeReference<>() {}); }
        /**
         * Sets blank.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setBlank(Boolean value) { set("blank", value); }
        /**
         * Returns slide id. Stable selected slide ID from the immutable saved snapshot. Prefer this over the retained zero-based index.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getSlideId() { return get("slide_id", new TypeReference<>() {}); }
        /**
         * Sets slide id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setSlideId(String value) { set("slide_id", value); }
    }
    /**
     * Wire representation of the live display schema.
     */
    public static final class LiveDisplay extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public LiveDisplay() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveDisplay(JsonNode value) { super(value); }
        /**
         * Returns live id. The live id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getLiveId() { return get("live_id", new TypeReference<>() {}); }
        /**
         * Sets live id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setLiveId(String value) { set("live_id", value); }
        /**
         * Returns status. The status value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getStatus() { return get("status", new TypeReference<>() {}); }
        /**
         * Sets status.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setStatus(JsonNode value) { set("status", value); }
        /**
         * Returns paused. The paused value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Boolean getPaused() { return get("paused", new TypeReference<>() {}); }
        /**
         * Sets paused.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setPaused(Boolean value) { set("paused", value); }
        /**
         * Returns showing. The showing value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Boolean getShowing() { return get("showing", new TypeReference<>() {}); }
        /**
         * Sets showing.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setShowing(Boolean value) { set("showing", value); }
        /**
         * Returns sequence. The sequence value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getSequence() { return get("sequence", new TypeReference<>() {}); }
        /**
         * Sets sequence.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setSequence(Long value) { set("sequence", value); }
        /**
         * Returns organization id. The organization id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getOrganizationId() { return get("organization_id", new TypeReference<>() {}); }
        /**
         * Sets organization id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setOrganizationId(String value) { set("organization_id", value); }
        /**
         * Returns type. The type value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getType() { return get("type", new TypeReference<>() {}); }
        /**
         * Sets type.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setType(JsonNode value) { set("type", value); }
        /**
         * Returns verses. The verses value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<DisplayVerse> getVerses() { return get("verses", new TypeReference<>() {}); }
        /**
         * Sets verses.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setVerses(List<DisplayVerse> value) { set("verses", value); }
        /**
         * Returns snapshot. The snapshot value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getSnapshot() { return get("snapshot", new TypeReference<>() {}); }
        /**
         * Sets snapshot.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setSnapshot(JsonNode value) { set("snapshot", value); }
    }
    /**
     * Wire representation of the service token configuration schema.
     */
    public static final class ServiceTokenConfiguration extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public ServiceTokenConfiguration() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public ServiceTokenConfiguration(JsonNode value) { super(value); }
        /**
         * Returns id. The id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getId() { return get("id", new TypeReference<>() {}); }
        /**
         * Sets id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setId(String value) { set("id", value); }
        /**
         * Returns organization id. The organization id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getOrganizationId() { return get("organization_id", new TypeReference<>() {}); }
        /**
         * Sets organization id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setOrganizationId(String value) { set("organization_id", value); }
        /**
         * Returns label. The label value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getLabel() { return get("label", new TypeReference<>() {}); }
        /**
         * Sets label.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setLabel(String value) { set("label", value); }
        /**
         * Returns scopes. The scopes value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<Scope> getScopes() { return get("scopes", new TypeReference<>() {}); }
        /**
         * Sets scopes.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setScopes(List<Scope> value) { set("scopes", value); }
        /**
         * Returns issued at. The issued at value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getIssuedAt() { return get("issued_at", new TypeReference<>() {}); }
        /**
         * Sets issued at.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setIssuedAt(Long value) { set("issued_at", value); }
        /**
         * Returns expires at. Unix timestamp in seconds; null means no deadline or no recorded use.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getExpiresAt() { return get("expires_at", new TypeReference<>() {}); }
        /**
         * Sets expires at.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setExpiresAt(Long value) { set("expires_at", value); }
    }
    /**
     * Partial configuration edit. Omitted label/scopes/expiry are preserved exactly. expires_at:null explicitly permits no expiration; finite deadlines must be future and at most 90 days ahead. The resulting scopes must be a subset of the caller scopes. Repeated authentication does not change the configuration ETag.
     */
    public static final class PatchServiceToken extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public PatchServiceToken() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PatchServiceToken(JsonNode value) { super(value); }
        /**
         * Returns label. At most 128 UTF-8 bytes.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getLabel() { return get("label", new TypeReference<>() {}); }
        /**
         * Sets label.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setLabel(String value) { set("label", value); }
        /**
         * Returns scopes. Every scope must be held by the issuer; duplicate entries are normalized.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<Scope> getScopes() { return get("scopes", new TypeReference<>() {}); }
        /**
         * Sets scopes.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setScopes(List<Scope> value) { set("scopes", value); }
        /**
         * Returns expires at. Future Unix seconds, at most 90 days ahead. Omit to preserve the exact existing expiry; null explicitly selects no expiration.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getExpiresAt() { return get("expires_at", new TypeReference<>() {}); }
        /**
         * Sets expires at.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setExpiresAt(Long value) { set("expires_at", value); }
    }
    /**
     * Wire representation of the empty object schema.
     */
    public static final class EmptyObject extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public EmptyObject() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public EmptyObject(JsonNode value) { super(value); }
    }
    /**
     * Wire representation of the translation provenance schema.
     */
    public static final class TranslationProvenance extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public TranslationProvenance() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public TranslationProvenance(JsonNode value) { super(value); }
        /**
         * Returns archive sha256. The archive sha256 value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getArchiveSha256() { return get("archive_sha256", new TypeReference<>() {}); }
        /**
         * Sets archive sha256.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setArchiveSha256(String value) { set("archive_sha256", value); }
        /**
         * Returns reviewed on. The reviewed on value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getReviewedOn() { return get("reviewed_on", new TypeReference<>() {}); }
        /**
         * Sets reviewed on.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setReviewedOn(String value) { set("reviewed_on", value); }
        /**
         * Returns source id. The source id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getSourceId() { return get("source_id", new TypeReference<>() {}); }
        /**
         * Sets source id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setSourceId(String value) { set("source_id", value); }
    }
    /**
     * Wire representation of the catalogue translation schema.
     */
    public static final class CatalogueTranslation extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public CatalogueTranslation() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public CatalogueTranslation(JsonNode value) { super(value); }
        /**
         * Returns slug. The slug value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getSlug() { return get("slug", new TypeReference<>() {}); }
        /**
         * Sets slug.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setSlug(String value) { set("slug", value); }
        /**
         * Returns name. The name value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getName() { return get("name", new TypeReference<>() {}); }
        /**
         * Sets name.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setName(String value) { set("name", value); }
        /**
         * Returns language. The language value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getLanguage() { return get("language", new TypeReference<>() {}); }
        /**
         * Sets language.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setLanguage(JsonNode value) { set("language", value); }
        /**
         * Returns book ids. The book ids value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<Long> getBookIds() { return get("book_ids", new TypeReference<>() {}); }
        /**
         * Sets book ids.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setBookIds(List<Long> value) { set("book_ids", value); }
        /**
         * Returns rights. The rights value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public TranslationRights getRights() { return get("rights", new TypeReference<>() {}); }
        /**
         * Sets rights.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setRights(TranslationRights value) { set("rights", value); }
        /**
         * Returns installed. The installed value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Boolean getInstalled() { return get("installed", new TypeReference<>() {}); }
        /**
         * Sets installed.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setInstalled(Boolean value) { set("installed", value); }
        /**
         * Returns provenance. The provenance value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public TranslationProvenance getProvenance() { return get("provenance", new TypeReference<>() {}); }
        /**
         * Sets provenance.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setProvenance(TranslationProvenance value) { set("provenance", value); }
    }
    /**
     * Wire representation of the book definition schema.
     */
    public static final class BookDefinition extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public BookDefinition() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public BookDefinition(JsonNode value) { super(value); }
        /**
         * Returns id. The id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getId() { return get("id", new TypeReference<>() {}); }
        /**
         * Sets id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setId(Long value) { set("id", value); }
        /**
         * Returns code. The code value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getCode() { return get("code", new TypeReference<>() {}); }
        /**
         * Sets code.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setCode(String value) { set("code", value); }
        /**
         * Returns aliases. The aliases value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<String> getAliases() { return get("aliases", new TypeReference<>() {}); }
        /**
         * Sets aliases.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setAliases(List<String> value) { set("aliases", value); }
        /**
         * Returns names. The names value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getNames() { return get("names", new TypeReference<>() {}); }
        /**
         * Sets names.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setNames(JsonNode value) { set("names", value); }
    }
    /**
     * Wire representation of the translation collection schema.
     */
    public static final class TranslationCollection extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public TranslationCollection() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public TranslationCollection(JsonNode value) { super(value); }
        /**
         * Returns data. The data value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<CatalogueTranslation> getData() { return get("data", new TypeReference<>() {}); }
        /**
         * Sets data.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setData(List<CatalogueTranslation> value) { set("data", value); }
        /**
         * Returns next cursor. Pass unchanged as after on the next request; null ends the collection.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getNextCursor() { return get("next_cursor", new TypeReference<>() {}); }
        /**
         * Sets next cursor.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setNextCursor(String value) { set("next_cursor", value); }
    }
    /**
     * Wire representation of the book collection schema.
     */
    public static final class BookCollection extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public BookCollection() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public BookCollection(JsonNode value) { super(value); }
        /**
         * Returns data. The data value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<BookDefinition> getData() { return get("data", new TypeReference<>() {}); }
        /**
         * Sets data.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setData(List<BookDefinition> value) { set("data", value); }
        /**
         * Returns next cursor. Pass unchanged as after on the next request; null ends the collection.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getNextCursor() { return get("next_cursor", new TypeReference<>() {}); }
        /**
         * Sets next cursor.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setNextCursor(String value) { set("next_cursor", value); }
    }
    /**
     * Wire representation of the organization schema.
     */
    public static final class Organization extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public Organization() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public Organization(JsonNode value) { super(value); }
        /**
         * Returns id. The id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getId() { return get("id", new TypeReference<>() {}); }
        /**
         * Sets id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setId(String value) { set("id", value); }
        /**
         * Returns name. The name value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getName() { return get("name", new TypeReference<>() {}); }
        /**
         * Sets name.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setName(String value) { set("name", value); }
        /**
         * Returns handle. The handle value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getHandle() { return get("handle", new TypeReference<>() {}); }
        /**
         * Sets handle.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setHandle(String value) { set("handle", value); }
        /**
         * Returns logo url. The logo url value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getLogoUrl() { return get("logo_url", new TypeReference<>() {}); }
        /**
         * Sets logo url.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setLogoUrl(String value) { set("logo_url", value); }
        /**
         * Returns website. The website value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getWebsite() { return get("website", new TypeReference<>() {}); }
        /**
         * Sets website.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setWebsite(String value) { set("website", value); }
    }
    /**
     * Wire representation of the membership schema.
     */
    public static final class Membership extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public Membership() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public Membership(JsonNode value) { super(value); }
        /**
         * Returns id. The id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getId() { return get("id", new TypeReference<>() {}); }
        /**
         * Sets id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setId(String value) { set("id", value); }
        /**
         * Returns organization id. The organization id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getOrganizationId() { return get("organization_id", new TypeReference<>() {}); }
        /**
         * Sets organization id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setOrganizationId(String value) { set("organization_id", value); }
        /**
         * Returns display name. The display name value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getDisplayName() { return get("display_name", new TypeReference<>() {}); }
        /**
         * Sets display name.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setDisplayName(String value) { set("display_name", value); }
        /**
         * Returns handle. The handle value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getHandle() { return get("handle", new TypeReference<>() {}); }
        /**
         * Sets handle.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setHandle(String value) { set("handle", value); }
        /**
         * Returns role. The role value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getRole() { return get("role", new TypeReference<>() {}); }
        /**
         * Sets role.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setRole(JsonNode value) { set("role", value); }
    }
    /**
     * Wire representation of the membership page schema.
     */
    public static final class MembershipPage extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public MembershipPage() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public MembershipPage(JsonNode value) { super(value); }
        /**
         * Returns data. The data value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<Membership> getData() { return get("data", new TypeReference<>() {}); }
        /**
         * Sets data.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setData(List<Membership> value) { set("data", value); }
        /**
         * Returns next cursor. The next cursor value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getNextCursor() { return get("next_cursor", new TypeReference<>() {}); }
        /**
         * Sets next cursor.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setNextCursor(String value) { set("next_cursor", value); }
    }
    /**
     * Wire representation of the membership patch schema.
     */
    public static final class MembershipPatch extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public MembershipPatch() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public MembershipPatch(JsonNode value) { super(value); }
        /**
         * Returns role. The role value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getRole() { return get("role", new TypeReference<>() {}); }
        /**
         * Sets role.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setRole(JsonNode value) { set("role", value); }
    }
    /**
     * Wire representation of the live metadata patch schema.
     */
    public static final class LiveMetadataPatch extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public LiveMetadataPatch() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveMetadataPatch(JsonNode value) { super(value); }
        /**
         * Returns name. Nonempty UTF-8 name, at most 128 bytes.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getName() { return get("name", new TypeReference<>() {}); }
        /**
         * Sets name.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setName(String value) { set("name", value); }
    }
    /**
     * Wire representation of the live workspace put schema.
     */
    public static final class LiveWorkspacePut extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public LiveWorkspacePut() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveWorkspacePut(JsonNode value) { super(value); }
        /**
         * Returns organization id. Immutable destination organization UUID. Personal workspaces and handles are not accepted.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getOrganizationId() { return get("organization_id", new TypeReference<>() {}); }
        /**
         * Sets organization id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setOrganizationId(String value) { set("organization_id", value); }
    }
    /**
     * Wire representation of the live queue item schema.
     */
    public static final class LiveQueueItem extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public LiveQueueItem() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveQueueItem(JsonNode value) { super(value); }
        /**
         * Returns id. Opaque stable queue item ID. Preserved through reordering, metadata edits and restart; removing/recreating a verse yields a new ID.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getId() { return get("id", new TypeReference<>() {}); }
        /**
         * Sets id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setId(String value) { set("id", value); }
        /**
         * Returns position. Zero-based position in the entire current queue, not the current page.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getPosition() { return get("position", new TypeReference<>() {}); }
        /**
         * Sets position.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setPosition(Long value) { set("position", value); }
        /**
         * Returns verse. The verse value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public DisplayVerse getVerse() { return get("verse", new TypeReference<>() {}); }
        /**
         * Sets verse.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setVerse(DisplayVerse value) { set("verse", value); }
    }
    /**
     * Wire representation of the live queue page schema.
     */
    public static final class LiveQueuePage extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public LiveQueuePage() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveQueuePage(JsonNode value) { super(value); }
        /**
         * Returns live id. The live id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getLiveId() { return get("live_id", new TypeReference<>() {}); }
        /**
         * Sets live id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setLiveId(String value) { set("live_id", value); }
        /**
         * Returns organization id. The organization id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getOrganizationId() { return get("organization_id", new TypeReference<>() {}); }
        /**
         * Sets organization id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setOrganizationId(String value) { set("organization_id", value); }
        /**
         * Returns sequence. The sequence value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getSequence() { return get("sequence", new TypeReference<>() {}); }
        /**
         * Sets sequence.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setSequence(Long value) { set("sequence", value); }
        /**
         * Returns data. The data value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<LiveQueueItem> getData() { return get("data", new TypeReference<>() {}); }
        /**
         * Sets data.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setData(List<LiveQueueItem> value) { set("data", value); }
        /**
         * Returns next cursor. Opaque sequence-bound cursor. Pass unchanged as after. Any intervening Live sequence change returns 409; malformed/unknown cursor returns 422.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getNextCursor() { return get("next_cursor", new TypeReference<>() {}); }
        /**
         * Sets next cursor.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setNextCursor(String value) { set("next_cursor", value); }
    }
    /**
     * Wire representation of the create live queue item schema.
     */
    public static final class CreateLiveQueueItem extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public CreateLiveQueueItem() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public CreateLiveQueueItem(JsonNode value) { super(value); }
        /**
         * Returns reference. The reference value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getReference() { return get("reference", new TypeReference<>() {}); }
        /**
         * Sets reference.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setReference(JsonNode value) { set("reference", value); }
        /**
         * Returns before item id. Insert before this item ID. Omit or use null to append. Unknown anchor returns 404.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getBeforeItemId() { return get("before_item_id", new TypeReference<>() {}); }
        /**
         * Sets before item id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setBeforeItemId(String value) { set("before_item_id", value); }
    }
    /**
     * Wire representation of the live queue order schema.
     */
    public static final class LiveQueueOrder extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public LiveQueueOrder() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveQueueOrder(JsonNode value) { super(value); }
        /**
         * Returns item ids. Complete permutation of the current item IDs. Missing, extra, foreign or duplicate IDs return 422. Empty is valid only for an empty queue.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<String> getItemIds() { return get("item_ids", new TypeReference<>() {}); }
        /**
         * Sets item ids.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setItemIds(List<String> value) { set("item_ids", value); }
    }
    /**
     * Wire representation of the live studio settings schema.
     */
    public static final class LiveStudioSettings extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public LiveStudioSettings() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveStudioSettings(JsonNode value) { super(value); }
        /**
         * Returns version. The version value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getVersion() { return get("version", new TypeReference<>() {}); }
        /**
         * Sets version.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setVersion(Long value) { set("version", value); }
        /**
         * Returns show logo. The show logo value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Boolean getShowLogo() { return get("showLogo", new TypeReference<>() {}); }
        /**
         * Sets show logo.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setShowLogo(Boolean value) { set("showLogo", value); }
        /**
         * Returns preset. The preset value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getPreset() { return get("preset", new TypeReference<>() {}); }
        /**
         * Sets preset.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setPreset(JsonNode value) { set("preset", value); }
        /**
         * Returns transition. The transition value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getTransition() { return get("transition", new TypeReference<>() {}); }
        /**
         * Sets transition.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setTransition(JsonNode value) { set("transition", value); }
        /**
         * Returns controls. The controls value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getControls() { return get("controls", new TypeReference<>() {}); }
        /**
         * Sets controls.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setControls(JsonNode value) { set("controls", value); }
        /**
         * Returns palettes. The palettes value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getPalettes() { return get("palettes", new TypeReference<>() {}); }
        /**
         * Sets palettes.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setPalettes(JsonNode value) { set("palettes", value); }
        /**
         * Returns container. The container value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getContainer() { return get("container", new TypeReference<>() {}); }
        /**
         * Sets container.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setContainer(JsonNode value) { set("container", value); }
        /**
         * Returns elements. The elements value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getElements() { return get("elements", new TypeReference<>() {}); }
        /**
         * Sets elements.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setElements(JsonNode value) { set("elements", value); }
        /**
         * Returns order. The order value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<JsonNode> getOrder() { return get("order", new TypeReference<>() {}); }
        /**
         * Sets order.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setOrder(List<JsonNode> value) { set("order", value); }
    }
    /**
     * Full settings replacement using the existing version-1 Studio model. Only preset and transition may be omitted; they default to default and none. Every other field and all nested keys are required. Unknown fields are rejected at every level.
     */
    public static final class LiveStudioSettingsInput extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public LiveStudioSettingsInput() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveStudioSettingsInput(JsonNode value) { super(value); }
        /**
         * Returns version. The version value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getVersion() { return get("version", new TypeReference<>() {}); }
        /**
         * Sets version.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setVersion(Long value) { set("version", value); }
        /**
         * Returns show logo. The show logo value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Boolean getShowLogo() { return get("showLogo", new TypeReference<>() {}); }
        /**
         * Sets show logo.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setShowLogo(Boolean value) { set("showLogo", value); }
        /**
         * Returns preset. The preset value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getPreset() { return get("preset", new TypeReference<>() {}); }
        /**
         * Sets preset.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setPreset(JsonNode value) { set("preset", value); }
        /**
         * Returns transition. The transition value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getTransition() { return get("transition", new TypeReference<>() {}); }
        /**
         * Sets transition.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setTransition(JsonNode value) { set("transition", value); }
        /**
         * Returns controls. The controls value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getControls() { return get("controls", new TypeReference<>() {}); }
        /**
         * Sets controls.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setControls(JsonNode value) { set("controls", value); }
        /**
         * Returns palettes. The palettes value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getPalettes() { return get("palettes", new TypeReference<>() {}); }
        /**
         * Sets palettes.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setPalettes(JsonNode value) { set("palettes", value); }
        /**
         * Returns container. The container value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getContainer() { return get("container", new TypeReference<>() {}); }
        /**
         * Sets container.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setContainer(JsonNode value) { set("container", value); }
        /**
         * Returns elements. The elements value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getElements() { return get("elements", new TypeReference<>() {}); }
        /**
         * Sets elements.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setElements(JsonNode value) { set("elements", value); }
        /**
         * Returns order. The order value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<JsonNode> getOrder() { return get("order", new TypeReference<>() {}); }
        /**
         * Sets order.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setOrder(List<JsonNode> value) { set("order", value); }
    }
    /**
     * Wire representation of the organization live studio schema.
     */
    public static final class OrganizationLiveStudio extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public OrganizationLiveStudio() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public OrganizationLiveStudio(JsonNode value) { super(value); }
        /**
         * Returns organization id. The organization id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getOrganizationId() { return get("organization_id", new TypeReference<>() {}); }
        /**
         * Sets organization id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setOrganizationId(String value) { set("organization_id", value); }
        /**
         * Returns revision. Settings revision independent of any Live sequence. Zero represents unsaved defaults; every accepted PUT increments the revision, even for identical settings.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getRevision() { return get("revision", new TypeReference<>() {}); }
        /**
         * Sets revision.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setRevision(Long value) { set("revision", value); }
        /**
         * Returns settings. The settings value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public LiveStudioSettings getSettings() { return get("settings", new TypeReference<>() {}); }
        /**
         * Sets settings.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setSettings(LiveStudioSettings value) { set("settings", value); }
    }
    /**
     * Wire representation of the put organization live studio schema.
     */
    public static final class PutOrganizationLiveStudio extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public PutOrganizationLiveStudio() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PutOrganizationLiveStudio(JsonNode value) { super(value); }
        /**
         * Returns settings. The settings value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public LiveStudioSettingsInput getSettings() { return get("settings", new TypeReference<>() {}); }
        /**
         * Sets settings.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setSettings(LiveStudioSettingsInput value) { set("settings", value); }
    }
    /**
     * Wire representation of the presentation element style schema.
     */
    public static final class PresentationElementStyle extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public PresentationElementStyle() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PresentationElementStyle(JsonNode value) { super(value); }
        /**
         * Returns font size. The font size value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public BigDecimal getFontSize() { return get("font_size", new TypeReference<>() {}); }
        /**
         * Sets font size.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setFontSize(BigDecimal value) { set("font_size", value); }
        /**
         * Returns font family. The font family value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getFontFamily() { return get("font_family", new TypeReference<>() {}); }
        /**
         * Sets font family.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setFontFamily(JsonNode value) { set("font_family", value); }
        /**
         * Returns color. The color value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getColor() { return get("color", new TypeReference<>() {}); }
        /**
         * Sets color.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setColor(String value) { set("color", value); }
        /**
         * Returns bold. The bold value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Boolean getBold() { return get("bold", new TypeReference<>() {}); }
        /**
         * Sets bold.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setBold(Boolean value) { set("bold", value); }
        /**
         * Returns italic. The italic value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Boolean getItalic() { return get("italic", new TypeReference<>() {}); }
        /**
         * Sets italic.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setItalic(Boolean value) { set("italic", value); }
        /**
         * Returns align. The align value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getAlign() { return get("align", new TypeReference<>() {}); }
        /**
         * Sets align.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setAlign(JsonNode value) { set("align", value); }
        /**
         * Returns line height. The line height value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public BigDecimal getLineHeight() { return get("line_height", new TypeReference<>() {}); }
        /**
         * Sets line height.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setLineHeight(BigDecimal value) { set("line_height", value); }
        /**
         * Returns fill. The fill value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getFill() { return get("fill", new TypeReference<>() {}); }
        /**
         * Sets fill.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setFill(String value) { set("fill", value); }
        /**
         * Returns stroke. The stroke value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getStroke() { return get("stroke", new TypeReference<>() {}); }
        /**
         * Sets stroke.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setStroke(String value) { set("stroke", value); }
        /**
         * Returns stroke width. The stroke width value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public BigDecimal getStrokeWidth() { return get("stroke_width", new TypeReference<>() {}); }
        /**
         * Sets stroke width.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setStrokeWidth(BigDecimal value) { set("stroke_width", value); }
        /**
         * Returns stroke style. The stroke style value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getStrokeStyle() { return get("stroke_style", new TypeReference<>() {}); }
        /**
         * Sets stroke style.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setStrokeStyle(JsonNode value) { set("stroke_style", value); }
        /**
         * Returns opacity. The opacity value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public BigDecimal getOpacity() { return get("opacity", new TypeReference<>() {}); }
        /**
         * Sets opacity.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setOpacity(BigDecimal value) { set("opacity", value); }
    }
    /**
     * Coordinates and dimensions are integer canvas units; x+width must be &lt;=1280 and y+height &lt;=720 (checked by the domain validator). IDs must be unique within a slide.
     */
    public static final class PresentationTextElement extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public PresentationTextElement() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PresentationTextElement(JsonNode value) { super(value); }
        /**
         * Returns id. The id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getId() { return get("id", new TypeReference<>() {}); }
        /**
         * Sets id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setId(String value) { set("id", value); }
        /**
         * Returns type. The type value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getType() { return get("type", new TypeReference<>() {}); }
        /**
         * Sets type.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setType(JsonNode value) { set("type", value); }
        /**
         * Returns x. The x value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getX() { return get("x", new TypeReference<>() {}); }
        /**
         * Sets x.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setX(Long value) { set("x", value); }
        /**
         * Returns y. The y value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getY() { return get("y", new TypeReference<>() {}); }
        /**
         * Sets y.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setY(Long value) { set("y", value); }
        /**
         * Returns width. The width value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getWidth() { return get("width", new TypeReference<>() {}); }
        /**
         * Sets width.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setWidth(Long value) { set("width", value); }
        /**
         * Returns height. The height value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getHeight() { return get("height", new TypeReference<>() {}); }
        /**
         * Sets height.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setHeight(Long value) { set("height", value); }
        /**
         * Returns style. The style value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public PresentationElementStyle getStyle() { return get("style", new TypeReference<>() {}); }
        /**
         * Sets style.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setStyle(PresentationElementStyle value) { set("style", value); }
        /**
         * Returns rotation. The rotation value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getRotation() { return get("rotation", new TypeReference<>() {}); }
        /**
         * Sets rotation.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setRotation(Long value) { set("rotation", value); }
        /**
         * Returns text. UTF-8 text, at most 20000 bytes; NUL is rejected.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getText() { return get("text", new TypeReference<>() {}); }
        /**
         * Sets text.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setText(String value) { set("text", value); }
    }
    /**
     * Coordinates and dimensions are integer canvas units; x+width must be &lt;=1280 and y+height &lt;=720 (checked by the domain validator). IDs must be unique within a slide. Known open-license edition/attribution fields are restored from the reviewed catalogue on saves and reads.
     */
    public static final class PresentationVerseElement extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public PresentationVerseElement() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PresentationVerseElement(JsonNode value) { super(value); }
        /**
         * Returns id. The id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getId() { return get("id", new TypeReference<>() {}); }
        /**
         * Sets id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setId(String value) { set("id", value); }
        /**
         * Returns type. The type value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getType() { return get("type", new TypeReference<>() {}); }
        /**
         * Sets type.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setType(JsonNode value) { set("type", value); }
        /**
         * Returns x. The x value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getX() { return get("x", new TypeReference<>() {}); }
        /**
         * Sets x.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setX(Long value) { set("x", value); }
        /**
         * Returns y. The y value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getY() { return get("y", new TypeReference<>() {}); }
        /**
         * Sets y.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setY(Long value) { set("y", value); }
        /**
         * Returns width. The width value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getWidth() { return get("width", new TypeReference<>() {}); }
        /**
         * Sets width.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setWidth(Long value) { set("width", value); }
        /**
         * Returns height. The height value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getHeight() { return get("height", new TypeReference<>() {}); }
        /**
         * Sets height.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setHeight(Long value) { set("height", value); }
        /**
         * Returns style. The style value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public PresentationElementStyle getStyle() { return get("style", new TypeReference<>() {}); }
        /**
         * Sets style.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setStyle(PresentationElementStyle value) { set("style", value); }
        /**
         * Returns rotation. The rotation value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getRotation() { return get("rotation", new TypeReference<>() {}); }
        /**
         * Sets rotation.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setRotation(Long value) { set("rotation", value); }
        /**
         * Returns text. UTF-8 text, at most 20000 bytes; NUL is rejected.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getText() { return get("text", new TypeReference<>() {}); }
        /**
         * Sets text.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setText(String value) { set("text", value); }
        /**
         * Returns translation. UTF-8 text, at most 200 bytes; NUL is rejected.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getTranslation() { return get("translation", new TypeReference<>() {}); }
        /**
         * Sets translation.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setTranslation(String value) { set("translation", value); }
        /**
         * Returns edition. UTF-8 text, at most 2000 bytes; NUL is rejected.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getEdition() { return get("edition", new TypeReference<>() {}); }
        /**
         * Sets edition.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setEdition(String value) { set("edition", value); }
        /**
         * Returns reference. UTF-8 text, at most 200 bytes; NUL is rejected.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getReference() { return get("reference", new TypeReference<>() {}); }
        /**
         * Sets reference.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setReference(String value) { set("reference", value); }
        /**
         * Returns attribution. UTF-8 text, at most 2000 bytes; NUL is rejected.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getAttribution() { return get("attribution", new TypeReference<>() {}); }
        /**
         * Sets attribution.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setAttribution(String value) { set("attribution", value); }
    }
    /**
     * Coordinates and dimensions are integer canvas units; x+width must be &lt;=1280 and y+height &lt;=720 (checked by the domain validator). IDs must be unique within a slide.
     */
    public static final class PresentationShapeElement extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public PresentationShapeElement() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PresentationShapeElement(JsonNode value) { super(value); }
        /**
         * Returns id. The id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getId() { return get("id", new TypeReference<>() {}); }
        /**
         * Sets id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setId(String value) { set("id", value); }
        /**
         * Returns type. The type value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getType() { return get("type", new TypeReference<>() {}); }
        /**
         * Sets type.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setType(JsonNode value) { set("type", value); }
        /**
         * Returns x. The x value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getX() { return get("x", new TypeReference<>() {}); }
        /**
         * Sets x.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setX(Long value) { set("x", value); }
        /**
         * Returns y. The y value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getY() { return get("y", new TypeReference<>() {}); }
        /**
         * Sets y.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setY(Long value) { set("y", value); }
        /**
         * Returns width. The width value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getWidth() { return get("width", new TypeReference<>() {}); }
        /**
         * Sets width.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setWidth(Long value) { set("width", value); }
        /**
         * Returns height. The height value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getHeight() { return get("height", new TypeReference<>() {}); }
        /**
         * Sets height.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setHeight(Long value) { set("height", value); }
        /**
         * Returns style. The style value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public PresentationElementStyle getStyle() { return get("style", new TypeReference<>() {}); }
        /**
         * Sets style.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setStyle(PresentationElementStyle value) { set("style", value); }
        /**
         * Returns rotation. The rotation value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getRotation() { return get("rotation", new TypeReference<>() {}); }
        /**
         * Sets rotation.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setRotation(Long value) { set("rotation", value); }
        /**
         * Returns shape. The shape value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getShape() { return get("shape", new TypeReference<>() {}); }
        /**
         * Sets shape.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setShape(JsonNode value) { set("shape", value); }
    }
    /**
     * Coordinates and dimensions are integer canvas units; x+width must be &lt;=1280 and y+height &lt;=720 (checked by the domain validator). IDs must be unique within a slide.
     */
    public static final class PresentationImageElement extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public PresentationImageElement() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PresentationImageElement(JsonNode value) { super(value); }
        /**
         * Returns id. The id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getId() { return get("id", new TypeReference<>() {}); }
        /**
         * Sets id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setId(String value) { set("id", value); }
        /**
         * Returns type. The type value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getType() { return get("type", new TypeReference<>() {}); }
        /**
         * Sets type.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setType(JsonNode value) { set("type", value); }
        /**
         * Returns x. The x value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getX() { return get("x", new TypeReference<>() {}); }
        /**
         * Sets x.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setX(Long value) { set("x", value); }
        /**
         * Returns y. The y value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getY() { return get("y", new TypeReference<>() {}); }
        /**
         * Sets y.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setY(Long value) { set("y", value); }
        /**
         * Returns width. The width value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getWidth() { return get("width", new TypeReference<>() {}); }
        /**
         * Sets width.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setWidth(Long value) { set("width", value); }
        /**
         * Returns height. The height value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getHeight() { return get("height", new TypeReference<>() {}); }
        /**
         * Sets height.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setHeight(Long value) { set("height", value); }
        /**
         * Returns style. The style value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public PresentationElementStyle getStyle() { return get("style", new TypeReference<>() {}); }
        /**
         * Sets style.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setStyle(PresentationElementStyle value) { set("style", value); }
        /**
         * Returns rotation. The rotation value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getRotation() { return get("rotation", new TypeReference<>() {}); }
        /**
         * Sets rotation.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setRotation(Long value) { set("rotation", value); }
        /**
         * Returns asset. The asset value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getAsset() { return get("asset", new TypeReference<>() {}); }
        /**
         * Sets asset.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setAsset(String value) { set("asset", value); }
        /**
         * Returns alt. UTF-8 text, at most 1000 bytes; NUL is rejected.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getAlt() { return get("alt", new TypeReference<>() {}); }
        /**
         * Sets alt.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setAlt(String value) { set("alt", value); }
        /**
         * Returns fit. The fit value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getFit() { return get("fit", new TypeReference<>() {}); }
        /**
         * Sets fit.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setFit(JsonNode value) { set("fit", value); }
    }
    /**
     * Coordinates and dimensions are integer canvas units; x+width must be &lt;=1280 and y+height &lt;=720 (checked by the domain validator). IDs must be unique within a slide.
     */
    public static final class PresentationVideoElement extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public PresentationVideoElement() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PresentationVideoElement(JsonNode value) { super(value); }
        /**
         * Returns id. The id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getId() { return get("id", new TypeReference<>() {}); }
        /**
         * Sets id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setId(String value) { set("id", value); }
        /**
         * Returns type. The type value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getType() { return get("type", new TypeReference<>() {}); }
        /**
         * Sets type.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setType(JsonNode value) { set("type", value); }
        /**
         * Returns x. The x value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getX() { return get("x", new TypeReference<>() {}); }
        /**
         * Sets x.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setX(Long value) { set("x", value); }
        /**
         * Returns y. The y value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getY() { return get("y", new TypeReference<>() {}); }
        /**
         * Sets y.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setY(Long value) { set("y", value); }
        /**
         * Returns width. The width value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getWidth() { return get("width", new TypeReference<>() {}); }
        /**
         * Sets width.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setWidth(Long value) { set("width", value); }
        /**
         * Returns height. The height value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getHeight() { return get("height", new TypeReference<>() {}); }
        /**
         * Sets height.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setHeight(Long value) { set("height", value); }
        /**
         * Returns style. The style value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public PresentationElementStyle getStyle() { return get("style", new TypeReference<>() {}); }
        /**
         * Sets style.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setStyle(PresentationElementStyle value) { set("style", value); }
        /**
         * Returns rotation. The rotation value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getRotation() { return get("rotation", new TypeReference<>() {}); }
        /**
         * Sets rotation.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setRotation(Long value) { set("rotation", value); }
        /**
         * Returns asset. The asset value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getAsset() { return get("asset", new TypeReference<>() {}); }
        /**
         * Sets asset.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setAsset(String value) { set("asset", value); }
        /**
         * Returns fit. The fit value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getFit() { return get("fit", new TypeReference<>() {}); }
        /**
         * Sets fit.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setFit(JsonNode value) { set("fit", value); }
        /**
         * Returns poster. The poster value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getPoster() { return get("poster", new TypeReference<>() {}); }
        /**
         * Sets poster.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setPoster(String value) { set("poster", value); }
        /**
         * Returns muted. The muted value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Boolean getMuted() { return get("muted", new TypeReference<>() {}); }
        /**
         * Sets muted.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setMuted(Boolean value) { set("muted", value); }
        /**
         * Returns loop. The loop value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Boolean getLoop() { return get("loop", new TypeReference<>() {}); }
        /**
         * Sets loop.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setLoop(Boolean value) { set("loop", value); }
    }
    /**
     * Coordinates and dimensions are integer canvas units; x+width must be &lt;=1280 and y+height &lt;=720 (checked by the domain validator). IDs must be unique within a slide.
     */
    public static final class PresentationYouTubeElement extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public PresentationYouTubeElement() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PresentationYouTubeElement(JsonNode value) { super(value); }
        /**
         * Returns id. The id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getId() { return get("id", new TypeReference<>() {}); }
        /**
         * Sets id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setId(String value) { set("id", value); }
        /**
         * Returns type. The type value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getType() { return get("type", new TypeReference<>() {}); }
        /**
         * Sets type.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setType(JsonNode value) { set("type", value); }
        /**
         * Returns x. The x value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getX() { return get("x", new TypeReference<>() {}); }
        /**
         * Sets x.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setX(Long value) { set("x", value); }
        /**
         * Returns y. The y value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getY() { return get("y", new TypeReference<>() {}); }
        /**
         * Sets y.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setY(Long value) { set("y", value); }
        /**
         * Returns width. The width value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getWidth() { return get("width", new TypeReference<>() {}); }
        /**
         * Sets width.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setWidth(Long value) { set("width", value); }
        /**
         * Returns height. The height value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getHeight() { return get("height", new TypeReference<>() {}); }
        /**
         * Sets height.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setHeight(Long value) { set("height", value); }
        /**
         * Returns style. The style value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public PresentationElementStyle getStyle() { return get("style", new TypeReference<>() {}); }
        /**
         * Sets style.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setStyle(PresentationElementStyle value) { set("style", value); }
        /**
         * Returns rotation. The rotation value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getRotation() { return get("rotation", new TypeReference<>() {}); }
        /**
         * Sets rotation.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setRotation(Long value) { set("rotation", value); }
        /**
         * Returns video id. The video id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getVideoId() { return get("video_id", new TypeReference<>() {}); }
        /**
         * Sets video id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setVideoId(String value) { set("video_id", value); }
    }
    /**
     * Wire representation of the presentation element schema.
     */
    public static final class PresentationElement {
        private final JsonNode value;
        /**
         * Copies a scalar or union wire value without adding wrapper fields.
         * @param value the non-null JSON value
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PresentationElement(JsonNode value) { this.value = value.deepCopy(); }
        /**
         * Returns the underlying wire representation.
         * @return a defensive copy of the JSON value
         */
        @JsonValue public JsonNode value() { return value.deepCopy(); }
        @Override public String toString() { return "PresentationElement[redacted]"; }
    }
    /**
     * Wire representation of the presentation slide schema.
     */
    public static final class PresentationSlide extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public PresentationSlide() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PresentationSlide(JsonNode value) { super(value); }
        /**
         * Returns id. The id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getId() { return get("id", new TypeReference<>() {}); }
        /**
         * Sets id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setId(String value) { set("id", value); }
        /**
         * Returns elements. The elements value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<PresentationElement> getElements() { return get("elements", new TypeReference<>() {}); }
        /**
         * Sets elements.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setElements(List<PresentationElement> value) { set("elements", value); }
        /**
         * Returns background. The background value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getBackground() { return get("background", new TypeReference<>() {}); }
        /**
         * Sets background.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setBackground(String value) { set("background", value); }
    }
    /**
     * Version-1 document, at most 2,000,000 encoded JSON bytes. Slide IDs are unique across the deck, element IDs within each slide. Omitted slide background is preserved as omitted. Save retains caller slide/element IDs; no automatic publishing. Existing same-workspace ready media references are checked by the domain; upload and portable import endpoints provide ready media. Uploading alone never modifies the saved document.
     */
    public static final class PresentationDocument extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public PresentationDocument() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PresentationDocument(JsonNode value) { super(value); }
        /**
         * Returns version. The version value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getVersion() { return get("version", new TypeReference<>() {}); }
        /**
         * Sets version.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setVersion(Long value) { set("version", value); }
        /**
         * Returns name. UTF-8 text, at most 200 bytes; NUL is rejected.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getName() { return get("name", new TypeReference<>() {}); }
        /**
         * Sets name.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setName(String value) { set("name", value); }
        /**
         * Returns slides. The slides value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<PresentationSlide> getSlides() { return get("slides", new TypeReference<>() {}); }
        /**
         * Sets slides.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setSlides(List<PresentationSlide> value) { set("slides", value); }
    }
    /**
     * Wire representation of the presentation schema.
     */
    public static final class Presentation extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public Presentation() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public Presentation(JsonNode value) { super(value); }
        /**
         * Returns id. The id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getId() { return get("id", new TypeReference<>() {}); }
        /**
         * Sets id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setId(String value) { set("id", value); }
        /**
         * Returns organization id. The organization id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getOrganizationId() { return get("organization_id", new TypeReference<>() {}); }
        /**
         * Sets organization id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setOrganizationId(String value) { set("organization_id", value); }
        /**
         * Returns revision. The revision value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getRevision() { return get("revision", new TypeReference<>() {}); }
        /**
         * Sets revision.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setRevision(Long value) { set("revision", value); }
        /**
         * Returns document. The document value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public PresentationDocument getDocument() { return get("document", new TypeReference<>() {}); }
        /**
         * Sets document.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setDocument(PresentationDocument value) { set("document", value); }
    }
    /**
     * Wire representation of the presentation summary schema.
     */
    public static final class PresentationSummary extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public PresentationSummary() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PresentationSummary(JsonNode value) { super(value); }
        /**
         * Returns id. The id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getId() { return get("id", new TypeReference<>() {}); }
        /**
         * Sets id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setId(String value) { set("id", value); }
        /**
         * Returns organization id. The organization id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getOrganizationId() { return get("organization_id", new TypeReference<>() {}); }
        /**
         * Sets organization id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setOrganizationId(String value) { set("organization_id", value); }
        /**
         * Returns name. The name value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getName() { return get("name", new TypeReference<>() {}); }
        /**
         * Sets name.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setName(String value) { set("name", value); }
        /**
         * Returns revision. The revision value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getRevision() { return get("revision", new TypeReference<>() {}); }
        /**
         * Sets revision.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setRevision(Long value) { set("revision", value); }
        /**
         * Returns slide count. The slide count value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getSlideCount() { return get("slide_count", new TypeReference<>() {}); }
        /**
         * Sets slide count.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setSlideCount(Long value) { set("slide_count", value); }
    }
    /**
     * Wire representation of the presentation page schema.
     */
    public static final class PresentationPage extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public PresentationPage() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PresentationPage(JsonNode value) { super(value); }
        /**
         * Returns data. The data value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<PresentationSummary> getData() { return get("data", new TypeReference<>() {}); }
        /**
         * Sets data.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setData(List<PresentationSummary> value) { set("data", value); }
        /**
         * Returns next cursor. The next cursor value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getNextCursor() { return get("next_cursor", new TypeReference<>() {}); }
        /**
         * Sets next cursor.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setNextCursor(String value) { set("next_cursor", value); }
    }
    /**
     * Wire representation of the create presentation schema.
     */
    public static final class CreatePresentation extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public CreatePresentation() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public CreatePresentation(JsonNode value) { super(value); }
        /**
         * Returns name. UTF-8 text, at most 200 bytes; NUL is rejected.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getName() { return get("name", new TypeReference<>() {}); }
        /**
         * Sets name.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setName(String value) { set("name", value); }
    }
    /**
     * Wire representation of the put presentation schema.
     */
    public static final class PutPresentation extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public PutPresentation() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PutPresentation(JsonNode value) { super(value); }
        /**
         * Returns document. The document value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public PresentationDocument getDocument() { return get("document", new TypeReference<>() {}); }
        /**
         * Sets document.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setDocument(PresentationDocument value) { set("document", value); }
    }
    /**
     * Explicitly capture the current saved revision of a same-organization deck. This API has no historical revision archive: if the current saved revision differs, return 409 presentation_revision_conflict. Optional slide_id selects a stable saved slide. Missing slide returns 422. Empty decks and stopped Lives return 409.
     */
    public static final class PresentationDisplayReference extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public PresentationDisplayReference() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PresentationDisplayReference(JsonNode value) { super(value); }
        /**
         * Returns type. The type value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getType() { return get("type", new TypeReference<>() {}); }
        /**
         * Sets type.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setType(JsonNode value) { set("type", value); }
        /**
         * Returns presentation id. The presentation id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getPresentationId() { return get("presentation_id", new TypeReference<>() {}); }
        /**
         * Sets presentation id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setPresentationId(String value) { set("presentation_id", value); }
        /**
         * Returns revision. The revision value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getRevision() { return get("revision", new TypeReference<>() {}); }
        /**
         * Sets revision.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setRevision(Long value) { set("revision", value); }
        /**
         * Returns slide id. The slide id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getSlideId() { return get("slide_id", new TypeReference<>() {}); }
        /**
         * Sets slide id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setSlideId(String value) { set("slide_id", value); }
    }
    /**
     * Wire representation of the presentation asset schema.
     */
    public static final class PresentationAsset extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public PresentationAsset() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PresentationAsset(JsonNode value) { super(value); }
        /**
         * Returns id. The id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getId() { return get("id", new TypeReference<>() {}); }
        /**
         * Sets id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setId(String value) { set("id", value); }
        /**
         * Returns organization id. The organization id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getOrganizationId() { return get("organization_id", new TypeReference<>() {}); }
        /**
         * Sets organization id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setOrganizationId(String value) { set("organization_id", value); }
        /**
         * Returns presentation id. The presentation id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getPresentationId() { return get("presentation_id", new TypeReference<>() {}); }
        /**
         * Sets presentation id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setPresentationId(String value) { set("presentation_id", value); }
        /**
         * Returns kind. The kind value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getKind() { return get("kind", new TypeReference<>() {}); }
        /**
         * Sets kind.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setKind(String value) { set("kind", value); }
        /**
         * Returns mime. The mime value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getMime() { return get("mime", new TypeReference<>() {}); }
        /**
         * Sets mime.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setMime(String value) { set("mime", value); }
        /**
         * Returns width. The width value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getWidth() { return get("width", new TypeReference<>() {}); }
        /**
         * Sets width.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setWidth(Long value) { set("width", value); }
        /**
         * Returns height. The height value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getHeight() { return get("height", new TypeReference<>() {}); }
        /**
         * Sets height.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setHeight(Long value) { set("height", value); }
        /**
         * Returns bytes. The bytes value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getBytes() { return get("bytes", new TypeReference<>() {}); }
        /**
         * Sets bytes.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setBytes(Long value) { set("bytes", value); }
    }
    /**
     * Safe requester-private job projection. Timestamps are Unix seconds. Jobs snapshot the saved revision at creation; later saves never alter the captured document. Expiry is initially ten minutes and resets to ten minutes on completion/failure. Jobs disappear on expiry, presentation deletion or successful download consumption. Starting a new export removes this requester’s failed jobs.
     */
    public static final class PresentationExport extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public PresentationExport() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PresentationExport(JsonNode value) { super(value); }
        /**
         * Returns id. The id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getId() { return get("id", new TypeReference<>() {}); }
        /**
         * Sets id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setId(String value) { set("id", value); }
        /**
         * Returns presentation id. The presentation id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getPresentationId() { return get("presentation_id", new TypeReference<>() {}); }
        /**
         * Sets presentation id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setPresentationId(String value) { set("presentation_id", value); }
        /**
         * Returns organization id. The organization id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getOrganizationId() { return get("organization_id", new TypeReference<>() {}); }
        /**
         * Sets organization id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setOrganizationId(String value) { set("organization_id", value); }
        /**
         * Returns revision. The revision value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getRevision() { return get("revision", new TypeReference<>() {}); }
        /**
         * Sets revision.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setRevision(Long value) { set("revision", value); }
        /**
         * Returns format. The format value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getFormat() { return get("format", new TypeReference<>() {}); }
        /**
         * Sets format.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setFormat(String value) { set("format", value); }
        /**
         * Returns status. The status value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getStatus() { return get("status", new TypeReference<>() {}); }
        /**
         * Sets status.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setStatus(String value) { set("status", value); }
        /**
         * Returns error. The error value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getError() { return get("error", new TypeReference<>() {}); }
        /**
         * Sets error.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setError(String value) { set("error", value); }
        /**
         * Returns bytes. The bytes value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getBytes() { return get("bytes", new TypeReference<>() {}); }
        /**
         * Sets bytes.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setBytes(Long value) { set("bytes", value); }
        /**
         * Returns created at. The created at value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getCreatedAt() { return get("created_at", new TypeReference<>() {}); }
        /**
         * Sets created at.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setCreatedAt(Long value) { set("created_at", value); }
        /**
         * Returns expires at. The expires at value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getExpiresAt() { return get("expires_at", new TypeReference<>() {}); }
        /**
         * Sets expires at.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setExpiresAt(Long value) { set("expires_at", value); }
        /**
         * Returns download url. Relative POST downloads URL, available only when completed. Requires the requesting credential and current presentation.export scope. Never a public URL.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getDownloadUrl() { return get("download_url", new TypeReference<>() {}); }
        /**
         * Sets download url.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setDownloadUrl(String value) { set("download_url", value); }
    }
    /**
     * Wire representation of the presentation export list schema.
     */
    public static final class PresentationExportList extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public PresentationExportList() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PresentationExportList(JsonNode value) { super(value); }
        /**
         * Returns data. The data value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<PresentationExport> getData() { return get("data", new TypeReference<>() {}); }
        /**
         * Sets data.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setData(List<PresentationExport> value) { set("data", value); }
        /**
         * Returns next cursor. The next cursor value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getNextCursor() { return get("next_cursor", new TypeReference<>() {}); }
        /**
         * Sets next cursor.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setNextCursor(String value) { set("next_cursor", value); }
    }
    /**
     * Explicit current saved revision. PNG/JPEG require one slide_index; other formats export the whole deck and reject slide_index. Empty presentations support only bibleit. Locale affects exported navigation labels.
     */
    public static final class CreatePresentationExport {
        private final JsonNode value;
        /**
         * Copies a scalar or union wire value without adding wrapper fields.
         * @param value the non-null JSON value
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public CreatePresentationExport(JsonNode value) { this.value = value.deepCopy(); }
        /**
         * Returns the underlying wire representation.
         * @return a defensive copy of the JSON value
         */
        @JsonValue public JsonNode value() { return value.deepCopy(); }
        @Override public String toString() { return "CreatePresentationExport[redacted]"; }
    }
    /**
     * Personal-account Reader Live resource. Uses account_id instead of organization_id; only the personal account owner can access it with current personal token authority.
     */
    public static final class PersonalLiveSummary extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public PersonalLiveSummary() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PersonalLiveSummary(JsonNode value) { super(value); }
        /**
         * Returns id. The id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getId() { return get("id", new TypeReference<>() {}); }
        /**
         * Sets id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setId(String value) { set("id", value); }
        /**
         * Returns name. The name value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getName() { return get("name", new TypeReference<>() {}); }
        /**
         * Sets name.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setName(String value) { set("name", value); }
        /**
         * Returns account id. The account id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getAccountId() { return get("account_id", new TypeReference<>() {}); }
        /**
         * Sets account id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setAccountId(String value) { set("account_id", value); }
    }
    /**
     * Personal-account Reader Live resource. Uses account_id instead of organization_id; only the personal account owner can access it with current personal token authority.
     */
    public static final class PersonalLive extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public PersonalLive() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PersonalLive(JsonNode value) { super(value); }
        /**
         * Returns id. The id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getId() { return get("id", new TypeReference<>() {}); }
        /**
         * Sets id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setId(String value) { set("id", value); }
        /**
         * Returns name. The name value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getName() { return get("name", new TypeReference<>() {}); }
        /**
         * Sets name.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setName(String value) { set("name", value); }
        /**
         * Returns status. The status value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getStatus() { return get("status", new TypeReference<>() {}); }
        /**
         * Sets status.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setStatus(String value) { set("status", value); }
        /**
         * Returns mode. The mode value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getMode() { return get("mode", new TypeReference<>() {}); }
        /**
         * Sets mode.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setMode(String value) { set("mode", value); }
        /**
         * Returns paused. The paused value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Boolean getPaused() { return get("paused", new TypeReference<>() {}); }
        /**
         * Sets paused.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setPaused(Boolean value) { set("paused", value); }
        /**
         * Returns sequence. The sequence value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getSequence() { return get("sequence", new TypeReference<>() {}); }
        /**
         * Sets sequence.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setSequence(Long value) { set("sequence", value); }
        /**
         * Returns account id. The account id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getAccountId() { return get("account_id", new TypeReference<>() {}); }
        /**
         * Sets account id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setAccountId(String value) { set("account_id", value); }
    }
    /**
     * Personal-account Reader Live resource. Uses account_id instead of organization_id; only the personal account owner can access it with current personal token authority.
     */
    public static final class PersonalLiveDisplay extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public PersonalLiveDisplay() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PersonalLiveDisplay(JsonNode value) { super(value); }
        /**
         * Returns live id. The live id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getLiveId() { return get("live_id", new TypeReference<>() {}); }
        /**
         * Sets live id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setLiveId(String value) { set("live_id", value); }
        /**
         * Returns status. The status value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getStatus() { return get("status", new TypeReference<>() {}); }
        /**
         * Sets status.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setStatus(JsonNode value) { set("status", value); }
        /**
         * Returns paused. The paused value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Boolean getPaused() { return get("paused", new TypeReference<>() {}); }
        /**
         * Sets paused.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setPaused(Boolean value) { set("paused", value); }
        /**
         * Returns showing. The showing value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Boolean getShowing() { return get("showing", new TypeReference<>() {}); }
        /**
         * Sets showing.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setShowing(Boolean value) { set("showing", value); }
        /**
         * Returns sequence. The sequence value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getSequence() { return get("sequence", new TypeReference<>() {}); }
        /**
         * Sets sequence.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setSequence(Long value) { set("sequence", value); }
        /**
         * Returns type. The type value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getType() { return get("type", new TypeReference<>() {}); }
        /**
         * Sets type.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setType(String value) { set("type", value); }
        /**
         * Returns verses. The verses value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<DisplayVerse> getVerses() { return get("verses", new TypeReference<>() {}); }
        /**
         * Sets verses.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setVerses(List<DisplayVerse> value) { set("verses", value); }
        /**
         * Returns snapshot. The snapshot value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getSnapshot() { return get("snapshot", new TypeReference<>() {}); }
        /**
         * Sets snapshot.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setSnapshot(JsonNode value) { set("snapshot", value); }
        /**
         * Returns account id. The account id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getAccountId() { return get("account_id", new TypeReference<>() {}); }
        /**
         * Sets account id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setAccountId(String value) { set("account_id", value); }
    }
    /**
     * Personal-account Reader Live resource. Uses account_id instead of organization_id; only the personal account owner can access it with current personal token authority.
     */
    public static final class PersonalLiveQueuePage extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public PersonalLiveQueuePage() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PersonalLiveQueuePage(JsonNode value) { super(value); }
        /**
         * Returns live id. The live id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getLiveId() { return get("live_id", new TypeReference<>() {}); }
        /**
         * Sets live id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setLiveId(String value) { set("live_id", value); }
        /**
         * Returns sequence. The sequence value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getSequence() { return get("sequence", new TypeReference<>() {}); }
        /**
         * Sets sequence.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setSequence(Long value) { set("sequence", value); }
        /**
         * Returns data. The data value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<LiveQueueItem> getData() { return get("data", new TypeReference<>() {}); }
        /**
         * Sets data.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setData(List<LiveQueueItem> value) { set("data", value); }
        /**
         * Returns next cursor. Opaque sequence-bound cursor. Pass unchanged as after. Any intervening Live sequence change returns 409; malformed/unknown cursor returns 422.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getNextCursor() { return get("next_cursor", new TypeReference<>() {}); }
        /**
         * Sets next cursor.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setNextCursor(String value) { set("next_cursor", value); }
        /**
         * Returns account id. The account id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getAccountId() { return get("account_id", new TypeReference<>() {}); }
        /**
         * Sets account id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setAccountId(String value) { set("account_id", value); }
    }
    /**
     * Personal-account Reader Live resource. Uses account_id instead of organization_id; only the personal account owner can access it with current personal token authority.
     */
    public static final class PersonalLiveList extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public PersonalLiveList() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PersonalLiveList(JsonNode value) { super(value); }
        /**
         * Returns data. The data value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<PersonalLiveSummary> getData() { return get("data", new TypeReference<>() {}); }
        /**
         * Sets data.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setData(List<PersonalLiveSummary> value) { set("data", value); }
        /**
         * Returns next cursor. Pass this value unchanged as after; null ends the collection.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getNextCursor() { return get("next_cursor", new TypeReference<>() {}); }
        /**
         * Sets next cursor.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setNextCursor(String value) { set("next_cursor", value); }
    }
    /**
     * Self-only safe user identity. No email, provider subject, internal principal, credentials or global user directory.
     */
    public static final class PersonalIdentity extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public PersonalIdentity() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PersonalIdentity(JsonNode value) { super(value); }
        /**
         * Returns id. The id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getId() { return get("id", new TypeReference<>() {}); }
        /**
         * Sets id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setId(String value) { set("id", value); }
        /**
         * Returns display name. The display name value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getDisplayName() { return get("display_name", new TypeReference<>() {}); }
        /**
         * Sets display name.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setDisplayName(String value) { set("display_name", value); }
        /**
         * Returns handle. The handle value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getHandle() { return get("handle", new TypeReference<>() {}); }
        /**
         * Sets handle.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setHandle(String value) { set("handle", value); }
        /**
         * Returns personal account id. The personal account id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getPersonalAccountId() { return get("personal_account_id", new TypeReference<>() {}); }
        /**
         * Sets personal account id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setPersonalAccountId(String value) { set("personal_account_id", value); }
    }
    /**
     * Wire representation of the personal account schema.
     */
    public static final class PersonalAccount extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public PersonalAccount() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PersonalAccount(JsonNode value) { super(value); }
        /**
         * Returns id. The id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getId() { return get("id", new TypeReference<>() {}); }
        /**
         * Sets id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setId(String value) { set("id", value); }
        /**
         * Returns kind. The kind value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getKind() { return get("kind", new TypeReference<>() {}); }
        /**
         * Sets kind.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setKind(String value) { set("kind", value); }
        /**
         * Returns name. The name value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getName() { return get("name", new TypeReference<>() {}); }
        /**
         * Sets name.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setName(String value) { set("name", value); }
        /**
         * Returns handle. The handle value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getHandle() { return get("handle", new TypeReference<>() {}); }
        /**
         * Sets handle.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setHandle(String value) { set("handle", value); }
    }
    /**
     * Wire representation of the personal account list schema.
     */
    public static final class PersonalAccountList extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public PersonalAccountList() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PersonalAccountList(JsonNode value) { super(value); }
        /**
         * Returns data. The data value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<PersonalAccount> getData() { return get("data", new TypeReference<>() {}); }
        /**
         * Sets data.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setData(List<PersonalAccount> value) { set("data", value); }
        /**
         * Returns next cursor. The next cursor value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getNextCursor() { return get("next_cursor", new TypeReference<>() {}); }
        /**
         * Sets next cursor.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setNextCursor(JsonNode value) { set("next_cursor", value); }
    }
    /**
     * Wire representation of the oauth scope schema.
     */
    public static final class OAuthScope {
        private final JsonNode value;
        /**
         * Copies a scalar or union wire value without adding wrapper fields.
         * @param value the non-null JSON value
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public OAuthScope(JsonNode value) { this.value = value.deepCopy(); }
        /**
         * Returns the underlying wire representation.
         * @return a defensive copy of the JSON value
         */
        @JsonValue public JsonNode value() { return value.deepCopy(); }
        @Override public String toString() { return "OAuthScope[redacted]"; }
    }
    /**
     * OAuth protocol error JSON, rather than REST Problem Details. Error redirects are sent only following a validated, session-bound consent decision. Invalid clients/redirects fail locally.
     */
    public static final class OAuthError extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public OAuthError() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public OAuthError(JsonNode value) { super(value); }
        /**
         * Returns error. The error value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getError() { return get("error", new TypeReference<>() {}); }
        /**
         * Sets error.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setError(String value) { set("error", value); }
    }
    /**
     * Opaque access token; at most 15 minutes and never beyond the grant deadline. refresh_token appears only with offline_access. Store tokens securely; hashes alone are retained server-side. Fixed grant lifetime 30 days; refreshing never extends it.
     */
    public static final class OAuthToken extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public OAuthToken() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public OAuthToken(JsonNode value) { super(value); }
        /**
         * Returns access token. The access token value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getAccessToken() { return get("access_token", new TypeReference<>() {}); }
        /**
         * Sets access token.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setAccessToken(String value) { set("access_token", value); }
        /**
         * Returns token type. The token type value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getTokenType() { return get("token_type", new TypeReference<>() {}); }
        /**
         * Sets token type.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setTokenType(String value) { set("token_type", value); }
        /**
         * Returns expires in. The expires in value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getExpiresIn() { return get("expires_in", new TypeReference<>() {}); }
        /**
         * Sets expires in.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setExpiresIn(Long value) { set("expires_in", value); }
        /**
         * Returns scope. The scope value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getScope() { return get("scope", new TypeReference<>() {}); }
        /**
         * Sets scope.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setScope(String value) { set("scope", value); }
        /**
         * Returns refresh token. The refresh token value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getRefreshToken() { return get("refresh_token", new TypeReference<>() {}); }
        /**
         * Sets refresh token.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setRefreshToken(String value) { set("refresh_token", value); }
    }
    /**
     * Wire representation of the oauth metadata schema.
     */
    public static final class OAuthMetadata extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public OAuthMetadata() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public OAuthMetadata(JsonNode value) { super(value); }
        /**
         * Returns issuer. The issuer value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getIssuer() { return get("issuer", new TypeReference<>() {}); }
        /**
         * Sets issuer.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setIssuer(String value) { set("issuer", value); }
        /**
         * Returns authorization endpoint. The authorization endpoint value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getAuthorizationEndpoint() { return get("authorization_endpoint", new TypeReference<>() {}); }
        /**
         * Sets authorization endpoint.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setAuthorizationEndpoint(String value) { set("authorization_endpoint", value); }
        /**
         * Returns token endpoint. The token endpoint value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getTokenEndpoint() { return get("token_endpoint", new TypeReference<>() {}); }
        /**
         * Sets token endpoint.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setTokenEndpoint(String value) { set("token_endpoint", value); }
        /**
         * Returns revocation endpoint. The revocation endpoint value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getRevocationEndpoint() { return get("revocation_endpoint", new TypeReference<>() {}); }
        /**
         * Sets revocation endpoint.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setRevocationEndpoint(String value) { set("revocation_endpoint", value); }
        /**
         * Returns response types supported. The response types supported value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<String> getResponseTypesSupported() { return get("response_types_supported", new TypeReference<>() {}); }
        /**
         * Sets response types supported.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setResponseTypesSupported(List<String> value) { set("response_types_supported", value); }
        /**
         * Returns grant types supported. The grant types supported value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<String> getGrantTypesSupported() { return get("grant_types_supported", new TypeReference<>() {}); }
        /**
         * Sets grant types supported.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setGrantTypesSupported(List<String> value) { set("grant_types_supported", value); }
        /**
         * Returns token endpoint auth methods supported. The token endpoint auth methods supported value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<String> getTokenEndpointAuthMethodsSupported() { return get("token_endpoint_auth_methods_supported", new TypeReference<>() {}); }
        /**
         * Sets token endpoint auth methods supported.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setTokenEndpointAuthMethodsSupported(List<String> value) { set("token_endpoint_auth_methods_supported", value); }
        /**
         * Returns revocation endpoint auth methods supported. The revocation endpoint auth methods supported value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<String> getRevocationEndpointAuthMethodsSupported() { return get("revocation_endpoint_auth_methods_supported", new TypeReference<>() {}); }
        /**
         * Sets revocation endpoint auth methods supported.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setRevocationEndpointAuthMethodsSupported(List<String> value) { set("revocation_endpoint_auth_methods_supported", value); }
        /**
         * Returns code challenge methods supported. The code challenge methods supported value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<String> getCodeChallengeMethodsSupported() { return get("code_challenge_methods_supported", new TypeReference<>() {}); }
        /**
         * Sets code challenge methods supported.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setCodeChallengeMethodsSupported(List<String> value) { set("code_challenge_methods_supported", value); }
        /**
         * Returns scopes supported. The scopes supported value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<String> getScopesSupported() { return get("scopes_supported", new TypeReference<>() {}); }
        /**
         * Sets scopes supported.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setScopesSupported(List<String> value) { set("scopes_supported", value); }
        /**
         * Returns authorization response iss parameter supported. The authorization response iss parameter supported value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Boolean getAuthorizationResponseIssParameterSupported() { return get("authorization_response_iss_parameter_supported", new TypeReference<>() {}); }
        /**
         * Sets authorization response iss parameter supported.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setAuthorizationResponseIssParameterSupported(Boolean value) { set("authorization_response_iss_parameter_supported", value); }
    }
    /**
     * Wire representation of the oauth grant schema.
     */
    public static final class OAuthGrant extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public OAuthGrant() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public OAuthGrant(JsonNode value) { super(value); }
        /**
         * Returns id. The id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getId() { return get("id", new TypeReference<>() {}); }
        /**
         * Sets id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setId(String value) { set("id", value); }
        /**
         * Returns client id. The client id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getClientId() { return get("client_id", new TypeReference<>() {}); }
        /**
         * Sets client id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setClientId(String value) { set("client_id", value); }
        /**
         * Returns client name. The client name value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getClientName() { return get("client_name", new TypeReference<>() {}); }
        /**
         * Sets client name.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setClientName(String value) { set("client_name", value); }
        /**
         * Returns scopes. The scopes value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<OAuthScope> getScopes() { return get("scopes", new TypeReference<>() {}); }
        /**
         * Sets scopes.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setScopes(List<OAuthScope> value) { set("scopes", value); }
        /**
         * Returns expires at. The expires at value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getExpiresAt() { return get("expires_at", new TypeReference<>() {}); }
        /**
         * Sets expires at.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setExpiresAt(Long value) { set("expires_at", value); }
    }
    /**
     * Wire representation of the oauth grant list schema.
     */
    public static final class OAuthGrantList extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public OAuthGrantList() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public OAuthGrantList(JsonNode value) { super(value); }
        /**
         * Returns data. The data value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<OAuthGrant> getData() { return get("data", new TypeReference<>() {}); }
        /**
         * Sets data.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setData(List<OAuthGrant> value) { set("data", value); }
    }
    /**
     * Wire representation of the live event viewer settings schema.
     */
    public static final class LiveEventViewerSettings extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public LiveEventViewerSettings() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveEventViewerSettings(JsonNode value) { super(value); }
        /**
         * Returns theme. The theme value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getTheme() { return get("theme", new TypeReference<>() {}); }
        /**
         * Sets theme.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setTheme(JsonNode value) { set("theme", value); }
        /**
         * Returns scale. Existing viewer font-size step, independent of Studio text size.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getScale() { return get("scale", new TypeReference<>() {}); }
        /**
         * Sets scale.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setScale(Long value) { set("scale", value); }
        /**
         * Returns presentation. The presentation value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Boolean getPresentation() { return get("presentation", new TypeReference<>() {}); }
        /**
         * Sets presentation.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setPresentation(Boolean value) { set("presentation", value); }
        /**
         * Returns preset. The preset value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getPreset() { return get("preset", new TypeReference<>() {}); }
        /**
         * Sets preset.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setPreset(JsonNode value) { set("preset", value); }
        /**
         * Returns transition. The transition value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getTransition() { return get("transition", new TypeReference<>() {}); }
        /**
         * Sets transition.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setTransition(JsonNode value) { set("transition", value); }
    }
    /**
     * Audience-safe Studio design. revision is independent of the Live sequence. Personal Lives use defaults and omit organization.
     */
    public static final class LiveEventDesign extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public LiveEventDesign() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveEventDesign(JsonNode value) { super(value); }
        /**
         * Returns settings. The settings value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public LiveStudioSettings getSettings() { return get("settings", new TypeReference<>() {}); }
        /**
         * Sets settings.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setSettings(LiveStudioSettings value) { set("settings", value); }
        /**
         * Returns revision. The revision value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getRevision() { return get("revision", new TypeReference<>() {}); }
        /**
         * Sets revision.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setRevision(Long value) { set("revision", value); }
        /**
         * Returns organization. The organization value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getOrganization() { return get("organization", new TypeReference<>() {}); }
        /**
         * Sets organization.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setOrganization(String value) { set("organization", value); }
    }
    /**
     * Current authorized snapshot. Queue is null in presentation mode. Presentation data is immutable snapshot metadata; editable documents, permissions, secrets, publisher identities and media paths are excluded. No audience membership or viewer statistics are created by reading.
     */
    public static final class LiveEventSnapshot extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public LiveEventSnapshot() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveEventSnapshot(JsonNode value) { super(value); }
        /**
         * Returns live. The live value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getLive() { return get("live", new TypeReference<>() {}); }
        /**
         * Sets live.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setLive(JsonNode value) { set("live", value); }
        /**
         * Returns display. The display value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getDisplay() { return get("display", new TypeReference<>() {}); }
        /**
         * Sets display.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setDisplay(JsonNode value) { set("display", value); }
        /**
         * Returns queue. The queue value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getQueue() { return get("queue", new TypeReference<>() {}); }
        /**
         * Sets queue.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setQueue(JsonNode value) { set("queue", value); }
        /**
         * Returns viewer settings. The viewer settings value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public LiveEventViewerSettings getViewerSettings() { return get("viewer_settings", new TypeReference<>() {}); }
        /**
         * Sets viewer settings.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setViewerSettings(LiveEventViewerSettings value) { set("viewer_settings", value); }
        /**
         * Returns design. The design value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public LiveEventDesign getDesign() { return get("design", new TypeReference<>() {}); }
        /**
         * Sets design.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setDesign(LiveEventDesign value) { set("design", value); }
    }
    /**
     * Wire representation of the live snapshot event schema.
     */
    public static final class LiveSnapshotEvent extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public LiveSnapshotEvent() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveSnapshotEvent(JsonNode value) { super(value); }
        /**
         * Returns schema version. The schema version value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getSchemaVersion() { return get("schema_version", new TypeReference<>() {}); }
        /**
         * Sets schema version.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setSchemaVersion(Long value) { set("schema_version", value); }
        /**
         * Returns type. The type value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getType() { return get("type", new TypeReference<>() {}); }
        /**
         * Sets type.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setType(String value) { set("type", value); }
        /**
         * Returns resource type. The resource type value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getResourceType() { return get("resource_type", new TypeReference<>() {}); }
        /**
         * Sets resource type.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setResourceType(String value) { set("resource_type", value); }
        /**
         * Returns resource id. The resource id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getResourceId() { return get("resource_id", new TypeReference<>() {}); }
        /**
         * Sets resource id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setResourceId(String value) { set("resource_id", value); }
        /**
         * Returns sequence. Current Live content sequence. Studio design revision can change independently. Never use an event cursor as an If-Match validator.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getSequence() { return get("sequence", new TypeReference<>() {}); }
        /**
         * Sets sequence.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setSequence(Long value) { set("sequence", value); }
        /**
         * Returns cursor. Opaque current-state comparison token covering the snapshot including independent design revisions. No durable log position, event ID, expiry promise or universal ETag.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getCursor() { return get("cursor", new TypeReference<>() {}); }
        /**
         * Sets cursor.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setCursor(String value) { set("cursor", value); }
        /**
         * Returns data. The data value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public LiveEventSnapshot getData() { return get("data", new TypeReference<>() {}); }
        /**
         * Sets data.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setData(LiveEventSnapshot value) { set("data", value); }
    }
    /**
     * Wire representation of the live heartbeat event schema.
     */
    public static final class LiveHeartbeatEvent extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public LiveHeartbeatEvent() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveHeartbeatEvent(JsonNode value) { super(value); }
        /**
         * Returns schema version. The schema version value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getSchemaVersion() { return get("schema_version", new TypeReference<>() {}); }
        /**
         * Sets schema version.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setSchemaVersion(Long value) { set("schema_version", value); }
        /**
         * Returns type. The type value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getType() { return get("type", new TypeReference<>() {}); }
        /**
         * Sets type.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setType(String value) { set("type", value); }
        /**
         * Returns resource type. The resource type value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getResourceType() { return get("resource_type", new TypeReference<>() {}); }
        /**
         * Sets resource type.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setResourceType(String value) { set("resource_type", value); }
        /**
         * Returns resource id. The resource id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getResourceId() { return get("resource_id", new TypeReference<>() {}); }
        /**
         * Sets resource id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setResourceId(String value) { set("resource_id", value); }
        /**
         * Returns sequence. Current Live content sequence. Studio design revision can change independently. Never use an event cursor as an If-Match validator.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getSequence() { return get("sequence", new TypeReference<>() {}); }
        /**
         * Sets sequence.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setSequence(Long value) { set("sequence", value); }
        /**
         * Returns cursor. Opaque current-state comparison token covering the snapshot including independent design revisions. No durable log position, event ID, expiry promise or universal ETag.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getCursor() { return get("cursor", new TypeReference<>() {}); }
        /**
         * Sets cursor.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setCursor(String value) { set("cursor", value); }
        /**
         * Returns data. The data value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public JsonNode getData() { return get("data", new TypeReference<>() {}); }
        /**
         * Sets data.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setData(JsonNode value) { set("data", value); }
    }
    /**
     * Event schema major version 1. Snapshot on connection/reconnection; changed snapshot when comparison differs; null-data heartbeat at wait expiry. Notifications may coalesce, repeat or be missed. Not exactly-once and no historical replay.
     */
    public static final class LiveEvent {
        private final JsonNode value;
        /**
         * Copies a scalar or union wire value without adding wrapper fields.
         * @param value the non-null JSON value
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveEvent(JsonNode value) { this.value = value.deepCopy(); }
        /**
         * Returns the underlying wire representation.
         * @return a defensive copy of the JSON value
         */
        @JsonValue public JsonNode value() { return value.deepCopy(); }
        @Override public String toString() { return "LiveEvent[redacted]"; }
    }
    /**
     * Wire representation of the account quota schema.
     */
    public static final class AccountQuota extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public AccountQuota() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public AccountQuota(JsonNode value) { super(value); }
        /**
         * Returns resource. The resource value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getResource() { return get("resource", new TypeReference<>() {}); }
        /**
         * Sets resource.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setResource(String value) { set("resource", value); }
        /**
         * Returns action. The action value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getAction() { return get("action", new TypeReference<>() {}); }
        /**
         * Sets action.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setAction(String value) { set("action", value); }
        /**
         * Returns limit. The limit value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getLimit() { return get("limit", new TypeReference<>() {}); }
        /**
         * Sets limit.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setLimit(Long value) { set("limit", value); }
        /**
         * Returns used. The used value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getUsed() { return get("used", new TypeReference<>() {}); }
        /**
         * Sets used.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setUsed(Long value) { set("used", value); }
    }
    /**
     * Wire representation of the account quota collection schema.
     */
    public static final class AccountQuotaCollection extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public AccountQuotaCollection() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public AccountQuotaCollection(JsonNode value) { super(value); }
        /**
         * Returns data. The data value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<AccountQuota> getData() { return get("data", new TypeReference<>() {}); }
        /**
         * Sets data.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setData(List<AccountQuota> value) { set("data", value); }
    }
    /**
     * Wire representation of the personal token metadata schema.
     */
    public static final class PersonalTokenMetadata extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public PersonalTokenMetadata() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PersonalTokenMetadata(JsonNode value) { super(value); }
        /**
         * Returns id. The id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getId() { return get("id", new TypeReference<>() {}); }
        /**
         * Sets id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setId(String value) { set("id", value); }
        /**
         * Returns label. The label value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getLabel() { return get("label", new TypeReference<>() {}); }
        /**
         * Sets label.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setLabel(String value) { set("label", value); }
        /**
         * Returns source. The source value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getSource() { return get("source", new TypeReference<>() {}); }
        /**
         * Sets source.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setSource(String value) { set("source", value); }
        /**
         * Returns scopes. The scopes value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<String> getScopes() { return get("scopes", new TypeReference<>() {}); }
        /**
         * Sets scopes.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setScopes(List<String> value) { set("scopes", value); }
        /**
         * Returns issued at. The issued at value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getIssuedAt() { return get("issued_at", new TypeReference<>() {}); }
        /**
         * Sets issued at.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setIssuedAt(Long value) { set("issued_at", value); }
        /**
         * Returns expires at. The expires at value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getExpiresAt() { return get("expires_at", new TypeReference<>() {}); }
        /**
         * Sets expires at.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setExpiresAt(Long value) { set("expires_at", value); }
        /**
         * Returns last used at. The last used at value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getLastUsedAt() { return get("last_used_at", new TypeReference<>() {}); }
        /**
         * Sets last used at.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setLastUsedAt(Long value) { set("last_used_at", value); }
        /**
         * Returns retiring. The retiring value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Boolean getRetiring() { return get("retiring", new TypeReference<>() {}); }
        /**
         * Sets retiring.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setRetiring(Boolean value) { set("retiring", value); }
    }
    /**
     * Wire representation of the personal token collection schema.
     */
    public static final class PersonalTokenCollection extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public PersonalTokenCollection() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PersonalTokenCollection(JsonNode value) { super(value); }
        /**
         * Returns data. The data value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<PersonalTokenMetadata> getData() { return get("data", new TypeReference<>() {}); }
        /**
         * Sets data.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setData(List<PersonalTokenMetadata> value) { set("data", value); }
        /**
         * Returns next cursor. The next cursor value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getNextCursor() { return get("next_cursor", new TypeReference<>() {}); }
        /**
         * Sets next cursor.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setNextCursor(String value) { set("next_cursor", value); }
    }
    /**
     * Wire representation of the personal directory entry schema.
     */
    public static final class PersonalDirectoryEntry extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public PersonalDirectoryEntry() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PersonalDirectoryEntry(JsonNode value) { super(value); }
        /**
         * Returns record. The record value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getRecordValue() { return get("record", new TypeReference<>() {}); }
        /**
         * Sets record.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setRecordValue(String value) { set("record", value); }
        /**
         * Returns id. The id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getId() { return get("id", new TypeReference<>() {}); }
        /**
         * Sets id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setId(String value) { set("id", value); }
        /**
         * Returns name. The name value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getName() { return get("name", new TypeReference<>() {}); }
        /**
         * Sets name.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setName(String value) { set("name", value); }
        /**
         * Returns handle. The handle value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getHandle() { return get("handle", new TypeReference<>() {}); }
        /**
         * Sets handle.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setHandle(String value) { set("handle", value); }
        /**
         * Returns role. The role value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getRole() { return get("role", new TypeReference<>() {}); }
        /**
         * Sets role.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setRole(String value) { set("role", value); }
        /**
         * Returns verification. The verification value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getVerification() { return get("verification", new TypeReference<>() {}); }
        /**
         * Sets verification.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setVerification(String value) { set("verification", value); }
        /**
         * Returns status. The status value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getStatus() { return get("status", new TypeReference<>() {}); }
        /**
         * Sets status.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setStatus(String value) { set("status", value); }
        /**
         * Returns application id. The application id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getApplicationId() { return get("application_id", new TypeReference<>() {}); }
        /**
         * Sets application id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setApplicationId(String value) { set("application_id", value); }
        /**
         * Returns domain. The domain value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getDomain() { return get("domain", new TypeReference<>() {}); }
        /**
         * Sets domain.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setDomain(String value) { set("domain", value); }
        /**
         * Returns state. The state value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getState() { return get("state", new TypeReference<>() {}); }
        /**
         * Sets state.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setState(String value) { set("state", value); }
        /**
         * Returns organization id. The organization id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getOrganizationId() { return get("organization_id", new TypeReference<>() {}); }
        /**
         * Sets organization id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setOrganizationId(String value) { set("organization_id", value); }
        /**
         * Returns verification url. The verification url value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getVerificationUrl() { return get("verification_url", new TypeReference<>() {}); }
        /**
         * Sets verification url.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setVerificationUrl(String value) { set("verification_url", value); }
        /**
         * Returns next action. The next action value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getNextAction() { return get("next_action", new TypeReference<>() {}); }
        /**
         * Sets next action.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setNextAction(String value) { set("next_action", value); }
    }
    /**
     * Wire representation of the personal directory schema.
     */
    public static final class PersonalDirectory extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public PersonalDirectory() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PersonalDirectory(JsonNode value) { super(value); }
        /**
         * Returns data. The data value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<PersonalDirectoryEntry> getData() { return get("data", new TypeReference<>() {}); }
        /**
         * Sets data.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setData(List<PersonalDirectoryEntry> value) { set("data", value); }
        /**
         * Returns next cursor. The next cursor value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getNextCursor() { return get("next_cursor", new TypeReference<>() {}); }
        /**
         * Sets next cursor.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setNextCursor(String value) { set("next_cursor", value); }
    }
    /**
     * Wire representation of the personal organization application schema.
     */
    public static final class PersonalOrganizationApplication extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public PersonalOrganizationApplication() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public PersonalOrganizationApplication(JsonNode value) { super(value); }
        /**
         * Returns application id. The application id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getApplicationId() { return get("application_id", new TypeReference<>() {}); }
        /**
         * Sets application id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setApplicationId(String value) { set("application_id", value); }
        /**
         * Returns name. The name value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getName() { return get("name", new TypeReference<>() {}); }
        /**
         * Sets name.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setName(String value) { set("name", value); }
        /**
         * Returns domain. The domain value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getDomain() { return get("domain", new TypeReference<>() {}); }
        /**
         * Sets domain.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setDomain(String value) { set("domain", value); }
        /**
         * Returns state. The state value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getState() { return get("state", new TypeReference<>() {}); }
        /**
         * Sets state.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setState(String value) { set("state", value); }
        /**
         * Returns organization id. The organization id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getOrganizationId() { return get("organization_id", new TypeReference<>() {}); }
        /**
         * Sets organization id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setOrganizationId(String value) { set("organization_id", value); }
        /**
         * Returns verification url. The verification url value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getVerificationUrl() { return get("verification_url", new TypeReference<>() {}); }
        /**
         * Sets verification url.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setVerificationUrl(String value) { set("verification_url", value); }
        /**
         * Returns next action. The next action value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getNextAction() { return get("next_action", new TypeReference<>() {}); }
        /**
         * Sets next action.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setNextAction(String value) { set("next_action", value); }
    }
    /**
     * Wire representation of the translation chapter metadata schema.
     */
    public static final class TranslationChapterMetadata extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public TranslationChapterMetadata() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public TranslationChapterMetadata(JsonNode value) { super(value); }
        /**
         * Returns chapter. The chapter value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getChapter() { return get("chapter", new TypeReference<>() {}); }
        /**
         * Sets chapter.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setChapter(Long value) { set("chapter", value); }
        /**
         * Returns verses. The verses value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getVerses() { return get("verses", new TypeReference<>() {}); }
        /**
         * Sets verses.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setVerses(Long value) { set("verses", value); }
    }
    /**
     * Wire representation of the translation book metadata schema.
     */
    public static final class TranslationBookMetadata extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public TranslationBookMetadata() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public TranslationBookMetadata(JsonNode value) { super(value); }
        /**
         * Returns book. The book value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getBook() { return get("book", new TypeReference<>() {}); }
        /**
         * Sets book.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setBook(Long value) { set("book", value); }
        /**
         * Returns name. The name value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getName() { return get("name", new TypeReference<>() {}); }
        /**
         * Sets name.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setName(String value) { set("name", value); }
        /**
         * Returns chapters. The chapters value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<TranslationChapterMetadata> getChapters() { return get("chapters", new TypeReference<>() {}); }
        /**
         * Sets chapters.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setChapters(List<TranslationChapterMetadata> value) { set("chapters", value); }
    }
    /**
     * Wire representation of the translation catalogue details schema.
     */
    public static final class TranslationCatalogueDetails extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public TranslationCatalogueDetails() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public TranslationCatalogueDetails(JsonNode value) { super(value); }
        /**
         * Returns translation. The translation value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getTranslation() { return get("translation", new TypeReference<>() {}); }
        /**
         * Sets translation.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setTranslation(String value) { set("translation", value); }
        /**
         * Returns data. The data value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<TranslationBookMetadata> getData() { return get("data", new TypeReference<>() {}); }
        /**
         * Sets data.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setData(List<TranslationBookMetadata> value) { set("data", value); }
    }
    /**
     * Wire representation of the live stack reference schema.
     */
    public static final class LiveStackReference extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public LiveStackReference() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveStackReference(JsonNode value) { super(value); }
        /**
         * Returns translation. The translation value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getTranslation() { return get("translation", new TypeReference<>() {}); }
        /**
         * Sets translation.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setTranslation(String value) { set("translation", value); }
        /**
         * Returns book id. The book id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getBookId() { return get("book_id", new TypeReference<>() {}); }
        /**
         * Sets book id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setBookId(Long value) { set("book_id", value); }
        /**
         * Returns chapter. The chapter value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getChapter() { return get("chapter", new TypeReference<>() {}); }
        /**
         * Sets chapter.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setChapter(Long value) { set("chapter", value); }
        /**
         * Returns verse. The verse value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getVerse() { return get("verse", new TypeReference<>() {}); }
        /**
         * Sets verse.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setVerse(Long value) { set("verse", value); }
    }
    /**
     * Wire representation of the live stack push schema.
     */
    public static final class LiveStackPush extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public LiveStackPush() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveStackPush(JsonNode value) { super(value); }
        /**
         * Returns reference. The reference value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public LiveStackReference getReference() { return get("reference", new TypeReference<>() {}); }
        /**
         * Sets reference.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setReference(LiveStackReference value) { set("reference", value); }
    }
    /**
     * Wire representation of the live stack removal schema.
     */
    public static final class LiveStackRemoval extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public LiveStackRemoval() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveStackRemoval(JsonNode value) { super(value); }
        /**
         * Returns count. The count value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getCount() { return get("count", new TypeReference<>() {}); }
        /**
         * Sets count.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setCount(Long value) { set("count", value); }
    }
    /**
     * Wire representation of the live stack verse schema.
     */
    public static final class LiveStackVerse extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public LiveStackVerse() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveStackVerse(JsonNode value) { super(value); }
        /**
         * Returns book id. The book id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getBookId() { return get("book_id", new TypeReference<>() {}); }
        /**
         * Sets book id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setBookId(Long value) { set("book_id", value); }
        /**
         * Returns book. The book value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getBook() { return get("book", new TypeReference<>() {}); }
        /**
         * Sets book.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setBook(String value) { set("book", value); }
        /**
         * Returns chapter. The chapter value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getChapter() { return get("chapter", new TypeReference<>() {}); }
        /**
         * Sets chapter.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setChapter(Long value) { set("chapter", value); }
        /**
         * Returns verse. The verse value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getVerse() { return get("verse", new TypeReference<>() {}); }
        /**
         * Sets verse.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setVerse(Long value) { set("verse", value); }
        /**
         * Returns text. The text value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getText() { return get("text", new TypeReference<>() {}); }
        /**
         * Sets text.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setText(String value) { set("text", value); }
        /**
         * Returns translation. The translation value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getTranslation() { return get("translation", new TypeReference<>() {}); }
        /**
         * Sets translation.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setTranslation(String value) { set("translation", value); }
        /**
         * Returns rights. The rights value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public TranslationRights getRights() { return get("rights", new TypeReference<>() {}); }
        /**
         * Sets rights.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setRights(TranslationRights value) { set("rights", value); }
        /**
         * Returns attribution. The attribution value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getAttribution() { return get("attribution", new TypeReference<>() {}); }
        /**
         * Sets attribution.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setAttribution(String value) { set("attribution", value); }
        /**
         * Returns position. The position value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getPosition() { return get("position", new TypeReference<>() {}); }
        /**
         * Sets position.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setPosition(Long value) { set("position", value); }
    }
    /**
     * Wire representation of the live stack schema.
     */
    public static final class LiveStack extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public LiveStack() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveStack(JsonNode value) { super(value); }
        /**
         * Returns live id. The live id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getLiveId() { return get("live_id", new TypeReference<>() {}); }
        /**
         * Sets live id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setLiveId(String value) { set("live_id", value); }
        /**
         * Returns sequence. The sequence value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getSequence() { return get("sequence", new TypeReference<>() {}); }
        /**
         * Sets sequence.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setSequence(Long value) { set("sequence", value); }
        /**
         * Returns affected. The affected value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getAffected() { return get("affected", new TypeReference<>() {}); }
        /**
         * Sets affected.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setAffected(Long value) { set("affected", value); }
        /**
         * Returns data. The data value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<LiveStackVerse> getData() { return get("data", new TypeReference<>() {}); }
        /**
         * Sets data.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setData(List<LiveStackVerse> value) { set("data", value); }
    }
    /**
     * Wire representation of the live settings schema.
     */
    public static final class LiveSettings extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public LiveSettings() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveSettings(JsonNode value) { super(value); }
        /**
         * Returns live id. The live id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getLiveId() { return get("live_id", new TypeReference<>() {}); }
        /**
         * Sets live id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setLiveId(String value) { set("live_id", value); }
        /**
         * Returns sequence. The sequence value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getSequence() { return get("sequence", new TypeReference<>() {}); }
        /**
         * Sets sequence.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setSequence(Long value) { set("sequence", value); }
        /**
         * Returns reference. The reference value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getReference() { return get("reference", new TypeReference<>() {}); }
        /**
         * Sets reference.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setReference(String value) { set("reference", value); }
        /**
         * Returns translations. The translations value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<String> getTranslations() { return get("translations", new TypeReference<>() {}); }
        /**
         * Sets translations.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setTranslations(List<String> value) { set("translations", value); }
    }
    /**
     * Wire representation of the live settings patch schema.
     */
    public static final class LiveSettingsPatch extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public LiveSettingsPatch() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveSettingsPatch(JsonNode value) { super(value); }
        /**
         * Returns reference. The reference value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getReference() { return get("reference", new TypeReference<>() {}); }
        /**
         * Sets reference.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setReference(String value) { set("reference", value); }
        /**
         * Returns translations. The translations value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<String> getTranslations() { return get("translations", new TypeReference<>() {}); }
        /**
         * Sets translations.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setTranslations(List<String> value) { set("translations", value); }
    }
    /**
     * Wire representation of the live subscriber schema.
     */
    public static final class LiveSubscriber extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public LiveSubscriber() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveSubscriber(JsonNode value) { super(value); }
        /**
         * Returns user. The user value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getUser() { return get("user", new TypeReference<>() {}); }
        /**
         * Sets user.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setUser(String value) { set("user", value); }
        /**
         * Returns access. The access value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getAccess() { return get("access", new TypeReference<>() {}); }
        /**
         * Sets access.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setAccess(String value) { set("access", value); }
        /**
         * Returns connected at. The connected at value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getConnectedAt() { return get("connected_at", new TypeReference<>() {}); }
        /**
         * Sets connected at.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setConnectedAt(Long value) { set("connected_at", value); }
        /**
         * Returns connected for seconds. The connected for seconds value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getConnectedForSeconds() { return get("connected_for_seconds", new TypeReference<>() {}); }
        /**
         * Sets connected for seconds.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setConnectedForSeconds(Long value) { set("connected_for_seconds", value); }
    }
    /**
     * Wire representation of the live statistics schema.
     */
    public static final class LiveStatistics extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public LiveStatistics() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveStatistics(JsonNode value) { super(value); }
        /**
         * Returns id. The id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getId() { return get("id", new TypeReference<>() {}); }
        /**
         * Sets id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setId(String value) { set("id", value); }
        /**
         * Returns sequence. The sequence value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getSequence() { return get("sequence", new TypeReference<>() {}); }
        /**
         * Sets sequence.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setSequence(Long value) { set("sequence", value); }
        /**
         * Returns running for seconds. The running for seconds value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getRunningForSeconds() { return get("running_for_seconds", new TypeReference<>() {}); }
        /**
         * Sets running for seconds.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setRunningForSeconds(Long value) { set("running_for_seconds", value); }
        /**
         * Returns stack entries. The stack entries value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getStackEntries() { return get("stack_entries", new TypeReference<>() {}); }
        /**
         * Sets stack entries.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setStackEntries(Long value) { set("stack_entries", value); }
        /**
         * Returns connections. The connections value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getConnections() { return get("connections", new TypeReference<>() {}); }
        /**
         * Sets connections.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setConnections(Long value) { set("connections", value); }
        /**
         * Returns user connections. The user connections value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getUserConnections() { return get("user_connections", new TypeReference<>() {}); }
        /**
         * Sets user connections.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setUserConnections(Long value) { set("user_connections", value); }
        /**
         * Returns anonymous connections. The anonymous connections value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getAnonymousConnections() { return get("anonymous_connections", new TypeReference<>() {}); }
        /**
         * Sets anonymous connections.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setAnonymousConnections(Long value) { set("anonymous_connections", value); }
        /**
         * Returns subscribers. The subscribers value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<LiveSubscriber> getSubscribers() { return get("subscribers", new TypeReference<>() {}); }
        /**
         * Sets subscribers.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setSubscribers(List<LiveSubscriber> value) { set("subscribers", value); }
    }
    /**
     * Wire representation of the live audience secret schema.
     */
    public static final class LiveAudienceSecret extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public LiveAudienceSecret() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveAudienceSecret(JsonNode value) { super(value); }
        /**
         * Returns live id. The live id value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getLiveId() { return get("live_id", new TypeReference<>() {}); }
        /**
         * Sets live id.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setLiveId(String value) { set("live_id", value); }
        /**
         * Returns sequence. The sequence value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getSequence() { return get("sequence", new TypeReference<>() {}); }
        /**
         * Sets sequence.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setSequence(Long value) { set("sequence", value); }
        /**
         * Returns protected. The protected value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Boolean getProtectedValue() { return get("protected", new TypeReference<>() {}); }
        /**
         * Sets protected.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setProtectedValue(Boolean value) { set("protected", value); }
        /**
         * Returns secret. The secret value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getSecret() { return get("secret", new TypeReference<>() {}); }
        /**
         * Sets secret.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setSecret(String value) { set("secret", value); }
    }
    /**
     * Wire representation of the live bulk deletion schema.
     */
    public static final class LiveBulkDeletion extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public LiveBulkDeletion() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public LiveBulkDeletion(JsonNode value) { super(value); }
        /**
         * Returns deleted. The deleted value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public Long getDeleted() { return get("deleted", new TypeReference<>() {}); }
        /**
         * Sets deleted.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setDeleted(Long value) { set("deleted", value); }
    }
    /**
     * One bounded book page, always paginated: default 50, maximum 500 verses, and at most 3MiB serialized JSON including metadata. The byte budget can return fewer verses with nonnull next_cursor. Stable chapter/verse order; no totals or unbounded mode.
     */
    public static final class ScriptureBookRead extends Model {
        /**
         * Creates an empty object; unset fields are omitted when serialized.
         */
        public ScriptureBookRead() {}
        /**
         * Creates a model from the complete JSON object, retaining unknown fields.
         * @param value the wire object to copy
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public ScriptureBookRead(JsonNode value) { super(value); }
        /**
         * Returns translation. The translation value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getTranslation() { return get("translation", new TypeReference<>() {}); }
        /**
         * Sets translation.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setTranslation(String value) { set("translation", value); }
        /**
         * Returns rights. The rights value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public TranslationRights getRights() { return get("rights", new TypeReference<>() {}); }
        /**
         * Sets rights.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setRights(TranslationRights value) { set("rights", value); }
        /**
         * Returns verses. The verses value.
         * @return the decoded field value; use hasField to test wire presence
         */
        public List<Verse> getVerses() { return get("verses", new TypeReference<>() {}); }
        /**
         * Sets verses.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setVerses(List<Verse> value) { set("verses", value); }
        /**
         * Returns next cursor. Required opaque continuation cursor. Null only on the last page; otherwise pass unchanged as after. Carries no authority.
         * @return the decoded field value; use hasField to test wire presence
         */
        public String getNextCursor() { return get("next_cursor", new TypeReference<>() {}); }
        /**
         * Sets next cursor.
         * @param value the Java value; null writes an explicit JSON null
         */
        public void setNextCursor(String value) { set("next_cursor", value); }
    }
}
