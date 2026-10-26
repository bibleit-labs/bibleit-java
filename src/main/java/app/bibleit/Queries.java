package app.bibleit;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/** Operation-specific query builders generated from the OpenAPI contract. */
public final class Queries {
    private Queries() {}

    /** Query parameters accepted by {@code getLiveEvents}. */
    public static final class GetLiveEventsQuery implements QueryParameters {
        private final String after;
        private final Integer waitSeconds;

        private GetLiveEventsQuery(Builder builder) {
            after = builder.after;
            waitSeconds = builder.waitSeconds;
            if (waitSeconds != null && waitSeconds < 0) throw new IllegalArgumentException("wait is below its minimum");

            if (waitSeconds != null && waitSeconds > 25) throw new IllegalArgumentException("wait is above its maximum");
        }

        /**
         * Creates a builder with all optional values unset.
         * @return a new query builder
         */
        public static Builder builder() { return new Builder(); }

        /**
         * Copies this query into a new builder.
         * @return a builder initialized from this query
         */
        public Builder toBuilder() {
            var builder = new Builder();
            builder.after = after;
            builder.waitSeconds = waitSeconds;
            return builder;
        }
        /**
         * Returns the after query value.
         * @return the typed value, or null when unset
         */
        public String getAfter() { return after; }
        /**
         * Returns the wait query value.
         * @return the typed value, or null when unset
         */
        public Integer getWaitSeconds() { return waitSeconds; }

        /** {@inheritDoc} */
        @Override public List<QueryParameter> parameters() {
            var parameters = new ArrayList<QueryParameter>();
            if (after != null) parameters.add(new QueryParameter("after", after.toString()));
            if (waitSeconds != null) parameters.add(new QueryParameter("wait", waitSeconds.toString()));
            return List.copyOf(parameters);
        }


        /** Builder for {@link GetLiveEventsQuery}. */
        public static final class Builder {
            private String after;
            private Integer waitSeconds;
            private Builder() {}
            /**
             * Sets the after parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder after(String value) { after = value; return this; }
            /**
             * Sets the wait parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder waitSeconds(Integer value) { waitSeconds = value; return this; }
            /**
             * Creates an immutable query and checks scalar bounds.
             * @return the operation-specific query
             * @throws IllegalArgumentException if a scalar bound is violated
             */
            public GetLiveEventsQuery build() { return new GetLiveEventsQuery(this); }
        }
    }

    /** Query parameters accepted by {@code listLiveQueueItems}. */
    public static final class ListLiveQueueItemsQuery implements QueryParameters {
        private final Integer limit;
        private final String after;

        private ListLiveQueueItemsQuery(Builder builder) {
            limit = builder.limit;
            if (limit != null && limit < 1) throw new IllegalArgumentException("limit is below its minimum");

            if (limit != null && limit > 100) throw new IllegalArgumentException("limit is above its maximum");
            after = builder.after;

            if (after != null && after.length() > 128) throw new IllegalArgumentException("after is too long");
        }

        /**
         * Creates a builder with all optional values unset.
         * @return a new query builder
         */
        public static Builder builder() { return new Builder(); }

        /**
         * Copies this query into a new builder.
         * @return a builder initialized from this query
         */
        public Builder toBuilder() {
            var builder = new Builder();
            builder.limit = limit;
            builder.after = after;
            return builder;
        }
        /**
         * Returns the limit query value.
         * @return the typed value, or null when unset
         */
        public Integer getLimit() { return limit; }
        /**
         * Returns the after query value.
         * @return the typed value, or null when unset
         */
        public String getAfter() { return after; }

        /** {@inheritDoc} */
        @Override public List<QueryParameter> parameters() {
            var parameters = new ArrayList<QueryParameter>();
            if (limit != null) parameters.add(new QueryParameter("limit", limit.toString()));
            if (after != null) parameters.add(new QueryParameter("after", after.toString()));
            return List.copyOf(parameters);
        }


        /** Builder for {@link ListLiveQueueItemsQuery}. */
        public static final class Builder {
            private Integer limit;
            private String after;
            private Builder() {}
            /**
             * Sets the limit parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder limit(Integer value) { limit = value; return this; }
            /**
             * Sets the after parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder after(String value) { after = value; return this; }
            /**
             * Creates an immutable query and checks scalar bounds.
             * @return the operation-specific query
             * @throws IllegalArgumentException if a scalar bound is violated
             */
            public ListLiveQueueItemsQuery build() { return new ListLiveQueueItemsQuery(this); }
        }
    }

    /** Query parameters accepted by {@code listLives}. */
    public static final class ListLivesQuery implements QueryParameters {
        private final Integer limit;
        private final String after;

        private ListLivesQuery(Builder builder) {
            limit = builder.limit;
            if (limit != null && limit < 1) throw new IllegalArgumentException("limit is below its minimum");

            if (limit != null && limit > 100) throw new IllegalArgumentException("limit is above its maximum");
            after = builder.after;

            if (after != null && after.length() > 128) throw new IllegalArgumentException("after is too long");
        }

        /**
         * Creates a builder with all optional values unset.
         * @return a new query builder
         */
        public static Builder builder() { return new Builder(); }

        /**
         * Copies this query into a new builder.
         * @return a builder initialized from this query
         */
        public Builder toBuilder() {
            var builder = new Builder();
            builder.limit = limit;
            builder.after = after;
            return builder;
        }
        /**
         * Returns the limit query value.
         * @return the typed value, or null when unset
         */
        public Integer getLimit() { return limit; }
        /**
         * Returns the after query value.
         * @return the typed value, or null when unset
         */
        public String getAfter() { return after; }

        /** {@inheritDoc} */
        @Override public List<QueryParameter> parameters() {
            var parameters = new ArrayList<QueryParameter>();
            if (limit != null) parameters.add(new QueryParameter("limit", limit.toString()));
            if (after != null) parameters.add(new QueryParameter("after", after.toString()));
            return List.copyOf(parameters);
        }


