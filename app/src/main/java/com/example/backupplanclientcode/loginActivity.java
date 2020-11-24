package com.example.backupplanclientcode;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.backupplanclientcode.Asyntask.GeneralTask;
import com.example.backupplanclientcode.Asyntask.GeneralTask.ResponseListener_General;
import com.example.backupplanclientcode.Bugsense.Bugsense;
import com.example.backupplanclientcode.Constant.Constant;
import com.example.backupplanclientcode.Preference.SettingPreference;
import com.example.backupplanclientcode.ServiceUrl.ServiceUrl;
import com.example.backupplanclientcode.Utility.PendingTask;
import com.google.api.client.json.Json;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class loginActivity extends FragmentActivity implements OnClickListener, ResponseListener_General {
    Button btn_login;
    ConnectionDetector connection;
    EditText edit_password;
    EditText edit_username;
    FragmentManager mFragmentManager = getSupportFragmentManager();
    /* access modifiers changed from: private */
    public JSONObject mNameValuePairs;
    SettingPreference pref;
    TextView tv_forgotPassword;
    TextView tv_guest_user;
    TextView tv_register;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        this.pref = new SettingPreference(getApplicationContext());
        new Bugsense().startBugsense(getApplicationContext());
        this.connection = new ConnectionDetector(getApplicationContext());
        check_already_login();
        check_notificaion();
    }

    private void check_notificaion() {
        if (getIntent().hasExtra("message")) {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(1);
            dialog.setContentView(R.layout.notification_dialog);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().setWindowAnimations(R.style.dialog_animation_fade);
            ((TextView) dialog.findViewById(R.id.tv_message)).setText(getIntent().getStringExtra("message").toString().trim());
            ((Button) dialog.findViewById(R.id.btn_ok)).setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    private void check_already_login() {
//        if (this.pref.getBooleanValue(Constant.isLogin, false) || this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
//            Log.d("~~~~~~~~>", "" + this.pref.getStringValue("user_id", ""));
//            startActivity(new Intent(getApplicationContext(), MenuListActivity.class));
//            finish();
//            return;
//        }
        settingControlId();
    }

    private void settingControlId() {
        this.edit_username = (EditText) findViewById(R.id.edit_username);
        this.edit_password = (EditText) findViewById(R.id.edit_password);
        this.tv_register = (TextView) findViewById(R.id.tv_register);
        this.tv_forgotPassword = (TextView) findViewById(R.id.tv_forgotPassword);
        this.btn_login = (Button) findViewById(R.id.btn_login);
        this.tv_guest_user = (TextView) findViewById(R.id.tv_guest_user);
        this.tv_guest_user.setOnClickListener(this);
        this.tv_register.setOnClickListener(this);
        this.tv_forgotPassword.setOnClickListener(this);
        this.btn_login.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login /*2131558663*/:
                if (this.edit_username.getText().toString().trim().isEmpty()) {
                    this.edit_username.setError("Field is required");
                    return;
                } else if (this.edit_password.getText().toString().trim().isEmpty()) {
                    this.edit_password.setError("Field is required");
                    return;
                } else if (this.connection.isConnectingToInternet()) {
                    try {
                        JSONObject nameValuePairs = new JSONObject();
//                    nameValuePairs.add(new BasicNameValuePair("username", this.edit_username.getText().toString().trim()));
//                    nameValuePairs.add(new BasicNameValuePair("password", this.edit_password.getText().toString().trim()));
//                    if (this.pref.getBooleanValue(Constant.isNotification, false)) {
//                        nameValuePairs.add(new BasicNameValuePair("notification_flag", "1"));
//                    } else {
//                        nameValuePairs.add(new BasicNameValuePair("notification_flag", "0"));
//                    }
//                    nameValuePairs.add(new BasicNameValuePair("token", YourBackupPlanApp.GetDeviceID(getApplicationContext())));
//                    nameValuePairs.add(new BasicNameValuePair("register_id","2"));// this.pref.getStringValue("gcm_registration_id", "")));
//                    nameValuePairs.add(new BasicNameValuePair("device_type", "1"));
//                    nameValuePairs.add(new BasicNameValuePair("is_guest", "0"));
//                    nameValuePairs.add(new BasicNameValuePair("device", "android"));
                        nameValuePairs.put("username", this.edit_username.getText().toString().trim());
                        nameValuePairs.put("password", this.edit_password.getText().toString().trim());
                        if (this.pref.getBooleanValue(Constant.isNotification, false)) {
                            nameValuePairs.put("notification_flag", "1");
                        } else {
                            nameValuePairs.put("notification_flag", "0");
                        }
                        nameValuePairs.put("token", YourBackupPlanApp.GetDeviceID(getApplicationContext()));
                        nameValuePairs.put("register_id", "2");// this.pref.getStringValue("gcm_registration_id", "")));
                        nameValuePairs.put("device_type", "1");
                        nameValuePairs.put("is_guest", "0");
                        nameValuePairs.put("device", "android");
                        this.mNameValuePairs = null;
                        this.mNameValuePairs = nameValuePairs;
                        Log.e("token", YourBackupPlanApp.GetDeviceID(getApplicationContext()).toString());
                        Log.e("register_id ", this.pref.getStringValue("gcm_registration_id", ""));
                        new GeneralTask(this, ServiceUrl.login, nameValuePairs, 1, "post").execute(new Void[0]);
                    } catch (Exception e) {
                        Log.d("test", "Error in login JsonObject");
                    }
                    return;
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.connectionFailMessage), Toast.LENGTH_SHORT).show();
                    return;
                }
            case R.id.tv_register /*2131558869*/:
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                return;
            case R.id.tv_forgotPassword /*2131558870*/:
                startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
                return;
            case R.id.tv_guest_user /*2131558871*/:
                startActivity(new Intent(getApplicationContext(), GuestuserLogin.class));
                finish();
                return;
            default:
                return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.pref.setStringValue(Constant.jwttoken, "");
        this.pref.setStringValue(Constant.guestCount, "0");
    }

    private void displayToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void on_GeneralSuccess(JSONObject response, int responseCode) {
        try {
            Log.d("~~~~~~~~~~~~>", "" + responseCode + " | " + response.toString());
            if (responseCode == 1) {
                if (response.getString("success").equalsIgnoreCase("1")) {
//                    displayToast(response.getString("message"));
//                    openLoginVerification();
                    saveUserInfo(response);
                    return;
                }
                displayToast(response.getString("message"));
            } else if (responseCode != 2) {
            } else {
                if (response.has("message")) {
//                    displayToast(response.getString("message"));
                    saveUserInfo(response);
                    return;
                }
                displayToast(response.getString("error"));
//                openLoginVerification();
            }
        } catch (Exception e) {
            e.printStackTrace();
            displayToast("" + e.getLocalizedMessage().toString());
        }
    }

    private void openLoginVerification() {
        LoginVerificationDialog.newInstance(new PendingTask() {
            public void onCompleteTask(Object object) {
                String verificationCode = (String) object;
                try {
                    JSONObject nameValuePairs = loginActivity.this.mNameValuePairs;
                    nameValuePairs.put("login_code", verificationCode);
                    nameValuePairs.put("device", "android");
                    new GeneralTask(loginActivity.this, ServiceUrl.login_verification, nameValuePairs, 2, "post").execute(new Void[0]);
                } catch (Exception e) {
                    Log.d("test", "Error in login_verificaton JsonObject");
                }
            }
        }).show(this.mFragmentManager, "Login Verification");
    }

    private void saveUserInfo(JSONObject response) throws JSONException {

        this.pref.setStringValue(Constant.jwttoken, response.getString("jwttoken").trim());

        Log.e("token..........", response.getString("jwttoken").trim());

        JSONObject login_detail = response.getJSONObject("login_detail");
        this.pref.setStringValue(Constant.user_name, login_detail.getString("username").trim());
        this.pref.setStringValue(Constant.user_email, login_detail.getString("email").trim());
        this.pref.setStringValue(Constant.user_id, login_detail.getString("user_id").trim());
        this.pref.setStringValue(Constant.WillsAndWishesFalg, login_detail.getString("wills_id"));
        this.pref.setStringValue(Constant.profile_id, login_detail.getString("profile_id"));
        this.pref.setStringValue(Constant.long_term_id, login_detail.getString("long_term_id"));
        this.pref.setStringValue(Constant.document_id, login_detail.getString("document_id"));
        this.pref.setStringValue(Constant.accountFlag, login_detail.getString("account_flag"));
        this.pref.setStringValue(Constant.assetFlag, login_detail.getString("assets_flag"));
        this.pref.setStringValue(Constant.WillsId, login_detail.getString("wills_id"));
        this.pref.setStringValue(Constant.investmentFlag, login_detail.getString("investment_flag"));
        this.pref.setStringValue(Constant.MortgageLoansFlag, login_detail.getString("loan_flag"));
        this.pref.setStringValue(Constant.medical_id, login_detail.getString("medical_id"));
        this.pref.setStringValue(Constant.internet_id, login_detail.getString("internet_id"));
        this.pref.setStringValue(Constant.RetirementFlag, login_detail.getString("retirement_flag"));
        this.pref.setStringValue(Constant.employer_id, login_detail.getString("employer_id"));
        this.pref.setStringValue(Constant.insurance_id, login_detail.getString("insurance_id"));
        this.pref.setStringValue(Constant.billToPay, login_detail.getString("bills_to_pay_flag"));
        this.pref.setStringValue(Constant.contactFlag, login_detail.getString("contact_flag"));
        this.pref.setStringValue(Constant.emergencyFlag, login_detail.getString("emergency_flag"));
        this.pref.setBooleanValue(Constant.isLogin, true);
        this.pref.setBooleanValue(Constant.isGuestLogin, false);
        this.pref.setBooleanValue(Constant.showAlertFirstTime, true);
        this.pref.setStringValue(Constant.subscription, "free");// login_detail.getString("subscription"));
        try {
            this.pref.setStringValue(Constant.coupon, login_detail.has(Param.COUPON) ? login_detail.getJSONObject(Param.COUPON).toString() : "");
        } catch (Exception e) {
            this.pref.setStringValue(Constant.coupon, null);
        }
        this.pref.setBooleanValue(Constant.subscription_expired, true);//login_detail.getBoolean("subscription_expired"));
//        this.pref.setStringValue(Constant.subscription_end_date, login_detail.getString("subscription_end_date"));
        displayToast(response.getString("message"));
        startActivity(new Intent(getApplicationContext(), MenuListActivity.class));
        finish();
    }
}