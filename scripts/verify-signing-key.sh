#!/usr/bin/env bash
# Verify public key availability before submitting signed artifacts to Central.
set -euo pipefail
set +x

# Capture only the metadata input we need, as an unexported shell variable.
unset signing_key
if [[ $# -eq 0 ]]; then
    signing_key=${MAVEN_GPG_PRIVATE_KEY:-${ORG_GRADLE_PROJECT_signingInMemoryKey:-}}
fi
# Neither the keyserver client nor metadata parser needs publishing or GitHub
# credentials in its process environment. This does not modify the caller's env.
unset MAVEN_GPG_PRIVATE_KEY MAVEN_GPG_PASSPHRASE MAVEN_CENTRAL_USERNAME MAVEN_CENTRAL_PASSWORD
unset ORG_GRADLE_PROJECT_signingInMemoryKey ORG_GRADLE_PROJECT_signingInMemoryKeyPassword
unset ORG_GRADLE_PROJECT_mavenCentralUsername ORG_GRADLE_PROJECT_mavenCentralPassword
unset GH_TOKEN GITHUB_TOKEN GH_ENTERPRISE_TOKEN GITHUB_ENTERPRISE_TOKEN

for command in curl gpg; do
    command -v "$command" >/dev/null || { echo "Install $command first." >&2; exit 1; }
done

workdir=$(mktemp -d "${TMPDIR:-/tmp}/bibleit-signing.XXXXXX")
trap 'rm -rf "$workdir"' EXIT
mkdir -m 700 "$workdir/gnupg"

primary_fingerprints() {
    gpg --homedir "$workdir/gnupg" --batch --with-colons --show-keys 2>/dev/null \
        | awk -F: '$1 == "pub" || $1 == "sec" { primary = 1; next } primary && $1 == "fpr" { print $10; primary = 0 }'
}

if [[ $# -gt 1 ]]; then
    echo 'Usage: verify-signing-key.sh [public-primary-fingerprint]' >&2
    exit 1
elif [[ $# -eq 1 ]]; then
    fingerprint=$(printf '%s' "$1" | tr '[:lower:]' '[:upper:]')
else
    if [[ -z "$signing_key" ]]; then
        echo 'Provide a public fingerprint or the signing key through the existing CI environment variable.' >&2
        exit 1
    fi
    # --show-keys inspects only metadata. No decryption or private-key import occurs.
    fingerprint=$(printf '%s\n' "$signing_key" | primary_fingerprints)
    unset signing_key
fi

if [[ ! "$fingerprint" =~ ^[A-F0-9]{40}$ ]]; then
    echo 'Exactly one OpenPGP v4 primary signing key is required.' >&2
    exit 1
fi

# Central supports this keyserver. A send-keys acknowledgement alone does not
# establish that a subsequent public lookup can retrieve the key.
curl --fail --silent --show-error --connect-timeout 10 --max-time 20 \
    --retry 3 --retry-delay 5 --retry-all-errors --retry-max-time 60 \
    "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x$fingerprint" \
    --output "$workdir/public.asc"
remote_fingerprint=$(primary_fingerprints < "$workdir/public.asc")
if [[ "$remote_fingerprint" != "$fingerprint" ]]; then
    echo 'The keyserver response did not match the configured signing fingerprint.' >&2
    exit 1
fi
printf 'Verified public signing key availability: %s\n' "$fingerprint"