        /** Builder for {@link ListLivesQuery}. */
        public static final class Builder {
            private Integer limit;
            private String after;
            private Builder() {}
            /**
             * Sets the limit parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder limit(Integer value) { limit = value; return this; }
            /**
             * Sets the after parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder after(String value) { after = value; return this; }
            /**
             * Creates an immutable query and checks scalar bounds.
             * @return the operation-specific query
             * @throws IllegalArgumentException if a scalar bound is violated
             */
            public ListLivesQuery build() { return new ListLivesQuery(this); }
        }
    }

    /** Query parameters accepted by {@code authorizeOAuth}. */
    public static final class AuthorizeOAuthQuery implements QueryParameters {
        private final ResponseType responseType;
        private final String clientId;
        private final URI redirectUri;
        private final String scope;
        private final String state;
        private final String codeChallenge;
        private final CodeChallengeMethod codeChallengeMethod;
        private final String request;

        private AuthorizeOAuthQuery(Builder builder) {
            responseType = builder.responseType;
            clientId = builder.clientId;
            redirectUri = builder.redirectUri;
            scope = builder.scope;

            if (scope != null && scope.length() < 1) throw new IllegalArgumentException("scope is too short");

            if (scope != null && scope.length() > 512) throw new IllegalArgumentException("scope is too long");
            state = builder.state;

            if (state != null && state.length() < 16) throw new IllegalArgumentException("state is too short");

            if (state != null && state.length() > 512) throw new IllegalArgumentException("state is too long");
            codeChallenge = builder.codeChallenge;
            codeChallengeMethod = builder.codeChallengeMethod;
            request = builder.request;
        }

        /**
         * Creates a builder with all optional values unset.
         * @return a new query builder
         */
        public static Builder builder() { return new Builder(); }

        /**
         * Copies this query into a new builder.
         * @return a builder initialized from this query
         */
        public Builder toBuilder() {
            var builder = new Builder();
            builder.responseType = responseType;
            builder.clientId = clientId;
            builder.redirectUri = redirectUri;
            builder.scope = scope;
            builder.state = state;
            builder.codeChallenge = codeChallenge;
            builder.codeChallengeMethod = codeChallengeMethod;
            builder.request = request;
            return builder;
        }
        /**
         * Returns the response_type query value.
         * @return the typed value, or null when unset
         */
        public ResponseType getResponseType() { return responseType; }
        /**
         * Returns the client_id query value.
         * @return the typed value, or null when unset
         */
        public String getClientId() { return clientId; }
        /**
         * Returns the redirect_uri query value.
         * @return the typed value, or null when unset
         */
        public URI getRedirectUri() { return redirectUri; }
        /**
         * Returns the scope query value.
         * @return the typed value, or null when unset
         */
        public String getScope() { return scope; }
        /**
         * Returns the state query value.
         * @return the typed value, or null when unset
         */
        public String getState() { return state; }
        /**
         * Returns the code_challenge query value.
         * @return the typed value, or null when unset
         */
        public String getCodeChallenge() { return codeChallenge; }
        /**
         * Returns the code_challenge_method query value.
         * @return the typed value, or null when unset
         */
        public CodeChallengeMethod getCodeChallengeMethod() { return codeChallengeMethod; }
        /**
         * Returns the request query value.
         * @return the typed value, or null when unset
         */
        public String getRequest() { return request; }

        /** {@inheritDoc} */
        @Override public List<QueryParameter> parameters() {
            var parameters = new ArrayList<QueryParameter>();
            if (responseType != null) parameters.add(new QueryParameter("response_type", responseType.wireValue()));
            if (clientId != null) parameters.add(new QueryParameter("client_id", clientId.toString()));
            if (redirectUri != null) parameters.add(new QueryParameter("redirect_uri", redirectUri.toString()));
            if (scope != null) parameters.add(new QueryParameter("scope", scope.toString()));
            if (state != null) parameters.add(new QueryParameter("state", state.toString()));
            if (codeChallenge != null) parameters.add(new QueryParameter("code_challenge", codeChallenge.toString()));
            if (codeChallengeMethod != null) parameters.add(new QueryParameter("code_challenge_method", codeChallengeMethod.wireValue()));
            if (request != null) parameters.add(new QueryParameter("request", request.toString()));
            return List.copyOf(parameters);
        }

        /** Supported values of the response_type parameter. */
        public enum ResponseType {

            /** The CODE wire value. */
            CODE("code");

            private final String value;
            ResponseType(String value) { this.value = value; }
            /**
             * Returns the contract wire representation.
             * @return the wire value
             */
            public String wireValue() { return value; }
        }

        /** Supported values of the code_challenge_method parameter. */
        public enum CodeChallengeMethod {

            /** The S256 wire value. */
            S256("S256");

            private final String value;
            CodeChallengeMethod(String value) { this.value = value; }
            /**
             * Returns the contract wire representation.
             * @return the wire value
             */
            public String wireValue() { return value; }
        }


        /** Builder for {@link AuthorizeOAuthQuery}. */
        public static final class Builder {
            private ResponseType responseType;
            private String clientId;
            private URI redirectUri;
            private String scope;
            private String state;
            private String codeChallenge;
            private CodeChallengeMethod codeChallengeMethod;
            private String request;
            private Builder() {}
            /**
             * Sets the response_type parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder responseType(ResponseType value) { responseType = value; return this; }
            /**
             * Sets the client_id parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder clientId(String value) { clientId = value; return this; }
            /**
             * Sets the redirect_uri parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder redirectUri(URI value) { redirectUri = value; return this; }
            /**
             * Sets the scope parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder scope(String value) { scope = value; return this; }
            /**
             * Sets the state parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder state(String value) { state = value; return this; }
            /**
             * Sets the code_challenge parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder codeChallenge(String value) { codeChallenge = value; return this; }
            /**
             * Sets the code_challenge_method parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder codeChallengeMethod(CodeChallengeMethod value) { codeChallengeMethod = value; return this; }
            /**
             * Sets the request parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder request(String value) { request = value; return this; }
            /**
             * Creates an immutable query and checks scalar bounds.
             * @return the operation-specific query
             * @throws IllegalArgumentException if a scalar bound is violated
             */
            public AuthorizeOAuthQuery build() { return new AuthorizeOAuthQuery(this); }
        }
    }

