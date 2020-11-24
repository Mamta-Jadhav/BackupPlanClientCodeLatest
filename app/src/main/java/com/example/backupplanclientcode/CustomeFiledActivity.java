package com.example.backupplanclientcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import com.example.backupplanclientcode.Bugsense.Bugsense;
import com.example.backupplanclientcode.Constant.Constant;
import com.example.backupplanclientcode.Preference.SettingPreference;
import java.util.ArrayList;

import static com.example.backupplanclientcode.LogOutTimerUtil.foreGround;
import static com.example.backupplanclientcode.LogOutTimerUtil.logout;

public class CustomeFiledActivity extends Activity implements OnClickListener, OnCheckedChangeListener, LogOutTimerUtil.LogOutListener {
    LinearLayout Siblings_layout;
    Button btn_add;
    Button btn_cancel;
    LinearLayout childredn_layout;
    ArrayList<String> customFieldList;
    CheckBox edit_Children;
    CheckBox edit_brother_siblings;
    CheckBox edit_sister_siblings;
    int flag = 3;
    SettingPreference pref;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_field_layout);
        this.pref = new SettingPreference(getApplicationContext());
        new Bugsense().startBugsense(getApplicationContext());
        findViewId();
    }

    private void findViewId() {
        this.customFieldList = new ArrayList<>();
        this.customFieldList.clear();
        this.Siblings_layout = findViewById(R.id.Siblings_layout);
        this.childredn_layout = findViewById(R.id.childredn_layout);
        if (getIntent().getStringExtra("visible_part").equalsIgnoreCase("siblings")) {
            this.Siblings_layout.setVisibility(View.VISIBLE);
        } else if (getIntent().getStringExtra("visible_part").equalsIgnoreCase("children")) {
            this.childredn_layout.setVisibility(View.VISIBLE);
        }
        this.btn_add = findViewById(R.id.btn_add);
        this.btn_cancel = findViewById(R.id.btn_cancel);
        this.btn_cancel.setOnClickListener(this);
        this.btn_add.setOnClickListener(this);
        this.edit_brother_siblings = findViewById(R.id.edit_brother_siblings);
        this.edit_sister_siblings = findViewById(R.id.edit_sister_siblings);
        this.edit_Children = findViewById(R.id.edit_Children);
        this.edit_sister_siblings.setOnCheckedChangeListener(this);
        this.edit_brother_siblings.setOnCheckedChangeListener(this);
        this.edit_Children.setOnCheckedChangeListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add /*2131558586*/:
                if (this.edit_brother_siblings.isChecked() && this.edit_sister_siblings.isChecked()) {
                    this.flag = 3;
                } else if (this.edit_brother_siblings.isChecked()) {
                    this.flag = 1;
                } else if (this.edit_sister_siblings.isChecked()) {
                    this.flag = 2;
                }
                Intent resultData = getIntent();
                resultData.putExtra("visible_part", getIntent().getStringExtra("visible_part"));
                resultData.putStringArrayListExtra("customFieldList", this.customFieldList);
                resultData.putExtra("sibling_flag", this.flag);
                setResult(-1, resultData);
                finish();
                return;
            case R.id.btn_cancel /*2131558588*/:
                setResult(0, null);
                finish();
                return;
            default:
                return;
        }
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.edit_Children /*2131558647*/:
                if (isChecked) {
                    buttonView.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_check), null);
                    this.customFieldList.add(getResources().getString(R.string.hint_Children));
                    return;
                }
                this.customFieldList.remove(getResources().getString(R.string.hint_Children));
                buttonView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                return;
            case R.id.edit_brother_siblings /*2131558649*/:
                if (isChecked) {
                    buttonView.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_check), null);
                    this.customFieldList.add(getResources().getString(R.string.hint_brother));
                    return;
                }
                this.customFieldList.remove(getResources().getString(R.string.hint_brother));
                buttonView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                return;
            case R.id.edit_sister_siblings /*2131558650*/:
                if (isChecked) {
                    this.customFieldList.add(getResources().getString(R.string.hint_sister));
                    buttonView.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_check), null);
                    return;
                }
                this.customFieldList.remove(getResources().getString(R.string.hint_sister));
                buttonView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                return;
            default:
                return;
        }
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
}
