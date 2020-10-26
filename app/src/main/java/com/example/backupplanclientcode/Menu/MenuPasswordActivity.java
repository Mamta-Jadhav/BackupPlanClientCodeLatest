package com.example.backupplanclientcode.Menu;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.example.backupplanclientcode.Bugsense.Bugsense;
import com.example.backupplanclientcode.R;

public class MenuPasswordActivity extends Activity {
    TextView actionBarTittle;
    Button btn_back;
    Button btn_save;
    TextView tv_firstPassword;
    TextView tv_lastpassword;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_password);
        new Bugsense().startBugsense(getApplicationContext());
        findViewId();
    }

    private void findViewId() {
        this.btn_save = (Button) findViewById(R.id.btn_save);
        this.btn_save.setVisibility(View.GONE);
        this.btn_back = (Button) findViewById(R.id.btn_back);
        this.btn_back.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MenuPasswordActivity.this.finish();
            }
        });
        this.actionBarTittle = (TextView) findViewById(R.id.actionBarTittle);
        this.actionBarTittle.setText(getResources().getString(R.string.menu_password));
        this.tv_firstPassword = (TextView) findViewById(R.id.tv_firstPassword);
        this.tv_firstPassword.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent("android.intent.action.VIEW");
                i.setData(Uri.parse("https://agilebits.com/onepassword"));
                MenuPasswordActivity.this.startActivity(i);
            }
        });
        this.tv_lastpassword = (TextView) findViewById(R.id.tv_lastpassword);
        this.tv_lastpassword.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent("android.intent.action.VIEW");
                i.setData(Uri.parse("https://lastpass.com/"));
                MenuPasswordActivity.this.startActivity(i);
            }
        });
    }
}
