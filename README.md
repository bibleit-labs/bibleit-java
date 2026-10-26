# Bibleit Java client

A Java SDK for [Bibleit](https://bibleit.app), providing Scripture reading,
search, Live resources, OAuth authentication, and SSH protocol helpers.

## Installation

Maven coordinates: `app.bibleit:bibleit-java`.

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("app.bibleit:bibleit-java:0.0.1")
}
```

Java 27 or newer is required. The package is `app.bibleit`.

## REST resources

All **95** operations and **105** schemas in the checked-in OpenAPI contract are
available. Credentials are explicit: personal `bt_`, delegated `oa_`, organization
`svc_`, or an empty string for public discovery. The server enforces permissions.

```java
import app.bibleit.BibleitClient;
import app.bibleit.Models;

var client = new BibleitClient(token);
try (var response = client.getPersonalIdentity()) {
    var identity = response.decode(Models.PersonalIdentity.class);
    System.out.println(identity.getId());
}
```

Use `Options.builder()` for JSON, query values, conditional writes, idempotency
keys, ranges and streaming uploads. Always close responses with
try-with-resources, including after decoding. Binary bodies stay streamed;
JSON decoding is limited to 4 MiB. `ProblemException` preserves HTTP status,
code, request ID, headers and original problem fields.

The REST origin is fixed to `https://api.bibleit.app/v1`. Browser and OAuth
requests use `https://bibleit.app`. The default transport verifies TLS, has no
cookie store and refuses redirects. SDK operations call the transport once;
there are no application retry loops. See [API and transport details](docs/api.md).

## Whole-book reading and Live events

```java
import app.bibleit.Options;

var pages = client.readPersonalBookPages(accountId, "web", "43", Options.defaults());
for (var page : pages) {
    System.out.println(page.path("verses"));
}
var event = client.pollLiveEvents(liveId, "", 25);
```

Page traversal is lazy, defaults to 50 verses (1–500 configurable), closes each
response before returning, and rejects cursor loops or inconsistent pages.
Stopping calls stops fetching. Treat any failed traversal as incomplete.
Event polling performs one request; run polling separately from mutations.

## OAuth and browser login

`OAuthClient.authorizationUrl(...)`, `metadata()`, `exchangeCode(...)`,
`refresh(...)` and `revoke(...)` implement registered delegated OAuth with S256
PKCE. `AuthClient.exchangeCode(...)` exchanges official CLI browser codes;
`BibleitClient.revokeCredential()` revokes the resulting browser credential.
Applications validate callback state, open browsers and securely store/rotate
credentials. The SDK does not read environment credentials or manage storage.

## SSH protocol helpers

```java
import app.bibleit.Protocol;
import app.bibleit.Reference;
import app.bibleit.SshClient;

var ssh = new SshClient(command -> {
    // Supply your own verified SSH connection and execute command.wire().
    // stdout is the command's InputStream; the application owns its lifetime.
    return Protocol.decodeCommandResponse(command, stdout);
});
var reading = ssh.readVerses(new Reference("web", "43", 3, 16));
System.out.println(reading.verses().getFirst().text());
```

`Commands` constructs validated protocol v1 commands; `Protocol` decodes finite
responses. `SshClient` adds typed discovery, reading, translations, token metadata,
organization and Live results. Unknown fields remain in `raw()` or `fields()`.
The executor owns authentication, host-key verification, cancellation and
connection lifetime. Streaming subscriptions need a caller-owned connection.
Wire commands and raw fields can contain secrets; their `toString()` is redacted.

## Development

```sh
sdk env                      # optional: selects the installed OpenJDK 27
./gradlew build              # tests, library, sources and Javadoc JARs
python3 scripts/generate.py  # regenerate checked-in wrappers and models
# Linux validation using the official Java 27 image:
docker build -t bibleit-java:verify .
```

See [CONTRIBUTING.md](CONTRIBUTING.md), [SECURITY.md](SECURITY.md),
[contract provenance](contract/README.md) and [publishing setup](docs/releasing.md).
MIT licensed; retain [LICENSE](LICENSE) and [THIRD_PARTY_NOTICES](THIRD_PARTY_NOTICES).
