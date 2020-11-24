package com.example.backupplanclientcode;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.backupplanclientcode.Asyntask.GeneralTask;
import com.example.backupplanclientcode.Bugsense.Bugsense;
import com.example.backupplanclientcode.Constant.Constant;
import com.example.backupplanclientcode.Database.DBHelper;
import com.example.backupplanclientcode.Menu.DocumentsMenu;
import com.example.backupplanclientcode.Menu.EmergencyMenuActivity;
import com.example.backupplanclientcode.Menu.EmployerMenuActivity;
import com.example.backupplanclientcode.Menu.InsuranceMenuActivity;
import com.example.backupplanclientcode.Menu.InternetMenuActivity;
import com.example.backupplanclientcode.Menu.MedicalMenuListActivity;
import com.example.backupplanclientcode.Menu.MenuGuestUserActivity;
import com.example.backupplanclientcode.Menu.MenuNotification;
import com.example.backupplanclientcode.Menu.MenuPasswordActivity;
import com.example.backupplanclientcode.Menu.MenuResourceActivity;
import com.example.backupplanclientcode.Preference.SettingPreference;
import com.example.backupplanclientcode.ServiceUrl.ServiceUrl;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.backupplanclientcode.LogOutTimerUtil.foreGround;
import static com.example.backupplanclientcode.LogOutTimerUtil.logout;

public class MenuListActivity extends Activity implements OnClickListener, GeneralTask.ResponseListener_General, LogOutTimerUtil.LogOutListener {

