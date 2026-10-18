# iX OS Feature Roadmap

**Project:** iX OS by CGL+  
**Status:** Requirements and implementation plan, not a claim that these features are implemented.  
**Current baseline:** AOSP/Cuttlefish product foundation and launcher prototype. A real AOSP build and device boot still need to be demonstrated.

## Product goals

- CGL+ visual identity: dark interface, yellow accent (`#f2c94c`), responsive phone/tablet/desktop layouts, and consistent animations.
- Use Cuttlefish as the first build-and-test target; add physical devices only through an explicit hardware support matrix.
- Distinguish real system services from launcher mock-ups, web shortcuts, optional services, vendor components, and licensed technologies.

## Phase 0 — Build foundation

- [ ] Validate product definition, lunch target, Soong namespace, launcher module, and scripts against a real AOSP checkout.
- [ ] Build and boot an iX OS Cuttlefish image; record reproducible build steps and smoke-test results.
- [ ] Expand CI checks; label source-only checks clearly so they are not mistaken for a full AOSP build.
- [ ] Define hardware support matrix: device/board, chipset, kernel, display, Wi-Fi, Bluetooth, audio, camera, sensors, battery, storage, USB, and modem.
- [ ] Define release signing, updates/rollback, logs policy, and security disclosure.

## Phase 1 — Core operating-system experience

### First boot, startup, home, and notifications
- [ ] First-boot setup wizard: language, region, accessibility, Wi-Fi, date/time, privacy, optional CGL+ account sign-in, screen lock, restore/skip, and finish.
- [ ] iX OS splash screen and startup animation, with reduced-motion support.
- [ ] Home screen, app drawer, recent apps, widgets, app shortcuts, and adaptive phone/tablet/desktop layouts.
- [ ] Real system-wide Notification Center and Quick Settings integrated into AOSP SystemUI, not just a launcher panel.
- [ ] Notification grouping, history, dismiss/snooze actions, lock-screen privacy, and media controls.
- [ ] Real battery/charging status where exposed by the platform, battery settings, and CGL+ charging animation. Do not treat emulator readings as proof of hardware support.

### Wi-Fi, Bluetooth, and devices
- [ ] Wi-Fi scan/connect, saved networks, captive portals, hotspot/tethering where supported, and connection diagnostics.
- [ ] Bluetooth pairing, reconnect/unpair, permissions, and supported profile status.
- [ ] Bluetooth keyboard, mouse, audio/headsets, BLE devices, and BLE control services supported by the target stack.
- [ ] USB host/accessory, hubs, keyboards, mice, storage, audio, and permission prompts where hardware and kernel drivers permit.
- [ ] SD/microSD and external storage on devices with the relevant slot/controller.
- [ ] Camera/microphone/location/nearby-device indicators, privacy controls, and permission management.

### Lock screen and identity
- [ ] Lock choices: None, PIN (4 digits, 6 digits, or custom length), pattern, and password.
- [ ] Optional fingerprint and face unlock through Android biometric APIs only when compatible secure hardware and vendor integration exist.
- [ ] Animated lock/unlock transitions, clock, notification privacy, emergency access, and failed-attempt rate limiting.
- [ ] CGL+ Accounts link: sign-in/out, account state, recovery, consent, privacy controls, and cross-device identity; never store passwords in plain text.

## Phase 2 — First-party CGL+ apps

Build and maintain a consistent, accessible app suite:

- [ ] Phone and SMS/MMS for supported cellular devices.
- [ ] Contacts, Clock, Calculator, Calendar, Camera, Gallery/Photos, Files, Settings, Recorder, Notes, browser, and media player.
- [ ] App permission/privacy dashboard, storage manager, battery settings, accessibility, and device information.
- [ ] Share sheet integration, widgets, URL/file handlers, and default-app selection.
- [ ] TV remote app for Sony, Apple TV, Fire TV, Roku, LG, and Samsung using supported network APIs or hardware. IR control requires an actual IR blaster.
- [ ] Test all apps for lifecycle, accessibility, localization, privacy, and low-memory behavior.

## Phase 3 — Android apps, store, and Lloyd AI

### Android application suite
- [ ] Run ordinary Android apps using the AOSP application/runtime stack; test installation, graphics, audio, permissions, notifications, and background execution.
- [ ] Define a CGL+ app catalogue/store strategy, including signing, provenance, updates, and malware-handling policy.
- [ ] Google Play Store and Google Mobile Services require appropriate licensing, device certification, and compatible vendor integration; do not claim they are bundled until that is actually true.
- [ ] Clearly label web shortcuts, open-source apps, optional services, and licensed native apps separately.

### Lloyd — CGL+ AI assistant
- [ ] Lloyd assistant UI with prompt input, conversation controls, loading/error states, and accessibility.
- [ ] Versioned knowledge base for CGL+ and iX OS documentation, FAQs, troubleshooting, release notes, and supported devices.
- [ ] Optional API-provider configuration for OpenAI/ChatGPT API, Google AI Studio/Gemini API, and Anthropic Claude API.
- [ ] Bring-your-own-key flow with consent, masked display, secure OS-backed storage where available, revoke/delete controls, usage warnings, and no key logging.
- [ ] Separate grounded CGL+/iX OS answers from general model answers; disclose provider data handling and avoid sending data without consent.
- [ ] Graceful offline, missing-key, quota, network, and provider-error behavior. A user API key does not imply a free subscription.

