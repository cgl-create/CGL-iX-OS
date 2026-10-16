# iX OS Adaptive Device and Input Detection

**Status:** Design proposal. No detection or mode-switching implementation is complete yet.

## Goals

iX OS should adapt its system shell to the available screen, interaction methods, and connected peripherals. Device mode is a UI policy, not a claim that the underlying hardware is fully supported.

## Signals

Use multiple signals instead of a single device label:

- Android resource configuration and current window bounds / size classes.
- Display density, orientation, posture/fold state where supported, and external displays.
- Available input devices and their reported capabilities, including touchscreens, styluses, keyboards, mice, touchpads, game controllers, and trackballs.
- Docking state and platform-provided desktop-mode signals, when available.
- User-selected mode and accessibility preferences.

Do not infer that every large display is a PC, or that a keyboard alone means desktop mode. A phone connected to a keyboard should remain usable as a phone unless the user or a supported desktop-mode policy chooses otherwise.

## Modes

- **Phone:** compact, touch-first navigation and thumb-reachable controls.
- **Tablet:** expanded touch layouts, optional split panes, and multi-window workflows.
- **Desktop:** pointer/keyboard optimized shell, taskbar or dock, window management where supported.
- **External-display / docked:** select a suitable layout when the platform and hardware expose this capability.
- **Fallback:** functional, accessible layout if device signals are incomplete.

These are adaptive layouts within one system experience, not separate OS binaries by themselves.

## Input behavior

- Touch is never the only way to activate critical controls when keyboard/pointer access is available.
- Important controls should target approximately 48 x 48 dp for touch where practical, with adequate spacing.
- Keyboard shortcuts, focus indicators, Escape/back behavior, context menus, and pointer hover affordances should be added for desktop use without making hover mandatory.
- Detect keyboard and mouse independently; support wired USB, Bluetooth HID, and other input methods exposed by Android.
- Support stylus-specific features only when the device reports the necessary capabilities.
- Respect font scaling, contrast, reduced-motion preferences where available, and screen readers.

## Mode switching

Provide automatic adaptation with a user override. Avoid disruptive forced switches while an app is active. Remember the user's preference per display/docking scenario when feasible, and expose a clear way to return to phone/tablet mode.

## Validation

Test emulator configurations and real hardware separately:

- Portrait and landscape phone layouts.
- Tablet and resizable-window layouts.
- Mouse only, keyboard only, keyboard + mouse, touchscreen + keyboard, and stylus where available.
- External display and dock scenarios where hardware supports them.
- Rotation, disconnect/reconnect, accessibility scaling, and process restart.
- Low-memory performance and touch target usability.

## Limitations

Software detection cannot create missing drivers or hardware features. Desktop window management, external displays, folding posture, and peripheral details depend on the Android release, device manufacturer, kernel, and hardware.
