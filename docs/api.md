# Java API conventions

`BibleitClient` is the resource client, `SshClient` executes validated SSH
commands, `OAuthClient` handles registered delegated OAuth, and `AuthClient`
handles official CLI browser-code exchange. Constructors accept a `Transport`
when applications need a trusted test or network adapter.

`getPersonalIdentity(options)` calls the personal identity resource; every REST wrapper also has an overload using
`Options.defaults()`. Path parameters retain URL order. Options follow path
parameters. Use Java interruption and request timeouts to control request execution.
Methods throw `IOException` and `InterruptedException`; HTTP errors are
`ProblemException`, OAuth errors `OAuthException`, and SSH errors
`ServerException`. Errors retain unknown server codes. Messages omit server detail.

## Request and response options

```java
var request = new Models.CreateLive();
request.setName("Sunday Service");
try (var response = client.createLive(organizationId,
        Options.builder().json(request).idempotencyKey(explicitKey).build())) {
    var live = response.decode(Models.Live.class);
}
```

`Options.builder()` accepts:

| Method | Meaning |
| --- | --- |
| `json(value)` | Java request object; null explicitly sends JSON null |
| `body(stream, exactLength, mediaType)` | One-use raw upload stream |
| `ifMatch(etag)` | Exact ETag for conditional writes |
| `idempotencyKey(key)` | Explicit operation-supported key |
| `destinationToken(token)` | Destination service token for workspace moves |
| `range(value)` | Binary response range |
| `timeout(duration)` | Request timeout; default 40 seconds |

JSON and raw bodies are mutually exclusive. Headers remain accessible through
`response.headers()`; binary contents through `response.body()`. Always close the
response. The caller owns upload streams. Options are immutable; their raw stream
is still one-use. `Response.decode(Models.Schema.class)` returns a typed Java bean. For
parameterized collections, use `decode(new TypeReference<List<Models.Verse>>() {})`.
The response decoder always requires an explicit Java target type.

## Typed queries

Each operation with query parameters has a generated immutable query type and
builder in `Queries`. Method overloads accept that operation's query type;
`Options` contains only shared HTTP settings. Unknown query keys and mismatched
query types are not accepted by those typed overloads.

```java
var query = Queries.SearchPersonalScriptureQuery.builder()
    .query("love")
    .limit(20)
    .build();
try (var response = client.searchPersonalScripture(accountId, "web", query)) {
    var results = response.decode(Models.ScriptureSearch.class);
}
```

Limits and wait durations use `Integer`; URI parameters use `URI`; constrained
values use generated enums. Null leaves a value unset. Query builders validate
scalar bounds, while the server enforces required fields and resource authority.
Encoding uses ordered `QueryParameter` values, with no query maps. The low-level
`call` overload accepts the `QueryParameters` interface for generic integrations.

## Models and contract

`Models` contains typed Java beans for the OpenAPI schemas, plus the browser
exchange result. Properties are ordinary Java fields with `getName()` /
`setName(value)` accessors. Nested getters return the same Java objects, so changes
to a nested object remain part of its parent object graph.

A new bean serializes as an empty object. Calling a setter with null records an
explicit JSON null. `hasField` distinguishes missing fields from explicit null;
`removeField` clears a property and omits it from the wire. `setField` assigns a
known typed field or retains a future extension value. `additionalProperties()`
exposes unknown fields as ordinary Java values; `toMap()` provides an unmodifiable
property snapshot with normal nested-object reference semantics.

Scalar schemas have a typed Java scalar constructor and `value()` accessor.
Object unions expose their known variant fields in a typed bean without invented
wire wrapper fields. Nullable object and array variants use nullable Java model
references and lists. The server validates variant-specific required fields.

Jackson handles serialization and deserialization directly. Numeric precision is
retained for unknown extension values; integer fields reject fractional values,
and scalar values are not silently converted from strings or numbers of another
kind. Enums and constraints remain in the bundled `/app/bibleit/openapi.json`.
The schema beans do not replace server authorization or business validation.

`BibleitClient.operations()` and `operationNames()` expose immutable operation
metadata. `call(operationId, pathParameters, options)` is the equivalent generic
entry point. The generated wrappers use exactly those operations.

To update, replace `contract/openapi.json` with a reviewed server export, run
`./gradlew generateCode`, review its diff, and run `./gradlew build`.
No sibling repository is needed for builds or regeneration.

## Pagination, polling and cancellation

`readPersonalBookPages` and `readBookPages` return `BookPages`, an
`Iterable<Models.ScriptureBookRead>`. Jackson deserializes each page into its Java
model, including typed `Models.Verse` entries. Each iterator starts an independent
lazy traversal:

