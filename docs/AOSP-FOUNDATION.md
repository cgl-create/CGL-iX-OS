# iX OS — AOSP Foundation Plan

**Status:** engineering plan; no bootable iX OS image exists yet.

## Objective

Build iX OS as an AOSP-derived operating system. The product must eventually boot as a system image with iX components integrated into Android, rather than merely launching an ordinary application.

## Milestone 0 — choose a real build target

The first target should be a virtual AOSP device (Cuttlefish) or another explicitly supported AOSP target. This lets the project validate the source/build pipeline before attempting a physical-device port.

A virtual target is a development milestone, not a claim that physical devices are supported.

## Milestone 1 — reproducible AOSP build

1. Select a specific, maintained AOSP release/branch and record its manifest revision.
2. Prepare a supported Linux x86-64 build host. AOSP builds are storage- and memory-intensive; plan for roughly 400 GB of free disk space and 64 GB RAM for a practical full build, subject to the chosen release and target.
3. Install the toolchain and dependencies from the selected AOSP branch's official instructions.
4. Sync source using Google's `repo` tool. Do not mirror the full AOSP source into this GitHub repository.
5. Build an explicitly supported virtual-device target and boot it in Cuttlefish.
6. Record exact commands, host specifications, manifest revision, build result, and known limitations.

Do not claim success until a system image has been built and booted. A CI job that only compiles the existing Java shell app is not an OS build.

## Milestone 2 — iX system integration

After the baseline AOSP image boots:

- Create a dedicated iX product/device configuration and build metadata.
- Define which components remain upstream AOSP and which are maintained by CGL+.
- Integrate the iX system interface into the Android system/launcher architecture, instead of treating the app prototype as the OS.
- Apply the CGL+ visual system, including yellow `#f2c94c`, while retaining Android permission boundaries, SELinux enforcement, and verified-boot support where available.
- Add automated checks and an image-artifact process with clear versioning.

The exact integration point (SystemUI, launcher, framework, or a combination) must be chosen based on the intended UX and the selected AOSP release.

## Milestone 3 — physical device port

Every physical model requires its own assessment of:

- bootloader unlock and flashing restrictions;
- device tree/product configuration and kernel compatibility;
- vendor partitions, proprietary firmware and blobs;
- display, touch, graphics, audio, Wi-Fi, Bluetooth, camera, sensors and power management;
- recovery, verified boot, rollback and a documented recovery path.

Do not flash an image to a phone or board until it is built for that exact device and has a tested recovery procedure.

## Target strategy

| Target | Role | Current status |
|---|---|---|
| AOSP virtual device / Cuttlefish | First system-image build and boot validation | Not yet built |
| Samsung Galaxy S24 Ultra | Possible later phone port; model/region/bootloader must be checked | Not validated |
| Raspberry Pi 3 B+ | Possible research port; 1 GB RAM and board support are major constraints | Not validated |
| Newer Raspberry Pi boards | Separate board-by-board assessment | Not validated |
| x86-64 PC/laptop | Separate hardware/product configuration and driver assessment | Not validated |

A single image should not be promised to work on all of these targets. They have different CPU architectures, boot flows, drivers, firmware and support requirements.

## Repository boundaries

This repository should hold iX product configuration, maintained patches, build instructions, automation and project-specific source. Keep large upstream AOSP checkouts and generated build outputs out of Git. Respect the individual licenses for AOSP and all imported components.

## Definition of the first real milestone

Milestone 1 is complete only when a documented AOSP-derived system image boots in the chosen virtual target. The Android shell app prototype and its successful Gradle build do not satisfy this milestone.
