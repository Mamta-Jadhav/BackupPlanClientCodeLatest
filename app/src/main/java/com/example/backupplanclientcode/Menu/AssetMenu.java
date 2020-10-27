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
import org.json.JSONObject;

public class AssetMenu extends Activity implements OnClickListener, ResponseListerProfile, ResponseListener_General {
    private static final int SELECT_PICTURE = 1;
    TextView actionBarTittle;
    ImageView addAutoIcon;
    ImageView addBoatIcon;
    ImageView addRealEstateIcon;
    JSONObject asset;
    LinearLayout autolayout;
    LinearLayout boatlayout;
    Button btn_back;
    Button btn_save;
    CompressImage compress;
    ConnectionDetector connection;
    ImageView currentImageVew;
    String delete_autos = "";
    String delete_boat = "";
    String delete_real_estate = "";
    ArrayList<HashMap<String, String>> list_images;
    SettingPreference pref;
    LinearLayout realEstateLayout;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_menu_layout);
        intilization();
        findviewId();
        checkAssetEdit();
    }

    private void intilization() {
        this.pref = new SettingPreference(getApplicationContext());
        this.compress = new CompressImage(getApplicationContext());
        this.connection = new ConnectionDetector(getApplicationContext());
        new Bugsense().startBugsense(getApplicationContext());
    }

    private void checkAssetEdit() {
        if (this.pref.getStringValue(Constant.assetFlag, "").equalsIgnoreCase("1")) {
            this.btn_save.setText("Edit");
            if (!this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                this.actionBarTittle.setText("Edit " + getResources().getString(R.string.menu_asset));
            }
            if (this.connection.isConnectingToInternet()) {
                try {
                    JSONObject nameValuePair = new JSONObject();
                    nameValuePair.put("user_id", this.pref.getStringValue(Constant.user_id, ""));//2
                    nameValuePair.put("token", this.pref.getStringValue(Constant.jwttoken, ""));
                    new GeneralTask(this, ServiceUrl.get_assets_detail, nameValuePair, 2, "post").execute(new Void[0]);
                } catch (Exception e) {
                }
                return;
            }
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.connectionFailMessage), Toast.LENGTH_SHORT).show();
            return;
        }
        this.actionBarTittle.setText(getResources().getString(R.string.menu_asset));
        addRealEstatelayout();
        addAutoLayout();
        addBoatLayout();
    }

    private void findviewId() {
        this.list_images = new ArrayList<>();
        this.list_images.clear();
        this.asset = new JSONObject();
        this.btn_save = (Button) findViewById(R.id.btn_save);
        this.btn_save.setOnClickListener(this);
        this.btn_back = (Button) findViewById(R.id.btn_back);
        this.btn_back.setOnClickListener(this);
        this.actionBarTittle = (TextView) findViewById(R.id.actionBarTittle);
        this.addBoatIcon = (ImageView) findViewById(R.id.addBoatIcon);
        this.addAutoIcon = (ImageView) findViewById(R.id.addAutoIcon);
        this.addRealEstateIcon = (ImageView) findViewById(R.id.addRealEstateIcon);
        this.addBoatIcon.setOnClickListener(this);
        this.addAutoIcon.setOnClickListener(this);
        this.addRealEstateIcon.setOnClickListener(this);
        this.realEstateLayout = (LinearLayout) findViewById(R.id.realEstateLayout);
        this.autolayout = (LinearLayout) findViewById(R.id.autolayout);
        this.boatlayout = (LinearLayout) findViewById(R.id.boatlayout);
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            setControlEnable();
        }
    }

    private void setControlEnable() {
        this.actionBarTittle.setText(getResources().getString(R.string.menu_asset));
        this.btn_save.setVisibility(View.GONE);
        this.addBoatIcon.setEnabled(false);
        this.addAutoIcon.setEnabled(false);
        this.addRealEstateIcon.setEnabled(false);
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
//            this.currentImageVew.setContentDescription(picturePath.toString());
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
    private void addBoatLayout() {
        final View boatLayout = LayoutInflater.from(this).inflate(R.layout.layout_boat, null);
        ((ImageView) boatLayout.findViewById(R.id.removeIcon)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AssetMenu.this.boatlayout.removeView(boatLayout);
            }
        });
        final ImageView img_boat = (ImageView) boatLayout.findViewById(R.id.img_boat);
        img_boat.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AssetMenu.this.currentImageVew = img_boat;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                AssetMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        EditText edit_boat_id = (EditText) boatLayout.findViewById(R.id.edit_boat_id);
        EditText edit_Make = (EditText) boatLayout.findViewById(R.id.edit_Make);
        EditText edit_Model = (EditText) boatLayout.findViewById(R.id.edit_Model);
        EditText edit_Year = (EditText) boatLayout.findViewById(R.id.edit_Year);
        EditText edit_Pair = (EditText) boatLayout.findViewById(R.id.edit_Pair);
        EditText edit_Value = (EditText) boatLayout.findViewById(R.id.edit_Value);
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            edit_boat_id.setEnabled(false);
            edit_Make.setEnabled(false);
            edit_Model.setEnabled(false);
            edit_Year.setEnabled(false);
            edit_Pair.setEnabled(false);
            edit_Value.setEnabled(false);
            img_boat.setEnabled(false);
        }
        this.boatlayout.addView(boatLayout);
    }

    @SuppressLint({"InflateParams"})
    private void addAutoLayout() {
        final View autoLayout = LayoutInflater.from(this).inflate(R.layout.layout_autos, null);
        ImageView removeIcon = (ImageView) autoLayout.findViewById(R.id.removeIcon);
        removeIcon.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AssetMenu.this.autolayout.removeView(autoLayout);
            }
        });
        final ImageView img_auto = (ImageView) autoLayout.findViewById(R.id.img_auto);
        img_auto.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AssetMenu.this.currentImageVew = img_auto;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                AssetMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        EditText edit_autos_id = (EditText) autoLayout.findViewById(R.id.edit_autos_id);
        EditText edit_Make = (EditText) autoLayout.findViewById(R.id.edit_Make);
        EditText edit_Model = (EditText) autoLayout.findViewById(R.id.edit_Model);
        EditText edit_Year = (EditText) autoLayout.findViewById(R.id.edit_Year);
        EditText edit_Pair = (EditText) autoLayout.findViewById(R.id.edit_Pair);
        EditText edit_Value = (EditText) autoLayout.findViewById(R.id.edit_Value);
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            img_auto.setEnabled(false);
            edit_autos_id.setEnabled(false);
            edit_Make.setEnabled(false);
            edit_Model.setEnabled(false);
            edit_Year.setEnabled(false);
            edit_Pair.setEnabled(false);
            edit_Value.setEnabled(false);
            removeIcon.setVisibility(View.GONE);
        }
        this.autolayout.addView(autoLayout);
    }

    @SuppressLint({"InflateParams"})
    private void addRealEstatelayout() {
        final View realEstate = LayoutInflater.from(this).inflate(R.layout.layout_real_estate, null);
        ImageView removeIcon = (ImageView) realEstate.findViewById(R.id.removeIcon);
        removeIcon.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AssetMenu.this.realEstateLayout.removeView(realEstate);
            }
        });
        final ImageView img_realEstate = (ImageView) realEstate.findViewById(R.id.img_realEstate);
        img_realEstate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AssetMenu.this.currentImageVew = img_realEstate;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                AssetMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        EditText edit_PropertyName = (EditText) realEstate.findViewById(R.id.edit_PropertyName);
        EditText edit_Address = (EditText) realEstate.findViewById(R.id.edit_Address);
        EditText edit_Register = (EditText) realEstate.findViewById(R.id.edit_Register);
        EditText edit_DateOfPurchase = (EditText) realEstate.findViewById(R.id.edit_DateOfPurchase);
        EditText edit_PricePaid = (EditText) realEstate.findViewById(R.id.edit_PricePaid);
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            img_realEstate.setEnabled(false);
            edit_PropertyName.setEnabled(false);
            edit_Address.setEnabled(false);
            edit_Register.setEnabled(false);
            edit_DateOfPurchase.setEnabled(false);
            edit_PricePaid.setEnabled(false);
            removeIcon.setVisibility(View.GONE);
        }
        this.realEstateLayout.addView(realEstate);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save /*2131558589*/:
                this.list_images.clear();
                this.asset = new JSONObject();
                PrepareRealEstateJson();
                PrepareAutoJson();
                PrepareBoatJson();
                PrepareSendJson();
                return;
            case R.id.btn_back /*2131558590*/:
                finish();
                return;
            case R.id.addRealEstateIcon /*2131558601*/:
                addRealEstatelayout();
                return;
            case R.id.addAutoIcon /*2131558603*/:
                addAutoLayout();
                return;
            case R.id.addBoatIcon /*2131558605*/:
                addBoatLayout();
                return;
            default:
                return;
        }
    }

    private void PrepareSendJson() {
        try {
            this.asset.put("user_id", this.pref.getStringValue(Constant.user_id, ""));
            this.asset.put("delete_real_estate", this.delete_real_estate);
            this.asset.put("delete_autos", this.delete_autos);
            this.asset.put("delete_boat", this.delete_boat);
            JSONObject sendAssetJson = new JSONObject();
            sendAssetJson.put("assets_data", this.asset);
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            entity.addPart("json_data", new StringBody(sendAssetJson.toString()));

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("json_data", sendAssetJson.toString()));
            for (int i = 0; i < this.list_images.size(); i++) {
                entity.addPart((String) ((HashMap) this.list_images.get(i)).get("image_name"), new FileBody(new File((String) ((HashMap) this.list_images.get(i)).get("image_path"))));
                nameValuePairs.add(new BasicNameValuePair((String) ((HashMap) this.list_images.get(i)).get("image_name"), new FileBody(new File((String) ((HashMap) this.list_images.get(i)).get("image_path"))).getFilename()));
                Log.i("file parameter", ((String) ((HashMap) this.list_images.get(i)).get("image_name")).toString());
                Log.i("file path", ((String) ((HashMap) this.list_images.get(i)).get("image_path")).toString());
            }
            if (this.btn_save.getText().toString().trim().equalsIgnoreCase("edit")) {
                new SaveProfileAsytask(this, ServiceUrl.edit_assets, entity).execute(new Void[0]);
            } else {
                new SaveProfileAsytask(this, ServiceUrl.save_assets, entity).execute(new Void[0]);
            }
            Log.e("send jon asset", sendAssetJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void PrepareBoatJson() {
        try {
            JSONArray boatArray = new JSONArray();
            for (int i = 0; i < this.boatlayout.getChildCount(); i++) {
                JSONObject jsonbj = new JSONObject();
                ViewGroup view = (ViewGroup) this.boatlayout.getChildAt(i);
                EditText edit_Make = (EditText) view.findViewById(R.id.edit_Make);
                EditText edit_Model = (EditText) view.findViewById(R.id.edit_Model);
                EditText edit_Year = (EditText) view.findViewById(R.id.edit_Year);
                EditText edit_Pair = (EditText) view.findViewById(R.id.edit_Pair);
                EditText edit_Value = (EditText) view.findViewById(R.id.edit_Value);
                EditText edit_boat_id = (EditText) view.findViewById(R.id.edit_boat_id);
                ImageView img_boat = (ImageView) view.findViewById(R.id.img_boat);
                if (img_boat.getContentDescription().toString().isEmpty()) {
                    jsonbj.put("b_photo", "");
                } else {
                    String[] arr = img_boat.getContentDescription().toString().split("/");
                    String atr = arr[arr.length - 1];
                    jsonbj.put("b_photo", atr);
                    HashMap<String, String> item_map = new HashMap<>();
                    item_map.put("image_path", img_boat.getContentDescription().toString());
                    item_map.put("image_name", "b_photo[]");
                    this.list_images.add(item_map);
                }
                jsonbj.put("boat_id", edit_boat_id.getText().toString().trim());
                jsonbj.put("b_make", edit_Make.getText().toString().trim());
                jsonbj.put("b_model", edit_Model.getText().toString().trim());
                jsonbj.put("b_year", edit_Year.getText().toString().trim());
                jsonbj.put("b_paid", edit_Pair.getText().toString().trim());
                jsonbj.put("b_value", edit_Value.getText().toString().trim());
                boatArray.put(jsonbj);
            }
            this.asset.put("boat", boatArray);
            Log.i("boats Array  :", boatArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void PrepareAutoJson() {
        try {
            JSONArray autoArray = new JSONArray();
            for (int i = 0; i < this.autolayout.getChildCount(); i++) {
                JSONObject jsonbj = new JSONObject();
                ViewGroup view = (ViewGroup) this.autolayout.getChildAt(i);
                EditText edit_Make = (EditText) view.findViewById(R.id.edit_Make);
                EditText edit_Model = (EditText) view.findViewById(R.id.edit_Model);
                EditText edit_Year = (EditText) view.findViewById(R.id.edit_Year);
                EditText edit_Pair = (EditText) view.findViewById(R.id.edit_Pair);
                EditText edit_Value = (EditText) view.findViewById(R.id.edit_Value);
                EditText edit_boat_id = (EditText) view.findViewById(R.id.edit_autos_id);
                ImageView img_auto = (ImageView) view.findViewById(R.id.img_auto);
                if (img_auto.getContentDescription().toString().isEmpty()) {
                    jsonbj.put("a_photo", "");
                } else {
                    String[] arr = img_auto.getContentDescription().toString().split("/");
                    String atr = arr[arr.length - 1];
                    jsonbj.put("a_photo", atr);
                    HashMap<String, String> item_map = new HashMap<>();
                    item_map.put("image_path", img_auto.getContentDescription().toString());
                    item_map.put("image_name", "a_photo[]");
                    this.list_images.add(item_map);
                }
                jsonbj.put("autos_id", edit_boat_id.getText().toString().trim());
                jsonbj.put("a_make", edit_Make.getText().toString().trim());
                jsonbj.put("a_model", edit_Model.getText().toString().trim());
                jsonbj.put("a_year", edit_Year.getText().toString().trim());
                jsonbj.put("a_paid", edit_Pair.getText().toString().trim());
                jsonbj.put("a_value", edit_Value.getText().toString().trim());
                autoArray.put(jsonbj);
            }
            this.asset.put("autos", autoArray);
            Log.i("autos Array  :", autoArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void PrepareRealEstateJson() {
        try {
            JSONArray RealStateArray = new JSONArray();
            for (int i = 0; i < this.realEstateLayout.getChildCount(); i++) {
                JSONObject jsonbj = new JSONObject();
                ViewGroup view = (ViewGroup) this.realEstateLayout.getChildAt(i);
                EditText edit_PropertyName = (EditText) view.findViewById(R.id.edit_PropertyName);
                EditText edit_Address = (EditText) view.findViewById(R.id.edit_Address);
                EditText edit_Register = (EditText) view.findViewById(R.id.edit_Register);
                EditText edit_DateOfPurchase = (EditText) view.findViewById(R.id.edit_DateOfPurchase);
                EditText edit_PricePaid = (EditText) view.findViewById(R.id.edit_PricePaid);
                EditText edit_realEstateId = (EditText) view.findViewById(R.id.edit_realEstateId);
                ImageView img_realEstate = (ImageView) view.findViewById(R.id.img_realEstate);
                if (img_realEstate.getContentDescription().toString().isEmpty()) {
                    jsonbj.put("r_photo", "");
                } else {
                    String[] arr = img_realEstate.getContentDescription().toString().split("/");
                    String atr = arr[arr.length - 1];
                    jsonbj.put("r_photo", atr);
                    HashMap<String, String> item_map = new HashMap<>();
                    item_map.put("image_path", img_realEstate.getContentDescription().toString());
                    item_map.put("image_name", "r_photo[]");
                    this.list_images.add(item_map);
                }
                jsonbj.put("real_estate_id", edit_realEstateId.getText().toString().trim());
                jsonbj.put("r_name", edit_PropertyName.getText().toString().trim());
                jsonbj.put("r_address", edit_Address.getText().toString().trim());
                jsonbj.put("r_owner", edit_Register.getText().toString().trim());
                jsonbj.put("r_purchase_date", edit_DateOfPurchase.getText().toString().trim());
                jsonbj.put("r_price", edit_PricePaid.getText().toString().trim());
                RealStateArray.put(jsonbj);
            }
            this.asset.put("real_estate", RealStateArray);
            Log.i("Real Estate Array  :", RealStateArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void on_ProfileSuccess(JSONObject response) {
        try {
            if (response.has("success")) {
                this.pref.setStringValue(Constant.assetFlag, "1");
                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void on_GeneralSuccess(JSONObject response, int responseCode) {
        show_RealEstate(response);
        show_Boat(response);
        show_Auto(response);
    }

    @SuppressLint({"InflateParams"})
    private void show_Auto(JSONObject response) {
        try {
            JSONArray asset_detail = response.getJSONObject("autos").getJSONArray("autos");
            if (asset_detail.length() < 1) {
                addRealEstatelayout();
                return;
            }
            for (int i = 0; i < asset_detail.length(); i++) {
                JSONObject json = asset_detail.getJSONObject(i);
                final View autoLayoutView = LayoutInflater.from(this).inflate(R.layout.layout_autos, null);
                ImageView removeIcon = (ImageView) autoLayoutView.findViewById(R.id.removeIcon);
                final ImageView img_auto = (ImageView) autoLayoutView.findViewById(R.id.img_auto);
                img_auto.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AssetMenu.this.currentImageVew = img_auto;
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction("android.intent.action.GET_CONTENT");
                        AssetMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                    }
                });
                UrlImageViewHelper.setUrlDrawable(img_auto, json.getString("a_photo").toString().trim(), (int) R.drawable.img);
                removeIcon.setTag(json.getString("auto_id").toString().trim());
                final ImageView imageView = removeIcon;
                removeIcon.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AssetMenu.this.autolayout.removeView(autoLayoutView);
                        if (AssetMenu.this.delete_autos.equalsIgnoreCase("")) {
                            AssetMenu.this.delete_autos = AssetMenu.this.delete_autos.concat(imageView.getTag().toString());
                            return;
                        }
                        AssetMenu.this.delete_autos = AssetMenu.this.delete_autos.concat("," + imageView.getTag().toString());
                    }
                });
                ((EditText) autoLayoutView.findViewById(R.id.edit_autos_id)).setText(json.getString("auto_id").toString().trim());
                EditText edit_Make = (EditText) autoLayoutView.findViewById(R.id.edit_Make);
                edit_Make.setText(json.getString("a_make").toString().trim());
                EditText edit_Model = (EditText) autoLayoutView.findViewById(R.id.edit_Model);
                edit_Model.setText(json.getString("a_model").toString().trim());
                EditText edit_Year = (EditText) autoLayoutView.findViewById(R.id.edit_Year);
                edit_Year.setText(json.getString("a_year").toString().trim());
                EditText edit_Pair = (EditText) autoLayoutView.findViewById(R.id.edit_Pair);
                edit_Pair.setText(json.getString("a_paid").toString().trim());
                EditText edit_Value = (EditText) autoLayoutView.findViewById(R.id.edit_Value);
                edit_Value.setText(json.getString("a_value").toString().trim());
                if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                    edit_Make.setEnabled(false);
                    edit_Model.setEnabled(false);
                    edit_Year.setEnabled(false);
                    edit_Pair.setEnabled(false);
                    edit_Value.setEnabled(false);
                    removeIcon.setVisibility(View.GONE);
                    img_auto.setEnabled(false);
                }
                this.autolayout.addView(autoLayoutView);
            }
        } catch (Exception e) {
            addAutoLayout();
            e.printStackTrace();
        }
    }

    @SuppressLint({"InflateParams"})
    private void show_Boat(JSONObject response) {
        try {
            JSONArray asset_detail = response.getJSONObject("boat").getJSONArray("boat");
            if (asset_detail.length() < 1) {
                addRealEstatelayout();
                return;
            }
            for (int i = 0; i < asset_detail.length(); i++) {
                JSONObject json = asset_detail.getJSONObject(i);
                final View boatLayoutView = LayoutInflater.from(this).inflate(R.layout.layout_boat, null);
                ImageView removeIcon = (ImageView) boatLayoutView.findViewById(R.id.removeIcon);
                final ImageView img_boat = (ImageView) boatLayoutView.findViewById(R.id.img_boat);
                img_boat.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AssetMenu.this.currentImageVew = img_boat;
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction("android.intent.action.GET_CONTENT");
                        AssetMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                    }
                });
                UrlImageViewHelper.setUrlDrawable(img_boat, json.getString("b_photo").toString().trim(), (int) R.drawable.img);
                removeIcon.setTag(json.getString("boat_id").toString().trim());
                final ImageView imageView = removeIcon;
                removeIcon.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AssetMenu.this.boatlayout.removeView(boatLayoutView);
                        if (AssetMenu.this.delete_boat.equalsIgnoreCase("")) {
                            AssetMenu.this.delete_boat = AssetMenu.this.delete_boat.concat(imageView.getTag().toString());
                            return;
                        }
                        AssetMenu.this.delete_boat = AssetMenu.this.delete_boat.concat("," + imageView.getTag().toString());
                    }
                });
                ((EditText) boatLayoutView.findViewById(R.id.edit_boat_id)).setText(json.getString("boat_id").toString().trim());
                EditText edit_Make = (EditText) boatLayoutView.findViewById(R.id.edit_Make);
                edit_Make.setText(json.getString("b_make").toString().trim());
                EditText edit_Model = (EditText) boatLayoutView.findViewById(R.id.edit_Model);
                edit_Model.setText(json.getString("b_model").toString().trim());
                EditText edit_Year = (EditText) boatLayoutView.findViewById(R.id.edit_Year);
                edit_Year.setText(json.getString("b_year").toString().trim());
                EditText edit_Pair = (EditText) boatLayoutView.findViewById(R.id.edit_Pair);
                edit_Pair.setText(json.getString("b_paid").toString().trim());
                EditText edit_Value = (EditText) boatLayoutView.findViewById(R.id.edit_Value);
                edit_Value.setText(json.getString("b_value").toString().trim());
                if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                    edit_Make.setEnabled(false);
                    edit_Model.setEnabled(false);
                    edit_Year.setEnabled(false);
                    edit_Pair.setEnabled(false);
                    edit_Value.setEnabled(false);
                    img_boat.setEnabled(false);
                }
                this.boatlayout.addView(boatLayoutView);
            }
        } catch (Exception e) {
            addBoatLayout();
            e.printStackTrace();
        }
    }

    @SuppressLint({"InflateParams"})
    private void show_RealEstate(JSONObject response) {
        try {
            JSONArray asset_detail = response.getJSONObject("real_estate").getJSONArray("real_estate");
            for (int i = 0; i < asset_detail.length(); i++) {
                JSONObject json = asset_detail.getJSONObject(i);
                View realEstateView = LayoutInflater.from(this).inflate(R.layout.layout_real_estate, null);
                ImageView removeIcon = (ImageView) realEstateView.findViewById(R.id.removeIcon);
                final ImageView img_realEstate = (ImageView) realEstateView.findViewById(R.id.img_realEstate);
                img_realEstate.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AssetMenu.this.currentImageVew = img_realEstate;
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction("android.intent.action.GET_CONTENT");
                        AssetMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                    }
                });
                UrlImageViewHelper.setUrlDrawable(img_realEstate, json.getString("r_photo").toString().trim(), (int) R.drawable.img);
                removeIcon.setTag(json.getString("real_estate_id").toString().trim());
                final View view = realEstateView;
                final ImageView imageView = removeIcon;
                removeIcon.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AssetMenu.this.realEstateLayout.removeView(view);
                        if (AssetMenu.this.delete_real_estate.equalsIgnoreCase("")) {
                            AssetMenu.this.delete_real_estate = AssetMenu.this.delete_real_estate.concat(imageView.getTag().toString());
                            return;
                        }
                        AssetMenu.this.delete_real_estate = AssetMenu.this.delete_real_estate.concat("," + imageView.getTag().toString());
                    }
                });
                ((EditText) realEstateView.findViewById(R.id.edit_realEstateId)).setText(json.getString("real_estate_id").toString().trim());
                EditText edit_PropertyName = (EditText) realEstateView.findViewById(R.id.edit_PropertyName);
                edit_PropertyName.setText(json.getString("r_name").toString().trim());
                EditText edit_Address = (EditText) realEstateView.findViewById(R.id.edit_Address);
                edit_Address.setText(json.getString("r_address").toString().trim());
                EditText edit_Register = (EditText) realEstateView.findViewById(R.id.edit_Register);
                edit_Register.setText(json.getString("r_owner").toString().trim());
                EditText edit_DateOfPurchase = (EditText) realEstateView.findViewById(R.id.edit_DateOfPurchase);
                edit_DateOfPurchase.setText(json.getString("r_purchase_date").toString().trim());
                EditText edit_PricePaid = (EditText) realEstateView.findViewById(R.id.edit_PricePaid);
                edit_PricePaid.setText(json.getString("r_price").toString().trim());
                if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                    edit_PropertyName.setEnabled(false);
                    edit_Address.setEnabled(false);
                    edit_Register.setEnabled(false);
                    edit_DateOfPurchase.setEnabled(false);
                    edit_PricePaid.setEnabled(false);
                    removeIcon.setVisibility(View.GONE);
                    img_realEstate.setEnabled(false);
                }
                this.realEstateLayout.addView(realEstateView);
            }
        } catch (Exception e) {
            addRealEstatelayout();
            e.printStackTrace();
        }
    }
}