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
import com.example.backupplanclientcode.Menu.LongTermCareMenuActivity;
import com.example.backupplanclientcode.Menu.MenuFuneralPlanning;
import com.example.backupplanclientcode.Menu.RetirementMenuActivity;
import com.example.backupplanclientcode.Menu.WillsAndWishActivity;

public class MenuListPlanningActivity extends Activity implements OnClickListener {
    TextView actionBarTittle;
    Button btn_back;
    Button btn_save;
    LinearLayout menu_FineralPlanning;
    LinearLayout menu_Retirement;
    LinearLayout menu_longTermCare;
    LinearLayout menu_will;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planning_menu_list_layout);
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
}
