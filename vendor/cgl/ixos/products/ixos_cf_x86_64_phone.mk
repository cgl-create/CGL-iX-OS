# iX OS development product. Based on the upstream Cuttlefish x86_64 phone.
$(call inherit-product, device/google/cuttlefish/vsoc_x86_64/phone/aosp_cf.mk)

PRODUCT_NAME := ixos_cf_x86_64_phone
PRODUCT_DEVICE := vsoc_x86_64
PRODUCT_BRAND := CGL+
PRODUCT_MODEL := iX OS Virtual Device
PRODUCT_MANUFACTURER := CGL+

# Expose the vendor Soong namespace so PRODUCT_PACKAGES can resolve IxOSLauncher.
PRODUCT_SOONG_NAMESPACES += vendor/cgl/ixos

PRODUCT_PACKAGES += \
    IxOSLauncher

PRODUCT_COPY_FILES += \
    vendor/cgl/ixos/config/ixos_build_info.txt:$(TARGET_COPY_OUT_PRODUCT)/etc/ixos_build_info.txt
