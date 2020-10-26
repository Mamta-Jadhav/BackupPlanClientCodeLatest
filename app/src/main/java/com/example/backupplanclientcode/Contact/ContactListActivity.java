package com.example.backupplanclientcode.Contact;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.backupplanclientcode.Adapter.ContactsListAdapter;
import com.example.backupplanclientcode.Bugsense.Bugsense;
import com.example.backupplanclientcode.Database.DBHelper;
import com.example.backupplanclientcode.Menu.ContactsMenuActivity;
import com.example.backupplanclientcode.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactListActivity extends Activity implements OnClickListener, TextWatcher, OnItemClickListener {
    TextView actionBarTittle;
    ContactsListAdapter adapter;
    ArrayList<HashMap<String, String>> array_sort;
    Button btn_back;
    Button btn_save;
    CheckBox chkSelectAll;
    ArrayList<HashMap<String, String>> contactHasMap;
    DBHelper dbHelper;
    EditText editSearchContact;
    ListView lvContacts;

    public class AsyTask extends AsyncTask<Void, Void, Void> {

        /* renamed from: pd */
        ProgressDialog f38pd;

        public AsyTask() {
        }

        /* access modifiers changed from: protected */
        @SuppressLint({"InlinedApi"})
        public void onPreExecute() {
            super.onPreExecute();
            this.f38pd = new ProgressDialog(ContactListActivity.this, 3);
            this.f38pd.setMessage(ContactListActivity.this.getResources().getString(R.string.txt_loading));
            this.f38pd.setCancelable(false);
            this.f38pd.show();
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Void result) {
            super.onPostExecute(result);
            this.f38pd.dismiss();
            ContactListActivity.this.show_contact_details();
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(Void... params) {
            ContactListActivity.this.getPhoneContacts();
            return null;
        }
    }

    public class saveContcts extends AsyncTask<Void, Void, Void> {
        @SuppressLint({"InlinedApi"})

        /* renamed from: pd */
        ProgressDialog f39pd = new ProgressDialog(ContactListActivity.this, 3);

        public saveContcts() {
        }

        /* access modifiers changed from: protected */
        @SuppressLint({"InlinedApi"})
        public void onPreExecute() {
            super.onPreExecute();
            this.f39pd = new ProgressDialog(ContactListActivity.this, 3);
            this.f39pd.setMessage(ContactListActivity.this.getResources().getString(R.string.txt_loading));
            this.f39pd.setCancelable(false);
            this.f39pd.show();
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(Void... params) {
            for (int i = 0; i < ContactListActivity.this.contactHasMap.size(); i++) {
                if (((String) ((HashMap) ContactListActivity.this.contactHasMap.get(i)).get("flag")).equalsIgnoreCase("1")) {
                    ContactListActivity.this.dbHelper.save_contact("", ((String) ((HashMap) ContactListActivity.this.contactHasMap.get(i)).get("Name")).toString(), ((String) ((HashMap) ContactListActivity.this.contactHasMap.get(i)).get("phoneNo")).toString());
                }
            }
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Void result) {
            super.onPostExecute(result);
            this.f39pd.dismiss();
            ContactListActivity.this.getAllContactList();
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lv_contact_layout);
        this.dbHelper = new DBHelper(getApplicationContext());
        new Bugsense().startBugsense(getApplicationContext());
        initControls();
        new AsyTask().execute(new Void[0]);
    }

    /* access modifiers changed from: private */
    public void getPhoneContacts() {
        try {
            ContentResolver cr = getContentResolver();
            Cursor cur = cr.query(Contacts.CONTENT_URI, null, null, null, "display_name ASC");
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    String id = cur.getString(cur.getColumnIndex("_id"));
                    String name = cur.getString(cur.getColumnIndex("display_name"));
                    if (Integer.parseInt(cur.getString(cur.getColumnIndex("has_phone_number"))) > 0) {
                        Cursor pCur = cr.query(Phone.CONTENT_URI, null, "contact_id = ?", new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            String phoneNo = pCur.getString(pCur.getColumnIndex("data1"));
                            if (!this.dbHelper.check_already_contacts(name, phoneNo)) {
                                HashMap<String, String> map = new HashMap<>();
                                map.put("phoneNo", phoneNo);
                                map.put("Name", name);
                                map.put("flag", "0");
                                this.contactHasMap.add(map);
                            }
                        }
                        pCur.close();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void show_contact_details() {
        this.adapter = new ContactsListAdapter(this, this.contactHasMap, false);
        this.adapter.notifyDataSetChanged();
        this.lvContacts.setAdapter(null);
        this.lvContacts.setAdapter(this.adapter);
    }

    private void initControls() {
        this.editSearchContact = (EditText) findViewById(R.id.editSearchContact);
        this.editSearchContact.addTextChangedListener(this);
        this.chkSelectAll = (CheckBox) findViewById(R.id.chkSelectAll);
        this.chkSelectAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < ContactListActivity.this.contactHasMap.size(); i++) {
                    HashMap<String, String> map = (HashMap) ContactListActivity.this.contactHasMap.get(i);
                    map.put("phoneNo", map.get("phoneNo"));
                    map.put("Name", map.get("Name"));
                    if (isChecked) {
                        map.put("flag", "1");
                    } else {
                        map.put("flag", "0");
                    }
                    ContactListActivity.this.contactHasMap.set(i, map);
                }
                ContactListActivity.this.adapter.notifyDataSetChanged();
            }
        });
        this.lvContacts = (ListView) findViewById(R.id.lvContacts);
        this.btn_save = (Button) findViewById(R.id.btn_save);
        this.btn_save.setText(getResources().getString(R.string.btn_add));
        this.btn_back = (Button) findViewById(R.id.btn_back);
        this.btn_back.setOnClickListener(this);
        this.btn_save.setOnClickListener(this);
        this.actionBarTittle = (TextView) findViewById(R.id.actionBarTittle);
        this.actionBarTittle.setText(getResources().getString(R.string.menu_contacts));
        this.contactHasMap = new ArrayList<>();
        this.contactHasMap.clear();
        this.array_sort = new ArrayList<>();
        this.array_sort.clear();
        this.lvContacts.setOnItemClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save /*2131558589*/:
                new saveContcts().execute(new Void[0]);
                return;
            case R.id.btn_back /*2131558590*/:
                finish();
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void getAllContactList() {
        setResult(-1, new Intent(getApplicationContext(), ContactsMenuActivity.class));
        finish();
    }

    private void displayToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @SuppressLint({"DefaultLocale"})
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int textlength = this.editSearchContact.getText().length();
        this.array_sort.clear();
        for (int i = 0; i < this.contactHasMap.size(); i++) {
            if (textlength <= ((String) ((HashMap) this.contactHasMap.get(i)).get("Name")).length() && this.editSearchContact.getText().toString().toLowerCase().contains(((String) ((HashMap) this.contactHasMap.get(i)).get("Name")).subSequence(0, textlength).toString().toLowerCase())) {
                HashMap<String, String> map = new HashMap<>();
                map.put("Name", (String) ((HashMap) this.contactHasMap.get(i)).get("Name"));
                map.put("phoneNo", (String) ((HashMap) this.contactHasMap.get(i)).get("phoneNo"));
                this.array_sort.add(map);
            }
        }
        if (this.array_sort.size() <= 0) {
            displayToast("No found any contact");
        }
    }

    public void afterTextChanged(Editable s) {
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        final TextView tv_contactName = (TextView) view.findViewById(R.id.tv_contactName);
        final TextView tv_contactNumber = (TextView) view.findViewById(R.id.tv_contactNumber);
        ((CheckBox) view.findViewById(R.id.chk_contact)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ContactListActivity.this.dbHelper.save_contact("", tv_contactName.getText().toString().trim(), tv_contactNumber.getText().toString().trim());
                    return;
                }
                ContactListActivity.this.dbHelper.delete_contact(tv_contactNumber.getText().toString().trim());
                Toast.makeText(ContactListActivity.this.getApplicationContext(), "delete", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
