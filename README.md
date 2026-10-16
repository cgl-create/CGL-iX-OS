# iX OS

**An Android-based adaptive operating-system research project by CGL+.**

iX OS explores a consistent experience across selected phones, tablets, PCs, laptops, and Raspberry Pi-class devices, with **CGL+ HyperLink** providing cross-device connectivity.

> **Status: AOSP foundation planning.** This repository does not yet contain a bootable iX OS image or a complete AOSP source tree. No physical device is currently certified as supported.

## Principles
- **Android foundation:** Android Open Source Project (AOSP), not Debian.
- **System, not just an app:** the goal is an AOSP-derived system image with iX components integrated into the OS.
- **Device-specific builds:** separate product and hardware configurations for each target.
- **Adaptive interface:** layouts and input behavior for phones, tablets, and desktop-sized displays, with capability-based input detection and user override.
- **Connectivity:** planned iX Share transports and optional NFC, Bluetooth, Wi-Fi Direct, and HDMI-CEC support only where hardware and platform APIs allow.
- **Interoperability:** HyperLink clients for other operating systems where technically possible.
- **Security:** preserve Android app sandboxing, SELinux enforcement, verified boot where supported, and explicit consent for sensitive actions.
- **Honest compatibility:** bootloader restrictions, drivers, firmware, and device maintenance determine support.

## Intended targets

| Target | Status | Notes |
|---|---|---|
| AOSP virtual device / Cuttlefish | First system-image target planned | Validate the real AOSP build and boot process before physical ports. |
| ARM64 Android phones/tablets | Research | Each model needs its own compatibility assessment. |
| x86-64 PCs/laptops | Research | Requires suitable platform, graphics, input, and driver support. |
| Raspberry Pi 3 B+ | Research; high risk | 1 GB RAM and board support constraints. |
| Newer Raspberry Pi boards | Research | Requires board-specific validation. |

These are goals, not a promise that every device can run iX OS.

## Repository map
- `docs/AOSP-FOUNDATION.md` — real OS build milestones, target strategy, and bring-up requirements.
- `docs/ARCHITECTURE.md` — system layers and design boundaries.
- `docs/ADAPTIVE-DEVICE.md` — form-factor detection, input adaptation, and validation.
- `docs/CONNECTIVITY.md` — iX Share transports and cross-device integration constraints.
- `docs/ROADMAP.md` — staged development plan.
- `devices/README.md` — device compatibility and bring-up checklist.
- `hyperlink/README.md` — CGL+ HyperLink concept and security requirements.
- `CONTRIBUTING.md` — engineering guidelines.

## Development strategy
1. Select a maintained AOSP release and a supported virtual-device target.
2. Build and boot a baseline AOSP-derived system image in Cuttlefish.
3. Record a reproducible build process and automated validation.
4. Integrate iX system components into AOSP; the existing Java shell app is only a UI prototype and does not count as an OS build.
5. Evaluate physical hardware targets individually before attempting ports.
6. Implement HyperLink separately from privileged OS components, with a defined security boundary.

The full AOSP source tree and generated build outputs should not be committed to this repository. Follow the selected AOSP branch's official build instructions and preserve upstream licensing notices.

## Linux application support
Android uses the Linux kernel, but that does not mean arbitrary desktop Linux programs run natively. CLI tools may be explored in a contained userspace; graphical Linux apps require a separate graphics and integration plan.

## Security
Never commit passwords, access tokens, signing keys, private certificates, Firebase service-account files, or other secrets. Report vulnerabilities privately to maintainers.

## License
No project-wide license has been selected yet. Do not assume original code is licensed for unrestricted reuse. AOSP and other dependencies retain their own licenses.
