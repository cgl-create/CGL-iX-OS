# CGL+ iX OS

**CGL+ iX OS is an Android/AOSP operating-system project.** The objective is a
real Android system image with iX-specific system components—not a website, a
Linux desktop theme, or an app-only deliverable.

> **Current status:** The repository now contains an AOSP Cuttlefish x86_64
> product definition and an in-tree Android HOME shell called `IXLauncher`.
> Source changes are committed, but no iX OS image has yet been built or
> boot-tested. Do not treat this as a released or bootable OS.

## Direction

- **Android foundation:** AOSP, not Debian.
- **System integration:** iX components are built into a device product.
- **CGL+ identity:** dark interface, yellow accent `#f2c94c), and original iX
  design rather than a skin for an unrelated operating system.
- **Adaptive UI:** phone, tablet, and desktop layouts, with connected-input
  detection and a user override.
- **Device-specific builds:** a separate product and hardware bring-up for
  each supported target; a universal binary is not assumed.
- **Security:** preserve Android app sandboxing, permissions, SELinux and
  verified boot where the chosen target supports them.
- **Connectivity:** CGL+ HyperLink and iX Share are planned as separate,
  security-reviewed services; unsupported AirDrop/Quick Share compatibility
  must not be claimed.

## Current development target

The first **build-validation** target is AOSP Cuttlefish x86_64. This gives us
a real AOSP product and virtual Android device on which to validate the build
pipeline and system-shell integration before attempting a physical port.

| Target | Role | Status |
|---|---|---|
| AOSP Cuttlefish x86_64 | First system-image build and boot validation | Product + launcher source committed; not built |
| Raspberry Pi 3 Model B+ | Primary physical-hardware research target | Separate legacy LineageOS 17.1 / Android 10 port; not built or boot-tested |
| ARM64 Android phones/tablets | Later device-specific ports | Research; each model needs its own compatibility assessment |
| x86-64 PCs/laptops | Later device-specific build | Research; driver and boot support not validated |
| Newer Raspberry Pi boards | Possible alternative physical target | Requires board-specific validation |

The Raspberry Pi 3 B+ has 1 GB RAM and its available community Android base is
legacy and discontinued. See [Pi 3 hardware notes](devices/rpi3/README.md).
The newer Android images available from the same community port project target
different Raspberry Pi hardware; do not confuse those images with Pi 3 support.

## First implemented component: IXLauncher

`apps/IXLauncher/` is a native Android package built by the AOSP Soong build
system and included by the iX Cuttlefish product. It provides:
- CGL+ iX OS branding with a dark interface and yellow accents.
- A real Android HOME activity.
- Discovery and launching of installed Android apps.
- Automatic phone/tablet/desktop layout selection from display width and
  keyboard/mouse signals.
- Manual mode override, Android Settings access, and an About screen that
  reports actual build properties.

This is only the first shell component. Notifications, quick settings, lock
screen, complete desktop window management, HyperLink, iX Share, CGL+ Accounts,
OTA updates and physical hardware support are **not implemented** yet.

## Build the first image

Read [Build the first iX OS system image](docs/BUILD-FIRST-IMAGE.md). In brief,
the source tree must be synced from Google's `android-latest-release` manifest
with this repository included through `build/aosp/local_manifest.xml`, then
built using:

```bash
source build/envsetup.sh
lunch ixos_cf_x86_64-aosp_current-userdebug
m
```

The required AOSP checkout is large and build-intensive. A practical full
build needs a supported Linux x86-64 build host with substantial RAM and free
disk. An 8 GB RAM machine is not a realistic full AOSP build host. A successful
build and actual Cuttlefish boot are still outstanding; source configuration
alone is not proof.

## Repository map

- `apps/IXLauncher/` — native Android HOME shell module.
- `device/cgl/ixos/` — iX Cuttlefish product configuration.
- `device/cgl/ixos/rpi3/` — separate Pi 3 product overlay for legacy source.
- `build/aosp/local_manifest.xml` — includes this repository in the AOSP tree.
- `build/aosp/rpi3-local-manifest.xml` — Pi 3 community device-tree starting point.
- `devices/rpi3/README.md` — Pi 3 source base and limitations.
- `docs/AOSP-FOUNDATION.md` — AOSP foundation and milestones.
- `docs/BUILD-FIRST-IMAGE.md` — build and validation instructions.
- `docs/ARCHITECTURE.md` — system layers and design boundaries.
- `docs/ADAPTIVE-DEVICE.md` — adaptive layout requirements and tests.
- `docs/CONNECTIVITY.md` — planned sharing transports and security boundaries.
- `docs/ROADMAP.md` — staged development plan.
- `scripts/validate-ixos-source.py` — static source wiring checks (not an OS build).

## Engineering boundaries

- The AOSP Cuttlefish target is not a Raspberry Pi or phone image.
- The Pi 3 product must be built with the matching legacy LineageOS 17.1 tree;
  it cannot be combined arbitrarily with current AOSP.
- The full AOSP source and generated images must not be committed here.
- Device firmware, proprietary blobs and upstream components retain their
  own licenses.
- Never commit passwords, access tokens, signing keys, private certificates or
  Firebase service-account files.
- Do not flash an image to a physical device unless it was built for that
  exact device and a tested recovery procedure exists.

## License

No project-wide license has been selected yet. Individual files and imported
components retain their stated licenses. Do not assume original code is
licensed for unrestricted reuse.
