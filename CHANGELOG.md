# Changelog

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