## Phase 4 — Casting and EDC desktop mode

### Casting
- [ ] Miracast/Wi-Fi Display where chipset, drivers, and Android stack support it.
- [ ] Google Cast/Chromecast sender or receiver functionality as appropriate, subject to service integration and required Google components.
- [ ] Research AirPlay interoperability; full compatibility may require licensed technology and must not be promised before testing.
- [ ] Device discovery, permissions, pairing/PIN where appropriate, connection status, disconnect, and privacy indicators.
- [ ] Publish a tested compatibility matrix covering target TVs/streaming devices, OS versions, and network conditions.

### EDC — Easy Desktop Cast
- [ ] Desktop mode inspired by phone-to-desktop experiences: external display, dock/taskbar, app switching, keyboard/mouse, and resizable windows where supported.
- [ ] Resolution/scaling, orientation, audio routing, screen-off behavior, and safe disconnect.
- [ ] Define concrete Chromebook/ChromeOS connectivity goals (for example file transfer, notifications, tethering, or display); do not assume full interoperability.

## Phase 5 — CGL+ HyperLink, sharing, and routines

### CGL+ HyperLink
- [ ] Define HyperLink as the CGL+ discovery, pairing, trust, and transfer service between supported iX devices.
- [ ] Versioned protocol and threat model; explicit pairing/revocation, encrypted transport, user consent, progress/cancel/retry, integrity verification, and safe receiving.
- [ ] Define supported payloads: files, links, text, photos/videos, and any additional explicitly supported types.
- [ ] Test two or more iX devices over the same Wi-Fi, hotspot, and other documented network paths; test relay across networks only if implemented.
- [ ] Test offline mode, interrupted transfers, duplicate filenames, large files, low storage, revoked trust, and invalid/malicious payloads.

### Five-method transfer test suite
The project needs a defined test matrix for all five transfer methods. Three priority names are **CGL+ iX Share**, **AirDrop**, and **Quick Share**. Proposed five-path scope:

1. **CGL+ iX Share** — native iX OS sharing experience.
2. **AirDrop interoperability** — research and test only where technically and legally feasible; Apple does not guarantee general third-party Android compatibility.
3. **Quick Share interoperability** — depends on the supported Android implementation and required Google/vendor components.
4. **HyperLink direct transfer** — CGL+ device-to-device protocol.
5. **Android system Share / alternate supported transfer path** — finalize exact scope before declaring the five-method suite complete.

For every path, record sender/receiver model and OS version, payload type/size, network path, discovery time, transfer time, outcome, integrity result, and logs with sensitive content removed. A similar button or UI label is not proof of protocol compatibility.

### Shortcuts and routines
- [ ] CGL+ Shortcuts/Routines for apps, approved settings actions, schedules, connectivity changes, and supported USB-device events.
- [ ] Permission gates and confirmation for sensitive actions; routines must not bypass lock-screen or privacy controls.
- [ ] Document routine triggers/actions and add repeatable tests.

## Phase 6 — Camera, cellular, eSIM, and device-specific support

- [ ] Camera drivers/HAL per supported target, camera/microphone use indicators, permission checks, and privacy tests.
- [ ] SIM support on devices with compatible modem, RIL/vendor implementation, antenna, and carrier configuration.
- [ ] eSIM only on devices with an eUICC and suitable vendor/carrier integration; activation/profile downloads require carrier support and security controls.
- [ ] Test calls, SMS/MMS, mobile data, emergency calling, dual-SIM, and carrier features where applicable.
- [ ] Validate battery, charging, USB, audio, storage, and biometric behavior on actual hardware.
- [ ] Add drivers for specific supported devices and components. “Every device in existence” is not a feasible acceptance criterion; maintain a compatibility matrix and accept hardware-specific contributions.

## Verification and release gates

A feature is **Implemented** only when relevant gates pass:

1. **Source:** code/configuration and tests are in the repository.
2. **Build:** relevant AOSP target compiles in a documented environment.
3. **Runtime:** feature is exercised on the declared target.
4. **Security/privacy:** permissions, secrets, trust, and failure modes are reviewed.
5. **Compatibility:** supported devices/versions and limitations are documented.
6. **Evidence:** test results and known issues are recorded.

Use statuses: **Planned**, **Prototype**, **Build-verified**, **Device-tested**, and **Released**. A UI mock-up or web shortcut is a prototype, not a completed system service.

## Important constraints

- AOSP is a foundation, not a complete hardware port. Wi-Fi, Bluetooth, camera, biometrics, battery, cellular, eSIM, USB, and display depend on the exact device, kernel, firmware, HALs, and vendor components.
- Google Play/GMS, AirDrop/AirPlay, Chromecast, and vendor-specific features may have licensing, certification, proprietary-protocol, or device restrictions.
- Biometrics require compatible hardware and secure integration; animations alone are not authentication.
- “All apps every phone has” means a prioritized CGL+ app suite with documented gaps, not a promise to clone proprietary apps/services.
