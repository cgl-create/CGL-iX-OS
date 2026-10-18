#
# Copyright (C) 2026 CGL+
# CGL+ iX OS product overlay for the Raspberry Pi 3 Model B/B+.
#
# Hardware support is inherited from the community LineageOS 17.1
# Raspberry Pi 3 device tree. This target must be built in a compatible
# LineageOS 17.1 source checkout, not a current AOSP main checkout.
#
# This is an initial product overlay; it has not yet been built or boot-tested.

$(call inherit-product, device/brcm/rpi3/lineage_rpi3.mk)

# CGL+ product identity. Keep these after the inherited product.
PRODUCT_NAME := ixos_rpi3
PRODUCT_DEVICE := rpi3
PRODUCT_BRAND := CGL+
PRODUCT_MODEL := CGL+ iX OS
PRODUCT_MANUFACTURER := CGL+

# Low-memory device: retain the upstream Raspberry Pi 3 Go / low-memory
# defaults. iX OS applications and overlays are integrated in later stages.
