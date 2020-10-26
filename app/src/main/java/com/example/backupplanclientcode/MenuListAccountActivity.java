package com.example.backupplanclientcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.backupplanclientcode.Bugsense.Bugsense;
import com.example.backupplanclientcode.Menu.AccountMenu;
import com.example.backupplanclientcode.Menu.BillsToPayMenuActivity;

public class MenuListAccountActivity extends Activity implements OnClickListener {
    TextView actionBarTittle;
    Button btn_back;
    Button btn_save;
    LinearLayout menu_account;
    LinearLayout menu_billstopay;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_menu_list_layout);
        new Bugsense().startBugsense(getApplicationContext());
        findViewID();
    }

    private void findViewID() {
        this.btn_save = (Button) findViewById(R.id.btn_save);
        this.btn_save.setVisibility(View.INVISIBLE);
        this.btn_back = (Button) findViewById(R.id.btn_back);
        this.btn_back.setOnClickListener(this);
        this.actionBarTittle = (TextView) findViewById(R.id.actionBarTittle);
        this.actionBarTittle.setText(getResources().getString(R.string.menu_account));
        this.menu_account = (LinearLayout) findViewById(R.id.menu_account);
        this.menu_account.setOnClickListener(this);
        this.menu_billstopay = (LinearLayout) findViewById(R.id.menu_billstopay);
        this.menu_billstopay.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_account /*2131558581*/:
                startActivity(new Intent(getApplicationContext(), AccountMenu.class));
                return;
            case R.id.menu_billstopay /*2131558583*/:
                startActivity(new Intent(getApplicationContext(), BillsToPayMenuActivity.class));
                return;
            case R.id.btn_back /*2131558590*/:
                finish();
                return;
            default:
                return;
        }
    }
}
