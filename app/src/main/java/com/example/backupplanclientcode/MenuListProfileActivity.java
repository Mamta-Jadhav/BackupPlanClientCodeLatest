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
import com.example.backupplanclientcode.Menu.ContactsMenuActivity;
import com.example.backupplanclientcode.Menu.ProfileMenu;

public class MenuListProfileActivity extends Activity {
    TextView actionBarTittle;
    Button btn_back;
    Button btn_save;
    LinearLayout menu_contact;
    LinearLayout menu_profile;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_and_contact);
        new Bugsense().startBugsense(getApplicationContext());
        initControls();
    }

    private void initControls() {
        this.btn_save = (Button) findViewById(R.id.btn_save);
        this.btn_back = (Button) findViewById(R.id.btn_back);
        this.actionBarTittle = (TextView) findViewById(R.id.actionBarTittle);
        this.actionBarTittle.setText(getResources().getString(R.string.menu_profile));
        this.btn_save.setVisibility(View.GONE);
        this.btn_back.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MenuListProfileActivity.this.finish();
            }
        });
        this.menu_profile = (LinearLayout) findViewById(R.id.menu_profile);
        this.menu_profile.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MenuListProfileActivity.this.startActivity(new Intent(MenuListProfileActivity.this.getApplicationContext(), ProfileMenu.class));
            }
        });
        this.menu_contact = (LinearLayout) findViewById(R.id.menu_contact);
        this.menu_contact.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MenuListProfileActivity.this.startActivity(new Intent(MenuListProfileActivity.this.getApplicationContext(), ContactsMenuActivity.class));
            }
        });
    }
}
