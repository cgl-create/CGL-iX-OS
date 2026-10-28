#
# Copyright (C) 2026 CGL+
# CGL+ iX OS product for an AOSP Cuttlefish x86_64 virtual phone.
#
# This is the first supported build-integration target. It is a virtual
# Android device for source/build validation, not a Raspberry Pi image.

$(call inherit-product, device/google/cuttlefish/vsoc_x86_64/phone/aosp_cf.mk)

PRODUCT_NAME := ixos_cf_x86_64
PRODUCT_DEVICE := vsoc_x86_64
PRODUCT_BRAND := CGL+
PRODUCT_MODEL := CGL+ iX OS
PRODUCT_MANUFACTURER := CGL+

# Build and install the iX home shell as part of the Android system product.
PRODUCT_PACKAGES += IXLauncher
