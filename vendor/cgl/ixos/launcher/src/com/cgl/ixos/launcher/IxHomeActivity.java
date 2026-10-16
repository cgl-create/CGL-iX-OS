package com.cgl.ixos.launcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public final class IxHomeActivity extends Activity {
    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_home);

        TextView status = findViewById(R.id.status_text);
        status.setText("iX OS • AOSP development build");

        Button apps = findViewById(R.id.apps_button);
        apps.setOnClickListener(v -> openAppDrawer());

        Button settings = findViewById(R.id.settings_button);
        settings.setOnClickListener(v ->
                startActivity(new Intent(Settings.ACTION_SETTINGS)));

        Button about = findViewById(R.id.about_button);
        about.setOnClickListener(v -> status.setText(
                "iX OS by CGL+\nAOSP Cuttlefish x86_64 development target\n" +
                "Build: development\nBrand: #F2C94C"));
    }

    private void openAppDrawer() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(Intent.createChooser(intent, "Open an app"));
        } catch (RuntimeException ignored) {
            TextView status = findViewById(R.id.status_text);
            status.setText("No launchable apps were found.");
        }
    }
}