    LinearLayout account_menu;
    Button btn_setting;
    DBHelper dbHelper;
    LinearLayout medical_menu;
    LinearLayout menu_Asset;
    LinearLayout menu_Planning;
    LinearLayout menu_add_coupon;
    LinearLayout menu_documents;
    LinearLayout menu_emergency;
    LinearLayout menu_employer;
    LinearLayout menu_guest_user;
    LinearLayout menu_insurance;
    LinearLayout menu_internet;
    LinearLayout menu_notificaion;
    LinearLayout menu_password;
    LinearLayout menu_profile;
    LinearLayout menu_resource;
    LinearLayout menu_upgrade;
    SettingPreference pref;
    TextView tv_guest_user;
    TextView tv_guest_user_entres;
    TextView tv_notificaion_status;
    TextView tv_password_entries;
    TextView tv_password_menu;
    Timer timer;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);
        this.pref = new SettingPreference(getApplicationContext());
        this.dbHelper = new DBHelper(getApplicationContext());
        new Bugsense().startBugsense(getApplicationContext());
        findViewId();
        check_notificaion();
        Log.d("###SUBSCRIPTION###", "subscription: " + this.pref.getStringValue(Constant.subscription, "") + " <--> subscription_expired: " + this.pref.getBooleanValue(Constant.subscription_expired, false));
    }

    private void check_notificaion() {
        if (getIntent().hasExtra("message")) {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(1);
            dialog.setContentView(R.layout.notification_dialog);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().setWindowAnimations(R.style.dialog_animation_fade);
            ((TextView) dialog.findViewById(R.id.tv_message)).setText(getIntent().getStringExtra("message").trim());
            dialog.findViewById(R.id.btn_ok).setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    private void findViewId() {
        this.menu_profile = findViewById(R.id.menu_profile);
        this.account_menu = findViewById(R.id.account_menu);
        this.menu_documents = findViewById(R.id.menu_documents);
        this.menu_Asset = findViewById(R.id.menu_Asset);
        this.menu_Planning = findViewById(R.id.menu_Planning);
        this.medical_menu = findViewById(R.id.medical_menu);
        this.menu_internet = findViewById(R.id.menu_internet);
        this.menu_employer = findViewById(R.id.menu_employer);
        this.menu_insurance = findViewById(R.id.menu_insurance);
        this.menu_emergency = findViewById(R.id.menu_emergency);
        this.menu_resource = findViewById(R.id.menu_resource);
        this.menu_notificaion = findViewById(R.id.menu_notificaion);
//        this.menu_add_coupon = findViewById(R.id.menu_add_coupon);
//        this.menu_upgrade = findViewById(R.id.menu_upgrade);
        this.menu_guest_user = findViewById(R.id.menu_guest_user);
        this.menu_password = findViewById(R.id.menu_password);
        this.btn_setting = findViewById(R.id.btn_setting);
        this.menu_profile.setOnClickListener(this);
        this.account_menu.setOnClickListener(this);
        this.menu_documents.setOnClickListener(this);
        this.menu_Asset.setOnClickListener(this);
        this.menu_Planning.setOnClickListener(this);
        this.medical_menu.setOnClickListener(this);
        this.menu_internet.setOnClickListener(this);
        this.menu_employer.setOnClickListener(this);
        this.menu_insurance.setOnClickListener(this);
        this.menu_emergency.setOnClickListener(this);
        this.menu_resource.setOnClickListener(this);
        this.menu_guest_user.setOnClickListener(this);
        this.menu_password.setOnClickListener(this);
        this.menu_notificaion.setOnClickListener(this);
//        this.menu_add_coupon.setOnClickListener(this);
//        this.menu_upgrade.setOnClickListener(this);
        this.tv_guest_user_entres = findViewById(R.id.tv_guest_user_entres);
        this.btn_setting.setOnClickListener(this);
        if (this.pref.getBooleanValue(Constant.showAlertFirstTime, false)) {
            show_user_message_dialog();
        }
        setEnableMenu();
    }

    private void check_notification_status() {
        this.tv_notificaion_status = findViewById(R.id.tv_notificaion_status);
        if (this.pref.getBooleanValue(Constant.isNotification, false)) {
            this.tv_notificaion_status.setText("ON");
        } else {
            this.tv_notificaion_status.setText("OFF");
        }
        try {
//            Cursor cur = this.dbHelper.getGuestUserList(this.pref.getStringValue(Constant.user_id, ""));
//            if (cur != null) {
            if (!pref.getStringValue(Constant.guestCount, "").equalsIgnoreCase("") && !pref.getStringValue(Constant.guestCount, "").equalsIgnoreCase("0")) {
                this.tv_guest_user_entres.setVisibility(View.VISIBLE);
                this.tv_guest_user_entres.setText(pref.getStringValue(Constant.guestCount, "") + " Entries");
//                this.tv_guest_user_entres.setText("" + cur.getCount() + " Entries");
            } else {
                this.tv_guest_user_entres.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEnableMenu() {
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            this.tv_guest_user = findViewById(R.id.tv_guest_user);
            this.tv_guest_user.setTextColor(-7829368);
            this.tv_guest_user_entres.setTextColor(-7829368);
            this.tv_password_menu = findViewById(R.id.tv_password_menu);
            this.tv_password_entries = findViewById(R.id.tv_password_entries);
            this.tv_password_menu.setTextColor(-7829368);
            this.tv_password_entries.setTextColor(-7829368);
        }
    }

    private void show_user_message_dialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.pop_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setWindowAnimations(R.style.dialog_animation_fade);
        dialog.findViewById(R.id.btn_ok).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MenuListActivity.this.pref.setBooleanValue(Constant.showAlertFirstTime, false);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void on_GeneralSuccess(JSONObject response, int responseCode) {
        try {
            Log.d("~~~~~~~~~~~~>", "" + responseCode + " | " + response.toString());
            if (responseCode == 1) {
                if (response.getBoolean("success") == true) {
                    displayToast("" + response.getString("message"));
                    this.pref.setBooleanValue(Constant.isLogin, false);
                    this.pref.setBooleanValue(Constant.isGuestLogin, false);
                    startActivity(new Intent(getApplicationContext(), loginActivity.class));
                    finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            displayToast("" + e.getLocalizedMessage());
        }
    }

    private void displayToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_setting /*2131558591*/:
                try {

                    if (!this.pref.getStringValue(Constant.jwttoken, "").equalsIgnoreCase("")) {
                        JSONObject nameValuePairs = new JSONObject();
                        nameValuePairs.put("token", this.pref.getStringValue(Constant.jwttoken, ""));

                        new GeneralTask(this, ServiceUrl.logout, nameValuePairs, 1, "get").execute();
                    } else {
                        displayToast("User logged out successfully.");
                        this.pref.setBooleanValue(Constant.isLogin, false);
                        this.pref.setBooleanValue(Constant.isGuestLogin, false);
                        startActivity(new Intent(getApplicationContext(), loginActivity.class));
                        finish();
                    }

                } catch (Exception e) {
                    Log.d("test", "Error in login JsonObject");
                }
                return;
            case R.id.menu_Asset /*2131558606*/:
//                if (Utility.isSubscriptionValid(this.pref)) {
                startActivity(new Intent(getApplicationContext(), MenuListAssetActivity.class));
                return;
//                }
//                showInvalidSubscriptionAlert();
//                return;
            case R.id.menu_profile /*2131558986*/:
                startActivity(new Intent(getApplicationContext(), MenuListProfileActivity.class));
                return;
            case R.id.account_menu /*2131558987*/:
//                if (Utility.isSubscriptionValid(this.pref)) {
                startActivity(new Intent(getApplicationContext(), MenuListAccountActivity.class));
                return;
//                }
//                showInvalidSubscriptionAlert();
//                return;
            case R.id.menu_documents /*2131558988*/:
//                if (Utility.isSubscriptionValid(this.pref)) {
                startActivity(new Intent(getApplicationContext(), DocumentsMenu.class));
                return;
//                }
//                showInvalidSubscriptionAlert();
//                return;
            case R.id.menu_emergency /*2131558990*/:
//                if (Utility.isSubscriptionValid(this.pref)) {
                startActivity(new Intent(getApplicationContext(), EmergencyMenuActivity.class));
                return;
//                }
//                showInvalidSubscriptionAlert();
//                return;
            case R.id.menu_employer /*2131558992*/:
//                if (Utility.isSubscriptionValid(this.pref)) {
                startActivity(new Intent(getApplicationContext(), EmployerMenuActivity.class));
                return;
//                }
//                showInvalidSubscriptionAlert();
//                return;
            case R.id.menu_insurance /*2131558994*/:
//                if (Utility.isSubscriptionValid(this.pref)) {
                startActivity(new Intent(getApplicationContext(), InsuranceMenuActivity.class));
                return;
//                }
//                showInvalidSubscriptionAlert();
//                return;
            case R.id.menu_internet /*2131558996*/:
                startActivity(new Intent(getApplicationContext(), InternetMenuActivity.class));
                return;
            case R.id.medical_menu /*2131558998*/:
//                if (Utility.isSubscriptionValid(this.pref)) {
                startActivity(new Intent(getApplicationContext(), MedicalMenuListActivity.class));
                return;
//                }
//                showInvalidSubscriptionAlert();
//                return;
            case R.id.menu_Planning /*2131559000*/:
//                if (Utility.isSubscriptionValid(this.pref)) {
                startActivity(new Intent(getApplicationContext(), MenuListPlanningActivity.class));
                return;
//                }
//                showInvalidSubscriptionAlert();
//                return;
            case R.id.menu_password /*2131559002*/:
                if (!this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                    startActivity(new Intent(getApplicationContext(), MenuPasswordActivity.class));
                    return;
                }
                return;
            /* case R.id.menu_add_coupon *//*2131559005*//*:
//                if (!this.pref.getBooleanValue(Constant.isGuestLogin, false) || this.pref.getStringValue(Constant.subscription, "").contains("sub")) {
                startActivity(new Intent(getApplicationContext(), MenuAddCouponActivity.class));
                return;
//                }
//                return;*/
            case R.id.menu_resource /*2131559008*/:
//                if (Utility.isSubscriptionValid(this.pref)) {
                startActivity(new Intent(getApplicationContext(), MenuResourceActivity.class));
                return;
//                }
//                showInvalidSubscriptionAlert();
//                return;
            case R.id.menu_notificaion /*2131559010*/:
//                if (Utility.isSubscriptionValid(this.pref)) {
                startActivity(new Intent(getApplicationContext(), MenuNotification.class));
                return;
//                }
//                showInvalidSubscriptionAlert();
//                return;
            /*case R.id.menu_upgrade *//*2131559012*//*:
//                if (!this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                startActivity(new Intent(getApplicationContext(), MenuUpgrade.class));
                return;
//                }
//                return;*/
            case R.id.menu_guest_user /*2131559014*/:
                if (!this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                    startActivity(new Intent(getApplicationContext(), MenuGuestUserActivity.class));
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void showInvalidSubscriptionAlert() {
        new AlertDialog.Builder(this).setTitle("Alert").setMessage("Please purchase our paid version in order to access paid features.\nCost $4.99 \nDo you want to buy?").setPositiveButton("17039379", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setNegativeButton("17039369", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();
    }

  /*  @Override
    protected void onPause() {
        super.onPause();

//        timer = new Timer();
//        Log.i("Main", "Invoking logout timer");
//        LogOutTimerTask logoutTimeTask = new LogOutTimerTask();
//        timer.schedule(logoutTimeTask, 30000); //auto logout in 5 minutes
    }*/

    @Override
    public void doLogout() {

        if (foreGround) {

            pref.setBooleanValue(Constant.isLogin, false);
            pref.setBooleanValue(Constant.isGuestLogin, false);

            Intent intent = new Intent(this, loginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            this.finish();
        } else {
            logout = "true";
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogOutTimerUtil.startLogoutTimer(this, this);
        Log.e("TAG", "OnStart () &&& Starting timer");

        if (logout.equals("true")) {

            logout = "false";

//redirect user to login screen

            pref.setBooleanValue(Constant.isLogin, false);
            pref.setBooleanValue(Constant.isGuestLogin, false);

            Intent intent = new Intent(this, loginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            this.finish();
        }
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        LogOutTimerUtil.startLogoutTimer(this, this);
        Log.e("TAG", "User interacting with screen");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("TAG", "onPause()");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.e("TAG", "onResume()");

        check_notification_status();

        if (logout.equals("true")) {

            logout = "false";

//redirect user to login screen
            pref.setBooleanValue(Constant.isLogin, false);
            pref.setBooleanValue(Constant.isGuestLogin, false);

            Intent intent = new Intent(this, loginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            this.finish();
        }
    }

    private class LogOutTimerTask extends TimerTask {

        @Override
        public void run() {

            //redirect user to login screen
            pref.setBooleanValue(Constant.isLogin, false);
            pref.setBooleanValue(Constant.isGuestLogin, false);
            startActivity(new Intent(getApplicationContext(), loginActivity.class));
            finish();
        }
    }
}