package com.example.backupplanclientcode.Menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.backupplanclientcode.Asyntask.GeneralTask;
import com.example.backupplanclientcode.Asyntask.GeneralTask.ResponseListener_General;
import com.example.backupplanclientcode.Bugsense.Bugsense;
import com.example.backupplanclientcode.ConnectionDetector;
import com.example.backupplanclientcode.Constant.Constant;
import com.example.backupplanclientcode.HttpLoader.HttpLoader;
import com.example.backupplanclientcode.LogOutTimerUtil;
import com.example.backupplanclientcode.Preference.SettingPreference;
import com.example.backupplanclientcode.R;
import com.example.backupplanclientcode.ServiceUrl.ServiceUrl;
import com.example.backupplanclientcode.TextViewerActivity;
import com.example.backupplanclientcode.loginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.drive.CreateFileActivityOptions;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.backupplanclientcode.LogOutTimerUtil.foreGround;
import static com.example.backupplanclientcode.LogOutTimerUtil.logout;

public class SaveProfileToGoogleDrive extends Activity implements ResponseListener_General, LogOutTimerUtil.LogOutListener {
    private static final int REQUEST_CODE_CREATOR = 2;
    private static final int REQUEST_CODE_SIGN_IN = 0;
    private static final String TAG = "drive-quickstart";
    private ConnectionDetector connection;
    private DriveClient mDriveClient;
    Throwable r5;
    private DriveResourceClient mDriveResourceClient;
    private GoogleSignInClient mGoogleSignInClient;
    private SettingPreference pref;
    /* access modifiers changed from: private */
    public String userProfileInfo = null;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_profile_to_google_drive);
        this.connection = new ConnectionDetector(getApplicationContext());
        new Bugsense().startBugsense(getApplicationContext());
        this.pref = new SettingPreference(getApplicationContext());
        findViewById(R.id.btn_open).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (SaveProfileToGoogleDrive.this.userProfileInfo != null) {
                    SaveProfileToGoogleDrive.this.generateNoteOnSD(SaveProfileToGoogleDrive.this, SaveProfileToGoogleDrive.this.userProfileInfo);
                }
            }
        });
        findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SaveProfileToGoogleDrive.this.finish();
            }
        });
        ((TextView) findViewById(R.id.actionBarTittle)).setText("Profile");
        findViewById(R.id.sign_in_button).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (SaveProfileToGoogleDrive.this.userProfileInfo != null) {
                    SaveProfileToGoogleDrive.this.signIn();
                }
            }
        });
        getProfile();
    }

    /* access modifiers changed from: private */
    public void signIn() {
        this.mGoogleSignInClient = buildGoogleSignInClient();
        startActivityForResult(this.mGoogleSignInClient.getSignInIntent(), 0);
    }

    private GoogleSignInClient buildGoogleSignInClient() {
        return GoogleSignIn.getClient((Activity) this, new Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestScopes(Drive.SCOPE_FILE, new Scope[0]).build());
    }

    private void saveFileToDrive() {
        this.mDriveResourceClient.createContents().continueWithTask(new Continuation<DriveContents, Task<Void>>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public Task<Void> then(@NonNull Task<DriveContents> task) throws Exception {
                return SaveProfileToGoogleDrive.this.createFileIntentSender((DriveContents) task.getResult());
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(@NonNull Exception e) {
                Log.w(SaveProfileToGoogleDrive.TAG, "Failed to create new contents.", e);
            }
        });
    }

    /* access modifiers changed from: private */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Task<Void> createFileIntentSender(DriveContents driveContents) {
        Log.i(TAG, "New contents created.");
        try {
            Writer writer = new OutputStreamWriter(driveContents.getOutputStream());
            Throwable th = null;
            writer.write(this.userProfileInfo);
            if (writer != null) {
                if (th != null) {
                    try {
                        writer.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    writer.close();
                }
            }

            return this.mDriveClient.newCreateFileActivityIntentSender(new CreateFileActivityOptions.Builder().setInitialMetadata(new MetadataChangeSet.Builder().setTitle("Profile").setMimeType(HTTP.PLAIN_TEXT_TYPE).setStarred(true).build()).setInitialDriveContents(driveContents).build()).continueWith(new Continuation<IntentSender, Void>() {
                public Void then(@NonNull Task<IntentSender> task) throws Exception {
                    SaveProfileToGoogleDrive.this.startIntentSenderForResult((IntentSender) task.getResult(), 2, null, 0, 0, 0);
                    return null;
                }
            });
        } catch (IOException e) {
            Log.w(TAG, "Unable to write file contents.", e);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == -1) {
                    this.mDriveClient = Drive.getDriveClient((Activity) this, GoogleSignIn.getLastSignedInAccount(this));
                    this.mDriveResourceClient = Drive.getDriveResourceClient((Activity) this, GoogleSignIn.getLastSignedInAccount(this));
                    saveFileToDrive();
                    return;
                }
                return;
            case 2:
                if (resultCode == -1) {
                    Toast.makeText(this, "Profile saved successfully on Google Drive.", Toast.LENGTH_LONG).show();
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void generateNoteOnSD(Context context, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "YourBackupPlan");
            if (!root.exists()) {
                root.mkdirs();
            }
            FileWriter writer = new FileWriter(new File(root, "Profile.txt"));
            writer.append(sBody);
            writer.flush();
            writer.close();
            showProfileData(sBody);
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "An error has occurred during opening profile file.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void showProfileData(String profileData) {
        Intent intent = new Intent(this, TextViewerActivity.class);
        intent.putExtra(HttpLoader.FILE_TYPE_TEXT, profileData);
        startActivity(intent);
    }

    private void getProfile() {
        if (!this.connection.isConnectingToInternet()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.connectionFailMessage), Toast.LENGTH_SHORT).show();
        } else if (!this.pref.getStringValue(Constant.profile_id, "").isEmpty() && !this.pref.getStringValue(Constant.user_id, "").isEmpty()) {
            try {
                JSONObject nameValuePairs = new JSONObject();
//                nameValuePairs.put("profile_id", this.pref.getStringValue(Constant.profile_id, ""));
                nameValuePairs.put("user_id", this.pref.getStringValue(Constant.user_id, ""));
                nameValuePairs.put("token", this.pref.getStringValue(Constant.jwttoken, ""));
                new GeneralTask(this, ServiceUrl.get_profile_detail, nameValuePairs, 2, "post").execute(new Void[0]);
            } catch (Exception e) {
            }
        }
    }

    public void on_GeneralSuccess(JSONObject response, int responseCode) {
        try {
            this.userProfileInfo = prepareDataForDrive(response.getJSONObject("profile"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String prepareDataForDrive(JSONObject json) throws JSONException {
        String driveData = (((((((((((("Profile \n" + "Full Name: " + json.getString("p_full_name").toString() + IOUtils.LINE_SEPARATOR_UNIX) + "Address: " + json.getString("p_address").toString() + IOUtils.LINE_SEPARATOR_UNIX) + "Mobile: " + json.getString("p_mobile").toString() + IOUtils.LINE_SEPARATOR_UNIX) + "Phone: " + json.getString("p_phone").toString() + IOUtils.LINE_SEPARATOR_UNIX) + "Birth Place: " + json.getString("p_birth_place").toString() + IOUtils.LINE_SEPARATOR_UNIX) + "Occupation: " + json.getString("p_current_occup").toString() + IOUtils.LINE_SEPARATOR_UNIX) + "SIN/SSN: " + json.getString("p_ssn").toString() + IOUtils.LINE_SEPARATOR_UNIX) + "\n\n\nParent\n") + "Birth Mother: " + json.getString("p_birth_mother").toString() + IOUtils.LINE_SEPARATOR_UNIX) + "Birth Father: " + json.getString("p_birth_father").toString() + IOUtils.LINE_SEPARATOR_UNIX) + "Step Mother: " + json.getString("p_step_mother").toString() + IOUtils.LINE_SEPARATOR_UNIX) + "Step Father: " + json.getString("p_step_father").toString() + IOUtils.LINE_SEPARATOR_UNIX) + "\n\n\nSiblings\n";
        JSONArray sibling = json.getJSONArray("sibling");
        for (int s = 0; s < sibling.length(); s++) {
            JSONObject siblingObj = sibling.getJSONObject(s);
            if (siblingObj.getString("brother").toString().length() > 0) {
                driveData = driveData + "Brother: " + siblingObj.getString("brother").toString() + IOUtils.LINE_SEPARATOR_UNIX;
            }
            if (siblingObj.getString("sister").toString().length() > 0) {
                driveData = driveData + "Sister: " + siblingObj.getString("sister").toString() + IOUtils.LINE_SEPARATOR_UNIX;
            }
        }
        String driveData2 = driveData + "\n\n\nChildern\n";
        JSONArray childrenJsonArray = json.getJSONArray("children");
        for (int c = 0; c < childrenJsonArray.length(); c++) {
            JSONObject childernObj = childrenJsonArray.getJSONObject(c);
            if (childernObj.getString("child_name").toString().length() > 0) {
                driveData2 = driveData2 + "Child: " + childernObj.getString("child_name").toString() + IOUtils.LINE_SEPARATOR_UNIX;
            }
        }
        JSONObject relationship = json.getJSONObject("relationship");
        String driveData3 = (((((((((((((((((driveData2 + "\n\n\nAttached Images\n") + "Alimony: " + relationship.getString("d_alimony").toString() + IOUtils.LINE_SEPARATOR_UNIX) + "Child Custody: " + relationship.getString("d_child_custody").toString() + IOUtils.LINE_SEPARATOR_UNIX) + "Divorce Certificate: " + relationship.getString("d_divorce_certi").toString() + IOUtils.LINE_SEPARATOR_UNIX) + "Sepration Agreement: " + relationship.getString("d_sepration_agree").toString() + IOUtils.LINE_SEPARATOR_UNIX) + "Marriage Agreement: " + relationship.getString("m_marriege_agree").toString() + IOUtils.LINE_SEPARATOR_UNIX) + "Marriage Certificate: " + relationship.getString("m_marriege_certi").toString() + IOUtils.LINE_SEPARATOR_UNIX) + "Birth Certificate: " + relationship.getString("s_birth_certi").toString() + IOUtils.LINE_SEPARATOR_UNIX) + "Citizen Paper: " + relationship.getString("s_citizen_paper").toString() + IOUtils.LINE_SEPARATOR_UNIX) + "Donation Rec: " + relationship.getString("s_donation_rec").toString() + IOUtils.LINE_SEPARATOR_UNIX) + "Passport: " + relationship.getString("s_passport").toString() + IOUtils.LINE_SEPARATOR_UNIX) + "Power Attorney: " + relationship.getString("s_power_attorney").toString() + IOUtils.LINE_SEPARATOR_UNIX) + "SSN: " + relationship.getString("s_ssn").toString() + IOUtils.LINE_SEPARATOR_UNIX) + "STrust: " + relationship.getString("s_trust").toString() + IOUtils.LINE_SEPARATOR_UNIX) + "War Record: " + relationship.getString("s_war_record").toString() + IOUtils.LINE_SEPARATOR_UNIX) + "Death Certificate: " + relationship.getString("w_death_certi").toString() + IOUtils.LINE_SEPARATOR_UNIX) + "Will: " + relationship.getString("w_will").toString() + IOUtils.LINE_SEPARATOR_UNIX) + "\n\n\nPets\n";
        JSONArray pet = json.getJSONArray("pet");
        for (int p = 0; p < pet.length(); p++) {
            JSONObject petObj = pet.getJSONObject(p);
            driveData3 = (((((driveData3 + "Pet Name: " + petObj.getString("p_name").toString() + IOUtils.LINE_SEPARATOR_UNIX) + "Pet Type: " + petObj.getString("p_type").toString() + IOUtils.LINE_SEPARATOR_UNIX) + "Date of Birth: " + petObj.getString("p_dob").toString() + IOUtils.LINE_SEPARATOR_UNIX) + "Veterinarian: " + petObj.getString("p_veterinarian").toString() + IOUtils.LINE_SEPARATOR_UNIX) + "Special need: " + petObj.getString("p_sepcial_need").toString() + IOUtils.LINE_SEPARATOR_UNIX) + "\n\n";
        }
        return driveData3;
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
