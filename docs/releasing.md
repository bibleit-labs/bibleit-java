# Maven Central releases

Coordinates: `app.bibleit:bibleit-java`. The POM is prepared for `0.0.1`;
this document does not assert that it has been published.

## One-time setup

1. Sign in at [Central Portal](https://central.sonatype.com/publishing/namespaces).
   Register `app.bibleit`. Add its assigned verification key as a TXT record at
   the root `bibleit.app` (DNS name `@`). Confirm public DNS with
   `dig +short TXT bibleit.app`, then select **Verify Namespace**.
   Wait until the portal shows **Verified**. Retain the TXT record.
2. In the Central Portal avatar menu, open your account and generate a **user
   token**. Keep both the token username and password; they differ from your
   normal sign-in credentials. Do not paste either into chat or source control.
3. Authenticate GitHub CLI with `gh auth login`. Run
   `bash scripts/setup-publishing.sh` locally. The script prompts for the Central
   credentials, creates a dedicated passphrase-protected RSA signing key named
   `Bibleit Labs` (or reuses that exact identity), publishes its public key and
   stores four secrets in the repository's `maven-central` environment:

   | Secret | Value |
   | --- | --- |
   | `MAVEN_CENTRAL_USERNAME` | Central user-token username |
   | `MAVEN_CENTRAL_PASSWORD` | Central user-token password |
   | `MAVEN_GPG_PRIVATE_KEY` | ASCII-armored secret signing key |
   | `MAVEN_GPG_PASSPHRASE` | Signing key passphrase |

   The script never writes a private-key export into the repository. Securely
   back up the signing key and revocation certificate outside the checkout.
   Configure required reviewers on `maven-central` if desired. Protect release
   tags and restrict who can modify publishing workflows.

References: [namespace verification](https://central.sonatype.org/register/namespace/),
[Central publishing plugin](https://central.sonatype.org/publish/publish-portal-maven/),
[GPG signing requirements](https://central.sonatype.org/publish/requirements/gpg/).

## Prepare and publish

1. Run `./mvnw verify`, `python3 scripts/generate.py` and check generated diffs.
   Run `docker build -t bibleit-java:verify .` for Java 27 Linux validation.
2. Update the POM version and changelog. Commit using Conventional Commits.
   For `0.0.1`, keep the existing POM version.
3. Merge the reviewed changes onto `main` and ensure CI passes.
4. Tag that commit and push:

   ```sh
   git tag -a v0.0.1 -m 'chore(release): 0.0.1'
   git push origin v0.0.1
   ```

   The tag workflow checks that the tag matches the POM and belongs to `main`,
   then runs `./mvnw -Prelease deploy`: tests, library/sources/Javadoc JARs,
   GPG signatures and upload through Central Publisher Portal. It automatically
   publishes only after validation and waits for publication. Manual dispatch
   accepts an existing release tag, using the same checks.
5. Inspect the Actions result and Central deployment. Confirm resolution of
   `app.bibleit:bibleit-java:0.0.1` from a clean Maven cache. Indexing can lag.
   Only then announce the release, make the GitHub repository public, and update
   bibleit-server documentation as planned by the maintainer.

Published coordinates are immutable. Never reuse a version after publication.
An ambiguous or failed run requires inspecting Central before rerunning; automatic
workflow concurrency prevents overlapping releases but does not make reruns safe.

## Local release bundle check

With a local signing key, the release profile can build a signed upload bundle
without publishing:

```sh
./mvnw -Prelease -Dcentral.skipUpload=true deploy
```

For a full local publication, supply a Maven `settings.xml` server named `central`
with user-token credentials. Prefer CI secret storage for regular releases.
Never commit settings containing credentials. Normal `verify` builds do not load
the Central publishing extension or need signing credentials.
