package com.example;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import androidx.appcompat.app.AppCompatDelegate;

public class ThemePreferences {
    private static final String PREF_NAME = "theme_prefs";
    private static final String KEY_IS_DARK = "is_dark_mode";
    private static final String KEY_HAS_USER_SET = "has_user_set_mode";

    public static boolean isDarkMode(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if (prefs.contains(KEY_IS_DARK)) {
            return prefs.getBoolean(KEY_IS_DARK, false);
        }
        int currentNightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES;
    }

    public static void setDarkMode(Context context, boolean isDark) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit()
             .putBoolean(KEY_IS_DARK, isDark)
             .putBoolean(KEY_HAS_USER_SET, true)
             .apply();
        applyTheme(isDark);
    }

    public static void applyInitialTheme(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if (prefs.contains(KEY_IS_DARK)) {
            applyTheme(prefs.getBoolean(KEY_IS_DARK, false));
        }
    }

    public static void applyTheme(boolean isDark) {
        AppCompatDelegate.setDefaultNightMode(
            isDark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
    }
}
