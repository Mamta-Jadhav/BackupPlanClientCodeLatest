package com.example.backupplanclientcode.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.google.firebase.auth.EmailAuthProvider;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "guestUser.db", null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table guest_user(ID INTEGER PRIMARY KEY AUTOINCREMENT,user_name text,password text,user_id text)");
        db.execSQL("create table contacts(ID INTEGER PRIMARY KEY AUTOINCREMENT,contact_name text,contact_number text,conatact_id text)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public boolean saveGuestUser(String user_name, String password, String user_id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_name", user_name);
        values.put("password", password);
        values.put("user_id", user_id);
        if (!check_duplicate_pass(password)) {
            return false;
        }
        long i = db.insert("guest_user", null, values);
        db.close();
        if (i == -1) {
            return false;
        }
        return true;
    }

    public void update_guest_user(String user_name, String password, String id) {
        getWritableDatabase().execSQL("UPDATE guest_user SET password='" + password + "', user_name='" + user_name + "' where ID='" + id + "'");
    }

    public boolean check_duplicate_pass(String pass) {
        boolean flag;
        try {
            if (getReadableDatabase().rawQuery("select * from guest_user where password='" + pass + "'", null).moveToNext()) {
                flag = false;
            } else {
                flag = true;
            }
            Log.e("compare passworad", "" + flag);
            return flag;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void save_contact(String conatact_id, String contact_name, String contact_number) {
        if (!check_already_contacts(contact_name, contact_number)) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("conatact_id", conatact_id);
            values.put("contact_name", contact_name);
            values.put("contact_number", contact_number);
            db.insert("contacts", null, values);
        }
    }

    public boolean check_already_contacts(String contact_name, String contact_number) {
        boolean flag = false;
        try {
            if (getReadableDatabase().rawQuery("select * from contacts where contact_name='" + contact_name + "' and contact_number='" + contact_number + "'", null).moveToNext()) {
                flag = true;
            } else {
                flag = false;
            }
            Log.e("compare contact", "" + flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public Cursor getContacts() {
        Cursor cursor = null;
        try {
            return getReadableDatabase().rawQuery("select * from contacts", null);
        } catch (Exception e) {
            e.printStackTrace();
            return cursor;
        }
    }

    public void delete_contact_table() {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("DROP TABLE IF EXISTS contacts");
        database.execSQL("create table contacts(ID INTEGER PRIMARY KEY AUTOINCREMENT,contact_name text,contact_number text,conatact_id text)");
    }

    public Cursor loginGuestUser(String pass, String user_id) {
        Cursor cursor = null;
        try {
            return getReadableDatabase().rawQuery("select * from guest_user where password='" + pass + "' and user_id='" + user_id + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
            return cursor;
        }
    }

    public Cursor getGuestUserList(String user_id) {
        Cursor cursor = null;
        try {
            return getReadableDatabase().rawQuery("select * from guest_user where user_id='" + user_id + "' ORDER BY ID DESC", null);
        } catch (Exception e) {
            e.printStackTrace();
            return cursor;
        }
    }

    public Cursor getUserDetail(String id, String user_id) {
        Cursor cursor = null;
        try {
            return getReadableDatabase().rawQuery("select * from guest_user where user_id='" + user_id + "' and ID=" + id, null);
        } catch (Exception e) {
            e.printStackTrace();
            return cursor;
        }
    }

    public void deleteGuestuser(String id, String user_id) {
        try {
            getWritableDatabase().execSQL("delete from guest_user where user_id='" + user_id + "' and ID=" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete_contact(String contact_number) {
        try {
            getWritableDatabase().execSQL("delete from contacts where contact_number='" + contact_number + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
