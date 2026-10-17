package com.cgl.ixos.launcher;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public final class IxHomeActivity extends Activity {
    private TextView status;

    private static final AppShortcut[] SHORTCUTS = {
        new AppShortcut("Phone", "☎", "#2FAD69", "tel:"),
        new AppShortcut("Messages", "●", "#4B91F0", "sms:"),
        new AppShortcut("Internet", "◎", "#3C78D8", "https://www.google.com/"),
        new AppShortcut("Gallery", "▧", "#C45BE6", "https://photos.google.com/"),
        new AppShortcut("Play Store", "▶", "#20A7A0", "https://play.google.com/store"),
        new AppShortcut("Google", "G", "#FFFFFF", "https://www.google.com/"),
        new AppShortcut("Gmail", "M", "#EA4335", "https://mail.google.com/"),
        new AppShortcut("Maps", "⌖", "#34A853", "https://maps.google.com/"),
        new AppShortcut("YouTube", "▶", "#FF0033", "https://youtube.com/"),
        new AppShortcut("Drive", "▲", "#F4B400", "https://drive.google.com/"),
        new AppShortcut("Photos", "✿", "#4285F4", "https://photos.google.com/"),
        new AppShortcut("CGL+ HyperLink", "C+", "#F2C94C", "https://crazygamelabs.co.uk/"),
        new AppShortcut("Files", "▤", "#4985D6", "https://drive.google.com/"),
        new AppShortcut("Settings", "⚙", "#69717D", null)
    };

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_home);

        status = findViewById(R.id.status_text);
        status.setText("iX OS • AOSP development build");

        GridLayout grid = findViewById(R.id.app_grid);
        for (AppShortcut shortcut : SHORTCUTS) {
            grid.addView(createShortcut(shortcut));
        }

        Button apps = findViewById(R.id.apps_button);
        apps.setOnClickListener(v -> status.setText(
                "App grid ready • Google web shortcuts and CGL+ HyperLink are included."));

        Button settings = findViewById(R.id.settings_button);
        settings.setOnClickListener(v ->
                startActivity(new Intent(Settings.ACTION_SETTINGS)));

        Button about = findViewById(R.id.about_button);
        about.setOnClickListener(v -> status.setText(
                "iX OS by CGL+\nAOSP Cuttlefish x86_64 development target\n" +
                "Build: development\nBrand: #F2C94C\n" +
                "Google shortcuts open web services; Google Mobile Services is not bundled."));
    }

    private View createShortcut(AppShortcut shortcut) {
        LinearLayout item = new LinearLayout(this);
        item.setOrientation(LinearLayout.VERTICAL);
        item.setGravity(Gravity.CENTER);
        item.setPadding(4, 8, 4, 8);
        item.setClickable(true);
        item.setFocusable(true);

        TextView icon = new TextView(this);
        icon.setText(shortcut.glyph);
        icon.setTextSize(shortcut.label.equals("Google") ? 26 : 22);
        icon.setTextColor(shortcut.label.equals("Google") ? Color.rgb(66, 133, 244) : Color.WHITE);
        icon.setGravity(Gravity.CENTER);
        GradientDrawable shape = new GradientDrawable();
        shape.setColor(Color.parseColor(shortcut.color));
        shape.setCornerRadius(dp(17));
        shape.setStroke(dp(1), Color.parseColor("#26FFFFFF"));
        icon.setBackground(shape);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(dp(54), dp(54));
        item.addView(icon, iconParams);

        TextView label = new TextView(this);
        label.setText(shortcut.label);
        label.setTextColor(Color.WHITE);
        label.setTextSize(10);
        label.setGravity(Gravity.CENTER);
        label.setMaxLines(2);
        LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        labelParams.topMargin = dp(6);
        item.addView(label, labelParams);

        item.setOnClickListener(v -> launchShortcut(shortcut));
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.setMargins(dp(2), dp(2), dp(2), dp(2));
        item.setLayoutParams(params);
        return item;
    }

    private void launchShortcut(AppShortcut shortcut) {
        if (shortcut.url == null) {
            startActivity(new Intent(Settings.ACTION_SETTINGS));
            return;
        }
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(shortcut.url)));
        } catch (RuntimeException e) {
            status.setText("Unable to open " + shortcut.label + ".");
        }
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    private static final class AppShortcut {
        final String label;
        final String glyph;
        final String color;
        final String url;

        AppShortcut(String label, String glyph, String color, String url) {
            this.label = label;
            this.glyph = glyph;
            this.color = color;
            this.url = url;
        }
    }
}