    /** Query parameters accepted by {@code listOrganizationMembers}. */
    public static final class ListOrganizationMembersQuery implements QueryParameters {
        private final Integer limit;
        private final String after;

        private ListOrganizationMembersQuery(Builder builder) {
            limit = builder.limit;
            if (limit != null && limit < 1) throw new IllegalArgumentException("limit is below its minimum");

            if (limit != null && limit > 100) throw new IllegalArgumentException("limit is above its maximum");
            after = builder.after;

            if (after != null && after.length() > 128) throw new IllegalArgumentException("after is too long");
        }

        /**
         * Creates a builder with all optional values unset.
         * @return a new query builder
         */
        public static Builder builder() { return new Builder(); }

        /**
         * Copies this query into a new builder.
         * @return a builder initialized from this query
         */
        public Builder toBuilder() {
            var builder = new Builder();
            builder.limit = limit;
            builder.after = after;
            return builder;
        }
        /**
         * Returns the limit query value.
         * @return the typed value, or null when unset
         */
        public Integer getLimit() { return limit; }
        /**
         * Returns the after query value.
         * @return the typed value, or null when unset
         */
        public String getAfter() { return after; }

        /** {@inheritDoc} */
        @Override public List<QueryParameter> parameters() {
            var parameters = new ArrayList<QueryParameter>();
            if (limit != null) parameters.add(new QueryParameter("limit", limit.toString()));
            if (after != null) parameters.add(new QueryParameter("after", after.toString()));
            return List.copyOf(parameters);
        }


        /** Builder for {@link ListOrganizationMembersQuery}. */
        public static final class Builder {
            private Integer limit;
            private String after;
            private Builder() {}
            /**
             * Sets the limit parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder limit(Integer value) { limit = value; return this; }
            /**
             * Sets the after parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder after(String value) { after = value; return this; }
            /**
             * Creates an immutable query and checks scalar bounds.
             * @return the operation-specific query
             * @throws IllegalArgumentException if a scalar bound is violated
             */
            public ListOrganizationMembersQuery build() { return new ListOrganizationMembersQuery(this); }
        }
    }

    /** Query parameters accepted by {@code listPersonalOrganizations}. */
    public static final class ListPersonalOrganizationsQuery implements QueryParameters {
        private final String after;

        private ListPersonalOrganizationsQuery(Builder builder) {
            after = builder.after;

            if (after != null && after.length() > 80) throw new IllegalArgumentException("after is too long");
        }

        /**
         * Creates a builder with all optional values unset.
         * @return a new query builder
         */
        public static Builder builder() { return new Builder(); }

        /**
         * Copies this query into a new builder.
         * @return a builder initialized from this query
         */
        public Builder toBuilder() {
            var builder = new Builder();
            builder.after = after;
            return builder;
        }
        /**
         * Returns the after query value.
         * @return the typed value, or null when unset
         */
        public String getAfter() { return after; }

        /** {@inheritDoc} */
        @Override public List<QueryParameter> parameters() {
            var parameters = new ArrayList<QueryParameter>();
            if (after != null) parameters.add(new QueryParameter("after", after.toString()));
            return List.copyOf(parameters);
        }


        /** Builder for {@link ListPersonalOrganizationsQuery}. */
        public static final class Builder {
            private String after;
            private Builder() {}
            /**
             * Sets the after parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder after(String value) { after = value; return this; }
            /**
             * Creates an immutable query and checks scalar bounds.
             * @return the operation-specific query
             * @throws IllegalArgumentException if a scalar bound is violated
             */
            public ListPersonalOrganizationsQuery build() { return new ListPersonalOrganizationsQuery(this); }
        }
    }

    /** Query parameters accepted by {@code readBook}. */
    public static final class ReadBookQuery implements QueryParameters {
        private final Integer limit;
        private final String after;

        private ReadBookQuery(Builder builder) {
            limit = builder.limit;
            if (limit != null && limit < 1) throw new IllegalArgumentException("limit is below its minimum");

            if (limit != null && limit > 500) throw new IllegalArgumentException("limit is above its maximum");
            after = builder.after;

            if (after != null && after.length() < 1) throw new IllegalArgumentException("after is too short");

            if (after != null && after.length() > 1024) throw new IllegalArgumentException("after is too long");
        }

        /**
         * Creates a builder with all optional values unset.
         * @return a new query builder
         */
        public static Builder builder() { return new Builder(); }

        /**
         * Copies this query into a new builder.
         * @return a builder initialized from this query
         */
        public Builder toBuilder() {
            var builder = new Builder();
            builder.limit = limit;
            builder.after = after;
            return builder;
        }
        /**
         * Returns the limit query value.
         * @return the typed value, or null when unset
         */
        public Integer getLimit() { return limit; }
        /**
         * Returns the after query value.
         * @return the typed value, or null when unset
         */
        public String getAfter() { return after; }

        /** {@inheritDoc} */
        @Override public List<QueryParameter> parameters() {
            var parameters = new ArrayList<QueryParameter>();
            if (limit != null) parameters.add(new QueryParameter("limit", limit.toString()));
            if (after != null) parameters.add(new QueryParameter("after", after.toString()));
            return List.copyOf(parameters);
        }


        /** Builder for {@link ReadBookQuery}. */
        public static final class Builder {
            private Integer limit;
            private String after;
            private Builder() {}
            /**
             * Sets the limit parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder limit(Integer value) { limit = value; return this; }
            /**
             * Sets the after parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder after(String value) { after = value; return this; }
            /**
             * Creates an immutable query and checks scalar bounds.
             * @return the operation-specific query
             * @throws IllegalArgumentException if a scalar bound is violated
             */
            public ReadBookQuery build() { return new ReadBookQuery(this); }
        }
    }

    /** Query parameters accepted by {@code listPersonalAccounts}. */
    public static final class ListPersonalAccountsQuery implements QueryParameters {
        private final Integer limit;
        private final String after;

        private ListPersonalAccountsQuery(Builder builder) {
            limit = builder.limit;
            if (limit != null && limit < 1) throw new IllegalArgumentException("limit is below its minimum");

            if (limit != null && limit > 100) throw new IllegalArgumentException("limit is above its maximum");
            after = builder.after;

            if (after != null && after.length() > 128) throw new IllegalArgumentException("after is too long");
        }

