#
# Copyright (C) 2026 CGL+
# Initial iX OS product for the AOSP Cuttlefish x86_64 virtual device.
# This is a development target, not a Raspberry Pi image.
#

$(call inherit-product, device/google/cuttlefish/shared/phone/device.mk)

PRODUCT_NAME := ixos_cf_x86_64
PRODUCT_DEVICE := vsoc_x86_64
PRODUCT_BRAND := CGL+
PRODUCT_MODEL := CGL+ iX OS
PRODUCT_MANUFACTURER := CGL+

# Keep the first target close to the upstream Cuttlefish phone configuration.
# iX system applications, overlays, and boot assets will be integrated in
# subsequent commits after this product builds successfully.
