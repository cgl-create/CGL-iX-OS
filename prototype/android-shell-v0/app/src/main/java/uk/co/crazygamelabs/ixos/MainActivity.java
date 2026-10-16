package uk.co.crazygamelabs.ixos;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public final class MainActivity extends Activity {
    private static final int BG = Color.rgb(16, 17, 22);
    private static final int PANEL = Color.rgb(31, 33, 42);
    private static final int EDGE = Color.argb(70, 255, 255, 255);
    private static final int YELLOW = Color.rgb(242, 201, 76);
    private static final int WHITE = Color.rgb(246, 247, 250);
    private static final int MUTED = Color.rgb(169, 174, 188);
    private LinearLayout content;
    private GridLayout appGrid;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(BG);
        getWindow().getDecorView().setSystemUiVisibility(0);
        buildShell();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        rebuildGrid();
    }

    private void buildShell() {
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(BG);

        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(22), dp(26), dp(22), dp(24));
        scroll.addView(content, new ScrollView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayout top = new LinearLayout(this);
        top.setGravity(Gravity.CENTER_VERTICAL);
        top.setOrientation(LinearLayout.HORIZONTAL);

        TextView brand = text("iX", 30, WHITE, true);
        TextView plus = text("+", 30, YELLOW, true);
        LinearLayout mark = new LinearLayout(this);
        mark.setGravity(Gravity.CENTER_VERTICAL);
        mark.addView(brand);
        mark.addView(plus);
        top.addView(mark, new LinearLayout.LayoutParams(0, -2, 1));

        TextView build = text("PREVIEW  ·  0.1.0", 11, MUTED, true);
        build.setLetterSpacing(0.08f);
        top.addView(build);
        content.addView(top);

        TextView eyebrow = text("CGL+ OPERATING SYSTEM", 11, YELLOW, true);
        eyebrow.setLetterSpacing(0.14f);
        LinearLayout.LayoutParams eyebrowParams = params(-2, -2);
        eyebrowParams.topMargin = dp(36);
        content.addView(eyebrow, eyebrowParams);

        TextView heading = text("A clearer way\nto work.", 34, WHITE, true);
        heading.setLineSpacing(dp(1), 1.0f);
        LinearLayout.LayoutParams headingParams = params(-2, -2);
        headingParams.topMargin = dp(8);
        content.addView(heading, headingParams);

        TextView subheading = text(
                "One adaptive experience. Built around you.", 15, MUTED, false);
        LinearLayout.LayoutParams subParams = params(-2, -2);
        subParams.topMargin = dp(10);
        content.addView(subheading, subParams);

        LinearLayout status = glassCard();
        status.setOrientation(LinearLayout.VERTICAL);
        TextView statusTitle = text("SYSTEM PREVIEW", 10, YELLOW, true);
        statusTitle.setLetterSpacing(0.12f);
        status.addView(statusTitle);
        TextView statusBody = text(
                "Native Android prototype\nTouch-first  ·  Adaptive layout  ·  CGL+ yellow",
                14, WHITE, false);
        statusBody.setLineSpacing(dp(5), 1.0f);
        LinearLayout.LayoutParams statusBodyParams = params(-1, -2);
        statusBodyParams.topMargin = dp(10);
        status.addView(statusBody, statusBodyParams);
        LinearLayout.LayoutParams statusParams = params(-1, -2);
        statusParams.topMargin = dp(24);
        content.addView(status, statusParams);

        TextView section = text("Your space", 21, WHITE, true);
        LinearLayout.LayoutParams sectionParams = params(-2, -2);
        sectionParams.topMargin = dp(30);
        content.addView(section, sectionParams);

        TextView sectionHint = text("Prototype tiles — services are not connected yet.", 13, MUTED, false);
        LinearLayout.LayoutParams hintParams = params(-2, -2);
        hintParams.topMargin = dp(4);
        content.addView(sectionHint, hintParams);

        appGrid = new GridLayout(this);
        appGrid.setUseDefaultMargins(false);
        LinearLayout.LayoutParams gridParams = params(-1, -2);
        gridParams.topMargin = dp(16);
        content.addView(appGrid, gridParams);
        rebuildGrid();

        TextView footer = text("CGL+  /  iX OS  /  EARLY PROTOTYPE", 10, MUTED, true);
        footer.setGravity(Gravity.CENTER);
        footer.setLetterSpacing(0.06f);
        LinearLayout.LayoutParams footerParams = params(-1, -2);
        footerParams.topMargin = dp(30);
        content.addView(footer, footerParams);

        setContentView(scroll);
    }

    private void rebuildGrid() {
        if (appGrid == null) return;
        appGrid.removeAllViews();
        int widthDp = getResources().getConfiguration().screenWidthDp;
        int columns = widthDp >= 840 ? 4 : (widthDp >= 600 ? 3 : 2);
        appGrid.setColumnCount(columns);
        appGrid.setRowCount((int) Math.ceil(4.0 / columns));

        String[][] apps = {
                {"Apps", "Explore your workspace"},
                {"HyperLink", "Connect your devices"},
                {"Files", "Your files, in one place"},
                {"Settings", "Make iX yours"}
        };

        for (String[] app : apps) {
            LinearLayout tile = glassCard();
            tile.setOrientation(LinearLayout.VERTICAL);
            tile.setMinimumHeight(dp(116));
            tile.setGravity(Gravity.CENTER_VERTICAL);

            TextView glyph = text(app[0].substring(0, 1), 22, YELLOW, true);
            glyph.setGravity(Gravity.CENTER);
            GradientDrawable glyphBg = rounded(Color.argb(28, 242, 201, 76), YELLOW, 14);
            glyph.setBackground(glyphBg);
            glyph.setWidth(dp(42));
            glyph.setHeight(dp(42));
            tile.addView(glyph);

            TextView name = text(app[0], 16, WHITE, true);
            LinearLayout.LayoutParams nameParams = params(-1, -2);
            nameParams.topMargin = dp(12);
            tile.addView(name, nameParams);

            TextView description = text(app[1], 12, MUTED, false);
            LinearLayout.LayoutParams descParams = params(-1, -2);
            descParams.topMargin = dp(3);
            tile.addView(description, descParams);

            tile.setContentDescription(app[0] + ". " + app[1] + ". Prototype placeholder.");
            tile.setClickable(true);
            tile.setFocusable(true);
            tile.setOnClickListener(v -> Toast.makeText(
                    this, app[0] + " is a placeholder in this prototype.",
                    Toast.LENGTH_SHORT).show());

            GridLayout.LayoutParams itemParams = new GridLayout.LayoutParams();
            itemParams.width = 0;
            itemParams.height = -2;
            itemParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            itemParams.setMargins(dp(4), dp(4), dp(4), dp(4));
            appGrid.addView(tile, itemParams);
        }
        appGrid.setVisibility(View.VISIBLE);
    }

    private LinearLayout glassCard() {
        LinearLayout card = new LinearLayout(this);
        card.setPadding(dp(17), dp(17), dp(17), dp(17));
        card.setBackground(rounded(PANEL, EDGE, 20));
        return card;
    }

    private TextView text(String value, int size, int color, boolean bold) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(size);
        view.setTextColor(color);
        view.setTypeface(Typeface.create("sans-serif", bold ? Typeface.BOLD : Typeface.NORMAL));
        return view;
    }

    private GradientDrawable rounded(int fill, int stroke, int radiusDp) {
        GradientDrawable shape = new GradientDrawable();
        shape.setColor(fill);
        shape.setCornerRadius(dp(radiusDp));
        shape.setStroke(dp(1), stroke);
        return shape;
    }

    private LinearLayout.LayoutParams params(int width, int height) {
        return new LinearLayout.LayoutParams(width, height);
    }

    private int dp(float value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
