package com.example.backupplanclientcode.Menu;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import com.example.backupplanclientcode.LogOutTimerUtil;
import com.example.backupplanclientcode.Preference.SettingPreference;
import com.example.backupplanclientcode.R;
import com.example.backupplanclientcode.ServiceUrl.ServiceUrl;
import com.example.backupplanclientcode.loginActivity;

import org.json.JSONObject;

import static com.example.backupplanclientcode.LogOutTimerUtil.foreGround;
import static com.example.backupplanclientcode.LogOutTimerUtil.logout;

public class MenuPasswordActivity extends Activity implements LogOutTimerUtil.LogOutListener, OnClickListener, GeneralTask.ResponseListener_General {
    TextView actionBarTittle;
    Button btn_back;
    Button btn_save;
    Button btn_changePassword;
    TextView tv_firstPassword;
    TextView tv_lastpassword;
    EditText editConfirmPassword;
    EditText editPassword;
    SettingPreference pref;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_password);
        this.pref = new SettingPreference(getApplicationContext());
        new Bugsense().startBugsense(getApplicationContext());
        findViewId();
    }

    private void findViewId() {
        this.btn_changePassword = (Button) findViewById(R.id.btn_changePassword);
        this.btn_changePassword.setOnClickListener(this);
        this.editPassword = (EditText) findViewById(R.id.editPassword);
        this.editConfirmPassword = (EditText) findViewById(R.id.editConfirmPassword);
        this.btn_save = (Button) findViewById(R.id.btn_save);
        this.btn_save.setVisibility(View.GONE);
        this.btn_back = (Button) findViewById(R.id.btn_back);
        this.btn_back.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MenuPasswordActivity.this.finish();
            }
        });
        this.actionBarTittle = (TextView) findViewById(R.id.actionBarTittle);
        this.actionBarTittle.setText(getResources().getString(R.string.menu_password));
        this.tv_firstPassword = (TextView) findViewById(R.id.tv_firstPassword);
        this.tv_firstPassword.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent("android.intent.action.VIEW");
                i.setData(Uri.parse("https://agilebits.com/onepassword"));
                MenuPasswordActivity.this.startActivity(i);
            }
        });
        this.tv_lastpassword = (TextView) findViewById(R.id.tv_lastpassword);
        this.tv_lastpassword.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent("android.intent.action.VIEW");
                i.setData(Uri.parse("https://lastpass.com/"));
                MenuPasswordActivity.this.startActivity(i);
            }
        });
    }

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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_changePassword /*2131558953*/:
                changePassword();
                return;
            default:
                return;
        }
    }

    private void changePassword() {
        if (this.editPassword.getText().toString().trim().isEmpty()) {
            this.editPassword.setError("field is Required");
        } else if (this.editConfirmPassword.getText().toString().trim().isEmpty()) {
            this.editConfirmPassword.setError("field is Required");
        } else if (this.editPassword.getText().toString().trim().equals(this.editConfirmPassword.getText().toString().trim())) {
            try {
                JSONObject nameValuePair = new JSONObject();
                nameValuePair.put("user_id", this.pref.getStringValue(Constant.user_id, ""));
                nameValuePair.put("new_password", this.editPassword.getText().toString().trim());

                nameValuePair.put("token", this.pref.getStringValue(Constant.jwttoken, ""));
                new GeneralTask(this, ServiceUrl.change_password, nameValuePair, 1, "post").execute(new Void[0]);
            } catch (Exception e) {
            }
        } else {
            Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_LONG).show();
        }
    }

    public void on_GeneralSuccess(JSONObject response, int responseCode) {
        try {
            if (responseCode == 1) {
                try {
                    Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
//                    Toast.makeText(getApplicationContext(), "Please login with your updated credentials.", Toast.LENGTH_LONG).show();
                    this.editPassword.setText("");
                    this.editConfirmPassword.setText("");
//                    pref.setBooleanValue(Constant.isLogin, false);
//                    pref.setBooleanValue(Constant.isGuestLogin, false);
//                    startActivity(new Intent(getApplicationContext(), loginActivity.class));
//                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {

        }
    }
}
