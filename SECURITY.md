# Security

Report vulnerabilities privately using the repository's GitHub security reporting
feature. Do not disclose credentials or private account/server data in issues.

The SDK pins service origins, verifies HTTPS and refuses redirects. It never
opens SSH connections, stores credentials or orchestrates browser login.
Applications own callback state, credential storage, single-use OAuth rotation,
SSH host verification and custom transport behavior. Never log wire commands,
authorization responses or original JSON containing secrets.

Supported releases receive fixes through new versions. Review dependency updates
and release notes before upgrading.
