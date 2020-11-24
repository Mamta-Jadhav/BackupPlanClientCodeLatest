package com.example.backupplanclientcode.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import com.example.backupplanclientcode.Bugsense.Bugsense;
import com.example.backupplanclientcode.Database.DBHelper;
import com.example.backupplanclientcode.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactsListAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> contactHasMap;
    private Context context;
    DBHelper dbHelper;
    boolean isSelectAll = false;

    public ContactsListAdapter(Context _context, ArrayList<HashMap<String, String>> contactHasMap2, boolean isSelectAll2) {
        this.contactHasMap = contactHasMap2;
        this.context = _context;
        this.isSelectAll = isSelectAll2;
        this.dbHelper = new DBHelper(this.context);
        new Bugsense().startBugsense(this.context);
    }

    @SuppressLint({"DefaultLocale", "ViewHolder"})
    public View getView(final int position, View v, ViewGroup parent) {
        View row = LayoutInflater.from(this.context).inflate(R.layout.lv_contact_list, parent, false);
        final TextView tv_contactName = (TextView) row.findViewById(R.id.tv_contactName);
        tv_contactName.setText((CharSequence) ((HashMap) this.contactHasMap.get(position)).get("Name"));
        final TextView tv_contactNumber = (TextView) row.findViewById(R.id.tv_contactNumber);
        tv_contactNumber.setText((CharSequence) ((HashMap) this.contactHasMap.get(position)).get("phoneNo"));
        CheckBox chk_contact = (CheckBox) row.findViewById(R.id.chk_contact);
        if (((String) ((HashMap) this.contactHasMap.get(position)).get("flag")).equalsIgnoreCase("1")) {
            chk_contact.setChecked(true);
        } else if (((String) ((HashMap) this.contactHasMap.get(position)).get("flag")).equalsIgnoreCase("0")) {
            chk_contact.setChecked(false);
        } else {
            chk_contact.setChecked(false);
        }
        chk_contact.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                HashMap<String, String> map = (HashMap) ContactsListAdapter.this.contactHasMap.get(position);
                map.put("phoneNo", map.get("phoneNo"));
                map.put("Name", map.get("Name"));
                Log.d("test", map.get("Name"));
                if (isChecked) {
                    map.put("flag", "1");
                    Log.d("test", 1+"");
                    ContactsListAdapter.this.dbHelper.save_contact("", tv_contactName.getText().toString().trim(), tv_contactNumber.getText().toString().trim());
                } else {
                    map.put("flag", "0");
                    Log.d("test", 0+"");
                    ContactsListAdapter.this.dbHelper.delete_contact(tv_contactNumber.getText().toString().trim());
                }
                ContactsListAdapter.this.contactHasMap.set(position, map);
            }
        });
        return row;
    }

    public int getCount() {
        return this.contactHasMap.size();
    }

    public Object getItem(int arg0) {
        return Integer.valueOf(arg0);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public void filterList(ArrayList<HashMap<String, String>> filterdNames) {
        Log.d("test", "FilterList Notified..."+filterdNames.size());
        this.contactHasMap = filterdNames;
        notifyDataSetChanged();
    }
}