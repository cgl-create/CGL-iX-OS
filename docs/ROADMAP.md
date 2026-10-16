# iX OS Roadmap

This roadmap describes intended work, not completed features. Mark items complete only when code, tests, and reproducible evidence exist.

## Phase 0 — Project definition
- [x] Create repository scaffold and document the concept.
- [x] State compatibility and security constraints.
- [ ] Choose the initial AOSP release/branch and review build requirements.
- [ ] Define contribution, review, and licensing policy.

## Phase 1 — Emulator proof of concept
- [ ] Establish a reproducible AOSP emulator build environment.
- [ ] Boot an unmodified baseline and record build steps.
- [ ] Prototype an iX launcher/shell and visual identity.
- [ ] Validate portrait, landscape, and large-display layouts.
- [ ] Prototype capability-based device-mode detection with a user override.
- [ ] Add smoke tests and release evidence.

## Phase 2 — Adaptive experience
- [ ] Specify phone, tablet, and desktop interactions.
- [ ] Prototype keyboard, mouse, and task/window switching.
- [ ] Test keyboard-only, mouse-only, combined input, touchscreen, and stylus paths where available.
- [ ] Review accessibility, privacy, and permission UX.
- [ ] Measure memory use and startup performance.

## Phase 3 — CGL+ HyperLink
- [ ] Define a versioned protocol and threat model.
- [ ] Implement explicit pairing and revocation.
- [ ] Prototype user-approved file transfer with integrity checks.
- [ ] Define iX Share transport selection and versioned protocol.
- [ ] Prototype local Wi-Fi sharing and evaluate Wi-Fi Direct.
- [ ] Evaluate Bluetooth transfer and accessory battery reporting without inventing unavailable readings.
- [ ] Evaluate NFC tap-to-pair where supported.
- [ ] Research AirDrop and Quick Share interoperability constraints; do not claim unsupported native compatibility.
- [ ] Evaluate HDMI-CEC only on targets with an exposed compatible hardware/software stack.
- [ ] Test unauthorized peers.
- [ ] Add clipboard sync as opt-in.
- [ ] Evaluate notifications, mirroring, remote input, and handoff separately.

## Phase 4 — Hardware evaluation
- [ ] Choose one unlockable, well-documented Android device.
- [ ] Verify bootloader, kernel, vendor image, and driver availability.
- [ ] Write a device bring-up guide and recovery procedure.
- [ ] Evaluate x86-64 PC support separately.
- [ ] Evaluate Raspberry Pi only after checking board-specific support and resource constraints.

## Phase 5 — Security and maintenance
- [ ] Document updates and rollback.
- [ ] Establish release signing and key custody.
- [ ] Test SELinux enforcing mode and security boundaries.
- [ ] Establish vulnerability reporting.
- [ ] Publish a tested compatibility matrix.

## Release criteria
A device is not supported merely because it reaches a boot animation. A supported release needs appropriate boot/recovery behavior, working display/input/network/audio, stability tests, security checks, update guidance, and a documented recovery path.
