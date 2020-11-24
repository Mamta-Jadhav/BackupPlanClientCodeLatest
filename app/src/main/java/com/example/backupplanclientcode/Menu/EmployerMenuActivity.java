package com.example.backupplanclientcode.Menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.backupplanclientcode.LogOutTimerUtil.foreGround;
import static com.example.backupplanclientcode.LogOutTimerUtil.logout;

public class EmployerMenuActivity extends Activity implements OnClickListener, ResponseListerProfile, ResponseListener_General, LogOutTimerUtil.LogOutListener {
    private static final int SELECT_PICTURE = 1;
    TextView actionBarTittle;
    Button btn_back;
    Button btn_save;
    CompressImage compress;
    ConnectionDetector connection;
    ImageView currentImageVew;
    EditText editAmountAccident;
    EditText editAmountLife;
    EditText editCmpAccident;
    EditText editCmpIns;
    EditText editCmpLife;
    EditText editCovrageAmount;
    EditText editid;
    ImageView imgDental;
    ImageView imgMedical;
    ImageView imgPension;
    ImageView imgTravel;
    SettingPreference pref;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_employer);
        this.pref = new SettingPreference(getApplicationContext());
        this.compress = new CompressImage(getApplicationContext());
        this.connection = new ConnectionDetector(getApplicationContext());
        new Bugsense().startBugsense(getApplicationContext());
        findViewId();
        checkAlredySaveAccount();
    }

    private void checkAlredySaveAccount() {
        if (this.pref.getStringValue(Constant.employer_id, "").equalsIgnoreCase("0") || this.pref.getStringValue(Constant.employer_id, "").isEmpty() || this.pref.getStringValue(Constant.user_id, "").isEmpty()) {
            this.actionBarTittle.setText(getResources().getString(R.string.menu_employer));
            return;
        }
        this.btn_save.setText("Edit");
        if (!this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            this.actionBarTittle.setText("Edit " + getResources().getString(R.string.menu_employer));
        }
        if (this.connection.isConnectingToInternet()) {
            try {
                JSONObject nameValuePair = new JSONObject();
                nameValuePair.put("user_id", this.pref.getStringValue(Constant.user_id, ""));
//                nameValuePair.put("employer_id", "2");//this.pref.getStringValue(Constant.employer_id, ""));
                nameValuePair.put("token", this.pref.getStringValue(Constant.jwttoken, ""));
                new GeneralTask(this, ServiceUrl.get_employer_detail, nameValuePair, 2, "post").execute(new Void[0]);
            } catch (Exception e) {
            }
            return;
        }
        displayMessage(getResources().getString(R.string.connectionFailMessage));
    }

    private void findViewId() {
        this.actionBarTittle = (TextView) findViewById(R.id.actionBarTittle);
        this.btn_save = (Button) findViewById(R.id.btn_save);
        this.btn_back = (Button) findViewById(R.id.btn_back);
        this.btn_back.setOnClickListener(this);
        this.btn_save.setOnClickListener(this);
        this.editid = (EditText) findViewById(R.id.editid);
        this.editAmountLife = (EditText) findViewById(R.id.editAmountLife);
        this.editCmpLife = (EditText) findViewById(R.id.editCmpLife);
        this.editAmountAccident = (EditText) findViewById(R.id.editAmountAccident);
        this.editCmpAccident = (EditText) findViewById(R.id.editCmpAccident);
        this.editCovrageAmount = (EditText) findViewById(R.id.editCovrageAmount);
        this.editCmpIns = (EditText) findViewById(R.id.editCmpIns);
        this.imgMedical = (ImageView) findViewById(R.id.imgMedical);
        this.imgMedical.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                EmployerMenuActivity.this.currentImageVew = EmployerMenuActivity.this.imgMedical;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                EmployerMenuActivity.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        this.imgTravel = (ImageView) findViewById(R.id.imgTravel);
        this.imgTravel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                EmployerMenuActivity.this.currentImageVew = EmployerMenuActivity.this.imgTravel;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                EmployerMenuActivity.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        this.imgDental = (ImageView) findViewById(R.id.imgDental);
        this.imgDental.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                EmployerMenuActivity.this.currentImageVew = EmployerMenuActivity.this.imgDental;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                EmployerMenuActivity.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        this.imgPension = (ImageView) findViewById(R.id.imgPension);
        this.imgPension.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                EmployerMenuActivity.this.currentImageVew = EmployerMenuActivity.this.imgPension;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                EmployerMenuActivity.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            setEnableControl();
        }
    }

    private void setEnableControl() {
        this.actionBarTittle.setText(getResources().getString(R.string.menu_employer));
        this.btn_save.setVisibility(View.GONE);
        this.editAmountLife.setEnabled(false);
        this.editCmpLife.setEnabled(false);
        this.editAmountAccident.setEnabled(false);
        this.editCmpAccident.setEnabled(false);
        this.editCovrageAmount.setEnabled(false);
        this.editCmpIns.setEnabled(false);
        this.imgMedical.setEnabled(false);
        this.imgTravel.setEnabled(false);
        this.imgDental.setEnabled(false);
        this.imgPension.setEnabled(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save /*2131558589*/:
                prePareJson();
                return;
            case R.id.btn_back /*2131558590*/:
                finish();
                return;
            default:
                return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void prePareJson() {
        try {
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            JSONObject json = new JSONObject();
            json.put("user_id", this.pref.getStringValue(Constant.user_id, ""));
            json.put("employer_id", this.editid.getText().toString().trim());
            json.put("life_ins_amt", this.editAmountLife.getText().toString().trim());
            json.put("life_ins_company", this.editCmpLife.getText().toString().trim());
            json.put("acc_dis_amt", this.editAmountAccident.getText().toString().trim());
            json.put("acc_dis_company", this.editCmpAccident.getText().toString().trim());
            json.put("dis_amount", this.editCovrageAmount.getText().toString().trim());
            json.put("dis_company", this.editCmpIns.getText().toString().trim());
            if (!this.imgMedical.getContentDescription().toString().isEmpty()) {
                String[] arr = this.imgMedical.getContentDescription().toString().split("/");
                String atr = arr[arr.length - 1];
                json.put("medical_photo", atr);
                Log.d("test", "TTAAGG :: " + this.imgMedical.getTag());
                if (this.imgMedical.getTag().toString().equalsIgnoreCase("1")) {
//                    URL domain = new URL(this.imgMedical.getContentDescription().toString());
//                    String destFolder = getCacheDir().getAbsolutePath();
//                    FileOutputStream out = new FileOutputStream(destFolder + "/" + atr);
//                    Log.d("test", "Path : " + domain.toURI().getPath());
//                    entity.addPart("medical_photo[]", new FileBody(new File(destFolder + "/" + atr)));
                    json.put("is_file", 0);
                } else {
                    json.put("is_file", 1);
                    entity.addPart("medical_photo[]", new FileBody(new File(this.imgMedical.getContentDescription().toString())));
                }
            } else {
                json.put("is_file", 0);
                json.put("medical_photo", "");
            }
            if (!this.imgTravel.getContentDescription().toString().isEmpty()) {
                String[] arr = this.imgTravel.getContentDescription().toString().split("/");
                String atr = arr[arr.length - 1];
                json.put("travel_photo", atr);
                if (this.imgTravel.getTag().toString().equalsIgnoreCase("1")) {
//                    URL domain = new URL(this.imgTravel.getContentDescription().toString());
//                    String destFolder = getCacheDir().getAbsolutePath();
//                    FileOutputStream out = new FileOutputStream(destFolder + "/" + atr);
//                    Log.d("test", "Path : " + domain.toURI().getPath());
//                    entity.addPart("travel_photo[]", new FileBody(new File(destFolder + "/" + atr)));
                    json.put("is_file", 0);
                } else {
                    json.put("is_file", 1);
                    entity.addPart("travel_photo[]", new FileBody(new File(this.imgTravel.getContentDescription().toString())));
                }
            } else {
                json.put("is_file", 0);
                json.put("travel_photo", "");
            }
            if (!this.imgDental.getContentDescription().toString().isEmpty()) {
                String[] arr = this.imgDental.getContentDescription().toString().split("/");
                String atr = arr[arr.length - 1];
                json.put("dental_photo", atr);
                if (this.imgDental.getTag().toString().equalsIgnoreCase("1")) {
//                    URL domain = new URL(this.imgDental.getContentDescription().toString());
//                    String destFolder = getCacheDir().getAbsolutePath();
//                    FileOutputStream out = new FileOutputStream(destFolder + "/" + atr);
//                    Log.d("test", "Path : " + domain.toURI().getPath());
//                    entity.addPart("dental_photo[]", new FileBody(new File(destFolder + "/" + atr)));
                    json.put("is_file", 0);
                } else {
                    json.put("is_file", 1);
                    entity.addPart("dental_photo[]", new FileBody(new File(this.imgDental.getContentDescription().toString())));
                }
            } else {
                json.put("is_file", 0);
                json.put("dental_photo", "");
            }
            if (!this.imgPension.getContentDescription().toString().isEmpty()) {
                String[] arr = this.imgPension.getContentDescription().toString().split("/");
                String atr = arr[arr.length - 1];
                json.put("pension_photo", atr);
                if (this.imgPension.getTag().toString().equalsIgnoreCase("1")) {
//                    URL domain = new URL(this.imgPension.getContentDescription().toString());
//                    String destFolder = getCacheDir().getAbsolutePath();
//                    FileOutputStream out = new FileOutputStream(destFolder + "/" + atr);
//                    Log.d("test", "Path : " + domain.toURI().getPath());
//                    entity.addPart("pension_photo[]", new FileBody(new File(destFolder + "/" + atr)));
                    json.put("is_file", 0);
                } else {
                    json.put("is_file", 1);
                    entity.addPart("pension_photo[]", new FileBody(new File(this.imgPension.getContentDescription().toString())));
                }
            } else {
                json.put("is_file", 0);
                json.put("pension_photo", "");
            }
            JSONObject sendJson = new JSONObject();
            sendJson.put("employer_data", json);
            Log.e("send json", sendJson.toString());
            entity.addPart("json_data", new StringBody(sendJson.toString()));
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("json_data", sendJson.toString()));
            if (!this.connection.isConnectingToInternet()) {
                displayMessage(getResources().getString(R.string.connectionFailMessage));
            } else if (this.btn_save.getText().toString().trim().equalsIgnoreCase("edit")) {
                new SaveProfileAsytask(this, ServiceUrl.edit_employer, entity).execute(new Void[0]);
            } else {
                new SaveProfileAsytask(this, ServiceUrl.save_employer, entity).execute(new Void[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void on_GeneralSuccess(JSONObject response, int responseCode) {
        try {
            JSONObject json = response.getJSONObject("employer");
            this.editid.setText(json.getString("employer_id").toString().trim());
            this.editAmountLife.setText(json.getString("life_ins_amt").toString().trim());
            this.editCmpLife.setText(json.getString("life_ins_company").toString().trim());
            this.editAmountAccident.setText(json.getString("acc_dis_amt").toString().trim());
            this.editCmpAccident.setText(json.getString("acc_dis_company").toString().trim());
            this.editCovrageAmount.setText(json.getString("dis_amount").toString().trim());
            this.editCmpIns.setText(json.getString("dis_company").toString().trim());
            UrlImageViewHelper.setUrlDrawable(this.imgMedical, json.getString("medical_photo").toString().trim(), (int) R.drawable.img);
            this.imgMedical.setContentDescription(json.getString("medical_photo").toString().trim());
            this.imgMedical.setTag("1");
            UrlImageViewHelper.setUrlDrawable(this.imgTravel, json.getString("travel_photo").toString().trim(), (int) R.drawable.img);
            this.imgTravel.setContentDescription(json.getString("travel_photo").toString().trim());
            this.imgTravel.setTag("1");
            UrlImageViewHelper.setUrlDrawable(this.imgDental, json.getString("dental_photo").toString().trim(), (int) R.drawable.img);
            this.imgDental.setContentDescription(json.getString("dental_photo").toString().trim());
            this.imgDental.setTag("1");
            UrlImageViewHelper.setUrlDrawable(this.imgPension, json.getString("pension_photo").toString().trim(), (int) R.drawable.img);
            this.imgPension.setContentDescription(json.getString("pension_photo").toString().trim());
            this.imgPension.setTag("1");
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
                if (response.has("employer_id")) {
                    this.pref.setStringValue(Constant.employer_id, response.getString("employer_id").toString());
                }
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean downloadFile(final String path) {
        try {
            URL url = new URL(path);

            URLConnection ucon = url.openConnection();
            ucon.setReadTimeout(5000);
            ucon.setConnectTimeout(10000);

            InputStream is = ucon.getInputStream();
            BufferedInputStream inStream = new BufferedInputStream(is, 1024 * 5);

            File file = new File(path);

            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            FileOutputStream outStream = new FileOutputStream(file);
            byte[] buff = new byte[5 * 1024];

            int len;
            while ((len = inStream.read(buff)) != -1) {
                outStream.write(buff, 0, len);
            }
            outStream.flush();
            outStream.close();
            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void displayMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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
            this.currentImageVew.setTag("0");
            Log.d("test", "TTAAGG res :: " + this.currentImageVew.getTag());

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