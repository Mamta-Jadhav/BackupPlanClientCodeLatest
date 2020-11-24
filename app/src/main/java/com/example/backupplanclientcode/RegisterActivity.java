package com.example.backupplanclientcode;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.backupplanclientcode.Asyntask.GeneralTask;
import com.example.backupplanclientcode.Bugsense.Bugsense;
import com.example.backupplanclientcode.Constant.Constant;
import com.example.backupplanclientcode.Preference.SettingPreference;
import com.example.backupplanclientcode.ServiceUrl.ServiceUrl;
import com.example.backupplanclientcode.Utility.Validation;
import com.google.firebase.auth.EmailAuthProvider;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class RegisterActivity extends Activity implements OnClickListener, GeneralTask.ResponseListener_General {
    Button btn_register;
    EditText edit_emaild;
    EditText edit_password;
    EditText edit_username;
    SettingPreference pref;
    TextView tv_skip;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        this.pref = new SettingPreference(getApplicationContext());
        new Bugsense().startBugsense(getApplicationContext());
        findViewId();
    }

    private void findViewId() {
        this.edit_username = (EditText) findViewById(R.id.edit_username);
        this.edit_emaild = (EditText) findViewById(R.id.edit_emaild);
        this.edit_password = (EditText) findViewById(R.id.edit_password);
        this.btn_register = (Button) findViewById(R.id.btn_register);
        this.tv_skip = (TextView) findViewById(R.id.tv_skip);
        this.btn_register.setOnClickListener(this);
        this.tv_skip.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register /*2131559119*/:
                if (this.edit_username.getText().toString().trim().isEmpty()) {
                    this.edit_username.setError("Field is required");
                    return;
                } else if (!new Validation().IsValidEmail(this.edit_emaild.getText().toString().trim())) {
                    this.edit_emaild.setError("Please enter valid email address");
                    return;
                } else if (this.edit_password.getText().toString().trim().isEmpty()) {
                    this.edit_password.setError("Field is required");
                    return;
                } else {
                    try {
                        JSONObject nameValuePairs = new JSONObject();
                        nameValuePairs.put("username", this.edit_username.getText().toString().trim());
                        nameValuePairs.put("password", this.edit_password.getText().toString().trim());
                        nameValuePairs.put("email", this.edit_emaild.getText().toString().trim());
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
                        new GeneralTask(this, ServiceUrl.register, nameValuePairs, 1, "post").execute(new Void[0]);
                    } catch (Exception e) {
                        Log.d("test", "Error in register JsonObject");
                    }
                    return;
                }
            case R.id.tv_skip /*2131559120*/:
                finish();
                return;
            default:
                return;
        }
    }

    private void displayToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    public void on_GeneralSuccess(JSONObject response, int responseCode) {
        try {
            Log.d("test", response.toString());
            if (response.has("success")) {
                if (response.getString("success").equalsIgnoreCase("1")) {
                    displayToast(response.getString("message"));
                    this.pref.setStringValue(Constant.user_name, response.getString("message"));
                    finish();
                    return;
                }
            } else {
                if (response.has("errors")) {
                    if (response.getJSONObject("errors").has("username")) {
                        displayToast(response.getJSONObject("errors").getJSONArray("username").get(0) + "");
                        return;
                    }
                    if (response.getJSONObject("errors").has("password")) {
                        displayToast(response.getJSONObject("errors").getJSONArray("password").get(0) + "");
                        return;
                    }
                }
            }
            displayToast(response.getString("message"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}