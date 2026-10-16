#!/usr/bin/env bash
set -euo pipefail

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
AOSP_TOP="${AOSP_TOP:-}"

if [[ -z "$AOSP_TOP" || ! -f "$AOSP_TOP/build/envsetup.sh" ]]; then
  echo "Set AOSP_TOP to the root of a synced AOSP checkout." >&2
  echo "Example: AOSP_TOP=/work/aosp $0" >&2
  exit 2
fi

AOSP_TOP="$(cd "$AOSP_TOP" && pwd)"
mkdir -p "$AOSP_TOP/vendor/cgl"
rm -rf "$AOSP_TOP/vendor/cgl/ixos"
cp -a "$REPO_ROOT/vendor/cgl/ixos" "$AOSP_TOP/vendor/cgl/ixos"

cd "$AOSP_TOP"
# AndroidProducts.mk registers the ixos_cf_x86_64_phone lunch product.
# shellcheck disable=SC1091
source build/envsetup.sh
lunch ixos_cf_x86_64_phone-userdebug
m

echo
echo "Build completed. Locate generated images with:"
echo "  find out/target/product/vsoc_x86_64 -maxdepth 1 -type f"
echo
echo "This confirms compilation only. Boot the image in Cuttlefish and verify"
echo "the iX OS home screen before claiming the first OS milestone complete."
