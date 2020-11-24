package com.example.backupplanclientcode;

import android.content.Intent;
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
import com.example.backupplanclientcode.Database.DBHelper;
import com.example.backupplanclientcode.Preference.SettingPreference;
import com.example.backupplanclientcode.ServiceUrl.ServiceUrl;
import com.example.backupplanclientcode.Utility.PendingTask;
import com.google.firebase.auth.EmailAuthProvider;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.backupplanclientcode.LogOutTimerUtil.foreGround;
import static com.example.backupplanclientcode.LogOutTimerUtil.logout;

public class GuestuserLogin extends FragmentActivity implements OnClickListener, ResponseListener_General, LogOutTimerUtil.LogOutListener {
    Button btn_login;
    ConnectionDetector connection;
    DBHelper dbHelper;
    EditText edit_password;
    EditText edit_username;
    FragmentManager mFragmentManager = getSupportFragmentManager();
    /* access modifiers changed from: private */
    public JSONObject mNameValuePairs;
    SettingPreference pref;
    TextView skip;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest_user_login);
        this.dbHelper = new DBHelper(getApplicationContext());
        this.pref = new SettingPreference(getApplicationContext());
        new Bugsense().startBugsense(getApplicationContext());
        this.connection = new ConnectionDetector(getApplicationContext());
        findViewId();
    }

    private void findViewId() {
        this.edit_username = (EditText) findViewById(R.id.edit_username);
        this.edit_password = (EditText) findViewById(R.id.editPassword);
        this.btn_login = (Button) findViewById(R.id.btn_login);
        this.skip = (TextView) findViewById(R.id.skip);
        this.skip.setOnClickListener(this);
        this.btn_login.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.skip /*2131558660*/:
                finish();
                return;
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
                        nameValuePairs.put("username", this.edit_username.getText().toString().trim());
                        nameValuePairs.put("password", this.edit_password.getText().toString().trim());
                        if (this.pref.getBooleanValue(Constant.isNotification, false)) {
                            nameValuePairs.put("notification_flag", "1");
                        } else {
                            nameValuePairs.put("notification_flag", "0");
                        }
                        nameValuePairs.put("token", YourBackupPlanApp.GetDeviceID(getApplicationContext()));
                        nameValuePairs.put("register_id", "2");//this.pref.getStringValue("gcm_registration_id", ""));
                        nameValuePairs.put("device_type", "1");
                        nameValuePairs.put("device", "android");
                        nameValuePairs.put("is_guest", "1");
                        this.mNameValuePairs = null;
                        this.mNameValuePairs = nameValuePairs;
                        Log.e("token", YourBackupPlanApp.GetDeviceID(getApplicationContext()).toString());
                        Log.e("register_id ", this.pref.getStringValue("gcm_registration_id", ""));
                        new GeneralTask(this, ServiceUrl.login, nameValuePairs, 1, "post").execute(new Void[0]);
                    } catch (Exception e) {
                    }
                    return;
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.connectionFailMessage), Toast.LENGTH_SHORT).show();
                    return;
                }
            default:
                return;
        }
    }

    private void displayToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    public void on_GeneralSuccess(JSONObject response, int responseCode) {
        try {
            Log.d("~~~~~~~~~~~~>", "" + responseCode + " | " + response.toString());
            if (responseCode == 1) {
                if (response.getString("success").equalsIgnoreCase("1")) {
                    displayToast(response.getString("message"));
//                    openLoginVerification();
                    saveUserInfo(response);
                    return;
                }
                displayToast(response.getString("message"));
            } else if (responseCode != 2) {
            } else {
                if (response.has("message")) {
                    displayToast(response.getString("message"));
                    saveUserInfo(response);
                    return;
                }
                displayToast(response.getString("error"));
                openLoginVerification();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openLoginVerification() {
        LoginVerificationDialog.newInstance(new PendingTask() {
            public void onCompleteTask(Object object) {
                String verificationCode = (String) object;
                try {
                    JSONObject nameValuePairs = GuestuserLogin.this.mNameValuePairs;
                    nameValuePairs.put("login_code", verificationCode);
                    new GeneralTask(GuestuserLogin.this, ServiceUrl.login_verification, nameValuePairs, 2, "post").execute(new Void[0]);
                } catch (Exception e) {
                }
            }
        }).show(this.mFragmentManager, "Login Verification");
    }

    private void saveUserInfo(JSONObject response) throws JSONException {
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
        this.pref.setStringValue(Constant.investmentFlag, login_detail.getString("investment_flag"));
        this.pref.setStringValue(Constant.MortgageLoansFlag, login_detail.getString("loan_flag"));
        this.pref.setStringValue(Constant.medical_id, login_detail.getString("medical_id"));
        this.pref.setStringValue(Constant.internet_id, login_detail.getString("internet_id"));
        this.pref.setStringValue(Constant.RetirementFlag, login_detail.getString("retirement_flag"));
        this.pref.setStringValue(Constant.employer_id, login_detail.getString("employer_id"));
        this.pref.setStringValue(Constant.insurance_id, login_detail.getString("insurance_id"));
        this.pref.setStringValue(Constant.billToPay, login_detail.getString("bills_to_pay_flag"));
        this.pref.setStringValue(Constant.emergencyFlag, login_detail.getString("emergency_flag"));
        this.pref.setBooleanValue(Constant.isLogin, true);
        this.pref.setBooleanValue(Constant.showAlertFirstTime, true);
        displayToast(response.getString("message"));
        this.pref.setBooleanValue(Constant.isGuestLogin, true);
        startActivity(new Intent(getApplicationContext(), MenuListActivity.class));
        finish();
    }

    @Override
    public void doLogout() {

        if(foreGround){

            pref.setBooleanValue(Constant.isLogin, false);
            pref.setBooleanValue(Constant.isGuestLogin, false);
            startActivity(new Intent(getApplicationContext(), loginActivity.class));
            finish();

        }else {
            logout = "true";
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        LogOutTimerUtil.startLogoutTimer(this, this);
        Log.e("TAG", "OnStart () &&& Starting timer");

        if(logout.equals("true")){

            logout = "false";

            //redirect user to login screen

            pref.setBooleanValue(Constant.isLogin, false);
            pref.setBooleanValue(Constant.isGuestLogin, false);
            startActivity(new Intent(getApplicationContext(), loginActivity.class));
            finish();
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

        if(logout.equals("true")){

            logout = "false";

            //redirect user to login screen
            pref.setBooleanValue(Constant.isLogin, false);
            pref.setBooleanValue(Constant.isGuestLogin, false);
            startActivity(new Intent(getApplicationContext(), loginActivity.class));
            finish();
        }
    }
}