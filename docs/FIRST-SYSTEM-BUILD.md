# First iX OS system-image build

This repository now contains an initial AOSP product definition and a CGL+ branded home activity intended to be compiled into a Cuttlefish system image. It is source integration, not a prebuilt or verified OS image.

## Product

- Product: `ixos_cf_x86_64_phone-userdebug`
- Base: upstream AOSP Cuttlefish x86_64 phone product
- Launcher module: `IxOSLauncher`
- Product metadata: `/product/etc/ixos_build_info.txt`
- Brand accent: `#F2C94C`

## Build

1. Prepare and sync a Linux AOSP checkout following the official instructions for the chosen AOSP branch and Cuttlefish target.
2. Keep this repository cloned separately from the AOSP source tree.
3. Run from this repository root:

   ```bash
   AOSP_TOP=/absolute/path/to/aosp ./scripts/build-cuttlefish.sh
   ```

4. If the lunch product is not listed, confirm that the copied `vendor/cgl/ixos/products/AndroidProducts.mk` is present and inspect the AOSP product-discovery output. Do not silently switch to the ordinary AOSP product and call it iX OS.
5. After a successful build, launch the resulting image using the Cuttlefish instructions for that same AOSP checkout.
6. Verify that the device boots, the iX OS home activity appears, System Settings opens, and the build metadata exists in `/product/etc/ixos_build_info.txt`.

## Acceptance criteria

- [ ] Product selection resolves to `ixos_cf_x86_64_phone-userdebug`.
- [ ] AOSP build completes without errors.
- [ ] Generated system image boots in Cuttlefish.
- [ ] iX OS home screen appears as a selectable HOME activity.
- [ ] Settings button opens Android Settings.
- [ ] Product metadata is present in the running image.
- [ ] Build revision, host details, build logs, and boot evidence are recorded.

## Important

No image artifact is committed here. The script does not download AOSP, bypass the upstream build requirements, or guarantee a successful build against every AOSP revision. Use the exact target and instructions supported by the selected AOSP manifest. The first verified Cuttlefish image is a development milestone; it does not imply support for the Galaxy S24 Ultra, Raspberry Pi, or arbitrary PCs.
