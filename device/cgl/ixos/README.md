# CGL+ iX OS AOSP product

This directory contains the first device-product configuration for **CGL+ iX OS**.

## First target

The initial target is an **AOSP Cuttlefish x86_64 virtual Android device**. It lets us validate that iX OS is a real Android system build before attempting hardware ports.

This is not a Raspberry Pi image, and this initial product definition is not yet a claim of a successful or bootable build. The Cuttlefish parent makefile and product must be validated against the exact AOSP release selected for the build.

## Integration

The AOSP local manifest is in `build/aosp/local_manifest.xml`. After initializing an AOSP source tree, copy that file to `.repo/local_manifests/ixos.xml`, sync the source, and select `ixos_cf_x86_64-userdebug` if the product is recognized.

## Hardware roadmap

The Raspberry Pi 3 B+ is a separate, high-risk port. It requires a compatible kernel, boot chain, device configuration, graphics, Wi-Fi/Bluetooth firmware, and other board-specific components. A Cuttlefish build does not imply Raspberry Pi compatibility.
