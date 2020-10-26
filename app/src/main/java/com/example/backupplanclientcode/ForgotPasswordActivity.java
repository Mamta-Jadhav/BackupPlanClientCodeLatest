package com.example.backupplanclientcode;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.backupplanclientcode.Asyntask.GeneralTask;
import com.example.backupplanclientcode.Asyntask.GeneralTask.ResponseListener_General;
import com.example.backupplanclientcode.Asyntask.SaveProfileAsytask;
import com.example.backupplanclientcode.Bugsense.Bugsense;
import com.example.backupplanclientcode.Constant.Constant;
import com.example.backupplanclientcode.Preference.SettingPreference;
import com.example.backupplanclientcode.ServiceUrl.ServiceUrl;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class ForgotPasswordActivity extends Activity implements OnClickListener, ResponseListener_General, SaveProfileAsytask.ResponseListerProfile {
    Button btn_forgotPassword;
    ConnectionDetector connection;
    EditText editEmail;
    SettingPreference pref;
    TextView skip;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_layout);
        new Bugsense().startBugsense(getApplicationContext());
        this.pref = new SettingPreference(getApplicationContext());
        this.connection = new ConnectionDetector(getApplicationContext());
        findViewID();
    }

    private void findViewID() {
        this.btn_forgotPassword = (Button) findViewById(R.id.btn_forgotPassword);
        this.editEmail = (EditText) findViewById(R.id.editEmail);
        this.skip = (TextView) findViewById(R.id.skip);
        this.btn_forgotPassword.setOnClickListener(this);
        this.skip.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_forgotPassword /*2131558659*/:
                if (this.editEmail.getText().toString().trim().isEmpty()) {
                    this.editEmail.setError("Filed is requierd");
                    return;
                } else {
                    forgotPassword(this.editEmail.getText().toString().trim());
                    return;
                }
            case R.id.skip /*2131558660*/:
                finish();
                return;
            default:
                return;
        }
    }

    private void forgotPassword(String email) {
        if (this.connection.isConnectingToInternet()) {
           try {
//               JSONObject nameValuePair = new JSONObject();
//               nameValuePair.put("email", email);
//               nameValuePair.put("token", this.pref.getStringValue(Constant.jwttoken, ""));
               List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
               nameValuePairs.add(new BasicNameValuePair("email", email));
//               nameValuePairs.add(new BasicNameValuePair("token", this.pref.getStringValue(Constant.jwttoken, "")));
//               new GeneralTask(this, ServiceUrl.forgot_password, nameValuePair, 1, "post").execute(new Void[0]);
               new SaveProfileAsytask(this, ServiceUrl.forgot_password, nameValuePairs).execute(new Void[0]);
           }catch(Exception e){} return;
        }
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.connectionFailMessage), Toast.LENGTH_SHORT).show();
    }

    public void on_GeneralSuccess(JSONObject response, int responseCode) {
        if (responseCode == 1) {
            try {
                if (!response.has("flag")) {
                    return;
                }
                if (Boolean.parseBoolean(response.getString("flag"))) {
                    Toast.makeText(getApplicationContext(), response.getString("msg"), Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
                Toast.makeText(getApplicationContext(), response.getString("msg"), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void on_ProfileSuccess(JSONObject jSONObject) {
        try {
            if (!jSONObject.has("flag")) {
                return;
            }
            if (Boolean.parseBoolean(jSONObject.getString("flag"))) {
                Toast.makeText(getApplicationContext(), jSONObject.getString("msg"), Toast.LENGTH_LONG).show();
                finish();
                return;
            }
            Toast.makeText(getApplicationContext(), jSONObject.getString("msg"), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
