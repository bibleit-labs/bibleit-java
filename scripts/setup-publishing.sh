#!/usr/bin/env bash
# Run locally in a trusted terminal. Secrets are read silently and sent via stdin.
set -euo pipefail
set +x
repo='bibleit-labs/bibleit-java'
environment='maven-central'
identity='Bibleit Labs'
for command in gh gpg; do command -v "$command" >/dev/null || { echo "Install $command first." >&2; exit 1; }; done
gh auth status >/dev/null
gh api "repos/$repo" --jq '.full_name'
printf 'Central user-token username: ' >&2
IFS= read -r -s central_username
printf '\nCentral user-token password: ' >&2
IFS= read -r -s central_password
printf '\nSigning-key passphrase (also used to export the key): ' >&2
IFS= read -r -s signing_passphrase
printf '\n' >&2
trap 'unset central_username central_password signing_passphrase' EXIT
if [[ -z "$central_username" || -z "$central_password" || -z "$signing_passphrase" ]]; then
    echo 'All credentials and a nonempty signing passphrase are required.' >&2; exit 1
fi
if ! gpg --batch --list-secret-keys "$identity" >/dev/null 2>&1; then
    printf '%s\n' "$signing_passphrase" | gpg --batch --pinentry-mode loopback --passphrase-fd 0 \
        --quick-generate-key "$identity" rsa3072 sign 2y
fi
fingerprint=$(gpg --batch --with-colons --list-secret-keys "$identity" 2>/dev/null | awk -F: '$1 == "fpr" { print $10; exit }')
[[ -n "$fingerprint" ]] || { echo 'No signing key found.' >&2; exit 1; }
# Prove the passphrase works before changing any remote secrets.
printf 'Bibleit signing verification\n' | gpg --batch --yes --pinentry-mode loopback \
    --passphrase-fd 3 --local-user "$fingerprint" --detach-sign --output /dev/null \
    3< <(printf '%s\n' "$signing_passphrase")
gpg --keyserver hkps://keyserver.ubuntu.com --send-keys "$fingerprint"
gpg --keyserver hkps://keys.openpgp.org --send-keys "$fingerprint"
gh api --method PUT "repos/$repo/environments/$environment" >/dev/null
printf '%s' "$central_username" | gh secret set MAVEN_CENTRAL_USERNAME --repo "$repo" --env "$environment"
printf '%s' "$central_password" | gh secret set MAVEN_CENTRAL_PASSWORD --repo "$repo" --env "$environment"
printf '%s' "$signing_passphrase" | gh secret set MAVEN_GPG_PASSPHRASE --repo "$repo" --env "$environment"
gpg --batch --pinentry-mode loopback --passphrase-fd 3 --armor --export-secret-keys "$fingerprint" \
    3< <(printf '%s\n' "$signing_passphrase") | gh secret set MAVEN_GPG_PRIVATE_KEY --repo "$repo" --env "$environment"
printf 'Configured %s environment secrets. Public signing fingerprint: %s\n' "$environment" "$fingerprint"
echo 'Back up the signing key and its revocation certificate securely.'
