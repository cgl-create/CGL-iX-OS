# Build the first iX OS system image

**Current status:** instructions and source integration are committed, but the
product has not yet been built or booted. These commands are a build recipe,
not evidence of a successful build.

## Host requirements

Use a supported Linux x86-64 machine. AOSP's full source and build are
resource-intensive; plan for about **400 GB of free disk** and **64 GB RAM**
for a practical full build, depending on release and configuration. Use an
SSD-backed Linux filesystem for the source and output where possible.

A low-memory Chromebook terminal, Raspberry Pi, or an 8 GB RAM PC is not a
realistic host for a full current AOSP build. Do not attempt the build on the
Pi itself.

Official references:
- [AOSP build requirements](https://source.android.com/docs/setup/start/requirements)
- [Try Android development](https://source.android.com/docs/setup/start)
- [Build Android](https://source.android.com/docs/setup/build/building)

## 1. Install host dependencies

Install the Repo launcher and Git using your Linux distribution's package
manager. For Debian/Ubuntu-based hosts:

```bash
sudo apt-get update
sudo apt-get install -y git repo python3 curl ca-certificates
```

Follow the current AOSP requirements page for the remaining host packages.
Use a case-sensitive filesystem with sufficient free space.

## 2. Initialize the AOSP source tree

```bash
mkdir -p ~/aosp/ixos
cd ~/aosp/ixos
repo init --partial-clone --no-use-superproject \
  -b android-latest-release \
  -u https://android.googlesource.com/platform/manifest
mkdir -p .repo/local_manifests
curl -fsSL \
  https://raw.githubusercontent.com/cgl-create/CGL-iX-OS/main/build/aosp/local_manifest.xml \
  -o .repo/local_manifests/ixos.xml
repo sync -c -j8
```

The local manifest places this repository at
`device/cgl/ixos`. The iX launcher source is under
`device/cgl/ixos/apps/IXLauncher`; Soong discovers its `Android.bp` in that
checkout. Keep the manifest and source revisions recorded with any build
results.

If the source sync fails, correct the sync failure before moving on. Do not
copy this product into a different Android release branch and assume
compatibility.

## 3. Select and build iX OS

From the AOSP source root:

```bash
source build/envsetup.sh
lunch ixos_cf_x86_64-aosp_current-userdebug
m
```

The product extends Google's Cuttlefish x86_64 phone configuration and adds
the native `IXLauncher` package. The first successful build must confirm that
the product is recognized, Soong compiles IXLauncher, and system images are
generated. Save the complete build log and the output of:

```bash
repo manifest -r > ixos-source-manifest.xml
```

## 4. Boot and verify

Boot the resulting Cuttlefish image using the launch procedure documented
for the Cuttlefish version included in the synced source tree. Verify all of
the following before calling this a successful OS milestone:

- Android reaches the home screen without a boot loop.
- **CGL+ iX OS** appears as the HOME shell.
- App icons are discovered and launch installed apps.
- Auto/Phone/Tablet/Desktop selectors update the visible layout.
- Keyboard and mouse work where exposed by the Cuttlefish configuration.
- Android Settings opens.
- Rebooting preserves normal Android operation.

If another HOME app appears, check the Soong override behavior and the actual
installed package set instead of assuming the default was changed.

## Important boundaries

- This target is a virtual Android system image, not a Raspberry Pi image.
- The Pi 3 B+ product uses a discontinued legacy LineageOS 17.1 / Android 10
  board port and must be brought up in its own compatible source tree.
- This first shell does not implement custom SystemUI, notifications, quick
  settings, HyperLink, iX Share, CGL+ Accounts, lock-screen replacement or OTA.
- Do not flash the Cuttlefish output to any physical device.
