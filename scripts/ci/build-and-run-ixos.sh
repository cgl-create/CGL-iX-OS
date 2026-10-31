#!/usr/bin/env bash
# Build the CGL+ iX OS AOSP product and optionally boot it in Cuttlefish.
# This script is intended for a properly provisioned self-hosted GitHub runner.
set -Eeuo pipefail

fail() {
  echo "::error::$*" >&2
  exit 1
}
note() {
  echo "::notice::$*"
}

: "${GITHUB_WORKSPACE:?GITHUB_WORKSPACE is required}"
: "${GITHUB_SHA:?GITHUB_SHA is required}"

AOSP_BRANCH="${AOSP_BRANCH:-android-latest-release}"
AOSP_ROOT="${AOSP_ROOT:-$HOME/aosp/ixos}"
BUILD_JOBS="${BUILD_JOBS:-16}"
SYNC_JOBS="${SYNC_JOBS:-8}"
RUN_CUTTLEFISH="${RUN_CUTTLEFISH:-true}"
LUNCH_TARGET="ixos_cf_x86_64-aosp_current-userdebug"
PACKAGE_NAME="com.cgl.ixos.launcher"
ACTIVITY_NAME="com.cgl.ixos.launcher/.IXLauncherActivity"
CVD_ROOT="${RUNNER_TEMP:-/tmp}/ixos-cuttlefish"
EVIDENCE_DIR="${RUNNER_TEMP:-/tmp}/ixos-evidence"
BUILD_LOG="$EVIDENCE_DIR/aosp-build.log"
BOOT_LOG="$EVIDENCE_DIR/cuttlefish-boot.log"

mkdir -p "$EVIDENCE_DIR"
: > "$BUILD_LOG"
: > "$BOOT_LOG"

cleanup() {
  set +e
  if [[ -x "$CVD_ROOT/bin/stop_cvd" ]]; then
    (
      cd "$CVD_ROOT"
      HOME="$CVD_ROOT" "$CVD_ROOT/bin/stop_cvd"
    ) >> "$BOOT_LOG" 2>&1
  fi
  if [[ -d "$CVD_ROOT" ]]; then
    find "$CVD_ROOT" -maxdepth 2 -type f \\( -name "*.log" -o -name "launcher.log" \\) \\
      -exec cp --parents {} "$EVIDENCE_DIR" \\; 2>/dev/null || true
  fi
  echo "Evidence directory: $EVIDENCE_DIR"
}
trap cleanup EXIT

[[ "$(uname -m)" == "x86_64" ]] || fail "The AOSP build runner must be Linux x86_64."
[[ "$AOSP_BRANCH" =~ ^[A-Za-z0-9._/-]+$ ]] || fail "Invalid AOSP_BRANCH value."
[[ "$BUILD_JOBS" =~ ^[0-9]+$ ]] && (( BUILD_JOBS >= 1 && BUILD_JOBS <= 64 )) \\
  || fail "BUILD_JOBS must be an integer between 1 and 64."
[[ "$SYNC_JOBS" =~ ^[0-9]+$ ]] && (( SYNC_JOBS >= 1 && SYNC_JOBS <= 32 )) \\
  || fail "SYNC_JOBS must be an integer between 1 and 32."

mkdir -p "$(dirname "$AOSP_ROOT")"
AVAILABLE_GIB="$(df -BG --output=avail "$(dirname "$AOSP_ROOT")" | tail -n 1 | tr -cd '0-9')"
(( AVAILABLE_GIB >= 400 )) || fail "AOSP requires at least 400 GiB free before checkout/build; found ${AVAILABLE_GIB:-0} GiB."

TOTAL_KIB="$(awk '/MemTotal:/ {print $2}' /proc/meminfo)"
(( TOTAL_KIB >= 64 * 1024 * 1024 )) || fail "AOSP requires at least 64 GiB RAM; this runner reports $(( TOTAL_KIB / 1024 / 1024 )) GiB."

command -v repo >/dev/null 2>&1 || fail "The repo launcher is missing. Install Google's Repo tool on this runner."
command -v git >/dev/null 2>&1 || fail "git is required."
command -v python3 >/dev/null 2>&1 || fail "python3 is required."
repo version

if [[ "$RUN_CUTTLEFISH" == "true" ]]; then
  [[ -r /dev/kvm && -w /dev/kvm ]] || fail "Cuttlefish needs readable/writable /dev/kvm. Enable KVM on the runner host."
  GROUPS_NOW=" $(id -Gn) "
  for group in kvm cvdnetwork render; do
    [[ "$GROUPS_NOW" == *" $group "* ]] || fail "The runner account must belong to the '$group' group. Add it, then restart the runner service."
  done
  dpkg-query -W -f='${Status}' cuttlefish-base 2>/dev/null | grep -q "install ok installed" \\
    || fail "Install the Cuttlefish host package 'cuttlefish-base' on this runner and reboot it before running this workflow."
fi

note "AOSP branch: $AOSP_BRANCH"
note "AOSP source directory: $AOSP_ROOT"
note "iX OS lunch target: $LUNCH_TARGET"
mkdir -p "$AOSP_ROOT"
cd "$AOSP_ROOT"

repo init --partial-clone -b "$AOSP_BRANCH" \\
  -u https://android.googlesource.com/platform/manifest

mkdir -p .repo/local_manifests
python3 - "$GITHUB_WORKSPACE/build/aosp/local_manifest.xml" \\
  ".repo/local_manifests/ixos.xml" "$GITHUB_SHA" <<'PY'
import sys
import xml.etree.ElementTree as ET