        /**
         * Creates a builder with all optional values unset.
         * @return a new query builder
         */
        public static Builder builder() { return new Builder(); }

        /**
         * Copies this query into a new builder.
         * @return a builder initialized from this query
         */
        public Builder toBuilder() {
            var builder = new Builder();
            builder.limit = limit;
            builder.after = after;
            return builder;
        }
        /**
         * Returns the limit query value.
         * @return the typed value, or null when unset
         */
        public Integer getLimit() { return limit; }
        /**
         * Returns the after query value.
         * @return the typed value, or null when unset
         */
        public String getAfter() { return after; }

        /** {@inheritDoc} */
        @Override public List<QueryParameter> parameters() {
            var parameters = new ArrayList<QueryParameter>();
            if (limit != null) parameters.add(new QueryParameter("limit", limit.toString()));
            if (after != null) parameters.add(new QueryParameter("after", after.toString()));
            return List.copyOf(parameters);
        }


        /** Builder for {@link ListPersonalAccountsQuery}. */
        public static final class Builder {
            private Integer limit;
            private String after;
            private Builder() {}
            /**
             * Sets the limit parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder limit(Integer value) { limit = value; return this; }
            /**
             * Sets the after parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder after(String value) { after = value; return this; }
            /**
             * Creates an immutable query and checks scalar bounds.
             * @return the operation-specific query
             * @throws IllegalArgumentException if a scalar bound is violated
             */
            public ListPersonalAccountsQuery build() { return new ListPersonalAccountsQuery(this); }
        }
    }

    /** Query parameters accepted by {@code listPersonalLives}. */
    public static final class ListPersonalLivesQuery implements QueryParameters {
        private final Integer limit;
        private final String after;

        private ListPersonalLivesQuery(Builder builder) {
            limit = builder.limit;
            if (limit != null && limit < 1) throw new IllegalArgumentException("limit is below its minimum");

            if (limit != null && limit > 100) throw new IllegalArgumentException("limit is above its maximum");
            after = builder.after;

            if (after != null && after.length() > 128) throw new IllegalArgumentException("after is too long");
        }

        /**
         * Creates a builder with all optional values unset.
         * @return a new query builder
         */
        public static Builder builder() { return new Builder(); }

        /**
         * Copies this query into a new builder.
         * @return a builder initialized from this query
         */
        public Builder toBuilder() {
            var builder = new Builder();
            builder.limit = limit;
            builder.after = after;
            return builder;
        }
        /**
         * Returns the limit query value.
         * @return the typed value, or null when unset
         */
        public Integer getLimit() { return limit; }
        /**
         * Returns the after query value.
         * @return the typed value, or null when unset
         */
        public String getAfter() { return after; }

        /** {@inheritDoc} */
        @Override public List<QueryParameter> parameters() {
            var parameters = new ArrayList<QueryParameter>();
            if (limit != null) parameters.add(new QueryParameter("limit", limit.toString()));
            if (after != null) parameters.add(new QueryParameter("after", after.toString()));
            return List.copyOf(parameters);
        }


        /** Builder for {@link ListPersonalLivesQuery}. */
        public static final class Builder {
            private Integer limit;
            private String after;
            private Builder() {}
            /**
             * Sets the limit parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder limit(Integer value) { limit = value; return this; }
            /**
             * Sets the after parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder after(String value) { after = value; return this; }
            /**
             * Creates an immutable query and checks scalar bounds.
             * @return the operation-specific query
             * @throws IllegalArgumentException if a scalar bound is violated
             */
            public ListPersonalLivesQuery build() { return new ListPersonalLivesQuery(this); }
        }
    }

    /** Query parameters accepted by {@code listPersonalTokens}. */
    public static final class ListPersonalTokensQuery implements QueryParameters {
        private final Integer limit;
        private final String after;

        private ListPersonalTokensQuery(Builder builder) {
            limit = builder.limit;
            if (limit != null && limit < 1) throw new IllegalArgumentException("limit is below its minimum");

            if (limit != null && limit > 100) throw new IllegalArgumentException("limit is above its maximum");
            after = builder.after;

            if (after != null && after.length() > 128) throw new IllegalArgumentException("after is too long");
        }

        /**
         * Creates a builder with all optional values unset.
         * @return a new query builder
         */
        public static Builder builder() { return new Builder(); }

        /**
         * Copies this query into a new builder.
         * @return a builder initialized from this query
         */
        public Builder toBuilder() {
            var builder = new Builder();
            builder.limit = limit;
            builder.after = after;
            return builder;
        }
        /**
         * Returns the limit query value.
         * @return the typed value, or null when unset
         */
        public Integer getLimit() { return limit; }
        /**
         * Returns the after query value.
         * @return the typed value, or null when unset
         */
        public String getAfter() { return after; }

        /** {@inheritDoc} */
        @Override public List<QueryParameter> parameters() {
            var parameters = new ArrayList<QueryParameter>();
            if (limit != null) parameters.add(new QueryParameter("limit", limit.toString()));
            if (after != null) parameters.add(new QueryParameter("after", after.toString()));
            return List.copyOf(parameters);
        }


        /** Builder for {@link ListPersonalTokensQuery}. */
        public static final class Builder {
            private Integer limit;
            private String after;
            private Builder() {}
            /**
             * Sets the limit parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder limit(Integer value) { limit = value; return this; }
            /**
             * Sets the after parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder after(String value) { after = value; return this; }
            /**
             * Creates an immutable query and checks scalar bounds.
             * @return the operation-specific query
             * @throws IllegalArgumentException if a scalar bound is violated
             */
            public ListPersonalTokensQuery build() { return new ListPersonalTokensQuery(this); }
        }
    }

    /** Query parameters accepted by {@code listPersonalTranslations}. */
    public static final class ListPersonalTranslationsQuery implements QueryParameters {
        private final Integer limit;
        private final String after;

        private ListPersonalTranslationsQuery(Builder builder) {
            limit = builder.limit;
            if (limit != null && limit < 1) throw new IllegalArgumentException("limit is below its minimum");

            if (limit != null && limit > 100) throw new IllegalArgumentException("limit is above its maximum");
            after = builder.after;

            if (after != null && after.length() > 128) throw new IllegalArgumentException("after is too long");
        }

