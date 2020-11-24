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
import android.util.Base64;
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
import com.example.backupplanclientcode.LogOutTimerUtil;
import com.example.backupplanclientcode.Preference.SettingPreference;
import com.example.backupplanclientcode.R;
import com.example.backupplanclientcode.ServiceUrl.ServiceUrl;
import com.example.backupplanclientcode.Utility.CompressImage;
import com.example.backupplanclientcode.loginActivity;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.example.backupplanclientcode.LogOutTimerUtil.foreGround;
import static com.example.backupplanclientcode.LogOutTimerUtil.logout;

public class MedicalMenuListActivity extends Activity implements OnClickListener, ResponseListerProfile, ResponseListener_General, LogOutTimerUtil.LogOutListener {
    private static final int SELECT_PICTURE = 1;
    TextView actionBarTittle;
    ImageView addAppointmentIcon;
    Button btn_back;
    Button btn_save;
    CompressImage compress;
    String selectedImage;
    ConnectionDetector connection;
    int countApointment = 0;
    ImageView currentImageVew;
    String delete_appoimment = "";
    EditText edit_Location;
    EditText edit_MedicalCard;
    EditText edit_doctor;
    EditText edit_medical_id;
    LinearLayout layout_appointment;
    SettingPreference pref;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medical_menu_layout);
        this.pref = new SettingPreference(getApplicationContext());
        this.compress = new CompressImage(getApplicationContext());
        this.connection = new ConnectionDetector(getApplicationContext());
        new Bugsense().startBugsense(getApplicationContext());
        findViwId();
        checkAlreadySaveinvestment();
    }

    private void findViwId() {
        this.btn_save = (Button) findViewById(R.id.btn_save);
        this.btn_back = (Button) findViewById(R.id.btn_back);
        this.actionBarTittle = (TextView) findViewById(R.id.actionBarTittle);
        this.addAppointmentIcon = (ImageView) findViewById(R.id.addAppointmentIcon);
        this.btn_save.setOnClickListener(this);
        this.btn_back.setOnClickListener(this);
        this.addAppointmentIcon.setOnClickListener(this);
        this.edit_doctor = (EditText) findViewById(R.id.edit_doctor);
        this.edit_Location = (EditText) findViewById(R.id.edit_Location);
        this.edit_MedicalCard = (EditText) findViewById(R.id.edit_MedicalCard);
        this.edit_medical_id = (EditText) findViewById(R.id.edit_medical_id);
        this.layout_appointment = (LinearLayout) findViewById(R.id.layout_appointment);
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            setEnableControl();
        }
    }

    private void setEnableControl() {
        this.actionBarTittle.setText(getResources().getString(R.string.menu_medical));
        this.btn_save.setVisibility(View.GONE);
        this.edit_doctor.setEnabled(false);
        this.edit_Location.setEnabled(false);
        this.edit_MedicalCard.setEnabled(false);
        this.edit_medical_id.setEnabled(false);
        this.addAppointmentIcon.setEnabled(false);
    }

    private void checkAlreadySaveinvestment() {
        if (this.pref.getStringValue(Constant.medical_id, "").equalsIgnoreCase("0") || this.pref.getStringValue(Constant.medical_id, "").isEmpty() || this.pref.getStringValue(Constant.user_id, "").isEmpty()) {
            this.actionBarTittle.setText(getResources().getString(R.string.menu_medical));
            addAppointmentlayout();
            return;
        }
        this.btn_save.setText("Edit");
        if (!this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            this.actionBarTittle.setText("Edit " + getResources().getString(R.string.menu_medical));
        }
        if (this.connection.isConnectingToInternet()) {
            try {
                JSONObject nameValuePair = new JSONObject();
                nameValuePair.put("user_id", this.pref.getStringValue(Constant.user_id, ""));
                nameValuePair.put("medical_id", this.pref.getStringValue(Constant.medical_id, "1"));
                nameValuePair.put("token", this.pref.getStringValue(Constant.jwttoken, ""));
                new GeneralTask(this, ServiceUrl.get_medical_detail, nameValuePair, 1, "post").execute(new Void[0]);
            } catch (Exception e) {

            }
            return;
        }
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.connectionFailMessage), Toast.LENGTH_SHORT).show();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save /*2131558589*/:
                prePareJson();
                return;
            case R.id.btn_back /*2131558590*/:
                finish();
                return;
            case R.id.addAppointmentIcon /*2131558884*/:
                addAppointmentlayout();
                return;
            default:
                return;
        }
    }

    private void prePareJson() {
        try {
            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            JSONArray jsonArray = new JSONArray();
            JSONObject appointJson = new JSONObject();
            appointJson.put("user_id", this.pref.getStringValue(Constant.user_id, ""));
            appointJson.put("delete_appoimment", this.delete_appoimment);
            appointJson.put("m_doctor", this.edit_doctor.getText().toString().trim());
            appointJson.put("m_location", this.edit_Location.getText().toString().trim());
            appointJson.put("m_medical_card", this.edit_MedicalCard.getText().toString().trim());
            appointJson.put("medical_id", this.edit_medical_id.getText().toString().trim());
            for (int i = 0; i < this.layout_appointment.getChildCount(); i++) {
                ViewGroup view = (ViewGroup) this.layout_appointment.getChildAt(i);
                JSONObject json = new JSONObject();
                EditText edit_Date = (EditText) view.findViewById(R.id.edit_Date);
                EditText edit_Physician = (EditText) view.findViewById(R.id.edit_Physician);
                EditText edit_complaint = (EditText) view.findViewById(R.id.edit_complaint);
                EditText edit_Evaluation = (EditText) view.findViewById(R.id.edit_Evaluation);
                EditText edit_Tritment = (EditText) view.findViewById(R.id.edit_Tritment);
                EditText edit_Results = (EditText) view.findViewById(R.id.edit_Results);
                EditText edit_Surgery = (EditText) view.findViewById(R.id.edit_Surgery);
                EditText edit_Prescription = (EditText) view.findViewById(R.id.edit_Prescription);
                ImageView img_Appointment = (ImageView) view.findViewById(R.id.img_Appointment);
                json.put("appointment_id", ((EditText) view.findViewById(R.id.edit_id)).getText().toString().trim());
                json.put("a_date", edit_Date.getText().toString().trim());
                json.put("a_doctor", edit_Physician.getText().toString().trim());
                json.put("a_complaint", edit_complaint.getText().toString().trim());
                json.put("a_evaluation", edit_Evaluation.getText().toString().trim());
                json.put("a_treatment", edit_Tritment.getText().toString().trim());
                json.put("a_surgery", edit_Surgery.getText().toString().trim());
                json.put("a_results", edit_Results.getText().toString().trim());
                json.put("a_prescription", edit_Prescription.getText().toString().trim());
                if (!img_Appointment.getContentDescription().toString().trim().isEmpty()) {
                    String[] arr = img_Appointment.getContentDescription().toString().split("/");
                    String atr = arr[arr.length - 1];
                    json.put("a_photo", atr);
                    if (img_Appointment.getTag().toString().equalsIgnoreCase("1")) {
//                        URL domain = new URL(img_Appointment.getContentDescription().toString());
//                        String destFolder = getCacheDir().getAbsolutePath();
//                        FileOutputStream out = new FileOutputStream(destFolder + "/" + atr);
//                        Log.d("test", "Path : " + domain.toURI().getPath());
//                        multipartEntity.addPart("a_photo[]", new FileBody(new File(destFolder + "/" + atr)));

                        json.put("is_file", 0);
                    } else {
                        multipartEntity.addPart("a_photo[]", new FileBody(new File(img_Appointment.getContentDescription().toString())));
                        json.put("is_file", 1);
                    }
                    Log.i("img_Appointment", img_Appointment.getContentDescription().toString());
                } else {
                    json.put("a_photo", "");
                    json.put("is_file", 0);
                }
                jsonArray.put(json);
            }
            appointJson.put("appoinment", jsonArray);
            JSONObject appoint_data = new JSONObject();
            appoint_data.put("medical_data", appointJson);
            JSONArray photo_array = new JSONArray();
            photo_array.put(0, selectedImage);
            Log.i("send json", appoint_data.toString());
            if (!this.connection.isConnectingToInternet()) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.connectionFailMessage), Toast.LENGTH_SHORT).show();
            } else if (this.btn_save.getText().toString().trim().equalsIgnoreCase("edit")) {
                multipartEntity.addPart("json_data", new StringBody(appoint_data.toString()));
                SaveProfileAsytask saveProfileAsytask = new SaveProfileAsytask(this, ServiceUrl.edit_medical, multipartEntity);
                saveProfileAsytask.execute(new Void[0]);
            } else {
                multipartEntity.addPart("json_data", new StringBody(appoint_data.toString()));
                SaveProfileAsytask saveProfileAsytask2 = new SaveProfileAsytask(this, ServiceUrl.save_medical, multipartEntity);
                saveProfileAsytask2.execute(new Void[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    private void addAppointmentlayout() {
        final View appointView = LayoutInflater.from(this).inflate(R.layout.layout_appointments, null);
        ImageView removeIcon = (ImageView) appointView.findViewById(R.id.removeIcon);
        removeIcon.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MedicalMenuListActivity.this.layout_appointment.removeView(appointView);
            }
        });
        if (this.countApointment == 0) {
            removeIcon.setVisibility(View.GONE);
        }
        final ImageView img_Appointment = (ImageView) appointView.findViewById(R.id.img_Appointment);
        img_Appointment.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MedicalMenuListActivity.this.currentImageVew = img_Appointment;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                MedicalMenuListActivity.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        EditText edit_id = (EditText) appointView.findViewById(R.id.edit_id);
        EditText edit_Date = (EditText) appointView.findViewById(R.id.edit_Date);
        EditText edit_Physician = (EditText) appointView.findViewById(R.id.edit_Physician);
        EditText edit_complaint = (EditText) appointView.findViewById(R.id.edit_complaint);
        EditText edit_Evaluation = (EditText) appointView.findViewById(R.id.edit_Evaluation);
        EditText edit_Tritment = (EditText) appointView.findViewById(R.id.edit_Tritment);
        EditText edit_Surgery = (EditText) appointView.findViewById(R.id.edit_Surgery);
        EditText edit_Results = (EditText) appointView.findViewById(R.id.edit_Results);
        EditText edit_Prescription = (EditText) appointView.findViewById(R.id.edit_Prescription);
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            removeIcon.setVisibility(View.GONE);
            img_Appointment.setEnabled(false);
            edit_id.setEnabled(false);
            edit_Date.setEnabled(false);
            edit_Physician.setEnabled(false);
            edit_complaint.setEnabled(false);
            edit_Evaluation.setEnabled(false);
            edit_Tritment.setEnabled(false);
            edit_Surgery.setEnabled(false);
            edit_Results.setEnabled(false);
            edit_Prescription.setEnabled(false);
        }
        this.layout_appointment.addView(appointView);
        this.countApointment++;
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
            /*
             * Get the column indexes of the data in the Cursor,
             * move to the first row in the Cursor, get the data,
             * and display it.
             */
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int column_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            Log.d("test", cursor.getString(nameIndex));
//            Log.d("test", getRealPathFromURI(imageUri));
            //            try {
