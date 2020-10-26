package com.example.backupplanclientcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import com.example.backupplanclientcode.Bugsense.Bugsense;
import com.example.backupplanclientcode.Preference.SettingPreference;
import java.util.ArrayList;

public class CustomeFiledActivity extends Activity implements OnClickListener, OnCheckedChangeListener {
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
        this.Siblings_layout = (LinearLayout) findViewById(R.id.Siblings_layout);
        this.childredn_layout = (LinearLayout) findViewById(R.id.childredn_layout);
        if (getIntent().getStringExtra("visible_part").equalsIgnoreCase("siblings")) {
            this.Siblings_layout.setVisibility(View.VISIBLE);
        } else if (getIntent().getStringExtra("visible_part").equalsIgnoreCase("children")) {
            this.childredn_layout.setVisibility(View.VISIBLE);
        }
        this.btn_add = (Button) findViewById(R.id.btn_add);
        this.btn_cancel = (Button) findViewById(R.id.btn_cancel);
        this.btn_cancel.setOnClickListener(this);
        this.btn_add.setOnClickListener(this);
        this.edit_brother_siblings = (CheckBox) findViewById(R.id.edit_brother_siblings);
        this.edit_sister_siblings = (CheckBox) findViewById(R.id.edit_sister_siblings);
        this.edit_Children = (CheckBox) findViewById(R.id.edit_Children);
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
}