        /**
         * Creates a builder with all optional values unset.
         * @return a new query builder
         */
        public static Builder builder() { return new Builder(); }

        /**
         * Copies this query into a new builder.
         * @return a builder initialized from this query
         */
        public Builder toBuilder() {
            var builder = new Builder();
            builder.limit = limit;
            builder.after = after;
            return builder;
        }
        /**
         * Returns the limit query value.
         * @return the typed value, or null when unset
         */
        public Integer getLimit() { return limit; }
        /**
         * Returns the after query value.
         * @return the typed value, or null when unset
         */
        public String getAfter() { return after; }

        /** {@inheritDoc} */
        @Override public List<QueryParameter> parameters() {
            var parameters = new ArrayList<QueryParameter>();
            if (limit != null) parameters.add(new QueryParameter("limit", limit.toString()));
            if (after != null) parameters.add(new QueryParameter("after", after.toString()));
            return List.copyOf(parameters);
        }


        /** Builder for {@link ListPersonalTranslationsQuery}. */
        public static final class Builder {
            private Integer limit;
            private String after;
            private Builder() {}
            /**
             * Sets the limit parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder limit(Integer value) { limit = value; return this; }
            /**
             * Sets the after parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder after(String value) { after = value; return this; }
            /**
             * Creates an immutable query and checks scalar bounds.
             * @return the operation-specific query
             * @throws IllegalArgumentException if a scalar bound is violated
             */
            public ListPersonalTranslationsQuery build() { return new ListPersonalTranslationsQuery(this); }
        }
    }

    /** Query parameters accepted by {@code readPersonalBook}. */
    public static final class ReadPersonalBookQuery implements QueryParameters {
        private final Integer limit;
        private final String after;

        private ReadPersonalBookQuery(Builder builder) {
            limit = builder.limit;
            if (limit != null && limit < 1) throw new IllegalArgumentException("limit is below its minimum");

            if (limit != null && limit > 500) throw new IllegalArgumentException("limit is above its maximum");
            after = builder.after;

            if (after != null && after.length() < 1) throw new IllegalArgumentException("after is too short");

            if (after != null && after.length() > 1024) throw new IllegalArgumentException("after is too long");
        }

        /**
         * Creates a builder with all optional values unset.
         * @return a new query builder
         */
        public static Builder builder() { return new Builder(); }

        /**
         * Copies this query into a new builder.
         * @return a builder initialized from this query
         */
        public Builder toBuilder() {
            var builder = new Builder();
            builder.limit = limit;
            builder.after = after;
            return builder;
        }
        /**
         * Returns the limit query value.
         * @return the typed value, or null when unset
         */
        public Integer getLimit() { return limit; }
        /**
         * Returns the after query value.
         * @return the typed value, or null when unset
         */
        public String getAfter() { return after; }

        /** {@inheritDoc} */
        @Override public List<QueryParameter> parameters() {
            var parameters = new ArrayList<QueryParameter>();
            if (limit != null) parameters.add(new QueryParameter("limit", limit.toString()));
            if (after != null) parameters.add(new QueryParameter("after", after.toString()));
            return List.copyOf(parameters);
        }


        /** Builder for {@link ReadPersonalBookQuery}. */
        public static final class Builder {
            private Integer limit;
            private String after;
            private Builder() {}
            /**
             * Sets the limit parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder limit(Integer value) { limit = value; return this; }
            /**
             * Sets the after parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder after(String value) { after = value; return this; }
            /**
             * Creates an immutable query and checks scalar bounds.
             * @return the operation-specific query
             * @throws IllegalArgumentException if a scalar bound is violated
             */
            public ReadPersonalBookQuery build() { return new ReadPersonalBookQuery(this); }
        }
    }

    /** Query parameters accepted by {@code searchPersonalScripture}. */
    public static final class SearchPersonalScriptureQuery implements QueryParameters {
        private final String query;
        private final Integer limit;

        private SearchPersonalScriptureQuery(Builder builder) {
            query = builder.query;

            if (query != null && query.length() < 1) throw new IllegalArgumentException("q is too short");

            if (query != null && query.length() > 256) throw new IllegalArgumentException("q is too long");
            limit = builder.limit;
            if (limit != null && limit < 1) throw new IllegalArgumentException("limit is below its minimum");

            if (limit != null && limit > 100) throw new IllegalArgumentException("limit is above its maximum");
        }

        /**
         * Creates a builder with all optional values unset.
         * @return a new query builder
         */
        public static Builder builder() { return new Builder(); }

        /**
         * Copies this query into a new builder.
         * @return a builder initialized from this query
         */
        public Builder toBuilder() {
            var builder = new Builder();
            builder.query = query;
            builder.limit = limit;
            return builder;
        }
        /**
         * Returns the q query value.
         * @return the typed value, or null when unset
         */
        public String getQuery() { return query; }
        /**
         * Returns the limit query value.
         * @return the typed value, or null when unset
         */
        public Integer getLimit() { return limit; }

        /** {@inheritDoc} */
        @Override public List<QueryParameter> parameters() {
            var parameters = new ArrayList<QueryParameter>();
            if (query != null) parameters.add(new QueryParameter("q", query.toString()));
            if (limit != null) parameters.add(new QueryParameter("limit", limit.toString()));
            return List.copyOf(parameters);
        }


        /** Builder for {@link SearchPersonalScriptureQuery}. */
        public static final class Builder {
            private String query;
            private Integer limit;
            private Builder() {}
            /**
             * Sets the q parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder query(String value) { query = value; return this; }
            /**
             * Sets the limit parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder limit(Integer value) { limit = value; return this; }
            /**
             * Creates an immutable query and checks scalar bounds.
             * @return the operation-specific query
             * @throws IllegalArgumentException if a scalar bound is violated
             */
            public SearchPersonalScriptureQuery build() { return new SearchPersonalScriptureQuery(this); }
        }
    }

    /** Query parameters accepted by {@code uploadPresentationAsset}. */
    public static final class UploadPresentationAssetQuery implements QueryParameters {
        private final Kind kind;

