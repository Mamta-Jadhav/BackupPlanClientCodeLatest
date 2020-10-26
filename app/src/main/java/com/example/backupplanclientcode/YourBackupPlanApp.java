package com.example.backupplanclientcode;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.example.backupplanclientcode.Bugsense.Bugsense;
import com.example.backupplanclientcode.Preference.SettingPreference;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.firebase.auth.PhoneAuthProvider;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Locale;

public class YourBackupPlanApp extends Application {
    public static String COUNTRY_CODE = "";
    public static final long REGISTRATION_EXPIRY_TIME_MS = 604800000;
    public static String chat_conversation_id = "";
    public static String exp_id_for_fav_flag = "";
    public static int fav_flag = 0;
    String GCM_reg_id;
    Context context;
    GoogleCloudMessaging gcm;
    Locale locale;
    ProgressDialog mProgressDialog;

    public static String isInBackground = "false";
    public static String isInForeground = "false";

    /* renamed from: mp */
    SettingPreference f58mp;

    private class registerBackground extends AsyncTask<String, String, String> {
        private registerBackground() {
        }

        /* access modifiers changed from: protected */
        public String doInBackground(String... params) {
            String str = "";
            try {
                if (YourBackupPlanApp.this.gcm == null) {
                    YourBackupPlanApp.this.gcm = GoogleCloudMessaging.getInstance(YourBackupPlanApp.this.context);
                }
                YourBackupPlanApp.this.GCM_reg_id = YourBackupPlanApp.this.gcm.register(YourBackupPlanApp.this.getResources().getString(R.string.gsm_sender));
                Log.e("", "registerBackground : gcm reg id : " + YourBackupPlanApp.this.GCM_reg_id);
                String msg = "Device registered, registration id = " + YourBackupPlanApp.this.GCM_reg_id;
                YourBackupPlanApp.this.setRegistrationId(YourBackupPlanApp.this.context, YourBackupPlanApp.this.GCM_reg_id);
                return msg;
            } catch (IOException ex) {
                return "Error :" + ex.getMessage();
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String msg) {
            if (!YourBackupPlanApp.this.GCM_reg_id.equalsIgnoreCase("")) {
                YourBackupPlanApp.this.f58mp.setStringValue("gcm_registration_id", YourBackupPlanApp.this.GCM_reg_id);
                if (YourBackupPlanApp.this.f58mp.getStringValue("device_id", "").equalsIgnoreCase("") || YourBackupPlanApp.this.f58mp.getStringValue("device_id", "") == null) {
                    YourBackupPlanApp.this.f58mp.setStringValue("device_id", YourBackupPlanApp.this.GetDeviceID());
                }
            }
        }
    }

    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
        this.f58mp = new SettingPreference(getApplicationContext());
        new Bugsense().startBugsense(getApplicationContext());
    }

    public void getGCMRegID() {
        if (this.f58mp.getStringValue("gcm_registration_id", "").equalsIgnoreCase("")) {
            this.GCM_reg_id = getRegistrationId(this.context);
            Log.e("Preference", "GCM_regId is : " + this.GCM_reg_id);
            if (this.GCM_reg_id.length() == 0) {
                new registerBackground().execute(new String[]{null, null, null});
                Log.e("length == 0", "GCM_regId is : " + this.GCM_reg_id);
            } else {
                this.f58mp.setStringValue("gcm_registration_id", this.GCM_reg_id);
                Log.e("length != 0", "GCM_regId is : " + this.GCM_reg_id);
                if (this.f58mp.getStringValue("device_id", "").equalsIgnoreCase("") || this.f58mp.getStringValue("device_id", "") == null) {
                    this.f58mp.setStringValue("device_id", GetDeviceID());
                }
            }
            this.gcm = GoogleCloudMessaging.getInstance(this.context);
            return;
        }
        Log.e("", "getGCMRegID : else :" + this.f58mp.getStringValue("gcm_registration_id", ""));
    }

    /* access modifiers changed from: private */
    public String GetDeviceID() {
        getApplicationContext();
        return ((TelephonyManager) getSystemService(PhoneAuthProvider.PROVIDER_ID)).getDeviceId();
    }

    public static String GetDeviceID(Context ctx) {
        return ((TelephonyManager) ctx.getSystemService(PhoneAuthProvider.PROVIDER_ID)).getDeviceId();
    }

    public String getRegistrationId(Context context2) {
        String registrationId = this.f58mp.getStringValue("reg_id", "");
        if (registrationId.length() == 0) {
            Log.e("gcm register not found", ".........Registration not found.");
            return "";
        } else if (Integer.parseInt(this.f58mp.getStringValue("appVersion", "")) == getAppVersion(context2) && !isRegistrationExpired()) {
            return registrationId;
        } else {
            Log.e("...", "App version changed or registration expired.");
            return "";
        }
    }

    private static int getAppVersion(Context context2) {
        try {
            return context2.getPackageManager().getPackageInfo(context2.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private boolean isRegistrationExpired() {
        return System.currentTimeMillis() > Long.parseLong(this.f58mp.getStringValue("onServerExpirationTimeMs", ""));
    }

    /* access modifiers changed from: private */
    public void setRegistrationId(Context context2, String regId) {
        int appVersion = getAppVersion(context2);
        Log.e("...", "Saving regId on app version " + appVersion);
        long expirationTime = System.currentTimeMillis() + REGISTRATION_EXPIRY_TIME_MS;
        this.f58mp.setStringValue("registration_id", regId);
        this.f58mp.setStringValue("appVersion", String.valueOf(appVersion));
        this.f58mp.setStringValue("onServerExpirationTimeMs", String.valueOf(expirationTime));
        Log.e("", "Setting registration expiry time to " + new Timestamp(expirationTime));
    }

    /* access modifiers changed from: protected */
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
