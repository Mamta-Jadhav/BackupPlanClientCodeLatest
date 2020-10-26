package com.example.backupplanclientcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.backupplanclientcode.Bugsense.Bugsense;
import com.google.android.gms.fitness.FitnessStatusCodes;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.gun0912.tedpermission.TedPermission.Builder;

import java.util.List;

public class Splash_Screen extends Activity {
    private static int SPLASH_TIME_OUT = FitnessStatusCodes.NEEDS_OAUTH_PERMISSIONS;
    PermissionListener permissionlistener = new PermissionListener() {
        public void onPermissionGranted() {
            ((YourBackupPlanApp) Splash_Screen.this.getApplication()).getGCMRegID();
            Splash_Screen.this.startApplication();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {

        }
    };

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        new Bugsense().startBugsense(getApplicationContext());
        checkPremissions();
    }

    private void checkPremissions() {
        ((Builder) ((Builder) ((Builder) TedPermission.with(this).setPermissionListener(this.permissionlistener)).setDeniedMessage((CharSequence) "These permissions are mandatory in order to proceed. \n Please turn on permissions from (Setting > Permission)")).setPermissions("android.permission.READ_CONTACTS", "android.permission.READ_PHONE_STATE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE")).check();
    }

    /* access modifiers changed from: private */
    public void startApplication() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Splash_Screen.this.startActivity(new Intent(Splash_Screen.this, loginActivity.class));
                Splash_Screen.this.finish();
            }
        }, (long) SPLASH_TIME_OUT);
    }
}
