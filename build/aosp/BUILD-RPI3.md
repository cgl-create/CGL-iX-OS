# Build CGL+ iX OS for Raspberry Pi 3 B+

## Status

This is an experimental Android 10 / LineageOS 17.1 source target. The product
scaffold and source manifest are in this repository; no bootable image has yet
been built or tested. The upstream Raspberry Pi device repositories are archived
and this is a legacy port, not a current secure Android release.

## Build host

Use a native 64-bit Linux machine. A full Android source build needs hundreds
of gigabytes of free disk and substantially more memory than the Raspberry Pi
itself. Do not run this on the Pi. A Chromebook Linux container or an 8 GB
Windows PC is unlikely to be a reliable full-build host.

Install the packages required by the LineageOS 17.1 build instructions,
including Git, Python, Java for Android 10, and the `repo` launcher. Use the
LineageOS documentation matching branch `lineage-17.1`.

## Initialize and sync

From an empty working directory on the Linux build host:

```sh
repo init -u https://github.com/LineageOS/android.git -b lineage-17.1
mkdir -p .repo/local_manifests
curl -L https://raw.githubusercontent.com/cgl-create/CGL-iX-OS/main/build/aosp/rpi3-full-manifest.xml -o .repo/local_manifests/rpi3.xml
repo sync -c --force-sync
```

Check that `device/cgl/ixos/AndroidProducts.mk` is present after sync and
that `device/brcm/rpi3`, `device/brcm/rpi-common`, `kernel/brcm/rpi-4.19`,
and `vendor/brcm` were checked out. If `repo sync` reports missing projects
or revision names, stop and resolve the manifest mismatch rather than building
from a partial tree.

## Configure and build

```sh
source build/envsetup.sh
lunch ixos_rpi3-userdebug
mka
```

The product is not considered buildable until the lunch target is discovered,
the build completes without errors, and the expected output files are verified
under `out/target/product/rpi3/`. Do not rename an arbitrary output to an image
or flash it. Raspberry Pi boot files, partition layout, image packaging, and
USB-boot behavior still need to be verified on the actual Pi 3 B+ hardware.

## Hardware validation checklist

- Build completes and produces a documented image artifact.
- Image packaging and partition layout match the Raspberry Pi 3 B/B+ boot flow.
- First boot succeeds from supported storage.
- HDMI, USB keyboard, Wi-Fi, Bluetooth, audio, and reboot/shutdown are tested.
- Record known failures and the exact build commit with any published image.
