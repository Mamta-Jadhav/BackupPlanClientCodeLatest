package com.example.backupplanclientcode.Menu;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.backupplanclientcode.Asyntask.GeneralTask;
import com.example.backupplanclientcode.Asyntask.GeneralTask.ResponseListener_General;
import com.example.backupplanclientcode.Asyntask.SaveProfileAsytask;
import com.example.backupplanclientcode.Asyntask.SaveProfileAsytask.ResponseListerProfile;
import com.example.backupplanclientcode.Bugsense.Bugsense;
import com.example.backupplanclientcode.ConnectionDetector;
import com.example.backupplanclientcode.Constant.Constant;
import com.example.backupplanclientcode.Preference.SettingPreference;
import com.example.backupplanclientcode.R;
import com.example.backupplanclientcode.ServiceUrl.ServiceUrl;

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

public class BillsToPayMenuActivity extends Activity implements OnClickListener, ResponseListerProfile, ResponseListener_General {
    TextView actionBarTittle;
    JSONArray b_data;
    Button btn_back;
    Button btn_save;
    ImageView closeDrop1;
    ImageView closeDrop2;
    ImageView closeDrop3;
    ConnectionDetector connection;
    LinearLayout layoutDrop1;
    LinearLayout layoutDrop2;
    LinearLayout layoutDrop3;
    LinearLayout layoutOpenDrop1;
    LinearLayout layoutOpenDrop2;
    LinearLayout layoutOpenDrop3;
    ImageView openDrop1;
    ImageView openDrop2;
    ImageView openDrop3;
    SettingPreference pref;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_bills_to_pay);
        intilization();
        findViewId();
        checkAlredySaveAccount();
    }

    private void intilization() {
        this.pref = new SettingPreference(getApplicationContext());
        this.connection = new ConnectionDetector(getApplicationContext());
        new Bugsense().startBugsense(getApplicationContext());
    }

    private void checkAlredySaveAccount() {
        if (!this.connection.isConnectingToInternet()) {
            displayMessage(getResources().getString(R.string.connectionFailMessage));
            show_all_layouts();
        } else if (this.pref.getStringValue(Constant.billToPay, "").equalsIgnoreCase("1")) {
            this.btn_save.setText("Edit");
            if (!this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                this.actionBarTittle.setText("Edit " + getResources().getString(R.string.menu_bill_to_pay));
            }
            try {
                JSONObject nameValuePair = new JSONObject();
                nameValuePair.put("user_id", this.pref.getStringValue(Constant.user_id, ""));
                nameValuePair.put("token", this.pref.getStringValue(Constant.jwttoken, ""));
                new GeneralTask(this, ServiceUrl.get_bill_to_pay_detail, nameValuePair, 2, "post").execute(new Void[0]);
            } catch (Exception e) {
            }
        } else {
            show_all_layouts();
        }
    }

    private void show_all_layouts() {
        this.actionBarTittle.setText(getResources().getString(R.string.menu_bill_to_pay));
        addlayoutRent1();
        addlayoutRent2();
        addlayoutRent3();
        addlayoutRent4();
        addlayoutRent5();
        addlayoutRent6();
        addlayoutRent7();
        addlayoutRent8();
        addlayoutRent9();
        addlayoutRent10();
        addlayoutRent11();
        addlayoutRent12();
        addlayoutRent13();
        addlayoutRent14();
        addlayoutRent15();
        addlayoutRent16();
        addlayoutRent17();
        addlayoutRent18();
        addlayoutRent19();
        addlayoutRent20();
        addlayoutRent21();
    }

    private void findViewId() {
        this.btn_save = (Button) findViewById(R.id.btn_save);
        this.btn_back = (Button) findViewById(R.id.btn_back);
        this.closeDrop1 = (ImageView) findViewById(R.id.closeDrop1);
        this.closeDrop1.setVisibility(View.GONE);
        this.closeDrop1.setOnClickListener(this);
        this.openDrop1 = (ImageView) findViewById(R.id.openDrop1);
        this.openDrop1.setVisibility(View.VISIBLE);
        this.openDrop1.setOnClickListener(this);
        this.closeDrop2 = (ImageView) findViewById(R.id.closeDrop2);
        this.closeDrop2.setVisibility(View.GONE);
        this.closeDrop2.setOnClickListener(this);
        this.openDrop2 = (ImageView) findViewById(R.id.openDrop2);
        this.openDrop2.setVisibility(View.VISIBLE);
        this.openDrop2.setOnClickListener(this);
        this.closeDrop3 = (ImageView) findViewById(R.id.closeDrop3);
        this.closeDrop3.setVisibility(View.GONE);
        this.closeDrop3.setOnClickListener(this);
        this.openDrop3 = (ImageView) findViewById(R.id.openDrop3);
        this.openDrop3.setVisibility(View.VISIBLE);
        this.openDrop3.setOnClickListener(this);
        this.actionBarTittle = (TextView) findViewById(R.id.actionBarTittle);
        this.layoutDrop1 = (LinearLayout) findViewById(R.id.layoutDrop1);
        this.layoutDrop2 = (LinearLayout) findViewById(R.id.layoutDrop2);
        this.layoutDrop3 = (LinearLayout) findViewById(R.id.layoutDrop3);
        this.layoutOpenDrop1 = (LinearLayout) findViewById(R.id.layoutOpenDrop1);
        this.layoutOpenDrop2 = (LinearLayout) findViewById(R.id.layoutOpenDrop2);
        this.layoutOpenDrop3 = (LinearLayout) findViewById(R.id.layoutOpenDrop3);
        this.layoutOpenDrop1.setOnClickListener(this);
        this.layoutOpenDrop2.setOnClickListener(this);
        this.layoutOpenDrop3.setOnClickListener(this);
        this.btn_save.setOnClickListener(this);
        this.btn_back.setOnClickListener(this);
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            setControlEnable();
        }
    }

    private void setControlEnable() {
        this.actionBarTittle.setText(getResources().getString(R.string.menu_bill_to_pay));
        this.btn_save.setVisibility(View.GONE);
    }

    @SuppressLint({"InflateParams"})
    private void addlayoutRent21() {
        View View = LayoutInflater.from(this).inflate(R.layout.layout_bills_to_pay, null);
        ((TextView) View.findViewById(R.id.rentLabel)).setText(getResources().getString(R.string.txt_rent21));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            EditText editAccount = (EditText) View.findViewById(R.id.editAccount);
            EditText editPayment = (EditText) View.findViewById(R.id.editPayment);
            ((EditText) View.findViewById(R.id.editCompany)).setEnabled(false);
            editAccount.setEnabled(false);
            editPayment.setEnabled(false);
            RadioButton isInstitution = (RadioButton) View.findViewById(R.id.isInstitution);
            ((RadioButton) View.findViewById(R.id.isOnline)).setEnabled(false);
            isInstitution.setEnabled(false);
        }
        this.layoutDrop3.addView(View);
    }

    @SuppressLint({"InflateParams"})
    private void addlayoutRent20() {
        View View = LayoutInflater.from(this).inflate(R.layout.layout_bills_to_pay, null);
        ((TextView) View.findViewById(R.id.rentLabel)).setText(getResources().getString(R.string.txt_rent20));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            EditText editAccount = (EditText) View.findViewById(R.id.editAccount);
            EditText editPayment = (EditText) View.findViewById(R.id.editPayment);
            ((EditText) View.findViewById(R.id.editCompany)).setEnabled(false);
            editAccount.setEnabled(false);
            editPayment.setEnabled(false);
            RadioButton isInstitution = (RadioButton) View.findViewById(R.id.isInstitution);
            ((RadioButton) View.findViewById(R.id.isOnline)).setEnabled(false);
            isInstitution.setEnabled(false);
        }
        this.layoutDrop3.addView(View);
    }

    @SuppressLint({"InflateParams"})
    private void addlayoutRent19() {
        View View = LayoutInflater.from(this).inflate(R.layout.layout_bills_to_pay, null);
        ((TextView) View.findViewById(R.id.rentLabel)).setText(getResources().getString(R.string.txt_rent19));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            EditText editAccount = (EditText) View.findViewById(R.id.editAccount);
            EditText editPayment = (EditText) View.findViewById(R.id.editPayment);
            ((EditText) View.findViewById(R.id.editCompany)).setEnabled(false);
            editAccount.setEnabled(false);
            editPayment.setEnabled(false);
            RadioButton isInstitution = (RadioButton) View.findViewById(R.id.isInstitution);
            ((RadioButton) View.findViewById(R.id.isOnline)).setEnabled(false);
            isInstitution.setEnabled(false);
        }
        this.layoutDrop3.addView(View);
    }

    @SuppressLint({"InflateParams"})
    private void addlayoutRent18() {
        View View = LayoutInflater.from(this).inflate(R.layout.layout_bills_to_pay, null);
        ((TextView) View.findViewById(R.id.rentLabel)).setText(getResources().getString(R.string.txt_rent18));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            EditText editAccount = (EditText) View.findViewById(R.id.editAccount);
            EditText editPayment = (EditText) View.findViewById(R.id.editPayment);
            ((EditText) View.findViewById(R.id.editCompany)).setEnabled(false);
            editAccount.setEnabled(false);
            editPayment.setEnabled(false);
            RadioButton isInstitution = (RadioButton) View.findViewById(R.id.isInstitution);
            ((RadioButton) View.findViewById(R.id.isOnline)).setEnabled(false);
            isInstitution.setEnabled(false);
        }
        this.layoutDrop3.addView(View);
    }

    @SuppressLint({"InflateParams"})
    private void addlayoutRent17() {
        View View = LayoutInflater.from(this).inflate(R.layout.layout_bills_to_pay, null);
        ((TextView) View.findViewById(R.id.rentLabel)).setText(getResources().getString(R.string.txt_rent17));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            EditText editAccount = (EditText) View.findViewById(R.id.editAccount);
            EditText editPayment = (EditText) View.findViewById(R.id.editPayment);
            ((EditText) View.findViewById(R.id.editCompany)).setEnabled(false);
            editAccount.setEnabled(false);
            editPayment.setEnabled(false);
            RadioButton isInstitution = (RadioButton) View.findViewById(R.id.isInstitution);
            ((RadioButton) View.findViewById(R.id.isOnline)).setEnabled(false);
            isInstitution.setEnabled(false);
        }
        this.layoutDrop3.addView(View);
    }

    @SuppressLint({"InflateParams"})
    private void addlayoutRent16() {
        View View = LayoutInflater.from(this).inflate(R.layout.layout_bills_to_pay, null);
        ((TextView) View.findViewById(R.id.rentLabel)).setText(getResources().getString(R.string.txt_rent16));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            EditText editAccount = (EditText) View.findViewById(R.id.editAccount);
            EditText editPayment = (EditText) View.findViewById(R.id.editPayment);
            ((EditText) View.findViewById(R.id.editCompany)).setEnabled(false);
            editAccount.setEnabled(false);
            editPayment.setEnabled(false);
            RadioButton isInstitution = (RadioButton) View.findViewById(R.id.isInstitution);
            ((RadioButton) View.findViewById(R.id.isOnline)).setEnabled(false);
            isInstitution.setEnabled(false);
        }
        this.layoutDrop3.addView(View);
    }

    @SuppressLint({"InflateParams"})
    private void addlayoutRent15() {
        View View = LayoutInflater.from(this).inflate(R.layout.layout_bills_to_pay, null);
        ((TextView) View.findViewById(R.id.rentLabel)).setText(getResources().getString(R.string.txt_rent15));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            EditText editAccount = (EditText) View.findViewById(R.id.editAccount);
            EditText editPayment = (EditText) View.findViewById(R.id.editPayment);
            ((EditText) View.findViewById(R.id.editCompany)).setEnabled(false);
            editAccount.setEnabled(false);
            editPayment.setEnabled(false);
            RadioButton isInstitution = (RadioButton) View.findViewById(R.id.isInstitution);
            ((RadioButton) View.findViewById(R.id.isOnline)).setEnabled(false);
            isInstitution.setEnabled(false);
        }
        this.layoutDrop3.addView(View);
    }

    @SuppressLint({"InflateParams"})
    private void addlayoutRent14() {
        View View = LayoutInflater.from(this).inflate(R.layout.layout_bills_to_pay, null);
        ((TextView) View.findViewById(R.id.rentLabel)).setText(getResources().getString(R.string.txt_rent14));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            EditText editAccount = (EditText) View.findViewById(R.id.editAccount);
            EditText editPayment = (EditText) View.findViewById(R.id.editPayment);
            ((EditText) View.findViewById(R.id.editCompany)).setEnabled(false);
            editAccount.setEnabled(false);
            editPayment.setEnabled(false);
            RadioButton isInstitution = (RadioButton) View.findViewById(R.id.isInstitution);
            ((RadioButton) View.findViewById(R.id.isOnline)).setEnabled(false);
            isInstitution.setEnabled(false);
        }
        this.layoutDrop3.addView(View);
    }

    @SuppressLint({"InflateParams"})
    private void addlayoutRent13() {
        View View = LayoutInflater.from(this).inflate(R.layout.layout_bills_to_pay, null);
        ((TextView) View.findViewById(R.id.rentLabel)).setText(getResources().getString(R.string.txt_rent13));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            EditText editAccount = (EditText) View.findViewById(R.id.editAccount);
            EditText editPayment = (EditText) View.findViewById(R.id.editPayment);
            ((EditText) View.findViewById(R.id.editCompany)).setEnabled(false);
            editAccount.setEnabled(false);
            editPayment.setEnabled(false);
            RadioButton isInstitution = (RadioButton) View.findViewById(R.id.isInstitution);
            ((RadioButton) View.findViewById(R.id.isOnline)).setEnabled(false);
            isInstitution.setEnabled(false);
        }
        this.layoutDrop3.addView(View);
    }

    @SuppressLint({"InflateParams"})
    private void addlayoutRent12() {
        View View = LayoutInflater.from(this).inflate(R.layout.layout_bills_to_pay, null);
        ((TextView) View.findViewById(R.id.rentLabel)).setText(getResources().getString(R.string.txt_rent12));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            EditText editAccount = (EditText) View.findViewById(R.id.editAccount);
            EditText editPayment = (EditText) View.findViewById(R.id.editPayment);
            ((EditText) View.findViewById(R.id.editCompany)).setEnabled(false);
            editAccount.setEnabled(false);
            editPayment.setEnabled(false);
            RadioButton isInstitution = (RadioButton) View.findViewById(R.id.isInstitution);
            ((RadioButton) View.findViewById(R.id.isOnline)).setEnabled(false);
            isInstitution.setEnabled(false);
        }
        this.layoutDrop2.addView(View);
    }

    @SuppressLint({"InflateParams"})
    private void addlayoutRent11() {
        View View = LayoutInflater.from(this).inflate(R.layout.layout_bills_to_pay, null);
        ((TextView) View.findViewById(R.id.rentLabel)).setText(getResources().getString(R.string.txt_rent11));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            EditText editAccount = (EditText) View.findViewById(R.id.editAccount);
            EditText editPayment = (EditText) View.findViewById(R.id.editPayment);
            ((EditText) View.findViewById(R.id.editCompany)).setEnabled(false);
            editAccount.setEnabled(false);
            editPayment.setEnabled(false);
            RadioButton isInstitution = (RadioButton) View.findViewById(R.id.isInstitution);
            ((RadioButton) View.findViewById(R.id.isOnline)).setEnabled(false);
            isInstitution.setEnabled(false);
        }
        this.layoutDrop2.addView(View);
    }

    @SuppressLint({"InflateParams"})
    private void addlayoutRent10() {
        View View = LayoutInflater.from(this).inflate(R.layout.layout_bills_to_pay, null);
        ((TextView) View.findViewById(R.id.rentLabel)).setText(getResources().getString(R.string.txt_rent10));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            EditText editAccount = (EditText) View.findViewById(R.id.editAccount);
            EditText editPayment = (EditText) View.findViewById(R.id.editPayment);
            ((EditText) View.findViewById(R.id.editCompany)).setEnabled(false);
            editAccount.setEnabled(false);
            editPayment.setEnabled(false);
            RadioButton isInstitution = (RadioButton) View.findViewById(R.id.isInstitution);
            ((RadioButton) View.findViewById(R.id.isOnline)).setEnabled(false);
            isInstitution.setEnabled(false);
        }
        this.layoutDrop2.addView(View);
    }

    @SuppressLint({"InflateParams"})
    private void addlayoutRent9() {
        View View1 = LayoutInflater.from(this).inflate(R.layout.layout_bills_to_pay, null);
        ((TextView) View1.findViewById(R.id.rentLabel)).setText(getResources().getString(R.string.txt_rent9));
        this.layoutDrop2.addView(View1);
        View View2 = LayoutInflater.from(this).inflate(R.layout.layout_bills_to_pay, null);
        ((TextView) View2.findViewById(R.id.rentLabel)).setText(getResources().getString(R.string.txt_rent9));
        this.layoutDrop2.addView(View2);
        View View3 = LayoutInflater.from(this).inflate(R.layout.layout_bills_to_pay, null);
        ((TextView) View3.findViewById(R.id.rentLabel)).setText(getResources().getString(R.string.txt_rent9));
        this.layoutDrop2.addView(View3);
        View View4 = LayoutInflater.from(this).inflate(R.layout.layout_bills_to_pay, null);
        ((TextView) View4.findViewById(R.id.rentLabel)).setText(getResources().getString(R.string.txt_rent9));
        this.layoutDrop2.addView(View4);
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            EditText editAccount1 = (EditText) View1.findViewById(R.id.editAccount);
            EditText editPayment1 = (EditText) View1.findViewById(R.id.editPayment);
            ((EditText) View1.findViewById(R.id.editCompany)).setEnabled(false);
            editAccount1.setEnabled(false);
            editPayment1.setEnabled(false);
            EditText editAccount2 = (EditText) View2.findViewById(R.id.editAccount);
            EditText editPayment2 = (EditText) View2.findViewById(R.id.editPayment);
            ((EditText) View2.findViewById(R.id.editCompany)).setEnabled(false);
            editAccount2.setEnabled(false);
            editPayment2.setEnabled(false);
            EditText editAccount3 = (EditText) View3.findViewById(R.id.editAccount);
            EditText editPayment3 = (EditText) View3.findViewById(R.id.editPayment);
            ((EditText) View3.findViewById(R.id.editCompany)).setEnabled(false);
            editAccount3.setEnabled(false);
            editPayment3.setEnabled(false);
            EditText editAccount4 = (EditText) View4.findViewById(R.id.editAccount);
            EditText editPayment4 = (EditText) View4.findViewById(R.id.editPayment);
            ((EditText) View4.findViewById(R.id.editCompany)).setEnabled(false);
            editAccount4.setEnabled(false);
            editPayment4.setEnabled(false);
            RadioButton isInstitution1 = (RadioButton) View1.findViewById(R.id.isInstitution);
            ((RadioButton) View1.findViewById(R.id.isOnline)).setEnabled(false);
            isInstitution1.setEnabled(false);
            RadioButton isInstitution2 = (RadioButton) View2.findViewById(R.id.isInstitution);
            ((RadioButton) View2.findViewById(R.id.isOnline)).setEnabled(false);
            isInstitution2.setEnabled(false);
            RadioButton isInstitution3 = (RadioButton) View3.findViewById(R.id.isInstitution);
            ((RadioButton) View3.findViewById(R.id.isOnline)).setEnabled(false);
            isInstitution3.setEnabled(false);
            RadioButton isInstitution4 = (RadioButton) View4.findViewById(R.id.isInstitution);
            ((RadioButton) View4.findViewById(R.id.isOnline)).setEnabled(false);
            isInstitution4.setEnabled(false);
        }
    }

    @SuppressLint({"InflateParams"})
    private void addlayoutRent8() {
        View View = LayoutInflater.from(this).inflate(R.layout.layout_bills_to_pay, null);
        ((TextView) View.findViewById(R.id.rentLabel)).setText(getResources().getString(R.string.txt_rent8));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            EditText editAccount = (EditText) View.findViewById(R.id.editAccount);
            EditText editPayment = (EditText) View.findViewById(R.id.editPayment);
            ((EditText) View.findViewById(R.id.editCompany)).setEnabled(false);
            editAccount.setEnabled(false);
            editPayment.setEnabled(false);
            RadioButton isInstitution = (RadioButton) View.findViewById(R.id.isInstitution);
            ((RadioButton) View.findViewById(R.id.isOnline)).setEnabled(false);
            isInstitution.setEnabled(false);
        }
        this.layoutDrop2.addView(View);
    }

    @SuppressLint({"InflateParams"})
    private void addlayoutRent7() {
        View View = LayoutInflater.from(this).inflate(R.layout.layout_bills_to_pay, null);
        ((TextView) View.findViewById(R.id.rentLabel)).setText(getResources().getString(R.string.txt_rent7));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            EditText editAccount = (EditText) View.findViewById(R.id.editAccount);
            EditText editPayment = (EditText) View.findViewById(R.id.editPayment);
            ((EditText) View.findViewById(R.id.editCompany)).setEnabled(false);
            editAccount.setEnabled(false);
            editPayment.setEnabled(false);
            RadioButton isInstitution = (RadioButton) View.findViewById(R.id.isInstitution);
            ((RadioButton) View.findViewById(R.id.isOnline)).setEnabled(false);
            isInstitution.setEnabled(false);
        }
        this.layoutDrop1.addView(View);
    }

    @SuppressLint({"InflateParams"})
    private void addlayoutRent6() {
        View View = LayoutInflater.from(this).inflate(R.layout.layout_bills_to_pay, null);
        ((TextView) View.findViewById(R.id.rentLabel)).setText(getResources().getString(R.string.txt_rent6));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            EditText editAccount = (EditText) View.findViewById(R.id.editAccount);
            EditText editPayment = (EditText) View.findViewById(R.id.editPayment);
            ((EditText) View.findViewById(R.id.editCompany)).setEnabled(false);
            editAccount.setEnabled(false);
            editPayment.setEnabled(false);
            RadioButton isInstitution = (RadioButton) View.findViewById(R.id.isInstitution);
            ((RadioButton) View.findViewById(R.id.isOnline)).setEnabled(false);
            isInstitution.setEnabled(false);
        }
        this.layoutDrop1.addView(View);
    }

    @SuppressLint({"InflateParams"})
    private void addlayoutRent5() {
        View View = LayoutInflater.from(this).inflate(R.layout.layout_bills_to_pay, null);
        ((TextView) View.findViewById(R.id.rentLabel)).setText(getResources().getString(R.string.txt_rent5));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            EditText editAccount = (EditText) View.findViewById(R.id.editAccount);
            EditText editPayment = (EditText) View.findViewById(R.id.editPayment);
            ((EditText) View.findViewById(R.id.editCompany)).setEnabled(false);
            editAccount.setEnabled(false);
            editPayment.setEnabled(false);
            RadioButton isInstitution = (RadioButton) View.findViewById(R.id.isInstitution);
            ((RadioButton) View.findViewById(R.id.isOnline)).setEnabled(false);
            isInstitution.setEnabled(false);
        }
        this.layoutDrop1.addView(View);
    }

    @SuppressLint({"InflateParams"})
    private void addlayoutRent4() {
        View View = LayoutInflater.from(this).inflate(R.layout.layout_bills_to_pay, null);
        ((TextView) View.findViewById(R.id.rentLabel)).setText(getResources().getString(R.string.txt_rent4));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            EditText editAccount = (EditText) View.findViewById(R.id.editAccount);
            EditText editPayment = (EditText) View.findViewById(R.id.editPayment);
            ((EditText) View.findViewById(R.id.editCompany)).setEnabled(false);
            editAccount.setEnabled(false);
            editPayment.setEnabled(false);
            RadioButton isInstitution = (RadioButton) View.findViewById(R.id.isInstitution);
            ((RadioButton) View.findViewById(R.id.isOnline)).setEnabled(false);
            isInstitution.setEnabled(false);
        }
        this.layoutDrop1.addView(View);
    }

    @SuppressLint({"InflateParams"})
    private void addlayoutRent3() {
        View View = LayoutInflater.from(this).inflate(R.layout.layout_bills_to_pay, null);
        ((TextView) View.findViewById(R.id.rentLabel)).setText(getResources().getString(R.string.txt_rent3));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            EditText editAccount = (EditText) View.findViewById(R.id.editAccount);
            EditText editPayment = (EditText) View.findViewById(R.id.editPayment);
            ((EditText) View.findViewById(R.id.editCompany)).setEnabled(false);
            editAccount.setEnabled(false);
            editPayment.setEnabled(false);
            RadioButton isInstitution = (RadioButton) View.findViewById(R.id.isInstitution);
            ((RadioButton) View.findViewById(R.id.isOnline)).setEnabled(false);
            isInstitution.setEnabled(false);
        }
        this.layoutDrop1.addView(View);
    }

    @SuppressLint({"InflateParams"})
    private void addlayoutRent2() {
        View View = LayoutInflater.from(this).inflate(R.layout.layout_bills_to_pay, null);
        ((TextView) View.findViewById(R.id.rentLabel)).setText(getResources().getString(R.string.txt_rent2));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            EditText editAccount = (EditText) View.findViewById(R.id.editAccount);
            EditText editPayment = (EditText) View.findViewById(R.id.editPayment);
            ((EditText) View.findViewById(R.id.editCompany)).setEnabled(false);
            editAccount.setEnabled(false);
            editPayment.setEnabled(false);
            RadioButton isInstitution = (RadioButton) View.findViewById(R.id.isInstitution);
            ((RadioButton) View.findViewById(R.id.isOnline)).setEnabled(false);
            isInstitution.setEnabled(false);
        }
        this.layoutDrop1.addView(View);
    }

    @SuppressLint({"InflateParams"})
    private void addlayoutRent1() {
        try {
            View View = LayoutInflater.from(this).inflate(R.layout.layout_bills_to_pay, null);
            ((TextView) View.findViewById(R.id.rentLabel)).setText(getResources().getString(R.string.txt_rent1));
            if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                EditText editAccount = (EditText) View.findViewById(R.id.editAccount);
                EditText editPayment = (EditText) View.findViewById(R.id.editPayment);
                ((EditText) View.findViewById(R.id.editCompany)).setEnabled(false);
                editAccount.setEnabled(false);
                editPayment.setEnabled(false);
                RadioButton isInstitution = (RadioButton) View.findViewById(R.id.isInstitution);
                ((RadioButton) View.findViewById(R.id.isOnline)).setEnabled(false);
                isInstitution.setEnabled(false);
            }
            this.layoutDrop1.addView(View);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save /*2131558589*/:
                prePareJsonDrop1();
                prePareJsonDrop2();
                prePareJsonDrop3();
                prePareSendJson();
                return;
            case R.id.btn_back /*2131558590*/:
                finish();
                return;
            case R.id.layoutOpenDrop1 /*2131558890*/:
                if (this.openDrop1.getVisibility() == View.VISIBLE) {
                    this.openDrop1.setVisibility(View.GONE);
                    this.closeDrop1.setVisibility(View.VISIBLE);
                    this.layoutDrop1.setVisibility(View.GONE);
                    return;
                }
                this.closeDrop1.setVisibility(View.GONE);
                this.openDrop1.setVisibility(View.VISIBLE);
                this.layoutDrop1.setVisibility(View.VISIBLE);
                return;
            case R.id.openDrop1 /*2131558891*/:
                if (this.openDrop1.getVisibility() == View.VISIBLE) {
                    this.openDrop1.setVisibility(View.GONE);
                    this.closeDrop1.setVisibility(View.VISIBLE);
                    this.layoutDrop1.setVisibility(View.GONE);
                    return;
                }
                return;
            case R.id.closeDrop1 /*2131558892*/:
                if (this.closeDrop1.getVisibility() == View.VISIBLE) {
                    this.closeDrop1.setVisibility(View.GONE);
                    this.openDrop1.setVisibility(View.VISIBLE);
                    this.layoutDrop1.setVisibility(View.VISIBLE);
                    return;
                }
                return;
            case R.id.layoutOpenDrop2 /*2131558894*/:
                if (this.openDrop2.getVisibility() == View.VISIBLE) {
                    this.openDrop2.setVisibility(View.GONE);
                    this.closeDrop2.setVisibility(View.VISIBLE);
                    this.layoutDrop2.setVisibility(View.GONE);
                    return;
                }
                this.closeDrop2.setVisibility(View.GONE);
                this.openDrop2.setVisibility(View.VISIBLE);
                this.layoutDrop2.setVisibility(View.VISIBLE);
                return;
            case R.id.openDrop2 /*2131558895*/:
                if (this.openDrop2.getVisibility() == View.VISIBLE) {
                    this.openDrop2.setVisibility(View.GONE);
                    this.closeDrop2.setVisibility(View.VISIBLE);
                    this.layoutDrop2.setVisibility(View.GONE);
                    return;
                }
                return;
            case R.id.closeDrop2 /*2131558896*/:
                if (this.closeDrop2.getVisibility() == View.VISIBLE) {
                    this.closeDrop2.setVisibility(View.GONE);
                    this.openDrop2.setVisibility(View.VISIBLE);
                    this.layoutDrop2.setVisibility(View.VISIBLE);
                    return;
                }
                return;
            case R.id.layoutOpenDrop3 /*2131558898*/:
                if (this.openDrop3.getVisibility() == View.VISIBLE) {
                    this.openDrop3.setVisibility(View.GONE);
                    this.closeDrop3.setVisibility(View.VISIBLE);
                    this.layoutDrop3.setVisibility(View.GONE);
                    return;
                }
                this.closeDrop3.setVisibility(View.GONE);
                this.openDrop3.setVisibility(View.VISIBLE);
                this.layoutDrop3.setVisibility(View.VISIBLE);
                return;
            case R.id.openDrop3 /*2131558899*/:
                if (this.openDrop3.getVisibility() == View.VISIBLE) {
                    this.openDrop3.setVisibility(View.GONE);
                    this.closeDrop3.setVisibility(View.VISIBLE);
                    this.layoutDrop3.setVisibility(View.GONE);
                    return;
                }
                return;
            case R.id.closeDrop3 /*2131558900*/:
                if (this.closeDrop3.getVisibility() == View.VISIBLE) {
                    this.closeDrop3.setVisibility(View.GONE);
                    this.openDrop3.setVisibility(View.VISIBLE);
                    this.layoutDrop3.setVisibility(View.VISIBLE);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void prePareJsonDrop1() {
        try {
            this.b_data = new JSONArray();
            for (int i = 0; i < this.layoutDrop1.getChildCount(); i++) {
                JSONObject jsonbj = new JSONObject();
                ViewGroup view = (ViewGroup) this.layoutDrop1.getChildAt(i);
                TextView rentLabel = (TextView) view.findViewById(R.id.rentLabel);
                EditText editCompany = (EditText) view.findViewById(R.id.editCompany);
                EditText editAccount = (EditText) view.findViewById(R.id.editAccount);
                EditText editPayment = (EditText) view.findViewById(R.id.editPayment);
                RadioButton isOnline = (RadioButton) view.findViewById(R.id.isOnline);
                jsonbj.put("bills_id", ((EditText) view.findViewById(R.id.editId)).getText().toString().trim());
                jsonbj.put("rent", rentLabel.getText().toString().trim());
                jsonbj.put("company", editCompany.getText().toString().trim());
                jsonbj.put("account", editAccount.getText().toString().trim());
                jsonbj.put("payment", editPayment.getText().toString().trim());
                jsonbj.put("drop_down", "1");
                if (isOnline.isChecked()) {
                    jsonbj.put("paid_at", "1");
                } else {
                    jsonbj.put("paid_at", "0");
                }
                this.b_data.put(jsonbj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prePareJsonDrop2() {
        int i = 0;
        while (i < this.layoutDrop2.getChildCount()) {
            try {
                JSONObject jsonbj = new JSONObject();
                ViewGroup view = (ViewGroup) this.layoutDrop2.getChildAt(i);
                TextView rentLabel = (TextView) view.findViewById(R.id.rentLabel);
                EditText editCompany = (EditText) view.findViewById(R.id.editCompany);
                EditText editAccount = (EditText) view.findViewById(R.id.editAccount);
                EditText editPayment = (EditText) view.findViewById(R.id.editPayment);
                RadioButton isOnline = (RadioButton) view.findViewById(R.id.isOnline);
                jsonbj.put("bills_id", ((EditText) view.findViewById(R.id.editId)).getText().toString().trim());
                jsonbj.put("rent", rentLabel.getText().toString().trim());
                jsonbj.put("company", editCompany.getText().toString().trim());
                jsonbj.put("account", editAccount.getText().toString().trim());
                jsonbj.put("payment", editPayment.getText().toString().trim());
                jsonbj.put("drop_down", "2");
                if (isOnline.isChecked()) {
                    jsonbj.put("paid_at", "1");
                } else {
                    jsonbj.put("paid_at", "0");
                }
                this.b_data.put(jsonbj);
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    private void prePareJsonDrop3() {
        int i = 0;
        while (i < this.layoutDrop3.getChildCount()) {
            try {
                JSONObject jsonbj = new JSONObject();
                ViewGroup view = (ViewGroup) this.layoutDrop3.getChildAt(i);
                TextView rentLabel = (TextView) view.findViewById(R.id.rentLabel);
                EditText editCompany = (EditText) view.findViewById(R.id.editCompany);
                EditText editAccount = (EditText) view.findViewById(R.id.editAccount);
                EditText editPayment = (EditText) view.findViewById(R.id.editPayment);
                RadioButton isOnline = (RadioButton) view.findViewById(R.id.isOnline);
                jsonbj.put("bills_id", ((EditText) view.findViewById(R.id.editId)).getText().toString().trim());
                jsonbj.put("rent", rentLabel.getText().toString().trim());
                jsonbj.put("company", editCompany.getText().toString().trim());
                jsonbj.put("account", editAccount.getText().toString().trim());
                jsonbj.put("payment", editPayment.getText().toString().trim());
                jsonbj.put("drop_down", "3");
                if (isOnline.isChecked()) {
                    jsonbj.put("paid_at", "1");
                } else {
                    jsonbj.put("paid_at", "0");
                }
                this.b_data.put(jsonbj);
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    private void prePareSendJson() {
        try {
            JSONObject json = new JSONObject();
            json.put("user_id", this.pref.getStringValue(Constant.user_id, ""));
            json.put("b_data", this.b_data);
            JSONObject sendJson = new JSONObject();
            sendJson.put("bills_to_pay_data", json);
            Log.i("send json obj :", sendJson.toString());
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            entity.addPart("json_data", new StringBody(sendJson.toString()));
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("json_data", sendJson.toString()));
            if (!this.connection.isConnectingToInternet()) {
                displayMessage(getResources().getString(R.string.connectionFailMessage));
            } else if (this.btn_save.getText().toString().trim().equalsIgnoreCase("edit")) {
                new SaveProfileAsytask(this, ServiceUrl.edit_bill_to_pay, nameValuePairs).execute(new Void[0]);
            } else {
                new SaveProfileAsytask(this, ServiceUrl.save_bill_to_pay, nameValuePairs).execute(new Void[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void on_GeneralSuccess(JSONObject response, int responseCode) {
        try {
            JSONObject josnArray1 = response.getJSONObject("bills_to_pay");
            JSONArray josnArray = josnArray1.getJSONArray("bills_to_pay");
            for (int i = 0; i < josnArray.length(); i++) {
                show_BillsToPay(josnArray.getJSONObject(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint({"InflateParams"})
    private void show_BillsToPay(JSONObject response) {
        try {
            View View = LayoutInflater.from(this).inflate(R.layout.layout_bills_to_pay, null);
            ((TextView) View.findViewById(R.id.rentLabel)).setText(response.getString("rent"));
            EditText editCompany = (EditText) View.findViewById(R.id.editCompany);
            EditText editAccount = (EditText) View.findViewById(R.id.editAccount);
            EditText editPayment = (EditText) View.findViewById(R.id.editPayment);
            ((EditText) View.findViewById(R.id.editId)).setText(response.getString("bills_id"));
            editCompany.setText(response.getString("company"));
            editAccount.setText(response.getString("account"));
            editPayment.setText(response.getString("payment"));
            RadioButton isOnline = (RadioButton) View.findViewById(R.id.isOnline);
            RadioButton isInstitution = (RadioButton) View.findViewById(R.id.isInstitution);
            if (response.getString("paid_at").toString().trim().equalsIgnoreCase("1")) {
                isOnline.setChecked(true);
                isInstitution.setChecked(false);
            } else {
                isOnline.setChecked(false);
                isInstitution.setChecked(true);
            }
            if (response.getString("drop_down").toString().trim().equalsIgnoreCase("1")) {
                this.layoutDrop1.addView(View);
            }
            if (response.getString("drop_down").toString().trim().equalsIgnoreCase("2")) {
                this.layoutDrop2.addView(View);
            }
            if (response.getString("drop_down").toString().trim().equalsIgnoreCase("3")) {
                this.layoutDrop3.addView(View);
            }
            if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                isOnline.setEnabled(false);
                isInstitution.setEnabled(false);
                editCompany.setEnabled(false);
                editAccount.setEnabled(false);
                editPayment.setEnabled(false);
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
                this.pref.setStringValue(Constant.billToPay, response.getString("success").toString());
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}