package com.example.backupplanclientcode.Menu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Video;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.example.backupplanclientcode.LogOutTimerUtil;
import com.example.backupplanclientcode.Preference.SettingPreference;
import com.example.backupplanclientcode.R;
import com.example.backupplanclientcode.ServiceUrl.ServiceUrl;
import com.example.backupplanclientcode.Utility.Utility;
import com.example.backupplanclientcode.Utility.CompressImage;
import com.example.backupplanclientcode.loginActivity;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.google.firebase.auth.EmailAuthProvider;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.backupplanclientcode.LogOutTimerUtil.foreGround;
import static com.example.backupplanclientcode.LogOutTimerUtil.logout;

public class InternetMenuActivity extends Activity implements OnClickListener, ResponseListerProfile, ResponseListener_General, LogOutTimerUtil.LogOutListener {
    private static final String DIGITAL = "DIGITAL";
    private static final int SELECT_PICTURE = 1;
    private static final String SOCIAL = "SOCIAL";
    private String TYPE = SOCIAL;
    TextView actionBarTittle;
    Button btn_back;
    Button btn_remove;
    Button btn_save;
    CompressImage compress;
    ConnectionDetector connection;
    /* access modifiers changed from: private */
    public ImageView currentImageVew;
    /* access modifiers changed from: private */
    public int deleteImage = 0;
    EditText edit_ComPassword;
    EditText edit_comLocation;
    EditText edit_compId;
    /* access modifiers changed from: private */
    public ImageView imageView;
    SettingPreference pref;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_internet);
        this.pref = new SettingPreference(getApplicationContext());
        this.compress = new CompressImage(getApplicationContext());
        this.connection = new ConnectionDetector(getApplicationContext());
        new Bugsense().startBugsense(getApplicationContext());
        try {
            findviewId();
            checkAlredySaveAccount();
        } catch (Exception e) {
            Log.d("~~~~~~~>", "" + e.getLocalizedMessage());
        }
    }

    private void findviewId() {
        this.actionBarTittle = (TextView) findViewById(R.id.actionBarTittle);
        this.btn_save = (Button) findViewById(R.id.btn_save);
        this.btn_back = (Button) findViewById(R.id.btn_back);
        this.edit_ComPassword = (EditText) findViewById(R.id.edit_ComPassword);
        this.edit_comLocation = (EditText) findViewById(R.id.edit_comLocation);
        this.edit_compId = (EditText) findViewById(R.id.edit_compId);
        this.imageView = (ImageView) findViewById(R.id.iv);
        this.btn_remove = (Button) findViewById(R.id.btn_remove);
        this.btn_back.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                InternetMenuActivity.this.finish();
            }
        });
        this.btn_save.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                InternetMenuActivity.this.saveInternetData(InternetMenuActivity.this.getAccounts());
            }
        });
        this.imageView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (!InternetMenuActivity.this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                    InternetMenuActivity.this.currentImageVew = InternetMenuActivity.this.imageView;
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction("android.intent.action.GET_CONTENT");
                    InternetMenuActivity.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                }
            }
        });
        this.btn_remove.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                InternetMenuActivity.this.deleteImage = 1;
                InternetMenuActivity.this.imageView.setImageResource(R.drawable.img);
            }
        });
    }

    private void checkAlredySaveAccount() {
        String stringValue = this.pref.getStringValue(Constant.internet_id, "");
        String stringValue2 = this.pref.getStringValue(Constant.user_id, "");
        this.actionBarTittle.setText(getResources().getString(R.string.menu_internet));
        if (!this.pref.getStringValue(Constant.internet_id, "").equalsIgnoreCase("0") && !this.pref.getStringValue(Constant.user_id, "").isEmpty()) {
            this.btn_save.setText("Save");
            try {
                if (!this.connection.isConnectingToInternet()) {
                    displayMessage(getResources().getString(R.string.connectionFailMessage));
                } else if (this.TYPE == SOCIAL) {
                    JSONObject nameValuePair = new JSONObject();
                    nameValuePair.put("user_id", this.pref.getStringValue(Constant.user_id, ""));
                    nameValuePair.put("internet_id", this.pref.getStringValue(Constant.internet_id, ""));
                    nameValuePair.put("token", this.pref.getStringValue(Constant.jwttoken, ""));
                    new GeneralTask(this, ServiceUrl.get_internet_detail, nameValuePair, 1, "post").execute(new Void[0]);
                    this.btn_save.setText("Edit");
                } else if (this.TYPE == DIGITAL) {
                    JSONObject nameValuePair2 = new JSONObject();
                    nameValuePair2.put("user_id", this.pref.getStringValue(Constant.user_id, ""));
                    nameValuePair2.put("token", this.pref.getStringValue(Constant.jwttoken, ""));
                    new GeneralTask(this, ServiceUrl.get_digital_detail, nameValuePair2, 2, "post").execute(new Void[0]);
                    this.btn_save.setText("Edit");
                }
            } catch (Exception e) {

            }
        }
    }

    public void onClick(View v) {
    }

    public void on_GeneralSuccess(JSONObject response, int responseCode) {
        Log.d("~~~~~~~~~~>>", "" + response.toString());
        if (response == null) {
            return;
        }
        if (responseCode == 1) {
            try {
                response = response.getJSONObject("internet_data");
                findViewById(R.id.layout_internet_social_header).setVisibility(View.VISIBLE);
                this.edit_compId.setText(response.getString("internet_id"));
                this.edit_ComPassword.setText(response.getString("c_password"));
                this.edit_comLocation.setText(response.getString("c_location"));
                UrlImageViewHelper.setUrlDrawable(this.imageView, response.getString("c_photo"), (int) R.drawable.img);
                this.imageView.setTag("1");
                this.imageView.setContentDescription(response.getString("c_photo"));
                populateAccounts(response.getJSONArray("account"));
                if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                    this.edit_ComPassword.setEnabled(false);
                    this.edit_comLocation.setEnabled(false);
                    return;
                }
                this.edit_ComPassword.setEnabled(true);
                this.edit_comLocation.setEnabled(true);
            } catch (Exception e) {
            }
        } else {
            try {
                findViewById(R.id.layout_internet_social_header).setVisibility(View.GONE);
                populateAccounts(response.getJSONArray("digital_data"));
            } catch (Exception e) {
            }
        }
    }

    private void populateAccounts(JSONArray accountsJarray) throws JSONException {
        LinearLayout layoutNewAccounts = (LinearLayout) findViewById(R.id.layoutAccounts);
        layoutNewAccounts.removeAllViews();
//        for (int i = 0; i < accountsJarray.length(); i++) {
//            JSONObject accountsJobj = accountsJarray.getJSONObject(i);
            final View child = getLayoutInflater().inflate(R.layout.layout_internet_account, null);
            ((TextView) child.findViewById(R.id.text_new_field_heading)).setText("Email");//accountsJobj.getString("accout_type"));
            populateAccountDetails(accountsJarray, child);
            child.findViewById(R.id.layout_add_new_field).setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (!InternetMenuActivity.this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                        final LinearLayout layoutFields = (LinearLayout) child.findViewById(R.id.layoutFields);
                        final View fieldsView = InternetMenuActivity.this.getLayoutInflater().inflate(R.layout.layout_internet, null);
                        EditText editText = (EditText) fieldsView.findViewById(R.id.edit_id);
                        ((EditText) fieldsView.findViewById(R.id.edit_AccName)).setHint(InternetMenuActivity.this.getResources().getString(R.string.txt_acc_name));
                        ((EditText) fieldsView.findViewById(R.id.edit_Username)).setHint(InternetMenuActivity.this.getResources().getString(R.string.txt_username));
                        ((EditText) fieldsView.findViewById(R.id.edit_Password)).setHint(InternetMenuActivity.this.getResources().getString(R.string.txt_password));
                        ((ImageView) fieldsView.findViewById(R.id.removeIcon)).setOnClickListener(new OnClickListener() {
                            public void onClick(View v) {
                                layoutFields.removeView(fieldsView);
                            }
                        });
                        layoutFields.addView(fieldsView);
                    }
                }
            });
            layoutNewAccounts.addView(child);
