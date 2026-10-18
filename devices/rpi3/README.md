# Raspberry Pi 3 Model B+ — iX OS hardware target

## Board-port selection

The initial hardware base is the community **LineageOS 17.1 / Android 10 Raspberry Pi 3** device tree maintained under the `lineage-rpi` GitHub organization. It explicitly targets Raspberry Pi 3 Model B and B+ and provides board-specific kernel, graphics, boot, Wi-Fi, Bluetooth, USB, and HDMI integration.

- Device tree: https://github.com/lineage-rpi/android_device_brcm_rpi3
- Shared device configuration: https://github.com/lineage-rpi/android_device_brcm_rpi-common
- Upstream build reference: https://konstakang.com/devices/rpi3/LineageOS17.1/

This is a **legacy Android 10 port**, not a maintained current Android release. Its original maintainer marks it discontinued and notes security and SELinux limitations. It is selected as a practical bring-up reference for this old 1 GB board, not as a secure daily-driver base.

## Current source in this repository

- `device/cgl/ixos/rpi3/AndroidProducts.mk` declares the iX OS product.
- `device/cgl/ixos/rpi3/ixos_rpi3.mk` inherits the community Pi 3 hardware product and overrides the product identity to CGL+ iX OS.
- `build/aosp/rpi3-local-manifest.xml` records the community device-tree repositories.

## Important build boundary

This is not yet a reproducible build. The manifest is only a starting point: the complete matching LineageOS 17.1 manifest, kernel source, vendor/proprietary files and build dependencies must also be present. The product has not been compiled, booted, or tested on physical hardware. Do not flash an image based on these files until a build and boot test succeed.

The product overlay must be integrated into a compatible LineageOS 17.1 source checkout. It is not compatible with a random current AOSP branch simply by copying the makefile.

## Build and licensing cautions

The original KonstaKANG Android 10 image page identifies some included components as CC BY-NC-SA 4.0 and prohibits commercial use of that build. CGL+ must review component licenses and independently build from appropriately licensed source; do not redistribute that prebuilt image or imply that its non-commercial components are cleared for commercial distribution.

## Hardware constraints

The Raspberry Pi 3 B+ has 1 GB RAM and uses a board-specific boot chain. Android performance and compatibility are constrained by RAM, graphics support, firmware, and the age of the available port. USB boot must be validated on the actual board and storage device.
