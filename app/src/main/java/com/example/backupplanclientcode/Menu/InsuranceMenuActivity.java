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

public class InsuranceMenuActivity extends Activity implements OnClickListener, ResponseListerProfile, ResponseListener_General {
    private static final int SELECT_PICTURE = 1;
    TextView actionBarTittle;
    ImageView addAutoMobile;
    ImageView addHouse;
    JSONArray autoMobileJsonArray;
    Button btn_back;
    Button btn_save;
    CompressImage compress;
    ConnectionDetector connection;
    ImageView currentImageVew;
    String delete_automobile = "";
    String delete_household = "";
    String delete_insurance_img = "";
    String delete_policy_img = "";
    String delete_travel_coverage = "";
    String delete_travel_itinerary = "";
    EditText editCmp1;
    EditText editCmp2;
    EditText editCompany;
    EditText editInsuranceId1;
    EditText editInsuranceId2;
    EditText editInsured1;
    EditText editInsured2;
    EditText editOwner1;
    EditText editOwner2;
    EditText editPayment1;
    EditText editPayment2;
    EditText editPersonal_id;
    EditText editPolicy;
    EditText editPolicy1;
    EditText editPolicy2;
    int firstTimeAutoMobile = 0;
    int firstTimeHouse = 0;
    JSONArray houseJsonArray;
    JSONObject json;
    LinearLayout layoutAutoMobile;
    LinearLayout layoutHouse;
    LinearLayout layoutImagesCoverage;
    LinearLayout layoutImagesItinerary;
    LinearLayout layoutImagesPolicy1;
    LinearLayout layoutImagesPolicy2;
    ArrayList<HashMap<String, String>> list_images;
    ImageView p_photo;
    SettingPreference pref;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_insurance);
        this.pref = new SettingPreference(getApplicationContext());
        this.compress = new CompressImage(getApplicationContext());
        this.connection = new ConnectionDetector(getApplicationContext());
        new Bugsense().startBugsense(getApplicationContext());
        findViewId();
        checkAlredySaveAccount();
    }

    private void checkAlredySaveAccount() {
        if (this.pref.getStringValue(Constant.insurance_id, "").equalsIgnoreCase("0") ||this.pref.getStringValue(Constant.insurance_id, "").isEmpty() || this.pref.getStringValue(Constant.user_id, "").isEmpty()) {
            this.actionBarTittle.setText(getResources().getString(R.string.menu_insurance));
            addHouseLayout(this.firstTimeHouse, null, false);
            addAutoMobile(this.firstTimeAutoMobile, null, false);
            addNewImageItinerary(null, false);
            addimageCovrage(null, false);
            addNewImagePolicy1(null, false);
            addNewImagePolicy2(null, false);
        } else {
            this.btn_save.setText("Edit");
            if (!this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                this.actionBarTittle.setText("Edit " + getResources().getString(R.string.menu_insurance));
            }
            if (this.connection.isConnectingToInternet()) {
                try {
                    JSONObject nameValuePair = new JSONObject();
                    nameValuePair.put("user_id", this.pref.getStringValue(Constant.user_id, ""));
                    nameValuePair.put("insurance_id", this.pref.getStringValue(Constant.insurance_id, ""));
                    nameValuePair.put("token", this.pref.getStringValue(Constant.jwttoken, ""));
                    new GeneralTask(this, ServiceUrl.get_insurance_detail, nameValuePair, 1, "post").execute(new Void[0]);
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
        this.btn_back = (Button) findViewById(R.id.btn_back);
        this.btn_save = (Button) findViewById(R.id.btn_save);
        this.actionBarTittle = (TextView) findViewById(R.id.actionBarTittle);
        this.btn_back.setOnClickListener(this);
        this.btn_save.setOnClickListener(this);
        this.addHouse = (ImageView) findViewById(R.id.addHouse);
        this.addHouse.setOnClickListener(this);
        this.addAutoMobile = (ImageView) findViewById(R.id.addAutoMobile);
        this.addAutoMobile.setOnClickListener(this);
        this.layoutHouse = (LinearLayout) findViewById(R.id.layoutHouse);
        this.layoutAutoMobile = (LinearLayout) findViewById(R.id.layoutAutoMobile);
        this.layoutImagesItinerary = (LinearLayout) findViewById(R.id.layoutImagesItinerary);
        this.layoutImagesCoverage = (LinearLayout) findViewById(R.id.layoutImagesCoverage);
        this.layoutImagesPolicy1 = (LinearLayout) findViewById(R.id.layoutImagesPolicy1);
        this.layoutImagesPolicy2 = (LinearLayout) findViewById(R.id.layoutImagesPolicy2);
        this.editPolicy = (EditText) findViewById(R.id.editPolicy);
        this.editCompany = (EditText) findViewById(R.id.editCompany);
        this.editPersonal_id = (EditText) findViewById(R.id.editPersonal_id);
        this.p_photo = (ImageView) findViewById(R.id.p_photo);
        this.p_photo.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                InsuranceMenuActivity.this.currentImageVew = InsuranceMenuActivity.this.p_photo;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                InsuranceMenuActivity.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        this.firstTimeHouse = 0;
        this.firstTimeAutoMobile = 0;
    }

    private void setEnableControl() {
        this.actionBarTittle.setText(getResources().getString(R.string.menu_insurance));
        this.btn_save.setVisibility(View.GONE);
        this.addHouse.setEnabled(false);
        this.addAutoMobile.setEnabled(false);
        this.editInsuranceId1 = (EditText) findViewById(R.id.editInsuranceId1);
        this.editInsuranceId1.setEnabled(false);
        this.editOwner1 = (EditText) findViewById(R.id.editOwner1);
        this.editOwner1.setEnabled(false);
        this.editInsured1 = (EditText) findViewById(R.id.editInsured1);
        this.editInsured1.setEnabled(false);
        this.editCmp1 = (EditText) findViewById(R.id.editCmp1);
        this.editCmp1.setEnabled(false);
        this.editPolicy1 = (EditText) findViewById(R.id.editPolicy1);
        this.editPolicy1.setEnabled(false);
        this.editPayment1 = (EditText) findViewById(R.id.editPayment1);
        this.editPayment1.setEnabled(false);
        this.editInsuranceId2 = (EditText) findViewById(R.id.editInsuranceId2);
        this.editInsuranceId2.setEnabled(false);
        this.editOwner2 = (EditText) findViewById(R.id.editOwner2);
        this.editOwner2.setEnabled(false);
        this.editInsured2 = (EditText) findViewById(R.id.editInsured2);
        this.editInsured2.setEnabled(false);
        this.editCmp2 = (EditText) findViewById(R.id.editCmp2);
        this.editCmp2.setEnabled(false);
        this.editCmp1 = (EditText) findViewById(R.id.editCmp1);
        this.editCmp1.setEnabled(false);
        this.editPayment2 = (EditText) findViewById(R.id.editPayment2);
        this.editPayment2.setEnabled(false);
        this.editPolicy.setEnabled(false);
        this.editCompany.setEnabled(false);
        this.p_photo.setEnabled(false);
    }

    @SuppressLint({"InflateParams"})
    private void addHouseLayout(int firstTime, JSONObject response, boolean isJson) {
        try {
            final View view = LayoutInflater.from(this).inflate(R.layout.layout_house, null);
            final ImageView imgReceipt = (ImageView) view.findViewById(R.id.imgReceipt);
            imgReceipt.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    InsuranceMenuActivity.this.currentImageVew = imgReceipt;
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction("android.intent.action.GET_CONTENT");
                    InsuranceMenuActivity.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                }
            });
            final ImageView removeIcon = (ImageView) view.findViewById(R.id.removeIcon);
            removeIcon.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    InsuranceMenuActivity.this.layoutHouse.removeView(view);
                    if (removeIcon.getTag().toString().isEmpty()) {
                        return;
                    }
                    if (InsuranceMenuActivity.this.delete_household.equalsIgnoreCase("")) {
                        InsuranceMenuActivity.this.delete_household = InsuranceMenuActivity.this.delete_household.concat(removeIcon.getTag().toString().trim());
                        return;
                    }
                    InsuranceMenuActivity.this.delete_household = InsuranceMenuActivity.this.delete_household.concat("," + removeIcon.getTag().toString().trim());
                }
            });
            if (firstTime == 0) {
                removeIcon.setVisibility(View.GONE);
            }
            ToggleButton isPhotoTaken = (ToggleButton) view.findViewById(R.id.isPhotoTaken);
            ToggleButton isPhotoTakenReceipt = (ToggleButton) view.findViewById(R.id.isPhotoTakenReceipt);
            isPhotoTaken.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        imgReceipt.setVisibility(View.VISIBLE);
                    } else {
                        imgReceipt.setVisibility(View.GONE);
                    }
                }
            });
            EditText editId = (EditText) view.findViewById(R.id.editId);
            EditText editItem = (EditText) view.findViewById(R.id.editItem);
            ToggleButton isReceipt = (ToggleButton) view.findViewById(R.id.isReceipt);
            if (isJson) {
                editId.setText(response.getString("household_id"));
                editItem.setText(response.getString("h_item"));
                if (response.getString("h_is_receipt").equalsIgnoreCase("1")) {
                    isReceipt.setChecked(true);
                } else {
                    isReceipt.setChecked(false);
                }
                if (response.getString("h_is_photo_receipt").equalsIgnoreCase("1")) {
                    isPhotoTakenReceipt.setChecked(true);
                } else {
                    isPhotoTakenReceipt.setChecked(false);
                }
                if (response.getString("h_is_item").equalsIgnoreCase("1")) {
                    isPhotoTaken.setChecked(true);
                } else {
                    isPhotoTaken.setChecked(false);
                }
                UrlImageViewHelper.setUrlDrawable(imgReceipt, response.getString("h_photo").toString().trim(), (int) R.drawable.img);
                removeIcon.setTag(response.getString("household_id").toString().trim());
            }
            if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                editId.setEnabled(false);
                editItem.setEnabled(false);
                isReceipt.setEnabled(false);
                removeIcon.setVisibility(View.GONE);
                isPhotoTaken.setEnabled(false);
                imgReceipt.setEnabled(false);
                isPhotoTakenReceipt.setEnabled(false);
            }
            this.layoutHouse.addView(view);
            this.firstTimeHouse++;
        } catch (Exception e) {
            addHouseLayout(this.firstTimeHouse, null, false);
            e.printStackTrace();
        }
    }

    @SuppressLint({"InflateParams"})
    private void addAutoMobile(int firstTime, JSONObject response, boolean isJson) {
        try {
            final View view = LayoutInflater.from(this).inflate(R.layout.layout_automobile, null);
            final ImageView imgPolicy = (ImageView) view.findViewById(R.id.imgPolicy);
            imgPolicy.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    InsuranceMenuActivity.this.currentImageVew = imgPolicy;
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction("android.intent.action.GET_CONTENT");
                    InsuranceMenuActivity.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                }
            });
            final ImageView removeIcon = (ImageView) view.findViewById(R.id.removeIcon);
            removeIcon.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    InsuranceMenuActivity.this.layoutAutoMobile.removeView(view);
                    if (removeIcon.getTag().toString().isEmpty()) {
                        return;
                    }
                    if (InsuranceMenuActivity.this.delete_automobile.equalsIgnoreCase("")) {
                        InsuranceMenuActivity.this.delete_automobile = InsuranceMenuActivity.this.delete_automobile.concat(removeIcon.getTag().toString().trim());
                        return;
                    }
                    InsuranceMenuActivity.this.delete_automobile = InsuranceMenuActivity.this.delete_automobile.concat("," + removeIcon.getTag().toString().trim());
                }
            });
            if (firstTime == 0) {
                removeIcon.setVisibility(View.GONE);
            }
            ToggleButton isPolicy = (ToggleButton) view.findViewById(R.id.isPolicy);
            isPolicy.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        imgPolicy.setVisibility(View.VISIBLE);
                    } else {
                        imgPolicy.setVisibility(View.GONE);
                    }
                }
            });
            EditText editAutoId = (EditText) view.findViewById(R.id.editAutoId);
            EditText editVehical = (EditText) view.findViewById(R.id.editVehical);
            EditText editModel = (EditText) view.findViewById(R.id.editModel);
            EditText edityear = (EditText) view.findViewById(R.id.edityear);
            if (isJson) {
                editAutoId.setText(response.getString("automobile_id"));
                editVehical.setText(response.getString("a_make"));
                editModel.setText(response.getString("a_model"));
                edityear.setText(response.getString("a_year"));
                if (response.getString("a_is_photo_policy").equalsIgnoreCase("1")) {
                    isPolicy.setChecked(true);
                } else {
                    isPolicy.setChecked(false);
                }
                UrlImageViewHelper.setUrlDrawable(imgPolicy, response.getString("a_photo").toString().trim(), (int) R.drawable.img);
                removeIcon.setTag(response.getString("automobile_id").toString().trim());
            }
            if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                removeIcon.setVisibility(View.GONE);
                editAutoId.setEnabled(false);
                editVehical.setEnabled(false);
                editModel.setEnabled(false);
                isPolicy.setEnabled(false);
                imgPolicy.setEnabled(false);
                edityear.setEnabled(false);
            }
            this.layoutAutoMobile.addView(view);
            this.firstTimeAutoMobile++;
        } catch (Exception e) {
            addAutoMobile(this.firstTimeAutoMobile, null, false);
            e.printStackTrace();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save /*2131558589*/:
                this.list_images.clear();
                this.autoMobileJsonArray = new JSONArray();
                this.houseJsonArray = new JSONArray();
                this.json = new JSONObject();
                prePareJsonHouse();
                prePareJsonAutoMobile();
                preParePolicy1Json();
                preParePolicy2Json();
                try {
                    prePareImageItineraryJson();
                    prePareImageCovrageJson();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                prePareSendJson();
                return;
            case R.id.btn_back /*2131558590*/:
                finish();
                return;
            case R.id.addHouse /*2131558960*/:
                addHouseLayout(this.firstTimeHouse, null, false);
                this.firstTimeHouse++;
                return;
            case R.id.addAutoMobile /*2131558962*/:
                addAutoMobile(this.firstTimeAutoMobile, null, false);
                this.firstTimeAutoMobile++;
                return;
            default:
                return;
        }
    }

    private void prePareSendJson() {
        try {
            JSONObject sendJson = new JSONObject();
            this.json.put("user_id", this.pref.getStringValue(Constant.user_id, ""));
            this.json.put("delete_household", this.delete_household);
            this.json.put("delete_insurance_img", this.delete_insurance_img);
            this.json.put("delete_automobile", this.delete_automobile);
            this.json.put("delete_policy_img", this.delete_policy_img);
            this.json.put("delete_travel_itinerary", this.delete_travel_itinerary);
            this.json.put("delete_travel_coverage", this.delete_travel_coverage);
            this.json.put("policy", this.editPolicy.getText().toString().trim());
            this.json.put("company", this.editCompany.getText().toString().trim());
            this.json.put("personal_id", this.editPersonal_id.getText().toString().trim());
            if (this.p_photo.getContentDescription().toString().isEmpty()) {
                this.json.put("p_photo", "");
            } else {
                this.json.put("p_photo", "p_photo");
                HashMap<String, String> item_map = new HashMap<>();
                item_map.put("image_path", this.p_photo.getContentDescription().toString());
                item_map.put("image_name", "p_photo");
                this.list_images.add(item_map);
            }
            sendJson.put("insurance_data", this.json);
            Log.e("insurance_data", sendJson.toString());
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            entity.addPart("json_data", new StringBody(sendJson.toString()));
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("json_data", sendJson.toString()));
            for (int i = 0; i < this.list_images.size(); i++) {
                entity.addPart((String) ((HashMap) this.list_images.get(i)).get("image_name"), new FileBody(new File((String) ((HashMap) this.list_images.get(i)).get("image_path")), "image/jpeg"));
                nameValuePairs.add(new BasicNameValuePair((String) ((HashMap) this.list_images.get(i)).get("image_name"), new FileBody(new File((String) ((HashMap) this.list_images.get(i)).get("image_path")), "image/jpeg").getFilename()));
                Log.i("file parameter", ((String) ((HashMap) this.list_images.get(i)).get("image_name")).toString());
                Log.i("file path", ((String) ((HashMap) this.list_images.get(i)).get("image_path")).toString());
            }

            if (!this.connection.isConnectingToInternet()) {
                displayMessage(getResources().getString(R.string.connectionFailMessage));
            } else if (this.btn_save.getText().toString().trim().equalsIgnoreCase("edit")) {
                new SaveProfileAsytask(this, ServiceUrl.edit_insurance, nameValuePairs).execute(new Void[0]);
            } else {
                new SaveProfileAsytask(this, ServiceUrl.save_insurance, nameValuePairs).execute(new Void[0]);
            }
            Log.e("Insurance json :", sendJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prePareImageCovrageJson() throws JSONException {
        String travel_coverage_image = "";
        int i = 0;
        while (i < this.layoutImagesCoverage.getChildCount()) {
            try {
                ImageView iv = (ImageView) ((ViewGroup) this.layoutImagesCoverage.getChildAt(i)).findViewById(R.id.iv);
                if (!iv.getContentDescription().toString().isEmpty()) {
                    HashMap<String, String> item_map = new HashMap<>();
                    item_map.put("image_path", iv.getContentDescription().toString());
                    item_map.put("image_name", "Coverage" + i);
                    this.list_images.add(item_map);
                    if (travel_coverage_image.equalsIgnoreCase("")) {
                        travel_coverage_image = travel_coverage_image.concat("Coverage" + i);
                    } else {
                        travel_coverage_image = travel_coverage_image.concat(",Coverage" + i);
                    }
                    if (!iv.getTag().toString().isEmpty() && !iv.getTag().toString().equalsIgnoreCase("image")) {
                        if (this.delete_travel_coverage.equalsIgnoreCase("")) {
                            this.delete_travel_coverage = this.delete_travel_coverage.concat(iv.getTag().toString().trim());
                        } else {
                            this.delete_travel_coverage = this.delete_travel_coverage.concat("," + iv.getTag().toString().trim());
                        }
                    }
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        this.json.put("travel_coverage_image", travel_coverage_image);
    }

    private void prePareImageItineraryJson() throws JSONException {
        String travel_itinerary_image = "";
        int i = 0;
        while (i < this.layoutImagesItinerary.getChildCount()) {
            try {
                ImageView iv = (ImageView) ((ViewGroup) this.layoutImagesItinerary.getChildAt(i)).findViewById(R.id.iv);
                if (!iv.getContentDescription().toString().isEmpty()) {
                    HashMap<String, String> item_map = new HashMap<>();
                    item_map.put("image_path", iv.getContentDescription().toString());
                    item_map.put("image_name", "itinerary" + i);
                    this.list_images.add(item_map);
                    if (travel_itinerary_image.equalsIgnoreCase("")) {
                        travel_itinerary_image = travel_itinerary_image.concat("itinerary" + i);
                    } else {
                        travel_itinerary_image = travel_itinerary_image.concat(",itinerary" + i);
                    }
                    if (!iv.getTag().toString().isEmpty() && !iv.getTag().toString().equalsIgnoreCase("image")) {
                        if (this.delete_travel_itinerary.equalsIgnoreCase("")) {
                            this.delete_travel_itinerary = this.delete_travel_itinerary.concat(iv.getTag().toString().trim());
                        } else {
                            this.delete_travel_itinerary = this.delete_travel_itinerary.concat("," + iv.getTag().toString().trim());
                        }
                    }
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        this.json.put("travel_itinerary_image", travel_itinerary_image);
    }

    private void preParePolicy2Json() {
        String ill_image = "";
        try {
            this.editInsuranceId2 = (EditText) findViewById(R.id.editInsuranceId2);
            this.editOwner2 = (EditText) findViewById(R.id.editOwner2);
            this.editInsured2 = (EditText) findViewById(R.id.editInsured2);
            this.editCmp2 = (EditText) findViewById(R.id.editCmp2);
            this.editPolicy2 = (EditText) findViewById(R.id.editPolicy2);
            this.editPayment2 = (EditText) findViewById(R.id.editPayment2);
            this.json.put("policy_id", this.editInsuranceId2.getText().toString().trim());
            this.json.put("ill_owner", this.editOwner2.getText().toString().trim());
            this.json.put("ill_insured_name", this.editInsured2.getText().toString().trim());
            this.json.put("ill_company_name", this.editCmp2.getText().toString().trim());
            this.json.put("ill_policy", this.editPolicy2.getText().toString().trim());
            this.json.put("ill_payment", this.editPayment2.getText().toString().trim());
            for (int i = 0; i < this.layoutImagesPolicy2.getChildCount(); i++) {
                ImageView iv = (ImageView) ((ViewGroup) this.layoutImagesPolicy2.getChildAt(i)).findViewById(R.id.iv);
                if (!iv.getContentDescription().toString().isEmpty()) {
                    HashMap<String, String> item_map = new HashMap<>();
                    item_map.put("image_path", iv.getContentDescription().toString());
                    item_map.put("image_name", "ill_image" + i);
                    this.list_images.add(item_map);
                    if (ill_image.equalsIgnoreCase("")) {
                        ill_image = ill_image.concat("ill_image" + i);
                    } else {
                        ill_image = ill_image.concat(",ill_image" + i);
                    }
                    if (!iv.getTag().toString().trim().isEmpty()) {
                        if (this.delete_insurance_img.equalsIgnoreCase("")) {
                            this.delete_policy_img = this.delete_policy_img.concat(iv.getTag().toString().trim());
                        } else {
                            this.delete_policy_img = this.delete_policy_img.concat("," + iv.getTag().toString().trim());
                        }
                    }
                }
            }
            this.json.put("ill_image", ill_image);
            this.json.put("house", this.houseJsonArray);
            this.json.put("automobile", this.autoMobileJsonArray);
            Log.e("json", this.json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void preParePolicy1Json() {
        String ins_image = "";
        try {
            this.editInsuranceId1 = (EditText) findViewById(R.id.editInsuranceId1);
            this.editOwner1 = (EditText) findViewById(R.id.editOwner1);
            this.editInsured1 = (EditText) findViewById(R.id.editInsured1);
            this.editCmp1 = (EditText) findViewById(R.id.editCmp1);
            this.editPolicy1 = (EditText) findViewById(R.id.editPolicy1);
            this.editPayment1 = (EditText) findViewById(R.id.editPayment1);
            this.json.put("insurance_id", this.editInsuranceId1.getText().toString().trim());
            this.json.put("ins_owner", this.editOwner1.getText().toString().trim());
            this.json.put("ins_insured_name", this.editInsured1.getText().toString().trim());
            this.json.put("ins_company_name", this.editCmp1.getText().toString().trim());
            this.json.put("ins_policy", this.editPolicy1.getText().toString().trim());
            this.json.put("ins_payment", this.editPayment1.getText().toString().trim());
            for (int i = 0; i < this.layoutImagesPolicy1.getChildCount(); i++) {
                ImageView iv = (ImageView) ((ViewGroup) this.layoutImagesPolicy1.getChildAt(i)).findViewById(R.id.iv);
                if (!iv.getContentDescription().toString().isEmpty()) {
                    HashMap<String, String> item_map = new HashMap<>();
                    item_map.put("image_path", iv.getContentDescription().toString());
                    item_map.put("image_name", "ins_image" + i);
                    this.list_images.add(item_map);
                    if (ins_image.equalsIgnoreCase("")) {
                        ins_image = ins_image.concat("ins_image" + i);
                    } else {
                        ins_image = ins_image.concat(",ins_image" + i);
                    }
                    if (!iv.getTag().toString().trim().isEmpty()) {
                        if (this.delete_insurance_img.equalsIgnoreCase("")) {
                            this.delete_insurance_img = this.delete_insurance_img.concat(iv.getTag().toString().trim());
                        } else {
                            this.delete_insurance_img = this.delete_insurance_img.concat("," + iv.getTag().toString().trim());
                        }
                    }
                }
            }
            this.json.put("ins_image", ins_image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prePareJsonAutoMobile() {
        int i = 0;
        while (i < this.layoutAutoMobile.getChildCount()) {
            try {
                JSONObject jsonbj = new JSONObject();
                ViewGroup view = (ViewGroup) this.layoutAutoMobile.getChildAt(i);
                EditText editVehical = (EditText) view.findViewById(R.id.editVehical);
                EditText editModel = (EditText) view.findViewById(R.id.editModel);
                EditText edityear = (EditText) view.findViewById(R.id.edityear);
                ToggleButton isPolicy = (ToggleButton) view.findViewById(R.id.isPolicy);
                jsonbj.put("automobile_id", ((EditText) view.findViewById(R.id.editAutoId)).getText().toString().trim());
                jsonbj.put("a_make", editVehical.getText().toString().trim());
                jsonbj.put("a_model", editModel.getText().toString().trim());
                jsonbj.put("a_year", edityear.getText().toString().trim());
                if (isPolicy.isChecked()) {
                    jsonbj.put("a_is_photo_policy", "1");
                    ImageView imgPolicy = (ImageView) view.findViewById(R.id.imgPolicy);
                    if (imgPolicy.getContentDescription().toString().isEmpty()) {
                        jsonbj.put("a_photo", "");
                    } else {
                        jsonbj.put("a_photo", "a_photo" + i);
                        HashMap<String, String> item_map = new HashMap<>();
                        item_map.put("image_path", imgPolicy.getContentDescription().toString());
                        item_map.put("image_name", "a_photo" + i);
                        this.list_images.add(item_map);
                    }
                } else {
                    jsonbj.put("a_is_photo_policy", "0");
                }
                this.autoMobileJsonArray.put(jsonbj);
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    private void prePareJsonHouse() {
        int i = 0;
        while (i < this.layoutHouse.getChildCount()) {
            try {
                JSONObject jsonbj = new JSONObject();
                ViewGroup view = (ViewGroup) this.layoutHouse.getChildAt(i);
                EditText editItem = (EditText) view.findViewById(R.id.editItem);
                ToggleButton isReceipt = (ToggleButton) view.findViewById(R.id.isReceipt);
                ToggleButton isPhotoTaken = (ToggleButton) view.findViewById(R.id.isPhotoTaken);
                ToggleButton isPhotoTakenReceipt = (ToggleButton) view.findViewById(R.id.isPhotoTakenReceipt);
                jsonbj.put("household_id", ((EditText) view.findViewById(R.id.editId)).getText().toString().trim());
                jsonbj.put("h_item", editItem.getText().toString().trim());
                if (isReceipt.isChecked()) {
                    jsonbj.put("h_is_receipt", "1");
                } else {
                    jsonbj.put("h_is_receipt", "0");
                }
                if (isPhotoTakenReceipt.isChecked()) {
                    jsonbj.put("h_is_photo_receipt", "1");
                } else {
                    jsonbj.put("h_is_photo_receipt", "0");
                }
                if (isPhotoTaken.isChecked()) {
                    jsonbj.put("h_is_item", "1");
                    ImageView imgReceipt = (ImageView) view.findViewById(R.id.imgReceipt);
                    if (imgReceipt.getContentDescription().toString().isEmpty()) {
                        jsonbj.put("h_photo", "");
                    } else {
                        jsonbj.put("h_photo", "h_photo" + i);
                        HashMap<String, String> item_map = new HashMap<>();
                        item_map.put("image_path", imgReceipt.getContentDescription().toString());
                        item_map.put("image_name", "h_photo" + i);
                        this.list_images.add(item_map);
                    }
                } else {
                    jsonbj.put("h_is_item", "0");
                }
                this.houseJsonArray.put(jsonbj);
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    @SuppressLint({"InflateParams"})
    private void addimageCovrage(JSONObject response, boolean flag) {
        try {
            final View view = LayoutInflater.from(this).inflate(R.layout.layout_images, null);
            final ImageView iv = (ImageView) view.findViewById(R.id.iv);
            iv.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    InsuranceMenuActivity.this.currentImageVew = iv;
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction("android.intent.action.GET_CONTENT");
                    InsuranceMenuActivity.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 3);
                }
            });
            iv.setTag("image");
            final Button btn_remove = (Button) view.findViewById(R.id.btn_remove);
            btn_remove.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    ((ViewGroup) view.getParent()).removeView(view);
                    if (!btn_remove.getTag().toString().isEmpty() && !btn_remove.getTag().toString().equalsIgnoreCase("image")) {
                        if (InsuranceMenuActivity.this.delete_travel_coverage.equalsIgnoreCase("")) {
                            InsuranceMenuActivity.this.delete_travel_coverage = InsuranceMenuActivity.this.delete_travel_coverage.concat(btn_remove.getTag().toString().trim());
                            return;
                        }
                        InsuranceMenuActivity.this.delete_travel_coverage = InsuranceMenuActivity.this.delete_travel_coverage.concat("," + btn_remove.getTag().toString().trim());
                    }
                }
            });
            if (flag) {
                UrlImageViewHelper.setUrlDrawable(iv, response.getString("c_photo").toString().trim(), (int) R.drawable.img);
                btn_remove.setTag(response.getString("travel_c_id").toString().trim());
                iv.setTag(response.getString("travel_c_id").toString().trim());
            }
            if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                btn_remove.setVisibility(View.GONE);
                iv.setEnabled(false);
            } else {
                for (int i = 0; i < this.layoutImagesCoverage.getChildCount(); i++) {
                    ViewGroup child = (ViewGroup) this.layoutImagesCoverage.getChildAt(i);
                    Button removeBTN = (Button) child.findViewById(R.id.btn_remove);
                    if (!((ImageView) child.findViewById(R.id.iv)).getContentDescription().toString().trim().isEmpty() || !removeBTN.getTag().toString().isEmpty()) {
                        removeBTN.setVisibility(View.VISIBLE);
                    } else {
                        removeBTN.setVisibility(View.GONE);
                    }
                }
            }
            btn_remove.setVisibility(View.GONE);
            this.layoutImagesCoverage.addView(view);
        } catch (Exception e) {
            addimageCovrage(null, false);
            e.printStackTrace();
        }
    }

    @SuppressLint({"InflateParams"})
    private void addNewImageItinerary(JSONObject response, boolean flag) {
        try {
            final View view = LayoutInflater.from(this).inflate(R.layout.layout_images, null);
            final ImageView iv = (ImageView) view.findViewById(R.id.iv);
            iv.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    InsuranceMenuActivity.this.currentImageVew = iv;
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction("android.intent.action.GET_CONTENT");
                    InsuranceMenuActivity.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
                }
            });
            iv.setTag("image");
            final Button btn_remove = (Button) view.findViewById(R.id.btn_remove);
            btn_remove.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    ((ViewGroup) view.getParent()).removeView(view);
                    if (!btn_remove.getTag().toString().isEmpty() && !iv.getTag().toString().equalsIgnoreCase("image")) {
                        if (InsuranceMenuActivity.this.delete_travel_itinerary.equalsIgnoreCase("")) {
                            InsuranceMenuActivity.this.delete_travel_itinerary = InsuranceMenuActivity.this.delete_travel_itinerary.concat(btn_remove.getTag().toString().trim());
                            return;
                        }
                        InsuranceMenuActivity.this.delete_travel_itinerary = InsuranceMenuActivity.this.delete_travel_itinerary.concat("," + btn_remove.getTag().toString().trim());
                    }
                }
            });
            if (flag) {
                UrlImageViewHelper.setUrlDrawable(iv, response.getString("t_photo").toString().trim(), (int) R.drawable.img);
                btn_remove.setTag(response.getString("travel_i_id").toString().trim());
                iv.setTag(response.getString("travel_i_id").toString().trim());
            }
            if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                iv.setEnabled(false);
                btn_remove.setVisibility(View.GONE);
            } else {
                for (int i = 0; i < this.layoutImagesItinerary.getChildCount(); i++) {
                    ViewGroup child = (ViewGroup) this.layoutImagesItinerary.getChildAt(i);
                    Button removeBTN = (Button) child.findViewById(R.id.btn_remove);
                    if (!((ImageView) child.findViewById(R.id.iv)).getContentDescription().toString().trim().isEmpty() || !removeBTN.getTag().toString().isEmpty()) {
                        removeBTN.setVisibility(View.VISIBLE);
                    } else {
                        removeBTN.setVisibility(View.GONE);
                    }
                }
            }
            btn_remove.setVisibility(View.GONE);
            this.layoutImagesItinerary.addView(view);
        } catch (Exception e) {
            addNewImageItinerary(null, false);
            e.printStackTrace();
        }
    }

    @SuppressLint({"InflateParams"})
    private void addNewImagePolicy1(JSONObject response, boolean flag) {
        try {
            final View view = LayoutInflater.from(this).inflate(R.layout.layout_images, null);
            final ImageView iv = (ImageView) view.findViewById(R.id.iv);
            iv.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    InsuranceMenuActivity.this.currentImageVew = iv;
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction("android.intent.action.GET_CONTENT");
                    InsuranceMenuActivity.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 4);
                }
            });
            iv.setTag("image");
            final Button btn_remove = (Button) view.findViewById(R.id.btn_remove);
            btn_remove.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    ((ViewGroup) view.getParent()).removeView(view);
                    if (btn_remove.getTag().toString().isEmpty()) {
                        return;
                    }
                    if (InsuranceMenuActivity.this.delete_insurance_img.equalsIgnoreCase("")) {
                        InsuranceMenuActivity.this.delete_insurance_img = InsuranceMenuActivity.this.delete_insurance_img.concat(btn_remove.getTag().toString().trim());
                        return;
                    }
                    InsuranceMenuActivity.this.delete_insurance_img = InsuranceMenuActivity.this.delete_insurance_img.concat("," + btn_remove.getTag().toString().trim());
                }
            });
            if (flag) {
                UrlImageViewHelper.setUrlDrawable(iv, response.getString("image").toString().trim(), (int) R.drawable.img);
                btn_remove.setTag(response.getString("image_id").toString().trim());
                iv.setTag(response.getString("image_id").toString().trim());
            }
            btn_remove.setVisibility(View.GONE);
            if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                btn_remove.setVisibility(View.GONE);
                iv.setEnabled(false);
            } else {
                for (int i = 0; i < this.layoutImagesPolicy1.getChildCount(); i++) {
                    ViewGroup child = (ViewGroup) this.layoutImagesPolicy1.getChildAt(i);
                    Button removeBTN = (Button) child.findViewById(R.id.btn_remove);
                    if (!((ImageView) child.findViewById(R.id.iv)).getContentDescription().toString().trim().isEmpty() || !removeBTN.getTag().toString().isEmpty()) {
                        removeBTN.setVisibility(View.VISIBLE);
                    } else {
                        removeBTN.setVisibility(View.GONE);
                    }
                }
            }
            this.layoutImagesPolicy1.addView(view);
        } catch (Exception e) {
            addNewImagePolicy1(null, false);
            e.printStackTrace();
        }
    }

    @SuppressLint({"InflateParams"})
    private void addNewImagePolicy2(JSONObject response, boolean flag) {
        try {
            final View view = LayoutInflater.from(this).inflate(R.layout.layout_images, null);
            final ImageView iv = (ImageView) view.findViewById(R.id.iv);
            iv.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    InsuranceMenuActivity.this.currentImageVew = iv;
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction("android.intent.action.GET_CONTENT");
                    InsuranceMenuActivity.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 5);
                }
            });
            iv.setTag("image");
            final Button btn_remove = (Button) view.findViewById(R.id.btn_remove);
            btn_remove.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    ((ViewGroup) view.getParent()).removeView(view);
                    if (btn_remove.getTag().toString().isEmpty()) {
                        return;
                    }
                    if (InsuranceMenuActivity.this.delete_policy_img.equalsIgnoreCase("")) {
                        InsuranceMenuActivity.this.delete_policy_img = InsuranceMenuActivity.this.delete_policy_img.concat(btn_remove.getTag().toString().trim());
                        return;
                    }
                    InsuranceMenuActivity.this.delete_policy_img = InsuranceMenuActivity.this.delete_policy_img.concat("," + btn_remove.getTag().toString().trim());
                }
            });
            if (flag) {
                UrlImageViewHelper.setUrlDrawable(iv, response.getString("image").toString().trim(), (int) R.drawable.img);
                btn_remove.setTag(response.getString("image_id").toString().trim());
                iv.setTag(response.getString("image_id").toString().trim());
            }
            if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                btn_remove.setVisibility(View.GONE);
                iv.setEnabled(false);
            } else {
                for (int i = 0; i < this.layoutImagesPolicy2.getChildCount(); i++) {
                    ViewGroup imageView = (ViewGroup) this.layoutImagesPolicy2.getChildAt(i);
                    ImageView image = (ImageView) imageView.findViewById(R.id.iv);
                    Button remove = (Button) imageView.findViewById(R.id.btn_remove);
                    if (!image.getContentDescription().toString().equalsIgnoreCase("") || !image.getTag().toString().isEmpty()) {
                        remove.setVisibility(View.VISIBLE);
                    } else {
                        remove.setVisibility(View.GONE);
                    }
                }
            }
            btn_remove.setVisibility(View.GONE);
            this.layoutImagesPolicy2.addView(view);
        } catch (Exception e) {
            addNewImagePolicy2(null, false);
            e.printStackTrace();
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
            this.currentImageVew.setContentDescription(picturePath.toString());
        }
        if (resultCode == -1 && requestCode == 2) {
            Uri selectedImageUri2 = data.getData();
            String[] filePathColumn2 = {"_data"};
            Cursor cursor2 = getContentResolver().query(selectedImageUri2, filePathColumn2, null, null, null);
            cursor2.moveToFirst();
            String picturePath2 = cursor2.getString(cursor2.getColumnIndex(filePathColumn2[0]));
            cursor2.close();
            this.currentImageVew.setImageBitmap(this.compress.compressImage(selectedImageUri2.toString(), picturePath2));
            if (this.currentImageVew.getTag().toString().equalsIgnoreCase("image")) {
                Log.e("equalsIgnoreCase(image)", "......if");
                if (this.currentImageVew.getContentDescription().toString().equalsIgnoreCase("")) {
                    Log.e("!equalsIgnoreCase()", "......if");
                    this.currentImageVew.setContentDescription(picturePath2.toString());
                    addNewImageItinerary(null, false);
                } else {
                    this.currentImageVew.setContentDescription(picturePath2.toString());
                    Log.e("!equalsIgnoreCase()", "......else");
                }
            }
        }
        if (resultCode == -1 && requestCode == 3) {
            Uri selectedImageUri3 = data.getData();
            String[] filePathColumn3 = {"_data"};
            Cursor cursor3 = getContentResolver().query(selectedImageUri3, filePathColumn3, null, null, null);
            cursor3.moveToFirst();
            String picturePath3 = cursor3.getString(cursor3.getColumnIndex(filePathColumn3[0]));
            cursor3.close();
            this.currentImageVew.setImageBitmap(this.compress.compressImage(selectedImageUri3.toString(), picturePath3));
            if (this.currentImageVew.getTag().toString().equalsIgnoreCase("image")) {
                Log.e("equalsIgnoreCase(image)", "......if");
                if (this.currentImageVew.getContentDescription().toString().equalsIgnoreCase("")) {
                    Log.e("!equalsIgnoreCase()", "......if");
                    this.currentImageVew.setContentDescription(picturePath3.toString());
                    addimageCovrage(null, false);
                } else {
                    this.currentImageVew.setContentDescription(picturePath3.toString());
                    Log.e("!equalsIgnoreCase()", "......else");
                }
            }
        }
        if (resultCode == -1 && requestCode == 4) {
            Uri selectedImageUri4 = data.getData();
            String[] filePathColumn4 = {"_data"};
            Cursor cursor4 = getContentResolver().query(selectedImageUri4, filePathColumn4, null, null, null);
            cursor4.moveToFirst();
            String picturePath4 = cursor4.getString(cursor4.getColumnIndex(filePathColumn4[0]));
            cursor4.close();
            this.currentImageVew.setImageBitmap(this.compress.compressImage(selectedImageUri4.toString(), picturePath4));
            if (this.currentImageVew.getTag().toString().equalsIgnoreCase("image")) {
                Log.e("equalsIgnoreCase(image)", "......if");
                if (this.currentImageVew.getContentDescription().toString().equalsIgnoreCase("")) {
                    Log.e("!equalsIgnoreCase()", "......if");
                    this.currentImageVew.setContentDescription(picturePath4.toString());
                    addNewImagePolicy1(null, false);
                } else {
                    this.currentImageVew.setContentDescription(picturePath4.toString());
                    Log.e("!equalsIgnoreCase()", "......else");
                }
            }
        }
        if (resultCode == -1 && requestCode == 5) {
            Uri selectedImageUri5 = data.getData();
            String[] filePathColumn5 = {"_data"};
            Cursor cursor5 = getContentResolver().query(selectedImageUri5, filePathColumn5, null, null, null);
            cursor5.moveToFirst();
            String picturePath5 = cursor5.getString(cursor5.getColumnIndex(filePathColumn5[0]));
            cursor5.close();
            this.currentImageVew.setImageBitmap(this.compress.compressImage(selectedImageUri5.toString(), picturePath5));
            if (this.currentImageVew.getTag().toString().equalsIgnoreCase("image")) {
                Log.e("equalsIgnoreCase(image)", "......if");
                if (this.currentImageVew.getContentDescription().toString().equalsIgnoreCase("")) {
                    Log.e("!equalsIgnoreCase()", "......if");
                    this.currentImageVew.setContentDescription(picturePath5.toString());
                    addNewImagePolicy2(null, false);
                    return;
                }
                this.currentImageVew.setContentDescription(picturePath5.toString());
                Log.e("!equalsIgnoreCase()", "......else");
            }
        }
    }

    public void on_GeneralSuccess(JSONObject response, int responseCode) {
        if (response == null) {
            try {
                addHouseLayout(this.firstTimeHouse, null, false);
                addAutoMobile(this.firstTimeAutoMobile, null, false);
                addNewImageItinerary(null, false);
                addimageCovrage(null, false);
                addNewImagePolicy1(null, false);
                addNewImagePolicy2(null, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showhouseDetail(response);
            showAutoMobileDetail(response);
            showPolicy1Detail(response);
            showPolicy2Detail(response);
            showItineraryImages(response);
            showCoverageImages(response);
        }
    }

    private void showCoverageImages(JSONObject response) {
        try {
            JSONArray jsoArray = response.getJSONObject("insurance_data").getJSONArray("travel_coverage");
            for (int i = 0; i < jsoArray.length(); i++) {
                addimageCovrage(jsoArray.getJSONObject(i), true);
            }
            addimageCovrage(null, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showItineraryImages(JSONObject response) {
        try {
            JSONArray jsoArray = response.getJSONObject("insurance_data").getJSONArray("travel_itinerary");
            for (int i = 0; i < jsoArray.length(); i++) {
                addNewImageItinerary(jsoArray.getJSONObject(i), true);
            }
            addNewImageItinerary(null, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPolicy2Detail(JSONObject response) {
        try {
            this.editInsuranceId2 = (EditText) findViewById(R.id.editInsuranceId2);
            this.editOwner2 = (EditText) findViewById(R.id.editOwner2);
            this.editInsured2 = (EditText) findViewById(R.id.editInsured2);
            this.editCmp2 = (EditText) findViewById(R.id.editCmp2);
            this.editPolicy2 = (EditText) findViewById(R.id.editPolicy2);
            this.editPayment2 = (EditText) findViewById(R.id.editPayment2);
            if (response.getJSONObject("insurance_data").has("illness_policy")) {
                JSONObject json2 = response.getJSONObject("insurance_data").getJSONObject("illness_policy");
                this.editInsuranceId2.setText(json2.getString("policy_id"));
                this.editOwner2.setText(json2.getString("i_owner"));
                this.editInsured2.setText(json2.getString("i_insured_name"));
                this.editCmp2.setText(json2.getString("i_company_name"));
                this.editPolicy2.setText(json2.getString("i_policy"));
                this.editPayment2.setText(json2.getString("i_payment"));
                JSONArray jsonArray = json2.getJSONArray("illness_policy_images");
                if (json2.has("illness_policy_images")) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        addNewImagePolicy2(jsonArray.getJSONObject(i), true);
                    }
                } else {
                    addNewImagePolicy2(null, false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPolicy1Detail(JSONObject response) {
        try {
            this.editInsuranceId1 = (EditText) findViewById(R.id.editInsuranceId1);
            this.editOwner1 = (EditText) findViewById(R.id.editOwner1);
            this.editInsured1 = (EditText) findViewById(R.id.editInsured1);
            this.editCmp1 = (EditText) findViewById(R.id.editCmp1);
            this.editPolicy1 = (EditText) findViewById(R.id.editPolicy1);
            this.editPayment1 = (EditText) findViewById(R.id.editPayment1);
            JSONObject insurance_data = response.getJSONObject("insurance_data");
            if (insurance_data.has("insurance_policy")) {
                JSONObject json2 = insurance_data.getJSONObject("insurance_policy");
                this.editInsuranceId1.setText(json2.getString("insurance_id"));
                this.editOwner1.setText(json2.getString("i_owner"));
                this.editInsured1.setText(json2.getString("i_insured_name"));
                this.editCmp1.setText(json2.getString("i_company_name"));
                this.editPolicy1.setText(json2.getString("i_policy"));
                this.editPayment1.setText(json2.getString("i_payment"));
                JSONArray jsonArray = json2.getJSONArray("insurance_policy_images");
                if (json2.has("insurance_policy_images")) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        addNewImagePolicy1(jsonArray.getJSONObject(i), true);
                    }
                } else {
                    addNewImagePolicy1(null, false);
                }
            }
            this.editPersonal_id.setText(insurance_data.getString("personal_id"));
            this.editPolicy.setText(insurance_data.getString("policy"));
            this.editCompany.setText(insurance_data.getString("company"));
            UrlImageViewHelper.setUrlDrawable(this.p_photo, insurance_data.getString("p_photo").toString().trim(), (int) R.drawable.img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAutoMobileDetail(JSONObject response) {
        try {
            JSONArray jsonArray = response.getJSONObject("insurance_data").getJSONArray("automobile");
            for (int i = 0; i < jsonArray.length(); i++) {
                addAutoMobile(i, jsonArray.getJSONObject(i), true);
                this.firstTimeAutoMobile++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showhouseDetail(JSONObject response) {
        try {
            JSONArray jsonArray = response.getJSONObject("insurance_data").getJSONArray("house");
            for (int i = 0; i < jsonArray.length(); i++) {
                addHouseLayout(i, jsonArray.getJSONObject(i), true);
                this.firstTimeHouse++;
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
                if (response.has("insurance_id")) {
                    this.pref.setStringValue(Constant.insurance_id, response.getString("insurance_id").toString());
                }
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayMessage(String string) {
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG).show();
    }
}
