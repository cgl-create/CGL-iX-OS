# Contributing to iX OS

The project is at an early planning stage. Clear documentation and small, reviewable changes are valuable.

## Before contributing
- Read the README, architecture, roadmap, and device support policy.
- Label work as a proposal, prototype, or tested implementation.
- Do not claim a device or feature works without reproducible evidence.
- Prefer documented AOSP interfaces and upstream-friendly changes.

## Changes
- Keep changes focused.
- Document trade-offs and security implications.
- Add tests or a manual verification checklist.
- Identify affected AOSP versions and device targets.
- Do not commit generated build output, proprietary firmware, secrets, or personal data.
- Never commit passwords, API tokens, signing keys, service-account JSON files, or private certificates.

## Security-sensitive changes
Pairing, authentication, file transfer, clipboard, notifications, mirroring, remote input, boot, and updates require threat-model notes and tests for unauthorized access. Report suspected vulnerabilities privately to maintainers.

## Licensing
No project-wide license has been selected. Do not assume original code is licensed for unrestricted reuse. Preserve notices and licenses for dependencies and upstream components.
