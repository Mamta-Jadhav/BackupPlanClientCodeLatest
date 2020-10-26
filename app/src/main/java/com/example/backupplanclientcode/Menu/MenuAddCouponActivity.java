package com.example.backupplanclientcode.Menu;

import android.app.Activity;
import android.content.Intent;
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
import com.example.backupplanclientcode.ConnectionDetector;
import com.example.backupplanclientcode.Constant.Constant;
import com.example.backupplanclientcode.MenuListActivity;
import com.example.backupplanclientcode.Preference.SettingPreference;
import com.example.backupplanclientcode.R;
import com.example.backupplanclientcode.ServiceUrl.ServiceUrl;
import com.google.firebase.analytics.FirebaseAnalytics.Param;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuAddCouponActivity extends Activity implements ResponseListener_General {
    private TextView actionBarTittle;
    Button btn_add;
    Button btn_back;
    Button btn_save;
    private ConnectionDetector connection;
    /* access modifiers changed from: private */
    public EditText etCoupon;
    private SettingPreference pref;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_add_coupon);
        new Bugsense().startBugsense(getApplicationContext());
        this.connection = new ConnectionDetector(getApplicationContext());
        this.pref = new SettingPreference(getApplicationContext());
        findViewId();
        try {
            checkCoupon();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void checkCoupon() throws JSONException {
        if (this.pref.getStringValue(Constant.subscription, "").equals(Param.COUPON)) {
            findViewById(R.id.layout_show_coupon).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_add_coupon).setVisibility(View.GONE);
        } else {
            findViewById(R.id.layout_show_coupon).setVisibility(View.GONE);
            findViewById(R.id.layout_add_coupon).setVisibility(View.VISIBLE);
        }
        if (this.pref.getStringValue(Constant.coupon, "") == null || this.pref.getStringValue(Constant.coupon, "").length() == 0) {
            findViewById(R.id.layout_show_coupon).setVisibility(View.GONE);
            findViewById(R.id.layout_add_coupon).setVisibility(View.VISIBLE);
        }
        JSONObject couponJson = new JSONObject(this.pref.getStringValue(Constant.coupon, ""));
        ((TextView) findViewById(R.id.tv_coupon_code)).setText(couponJson.getString("coupon_code"));
        ((TextView) findViewById(R.id.tv_coupon_expiry)).setText(couponJson.getString("coupon_expiry"));
    }

    private void findViewId() {
        this.etCoupon = (EditText) findViewById(R.id.txt_coupon_code);
        this.btn_save = (Button) findViewById(R.id.btn_save);
        this.btn_save.setVisibility(View.GONE);
        this.btn_add = (Button) findViewById(R.id.btn_add);
        this.etCoupon = (EditText) findViewById(R.id.txt_coupon_code);
        this.btn_back = (Button) findViewById(R.id.btn_back);
        this.btn_back.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MenuAddCouponActivity.this.finish();
            }
        });
        this.actionBarTittle = (TextView) findViewById(R.id.actionBarTittle);
        this.actionBarTittle.setText(getResources().getString(R.string.text_coupon_code));
        this.btn_add.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (MenuAddCouponActivity.this.etCoupon.getText().length() > 0) {
                    MenuAddCouponActivity.this.addCouponCode(MenuAddCouponActivity.this.etCoupon.getText().toString());
                } else {
                    MenuAddCouponActivity.this.displayMessage("Please enter coupon code to proceed.");
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void addCouponCode(String coupon) {
        if (this.connection.isConnectingToInternet()) {
            try {
                JSONObject nameValuePairs = new JSONObject();
                nameValuePairs.put("user_id", this.pref.getStringValue(Constant.user_id, ""));
                nameValuePairs.put("coupon_code", coupon);
                nameValuePairs.put("device", "android");
                new GeneralTask(this, ServiceUrl.add_coupon_code, nameValuePairs, 1, "post").execute(new Void[0]);
            } catch (Exception e) {
            }
            return;
        }
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.connectionFailMessage), Toast.LENGTH_SHORT).show();
    }

    /* access modifiers changed from: private */
    public void displayMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    public void on_GeneralSuccess(JSONObject response, int responseCode) {
        try {
            if (response.has("error")) {
                displayMessage(response.getString("error"));
            }
            if (response.has("message")) {
                displayMessage(response.getString("message"));
            }
            if (response.getInt("success") == 1) {
                saveUserInfo(response);
            }
        } catch (Exception e) {
        }
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
        this.pref.setStringValue(Constant.subscription, login_detail.getString("subscription"));
        try {
            this.pref.setStringValue(Constant.coupon, login_detail.has(Param.COUPON) ? login_detail.getJSONObject(Param.COUPON).toString() : "");
        } catch (Exception e) {
            this.pref.setStringValue(Constant.coupon, null);
        }
        this.pref.setBooleanValue(Constant.subscription_expired, login_detail.getBoolean("subscription_expired"));
        this.pref.setStringValue(Constant.subscription_end_date, login_detail.getString("subscription_end_date"));
        displayMessage(response.getString("message"));
        startActivity(new Intent(getApplicationContext(), MenuListActivity.class));
        finish();
    }
}