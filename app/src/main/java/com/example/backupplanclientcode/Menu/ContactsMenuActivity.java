package com.example.backupplanclientcode.Menu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.backupplanclientcode.Asyntask.GeneralTask;
import com.example.backupplanclientcode.Asyntask.GeneralTask.ResponseListener_General;
import com.example.backupplanclientcode.Asyntask.SaveProfileAsytask;
import com.example.backupplanclientcode.Asyntask.SaveProfileAsytask.ResponseListerProfile;
import com.example.backupplanclientcode.Bugsense.Bugsense;
import com.example.backupplanclientcode.ConnectionDetector;
import com.example.backupplanclientcode.Constant.Constant;
import com.example.backupplanclientcode.Contact.ContactListActivity;
import com.example.backupplanclientcode.Database.DBHelper;
import com.example.backupplanclientcode.Preference.SettingPreference;
import com.example.backupplanclientcode.R;
import com.example.backupplanclientcode.ServiceUrl.ServiceUrl;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContactsMenuActivity extends Activity implements OnClickListener, ResponseListerProfile, ResponseListener_General {
    TextView actionBarTittle;
    LinearLayout addContactForphone;
    LinearLayout addContacts;
    Button btn_back;
    Button btn_save;
    ConnectionDetector connection;
    int contactCounts = 0;
    DBHelper dbHelper;
    String delete_contact = "";
    LinearLayout layoutContacts;
    SettingPreference pref;
    LinearLayout prevContacts;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_contacts);
        intilization();
        initControls();
    }

    private void intilization() {
        this.pref = new SettingPreference(getApplicationContext());
        this.connection = new ConnectionDetector(getApplicationContext());
        new Bugsense().startBugsense(getApplicationContext());
        this.dbHelper = new DBHelper(getApplicationContext());
        this.dbHelper.save_contact("", "", "");
        this.dbHelper.delete_contact_table();
    }

    private void initControls() {
        this.btn_save = (Button) findViewById(R.id.btn_save);
        this.btn_back = (Button) findViewById(R.id.btn_back);
        this.actionBarTittle = (TextView) findViewById(R.id.actionBarTittle);
        this.actionBarTittle.setText(getResources().getString(R.string.menu_contacts));
        this.btn_save.setOnClickListener(this);
        this.btn_back.setOnClickListener(this);
        this.layoutContacts = (LinearLayout) findViewById(R.id.layoutContacts);
        this.addContactForphone = (LinearLayout) findViewById(R.id.addContactForphone);
        this.addContacts = (LinearLayout) findViewById(R.id.addContacts);
        this.prevContacts = (LinearLayout) findViewById(R.id.prevContacts);
        this.addContactForphone.setOnClickListener(this);
        this.addContacts.setOnClickListener(this);
        if (!this.connection.isConnectingToInternet()) {
            displayToast(getResources().getString(R.string.connectionFailMessage));
        } else if (this.pref.getStringValue(Constant.contactFlag, "").equalsIgnoreCase("1")) {
            this.btn_save.setText("Edit");
            if (!this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                this.actionBarTittle.setText("Edit " + getResources().getString(R.string.menu_contacts));
                try {
                    JSONObject nameValuePair = new JSONObject();
                    nameValuePair.put("user_id", this.pref.getStringValue(Constant.user_id, ""));//23
                    nameValuePair.put("token", this.pref.getStringValue(Constant.jwttoken, ""));
                    new GeneralTask(this, ServiceUrl.get_contact_detail, nameValuePair, 2, "post").execute(new Void[0]);
                } catch (Exception e) {
                }
            }
        } else {
            addContactLayout(null, false, "");
        }
    }

    private void displayToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint({"InflateParams"})
    private void addContactLayout(JSONObject json, boolean flag, String contact_id) {
        try {
            final View view = LayoutInflater.from(this).inflate(R.layout.layout_contacts, null);
            final ImageView remove = (ImageView) view.findViewById(R.id.remove);
            remove.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    ContactsMenuActivity.this.layoutContacts.removeView(view);
                    ContactsMenuActivity.this.dbHelper.delete_contact(((EditText) view.findViewById(R.id.edit_contact)).getText().toString());
                    if (ContactsMenuActivity.this.delete_contact.equalsIgnoreCase("")) {
                        ContactsMenuActivity.this.delete_contact = ContactsMenuActivity.this.delete_contact.concat(remove.getTag().toString());
                        return;
                    }
                    ContactsMenuActivity.this.delete_contact = ContactsMenuActivity.this.delete_contact.concat("," + remove.getTag().toString());
                }
            });
            remove.setVisibility(View.VISIBLE);
            if (flag) {
                EditText edit_name = (EditText) view.findViewById(R.id.edit_name);
                EditText edit_contact = (EditText) view.findViewById(R.id.edit_contact);
                ((EditText) view.findViewById(R.id.editId)).setText(contact_id);
                edit_name.setText(json.getString("contact_name"));
                edit_contact.setText(json.getString("contact_number"));
                remove.setTag(contact_id);
                this.dbHelper.save_contact(contact_id, json.getString("contact_name"), json.getString("contact_number"));
            }
            this.layoutContacts.addView(view);
            this.contactCounts++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save /*2131558589*/:
                prePareContactJSon();
                return;
            case R.id.btn_back /*2131558590*/:
                finish();
                return;
            case R.id.addContacts /*2131558904*/:
                addContactLayout(null, false, "");
                return;
            case R.id.addContactForphone /*2131558905*/:
                startActivityForResult(new Intent(getApplicationContext(), ContactListActivity.class), 1);
                return;
            default:
                return;
        }
    }

    private void prePareContactJSon() {
        try {
            JSONObject contact_data = new JSONObject();
            JSONArray contact = new JSONArray();
            for (int i = 0; i < this.prevContacts.getChildCount(); i++) {
                JSONObject jsonbj = new JSONObject();
                ViewGroup view = (ViewGroup) this.prevContacts.getChildAt(i);
                EditText edit_name = (EditText) view.findViewById(R.id.edit_name);
                EditText edit_contact = (EditText) view.findViewById(R.id.edit_contact);
                jsonbj.put("contact_id", ((EditText) view.findViewById(R.id.editId)).getText().toString().trim());
                jsonbj.put("contact_name", edit_name.getText().toString().trim());
                jsonbj.put("contact_number", edit_contact.getText().toString().trim());
                contact.put(jsonbj);
            }
            for (int i2 = 0; i2 < this.layoutContacts.getChildCount(); i2++) {
                JSONObject jsonbj2 = new JSONObject();
                ViewGroup view2 = (ViewGroup) this.layoutContacts.getChildAt(i2);
                EditText edit_name2 = (EditText) view2.findViewById(R.id.edit_name);
                EditText edit_contact2 = (EditText) view2.findViewById(R.id.edit_contact);
                jsonbj2.put("contact_id", ((EditText) view2.findViewById(R.id.editId)).getText().toString().trim());
                jsonbj2.put("contact_name", edit_name2.getText().toString().trim());
                jsonbj2.put("contact_number", edit_contact2.getText().toString().trim());
                contact.put(jsonbj2);
            }
            contact_data.put("user_id", this.pref.getStringValue(Constant.user_id, ""));
            contact_data.put("delete_contact", this.delete_contact);
            contact_data.put("contact", contact);
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
//            MultipartBody entity1 = new MultipartBody().
            JSONObject sendJson = new JSONObject();

            sendJson.put("contact_data", contact_data);
//            entity.addPart("json_data", new StringBody(sendJson.toString()));
//            FormBodyPart bodyPart = new FormBodyPart("json_data", new StringBody(contact_data.toString()));
//            entity.addPart(bodyPart);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("json_data", sendJson.toString()));
//
//            FormBodyPart part2= new FormBodyPart("json_data", new StringBody(contact_data.toString()));
//            entity.addPart(part2);

//            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
//            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//
//            entityBuilder.addTextBody("json_data", String.valueOf(sendJson));
//
//            HttpEntity entity1 =  entityBuilder.build();

//            StringEntity se = new StringEntity( contact_data.toString());
//            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "multipart/form-data"));
//            new PostData().execute(sendJson.toString());


            Log.e("sendJson", sendJson.toString());
            if (!this.connection.isConnectingToInternet()) {
                return;
            }
            if (this.pref.getStringValue(Constant.contactFlag, "").equalsIgnoreCase("1")) {
                new SaveProfileAsytask(this, ServiceUrl.edit_contact, nameValuePairs).execute(new Void[0]);
            } else {
                new SaveProfileAsytask(this, ServiceUrl.save_contact, nameValuePairs).execute(new Void[0]);
//                JSONObject nameValuePair = new JSONObject();
//                nameValuePair.put("json_data", contact_data);//this.pref.getStringValue(Constant.user_id, ""));
////                nameValuePair.put("token", this.pref.getStringValue(Constant.jwttoken, ""));
//                new GeneralTask(this, ServiceUrl.save_contact, nameValuePair, 1, "posta").execute(new Void[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 1) {
            addContactList();
        }
    }

    private void addContactList() {
        try {
            this.layoutContacts.removeAllViews();
            Cursor cur = this.dbHelper.getContacts();
            while (cur.moveToNext()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("user_id", "");
                jsonObject.put("contact_name", cur.getString(cur.getColumnIndex("contact_name")));
                jsonObject.put("contact_number", cur.getString(cur.getColumnIndex("contact_number")));
                addContactLayout(jsonObject, true, cur.getString(cur.getColumnIndex("conatact_id")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void on_ProfileSuccess(JSONObject response) {
        try {
            if (response.has("message")) {
                Toast.makeText(getApplicationContext(), response.getString("message").toString(), Toast.LENGTH_SHORT).show();
            }
            if (response.has("success") && response.getString("success").toString().trim().equalsIgnoreCase("1")) {
                this.pref.setStringValue(Constant.contactFlag, response.getString("success").toString());
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void on_GeneralSuccess(JSONObject response, int responseCode) {
        try {
            JSONObject jsonObject = response.getJSONObject("contact");
            JSONArray jsonArray = jsonObject.getJSONArray("contact");
            if (jsonArray.length() == 0) {
                addContactLayout(null, false, "");
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                addContactLayout(json, true, json.getString("contact_id"));
            }
        } catch (Exception e) {
        }
    }

    private class PostData extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... params) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(ServiceUrl.save_contact +"?token=" + pref.getStringValue(Constant.jwttoken, ""));

//            Log.d("test", ServiceUrl.save_contact + pref.getStringValue(Constant.jwttoken, ""));
            try {
                // Add your data
                String Name = params[0];

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("json_data", Name));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                Log.d("test", EntityUtils.toString(response.getEntity()));
                Log.d("test", EntityUtils.toString(response.getEntity()));
                Log.d("test", EntityUtils.toString(response.getEntity()));
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
            return null;
        }
    }
}