//                String[] proj = { MediaStore.Images.Media.DATA };
//                cursor = getContentResolver().query(imageUri,  proj, null, null, null);
//                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                cursor.moveToFirst();
//            Log.d("test", cursor.getString(column_index) + "");
//            } finally {
//                if (cursor != null) {
//                    cursor.close();
//                }
//            }
            this.selectedImage = cursor.getString(nameIndex);
            this.currentImageVew.setImageBitmap(selectedImage);
            this.currentImageVew.setContentDescription(imageUri.getPath());
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
            this.currentImageVew.setTag("0");

            Log.d("test", "selectedImage " + selectedImage);
            Log.d("test", "imageUri.getPath() " + imageUri.getPath());

        }
    }

    public static String getPath(Context context, Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }

    public void on_GeneralSuccess(JSONObject response, int responseCode) {
        if (response == null) {
            addAppointmentlayout();
        } else {
            showAppointmentDetail(response);
        }
    }

    @SuppressLint({"InflateParams"})
    private void showAppointmentDetail(JSONObject response) {
        try {
            JSONObject medical_data = response.getJSONObject("medical_data");
            JSONArray appointmentArray = medical_data.getJSONArray("appoinment");
            this.edit_doctor.setText(medical_data.getString("m_doctor").toString().trim());
            this.edit_Location.setText(medical_data.getString("m_location").toString().trim());
            this.edit_MedicalCard.setText(medical_data.getString("m_medical_card").toString().trim());
            this.edit_medical_id.setText(medical_data.getString("medical_id").toString().trim());
            for (int i = 0; i < appointmentArray.length(); i++) {
                JSONObject json = appointmentArray.getJSONObject(i);
                View view = LayoutInflater.from(this).inflate(R.layout.layout_appointments, null);
                EditText edit_id = (EditText) view.findViewById(R.id.edit_id);
                EditText edit_Date = (EditText) view.findViewById(R.id.edit_Date);
                EditText edit_Physician = (EditText) view.findViewById(R.id.edit_Physician);
                EditText edit_complaint = (EditText) view.findViewById(R.id.edit_complaint);
                EditText edit_Evaluation = (EditText) view.findViewById(R.id.edit_Evaluation);
                EditText edit_Tritment = (EditText) view.findViewById(R.id.edit_Tritment);
                EditText edit_Surgery = (EditText) view.findViewById(R.id.edit_Surgery);
                EditText edit_Results = (EditText) view.findViewById(R.id.edit_Results);
                EditText edit_Prescription = (EditText) view.findViewById(R.id.edit_Prescription);
                if (json.has("appointment_id")) {
                    edit_id.setText(json.getString("appointment_id"));
                }
                if (json.has("a_date")) {
                    edit_Date.setText(json.getString("a_date"));
                }
                if (json.has("a_doctor")) {
                    edit_Physician.setText(json.getString("a_doctor"));
                }
                if (json.has("a_complaint")) {
                    edit_complaint.setText(json.getString("a_complaint"));
                }
                if (json.has("a_evaluation")) {
                    edit_Evaluation.setText(json.getString("a_evaluation"));
                }
                if (json.has("a_treatment")) {
                    edit_Tritment.setText(json.getString("a_treatment"));
                }
                if (json.has("a_surgery")) {
                    edit_Surgery.setText(json.getString("a_surgery"));
                }
                if (json.has("a_results")) {
                    edit_Results.setText(json.getString("a_results"));
                }
                if (json.has("a_prescription")) {
                    edit_Prescription.setText(json.getString("a_prescription"));
                }
                ImageView removeIcon = (ImageView) view.findViewById(R.id.removeIcon);
                if (i == 0) {
                    removeIcon.setVisibility(View.GONE);
                }
                if (json.has("appointment_id")) {
                    removeIcon.setTag(json.getString("appointment_id"));
                    final View view2 = view;
                    final ImageView imageView = removeIcon;
                    removeIcon.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            MedicalMenuListActivity.this.layout_appointment.removeView(view2);
                            if (MedicalMenuListActivity.this.delete_appoimment.equalsIgnoreCase("")) {
                                MedicalMenuListActivity.this.delete_appoimment = MedicalMenuListActivity.this.delete_appoimment.concat(imageView.getTag().toString());
                                return;
                            }
                            MedicalMenuListActivity.this.delete_appoimment = MedicalMenuListActivity.this.delete_appoimment.concat("," + imageView.getTag().toString());
                        }
                    });
                }
                ImageView img_Appointment = (ImageView) view.findViewById(R.id.img_Appointment);
                if (json.has("a_photo")) {
                    UrlImageViewHelper.setUrlDrawable(img_Appointment, json.getString("a_photo").toString().trim(), (int) R.drawable.img);
                    img_Appointment.setTag("1");
                    img_Appointment.setContentDescription(json.getString("a_photo").toString().trim());
                    final ImageView imageView2 = img_Appointment;
                    img_Appointment.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            MedicalMenuListActivity.this.currentImageVew = imageView2;
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction("android.intent.action.GET_CONTENT");
                            MedicalMenuListActivity.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                        }
                    });
                }
                if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                    removeIcon.setVisibility(View.GONE);
                    img_Appointment.setEnabled(false);
                    edit_id.setEnabled(false);
                    edit_Date.setEnabled(false);
                    edit_Physician.setEnabled(false);
                    edit_complaint.setEnabled(false);
                    edit_Evaluation.setEnabled(false);
                    edit_Tritment.setEnabled(false);
                    edit_Surgery.setEnabled(false);
                    edit_Results.setEnabled(false);
                    edit_Prescription.setEnabled(false);
                }
                this.layout_appointment.addView(view);
                this.countApointment++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void on_ProfileSuccess(JSONObject response) {
        try {
            if (response.has("message")) {
                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
            }
            if (response.has("success")) {
                if (response.has("medical_id")) {
                    this.pref.setStringValue(Constant.medical_id, response.getString("medical_id"));
                }
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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