source, destination, revision = sys.argv[1:4]
tree = ET.parse(source)
projects = [
    project for project in tree.getroot().findall("project")
    if project.get("name") == "CGL-iX-OS"
]
if len(projects) != 1:
    raise SystemExit("Expected exactly one CGL-iX-OS project in the local manifest")
projects[0].set("revision", revision)
tree.write(destination, encoding="UTF-8", xml_declaration=True)
print(f"Pinned CGL-iX-OS to workflow commit {revision}")
PY

repo sync -c -j"$SYNC_JOBS"

ACTUAL_IXOS_SHA="$(git -C device/cgl/ixos rev-parse HEAD)"
[[ "$ACTUAL_IXOS_SHA" == "$GITHUB_SHA" ]] \\
  || fail "AOSP synced iX OS revision $ACTUAL_IXOS_SHA, expected $GITHUB_SHA."

{
  echo "=== AOSP source manifest ==="
  repo manifest -r
  echo
  echo "=== Lunch and build ==="
  source build/envsetup.sh
  lunch "$LUNCH_TARGET"
  m -j"$BUILD_JOBS" dist
} 2>&1 | tee "$BUILD_LOG"

DIST_DIR="$AOSP_ROOT/out/dist"
[[ -d "$DIST_DIR" ]] || fail "AOSP did not create out/dist."
IMAGE_ZIP="$(find "$DIST_DIR" -maxdepth 1 -type f -name 'ixos_cf_x86_64-img-*.zip' -print | sort | tail -n 1)"
HOST_PACKAGE="$(find "$DIST_DIR" -maxdepth 1 -type f -name 'cvd-host_package.tar.gz' -print | head -n 1)"

[[ -n "$IMAGE_ZIP" && -f "$IMAGE_ZIP" ]] || {
  find "$DIST_DIR" -maxdepth 1 -type f -printf '%f\\n' | sort
  fail "The iX OS Cuttlefish image ZIP was not found in out/dist."
}
note "Built image: $IMAGE_ZIP"

if [[ "$RUN_CUTTLEFISH" != "true" ]]; then
  note "RUN_CUTTLEFISH=false; image build finished without boot test."
  exit 0
fi

[[ -n "$HOST_PACKAGE" && -f "$HOST_PACKAGE" ]] || fail "m dist did not produce cvd-host_package.tar.gz."

rm -rf "$CVD_ROOT"
mkdir -p "$CVD_ROOT"
tar -xzf "$HOST_PACKAGE" -C "$CVD_ROOT"
unzip -q "$IMAGE_ZIP" -d "$CVD_ROOT"

LAUNCH_CVD="$CVD_ROOT/bin/launch_cvd"
STOP_CVD="$CVD_ROOT/bin/stop_cvd"
ADB="$CVD_ROOT/bin/adb"
[[ -x "$LAUNCH_CVD" && -x "$STOP_CVD" && -x "$ADB" ]] \\
  || fail "The Cuttlefish host package is missing launch_cvd, stop_cvd, or adb."

{
  echo "Starting Cuttlefish from the iX OS build artifacts..."
  cd "$CVD_ROOT"
  HOME="$CVD_ROOT" "$LAUNCH_CVD" --daemon --start_webrtc=false
  echo "Waiting for ADB..."
  timeout 300 "$ADB" wait-for-device

  booted=false
  for attempt in $(seq 1 120); do
    value="$(timeout 15 "$ADB" shell getprop sys.boot_completed 2>/dev/null | tr -d '\\r' || true)"
    if [[ "$value" == "1" ]]; then
      booted=true
      break
    fi
    sleep 5
  done
  [[ "$booted" == "true" ]] || {
    "$ADB" logcat -d -t 2000 || true
    fail "Cuttlefish did not reach sys.boot_completed=1."
  }

  echo "Checking that the iX OS launcher is installed..."
  "$ADB" shell pm path "$PACKAGE_NAME" | tee "$EVIDENCE_DIR/launcher-package.txt"
  "$ADB" shell pm path "$PACKAGE_NAME" | grep -q "package:"

  echo "Starting the iX OS home activity..."
  "$ADB" shell am start -W -n "$ACTIVITY_NAME" | tee "$EVIDENCE_DIR/launcher-start.txt"
  sleep 8
  "$ADB" shell dumpsys activity activities > "$EVIDENCE_DIR/activities.txt"
  grep -q "$PACKAGE_NAME" "$EVIDENCE_DIR/activities.txt" \\
    || fail "The iX OS launcher process/activity was not found after launch."

  "$ADB" exec-out screencap -p > "$EVIDENCE_DIR/ixos-home.png"
  "$ADB" shell getprop > "$EVIDENCE_DIR/device-properties.txt"
  "$ADB" logcat -d -t 1000 > "$EVIDENCE_DIR/logcat.txt"

  echo "Cuttlefish booted and the iX OS launcher activity started successfully."
} 2>&1 | tee -a "$BOOT_LOG"

if [[ -n "${GITHUB_STEP_SUMMARY:-}" ]]; then
  {
    echo "## CGL+ iX OS build result"
    echo
    echo "- **AOSP branch:** `$AOSP_BRANCH`"
    echo "- **iX OS commit:** `$GITHUB_SHA`"
    echo "- **Lunch target:** `$LUNCH_TARGET`"
    echo "- **Image:** `$(basename "$IMAGE_ZIP")`"
    echo "- **Cuttlefish:** booted successfully"
    echo "- **Launcher:** `$PACKAGE_NAME` started successfully"
    echo
    echo "The full image is in the runner's AOSP `out/dist` directory. Upload it separately only when artifact storage is available."
  } >> "$GITHUB_STEP_SUMMARY"
fi