        private UploadPresentationAssetQuery(Builder builder) {
            kind = builder.kind;
        }

        /**
         * Creates a builder with all optional values unset.
         * @return a new query builder
         */
        public static Builder builder() { return new Builder(); }

        /**
         * Copies this query into a new builder.
         * @return a builder initialized from this query
         */
        public Builder toBuilder() {
            var builder = new Builder();
            builder.kind = kind;
            return builder;
        }
        /**
         * Returns the kind query value.
         * @return the typed value, or null when unset
         */
        public Kind getKind() { return kind; }

        /** {@inheritDoc} */
        @Override public List<QueryParameter> parameters() {
            var parameters = new ArrayList<QueryParameter>();
            if (kind != null) parameters.add(new QueryParameter("kind", kind.wireValue()));
            return List.copyOf(parameters);
        }

        /** Supported values of the kind parameter. */
        public enum Kind {

            /** The IMAGE wire value. */
            IMAGE("image"),

            /** The VIDEO wire value. */
            VIDEO("video");

            private final String value;
            Kind(String value) { this.value = value; }
            /**
             * Returns the contract wire representation.
             * @return the wire value
             */
            public String wireValue() { return value; }
        }


        /** Builder for {@link UploadPresentationAssetQuery}. */
        public static final class Builder {
            private Kind kind;
            private Builder() {}
            /**
             * Sets the kind parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder kind(Kind value) { kind = value; return this; }
            /**
             * Creates an immutable query and checks scalar bounds.
             * @return the operation-specific query
             * @throws IllegalArgumentException if a scalar bound is violated
             */
            public UploadPresentationAssetQuery build() { return new UploadPresentationAssetQuery(this); }
        }
    }

    /** Query parameters accepted by {@code listPresentationExports}. */
    public static final class ListPresentationExportsQuery implements QueryParameters {
        private final Integer limit;
        private final String after;

        private ListPresentationExportsQuery(Builder builder) {
            limit = builder.limit;
            if (limit != null && limit < 1) throw new IllegalArgumentException("limit is below its minimum");

            if (limit != null && limit > 100) throw new IllegalArgumentException("limit is above its maximum");
            after = builder.after;

            if (after != null && after.length() > 128) throw new IllegalArgumentException("after is too long");
        }

        /**
         * Creates a builder with all optional values unset.
         * @return a new query builder
         */
        public static Builder builder() { return new Builder(); }

        /**
         * Copies this query into a new builder.
         * @return a builder initialized from this query
         */
        public Builder toBuilder() {
            var builder = new Builder();
            builder.limit = limit;
            builder.after = after;
            return builder;
        }
        /**
         * Returns the limit query value.
         * @return the typed value, or null when unset
         */
        public Integer getLimit() { return limit; }
        /**
         * Returns the after query value.
         * @return the typed value, or null when unset
         */
        public String getAfter() { return after; }

        /** {@inheritDoc} */
        @Override public List<QueryParameter> parameters() {
            var parameters = new ArrayList<QueryParameter>();
            if (limit != null) parameters.add(new QueryParameter("limit", limit.toString()));
            if (after != null) parameters.add(new QueryParameter("after", after.toString()));
            return List.copyOf(parameters);
        }


        /** Builder for {@link ListPresentationExportsQuery}. */
        public static final class Builder {
            private Integer limit;
            private String after;
            private Builder() {}
            /**
             * Sets the limit parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder limit(Integer value) { limit = value; return this; }
            /**
             * Sets the after parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder after(String value) { after = value; return this; }
            /**
             * Creates an immutable query and checks scalar bounds.
             * @return the operation-specific query
             * @throws IllegalArgumentException if a scalar bound is violated
             */
            public ListPresentationExportsQuery build() { return new ListPresentationExportsQuery(this); }
        }
    }

    /** Query parameters accepted by {@code listPresentations}. */
    public static final class ListPresentationsQuery implements QueryParameters {
        private final Integer limit;
        private final String after;

        private ListPresentationsQuery(Builder builder) {
            limit = builder.limit;
            if (limit != null && limit < 1) throw new IllegalArgumentException("limit is below its minimum");

            if (limit != null && limit > 100) throw new IllegalArgumentException("limit is above its maximum");
            after = builder.after;

            if (after != null && after.length() > 128) throw new IllegalArgumentException("after is too long");
        }

        /**
         * Creates a builder with all optional values unset.
         * @return a new query builder
         */
        public static Builder builder() { return new Builder(); }

        /**
         * Copies this query into a new builder.
         * @return a builder initialized from this query
         */
        public Builder toBuilder() {
            var builder = new Builder();
            builder.limit = limit;
            builder.after = after;
            return builder;
        }
        /**
         * Returns the limit query value.
         * @return the typed value, or null when unset
         */
        public Integer getLimit() { return limit; }
        /**
         * Returns the after query value.
         * @return the typed value, or null when unset
         */
        public String getAfter() { return after; }

        /** {@inheritDoc} */
        @Override public List<QueryParameter> parameters() {
            var parameters = new ArrayList<QueryParameter>();
            if (limit != null) parameters.add(new QueryParameter("limit", limit.toString()));
            if (after != null) parameters.add(new QueryParameter("after", after.toString()));
            return List.copyOf(parameters);
        }


        /** Builder for {@link ListPresentationsQuery}. */
        public static final class Builder {
            private Integer limit;
            private String after;
            private Builder() {}
            /**
             * Sets the limit parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder limit(Integer value) { limit = value; return this; }
            /**
             * Sets the after parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder after(String value) { after = value; return this; }
            /**
             * Creates an immutable query and checks scalar bounds.
             * @return the operation-specific query
             * @throws IllegalArgumentException if a scalar bound is violated
             */
            public ListPresentationsQuery build() { return new ListPresentationsQuery(this); }
        }
    }

    /** Query parameters accepted by {@code searchScripture}. */
    public static final class SearchScriptureQuery implements QueryParameters {
        private final String query;
        private final Integer limit;

