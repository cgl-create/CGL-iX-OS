# iX OS — AOSP Foundation

**Status:** first system-product integration committed; no buildable image has
yet been verified and no bootable iX OS image exists yet.

## Objective

Build iX OS as an AOSP-derived operating system. The product must boot as an
Android system image with iX-specific components integrated into Android,
rather than merely launching a separate ordinary application.

## Milestone 0 — reproducible AOSP product

The repository now defines an iX Cuttlefish x86_64 product and includes
`IXLauncher` as a native Soong Android package. The selected manifest track is
Google's `android-latest-release`, which follows the latest public Android
release branch. The Cuttlefish product is the first build validation target;
it is not physical-device support.

Build and boot status must remain marked incomplete until the image is
compiled and Cuttlefish actually starts.

## Milestone 1 — reproducible AOSP build

1. Use a supported Linux x86-64 build host. AOSP builds are storage- and
   memory-intensive; plan for roughly 400 GB of free disk space and 64 GB RAM
   for a practical full build, subject to the chosen release and target.
2. Follow the exact commands in [Build the first image](BUILD-FIRST-IMAGE.md).
3. Record host specifications, the resolved AOSP manifest revision, build
   output, boot result and known limitations.
4. Keep the full upstream AOSP tree and generated images outside this GitHub
   repository.
5. Fix product/module errors based on actual build output; do not infer build
   success from a source commit alone.

## Milestone 2 — iX system integration

The first in-tree iX component is `apps/IXLauncher/`, integrated through
`PRODUCT_PACKAGES += IXLauncher` in the Cuttlefish product. It currently
implements an Android HOME activity, app launching, and an overrideable
adaptive layout mode.

After the first image boots:
- Verify that IXLauncher is actually selected as HOME at first boot.
- Test app enumeration and launching under the selected Android release.
- Verify touch, keyboard and pointer behavior across phone/tablet/desktop
  display sizes.
- Decide whether subsequent system integration belongs in Launcher, SystemUI,
  framework overlays or multiple layers.
- Apply CGL+ visual styling without bypassing Android permissions, SELinux or
  verified-boot protections.

The launcher is not yet a replacement for SystemUI and does not implement
notifications, quick settings, device pairing, screen casting or account
services.

## Milestone 3 — physical device port

Every physical model requires its own assessment of:
- bootloader unlock and flashing restrictions;
- device tree/product configuration and kernel compatibility;
- vendor partitions, proprietary firmware and blobs;
- display, touch, graphics, audio, Wi-Fi, Bluetooth, camera, sensors and power
  management;
- recovery, verified boot, rollback and a documented recovery path.

Do not flash an image to a phone or board until it is built for that exact
device and has a tested recovery procedure.

## Target strategy

| Target | Role | Current status |
|---|---|---|
| AOSP Cuttlefish x86_64 | First system-image build and boot validation | Product and launcher source committed; not built |
| Raspberry Pi 3 B+ | Primary physical-hardware research target | Separate legacy Android 10 / LineageOS 17.1 port; not built or boot-tested |
| Redmi K20 Pro / Xiaomi Mi 9T Pro | Possible later phone port | Not validated |
| Samsung Galaxy S24 Ultra | Possible later phone port | Exact model/region and bootloader status not validated |
| x86-64 PC/laptop | Separate hardware/product configuration | Not validated |

A single image should not be promised to work on all of these targets. They
have different CPU architectures, boot flows, drivers, firmware and support
requirements.

## Repository boundaries

This repository holds iX product configuration, maintained patches, build
instructions, automation and project-specific source. Keep large upstream
AOSP checkouts and generated build outputs out of Git. Respect the licenses of
AOSP and every imported component.

## Definition of the first real milestone

The first milestone is complete only when a documented AOSP-derived system
image is built and boots in Cuttlefish, IXLauncher opens as HOME, installed
apps can launch, and the exact source/build revision is recorded. Source
configuration alone does not satisfy this milestone.
