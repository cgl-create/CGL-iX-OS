# iX OS Roadmap

This roadmap describes intended work, not completed features. Mark a milestone
complete only when code, tests and reproducible evidence exist.

## Phase 0 — Project definition
- [x] Create repository scaffold and document the concept.
- [x] State compatibility and security constraints.
- [x] Choose the first build-validation class: AOSP Cuttlefish x86_64.
- [ ] Record and pin a tested AOSP manifest revision.
- [ ] Define the project's contribution and licensing policy.

## Phase 1 — First AOSP system image
- [x] Add an AOSP Cuttlefish product definition.
- [x] Add IXLauncher as a native Soong package and include it in the product.
- [ ] Sync the selected AOSP source manifest and resolve product discovery.
- [ ] Build an unmodified/near-stock Cuttlefish baseline.
- [ ] Build the iX product and boot it in Cuttlefish.
- [ ] Verify IXLauncher is the default HOME activity.
- [ ] Add repeatable boot, launcher and app-launch smoke tests.
- [ ] Record a manifest revision and release evidence.

## Phase 2 — Adaptive experience
- [x] Implement initial phone/tablet/desktop layout selection in IXLauncher.
- [x] Add a manual layout override.
- [ ] Validate portrait and landscape phone layouts.
- [ ] Test tablet and resizable-window layouts.
- [ ] Validate keyboard-only, mouse-only, keyboard + mouse and touch paths.
- [ ] Evaluate desktop task switching and multi-window behaviour.
- [ ] Review accessibility, privacy and permission UX.
- [ ] Measure memory use and startup performance.

## Phase 3 — System UI integration
- [ ] Specify notification shade, quick settings and status/navigation behavior.
- [ ] Define whether to maintain SystemUI changes or overlays.
- [ ] Implement lock-screen options within Android security boundaries.
- [ ] Add visual and regression testing.

## Phase 4 — CGL+ HyperLink and iX Share
- [ ] Define a versioned protocol and threat model.
- [ ] Implement explicit pairing, cryptographic device identity and revocation.
- [ ] Prototype user-approved file transfer with integrity checks.
- [ ] Evaluate local Wi-Fi, Wi-Fi Direct and Bluetooth transport options.
- [ ] Evaluate NFC tap-to-pair where supported.
- [ ] Research AirDrop and Quick Share compatibility; do not claim unsupported interoperability.
- [ ] Evaluate HDMI-CEC only on devices with a compatible hardware/software stack.
- [ ] Test unauthorized peers; make clipboard sync opt-in.

## Phase 5 — Physical hardware
- [ ] Reconstruct a complete, reproducible LineageOS 17.1 source manifest for Pi 3 B+.
- [ ] Build and boot the Pi 3 legacy hardware target before adding iX changes.
- [ ] Evaluate a maintained modern Android-capable board separately.
- [ ] Evaluate an unlockable phone model and document recovery instructions.
- [ ] Evaluate x86-64 PC support independently.

## Phase 6 — Security and maintenance
- [ ] Document updates and rollback.
- [ ] Establish release signing and key custody.
- [ ] Test SELinux enforcing mode and security boundaries.
- [ ] Establish vulnerability reporting.
- [ ] Publish a tested compatibility matrix.

## Release criteria

A device is not supported merely because it reaches a boot animation. A
supported release needs appropriate boot/recovery behavior, working
display/input/network/audio, stability tests, security checks, update guidance
and a documented recovery path.
