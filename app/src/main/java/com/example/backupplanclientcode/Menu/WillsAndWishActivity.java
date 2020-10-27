package com.example.backupplanclientcode.Menu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
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
import android.widget.ToggleButton;

import com.example.backupplanclientcode.Asyntask.GeneralTask;
import com.example.backupplanclientcode.Asyntask.GeneralTask.ResponseListener_General;
import com.example.backupplanclientcode.Asyntask.SaveProfileAsytask;
import com.example.backupplanclientcode.Asyntask.SaveProfileAsytask.ResponseListerProfile;
import com.example.backupplanclientcode.Bugsense.Bugsense;
import com.example.backupplanclientcode.ConnectionDetector;
import com.example.backupplanclientcode.Constant.Constant;
import com.example.backupplanclientcode.Preference.SettingPreference;
import com.example.backupplanclientcode.R;
import com.example.backupplanclientcode.ServiceUrl.ServiceUrl;
import com.example.backupplanclientcode.Utility.CompressImage;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

public class WillsAndWishActivity extends Activity implements OnClickListener, ResponseListerProfile, ResponseListener_General {
    private static final int SELECT_PICTURE = 1;
    TextView actionBarTittle;
    Button btn_back;
    Button btn_save;
    String collectImages = "";
    CompressImage compress;
    ConnectionDetector connection;
    ImageView currentImageVew;
    String delete_images = "";
    EditText editLocation;
    EditText edit_wills_id;
    EditText edit_wishes_id;
    LinearLayout layout_images;
    ArrayList<HashMap<String, String>> list_images;
    SettingPreference pref;
    TextView url1;
    TextView url2;
    ToggleButton yesNoHaveHealth;
    ToggleButton yesNoHaveLiving;
    ToggleButton yesNoHaveWill;
    ToggleButton yesNoPowerAttorney;
    ToggleButton yesNoService;
    ToggleButton yesNoUpDate;
    ToggleButton yesNohaveTrust;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wills);
        this.pref = new SettingPreference(getApplicationContext());
        this.compress = new CompressImage(getApplicationContext());
        this.connection = new ConnectionDetector(getApplicationContext());
        new Bugsense().startBugsense(getApplicationContext());
        findViewId();
        checkAlredySaveAccount();
    }

    private void findViewId() {
        this.list_images = new ArrayList<>();
        this.list_images.clear();
        this.btn_save = (Button) findViewById(R.id.btn_save);
        this.btn_back = (Button) findViewById(R.id.btn_back);
        this.actionBarTittle = (TextView) findViewById(R.id.actionBarTittle);
        this.btn_save.setOnClickListener(this);
        this.btn_back.setOnClickListener(this);
        this.yesNoHaveWill = (ToggleButton) findViewById(R.id.yesNoHaveWill);
        this.yesNohaveTrust = (ToggleButton) findViewById(R.id.yesNohaveTrust);
        this.yesNoUpDate = (ToggleButton) findViewById(R.id.yesNoUpDate);
        this.yesNoHaveLiving = (ToggleButton) findViewById(R.id.yesNoHaveLiving);
        this.yesNoPowerAttorney = (ToggleButton) findViewById(R.id.yesNoPowerAttorney);
        this.yesNoHaveHealth = (ToggleButton) findViewById(R.id.yesNoHaveHealth);
        this.yesNoService = (ToggleButton) findViewById(R.id.yesNoService);
        this.edit_wills_id = (EditText) findViewById(R.id.edit_wills_id);
        this.editLocation = (EditText) findViewById(R.id.editLocation);
        this.edit_wishes_id = (EditText) findViewById(R.id.edit_wishes_id);
        this.layout_images = (LinearLayout) findViewById(R.id.layout_images);
        this.url1 = (TextView) findViewById(R.id.url1);
        this.url1.setOnClickListener(this);
        this.url2 = (TextView) findViewById(R.id.url2);
        this.url2.setOnClickListener(this);
    }

    private void setEnableControl() {
        this.actionBarTittle.setText(getResources().getString(R.string.menu_will_wishes));
        this.btn_save.setVisibility(View.GONE);
        this.yesNoHaveWill.setEnabled(false);
        this.yesNohaveTrust.setEnabled(false);
        this.yesNoUpDate.setEnabled(false);
        this.yesNoHaveLiving.setEnabled(false);
        this.yesNoPowerAttorney.setEnabled(false);
        this.yesNoHaveHealth.setEnabled(false);
        this.yesNoService.setEnabled(false);
        this.edit_wills_id.setEnabled(false);
        this.editLocation.setEnabled(false);
        this.edit_wishes_id.setEnabled(false);
        for (int i = 0; i < this.layout_images.getChildCount(); i++) {
            ((Button) ((ViewGroup) this.layout_images.getChildAt(i)).findViewById(R.id.btn_remove)).setVisibility(View.GONE);
        }
    }

    private void checkAlredySaveAccount() {
//        if (this.pref.getStringValue(Constant.WillsAndWishesFalg, "").equalsIgnoreCase("1")) {
//            this.btn_save.setText("Edit");
//            if (!this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
//                this.actionBarTittle.setText("Edit " + getResources().getString(R.string.menu_will_wishes));
//            }
//            if (this.connection.isConnectingToInternet()) {
//                try {
//                    JSONObject nameValuePair = new JSONObject();
//                    nameValuePair.put("user_id", this.pref.getStringValue(Constant.user_id, ""));
//                nameValuePair.put("token", this.pref.getStringValue(Constant.jwttoken, ""));
//                    new GeneralTask(this, ServiceUrl.get_wills_detail, nameValuePair, 2, "post").execute(new Void[0]);
//                } catch (Exception e) {
//                }
//            } else {
//                displayMessage(getResources().getString(R.string.connectionFailMessage));
//            }
//        } else {
        this.actionBarTittle.setText(getResources().getString(R.string.menu_will_wishes));
        addMoreImages();
//        }
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            setEnableControl();
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == -1 && requestCode == 1) {
//            Uri selectedImageUri = data.getData();
//            String[] filePathColumn = {"_data"};
//            Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
//            cursor.moveToFirst();
//            String picturePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
//            cursor.close();
//            this.currentImageVew.setImageBitmap(this.compress.compressImage(selectedImageUri.toString(), picturePath));
//            if (this.currentImageVew.getTag().toString().equalsIgnoreCase("image")) {
//                Log.e("equalsIgnoreCase(image)", "......if");
//                if (this.currentImageVew.getContentDescription().toString().equalsIgnoreCase("")) {
//                    Log.e("!equalsIgnoreCase()", "......if");
//                    this.currentImageVew.setContentDescription(picturePath.toString());
//                    addMoreImages();
//                    return;
//                }
//                this.currentImageVew.setContentDescription(picturePath.toString());
//                Log.e("!equalsIgnoreCase()", "......else");
//            }
//        }
        if (resultCode == -1 && requestCode == 1) {
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

            this.currentImageVew.setImageBitmap(selectedImage);
            this.currentImageVew.setContentDescription(cursor.getString(nameIndex));
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
            this.currentImageVew.setContentDescription(finalFile.getPath());

            Log.d("test", "selectedImage " + selectedImage);
            Log.d("test", "imageUri.getPath() " + imageUri.getPath());

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

    @SuppressLint({"InflateParams"})
    private void addMoreImages() {
        final View moreImagesLayout = LayoutInflater.from(this).inflate(R.layout.layout_images, null);
        final ImageView iv = (ImageView) moreImagesLayout.findViewById(R.id.iv);
        iv.setTag("image");
        iv.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                WillsAndWishActivity.this.currentImageVew = iv;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                WillsAndWishActivity.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
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
        } else {
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
        this.layout_images.addView(moreImagesLayout);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save /*2131558589*/:
                prePareWillsJson();
                return;
            case R.id.btn_back /*2131558590*/:
                finish();
                return;
            case R.id.url1 /*2131558856*/:
                Intent i = new Intent("android.intent.action.VIEW");
                i.setData(Uri.parse("https://w3.legalshield.com/aasites/Multisite?site=hub&assoc=tinaolexa"));
                startActivity(i);
                return;
            case R.id.url2 /*2131558861*/:
                Intent i2 = new Intent("android.intent.action.VIEW");
                i2.setData(Uri.parse("http://www.yourbackupplan.ca/#!uploads/cw5j"));
                startActivity(i2);
                return;
            default:
                return;
        }
    }

    private void prePareWillsJson() {
        try {
            JSONObject willsJson = new JSONObject();
            willsJson.put("user_id", this.pref.getStringValue(Constant.user_id, ""));
            willsJson.put("wills_id", this.edit_wills_id.getText().toString().trim());
            willsJson.put("wishes_id", this.edit_wishes_id.getText().toString().trim());
            if (this.yesNoHaveWill.isChecked()) {
                willsJson.put("is_wills", "1");
            } else {
                willsJson.put("is_wills", "0");
            }
            if (this.yesNohaveTrust.isChecked()) {
                willsJson.put("is_trust", "1");
            } else {
                willsJson.put("is_trust", "0");
            }
            if (this.yesNoUpDate.isChecked()) {
                willsJson.put("is_up_date", "1");
            } else {
                willsJson.put("is_up_date", "0");
            }
            if (this.yesNoHaveLiving.isChecked()) {
                willsJson.put("is_living_will", "1");
            } else {
                willsJson.put("is_living_will", "0");
            }
            if (this.yesNoPowerAttorney.isChecked()) {
                willsJson.put("is_power_atto", "1");
            } else {
                willsJson.put("is_power_atto", "0");
            }
            if (this.yesNoHaveHealth.isChecked()) {
                willsJson.put("is_health_direct", "1");
            } else {
                willsJson.put("is_health_direct", "0");
            }
            if (this.yesNoService.isChecked()) {
                willsJson.put("w_is_service", "1");
            } else {
                willsJson.put("w_is_service", "0");
            }
            for (int i = 0; i < this.layout_images.getChildCount(); i++) {
                ImageView iv = (ImageView) ((ViewGroup) this.layout_images.getChildAt(i)).findViewById(R.id.iv);
                if (!iv.getContentDescription().toString().equalsIgnoreCase("")) {
                    HashMap<String, String> item_map = new HashMap<>();
                    item_map.put("image_path", iv.getContentDescription().toString());
                    item_map.put("image_name", "image" + i);
                    this.list_images.add(item_map);
                    Log.i("images :", this.list_images.toString());
                    if (this.collectImages.equalsIgnoreCase("")) {
                        this.collectImages = this.collectImages.concat(iv.getContentDescription().toString());
                    } else {
                        this.collectImages = this.collectImages.concat(",image" + i);
                    }
                }
                if (!iv.getTag().toString().trim().equalsIgnoreCase("")) {
                    if (this.delete_images.equalsIgnoreCase("")) {
                        this.delete_images = this.delete_images.concat(iv.getTag().toString());
                    } else {
                        this.delete_images = this.delete_images.concat("," + iv.getTag().toString());
                    }
                }
            }
            willsJson.put("wishes_images", "a.jpg");//this.collectImages);
            willsJson.put("w_location", this.editLocation.getText().toString().trim());
            willsJson.put("delete_images", this.delete_images);
            MultipartEntity entity = new MultipartEntity();
            JSONObject sendJson = new JSONObject();
            sendJson.put("wills_data", willsJson);
            entity.addPart("json_data", new StringBody(sendJson.toString()));
            for (int i2 = 0; i2 < this.list_images.size(); i2++) {
//                entity.addPart((String) ((HashMap) this.list_images.get(i2)).get("image_name"), new FileBody(new File((String) ((HashMap) this.list_images.get(i2)).get("image_path"))));
                entity.addPart("wishes_images[]", new FileBody(new File((String) ((HashMap) this.list_images.get(i2)).get("image_path"))));
                Log.e("send image path :", (String) ((HashMap) this.list_images.get(i2)).get("image_path"));
            }
            Log.e("send wills json :", sendJson.toString());
            Log.i("images :", this.list_images.toString());
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("json_data", sendJson.toString()));
            for (int i2 = 0; i2 < this.list_images.size(); i2++) {
                nameValuePairs.add((new BasicNameValuePair((String) ((HashMap) this.list_images.get(i2)).get("image_name"), new FileBody(new File((String) ((HashMap) this.list_images.get(i2)).get("image_path"))).getFilename())));
            }
            if (!this.connection.isConnectingToInternet()) {
                displayMessage(getResources().getString(R.string.connectionFailMessage));
            } else if (this.btn_save.getText().toString().trim().equalsIgnoreCase("edit")) {
                new SaveProfileAsytask(this, ServiceUrl.edit_wills, entity).execute(new Void[0]);
            } else {
                new SaveProfileAsytask(this, ServiceUrl.save_wills, entity).execute(new Void[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void on_GeneralSuccess(JSONObject response, int responseCode) {
        if (response == null) {
            addMoreImages();
        } else {
            show_wills(response);
        }
    }

    @SuppressLint({"InflateParams"})
    private void show_wills(JSONObject response) {
        try {
//            JSONObject json1 = response.getJSONObject("wills");
            JSONArray jsona = response.getJSONArray("wills");
            JSONObject json = jsona.getJSONObject(0);
            if (response.has("wills")) {
                if (json.getString("is_wills").equalsIgnoreCase("1")) {
                    this.yesNoHaveWill.setChecked(true);
                } else {
                    this.yesNoHaveWill.setChecked(false);
                }
                if (json.getString("is_trust").equalsIgnoreCase("1")) {
                    this.yesNohaveTrust.setChecked(true);
                } else {
                    this.yesNohaveTrust.setChecked(false);
                }
                if (json.getString("is_up_date").equalsIgnoreCase("1")) {
                    this.yesNoUpDate.setChecked(true);
                } else {
                    this.yesNoUpDate.setChecked(false);
                }
                if (json.getString("is_living_will").equalsIgnoreCase("1")) {
                    this.yesNoHaveLiving.setChecked(true);
                } else {
                    this.yesNoHaveLiving.setChecked(false);
                }
                if (json.getString("is_power_atto").equalsIgnoreCase("1")) {
                    this.yesNoPowerAttorney.setChecked(true);
                } else {
                    this.yesNoPowerAttorney.setChecked(false);
                }
                if (json.getString("is_health_direct").equalsIgnoreCase("1")) {
                    this.yesNoHaveHealth.setChecked(true);
                } else {
                    this.yesNoHaveHealth.setChecked(false);
                }
                if (json.getString("w_is_service").equalsIgnoreCase("1")) {
                    this.yesNoService.setChecked(true);
                } else {
                    this.yesNoService.setChecked(false);
                }
                this.edit_wills_id.setText(json.getString("wills_id").toString().trim());
                this.editLocation.setText(json.getString("w_location").toString().trim());
                JSONArray wishes_images = json.getJSONArray("wishes_images");
                for (int i = 0; i < wishes_images.length(); i++) {
                    JSONObject imageDetail = wishes_images.getJSONObject(i);
                    final View moreImagesLayout = LayoutInflater.from(this).inflate(R.layout.layout_images, null);
                    final ImageView iv = (ImageView) moreImagesLayout.findViewById(R.id.iv);
                    iv.setTag(imageDetail.getString("image_id").toString().trim());
                    UrlImageViewHelper.setUrlDrawable(iv, imageDetail.getString("image").toString().trim(), (int) R.drawable.img);
                    iv.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            WillsAndWishActivity.this.currentImageVew = iv;
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction("android.intent.action.GET_CONTENT");
                            WillsAndWishActivity.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                        }
                    });
                    Button btn_remove = (Button) moreImagesLayout.findViewById(R.id.btn_remove);
                    btn_remove.setVisibility(View.VISIBLE);
                    btn_remove.setTag(imageDetail.getString("image_id").toString().trim());
                    btn_remove.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            ((ViewGroup) moreImagesLayout.getParent()).removeView(moreImagesLayout);
                            if (WillsAndWishActivity.this.delete_images.equalsIgnoreCase("")) {
                                WillsAndWishActivity.this.delete_images = WillsAndWishActivity.this.delete_images.concat(iv.getTag().toString());
                                return;
                            }
                            WillsAndWishActivity.this.delete_images = WillsAndWishActivity.this.delete_images.concat("," + iv.getTag().toString());
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
            e.printStackTrace();
        }
    }

    public void on_ProfileSuccess(JSONObject response) {
        try {
            if (response.has("message")) {
                displayMessage(response.getString("message").toString());
            }
            if (response.has("success") && response.getString("success").toString().trim().equalsIgnoreCase("1")) {
                this.pref.setStringValue(Constant.WillsAndWishesFalg, response.getString("success").toString());
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayMessage(String string) {
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG).show();
    }
}