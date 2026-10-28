# CGL+ iX OS AOSP product

The current AOSP product integrates **IXLauncher** as a native Android build
module. Its launcher activity is built into the system image through Soong;
it is not a standalone website or a separate APK users must sideload.

## First build target

The initial build-validation target is **AOSP Cuttlefish x86_64**. The product
inherits Google's Cuttlefish phone device configuration and adds the iX home
shell under `apps/IXLauncher/`.

- Product: `ixos_cf_x86_64`
- Lunch target: `ixos_cf_x86_64-aosp_current-userdebug`
- System package: `IXLauncher`
- Current status: source integration committed; build and boot not yet verified

Cuttlefish is a virtual Android device used to validate the real AOSP build
pipeline. It is not a Raspberry Pi image and does not establish compatibility
with physical phones, laptops, or Pi boards.

## iX shell scope

The first shell provides:
- CGL+ / iX OS identity and dark UI with the `#f2c94c` accent.
- A real Android HOME activity packaged in the AOSP product.
- Discovery and launching of installed Android apps.
- Automatic phone/tablet/desktop layout selection based on display width and
  keyboard/mouse signals, with manual overrides.
- Android Settings entry point and a truthful build/about screen.

It is an early shell, not a completed SystemUI replacement. Notifications,
quick settings, lock-screen changes, CGL+ Accounts, HyperLink, iX Share, OTA
updates and physical-device integrations remain separate engineering work.

## Integration

The AOSP local manifest is `build/aosp/local_manifest.xml`. Follow
[`BUILD-FIRST-IMAGE.md`](../../../docs/BUILD-FIRST-IMAGE.md) to sync the
current AOSP release branch and select the product.

The Raspberry Pi 3 B+ overlay is maintained separately under
`device/cgl/ixos/rpi3/` and must be built inside a compatible legacy
LineageOS 17.1 source checkout. It is not part of the current Cuttlefish
lunch target.
