# Changelog

## 0.1.0 — 2026-11-02

- Use response-only `OAuthGrantedScope` for owner grant scopes, including MCP
  staging and rename values; retain the ordinary authorization scope contract.
- **Migration:** update `OAuthGrant` scope getters/setters from `List<OAuthScope>`
  to `List<OAuthGrantedScope>` and recompile dependent applications. Precompiled
  callers that cast scope elements to `OAuthScope` must also be upgraded. JSON
  representations remain unchanged.
- Refresh the canonical v1 contract and all 111 schema models. Add the safe-staging
  REST operation, retain seven existing protocol wrappers, and exclude new
  browser/MCP protocol routes from the ordinary REST origin.
- Verify response decoding, request/response scope separation, all 96 generated
  methods, deterministic generation, native builds and strict Javadoc.

## 0.0.2 — 2026-10-26

- Replace tree-backed models with typed Java fields and direct Jackson binding.
- Return typed Live events, nested snapshot payloads, authorization results, and error details.
- Add cursor-free/default-wait polling, default book paging, and optional auth/SSH overloads.
- Support explicit JSON null request bodies without a tree value.
- Preserve missing/null distinction, unknown fields, exact integers, and pagination checks.
- API changes: response decoding requires a Java target type; model wire views use `toMap()`.

## 0.0.1 — 2026-10-26

- Initial Java 27 SDK with 95 REST operations and 105 schema models.
- Streaming responses/uploads, problem metadata, conditional writes and options.
- Lazy iterable book pagination, versioned Live polling, registered OAuth and browser codes.
- SSH command builders, strict protocol v1 decoding and typed domain results.
- Gradle Wrapper, Java 27 Docker validation, cross-platform CI and signed Central publishing.
- Typed operation-specific query builders generated with OpenAPI Generator.
- Explicit Java imports and complete public API Javadocs, checked during builds.
