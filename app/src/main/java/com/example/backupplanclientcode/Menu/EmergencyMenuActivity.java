package com.example.backupplanclientcode.Menu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.backupplanclientcode.Asyntask.GeneralTask;
import com.example.backupplanclientcode.Asyntask.GeneralTask.ResponseListener_General;
import com.example.backupplanclientcode.Asyntask.SaveProfileAsytask;
import com.example.backupplanclientcode.Asyntask.SaveProfileAsytask.ResponseListerProfile;
import com.example.backupplanclientcode.Bugsense.Bugsense;
import com.example.backupplanclientcode.ConnectionDetector;
import com.example.backupplanclientcode.Constant.Constant;
import com.example.backupplanclientcode.LogOutTimerUtil;
import com.example.backupplanclientcode.Preference.SettingPreference;
import com.example.backupplanclientcode.R;
import com.example.backupplanclientcode.ServiceUrl.ServiceUrl;
import com.example.backupplanclientcode.loginActivity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.backupplanclientcode.LogOutTimerUtil.foreGround;
import static com.example.backupplanclientcode.LogOutTimerUtil.logout;

public class EmergencyMenuActivity extends Activity implements OnClickListener, ResponseListerProfile, ResponseListener_General, LogOutTimerUtil.LogOutListener {
    JSONArray NonemergencyArray;
    TextView actionBarTittle;
    ImageView addEmergency;
    ImageView addNonEmergency;
    Button btn_back;
    Button btn_save;
    ConnectionDetector connection;
    String delete_emergency = "";
    String delete_non_emergency = "";
    JSONArray emergencyArray;
    JSONObject emergency_data;
    boolean firstEmergency = true;
    boolean firstTimeNonEmergency = true;
    String kit_id = "";
    LinearLayout layoutEmergency;
    LinearLayout layoutNonEmergency;
    SettingPreference pref;
    ToggleButton yesNoKit1;
    ToggleButton yesNoKit10;
    ToggleButton yesNoKit11;
    ToggleButton yesNoKit12;
    ToggleButton yesNoKit13;
    ToggleButton yesNoKit14;
    ToggleButton yesNoKit15;
    ToggleButton yesNoKit16;
    ToggleButton yesNoKit17;
    ToggleButton yesNoKit18;
    ToggleButton yesNoKit19;
    ToggleButton yesNoKit2;
    ToggleButton yesNoKit20;
    ToggleButton yesNoKit21;
    ToggleButton yesNoKit22;
    ToggleButton yesNoKit23;
    ToggleButton yesNoKit24;
    ToggleButton yesNoKit25;
    ToggleButton yesNoKit26;
    ToggleButton yesNoKit27;
    ToggleButton yesNoKit28;
    ToggleButton yesNoKit29;
    ToggleButton yesNoKit3;
    ToggleButton yesNoKit30;
    ToggleButton yesNoKit31;
    ToggleButton yesNoKit4;
    ToggleButton yesNoKit5;
    ToggleButton yesNoKit6;
    ToggleButton yesNoKit7;
    ToggleButton yesNoKit8;
    ToggleButton yesNoKit9;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_emergency);
        this.pref = new SettingPreference(getApplicationContext());
        this.connection = new ConnectionDetector(getApplicationContext());
        new Bugsense().startBugsense(getApplicationContext());
        findViewId();
        findToggleButton();
        checkAlredySaveAccount();
        setEnableControl();
    }

    private void setEnableControl() {
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            this.actionBarTittle.setText(getResources().getString(R.string.menu_emergency));
            this.btn_save.setVisibility(View.GONE);
            this.yesNoKit1.setEnabled(false);
            this.yesNoKit2.setEnabled(false);
            this.yesNoKit3.setEnabled(false);
            this.yesNoKit4.setEnabled(false);
            this.yesNoKit5.setEnabled(false);
            this.yesNoKit6.setEnabled(false);
            this.yesNoKit7.setEnabled(false);
            this.yesNoKit8.setEnabled(false);
            this.yesNoKit9.setEnabled(false);
            this.yesNoKit10.setEnabled(false);
            this.yesNoKit11.setEnabled(false);
            this.yesNoKit12.setEnabled(false);
            this.yesNoKit13.setEnabled(false);
            this.yesNoKit14.setEnabled(false);
            this.yesNoKit15.setEnabled(false);
            this.yesNoKit16.setEnabled(false);
            this.yesNoKit17.setEnabled(false);
            this.yesNoKit18.setEnabled(false);
            this.yesNoKit19.setEnabled(false);
            this.yesNoKit20.setEnabled(false);
            this.yesNoKit21.setEnabled(false);
            this.yesNoKit22.setEnabled(false);
            this.yesNoKit23.setEnabled(false);
            this.yesNoKit24.setEnabled(false);
            this.yesNoKit25.setEnabled(false);
            this.yesNoKit26.setEnabled(false);
            this.yesNoKit27.setEnabled(false);
            this.yesNoKit28.setEnabled(false);
            this.yesNoKit29.setEnabled(false);
            this.yesNoKit30.setEnabled(false);
            this.yesNoKit31.setEnabled(false);
            this.addEmergency.setEnabled(false);
            this.addNonEmergency.setEnabled(false);
        }
    }

    private void findToggleButton() {
        this.yesNoKit1 = (ToggleButton) findViewById(R.id.yesNoKit1);
        this.yesNoKit2 = (ToggleButton) findViewById(R.id.yesNoKit2);
        this.yesNoKit3 = (ToggleButton) findViewById(R.id.yesNokit3);
        this.yesNoKit4 = (ToggleButton) findViewById(R.id.yesNokit4);
        this.yesNoKit5 = (ToggleButton) findViewById(R.id.yesNokit5);
        this.yesNoKit6 = (ToggleButton) findViewById(R.id.yesNokit6);
        this.yesNoKit7 = (ToggleButton) findViewById(R.id.yesNokit7);
        this.yesNoKit8 = (ToggleButton) findViewById(R.id.yesNokit8);
        this.yesNoKit9 = (ToggleButton) findViewById(R.id.yesNokit9);
        this.yesNoKit10 = (ToggleButton) findViewById(R.id.yesNokit10);
        this.yesNoKit11 = (ToggleButton) findViewById(R.id.yesNokit11);
        this.yesNoKit12 = (ToggleButton) findViewById(R.id.yesNokit12);
        this.yesNoKit13 = (ToggleButton) findViewById(R.id.yesNokit13);
        this.yesNoKit14 = (ToggleButton) findViewById(R.id.yesNokit14);
        this.yesNoKit15 = (ToggleButton) findViewById(R.id.yesNokit15);
        this.yesNoKit16 = (ToggleButton) findViewById(R.id.yesNokit16);
        this.yesNoKit17 = (ToggleButton) findViewById(R.id.yesNokit17);
        this.yesNoKit18 = (ToggleButton) findViewById(R.id.yesNokit18);
        this.yesNoKit19 = (ToggleButton) findViewById(R.id.yesNokit19);
        this.yesNoKit20 = (ToggleButton) findViewById(R.id.yesNokit20);
        this.yesNoKit21 = (ToggleButton) findViewById(R.id.yesNokit21);
        this.yesNoKit22 = (ToggleButton) findViewById(R.id.yesNokit22);
        this.yesNoKit23 = (ToggleButton) findViewById(R.id.yesNokit23);
        this.yesNoKit24 = (ToggleButton) findViewById(R.id.yesNokit24);
        this.yesNoKit25 = (ToggleButton) findViewById(R.id.yesNokit25);
        this.yesNoKit26 = (ToggleButton) findViewById(R.id.yesNokit26);
        this.yesNoKit27 = (ToggleButton) findViewById(R.id.yesNokit27);
        this.yesNoKit28 = (ToggleButton) findViewById(R.id.yesNokit28);
        this.yesNoKit29 = (ToggleButton) findViewById(R.id.yesNokit29);
        this.yesNoKit30 = (ToggleButton) findViewById(R.id.yesNokit30);
        this.yesNoKit31 = (ToggleButton) findViewById(R.id.yesNokit31);
    }

    private void checkAlredySaveAccount() {
        if (this.pref.getStringValue(Constant.emergencyFlag, "").equalsIgnoreCase("1")) {
            this.btn_save.setText("Edit");
            if (!this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                this.actionBarTittle.setText("Edit " + getResources().getString(R.string.menu_emergency));
            }
            if (this.connection.isConnectingToInternet()) {
                try {
                    JSONObject nameValuePair = new JSONObject();
                    nameValuePair.put("user_id", "2");//this.pref.getStringValue(Constant.user_id, ""));
                    nameValuePair.put("token", this.pref.getStringValue(Constant.jwttoken, ""));
                    new GeneralTask(this, ServiceUrl.get_emergency_detail, nameValuePair, 2, "post").execute(new Void[0]);
                } catch (Exception e) {
                }
                return;
            }
            displayMessage(getResources().getString(R.string.connectionFailMessage));
            return;
        }
        this.actionBarTittle.setText(getResources().getString(R.string.menu_emergency));
        addlayoutNonEmergency(null, false);
        addlayoutEmergency(null, false);
    }

    private void findViewId() {
        this.actionBarTittle = (TextView) findViewById(R.id.actionBarTittle);
        this.btn_save = (Button) findViewById(R.id.btn_save);
        this.btn_back = (Button) findViewById(R.id.btn_back);
        this.btn_save.setOnClickListener(this);
        this.btn_back.setOnClickListener(this);
        this.layoutEmergency = (LinearLayout) findViewById(R.id.layoutEmergency);
        this.layoutNonEmergency = (LinearLayout) findViewById(R.id.layoutNonEmergency);
        this.addEmergency = (ImageView) findViewById(R.id.addEmergency);
        this.addNonEmergency = (ImageView) findViewById(R.id.addNonEmergency);
        this.addNonEmergency.setOnClickListener(this);
        this.addEmergency.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save /*2131558589*/:
                prePareEmerencyJson();
                prePareNonEmerencyJson();
                prePareJsonKit();
                prePareSendJson();
                return;
            case R.id.btn_back /*2131558590*/:
                finish();
                return;
            case R.id.addEmergency /*2131558907*/:
                addlayoutEmergency(null, false);
                return;
            case R.id.addNonEmergency /*2131558909*/:
                addlayoutNonEmergency(null, false);
                return;
            default:
                return;
        }
    }

    private void prePareSendJson() {
        try {
            JSONObject sendJson = new JSONObject();
            sendJson.put("emergency_data", this.emergency_data);
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            entity.addPart("json_data", new StringBody("{\n" +
                    "    \"emergency_data\": {\n" +
                    "        \"user_id\": \"29\",\n" +
                    "        \"emergency_id\": \"2\",\n" +
                    "        \"home_security\": \"hs\",\n" +
                    "        \"police\": \"qqq\",\n" +
                    "        \"fire\": \"qq\",\n" +
                    "        \"health_clinic\": \"qqq1\",\n" +
                    "        \"poison_ctrl\": \"1\",\n" +
                    "        \"non_emergency\": [\n" +
                    "            {\n" +
                    "                \"non_emergency_id\": \"1\",\n" +
                    "                \"teacher\": \"ss\",\n" +
                    "                \"babysitter\": \"aa\",\n" +
                    "                \"sister\": \"ss\",\n" +
                    "                \"friend\": \"aa\",\n" +
                    "                \"user_id\": \"12\"\n" +
                    "            }\n" +
                    "        ]\n" +
                    "    }\n" +
                    "}"));//new StringBody(sendJson.toString()));
            if (!this.connection.isConnectingToInternet()) {
                displayMessage(getResources().getString(R.string.connectionFailMessage));
            } else if (this.btn_save.getText().toString().trim().equalsIgnoreCase("edit")) {
                JSONObject nameValuePair = new JSONObject();
                nameValuePair.put("json_data", sendJson);
//                nameValuePair.put("token", this.pref.getStringValue(Constant.jwttoken, ""));
//                new GeneralTask(this, ServiceUrl.edit_emergency, nameValuePair, 1, "post").execute(new Void[0]);
              Log.d("test", this.pref.getStringValue(Constant.jwttoken, ""));
              new SaveProfileAsytask(this, ServiceUrl.edit_emergency, entity).execute(new Void[0]);
            } else {
                new SaveProfileAsytask(this, ServiceUrl.save_emergency, entity).execute(new Void[0]);
            }
            Log.i("send json obj..", sendJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prePareJsonKit() {
        try {
            this.emergency_data = new JSONObject();
            this.emergency_data.put("user_id", this.pref.getStringValue(Constant.user_id, ""));
            this.emergency_data.put("delete_emergency", this.delete_emergency);
            this.emergency_data.put("delete_non_emergency", this.delete_non_emergency);
            this.emergency_data.put("emergency", this.emergencyArray);
            this.emergency_data.put("non_emergency", this.NonemergencyArray);
            this.emergency_data.put("kit_id", this.kit_id);
            if (this.yesNoKit1.isChecked()) {
                this.emergency_data.put("manual_can", "1");
            } else {
                this.emergency_data.put("manual_can", "0");
            }
            if (this.yesNoKit2.isChecked()) {
                this.emergency_data.put("garbage_bag", "1");
            } else {
                this.emergency_data.put("garbage_bag", "0");
            }
            if (this.yesNoKit3.isChecked()) {
                this.emergency_data.put("scraper", "1");
            } else {
                this.emergency_data.put("scraper", "0");
            }
            if (this.yesNoKit4.isChecked()) {
                this.emergency_data.put("prescription", "1");
            } else {
                this.emergency_data.put("prescription", "0");
            }
            if (this.yesNoKit5.isChecked()) {
                this.emergency_data.put("medication", "1");
            } else {
                this.emergency_data.put("medication", "0");
            }
            if (this.yesNoKit6.isChecked()) {
                this.emergency_data.put("medical", "1");
            } else {
                this.emergency_data.put("medical", "0");
            }
            if (this.yesNoKit7.isChecked()) {
                this.emergency_data.put("glasses", "1");
            } else {
                this.emergency_data.put("glasses", "0");
            }
            if (this.yesNoKit8.isChecked()) {
                this.emergency_data.put("corded_phone", "1");
            } else {
                this.emergency_data.put("corded_phone", "0");
            }
            if (this.yesNoKit9.isChecked()) {
                this.emergency_data.put("wind_up_flash_light", "1");
            } else {
                this.emergency_data.put("wind_up_flash_light", "0");
            }
            if (this.yesNoKit10.isChecked()) {
                this.emergency_data.put("non_perishable", "1");
            } else {
                this.emergency_data.put("non_perishable", "0");
            }
            if (this.yesNoKit11.isChecked()) {
                this.emergency_data.put("infant_formula", "1");
            } else {
                this.emergency_data.put("infant_formula", "0");
            }
            if (this.yesNoKit12.isChecked()) {
                this.emergency_data.put("protein_drink", "1");
            } else {
                this.emergency_data.put("protein_drink", "0");
            }
            if (this.yesNoKit13.isChecked()) {
                this.emergency_data.put("energy_bars", "1");
            } else {
                this.emergency_data.put("energy_bars", "0");
            }
            if (this.yesNoKit14.isChecked()) {
                this.emergency_data.put("toliet_paper", "1");
            } else {
                this.emergency_data.put("toliet_paper", "0");
            }
            if (this.yesNoKit15.isChecked()) {
                this.emergency_data.put("bleach", "1");
            } else {
                this.emergency_data.put("bleach", "0");
            }
            if (this.yesNoKit16.isChecked()) {
                this.emergency_data.put("basic_hand_tool", "1");
            } else {
                this.emergency_data.put("basic_hand_tool", "0");
            }
            if (this.yesNoKit17.isChecked()) {
                this.emergency_data.put("small_stove", "1");
            } else {
                this.emergency_data.put("small_stove", "0");
            }
            if (this.yesNoKit18.isChecked()) {
                this.emergency_data.put("first_aid_kit", "1");
            } else {
                this.emergency_data.put("first_aid_kit", "0");
            }
            if (this.yesNoKit19.isChecked()) {
                this.emergency_data.put("whistle", "1");
            } else {
                this.emergency_data.put("whistle", "0");
            }
            if (this.yesNoKit20.isChecked()) {
                this.emergency_data.put("extra_keys", "1");
            } else {
                this.emergency_data.put("extra_keys", "0");
            }
            if (this.yesNoKit21.isChecked()) {
                this.emergency_data.put("blankets", "1");
            } else {
                this.emergency_data.put("blankets", "0");
            }
            if (this.yesNoKit22.isChecked()) {
                this.emergency_data.put("wind_up_radio", "1");
            } else {
                this.emergency_data.put("wind_up_radio", "0");
            }
            if (this.yesNoKit23.isChecked()) {
                this.emergency_data.put("extra_clothing", "1");
            } else {
                this.emergency_data.put("extra_clothing", "0");
            }
            if (this.yesNoKit24.isChecked()) {
                this.emergency_data.put("duct_tape", "1");
            } else {
                this.emergency_data.put("duct_tape", "0");
            }
            if (this.yesNoKit25.isChecked()) {
                this.emergency_data.put("toiletries", "1");
            } else {
                this.emergency_data.put("toiletries", "0");
            }
            if (this.yesNoKit26.isChecked()) {
                this.emergency_data.put("hand_sanitizer", "1");
            } else {
                this.emergency_data.put("hand_sanitizer", "0");
            }
            if (this.yesNoKit27.isChecked()) {
                this.emergency_data.put("utensils", "1");
            } else {
                this.emergency_data.put("utensils", "0");
            }
            if (this.yesNoKit28.isChecked()) {
                this.emergency_data.put("paper_tower", "1");
            } else {
                this.emergency_data.put("paper_tower", "0");
            }
            if (this.yesNoKit29.isChecked()) {
                this.emergency_data.put("emergancy_plan_change", "1");
            } else {
                this.emergency_data.put("emergancy_plan_change", "0");
            }
            if (this.yesNoKit30.isChecked()) {
                this.emergency_data.put("candles", "1");
            } else {
                this.emergency_data.put("candles", "0");
            }
            if (this.yesNoKit31.isChecked()) {
                this.emergency_data.put("matches", "1");
            } else {
                this.emergency_data.put("matches", "0");
            }
        } catch (Exception e) {
            try {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private void prePareNonEmerencyJson() {
        try {
            this.NonemergencyArray = new JSONArray();
            for (int i = 0; i < this.layoutNonEmergency.getChildCount(); i++) {
                JSONObject jsonbj = new JSONObject();
                ViewGroup view = (ViewGroup) this.layoutNonEmergency.getChildAt(i);
                EditText editTeacher = (EditText) view.findViewById(R.id.editTeacher);
                EditText editBabySister = (EditText) view.findViewById(R.id.editBabySister);
                EditText editSister = (EditText) view.findViewById(R.id.editSister);
                EditText editFriend = (EditText) view.findViewById(R.id.editFriend);
                jsonbj.put("non_emergency_id", ((EditText) view.findViewById(R.id.editId)).getText().toString().trim());
                jsonbj.put("teacher", editTeacher.getText().toString().trim());
                jsonbj.put("babysitter", editBabySister.getText().toString().trim());
                jsonbj.put("sister", editSister.getText().toString().trim());
                jsonbj.put("friend", editFriend.getText().toString().trim());
                this.NonemergencyArray.put(jsonbj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prePareEmerencyJson() {
        try {
            this.emergencyArray = new JSONArray();
            for (int i = 0; i < this.layoutEmergency.getChildCount(); i++) {
                JSONObject jsonbj = new JSONObject();
                ViewGroup view = (ViewGroup) this.layoutEmergency.getChildAt(i);
                EditText editPoliceNo = (EditText) view.findViewById(R.id.editPoliceNo);
                EditText editFireNo = (EditText) view.findViewById(R.id.editFireNo);
                EditText editClinic = (EditText) view.findViewById(R.id.editClinic);
                EditText editPosionControl = (EditText) view.findViewById(R.id.editPosionControl);
                EditText editHomeSecurity = (EditText) view.findViewById(R.id.editHomeSecurity);
                jsonbj.put("emergency_id", ((EditText) view.findViewById(R.id.editId)).getText().toString().trim());
                jsonbj.put("police", editPoliceNo.getText().toString().trim());
                jsonbj.put("fire", editFireNo.getText().toString().trim());
                jsonbj.put("health_clinic", editClinic.getText().toString().trim());
                jsonbj.put("poison_ctrl", editPosionControl.getText().toString().trim());
                jsonbj.put("home_security", editHomeSecurity.getText().toString().trim());
                this.emergencyArray.put(jsonbj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint({"InflateParams"})
    private void addlayoutEmergency(JSONObject json, boolean isJson) {
        try {
            final View view = LayoutInflater.from(this).inflate(R.layout.layout_emergency, null);
            final ImageView removeIcon = (ImageView) view.findViewById(R.id.removeIcon);
            removeIcon.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    EmergencyMenuActivity.this.layoutEmergency.removeView(view);
                    if (EmergencyMenuActivity.this.delete_emergency.equalsIgnoreCase("")) {
                        EmergencyMenuActivity.this.delete_emergency = EmergencyMenuActivity.this.delete_emergency.concat(removeIcon.getTag().toString());
                        return;
                    }
                    EmergencyMenuActivity.this.delete_emergency = EmergencyMenuActivity.this.delete_emergency.concat("," + removeIcon.getTag().toString());
                }
            });
            if (this.firstEmergency) {
                removeIcon.setVisibility(View.GONE);
            } else {
                removeIcon.setVisibility(View.VISIBLE);
            }
            EditText editId = (EditText) view.findViewById(R.id.editId);
            EditText editPoliceNo = (EditText) view.findViewById(R.id.editPoliceNo);
            EditText editFireNo = (EditText) view.findViewById(R.id.editFireNo);
            EditText editClinic = (EditText) view.findViewById(R.id.editClinic);
            EditText editPosionControl = (EditText) view.findViewById(R.id.editPosionControl);
            EditText editHomeSecurity = (EditText) view.findViewById(R.id.editHomeSecurity);
            if (isJson) {
                editId.setText(json.getString("emergency_id"));
                editPoliceNo.setText(json.getString("police"));
                editFireNo.setText(json.getString("fire"));
                editClinic.setText(json.getString("health_clinic"));
                editPosionControl.setText(json.getString("poison_ctrl"));
                editHomeSecurity.setText(json.getString("home_security"));
                removeIcon.setTag(json.getString("emergency_id"));
            }
            if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                removeIcon.setVisibility(View.GONE);
                editPoliceNo.setEnabled(false);
                editFireNo.setEnabled(false);
                editClinic.setEnabled(false);
                editPosionControl.setEnabled(false);
                editHomeSecurity.setEnabled(false);
            }
            this.layoutEmergency.addView(view);
            this.firstEmergency = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint({"InflateParams"})
    private void addlayoutNonEmergency(JSONObject json, boolean isJson) {
        try {
            final View view = LayoutInflater.from(this).inflate(R.layout.layout_non_emergency, null);
            final ImageView removeIcon = (ImageView) view.findViewById(R.id.removeIcon);
            removeIcon.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    EmergencyMenuActivity.this.layoutNonEmergency.removeView(view);
                    if (EmergencyMenuActivity.this.delete_non_emergency.equalsIgnoreCase("")) {
                        EmergencyMenuActivity.this.delete_non_emergency = EmergencyMenuActivity.this.delete_non_emergency.concat(removeIcon.getTag().toString());
                        return;
                    }
                    EmergencyMenuActivity.this.delete_non_emergency = EmergencyMenuActivity.this.delete_non_emergency.concat("," + removeIcon.getTag().toString());
                }
            });
            if (this.firstTimeNonEmergency) {
                removeIcon.setVisibility(View.GONE);
            } else {
                removeIcon.setVisibility(View.VISIBLE);
            }
            EditText editId = (EditText) view.findViewById(R.id.editId);
            EditText editTeacher = (EditText) view.findViewById(R.id.editTeacher);
            EditText editBabySister = (EditText) view.findViewById(R.id.editBabySister);
            EditText editSister = (EditText) view.findViewById(R.id.editSister);
            EditText editFriend = (EditText) view.findViewById(R.id.editFriend);
            if (isJson) {
                editId.setText(json.getString("non_emergency_id"));
                editTeacher.setText(json.getString("teacher"));
                editBabySister.setText(json.getString("babysitter"));
                editSister.setText(json.getString("sister"));
                editFriend.setText(json.getString("friend"));
                removeIcon.setTag(json.getString("non_emergency_id"));
            }
            if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                removeIcon.setVisibility(View.GONE);
                editTeacher.setEnabled(false);
                editBabySister.setEnabled(false);
                editSister.setEnabled(false);
                editFriend.setEnabled(false);
            }
            this.layoutNonEmergency.addView(view);
            this.firstTimeNonEmergency = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void on_GeneralSuccess(JSONObject response, int responseCode) {
        if (response == null) {
            try {
                addlayoutNonEmergency(null, false);
                addlayoutEmergency(null, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            show_emergency(response);
            show_Non_emergency(response);
        }
    }

    private void show_Non_emergency(JSONObject response) {
        try {
            JSONArray jsonArray = response.getJSONObject("emergency").getJSONArray("non_emergency");
            if (jsonArray.length() == 0) {
                addlayoutNonEmergency(null, false);
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                addlayoutNonEmergency(jsonArray.getJSONObject(i), true);
            }
        } catch (Exception e) {
            addlayoutNonEmergency(null, false);
            e.printStackTrace();
        }
    }

    private void show_emergency(JSONObject response) {
        try {
            JSONArray jsonArray = response.getJSONObject("emergency").getJSONArray("emergency");
            if (jsonArray.length() == 0) {
                addlayoutEmergency(null, false);
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                addlayoutEmergency(jsonArray.getJSONObject(i), true);
            }
            showEmergencyKit(response);
        } catch (Exception e) {
            addlayoutEmergency(null, false);
            e.printStackTrace();
        }
    }

    private void showEmergencyKit(JSONObject response) {
        try {
            JSONObject json = response.getJSONObject("emergency");
            this.kit_id = json.getString("kit_id");
            if (json.getString("manual_can").equalsIgnoreCase("1")) {
                this.yesNoKit1.setChecked(true);
            } else {
                this.yesNoKit1.setChecked(false);
            }
            if (json.getString("garbage_bag").equalsIgnoreCase("1")) {
                this.yesNoKit2.setChecked(true);
            } else {
                this.yesNoKit2.setChecked(false);
            }
            if (json.getString("scraper").equalsIgnoreCase("1")) {
                this.yesNoKit3.setChecked(true);
            } else {
                this.yesNoKit3.setChecked(false);
            }
            if (json.getString("prescription").equalsIgnoreCase("1")) {
                this.yesNoKit4.setChecked(true);
            } else {
                this.yesNoKit4.setChecked(false);
            }
            if (json.getString("medication").equalsIgnoreCase("1")) {
                this.yesNoKit5.setChecked(true);
            } else {
                this.yesNoKit5.setChecked(false);
            }
            if (json.getString("medical").equalsIgnoreCase("1")) {
                this.yesNoKit6.setChecked(true);
            } else {
                this.yesNoKit6.setChecked(false);
            }
            if (json.getString("glasses").equalsIgnoreCase("1")) {
                this.yesNoKit7.setChecked(true);
            } else {
                this.yesNoKit7.setChecked(false);
            }
            if (json.getString("corded_phone").equalsIgnoreCase("1")) {
                this.yesNoKit8.setChecked(true);
            } else {
                this.yesNoKit8.setChecked(false);
            }
            if (json.getString("wind_up_flash_light").equalsIgnoreCase("1")) {
                this.yesNoKit9.setChecked(true);
            } else {
                this.yesNoKit9.setChecked(false);
            }
            if (json.getString("non_perishable").equalsIgnoreCase("1")) {
                this.yesNoKit10.setChecked(true);
            } else {
                this.yesNoKit10.setChecked(false);
            }
            if (json.getString("infant_formula").equalsIgnoreCase("1")) {
                this.yesNoKit11.setChecked(true);
            } else {
                this.yesNoKit11.setChecked(false);
            }
            if (json.getString("protein_drink").equalsIgnoreCase("1")) {
                this.yesNoKit12.setChecked(true);
            } else {
                this.yesNoKit12.setChecked(false);
            }
            if (json.getString("energy_bars").equalsIgnoreCase("1")) {
                this.yesNoKit13.setChecked(true);
            } else {
                this.yesNoKit13.setChecked(false);
            }
            if (json.getString("toliet_paper").equalsIgnoreCase("1")) {
                this.yesNoKit14.setChecked(true);
            } else {
                this.yesNoKit14.setChecked(false);
            }
            if (json.getString("bleach").equalsIgnoreCase("1")) {
                this.yesNoKit15.setChecked(true);
            } else {
                this.yesNoKit15.setChecked(false);
            }
            if (json.getString("basic_hand_tool").equalsIgnoreCase("1")) {
                this.yesNoKit16.setChecked(true);
            } else {
                this.yesNoKit16.setChecked(false);
            }
            if (json.getString("small_stove").equalsIgnoreCase("1")) {
                this.yesNoKit17.setChecked(true);
            } else {
                this.yesNoKit17.setChecked(false);
            }
            if (json.getString("first_aid_kit").equalsIgnoreCase("1")) {
                this.yesNoKit18.setChecked(true);
            } else {
                this.yesNoKit18.setChecked(false);
            }
            if (json.getString("whistle").equalsIgnoreCase("1")) {
                this.yesNoKit19.setChecked(true);
            } else {
                this.yesNoKit19.setChecked(false);
            }
            if (json.getString("extra_keys").equalsIgnoreCase("1")) {
                this.yesNoKit20.setChecked(true);
            } else {
                this.yesNoKit20.setChecked(false);
            }
            if (json.getString("blankets").equalsIgnoreCase("1")) {
                this.yesNoKit21.setChecked(true);
            } else {
                this.yesNoKit21.setChecked(false);
            }
            if (json.getString("wind_up_radio").equalsIgnoreCase("1")) {
                this.yesNoKit22.setChecked(true);
            } else {
                this.yesNoKit22.setChecked(false);
            }
            if (json.getString("extra_clothing").equalsIgnoreCase("1")) {
                this.yesNoKit23.setChecked(true);
            } else {
                this.yesNoKit23.setChecked(false);
            }
            if (json.getString("duct_tape").equalsIgnoreCase("1")) {
                this.yesNoKit24.setChecked(true);
            } else {
                this.yesNoKit24.setChecked(false);
            }
            if (json.getString("toiletries").equalsIgnoreCase("1")) {
                this.yesNoKit25.setChecked(true);
            } else {
                this.yesNoKit25.setChecked(false);
            }
            if (json.getString("hand_sanitizer").equalsIgnoreCase("1")) {
                this.yesNoKit26.setChecked(true);
            } else {
                this.yesNoKit26.setChecked(false);
            }
            if (json.getString("utensils").equalsIgnoreCase("1")) {
                this.yesNoKit27.setChecked(true);
            } else {
                this.yesNoKit27.setChecked(false);
            }
            if (json.getString("paper_tower").equalsIgnoreCase("1")) {
                this.yesNoKit28.setChecked(true);
            } else {
                this.yesNoKit28.setChecked(false);
            }
            if (json.getString("emergancy_plan_change").equalsIgnoreCase("1")) {
                this.yesNoKit29.setChecked(true);
            } else {
                this.yesNoKit29.setChecked(false);
            }
            if (json.getString("candles").equalsIgnoreCase("1")) {
                this.yesNoKit30.setChecked(true);
            } else {
                this.yesNoKit30.setChecked(false);
            }
            if (json.getString("matches").equalsIgnoreCase("1")) {
                this.yesNoKit31.setChecked(true);
            } else {
                this.yesNoKit31.setChecked(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void on_ProfileSuccess(JSONObject response) {
        try {
            if (response.has("message")) {
                displayMessage(response.getString("message").toString());
            }
            if (response.has("success") && response.getString("success").toString().trim().equalsIgnoreCase("1")) {
                this.pref.setStringValue(Constant.emergencyFlag, response.getString("success").toString());
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayMessage(String string) {
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG).show();
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