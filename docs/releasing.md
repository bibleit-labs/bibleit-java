# Releasing to Maven Central

The library is published as `app.bibleit:bibleit-java` using Gradle and the
[Gradle Maven Publish Plugin](https://vanniktech.github.io/gradle-maven-publish-plugin/central/).

## Maintainer setup

Register the `app.bibleit` namespace in [Central Portal](https://central.sonatype.com).
Prove control of `bibleit.app` using the assigned root-domain DNS TXT record.
Wait until the namespace is verified before publishing.

Generate a Central **user token**, keeping its token username and password
separate from account sign-in credentials. In an authenticated local terminal,
run `bash scripts/setup-publishing.sh`. This creates or reuses a passphrase-protected
`Bibleit Labs` signing key and configures the repository's `maven-central`
GitHub Actions environment:

| Secret | Value |
| --- | --- |
| `MAVEN_CENTRAL_USERNAME` | Central user-token username |
| `MAVEN_CENTRAL_PASSWORD` | Central user-token password |
| `MAVEN_GPG_PRIVATE_KEY` | ASCII-armored secret signing key |
| `MAVEN_GPG_PASSPHRASE` | Signing key passphrase |

The workflow maps these secrets to the publishing plugin's Gradle environment
properties. It uses in-memory signing; no private-key file enters the checkout.
Keep credentials out of source, issues and logs. Back up the signing key and
revocation certificate securely. Configure environment reviewers and tag
protection according to the project's release policy.

Publish the **public** key to a
[Central-supported keyserver](https://central.sonatype.org/publish/requirements/gpg/)
and verify retrieval by its full fingerprint before releasing. Keyserver
propagation can delay validation; a successful upload alone does not establish
that Central can retrieve it. Never share the private key or passphrase publicly.

## Release process

1. Run `./gradlew clean build`, `python3 scripts/generate.py` and
   `docker build -t bibleit-java:verify .`.
2. Set `version` in `gradle.properties` and update `CHANGELOG.md`.
3. Commit using Conventional Commits, merge to `main` and ensure CI passes.
4. Create and push an annotated release tag matching that version:

   ```sh
   git tag -a v0.0.1 -m 'chore(release): 0.0.1'
   git push origin v0.0.1
   ```

5. The publishing workflow validates credentials, the tag, version and ancestry,
   then runs `./gradlew build publishAndReleaseToMavenCentral`. Gradle generates
   the POM, library, sources and Javadoc JARs, signs them and submits them to
   Central. Inspect both the Actions result and Central deployment status.
6. Verify resolution from a clean dependency cache before announcing the release.
   Central indexing and repository availability may lag publication.

Manual dispatch accepts an existing release tag and applies the same checks.
A failed or ambiguous run requires inspecting Central before retrying. Published
coordinates are immutable; never replace a published version or move a published
release tag.

## Local publication checks

`./gradlew build generatePomFileForMavenPublication` validates the unsigned
artifacts and POM without publishing. With the signing environment configured,
`./gradlew publishToMavenLocal` also exercises signing against a local repository.
Normal builds and tests do not require credentials.
