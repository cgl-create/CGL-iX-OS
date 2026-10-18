#
# Copyright (C) 2026 CGL+
# CGL+ iX OS product discovery.
#
# The Raspberry Pi 3 target is based on the legacy LineageOS 17.1
# (Android 10) community device tree. It is experimental and untested.

PRODUCT_MAKEFILES := \
    $(LOCAL_DIR)/ixos_cf_x86_64.mk \
    $(LOCAL_DIR)/rpi3/ixos_rpi3.mk

COMMON_LUNCH_CHOICES := \
    ixos_cf_x86_64-userdebug \
    ixos_rpi3-userdebug
