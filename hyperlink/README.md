# CGL+ HyperLink

CGL+ HyperLink is the planned cross-device connectivity layer for iX OS, with companion clients considered for Android, Windows, macOS, Linux, and Raspberry Pi systems.

**Status:** Concept and requirements only. No working protocol or client is implemented here yet.

## Planned capabilities
1. **HyperShare** — user-approved file, photo, video, document, and link transfer, including nearby offline transfer where supported.
2. **HyperClipboard** — opt-in clipboard synchronization.
3. **HyperNotify** — user-selected notification relay, subject to platform permissions.
4. **HyperScreen** — screen mirroring where platform APIs permit it.
5. **HyperControl** — shared keyboard/mouse and remote input only after explicit authorization.
6. **HyperHandoff** — handoff of supported activities; arbitrary app streaming is not assumed to work everywhere.

Availability varies by OS, permissions, and hardware. Companion clients cannot silently obtain privileges the platform does not grant.

## Initial scope
Start with discovery, explicit pairing, and basic file transfer. Defer remote input, mirroring, notifications, and handoff until the trust model has been reviewed.

## Security requirements
- Explicit approval on the receiving device.
- Short-lived, single-use pairing challenges.
- Cryptographic device authentication and peer identity verification.
- Private keys stored in platform secure storage where available; never upload them.
- Device revocation and re-pairing.
- Encrypted transport and file-integrity verification.
- Treat local networks and discovery messages as untrusted.
- Account login alone never grants control.
- Separate, visible consent for screen viewing, remote input, notification access, and clipboard sync.
- Clear transfer progress, cancellation, and destination confirmation.
- Do not upload user files to CGL+ Accounts; accounts store identity and pairing metadata only.

## Proposed milestones
- H0: protocol and threat model.
- H1: pairing and revocation.
- H2: local file transfer with integrity verification.
- H3: opt-in clipboard synchronization.
- H4: platform-specific notification relay.
- H5: evaluate mirroring and remote input per platform.

## Open protocol decisions
Transport discovery/fallback, message framing and versioning, cryptographic handshake/key rotation, transfer resumability, platform permissions, and compatibility policy.
