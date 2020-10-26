package com.example.backupplanclientcode.Menu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.example.backupplanclientcode.Asyntask.GeneralTask;
import com.example.backupplanclientcode.Asyntask.GeneralTask.ResponseListener_General;
import com.example.backupplanclientcode.Bugsense.Bugsense;
import com.example.backupplanclientcode.ConnectionDetector;
import com.example.backupplanclientcode.Constant.Constant;
import com.example.backupplanclientcode.Database.DBHelper;
import com.example.backupplanclientcode.Preference.SettingPreference;
import com.example.backupplanclientcode.R;
import com.example.backupplanclientcode.ServiceUrl.ServiceUrl;
import com.google.firebase.auth.EmailAuthProvider;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuGuestUserActivity extends Activity implements OnClickListener, ResponseListener_General {
    private static final int KEY_DELETE_GUEST = 200;
    private static final int KEY_GET_GUESTS = 100;
    TextView actionBarTittle;
    ImageView addIcon;
    Button btn_back;
    Button btn_changePassword;
    Button btn_save;
    private ConnectionDetector connection;
    DBHelper dbHelper;
    EditText editConfirmPassword;
    EditText editPassword;
    LinearLayout guestuserListView;
    SettingPreference pref;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_guest_user);
        this.pref = new SettingPreference(getApplicationContext());
        this.dbHelper = new DBHelper(getApplicationContext());
        new Bugsense().startBugsense(getApplicationContext());
        this.connection = new ConnectionDetector(getApplicationContext());
        findViewId();
    }

    private void findViewId() {
        this.addIcon = (ImageView) findViewById(R.id.addIcon);
        this.btn_save = (Button) findViewById(R.id.btn_save);
        this.btn_back = (Button) findViewById(R.id.btn_back);
        this.btn_changePassword = (Button) findViewById(R.id.btn_changePassword);
        this.btn_changePassword.setOnClickListener(this);
        this.actionBarTittle = (TextView) findViewById(R.id.actionBarTittle);
        this.guestuserListView = (LinearLayout) findViewById(R.id.guestuserListView);
        this.btn_back.setOnClickListener(this);
        this.addIcon.setOnClickListener(this);
        this.actionBarTittle.setText(getResources().getString(R.string.menu_guest_user));
        this.btn_save.setVisibility(View.GONE);
        this.editPassword = (EditText) findViewById(R.id.editPassword);
        this.editConfirmPassword = (EditText) findViewById(R.id.editConfirmPassword);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.connection.isConnectingToInternet()) {
            getGuestUsers();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.connectionFailMessage), Toast.LENGTH_SHORT).show();
        }
    }

    /* access modifiers changed from: private */
    public void showUserDeleteDialog(final String id) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.delete_user_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setWindowAnimations(R.style.dialog_animation_fade);
        ((Button) dialog.findViewById(R.id.btn_ok)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MenuGuestUserActivity.this.deleteGuestUser(id);
                dialog.dismiss();
                MenuGuestUserActivity.this.onResume();
            }
        });
        ((Button) dialog.findViewById(R.id.btn_cancel)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back /*2131558590*/:
                finish();
                return;
            case R.id.btn_changePassword /*2131558953*/:
                changePassword();
                return;
            case R.id.addIcon /*2131558955*/:
                startActivity(new Intent(getApplicationContext(), MenuAddNewUser.class));
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
                    this.editPassword.setText("");
                    this.editConfirmPassword.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (responseCode == 100) {
                if (response.has("user")) {
                    setGuestUser(response.getJSONObject("user").getJSONArray("user"));
                } else {
                    Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                }
            } else if (responseCode == 200) {
                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                onResume();
            }
        } catch (Exception e) {
        }
    }

    private void getGuestUsers() {
        String userId = this.pref.getStringValue(Constant.user_id, "");
        try {
            JSONObject nameValuePairs = new JSONObject();
            nameValuePairs.put("user_id", userId);
            nameValuePairs.put("token", this.pref.getStringValue(Constant.jwttoken, ""));
            new GeneralTask(this, ServiceUrl.get_guest_users, nameValuePairs, 100, "post").execute(new Void[0]);
        } catch (Exception e) {
        }
    }

    private void setGuestUser(JSONArray guestUsers) throws JSONException {
        this.guestuserListView.removeAllViews();
        if (guestUsers.length() == 0) {
            View view = LayoutInflater.from(this).inflate(R.layout.lv_guest_user, null);
            TextView tv_userName = (TextView) view.findViewById(R.id.tv_userName);
            tv_userName.setTextColor(-12303292);
            tv_userName.setText("No user found!");
            LayoutParams lllp = (LayoutParams) tv_userName.getLayoutParams();
            lllp.gravity = 1;
            tv_userName.setLayoutParams(lllp);
            ImageView editUser = (ImageView) view.findViewById(R.id.editUser);
            ((ImageView) view.findViewById(R.id.deleteuser)).setVisibility(View.GONE);
            editUser.setVisibility(View.GONE);
            this.guestuserListView.addView(view);
            return;
        }
        for (int i = 0; i < guestUsers.length(); i++) {
            JSONObject guestJson = guestUsers.getJSONObject(i);
            final String userName = guestJson.getString("username");
            final String guesyUserId = guestJson.getString("user_id");
            final String email = guestJson.getString("email");
            final String password = guestJson.getString("password");
            View view2 = LayoutInflater.from(this).inflate(R.layout.lv_guest_user, null);
            ((TextView) view2.findViewById(R.id.tv_userName)).setText(userName);
            final ImageView deleteuser = (ImageView) view2.findViewById(R.id.deleteuser);
            ImageView editUser2 = (ImageView) view2.findViewById(R.id.editUser);
            deleteuser.setTag(guesyUserId);
            deleteuser.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    MenuGuestUserActivity.this.showUserDeleteDialog(deleteuser.getTag().toString());
                }
            });
            editUser2.setTag(guesyUserId);
            editUser2.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(MenuGuestUserActivity.this.getApplicationContext(), MenuAddNewUser.class);
                    i.putExtra("id", guesyUserId);
                    i.putExtra("username", userName);
                    i.putExtra("email", email);
                    i.putExtra("password", password);
                    MenuGuestUserActivity.this.startActivity(i);
                }
            });
            this.guestuserListView.addView(view2);
        }
    }

    /* access modifiers changed from: private */
    public void deleteGuestUser(String guestUserId) {
        String userId = this.pref.getStringValue(Constant.user_id, "");
        try {
//            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//            nameValuePairs.add(new BasicNameValuePair("user_id", userId));
//            nameValuePairs.add(new BasicNameValuePair("guest_id", guestUserId));
            JSONObject nameValuePairs = new JSONObject();
            nameValuePairs.put("user_id", userId);
            nameValuePairs.put("guest_id", guestUserId);
            nameValuePairs.put("token", this.pref.getStringValue(Constant.jwttoken, ""));
            new GeneralTask(this, ServiceUrl.delete_guest_user, nameValuePairs, 200, "post").execute(new Void[0]);
        } catch (Exception e) {
        }
    }
}