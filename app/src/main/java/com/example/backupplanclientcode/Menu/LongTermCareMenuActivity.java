package com.example.backupplanclientcode.Menu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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
import com.example.backupplanclientcode.Utility.CompressImage;
import com.example.backupplanclientcode.loginActivity;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.backupplanclientcode.LogOutTimerUtil.foreGround;
import static com.example.backupplanclientcode.LogOutTimerUtil.logout;

public class LongTermCareMenuActivity extends Activity implements OnClickListener, ResponseListerProfile, ResponseListener_General, LogOutTimerUtil.LogOutListener {
    private static final int EDIT_PICTURE = 2;
    private static final int SELECT_PICTURE = 1;
    TextView actionBarTittle;
    Button btn_back;
    Button btn_save;
    String collectImages = "";
    CompressImage compress;
    ConnectionDetector connection;
    ImageView currentImageVew;
    String delete_images = "";
    LinearLayout layout_images;
    ArrayList<HashMap<String, String>> list_images;
    String long_term_id = "";
    SettingPreference pref;
    TextView tv_tip;
    ToggleButton yesNoLongTermCare;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_long_term_care);
        this.pref = new SettingPreference(getApplicationContext());
        this.compress = new CompressImage(getApplicationContext());
        this.connection = new ConnectionDetector(getApplicationContext());
        new Bugsense().startBugsense(getApplicationContext());
        findViewId();
        checkAlredySaveAccount();
    }

    private void checkAlredySaveAccount() {
        if (this.pref.getStringValue(Constant.long_term_id, "").isEmpty() || this.pref.getStringValue(Constant.user_id, "").isEmpty()) {
            this.actionBarTittle.setText(getResources().getString(R.string.menu_longTermCare));
            addMoreImages();
        } else {
            this.btn_save.setText("Edit");
            if (!this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                this.actionBarTittle.setText("Edit " + getResources().getString(R.string.menu_longTermCare));
            }
            if (this.connection.isConnectingToInternet()) {
                try {
                    JSONObject nameValuePair = new JSONObject();
                    nameValuePair.put("user_id", "2");// this.pref.getStringValue(Constant.user_id, ""));
                    nameValuePair.put("token", this.pref.getStringValue(Constant.jwttoken, ""));
//                    nameValuePair.put("long_term_id", "2");//this.pref.getStringValue(Constant.long_term_id, ""));
                    new GeneralTask(this, ServiceUrl.get_long_term_care_detail, nameValuePair, 2, "post").execute(new Void[0]);
                } catch (Exception e) {
                }
            } else {
                displayMessage(getResources().getString(R.string.connectionFailMessage));
            }
        }
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            setEnableControl();
        }
    }

    private void findViewId() {
        this.list_images = new ArrayList<>();
        this.list_images.clear();
        this.btn_save = (Button) findViewById(R.id.btn_save);
        this.btn_back = (Button) findViewById(R.id.btn_back);
        this.btn_save.setOnClickListener(this);
        this.btn_back.setOnClickListener(this);
        this.layout_images = (LinearLayout) findViewById(R.id.layout_images);
        this.yesNoLongTermCare = (ToggleButton) findViewById(R.id.yesNoLongTermCare);
        this.yesNoLongTermCare.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    LongTermCareMenuActivity.this.layout_images.setVisibility(View.VISIBLE);
                } else {
                    LongTermCareMenuActivity.this.layout_images.setVisibility(View.GONE);
                }
            }
        });
        this.actionBarTittle = (TextView) findViewById(R.id.actionBarTittle);
        this.tv_tip = (TextView) findViewById(R.id.tv_tip);
        this.tv_tip.setOnClickListener(this);
    }

    private void setEnableControl() {
        this.actionBarTittle.setText(getResources().getString(R.string.menu_longTermCare));
        this.btn_save.setVisibility(View.GONE);
        this.yesNoLongTermCare.setEnabled(false);
        for (int i = 0; i < this.layout_images.getChildCount(); i++) {
            ((Button) ((ViewGroup) this.layout_images.getChildAt(i)).findViewById(R.id.btn_remove)).setVisibility(View.GONE);
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 1) {
            Uri selectedImageUri = data.getData();
            String[] filePathColumn = {"_data"};
            Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            String picturePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
            cursor.close();
            this.currentImageVew.setImageBitmap(this.compress.compressImage(selectedImageUri.toString(), picturePath));
            if (this.currentImageVew.getTag().toString().equalsIgnoreCase("image")) {
                if (this.currentImageVew.getContentDescription().toString().equalsIgnoreCase("")) {
                    this.currentImageVew.setContentDescription(picturePath.toString());
                    addMoreImages();
                } else {
                    this.currentImageVew.setContentDescription(picturePath.toString());
                }
            }
        }
        if (resultCode == -1 && requestCode == 2) {
            Uri selectedImageUri2 = data.getData();
            String[] filePathColumn2 = {"_data"};
            Cursor cursor2 = getContentResolver().query(selectedImageUri2, filePathColumn2, null, null, null);
            cursor2.moveToFirst();
            String picturePath2 = cursor2.getString(cursor2.getColumnIndex(filePathColumn2[0]));
            cursor2.close();
            this.currentImageVew.setImageBitmap(this.compress.compressImage(selectedImageUri2.toString(), picturePath2));
            this.currentImageVew.setContentDescription(picturePath2.toString());
            addMoreImages();
        }
    }

    @SuppressLint({"InflateParams"})
    private void addMoreImages() {
        final View moreImagesLayout = LayoutInflater.from(this).inflate(R.layout.layout_images, null);
        this.layout_images.addView(moreImagesLayout);
        final ImageView iv = (ImageView) moreImagesLayout.findViewById(R.id.iv);
        iv.setTag("image");
        iv.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LongTermCareMenuActivity.this.currentImageVew = iv;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                LongTermCareMenuActivity.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        Button btn_remove = (Button) moreImagesLayout.findViewById(R.id.btn_remove);
        btn_remove.setVisibility(View.VISIBLE);
        btn_remove.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ((ViewGroup) moreImagesLayout.getParent()).removeView(moreImagesLayout);
            }
        });
        btn_remove.setVisibility(View.GONE);
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            btn_remove.setVisibility(View.GONE);
            iv.setEnabled(false);
            return;
        }
        for (int i = 0; i < this.layout_images.getChildCount(); i++) {
            ViewGroup child = (ViewGroup) this.layout_images.getChildAt(i);
            Button removeBTN = (Button) child.findViewById(R.id.btn_remove);
            if (!((ImageView) child.findViewById(R.id.iv)).getContentDescription().toString().trim().isEmpty() || !removeBTN.getTag().toString().isEmpty()) {
                removeBTN.setVisibility(View.VISIBLE);
            } else {
                removeBTN.setVisibility(View.GONE);
            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save /*2131558589*/:
                prePareJson();
                return;
            case R.id.btn_back /*2131558590*/:
                finish();
                return;
            case R.id.tv_tip /*2131558811*/:
                Intent i = new Intent("android.intent.action.VIEW");
                i.setData(Uri.parse("http://www.yourbackupplan.ca/#!LONG�TERM�CARE-PLANNING/c126t/ThumbListItem1_id2qpiwe4_1"));
                startActivity(i);
                return;
            default:
                return;
        }
    }

    private void prePareJson() {
        try {
            this.collectImages = "";
            JSONObject json = new JSONObject();
            json.put("user_id", this.pref.getStringValue(Constant.user_id, ""));
            json.put("long_term_id", this.long_term_id);
            if (this.yesNoLongTermCare.isChecked()) {
                json.put("is_long_term_policy", "1");
            } else {
                json.put("is_long_term_policy", "0");
            }
            for (int i = 0; i < this.layout_images.getChildCount(); i++) {
                ImageView iv = (ImageView) ((ViewGroup) this.layout_images.getChildAt(i)).findViewById(R.id.iv);
                if (!iv.getContentDescription().toString().trim().isEmpty()) {
                    HashMap<String, String> item_map = new HashMap<>();
                    item_map.put("image_path", iv.getContentDescription().toString());
                    item_map.put("image_name", "image" + i);
                    this.list_images.add(item_map);
                    if (this.collectImages.equalsIgnoreCase("")) {
                        this.collectImages = this.collectImages.concat("image" + i);
                    } else {
                        this.collectImages = this.collectImages.concat(",image" + i);
                    }
                    if (!iv.getTag().toString().trim().isEmpty()) {
                        if (this.delete_images.equalsIgnoreCase("")) {
                            this.delete_images = this.delete_images.concat(iv.getTag().toString().trim());
                        } else {
                            this.delete_images = this.delete_images.concat("," + iv.getTag().toString().trim());
                        }
                    }
                }
            }
            json.put("long_term_care_images", this.collectImages);
            json.put("delete_images", this.delete_images);
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            JSONObject sendJson = new JSONObject();
            sendJson.put("long_care_data", json);
            entity.addPart("json_data", new StringBody(sendJson.toString()));
            for (int i2 = 0; i2 < this.list_images.size(); i2++) {
                entity.addPart((String) ((HashMap) this.list_images.get(i2)).get("image_name"), new FileBody(new File((String) ((HashMap) this.list_images.get(i2)).get("image_path")), "image/jpeg"));
            }
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("json_data", sendJson.toString()));
            Log.e("list of images :", this.list_images.toString());
            Log.e("send json :", sendJson.toString());
            if (!this.connection.isConnectingToInternet()) {
                displayMessage(getResources().getString(R.string.connectionFailMessage));
            } else if (this.btn_save.getText().toString().trim().equalsIgnoreCase("edit")) {
                new SaveProfileAsytask(this, ServiceUrl.edit_long_term_care, nameValuePairs).execute(new Void[0]);
            } else {
                new SaveProfileAsytask(this, ServiceUrl.save_long_term_care, nameValuePairs).execute(new Void[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint({"InflateParams"})
    public void on_GeneralSuccess(JSONObject response, int responseCode) {
        try {
            if (response == null) {
                try {
                    addMoreImages();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (response.has("success") && response.getString("success").equalsIgnoreCase("1")) {
                JSONObject json = response.getJSONObject("long_term_care");
                if (json.getString("is_long_term_policy").equalsIgnoreCase("1")) {
                    this.yesNoLongTermCare.setChecked(true);
                } else {
                    this.yesNoLongTermCare.setChecked(false);
                }
                this.long_term_id = json.getString("long_term_id").toString().trim();
                JSONArray jsonArray = json.getJSONArray("long_care_images");
                for (int i = 0; i < jsonArray.length(); i++) {
                    final View moreImagesLayout = LayoutInflater.from(this).inflate(R.layout.layout_images, null);
                    JSONObject jsonImages = jsonArray.getJSONObject(i);
                    final ImageView iv = (ImageView) moreImagesLayout.findViewById(R.id.iv);
                    UrlImageViewHelper.setUrlDrawable(iv, jsonImages.getString("image").toString().trim(), (int) R.drawable.img);
                    iv.setTag(jsonImages.getString("image_id").toString().trim());
                    iv.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            LongTermCareMenuActivity.this.currentImageVew = iv;
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction("android.intent.action.GET_CONTENT");
                            LongTermCareMenuActivity.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
                        }
                    });
                    final Button btn_remove = (Button) moreImagesLayout.findViewById(R.id.btn_remove);
                    btn_remove.setVisibility(View.VISIBLE);
                    btn_remove.setTag(jsonImages.getString("image_id").toString().trim());
                    btn_remove.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            ((ViewGroup) moreImagesLayout.getParent()).removeView(moreImagesLayout);
                            if (LongTermCareMenuActivity.this.delete_images.equalsIgnoreCase("")) {
                                LongTermCareMenuActivity.this.delete_images = LongTermCareMenuActivity.this.delete_images.concat(btn_remove.getTag().toString().trim());
                                return;
                            }
                            LongTermCareMenuActivity.this.delete_images = LongTermCareMenuActivity.this.delete_images.concat("," + btn_remove.getTag().toString().trim());
                        }
                    });
                    if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                        btn_remove.setVisibility(View.GONE);
                        iv.setEnabled(false);
                    }
                    this.layout_images.addView(moreImagesLayout);
                }
                addMoreImages();
            }
        } catch (Exception e) {

        }
    }

    private void displayMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void on_ProfileSuccess(JSONObject response) {
        try {
            if (response.has("message")) {
                displayMessage(response.getString("message").toString());
            }
            if (response.has("success") && response.getString("success").toString().trim().equalsIgnoreCase("1")) {
                if (response.has("long_term_id")) {
                    this.pref.setStringValue(Constant.long_term_id, response.getString("long_term_id").toString());
                }
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doLogout() {

        if(foreGround){

            pref.setBooleanValue(Constant.isLogin, false);
            pref.setBooleanValue(Constant.isGuestLogin, false);
            startActivity(new Intent(getApplicationContext(), loginActivity.class));
            finish();

        }else {
            logout = "true";
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        LogOutTimerUtil.startLogoutTimer(this, this);
        Log.e("TAG", "OnStart () &&& Starting timer");

        if(logout.equals("true")){

            logout = "false";

            //redirect user to login screen

            pref.setBooleanValue(Constant.isLogin, false);
            pref.setBooleanValue(Constant.isGuestLogin, false);
            startActivity(new Intent(getApplicationContext(), loginActivity.class));
            finish();
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

        if(logout.equals("true")){

            logout = "false";

            //redirect user to login screen
            pref.setBooleanValue(Constant.isLogin, false);
            pref.setBooleanValue(Constant.isGuestLogin, false);
            startActivity(new Intent(getApplicationContext(), loginActivity.class));
            finish();
        }
    }
}
