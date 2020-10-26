package com.example.backupplanclientcode.Menu;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.example.backupplanclientcode.Bugsense.Bugsense;
import com.example.backupplanclientcode.Constant.Constant;
import com.example.backupplanclientcode.LogOutTimerUtil;
import com.example.backupplanclientcode.Preference.SettingPreference;
import com.example.backupplanclientcode.R;
import com.example.backupplanclientcode.loginActivity;

import static com.example.backupplanclientcode.LogOutTimerUtil.foreGround;
import static com.example.backupplanclientcode.LogOutTimerUtil.logout;

public class MenuFuneralPlanning extends Activity implements LogOutTimerUtil.LogOutListener {
    TextView actionBarTittle;
    Button btn_back;
    Button btn_save;
    TextView url_funeral;
    SettingPreference pref;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_funeral_plannning);
        this.pref = new SettingPreference(getApplicationContext());
        new Bugsense().startBugsense(getApplicationContext());
        findViewId();
    }

    private void findViewId() {
        this.btn_save = (Button) findViewById(R.id.btn_save);
        this.btn_save.setVisibility(View.GONE);
        this.btn_back = (Button) findViewById(R.id.btn_back);
        this.btn_back.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MenuFuneralPlanning.this.finish();
            }
        });
        this.actionBarTittle = (TextView) findViewById(R.id.actionBarTittle);
        this.actionBarTittle.setText(getResources().getString(R.string.menu_fineralPlannning));
        this.url_funeral = (TextView) findViewById(R.id.url_funeral);
        this.url_funeral.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent("android.intent.action.VIEW");
                i.setData(Uri.parse("http://www.yourbackupplan.ca/#!uploads/cw5j"));
                MenuFuneralPlanning.this.startActivity(i);
            }
        });
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
