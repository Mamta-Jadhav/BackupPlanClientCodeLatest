package com.example.backupplanclientcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.backupplanclientcode.Bugsense.Bugsense;
import com.example.backupplanclientcode.Constant.Constant;
import com.example.backupplanclientcode.Menu.AssetMenu;
import com.example.backupplanclientcode.Menu.InvestmentMenu;
import com.example.backupplanclientcode.Menu.MortgagesLoansMenu;
import com.example.backupplanclientcode.Preference.SettingPreference;

import static com.example.backupplanclientcode.LogOutTimerUtil.foreGround;
import static com.example.backupplanclientcode.LogOutTimerUtil.logout;

public class MenuListAssetActivity extends Activity implements OnClickListener, LogOutTimerUtil.LogOutListener {
    TextView actionBarTittle;
    Button btn_back;
    Button btn_save;
    LinearLayout menu_Asset;
    LinearLayout menu_Investment;
    LinearLayout menu_Mortgage;
    SettingPreference pref;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_sub_menu_layout);
        this.pref = new SettingPreference(getApplicationContext());
        new Bugsense().startBugsense(getApplicationContext());
        findViewId();
    }

    private void findViewId() {
        this.btn_save = findViewById(R.id.btn_save);
        this.btn_save.setVisibility(View.GONE);
        this.btn_back = findViewById(R.id.btn_back);
        this.btn_back.setOnClickListener(this);
        this.actionBarTittle = findViewById(R.id.actionBarTittle);
        this.actionBarTittle.setText(getResources().getString(R.string.menu_asset));
        this.menu_Investment = findViewById(R.id.menu_Investment);
        this.menu_Investment.setOnClickListener(this);
        this.menu_Asset = findViewById(R.id.menu_Asset);
        this.menu_Asset.setOnClickListener(this);
        this.menu_Mortgage = findViewById(R.id.menu_Mortgage);
        this.menu_Mortgage.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back /*2131558590*/:
                finish();
                return;
            case R.id.menu_Asset /*2131558606*/:
                startActivity(new Intent(getApplicationContext(), AssetMenu.class));
                return;
            case R.id.menu_Investment /*2131558607*/:
                startActivity(new Intent(getApplicationContext(), InvestmentMenu.class));
                return;
            case R.id.menu_Mortgage /*2131558608*/:
                startActivity(new Intent(getApplicationContext(), MortgagesLoansMenu.class));
                return;
            default:
                return;
        }
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