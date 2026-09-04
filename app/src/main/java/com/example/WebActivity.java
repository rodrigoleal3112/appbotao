package com.example;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class WebActivity extends AppCompatActivity {

    public static final String EXTRA_SERVICE_ID = "extra_service_id";

    private WebView webView;
    private LinearProgressIndicator progressBar;
    private LinearLayout layoutError;
    private MaterialToolbar toolbar;
    private ExtendedFloatingActionButton fabBack;

    private BusService busService;

    public static Intent newIntent(Context context, BusService service) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(EXTRA_SERVICE_ID, service.getId());
        return intent;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ThemePreferences.applyInitialTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_browser);

        String serviceId = getIntent().getStringExtra(EXTRA_SERVICE_ID);
        busService = BusService.fromId(serviceId);

        toolbar = findViewById(R.id.web_toolbar);
        progressBar = findViewById(R.id.web_progress_bar);
        webView = findViewById(R.id.web_view);
        layoutError = findViewById(R.id.layout_error);
        fabBack = findViewById(R.id.fab_back);

        MaterialButton btnRetry = findViewById(R.id.btn_retry);
        MaterialButton btnErrorBack = findViewById(R.id.btn_error_back);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setTitle(busService.getTitle() + (busService.hasMap() ? " (com Mapa)" : ""));
        toolbar.setSubtitle(busService.getUrl());
        toolbar.setNavigationOnClickListener(v -> finish());

        // Floating action button "Voltar"
        fabBack.setOnClickListener(v -> finish());

        // Retry and back in error view
        btnRetry.setOnClickListener(v -> {
            layoutError.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            webView.reload();
        });
        btnErrorBack.setOnClickListener(v -> finish());

        // Configure WebView
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        String ua = settings.getUserAgentString();
        if (ua != null) {
            settings.setUserAgentString(ua.replace("; wv", ""));
        }

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                layoutError.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (request != null && request.isForMainFrame()) {
                    progressBar.setVisibility(View.GONE);
                    layoutError.setVisibility(View.VISIBLE);
                    webView.setVisibility(View.GONE);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (request == null || request.getUrl() == null) return false;
                String targetUrl = request.getUrl().toString();
                if (targetUrl.startsWith("http://") || targetUrl.startsWith("https://")) {
                    return false;
                } else {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(targetUrl));
                        startActivity(intent);
                        return true;
                    } catch (Exception e) {
                        return true;
                    }
                }
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgressCompat(newProgress, true);
                if (newProgress >= 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        // Load the service URL
        webView.loadUrl(busService.getUrl());

        // Back press handling: go back in web history if possible, otherwise finish activity
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web_browser, menu);
        MenuItem themeItem = menu.findItem(R.id.action_toggle_theme);
        if (themeItem != null) {
            boolean isDark = ThemePreferences.isDarkMode(this);
            themeItem.setIcon(isDark ? R.drawable.ic_light_mode : R.drawable.ic_dark_mode);
            themeItem.setTitle(isDark ? "Modo Claro" : "Modo Escuro");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_web_back) {
            if (webView.canGoBack()) {
                webView.goBack();
            }
            return true;
        } else if (id == R.id.action_refresh) {
            layoutError.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            webView.reload();
            return true;
        } else if (id == R.id.action_open_external) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(busService.getUrl()));
            startActivity(intent);
            return true;
        } else if (id == R.id.action_toggle_theme) {
            boolean isDark = ThemePreferences.isDarkMode(this);
            ThemePreferences.setDarkMode(this, !isDark);
            recreate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }
}
