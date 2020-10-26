package com.example.backupplanclientcode.Menu;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.backupplanclientcode.Asyntask.GeneralTask;
import com.example.backupplanclientcode.Asyntask.GeneralTask.ResponseListener_General;
import com.example.backupplanclientcode.Bugsense.Bugsense;
import com.example.backupplanclientcode.Constant.Constant;
import com.example.backupplanclientcode.Database.DBHelper;
import com.example.backupplanclientcode.Preference.SettingPreference;
import com.example.backupplanclientcode.R;
import com.example.backupplanclientcode.ServiceUrl.ServiceUrl;
import com.example.backupplanclientcode.Utility.Validation;
import com.google.firebase.auth.EmailAuthProvider;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class MenuAddNewUser extends Activity implements OnClickListener, ResponseListener_General {
    private static final int KEY_ADD_GUEST = 300;
    private static final int KEY_EDIT_GUEST = 400;
    TextView actionBarTittle;
    Button btn_back;
    Button btn_save;
    DBHelper dbHelper;
    EditText edit_Password;
    EditText edit_Username;
    EditText edit_email;
    EditText edit_id;
    private String guestUserId;
    SettingPreference pref;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_guest_user);
        this.dbHelper = new DBHelper(getApplicationContext());
        this.pref = new SettingPreference(getApplicationContext());
        new Bugsense().startBugsense(getApplicationContext());
        findViewId();
    }

    private void findViewId() {
        this.btn_save = (Button) findViewById(R.id.btn_save);
        this.btn_back = (Button) findViewById(R.id.btn_back);
        this.btn_back.setOnClickListener(this);
        this.btn_save.setOnClickListener(this);
        this.actionBarTittle = (TextView) findViewById(R.id.actionBarTittle);
        this.actionBarTittle.setText(getResources().getString(R.string.txt_add_guest_user));
        this.edit_id = (EditText) findViewById(R.id.edit_id);
        this.edit_Username = (EditText) findViewById(R.id.edit_Username);
        this.edit_Password = (EditText) findViewById(R.id.edit_Password);
        this.edit_email = (EditText) findViewById(R.id.edit_emaild);
        if (getIntent().hasExtra("id")) {
            getEdituserDetail();
            this.actionBarTittle.setText(getResources().getString(R.string.txt_edit_user));
            return;
        }
        this.actionBarTittle.setText(getResources().getString(R.string.txt_add_guest_user));
    }

    private void getEdituserDetail() {
        this.guestUserId = getIntent().getStringExtra("id").toString();
        this.edit_Username.setText(getIntent().getStringExtra("username").toString());
        this.edit_Password.setText(getIntent().getStringExtra("password").toString());
        this.edit_email.setText(getIntent().getStringExtra("email").toString());
        this.btn_save.setText("Edit");
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save /*2131558589*/:
                if (this.btn_save.getText().toString().equalsIgnoreCase("edit")) {
                    editUserDetail();
                    return;
                } else {
                    saveGuestUser();
                    return;
                }
            case R.id.btn_back /*2131558590*/:
                finish();
                return;
            default:
                return;
        }
    }

    private void editUserDetail() {
        if (this.edit_Username.getText().toString().trim().isEmpty()) {
            this.edit_Username.setError("Field is required ");
        } else if (this.edit_Password.getText().toString().trim().isEmpty()) {
            this.edit_Password.setError("Field is required ");
        } else if (!new Validation().IsValidEmail(this.edit_email.getText().toString().trim())) {
            this.edit_email.setError("Please enter valid email address");
        } else {
            String userId = this.pref.getStringValue(Constant.user_id, "");
            try {
                JSONObject nameValuePairs = new JSONObject();
                nameValuePairs.put("user_id", userId);
                nameValuePairs.put("guest_id", this.guestUserId);
                nameValuePairs.put("username", this.edit_Username.getText().toString());
                nameValuePairs.put("email", this.edit_email.getText().toString());
                nameValuePairs.put("password", this.edit_Password.getText().toString());
                nameValuePairs.put("token", this.pref.getStringValue(Constant.jwttoken, ""));
                new GeneralTask(this, ServiceUrl.edit_guest_user, nameValuePairs, 400, "post").execute(new Void[0]);
            } catch (Exception e) {
            }
        }
    }

    private void saveGuestUser() {
        if (this.edit_Username.getText().toString().trim().isEmpty()) {
            this.edit_Username.setError("Field is required ");
        } else if (this.edit_Password.getText().toString().trim().isEmpty()) {
            this.edit_Password.setError("Field is required ");
        } else if (!new Validation().IsValidEmail(this.edit_email.getText().toString().trim())) {
            this.edit_email.setError("Please enter valid email address");
        } else {
            String userId = this.pref.getStringValue(Constant.user_id, "");
            try {
                JSONObject nameValuePairs = new JSONObject();
                nameValuePairs.put("user_id", userId);
                nameValuePairs.put("username", this.edit_Username.getText().toString());
                nameValuePairs.put("email", this.edit_email.getText().toString());
                nameValuePairs.put("password", this.edit_Password.getText().toString());
                nameValuePairs.put("token", this.pref.getStringValue(Constant.jwttoken, ""));
                new GeneralTask(this, ServiceUrl.add_guest_user, nameValuePairs, 300, "post").execute(new Void[0]);
            }catch (Exception e){}
        }
    }

    private void showUserDeleteDialog(String msg) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.delete_user_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setWindowAnimations(R.style.dialog_animation_fade);
        ((TextView) dialog.findViewById(R.id.tv_text)).setText(msg);
        ((Button) dialog.findViewById(R.id.btn_ok)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                MenuAddNewUser.this.edit_Username.setText("");
                MenuAddNewUser.this.edit_Password.setText("");
                MenuAddNewUser.this.finish();
            }
        });
        ((Button) dialog.findViewById(R.id.btn_cancel)).setVisibility(View.GONE);
        dialog.show();
    }

    private void displayToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    public void on_GeneralSuccess(JSONObject response, int responseCode) {
        try {
            if (responseCode == 300) {
                try {
                    if (response.getInt("success") == 1) {
                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                    Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                }
            } else if (responseCode != 400) {
            } else {
                if (response.getInt("success") == 1) {
                    Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
        }
    }
}