        private SearchScriptureQuery(Builder builder) {
            query = builder.query;

            if (query != null && query.length() < 1) throw new IllegalArgumentException("q is too short");

            if (query != null && query.length() > 256) throw new IllegalArgumentException("q is too long");
            limit = builder.limit;
            if (limit != null && limit < 1) throw new IllegalArgumentException("limit is below its minimum");

            if (limit != null && limit > 100) throw new IllegalArgumentException("limit is above its maximum");
        }

        /**
         * Creates a builder with all optional values unset.
         * @return a new query builder
         */
        public static Builder builder() { return new Builder(); }

        /**
         * Copies this query into a new builder.
         * @return a builder initialized from this query
         */
        public Builder toBuilder() {
            var builder = new Builder();
            builder.query = query;
            builder.limit = limit;
            return builder;
        }
        /**
         * Returns the q query value.
         * @return the typed value, or null when unset
         */
        public String getQuery() { return query; }
        /**
         * Returns the limit query value.
         * @return the typed value, or null when unset
         */
        public Integer getLimit() { return limit; }

        /** {@inheritDoc} */
        @Override public List<QueryParameter> parameters() {
            var parameters = new ArrayList<QueryParameter>();
            if (query != null) parameters.add(new QueryParameter("q", query.toString()));
            if (limit != null) parameters.add(new QueryParameter("limit", limit.toString()));
            return List.copyOf(parameters);
        }


        /** Builder for {@link SearchScriptureQuery}. */
        public static final class Builder {
            private String query;
            private Integer limit;
            private Builder() {}
            /**
             * Sets the q parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder query(String value) { query = value; return this; }
            /**
             * Sets the limit parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder limit(Integer value) { limit = value; return this; }
            /**
             * Creates an immutable query and checks scalar bounds.
             * @return the operation-specific query
             * @throws IllegalArgumentException if a scalar bound is violated
             */
            public SearchScriptureQuery build() { return new SearchScriptureQuery(this); }
        }
    }

    /** Query parameters accepted by {@code listServiceTokens}. */
    public static final class ListServiceTokensQuery implements QueryParameters {
        private final Integer limit;
        private final String after;

        private ListServiceTokensQuery(Builder builder) {
            limit = builder.limit;
            if (limit != null && limit < 1) throw new IllegalArgumentException("limit is below its minimum");

            if (limit != null && limit > 100) throw new IllegalArgumentException("limit is above its maximum");
            after = builder.after;

            if (after != null && after.length() > 128) throw new IllegalArgumentException("after is too long");
        }

        /**
         * Creates a builder with all optional values unset.
         * @return a new query builder
         */
        public static Builder builder() { return new Builder(); }

        /**
         * Copies this query into a new builder.
         * @return a builder initialized from this query
         */
        public Builder toBuilder() {
            var builder = new Builder();
            builder.limit = limit;
            builder.after = after;
            return builder;
        }
        /**
         * Returns the limit query value.
         * @return the typed value, or null when unset
         */
        public Integer getLimit() { return limit; }
        /**
         * Returns the after query value.
         * @return the typed value, or null when unset
         */
        public String getAfter() { return after; }

        /** {@inheritDoc} */
        @Override public List<QueryParameter> parameters() {
            var parameters = new ArrayList<QueryParameter>();
            if (limit != null) parameters.add(new QueryParameter("limit", limit.toString()));
            if (after != null) parameters.add(new QueryParameter("after", after.toString()));
            return List.copyOf(parameters);
        }


        /** Builder for {@link ListServiceTokensQuery}. */
        public static final class Builder {
            private Integer limit;
            private String after;
            private Builder() {}
            /**
             * Sets the limit parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder limit(Integer value) { limit = value; return this; }
            /**
             * Sets the after parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder after(String value) { after = value; return this; }
            /**
             * Creates an immutable query and checks scalar bounds.
             * @return the operation-specific query
             * @throws IllegalArgumentException if a scalar bound is violated
             */
            public ListServiceTokensQuery build() { return new ListServiceTokensQuery(this); }
        }
    }

    /** Query parameters accepted by {@code listBooks}. */
    public static final class ListBooksQuery implements QueryParameters {
        private final Integer limit;
        private final String after;

        private ListBooksQuery(Builder builder) {
            limit = builder.limit;
            if (limit != null && limit < 1) throw new IllegalArgumentException("limit is below its minimum");

            if (limit != null && limit > 100) throw new IllegalArgumentException("limit is above its maximum");
            after = builder.after;

            if (after != null && after.length() > 128) throw new IllegalArgumentException("after is too long");
        }

        /**
         * Creates a builder with all optional values unset.
         * @return a new query builder
         */
        public static Builder builder() { return new Builder(); }

        /**
         * Copies this query into a new builder.
         * @return a builder initialized from this query
         */
        public Builder toBuilder() {
            var builder = new Builder();
            builder.limit = limit;
            builder.after = after;
            return builder;
        }
        /**
         * Returns the limit query value.
         * @return the typed value, or null when unset
         */
        public Integer getLimit() { return limit; }
        /**
         * Returns the after query value.
         * @return the typed value, or null when unset
         */
        public String getAfter() { return after; }

        /** {@inheritDoc} */
        @Override public List<QueryParameter> parameters() {
            var parameters = new ArrayList<QueryParameter>();
            if (limit != null) parameters.add(new QueryParameter("limit", limit.toString()));
            if (after != null) parameters.add(new QueryParameter("after", after.toString()));
            return List.copyOf(parameters);
        }


        /** Builder for {@link ListBooksQuery}. */
        public static final class Builder {
            private Integer limit;
            private String after;
            private Builder() {}
            /**
             * Sets the limit parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder limit(Integer value) { limit = value; return this; }
            /**
             * Sets the after parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder after(String value) { after = value; return this; }
            /**
             * Creates an immutable query and checks scalar bounds.
             * @return the operation-specific query
             * @throws IllegalArgumentException if a scalar bound is violated
             */
            public ListBooksQuery build() { return new ListBooksQuery(this); }
        }
    }

    /** Query parameters accepted by {@code listOrganizationTranslations}. */
    public static final class ListOrganizationTranslationsQuery implements QueryParameters {
        private final Integer limit;
        private final String after;

