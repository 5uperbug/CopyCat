package com.github.copycat.android.core;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by rohit on 11/16/15.
 */
public class SettingsManager {
    private static SettingsManager sManager;

    private final static String SETTINGS_SHOW_TOAST = "copycat.show_toasts";
    private final static String SETTINGS_MONITOR_ENABLED = "copycat.enabled";

    private Context context;
    private SharedPreferences preferences;

    public SettingsManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(SettingsManager.class.getName(), Context.MODE_PRIVATE);
    }

    public static SettingsManager getInstance(Context context) {
        if (sManager == null) {
            sManager = new SettingsManager(context);
        }
        return sManager;
    }

    public boolean getNotifyToast() {
        return preferences.getBoolean(SETTINGS_SHOW_TOAST, true);
    }

    public void setNotifyToast(boolean status) {
        preferences.edit().putBoolean(SETTINGS_SHOW_TOAST, status).apply();
    }

    public boolean getServiceEnabled() {
        return preferences.getBoolean(SETTINGS_MONITOR_ENABLED, true);
    }

    public void setServiceEnabled(boolean status) {
        preferences.edit().putBoolean(SETTINGS_SHOW_TOAST, status).apply();
    }
}
