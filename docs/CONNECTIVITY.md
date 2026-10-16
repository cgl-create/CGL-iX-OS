# iX OS Connectivity and Sharing

**Status:** Architecture proposal. The capabilities below are planned or under evaluation; they are not implemented or certified.

## 1. Design principles

- Use Android platform APIs and hardware abstractions where available; avoid requiring root for ordinary sharing.
- Explain the selected transport and required permissions before sharing.
- Require explicit recipient approval, especially for first-time pairing or sensitive actions.
- Use cryptographic peer identity, authenticated sessions, integrity checks, short-lived pairing challenges, revocation, and sensible transfer timeouts.
- Never expose received files automatically. Let the recipient choose whether and where to save them.
- Keep local discovery and transfer usable without a CGL+ account or cloud service.
- Report unavailable features honestly instead of showing a fake connected/compatible status.

## 2. iX Share

**iX Share** is the proposed cross-platform sharing feature and the preferred iX OS experience for sending files, links, text, and selected supported content between enrolled or approved devices.

### Transport selection

1. **Local Wi-Fi / LAN:** preferred for high-throughput transfers when peers can securely discover and reach each other on the same network.
2. **Wi-Fi Direct:** direct peer-to-peer transfer where the device APIs, drivers, and permissions support it.
3. **Bluetooth:** fallback for discovery, pairing, small payloads, or file transfer where supported; generally slower for large files.
4. **NFC:** optional tap-to-pair or tap-to-initiate handoff of a small bootstrap payload. NFC is not the default transport for large files.
5. **QR code / short pairing code:** fallback for pairing devices that cannot discover each other automatically.
6. **Internet relay:** optional future feature, disabled unless deliberately designed, secured, and consented to; local sharing should not silently upload content.

The UI should show the chosen transport, recipient, progress, and any fallback. Users may be allowed to choose a transport manually.

### Security and privacy

- First-time sharing requires recipient confirmation unless the user has explicitly configured a narrowly scoped trusted-device rule.
- Use authenticated encryption and verify transferred content integrity.
- Pairing tokens must be short-lived and single-use where possible.
- Include block, forget, revoke, and device-name controls.
- Do not allow account sign-in alone to authorize remote screen control, input, or broad file access.
- Keep a minimal transfer history that users can clear; do not retain file contents in a cloud account.
- Validate filenames, paths, file sizes, MIME types, and archive extraction to prevent traversal or resource-exhaustion attacks.
- Do not automatically execute received files or packages.

### Companion support

Plan clients for Android and investigate Windows, macOS, Linux, and Raspberry Pi support. Each client must use the same versioned protocol and security review. Features will vary by platform permissions and available APIs.

## 3. Bluetooth device information

iX OS should show connected Bluetooth devices, connection state, supported profiles, and battery level only when the remote device and Bluetooth profile expose it.

- Battery readings are not guaranteed for every headphone, speaker, keyboard, or mouse.
- Some accessories report a single battery percentage; others may expose left/right earbud and case levels, or none at all.
- A missing reading must be displayed as **Unavailable**, not estimated or invented.
- Do not imply that iX OS can control proprietary headphone features without a documented protocol or supported API.
- Bluetooth file transfer and iX Share over Bluetooth require separate compatibility testing.

## 4. NFC

Use NFC, when hardware and Android support are present, for short-range interactions such as tap-to-pair, exchanging a small pairing bootstrap, or opening a share confirmation screen.

- NFC hardware is optional and absent from many PCs and tablets.
- Do not transfer large files over NFC itself.
- Never trust an NFC tag or peer solely because it was tapped; authenticate the subsequent session and ask for confirmation where appropriate.

## 5. Wi-Fi Direct

Use Android's supported Wi-Fi peer-to-peer APIs where available. Validate discovery, group negotiation, permissions, radio coexistence, and behavior across vendors. Wi-Fi Direct is not guaranteed on every target and can be restricted by platform version or manufacturer implementation.

## 6. AirDrop and Quick Share interoperability

- **Apple AirDrop:** AirDrop is an Apple-controlled feature without a generally available official third-party API for implementing full native AirDrop compatibility. Do not claim that iX OS can join AirDrop natively. Cross-platform sharing with Apple devices should be investigated separately and must not depend on reverse-engineering proprietary behavior as a core requirement.
- **Google / Android Quick Share:** native availability and integration depend on the Android build, vendor/Google components, certification, and published integration options. Do not promise that a custom AOSP build can simply bundle or impersonate Quick Share.
- **iX Share:** build this as the OS's own cross-platform option with documented protocol versions and companion clients. It should not pretend to be AirDrop or Quick Share.

The UI may show a separate entry for a compatible system-provided sharing service only when that service is actually installed and available.

## 7. Bluetooth sharing

Support Android's available Bluetooth sharing mechanisms where the stack, device profiles, and receiving platform permit them. Present Bluetooth as a transport choice inside iX Share when feasible, rather than assuming every platform implements identical file-transfer behavior.

## 8. HDMI-CEC

HDMI Consumer Electronics Control (CEC) is a feature for controlling compatible HDMI-connected equipment, not a general device-discovery or file-sharing transport.

- Implement only on devices with an HDMI/CEC controller, compatible physical path or adapter, kernel/driver support, and an Android HAL or vendor interface exposing CEC.
- Many phones have no HDMI output or CEC path; a USB-C-to-HDMI adapter does not guarantee CEC support.
- Potential features include compatible display power/input control and supported remote-control actions, subject to device policy and user consent.
- Never advertise CEC as available merely because an external display is connected.

## 9. Implementation sequence

1. Specify iX Share protocol, threat model, file-transfer UX, and device identity.
2. Build a local-network prototype with explicit approval and integrity verification.
3. Add Wi-Fi Direct as an optional transport.
4. Add Bluetooth discovery / supported transfer paths and honest battery reporting.
5. Add NFC tap-to-pair where hardware is present.
6. Evaluate Quick Share integration options without claiming unsupported compatibility.
7. Research HDMI-CEC only for target hardware that exposes the required stack.
8. Add cross-platform clients and compatibility tests incrementally.

## 10. Acceptance criteria

A capability is complete only when it has automated or reproducible tests, a documented hardware/platform matrix, correct unavailable-state behavior, security review, and evidence on the actual supported devices. This document describes intent, not current functionality.
