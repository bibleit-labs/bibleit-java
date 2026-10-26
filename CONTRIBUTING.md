# Contributing

Use Java 27 and the checked-in Gradle Wrapper (`./gradlew build`). Docker validation
runs `docker build -t bibleit-java:verify .`. Tests use synthetic responses and
shared protocol fixtures; they make no production API requests.

Regenerate REST wrappers/models with `./gradlew generateCode`; commit the
contract and generated changes together. Preserve fixed origins, TLS verification,
explicit credentials, bounded JSON, resource closure and no SDK retry loops.
The SSH application owns host verification and connection lifetime.

Use Conventional Commits, for example `feat(client): add Java resource SDK` or
`fix(protocol): reject truncated records`. Use a focused branch such as
`feat/pagination`. Describe final behavior and
relevant validation in pull requests. Keep credentials, account data and signing
keys out of source, fixtures, logs and issues.

Retain MIT and dependency redistribution notices. Translation content, service
access and quotas are governed separately from the SDK software license.

Java source uses explicit imports. Document public types, constructors and methods
with parameter, return and exception contracts. The normal Gradle build validates
Javadoc with full doclint and treats warnings as errors.
