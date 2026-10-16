# Device Support and Bring-up

## Policy
iX OS uses separate device targets and builds. Universal compatibility across all Android phones, PCs, laptops, or Raspberry Pi boards is not promised.

Before selecting a device, check:
- Bootloader unlock availability and OEM restrictions.
- AOSP compatibility and device tree/vendor source availability.
- Kernel, drivers, firmware, and HALs.
- GPU/display acceleration and input.
- Wi-Fi, Bluetooth, cellular, audio, camera, sensors, suspend/resume, and power management.
- Partition layout, verified boot, recovery, rollback, and stock restoration.
- License requirements for vendor binaries.

## Candidate targets

| Target | Status | Main concern |
|---|---|---|
| Android Emulator | First target planned | Establish baseline AOSP build. |
| Samsung Galaxy S24 Ultra | Not validated | Model/region bootloader restrictions and vendor support. |
| Redmi K20 Pro / Xiaomi Mi 9T Pro | Not validated | Exact variant, bootloader state, device-specific sources. |
| Raspberry Pi 3 B+ | Not validated; high risk | 1 GB RAM and board-specific boot/display/network support. |
| x86-64 laptop/desktop | Not validated | Graphics, audio, Wi-Fi, suspend, and hardware diversity. |

These are evaluation candidates, not support claims.

## Bring-up checklist
1. Record exact model/variant, SoC, RAM, storage, and firmware.
2. Verify bootloader unlock and official recovery instructions.
3. Identify AOSP branch and source provenance.
4. Record kernel and firmware requirements.
5. Document reproducible build commands.
6. Test boot, recovery, verified boot, and rollback behavior.
7. Test display, input, GPU, audio, Wi-Fi, Bluetooth, camera, sensors, power, and thermals.
8. Record defects and recovery route.
9. Define security updates and maintenance ownership.
10. Set status: experimental, community-supported, or officially supported.

## Flashing safety
Never flash an image until its exact device variant and partition layout are verified. Back up data and understand the recovery route. An incompatible image can erase data or leave a device unbootable.
