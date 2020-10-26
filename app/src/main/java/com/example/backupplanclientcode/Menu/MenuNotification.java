package com.example.backupplanclientcode.Menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.backupplanclientcode.Asyntask.GeneralTask;
import com.example.backupplanclientcode.Asyntask.GeneralTask.ResponseListener_General;
import com.example.backupplanclientcode.Asyntask.SaveProfileAsytask;
import com.example.backupplanclientcode.Bugsense.Bugsense;
import com.example.backupplanclientcode.Constant.Constant;
import com.example.backupplanclientcode.Preference.SettingPreference;
import com.example.backupplanclientcode.R;
import com.example.backupplanclientcode.ServiceUrl.ServiceUrl;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class MenuNotification extends Activity implements ResponseListener_General, SaveProfileAsytask.ResponseListerProfile {
    TextView actionBarTittle;
    Button btn_back;
    Button btn_save;
    ToggleButton isNotificaion;
    SettingPreference pref;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_notification);
        this.pref = new SettingPreference(getApplicationContext());
        new Bugsense().startBugsense(getApplicationContext());
        findViewId();
    }

    private void findViewId() {
        this.btn_save = (Button) findViewById(R.id.btn_save);
        this.btn_save.setVisibility(View.GONE);
        this.isNotificaion = (ToggleButton) findViewById(R.id.isNotificaion);
        this.isNotificaion.setChecked(this.pref.getBooleanValue(Constant.isNotification, true));
        this.isNotificaion.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("user_id", "23"));
                    if (isChecked) {
                        nameValuePairs.add(new BasicNameValuePair("notification_flag", "1"));
                    } else {
                        nameValuePairs.add(new BasicNameValuePair("notification_flag", "0"));
                    }
                    new SaveProfileAsytask(MenuNotification.this, ServiceUrl.save_notification, nameValuePairs).execute(new Void[0]);

//                    JSONObject nameValuePair = new JSONObject();
//                    nameValuePair.put("user_id", MenuNotification.this.pref.getStringValue(Constant.user_id, ""));
//                    if (isChecked) {
//                        nameValuePair.put("notification_flag", "1");
//                    } else {
//                        nameValuePair.put("notification_flag", "0");
//                    }
//                    new GeneralTask(MenuNotification.this, ServiceUrl.save_notification, nameValuePair, 1, "post").execute(new Void[0]);
                } catch (Exception e) {
                }
            }
        });
        this.btn_back = (Button) findViewById(R.id.btn_back);
        this.btn_back.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MenuNotification.this.finish();
            }
        });
        this.actionBarTittle = (TextView) findViewById(R.id.actionBarTittle);
        this.actionBarTittle.setText(getResources().getString(R.string.menu_notification));
    }

    public void on_GeneralSuccess(JSONObject response, int responseCode) {
        if (responseCode == 1) {
            try {
                if (response.getString("success").equalsIgnoreCase("1")) {
                    if (response.getString("notification_flag").equalsIgnoreCase("1")) {
                        this.pref.setBooleanValue(Constant.isNotification, true);
                    } else {
                        this.pref.setBooleanValue(Constant.isNotification, false);
                    }
                    Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void on_ProfileSuccess(JSONObject jSONObject) {
        try {
            if (jSONObject.getString("success").equalsIgnoreCase("1")) {
                if (jSONObject.has("notification_flag")) {
                    if (jSONObject.getString("notification_flag").equalsIgnoreCase("1")) {
                        this.pref.setBooleanValue(Constant.isNotification, true);
                    } else {
                        this.pref.setBooleanValue(Constant.isNotification, false);
                    }
                }
                Toast.makeText(getApplicationContext(), jSONObject.getString("message"), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}