#!/usr/bin/env python3
"""Validate iX OS source wiring without claiming an Android build succeeded."""

from pathlib import Path
import sys
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]
ANDROID = "{http://schemas.android.com/apk/res/android}"


def require(condition: bool, message: str) -> None:
    if not condition:
        raise SystemExit(f"FAIL: {message}")
    print(f"PASS: {message}")


def read(relative: str) -> str:
    path = ROOT / relative
    require(path.is_file(), f"found {relative}")
    return path.read_text(encoding="utf-8")


def main() -> None:
    manifest_path = ROOT / "apps/IXLauncher/AndroidManifest.xml"
    strings_path = ROOT / "apps/IXLauncher/res/values/strings.xml"
    local_manifest_path = ROOT / "build/aosp/local_manifest.xml"

    app_manifest = ET.parse(manifest_path).getroot()
    strings = ET.parse(strings_path).getroot()
    local_manifest = ET.parse(local_manifest_path).getroot()

    require(
        app_manifest.attrib.get("package") == "com.cgl.ixos.launcher",
        "launcher manifest declares the expected package",
    )
    home_filter_found = False
    for intent_filter in app_manifest.findall(".//intent-filter"):
        actions = {item.attrib.get(ANDROID + "name")
                   for item in intent_filter.findall("action")}
        categories = {item.attrib.get(ANDROID + "name")
                      for item in intent_filter.findall("category")}
        if ("android.intent.action.MAIN" in actions
                and "android.intent.category.HOME" in categories
                and "android.intent.category.DEFAULT" in categories):
            home_filter_found = True
    require(home_filter_found, "IXLauncher declares a HOME intent filter")
    require(
        any(item.attrib.get("name") == "app_name" and item.text == "CGL+ iX OS"
            for item in strings.findall("string")),
        "launcher label is CGL+ iX OS",
    )

    bp = read("apps/IXLauncher/Android.bp")
    source = read("apps/IXLauncher/src/com/cgl/ixos/launcher/IXLauncherActivity.java")
    product = read("device/cgl/ixos/ixos_cf_x86_64.mk")
    products = read("device/cgl/ixos/AndroidProducts.mk")
    require('name: "IXLauncher"' in bp, "Soong module is named IXLauncher")
    require("PRODUCT_PACKAGES += IXLauncher" in product,
            "Cuttlefish product includes IXLauncher")
    require("device/google/cuttlefish/vsoc_x86_64/phone/aosp_cf.mk" in product,
            "product inherits the Cuttlefish x86_64 phone configuration")
    require("ixos_cf_x86_64-aosp_current-userdebug" in products,
            "current AOSP lunch choice is declared")
    require("getLaunchIntentForPackage" in source,
            "launcher contains installed-app launch handling")
    require("resolveMode()" in source and '"desktop"' in source
            and '"tablet"' in source,
            "adaptive layout mode logic is present")

    projects = local_manifest.findall("project")
    require(
        any(p.attrib.get("path") == "device/cgl/ixos"
            and p.attrib.get("name") == "CGL-iX-OS"
            for p in projects),
        "AOSP local manifest mounts the CGL+ iX OS repository",
    )

    print(
        "\nSource checks passed. These checks do NOT compile Android, "
        "validate all Soong properties, or prove an image boots."
    )


if __name__ == "__main__":
    try:
        main()
    except (OSError, ET.ParseError) as error:
        print(f"FAIL: {error}", file=sys.stderr)
        raise SystemExit(1)
