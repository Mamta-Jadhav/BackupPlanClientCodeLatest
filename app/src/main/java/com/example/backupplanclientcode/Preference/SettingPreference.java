package com.example.backupplanclientcode.Preference;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import com.example.backupplanclientcode.Constant.Constant;

public class SettingPreference {
    Context context;

    public SettingPreference(Context ctx) {
        this.context = ctx;
    }

    public void setStringValue(String Key, String value) {
        Editor editor = this.context.getSharedPreferences(Constant.PrefName, 0).edit();
        editor.putString(Key, value);
        editor.commit();
    }

    public void setBooleanValue(String Key, boolean value) {
        Editor editor = this.context.getSharedPreferences(Constant.PrefName, 0).edit();
        editor.putBoolean(Key, value);
        editor.commit();
    }

    public String getStringValue(String Key, String defaultString) {
        return this.context.getSharedPreferences(Constant.PrefName, 0).getString(Key, defaultString);
    }

    public boolean getBooleanValue(String Key, boolean defaultValue) {
        return this.context.getSharedPreferences(Constant.PrefName, 0).getBoolean(Key, defaultValue);
    }
}