        private ListOrganizationTranslationsQuery(Builder builder) {
            limit = builder.limit;
            if (limit != null && limit < 1) throw new IllegalArgumentException("limit is below its minimum");

            if (limit != null && limit > 100) throw new IllegalArgumentException("limit is above its maximum");
            after = builder.after;

            if (after != null && after.length() > 128) throw new IllegalArgumentException("after is too long");
        }

        /**
         * Creates a builder with all optional values unset.
         * @return a new query builder
         */
        public static Builder builder() { return new Builder(); }

        /**
         * Copies this query into a new builder.
         * @return a builder initialized from this query
         */
        public Builder toBuilder() {
            var builder = new Builder();
            builder.limit = limit;
            builder.after = after;
            return builder;
        }
        /**
         * Returns the limit query value.
         * @return the typed value, or null when unset
         */
        public Integer getLimit() { return limit; }
        /**
         * Returns the after query value.
         * @return the typed value, or null when unset
         */
        public String getAfter() { return after; }

        /** {@inheritDoc} */
        @Override public List<QueryParameter> parameters() {
            var parameters = new ArrayList<QueryParameter>();
            if (limit != null) parameters.add(new QueryParameter("limit", limit.toString()));
            if (after != null) parameters.add(new QueryParameter("after", after.toString()));
            return List.copyOf(parameters);
        }


        /** Builder for {@link ListOrganizationTranslationsQuery}. */
        public static final class Builder {
            private Integer limit;
            private String after;
            private Builder() {}
            /**
             * Sets the limit parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder limit(Integer value) { limit = value; return this; }
            /**
             * Sets the after parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder after(String value) { after = value; return this; }
            /**
             * Creates an immutable query and checks scalar bounds.
             * @return the operation-specific query
             * @throws IllegalArgumentException if a scalar bound is violated
             */
            public ListOrganizationTranslationsQuery build() { return new ListOrganizationTranslationsQuery(this); }
        }
    }

    /** Query parameters accepted by {@code listTranslationBooks}. */
    public static final class ListTranslationBooksQuery implements QueryParameters {
        private final Integer limit;
        private final String after;

        private ListTranslationBooksQuery(Builder builder) {
            limit = builder.limit;
            if (limit != null && limit < 1) throw new IllegalArgumentException("limit is below its minimum");

            if (limit != null && limit > 100) throw new IllegalArgumentException("limit is above its maximum");
            after = builder.after;

            if (after != null && after.length() > 128) throw new IllegalArgumentException("after is too long");
        }

        /**
         * Creates a builder with all optional values unset.
         * @return a new query builder
         */
        public static Builder builder() { return new Builder(); }

        /**
         * Copies this query into a new builder.
         * @return a builder initialized from this query
         */
        public Builder toBuilder() {
            var builder = new Builder();
            builder.limit = limit;
            builder.after = after;
            return builder;
        }
        /**
         * Returns the limit query value.
         * @return the typed value, or null when unset
         */
        public Integer getLimit() { return limit; }
        /**
         * Returns the after query value.
         * @return the typed value, or null when unset
         */
        public String getAfter() { return after; }

        /** {@inheritDoc} */
        @Override public List<QueryParameter> parameters() {
            var parameters = new ArrayList<QueryParameter>();
            if (limit != null) parameters.add(new QueryParameter("limit", limit.toString()));
            if (after != null) parameters.add(new QueryParameter("after", after.toString()));
            return List.copyOf(parameters);
        }


        /** Builder for {@link ListTranslationBooksQuery}. */
        public static final class Builder {
            private Integer limit;
            private String after;
            private Builder() {}
            /**
             * Sets the limit parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder limit(Integer value) { limit = value; return this; }
            /**
             * Sets the after parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder after(String value) { after = value; return this; }
            /**
             * Creates an immutable query and checks scalar bounds.
             * @return the operation-specific query
             * @throws IllegalArgumentException if a scalar bound is violated
             */
            public ListTranslationBooksQuery build() { return new ListTranslationBooksQuery(this); }
        }
    }

    /** Query parameters accepted by {@code listTranslationCatalogue}. */
    public static final class ListTranslationCatalogueQuery implements QueryParameters {
        private final Integer limit;
        private final String after;

        private ListTranslationCatalogueQuery(Builder builder) {
            limit = builder.limit;
            if (limit != null && limit < 1) throw new IllegalArgumentException("limit is below its minimum");

            if (limit != null && limit > 100) throw new IllegalArgumentException("limit is above its maximum");
            after = builder.after;

            if (after != null && after.length() > 128) throw new IllegalArgumentException("after is too long");
        }

        /**
         * Creates a builder with all optional values unset.
         * @return a new query builder
         */
        public static Builder builder() { return new Builder(); }

        /**
         * Copies this query into a new builder.
         * @return a builder initialized from this query
         */
        public Builder toBuilder() {
            var builder = new Builder();
            builder.limit = limit;
            builder.after = after;
            return builder;
        }
        /**
         * Returns the limit query value.
         * @return the typed value, or null when unset
         */
        public Integer getLimit() { return limit; }
        /**
         * Returns the after query value.
         * @return the typed value, or null when unset
         */
        public String getAfter() { return after; }

        /** {@inheritDoc} */
        @Override public List<QueryParameter> parameters() {
            var parameters = new ArrayList<QueryParameter>();
            if (limit != null) parameters.add(new QueryParameter("limit", limit.toString()));
            if (after != null) parameters.add(new QueryParameter("after", after.toString()));
            return List.copyOf(parameters);
        }


        /** Builder for {@link ListTranslationCatalogueQuery}. */
        public static final class Builder {
            private Integer limit;
            private String after;
            private Builder() {}
            /**
             * Sets the limit parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder limit(Integer value) { limit = value; return this; }
            /**
             * Sets the after parameter; null omits it from the request.
             * @param value the typed parameter value
             * @return this builder
             */
            public Builder after(String value) { after = value; return this; }
            /**
             * Creates an immutable query and checks scalar bounds.
             * @return the operation-specific query
             * @throws IllegalArgumentException if a scalar bound is violated
             */
            public ListTranslationCatalogueQuery build() { return new ListTranslationCatalogueQuery(this); }
        }
    }

}
