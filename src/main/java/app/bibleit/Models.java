package app.bibleit;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.type.TypeReference;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/** Typed Jackson beans; unions expose their known variant fields without wire wrappers. */
public final class Models {
    private Models() {}
    /**
     * Common wire-field presence and unknown-field support for typed Java beans.
     */
    public abstract static class Model {
        private final Set<String> present = new HashSet<>();
        private final Map<String, Object> additional = new LinkedHashMap<>();
        /**
         * Creates an object with no wire fields set.
         */
        protected Model() {}
        /**
         * Tests wire presence, distinguishing an omitted property from explicit null.
         * @param name the wire property name
         * @return true when the property was supplied
         */
        public boolean hasField(String name) { return present.contains(name); }
        /**
         * Removes a property and resets a known Java field to null.
         * @param name the wire property name
         */
        public void removeField(String name) { clearKnownField(name); present.remove(name); additional.remove(name); }
        /**
         * Assigns a known typed property or retains an unknown extension property.
         * @param name the wire property name
         * @param value a Java value; null is an explicit JSON null
         */
        @JsonAnySetter public void setField(String name, Object value) { Objects.requireNonNull(name); if (!assignKnownField(name, value)) { additional.put(name, value); present.add(name); } }
        /**
         * Returns the unknown server properties as ordinary Java values.
         * @return an unmodifiable snapshot of additional properties
         */
        public Map<String, Object> additionalProperties() { return Collections.unmodifiableMap(new LinkedHashMap<>(additional)); }
        /**
         * Returns a wire-property snapshot containing typed Java values.
         * @return the property snapshot; nested objects retain normal Java reference semantics
         */
        @JsonValue public Map<String, Object> toMap() { var values = new LinkedHashMap<>(additional); appendFields(values); return Collections.unmodifiableMap(values); }
        /**
         * Records that a typed setter supplied a property.
         * @param name the wire property name
         */
        protected void markField(String name) { present.add(name); }
        /**
         * Collects known properties that were explicitly set.
         * @param fields the output property snapshot
         */
        protected abstract void appendFields(Map<String, Object> fields);
        /**
         * Assigns an extension-style value to a known typed property.
         * @param name the wire property name
         * @param value the Java value
         * @return true when the name identifies a known property
         */
        protected abstract boolean assignKnownField(String name, Object value);
        /**
         * Resets a known Java property without marking it present.
         * @param name the wire property name
         */
        protected abstract void clearKnownField(String name);
        @Override public boolean equals(Object other) { return other != null && getClass() == other.getClass() && toMap().equals(((Model) other).toMap()); }
        @Override public int hashCode() { return Objects.hash(getClass(), toMap()); }
        @Override public String toString() { return getClass().getSimpleName() + "[redacted]"; }
    }
    /**
     * Java representation of problem.
     */
    public static final class Problem extends Model {
        private String type;
        private String title;
        private Long status;
        private String code;
        private String requestId;
        private String detail;
        /**
         * Creates an object with all properties unset.
         */
        public Problem() {}
        /**
         * Returns type.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("type") public String getType() { return type; }
        /**
         * Sets type.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("type") public void setType(String value) { type = value; markField("type"); }
        /**
         * Returns title.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("title") public String getTitle() { return title; }
        /**
         * Sets title.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("title") public void setTitle(String value) { title = value; markField("title"); }
        /**
         * Returns status.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("status") public Long getStatus() { return status; }
        /**
         * Sets status.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("status") public void setStatus(Long value) { status = value; markField("status"); }
        /**
         * Returns code.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("code") public String getCode() { return code; }
        /**
         * Sets code.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("code") public void setCode(String value) { code = value; markField("code"); }
        /**
         * Returns request id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("request_id") public String getRequestId() { return requestId; }
        /**
         * Sets request id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("request_id") public void setRequestId(String value) { requestId = value; markField("request_id"); }
        /**
         * Safe explanation and recovery guidance, including invalid, mismatched and stale Scripture cursors.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("detail") public String getDetail() { return detail; }
        /**
         * Sets detail.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("detail") public void setDetail(String value) { detail = value; markField("detail"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("type")) fields.put("type", type);
            if (hasField("title")) fields.put("title", title);
            if (hasField("status")) fields.put("status", status);
            if (hasField("code")) fields.put("code", code);
            if (hasField("request_id")) fields.put("request_id", requestId);
            if (hasField("detail")) fields.put("detail", detail);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "type" -> { setType(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "title" -> { setTitle(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "status" -> { setStatus(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "code" -> { setCode(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "request_id" -> { setRequestId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "detail" -> { setDetail(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "type" -> type = null;
                case "title" -> title = null;
                case "status" -> status = null;
                case "code" -> code = null;
                case "request_id" -> requestId = null;
                case "detail" -> detail = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of service token.
     */
    public static final class ServiceToken extends Model {
        private String id;
        private String organizationId;
        private String label;
        private List<Scope> scopes;
        private Long issuedAt;
        private Long expiresAt;
        private Long lastUsedAt;
        /**
         * Creates an object with all properties unset.
         */
        public ServiceToken() {}
        /**
         * Returns id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("id") public String getId() { return id; }
        /**
         * Sets id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("id") public void setId(String value) { id = value; markField("id"); }
        /**
         * Returns organization id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("organization_id") public String getOrganizationId() { return organizationId; }
        /**
         * Sets organization id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("organization_id") public void setOrganizationId(String value) { organizationId = value; markField("organization_id"); }
        /**
         * Returns label.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("label") public String getLabel() { return label; }
        /**
         * Sets label.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("label") public void setLabel(String value) { label = value; markField("label"); }
        /**
         * Returns scopes.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("scopes") public List<Scope> getScopes() { return scopes; }
        /**
         * Sets scopes.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("scopes") public void setScopes(List<Scope> value) { scopes = value; markField("scopes"); }
        /**
         * Returns issued at.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("issued_at") public Long getIssuedAt() { return issuedAt; }
        /**
         * Sets issued at.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("issued_at") public void setIssuedAt(Long value) { issuedAt = value; markField("issued_at"); }
        /**
         * Unix timestamp in seconds; null means no deadline or no recorded use.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("expires_at") public Long getExpiresAt() { return expiresAt; }
        /**
         * Sets expires at.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("expires_at") public void setExpiresAt(Long value) { expiresAt = value; markField("expires_at"); }
        /**
         * Unix timestamp in seconds; null means no deadline or no recorded use.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("last_used_at") public Long getLastUsedAt() { return lastUsedAt; }
        /**
         * Sets last used at.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("last_used_at") public void setLastUsedAt(Long value) { lastUsedAt = value; markField("last_used_at"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("id")) fields.put("id", id);
            if (hasField("organization_id")) fields.put("organization_id", organizationId);
            if (hasField("label")) fields.put("label", label);
            if (hasField("scopes")) fields.put("scopes", scopes);
            if (hasField("issued_at")) fields.put("issued_at", issuedAt);
            if (hasField("expires_at")) fields.put("expires_at", expiresAt);
            if (hasField("last_used_at")) fields.put("last_used_at", lastUsedAt);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "id" -> { setId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "organization_id" -> { setOrganizationId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "label" -> { setLabel(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "scopes" -> { setScopes(Json.MAPPER.convertValue(value, new TypeReference<List<Scope>>() {})); yield true; }
                case "issued_at" -> { setIssuedAt(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "expires_at" -> { setExpiresAt(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "last_used_at" -> { setLastUsedAt(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "id" -> id = null;
                case "organization_id" -> organizationId = null;
                case "label" -> label = null;
                case "scopes" -> scopes = null;
                case "issued_at" -> issuedAt = null;
                case "expires_at" -> expiresAt = null;
                case "last_used_at" -> lastUsedAt = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of create service token.
     */
    public static final class CreateServiceToken extends Model {
        private String label;
        private List<Scope> scopes;
        private Long expiresAt;
        /**
         * Creates an object with all properties unset.
         */
        public CreateServiceToken() {}
        /**
         * At most 128 UTF-8 bytes.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("label") public String getLabel() { return label; }
        /**
         * Sets label.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("label") public void setLabel(String value) { label = value; markField("label"); }
        /**
         * Every scope must be held by the issuer; duplicate entries are normalized.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("scopes") public List<Scope> getScopes() { return scopes; }
        /**
         * Sets scopes.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("scopes") public void setScopes(List<Scope> value) { scopes = value; markField("scopes"); }
        /**
         * Future Unix seconds, at most 90 days ahead. Omit for 30 days; null explicitly permits no expiration.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("expires_at") public Long getExpiresAt() { return expiresAt; }
        /**
         * Sets expires at.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("expires_at") public void setExpiresAt(Long value) { expiresAt = value; markField("expires_at"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("label")) fields.put("label", label);
            if (hasField("scopes")) fields.put("scopes", scopes);
            if (hasField("expires_at")) fields.put("expires_at", expiresAt);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "label" -> { setLabel(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "scopes" -> { setScopes(Json.MAPPER.convertValue(value, new TypeReference<List<Scope>>() {})); yield true; }
                case "expires_at" -> { setExpiresAt(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "label" -> label = null;
                case "scopes" -> scopes = null;
                case "expires_at" -> expiresAt = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of issued service token.
     */
    public static final class IssuedServiceToken extends Model {
        private String id;
        private String organizationId;
        private String label;
        private List<Scope> scopes;
        private Long expiresAt;
        private String secret;
        /**
         * Creates an object with all properties unset.
         */
        public IssuedServiceToken() {}
        /**
         * Returns id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("id") public String getId() { return id; }
        /**
         * Sets id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("id") public void setId(String value) { id = value; markField("id"); }
        /**
         * Returns organization id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("organization_id") public String getOrganizationId() { return organizationId; }
        /**
         * Sets organization id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("organization_id") public void setOrganizationId(String value) { organizationId = value; markField("organization_id"); }
        /**
         * Returns label.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("label") public String getLabel() { return label; }
        /**
         * Sets label.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("label") public void setLabel(String value) { label = value; markField("label"); }
        /**
         * Returns scopes.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("scopes") public List<Scope> getScopes() { return scopes; }
        /**
         * Sets scopes.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("scopes") public void setScopes(List<Scope> value) { scopes = value; markField("scopes"); }
        /**
         * Unix timestamp in seconds; null means no deadline or no recorded use.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("expires_at") public Long getExpiresAt() { return expiresAt; }
        /**
         * Sets expires at.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("expires_at") public void setExpiresAt(Long value) { expiresAt = value; markField("expires_at"); }
        /**
         * Returned once. Store in a confidential backend.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("secret") public String getSecret() { return secret; }
        /**
         * Sets secret.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("secret") public void setSecret(String value) { secret = value; markField("secret"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("id")) fields.put("id", id);
            if (hasField("organization_id")) fields.put("organization_id", organizationId);
            if (hasField("label")) fields.put("label", label);
            if (hasField("scopes")) fields.put("scopes", scopes);
            if (hasField("expires_at")) fields.put("expires_at", expiresAt);
            if (hasField("secret")) fields.put("secret", secret);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "id" -> { setId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "organization_id" -> { setOrganizationId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "label" -> { setLabel(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "scopes" -> { setScopes(Json.MAPPER.convertValue(value, new TypeReference<List<Scope>>() {})); yield true; }
                case "expires_at" -> { setExpiresAt(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "secret" -> { setSecret(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "id" -> id = null;
                case "organization_id" -> organizationId = null;
                case "label" -> label = null;
                case "scopes" -> scopes = null;
                case "expires_at" -> expiresAt = null;
                case "secret" -> secret = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live summary.
     */
    public static final class LiveSummary extends Model {
        private String id;
        private String organizationId;
        private String name;
        /**
         * Creates an object with all properties unset.
         */
        public LiveSummary() {}
        /**
         * Returns id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("id") public String getId() { return id; }
        /**
         * Sets id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("id") public void setId(String value) { id = value; markField("id"); }
        /**
         * Returns organization id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("organization_id") public String getOrganizationId() { return organizationId; }
        /**
         * Sets organization id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("organization_id") public void setOrganizationId(String value) { organizationId = value; markField("organization_id"); }
        /**
         * Returns name.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("name") public String getName() { return name; }
        /**
         * Sets name.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("name") public void setName(String value) { name = value; markField("name"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("id")) fields.put("id", id);
            if (hasField("organization_id")) fields.put("organization_id", organizationId);
            if (hasField("name")) fields.put("name", name);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "id" -> { setId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "organization_id" -> { setOrganizationId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "name" -> { setName(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "id" -> id = null;
                case "organization_id" -> organizationId = null;
                case "name" -> name = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of create live.
     */
    public static final class CreateLive extends Model {
        private String name;
        /**
         * Creates an object with all properties unset.
         */
        public CreateLive() {}
        /**
         * At most 128 UTF-8 bytes.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("name") public String getName() { return name; }
        /**
         * Sets name.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("name") public void setName(String value) { name = value; markField("name"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("name")) fields.put("name", name);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "name" -> { setName(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "name" -> name = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live.
     */
    public static final class Live extends Model {
        private String id;
        private String organizationId;
        private String name;
        private String status;
        private String mode;
        private Boolean paused;
        private Long sequence;
        /**
         * Creates an object with all properties unset.
         */
        public Live() {}
        /**
         * Returns id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("id") public String getId() { return id; }
        /**
         * Sets id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("id") public void setId(String value) { id = value; markField("id"); }
        /**
         * Returns organization id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("organization_id") public String getOrganizationId() { return organizationId; }
        /**
         * Sets organization id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("organization_id") public void setOrganizationId(String value) { organizationId = value; markField("organization_id"); }
        /**
         * Returns name.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("name") public String getName() { return name; }
        /**
         * Sets name.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("name") public void setName(String value) { name = value; markField("name"); }
        /**
         * Returns status.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("status") public String getStatus() { return status; }
        /**
         * Sets status.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("status") public void setStatus(String value) { status = value; markField("status"); }
        /**
         * Returns mode.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("mode") public String getMode() { return mode; }
        /**
         * Sets mode.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("mode") public void setMode(String value) { mode = value; markField("mode"); }
        /**
         * Returns paused.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("paused") public Boolean getPaused() { return paused; }
        /**
         * Sets paused.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("paused") public void setPaused(Boolean value) { paused = value; markField("paused"); }
        /**
         * Returns sequence.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("sequence") public Long getSequence() { return sequence; }
        /**
         * Sets sequence.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("sequence") public void setSequence(Long value) { sequence = value; markField("sequence"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("id")) fields.put("id", id);
            if (hasField("organization_id")) fields.put("organization_id", organizationId);
            if (hasField("name")) fields.put("name", name);
            if (hasField("status")) fields.put("status", status);
            if (hasField("mode")) fields.put("mode", mode);
            if (hasField("paused")) fields.put("paused", paused);
            if (hasField("sequence")) fields.put("sequence", sequence);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "id" -> { setId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "organization_id" -> { setOrganizationId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "name" -> { setName(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "status" -> { setStatus(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "mode" -> { setMode(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "paused" -> { setPaused(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "sequence" -> { setSequence(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "id" -> id = null;
                case "organization_id" -> organizationId = null;
                case "name" -> name = null;
                case "status" -> status = null;
                case "mode" -> mode = null;
                case "paused" -> paused = null;
                case "sequence" -> sequence = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of service token list.
     */
    public static final class ServiceTokenList extends Model {
        private List<ServiceToken> data;
        private String nextCursor;
        /**
         * Creates an object with all properties unset.
         */
        public ServiceTokenList() {}
        /**
         * Returns data.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("data") public List<ServiceToken> getData() { return data; }
        /**
         * Sets data.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("data") public void setData(List<ServiceToken> value) { data = value; markField("data"); }
        /**
         * Pass this value unchanged as after; null ends the collection.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("next_cursor") public String getNextCursor() { return nextCursor; }
        /**
         * Sets next cursor.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("next_cursor") public void setNextCursor(String value) { nextCursor = value; markField("next_cursor"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("data")) fields.put("data", data);
            if (hasField("next_cursor")) fields.put("next_cursor", nextCursor);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "data" -> { setData(Json.MAPPER.convertValue(value, new TypeReference<List<ServiceToken>>() {})); yield true; }
                case "next_cursor" -> { setNextCursor(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "data" -> data = null;
                case "next_cursor" -> nextCursor = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live list.
     */
    public static final class LiveList extends Model {
        private List<LiveSummary> data;
        private String nextCursor;
        /**
         * Creates an object with all properties unset.
         */
        public LiveList() {}
        /**
         * Returns data.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("data") public List<LiveSummary> getData() { return data; }
        /**
         * Sets data.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("data") public void setData(List<LiveSummary> value) { data = value; markField("data"); }
        /**
         * Pass this value unchanged as after; null ends the collection.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("next_cursor") public String getNextCursor() { return nextCursor; }
        /**
         * Sets next cursor.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("next_cursor") public void setNextCursor(String value) { nextCursor = value; markField("next_cursor"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("data")) fields.put("data", data);
            if (hasField("next_cursor")) fields.put("next_cursor", nextCursor);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "data" -> { setData(Json.MAPPER.convertValue(value, new TypeReference<List<LiveSummary>>() {})); yield true; }
                case "next_cursor" -> { setNextCursor(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "data" -> data = null;
                case "next_cursor" -> nextCursor = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of translation rights.
     */
    public static final class TranslationRights extends Model {
        private String edition;
        private String rightsStatus;
        private String licenseId;
        private String licenseUrl;
        private String sourceUrl;
        private String copyright;
        private String changeNotice;
        private String attribution;
        /**
         * Creates an object with all properties unset.
         */
        public TranslationRights() {}
        /**
         * Returns edition.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("edition") public String getEdition() { return edition; }
        /**
         * Sets edition.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("edition") public void setEdition(String value) { edition = value; markField("edition"); }
        /**
         * Returns rights status.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("rights_status") public String getRightsStatus() { return rightsStatus; }
        /**
         * Sets rights status.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("rights_status") public void setRightsStatus(String value) { rightsStatus = value; markField("rights_status"); }
        /**
         * Returns license id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("license_id") public String getLicenseId() { return licenseId; }
        /**
         * Sets license id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("license_id") public void setLicenseId(String value) { licenseId = value; markField("license_id"); }
        /**
         * Returns license url.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("license_url") public String getLicenseUrl() { return licenseUrl; }
        /**
         * Sets license url.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("license_url") public void setLicenseUrl(String value) { licenseUrl = value; markField("license_url"); }
        /**
         * Returns source url.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("source_url") public String getSourceUrl() { return sourceUrl; }
        /**
         * Sets source url.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("source_url") public void setSourceUrl(String value) { sourceUrl = value; markField("source_url"); }
        /**
         * Returns copyright.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("copyright") public String getCopyright() { return copyright; }
        /**
         * Sets copyright.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("copyright") public void setCopyright(String value) { copyright = value; markField("copyright"); }
        /**
         * Returns change notice.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("change_notice") public String getChangeNotice() { return changeNotice; }
        /**
         * Sets change notice.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("change_notice") public void setChangeNotice(String value) { changeNotice = value; markField("change_notice"); }
        /**
         * Returns attribution.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("attribution") public String getAttribution() { return attribution; }
        /**
         * Sets attribution.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("attribution") public void setAttribution(String value) { attribution = value; markField("attribution"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("edition")) fields.put("edition", edition);
            if (hasField("rights_status")) fields.put("rights_status", rightsStatus);
            if (hasField("license_id")) fields.put("license_id", licenseId);
            if (hasField("license_url")) fields.put("license_url", licenseUrl);
            if (hasField("source_url")) fields.put("source_url", sourceUrl);
            if (hasField("copyright")) fields.put("copyright", copyright);
            if (hasField("change_notice")) fields.put("change_notice", changeNotice);
            if (hasField("attribution")) fields.put("attribution", attribution);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "edition" -> { setEdition(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "rights_status" -> { setRightsStatus(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "license_id" -> { setLicenseId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "license_url" -> { setLicenseUrl(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "source_url" -> { setSourceUrl(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "copyright" -> { setCopyright(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "change_notice" -> { setChangeNotice(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "attribution" -> { setAttribution(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "edition" -> edition = null;
                case "rights_status" -> rightsStatus = null;
                case "license_id" -> licenseId = null;
                case "license_url" -> licenseUrl = null;
                case "source_url" -> sourceUrl = null;
                case "copyright" -> copyright = null;
                case "change_notice" -> changeNotice = null;
                case "attribution" -> attribution = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of verse.
     */
    public static final class Verse extends Model {
        private Long bookId;
        private String book;
        private Long chapter;
        private Long verse;
        private String text;
        /**
         * Creates an object with all properties unset.
         */
        public Verse() {}
        /**
         * Returns book id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("book_id") public Long getBookId() { return bookId; }
        /**
         * Sets book id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("book_id") public void setBookId(Long value) { bookId = value; markField("book_id"); }
        /**
         * Returns book.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("book") public String getBook() { return book; }
        /**
         * Sets book.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("book") public void setBook(String value) { book = value; markField("book"); }
        /**
         * Returns chapter.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("chapter") public Long getChapter() { return chapter; }
        /**
         * Sets chapter.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("chapter") public void setChapter(Long value) { chapter = value; markField("chapter"); }
        /**
         * Returns verse.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("verse") public Long getVerse() { return verse; }
        /**
         * Sets verse.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("verse") public void setVerse(Long value) { verse = value; markField("verse"); }
        /**
         * Returns text.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("text") public String getText() { return text; }
        /**
         * Sets text.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("text") public void setText(String value) { text = value; markField("text"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("book_id")) fields.put("book_id", bookId);
            if (hasField("book")) fields.put("book", book);
            if (hasField("chapter")) fields.put("chapter", chapter);
            if (hasField("verse")) fields.put("verse", verse);
            if (hasField("text")) fields.put("text", text);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "book_id" -> { setBookId(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "book" -> { setBook(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "chapter" -> { setChapter(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "verse" -> { setVerse(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "text" -> { setText(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "book_id" -> bookId = null;
                case "book" -> book = null;
                case "chapter" -> chapter = null;
                case "verse" -> verse = null;
                case "text" -> text = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of scripture read.
     */
    public static final class ScriptureRead extends Model {
        private String translation;
        private TranslationRights rights;
        private List<Verse> verses;
        /**
         * Creates an object with all properties unset.
         */
        public ScriptureRead() {}
        /**
         * Returns translation.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("translation") public String getTranslation() { return translation; }
        /**
         * Sets translation.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("translation") public void setTranslation(String value) { translation = value; markField("translation"); }
        /**
         * Returns rights.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("rights") public TranslationRights getRights() { return rights; }
        /**
         * Sets rights.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("rights") public void setRights(TranslationRights value) { rights = value; markField("rights"); }
        /**
         * Returns verses.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("verses") public List<Verse> getVerses() { return verses; }
        /**
         * Sets verses.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("verses") public void setVerses(List<Verse> value) { verses = value; markField("verses"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("translation")) fields.put("translation", translation);
            if (hasField("rights")) fields.put("rights", rights);
            if (hasField("verses")) fields.put("verses", verses);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "translation" -> { setTranslation(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "rights" -> { setRights(Json.MAPPER.convertValue(value, new TypeReference<TranslationRights>() {})); yield true; }
                case "verses" -> { setVerses(Json.MAPPER.convertValue(value, new TypeReference<List<Verse>>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "translation" -> translation = null;
                case "rights" -> rights = null;
                case "verses" -> verses = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of scripture search.
     */
    public static final class ScriptureSearch extends Model {
        private String translation;
        private TranslationRights rights;
        private List<Verse> verses;
        private Boolean hasMore;
        /**
         * Creates an object with all properties unset.
         */
        public ScriptureSearch() {}
        /**
         * Returns translation.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("translation") public String getTranslation() { return translation; }
        /**
         * Sets translation.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("translation") public void setTranslation(String value) { translation = value; markField("translation"); }
        /**
         * Returns rights.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("rights") public TranslationRights getRights() { return rights; }
        /**
         * Sets rights.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("rights") public void setRights(TranslationRights value) { rights = value; markField("rights"); }
        /**
         * Returns verses.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("verses") public List<Verse> getVerses() { return verses; }
        /**
         * Sets verses.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("verses") public void setVerses(List<Verse> value) { verses = value; markField("verses"); }
        /**
         * Returns has more.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("has_more") public Boolean getHasMore() { return hasMore; }
        /**
         * Sets has more.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("has_more") public void setHasMore(Boolean value) { hasMore = value; markField("has_more"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("translation")) fields.put("translation", translation);
            if (hasField("rights")) fields.put("rights", rights);
            if (hasField("verses")) fields.put("verses", verses);
            if (hasField("has_more")) fields.put("has_more", hasMore);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "translation" -> { setTranslation(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "rights" -> { setRights(Json.MAPPER.convertValue(value, new TypeReference<TranslationRights>() {})); yield true; }
                case "verses" -> { setVerses(Json.MAPPER.convertValue(value, new TypeReference<List<Verse>>() {})); yield true; }
                case "has_more" -> { setHasMore(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "translation" -> translation = null;
                case "rights" -> rights = null;
                case "verses" -> verses = null;
                case "has_more" -> hasMore = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of display verse.
     */
    public static final class DisplayVerse extends Model {
        private Long bookId;
        private String book;
        private Long chapter;
        private Long verse;
        private String text;
        private String translation;
        private TranslationRights rights;
        private String attribution;
        /**
         * Creates an object with all properties unset.
         */
        public DisplayVerse() {}
        /**
         * Returns book id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("book_id") public Long getBookId() { return bookId; }
        /**
         * Sets book id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("book_id") public void setBookId(Long value) { bookId = value; markField("book_id"); }
        /**
         * Returns book.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("book") public String getBook() { return book; }
        /**
         * Sets book.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("book") public void setBook(String value) { book = value; markField("book"); }
        /**
         * Returns chapter.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("chapter") public Long getChapter() { return chapter; }
        /**
         * Sets chapter.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("chapter") public void setChapter(Long value) { chapter = value; markField("chapter"); }
        /**
         * Returns verse.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("verse") public Long getVerse() { return verse; }
        /**
         * Sets verse.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("verse") public void setVerse(Long value) { verse = value; markField("verse"); }
        /**
         * Returns text.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("text") public String getText() { return text; }
        /**
         * Sets text.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("text") public void setText(String value) { text = value; markField("text"); }
        /**
         * Returns translation.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("translation") public String getTranslation() { return translation; }
        /**
         * Sets translation.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("translation") public void setTranslation(String value) { translation = value; markField("translation"); }
        /**
         * Returns rights.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("rights") public TranslationRights getRights() { return rights; }
        /**
         * Sets rights.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("rights") public void setRights(TranslationRights value) { rights = value; markField("rights"); }
        /**
         * Returns attribution.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("attribution") public String getAttribution() { return attribution; }
        /**
         * Sets attribution.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("attribution") public void setAttribution(String value) { attribution = value; markField("attribution"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("book_id")) fields.put("book_id", bookId);
            if (hasField("book")) fields.put("book", book);
            if (hasField("chapter")) fields.put("chapter", chapter);
            if (hasField("verse")) fields.put("verse", verse);
            if (hasField("text")) fields.put("text", text);
            if (hasField("translation")) fields.put("translation", translation);
            if (hasField("rights")) fields.put("rights", rights);
            if (hasField("attribution")) fields.put("attribution", attribution);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "book_id" -> { setBookId(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "book" -> { setBook(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "chapter" -> { setChapter(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "verse" -> { setVerse(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "text" -> { setText(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "translation" -> { setTranslation(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "rights" -> { setRights(Json.MAPPER.convertValue(value, new TypeReference<TranslationRights>() {})); yield true; }
                case "attribution" -> { setAttribution(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "book_id" -> bookId = null;
                case "book" -> book = null;
                case "chapter" -> chapter = null;
                case "verse" -> verse = null;
                case "text" -> text = null;
                case "translation" -> translation = null;
                case "rights" -> rights = null;
                case "attribution" -> attribution = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of scripture reference.
     */
    public static final class ScriptureReference extends Model {
        private String translation;
        private Long bookId;
        private Long chapter;
        private Long verseStart;
        private Long verseEnd;
        /**
         * Creates an object with all properties unset.
         */
        public ScriptureReference() {}
        /**
         * Returns translation.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("translation") public String getTranslation() { return translation; }
        /**
         * Sets translation.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("translation") public void setTranslation(String value) { translation = value; markField("translation"); }
        /**
         * Returns book id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("book_id") public Long getBookId() { return bookId; }
        /**
         * Sets book id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("book_id") public void setBookId(Long value) { bookId = value; markField("book_id"); }
        /**
         * Returns chapter.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("chapter") public Long getChapter() { return chapter; }
        /**
         * Sets chapter.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("chapter") public void setChapter(Long value) { chapter = value; markField("chapter"); }
        /**
         * Returns verse start.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("verse_start") public Long getVerseStart() { return verseStart; }
        /**
         * Sets verse start.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("verse_start") public void setVerseStart(Long value) { verseStart = value; markField("verse_start"); }
        /**
         * Returns verse end.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("verse_end") public Long getVerseEnd() { return verseEnd; }
        /**
         * Sets verse end.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("verse_end") public void setVerseEnd(Long value) { verseEnd = value; markField("verse_end"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("translation")) fields.put("translation", translation);
            if (hasField("book_id")) fields.put("book_id", bookId);
            if (hasField("chapter")) fields.put("chapter", chapter);
            if (hasField("verse_start")) fields.put("verse_start", verseStart);
            if (hasField("verse_end")) fields.put("verse_end", verseEnd);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "translation" -> { setTranslation(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "book_id" -> { setBookId(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "chapter" -> { setChapter(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "verse_start" -> { setVerseStart(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "verse_end" -> { setVerseEnd(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "translation" -> translation = null;
                case "book_id" -> bookId = null;
                case "chapter" -> chapter = null;
                case "verse_start" -> verseStart = null;
                case "verse_end" -> verseEnd = null;
                default -> { }
            }
        }
    }
    /**
     * Replace with credited scripture, an explicitly selected saved presentation revision, or clear. Scripture needs translation.read; presentation needs presentation.present in addition to live.update. Saving a presentation never publishes it. Scripture ranges contain 1..100 existing verses in one chapter. Known variant fields share one wire object; the server validates variant-specific requirements.
     */
    public static final class ReplaceDisplay extends Model {
        private String type;
        private ScriptureReference reference;
        private String presentationId;
        private Long revision;
        private String slideId;
        /**
         * Creates an object with all properties unset.
         */
        public ReplaceDisplay() {}
        /**
         * Returns type.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("type") public String getType() { return type; }
        /**
         * Sets type.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("type") public void setType(String value) { type = value; markField("type"); }
        /**
         * Returns reference.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("reference") public ScriptureReference getReference() { return reference; }
        /**
         * Sets reference.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("reference") public void setReference(ScriptureReference value) { reference = value; markField("reference"); }
        /**
         * Returns presentation id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("presentation_id") public String getPresentationId() { return presentationId; }
        /**
         * Sets presentation id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("presentation_id") public void setPresentationId(String value) { presentationId = value; markField("presentation_id"); }
        /**
         * Returns revision.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("revision") public Long getRevision() { return revision; }
        /**
         * Sets revision.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("revision") public void setRevision(Long value) { revision = value; markField("revision"); }
        /**
         * Returns slide id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("slide_id") public String getSlideId() { return slideId; }
        /**
         * Sets slide id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("slide_id") public void setSlideId(String value) { slideId = value; markField("slide_id"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("type")) fields.put("type", type);
            if (hasField("reference")) fields.put("reference", reference);
            if (hasField("presentation_id")) fields.put("presentation_id", presentationId);
            if (hasField("revision")) fields.put("revision", revision);
            if (hasField("slide_id")) fields.put("slide_id", slideId);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "type" -> { setType(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "reference" -> { setReference(Json.MAPPER.convertValue(value, new TypeReference<ScriptureReference>() {})); yield true; }
                case "presentation_id" -> { setPresentationId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "revision" -> { setRevision(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "slide_id" -> { setSlideId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "type" -> type = null;
                case "reference" -> reference = null;
                case "presentation_id" -> presentationId = null;
                case "revision" -> revision = null;
                case "slide_id" -> slideId = null;
                default -> { }
            }
        }
    }
    /**
     * Change lifecycle status and/or pause state atomically. Pausing a stopped Live returns 409. Stopping resets pause and visibility; starting alone does not redisplay content. paused:false resumes retained content on a running Live.
     */
    public static final class PlaybackPatch extends Model {
        private String status;
        private Boolean paused;
        /**
         * Creates an object with all properties unset.
         */
        public PlaybackPatch() {}
        /**
         * Returns status.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("status") public String getStatus() { return status; }
        /**
         * Sets status.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("status") public void setStatus(String value) { status = value; markField("status"); }
        /**
         * Returns paused.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("paused") public Boolean getPaused() { return paused; }
        /**
         * Sets paused.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("paused") public void setPaused(Boolean value) { paused = value; markField("paused"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("status")) fields.put("status", status);
            if (hasField("paused")) fields.put("paused", paused);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "status" -> { setStatus(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "paused" -> { setPaused(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "status" -> status = null;
                case "paused" -> paused = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of playback.
     */
    public static final class Playback extends Model {
        private String liveId;
        private String status;
        private Boolean paused;
        private Boolean showing;
        private Long sequence;
        /**
         * Creates an object with all properties unset.
         */
        public Playback() {}
        /**
         * Returns live id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("live_id") public String getLiveId() { return liveId; }
        /**
         * Sets live id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("live_id") public void setLiveId(String value) { liveId = value; markField("live_id"); }
        /**
         * Returns status.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("status") public String getStatus() { return status; }
        /**
         * Sets status.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("status") public void setStatus(String value) { status = value; markField("status"); }
        /**
         * Returns paused.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("paused") public Boolean getPaused() { return paused; }
        /**
         * Sets paused.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("paused") public void setPaused(Boolean value) { paused = value; markField("paused"); }
        /**
         * Returns showing.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("showing") public Boolean getShowing() { return showing; }
        /**
         * Sets showing.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("showing") public void setShowing(Boolean value) { showing = value; markField("showing"); }
        /**
         * Returns sequence.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("sequence") public Long getSequence() { return sequence; }
        /**
         * Sets sequence.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("sequence") public void setSequence(Long value) { sequence = value; markField("sequence"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("live_id")) fields.put("live_id", liveId);
            if (hasField("status")) fields.put("status", status);
            if (hasField("paused")) fields.put("paused", paused);
            if (hasField("showing")) fields.put("showing", showing);
            if (hasField("sequence")) fields.put("sequence", sequence);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "live_id" -> { setLiveId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "status" -> { setStatus(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "paused" -> { setPaused(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "showing" -> { setShowing(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "sequence" -> { setSequence(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "live_id" -> liveId = null;
                case "status" -> status = null;
                case "paused" -> paused = null;
                case "showing" -> showing = null;
                case "sequence" -> sequence = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of snapshot summary.
     */
    public static final class SnapshotSummary extends Model {
        private String id;
        private String deck;
        private Long revision;
        private Long index;
        private Boolean blank;
        private String slideId;
        /**
         * Creates an object with all properties unset.
         */
        public SnapshotSummary() {}
        /**
         * Returns id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("id") public String getId() { return id; }
        /**
         * Sets id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("id") public void setId(String value) { id = value; markField("id"); }
        /**
         * Returns deck.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("deck") public String getDeck() { return deck; }
        /**
         * Sets deck.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("deck") public void setDeck(String value) { deck = value; markField("deck"); }
        /**
         * Returns revision.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("revision") public Long getRevision() { return revision; }
        /**
         * Sets revision.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("revision") public void setRevision(Long value) { revision = value; markField("revision"); }
        /**
         * Returns index.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("index") public Long getIndex() { return index; }
        /**
         * Sets index.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("index") public void setIndex(Long value) { index = value; markField("index"); }
        /**
         * Returns blank.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("blank") public Boolean getBlank() { return blank; }
        /**
         * Sets blank.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("blank") public void setBlank(Boolean value) { blank = value; markField("blank"); }
        /**
         * Stable selected slide ID from the immutable saved snapshot. Prefer this over the retained zero-based index.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("slide_id") public String getSlideId() { return slideId; }
        /**
         * Sets slide id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("slide_id") public void setSlideId(String value) { slideId = value; markField("slide_id"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("id")) fields.put("id", id);
            if (hasField("deck")) fields.put("deck", deck);
            if (hasField("revision")) fields.put("revision", revision);
            if (hasField("index")) fields.put("index", index);
            if (hasField("blank")) fields.put("blank", blank);
            if (hasField("slide_id")) fields.put("slide_id", slideId);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "id" -> { setId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "deck" -> { setDeck(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "revision" -> { setRevision(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "index" -> { setIndex(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "blank" -> { setBlank(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "slide_id" -> { setSlideId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "id" -> id = null;
                case "deck" -> deck = null;
                case "revision" -> revision = null;
                case "index" -> index = null;
                case "blank" -> blank = null;
                case "slide_id" -> slideId = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live display.
     */
    public static final class LiveDisplay extends Model {
        private String liveId;
        private String status;
        private Boolean paused;
        private Boolean showing;
        private Long sequence;
        private String organizationId;
        private String type;
        private List<DisplayVerse> verses;
        private SnapshotSummary snapshot;
        /**
         * Creates an object with all properties unset.
         */
        public LiveDisplay() {}
        /**
         * Returns live id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("live_id") public String getLiveId() { return liveId; }
        /**
         * Sets live id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("live_id") public void setLiveId(String value) { liveId = value; markField("live_id"); }
        /**
         * Returns status.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("status") public String getStatus() { return status; }
        /**
         * Sets status.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("status") public void setStatus(String value) { status = value; markField("status"); }
        /**
         * Returns paused.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("paused") public Boolean getPaused() { return paused; }
        /**
         * Sets paused.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("paused") public void setPaused(Boolean value) { paused = value; markField("paused"); }
        /**
         * Returns showing.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("showing") public Boolean getShowing() { return showing; }
        /**
         * Sets showing.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("showing") public void setShowing(Boolean value) { showing = value; markField("showing"); }
        /**
         * Returns sequence.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("sequence") public Long getSequence() { return sequence; }
        /**
         * Sets sequence.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("sequence") public void setSequence(Long value) { sequence = value; markField("sequence"); }
        /**
         * Returns organization id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("organization_id") public String getOrganizationId() { return organizationId; }
        /**
         * Sets organization id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("organization_id") public void setOrganizationId(String value) { organizationId = value; markField("organization_id"); }
        /**
         * Returns type.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("type") public String getType() { return type; }
        /**
         * Sets type.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("type") public void setType(String value) { type = value; markField("type"); }
        /**
         * Returns verses.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("verses") public List<DisplayVerse> getVerses() { return verses; }
        /**
         * Sets verses.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("verses") public void setVerses(List<DisplayVerse> value) { verses = value; markField("verses"); }
        /**
         * Returns snapshot.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("snapshot") public SnapshotSummary getSnapshot() { return snapshot; }
        /**
         * Sets snapshot.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("snapshot") public void setSnapshot(SnapshotSummary value) { snapshot = value; markField("snapshot"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("live_id")) fields.put("live_id", liveId);
            if (hasField("status")) fields.put("status", status);
            if (hasField("paused")) fields.put("paused", paused);
            if (hasField("showing")) fields.put("showing", showing);
            if (hasField("sequence")) fields.put("sequence", sequence);
            if (hasField("organization_id")) fields.put("organization_id", organizationId);
            if (hasField("type")) fields.put("type", type);
            if (hasField("verses")) fields.put("verses", verses);
            if (hasField("snapshot")) fields.put("snapshot", snapshot);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "live_id" -> { setLiveId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "status" -> { setStatus(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "paused" -> { setPaused(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "showing" -> { setShowing(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "sequence" -> { setSequence(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "organization_id" -> { setOrganizationId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "type" -> { setType(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "verses" -> { setVerses(Json.MAPPER.convertValue(value, new TypeReference<List<DisplayVerse>>() {})); yield true; }
                case "snapshot" -> { setSnapshot(Json.MAPPER.convertValue(value, new TypeReference<SnapshotSummary>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "live_id" -> liveId = null;
                case "status" -> status = null;
                case "paused" -> paused = null;
                case "showing" -> showing = null;
                case "sequence" -> sequence = null;
                case "organization_id" -> organizationId = null;
                case "type" -> type = null;
                case "verses" -> verses = null;
                case "snapshot" -> snapshot = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of service token configuration.
     */
    public static final class ServiceTokenConfiguration extends Model {
        private String id;
        private String organizationId;
        private String label;
        private List<Scope> scopes;
        private Long issuedAt;
        private Long expiresAt;
        /**
         * Creates an object with all properties unset.
         */
        public ServiceTokenConfiguration() {}
        /**
         * Returns id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("id") public String getId() { return id; }
        /**
         * Sets id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("id") public void setId(String value) { id = value; markField("id"); }
        /**
         * Returns organization id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("organization_id") public String getOrganizationId() { return organizationId; }
        /**
         * Sets organization id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("organization_id") public void setOrganizationId(String value) { organizationId = value; markField("organization_id"); }
        /**
         * Returns label.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("label") public String getLabel() { return label; }
        /**
         * Sets label.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("label") public void setLabel(String value) { label = value; markField("label"); }
        /**
         * Returns scopes.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("scopes") public List<Scope> getScopes() { return scopes; }
        /**
         * Sets scopes.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("scopes") public void setScopes(List<Scope> value) { scopes = value; markField("scopes"); }
        /**
         * Returns issued at.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("issued_at") public Long getIssuedAt() { return issuedAt; }
        /**
         * Sets issued at.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("issued_at") public void setIssuedAt(Long value) { issuedAt = value; markField("issued_at"); }
        /**
         * Unix timestamp in seconds; null means no deadline or no recorded use.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("expires_at") public Long getExpiresAt() { return expiresAt; }
        /**
         * Sets expires at.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("expires_at") public void setExpiresAt(Long value) { expiresAt = value; markField("expires_at"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("id")) fields.put("id", id);
            if (hasField("organization_id")) fields.put("organization_id", organizationId);
            if (hasField("label")) fields.put("label", label);
            if (hasField("scopes")) fields.put("scopes", scopes);
            if (hasField("issued_at")) fields.put("issued_at", issuedAt);
            if (hasField("expires_at")) fields.put("expires_at", expiresAt);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "id" -> { setId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "organization_id" -> { setOrganizationId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "label" -> { setLabel(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "scopes" -> { setScopes(Json.MAPPER.convertValue(value, new TypeReference<List<Scope>>() {})); yield true; }
                case "issued_at" -> { setIssuedAt(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "expires_at" -> { setExpiresAt(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "id" -> id = null;
                case "organization_id" -> organizationId = null;
                case "label" -> label = null;
                case "scopes" -> scopes = null;
                case "issued_at" -> issuedAt = null;
                case "expires_at" -> expiresAt = null;
                default -> { }
            }
        }
    }
    /**
     * Partial configuration edit. Omitted label/scopes/expiry are preserved exactly. expires_at:null explicitly permits no expiration; finite deadlines must be future and at most 90 days ahead. The resulting scopes must be a subset of the caller scopes. Repeated authentication does not change the configuration ETag.
     */
    public static final class PatchServiceToken extends Model {
        private String label;
        private List<Scope> scopes;
        private Long expiresAt;
        /**
         * Creates an object with all properties unset.
         */
        public PatchServiceToken() {}
        /**
         * At most 128 UTF-8 bytes.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("label") public String getLabel() { return label; }
        /**
         * Sets label.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("label") public void setLabel(String value) { label = value; markField("label"); }
        /**
         * Every scope must be held by the issuer; duplicate entries are normalized.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("scopes") public List<Scope> getScopes() { return scopes; }
        /**
         * Sets scopes.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("scopes") public void setScopes(List<Scope> value) { scopes = value; markField("scopes"); }
        /**
         * Future Unix seconds, at most 90 days ahead. Omit to preserve the exact existing expiry; null explicitly selects no expiration.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("expires_at") public Long getExpiresAt() { return expiresAt; }
        /**
         * Sets expires at.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("expires_at") public void setExpiresAt(Long value) { expiresAt = value; markField("expires_at"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("label")) fields.put("label", label);
            if (hasField("scopes")) fields.put("scopes", scopes);
            if (hasField("expires_at")) fields.put("expires_at", expiresAt);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "label" -> { setLabel(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "scopes" -> { setScopes(Json.MAPPER.convertValue(value, new TypeReference<List<Scope>>() {})); yield true; }
                case "expires_at" -> { setExpiresAt(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "label" -> label = null;
                case "scopes" -> scopes = null;
                case "expires_at" -> expiresAt = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of empty object.
     */
    public static final class EmptyObject extends Model {
        /**
         * Creates an object with all properties unset.
         */
        public EmptyObject() {}
        @Override protected void appendFields(Map<String, Object> fields) {
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                default -> { }
            }
        }
    }
    /**
     * Java representation of translation provenance.
     */
    public static final class TranslationProvenance extends Model {
        private String archiveSha256;
        private String reviewedOn;
        private String sourceId;
        /**
         * Creates an object with all properties unset.
         */
        public TranslationProvenance() {}
        /**
         * Returns archive sha256.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("archive_sha256") public String getArchiveSha256() { return archiveSha256; }
        /**
         * Sets archive sha256.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("archive_sha256") public void setArchiveSha256(String value) { archiveSha256 = value; markField("archive_sha256"); }
        /**
         * Returns reviewed on.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("reviewed_on") public String getReviewedOn() { return reviewedOn; }
        /**
         * Sets reviewed on.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("reviewed_on") public void setReviewedOn(String value) { reviewedOn = value; markField("reviewed_on"); }
        /**
         * Returns source id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("source_id") public String getSourceId() { return sourceId; }
        /**
         * Sets source id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("source_id") public void setSourceId(String value) { sourceId = value; markField("source_id"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("archive_sha256")) fields.put("archive_sha256", archiveSha256);
            if (hasField("reviewed_on")) fields.put("reviewed_on", reviewedOn);
            if (hasField("source_id")) fields.put("source_id", sourceId);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "archive_sha256" -> { setArchiveSha256(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "reviewed_on" -> { setReviewedOn(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "source_id" -> { setSourceId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "archive_sha256" -> archiveSha256 = null;
                case "reviewed_on" -> reviewedOn = null;
                case "source_id" -> sourceId = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of catalogue translation.
     */
    public static final class CatalogueTranslation extends Model {
        private String slug;
        private String name;
        private String language;
        private List<Long> bookIds;
        private TranslationRights rights;
        private Boolean installed;
        private TranslationProvenance provenance;
        /**
         * Creates an object with all properties unset.
         */
        public CatalogueTranslation() {}
        /**
         * Returns slug.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("slug") public String getSlug() { return slug; }
        /**
         * Sets slug.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("slug") public void setSlug(String value) { slug = value; markField("slug"); }
        /**
         * Returns name.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("name") public String getName() { return name; }
        /**
         * Sets name.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("name") public void setName(String value) { name = value; markField("name"); }
        /**
         * Returns language.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("language") public String getLanguage() { return language; }
        /**
         * Sets language.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("language") public void setLanguage(String value) { language = value; markField("language"); }
        /**
         * Returns book ids.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("book_ids") public List<Long> getBookIds() { return bookIds; }
        /**
         * Sets book ids.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("book_ids") public void setBookIds(List<Long> value) { bookIds = value; markField("book_ids"); }
        /**
         * Returns rights.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("rights") public TranslationRights getRights() { return rights; }
        /**
         * Sets rights.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("rights") public void setRights(TranslationRights value) { rights = value; markField("rights"); }
        /**
         * Returns installed.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("installed") public Boolean getInstalled() { return installed; }
        /**
         * Sets installed.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("installed") public void setInstalled(Boolean value) { installed = value; markField("installed"); }
        /**
         * Returns provenance.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("provenance") public TranslationProvenance getProvenance() { return provenance; }
        /**
         * Sets provenance.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("provenance") public void setProvenance(TranslationProvenance value) { provenance = value; markField("provenance"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("slug")) fields.put("slug", slug);
            if (hasField("name")) fields.put("name", name);
            if (hasField("language")) fields.put("language", language);
            if (hasField("book_ids")) fields.put("book_ids", bookIds);
            if (hasField("rights")) fields.put("rights", rights);
            if (hasField("installed")) fields.put("installed", installed);
            if (hasField("provenance")) fields.put("provenance", provenance);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "slug" -> { setSlug(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "name" -> { setName(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "language" -> { setLanguage(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "book_ids" -> { setBookIds(Json.MAPPER.convertValue(value, new TypeReference<List<Long>>() {})); yield true; }
                case "rights" -> { setRights(Json.MAPPER.convertValue(value, new TypeReference<TranslationRights>() {})); yield true; }
                case "installed" -> { setInstalled(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "provenance" -> { setProvenance(Json.MAPPER.convertValue(value, new TypeReference<TranslationProvenance>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "slug" -> slug = null;
                case "name" -> name = null;
                case "language" -> language = null;
                case "book_ids" -> bookIds = null;
                case "rights" -> rights = null;
                case "installed" -> installed = null;
                case "provenance" -> provenance = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of book definition.
     */
    public static final class BookDefinition extends Model {
        private Long id;
        private String code;
        private List<String> aliases;
        private BookDefinitionNames names;
        /**
         * Creates an object with all properties unset.
         */
        public BookDefinition() {}
        /**
         * Returns id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("id") public Long getId() { return id; }
        /**
         * Sets id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("id") public void setId(Long value) { id = value; markField("id"); }
        /**
         * Returns code.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("code") public String getCode() { return code; }
        /**
         * Sets code.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("code") public void setCode(String value) { code = value; markField("code"); }
        /**
         * Returns aliases.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("aliases") public List<String> getAliases() { return aliases; }
        /**
         * Sets aliases.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("aliases") public void setAliases(List<String> value) { aliases = value; markField("aliases"); }
        /**
         * Returns names.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("names") public BookDefinitionNames getNames() { return names; }
        /**
         * Sets names.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("names") public void setNames(BookDefinitionNames value) { names = value; markField("names"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("id")) fields.put("id", id);
            if (hasField("code")) fields.put("code", code);
            if (hasField("aliases")) fields.put("aliases", aliases);
            if (hasField("names")) fields.put("names", names);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "id" -> { setId(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "code" -> { setCode(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "aliases" -> { setAliases(Json.MAPPER.convertValue(value, new TypeReference<List<String>>() {})); yield true; }
                case "names" -> { setNames(Json.MAPPER.convertValue(value, new TypeReference<BookDefinitionNames>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "id" -> id = null;
                case "code" -> code = null;
                case "aliases" -> aliases = null;
                case "names" -> names = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of translation collection.
     */
    public static final class TranslationCollection extends Model {
        private List<CatalogueTranslation> data;
        private String nextCursor;
        /**
         * Creates an object with all properties unset.
         */
        public TranslationCollection() {}
        /**
         * Returns data.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("data") public List<CatalogueTranslation> getData() { return data; }
        /**
         * Sets data.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("data") public void setData(List<CatalogueTranslation> value) { data = value; markField("data"); }
        /**
         * Pass unchanged as after on the next request; null ends the collection.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("next_cursor") public String getNextCursor() { return nextCursor; }
        /**
         * Sets next cursor.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("next_cursor") public void setNextCursor(String value) { nextCursor = value; markField("next_cursor"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("data")) fields.put("data", data);
            if (hasField("next_cursor")) fields.put("next_cursor", nextCursor);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "data" -> { setData(Json.MAPPER.convertValue(value, new TypeReference<List<CatalogueTranslation>>() {})); yield true; }
                case "next_cursor" -> { setNextCursor(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "data" -> data = null;
                case "next_cursor" -> nextCursor = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of book collection.
     */
    public static final class BookCollection extends Model {
        private List<BookDefinition> data;
        private String nextCursor;
        /**
         * Creates an object with all properties unset.
         */
        public BookCollection() {}
        /**
         * Returns data.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("data") public List<BookDefinition> getData() { return data; }
        /**
         * Sets data.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("data") public void setData(List<BookDefinition> value) { data = value; markField("data"); }
        /**
         * Pass unchanged as after on the next request; null ends the collection.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("next_cursor") public String getNextCursor() { return nextCursor; }
        /**
         * Sets next cursor.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("next_cursor") public void setNextCursor(String value) { nextCursor = value; markField("next_cursor"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("data")) fields.put("data", data);
            if (hasField("next_cursor")) fields.put("next_cursor", nextCursor);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "data" -> { setData(Json.MAPPER.convertValue(value, new TypeReference<List<BookDefinition>>() {})); yield true; }
                case "next_cursor" -> { setNextCursor(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "data" -> data = null;
                case "next_cursor" -> nextCursor = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of organization.
     */
    public static final class Organization extends Model {
        private String id;
        private String name;
        private String handle;
        private String logoUrl;
        private String website;
        /**
         * Creates an object with all properties unset.
         */
        public Organization() {}
        /**
         * Returns id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("id") public String getId() { return id; }
        /**
         * Sets id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("id") public void setId(String value) { id = value; markField("id"); }
        /**
         * Returns name.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("name") public String getName() { return name; }
        /**
         * Sets name.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("name") public void setName(String value) { name = value; markField("name"); }
        /**
         * Returns handle.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("handle") public String getHandle() { return handle; }
        /**
         * Sets handle.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("handle") public void setHandle(String value) { handle = value; markField("handle"); }
        /**
         * Returns logo url.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("logo_url") public String getLogoUrl() { return logoUrl; }
        /**
         * Sets logo url.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("logo_url") public void setLogoUrl(String value) { logoUrl = value; markField("logo_url"); }
        /**
         * Returns website.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("website") public String getWebsite() { return website; }
        /**
         * Sets website.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("website") public void setWebsite(String value) { website = value; markField("website"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("id")) fields.put("id", id);
            if (hasField("name")) fields.put("name", name);
            if (hasField("handle")) fields.put("handle", handle);
            if (hasField("logo_url")) fields.put("logo_url", logoUrl);
            if (hasField("website")) fields.put("website", website);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "id" -> { setId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "name" -> { setName(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "handle" -> { setHandle(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "logo_url" -> { setLogoUrl(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "website" -> { setWebsite(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "id" -> id = null;
                case "name" -> name = null;
                case "handle" -> handle = null;
                case "logo_url" -> logoUrl = null;
                case "website" -> website = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of membership.
     */
    public static final class Membership extends Model {
        private String id;
        private String organizationId;
        private String displayName;
        private String handle;
        private String role;
        /**
         * Creates an object with all properties unset.
         */
        public Membership() {}
        /**
         * Returns id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("id") public String getId() { return id; }
        /**
         * Sets id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("id") public void setId(String value) { id = value; markField("id"); }
        /**
         * Returns organization id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("organization_id") public String getOrganizationId() { return organizationId; }
        /**
         * Sets organization id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("organization_id") public void setOrganizationId(String value) { organizationId = value; markField("organization_id"); }
        /**
         * Returns display name.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("display_name") public String getDisplayName() { return displayName; }
        /**
         * Sets display name.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("display_name") public void setDisplayName(String value) { displayName = value; markField("display_name"); }
        /**
         * Returns handle.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("handle") public String getHandle() { return handle; }
        /**
         * Sets handle.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("handle") public void setHandle(String value) { handle = value; markField("handle"); }
        /**
         * Returns role.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("role") public String getRole() { return role; }
        /**
         * Sets role.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("role") public void setRole(String value) { role = value; markField("role"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("id")) fields.put("id", id);
            if (hasField("organization_id")) fields.put("organization_id", organizationId);
            if (hasField("display_name")) fields.put("display_name", displayName);
            if (hasField("handle")) fields.put("handle", handle);
            if (hasField("role")) fields.put("role", role);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "id" -> { setId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "organization_id" -> { setOrganizationId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "display_name" -> { setDisplayName(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "handle" -> { setHandle(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "role" -> { setRole(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "id" -> id = null;
                case "organization_id" -> organizationId = null;
                case "display_name" -> displayName = null;
                case "handle" -> handle = null;
                case "role" -> role = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of membership page.
     */
    public static final class MembershipPage extends Model {
        private List<Membership> data;
        private String nextCursor;
        /**
         * Creates an object with all properties unset.
         */
        public MembershipPage() {}
        /**
         * Returns data.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("data") public List<Membership> getData() { return data; }
        /**
         * Sets data.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("data") public void setData(List<Membership> value) { data = value; markField("data"); }
        /**
         * Returns next cursor.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("next_cursor") public String getNextCursor() { return nextCursor; }
        /**
         * Sets next cursor.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("next_cursor") public void setNextCursor(String value) { nextCursor = value; markField("next_cursor"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("data")) fields.put("data", data);
            if (hasField("next_cursor")) fields.put("next_cursor", nextCursor);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "data" -> { setData(Json.MAPPER.convertValue(value, new TypeReference<List<Membership>>() {})); yield true; }
                case "next_cursor" -> { setNextCursor(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "data" -> data = null;
                case "next_cursor" -> nextCursor = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of membership patch.
     */
    public static final class MembershipPatch extends Model {
        private String role;
        /**
         * Creates an object with all properties unset.
         */
        public MembershipPatch() {}
        /**
         * Returns role.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("role") public String getRole() { return role; }
        /**
         * Sets role.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("role") public void setRole(String value) { role = value; markField("role"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("role")) fields.put("role", role);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "role" -> { setRole(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "role" -> role = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live metadata patch.
     */
    public static final class LiveMetadataPatch extends Model {
        private String name;
        /**
         * Creates an object with all properties unset.
         */
        public LiveMetadataPatch() {}
        /**
         * Nonempty UTF-8 name, at most 128 bytes.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("name") public String getName() { return name; }
        /**
         * Sets name.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("name") public void setName(String value) { name = value; markField("name"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("name")) fields.put("name", name);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "name" -> { setName(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "name" -> name = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live workspace put.
     */
    public static final class LiveWorkspacePut extends Model {
        private String organizationId;
        /**
         * Creates an object with all properties unset.
         */
        public LiveWorkspacePut() {}
        /**
         * Immutable destination organization UUID. Personal workspaces and handles are not accepted.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("organization_id") public String getOrganizationId() { return organizationId; }
        /**
         * Sets organization id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("organization_id") public void setOrganizationId(String value) { organizationId = value; markField("organization_id"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("organization_id")) fields.put("organization_id", organizationId);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "organization_id" -> { setOrganizationId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "organization_id" -> organizationId = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live queue item.
     */
    public static final class LiveQueueItem extends Model {
        private String id;
        private Long position;
        private DisplayVerse verse;
        /**
         * Creates an object with all properties unset.
         */
        public LiveQueueItem() {}
        /**
         * Opaque stable queue item ID. Preserved through reordering, metadata edits and restart; removing/recreating a verse yields a new ID.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("id") public String getId() { return id; }
        /**
         * Sets id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("id") public void setId(String value) { id = value; markField("id"); }
        /**
         * Zero-based position in the entire current queue, not the current page.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("position") public Long getPosition() { return position; }
        /**
         * Sets position.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("position") public void setPosition(Long value) { position = value; markField("position"); }
        /**
         * Returns verse.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("verse") public DisplayVerse getVerse() { return verse; }
        /**
         * Sets verse.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("verse") public void setVerse(DisplayVerse value) { verse = value; markField("verse"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("id")) fields.put("id", id);
            if (hasField("position")) fields.put("position", position);
            if (hasField("verse")) fields.put("verse", verse);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "id" -> { setId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "position" -> { setPosition(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "verse" -> { setVerse(Json.MAPPER.convertValue(value, new TypeReference<DisplayVerse>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "id" -> id = null;
                case "position" -> position = null;
                case "verse" -> verse = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live queue page.
     */
    public static final class LiveQueuePage extends Model {
        private String liveId;
        private String organizationId;
        private Long sequence;
        private List<LiveQueueItem> data;
        private String nextCursor;
        /**
         * Creates an object with all properties unset.
         */
        public LiveQueuePage() {}
        /**
         * Returns live id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("live_id") public String getLiveId() { return liveId; }
        /**
         * Sets live id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("live_id") public void setLiveId(String value) { liveId = value; markField("live_id"); }
        /**
         * Returns organization id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("organization_id") public String getOrganizationId() { return organizationId; }
        /**
         * Sets organization id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("organization_id") public void setOrganizationId(String value) { organizationId = value; markField("organization_id"); }
        /**
         * Returns sequence.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("sequence") public Long getSequence() { return sequence; }
        /**
         * Sets sequence.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("sequence") public void setSequence(Long value) { sequence = value; markField("sequence"); }
        /**
         * Returns data.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("data") public List<LiveQueueItem> getData() { return data; }
        /**
         * Sets data.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("data") public void setData(List<LiveQueueItem> value) { data = value; markField("data"); }
        /**
         * Opaque sequence-bound cursor. Pass unchanged as after. Any intervening Live sequence change returns 409; malformed/unknown cursor returns 422.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("next_cursor") public String getNextCursor() { return nextCursor; }
        /**
         * Sets next cursor.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("next_cursor") public void setNextCursor(String value) { nextCursor = value; markField("next_cursor"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("live_id")) fields.put("live_id", liveId);
            if (hasField("organization_id")) fields.put("organization_id", organizationId);
            if (hasField("sequence")) fields.put("sequence", sequence);
            if (hasField("data")) fields.put("data", data);
            if (hasField("next_cursor")) fields.put("next_cursor", nextCursor);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "live_id" -> { setLiveId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "organization_id" -> { setOrganizationId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "sequence" -> { setSequence(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "data" -> { setData(Json.MAPPER.convertValue(value, new TypeReference<List<LiveQueueItem>>() {})); yield true; }
                case "next_cursor" -> { setNextCursor(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "live_id" -> liveId = null;
                case "organization_id" -> organizationId = null;
                case "sequence" -> sequence = null;
                case "data" -> data = null;
                case "next_cursor" -> nextCursor = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of create live queue item.
     */
    public static final class CreateLiveQueueItem extends Model {
        private CreateLiveQueueItemReference reference;
        private String beforeItemId;
        /**
         * Creates an object with all properties unset.
         */
        public CreateLiveQueueItem() {}
        /**
         * Returns reference.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("reference") public CreateLiveQueueItemReference getReference() { return reference; }
        /**
         * Sets reference.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("reference") public void setReference(CreateLiveQueueItemReference value) { reference = value; markField("reference"); }
        /**
         * Insert before this item ID. Omit or use null to append. Unknown anchor returns 404.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("before_item_id") public String getBeforeItemId() { return beforeItemId; }
        /**
         * Sets before item id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("before_item_id") public void setBeforeItemId(String value) { beforeItemId = value; markField("before_item_id"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("reference")) fields.put("reference", reference);
            if (hasField("before_item_id")) fields.put("before_item_id", beforeItemId);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "reference" -> { setReference(Json.MAPPER.convertValue(value, new TypeReference<CreateLiveQueueItemReference>() {})); yield true; }
                case "before_item_id" -> { setBeforeItemId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "reference" -> reference = null;
                case "before_item_id" -> beforeItemId = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live queue order.
     */
    public static final class LiveQueueOrder extends Model {
        private List<String> itemIds;
        /**
         * Creates an object with all properties unset.
         */
        public LiveQueueOrder() {}
        /**
         * Complete permutation of the current item IDs. Missing, extra, foreign or duplicate IDs return 422. Empty is valid only for an empty queue.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("item_ids") public List<String> getItemIds() { return itemIds; }
        /**
         * Sets item ids.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("item_ids") public void setItemIds(List<String> value) { itemIds = value; markField("item_ids"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("item_ids")) fields.put("item_ids", itemIds);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "item_ids" -> { setItemIds(Json.MAPPER.convertValue(value, new TypeReference<List<String>>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "item_ids" -> itemIds = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live studio settings.
     */
    public static final class LiveStudioSettings extends Model {
        private Long version;
        private Boolean showLogo;
        private String preset;
        private String transition;
        private LiveStudioSettingsControls controls;
        private LiveStudioSettingsPalettes palettes;
        private LiveStudioSettingsContainer container;
        private LiveStudioSettingsElements elements;
        private List<String> order;
        /**
         * Creates an object with all properties unset.
         */
        public LiveStudioSettings() {}
        /**
         * Returns version.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("version") public Long getVersion() { return version; }
        /**
         * Sets version.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("version") public void setVersion(Long value) { version = value; markField("version"); }
        /**
         * Returns show logo.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("showLogo") public Boolean getShowLogo() { return showLogo; }
        /**
         * Sets show logo.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("showLogo") public void setShowLogo(Boolean value) { showLogo = value; markField("showLogo"); }
        /**
         * Returns preset.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("preset") public String getPreset() { return preset; }
        /**
         * Sets preset.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("preset") public void setPreset(String value) { preset = value; markField("preset"); }
        /**
         * Returns transition.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("transition") public String getTransition() { return transition; }
        /**
         * Sets transition.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("transition") public void setTransition(String value) { transition = value; markField("transition"); }
        /**
         * Returns controls.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("controls") public LiveStudioSettingsControls getControls() { return controls; }
        /**
         * Sets controls.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("controls") public void setControls(LiveStudioSettingsControls value) { controls = value; markField("controls"); }
        /**
         * Returns palettes.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("palettes") public LiveStudioSettingsPalettes getPalettes() { return palettes; }
        /**
         * Sets palettes.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("palettes") public void setPalettes(LiveStudioSettingsPalettes value) { palettes = value; markField("palettes"); }
        /**
         * Returns container.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("container") public LiveStudioSettingsContainer getContainer() { return container; }
        /**
         * Sets container.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("container") public void setContainer(LiveStudioSettingsContainer value) { container = value; markField("container"); }
        /**
         * Returns elements.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("elements") public LiveStudioSettingsElements getElements() { return elements; }
        /**
         * Sets elements.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("elements") public void setElements(LiveStudioSettingsElements value) { elements = value; markField("elements"); }
        /**
         * Returns order.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("order") public List<String> getOrder() { return order; }
        /**
         * Sets order.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("order") public void setOrder(List<String> value) { order = value; markField("order"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("version")) fields.put("version", version);
            if (hasField("showLogo")) fields.put("showLogo", showLogo);
            if (hasField("preset")) fields.put("preset", preset);
            if (hasField("transition")) fields.put("transition", transition);
            if (hasField("controls")) fields.put("controls", controls);
            if (hasField("palettes")) fields.put("palettes", palettes);
            if (hasField("container")) fields.put("container", container);
            if (hasField("elements")) fields.put("elements", elements);
            if (hasField("order")) fields.put("order", order);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "version" -> { setVersion(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "showLogo" -> { setShowLogo(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "preset" -> { setPreset(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "transition" -> { setTransition(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "controls" -> { setControls(Json.MAPPER.convertValue(value, new TypeReference<LiveStudioSettingsControls>() {})); yield true; }
                case "palettes" -> { setPalettes(Json.MAPPER.convertValue(value, new TypeReference<LiveStudioSettingsPalettes>() {})); yield true; }
                case "container" -> { setContainer(Json.MAPPER.convertValue(value, new TypeReference<LiveStudioSettingsContainer>() {})); yield true; }
                case "elements" -> { setElements(Json.MAPPER.convertValue(value, new TypeReference<LiveStudioSettingsElements>() {})); yield true; }
                case "order" -> { setOrder(Json.MAPPER.convertValue(value, new TypeReference<List<String>>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "version" -> version = null;
                case "showLogo" -> showLogo = null;
                case "preset" -> preset = null;
                case "transition" -> transition = null;
                case "controls" -> controls = null;
                case "palettes" -> palettes = null;
                case "container" -> container = null;
                case "elements" -> elements = null;
                case "order" -> order = null;
                default -> { }
            }
        }
    }
    /**
     * Full settings replacement using the existing version-1 Studio model. Only preset and transition may be omitted; they default to default and none. Every other field and all nested keys are required. Unknown fields are rejected at every level.
     */
    public static final class LiveStudioSettingsInput extends Model {
        private Long version;
        private Boolean showLogo;
        private String preset;
        private String transition;
        private LiveStudioSettingsInputControls controls;
        private LiveStudioSettingsInputPalettes palettes;
        private LiveStudioSettingsInputContainer container;
        private LiveStudioSettingsInputElements elements;
        private List<String> order;
        /**
         * Creates an object with all properties unset.
         */
        public LiveStudioSettingsInput() {}
        /**
         * Returns version.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("version") public Long getVersion() { return version; }
        /**
         * Sets version.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("version") public void setVersion(Long value) { version = value; markField("version"); }
        /**
         * Returns show logo.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("showLogo") public Boolean getShowLogo() { return showLogo; }
        /**
         * Sets show logo.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("showLogo") public void setShowLogo(Boolean value) { showLogo = value; markField("showLogo"); }
        /**
         * Returns preset.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("preset") public String getPreset() { return preset; }
        /**
         * Sets preset.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("preset") public void setPreset(String value) { preset = value; markField("preset"); }
        /**
         * Returns transition.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("transition") public String getTransition() { return transition; }
        /**
         * Sets transition.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("transition") public void setTransition(String value) { transition = value; markField("transition"); }
        /**
         * Returns controls.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("controls") public LiveStudioSettingsInputControls getControls() { return controls; }
        /**
         * Sets controls.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("controls") public void setControls(LiveStudioSettingsInputControls value) { controls = value; markField("controls"); }
        /**
         * Returns palettes.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("palettes") public LiveStudioSettingsInputPalettes getPalettes() { return palettes; }
        /**
         * Sets palettes.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("palettes") public void setPalettes(LiveStudioSettingsInputPalettes value) { palettes = value; markField("palettes"); }
        /**
         * Returns container.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("container") public LiveStudioSettingsInputContainer getContainer() { return container; }
        /**
         * Sets container.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("container") public void setContainer(LiveStudioSettingsInputContainer value) { container = value; markField("container"); }
        /**
         * Returns elements.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("elements") public LiveStudioSettingsInputElements getElements() { return elements; }
        /**
         * Sets elements.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("elements") public void setElements(LiveStudioSettingsInputElements value) { elements = value; markField("elements"); }
        /**
         * Returns order.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("order") public List<String> getOrder() { return order; }
        /**
         * Sets order.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("order") public void setOrder(List<String> value) { order = value; markField("order"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("version")) fields.put("version", version);
            if (hasField("showLogo")) fields.put("showLogo", showLogo);
            if (hasField("preset")) fields.put("preset", preset);
            if (hasField("transition")) fields.put("transition", transition);
            if (hasField("controls")) fields.put("controls", controls);
            if (hasField("palettes")) fields.put("palettes", palettes);
            if (hasField("container")) fields.put("container", container);
            if (hasField("elements")) fields.put("elements", elements);
            if (hasField("order")) fields.put("order", order);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "version" -> { setVersion(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "showLogo" -> { setShowLogo(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "preset" -> { setPreset(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "transition" -> { setTransition(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "controls" -> { setControls(Json.MAPPER.convertValue(value, new TypeReference<LiveStudioSettingsInputControls>() {})); yield true; }
                case "palettes" -> { setPalettes(Json.MAPPER.convertValue(value, new TypeReference<LiveStudioSettingsInputPalettes>() {})); yield true; }
                case "container" -> { setContainer(Json.MAPPER.convertValue(value, new TypeReference<LiveStudioSettingsInputContainer>() {})); yield true; }
                case "elements" -> { setElements(Json.MAPPER.convertValue(value, new TypeReference<LiveStudioSettingsInputElements>() {})); yield true; }
                case "order" -> { setOrder(Json.MAPPER.convertValue(value, new TypeReference<List<String>>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "version" -> version = null;
                case "showLogo" -> showLogo = null;
                case "preset" -> preset = null;
                case "transition" -> transition = null;
                case "controls" -> controls = null;
                case "palettes" -> palettes = null;
                case "container" -> container = null;
                case "elements" -> elements = null;
                case "order" -> order = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of organization live studio.
     */
    public static final class OrganizationLiveStudio extends Model {
        private String organizationId;
        private Long revision;
        private LiveStudioSettings settings;
        /**
         * Creates an object with all properties unset.
         */
        public OrganizationLiveStudio() {}
        /**
         * Returns organization id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("organization_id") public String getOrganizationId() { return organizationId; }
        /**
         * Sets organization id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("organization_id") public void setOrganizationId(String value) { organizationId = value; markField("organization_id"); }
        /**
         * Settings revision independent of any Live sequence. Zero represents unsaved defaults; every accepted PUT increments the revision, even for identical settings.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("revision") public Long getRevision() { return revision; }
        /**
         * Sets revision.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("revision") public void setRevision(Long value) { revision = value; markField("revision"); }
        /**
         * Returns settings.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("settings") public LiveStudioSettings getSettings() { return settings; }
        /**
         * Sets settings.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("settings") public void setSettings(LiveStudioSettings value) { settings = value; markField("settings"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("organization_id")) fields.put("organization_id", organizationId);
            if (hasField("revision")) fields.put("revision", revision);
            if (hasField("settings")) fields.put("settings", settings);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "organization_id" -> { setOrganizationId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "revision" -> { setRevision(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "settings" -> { setSettings(Json.MAPPER.convertValue(value, new TypeReference<LiveStudioSettings>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "organization_id" -> organizationId = null;
                case "revision" -> revision = null;
                case "settings" -> settings = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of put organization live studio.
     */
    public static final class PutOrganizationLiveStudio extends Model {
        private LiveStudioSettingsInput settings;
        /**
         * Creates an object with all properties unset.
         */
        public PutOrganizationLiveStudio() {}
        /**
         * Returns settings.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("settings") public LiveStudioSettingsInput getSettings() { return settings; }
        /**
         * Sets settings.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("settings") public void setSettings(LiveStudioSettingsInput value) { settings = value; markField("settings"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("settings")) fields.put("settings", settings);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "settings" -> { setSettings(Json.MAPPER.convertValue(value, new TypeReference<LiveStudioSettingsInput>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "settings" -> settings = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of presentation element style.
     */
    public static final class PresentationElementStyle extends Model {
        private BigDecimal fontSize;
        private String fontFamily;
        private String color;
        private Boolean bold;
        private Boolean italic;
        private String align;
        private BigDecimal lineHeight;
        private String fill;
        private String stroke;
        private BigDecimal strokeWidth;
        private String strokeStyle;
        private BigDecimal opacity;
        /**
         * Creates an object with all properties unset.
         */
        public PresentationElementStyle() {}
        /**
         * Returns font size.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("font_size") public BigDecimal getFontSize() { return fontSize; }
        /**
         * Sets font size.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("font_size") public void setFontSize(BigDecimal value) { fontSize = value; markField("font_size"); }
        /**
         * Returns font family.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("font_family") public String getFontFamily() { return fontFamily; }
        /**
         * Sets font family.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("font_family") public void setFontFamily(String value) { fontFamily = value; markField("font_family"); }
        /**
         * Returns color.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("color") public String getColor() { return color; }
        /**
         * Sets color.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("color") public void setColor(String value) { color = value; markField("color"); }
        /**
         * Returns bold.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("bold") public Boolean getBold() { return bold; }
        /**
         * Sets bold.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("bold") public void setBold(Boolean value) { bold = value; markField("bold"); }
        /**
         * Returns italic.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("italic") public Boolean getItalic() { return italic; }
        /**
         * Sets italic.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("italic") public void setItalic(Boolean value) { italic = value; markField("italic"); }
        /**
         * Returns align.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("align") public String getAlign() { return align; }
        /**
         * Sets align.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("align") public void setAlign(String value) { align = value; markField("align"); }
        /**
         * Returns line height.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("line_height") public BigDecimal getLineHeight() { return lineHeight; }
        /**
         * Sets line height.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("line_height") public void setLineHeight(BigDecimal value) { lineHeight = value; markField("line_height"); }
        /**
         * Returns fill.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("fill") public String getFill() { return fill; }
        /**
         * Sets fill.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("fill") public void setFill(String value) { fill = value; markField("fill"); }
        /**
         * Returns stroke.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("stroke") public String getStroke() { return stroke; }
        /**
         * Sets stroke.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("stroke") public void setStroke(String value) { stroke = value; markField("stroke"); }
        /**
         * Returns stroke width.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("stroke_width") public BigDecimal getStrokeWidth() { return strokeWidth; }
        /**
         * Sets stroke width.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("stroke_width") public void setStrokeWidth(BigDecimal value) { strokeWidth = value; markField("stroke_width"); }
        /**
         * Returns stroke style.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("stroke_style") public String getStrokeStyle() { return strokeStyle; }
        /**
         * Sets stroke style.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("stroke_style") public void setStrokeStyle(String value) { strokeStyle = value; markField("stroke_style"); }
        /**
         * Returns opacity.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("opacity") public BigDecimal getOpacity() { return opacity; }
        /**
         * Sets opacity.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("opacity") public void setOpacity(BigDecimal value) { opacity = value; markField("opacity"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("font_size")) fields.put("font_size", fontSize);
            if (hasField("font_family")) fields.put("font_family", fontFamily);
            if (hasField("color")) fields.put("color", color);
            if (hasField("bold")) fields.put("bold", bold);
            if (hasField("italic")) fields.put("italic", italic);
            if (hasField("align")) fields.put("align", align);
            if (hasField("line_height")) fields.put("line_height", lineHeight);
            if (hasField("fill")) fields.put("fill", fill);
            if (hasField("stroke")) fields.put("stroke", stroke);
            if (hasField("stroke_width")) fields.put("stroke_width", strokeWidth);
            if (hasField("stroke_style")) fields.put("stroke_style", strokeStyle);
            if (hasField("opacity")) fields.put("opacity", opacity);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "font_size" -> { setFontSize(Json.MAPPER.convertValue(value, new TypeReference<BigDecimal>() {})); yield true; }
                case "font_family" -> { setFontFamily(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "color" -> { setColor(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "bold" -> { setBold(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "italic" -> { setItalic(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "align" -> { setAlign(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "line_height" -> { setLineHeight(Json.MAPPER.convertValue(value, new TypeReference<BigDecimal>() {})); yield true; }
                case "fill" -> { setFill(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "stroke" -> { setStroke(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "stroke_width" -> { setStrokeWidth(Json.MAPPER.convertValue(value, new TypeReference<BigDecimal>() {})); yield true; }
                case "stroke_style" -> { setStrokeStyle(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "opacity" -> { setOpacity(Json.MAPPER.convertValue(value, new TypeReference<BigDecimal>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "font_size" -> fontSize = null;
                case "font_family" -> fontFamily = null;
                case "color" -> color = null;
                case "bold" -> bold = null;
                case "italic" -> italic = null;
                case "align" -> align = null;
                case "line_height" -> lineHeight = null;
                case "fill" -> fill = null;
                case "stroke" -> stroke = null;
                case "stroke_width" -> strokeWidth = null;
                case "stroke_style" -> strokeStyle = null;
                case "opacity" -> opacity = null;
                default -> { }
            }
        }
    }
    /**
     * Coordinates and dimensions are integer canvas units; x+width must be &lt;=1280 and y+height &lt;=720 (checked by the domain validator). IDs must be unique within a slide.
     */
    public static final class PresentationTextElement extends Model {
        private String id;
        private String type;
        private Long x;
        private Long y;
        private Long width;
        private Long height;
        private PresentationElementStyle style;
        private Long rotation;
        private String text;
        /**
         * Creates an object with all properties unset.
         */
        public PresentationTextElement() {}
        /**
         * Returns id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("id") public String getId() { return id; }
        /**
         * Sets id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("id") public void setId(String value) { id = value; markField("id"); }
        /**
         * Returns type.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("type") public String getType() { return type; }
        /**
         * Sets type.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("type") public void setType(String value) { type = value; markField("type"); }
        /**
         * Returns x.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("x") public Long getX() { return x; }
        /**
         * Sets x.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("x") public void setX(Long value) { x = value; markField("x"); }
        /**
         * Returns y.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("y") public Long getY() { return y; }
        /**
         * Sets y.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("y") public void setY(Long value) { y = value; markField("y"); }
        /**
         * Returns width.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("width") public Long getWidth() { return width; }
        /**
         * Sets width.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("width") public void setWidth(Long value) { width = value; markField("width"); }
        /**
         * Returns height.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("height") public Long getHeight() { return height; }
        /**
         * Sets height.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("height") public void setHeight(Long value) { height = value; markField("height"); }
        /**
         * Returns style.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("style") public PresentationElementStyle getStyle() { return style; }
        /**
         * Sets style.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("style") public void setStyle(PresentationElementStyle value) { style = value; markField("style"); }
        /**
         * Returns rotation.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("rotation") public Long getRotation() { return rotation; }
        /**
         * Sets rotation.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("rotation") public void setRotation(Long value) { rotation = value; markField("rotation"); }
        /**
         * UTF-8 text, at most 20000 bytes; NUL is rejected.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("text") public String getText() { return text; }
        /**
         * Sets text.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("text") public void setText(String value) { text = value; markField("text"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("id")) fields.put("id", id);
            if (hasField("type")) fields.put("type", type);
            if (hasField("x")) fields.put("x", x);
            if (hasField("y")) fields.put("y", y);
            if (hasField("width")) fields.put("width", width);
            if (hasField("height")) fields.put("height", height);
            if (hasField("style")) fields.put("style", style);
            if (hasField("rotation")) fields.put("rotation", rotation);
            if (hasField("text")) fields.put("text", text);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "id" -> { setId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "type" -> { setType(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "x" -> { setX(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "y" -> { setY(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "width" -> { setWidth(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "height" -> { setHeight(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "style" -> { setStyle(Json.MAPPER.convertValue(value, new TypeReference<PresentationElementStyle>() {})); yield true; }
                case "rotation" -> { setRotation(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "text" -> { setText(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "id" -> id = null;
                case "type" -> type = null;
                case "x" -> x = null;
                case "y" -> y = null;
                case "width" -> width = null;
                case "height" -> height = null;
                case "style" -> style = null;
                case "rotation" -> rotation = null;
                case "text" -> text = null;
                default -> { }
            }
        }
    }
    /**
     * Coordinates and dimensions are integer canvas units; x+width must be &lt;=1280 and y+height &lt;=720 (checked by the domain validator). IDs must be unique within a slide. Known open-license edition/attribution fields are restored from the reviewed catalogue on saves and reads.
     */
    public static final class PresentationVerseElement extends Model {
        private String id;
        private String type;
        private Long x;
        private Long y;
        private Long width;
        private Long height;
        private PresentationElementStyle style;
        private Long rotation;
        private String text;
        private String translation;
        private String edition;
        private String reference;
        private String attribution;
        /**
         * Creates an object with all properties unset.
         */
        public PresentationVerseElement() {}
        /**
         * Returns id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("id") public String getId() { return id; }
        /**
         * Sets id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("id") public void setId(String value) { id = value; markField("id"); }
        /**
         * Returns type.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("type") public String getType() { return type; }
        /**
         * Sets type.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("type") public void setType(String value) { type = value; markField("type"); }
        /**
         * Returns x.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("x") public Long getX() { return x; }
        /**
         * Sets x.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("x") public void setX(Long value) { x = value; markField("x"); }
        /**
         * Returns y.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("y") public Long getY() { return y; }
        /**
         * Sets y.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("y") public void setY(Long value) { y = value; markField("y"); }
        /**
         * Returns width.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("width") public Long getWidth() { return width; }
        /**
         * Sets width.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("width") public void setWidth(Long value) { width = value; markField("width"); }
        /**
         * Returns height.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("height") public Long getHeight() { return height; }
        /**
         * Sets height.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("height") public void setHeight(Long value) { height = value; markField("height"); }
        /**
         * Returns style.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("style") public PresentationElementStyle getStyle() { return style; }
        /**
         * Sets style.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("style") public void setStyle(PresentationElementStyle value) { style = value; markField("style"); }
        /**
         * Returns rotation.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("rotation") public Long getRotation() { return rotation; }
        /**
         * Sets rotation.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("rotation") public void setRotation(Long value) { rotation = value; markField("rotation"); }
        /**
         * UTF-8 text, at most 20000 bytes; NUL is rejected.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("text") public String getText() { return text; }
        /**
         * Sets text.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("text") public void setText(String value) { text = value; markField("text"); }
        /**
         * UTF-8 text, at most 200 bytes; NUL is rejected.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("translation") public String getTranslation() { return translation; }
        /**
         * Sets translation.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("translation") public void setTranslation(String value) { translation = value; markField("translation"); }
        /**
         * UTF-8 text, at most 2000 bytes; NUL is rejected.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("edition") public String getEdition() { return edition; }
        /**
         * Sets edition.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("edition") public void setEdition(String value) { edition = value; markField("edition"); }
        /**
         * UTF-8 text, at most 200 bytes; NUL is rejected.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("reference") public String getReference() { return reference; }
        /**
         * Sets reference.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("reference") public void setReference(String value) { reference = value; markField("reference"); }
        /**
         * UTF-8 text, at most 2000 bytes; NUL is rejected.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("attribution") public String getAttribution() { return attribution; }
        /**
         * Sets attribution.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("attribution") public void setAttribution(String value) { attribution = value; markField("attribution"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("id")) fields.put("id", id);
            if (hasField("type")) fields.put("type", type);
            if (hasField("x")) fields.put("x", x);
            if (hasField("y")) fields.put("y", y);
            if (hasField("width")) fields.put("width", width);
            if (hasField("height")) fields.put("height", height);
            if (hasField("style")) fields.put("style", style);
            if (hasField("rotation")) fields.put("rotation", rotation);
            if (hasField("text")) fields.put("text", text);
            if (hasField("translation")) fields.put("translation", translation);
            if (hasField("edition")) fields.put("edition", edition);
            if (hasField("reference")) fields.put("reference", reference);
            if (hasField("attribution")) fields.put("attribution", attribution);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "id" -> { setId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "type" -> { setType(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "x" -> { setX(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "y" -> { setY(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "width" -> { setWidth(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "height" -> { setHeight(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "style" -> { setStyle(Json.MAPPER.convertValue(value, new TypeReference<PresentationElementStyle>() {})); yield true; }
                case "rotation" -> { setRotation(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "text" -> { setText(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "translation" -> { setTranslation(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "edition" -> { setEdition(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "reference" -> { setReference(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "attribution" -> { setAttribution(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "id" -> id = null;
                case "type" -> type = null;
                case "x" -> x = null;
                case "y" -> y = null;
                case "width" -> width = null;
                case "height" -> height = null;
                case "style" -> style = null;
                case "rotation" -> rotation = null;
                case "text" -> text = null;
                case "translation" -> translation = null;
                case "edition" -> edition = null;
                case "reference" -> reference = null;
                case "attribution" -> attribution = null;
                default -> { }
            }
        }
    }
    /**
     * Coordinates and dimensions are integer canvas units; x+width must be &lt;=1280 and y+height &lt;=720 (checked by the domain validator). IDs must be unique within a slide.
     */
    public static final class PresentationShapeElement extends Model {
        private String id;
        private String type;
        private Long x;
        private Long y;
        private Long width;
        private Long height;
        private PresentationElementStyle style;
        private Long rotation;
        private String shape;
        /**
         * Creates an object with all properties unset.
         */
        public PresentationShapeElement() {}
        /**
         * Returns id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("id") public String getId() { return id; }
        /**
         * Sets id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("id") public void setId(String value) { id = value; markField("id"); }
        /**
         * Returns type.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("type") public String getType() { return type; }
        /**
         * Sets type.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("type") public void setType(String value) { type = value; markField("type"); }
        /**
         * Returns x.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("x") public Long getX() { return x; }
        /**
         * Sets x.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("x") public void setX(Long value) { x = value; markField("x"); }
        /**
         * Returns y.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("y") public Long getY() { return y; }
        /**
         * Sets y.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("y") public void setY(Long value) { y = value; markField("y"); }
        /**
         * Returns width.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("width") public Long getWidth() { return width; }
        /**
         * Sets width.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("width") public void setWidth(Long value) { width = value; markField("width"); }
        /**
         * Returns height.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("height") public Long getHeight() { return height; }
        /**
         * Sets height.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("height") public void setHeight(Long value) { height = value; markField("height"); }
        /**
         * Returns style.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("style") public PresentationElementStyle getStyle() { return style; }
        /**
         * Sets style.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("style") public void setStyle(PresentationElementStyle value) { style = value; markField("style"); }
        /**
         * Returns rotation.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("rotation") public Long getRotation() { return rotation; }
        /**
         * Sets rotation.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("rotation") public void setRotation(Long value) { rotation = value; markField("rotation"); }
        /**
         * Returns shape.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("shape") public String getShape() { return shape; }
        /**
         * Sets shape.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("shape") public void setShape(String value) { shape = value; markField("shape"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("id")) fields.put("id", id);
            if (hasField("type")) fields.put("type", type);
            if (hasField("x")) fields.put("x", x);
            if (hasField("y")) fields.put("y", y);
            if (hasField("width")) fields.put("width", width);
            if (hasField("height")) fields.put("height", height);
            if (hasField("style")) fields.put("style", style);
            if (hasField("rotation")) fields.put("rotation", rotation);
            if (hasField("shape")) fields.put("shape", shape);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "id" -> { setId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "type" -> { setType(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "x" -> { setX(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "y" -> { setY(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "width" -> { setWidth(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "height" -> { setHeight(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "style" -> { setStyle(Json.MAPPER.convertValue(value, new TypeReference<PresentationElementStyle>() {})); yield true; }
                case "rotation" -> { setRotation(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "shape" -> { setShape(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "id" -> id = null;
                case "type" -> type = null;
                case "x" -> x = null;
                case "y" -> y = null;
                case "width" -> width = null;
                case "height" -> height = null;
                case "style" -> style = null;
                case "rotation" -> rotation = null;
                case "shape" -> shape = null;
                default -> { }
            }
        }
    }
    /**
     * Coordinates and dimensions are integer canvas units; x+width must be &lt;=1280 and y+height &lt;=720 (checked by the domain validator). IDs must be unique within a slide.
     */
    public static final class PresentationImageElement extends Model {
        private String id;
        private String type;
        private Long x;
        private Long y;
        private Long width;
        private Long height;
        private PresentationElementStyle style;
        private Long rotation;
        private String asset;
        private String alt;
        private String fit;
        /**
         * Creates an object with all properties unset.
         */
        public PresentationImageElement() {}
        /**
         * Returns id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("id") public String getId() { return id; }
        /**
         * Sets id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("id") public void setId(String value) { id = value; markField("id"); }
        /**
         * Returns type.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("type") public String getType() { return type; }
        /**
         * Sets type.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("type") public void setType(String value) { type = value; markField("type"); }
        /**
         * Returns x.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("x") public Long getX() { return x; }
        /**
         * Sets x.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("x") public void setX(Long value) { x = value; markField("x"); }
        /**
         * Returns y.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("y") public Long getY() { return y; }
        /**
         * Sets y.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("y") public void setY(Long value) { y = value; markField("y"); }
        /**
         * Returns width.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("width") public Long getWidth() { return width; }
        /**
         * Sets width.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("width") public void setWidth(Long value) { width = value; markField("width"); }
        /**
         * Returns height.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("height") public Long getHeight() { return height; }
        /**
         * Sets height.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("height") public void setHeight(Long value) { height = value; markField("height"); }
        /**
         * Returns style.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("style") public PresentationElementStyle getStyle() { return style; }
        /**
         * Sets style.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("style") public void setStyle(PresentationElementStyle value) { style = value; markField("style"); }
        /**
         * Returns rotation.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("rotation") public Long getRotation() { return rotation; }
        /**
         * Sets rotation.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("rotation") public void setRotation(Long value) { rotation = value; markField("rotation"); }
        /**
         * Returns asset.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("asset") public String getAsset() { return asset; }
        /**
         * Sets asset.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("asset") public void setAsset(String value) { asset = value; markField("asset"); }
        /**
         * UTF-8 text, at most 1000 bytes; NUL is rejected.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("alt") public String getAlt() { return alt; }
        /**
         * Sets alt.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("alt") public void setAlt(String value) { alt = value; markField("alt"); }
        /**
         * Returns fit.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("fit") public String getFit() { return fit; }
        /**
         * Sets fit.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("fit") public void setFit(String value) { fit = value; markField("fit"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("id")) fields.put("id", id);
            if (hasField("type")) fields.put("type", type);
            if (hasField("x")) fields.put("x", x);
            if (hasField("y")) fields.put("y", y);
            if (hasField("width")) fields.put("width", width);
            if (hasField("height")) fields.put("height", height);
            if (hasField("style")) fields.put("style", style);
            if (hasField("rotation")) fields.put("rotation", rotation);
            if (hasField("asset")) fields.put("asset", asset);
            if (hasField("alt")) fields.put("alt", alt);
            if (hasField("fit")) fields.put("fit", fit);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "id" -> { setId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "type" -> { setType(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "x" -> { setX(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "y" -> { setY(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "width" -> { setWidth(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "height" -> { setHeight(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "style" -> { setStyle(Json.MAPPER.convertValue(value, new TypeReference<PresentationElementStyle>() {})); yield true; }
                case "rotation" -> { setRotation(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "asset" -> { setAsset(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "alt" -> { setAlt(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "fit" -> { setFit(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "id" -> id = null;
                case "type" -> type = null;
                case "x" -> x = null;
                case "y" -> y = null;
                case "width" -> width = null;
                case "height" -> height = null;
                case "style" -> style = null;
                case "rotation" -> rotation = null;
                case "asset" -> asset = null;
                case "alt" -> alt = null;
                case "fit" -> fit = null;
                default -> { }
            }
        }
    }
    /**
     * Coordinates and dimensions are integer canvas units; x+width must be &lt;=1280 and y+height &lt;=720 (checked by the domain validator). IDs must be unique within a slide.
     */
    public static final class PresentationVideoElement extends Model {
        private String id;
        private String type;
        private Long x;
        private Long y;
        private Long width;
        private Long height;
        private PresentationElementStyle style;
        private Long rotation;
        private String asset;
        private String fit;
        private String poster;
        private Boolean muted;
        private Boolean loop;
        /**
         * Creates an object with all properties unset.
         */
        public PresentationVideoElement() {}
        /**
         * Returns id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("id") public String getId() { return id; }
        /**
         * Sets id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("id") public void setId(String value) { id = value; markField("id"); }
        /**
         * Returns type.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("type") public String getType() { return type; }
        /**
         * Sets type.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("type") public void setType(String value) { type = value; markField("type"); }
        /**
         * Returns x.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("x") public Long getX() { return x; }
        /**
         * Sets x.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("x") public void setX(Long value) { x = value; markField("x"); }
        /**
         * Returns y.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("y") public Long getY() { return y; }
        /**
         * Sets y.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("y") public void setY(Long value) { y = value; markField("y"); }
        /**
         * Returns width.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("width") public Long getWidth() { return width; }
        /**
         * Sets width.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("width") public void setWidth(Long value) { width = value; markField("width"); }
        /**
         * Returns height.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("height") public Long getHeight() { return height; }
        /**
         * Sets height.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("height") public void setHeight(Long value) { height = value; markField("height"); }
        /**
         * Returns style.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("style") public PresentationElementStyle getStyle() { return style; }
        /**
         * Sets style.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("style") public void setStyle(PresentationElementStyle value) { style = value; markField("style"); }
        /**
         * Returns rotation.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("rotation") public Long getRotation() { return rotation; }
        /**
         * Sets rotation.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("rotation") public void setRotation(Long value) { rotation = value; markField("rotation"); }
        /**
         * Returns asset.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("asset") public String getAsset() { return asset; }
        /**
         * Sets asset.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("asset") public void setAsset(String value) { asset = value; markField("asset"); }
        /**
         * Returns fit.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("fit") public String getFit() { return fit; }
        /**
         * Sets fit.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("fit") public void setFit(String value) { fit = value; markField("fit"); }
        /**
         * Returns poster.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("poster") public String getPoster() { return poster; }
        /**
         * Sets poster.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("poster") public void setPoster(String value) { poster = value; markField("poster"); }
        /**
         * Returns muted.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("muted") public Boolean getMuted() { return muted; }
        /**
         * Sets muted.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("muted") public void setMuted(Boolean value) { muted = value; markField("muted"); }
        /**
         * Returns loop.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("loop") public Boolean getLoop() { return loop; }
        /**
         * Sets loop.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("loop") public void setLoop(Boolean value) { loop = value; markField("loop"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("id")) fields.put("id", id);
            if (hasField("type")) fields.put("type", type);
            if (hasField("x")) fields.put("x", x);
            if (hasField("y")) fields.put("y", y);
            if (hasField("width")) fields.put("width", width);
            if (hasField("height")) fields.put("height", height);
            if (hasField("style")) fields.put("style", style);
            if (hasField("rotation")) fields.put("rotation", rotation);
            if (hasField("asset")) fields.put("asset", asset);
            if (hasField("fit")) fields.put("fit", fit);
            if (hasField("poster")) fields.put("poster", poster);
            if (hasField("muted")) fields.put("muted", muted);
            if (hasField("loop")) fields.put("loop", loop);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "id" -> { setId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "type" -> { setType(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "x" -> { setX(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "y" -> { setY(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "width" -> { setWidth(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "height" -> { setHeight(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "style" -> { setStyle(Json.MAPPER.convertValue(value, new TypeReference<PresentationElementStyle>() {})); yield true; }
                case "rotation" -> { setRotation(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "asset" -> { setAsset(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "fit" -> { setFit(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "poster" -> { setPoster(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "muted" -> { setMuted(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "loop" -> { setLoop(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "id" -> id = null;
                case "type" -> type = null;
                case "x" -> x = null;
                case "y" -> y = null;
                case "width" -> width = null;
                case "height" -> height = null;
                case "style" -> style = null;
                case "rotation" -> rotation = null;
                case "asset" -> asset = null;
                case "fit" -> fit = null;
                case "poster" -> poster = null;
                case "muted" -> muted = null;
                case "loop" -> loop = null;
                default -> { }
            }
        }
    }
    /**
     * Coordinates and dimensions are integer canvas units; x+width must be &lt;=1280 and y+height &lt;=720 (checked by the domain validator). IDs must be unique within a slide.
     */
    public static final class PresentationYouTubeElement extends Model {
        private String id;
        private String type;
        private Long x;
        private Long y;
        private Long width;
        private Long height;
        private PresentationElementStyle style;
        private Long rotation;
        private String videoId;
        /**
         * Creates an object with all properties unset.
         */
        public PresentationYouTubeElement() {}
        /**
         * Returns id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("id") public String getId() { return id; }
        /**
         * Sets id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("id") public void setId(String value) { id = value; markField("id"); }
        /**
         * Returns type.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("type") public String getType() { return type; }
        /**
         * Sets type.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("type") public void setType(String value) { type = value; markField("type"); }
        /**
         * Returns x.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("x") public Long getX() { return x; }
        /**
         * Sets x.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("x") public void setX(Long value) { x = value; markField("x"); }
        /**
         * Returns y.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("y") public Long getY() { return y; }
        /**
         * Sets y.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("y") public void setY(Long value) { y = value; markField("y"); }
        /**
         * Returns width.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("width") public Long getWidth() { return width; }
        /**
         * Sets width.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("width") public void setWidth(Long value) { width = value; markField("width"); }
        /**
         * Returns height.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("height") public Long getHeight() { return height; }
        /**
         * Sets height.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("height") public void setHeight(Long value) { height = value; markField("height"); }
        /**
         * Returns style.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("style") public PresentationElementStyle getStyle() { return style; }
        /**
         * Sets style.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("style") public void setStyle(PresentationElementStyle value) { style = value; markField("style"); }
        /**
         * Returns rotation.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("rotation") public Long getRotation() { return rotation; }
        /**
         * Sets rotation.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("rotation") public void setRotation(Long value) { rotation = value; markField("rotation"); }
        /**
         * Returns video id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("video_id") public String getVideoId() { return videoId; }
        /**
         * Sets video id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("video_id") public void setVideoId(String value) { videoId = value; markField("video_id"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("id")) fields.put("id", id);
            if (hasField("type")) fields.put("type", type);
            if (hasField("x")) fields.put("x", x);
            if (hasField("y")) fields.put("y", y);
            if (hasField("width")) fields.put("width", width);
            if (hasField("height")) fields.put("height", height);
            if (hasField("style")) fields.put("style", style);
            if (hasField("rotation")) fields.put("rotation", rotation);
            if (hasField("video_id")) fields.put("video_id", videoId);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "id" -> { setId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "type" -> { setType(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "x" -> { setX(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "y" -> { setY(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "width" -> { setWidth(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "height" -> { setHeight(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "style" -> { setStyle(Json.MAPPER.convertValue(value, new TypeReference<PresentationElementStyle>() {})); yield true; }
                case "rotation" -> { setRotation(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "video_id" -> { setVideoId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "id" -> id = null;
                case "type" -> type = null;
                case "x" -> x = null;
                case "y" -> y = null;
                case "width" -> width = null;
                case "height" -> height = null;
                case "style" -> style = null;
                case "rotation" -> rotation = null;
                case "video_id" -> videoId = null;
                default -> { }
            }
        }
    }
    /**
     * Typed union object. Known variant fields share one wire object; the server validates variant-specific requirements.
     */
    public static final class PresentationElement extends Model {
        private String id;
        private String type;
        private Long x;
        private Long y;
        private Long width;
        private Long height;
        private PresentationElementStyle style;
        private Long rotation;
        private String text;
        private String translation;
        private String edition;
        private String reference;
        private String attribution;
        private String shape;
        private String asset;
        private String alt;
        private String fit;
        private String poster;
        private Boolean muted;
        private Boolean loop;
        private String videoId;
        /**
         * Creates an object with all properties unset.
         */
        public PresentationElement() {}
        /**
         * Returns id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("id") public String getId() { return id; }
        /**
         * Sets id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("id") public void setId(String value) { id = value; markField("id"); }
        /**
         * Returns type.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("type") public String getType() { return type; }
        /**
         * Sets type.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("type") public void setType(String value) { type = value; markField("type"); }
        /**
         * Returns x.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("x") public Long getX() { return x; }
        /**
         * Sets x.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("x") public void setX(Long value) { x = value; markField("x"); }
        /**
         * Returns y.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("y") public Long getY() { return y; }
        /**
         * Sets y.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("y") public void setY(Long value) { y = value; markField("y"); }
        /**
         * Returns width.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("width") public Long getWidth() { return width; }
        /**
         * Sets width.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("width") public void setWidth(Long value) { width = value; markField("width"); }
        /**
         * Returns height.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("height") public Long getHeight() { return height; }
        /**
         * Sets height.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("height") public void setHeight(Long value) { height = value; markField("height"); }
        /**
         * Returns style.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("style") public PresentationElementStyle getStyle() { return style; }
        /**
         * Sets style.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("style") public void setStyle(PresentationElementStyle value) { style = value; markField("style"); }
        /**
         * Returns rotation.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("rotation") public Long getRotation() { return rotation; }
        /**
         * Sets rotation.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("rotation") public void setRotation(Long value) { rotation = value; markField("rotation"); }
        /**
         * UTF-8 text, at most 20000 bytes; NUL is rejected.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("text") public String getText() { return text; }
        /**
         * Sets text.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("text") public void setText(String value) { text = value; markField("text"); }
        /**
         * UTF-8 text, at most 200 bytes; NUL is rejected.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("translation") public String getTranslation() { return translation; }
        /**
         * Sets translation.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("translation") public void setTranslation(String value) { translation = value; markField("translation"); }
        /**
         * UTF-8 text, at most 2000 bytes; NUL is rejected.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("edition") public String getEdition() { return edition; }
        /**
         * Sets edition.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("edition") public void setEdition(String value) { edition = value; markField("edition"); }
        /**
         * UTF-8 text, at most 200 bytes; NUL is rejected.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("reference") public String getReference() { return reference; }
        /**
         * Sets reference.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("reference") public void setReference(String value) { reference = value; markField("reference"); }
        /**
         * UTF-8 text, at most 2000 bytes; NUL is rejected.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("attribution") public String getAttribution() { return attribution; }
        /**
         * Sets attribution.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("attribution") public void setAttribution(String value) { attribution = value; markField("attribution"); }
        /**
         * Returns shape.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("shape") public String getShape() { return shape; }
        /**
         * Sets shape.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("shape") public void setShape(String value) { shape = value; markField("shape"); }
        /**
         * Returns asset.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("asset") public String getAsset() { return asset; }
        /**
         * Sets asset.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("asset") public void setAsset(String value) { asset = value; markField("asset"); }
        /**
         * UTF-8 text, at most 1000 bytes; NUL is rejected.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("alt") public String getAlt() { return alt; }
        /**
         * Sets alt.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("alt") public void setAlt(String value) { alt = value; markField("alt"); }
        /**
         * Returns fit.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("fit") public String getFit() { return fit; }
        /**
         * Sets fit.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("fit") public void setFit(String value) { fit = value; markField("fit"); }
        /**
         * Returns poster.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("poster") public String getPoster() { return poster; }
        /**
         * Sets poster.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("poster") public void setPoster(String value) { poster = value; markField("poster"); }
        /**
         * Returns muted.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("muted") public Boolean getMuted() { return muted; }
        /**
         * Sets muted.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("muted") public void setMuted(Boolean value) { muted = value; markField("muted"); }
        /**
         * Returns loop.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("loop") public Boolean getLoop() { return loop; }
        /**
         * Sets loop.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("loop") public void setLoop(Boolean value) { loop = value; markField("loop"); }
        /**
         * Returns video id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("video_id") public String getVideoId() { return videoId; }
        /**
         * Sets video id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("video_id") public void setVideoId(String value) { videoId = value; markField("video_id"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("id")) fields.put("id", id);
            if (hasField("type")) fields.put("type", type);
            if (hasField("x")) fields.put("x", x);
            if (hasField("y")) fields.put("y", y);
            if (hasField("width")) fields.put("width", width);
            if (hasField("height")) fields.put("height", height);
            if (hasField("style")) fields.put("style", style);
            if (hasField("rotation")) fields.put("rotation", rotation);
            if (hasField("text")) fields.put("text", text);
            if (hasField("translation")) fields.put("translation", translation);
            if (hasField("edition")) fields.put("edition", edition);
            if (hasField("reference")) fields.put("reference", reference);
            if (hasField("attribution")) fields.put("attribution", attribution);
            if (hasField("shape")) fields.put("shape", shape);
            if (hasField("asset")) fields.put("asset", asset);
            if (hasField("alt")) fields.put("alt", alt);
            if (hasField("fit")) fields.put("fit", fit);
            if (hasField("poster")) fields.put("poster", poster);
            if (hasField("muted")) fields.put("muted", muted);
            if (hasField("loop")) fields.put("loop", loop);
            if (hasField("video_id")) fields.put("video_id", videoId);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "id" -> { setId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "type" -> { setType(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "x" -> { setX(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "y" -> { setY(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "width" -> { setWidth(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "height" -> { setHeight(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "style" -> { setStyle(Json.MAPPER.convertValue(value, new TypeReference<PresentationElementStyle>() {})); yield true; }
                case "rotation" -> { setRotation(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "text" -> { setText(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "translation" -> { setTranslation(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "edition" -> { setEdition(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "reference" -> { setReference(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "attribution" -> { setAttribution(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "shape" -> { setShape(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "asset" -> { setAsset(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "alt" -> { setAlt(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "fit" -> { setFit(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "poster" -> { setPoster(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "muted" -> { setMuted(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "loop" -> { setLoop(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "video_id" -> { setVideoId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "id" -> id = null;
                case "type" -> type = null;
                case "x" -> x = null;
                case "y" -> y = null;
                case "width" -> width = null;
                case "height" -> height = null;
                case "style" -> style = null;
                case "rotation" -> rotation = null;
                case "text" -> text = null;
                case "translation" -> translation = null;
                case "edition" -> edition = null;
                case "reference" -> reference = null;
                case "attribution" -> attribution = null;
                case "shape" -> shape = null;
                case "asset" -> asset = null;
                case "alt" -> alt = null;
                case "fit" -> fit = null;
                case "poster" -> poster = null;
                case "muted" -> muted = null;
                case "loop" -> loop = null;
                case "video_id" -> videoId = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of presentation slide.
     */
    public static final class PresentationSlide extends Model {
        private String id;
        private List<PresentationElement> elements;
        private String background;
        /**
         * Creates an object with all properties unset.
         */
        public PresentationSlide() {}
        /**
         * Returns id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("id") public String getId() { return id; }
        /**
         * Sets id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("id") public void setId(String value) { id = value; markField("id"); }
        /**
         * Returns elements.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("elements") public List<PresentationElement> getElements() { return elements; }
        /**
         * Sets elements.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("elements") public void setElements(List<PresentationElement> value) { elements = value; markField("elements"); }
        /**
         * Returns background.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("background") public String getBackground() { return background; }
        /**
         * Sets background.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("background") public void setBackground(String value) { background = value; markField("background"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("id")) fields.put("id", id);
            if (hasField("elements")) fields.put("elements", elements);
            if (hasField("background")) fields.put("background", background);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "id" -> { setId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "elements" -> { setElements(Json.MAPPER.convertValue(value, new TypeReference<List<PresentationElement>>() {})); yield true; }
                case "background" -> { setBackground(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "id" -> id = null;
                case "elements" -> elements = null;
                case "background" -> background = null;
                default -> { }
            }
        }
    }
    /**
     * Version-1 document, at most 2,000,000 encoded JSON bytes. Slide IDs are unique across the deck, element IDs within each slide. Omitted slide background is preserved as omitted. Save retains caller slide/element IDs; no automatic publishing. Existing same-workspace ready media references are checked by the domain; upload and portable import endpoints provide ready media. Uploading alone never modifies the saved document.
     */
    public static final class PresentationDocument extends Model {
        private Long version;
        private String name;
        private List<PresentationSlide> slides;
        /**
         * Creates an object with all properties unset.
         */
        public PresentationDocument() {}
        /**
         * Returns version.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("version") public Long getVersion() { return version; }
        /**
         * Sets version.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("version") public void setVersion(Long value) { version = value; markField("version"); }
        /**
         * UTF-8 text, at most 200 bytes; NUL is rejected.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("name") public String getName() { return name; }
        /**
         * Sets name.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("name") public void setName(String value) { name = value; markField("name"); }
        /**
         * Returns slides.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("slides") public List<PresentationSlide> getSlides() { return slides; }
        /**
         * Sets slides.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("slides") public void setSlides(List<PresentationSlide> value) { slides = value; markField("slides"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("version")) fields.put("version", version);
            if (hasField("name")) fields.put("name", name);
            if (hasField("slides")) fields.put("slides", slides);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "version" -> { setVersion(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "name" -> { setName(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "slides" -> { setSlides(Json.MAPPER.convertValue(value, new TypeReference<List<PresentationSlide>>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "version" -> version = null;
                case "name" -> name = null;
                case "slides" -> slides = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of presentation.
     */
    public static final class Presentation extends Model {
        private String id;
        private String organizationId;
        private Long revision;
        private PresentationDocument document;
        /**
         * Creates an object with all properties unset.
         */
        public Presentation() {}
        /**
         * Returns id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("id") public String getId() { return id; }
        /**
         * Sets id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("id") public void setId(String value) { id = value; markField("id"); }
        /**
         * Returns organization id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("organization_id") public String getOrganizationId() { return organizationId; }
        /**
         * Sets organization id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("organization_id") public void setOrganizationId(String value) { organizationId = value; markField("organization_id"); }
        /**
         * Returns revision.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("revision") public Long getRevision() { return revision; }
        /**
         * Sets revision.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("revision") public void setRevision(Long value) { revision = value; markField("revision"); }
        /**
         * Returns document.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("document") public PresentationDocument getDocument() { return document; }
        /**
         * Sets document.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("document") public void setDocument(PresentationDocument value) { document = value; markField("document"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("id")) fields.put("id", id);
            if (hasField("organization_id")) fields.put("organization_id", organizationId);
            if (hasField("revision")) fields.put("revision", revision);
            if (hasField("document")) fields.put("document", document);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "id" -> { setId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "organization_id" -> { setOrganizationId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "revision" -> { setRevision(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "document" -> { setDocument(Json.MAPPER.convertValue(value, new TypeReference<PresentationDocument>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "id" -> id = null;
                case "organization_id" -> organizationId = null;
                case "revision" -> revision = null;
                case "document" -> document = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of presentation summary.
     */
    public static final class PresentationSummary extends Model {
        private String id;
        private String organizationId;
        private String name;
        private Long revision;
        private Long slideCount;
        /**
         * Creates an object with all properties unset.
         */
        public PresentationSummary() {}
        /**
         * Returns id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("id") public String getId() { return id; }
        /**
         * Sets id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("id") public void setId(String value) { id = value; markField("id"); }
        /**
         * Returns organization id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("organization_id") public String getOrganizationId() { return organizationId; }
        /**
         * Sets organization id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("organization_id") public void setOrganizationId(String value) { organizationId = value; markField("organization_id"); }
        /**
         * Returns name.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("name") public String getName() { return name; }
        /**
         * Sets name.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("name") public void setName(String value) { name = value; markField("name"); }
        /**
         * Returns revision.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("revision") public Long getRevision() { return revision; }
        /**
         * Sets revision.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("revision") public void setRevision(Long value) { revision = value; markField("revision"); }
        /**
         * Returns slide count.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("slide_count") public Long getSlideCount() { return slideCount; }
        /**
         * Sets slide count.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("slide_count") public void setSlideCount(Long value) { slideCount = value; markField("slide_count"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("id")) fields.put("id", id);
            if (hasField("organization_id")) fields.put("organization_id", organizationId);
            if (hasField("name")) fields.put("name", name);
            if (hasField("revision")) fields.put("revision", revision);
            if (hasField("slide_count")) fields.put("slide_count", slideCount);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "id" -> { setId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "organization_id" -> { setOrganizationId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "name" -> { setName(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "revision" -> { setRevision(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "slide_count" -> { setSlideCount(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "id" -> id = null;
                case "organization_id" -> organizationId = null;
                case "name" -> name = null;
                case "revision" -> revision = null;
                case "slide_count" -> slideCount = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of presentation page.
     */
    public static final class PresentationPage extends Model {
        private List<PresentationSummary> data;
        private String nextCursor;
        /**
         * Creates an object with all properties unset.
         */
        public PresentationPage() {}
        /**
         * Returns data.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("data") public List<PresentationSummary> getData() { return data; }
        /**
         * Sets data.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("data") public void setData(List<PresentationSummary> value) { data = value; markField("data"); }
        /**
         * Returns next cursor.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("next_cursor") public String getNextCursor() { return nextCursor; }
        /**
         * Sets next cursor.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("next_cursor") public void setNextCursor(String value) { nextCursor = value; markField("next_cursor"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("data")) fields.put("data", data);
            if (hasField("next_cursor")) fields.put("next_cursor", nextCursor);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "data" -> { setData(Json.MAPPER.convertValue(value, new TypeReference<List<PresentationSummary>>() {})); yield true; }
                case "next_cursor" -> { setNextCursor(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "data" -> data = null;
                case "next_cursor" -> nextCursor = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of create presentation.
     */
    public static final class CreatePresentation extends Model {
        private String name;
        /**
         * Creates an object with all properties unset.
         */
        public CreatePresentation() {}
        /**
         * UTF-8 text, at most 200 bytes; NUL is rejected.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("name") public String getName() { return name; }
        /**
         * Sets name.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("name") public void setName(String value) { name = value; markField("name"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("name")) fields.put("name", name);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "name" -> { setName(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "name" -> name = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of put presentation.
     */
    public static final class PutPresentation extends Model {
        private PresentationDocument document;
        /**
         * Creates an object with all properties unset.
         */
        public PutPresentation() {}
        /**
         * Returns document.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("document") public PresentationDocument getDocument() { return document; }
        /**
         * Sets document.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("document") public void setDocument(PresentationDocument value) { document = value; markField("document"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("document")) fields.put("document", document);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "document" -> { setDocument(Json.MAPPER.convertValue(value, new TypeReference<PresentationDocument>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "document" -> document = null;
                default -> { }
            }
        }
    }
    /**
     * Explicitly capture the current saved revision of a same-organization deck. This API has no historical revision archive: if the current saved revision differs, return 409 presentation_revision_conflict. Optional slide_id selects a stable saved slide. Missing slide returns 422. Empty decks and stopped Lives return 409.
     */
    public static final class PresentationDisplayReference extends Model {
        private String type;
        private String presentationId;
        private Long revision;
        private String slideId;
        /**
         * Creates an object with all properties unset.
         */
        public PresentationDisplayReference() {}
        /**
         * Returns type.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("type") public String getType() { return type; }
        /**
         * Sets type.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("type") public void setType(String value) { type = value; markField("type"); }
        /**
         * Returns presentation id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("presentation_id") public String getPresentationId() { return presentationId; }
        /**
         * Sets presentation id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("presentation_id") public void setPresentationId(String value) { presentationId = value; markField("presentation_id"); }
        /**
         * Returns revision.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("revision") public Long getRevision() { return revision; }
        /**
         * Sets revision.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("revision") public void setRevision(Long value) { revision = value; markField("revision"); }
        /**
         * Returns slide id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("slide_id") public String getSlideId() { return slideId; }
        /**
         * Sets slide id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("slide_id") public void setSlideId(String value) { slideId = value; markField("slide_id"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("type")) fields.put("type", type);
            if (hasField("presentation_id")) fields.put("presentation_id", presentationId);
            if (hasField("revision")) fields.put("revision", revision);
            if (hasField("slide_id")) fields.put("slide_id", slideId);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "type" -> { setType(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "presentation_id" -> { setPresentationId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "revision" -> { setRevision(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "slide_id" -> { setSlideId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "type" -> type = null;
                case "presentation_id" -> presentationId = null;
                case "revision" -> revision = null;
                case "slide_id" -> slideId = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of presentation asset.
     */
    public static final class PresentationAsset extends Model {
        private String id;
        private String organizationId;
        private String presentationId;
        private String kind;
        private String mime;
        private Long width;
        private Long height;
        private Long bytes;
        /**
         * Creates an object with all properties unset.
         */
        public PresentationAsset() {}
        /**
         * Returns id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("id") public String getId() { return id; }
        /**
         * Sets id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("id") public void setId(String value) { id = value; markField("id"); }
        /**
         * Returns organization id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("organization_id") public String getOrganizationId() { return organizationId; }
        /**
         * Sets organization id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("organization_id") public void setOrganizationId(String value) { organizationId = value; markField("organization_id"); }
        /**
         * Returns presentation id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("presentation_id") public String getPresentationId() { return presentationId; }
        /**
         * Sets presentation id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("presentation_id") public void setPresentationId(String value) { presentationId = value; markField("presentation_id"); }
        /**
         * Returns kind.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("kind") public String getKind() { return kind; }
        /**
         * Sets kind.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("kind") public void setKind(String value) { kind = value; markField("kind"); }
        /**
         * Returns mime.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("mime") public String getMime() { return mime; }
        /**
         * Sets mime.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("mime") public void setMime(String value) { mime = value; markField("mime"); }
        /**
         * Returns width.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("width") public Long getWidth() { return width; }
        /**
         * Sets width.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("width") public void setWidth(Long value) { width = value; markField("width"); }
        /**
         * Returns height.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("height") public Long getHeight() { return height; }
        /**
         * Sets height.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("height") public void setHeight(Long value) { height = value; markField("height"); }
        /**
         * Returns bytes.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("bytes") public Long getBytes() { return bytes; }
        /**
         * Sets bytes.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("bytes") public void setBytes(Long value) { bytes = value; markField("bytes"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("id")) fields.put("id", id);
            if (hasField("organization_id")) fields.put("organization_id", organizationId);
            if (hasField("presentation_id")) fields.put("presentation_id", presentationId);
            if (hasField("kind")) fields.put("kind", kind);
            if (hasField("mime")) fields.put("mime", mime);
            if (hasField("width")) fields.put("width", width);
            if (hasField("height")) fields.put("height", height);
            if (hasField("bytes")) fields.put("bytes", bytes);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "id" -> { setId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "organization_id" -> { setOrganizationId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "presentation_id" -> { setPresentationId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "kind" -> { setKind(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "mime" -> { setMime(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "width" -> { setWidth(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "height" -> { setHeight(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "bytes" -> { setBytes(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "id" -> id = null;
                case "organization_id" -> organizationId = null;
                case "presentation_id" -> presentationId = null;
                case "kind" -> kind = null;
                case "mime" -> mime = null;
                case "width" -> width = null;
                case "height" -> height = null;
                case "bytes" -> bytes = null;
                default -> { }
            }
        }
    }
    /**
     * Safe requester-private job projection. Timestamps are Unix seconds. Jobs snapshot the saved revision at creation; later saves never alter the captured document. Expiry is initially ten minutes and resets to ten minutes on completion/failure. Jobs disappear on expiry, presentation deletion or successful download consumption. Starting a new export removes this requester’s failed jobs.
     */
    public static final class PresentationExport extends Model {
        private String id;
        private String presentationId;
        private String organizationId;
        private Long revision;
        private String format;
        private String status;
        private String error;
        private Long bytes;
        private Long createdAt;
        private Long expiresAt;
        private String downloadUrl;
        /**
         * Creates an object with all properties unset.
         */
        public PresentationExport() {}
        /**
         * Returns id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("id") public String getId() { return id; }
        /**
         * Sets id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("id") public void setId(String value) { id = value; markField("id"); }
        /**
         * Returns presentation id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("presentation_id") public String getPresentationId() { return presentationId; }
        /**
         * Sets presentation id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("presentation_id") public void setPresentationId(String value) { presentationId = value; markField("presentation_id"); }
        /**
         * Returns organization id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("organization_id") public String getOrganizationId() { return organizationId; }
        /**
         * Sets organization id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("organization_id") public void setOrganizationId(String value) { organizationId = value; markField("organization_id"); }
        /**
         * Returns revision.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("revision") public Long getRevision() { return revision; }
        /**
         * Sets revision.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("revision") public void setRevision(Long value) { revision = value; markField("revision"); }
        /**
         * Returns format.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("format") public String getFormat() { return format; }
        /**
         * Sets format.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("format") public void setFormat(String value) { format = value; markField("format"); }
        /**
         * Returns status.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("status") public String getStatus() { return status; }
        /**
         * Sets status.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("status") public void setStatus(String value) { status = value; markField("status"); }
        /**
         * Returns error.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("error") public String getError() { return error; }
        /**
         * Sets error.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("error") public void setError(String value) { error = value; markField("error"); }
        /**
         * Returns bytes.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("bytes") public Long getBytes() { return bytes; }
        /**
         * Sets bytes.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("bytes") public void setBytes(Long value) { bytes = value; markField("bytes"); }
        /**
         * Returns created at.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("created_at") public Long getCreatedAt() { return createdAt; }
        /**
         * Sets created at.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("created_at") public void setCreatedAt(Long value) { createdAt = value; markField("created_at"); }
        /**
         * Returns expires at.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("expires_at") public Long getExpiresAt() { return expiresAt; }
        /**
         * Sets expires at.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("expires_at") public void setExpiresAt(Long value) { expiresAt = value; markField("expires_at"); }
        /**
         * Relative POST downloads URL, available only when completed. Requires the requesting credential and current presentation.export scope. Never a public URL.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("download_url") public String getDownloadUrl() { return downloadUrl; }
        /**
         * Sets download url.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("download_url") public void setDownloadUrl(String value) { downloadUrl = value; markField("download_url"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("id")) fields.put("id", id);
            if (hasField("presentation_id")) fields.put("presentation_id", presentationId);
            if (hasField("organization_id")) fields.put("organization_id", organizationId);
            if (hasField("revision")) fields.put("revision", revision);
            if (hasField("format")) fields.put("format", format);
            if (hasField("status")) fields.put("status", status);
            if (hasField("error")) fields.put("error", error);
            if (hasField("bytes")) fields.put("bytes", bytes);
            if (hasField("created_at")) fields.put("created_at", createdAt);
            if (hasField("expires_at")) fields.put("expires_at", expiresAt);
            if (hasField("download_url")) fields.put("download_url", downloadUrl);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "id" -> { setId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "presentation_id" -> { setPresentationId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "organization_id" -> { setOrganizationId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "revision" -> { setRevision(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "format" -> { setFormat(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "status" -> { setStatus(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "error" -> { setError(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "bytes" -> { setBytes(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "created_at" -> { setCreatedAt(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "expires_at" -> { setExpiresAt(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "download_url" -> { setDownloadUrl(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "id" -> id = null;
                case "presentation_id" -> presentationId = null;
                case "organization_id" -> organizationId = null;
                case "revision" -> revision = null;
                case "format" -> format = null;
                case "status" -> status = null;
                case "error" -> error = null;
                case "bytes" -> bytes = null;
                case "created_at" -> createdAt = null;
                case "expires_at" -> expiresAt = null;
                case "download_url" -> downloadUrl = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of presentation export list.
     */
    public static final class PresentationExportList extends Model {
        private List<PresentationExport> data;
        private String nextCursor;
        /**
         * Creates an object with all properties unset.
         */
        public PresentationExportList() {}
        /**
         * Returns data.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("data") public List<PresentationExport> getData() { return data; }
        /**
         * Sets data.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("data") public void setData(List<PresentationExport> value) { data = value; markField("data"); }
        /**
         * Returns next cursor.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("next_cursor") public String getNextCursor() { return nextCursor; }
        /**
         * Sets next cursor.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("next_cursor") public void setNextCursor(String value) { nextCursor = value; markField("next_cursor"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("data")) fields.put("data", data);
            if (hasField("next_cursor")) fields.put("next_cursor", nextCursor);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "data" -> { setData(Json.MAPPER.convertValue(value, new TypeReference<List<PresentationExport>>() {})); yield true; }
                case "next_cursor" -> { setNextCursor(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "data" -> data = null;
                case "next_cursor" -> nextCursor = null;
                default -> { }
            }
        }
    }
    /**
     * Explicit current saved revision. PNG/JPEG require one slide_index; other formats export the whole deck and reject slide_index. Empty presentations support only bibleit. Locale affects exported navigation labels. Known variant fields share one wire object; the server validates variant-specific requirements.
     */
    public static final class CreatePresentationExport extends Model {
        private Long revision;
        private String format;
        private String locale;
        private Long slideIndex;
        /**
         * Creates an object with all properties unset.
         */
        public CreatePresentationExport() {}
        /**
         * Returns revision.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("revision") public Long getRevision() { return revision; }
        /**
         * Sets revision.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("revision") public void setRevision(Long value) { revision = value; markField("revision"); }
        /**
         * Returns format.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("format") public String getFormat() { return format; }
        /**
         * Sets format.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("format") public void setFormat(String value) { format = value; markField("format"); }
        /**
         * Returns locale.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("locale") public String getLocale() { return locale; }
        /**
         * Sets locale.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("locale") public void setLocale(String value) { locale = value; markField("locale"); }
        /**
         * Required zero-based index in this saved revision.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("slide_index") public Long getSlideIndex() { return slideIndex; }
        /**
         * Sets slide index.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("slide_index") public void setSlideIndex(Long value) { slideIndex = value; markField("slide_index"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("revision")) fields.put("revision", revision);
            if (hasField("format")) fields.put("format", format);
            if (hasField("locale")) fields.put("locale", locale);
            if (hasField("slide_index")) fields.put("slide_index", slideIndex);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "revision" -> { setRevision(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "format" -> { setFormat(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "locale" -> { setLocale(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "slide_index" -> { setSlideIndex(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "revision" -> revision = null;
                case "format" -> format = null;
                case "locale" -> locale = null;
                case "slide_index" -> slideIndex = null;
                default -> { }
            }
        }
    }
    /**
     * Personal-account Reader Live resource. Uses account_id instead of organization_id; only the personal account owner can access it with current personal token authority.
     */
    public static final class PersonalLiveSummary extends Model {
        private String id;
        private String name;
        private String accountId;
        /**
         * Creates an object with all properties unset.
         */
        public PersonalLiveSummary() {}
        /**
         * Returns id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("id") public String getId() { return id; }
        /**
         * Sets id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("id") public void setId(String value) { id = value; markField("id"); }
        /**
         * Returns name.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("name") public String getName() { return name; }
        /**
         * Sets name.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("name") public void setName(String value) { name = value; markField("name"); }
        /**
         * Returns account id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("account_id") public String getAccountId() { return accountId; }
        /**
         * Sets account id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("account_id") public void setAccountId(String value) { accountId = value; markField("account_id"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("id")) fields.put("id", id);
            if (hasField("name")) fields.put("name", name);
            if (hasField("account_id")) fields.put("account_id", accountId);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "id" -> { setId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "name" -> { setName(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "account_id" -> { setAccountId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "id" -> id = null;
                case "name" -> name = null;
                case "account_id" -> accountId = null;
                default -> { }
            }
        }
    }
    /**
     * Personal-account Reader Live resource. Uses account_id instead of organization_id; only the personal account owner can access it with current personal token authority.
     */
    public static final class PersonalLive extends Model {
        private String id;
        private String name;
        private String status;
        private String mode;
        private Boolean paused;
        private Long sequence;
        private String accountId;
        /**
         * Creates an object with all properties unset.
         */
        public PersonalLive() {}
        /**
         * Returns id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("id") public String getId() { return id; }
        /**
         * Sets id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("id") public void setId(String value) { id = value; markField("id"); }
        /**
         * Returns name.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("name") public String getName() { return name; }
        /**
         * Sets name.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("name") public void setName(String value) { name = value; markField("name"); }
        /**
         * Returns status.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("status") public String getStatus() { return status; }
        /**
         * Sets status.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("status") public void setStatus(String value) { status = value; markField("status"); }
        /**
         * Returns mode.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("mode") public String getMode() { return mode; }
        /**
         * Sets mode.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("mode") public void setMode(String value) { mode = value; markField("mode"); }
        /**
         * Returns paused.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("paused") public Boolean getPaused() { return paused; }
        /**
         * Sets paused.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("paused") public void setPaused(Boolean value) { paused = value; markField("paused"); }
        /**
         * Returns sequence.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("sequence") public Long getSequence() { return sequence; }
        /**
         * Sets sequence.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("sequence") public void setSequence(Long value) { sequence = value; markField("sequence"); }
        /**
         * Returns account id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("account_id") public String getAccountId() { return accountId; }
        /**
         * Sets account id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("account_id") public void setAccountId(String value) { accountId = value; markField("account_id"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("id")) fields.put("id", id);
            if (hasField("name")) fields.put("name", name);
            if (hasField("status")) fields.put("status", status);
            if (hasField("mode")) fields.put("mode", mode);
            if (hasField("paused")) fields.put("paused", paused);
            if (hasField("sequence")) fields.put("sequence", sequence);
            if (hasField("account_id")) fields.put("account_id", accountId);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "id" -> { setId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "name" -> { setName(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "status" -> { setStatus(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "mode" -> { setMode(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "paused" -> { setPaused(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "sequence" -> { setSequence(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "account_id" -> { setAccountId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "id" -> id = null;
                case "name" -> name = null;
                case "status" -> status = null;
                case "mode" -> mode = null;
                case "paused" -> paused = null;
                case "sequence" -> sequence = null;
                case "account_id" -> accountId = null;
                default -> { }
            }
        }
    }
    /**
     * Personal-account Reader Live resource. Uses account_id instead of organization_id; only the personal account owner can access it with current personal token authority.
     */
    public static final class PersonalLiveDisplay extends Model {
        private String liveId;
        private String status;
        private Boolean paused;
        private Boolean showing;
        private Long sequence;
        private String type;
        private List<DisplayVerse> verses;
        private Void snapshot;
        private String accountId;
        /**
         * Creates an object with all properties unset.
         */
        public PersonalLiveDisplay() {}
        /**
         * Returns live id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("live_id") public String getLiveId() { return liveId; }
        /**
         * Sets live id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("live_id") public void setLiveId(String value) { liveId = value; markField("live_id"); }
        /**
         * Returns status.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("status") public String getStatus() { return status; }
        /**
         * Sets status.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("status") public void setStatus(String value) { status = value; markField("status"); }
        /**
         * Returns paused.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("paused") public Boolean getPaused() { return paused; }
        /**
         * Sets paused.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("paused") public void setPaused(Boolean value) { paused = value; markField("paused"); }
        /**
         * Returns showing.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("showing") public Boolean getShowing() { return showing; }
        /**
         * Sets showing.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("showing") public void setShowing(Boolean value) { showing = value; markField("showing"); }
        /**
         * Returns sequence.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("sequence") public Long getSequence() { return sequence; }
        /**
         * Sets sequence.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("sequence") public void setSequence(Long value) { sequence = value; markField("sequence"); }
        /**
         * Returns type.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("type") public String getType() { return type; }
        /**
         * Sets type.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("type") public void setType(String value) { type = value; markField("type"); }
        /**
         * Returns verses.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("verses") public List<DisplayVerse> getVerses() { return verses; }
        /**
         * Sets verses.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("verses") public void setVerses(List<DisplayVerse> value) { verses = value; markField("verses"); }
        /**
         * Returns snapshot.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("snapshot") public Void getSnapshot() { return snapshot; }
        /**
         * Sets snapshot.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("snapshot") public void setSnapshot(Void value) { snapshot = value; markField("snapshot"); }
        /**
         * Returns account id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("account_id") public String getAccountId() { return accountId; }
        /**
         * Sets account id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("account_id") public void setAccountId(String value) { accountId = value; markField("account_id"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("live_id")) fields.put("live_id", liveId);
            if (hasField("status")) fields.put("status", status);
            if (hasField("paused")) fields.put("paused", paused);
            if (hasField("showing")) fields.put("showing", showing);
            if (hasField("sequence")) fields.put("sequence", sequence);
            if (hasField("type")) fields.put("type", type);
            if (hasField("verses")) fields.put("verses", verses);
            if (hasField("snapshot")) fields.put("snapshot", snapshot);
            if (hasField("account_id")) fields.put("account_id", accountId);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "live_id" -> { setLiveId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "status" -> { setStatus(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "paused" -> { setPaused(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "showing" -> { setShowing(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "sequence" -> { setSequence(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "type" -> { setType(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "verses" -> { setVerses(Json.MAPPER.convertValue(value, new TypeReference<List<DisplayVerse>>() {})); yield true; }
                case "snapshot" -> { setSnapshot(Json.MAPPER.convertValue(value, new TypeReference<Void>() {})); yield true; }
                case "account_id" -> { setAccountId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "live_id" -> liveId = null;
                case "status" -> status = null;
                case "paused" -> paused = null;
                case "showing" -> showing = null;
                case "sequence" -> sequence = null;
                case "type" -> type = null;
                case "verses" -> verses = null;
                case "snapshot" -> snapshot = null;
                case "account_id" -> accountId = null;
                default -> { }
            }
        }
    }
    /**
     * Personal-account Reader Live resource. Uses account_id instead of organization_id; only the personal account owner can access it with current personal token authority.
     */
    public static final class PersonalLiveQueuePage extends Model {
        private String liveId;
        private Long sequence;
        private List<LiveQueueItem> data;
        private String nextCursor;
        private String accountId;
        /**
         * Creates an object with all properties unset.
         */
        public PersonalLiveQueuePage() {}
        /**
         * Returns live id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("live_id") public String getLiveId() { return liveId; }
        /**
         * Sets live id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("live_id") public void setLiveId(String value) { liveId = value; markField("live_id"); }
        /**
         * Returns sequence.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("sequence") public Long getSequence() { return sequence; }
        /**
         * Sets sequence.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("sequence") public void setSequence(Long value) { sequence = value; markField("sequence"); }
        /**
         * Returns data.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("data") public List<LiveQueueItem> getData() { return data; }
        /**
         * Sets data.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("data") public void setData(List<LiveQueueItem> value) { data = value; markField("data"); }
        /**
         * Opaque sequence-bound cursor. Pass unchanged as after. Any intervening Live sequence change returns 409; malformed/unknown cursor returns 422.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("next_cursor") public String getNextCursor() { return nextCursor; }
        /**
         * Sets next cursor.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("next_cursor") public void setNextCursor(String value) { nextCursor = value; markField("next_cursor"); }
        /**
         * Returns account id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("account_id") public String getAccountId() { return accountId; }
        /**
         * Sets account id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("account_id") public void setAccountId(String value) { accountId = value; markField("account_id"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("live_id")) fields.put("live_id", liveId);
            if (hasField("sequence")) fields.put("sequence", sequence);
            if (hasField("data")) fields.put("data", data);
            if (hasField("next_cursor")) fields.put("next_cursor", nextCursor);
            if (hasField("account_id")) fields.put("account_id", accountId);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "live_id" -> { setLiveId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "sequence" -> { setSequence(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "data" -> { setData(Json.MAPPER.convertValue(value, new TypeReference<List<LiveQueueItem>>() {})); yield true; }
                case "next_cursor" -> { setNextCursor(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "account_id" -> { setAccountId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "live_id" -> liveId = null;
                case "sequence" -> sequence = null;
                case "data" -> data = null;
                case "next_cursor" -> nextCursor = null;
                case "account_id" -> accountId = null;
                default -> { }
            }
        }
    }
    /**
     * Personal-account Reader Live resource. Uses account_id instead of organization_id; only the personal account owner can access it with current personal token authority.
     */
    public static final class PersonalLiveList extends Model {
        private List<PersonalLiveSummary> data;
        private String nextCursor;
        /**
         * Creates an object with all properties unset.
         */
        public PersonalLiveList() {}
        /**
         * Returns data.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("data") public List<PersonalLiveSummary> getData() { return data; }
        /**
         * Sets data.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("data") public void setData(List<PersonalLiveSummary> value) { data = value; markField("data"); }
        /**
         * Pass this value unchanged as after; null ends the collection.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("next_cursor") public String getNextCursor() { return nextCursor; }
        /**
         * Sets next cursor.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("next_cursor") public void setNextCursor(String value) { nextCursor = value; markField("next_cursor"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("data")) fields.put("data", data);
            if (hasField("next_cursor")) fields.put("next_cursor", nextCursor);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "data" -> { setData(Json.MAPPER.convertValue(value, new TypeReference<List<PersonalLiveSummary>>() {})); yield true; }
                case "next_cursor" -> { setNextCursor(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "data" -> data = null;
                case "next_cursor" -> nextCursor = null;
                default -> { }
            }
        }
    }
    /**
     * Self-only safe user identity. No email, provider subject, internal principal, credentials or global user directory.
     */
    public static final class PersonalIdentity extends Model {
        private String id;
        private String displayName;
        private String handle;
        private String personalAccountId;
        /**
         * Creates an object with all properties unset.
         */
        public PersonalIdentity() {}
        /**
         * Returns id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("id") public String getId() { return id; }
        /**
         * Sets id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("id") public void setId(String value) { id = value; markField("id"); }
        /**
         * Returns display name.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("display_name") public String getDisplayName() { return displayName; }
        /**
         * Sets display name.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("display_name") public void setDisplayName(String value) { displayName = value; markField("display_name"); }
        /**
         * Returns handle.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("handle") public String getHandle() { return handle; }
        /**
         * Sets handle.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("handle") public void setHandle(String value) { handle = value; markField("handle"); }
        /**
         * Returns personal account id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("personal_account_id") public String getPersonalAccountId() { return personalAccountId; }
        /**
         * Sets personal account id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("personal_account_id") public void setPersonalAccountId(String value) { personalAccountId = value; markField("personal_account_id"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("id")) fields.put("id", id);
            if (hasField("display_name")) fields.put("display_name", displayName);
            if (hasField("handle")) fields.put("handle", handle);
            if (hasField("personal_account_id")) fields.put("personal_account_id", personalAccountId);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "id" -> { setId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "display_name" -> { setDisplayName(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "handle" -> { setHandle(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "personal_account_id" -> { setPersonalAccountId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "id" -> id = null;
                case "display_name" -> displayName = null;
                case "handle" -> handle = null;
                case "personal_account_id" -> personalAccountId = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of personal account.
     */
    public static final class PersonalAccount extends Model {
        private String id;
        private String kind;
        private String name;
        private String handle;
        /**
         * Creates an object with all properties unset.
         */
        public PersonalAccount() {}
        /**
         * Returns id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("id") public String getId() { return id; }
        /**
         * Sets id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("id") public void setId(String value) { id = value; markField("id"); }
        /**
         * Returns kind.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("kind") public String getKind() { return kind; }
        /**
         * Sets kind.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("kind") public void setKind(String value) { kind = value; markField("kind"); }
        /**
         * Returns name.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("name") public String getName() { return name; }
        /**
         * Sets name.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("name") public void setName(String value) { name = value; markField("name"); }
        /**
         * Returns handle.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("handle") public String getHandle() { return handle; }
        /**
         * Sets handle.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("handle") public void setHandle(String value) { handle = value; markField("handle"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("id")) fields.put("id", id);
            if (hasField("kind")) fields.put("kind", kind);
            if (hasField("name")) fields.put("name", name);
            if (hasField("handle")) fields.put("handle", handle);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "id" -> { setId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "kind" -> { setKind(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "name" -> { setName(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "handle" -> { setHandle(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "id" -> id = null;
                case "kind" -> kind = null;
                case "name" -> name = null;
                case "handle" -> handle = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of personal account list.
     */
    public static final class PersonalAccountList extends Model {
        private List<PersonalAccount> data;
        private Void nextCursor;
        /**
         * Creates an object with all properties unset.
         */
        public PersonalAccountList() {}
        /**
         * Returns data.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("data") public List<PersonalAccount> getData() { return data; }
        /**
         * Sets data.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("data") public void setData(List<PersonalAccount> value) { data = value; markField("data"); }
        /**
         * Returns next cursor.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("next_cursor") public Void getNextCursor() { return nextCursor; }
        /**
         * Sets next cursor.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("next_cursor") public void setNextCursor(Void value) { nextCursor = value; markField("next_cursor"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("data")) fields.put("data", data);
            if (hasField("next_cursor")) fields.put("next_cursor", nextCursor);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "data" -> { setData(Json.MAPPER.convertValue(value, new TypeReference<List<PersonalAccount>>() {})); yield true; }
                case "next_cursor" -> { setNextCursor(Json.MAPPER.convertValue(value, new TypeReference<Void>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "data" -> data = null;
                case "next_cursor" -> nextCursor = null;
                default -> { }
            }
        }
    }
    /**
     * OAuth protocol error JSON, rather than REST Problem Details. Error redirects are sent only following a validated, session-bound consent decision. Invalid clients/redirects fail locally.
     */
    public static final class OAuthError extends Model {
        private String error;
        private String errorDescription;
        /**
         * Creates an object with all properties unset.
         */
        public OAuthError() {}
        /**
         * Returns error.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("error") public String getError() { return error; }
        /**
         * Sets error.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("error") public void setError(String value) { error = value; markField("error"); }
        /**
         * Untrusted OAuth recovery guidance.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("error_description") public String getErrorDescription() { return errorDescription; }
        /**
         * Sets error description.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("error_description") public void setErrorDescription(String value) { errorDescription = value; markField("error_description"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("error")) fields.put("error", error);
            if (hasField("error_description")) fields.put("error_description", errorDescription);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "error" -> { setError(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "error_description" -> { setErrorDescription(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "error" -> error = null;
                case "error_description" -> errorDescription = null;
                default -> { }
            }
        }
    }
    /**
     * Opaque access token; at most 15 minutes and never beyond the grant deadline. refresh_token appears only with offline_access. Store tokens securely; hashes alone are retained server-side. Fixed grant lifetime 30 days; refreshing never extends it.
     */
    public static final class OAuthToken extends Model {
        private String accessToken;
        private String tokenType;
        private Long expiresIn;
        private String scope;
        private String refreshToken;
        /**
         * Creates an object with all properties unset.
         */
        public OAuthToken() {}
        /**
         * Returns access token.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("access_token") public String getAccessToken() { return accessToken; }
        /**
         * Sets access token.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("access_token") public void setAccessToken(String value) { accessToken = value; markField("access_token"); }
        /**
         * Returns token type.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("token_type") public String getTokenType() { return tokenType; }
        /**
         * Sets token type.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("token_type") public void setTokenType(String value) { tokenType = value; markField("token_type"); }
        /**
         * Returns expires in.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("expires_in") public Long getExpiresIn() { return expiresIn; }
        /**
         * Sets expires in.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("expires_in") public void setExpiresIn(Long value) { expiresIn = value; markField("expires_in"); }
        /**
         * Returns scope.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("scope") public String getScope() { return scope; }
        /**
         * Sets scope.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("scope") public void setScope(String value) { scope = value; markField("scope"); }
        /**
         * Returns refresh token.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("refresh_token") public String getRefreshToken() { return refreshToken; }
        /**
         * Sets refresh token.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("refresh_token") public void setRefreshToken(String value) { refreshToken = value; markField("refresh_token"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("access_token")) fields.put("access_token", accessToken);
            if (hasField("token_type")) fields.put("token_type", tokenType);
            if (hasField("expires_in")) fields.put("expires_in", expiresIn);
            if (hasField("scope")) fields.put("scope", scope);
            if (hasField("refresh_token")) fields.put("refresh_token", refreshToken);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "access_token" -> { setAccessToken(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "token_type" -> { setTokenType(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "expires_in" -> { setExpiresIn(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "scope" -> { setScope(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "refresh_token" -> { setRefreshToken(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "access_token" -> accessToken = null;
                case "token_type" -> tokenType = null;
                case "expires_in" -> expiresIn = null;
                case "scope" -> scope = null;
                case "refresh_token" -> refreshToken = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of oauth metadata.
     */
    public static final class OAuthMetadata extends Model {
        private String issuer;
        private String authorizationEndpoint;
        private String tokenEndpoint;
        private String revocationEndpoint;
        private List<String> responseTypesSupported;
        private List<String> grantTypesSupported;
        private List<String> tokenEndpointAuthMethodsSupported;
        private List<String> revocationEndpointAuthMethodsSupported;
        private List<String> codeChallengeMethodsSupported;
        private List<String> scopesSupported;
        private Boolean authorizationResponseIssParameterSupported;
        /**
         * Creates an object with all properties unset.
         */
        public OAuthMetadata() {}
        /**
         * Returns issuer.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("issuer") public String getIssuer() { return issuer; }
        /**
         * Sets issuer.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("issuer") public void setIssuer(String value) { issuer = value; markField("issuer"); }
        /**
         * Returns authorization endpoint.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("authorization_endpoint") public String getAuthorizationEndpoint() { return authorizationEndpoint; }
        /**
         * Sets authorization endpoint.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("authorization_endpoint") public void setAuthorizationEndpoint(String value) { authorizationEndpoint = value; markField("authorization_endpoint"); }
        /**
         * Returns token endpoint.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("token_endpoint") public String getTokenEndpoint() { return tokenEndpoint; }
        /**
         * Sets token endpoint.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("token_endpoint") public void setTokenEndpoint(String value) { tokenEndpoint = value; markField("token_endpoint"); }
        /**
         * Returns revocation endpoint.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("revocation_endpoint") public String getRevocationEndpoint() { return revocationEndpoint; }
        /**
         * Sets revocation endpoint.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("revocation_endpoint") public void setRevocationEndpoint(String value) { revocationEndpoint = value; markField("revocation_endpoint"); }
        /**
         * Returns response types supported.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("response_types_supported") public List<String> getResponseTypesSupported() { return responseTypesSupported; }
        /**
         * Sets response types supported.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("response_types_supported") public void setResponseTypesSupported(List<String> value) { responseTypesSupported = value; markField("response_types_supported"); }
        /**
         * Returns grant types supported.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("grant_types_supported") public List<String> getGrantTypesSupported() { return grantTypesSupported; }
        /**
         * Sets grant types supported.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("grant_types_supported") public void setGrantTypesSupported(List<String> value) { grantTypesSupported = value; markField("grant_types_supported"); }
        /**
         * Returns token endpoint auth methods supported.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("token_endpoint_auth_methods_supported") public List<String> getTokenEndpointAuthMethodsSupported() { return tokenEndpointAuthMethodsSupported; }
        /**
         * Sets token endpoint auth methods supported.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("token_endpoint_auth_methods_supported") public void setTokenEndpointAuthMethodsSupported(List<String> value) { tokenEndpointAuthMethodsSupported = value; markField("token_endpoint_auth_methods_supported"); }
        /**
         * Returns revocation endpoint auth methods supported.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("revocation_endpoint_auth_methods_supported") public List<String> getRevocationEndpointAuthMethodsSupported() { return revocationEndpointAuthMethodsSupported; }
        /**
         * Sets revocation endpoint auth methods supported.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("revocation_endpoint_auth_methods_supported") public void setRevocationEndpointAuthMethodsSupported(List<String> value) { revocationEndpointAuthMethodsSupported = value; markField("revocation_endpoint_auth_methods_supported"); }
        /**
         * Returns code challenge methods supported.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("code_challenge_methods_supported") public List<String> getCodeChallengeMethodsSupported() { return codeChallengeMethodsSupported; }
        /**
         * Sets code challenge methods supported.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("code_challenge_methods_supported") public void setCodeChallengeMethodsSupported(List<String> value) { codeChallengeMethodsSupported = value; markField("code_challenge_methods_supported"); }
        /**
         * Returns scopes supported.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("scopes_supported") public List<String> getScopesSupported() { return scopesSupported; }
        /**
         * Sets scopes supported.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("scopes_supported") public void setScopesSupported(List<String> value) { scopesSupported = value; markField("scopes_supported"); }
        /**
         * Returns authorization response iss parameter supported.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("authorization_response_iss_parameter_supported") public Boolean getAuthorizationResponseIssParameterSupported() { return authorizationResponseIssParameterSupported; }
        /**
         * Sets authorization response iss parameter supported.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("authorization_response_iss_parameter_supported") public void setAuthorizationResponseIssParameterSupported(Boolean value) { authorizationResponseIssParameterSupported = value; markField("authorization_response_iss_parameter_supported"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("issuer")) fields.put("issuer", issuer);
            if (hasField("authorization_endpoint")) fields.put("authorization_endpoint", authorizationEndpoint);
            if (hasField("token_endpoint")) fields.put("token_endpoint", tokenEndpoint);
            if (hasField("revocation_endpoint")) fields.put("revocation_endpoint", revocationEndpoint);
            if (hasField("response_types_supported")) fields.put("response_types_supported", responseTypesSupported);
            if (hasField("grant_types_supported")) fields.put("grant_types_supported", grantTypesSupported);
            if (hasField("token_endpoint_auth_methods_supported")) fields.put("token_endpoint_auth_methods_supported", tokenEndpointAuthMethodsSupported);
            if (hasField("revocation_endpoint_auth_methods_supported")) fields.put("revocation_endpoint_auth_methods_supported", revocationEndpointAuthMethodsSupported);
            if (hasField("code_challenge_methods_supported")) fields.put("code_challenge_methods_supported", codeChallengeMethodsSupported);
            if (hasField("scopes_supported")) fields.put("scopes_supported", scopesSupported);
            if (hasField("authorization_response_iss_parameter_supported")) fields.put("authorization_response_iss_parameter_supported", authorizationResponseIssParameterSupported);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "issuer" -> { setIssuer(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "authorization_endpoint" -> { setAuthorizationEndpoint(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "token_endpoint" -> { setTokenEndpoint(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "revocation_endpoint" -> { setRevocationEndpoint(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "response_types_supported" -> { setResponseTypesSupported(Json.MAPPER.convertValue(value, new TypeReference<List<String>>() {})); yield true; }
                case "grant_types_supported" -> { setGrantTypesSupported(Json.MAPPER.convertValue(value, new TypeReference<List<String>>() {})); yield true; }
                case "token_endpoint_auth_methods_supported" -> { setTokenEndpointAuthMethodsSupported(Json.MAPPER.convertValue(value, new TypeReference<List<String>>() {})); yield true; }
                case "revocation_endpoint_auth_methods_supported" -> { setRevocationEndpointAuthMethodsSupported(Json.MAPPER.convertValue(value, new TypeReference<List<String>>() {})); yield true; }
                case "code_challenge_methods_supported" -> { setCodeChallengeMethodsSupported(Json.MAPPER.convertValue(value, new TypeReference<List<String>>() {})); yield true; }
                case "scopes_supported" -> { setScopesSupported(Json.MAPPER.convertValue(value, new TypeReference<List<String>>() {})); yield true; }
                case "authorization_response_iss_parameter_supported" -> { setAuthorizationResponseIssParameterSupported(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "issuer" -> issuer = null;
                case "authorization_endpoint" -> authorizationEndpoint = null;
                case "token_endpoint" -> tokenEndpoint = null;
                case "revocation_endpoint" -> revocationEndpoint = null;
                case "response_types_supported" -> responseTypesSupported = null;
                case "grant_types_supported" -> grantTypesSupported = null;
                case "token_endpoint_auth_methods_supported" -> tokenEndpointAuthMethodsSupported = null;
                case "revocation_endpoint_auth_methods_supported" -> revocationEndpointAuthMethodsSupported = null;
                case "code_challenge_methods_supported" -> codeChallengeMethodsSupported = null;
                case "scopes_supported" -> scopesSupported = null;
                case "authorization_response_iss_parameter_supported" -> authorizationResponseIssParameterSupported = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of oauth grant.
     */
    public static final class OAuthGrant extends Model {
        private String id;
        private String clientId;
        private String clientName;
        private List<OAuthScope> scopes;
        private Long expiresAt;
        /**
         * Creates an object with all properties unset.
         */
        public OAuthGrant() {}
        /**
         * Returns id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("id") public String getId() { return id; }
        /**
         * Sets id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("id") public void setId(String value) { id = value; markField("id"); }
        /**
         * Returns client id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("client_id") public String getClientId() { return clientId; }
        /**
         * Sets client id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("client_id") public void setClientId(String value) { clientId = value; markField("client_id"); }
        /**
         * Returns client name.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("client_name") public String getClientName() { return clientName; }
        /**
         * Sets client name.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("client_name") public void setClientName(String value) { clientName = value; markField("client_name"); }
        /**
         * Returns scopes.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("scopes") public List<OAuthScope> getScopes() { return scopes; }
        /**
         * Sets scopes.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("scopes") public void setScopes(List<OAuthScope> value) { scopes = value; markField("scopes"); }
        /**
         * Returns expires at.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("expires_at") public Long getExpiresAt() { return expiresAt; }
        /**
         * Sets expires at.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("expires_at") public void setExpiresAt(Long value) { expiresAt = value; markField("expires_at"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("id")) fields.put("id", id);
            if (hasField("client_id")) fields.put("client_id", clientId);
            if (hasField("client_name")) fields.put("client_name", clientName);
            if (hasField("scopes")) fields.put("scopes", scopes);
            if (hasField("expires_at")) fields.put("expires_at", expiresAt);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "id" -> { setId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "client_id" -> { setClientId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "client_name" -> { setClientName(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "scopes" -> { setScopes(Json.MAPPER.convertValue(value, new TypeReference<List<OAuthScope>>() {})); yield true; }
                case "expires_at" -> { setExpiresAt(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "id" -> id = null;
                case "client_id" -> clientId = null;
                case "client_name" -> clientName = null;
                case "scopes" -> scopes = null;
                case "expires_at" -> expiresAt = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of oauth grant list.
     */
    public static final class OAuthGrantList extends Model {
        private List<OAuthGrant> data;
        /**
         * Creates an object with all properties unset.
         */
        public OAuthGrantList() {}
        /**
         * Returns data.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("data") public List<OAuthGrant> getData() { return data; }
        /**
         * Sets data.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("data") public void setData(List<OAuthGrant> value) { data = value; markField("data"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("data")) fields.put("data", data);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "data" -> { setData(Json.MAPPER.convertValue(value, new TypeReference<List<OAuthGrant>>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "data" -> data = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live event viewer settings.
     */
    public static final class LiveEventViewerSettings extends Model {
        private String theme;
        private Long scale;
        private Boolean presentation;
        private String preset;
        private String transition;
        /**
         * Creates an object with all properties unset.
         */
        public LiveEventViewerSettings() {}
        /**
         * Returns theme.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("theme") public String getTheme() { return theme; }
        /**
         * Sets theme.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("theme") public void setTheme(String value) { theme = value; markField("theme"); }
        /**
         * Existing viewer font-size step, independent of Studio text size.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("scale") public Long getScale() { return scale; }
        /**
         * Sets scale.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("scale") public void setScale(Long value) { scale = value; markField("scale"); }
        /**
         * Returns presentation.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("presentation") public Boolean getPresentation() { return presentation; }
        /**
         * Sets presentation.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("presentation") public void setPresentation(Boolean value) { presentation = value; markField("presentation"); }
        /**
         * Returns preset.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("preset") public String getPreset() { return preset; }
        /**
         * Sets preset.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("preset") public void setPreset(String value) { preset = value; markField("preset"); }
        /**
         * Returns transition.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("transition") public String getTransition() { return transition; }
        /**
         * Sets transition.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("transition") public void setTransition(String value) { transition = value; markField("transition"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("theme")) fields.put("theme", theme);
            if (hasField("scale")) fields.put("scale", scale);
            if (hasField("presentation")) fields.put("presentation", presentation);
            if (hasField("preset")) fields.put("preset", preset);
            if (hasField("transition")) fields.put("transition", transition);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "theme" -> { setTheme(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "scale" -> { setScale(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "presentation" -> { setPresentation(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "preset" -> { setPreset(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "transition" -> { setTransition(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "theme" -> theme = null;
                case "scale" -> scale = null;
                case "presentation" -> presentation = null;
                case "preset" -> preset = null;
                case "transition" -> transition = null;
                default -> { }
            }
        }
    }
    /**
     * Audience-safe Studio design. revision is independent of the Live sequence. Personal Lives use defaults and omit organization.
     */
    public static final class LiveEventDesign extends Model {
        private LiveStudioSettings settings;
        private Long revision;
        private String organization;
        /**
         * Creates an object with all properties unset.
         */
        public LiveEventDesign() {}
        /**
         * Returns settings.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("settings") public LiveStudioSettings getSettings() { return settings; }
        /**
         * Sets settings.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("settings") public void setSettings(LiveStudioSettings value) { settings = value; markField("settings"); }
        /**
         * Returns revision.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("revision") public Long getRevision() { return revision; }
        /**
         * Sets revision.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("revision") public void setRevision(Long value) { revision = value; markField("revision"); }
        /**
         * Returns organization.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("organization") public String getOrganization() { return organization; }
        /**
         * Sets organization.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("organization") public void setOrganization(String value) { organization = value; markField("organization"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("settings")) fields.put("settings", settings);
            if (hasField("revision")) fields.put("revision", revision);
            if (hasField("organization")) fields.put("organization", organization);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "settings" -> { setSettings(Json.MAPPER.convertValue(value, new TypeReference<LiveStudioSettings>() {})); yield true; }
                case "revision" -> { setRevision(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "organization" -> { setOrganization(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "settings" -> settings = null;
                case "revision" -> revision = null;
                case "organization" -> organization = null;
                default -> { }
            }
        }
    }
    /**
     * Current authorized snapshot. Queue is null in presentation mode. Presentation data is immutable snapshot metadata; editable documents, permissions, secrets, publisher identities and media paths are excluded. No audience membership or viewer statistics are created by reading.
     */
    public static final class LiveEventSnapshot extends Model {
        private LiveEventSnapshotLive live;
        private LiveEventSnapshotDisplay display;
        private List<LiveQueueItem> queue;
        private LiveEventViewerSettings viewerSettings;
        private LiveEventDesign design;
        /**
         * Creates an object with all properties unset.
         */
        public LiveEventSnapshot() {}
        /**
         * Returns live.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("live") public LiveEventSnapshotLive getLive() { return live; }
        /**
         * Sets live.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("live") public void setLive(LiveEventSnapshotLive value) { live = value; markField("live"); }
        /**
         * Returns display.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("display") public LiveEventSnapshotDisplay getDisplay() { return display; }
        /**
         * Sets display.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("display") public void setDisplay(LiveEventSnapshotDisplay value) { display = value; markField("display"); }
        /**
         * Returns queue.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("queue") public List<LiveQueueItem> getQueue() { return queue; }
        /**
         * Sets queue.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("queue") public void setQueue(List<LiveQueueItem> value) { queue = value; markField("queue"); }
        /**
         * Returns viewer settings.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("viewer_settings") public LiveEventViewerSettings getViewerSettings() { return viewerSettings; }
        /**
         * Sets viewer settings.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("viewer_settings") public void setViewerSettings(LiveEventViewerSettings value) { viewerSettings = value; markField("viewer_settings"); }
        /**
         * Returns design.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("design") public LiveEventDesign getDesign() { return design; }
        /**
         * Sets design.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("design") public void setDesign(LiveEventDesign value) { design = value; markField("design"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("live")) fields.put("live", live);
            if (hasField("display")) fields.put("display", display);
            if (hasField("queue")) fields.put("queue", queue);
            if (hasField("viewer_settings")) fields.put("viewer_settings", viewerSettings);
            if (hasField("design")) fields.put("design", design);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "live" -> { setLive(Json.MAPPER.convertValue(value, new TypeReference<LiveEventSnapshotLive>() {})); yield true; }
                case "display" -> { setDisplay(Json.MAPPER.convertValue(value, new TypeReference<LiveEventSnapshotDisplay>() {})); yield true; }
                case "queue" -> { setQueue(Json.MAPPER.convertValue(value, new TypeReference<List<LiveQueueItem>>() {})); yield true; }
                case "viewer_settings" -> { setViewerSettings(Json.MAPPER.convertValue(value, new TypeReference<LiveEventViewerSettings>() {})); yield true; }
                case "design" -> { setDesign(Json.MAPPER.convertValue(value, new TypeReference<LiveEventDesign>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "live" -> live = null;
                case "display" -> display = null;
                case "queue" -> queue = null;
                case "viewer_settings" -> viewerSettings = null;
                case "design" -> design = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live snapshot event.
     */
    public static final class LiveSnapshotEvent extends Model {
        private Long schemaVersion;
        private String type;
        private String resourceType;
        private String resourceId;
        private Long sequence;
        private String cursor;
        private LiveEventSnapshot data;
        /**
         * Creates an object with all properties unset.
         */
        public LiveSnapshotEvent() {}
        /**
         * Returns schema version.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("schema_version") public Long getSchemaVersion() { return schemaVersion; }
        /**
         * Sets schema version.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("schema_version") public void setSchemaVersion(Long value) { schemaVersion = value; markField("schema_version"); }
        /**
         * Returns type.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("type") public String getType() { return type; }
        /**
         * Sets type.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("type") public void setType(String value) { type = value; markField("type"); }
        /**
         * Returns resource type.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("resource_type") public String getResourceType() { return resourceType; }
        /**
         * Sets resource type.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("resource_type") public void setResourceType(String value) { resourceType = value; markField("resource_type"); }
        /**
         * Returns resource id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("resource_id") public String getResourceId() { return resourceId; }
        /**
         * Sets resource id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("resource_id") public void setResourceId(String value) { resourceId = value; markField("resource_id"); }
        /**
         * Current Live content sequence. Studio design revision can change independently. Never use an event cursor as an If-Match validator.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("sequence") public Long getSequence() { return sequence; }
        /**
         * Sets sequence.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("sequence") public void setSequence(Long value) { sequence = value; markField("sequence"); }
        /**
         * Opaque current-state comparison token covering the snapshot including independent design revisions. No durable log position, event ID, expiry promise or universal ETag.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("cursor") public String getCursor() { return cursor; }
        /**
         * Sets cursor.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("cursor") public void setCursor(String value) { cursor = value; markField("cursor"); }
        /**
         * Returns data.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("data") public LiveEventSnapshot getData() { return data; }
        /**
         * Sets data.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("data") public void setData(LiveEventSnapshot value) { data = value; markField("data"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("schema_version")) fields.put("schema_version", schemaVersion);
            if (hasField("type")) fields.put("type", type);
            if (hasField("resource_type")) fields.put("resource_type", resourceType);
            if (hasField("resource_id")) fields.put("resource_id", resourceId);
            if (hasField("sequence")) fields.put("sequence", sequence);
            if (hasField("cursor")) fields.put("cursor", cursor);
            if (hasField("data")) fields.put("data", data);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "schema_version" -> { setSchemaVersion(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "type" -> { setType(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "resource_type" -> { setResourceType(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "resource_id" -> { setResourceId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "sequence" -> { setSequence(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "cursor" -> { setCursor(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "data" -> { setData(Json.MAPPER.convertValue(value, new TypeReference<LiveEventSnapshot>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "schema_version" -> schemaVersion = null;
                case "type" -> type = null;
                case "resource_type" -> resourceType = null;
                case "resource_id" -> resourceId = null;
                case "sequence" -> sequence = null;
                case "cursor" -> cursor = null;
                case "data" -> data = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live heartbeat event.
     */
    public static final class LiveHeartbeatEvent extends Model {
        private Long schemaVersion;
        private String type;
        private String resourceType;
        private String resourceId;
        private Long sequence;
        private String cursor;
        private Void data;
        /**
         * Creates an object with all properties unset.
         */
        public LiveHeartbeatEvent() {}
        /**
         * Returns schema version.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("schema_version") public Long getSchemaVersion() { return schemaVersion; }
        /**
         * Sets schema version.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("schema_version") public void setSchemaVersion(Long value) { schemaVersion = value; markField("schema_version"); }
        /**
         * Returns type.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("type") public String getType() { return type; }
        /**
         * Sets type.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("type") public void setType(String value) { type = value; markField("type"); }
        /**
         * Returns resource type.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("resource_type") public String getResourceType() { return resourceType; }
        /**
         * Sets resource type.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("resource_type") public void setResourceType(String value) { resourceType = value; markField("resource_type"); }
        /**
         * Returns resource id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("resource_id") public String getResourceId() { return resourceId; }
        /**
         * Sets resource id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("resource_id") public void setResourceId(String value) { resourceId = value; markField("resource_id"); }
        /**
         * Current Live content sequence. Studio design revision can change independently. Never use an event cursor as an If-Match validator.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("sequence") public Long getSequence() { return sequence; }
        /**
         * Sets sequence.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("sequence") public void setSequence(Long value) { sequence = value; markField("sequence"); }
        /**
         * Opaque current-state comparison token covering the snapshot including independent design revisions. No durable log position, event ID, expiry promise or universal ETag.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("cursor") public String getCursor() { return cursor; }
        /**
         * Sets cursor.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("cursor") public void setCursor(String value) { cursor = value; markField("cursor"); }
        /**
         * Returns data.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("data") public Void getData() { return data; }
        /**
         * Sets data.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("data") public void setData(Void value) { data = value; markField("data"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("schema_version")) fields.put("schema_version", schemaVersion);
            if (hasField("type")) fields.put("type", type);
            if (hasField("resource_type")) fields.put("resource_type", resourceType);
            if (hasField("resource_id")) fields.put("resource_id", resourceId);
            if (hasField("sequence")) fields.put("sequence", sequence);
            if (hasField("cursor")) fields.put("cursor", cursor);
            if (hasField("data")) fields.put("data", data);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "schema_version" -> { setSchemaVersion(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "type" -> { setType(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "resource_type" -> { setResourceType(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "resource_id" -> { setResourceId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "sequence" -> { setSequence(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "cursor" -> { setCursor(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "data" -> { setData(Json.MAPPER.convertValue(value, new TypeReference<Void>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "schema_version" -> schemaVersion = null;
                case "type" -> type = null;
                case "resource_type" -> resourceType = null;
                case "resource_id" -> resourceId = null;
                case "sequence" -> sequence = null;
                case "cursor" -> cursor = null;
                case "data" -> data = null;
                default -> { }
            }
        }
    }
    /**
     * Event schema major version 1. Snapshot on connection/reconnection; changed snapshot when comparison differs; null-data heartbeat at wait expiry. Notifications may coalesce, repeat or be missed. Not exactly-once and no historical replay. Known variant fields share one wire object; the server validates variant-specific requirements.
     */
    public static final class LiveEvent extends Model {
        private Long schemaVersion;
        private String type;
        private String resourceType;
        private String resourceId;
        private Long sequence;
        private String cursor;
        private LiveEventSnapshot data;
        /**
         * Creates an object with all properties unset.
         */
        public LiveEvent() {}
        /**
         * Returns schema version.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("schema_version") public Long getSchemaVersion() { return schemaVersion; }
        /**
         * Sets schema version.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("schema_version") public void setSchemaVersion(Long value) { schemaVersion = value; markField("schema_version"); }
        /**
         * Returns type.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("type") public String getType() { return type; }
        /**
         * Sets type.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("type") public void setType(String value) { type = value; markField("type"); }
        /**
         * Returns resource type.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("resource_type") public String getResourceType() { return resourceType; }
        /**
         * Sets resource type.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("resource_type") public void setResourceType(String value) { resourceType = value; markField("resource_type"); }
        /**
         * Returns resource id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("resource_id") public String getResourceId() { return resourceId; }
        /**
         * Sets resource id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("resource_id") public void setResourceId(String value) { resourceId = value; markField("resource_id"); }
        /**
         * Current Live content sequence. Studio design revision can change independently. Never use an event cursor as an If-Match validator.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("sequence") public Long getSequence() { return sequence; }
        /**
         * Sets sequence.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("sequence") public void setSequence(Long value) { sequence = value; markField("sequence"); }
        /**
         * Opaque current-state comparison token covering the snapshot including independent design revisions. No durable log position, event ID, expiry promise or universal ETag.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("cursor") public String getCursor() { return cursor; }
        /**
         * Sets cursor.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("cursor") public void setCursor(String value) { cursor = value; markField("cursor"); }
        /**
         * Returns data.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("data") public LiveEventSnapshot getData() { return data; }
        /**
         * Sets data.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("data") public void setData(LiveEventSnapshot value) { data = value; markField("data"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("schema_version")) fields.put("schema_version", schemaVersion);
            if (hasField("type")) fields.put("type", type);
            if (hasField("resource_type")) fields.put("resource_type", resourceType);
            if (hasField("resource_id")) fields.put("resource_id", resourceId);
            if (hasField("sequence")) fields.put("sequence", sequence);
            if (hasField("cursor")) fields.put("cursor", cursor);
            if (hasField("data")) fields.put("data", data);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "schema_version" -> { setSchemaVersion(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "type" -> { setType(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "resource_type" -> { setResourceType(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "resource_id" -> { setResourceId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "sequence" -> { setSequence(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "cursor" -> { setCursor(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "data" -> { setData(Json.MAPPER.convertValue(value, new TypeReference<LiveEventSnapshot>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "schema_version" -> schemaVersion = null;
                case "type" -> type = null;
                case "resource_type" -> resourceType = null;
                case "resource_id" -> resourceId = null;
                case "sequence" -> sequence = null;
                case "cursor" -> cursor = null;
                case "data" -> data = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of account quota.
     */
    public static final class AccountQuota extends Model {
        private String resource;
        private String action;
        private Long limit;
        private Long used;
        /**
         * Creates an object with all properties unset.
         */
        public AccountQuota() {}
        /**
         * Returns resource.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("resource") public String getResource() { return resource; }
        /**
         * Sets resource.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("resource") public void setResource(String value) { resource = value; markField("resource"); }
        /**
         * Returns action.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("action") public String getAction() { return action; }
        /**
         * Sets action.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("action") public void setAction(String value) { action = value; markField("action"); }
        /**
         * Returns limit.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("limit") public Long getLimit() { return limit; }
        /**
         * Sets limit.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("limit") public void setLimit(Long value) { limit = value; markField("limit"); }
        /**
         * Returns used.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("used") public Long getUsed() { return used; }
        /**
         * Sets used.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("used") public void setUsed(Long value) { used = value; markField("used"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("resource")) fields.put("resource", resource);
            if (hasField("action")) fields.put("action", action);
            if (hasField("limit")) fields.put("limit", limit);
            if (hasField("used")) fields.put("used", used);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "resource" -> { setResource(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "action" -> { setAction(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "limit" -> { setLimit(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "used" -> { setUsed(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "resource" -> resource = null;
                case "action" -> action = null;
                case "limit" -> limit = null;
                case "used" -> used = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of account quota collection.
     */
    public static final class AccountQuotaCollection extends Model {
        private List<AccountQuota> data;
        /**
         * Creates an object with all properties unset.
         */
        public AccountQuotaCollection() {}
        /**
         * Returns data.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("data") public List<AccountQuota> getData() { return data; }
        /**
         * Sets data.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("data") public void setData(List<AccountQuota> value) { data = value; markField("data"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("data")) fields.put("data", data);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "data" -> { setData(Json.MAPPER.convertValue(value, new TypeReference<List<AccountQuota>>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "data" -> data = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of personal token metadata.
     */
    public static final class PersonalTokenMetadata extends Model {
        private String id;
        private String label;
        private String source;
        private List<String> scopes;
        private Long issuedAt;
        private Long expiresAt;
        private Long lastUsedAt;
        private Boolean retiring;
        /**
         * Creates an object with all properties unset.
         */
        public PersonalTokenMetadata() {}
        /**
         * Returns id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("id") public String getId() { return id; }
        /**
         * Sets id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("id") public void setId(String value) { id = value; markField("id"); }
        /**
         * Returns label.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("label") public String getLabel() { return label; }
        /**
         * Sets label.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("label") public void setLabel(String value) { label = value; markField("label"); }
        /**
         * Returns source.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("source") public String getSource() { return source; }
        /**
         * Sets source.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("source") public void setSource(String value) { source = value; markField("source"); }
        /**
         * Returns scopes.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("scopes") public List<String> getScopes() { return scopes; }
        /**
         * Sets scopes.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("scopes") public void setScopes(List<String> value) { scopes = value; markField("scopes"); }
        /**
         * Returns issued at.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("issued_at") public Long getIssuedAt() { return issuedAt; }
        /**
         * Sets issued at.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("issued_at") public void setIssuedAt(Long value) { issuedAt = value; markField("issued_at"); }
        /**
         * Returns expires at.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("expires_at") public Long getExpiresAt() { return expiresAt; }
        /**
         * Sets expires at.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("expires_at") public void setExpiresAt(Long value) { expiresAt = value; markField("expires_at"); }
        /**
         * Returns last used at.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("last_used_at") public Long getLastUsedAt() { return lastUsedAt; }
        /**
         * Sets last used at.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("last_used_at") public void setLastUsedAt(Long value) { lastUsedAt = value; markField("last_used_at"); }
        /**
         * Returns retiring.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("retiring") public Boolean getRetiring() { return retiring; }
        /**
         * Sets retiring.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("retiring") public void setRetiring(Boolean value) { retiring = value; markField("retiring"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("id")) fields.put("id", id);
            if (hasField("label")) fields.put("label", label);
            if (hasField("source")) fields.put("source", source);
            if (hasField("scopes")) fields.put("scopes", scopes);
            if (hasField("issued_at")) fields.put("issued_at", issuedAt);
            if (hasField("expires_at")) fields.put("expires_at", expiresAt);
            if (hasField("last_used_at")) fields.put("last_used_at", lastUsedAt);
            if (hasField("retiring")) fields.put("retiring", retiring);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "id" -> { setId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "label" -> { setLabel(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "source" -> { setSource(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "scopes" -> { setScopes(Json.MAPPER.convertValue(value, new TypeReference<List<String>>() {})); yield true; }
                case "issued_at" -> { setIssuedAt(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "expires_at" -> { setExpiresAt(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "last_used_at" -> { setLastUsedAt(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "retiring" -> { setRetiring(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "id" -> id = null;
                case "label" -> label = null;
                case "source" -> source = null;
                case "scopes" -> scopes = null;
                case "issued_at" -> issuedAt = null;
                case "expires_at" -> expiresAt = null;
                case "last_used_at" -> lastUsedAt = null;
                case "retiring" -> retiring = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of personal token collection.
     */
    public static final class PersonalTokenCollection extends Model {
        private List<PersonalTokenMetadata> data;
        private String nextCursor;
        /**
         * Creates an object with all properties unset.
         */
        public PersonalTokenCollection() {}
        /**
         * Returns data.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("data") public List<PersonalTokenMetadata> getData() { return data; }
        /**
         * Sets data.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("data") public void setData(List<PersonalTokenMetadata> value) { data = value; markField("data"); }
        /**
         * Returns next cursor.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("next_cursor") public String getNextCursor() { return nextCursor; }
        /**
         * Sets next cursor.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("next_cursor") public void setNextCursor(String value) { nextCursor = value; markField("next_cursor"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("data")) fields.put("data", data);
            if (hasField("next_cursor")) fields.put("next_cursor", nextCursor);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "data" -> { setData(Json.MAPPER.convertValue(value, new TypeReference<List<PersonalTokenMetadata>>() {})); yield true; }
                case "next_cursor" -> { setNextCursor(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "data" -> data = null;
                case "next_cursor" -> nextCursor = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of personal directory entry.
     */
    public static final class PersonalDirectoryEntry extends Model {
        private String recordValue;
        private String id;
        private String name;
        private String handle;
        private String role;
        private String verification;
        private String status;
        private String applicationId;
        private String domain;
        private String state;
        private String organizationId;
        private String verificationUrl;
        private String nextAction;
        /**
         * Creates an object with all properties unset.
         */
        public PersonalDirectoryEntry() {}
        /**
         * Returns record.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("record") public String getRecordValue() { return recordValue; }
        /**
         * Sets record.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("record") public void setRecordValue(String value) { recordValue = value; markField("record"); }
        /**
         * Returns id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("id") public String getId() { return id; }
        /**
         * Sets id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("id") public void setId(String value) { id = value; markField("id"); }
        /**
         * Returns name.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("name") public String getName() { return name; }
        /**
         * Sets name.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("name") public void setName(String value) { name = value; markField("name"); }
        /**
         * Returns handle.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("handle") public String getHandle() { return handle; }
        /**
         * Sets handle.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("handle") public void setHandle(String value) { handle = value; markField("handle"); }
        /**
         * Returns role.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("role") public String getRole() { return role; }
        /**
         * Sets role.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("role") public void setRole(String value) { role = value; markField("role"); }
        /**
         * Returns verification.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("verification") public String getVerification() { return verification; }
        /**
         * Sets verification.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("verification") public void setVerification(String value) { verification = value; markField("verification"); }
        /**
         * Returns status.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("status") public String getStatus() { return status; }
        /**
         * Sets status.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("status") public void setStatus(String value) { status = value; markField("status"); }
        /**
         * Returns application id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("application_id") public String getApplicationId() { return applicationId; }
        /**
         * Sets application id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("application_id") public void setApplicationId(String value) { applicationId = value; markField("application_id"); }
        /**
         * Returns domain.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("domain") public String getDomain() { return domain; }
        /**
         * Sets domain.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("domain") public void setDomain(String value) { domain = value; markField("domain"); }
        /**
         * Returns state.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("state") public String getState() { return state; }
        /**
         * Sets state.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("state") public void setState(String value) { state = value; markField("state"); }
        /**
         * Returns organization id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("organization_id") public String getOrganizationId() { return organizationId; }
        /**
         * Sets organization id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("organization_id") public void setOrganizationId(String value) { organizationId = value; markField("organization_id"); }
        /**
         * Returns verification url.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("verification_url") public String getVerificationUrl() { return verificationUrl; }
        /**
         * Sets verification url.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("verification_url") public void setVerificationUrl(String value) { verificationUrl = value; markField("verification_url"); }
        /**
         * Returns next action.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("next_action") public String getNextAction() { return nextAction; }
        /**
         * Sets next action.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("next_action") public void setNextAction(String value) { nextAction = value; markField("next_action"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("record")) fields.put("record", recordValue);
            if (hasField("id")) fields.put("id", id);
            if (hasField("name")) fields.put("name", name);
            if (hasField("handle")) fields.put("handle", handle);
            if (hasField("role")) fields.put("role", role);
            if (hasField("verification")) fields.put("verification", verification);
            if (hasField("status")) fields.put("status", status);
            if (hasField("application_id")) fields.put("application_id", applicationId);
            if (hasField("domain")) fields.put("domain", domain);
            if (hasField("state")) fields.put("state", state);
            if (hasField("organization_id")) fields.put("organization_id", organizationId);
            if (hasField("verification_url")) fields.put("verification_url", verificationUrl);
            if (hasField("next_action")) fields.put("next_action", nextAction);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "record" -> { setRecordValue(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "id" -> { setId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "name" -> { setName(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "handle" -> { setHandle(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "role" -> { setRole(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "verification" -> { setVerification(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "status" -> { setStatus(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "application_id" -> { setApplicationId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "domain" -> { setDomain(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "state" -> { setState(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "organization_id" -> { setOrganizationId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "verification_url" -> { setVerificationUrl(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "next_action" -> { setNextAction(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "record" -> recordValue = null;
                case "id" -> id = null;
                case "name" -> name = null;
                case "handle" -> handle = null;
                case "role" -> role = null;
                case "verification" -> verification = null;
                case "status" -> status = null;
                case "application_id" -> applicationId = null;
                case "domain" -> domain = null;
                case "state" -> state = null;
                case "organization_id" -> organizationId = null;
                case "verification_url" -> verificationUrl = null;
                case "next_action" -> nextAction = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of personal directory.
     */
    public static final class PersonalDirectory extends Model {
        private List<PersonalDirectoryEntry> data;
        private String nextCursor;
        /**
         * Creates an object with all properties unset.
         */
        public PersonalDirectory() {}
        /**
         * Returns data.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("data") public List<PersonalDirectoryEntry> getData() { return data; }
        /**
         * Sets data.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("data") public void setData(List<PersonalDirectoryEntry> value) { data = value; markField("data"); }
        /**
         * Returns next cursor.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("next_cursor") public String getNextCursor() { return nextCursor; }
        /**
         * Sets next cursor.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("next_cursor") public void setNextCursor(String value) { nextCursor = value; markField("next_cursor"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("data")) fields.put("data", data);
            if (hasField("next_cursor")) fields.put("next_cursor", nextCursor);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "data" -> { setData(Json.MAPPER.convertValue(value, new TypeReference<List<PersonalDirectoryEntry>>() {})); yield true; }
                case "next_cursor" -> { setNextCursor(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "data" -> data = null;
                case "next_cursor" -> nextCursor = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of personal organization application.
     */
    public static final class PersonalOrganizationApplication extends Model {
        private String applicationId;
        private String name;
        private String domain;
        private String state;
        private String organizationId;
        private String verificationUrl;
        private String nextAction;
        /**
         * Creates an object with all properties unset.
         */
        public PersonalOrganizationApplication() {}
        /**
         * Returns application id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("application_id") public String getApplicationId() { return applicationId; }
        /**
         * Sets application id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("application_id") public void setApplicationId(String value) { applicationId = value; markField("application_id"); }
        /**
         * Returns name.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("name") public String getName() { return name; }
        /**
         * Sets name.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("name") public void setName(String value) { name = value; markField("name"); }
        /**
         * Returns domain.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("domain") public String getDomain() { return domain; }
        /**
         * Sets domain.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("domain") public void setDomain(String value) { domain = value; markField("domain"); }
        /**
         * Returns state.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("state") public String getState() { return state; }
        /**
         * Sets state.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("state") public void setState(String value) { state = value; markField("state"); }
        /**
         * Returns organization id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("organization_id") public String getOrganizationId() { return organizationId; }
        /**
         * Sets organization id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("organization_id") public void setOrganizationId(String value) { organizationId = value; markField("organization_id"); }
        /**
         * Returns verification url.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("verification_url") public String getVerificationUrl() { return verificationUrl; }
        /**
         * Sets verification url.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("verification_url") public void setVerificationUrl(String value) { verificationUrl = value; markField("verification_url"); }
        /**
         * Returns next action.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("next_action") public String getNextAction() { return nextAction; }
        /**
         * Sets next action.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("next_action") public void setNextAction(String value) { nextAction = value; markField("next_action"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("application_id")) fields.put("application_id", applicationId);
            if (hasField("name")) fields.put("name", name);
            if (hasField("domain")) fields.put("domain", domain);
            if (hasField("state")) fields.put("state", state);
            if (hasField("organization_id")) fields.put("organization_id", organizationId);
            if (hasField("verification_url")) fields.put("verification_url", verificationUrl);
            if (hasField("next_action")) fields.put("next_action", nextAction);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "application_id" -> { setApplicationId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "name" -> { setName(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "domain" -> { setDomain(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "state" -> { setState(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "organization_id" -> { setOrganizationId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "verification_url" -> { setVerificationUrl(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "next_action" -> { setNextAction(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "application_id" -> applicationId = null;
                case "name" -> name = null;
                case "domain" -> domain = null;
                case "state" -> state = null;
                case "organization_id" -> organizationId = null;
                case "verification_url" -> verificationUrl = null;
                case "next_action" -> nextAction = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of translation chapter metadata.
     */
    public static final class TranslationChapterMetadata extends Model {
        private Long chapter;
        private Long verses;
        /**
         * Creates an object with all properties unset.
         */
        public TranslationChapterMetadata() {}
        /**
         * Returns chapter.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("chapter") public Long getChapter() { return chapter; }
        /**
         * Sets chapter.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("chapter") public void setChapter(Long value) { chapter = value; markField("chapter"); }
        /**
         * Returns verses.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("verses") public Long getVerses() { return verses; }
        /**
         * Sets verses.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("verses") public void setVerses(Long value) { verses = value; markField("verses"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("chapter")) fields.put("chapter", chapter);
            if (hasField("verses")) fields.put("verses", verses);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "chapter" -> { setChapter(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "verses" -> { setVerses(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "chapter" -> chapter = null;
                case "verses" -> verses = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of translation book metadata.
     */
    public static final class TranslationBookMetadata extends Model {
        private Long book;
        private String name;
        private List<TranslationChapterMetadata> chapters;
        /**
         * Creates an object with all properties unset.
         */
        public TranslationBookMetadata() {}
        /**
         * Returns book.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("book") public Long getBook() { return book; }
        /**
         * Sets book.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("book") public void setBook(Long value) { book = value; markField("book"); }
        /**
         * Returns name.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("name") public String getName() { return name; }
        /**
         * Sets name.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("name") public void setName(String value) { name = value; markField("name"); }
        /**
         * Returns chapters.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("chapters") public List<TranslationChapterMetadata> getChapters() { return chapters; }
        /**
         * Sets chapters.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("chapters") public void setChapters(List<TranslationChapterMetadata> value) { chapters = value; markField("chapters"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("book")) fields.put("book", book);
            if (hasField("name")) fields.put("name", name);
            if (hasField("chapters")) fields.put("chapters", chapters);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "book" -> { setBook(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "name" -> { setName(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "chapters" -> { setChapters(Json.MAPPER.convertValue(value, new TypeReference<List<TranslationChapterMetadata>>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "book" -> book = null;
                case "name" -> name = null;
                case "chapters" -> chapters = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of translation catalogue details.
     */
    public static final class TranslationCatalogueDetails extends Model {
        private String translation;
        private List<TranslationBookMetadata> data;
        /**
         * Creates an object with all properties unset.
         */
        public TranslationCatalogueDetails() {}
        /**
         * Returns translation.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("translation") public String getTranslation() { return translation; }
        /**
         * Sets translation.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("translation") public void setTranslation(String value) { translation = value; markField("translation"); }
        /**
         * Returns data.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("data") public List<TranslationBookMetadata> getData() { return data; }
        /**
         * Sets data.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("data") public void setData(List<TranslationBookMetadata> value) { data = value; markField("data"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("translation")) fields.put("translation", translation);
            if (hasField("data")) fields.put("data", data);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "translation" -> { setTranslation(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "data" -> { setData(Json.MAPPER.convertValue(value, new TypeReference<List<TranslationBookMetadata>>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "translation" -> translation = null;
                case "data" -> data = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live stack reference.
     */
    public static final class LiveStackReference extends Model {
        private String translation;
        private Long bookId;
        private Long chapter;
        private Long verse;
        /**
         * Creates an object with all properties unset.
         */
        public LiveStackReference() {}
        /**
         * Returns translation.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("translation") public String getTranslation() { return translation; }
        /**
         * Sets translation.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("translation") public void setTranslation(String value) { translation = value; markField("translation"); }
        /**
         * Returns book id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("book_id") public Long getBookId() { return bookId; }
        /**
         * Sets book id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("book_id") public void setBookId(Long value) { bookId = value; markField("book_id"); }
        /**
         * Returns chapter.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("chapter") public Long getChapter() { return chapter; }
        /**
         * Sets chapter.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("chapter") public void setChapter(Long value) { chapter = value; markField("chapter"); }
        /**
         * Returns verse.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("verse") public Long getVerse() { return verse; }
        /**
         * Sets verse.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("verse") public void setVerse(Long value) { verse = value; markField("verse"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("translation")) fields.put("translation", translation);
            if (hasField("book_id")) fields.put("book_id", bookId);
            if (hasField("chapter")) fields.put("chapter", chapter);
            if (hasField("verse")) fields.put("verse", verse);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "translation" -> { setTranslation(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "book_id" -> { setBookId(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "chapter" -> { setChapter(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "verse" -> { setVerse(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "translation" -> translation = null;
                case "book_id" -> bookId = null;
                case "chapter" -> chapter = null;
                case "verse" -> verse = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live stack push.
     */
    public static final class LiveStackPush extends Model {
        private LiveStackReference reference;
        /**
         * Creates an object with all properties unset.
         */
        public LiveStackPush() {}
        /**
         * Returns reference.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("reference") public LiveStackReference getReference() { return reference; }
        /**
         * Sets reference.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("reference") public void setReference(LiveStackReference value) { reference = value; markField("reference"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("reference")) fields.put("reference", reference);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "reference" -> { setReference(Json.MAPPER.convertValue(value, new TypeReference<LiveStackReference>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "reference" -> reference = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live stack removal.
     */
    public static final class LiveStackRemoval extends Model {
        private Long count;
        /**
         * Creates an object with all properties unset.
         */
        public LiveStackRemoval() {}
        /**
         * Returns count.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("count") public Long getCount() { return count; }
        /**
         * Sets count.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("count") public void setCount(Long value) { count = value; markField("count"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("count")) fields.put("count", count);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "count" -> { setCount(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "count" -> count = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live stack verse.
     */
    public static final class LiveStackVerse extends Model {
        private Long bookId;
        private String book;
        private Long chapter;
        private Long verse;
        private String text;
        private String translation;
        private TranslationRights rights;
        private String attribution;
        private Long position;
        /**
         * Creates an object with all properties unset.
         */
        public LiveStackVerse() {}
        /**
         * Returns book id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("book_id") public Long getBookId() { return bookId; }
        /**
         * Sets book id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("book_id") public void setBookId(Long value) { bookId = value; markField("book_id"); }
        /**
         * Returns book.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("book") public String getBook() { return book; }
        /**
         * Sets book.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("book") public void setBook(String value) { book = value; markField("book"); }
        /**
         * Returns chapter.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("chapter") public Long getChapter() { return chapter; }
        /**
         * Sets chapter.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("chapter") public void setChapter(Long value) { chapter = value; markField("chapter"); }
        /**
         * Returns verse.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("verse") public Long getVerse() { return verse; }
        /**
         * Sets verse.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("verse") public void setVerse(Long value) { verse = value; markField("verse"); }
        /**
         * Returns text.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("text") public String getText() { return text; }
        /**
         * Sets text.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("text") public void setText(String value) { text = value; markField("text"); }
        /**
         * Returns translation.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("translation") public String getTranslation() { return translation; }
        /**
         * Sets translation.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("translation") public void setTranslation(String value) { translation = value; markField("translation"); }
        /**
         * Returns rights.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("rights") public TranslationRights getRights() { return rights; }
        /**
         * Sets rights.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("rights") public void setRights(TranslationRights value) { rights = value; markField("rights"); }
        /**
         * Returns attribution.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("attribution") public String getAttribution() { return attribution; }
        /**
         * Sets attribution.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("attribution") public void setAttribution(String value) { attribution = value; markField("attribution"); }
        /**
         * Returns position.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("position") public Long getPosition() { return position; }
        /**
         * Sets position.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("position") public void setPosition(Long value) { position = value; markField("position"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("book_id")) fields.put("book_id", bookId);
            if (hasField("book")) fields.put("book", book);
            if (hasField("chapter")) fields.put("chapter", chapter);
            if (hasField("verse")) fields.put("verse", verse);
            if (hasField("text")) fields.put("text", text);
            if (hasField("translation")) fields.put("translation", translation);
            if (hasField("rights")) fields.put("rights", rights);
            if (hasField("attribution")) fields.put("attribution", attribution);
            if (hasField("position")) fields.put("position", position);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "book_id" -> { setBookId(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "book" -> { setBook(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "chapter" -> { setChapter(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "verse" -> { setVerse(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "text" -> { setText(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "translation" -> { setTranslation(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "rights" -> { setRights(Json.MAPPER.convertValue(value, new TypeReference<TranslationRights>() {})); yield true; }
                case "attribution" -> { setAttribution(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "position" -> { setPosition(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "book_id" -> bookId = null;
                case "book" -> book = null;
                case "chapter" -> chapter = null;
                case "verse" -> verse = null;
                case "text" -> text = null;
                case "translation" -> translation = null;
                case "rights" -> rights = null;
                case "attribution" -> attribution = null;
                case "position" -> position = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live stack.
     */
    public static final class LiveStack extends Model {
        private String liveId;
        private Long sequence;
        private Long affected;
        private List<LiveStackVerse> data;
        /**
         * Creates an object with all properties unset.
         */
        public LiveStack() {}
        /**
         * Returns live id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("live_id") public String getLiveId() { return liveId; }
        /**
         * Sets live id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("live_id") public void setLiveId(String value) { liveId = value; markField("live_id"); }
        /**
         * Returns sequence.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("sequence") public Long getSequence() { return sequence; }
        /**
         * Sets sequence.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("sequence") public void setSequence(Long value) { sequence = value; markField("sequence"); }
        /**
         * Returns affected.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("affected") public Long getAffected() { return affected; }
        /**
         * Sets affected.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("affected") public void setAffected(Long value) { affected = value; markField("affected"); }
        /**
         * Returns data.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("data") public List<LiveStackVerse> getData() { return data; }
        /**
         * Sets data.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("data") public void setData(List<LiveStackVerse> value) { data = value; markField("data"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("live_id")) fields.put("live_id", liveId);
            if (hasField("sequence")) fields.put("sequence", sequence);
            if (hasField("affected")) fields.put("affected", affected);
            if (hasField("data")) fields.put("data", data);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "live_id" -> { setLiveId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "sequence" -> { setSequence(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "affected" -> { setAffected(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "data" -> { setData(Json.MAPPER.convertValue(value, new TypeReference<List<LiveStackVerse>>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "live_id" -> liveId = null;
                case "sequence" -> sequence = null;
                case "affected" -> affected = null;
                case "data" -> data = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live settings.
     */
    public static final class LiveSettings extends Model {
        private String liveId;
        private Long sequence;
        private String reference;
        private List<String> translations;
        /**
         * Creates an object with all properties unset.
         */
        public LiveSettings() {}
        /**
         * Returns live id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("live_id") public String getLiveId() { return liveId; }
        /**
         * Sets live id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("live_id") public void setLiveId(String value) { liveId = value; markField("live_id"); }
        /**
         * Returns sequence.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("sequence") public Long getSequence() { return sequence; }
        /**
         * Sets sequence.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("sequence") public void setSequence(Long value) { sequence = value; markField("sequence"); }
        /**
         * Returns reference.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("reference") public String getReference() { return reference; }
        /**
         * Sets reference.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("reference") public void setReference(String value) { reference = value; markField("reference"); }
        /**
         * Returns translations.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("translations") public List<String> getTranslations() { return translations; }
        /**
         * Sets translations.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("translations") public void setTranslations(List<String> value) { translations = value; markField("translations"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("live_id")) fields.put("live_id", liveId);
            if (hasField("sequence")) fields.put("sequence", sequence);
            if (hasField("reference")) fields.put("reference", reference);
            if (hasField("translations")) fields.put("translations", translations);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "live_id" -> { setLiveId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "sequence" -> { setSequence(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "reference" -> { setReference(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "translations" -> { setTranslations(Json.MAPPER.convertValue(value, new TypeReference<List<String>>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "live_id" -> liveId = null;
                case "sequence" -> sequence = null;
                case "reference" -> reference = null;
                case "translations" -> translations = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live settings patch.
     */
    public static final class LiveSettingsPatch extends Model {
        private String reference;
        private List<String> translations;
        /**
         * Creates an object with all properties unset.
         */
        public LiveSettingsPatch() {}
        /**
         * Returns reference.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("reference") public String getReference() { return reference; }
        /**
         * Sets reference.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("reference") public void setReference(String value) { reference = value; markField("reference"); }
        /**
         * Returns translations.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("translations") public List<String> getTranslations() { return translations; }
        /**
         * Sets translations.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("translations") public void setTranslations(List<String> value) { translations = value; markField("translations"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("reference")) fields.put("reference", reference);
            if (hasField("translations")) fields.put("translations", translations);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "reference" -> { setReference(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "translations" -> { setTranslations(Json.MAPPER.convertValue(value, new TypeReference<List<String>>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "reference" -> reference = null;
                case "translations" -> translations = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live subscriber.
     */
    public static final class LiveSubscriber extends Model {
        private String user;
        private String access;
        private Long connectedAt;
        private Long connectedForSeconds;
        /**
         * Creates an object with all properties unset.
         */
        public LiveSubscriber() {}
        /**
         * Returns user.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("user") public String getUser() { return user; }
        /**
         * Sets user.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("user") public void setUser(String value) { user = value; markField("user"); }
        /**
         * Returns access.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("access") public String getAccess() { return access; }
        /**
         * Sets access.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("access") public void setAccess(String value) { access = value; markField("access"); }
        /**
         * Returns connected at.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("connected_at") public Long getConnectedAt() { return connectedAt; }
        /**
         * Sets connected at.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("connected_at") public void setConnectedAt(Long value) { connectedAt = value; markField("connected_at"); }
        /**
         * Returns connected for seconds.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("connected_for_seconds") public Long getConnectedForSeconds() { return connectedForSeconds; }
        /**
         * Sets connected for seconds.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("connected_for_seconds") public void setConnectedForSeconds(Long value) { connectedForSeconds = value; markField("connected_for_seconds"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("user")) fields.put("user", user);
            if (hasField("access")) fields.put("access", access);
            if (hasField("connected_at")) fields.put("connected_at", connectedAt);
            if (hasField("connected_for_seconds")) fields.put("connected_for_seconds", connectedForSeconds);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "user" -> { setUser(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "access" -> { setAccess(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "connected_at" -> { setConnectedAt(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "connected_for_seconds" -> { setConnectedForSeconds(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "user" -> user = null;
                case "access" -> access = null;
                case "connected_at" -> connectedAt = null;
                case "connected_for_seconds" -> connectedForSeconds = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live statistics.
     */
    public static final class LiveStatistics extends Model {
        private String id;
        private Long sequence;
        private Long runningForSeconds;
        private Long stackEntries;
        private Long connections;
        private Long userConnections;
        private Long anonymousConnections;
        private List<LiveSubscriber> subscribers;
        /**
         * Creates an object with all properties unset.
         */
        public LiveStatistics() {}
        /**
         * Returns id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("id") public String getId() { return id; }
        /**
         * Sets id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("id") public void setId(String value) { id = value; markField("id"); }
        /**
         * Returns sequence.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("sequence") public Long getSequence() { return sequence; }
        /**
         * Sets sequence.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("sequence") public void setSequence(Long value) { sequence = value; markField("sequence"); }
        /**
         * Returns running for seconds.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("running_for_seconds") public Long getRunningForSeconds() { return runningForSeconds; }
        /**
         * Sets running for seconds.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("running_for_seconds") public void setRunningForSeconds(Long value) { runningForSeconds = value; markField("running_for_seconds"); }
        /**
         * Returns stack entries.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("stack_entries") public Long getStackEntries() { return stackEntries; }
        /**
         * Sets stack entries.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("stack_entries") public void setStackEntries(Long value) { stackEntries = value; markField("stack_entries"); }
        /**
         * Returns connections.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("connections") public Long getConnections() { return connections; }
        /**
         * Sets connections.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("connections") public void setConnections(Long value) { connections = value; markField("connections"); }
        /**
         * Returns user connections.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("user_connections") public Long getUserConnections() { return userConnections; }
        /**
         * Sets user connections.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("user_connections") public void setUserConnections(Long value) { userConnections = value; markField("user_connections"); }
        /**
         * Returns anonymous connections.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("anonymous_connections") public Long getAnonymousConnections() { return anonymousConnections; }
        /**
         * Sets anonymous connections.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("anonymous_connections") public void setAnonymousConnections(Long value) { anonymousConnections = value; markField("anonymous_connections"); }
        /**
         * Returns subscribers.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("subscribers") public List<LiveSubscriber> getSubscribers() { return subscribers; }
        /**
         * Sets subscribers.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("subscribers") public void setSubscribers(List<LiveSubscriber> value) { subscribers = value; markField("subscribers"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("id")) fields.put("id", id);
            if (hasField("sequence")) fields.put("sequence", sequence);
            if (hasField("running_for_seconds")) fields.put("running_for_seconds", runningForSeconds);
            if (hasField("stack_entries")) fields.put("stack_entries", stackEntries);
            if (hasField("connections")) fields.put("connections", connections);
            if (hasField("user_connections")) fields.put("user_connections", userConnections);
            if (hasField("anonymous_connections")) fields.put("anonymous_connections", anonymousConnections);
            if (hasField("subscribers")) fields.put("subscribers", subscribers);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "id" -> { setId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "sequence" -> { setSequence(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "running_for_seconds" -> { setRunningForSeconds(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "stack_entries" -> { setStackEntries(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "connections" -> { setConnections(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "user_connections" -> { setUserConnections(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "anonymous_connections" -> { setAnonymousConnections(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "subscribers" -> { setSubscribers(Json.MAPPER.convertValue(value, new TypeReference<List<LiveSubscriber>>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "id" -> id = null;
                case "sequence" -> sequence = null;
                case "running_for_seconds" -> runningForSeconds = null;
                case "stack_entries" -> stackEntries = null;
                case "connections" -> connections = null;
                case "user_connections" -> userConnections = null;
                case "anonymous_connections" -> anonymousConnections = null;
                case "subscribers" -> subscribers = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live audience secret.
     */
    public static final class LiveAudienceSecret extends Model {
        private String liveId;
        private Long sequence;
        private Boolean protectedValue;
        private String secret;
        /**
         * Creates an object with all properties unset.
         */
        public LiveAudienceSecret() {}
        /**
         * Returns live id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("live_id") public String getLiveId() { return liveId; }
        /**
         * Sets live id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("live_id") public void setLiveId(String value) { liveId = value; markField("live_id"); }
        /**
         * Returns sequence.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("sequence") public Long getSequence() { return sequence; }
        /**
         * Sets sequence.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("sequence") public void setSequence(Long value) { sequence = value; markField("sequence"); }
        /**
         * Returns protected.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("protected") public Boolean getProtectedValue() { return protectedValue; }
        /**
         * Sets protected.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("protected") public void setProtectedValue(Boolean value) { protectedValue = value; markField("protected"); }
        /**
         * Returns secret.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("secret") public String getSecret() { return secret; }
        /**
         * Sets secret.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("secret") public void setSecret(String value) { secret = value; markField("secret"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("live_id")) fields.put("live_id", liveId);
            if (hasField("sequence")) fields.put("sequence", sequence);
            if (hasField("protected")) fields.put("protected", protectedValue);
            if (hasField("secret")) fields.put("secret", secret);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "live_id" -> { setLiveId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "sequence" -> { setSequence(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "protected" -> { setProtectedValue(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "secret" -> { setSecret(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "live_id" -> liveId = null;
                case "sequence" -> sequence = null;
                case "protected" -> protectedValue = null;
                case "secret" -> secret = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live bulk deletion.
     */
    public static final class LiveBulkDeletion extends Model {
        private Long deleted;
        /**
         * Creates an object with all properties unset.
         */
        public LiveBulkDeletion() {}
        /**
         * Returns deleted.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("deleted") public Long getDeleted() { return deleted; }
        /**
         * Sets deleted.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("deleted") public void setDeleted(Long value) { deleted = value; markField("deleted"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("deleted")) fields.put("deleted", deleted);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "deleted" -> { setDeleted(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "deleted" -> deleted = null;
                default -> { }
            }
        }
    }
    /**
     * One bounded book page, always paginated: default 50, maximum 500 verses, and at most 3MiB serialized JSON including metadata. The byte budget can return fewer verses with nonnull next_cursor. Stable chapter/verse order; no totals or unbounded mode.
     */
    public static final class ScriptureBookRead extends Model {
        private String translation;
        private TranslationRights rights;
        private List<Verse> verses;
        private String nextCursor;
        /**
         * Creates an object with all properties unset.
         */
        public ScriptureBookRead() {}
        /**
         * Returns translation.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("translation") public String getTranslation() { return translation; }
        /**
         * Sets translation.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("translation") public void setTranslation(String value) { translation = value; markField("translation"); }
        /**
         * Returns rights.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("rights") public TranslationRights getRights() { return rights; }
        /**
         * Sets rights.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("rights") public void setRights(TranslationRights value) { rights = value; markField("rights"); }
        /**
         * Returns verses.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("verses") public List<Verse> getVerses() { return verses; }
        /**
         * Sets verses.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("verses") public void setVerses(List<Verse> value) { verses = value; markField("verses"); }
        /**
         * Required opaque continuation cursor. Null only on the last page; otherwise pass unchanged as after. Carries no authority.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("next_cursor") public String getNextCursor() { return nextCursor; }
        /**
         * Sets next cursor.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("next_cursor") public void setNextCursor(String value) { nextCursor = value; markField("next_cursor"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("translation")) fields.put("translation", translation);
            if (hasField("rights")) fields.put("rights", rights);
            if (hasField("verses")) fields.put("verses", verses);
            if (hasField("next_cursor")) fields.put("next_cursor", nextCursor);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "translation" -> { setTranslation(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "rights" -> { setRights(Json.MAPPER.convertValue(value, new TypeReference<TranslationRights>() {})); yield true; }
                case "verses" -> { setVerses(Json.MAPPER.convertValue(value, new TypeReference<List<Verse>>() {})); yield true; }
                case "next_cursor" -> { setNextCursor(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "translation" -> translation = null;
                case "rights" -> rights = null;
                case "verses" -> verses = null;
                case "next_cursor" -> nextCursor = null;
                default -> { }
            }
        }
    }
    /**
     * Approved browser-code authorization, including the secret access token. Applications own secure storage.
     */
    public static final class AuthorizationResult extends Model {
        private String actor;
        private String identityName;
        private String displayName;
        private String authProvider;
        private String tokenId;
        private String accessToken;
        /**
         * Creates an object with all properties unset.
         */
        public AuthorizationResult() {}
        /**
         * Returns actor.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("actor") public String getActor() { return actor; }
        /**
         * Sets actor.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("actor") public void setActor(String value) { actor = value; markField("actor"); }
        /**
         * Returns identity name.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("identity_name") public String getIdentityName() { return identityName; }
        /**
         * Sets identity name.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("identity_name") public void setIdentityName(String value) { identityName = value; markField("identity_name"); }
        /**
         * Returns display name.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("display_name") public String getDisplayName() { return displayName; }
        /**
         * Sets display name.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("display_name") public void setDisplayName(String value) { displayName = value; markField("display_name"); }
        /**
         * Returns auth provider.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("auth_provider") public String getAuthProvider() { return authProvider; }
        /**
         * Sets auth provider.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("auth_provider") public void setAuthProvider(String value) { authProvider = value; markField("auth_provider"); }
        /**
         * Returns token id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("token_id") public String getTokenId() { return tokenId; }
        /**
         * Sets token id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("token_id") public void setTokenId(String value) { tokenId = value; markField("token_id"); }
        /**
         * Returns access token.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("access_token") public String getAccessToken() { return accessToken; }
        /**
         * Sets access token.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("access_token") public void setAccessToken(String value) { accessToken = value; markField("access_token"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("actor")) fields.put("actor", actor);
            if (hasField("identity_name")) fields.put("identity_name", identityName);
            if (hasField("display_name")) fields.put("display_name", displayName);
            if (hasField("auth_provider")) fields.put("auth_provider", authProvider);
            if (hasField("token_id")) fields.put("token_id", tokenId);
            if (hasField("access_token")) fields.put("access_token", accessToken);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "actor" -> { setActor(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "identity_name" -> { setIdentityName(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "display_name" -> { setDisplayName(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "auth_provider" -> { setAuthProvider(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "token_id" -> { setTokenId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "access_token" -> { setAccessToken(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "actor" -> actor = null;
                case "identity_name" -> identityName = null;
                case "display_name" -> displayName = null;
                case "auth_provider" -> authProvider = null;
                case "token_id" -> tokenId = null;
                case "access_token" -> accessToken = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of book definition names.
     */
    public static final class BookDefinitionNames extends Model {
        private String en;
        private String pt;
        private String de;
        /**
         * Creates an object with all properties unset.
         */
        public BookDefinitionNames() {}
        /**
         * Returns en.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("en") public String getEn() { return en; }
        /**
         * Sets en.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("en") public void setEn(String value) { en = value; markField("en"); }
        /**
         * Returns pt.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("pt") public String getPt() { return pt; }
        /**
         * Sets pt.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("pt") public void setPt(String value) { pt = value; markField("pt"); }
        /**
         * Returns de.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("de") public String getDe() { return de; }
        /**
         * Sets de.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("de") public void setDe(String value) { de = value; markField("de"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("en")) fields.put("en", en);
            if (hasField("pt")) fields.put("pt", pt);
            if (hasField("de")) fields.put("de", de);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "en" -> { setEn(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "pt" -> { setPt(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "de" -> { setDe(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "en" -> en = null;
                case "pt" -> pt = null;
                case "de" -> de = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of create live queue item reference.
     */
    public static final class CreateLiveQueueItemReference extends Model {
        private String translation;
        private Long bookId;
        private Long chapter;
        private Long verse;
        /**
         * Creates an object with all properties unset.
         */
        public CreateLiveQueueItemReference() {}
        /**
         * Returns translation.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("translation") public String getTranslation() { return translation; }
        /**
         * Sets translation.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("translation") public void setTranslation(String value) { translation = value; markField("translation"); }
        /**
         * Returns book id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("book_id") public Long getBookId() { return bookId; }
        /**
         * Sets book id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("book_id") public void setBookId(Long value) { bookId = value; markField("book_id"); }
        /**
         * Returns chapter.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("chapter") public Long getChapter() { return chapter; }
        /**
         * Sets chapter.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("chapter") public void setChapter(Long value) { chapter = value; markField("chapter"); }
        /**
         * Returns verse.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("verse") public Long getVerse() { return verse; }
        /**
         * Sets verse.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("verse") public void setVerse(Long value) { verse = value; markField("verse"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("translation")) fields.put("translation", translation);
            if (hasField("book_id")) fields.put("book_id", bookId);
            if (hasField("chapter")) fields.put("chapter", chapter);
            if (hasField("verse")) fields.put("verse", verse);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "translation" -> { setTranslation(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "book_id" -> { setBookId(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "chapter" -> { setChapter(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "verse" -> { setVerse(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "translation" -> translation = null;
                case "book_id" -> bookId = null;
                case "chapter" -> chapter = null;
                case "verse" -> verse = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live studio settings controls.
     */
    public static final class LiveStudioSettingsControls extends Model {
        private Boolean theme;
        private Boolean fullscreen;
        private Boolean fontSize;
        /**
         * Creates an object with all properties unset.
         */
        public LiveStudioSettingsControls() {}
        /**
         * Returns theme.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("theme") public Boolean getTheme() { return theme; }
        /**
         * Sets theme.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("theme") public void setTheme(Boolean value) { theme = value; markField("theme"); }
        /**
         * Returns fullscreen.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("fullscreen") public Boolean getFullscreen() { return fullscreen; }
        /**
         * Sets fullscreen.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("fullscreen") public void setFullscreen(Boolean value) { fullscreen = value; markField("fullscreen"); }
        /**
         * Returns font size.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("fontSize") public Boolean getFontSize() { return fontSize; }
        /**
         * Sets font size.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("fontSize") public void setFontSize(Boolean value) { fontSize = value; markField("fontSize"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("theme")) fields.put("theme", theme);
            if (hasField("fullscreen")) fields.put("fullscreen", fullscreen);
            if (hasField("fontSize")) fields.put("fontSize", fontSize);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "theme" -> { setTheme(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "fullscreen" -> { setFullscreen(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "fontSize" -> { setFontSize(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "theme" -> theme = null;
                case "fullscreen" -> fullscreen = null;
                case "fontSize" -> fontSize = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live studio settings palettes.
     */
    public static final class LiveStudioSettingsPalettes extends Model {
        private LiveStudioSettingsPalettesDark dark;
        private LiveStudioSettingsPalettesLight light;
        /**
         * Creates an object with all properties unset.
         */
        public LiveStudioSettingsPalettes() {}
        /**
         * Returns dark.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("dark") public LiveStudioSettingsPalettesDark getDark() { return dark; }
        /**
         * Sets dark.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("dark") public void setDark(LiveStudioSettingsPalettesDark value) { dark = value; markField("dark"); }
        /**
         * Returns light.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("light") public LiveStudioSettingsPalettesLight getLight() { return light; }
        /**
         * Sets light.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("light") public void setLight(LiveStudioSettingsPalettesLight value) { light = value; markField("light"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("dark")) fields.put("dark", dark);
            if (hasField("light")) fields.put("light", light);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "dark" -> { setDark(Json.MAPPER.convertValue(value, new TypeReference<LiveStudioSettingsPalettesDark>() {})); yield true; }
                case "light" -> { setLight(Json.MAPPER.convertValue(value, new TypeReference<LiveStudioSettingsPalettesLight>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "dark" -> dark = null;
                case "light" -> light = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live studio settings container.
     */
    public static final class LiveStudioSettingsContainer extends Model {
        private Long radius;
        private Long padding;
        private String border;
        /**
         * Creates an object with all properties unset.
         */
        public LiveStudioSettingsContainer() {}
        /**
         * Returns radius.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("radius") public Long getRadius() { return radius; }
        /**
         * Sets radius.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("radius") public void setRadius(Long value) { radius = value; markField("radius"); }
        /**
         * Returns padding.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("padding") public Long getPadding() { return padding; }
        /**
         * Sets padding.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("padding") public void setPadding(Long value) { padding = value; markField("padding"); }
        /**
         * Six-digit hexadecimal color; null inherits the selected preset.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("border") public String getBorder() { return border; }
        /**
         * Sets border.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("border") public void setBorder(String value) { border = value; markField("border"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("radius")) fields.put("radius", radius);
            if (hasField("padding")) fields.put("padding", padding);
            if (hasField("border")) fields.put("border", border);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "radius" -> { setRadius(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "padding" -> { setPadding(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "border" -> { setBorder(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "radius" -> radius = null;
                case "padding" -> padding = null;
                case "border" -> border = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live studio settings elements.
     */
    public static final class LiveStudioSettingsElements extends Model {
        private LiveStudioSettingsElementsTranslation translation;
        private LiveStudioSettingsElementsReference reference;
        private LiveStudioSettingsElementsText text;
        /**
         * Creates an object with all properties unset.
         */
        public LiveStudioSettingsElements() {}
        /**
         * Returns translation.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("translation") public LiveStudioSettingsElementsTranslation getTranslation() { return translation; }
        /**
         * Sets translation.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("translation") public void setTranslation(LiveStudioSettingsElementsTranslation value) { translation = value; markField("translation"); }
        /**
         * Returns reference.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("reference") public LiveStudioSettingsElementsReference getReference() { return reference; }
        /**
         * Sets reference.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("reference") public void setReference(LiveStudioSettingsElementsReference value) { reference = value; markField("reference"); }
        /**
         * Returns text.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("text") public LiveStudioSettingsElementsText getText() { return text; }
        /**
         * Sets text.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("text") public void setText(LiveStudioSettingsElementsText value) { text = value; markField("text"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("translation")) fields.put("translation", translation);
            if (hasField("reference")) fields.put("reference", reference);
            if (hasField("text")) fields.put("text", text);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "translation" -> { setTranslation(Json.MAPPER.convertValue(value, new TypeReference<LiveStudioSettingsElementsTranslation>() {})); yield true; }
                case "reference" -> { setReference(Json.MAPPER.convertValue(value, new TypeReference<LiveStudioSettingsElementsReference>() {})); yield true; }
                case "text" -> { setText(Json.MAPPER.convertValue(value, new TypeReference<LiveStudioSettingsElementsText>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "translation" -> translation = null;
                case "reference" -> reference = null;
                case "text" -> text = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live studio settings input controls.
     */
    public static final class LiveStudioSettingsInputControls extends Model {
        private Boolean theme;
        private Boolean fullscreen;
        private Boolean fontSize;
        /**
         * Creates an object with all properties unset.
         */
        public LiveStudioSettingsInputControls() {}
        /**
         * Returns theme.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("theme") public Boolean getTheme() { return theme; }
        /**
         * Sets theme.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("theme") public void setTheme(Boolean value) { theme = value; markField("theme"); }
        /**
         * Returns fullscreen.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("fullscreen") public Boolean getFullscreen() { return fullscreen; }
        /**
         * Sets fullscreen.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("fullscreen") public void setFullscreen(Boolean value) { fullscreen = value; markField("fullscreen"); }
        /**
         * Returns font size.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("fontSize") public Boolean getFontSize() { return fontSize; }
        /**
         * Sets font size.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("fontSize") public void setFontSize(Boolean value) { fontSize = value; markField("fontSize"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("theme")) fields.put("theme", theme);
            if (hasField("fullscreen")) fields.put("fullscreen", fullscreen);
            if (hasField("fontSize")) fields.put("fontSize", fontSize);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "theme" -> { setTheme(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "fullscreen" -> { setFullscreen(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "fontSize" -> { setFontSize(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "theme" -> theme = null;
                case "fullscreen" -> fullscreen = null;
                case "fontSize" -> fontSize = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live studio settings input palettes.
     */
    public static final class LiveStudioSettingsInputPalettes extends Model {
        private LiveStudioSettingsInputPalettesDark dark;
        private LiveStudioSettingsInputPalettesLight light;
        /**
         * Creates an object with all properties unset.
         */
        public LiveStudioSettingsInputPalettes() {}
        /**
         * Returns dark.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("dark") public LiveStudioSettingsInputPalettesDark getDark() { return dark; }
        /**
         * Sets dark.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("dark") public void setDark(LiveStudioSettingsInputPalettesDark value) { dark = value; markField("dark"); }
        /**
         * Returns light.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("light") public LiveStudioSettingsInputPalettesLight getLight() { return light; }
        /**
         * Sets light.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("light") public void setLight(LiveStudioSettingsInputPalettesLight value) { light = value; markField("light"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("dark")) fields.put("dark", dark);
            if (hasField("light")) fields.put("light", light);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "dark" -> { setDark(Json.MAPPER.convertValue(value, new TypeReference<LiveStudioSettingsInputPalettesDark>() {})); yield true; }
                case "light" -> { setLight(Json.MAPPER.convertValue(value, new TypeReference<LiveStudioSettingsInputPalettesLight>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "dark" -> dark = null;
                case "light" -> light = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live studio settings input container.
     */
    public static final class LiveStudioSettingsInputContainer extends Model {
        private Long radius;
        private Long padding;
        private String border;
        /**
         * Creates an object with all properties unset.
         */
        public LiveStudioSettingsInputContainer() {}
        /**
         * Returns radius.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("radius") public Long getRadius() { return radius; }
        /**
         * Sets radius.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("radius") public void setRadius(Long value) { radius = value; markField("radius"); }
        /**
         * Returns padding.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("padding") public Long getPadding() { return padding; }
        /**
         * Sets padding.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("padding") public void setPadding(Long value) { padding = value; markField("padding"); }
        /**
         * Six-digit hexadecimal color; null inherits the selected preset.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("border") public String getBorder() { return border; }
        /**
         * Sets border.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("border") public void setBorder(String value) { border = value; markField("border"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("radius")) fields.put("radius", radius);
            if (hasField("padding")) fields.put("padding", padding);
            if (hasField("border")) fields.put("border", border);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "radius" -> { setRadius(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "padding" -> { setPadding(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "border" -> { setBorder(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "radius" -> radius = null;
                case "padding" -> padding = null;
                case "border" -> border = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live studio settings input elements.
     */
    public static final class LiveStudioSettingsInputElements extends Model {
        private LiveStudioSettingsInputElementsTranslation translation;
        private LiveStudioSettingsInputElementsReference reference;
        private LiveStudioSettingsInputElementsText text;
        /**
         * Creates an object with all properties unset.
         */
        public LiveStudioSettingsInputElements() {}
        /**
         * Returns translation.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("translation") public LiveStudioSettingsInputElementsTranslation getTranslation() { return translation; }
        /**
         * Sets translation.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("translation") public void setTranslation(LiveStudioSettingsInputElementsTranslation value) { translation = value; markField("translation"); }
        /**
         * Returns reference.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("reference") public LiveStudioSettingsInputElementsReference getReference() { return reference; }
        /**
         * Sets reference.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("reference") public void setReference(LiveStudioSettingsInputElementsReference value) { reference = value; markField("reference"); }
        /**
         * Returns text.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("text") public LiveStudioSettingsInputElementsText getText() { return text; }
        /**
         * Sets text.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("text") public void setText(LiveStudioSettingsInputElementsText value) { text = value; markField("text"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("translation")) fields.put("translation", translation);
            if (hasField("reference")) fields.put("reference", reference);
            if (hasField("text")) fields.put("text", text);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "translation" -> { setTranslation(Json.MAPPER.convertValue(value, new TypeReference<LiveStudioSettingsInputElementsTranslation>() {})); yield true; }
                case "reference" -> { setReference(Json.MAPPER.convertValue(value, new TypeReference<LiveStudioSettingsInputElementsReference>() {})); yield true; }
                case "text" -> { setText(Json.MAPPER.convertValue(value, new TypeReference<LiveStudioSettingsInputElementsText>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "translation" -> translation = null;
                case "reference" -> reference = null;
                case "text" -> text = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live event snapshot live.
     */
    public static final class LiveEventSnapshotLive extends Model {
        private String id;
        private String organizationId;
        private String name;
        private String status;
        private String mode;
        private Boolean paused;
        private Long sequence;
        private String accountId;
        /**
         * Creates an object with all properties unset.
         */
        public LiveEventSnapshotLive() {}
        /**
         * Returns id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("id") public String getId() { return id; }
        /**
         * Sets id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("id") public void setId(String value) { id = value; markField("id"); }
        /**
         * Returns organization id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("organization_id") public String getOrganizationId() { return organizationId; }
        /**
         * Sets organization id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("organization_id") public void setOrganizationId(String value) { organizationId = value; markField("organization_id"); }
        /**
         * Returns name.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("name") public String getName() { return name; }
        /**
         * Sets name.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("name") public void setName(String value) { name = value; markField("name"); }
        /**
         * Returns status.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("status") public String getStatus() { return status; }
        /**
         * Sets status.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("status") public void setStatus(String value) { status = value; markField("status"); }
        /**
         * Returns mode.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("mode") public String getMode() { return mode; }
        /**
         * Sets mode.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("mode") public void setMode(String value) { mode = value; markField("mode"); }
        /**
         * Returns paused.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("paused") public Boolean getPaused() { return paused; }
        /**
         * Sets paused.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("paused") public void setPaused(Boolean value) { paused = value; markField("paused"); }
        /**
         * Returns sequence.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("sequence") public Long getSequence() { return sequence; }
        /**
         * Sets sequence.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("sequence") public void setSequence(Long value) { sequence = value; markField("sequence"); }
        /**
         * Returns account id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("account_id") public String getAccountId() { return accountId; }
        /**
         * Sets account id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("account_id") public void setAccountId(String value) { accountId = value; markField("account_id"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("id")) fields.put("id", id);
            if (hasField("organization_id")) fields.put("organization_id", organizationId);
            if (hasField("name")) fields.put("name", name);
            if (hasField("status")) fields.put("status", status);
            if (hasField("mode")) fields.put("mode", mode);
            if (hasField("paused")) fields.put("paused", paused);
            if (hasField("sequence")) fields.put("sequence", sequence);
            if (hasField("account_id")) fields.put("account_id", accountId);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "id" -> { setId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "organization_id" -> { setOrganizationId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "name" -> { setName(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "status" -> { setStatus(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "mode" -> { setMode(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "paused" -> { setPaused(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "sequence" -> { setSequence(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "account_id" -> { setAccountId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "id" -> id = null;
                case "organization_id" -> organizationId = null;
                case "name" -> name = null;
                case "status" -> status = null;
                case "mode" -> mode = null;
                case "paused" -> paused = null;
                case "sequence" -> sequence = null;
                case "account_id" -> accountId = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live event snapshot display.
     */
    public static final class LiveEventSnapshotDisplay extends Model {
        private String liveId;
        private String status;
        private Boolean paused;
        private Boolean showing;
        private Long sequence;
        private String organizationId;
        private String type;
        private List<DisplayVerse> verses;
        private SnapshotSummary snapshot;
        private String accountId;
        /**
         * Creates an object with all properties unset.
         */
        public LiveEventSnapshotDisplay() {}
        /**
         * Returns live id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("live_id") public String getLiveId() { return liveId; }
        /**
         * Sets live id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("live_id") public void setLiveId(String value) { liveId = value; markField("live_id"); }
        /**
         * Returns status.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("status") public String getStatus() { return status; }
        /**
         * Sets status.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("status") public void setStatus(String value) { status = value; markField("status"); }
        /**
         * Returns paused.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("paused") public Boolean getPaused() { return paused; }
        /**
         * Sets paused.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("paused") public void setPaused(Boolean value) { paused = value; markField("paused"); }
        /**
         * Returns showing.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("showing") public Boolean getShowing() { return showing; }
        /**
         * Sets showing.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("showing") public void setShowing(Boolean value) { showing = value; markField("showing"); }
        /**
         * Returns sequence.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("sequence") public Long getSequence() { return sequence; }
        /**
         * Sets sequence.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("sequence") public void setSequence(Long value) { sequence = value; markField("sequence"); }
        /**
         * Returns organization id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("organization_id") public String getOrganizationId() { return organizationId; }
        /**
         * Sets organization id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("organization_id") public void setOrganizationId(String value) { organizationId = value; markField("organization_id"); }
        /**
         * Returns type.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("type") public String getType() { return type; }
        /**
         * Sets type.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("type") public void setType(String value) { type = value; markField("type"); }
        /**
         * Returns verses.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("verses") public List<DisplayVerse> getVerses() { return verses; }
        /**
         * Sets verses.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("verses") public void setVerses(List<DisplayVerse> value) { verses = value; markField("verses"); }
        /**
         * Returns snapshot.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("snapshot") public SnapshotSummary getSnapshot() { return snapshot; }
        /**
         * Sets snapshot.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("snapshot") public void setSnapshot(SnapshotSummary value) { snapshot = value; markField("snapshot"); }
        /**
         * Returns account id.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("account_id") public String getAccountId() { return accountId; }
        /**
         * Sets account id.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("account_id") public void setAccountId(String value) { accountId = value; markField("account_id"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("live_id")) fields.put("live_id", liveId);
            if (hasField("status")) fields.put("status", status);
            if (hasField("paused")) fields.put("paused", paused);
            if (hasField("showing")) fields.put("showing", showing);
            if (hasField("sequence")) fields.put("sequence", sequence);
            if (hasField("organization_id")) fields.put("organization_id", organizationId);
            if (hasField("type")) fields.put("type", type);
            if (hasField("verses")) fields.put("verses", verses);
            if (hasField("snapshot")) fields.put("snapshot", snapshot);
            if (hasField("account_id")) fields.put("account_id", accountId);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "live_id" -> { setLiveId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "status" -> { setStatus(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "paused" -> { setPaused(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "showing" -> { setShowing(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "sequence" -> { setSequence(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "organization_id" -> { setOrganizationId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "type" -> { setType(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "verses" -> { setVerses(Json.MAPPER.convertValue(value, new TypeReference<List<DisplayVerse>>() {})); yield true; }
                case "snapshot" -> { setSnapshot(Json.MAPPER.convertValue(value, new TypeReference<SnapshotSummary>() {})); yield true; }
                case "account_id" -> { setAccountId(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "live_id" -> liveId = null;
                case "status" -> status = null;
                case "paused" -> paused = null;
                case "showing" -> showing = null;
                case "sequence" -> sequence = null;
                case "organization_id" -> organizationId = null;
                case "type" -> type = null;
                case "verses" -> verses = null;
                case "snapshot" -> snapshot = null;
                case "account_id" -> accountId = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live studio settings palettes dark.
     */
    public static final class LiveStudioSettingsPalettesDark extends Model {
        private String background;
        private String container;
        private String translation;
        private String reference;
        private String text;
        /**
         * Creates an object with all properties unset.
         */
        public LiveStudioSettingsPalettesDark() {}
        /**
         * Six-digit hexadecimal color; null inherits the selected preset.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("background") public String getBackground() { return background; }
        /**
         * Sets background.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("background") public void setBackground(String value) { background = value; markField("background"); }
        /**
         * Six-digit hexadecimal color; null inherits the selected preset.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("container") public String getContainer() { return container; }
        /**
         * Sets container.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("container") public void setContainer(String value) { container = value; markField("container"); }
        /**
         * Six-digit hexadecimal color; null inherits the selected preset.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("translation") public String getTranslation() { return translation; }
        /**
         * Sets translation.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("translation") public void setTranslation(String value) { translation = value; markField("translation"); }
        /**
         * Six-digit hexadecimal color; null inherits the selected preset.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("reference") public String getReference() { return reference; }
        /**
         * Sets reference.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("reference") public void setReference(String value) { reference = value; markField("reference"); }
        /**
         * Six-digit hexadecimal color; null inherits the selected preset.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("text") public String getText() { return text; }
        /**
         * Sets text.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("text") public void setText(String value) { text = value; markField("text"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("background")) fields.put("background", background);
            if (hasField("container")) fields.put("container", container);
            if (hasField("translation")) fields.put("translation", translation);
            if (hasField("reference")) fields.put("reference", reference);
            if (hasField("text")) fields.put("text", text);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "background" -> { setBackground(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "container" -> { setContainer(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "translation" -> { setTranslation(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "reference" -> { setReference(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "text" -> { setText(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "background" -> background = null;
                case "container" -> container = null;
                case "translation" -> translation = null;
                case "reference" -> reference = null;
                case "text" -> text = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live studio settings palettes light.
     */
    public static final class LiveStudioSettingsPalettesLight extends Model {
        private String background;
        private String container;
        private String translation;
        private String reference;
        private String text;
        /**
         * Creates an object with all properties unset.
         */
        public LiveStudioSettingsPalettesLight() {}
        /**
         * Six-digit hexadecimal color; null inherits the selected preset.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("background") public String getBackground() { return background; }
        /**
         * Sets background.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("background") public void setBackground(String value) { background = value; markField("background"); }
        /**
         * Six-digit hexadecimal color; null inherits the selected preset.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("container") public String getContainer() { return container; }
        /**
         * Sets container.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("container") public void setContainer(String value) { container = value; markField("container"); }
        /**
         * Six-digit hexadecimal color; null inherits the selected preset.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("translation") public String getTranslation() { return translation; }
        /**
         * Sets translation.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("translation") public void setTranslation(String value) { translation = value; markField("translation"); }
        /**
         * Six-digit hexadecimal color; null inherits the selected preset.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("reference") public String getReference() { return reference; }
        /**
         * Sets reference.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("reference") public void setReference(String value) { reference = value; markField("reference"); }
        /**
         * Six-digit hexadecimal color; null inherits the selected preset.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("text") public String getText() { return text; }
        /**
         * Sets text.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("text") public void setText(String value) { text = value; markField("text"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("background")) fields.put("background", background);
            if (hasField("container")) fields.put("container", container);
            if (hasField("translation")) fields.put("translation", translation);
            if (hasField("reference")) fields.put("reference", reference);
            if (hasField("text")) fields.put("text", text);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "background" -> { setBackground(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "container" -> { setContainer(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "translation" -> { setTranslation(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "reference" -> { setReference(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "text" -> { setText(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "background" -> background = null;
                case "container" -> container = null;
                case "translation" -> translation = null;
                case "reference" -> reference = null;
                case "text" -> text = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live studio settings elements translation.
     */
    public static final class LiveStudioSettingsElementsTranslation extends Model {
        private String font;
        private Long size;
        private Boolean bold;
        private Boolean italic;
        private String align;
        private Long lineHeight;
        /**
         * Creates an object with all properties unset.
         */
        public LiveStudioSettingsElementsTranslation() {}
        /**
         * Returns font.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("font") public String getFont() { return font; }
        /**
         * Sets font.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("font") public void setFont(String value) { font = value; markField("font"); }
        /**
         * Returns size.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("size") public Long getSize() { return size; }
        /**
         * Sets size.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("size") public void setSize(Long value) { size = value; markField("size"); }
        /**
         * Returns bold.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("bold") public Boolean getBold() { return bold; }
        /**
         * Sets bold.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("bold") public void setBold(Boolean value) { bold = value; markField("bold"); }
        /**
         * Returns italic.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("italic") public Boolean getItalic() { return italic; }
        /**
         * Sets italic.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("italic") public void setItalic(Boolean value) { italic = value; markField("italic"); }
        /**
         * Returns align.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("align") public String getAlign() { return align; }
        /**
         * Sets align.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("align") public void setAlign(String value) { align = value; markField("align"); }
        /**
         * Returns line height.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("lineHeight") public Long getLineHeight() { return lineHeight; }
        /**
         * Sets line height.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("lineHeight") public void setLineHeight(Long value) { lineHeight = value; markField("lineHeight"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("font")) fields.put("font", font);
            if (hasField("size")) fields.put("size", size);
            if (hasField("bold")) fields.put("bold", bold);
            if (hasField("italic")) fields.put("italic", italic);
            if (hasField("align")) fields.put("align", align);
            if (hasField("lineHeight")) fields.put("lineHeight", lineHeight);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "font" -> { setFont(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "size" -> { setSize(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "bold" -> { setBold(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "italic" -> { setItalic(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "align" -> { setAlign(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "lineHeight" -> { setLineHeight(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "font" -> font = null;
                case "size" -> size = null;
                case "bold" -> bold = null;
                case "italic" -> italic = null;
                case "align" -> align = null;
                case "lineHeight" -> lineHeight = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live studio settings elements reference.
     */
    public static final class LiveStudioSettingsElementsReference extends Model {
        private String font;
        private Long size;
        private Boolean bold;
        private Boolean italic;
        private String align;
        private Long lineHeight;
        /**
         * Creates an object with all properties unset.
         */
        public LiveStudioSettingsElementsReference() {}
        /**
         * Returns font.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("font") public String getFont() { return font; }
        /**
         * Sets font.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("font") public void setFont(String value) { font = value; markField("font"); }
        /**
         * Returns size.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("size") public Long getSize() { return size; }
        /**
         * Sets size.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("size") public void setSize(Long value) { size = value; markField("size"); }
        /**
         * Returns bold.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("bold") public Boolean getBold() { return bold; }
        /**
         * Sets bold.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("bold") public void setBold(Boolean value) { bold = value; markField("bold"); }
        /**
         * Returns italic.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("italic") public Boolean getItalic() { return italic; }
        /**
         * Sets italic.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("italic") public void setItalic(Boolean value) { italic = value; markField("italic"); }
        /**
         * Returns align.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("align") public String getAlign() { return align; }
        /**
         * Sets align.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("align") public void setAlign(String value) { align = value; markField("align"); }
        /**
         * Returns line height.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("lineHeight") public Long getLineHeight() { return lineHeight; }
        /**
         * Sets line height.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("lineHeight") public void setLineHeight(Long value) { lineHeight = value; markField("lineHeight"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("font")) fields.put("font", font);
            if (hasField("size")) fields.put("size", size);
            if (hasField("bold")) fields.put("bold", bold);
            if (hasField("italic")) fields.put("italic", italic);
            if (hasField("align")) fields.put("align", align);
            if (hasField("lineHeight")) fields.put("lineHeight", lineHeight);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "font" -> { setFont(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "size" -> { setSize(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "bold" -> { setBold(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "italic" -> { setItalic(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "align" -> { setAlign(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "lineHeight" -> { setLineHeight(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "font" -> font = null;
                case "size" -> size = null;
                case "bold" -> bold = null;
                case "italic" -> italic = null;
                case "align" -> align = null;
                case "lineHeight" -> lineHeight = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live studio settings elements text.
     */
    public static final class LiveStudioSettingsElementsText extends Model {
        private String font;
        private Long size;
        private Boolean bold;
        private Boolean italic;
        private String align;
        private Long lineHeight;
        /**
         * Creates an object with all properties unset.
         */
        public LiveStudioSettingsElementsText() {}
        /**
         * Returns font.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("font") public String getFont() { return font; }
        /**
         * Sets font.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("font") public void setFont(String value) { font = value; markField("font"); }
        /**
         * Returns size.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("size") public Long getSize() { return size; }
        /**
         * Sets size.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("size") public void setSize(Long value) { size = value; markField("size"); }
        /**
         * Returns bold.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("bold") public Boolean getBold() { return bold; }
        /**
         * Sets bold.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("bold") public void setBold(Boolean value) { bold = value; markField("bold"); }
        /**
         * Returns italic.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("italic") public Boolean getItalic() { return italic; }
        /**
         * Sets italic.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("italic") public void setItalic(Boolean value) { italic = value; markField("italic"); }
        /**
         * Returns align.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("align") public String getAlign() { return align; }
        /**
         * Sets align.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("align") public void setAlign(String value) { align = value; markField("align"); }
        /**
         * Returns line height.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("lineHeight") public Long getLineHeight() { return lineHeight; }
        /**
         * Sets line height.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("lineHeight") public void setLineHeight(Long value) { lineHeight = value; markField("lineHeight"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("font")) fields.put("font", font);
            if (hasField("size")) fields.put("size", size);
            if (hasField("bold")) fields.put("bold", bold);
            if (hasField("italic")) fields.put("italic", italic);
            if (hasField("align")) fields.put("align", align);
            if (hasField("lineHeight")) fields.put("lineHeight", lineHeight);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "font" -> { setFont(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "size" -> { setSize(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "bold" -> { setBold(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "italic" -> { setItalic(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "align" -> { setAlign(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "lineHeight" -> { setLineHeight(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "font" -> font = null;
                case "size" -> size = null;
                case "bold" -> bold = null;
                case "italic" -> italic = null;
                case "align" -> align = null;
                case "lineHeight" -> lineHeight = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live studio settings input palettes dark.
     */
    public static final class LiveStudioSettingsInputPalettesDark extends Model {
        private String background;
        private String container;
        private String translation;
        private String reference;
        private String text;
        /**
         * Creates an object with all properties unset.
         */
        public LiveStudioSettingsInputPalettesDark() {}
        /**
         * Six-digit hexadecimal color; null inherits the selected preset.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("background") public String getBackground() { return background; }
        /**
         * Sets background.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("background") public void setBackground(String value) { background = value; markField("background"); }
        /**
         * Six-digit hexadecimal color; null inherits the selected preset.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("container") public String getContainer() { return container; }
        /**
         * Sets container.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("container") public void setContainer(String value) { container = value; markField("container"); }
        /**
         * Six-digit hexadecimal color; null inherits the selected preset.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("translation") public String getTranslation() { return translation; }
        /**
         * Sets translation.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("translation") public void setTranslation(String value) { translation = value; markField("translation"); }
        /**
         * Six-digit hexadecimal color; null inherits the selected preset.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("reference") public String getReference() { return reference; }
        /**
         * Sets reference.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("reference") public void setReference(String value) { reference = value; markField("reference"); }
        /**
         * Six-digit hexadecimal color; null inherits the selected preset.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("text") public String getText() { return text; }
        /**
         * Sets text.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("text") public void setText(String value) { text = value; markField("text"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("background")) fields.put("background", background);
            if (hasField("container")) fields.put("container", container);
            if (hasField("translation")) fields.put("translation", translation);
            if (hasField("reference")) fields.put("reference", reference);
            if (hasField("text")) fields.put("text", text);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "background" -> { setBackground(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "container" -> { setContainer(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "translation" -> { setTranslation(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "reference" -> { setReference(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "text" -> { setText(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "background" -> background = null;
                case "container" -> container = null;
                case "translation" -> translation = null;
                case "reference" -> reference = null;
                case "text" -> text = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live studio settings input palettes light.
     */
    public static final class LiveStudioSettingsInputPalettesLight extends Model {
        private String background;
        private String container;
        private String translation;
        private String reference;
        private String text;
        /**
         * Creates an object with all properties unset.
         */
        public LiveStudioSettingsInputPalettesLight() {}
        /**
         * Six-digit hexadecimal color; null inherits the selected preset.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("background") public String getBackground() { return background; }
        /**
         * Sets background.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("background") public void setBackground(String value) { background = value; markField("background"); }
        /**
         * Six-digit hexadecimal color; null inherits the selected preset.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("container") public String getContainer() { return container; }
        /**
         * Sets container.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("container") public void setContainer(String value) { container = value; markField("container"); }
        /**
         * Six-digit hexadecimal color; null inherits the selected preset.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("translation") public String getTranslation() { return translation; }
        /**
         * Sets translation.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("translation") public void setTranslation(String value) { translation = value; markField("translation"); }
        /**
         * Six-digit hexadecimal color; null inherits the selected preset.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("reference") public String getReference() { return reference; }
        /**
         * Sets reference.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("reference") public void setReference(String value) { reference = value; markField("reference"); }
        /**
         * Six-digit hexadecimal color; null inherits the selected preset.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("text") public String getText() { return text; }
        /**
         * Sets text.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("text") public void setText(String value) { text = value; markField("text"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("background")) fields.put("background", background);
            if (hasField("container")) fields.put("container", container);
            if (hasField("translation")) fields.put("translation", translation);
            if (hasField("reference")) fields.put("reference", reference);
            if (hasField("text")) fields.put("text", text);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "background" -> { setBackground(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "container" -> { setContainer(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "translation" -> { setTranslation(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "reference" -> { setReference(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "text" -> { setText(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "background" -> background = null;
                case "container" -> container = null;
                case "translation" -> translation = null;
                case "reference" -> reference = null;
                case "text" -> text = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live studio settings input elements translation.
     */
    public static final class LiveStudioSettingsInputElementsTranslation extends Model {
        private String font;
        private Long size;
        private Boolean bold;
        private Boolean italic;
        private String align;
        private Long lineHeight;
        /**
         * Creates an object with all properties unset.
         */
        public LiveStudioSettingsInputElementsTranslation() {}
        /**
         * Returns font.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("font") public String getFont() { return font; }
        /**
         * Sets font.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("font") public void setFont(String value) { font = value; markField("font"); }
        /**
         * Returns size.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("size") public Long getSize() { return size; }
        /**
         * Sets size.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("size") public void setSize(Long value) { size = value; markField("size"); }
        /**
         * Returns bold.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("bold") public Boolean getBold() { return bold; }
        /**
         * Sets bold.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("bold") public void setBold(Boolean value) { bold = value; markField("bold"); }
        /**
         * Returns italic.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("italic") public Boolean getItalic() { return italic; }
        /**
         * Sets italic.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("italic") public void setItalic(Boolean value) { italic = value; markField("italic"); }
        /**
         * Returns align.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("align") public String getAlign() { return align; }
        /**
         * Sets align.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("align") public void setAlign(String value) { align = value; markField("align"); }
        /**
         * Returns line height.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("lineHeight") public Long getLineHeight() { return lineHeight; }
        /**
         * Sets line height.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("lineHeight") public void setLineHeight(Long value) { lineHeight = value; markField("lineHeight"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("font")) fields.put("font", font);
            if (hasField("size")) fields.put("size", size);
            if (hasField("bold")) fields.put("bold", bold);
            if (hasField("italic")) fields.put("italic", italic);
            if (hasField("align")) fields.put("align", align);
            if (hasField("lineHeight")) fields.put("lineHeight", lineHeight);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "font" -> { setFont(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "size" -> { setSize(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "bold" -> { setBold(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "italic" -> { setItalic(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "align" -> { setAlign(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "lineHeight" -> { setLineHeight(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "font" -> font = null;
                case "size" -> size = null;
                case "bold" -> bold = null;
                case "italic" -> italic = null;
                case "align" -> align = null;
                case "lineHeight" -> lineHeight = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live studio settings input elements reference.
     */
    public static final class LiveStudioSettingsInputElementsReference extends Model {
        private String font;
        private Long size;
        private Boolean bold;
        private Boolean italic;
        private String align;
        private Long lineHeight;
        /**
         * Creates an object with all properties unset.
         */
        public LiveStudioSettingsInputElementsReference() {}
        /**
         * Returns font.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("font") public String getFont() { return font; }
        /**
         * Sets font.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("font") public void setFont(String value) { font = value; markField("font"); }
        /**
         * Returns size.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("size") public Long getSize() { return size; }
        /**
         * Sets size.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("size") public void setSize(Long value) { size = value; markField("size"); }
        /**
         * Returns bold.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("bold") public Boolean getBold() { return bold; }
        /**
         * Sets bold.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("bold") public void setBold(Boolean value) { bold = value; markField("bold"); }
        /**
         * Returns italic.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("italic") public Boolean getItalic() { return italic; }
        /**
         * Sets italic.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("italic") public void setItalic(Boolean value) { italic = value; markField("italic"); }
        /**
         * Returns align.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("align") public String getAlign() { return align; }
        /**
         * Sets align.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("align") public void setAlign(String value) { align = value; markField("align"); }
        /**
         * Returns line height.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("lineHeight") public Long getLineHeight() { return lineHeight; }
        /**
         * Sets line height.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("lineHeight") public void setLineHeight(Long value) { lineHeight = value; markField("lineHeight"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("font")) fields.put("font", font);
            if (hasField("size")) fields.put("size", size);
            if (hasField("bold")) fields.put("bold", bold);
            if (hasField("italic")) fields.put("italic", italic);
            if (hasField("align")) fields.put("align", align);
            if (hasField("lineHeight")) fields.put("lineHeight", lineHeight);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "font" -> { setFont(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "size" -> { setSize(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "bold" -> { setBold(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "italic" -> { setItalic(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "align" -> { setAlign(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "lineHeight" -> { setLineHeight(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "font" -> font = null;
                case "size" -> size = null;
                case "bold" -> bold = null;
                case "italic" -> italic = null;
                case "align" -> align = null;
                case "lineHeight" -> lineHeight = null;
                default -> { }
            }
        }
    }
    /**
     * Java representation of live studio settings input elements text.
     */
    public static final class LiveStudioSettingsInputElementsText extends Model {
        private String font;
        private Long size;
        private Boolean bold;
        private Boolean italic;
        private String align;
        private Long lineHeight;
        /**
         * Creates an object with all properties unset.
         */
        public LiveStudioSettingsInputElementsText() {}
        /**
         * Returns font.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("font") public String getFont() { return font; }
        /**
         * Sets font.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("font") public void setFont(String value) { font = value; markField("font"); }
        /**
         * Returns size.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("size") public Long getSize() { return size; }
        /**
         * Sets size.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("size") public void setSize(Long value) { size = value; markField("size"); }
        /**
         * Returns bold.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("bold") public Boolean getBold() { return bold; }
        /**
         * Sets bold.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("bold") public void setBold(Boolean value) { bold = value; markField("bold"); }
        /**
         * Returns italic.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("italic") public Boolean getItalic() { return italic; }
        /**
         * Sets italic.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("italic") public void setItalic(Boolean value) { italic = value; markField("italic"); }
        /**
         * Returns align.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("align") public String getAlign() { return align; }
        /**
         * Sets align.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("align") public void setAlign(String value) { align = value; markField("align"); }
        /**
         * Returns line height.
         * @return the typed Java value, or null when absent or explicitly null
         */
        @JsonProperty("lineHeight") public Long getLineHeight() { return lineHeight; }
        /**
         * Sets line height.
         * @param value the Java value; null writes explicit JSON null
         */
        @JsonProperty("lineHeight") public void setLineHeight(Long value) { lineHeight = value; markField("lineHeight"); }
        @Override protected void appendFields(Map<String, Object> fields) {
            if (hasField("font")) fields.put("font", font);
            if (hasField("size")) fields.put("size", size);
            if (hasField("bold")) fields.put("bold", bold);
            if (hasField("italic")) fields.put("italic", italic);
            if (hasField("align")) fields.put("align", align);
            if (hasField("lineHeight")) fields.put("lineHeight", lineHeight);
        }
        @Override protected boolean assignKnownField(String name, Object value) {
            return switch (name) {
                case "font" -> { setFont(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "size" -> { setSize(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                case "bold" -> { setBold(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "italic" -> { setItalic(Json.MAPPER.convertValue(value, new TypeReference<Boolean>() {})); yield true; }
                case "align" -> { setAlign(Json.MAPPER.convertValue(value, new TypeReference<String>() {})); yield true; }
                case "lineHeight" -> { setLineHeight(Json.MAPPER.convertValue(value, new TypeReference<Long>() {})); yield true; }
                default -> false;
            };
        }
        @Override protected void clearKnownField(String name) {
            switch (name) {
                case "font" -> font = null;
                case "size" -> size = null;
                case "bold" -> bold = null;
                case "italic" -> italic = null;
                case "align" -> align = null;
                case "lineHeight" -> lineHeight = null;
                default -> { }
            }
        }
    }
    /**
     * Typed scalar value for scope.
     */
    public static final class Scope {
        private final String value;
        /**
         * Creates a typed scalar without introducing a wire wrapper.
         * @param value the scalar Java value
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public Scope(String value) { this.value = Objects.requireNonNull(value); }
        /**
         * Returns the scalar wire value.
         * @return the Java scalar value
         */
        @JsonValue public String value() { return value; }
        @Override public boolean equals(Object other) { return other instanceof Scope scalar && value.equals(scalar.value); }
        @Override public int hashCode() { return value.hashCode(); }
        @Override public String toString() { return "Scope[redacted]"; }
    }
    /**
     * Typed scalar value for oauth scope.
     */
    public static final class OAuthScope {
        private final String value;
        /**
         * Creates a typed scalar without introducing a wire wrapper.
         * @param value the scalar Java value
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public OAuthScope(String value) { this.value = Objects.requireNonNull(value); }
        /**
         * Returns the scalar wire value.
         * @return the Java scalar value
         */
        @JsonValue public String value() { return value; }
        @Override public boolean equals(Object other) { return other instanceof OAuthScope scalar && value.equals(scalar.value); }
        @Override public int hashCode() { return value.hashCode(); }
        @Override public String toString() { return "OAuthScope[redacted]"; }
    }
}
