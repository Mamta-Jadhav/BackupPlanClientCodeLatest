package com.example.backupplanclientcode.Menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.backupplanclientcode.Asyntask.GeneralTask;
import com.example.backupplanclientcode.Asyntask.GeneralTask.ResponseListener_General;
import com.example.backupplanclientcode.BillingUtil.IabHelper;
import com.example.backupplanclientcode.BillingUtil.IabHelper.IabAsyncInProgressException;
import com.example.backupplanclientcode.BillingUtil.IabHelper.OnConsumeFinishedListener;
import com.example.backupplanclientcode.BillingUtil.IabHelper.OnIabPurchaseFinishedListener;
import com.example.backupplanclientcode.BillingUtil.IabHelper.OnIabSetupFinishedListener;
import com.example.backupplanclientcode.BillingUtil.IabHelper.QueryInventoryFinishedListener;
import com.example.backupplanclientcode.BillingUtil.IabResult;
import com.example.backupplanclientcode.BillingUtil.Inventory;
import com.example.backupplanclientcode.BillingUtil.Purchase;
import com.example.backupplanclientcode.ConnectionDetector;
import com.example.backupplanclientcode.Constant.Constant;
import com.example.backupplanclientcode.IInAppBillingService;
import com.example.backupplanclientcode.LogOutTimerUtil;
import com.example.backupplanclientcode.Preference.SettingPreference;
import com.example.backupplanclientcode.R;
import com.example.backupplanclientcode.ServiceUrl.ServiceUrl;
import com.example.backupplanclientcode.loginActivity;
import com.google.firebase.analytics.FirebaseAnalytics.Param;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.backupplanclientcode.LogOutTimerUtil.foreGround;
import static com.example.backupplanclientcode.LogOutTimerUtil.logout;

public class MenuUpgrade extends Activity implements ResponseListener_General, LogOutTimerUtil.LogOutListener {
    public static final int BILLING_RESPONSE_RESULT_OK = 0;
    static final String ITEM_SKU = "com.example.backupplanclientcode.purchase";
    private ConnectionDetector connection;
    OnConsumeFinishedListener mConsumeFinishedListener = new OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            if (result.isSuccess()) {
                MenuUpgrade.this.displayToast("Please wait a moment...");
                MenuUpgrade.this.sendPurchaseRequest();
                Log.d("~~~~~~~~~>", "Ready for another purchase");
                return;
            }
            MenuUpgrade.this.displayToast("Could not complete purchase transaction.");
        }
    };
    /* access modifiers changed from: private */
    public IabHelper mIabHelper;
    OnIabPurchaseFinishedListener mPurchaseFinishedListener = new OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (result.isFailure()) {
                MenuUpgrade.this.displayToast("Purchase transaction failed.");
            } else if (purchase.getSku().equals(MenuUpgrade.ITEM_SKU)) {
                MenuUpgrade.this.consumeItem();
            }
        }
    };
    QueryInventoryFinishedListener mReceivedInventoryListener = new QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            if (result.isFailure()) {
                MenuUpgrade.this.displayToast("Purchase transaction failed.");
                return;
            }
            try {
                MenuUpgrade.this.mIabHelper.consumeAsync(inventory.getPurchase(MenuUpgrade.ITEM_SKU), MenuUpgrade.this.mConsumeFinishedListener);
            } catch (IabAsyncInProgressException e) {
                e.printStackTrace();
            }
        }
    };
    private IInAppBillingService mService;
    private SettingPreference pref;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_upgrade);
        this.connection = new ConnectionDetector(getApplicationContext());
        this.pref = new SettingPreference(getApplicationContext());
        setPlanDetails();
        findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MenuUpgrade.this.finish();
            }
        });
        findViewById(R.id.layoutPlan1).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                try {
                    MenuUpgrade.this.mIabHelper.launchPurchaseFlow(MenuUpgrade.this, MenuUpgrade.ITEM_SKU, 10001, MenuUpgrade.this.mPurchaseFinishedListener, "mypurchasetoken");
                } catch (IabAsyncInProgressException e) {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.layoutPlan2).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
            }
        });
        initInAppPurchase();
        if (this.pref.getStringValue(Constant.subscription, "free").equals("paid")) {
            findViewById(R.id.layoutPlan1).setVisibility(View.GONE);
            findViewById(R.id.layout_purchased).setVisibility(View.VISIBLE);
            return;
        }
        findViewById(R.id.layoutPlan1).setVisibility(View.VISIBLE);
        findViewById(R.id.layout_purchased).setVisibility(View.GONE);
    }

    private void setPlanDetails() {
        ((TextView) findViewById(R.id.tvPlanPrice1)).setText("$4.99");
    }

    private void initInAppPurchase() {
        this.mIabHelper = new IabHelper(this, getString(R.string.in_app_key));
        this.mIabHelper.startSetup(new OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.d("~~~~~~~", "In-app Billing setup failed: " + result);
                } else {
                    Log.d("~~~~~~~", "In-app Billing is set up OK");
                }
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mIabHelper != null) {
            try {
                this.mIabHelper.dispose();
                this.mIabHelper = null;
            } catch (IabAsyncInProgressException e) {
                e.printStackTrace();
            }
        }
    }

    public void consumeItem() {
        try {
            this.mIabHelper.queryInventoryAsync(this.mReceivedInventoryListener);
        } catch (IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!this.mIabHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /* access modifiers changed from: private */
    public void displayToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    /* access modifiers changed from: private */
    public void sendPurchaseRequest() {
        if (this.connection.isConnectingToInternet()) {
            try {
                JSONObject nameValuePairs = new JSONObject();
                nameValuePairs.put("user_id", this.pref.getStringValue(Constant.user_id, ""));
                nameValuePairs.put("plan_id", ITEM_SKU);
                nameValuePairs.put("amount", "4.99");
                nameValuePairs.put("trans_identifier", "425334");
                nameValuePairs.put("product_identifier", "425334");
                nameValuePairs.put("local_amount", "4.99");
                nameValuePairs.put("device", "android");
                new GeneralTask(this, ServiceUrl.update_user_subscription, nameValuePairs, 100, "post").execute(new Void[0]);
            } catch (Exception e) {
            }
            return;
        }
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.connectionFailMessage), Toast.LENGTH_SHORT).show();
    }

    public void on_GeneralSuccess(JSONObject response, int responseCode) {
        if (responseCode == 100) {
            try {
                if (response.has("message")) {
                    saveUserInfo(response);
                }
            } catch (Exception e) {
                e.printStackTrace();
                displayToast("" + e.getLocalizedMessage().toString());
            }
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
        displayToast(response.getString("message"));
        finish();
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