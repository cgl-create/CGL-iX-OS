# iX OS — Android Shell Prototype v0

This is the first **native Android app prototype** for the iX OS user experience. It is not a complete operating system, AOSP fork, or device image.

## What this prototype demonstrates
- Native Android UI (Java; no web view and no React).
- CGL+ yellow accent (`#f2c94c`) on a dark, frosted-card visual system.
- Responsive app grid: two columns on compact screens and four on wider screens.
- Touch-friendly placeholder app tiles and a basic adaptive shell layout.

## Open and run
1. Install Android Studio and an Android SDK with API 35 (or install the SDK version requested by Gradle).
2. Open this folder in Android Studio: `prototype/android-shell-v0`.
3. Let Gradle sync, then run the `app` configuration on an Android emulator.
4. Rotate the emulator and use a larger emulator profile to check the adaptive grid.

## Current limits
- Tiles are placeholders, not launchable system apps.
- This does not replace Android's launcher or modify the Android system image.
- No AOSP build, boot image, device drivers, desktop window manager, HyperLink, or system privileges are included.
- Hardware support must be validated separately for each device.

## Next milestone
Validate this shell UI on emulator in portrait, landscape, and a wide display. Then decide whether to evolve it into a launcher app or integrate selected components into an AOSP product build.
