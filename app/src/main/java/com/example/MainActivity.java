package com.example;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.materialswitch.MaterialSwitch;

public class MainActivity extends AppCompatActivity {

    private MaterialSwitch switchTheme;
    private ImageView ivThemeIcon;
    private TextView tvThemeTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ThemePreferences.applyInitialTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupThemeToggle();
        setupBusButtons();
    }

    private void initViews() {
        switchTheme = findViewById(R.id.switch_theme);
        ivThemeIcon = findViewById(R.id.iv_theme_icon);
        tvThemeTitle = findViewById(R.id.tv_theme_title);
    }

    private void setupThemeToggle() {
        boolean isDark = ThemePreferences.isDarkMode(this);
        updateThemeUI(isDark);

        switchTheme.setChecked(isDark);
        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked != ThemePreferences.isDarkMode(MainActivity.this)) {
                ThemePreferences.setDarkMode(MainActivity.this, isChecked);
            }
        });

        MaterialCardView cardThemeToggle = findViewById(R.id.card_theme_toggle);
        cardThemeToggle.setOnClickListener(v -> {
            boolean nextState = !switchTheme.isChecked();
            switchTheme.setChecked(nextState);
        });
    }

    private void updateThemeUI(boolean isDark) {
        if (isDark) {
            ivThemeIcon.setImageResource(R.drawable.ic_dark_mode);
            tvThemeTitle.setText("Modo Escuro Ativo");
        } else {
            ivThemeIcon.setImageResource(R.drawable.ic_light_mode);
            tvThemeTitle.setText("Modo Claro Ativo");
        }
    }

    private void setupBusButtons() {
        // --- BOTÃO 1: JOTUR (com mapa) ---
        MaterialButton btnOpenJotur = findViewById(R.id.btn_open_jotur);
        MaterialButton btnExternalJotur = findViewById(R.id.btn_external_jotur);
        MaterialCardView cardJotur = findViewById(R.id.card_jotur);

        btnOpenJotur.setOnClickListener(v -> openBusService(BusService.JOTUR));
        cardJotur.setOnClickListener(v -> openBusService(BusService.JOTUR));
        btnExternalJotur.setOnClickListener(v -> openExternalUrl(BusService.JOTUR.getUrl()));

        // --- BOTÃO 2: CONSÓRCIO FÊNIX ---
        MaterialButton btnOpenFenix = findViewById(R.id.btn_open_fenix);
        MaterialButton btnExternalFenix = findViewById(R.id.btn_external_fenix);
        MaterialCardView cardFenix = findViewById(R.id.card_fenix);

        btnOpenFenix.setOnClickListener(v -> openBusService(BusService.CONSORCIO_FENIX));
        cardFenix.setOnClickListener(v -> openBusService(BusService.CONSORCIO_FENIX));
        btnExternalFenix.setOnClickListener(v -> openExternalUrl(BusService.CONSORCIO_FENIX.getUrl()));
    }

    private void openBusService(BusService service) {
        Intent intent = WebActivity.newIntent(this, service);
        startActivity(intent);
    }

    private void openExternalUrl(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isDark = ThemePreferences.isDarkMode(this);
        if (switchTheme != null) {
            switchTheme.setChecked(isDark);
            updateThemeUI(isDark);
        }
    }
}
