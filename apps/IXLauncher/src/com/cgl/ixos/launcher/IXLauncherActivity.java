/*
 * Copyright (C) 2026 CGL+
 * SPDX-License-Identifier: Apache-2.0
 */
package com.cgl.ixos.launcher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.InputDevice;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextClock;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * The first iX OS system-shell implementation.
 *
 * This is built as part of the AOSP product, rather than distributed as a
 * separate end-user APK. The shell launches installed Android apps and
 * provides an overrideable phone/tablet/desktop presentation mode.
 */
public final class IXLauncherActivity extends Activity {
    private static final int BG = Color.rgb(13, 15, 18);
    private static final int CARD = Color.rgb(27, 31, 37);
    private static final int CARD_SELECTED = Color.rgb(54, 46, 23);
    private static final int ACCENT = Color.rgb(242, 201, 76);
    private static final int TEXT = Color.rgb(245, 246, 248);
    private static final int MUTED = Color.rgb(164, 172, 183);
    private static final String PREFS = "ix_shell_preferences";
    private static final String MODE_KEY = "display_mode";

    private String requestedMode;
    private TextView modeSummary;
    private GridLayout appGrid;
    private TextView appCount;
    private LinearLayout modeChips;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(BG);
        getWindow().getDecorView().setSystemUiVisibility(0);
        requestedMode = getPreferences(MODE_PRIVATE).getString(MODE_KEY, "auto");
        buildShell();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (appGrid != null) {
            refreshMode();
        }
    }

    private void buildShell() {
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(BG);

        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(dp(22), dp(22), dp(22), dp(28));
        scroll.addView(page, new ScrollView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        setContentView(scroll);

        TextView brand = new TextView(this);
        SpannableString brandText = new SpannableString("CGL+  iX OS");
        brandText.setSpan(new ForegroundColorSpan(ACCENT), 0, 4,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        brandText.setSpan(new StyleSpan(Typeface.BOLD), 0, 4,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        brandText.setSpan(new StyleSpan(Typeface.BOLD), 6, brandText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        brand.setText(brandText);
        brand.setTextSize(23);
        brand.setLetterSpacing(0.01f);
        page.addView(brand, wrap());

        TextClock clock = new TextClock(this);
        clock.setFormat12Hour("h:mm a");
        clock.setFormat24Hour("HH:mm");
        clock.setTextColor(TEXT);
        clock.setTextSize(48);
        clock.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        LinearLayout.LayoutParams clockParams = wrap();
        clockParams.topMargin = dp(28);
        page.addView(clock, clockParams);

        TextClock date = new TextClock(this);
        date.setFormat12Hour("EEEE, d MMMM");
        date.setFormat24Hour("EEEE, d MMMM");
        date.setTextColor(MUTED);
        date.setTextSize(14);
        LinearLayout.LayoutParams dateParams = wrap();
        dateParams.topMargin = dp(2);
        page.addView(date, dateParams);

        TextView tagline = text("Your device. Your way.", 25, TEXT, true);
        LinearLayout.LayoutParams taglineParams = wrap();
        taglineParams.topMargin = dp(26);
        page.addView(tagline, taglineParams);

        TextView intro = text(
                "An adaptive Android system experience from CGL+. "
                        + "Choose a layout or let iX respond to your display and input devices.",
                14, MUTED, false);
        intro.setLineSpacing(dp(3), 1.0f);
        LinearLayout.LayoutParams introParams = wrap();
        introParams.topMargin = dp(8);
        page.addView(intro, introParams);

        LinearLayout modeCard = card();
        LinearLayout.LayoutParams modeCardParams = matchWrap();
        modeCardParams.topMargin = dp(24);
        page.addView(modeCard, modeCardParams);

        TextView modeHeading = text("ADAPTIVE EXPERIENCE", 12, ACCENT, true);
        modeCard.addView(modeHeading, wrap());

        modeSummary = text("", 14, TEXT, false);
        LinearLayout.LayoutParams summaryParams = wrap();
        summaryParams.topMargin = dp(8);
        modeCard.addView(modeSummary, summaryParams);

        HorizontalScrollView modeScroller = new HorizontalScrollView(this);
        modeScroller.setHorizontalScrollBarEnabled(false);
        modeChips = new LinearLayout(this);
        modeChips.setOrientation(LinearLayout.HORIZONTAL);
        modeScroller.addView(modeChips, new HorizontalScrollView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        LinearLayout.LayoutParams chipsParams = matchWrap();
        chipsParams.topMargin = dp(16);
        modeCard.addView(modeScroller, chipsParams);
        addModeChip("Auto", "auto");
        addModeChip("Phone", "phone");
        addModeChip("Tablet", "tablet");
        addModeChip("Desktop", "desktop");

        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams actionsParams = matchWrap();
        actionsParams.topMargin = dp(16);
        page.addView(actions, actionsParams);
        actions.addView(actionTile("System settings", "Open Android settings", () -> {
            try {
                startActivity(new Intent(Settings.ACTION_SETTINGS));
            } catch (Exception ignored) {
                showMessage("Settings", "The Android Settings activity is not available in this build.");
            }
        }), new LinearLayout.LayoutParams(0, dp(94), 1));
        View spacer = new View(this);
        actions.addView(spacer, new LinearLayout.LayoutParams(dp(12), 1));
        actions.addView(actionTile("About iX", "Build and project status", this::showAbout),
                new LinearLayout.LayoutParams(0, dp(94), 1));

        TextView appsHeading = text("Your apps", 21, TEXT, true);
        LinearLayout.LayoutParams appsHeadingParams = wrap();
        appsHeadingParams.topMargin = dp(30);
        page.addView(appsHeading, appsHeadingParams);

        appCount = text("Discovering installed apps…", 13, MUTED, false);
        LinearLayout.LayoutParams countParams = wrap();
        countParams.topMargin = dp(4);
        page.addView(appCount, countParams);

        appGrid = new GridLayout(this);
        appGrid.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        appGrid.setUseDefaultMargins(false);
        LinearLayout.LayoutParams gridParams = matchWrap();
        gridParams.topMargin = dp(14);
        page.addView(appGrid, gridParams);

        TextView footer = text("CGL+ iX OS • Early development build", 12, MUTED, false);
        footer.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams footerParams = matchWrap();
        footerParams.topMargin = dp(28);
        page.addView(footer, footerParams);

        refreshMode();
        refreshApps();
    }

    private void addModeChip(String label, String mode) {
        TextView chip = text(label, 13, TEXT, true);
        chip.setGravity(Gravity.CENTER);
        chip.setPadding(dp(15), dp(10), dp(15), dp(10));
        chip.setBackground(roundRect(CARD, dp(18), MUTED, dp(1)));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.rightMargin = dp(8);
        modeChips.addView(chip, params);
        chip.setOnClickListener(v -> {
            requestedMode = mode;
            getPreferences(MODE_PRIVATE).edit().putString(MODE_KEY, mode).apply();
            refreshMode();
        });
        chip.setTag(mode);
    }

    private void refreshMode() {
        if (modeSummary == null || modeChips == null) {
            return;
        }

        String active = resolveMode();
        int widthDp = getResources().getConfiguration().screenWidthDp;
        boolean keyboard = hasInputSource(InputDevice.SOURCE_KEYBOARD);
        boolean mouse = hasInputSource(InputDevice.SOURCE_MOUSE);
        String details = "Active layout: " + capitalize(active)
                + "  •  Display: " + widthDp + " dp";
        if ("auto".equals(requestedMode)) {
            details += "
Auto mode uses display width and connected keyboard/mouse signals.";
        } else {
            details += "
Manual mode is on. Select Auto to return to detection.";
        }
        modeSummary.setText(details);

        for (int i = 0; i < modeChips.getChildCount(); i++) {
            View child = modeChips.getChildAt(i);
            String mode = (String) child.getTag();
            boolean selected = requestedMode.equals(mode);
            child.setBackground(roundRect(selected ? CARD_SELECTED : CARD,
                    dp(18), selected ? ACCENT : MUTED, dp(selected ? 2 : 1)));
            ((TextView) child).setTextColor(selected ? ACCENT : TEXT);
        }

        if (appGrid != null) {
            appGrid.setColumnCount("desktop".equals(active) ? 5
                    : "tablet".equals(active) ? 4 : 2);
            refreshApps();
        }
    }

    private String resolveMode() {
        if (!"auto".equals(requestedMode)) {
            return requestedMode;
        }
        int widthDp = getResources().getConfiguration().screenWidthDp;
        boolean keyboard = hasInputSource(InputDevice.SOURCE_KEYBOARD);
        boolean mouse = hasInputSource(InputDevice.SOURCE_MOUSE);
        if (widthDp >= 840 && keyboard && mouse) {
            return "desktop";
        }
        if (widthDp >= 600) {
            return "tablet";
        }
        return "phone";
    }

    private boolean hasInputSource(int source) {
        int[] ids = InputDevice.getDeviceIds();
        for (int id : ids) {
            InputDevice device = InputDevice.getDevice(id);
            if (device != null && (device.getSources() & source) == source) {
                return true;
            }
        }
        return false;
    }

    private void refreshApps() {
        if (appGrid == null) {
            return;
        }
        appGrid.removeAllViews();

        Intent query = new Intent(Intent.ACTION_MAIN);
        query.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager pm = getPackageManager();
        List<ResolveInfo> found = pm.queryIntentActivities(query, 0);
        List<ResolveInfo> apps = new ArrayList<>();
        Set<String> packageNames = new HashSet<>();
        for (ResolveInfo info : found) {
            if (info.activityInfo == null) {
                continue;
            }
            String packageName = info.activityInfo.packageName;
            if (getPackageName().equals(packageName) || !packageNames.add(packageName)) {
                continue;
            }
            apps.add(info);
        }
        Collections.sort(apps, (left, right) ->
                left.loadLabel(pm).toString().compareToIgnoreCase(
                        right.loadLabel(pm).toString()));

        int columns = "desktop".equals(resolveMode()) ? 5
                : "tablet".equals(resolveMode()) ? 4 : 2;
        appGrid.setColumnCount(columns);

        if (apps.isEmpty()) {
            appCount.setText("No launchable apps were found yet.");
            TextView empty = text("Apps installed on this Android build will appear here.",
                    14, MUTED, false);
            empty.setPadding(dp(8), dp(18), dp(8), dp(18));
            appGrid.addView(empty, new GridLayout.LayoutParams(
                    GridLayout.spec(0), GridLayout.spec(0, columns)));
            return;
        }

        int limit = Math.min(apps.size(), 60);
        appCount.setText(limit + (apps.size() > limit ? "+" : "")
                + " apps  •  Tap an app to open it");
        for (int i = 0; i < limit; i++) {
            ResolveInfo info = apps.get(i);
            String packageName = info.activityInfo.packageName;
            CharSequence label = info.loadLabel(pm);
            TextView tile = text(label == null ? packageName : label.toString(),
                    14, TEXT, true);
            tile.setGravity(Gravity.CENTER);
            tile.setPadding(dp(8), dp(12), dp(8), dp(12));
            tile.setMinHeight(dp(94));
            tile.setBackground(roundRect(CARD, dp(16), Color.rgb(49, 54, 62), dp(1)));
            Drawable icon = info.loadIcon(pm);
            if (icon != null) {
                icon.setBounds(0, 0, dp(30), dp(30));
                tile.setCompoundDrawables(null, icon, null, null);
                tile.setCompoundDrawablePadding(dp(8));
            }
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                    GridLayout.spec(i / columns),
                    GridLayout.spec(i % columns, 1f));
            params.width = 0;
            params.height = dp(104);
            params.setMargins(dp(4), dp(4), dp(4), dp(4));
            appGrid.addView(tile, params);
            tile.setOnClickListener(v -> {
                Intent launch = pm.getLaunchIntentForPackage(packageName);
                if (launch == null) {
                    showMessage("App unavailable", "Android did not provide a launch intent for "
                            + packageName + ".");
                    return;
                }
                launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    startActivity(launch);
                } catch (Exception ex) {
                    showMessage("Couldn't open app", packageName + "
" + ex.getMessage());
                }
            });
        }
    }

    private View actionTile(String heading, String subheading, Runnable action) {
        LinearLayout tile = card();
        tile.setGravity(Gravity.CENTER_VERTICAL);
        TextView title = text(heading, 15, TEXT, true);
        tile.addView(title, wrap());
        TextView subtitle = text(subheading, 12, MUTED, false);
        LinearLayout.LayoutParams subParams = wrap();
        subParams.topMargin = dp(5);
        tile.addView(subtitle, subParams);
        tile.setOnClickListener(v -> action.run());
        tile.setFocusable(true);
        tile.setClickable(true);
        return tile;
    }

    private void showAbout() {
        String body = "CGL+ iX OS\n"
                + "Android system-shell prototype\n\n"
                + "Android release: " + android.os.Build.VERSION.RELEASE + "\n"
                + "Build device: " + android.os.Build.DEVICE + "\n"
                + "Build product: " + android.os.Build.PRODUCT + "\n\n"
                + "This screen is built into the iX OS AOSP product. "
                + "Hardware support, networking features, HyperLink, and release security "
                + "must be validated separately.";
        new AlertDialog.Builder(this)
                .setTitle("About iX OS")
                .setMessage(body)
                .setPositiveButton("Close", null)
                .show();
    }

    private void showMessage(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    private LinearLayout card() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dp(16), dp(16), dp(16), dp(16));
        layout.setBackground(roundRect(CARD, dp(20), Color.TRANSPARENT, 0));
        return layout;
    }

    private TextView text(String value, float sizeSp, int color, boolean bold) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(sizeSp);
        view.setTextColor(color);
        view.setFontFeatureSettings("kern");
        view.setTypeface(Typeface.create("sans-serif", bold ? Typeface.BOLD : Typeface.NORMAL));
        return view;
    }

    private GradientDrawable roundRect(int color, int radius, int strokeColor, int strokeWidth) {
        GradientDrawable shape = new GradientDrawable();
        shape.setColor(color);
        shape.setCornerRadius(radius);
        if (strokeWidth > 0) {
            shape.setStroke(strokeWidth, strokeColor);
        }
        return shape;
    }

    private LinearLayout.LayoutParams wrap() {
        return new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private LinearLayout.LayoutParams matchWrap() {
        return new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private String capitalize(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        return value.substring(0, 1).toUpperCase(Locale.ROOT) + value.substring(1);
    }
}
