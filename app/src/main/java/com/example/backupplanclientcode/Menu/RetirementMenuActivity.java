package com.example.backupplanclientcode.Menu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class RetirementMenuActivity extends Activity implements OnClickListener, ResponseListerProfile, ResponseListener_General, TextWatcher, LogOutTimerUtil.LogOutListener {
    TextView actionBarTittle;
    Button btn_back;
    Button btn_save;
    ConnectionDetector connection;
    String delete_retirement = "";
    TextView editTotal;
    LinearLayout layout_retirement;
    SettingPreference pref;
    int total = 0;
    ArrayList<EditText> totalIncome;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_retirement);
        this.pref = new SettingPreference(getApplicationContext());
        this.connection = new ConnectionDetector(getApplicationContext());
        new Bugsense().startBugsense(getApplicationContext());
        findViewid();
        checkAlredySaveAccount();
    }

    private void checkAlredySaveAccount() {
        if (this.pref.getStringValue(Constant.RetirementFlag, "").equalsIgnoreCase("1")) {
        this.btn_save.setText("Edit");
        if (!this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            this.actionBarTittle.setText("Edit " + getResources().getString(R.string.menu_Retirement));
        }
        if (this.connection.isConnectingToInternet()) {
            try {
                JSONObject nameValuePair = new JSONObject();
                nameValuePair.put("user_id", this.pref.getStringValue(Constant.user_id, ""));
                nameValuePair.put("token", this.pref.getStringValue(Constant.jwttoken, ""));
                new GeneralTask(this, ServiceUrl.get_retirement_detail, nameValuePair, 2, "post").execute(new Void[0]);
            } catch (Exception e) {
            }
            return;
        }
        displayMessage(getResources().getString(R.string.connectionFailMessage));
        show_all_layouts();
        return;
        }
        this.actionBarTittle.setText(getResources().getString(R.string.menu_Retirement));
        show_all_layouts();
    }

    private void show_all_layouts() {
        addIncome1();
        addIncome2();
        addIncome3();
        addIncome4();
        addIncome5();
        addIncome6();
        addIncome7();
        addIncome8();
        addIncome9();
        addIncome10();
        addIncome11();
        addIncome12();
        addIncome13();
        addIncome14();
        addIncome15();
        addIncome16();
        addIncome17();
        addIncome18();
        addIncome19();
        addIncome20();
        addIncome21();
    }

    private void findViewid() {
        this.btn_save = (Button) findViewById(R.id.btn_save);
        this.btn_back = (Button) findViewById(R.id.btn_back);
        this.actionBarTittle = (TextView) findViewById(R.id.actionBarTittle);
        this.btn_back.setOnClickListener(this);
        this.btn_save.setOnClickListener(this);
        this.layout_retirement = (LinearLayout) findViewById(R.id.layout_retirement);
        this.editTotal = (TextView) findViewById(R.id.editTotal);
        this.totalIncome = new ArrayList<>();
        this.totalIncome.clear();
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            setEnableControl();
        }
    }

    private void setEnableControl() {
        this.actionBarTittle.setText(getResources().getString(R.string.menu_Retirement));
        this.btn_save.setVisibility(View.GONE);
    }

    @SuppressLint({"InflateParams"})
    private void addIncome1() {
        View income1 = LayoutInflater.from(this).inflate(R.layout.layout_retirement, null);
        TextView lbl_retirement = (TextView) income1.findViewById(R.id.lbl_retirement);
        EditText edit_income = (EditText) income1.findViewById(R.id.edit_income);
        edit_income.addTextChangedListener(this);
        this.totalIncome.add(edit_income);
        EditText edit_desc = (EditText) income1.findViewById(R.id.edit_desc);
        lbl_retirement.setText(getResources().getString(R.string.INC1));
        edit_income.setHint(getResources().getString(R.string.hint_month_income));
        edit_desc.setHint(getResources().getString(R.string.hint_desc));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            edit_income.setEnabled(false);
            edit_desc.setEnabled(false);
        }
        this.layout_retirement.addView(income1);
    }

    @SuppressLint({"InflateParams"})
    private void addIncome2() {
        View income1 = LayoutInflater.from(this).inflate(R.layout.layout_retirement, null);
        TextView lbl_retirement = (TextView) income1.findViewById(R.id.lbl_retirement);
        EditText edit_income = (EditText) income1.findViewById(R.id.edit_income);
        edit_income.addTextChangedListener(this);
        this.totalIncome.add(edit_income);
        EditText edit_desc = (EditText) income1.findViewById(R.id.edit_desc);
        lbl_retirement.setText(getResources().getString(R.string.INC2));
        edit_income.setHint(getResources().getString(R.string.hint_month_income));
        edit_desc.setHint(getResources().getString(R.string.hint_desc));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            edit_income.setEnabled(false);
            edit_desc.setEnabled(false);
        }
        this.layout_retirement.addView(income1);
    }

    @SuppressLint({"InflateParams"})
    private void addIncome3() {
        View income1 = LayoutInflater.from(this).inflate(R.layout.layout_retirement, null);
        TextView lbl_retirement = (TextView) income1.findViewById(R.id.lbl_retirement);
        EditText edit_income = (EditText) income1.findViewById(R.id.edit_income);
        edit_income.addTextChangedListener(this);
        this.totalIncome.add(edit_income);
        EditText edit_desc = (EditText) income1.findViewById(R.id.edit_desc);
        lbl_retirement.setText(getResources().getString(R.string.INC3));
        edit_income.setHint(getResources().getString(R.string.hint_month_income));
        edit_desc.setHint(getResources().getString(R.string.hint_desc));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            edit_income.setEnabled(false);
            edit_desc.setEnabled(false);
        }
        this.layout_retirement.addView(income1);
    }

    @SuppressLint({"InflateParams"})
    private void addIncome4() {
        View income1 = LayoutInflater.from(this).inflate(R.layout.layout_retirement, null);
        TextView lbl_retirement = (TextView) income1.findViewById(R.id.lbl_retirement);
        EditText edit_income = (EditText) income1.findViewById(R.id.edit_income);
        edit_income.addTextChangedListener(this);
        this.totalIncome.add(edit_income);
        EditText edit_desc = (EditText) income1.findViewById(R.id.edit_desc);
        lbl_retirement.setText(getResources().getString(R.string.INC4));
        edit_income.setHint(getResources().getString(R.string.hint_month_income));
        edit_desc.setHint(getResources().getString(R.string.hint_desc));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            edit_income.setEnabled(false);
            edit_desc.setEnabled(false);
        }
        this.layout_retirement.addView(income1);
    }

    @SuppressLint({"InflateParams"})
    private void addIncome5() {
        View income1 = LayoutInflater.from(this).inflate(R.layout.layout_retirement, null);
        TextView lbl_retirement = (TextView) income1.findViewById(R.id.lbl_retirement);
        EditText edit_income = (EditText) income1.findViewById(R.id.edit_income);
        edit_income.addTextChangedListener(this);
        this.totalIncome.add(edit_income);
        EditText edit_desc = (EditText) income1.findViewById(R.id.edit_desc);
        lbl_retirement.setText(getResources().getString(R.string.INC5));
        edit_income.setHint(getResources().getString(R.string.hint_month_income));
        edit_desc.setHint(getResources().getString(R.string.hint_desc));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            edit_income.setEnabled(false);
            edit_desc.setEnabled(false);
        }
        this.layout_retirement.addView(income1);
    }

    @SuppressLint({"InflateParams"})
    private void addIncome6() {
        View income1 = LayoutInflater.from(this).inflate(R.layout.layout_retirement, null);
        TextView lbl_retirement = (TextView) income1.findViewById(R.id.lbl_retirement);
        EditText edit_income = (EditText) income1.findViewById(R.id.edit_income);
        edit_income.addTextChangedListener(this);
        this.totalIncome.add(edit_income);
        EditText edit_desc = (EditText) income1.findViewById(R.id.edit_desc);
        lbl_retirement.setText(getResources().getString(R.string.INC6));
        edit_income.setHint(getResources().getString(R.string.hint_month_income));
        edit_desc.setHint(getResources().getString(R.string.hint_desc));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            edit_income.setEnabled(false);
            edit_desc.setEnabled(false);
        }
        this.layout_retirement.addView(income1);
    }

    @SuppressLint({"InflateParams"})
    private void addIncome7() {
        View income1 = LayoutInflater.from(this).inflate(R.layout.layout_retirement, null);
        TextView lbl_retirement = (TextView) income1.findViewById(R.id.lbl_retirement);
        EditText edit_income = (EditText) income1.findViewById(R.id.edit_income);
        edit_income.addTextChangedListener(this);
        this.totalIncome.add(edit_income);
        EditText edit_desc = (EditText) income1.findViewById(R.id.edit_desc);
        lbl_retirement.setText(getResources().getString(R.string.INC7));
        edit_income.setHint(getResources().getString(R.string.hint_month_income));
        edit_desc.setHint(getResources().getString(R.string.hint_desc));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            edit_income.setEnabled(false);
            edit_desc.setEnabled(false);
        }
        this.layout_retirement.addView(income1);
    }

    @SuppressLint({"InflateParams"})
    private void addIncome8() {
        View income1 = LayoutInflater.from(this).inflate(R.layout.layout_retirement, null);
        TextView lbl_retirement = (TextView) income1.findViewById(R.id.lbl_retirement);
        EditText edit_income = (EditText) income1.findViewById(R.id.edit_income);
        edit_income.addTextChangedListener(this);
        this.totalIncome.add(edit_income);
        EditText edit_desc = (EditText) income1.findViewById(R.id.edit_desc);
        lbl_retirement.setText(getResources().getString(R.string.INC8));
        edit_income.setHint(getResources().getString(R.string.hint_month_income));
        edit_desc.setHint(getResources().getString(R.string.hint_desc));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            edit_income.setEnabled(false);
            edit_desc.setEnabled(false);
        }
        this.layout_retirement.addView(income1);
    }

    @SuppressLint({"InflateParams"})
    private void addIncome9() {
        View income1 = LayoutInflater.from(this).inflate(R.layout.layout_retirement, null);
        TextView lbl_retirement = (TextView) income1.findViewById(R.id.lbl_retirement);
        EditText edit_income = (EditText) income1.findViewById(R.id.edit_income);
        edit_income.addTextChangedListener(this);
        this.totalIncome.add(edit_income);
        EditText edit_desc = (EditText) income1.findViewById(R.id.edit_desc);
        lbl_retirement.setText(getResources().getString(R.string.INC9));
        edit_income.setHint(getResources().getString(R.string.hint_month_income));
        edit_desc.setHint(getResources().getString(R.string.hint_desc));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            edit_income.setEnabled(false);
            edit_desc.setEnabled(false);
        }
        this.layout_retirement.addView(income1);
    }

    @SuppressLint({"InflateParams"})
    private void addIncome10() {
        View income1 = LayoutInflater.from(this).inflate(R.layout.layout_retirement, null);
        TextView lbl_retirement = (TextView) income1.findViewById(R.id.lbl_retirement);
        EditText edit_income = (EditText) income1.findViewById(R.id.edit_income);
        edit_income.addTextChangedListener(this);
        this.totalIncome.add(edit_income);
        EditText edit_desc = (EditText) income1.findViewById(R.id.edit_desc);
        lbl_retirement.setText(getResources().getString(R.string.INC10));
        edit_income.setHint(getResources().getString(R.string.hint_month_income));
        edit_desc.setHint(getResources().getString(R.string.hint_desc));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            edit_income.setEnabled(false);
            edit_desc.setEnabled(false);
        }
        this.layout_retirement.addView(income1);
    }

    @SuppressLint({"InflateParams"})
    private void addIncome11() {
        View income1 = LayoutInflater.from(this).inflate(R.layout.layout_retirement, null);
        TextView lbl_retirement = (TextView) income1.findViewById(R.id.lbl_retirement);
        EditText edit_income = (EditText) income1.findViewById(R.id.edit_income);
        edit_income.addTextChangedListener(this);
        this.totalIncome.add(edit_income);
        EditText edit_desc = (EditText) income1.findViewById(R.id.edit_desc);
        lbl_retirement.setText(getResources().getString(R.string.INC11));
        edit_income.setHint(getResources().getString(R.string.hint_month_income));
        edit_desc.setHint(getResources().getString(R.string.hint_desc));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            edit_income.setEnabled(false);
            edit_desc.setEnabled(false);
        }
        this.layout_retirement.addView(income1);
    }

    @SuppressLint({"InflateParams"})
    private void addIncome12() {
        View income1 = LayoutInflater.from(this).inflate(R.layout.layout_retirement, null);
        TextView lbl_retirement = (TextView) income1.findViewById(R.id.lbl_retirement);
        EditText edit_income = (EditText) income1.findViewById(R.id.edit_income);
        edit_income.addTextChangedListener(this);
        this.totalIncome.add(edit_income);
        EditText edit_desc = (EditText) income1.findViewById(R.id.edit_desc);
        lbl_retirement.setText(getResources().getString(R.string.INC12));
        edit_income.setHint(getResources().getString(R.string.hint_month_income));
        edit_desc.setHint(getResources().getString(R.string.hint_desc));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            edit_income.setEnabled(false);
            edit_desc.setEnabled(false);
        }
        this.layout_retirement.addView(income1);
    }

    @SuppressLint({"InflateParams"})
    private void addIncome13() {
        View income1 = LayoutInflater.from(this).inflate(R.layout.layout_retirement, null);
        TextView lbl_retirement = (TextView) income1.findViewById(R.id.lbl_retirement);
        EditText edit_income = (EditText) income1.findViewById(R.id.edit_income);
        edit_income.addTextChangedListener(this);
        this.totalIncome.add(edit_income);
        EditText edit_desc = (EditText) income1.findViewById(R.id.edit_desc);
        lbl_retirement.setText(getResources().getString(R.string.INC13));
        edit_income.setHint(getResources().getString(R.string.hint_month_income));
        edit_desc.setHint(getResources().getString(R.string.hint_desc));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            edit_income.setEnabled(false);
            edit_desc.setEnabled(false);
        }
        this.layout_retirement.addView(income1);
    }

    @SuppressLint({"InflateParams"})
    private void addIncome14() {
        View income1 = LayoutInflater.from(this).inflate(R.layout.layout_retirement, null);
        TextView lbl_retirement = (TextView) income1.findViewById(R.id.lbl_retirement);
        EditText edit_income = (EditText) income1.findViewById(R.id.edit_income);
        edit_income.addTextChangedListener(this);
        this.totalIncome.add(edit_income);
        EditText edit_desc = (EditText) income1.findViewById(R.id.edit_desc);
        lbl_retirement.setText(getResources().getString(R.string.INC14));
        edit_income.setHint(getResources().getString(R.string.hint_month_income));
        edit_desc.setHint(getResources().getString(R.string.hint_desc));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            edit_income.setEnabled(false);
            edit_desc.setEnabled(false);
        }
        this.layout_retirement.addView(income1);
    }

    @SuppressLint({"InflateParams"})
    private void addIncome15() {
        View income1 = LayoutInflater.from(this).inflate(R.layout.layout_retirement, null);
        TextView lbl_retirement = (TextView) income1.findViewById(R.id.lbl_retirement);
        EditText edit_income = (EditText) income1.findViewById(R.id.edit_income);
        edit_income.addTextChangedListener(this);
        this.totalIncome.add(edit_income);
        EditText edit_desc = (EditText) income1.findViewById(R.id.edit_desc);
        lbl_retirement.setText(getResources().getString(R.string.INC15));
        edit_income.setHint(getResources().getString(R.string.hint_month_income));
        edit_desc.setHint(getResources().getString(R.string.hint_desc));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            edit_income.setEnabled(false);
            edit_desc.setEnabled(false);
        }
        this.layout_retirement.addView(income1);
    }

    @SuppressLint({"InflateParams"})
    private void addIncome16() {
        View income1 = LayoutInflater.from(this).inflate(R.layout.layout_retirement, null);
        TextView lbl_retirement = (TextView) income1.findViewById(R.id.lbl_retirement);
        EditText edit_income = (EditText) income1.findViewById(R.id.edit_income);
        edit_income.addTextChangedListener(this);
        this.totalIncome.add(edit_income);
        EditText edit_desc = (EditText) income1.findViewById(R.id.edit_desc);
        lbl_retirement.setText(getResources().getString(R.string.INC16));
        edit_income.setHint(getResources().getString(R.string.hint_month_income));
        edit_desc.setHint(getResources().getString(R.string.hint_desc));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            edit_income.setEnabled(false);
            edit_desc.setEnabled(false);
        }
        this.layout_retirement.addView(income1);
    }

    @SuppressLint({"InflateParams"})
    private void addIncome17() {
        View income1 = LayoutInflater.from(this).inflate(R.layout.layout_retirement, null);
        TextView lbl_retirement = (TextView) income1.findViewById(R.id.lbl_retirement);
        EditText edit_income = (EditText) income1.findViewById(R.id.edit_income);
        edit_income.addTextChangedListener(this);
        this.totalIncome.add(edit_income);
        EditText edit_desc = (EditText) income1.findViewById(R.id.edit_desc);
        lbl_retirement.setText(getResources().getString(R.string.INC17));
        edit_income.setHint(getResources().getString(R.string.hint_month_income));
        edit_desc.setHint(getResources().getString(R.string.hint_desc));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            edit_income.setEnabled(false);
            edit_desc.setEnabled(false);
        }
        this.layout_retirement.addView(income1);
    }

    @SuppressLint({"InflateParams"})
    private void addIncome18() {
        View income1 = LayoutInflater.from(this).inflate(R.layout.layout_retirement, null);
        TextView lbl_retirement = (TextView) income1.findViewById(R.id.lbl_retirement);
        EditText edit_income = (EditText) income1.findViewById(R.id.edit_income);
        edit_income.addTextChangedListener(this);
        this.totalIncome.add(edit_income);
        EditText edit_desc = (EditText) income1.findViewById(R.id.edit_desc);
        lbl_retirement.setText(getResources().getString(R.string.INC18));
        edit_income.setHint(getResources().getString(R.string.hint_month_income));
        edit_desc.setHint(getResources().getString(R.string.hint_desc));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            edit_income.setEnabled(false);
            edit_desc.setEnabled(false);
        }
        this.layout_retirement.addView(income1);
    }

    @SuppressLint({"InflateParams"})
    private void addIncome19() {
        View income1 = LayoutInflater.from(this).inflate(R.layout.layout_retirement, null);
        TextView lbl_retirement = (TextView) income1.findViewById(R.id.lbl_retirement);
        EditText edit_income = (EditText) income1.findViewById(R.id.edit_income);
        edit_income.addTextChangedListener(this);
        this.totalIncome.add(edit_income);
        EditText edit_desc = (EditText) income1.findViewById(R.id.edit_desc);
        lbl_retirement.setText(getResources().getString(R.string.INC19));
        edit_income.setHint(getResources().getString(R.string.hint_month_income));
        edit_desc.setHint(getResources().getString(R.string.hint_desc));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            edit_income.setEnabled(false);
            edit_desc.setEnabled(false);
        }
        this.layout_retirement.addView(income1);
    }

    @SuppressLint({"InflateParams"})
    private void addIncome20() {
        View income1 = LayoutInflater.from(this).inflate(R.layout.layout_retirement, null);
        TextView lbl_retirement = (TextView) income1.findViewById(R.id.lbl_retirement);
        EditText edit_income = (EditText) income1.findViewById(R.id.edit_income);
        edit_income.addTextChangedListener(this);
        this.totalIncome.add(edit_income);
        EditText edit_desc = (EditText) income1.findViewById(R.id.edit_desc);
        lbl_retirement.setText(getResources().getString(R.string.INC20));
        edit_income.setHint(getResources().getString(R.string.hint_month_income));
        edit_desc.setHint(getResources().getString(R.string.hint_desc));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            edit_income.setEnabled(false);
            edit_desc.setEnabled(false);
        }
        this.layout_retirement.addView(income1);
    }

    @SuppressLint({"InflateParams"})
    private void addIncome21() {
        View income1 = LayoutInflater.from(this).inflate(R.layout.layout_retirement, null);
        TextView lbl_retirement = (TextView) income1.findViewById(R.id.lbl_retirement);
        EditText edit_income = (EditText) income1.findViewById(R.id.edit_income);
        edit_income.addTextChangedListener(this);
        this.totalIncome.add(edit_income);
        EditText edit_desc = (EditText) income1.findViewById(R.id.edit_desc);
        lbl_retirement.setText(getResources().getString(R.string.INC21));
        edit_income.setHint(getResources().getString(R.string.hint_month_income));
        edit_desc.setHint(getResources().getString(R.string.hint_desc));
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            edit_income.setEnabled(false);
            edit_desc.setEnabled(false);
        }
        this.layout_retirement.addView(income1);
    }

    private void prepareJson() {
        try {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < this.layout_retirement.getChildCount(); i++) {
                JSONObject jsonbj = new JSONObject();
                ViewGroup view = (ViewGroup) this.layout_retirement.getChildAt(i);
                EditText edit_retirement_id = (EditText) view.findViewById(R.id.edit_retirement_id);
                EditText edit_income = (EditText) view.findViewById(R.id.edit_income);
                EditText edit_desc = (EditText) view.findViewById(R.id.edit_desc);
                jsonbj.put("income", ((TextView) view.findViewById(R.id.lbl_retirement)).getText().toString().trim());
                jsonbj.put("monthly_income", edit_income.getText().toString().trim());
                jsonbj.put("descritption", edit_desc.getText().toString().trim());
                jsonbj.put("retirement_id", edit_retirement_id.getText().toString().trim());
                jsonArray.put(jsonbj);
            }
            JSONObject r_data = new JSONObject();
            r_data.put("user_id", this.pref.getStringValue(Constant.user_id, ""));
            r_data.put("delete_retirement", this.delete_retirement);
            r_data.put("r_data", jsonArray);
            JSONObject retirement_data = new JSONObject();
            retirement_data.put("retirement_data", r_data);
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            entity.addPart("json_data", new StringBody(retirement_data.toString()));
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("json_data", retirement_data.toString()));
            if (!this.connection.isConnectingToInternet()) {
                displayMessage(getResources().getString(R.string.connectionFailMessage));
            } else if (this.btn_save.getText().toString().trim().equalsIgnoreCase("edit")) {
                new SaveProfileAsytask(this, ServiceUrl.edit_retirement, entity).execute(new Void[0]);
                /*JSONObject nameValuePair = new JSONObject();
                nameValuePair.put("json_data", retirement_data);
//                nameValuePair.put("a_photo[]", photo_array);
                nameValuePair.put("token", this.pref.getStringValue(Constant.jwttoken, ""));
                new GeneralTask(this, ServiceUrl.save_retirement, nameValuePair, 1, "post").execute(new Void[0]);*/
            } else {
                new SaveProfileAsytask(this, ServiceUrl.save_retirement, entity).execute(new Void[0]);
                /*JSONObject nameValuePair = new JSONObject();
                nameValuePair.put("json_data", retirement_data);
//                nameValuePair.put("a_photo[]", photo_array);
                nameValuePair.put("token", this.pref.getStringValue(Constant.jwttoken, ""));
                new GeneralTask(this, ServiceUrl.save_retirement, nameValuePair, 1, "post").execute(new Void[0]);*/
            }
            Log.e("retirement_data", retirement_data.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save /*2131558589*/:
                prepareJson();
                return;
            case R.id.btn_back /*2131558590*/:
                finish();
                return;
            default:
                return;
        }
    }

    @SuppressLint({"InflateParams"})
    public void on_GeneralSuccess(JSONObject response, int responseCode) {
        try {
            if (response == null) {
                try {
                    show_all_layouts();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                JSONObject Obje = response.getJSONObject("retirement");
                JSONArray array = Obje.getJSONArray("retirement");
                if (array.length() <= 1) {
                    show_all_layouts();
                }
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.getJSONObject(i);
                    View view = LayoutInflater.from(this).inflate(R.layout.layout_retirement, null);
                    ((TextView) view.findViewById(R.id.lbl_retirement)).setText(json.getString("income").toString().trim());
                    ((EditText) view.findViewById(R.id.edit_retirement_id)).setText(json.getString("retirement_id"));
                    EditText edit_income = (EditText) view.findViewById(R.id.edit_income);
                    edit_income.addTextChangedListener(this);
                    this.totalIncome.add(edit_income);
                    edit_income.setText(json.getString("monthly_income").toString().trim());
                    EditText edit_desc = (EditText) view.findViewById(R.id.edit_desc);
                    edit_desc.setText(json.getString("descritption").toString().trim());
                    edit_income.setHint(getResources().getString(R.string.hint_month_income));
                    edit_desc.setHint(getResources().getString(R.string.hint_desc));
                    if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                        edit_income.setEnabled(false);
                        edit_desc.setEnabled(false);
                    }
                    this.layout_retirement.addView(view);
                }
            }
        } catch (Exception e) {

            Log.d("Exception:", e.getMessage());
        }
    }

    public void on_ProfileSuccess(JSONObject response) {
        try {
            if (response.has("message")) {
                displayMessage(response.getString("message").toString());
            }
            if (response.has("success") && response.getString("success").toString().trim().equalsIgnoreCase("1")) {
                this.pref.setStringValue(Constant.RetirementFlag, response.getString("success").toString());
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    public void afterTextChanged(Editable s) {
        this.total = 0;
        for (int i = 0; i < this.totalIncome.size(); i++) {
            if (!((EditText) this.totalIncome.get(i)).getText().toString().trim().isEmpty()) {
                this.total = Integer.parseInt(((EditText) this.totalIncome.get(i)).getText().toString().trim()) + this.total;
            }
        }
        this.editTotal.setText("" + this.total);
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
