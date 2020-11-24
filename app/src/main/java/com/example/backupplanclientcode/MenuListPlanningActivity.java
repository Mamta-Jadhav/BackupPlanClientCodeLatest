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
import com.example.backupplanclientcode.Menu.LongTermCareMenuActivity;
import com.example.backupplanclientcode.Menu.MenuFuneralPlanning;
import com.example.backupplanclientcode.Menu.RetirementMenuActivity;
import com.example.backupplanclientcode.Menu.WillsAndWishActivity;
import com.example.backupplanclientcode.Preference.SettingPreference;

import static com.example.backupplanclientcode.LogOutTimerUtil.foreGround;
import static com.example.backupplanclientcode.LogOutTimerUtil.logout;

public class MenuListPlanningActivity extends Activity implements OnClickListener, LogOutTimerUtil.LogOutListener {
    TextView actionBarTittle;
    Button btn_back;
    Button btn_save;
    LinearLayout menu_FineralPlanning;
    LinearLayout menu_Retirement;
    LinearLayout menu_longTermCare;
    LinearLayout menu_will;
    SettingPreference pref;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planning_menu_list_layout);
        this.pref = new SettingPreference(getApplicationContext());
        new Bugsense().startBugsense(getApplicationContext());
        findViewId();
    }

    private void findViewId() {
        this.btn_back = (Button) findViewById(R.id.btn_back);
        this.btn_back.setOnClickListener(this);
        this.btn_save = (Button) findViewById(R.id.btn_save);
        this.btn_save.setVisibility(View.GONE);
        ((TextView) findViewById(R.id.actionBarTittle)).setText(getResources().getString(R.string.menu_planning));
        this.menu_will = (LinearLayout) findViewById(R.id.menu_will);
        this.menu_will.setOnClickListener(this);
        this.menu_longTermCare = (LinearLayout) findViewById(R.id.menu_longTermCare);
        this.menu_longTermCare.setOnClickListener(this);
        this.menu_Retirement = (LinearLayout) findViewById(R.id.menu_Retirement);
        this.menu_Retirement.setOnClickListener(this);
        this.menu_FineralPlanning = (LinearLayout) findViewById(R.id.menu_FineralPlanning);
        this.menu_FineralPlanning.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back /*2131558590*/:
                finish();
                return;
            case R.id.menu_will /*2131559077*/:
                startActivity(new Intent(getApplicationContext(), WillsAndWishActivity.class));
                return;
            case R.id.menu_longTermCare /*2131559078*/:
                startActivity(new Intent(getApplicationContext(), LongTermCareMenuActivity.class));
                return;
            case R.id.menu_FineralPlanning /*2131559079*/:
                startActivity(new Intent(getApplicationContext(), MenuFuneralPlanning.class));
                return;
            case R.id.menu_Retirement /*2131559080*/:
                startActivity(new Intent(getApplicationContext(), RetirementMenuActivity.class));
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
