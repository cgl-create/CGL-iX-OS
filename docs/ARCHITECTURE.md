# iX OS Architecture

**Status:** Initial proposal; subject to revision during prototyping.

## 1. Goal
iX OS is an Android-based system with an adaptive experience across selected mobile and desktop-class devices. It is not a single universal binary; hardware-specific builds are expected.

## 2. Conceptual layers
1. **Hardware and boot chain:** SoC/CPU, memory, storage, display, input, radios, firmware, bootloader, and verified-boot support.
2. **Linux kernel and device support:** compatible kernel/configuration, device trees, modules, vendor interfaces, firmware, and drivers where legally and technically available. SELinux should remain enforcing in production.
3. **Android platform:** AOSP framework and services, ART, Binder IPC, Package Manager, permissions, application sandbox, HALs, and vendor interfaces.
4. **iX system experience:** adaptive launcher/navigation, large-screen layouts, keyboard/mouse support, task switching, and settings.
5. **CGL+ services and apps:** HyperLink connectivity, later CGL+ Accounts integration, and optional isolated Linux userspace tools.

## 3. Adaptive experience
- **Phone:** touch-first compact layouts.
- **Tablet:** expanded layouts, split panes, and multi-window workflows.
- **PC/laptop:** keyboard and pointer support, resizable windows where supported, desktop-style task switching.
- **Other form factors:** out of initial scope unless explicitly added.

Adaptive UI does not provide missing hardware drivers. Display, GPU, audio, Wi-Fi, Bluetooth, suspend/resume, and input need independent validation.

## 4. Adaptive device and input detection

The shell should select layouts using window size, resource configuration, input-device capabilities, display/docking signals, and user preference. Do not infer full device type from screen size or a single connected peripheral. Automatic mode selection must be overrideable. See [Adaptive Device and Input Detection](ADAPTIVE-DEVICE.md) for requirements and tests.

## 5. Connectivity and sharing

CGL+ HyperLink is the broader cross-device service concept; **iX Share** is the proposed user-facing file/link/text sharing feature. Transport options may include local Wi-Fi, Wi-Fi Direct, Bluetooth, and NFC-based pairing, depending on platform and hardware support. Bluetooth battery levels are shown only when accessories report them through supported profiles. Apple AirDrop and Google Quick Share compatibility must not be claimed without a supported integration path. HDMI-CEC requires a compatible HDMI/CEC hardware and software stack and is not a general-purpose sharing protocol. See [Connectivity and Sharing](CONNECTIVITY.md).

## 6. Linux application support
Android already uses the Linux kernel; arbitrary desktop Linux programs do not automatically run natively.
- **Stage A:** optional CLI tools in a constrained userspace without root by default.
- **Stage B:** evaluate package management and filesystem isolation.
- **Stage C:** investigate graphical Linux apps only after choosing graphics/display integration.
Do not claim full desktop Linux compatibility until tested.

## 7. Build strategy
- Start with an Android emulator and documented AOSP release/branch.
- Keep upstream AOSP changes separate from iX-specific code where practical.
- Maintain device-specific manifests and reproducible build notes.
- Full AOSP builds require substantial host RAM, CPU, and disk; they are not expected to run on a phone.
- Do not commit build output or proprietary firmware.

## 8. Security boundaries
- Preserve Android sandboxing, runtime permissions, SELinux policy, and security updates.
- Keep signing keys out of source control.
- Treat HyperLink as untrusted network-facing software: require explicit approval, short-lived challenges, cryptographic device identity, and revocation.
- Account login alone must not authorize screen control, remote input, or file access.
- Device-local unlock should work offline after enrollment and should not reuse the account password.
- Avoid unnecessary telemetry and do not store transferred content in the account service.

## 9. Open decisions
- Initial AOSP release/branch.
- UI approach: custom launcher, SystemUI changes, or both.
- Emulator configuration and desktop windowing strategy.
- Supported device shortlist and bootloader policy.
- Linux userspace approach.
- Update, rollback, recovery, and release-signing design.