//        }
    }

    private void populateAccountDetails(JSONArray accountJarray, View accountView) throws JSONException {
        final LinearLayout layoutFields = (LinearLayout) accountView.findViewById(R.id.layoutFields);
        for (int j = 0; j < accountJarray.length(); j++) {
            JSONObject accountJobj = accountJarray.getJSONObject(j);
            final View fieldsView = getLayoutInflater().inflate(R.layout.layout_internet, null);
            EditText edit_id = (EditText) fieldsView.findViewById(R.id.edit_id);
            EditText edit_AccName = (EditText) fieldsView.findViewById(R.id.edit_AccName);
            edit_AccName.setText(accountJobj.getString("e_account"));
            edit_AccName.setTag(accountJobj.getString("email_id"));
            EditText edit_Username = (EditText) fieldsView.findViewById(R.id.edit_Username);
            edit_Username.setText(accountJobj.getString("username"));
            EditText edit_Password = (EditText) fieldsView.findViewById(R.id.edit_Password);
            edit_Password.setText(accountJobj.getString("password"));
            ((ImageView) fieldsView.findViewById(R.id.removeIcon)).setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    layoutFields.removeView(fieldsView);
                }
            });
            if (accountJobj.has("email_id")) {
                edit_id.setTag(accountJobj.getString("email_id"));
            } else if (accountJobj.has("facebook_id")) {
                edit_id.setTag(accountJobj.getString("facebook_id"));
            } else if (accountJobj.has("twitter_id")) {
                edit_id.setTag(accountJobj.getString("twitter_id"));
            } else if (accountJobj.has("linkedin_id")) {
                edit_id.setTag(accountJobj.getString("linkedin_id"));
            } else if (accountJobj.has("instagram_id")) {
                edit_id.setTag(accountJobj.getString("instagram_id"));
            } else if (accountJobj.has("website_id")) {
                edit_id.setTag(accountJobj.getString("website_id"));
            } else if (accountJobj.has("blog_id")) {
                edit_id.setTag(accountJobj.getString("blog_id"));
            } else if (accountJobj.has("cpanel_id")) {
                edit_id.setTag(accountJobj.getString("cpanel_id"));
            } else if (accountJobj.has("category_id")) {
                edit_id.setTag(accountJobj.getString("category_id"));
                edit_AccName.setTag(accountJobj.getString("account_id"));
            }
            if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                edit_AccName.setEnabled(false);
                edit_Username.setEnabled(false);
                edit_Password.setEnabled(false);
            } else {
                edit_AccName.setEnabled(true);
                edit_Username.setEnabled(true);
                edit_Password.setEnabled(true);
            }
            layoutFields.addView(fieldsView);
        }
    }

    public void on_ProfileSuccess(JSONObject response) {
        Log.d("test", response.toString());
        try {
            if (response.has("message")) {
                displayMessage(response.getString("message").toString());
            }
            if (response.has("success") && response.getString("success").toString().trim().equalsIgnoreCase("1")) {
                if (response.has("internet_id")) {
                    this.pref.setStringValue(Constant.internet_id, response.getString("internet_id").toString());
                }
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    public void onSocialClick(View view) {
        view.setTag("1");
        findViewById(R.id.layout_social_opt).setTag("0");
        findViewById(R.id.layout_social_opt).setBackgroundColor(Color.parseColor("#e00700"));
        findViewById(R.id.layout_digital_opt).setBackgroundColor(Color.parseColor("#000000"));
        this.TYPE = SOCIAL;
        ((TextView) findViewById(R.id.tv_add_new_account)).setText("Add New Social Account");
        if (this.connection.isConnectingToInternet()) {
            try {
                JSONObject nameValuePair = new JSONObject();
                nameValuePair.put("user_id", this.pref.getStringValue(Constant.user_id, ""));
                nameValuePair.put("internet_id", this.pref.getStringValue(Constant.internet_id, ""));
                nameValuePair.put("token", this.pref.getStringValue(Constant.jwttoken, ""));
                new GeneralTask(this, ServiceUrl.get_internet_detail, nameValuePair, 1, "post").execute(new Void[0]);
                this.btn_save.setText("Edit");
            } catch (Exception e) {

            }
            return;
        }
        displayMessage(getResources().getString(R.string.connectionFailMessage));
    }

    public void onDigitalClick(View view) {
        view.setTag("1");
        findViewById(R.id.layout_digital_opt).setTag("0");
        findViewById(R.id.layout_social_opt).setBackgroundColor(Color.parseColor("#000000"));
        findViewById(R.id.layout_digital_opt).setBackgroundColor(Color.parseColor("#e00700"));
        this.TYPE = DIGITAL;
        ((TextView) findViewById(R.id.tv_add_new_account)).setText("Add New Digital Account");
        if (this.connection.isConnectingToInternet()) {
            try {
                JSONObject nameValuePair = new JSONObject();
                nameValuePair.put("user_id", this.pref.getStringValue(Constant.user_id, ""));
                nameValuePair.put("token", this.pref.getStringValue(Constant.jwttoken, ""));
                new GeneralTask(this, ServiceUrl.get_digital_detail, nameValuePair, 2, "post").execute(new Void[0]);
                this.btn_save.setText("Edit");
            } catch (Exception e) {
            }
            return;
        }
        displayMessage(getResources().getString(R.string.connectionFailMessage));
    }

    public void onAddNewAccountLayoutClick(View view) {
    }

    public void onAddNewAccountButtonClick(View view) {
        if (!this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            findViewById(R.id.layout_add_new_acc).setVisibility(View.VISIBLE);
//            ((EditText) findViewById(R.id.txt_acc_name)).setText("");
        }
    }

    public void onAddNewSocialAccount(View view) {
        EditText text = ((EditText) findViewById(R.id.txt_acc_name));
        text.setEnabled(false);
        String socialAccountName = text.getText().toString();
        if (socialAccountName.length() > 0) {
            findViewById(R.id.layout_add_new_acc).setVisibility(View.GONE);
            addNewAccountParentField(socialAccountName);
            return;
        }
        displayMessage("Please enter a valid Account Name.");
    }

    private void addNewAccountParentField(String title) {
        LinearLayout layoutNewAccounts = (LinearLayout) findViewById(R.id.layoutAccounts);
        final View child = getLayoutInflater().inflate(R.layout.layout_internet_account, null);
        ((TextView) child.findViewById(R.id.text_new_field_heading)).setText(title);
        child.findViewById(R.id.layout_add_new_field).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (!InternetMenuActivity.this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                    final LinearLayout layoutFields = (LinearLayout) child.findViewById(R.id.layoutFields);
                    final View fieldsView = InternetMenuActivity.this.getLayoutInflater().inflate(R.layout.layout_internet, null);
                    EditText editText = (EditText) fieldsView.findViewById(R.id.edit_id);
                    ((EditText) fieldsView.findViewById(R.id.edit_AccName)).setHint(InternetMenuActivity.this.getResources().getString(R.string.txt_acc_name));
                    ((EditText) fieldsView.findViewById(R.id.edit_Username)).setHint(InternetMenuActivity.this.getResources().getString(R.string.txt_username));
                    ((EditText) fieldsView.findViewById(R.id.edit_Password)).setHint(InternetMenuActivity.this.getResources().getString(R.string.txt_password));
                    ((ImageView) fieldsView.findViewById(R.id.removeIcon)).setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            layoutFields.removeView(fieldsView);
                        }
                    });
                    layoutFields.addView(fieldsView);
                }
            }
        });
        layoutNewAccounts.addView(child);
    }

    /* access modifiers changed from: private */
    public JSONArray getAccounts() {
        try {
            JSONArray internetAccountsJarray = new JSONArray();
            LinearLayout layoutInternetAccounts = (LinearLayout) findViewById(R.id.layoutAccounts);
            int childCount = layoutInternetAccounts.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = layoutInternetAccounts.getChildAt(i);
                JSONObject internetAccountJobj = new JSONObject();
                String text_new_field_heading = ((TextView) child.findViewById(R.id.text_new_field_heading)).getText().toString();
                internetAccountJobj.put("accout_type", text_new_field_heading);
                internetAccountJobj.put("accounts", getAccountDetails((LinearLayout) child.findViewById(R.id.layoutFields), text_new_field_heading));
                Log.d("~~~~~JOBJ~~~~~~>", "" + internetAccountJobj.toString());
                internetAccountsJarray.put(internetAccountJobj);
            }
            return internetAccountsJarray;
        } catch (Exception e) {
            Log.d("~~~~~~E~~~~~>", "" + e.getLocalizedMessage());
            return null;
        }
    }

    private JSONArray getAccountDetails(LinearLayout layoutFields, String heading) throws JSONException {
        String obj;
        JSONArray accountsJarray = new JSONArray();
        int childCount = layoutFields.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = layoutFields.getChildAt(i);
            JSONObject accountJobj = new JSONObject();
            accountJobj.put("user_id", Integer.valueOf(this.pref.getStringValue(Constant.user_id, "")));
            accountJobj.put("internet_id", Integer.valueOf(this.pref.getStringValue(Constant.internet_id, "")));
            accountJobj.put("account", ((EditText) child.findViewById(R.id.edit_AccName)).getText().toString());
            accountJobj.put("account_id", ((EditText) child.findViewById(R.id.edit_AccName)).getTag().toString());
            accountJobj.put("username", ((EditText) child.findViewById(R.id.edit_Username)).getText().toString());
            accountJobj.put("password", ((EditText) child.findViewById(R.id.edit_Password)).getText().toString());
            int edit_id = Integer.valueOf(((EditText) child.findViewById(R.id.edit_id)).getTag().toString() == null ? "0" : ((EditText) child.findViewById(R.id.edit_id)).getTag().toString()).intValue();
            if (heading.equals("email")) {
                if (edit_id != 0) {
                    accountJobj.put("email_id", edit_id);
                }
            } else if (heading.equals("facebook")) {
                if (edit_id != 0) {
                    accountJobj.put("facebook_id", edit_id);
                }
            } else if (heading.equals("twitter")) {
                if (edit_id != 0) {
                    accountJobj.put("twitter_id", edit_id);
                }
            } else if (heading.equals("linkedin")) {
                if (edit_id != 0) {
                    accountJobj.put("linkedin_id", edit_id);
                }
            } else if (heading.equals("instagram")) {
                if (edit_id != 0) {
                    accountJobj.put("instagram_id", edit_id);
                }
            } else if (heading.equals("website")) {
                if (edit_id != 0) {
                    accountJobj.put("website_id", edit_id);
                }
            } else if (heading.equals("blog")) {
                if (edit_id != 0) {
                    accountJobj.put("blog_id", edit_id);
                }
            } else if (!heading.equals("cpanel")) {
                if (((EditText) child.findViewById(R.id.edit_AccName)).getTag().toString() == null) {
                    obj = "0";
                } else {
                    obj = ((EditText) child.findViewById(R.id.edit_AccName)).getTag().toString();
                }
                int accountId = Integer.valueOf(obj).intValue();
                if (accountId != 0) {
                    accountJobj.put("category_id", edit_id);
                    accountJobj.put("account_id", accountId);
                }
            } else if (edit_id != 0) {
                accountJobj.put("cpanel_id", edit_id);
            }
            accountsJarray.put(accountJobj);
        }
        return accountsJarray;
    }

    /* access modifiers changed from: private */
    public void saveInternetData(JSONArray internetAccountsJarray) {
        try {
            JSONObject request_data = new JSONObject();
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            request_data.put("user_id", Integer.valueOf(this.pref.getStringValue(Constant.user_id, "0")));
            if (this.TYPE == SOCIAL) {
                request_data.put("internet_id", Integer.valueOf(this.pref.getStringValue(Constant.internet_id, "0")));
                request_data.put("c_password", this.edit_ComPassword.getText().toString().trim());
                request_data.put("c_location", this.edit_comLocation.getText().toString().trim());
                request_data.put("internet_data", internetAccountsJarray);
                if (this.imageView.getContentDescription().toString().trim().isEmpty()) {
                    request_data.put("c_photo", "");
                    request_data.put("is_file", "0");
                    Log.i("c_photo", this.imageView.getContentDescription().toString());
                } else {
                    String[] arr = this.imageView.getContentDescription().toString().split("/");
                    String atr = arr[arr.length - 1];
                    request_data.put("delete_internet_image", this.deleteImage);
//                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                    Utility.getBitmapFromPath(this.imageView.getContentDescription().toString()).compress(CompressFormat.JPEG, 75, bos);
                    if (this.imageView.getTag().toString().equalsIgnoreCase("0")) {
                        entity.addPart("c_photo[]", new FileBody(new File(this.imageView.getContentDescription().toString())));
                        request_data.put("c_photo", atr);
                        request_data.put("is_file", "1");
                        Log.i("c_photo tag 0", this.imageView.getContentDescription().toString());
                    }else {
                        request_data.put("c_photo", atr);
                        request_data.put("is_file", "0");
                        Log.i("c_photo tag 1", this.imageView.getContentDescription().toString());
                    }
                }
            } else {
                request_data.put("digital_data", internetAccountsJarray);
            }
            entity.addPart("json_data", new StringBody(request_data.toString()));
            Log.e("internet/digital", request_data.toString());
            if (!this.connection.isConnectingToInternet()) {
                displayMessage(getResources().getString(R.string.connectionFailMessage));
            } else if (this.TYPE == SOCIAL) {
                if (this.btn_save.getText().toString().trim().equalsIgnoreCase("save")) {
                    Log.d("test", "Here1");
                    new SaveProfileAsytask(this, ServiceUrl.save_internet, entity).execute(new Void[0]);
                }else{
                    Log.d("test", "Here2");
                    new SaveProfileAsytask(this, ServiceUrl.edit_internet, entity).execute(new Void[0]);
                }
            } else {
                new SaveProfileAsytask(this, ServiceUrl.save_digital, entity).execute(new Void[0]);
            }
        } catch (Exception e) {
            Log.d("~~~~~~Save-E~~~~~>", "" + e.getLocalizedMessage());
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 1) {
            try {
                final Uri imageUri = data.getData();
                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(imageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                Cursor cursor = getContentResolver().query(imageUri, null, null, null, null);

                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                int column_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                Log.d("test", cursor.getString(nameIndex));

                this.imageView.setImageBitmap(selectedImage);
                this.imageView.setContentDescription(cursor.getString(nameIndex));
                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), selectedImage);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(tempUri));

                System.out.println(finalFile.getAbsoluteFile());
                System.out.println(finalFile.getName());
                try {
                    System.out.println(finalFile.getCanonicalPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.imageView.setContentDescription(finalFile.getPath());
                this.imageView.setTag("0");

                Log.d("test", "selectedImage " + selectedImage);
                Log.d("test", "imageUri.getPath() " + imageUri.getPath());
                this.deleteImage = 0;
            } catch (Exception e) {
                Log.d("~~~~~E~~~~~>", "" + e.getLocalizedMessage());
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @SuppressLint({"NewApi"})
    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        if (VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                return Environment.getExternalStorageDirectory() + "/" + DocumentsContract.getDocumentId(uri).split(":")[1];
            } else if (isDownloadsDocument(uri)) {
                uri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(DocumentsContract.getDocumentId(uri)).longValue());
            } else if (isMediaDocument(uri)) {
                String[] split = DocumentsContract.getDocumentId(uri).split(":");
                String type = split[0];
                if ("image".equals(type)) {
                    uri = Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{split[1]};
            }
        }
        if (Param.CONTENT.equalsIgnoreCase(uri.getScheme())) {
            try {
                Cursor cursor = context.getContentResolver().query(uri, new String[]{"_data"}, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    @Override
    public void doLogout() {

        if (foreGround) {

            pref.setBooleanValue(Constant.isLogin, false);
            pref.setBooleanValue(Constant.isGuestLogin, false);

            Intent intent = new Intent(this, loginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            this.finish();
        } else {
            logout = "true";
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogOutTimerUtil.startLogoutTimer(this, this);
        Log.e("TAG", "OnStart () &&& Starting timer");

        if (logout.equals("true")) {

            logout = "false";

//redirect user to login screen

            pref.setBooleanValue(Constant.isLogin, false);
            pref.setBooleanValue(Constant.isGuestLogin, false);

            Intent intent = new Intent(this, loginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            this.finish();
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

        if (logout.equals("true")) {

            logout = "false";

//redirect user to login screen
            pref.setBooleanValue(Constant.isLogin, false);
            pref.setBooleanValue(Constant.isGuestLogin, false);

            Intent intent = new Intent(this, loginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            this.finish();
        }
    }
}
