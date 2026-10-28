#
# Copyright (C) 2026 CGL+
# CGL+ iX OS product discovery for the current AOSP release tree.
#
# The Raspberry Pi 3 overlay belongs to its separate legacy LineageOS 17.1
# checkout and is intentionally not advertised as a current AOSP lunch target.

PRODUCT_MAKEFILES := \
    $(LOCAL_DIR)/ixos_cf_x86_64.mk

COMMON_LUNCH_CHOICES := \
    ixos_cf_x86_64-aosp_current-userdebug