```java
for (var page : client.readPersonalBookPages(accountId, "web", "43", Options.defaults())) {
    for (var verse : page.getVerses()) {
        System.out.println(verse.getText());
    }
}
```

Repeated `hasNext()` calls cache the next page. No request is made until an
iterator is advanced, and each response is closed before the page is returned.
Breaking the loop stops fetching. A fresh enhanced `for` loop starts again from
the original options. Cursor, count, rights, translation and ordered verse
coordinates are validated before yielding each page.

Iterator methods throw `UncheckedIOException` on I/O failures. Interruption is
wrapped in an `InterruptedIOException`, and the thread's interrupt flag is
restored. A failed iterator is exhausted; partial results are incomplete.

`pollLiveEvents(liveId)` starts a fresh poll with the default 25-second wait.
`pollLiveEvents(liveId, waitSeconds)` chooses a 0–25 second wait without a cursor.
`pollLiveEvents(liveId, after)` resumes with the default wait; the three-argument
overload specifies both values. Each returns a typed `Models.LiveEvent`, with a
`Models.LiveEventSnapshot` from `getData()` or null for a heartbeat. Nested Live,
display, queue, viewer settings, and design fields are Java model objects.
Cursors are opaque and are not ETags.
The SDK starts no polling task or retry loop.

JDK `HttpClient` sends synchronous requests and honors interruption while sending.
Callers must close returned streaming responses. Request timeout covers receipt
of the HTTP response; body reads after headers use the JDK stream and can block.
For a deadline spanning body consumption, arrange stream closure/cancellation in
the application or provide an application-managed transport. `BookPages` checks
interruption before each fetch. SSH executors own interruption during execution.

## Transport boundary

`Transport.send(HttpRequest)` returns a `Response`. Custom transports are trusted:
they own TLS verification, cancellation, concurrency, redirects and replay policy.
The default uses the JDK HTTP/1.1 client with TLS verification, no redirects, no
cookies, and a 10-second connection timeout. The SDK calls `send` once per
operation. The JDK can recover connection-establishment failures and retry
idempotent requests internally; there is no SDK retry policy. Never manually
replay ambiguous OAuth exchanges or single-use refresh attempts.

Clients may be shared when their transport supports concurrent sends; individual
responses, upload streams and `BookPages` readers belong to one consumer.
No base URL, environment credential discovery, CLI config, SSH connection setup,
browser orchestration, or command-to-REST adapter is provided.

## SSH results

`ping`, `identity`, `read`, `search`, translation add/remove/list and `execute`
return `CommandResponse`. Domain methods such as `getIdentity`, `readVerses`,
`getTranslationCatalog`, `listTokens`, `listOrganizations`, `getLiveStats`, and
`getLiveStack` return records in `SshModels`. Each result retains `raw()`; entries
retain `fields()`. Nullable optional fields preserve absent metadata. Timestamp
and numeric fields use Java `long` or nullable `Long` with checked parsing.

Use `command.wire()` explicitly to execute over SSH. `toString()` redacts wire
values. Request quoting preserves literal backslashes and rejects quotes,
newlines and NUL. Response escaping is distinct. `Protocol.decodeResponse`
requires END for collection replies; use `decodeCommandResponse` for
command-specific single-record account-translation framing. Finite executors
reject subscription commands. Typed decoders validate record kinds, counts,
coordinates and duplicate identifiers without interpreting opaque terms.

## Convenience defaults

`new BibleitClient()` creates a public-discovery client. Whole-book methods accept
just their account/organization, translation and book identifiers when the
50-verse page size and standard HTTP settings are sufficient.

`AuthClient.exchangeCode(code, verifier)` omits an optional device label and returns
`Models.AuthorizationResult`. `OAuthClient.refresh(clientId, refreshToken)` retains
the grant's existing scopes. Credentials and PKCE values remain explicit.

SSH helpers provide no-argument root help, first-page organization listings and
unnamed Live creation. Pop helpers omit the optional count, and secret management
accepts an action without an unused secret value. `Reference` constructors accept
a whole book or chapter without zero-valued trailing arguments.

## Migrating from 0.0.1

Decode responses into an explicit Java class or Jackson `TypeReference`. Use model
getters for events and authorization results. Model fields now have ordinary Java
object identity; `toMap()` replaces the old tree representation. Scalar model
constructors take their Java scalar type, and object models use setters or Jackson
deserialization. HTTP and OAuth exceptions expose typed problem objects.
