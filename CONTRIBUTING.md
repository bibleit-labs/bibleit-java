# Contributing

Use Java 27 and the checked-in Maven Wrapper (`./mvnw verify`). Docker validation
runs `docker build -t bibleit-java:verify .`. Tests use synthetic responses and
shared protocol fixtures; they make no production API requests.

Regenerate REST wrappers/models with `python3 scripts/generate.py`; commit the
contract and generated changes together. Preserve fixed origins, TLS verification,
explicit credentials, bounded JSON, resource closure and no SDK retry loops.
The SSH application owns host verification and connection lifetime.

Use Conventional Commits, for example `feat(client): add Java resource SDK` or
`fix(protocol): reject truncated records`. Branches must not use a codex prefix.
Use a focused branch such as `feat/java-client`. Describe final behavior and
relevant validation in pull requests. Keep credentials, account data and signing
keys out of source, fixtures, logs and issues.

Retain MIT and dependency redistribution notices. Translation content, service
access and quotas are governed separately from the SDK software license.
