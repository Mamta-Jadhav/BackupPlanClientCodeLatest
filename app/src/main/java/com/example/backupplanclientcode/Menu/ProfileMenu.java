package com.example.backupplanclientcode.Menu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.backupplanclientcode.CustomeFiledActivity;
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

public class ProfileMenu extends Activity implements OnClickListener, ResponseListerProfile, ResponseListener_General {
    private static final int SELECT_IMAGE = 4;
    private static final int SELECT_PICTURE = 2;
    private static final int SELECT_SIBLING = 3;
    TextView actionBarTittle;
    LinearLayout addChildresnLayout;
    LinearLayout add_sibling_layout;
    Button btn_back;
    Button btn_save;
    int child_images_count = 0;
    ImageView childrenAddIcon;
    int childrenCount = 0;
    CompressImage compress;
    ConnectionDetector connection;
    ImageView current_imageview;
    View current_view;
    String delete_children = "";
    String delete_images = "";
    String delete_pet = "";
    String delete_sibling = "";
    EditText edit_CommonLaw;
    EditText edit_SpousePartnerName;
    EditText edit_address;
    EditText edit_birth_Father;
    EditText edit_birth_mother;
    EditText edit_birthplace;
    EditText edit_current_occupation;
    EditText edit_fullname;
    EditText edit_mobile;
    EditText edit_phone;
    EditText edit_sin_ssn;
    EditText edit_step_father;
    EditText edit_step_mother;
    ImageView imgAlimony;
    ImageView imgBirthCertificate;
    ImageView imgChildCustodyRecords;
    ImageView imgDeathCertificate;
    ImageView imgDivorceDecreeCertificate;
    ImageView imgImmigrationPapers;
    ImageView imgMaintenanceRecords;
    ImageView imgMarriageCertificate;
    ImageView imgOrganDonationRecords;
    ImageView imgPassport;
    ImageView imgPreMarriageAgreements;
    ImageView imgSIN_SSN;
    ImageView imgSeparationAgreement;
    ImageView imgTrust;
    ImageView imgWarVeteranRecords;
    ImageView imgWill;
    ImageView imgWillPowerAttorney;
    JSONArray jsonArr_children = new JSONArray();
    JSONArray jsonArr_pets;
    JSONArray jsonArr_sibling;
    JSONObject json_profile = new JSONObject();
    LayoutInflater linf;
    ArrayList<HashMap<String, String>> list_images;
    LinearLayout mainLayout;
    ImageView petIcon;
    LinearLayout petLayouts;
    int petlayoutCount = 0;
    SettingPreference pref;
    JSONObject profileDetail_JSON = new JSONObject();
    String relationship_id = "";
    int scrollview_child_count = 0;
    int siblingCount = 0;
    JSONObject siblingJson;
    ImageView sibling_add_icon;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_form_layout);
        this.pref = new SettingPreference(getApplicationContext());
        this.compress = new CompressImage(getApplicationContext());
        this.connection = new ConnectionDetector(getApplicationContext());
        new Bugsense().startBugsense(getApplicationContext());
        findViewId();
        gettingProfile();
    }

    private void gettingProfile() {
        if (!this.connection.isConnectingToInternet()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.connectionFailMessage), Toast.LENGTH_SHORT).show();
            ADDParentToLayout(true);
            ArrayList<String> customFiled = new ArrayList<>();
            customFiled.add(getResources().getString(R.string.hint_brother));
            customFiled.add(getResources().getString(R.string.hint_sister));
            addSiblingLayout(this.siblingCount, customFiled, 3);
            addPetSLayout(this.petlayoutCount);
        } else if (this.pref.getStringValue(Constant.profile_id, "").equalsIgnoreCase("0") || this.pref.getStringValue(Constant.profile_id, "").isEmpty() || this.pref.getStringValue(Constant.user_id, "").isEmpty()) {
            this.actionBarTittle.setText(getResources().getString(R.string.menu_profile));
            ADDParentToLayout(true);
            ArrayList<String> customFiled2 = new ArrayList<>();
            customFiled2.add(getResources().getString(R.string.hint_brother));
            customFiled2.add(getResources().getString(R.string.hint_sister));
            addSiblingLayout(this.siblingCount, customFiled2, 3);
            addPetSLayout(this.petlayoutCount);
        } else {
            try {
                JSONObject nameValuePairs = new JSONObject();
//                nameValuePairs.put("profile_id", this.pref.getStringValue(Constant.profile_id, ""));
                nameValuePairs.put("user_id", this.pref.getStringValue(Constant.user_id, ""));
                nameValuePairs.put("token", this.pref.getStringValue(Constant.jwttoken, ""));
                new GeneralTask(this, ServiceUrl.get_profile_detail, nameValuePairs, 2, "post").execute(new Void[0]);
                this.btn_save.setText("Save");
                if (!this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                    this.actionBarTittle.setText("Edit " + getResources().getString(R.string.menu_profile));
                }
            } catch (Exception e) {

            }
        }
    }

    private void findViewId() {
        this.actionBarTittle = (TextView) findViewById(R.id.actionBarTittle);
        this.btn_back = (Button) findViewById(R.id.btn_back);
        this.btn_save = (Button) findViewById(R.id.btn_save);
        this.sibling_add_icon = (ImageView) findViewById(R.id.sibling_add_icon);
        this.childrenAddIcon = (ImageView) findViewById(R.id.childrenAddIcon);
        this.petIcon = (ImageView) findViewById(R.id.petIcon);
        this.imgPassport = (ImageView) findViewById(R.id.imgPassport);
        this.imgPassport.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ProfileMenu.this.current_imageview = ProfileMenu.this.imgPassport;
                ProfileMenu.this.select_certificate();
            }
        });
        this.imgBirthCertificate = (ImageView) findViewById(R.id.imgBirthCertificate);
        this.imgBirthCertificate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ProfileMenu.this.current_imageview = ProfileMenu.this.imgBirthCertificate;
                ProfileMenu.this.select_certificate();
            }
        });
        this.imgSIN_SSN = (ImageView) findViewById(R.id.imgSIN_SSN);
        this.imgSIN_SSN.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ProfileMenu.this.current_imageview = ProfileMenu.this.imgSIN_SSN;
                ProfileMenu.this.select_certificate();
            }
        });
        this.imgImmigrationPapers = (ImageView) findViewById(R.id.imgImmigrationPapers);
        this.imgImmigrationPapers.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ProfileMenu.this.current_imageview = ProfileMenu.this.imgImmigrationPapers;
                ProfileMenu.this.select_certificate();
            }
        });
        this.imgWillPowerAttorney = (ImageView) findViewById(R.id.imgWillPowerAttorney);
        this.imgWillPowerAttorney.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ProfileMenu.this.current_imageview = ProfileMenu.this.imgWillPowerAttorney;
                ProfileMenu.this.select_certificate();
            }
        });
        this.imgTrust = (ImageView) findViewById(R.id.imgTrust);
        this.imgTrust.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ProfileMenu.this.current_imageview = ProfileMenu.this.imgTrust;
                ProfileMenu.this.select_certificate();
            }
        });
        this.imgWarVeteranRecords = (ImageView) findViewById(R.id.imgWarVeteranRecords);
        this.imgWarVeteranRecords.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ProfileMenu.this.current_imageview = ProfileMenu.this.imgWarVeteranRecords;
                ProfileMenu.this.select_certificate();
            }
        });
        this.imgOrganDonationRecords = (ImageView) findViewById(R.id.imgOrganDonationRecords);
        this.imgOrganDonationRecords.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ProfileMenu.this.current_imageview = ProfileMenu.this.imgOrganDonationRecords;
                ProfileMenu.this.select_certificate();
            }
        });
        this.imgMarriageCertificate = (ImageView) findViewById(R.id.imgMarriageCertificate);
        this.imgMarriageCertificate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ProfileMenu.this.current_imageview = ProfileMenu.this.imgMarriageCertificate;
                ProfileMenu.this.select_certificate();
            }
        });
        this.imgPreMarriageAgreements = (ImageView) findViewById(R.id.imgPreMarriageAgreements);
        this.imgPreMarriageAgreements.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ProfileMenu.this.current_imageview = ProfileMenu.this.imgPreMarriageAgreements;
                ProfileMenu.this.select_certificate();
            }
        });
        this.imgMaintenanceRecords = (ImageView) findViewById(R.id.imgMaintenanceRecords);
        this.imgMaintenanceRecords.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ProfileMenu.this.current_imageview = ProfileMenu.this.imgMaintenanceRecords;
                ProfileMenu.this.select_certificate();
            }
        });
        this.imgAlimony = (ImageView) findViewById(R.id.imgAlimony);
        this.imgAlimony.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ProfileMenu.this.current_imageview = ProfileMenu.this.imgAlimony;
                ProfileMenu.this.select_certificate();
            }
        });
        this.imgChildCustodyRecords = (ImageView) findViewById(R.id.imgChildCustodyRecords);
        this.imgChildCustodyRecords.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ProfileMenu.this.current_imageview = ProfileMenu.this.imgChildCustodyRecords;
                ProfileMenu.this.select_certificate();
            }
        });
        this.imgDivorceDecreeCertificate = (ImageView) findViewById(R.id.imgDivorceDecreeCertificate);
        this.imgDivorceDecreeCertificate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ProfileMenu.this.current_imageview = ProfileMenu.this.imgDivorceDecreeCertificate;
                ProfileMenu.this.select_certificate();
            }
        });
        this.imgSeparationAgreement = (ImageView) findViewById(R.id.imgSeparationAgreement);
        this.imgSeparationAgreement.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ProfileMenu.this.current_imageview = ProfileMenu.this.imgSeparationAgreement;
                ProfileMenu.this.select_certificate();
            }
        });
        this.imgDeathCertificate = (ImageView) findViewById(R.id.imgDeathCertificate);
        this.imgDeathCertificate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ProfileMenu.this.current_imageview = ProfileMenu.this.imgDeathCertificate;
                ProfileMenu.this.select_certificate();
            }
        });
        this.imgWill = (ImageView) findViewById(R.id.imgWill);
        this.imgWill.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ProfileMenu.this.current_imageview = ProfileMenu.this.imgWill;
                ProfileMenu.this.select_certificate();
            }
        });
        this.edit_fullname = (EditText) findViewById(R.id.edit_fullname);
        this.edit_address = (EditText) findViewById(R.id.edit_address);
        this.edit_phone = (EditText) findViewById(R.id.edit_phone);
        this.edit_mobile = (EditText) findViewById(R.id.edit_mobile);
        this.edit_birthplace = (EditText) findViewById(R.id.edit_birthplace);
        this.edit_current_occupation = (EditText) findViewById(R.id.edit_current_occupation);
        this.edit_sin_ssn = (EditText) findViewById(R.id.edit_sin_ssn);
        this.edit_birth_mother = (EditText) findViewById(R.id.edit_birth_mother);
        this.edit_birth_Father = (EditText) findViewById(R.id.edit_birth_Father);
        this.edit_step_mother = (EditText) findViewById(R.id.edit_step_mother);
        this.edit_step_father = (EditText) findViewById(R.id.edit_step_father);
        this.edit_SpousePartnerName = (EditText) findViewById(R.id.edit_SpousePartnerName);
        this.edit_CommonLaw = (EditText) findViewById(R.id.edit_CommonLaw);
        this.add_sibling_layout = (LinearLayout) findViewById(R.id.add_sibling_layout);
        this.addChildresnLayout = (LinearLayout) findViewById(R.id.addChildresnLayout);
        this.petLayouts = (LinearLayout) findViewById(R.id.petLayouts);
        this.mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        this.btn_back.setOnClickListener(this);
        this.btn_save.setOnClickListener(this);
        this.sibling_add_icon.setOnClickListener(this);
        this.childrenAddIcon.setOnClickListener(this);
        this.petIcon.setOnClickListener(this);
        this.linf = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.linf = LayoutInflater.from(this);
        this.mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            setEnableControl();
        }
    }

    private void setEnableControl() {
        this.actionBarTittle.setText(getResources().getString(R.string.menu_profile));
        this.imgPassport.setEnabled(false);
        this.imgBirthCertificate.setEnabled(false);
        this.imgSIN_SSN.setEnabled(false);
        this.imgImmigrationPapers.setEnabled(false);
        this.imgWillPowerAttorney.setEnabled(false);
        this.imgTrust.setEnabled(false);
        this.imgWarVeteranRecords.setEnabled(false);
        this.imgOrganDonationRecords.setEnabled(false);
        this.imgMarriageCertificate.setEnabled(false);
        this.imgPreMarriageAgreements.setEnabled(false);
        this.imgMaintenanceRecords.setEnabled(false);
        this.imgAlimony.setEnabled(false);
        this.imgChildCustodyRecords.setEnabled(false);
        this.imgDivorceDecreeCertificate.setEnabled(false);
        this.imgSeparationAgreement.setEnabled(false);
        this.imgDeathCertificate.setEnabled(false);
        this.imgWill.setEnabled(false);
        this.edit_fullname.setEnabled(false);
        this.edit_address.setEnabled(false);
        this.edit_phone.setEnabled(false);
        this.edit_mobile.setEnabled(false);
        this.edit_birthplace.setEnabled(false);
        this.edit_current_occupation.setEnabled(false);
        this.edit_sin_ssn.setEnabled(false);
        this.edit_birth_mother.setEnabled(false);
        this.edit_birth_Father.setEnabled(false);
        this.edit_step_mother.setEnabled(false);
        this.edit_step_father.setEnabled(false);
        this.edit_SpousePartnerName.setEnabled(false);
        this.edit_CommonLaw.setEnabled(false);
        this.sibling_add_icon.setEnabled(false);
        this.childrenAddIcon.setEnabled(false);
        this.petIcon.setEnabled(false);
        this.btn_save.setVisibility(View.GONE);
        for (int i = 0; i < this.addChildresnLayout.getChildCount(); i++) {
            LinearLayout layout = (LinearLayout) ((ViewGroup) this.addChildresnLayout.getChildAt(i)).findViewById(R.id.layout_images);
            for (int j = 0; j < layout.getChildCount(); j++) {
                ((Button) ((ViewGroup) layout.getChildAt(j)).findViewById(R.id.btn_remove)).setVisibility(View.GONE);
            }
        }
    }

    @SuppressLint({"InflateParams"})
    private void addPetSLayout(int count) {
        final View view = this.linf.inflate(R.layout.layout_pets, null);
        ImageView removepetIcon = (ImageView) view.findViewById(R.id.removepetIcon);
        removepetIcon.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ProfileMenu.this.petLayouts.removeView(view);
            }
        });
        if (count == 0) {
            removepetIcon.setVisibility(View.GONE);
        }
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            removepetIcon.setVisibility(View.GONE);
        }
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            EditText edit_petType = (EditText) view.findViewById(R.id.edit_petType);
            EditText edit_petDateOfBirth = (EditText) view.findViewById(R.id.edit_petDateOfBirth);
            EditText edit_petVeterinarian = (EditText) view.findViewById(R.id.edit_petVeterinarian);
            EditText edit_petSpecialneeds = (EditText) view.findViewById(R.id.edit_petSpecialneeds);
            ((EditText) view.findViewById(R.id.edit_petname)).setEnabled(false);
            edit_petType.setEnabled(false);
            edit_petDateOfBirth.setEnabled(false);
            edit_petVeterinarian.setEnabled(false);
            edit_petSpecialneeds.setEnabled(false);
        }
        this.petLayouts.addView(view);
        this.petlayoutCount++;
    }

    /* access modifiers changed from: private */
    public void select_certificate() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 4);
    }

    @SuppressLint({"InflateParams"})
    private void child_more_images(final View view, boolean flag) {
        LinearLayout layout_images = (LinearLayout) view.findViewById(R.id.layout_images);
        final View view_images = this.linf.inflate(R.layout.layout_images, null);
        final ImageView addMoreImage = (ImageView) view_images.findViewById(R.id.iv);
        Button btn_remove = (Button) view_images.findViewById(R.id.btn_remove);
        addMoreImage.setTag("image");
        addMoreImage.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ProfileMenu.this.current_view = view;
                ProfileMenu.this.current_imageview = addMoreImage;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                ProfileMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
            }
        });
        btn_remove.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ((ViewGroup) view_images.getParent()).removeView(view_images);
            }
        });
        layout_images.addView(view_images);
        btn_remove.setVisibility(View.GONE);
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            btn_remove.setVisibility(View.GONE);
            addMoreImage.setEnabled(false);
            return;
        }
        for (int i = 0; i < this.addChildresnLayout.getChildCount(); i++) {
            LinearLayout layout = (LinearLayout) ((ViewGroup) this.addChildresnLayout.getChildAt(i)).findViewById(R.id.layout_images);
            for (int j = 0; j < layout.getChildCount(); j++) {
                ViewGroup childImages = (ViewGroup) layout.getChildAt(j);
                Button removeBTN = (Button) childImages.findViewById(R.id.btn_remove);
                if (!((ImageView) childImages.findViewById(R.id.iv)).getContentDescription().toString().trim().isEmpty() || !removeBTN.getTag().toString().isEmpty()) {
                    removeBTN.setVisibility(View.VISIBLE);
                } else {
                    removeBTN.setVisibility(View.GONE);
                }
            }
        }
    }

    @SuppressLint({"InflateParams"})
    private void addSiblingLayout(int count, ArrayList<String> arrayList, int flag) {
        if (flag != 0) {
            final View view = this.linf.inflate(R.layout.sibling_layout, null);
            ImageView removeSiblingIcon = (ImageView) view.findViewById(R.id.removeSiblingIcon);
            removeSiblingIcon.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    ProfileMenu.this.add_sibling_layout.removeView(view);
                }
            });
            if (count == 0) {
                removeSiblingIcon.setVisibility(View.GONE);
            } else {
                removeSiblingIcon.setVisibility(View.VISIBLE);
            }
            EditText editViewBrother = (EditText) view.findViewById(R.id.editViewBrother);
            EditText editViewSister = (EditText) view.findViewById(R.id.editViewSister);
            if (flag == 1) {
                editViewBrother.setVisibility(View.VISIBLE);
                editViewSister.setVisibility(View.GONE);
            } else if (flag == 2) {
                editViewBrother.setVisibility(View.GONE);
                editViewSister.setVisibility(View.VISIBLE);
            } else {
                editViewBrother.setVisibility(View.VISIBLE);
                editViewSister.setVisibility(View.VISIBLE);
            }
            if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                editViewBrother.setEnabled(false);
                editViewSister.setEnabled(false);
                removeSiblingIcon.setVisibility(View.GONE);
            }
            this.add_sibling_layout.addView(view);
        }
    }

    @SuppressLint({"InflateParams"})
    public void ADDParentToLayout(boolean flag) {
        final View view = this.linf.inflate(R.layout.layout_parent, null);
        ImageView removeChildIcon = (ImageView) view.findViewById(R.id.removeChildIcon);
        removeChildIcon.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ProfileMenu.this.addChildresnLayout.removeView(view);
            }
        });
        Button btn_remove = (Button) view.findViewById(R.id.btn_remove);
        if (flag) {
            btn_remove.setVisibility(View.GONE);
        } else {
            btn_remove.setVisibility(View.VISIBLE);
        }
        EditText childname = (EditText) view.findViewById(R.id.et);
        final ImageView iv = (ImageView) view.findViewById(R.id.iv);
        iv.setTag("image");
        iv.setContentDescription("");
        iv.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ProfileMenu.this.current_view = view;
                ProfileMenu.this.current_imageview = iv;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                ProfileMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
            }
        });
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            iv.setEnabled(false);
            removeChildIcon.setVisibility(View.GONE);
            childname.setEnabled(false);
            btn_remove.setVisibility(View.GONE);
        }
        if (this.child_images_count == 0) {
            removeChildIcon.setVisibility(View.GONE);
        }
        this.addChildresnLayout.addView(view);
        this.child_images_count++;
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 3) {
            addSiblingLayout(this.siblingCount, data.getStringArrayListExtra("customFieldList"), data.getIntExtra("sibling_flag", 0));
            this.siblingCount++;
        } else if (resultCode == -1 && requestCode == 2) {
            Uri selectedImageUri = data.getData();
            String[] filePathColumn = {"_data"};
            Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            String picturePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
            cursor.close();
            this.current_imageview.setImageBitmap(this.compress.compressImage(selectedImageUri.toString(), picturePath));
            if (this.current_imageview.getTag().toString().equalsIgnoreCase("image")) {
                Log.e("equalsIgnoreCase(image)", "......if");
                if (this.current_imageview.getContentDescription().toString().equalsIgnoreCase("")) {
                    Log.e("!equalsIgnoreCase()", "......if");
                    this.current_imageview.setContentDescription(picturePath.toString());
                    child_more_images(this.current_view, true);
                } else {
                    this.current_imageview.setContentDescription(picturePath.toString());
                    Log.e("!equalsIgnoreCase()", "......else");
                }
            }
            ((Button) this.current_view.findViewById(R.id.btn_remove)).setVisibility(View.VISIBLE);
            Log.e("select image path", picturePath.toString());
            if (this.current_imageview.getTag().toString().equalsIgnoreCase("image")) {
                return;
            }
            if (this.delete_images.equalsIgnoreCase("")) {
                this.delete_images = this.delete_images.concat(this.current_imageview.getTag().toString());
            } else {
                this.delete_images = this.delete_images.concat("," + this.current_imageview.getTag().toString());
            }
        } else if (resultCode == -1 && requestCode == 4) {
            Uri selectedImageUri2 = data.getData();
            String[] filePathColumn2 = {"_data"};
            Cursor cursor2 = getContentResolver().query(selectedImageUri2, filePathColumn2, null, null, null);
            cursor2.moveToFirst();
            String picturePath2 = cursor2.getString(cursor2.getColumnIndex(filePathColumn2[0]));
            cursor2.close();
            this.current_imageview.setImageBitmap(this.compress.compressImage(selectedImageUri2.toString(), picturePath2));
            this.current_imageview.setContentDescription(picturePath2.toString());
            Log.e("select image path", picturePath2.toString());
        }
    }

    private void PrepareJsonArray() {
        int count = 0;
        this.list_images = new ArrayList<>();
        for (int i = 0; i < this.addChildresnLayout.getChildCount(); i++) {
            JSONObject obj = new JSONObject();
            ViewGroup parent = (ViewGroup) this.addChildresnLayout.getChildAt(i);
            EditText edit_children_id = (EditText) parent.findViewById(R.id.edit_children_id);
            LinearLayout layout_images = (LinearLayout) parent.findViewById(R.id.layout_images);
            try {
                obj.put("child_name", ((EditText) parent.findViewById(R.id.et)).getText().toString());
                obj.put("children_id", edit_children_id.getText().toString().trim());
                String str = "";
                for (int j = 0; j < layout_images.getChildCount(); j++) {
                    HashMap<String, String> item_map = new HashMap<>();
                    ImageView iv = (ImageView) ((ViewGroup) layout_images.getChildAt(j)).findViewById(R.id.iv);
                    if (!iv.getContentDescription().toString().equalsIgnoreCase("")) {
                        Log.e("is not Empty", "...." + iv.getContentDescription().toString());
                        if (str.equalsIgnoreCase("")) {
                            str = iv.getTag().toString() + count;
                        } else {
                            str = str + "," + iv.getTag() + count;
                        }
                        item_map.put("image_name", iv.getTag().toString() + count);
                        item_map.put("image_path", iv.getContentDescription().toString());
                        count++;
                        this.list_images.add(item_map);
                    } else {
                        Log.e("is Empty", "...." + iv.getContentDescription().toString());
                    }
                }
                obj.put("children_images", str);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            this.jsonArr_children.put(obj);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save /*2131558589*/:
                PrepareJsonArray();
                PrepareJsonSibling();
                PrepareJsonPets();
                PrepareProfileJson();
                SendingJson();
                return;
            case R.id.btn_back /*2131558590*/:
                finish();
                return;
            case R.id.sibling_add_icon /*2131559096*/:
                Intent addField = new Intent(this, CustomeFiledActivity.class);
                addField.putExtra("visible_part", "siblings");
                startActivityForResult(addField, 3);
                return;
            case R.id.childrenAddIcon /*2131559100*/:
                ADDParentToLayout(true);
                return;
            case R.id.petIcon /*2131559118*/:
                addPetSLayout(this.petlayoutCount);
                this.petlayoutCount++;
                return;
            default:
                return;
        }
    }

    private void SendingJson() {
        try {
            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            multipartEntity.addPart("json_data", new StringBody(this.json_profile.toString()));
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("json_data", this.json_profile.toString()));
            Log.i("Send Json data", this.json_profile.toString());
            for (int i = 0; i < this.list_images.size(); i++) {
                File file = new File((String) ((HashMap) this.list_images.get(i)).get("image_path"));
                multipartEntity.addPart((String) ((HashMap) this.list_images.get(i)).get("image_name"), new FileBody(file, "image/jpeg"));
                Log.i("file parameter", ((String) ((HashMap) this.list_images.get(i)).get("image_name")).toString());
                Log.i("file path", ((String) ((HashMap) this.list_images.get(i)).get("image_path")).toString());
            }
            if (!this.imgPassport.getContentDescription().toString().isEmpty()) {
                File file2 = new File(this.imgPassport.getContentDescription().toString());
                multipartEntity.addPart("s_passport", new FileBody(file2, "image/jpeg"));
            }
            if (!this.imgBirthCertificate.getContentDescription().toString().isEmpty()) {
                File file3 = new File(this.imgBirthCertificate.getContentDescription().toString());
                multipartEntity.addPart("s_birth_certi", new FileBody(file3, "image/jpeg"));
            }
            if (!this.imgSIN_SSN.getContentDescription().toString().equalsIgnoreCase("")) {
                File file4 = new File(this.imgSIN_SSN.getContentDescription().toString());
                FileBody fileBody = new FileBody(file4, "image/jpeg");
                multipartEntity.addPart("s_ssn", fileBody);
            }
            if (!this.imgImmigrationPapers.getContentDescription().toString().isEmpty()) {
                File file5 = new File(this.imgImmigrationPapers.getContentDescription().toString());
                multipartEntity.addPart("s_citizen_paper", new FileBody(file5, "image/jpeg"));
            }
            if (!this.imgWillPowerAttorney.getContentDescription().toString().isEmpty()) {
                File file6 = new File(this.imgWillPowerAttorney.getContentDescription().toString());
                FileBody fileBody2 = new FileBody(file6, "image/jpeg");
                multipartEntity.addPart("s_power_attorney", fileBody2);
            }
            if (!this.imgTrust.getContentDescription().toString().isEmpty()) {
                File file7 = new File(this.imgTrust.getContentDescription().toString());
                FileBody fileBody3 = new FileBody(file7, "image/jpeg");
                multipartEntity.addPart("s_trust", fileBody3);
            }
            if (!this.imgWarVeteranRecords.getContentDescription().toString().isEmpty()) {
                File file8 = new File(this.imgWarVeteranRecords.getContentDescription().toString());
                FileBody fileBody4 = new FileBody(file8, "image/jpeg");
                multipartEntity.addPart("s_war_record", fileBody4);
            }
            if (!this.imgOrganDonationRecords.getContentDescription().toString().isEmpty()) {
                File file9 = new File(this.imgOrganDonationRecords.getContentDescription().toString());
                multipartEntity.addPart("s_donation_rec", new FileBody(file9, "image/jpeg"));
            }
            if (!this.imgMarriageCertificate.getContentDescription().toString().isEmpty()) {
                File file10 = new File(this.imgMarriageCertificate.getContentDescription().toString());
                multipartEntity.addPart("m_marriege_certi", new FileBody(file10, "image/jpeg"));
            }
            if (!this.imgPreMarriageAgreements.getContentDescription().toString().isEmpty()) {
                File file11 = new File(this.imgPreMarriageAgreements.getContentDescription().toString());
                multipartEntity.addPart("m_marriege_agree", new FileBody(file11, "image/jpeg"));
            }
            if (!this.imgMaintenanceRecords.getContentDescription().toString().isEmpty()) {
                File file12 = new File(this.imgMaintenanceRecords.getContentDescription().toString());
                multipartEntity.addPart("d_maintenance_rec", new FileBody(file12, "image/jpeg"));
            }
            if (!this.imgAlimony.getContentDescription().toString().isEmpty()) {
                File file13 = new File(this.imgAlimony.getContentDescription().toString());
                multipartEntity.addPart("d_alimony", new FileBody(file13, "image/jpeg"));
            }
            if (!this.imgChildCustodyRecords.getContentDescription().toString().isEmpty()) {
                File file14 = new File(this.imgChildCustodyRecords.getContentDescription().toString());
                multipartEntity.addPart("d_child_custody", new FileBody(file14, "image/jpeg"));
            }
            if (!this.imgDivorceDecreeCertificate.getContentDescription().toString().isEmpty()) {
                File file15 = new File(this.imgDivorceDecreeCertificate.getContentDescription().toString());
                multipartEntity.addPart("d_divorce_certi", new FileBody(file15, "image/jpeg"));
            }
            if (!this.imgSeparationAgreement.getContentDescription().toString().isEmpty()) {
                File file16 = new File(this.imgSeparationAgreement.getContentDescription().toString());
                multipartEntity.addPart("d_sepration_agree", new FileBody(file16, "image/jpeg"));
            }
            if (!this.imgDeathCertificate.getContentDescription().toString().isEmpty()) {
                File file17 = new File(this.imgDeathCertificate.getContentDescription().toString());
                FileBody fileBody5 = new FileBody(file17, "image/jpeg");
                multipartEntity.addPart("w_death_certi", fileBody5);
            }
            if (!this.imgWill.getContentDescription().toString().isEmpty()) {
                FileBody fileBody6 = new FileBody(new File(this.imgWill.getContentDescription().toString()), "image/jpeg");
                multipartEntity.addPart("w_will", fileBody6);
            }
            if (!this.connection.isConnectingToInternet()) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.connectionFailMessage), Toast.LENGTH_SHORT).show();
            } else if (this.btn_save.getText().toString().trim().equalsIgnoreCase("save")) {
                SaveProfileAsytask saveProfileAsytask = new SaveProfileAsytask(this, ServiceUrl.save_profile, nameValuePairs);
                saveProfileAsytask.execute(new Void[0]);
            } else {
                SaveProfileAsytask saveProfileAsytask2 = new SaveProfileAsytask(this, ServiceUrl.edit_profile, nameValuePairs);
                saveProfileAsytask2.execute(new Void[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void PrepareProfileJson() {
        try {
            JSONObject jobj = new JSONObject();
            jobj.put("sibling", this.jsonArr_sibling);
            jobj.put("children", this.jsonArr_children);
            jobj.put("pet", this.jsonArr_pets);
            try {
                jobj.put("relationship_id", this.relationship_id);
                jobj.put("delete_images", this.delete_images);
                jobj.put("delete_children", this.delete_children);
                jobj.put("delete_pet", this.delete_pet);
                jobj.put("delete_sibling", this.delete_sibling);
                jobj.put("user_id", this.pref.getStringValue(Constant.user_id, "").toString());
                jobj.put("p_full_name", this.edit_fullname.getText().toString().trim());
                jobj.put("p_address", this.edit_address.getText().toString().trim());
                if (this.pref.getStringValue(Constant.profile_id, "") != "0" && this.pref.getStringValue(Constant.profile_id, "") != "") {
                    jobj.put("profile_id", this.pref.getStringValue(Constant.profile_id, ""));
                } else {
                    jobj.put("profile_id", "");
                }
                jobj.put("p_phone", this.edit_phone.getText().toString().trim());
                jobj.put("p_mobile", this.edit_mobile.getText().toString().trim());
                jobj.put("p_birth_place", this.edit_birthplace.getText().toString().trim());
                jobj.put("p_current_occup", this.edit_current_occupation.getText().toString().trim());
                jobj.put("p_ssn", this.edit_sin_ssn.getText().toString().trim());
                jobj.put("p_birth_mother", this.edit_birth_mother.getText().toString().trim());
                jobj.put("p_birth_father", this.edit_birth_Father.getText().toString().trim());
                jobj.put("p_step_mother", this.edit_step_mother.getText().toString().trim());
                jobj.put("p_step_father", this.edit_birth_Father.getText().toString().trim());
                jobj.put("p_partner_name", this.edit_SpousePartnerName.getText().toString().trim());
                jobj.put("p_partner_law", this.edit_CommonLaw.getText().toString().trim());
                JSONObject json_Relation = new JSONObject();
                json_Relation.put("s_passport", this.imgPassport.getContentDescription().toString());
                json_Relation.put("s_birth_certi", this.imgBirthCertificate.getContentDescription().toString());
                json_Relation.put("s_ssn", this.imgSIN_SSN.getContentDescription().toString());
                json_Relation.put("s_citizen_paper", this.imgImmigrationPapers.getContentDescription().toString());
                json_Relation.put("s_power_attorney", this.imgWillPowerAttorney.getContentDescription().toString());
                json_Relation.put("s_trust", this.imgTrust.getContentDescription().toString());
                json_Relation.put("s_war_record", this.imgWarVeteranRecords.getContentDescription().toString());
                json_Relation.put("s_donation_rec", this.imgOrganDonationRecords.getContentDescription().toString());
                json_Relation.put("m_marriege_certi", this.imgMarriageCertificate.getContentDescription().toString());
                json_Relation.put("m_marriege_agree", this.imgPreMarriageAgreements.getContentDescription().toString());
                json_Relation.put("d_maintenance_rec", this.imgMaintenanceRecords.getContentDescription().toString());
                json_Relation.put("d_alimony", this.imgAlimony.getContentDescription().toString());
                json_Relation.put("d_child_custody", this.imgChildCustodyRecords.getContentDescription().toString());
                json_Relation.put("d_divorce_certi", this.imgDivorceDecreeCertificate.getContentDescription().toString());
                json_Relation.put("d_sepration_agree", this.imgSeparationAgreement.getContentDescription().toString());
                json_Relation.put("w_death_certi", this.imgDeathCertificate.getContentDescription().toString());
                json_Relation.put("w_will", this.imgWill.getContentDescription().toString());
                jobj.put("relationship", json_Relation);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            this.json_profile.put("profile_data", jobj);
            Log.e("json_profile", "....." + this.json_profile);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
    }

    private void PrepareJsonPets() {
        try {
            this.jsonArr_pets = new JSONArray();
            for (int i = 0; i < this.petLayouts.getChildCount(); i++) {
                JSONObject siblingJson2 = new JSONObject();
                ViewGroup parent = (ViewGroup) this.petLayouts.getChildAt(i);
                EditText edit_petname = (EditText) parent.findViewById(R.id.edit_petname);
                EditText edit_petType = (EditText) parent.findViewById(R.id.edit_petType);
                EditText edit_petDateOfBirth = (EditText) parent.findViewById(R.id.edit_petDateOfBirth);
                EditText edit_petVeterinarian = (EditText) parent.findViewById(R.id.edit_petVeterinarian);
                EditText edit_petSpecialneeds = (EditText) parent.findViewById(R.id.edit_petSpecialneeds);
                siblingJson2.put("pet_id", ((EditText) parent.findViewById(R.id.edit_petid)).getText().toString().trim());
                siblingJson2.put("p_name", edit_petname.getText().toString().trim());
                siblingJson2.put("p_type", edit_petType.getText().toString().trim());
                siblingJson2.put("p_dob", edit_petDateOfBirth.getText().toString().trim());
                siblingJson2.put("p_veterinarian", edit_petVeterinarian.getText().toString().trim());
                siblingJson2.put("p_sepcial_need", edit_petSpecialneeds.getText().toString().trim());
                this.jsonArr_pets.put(siblingJson2);
            }
            Log.i("Pets Json :", this.jsonArr_pets.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void PrepareJsonSibling() {
        try {
            this.jsonArr_sibling = new JSONArray();
            for (int i = 0; i < this.add_sibling_layout.getChildCount(); i++) {
                JSONObject siblingJson2 = new JSONObject();
                ViewGroup parent = (ViewGroup) this.add_sibling_layout.getChildAt(i);
                EditText editViewBrother = (EditText) parent.findViewById(R.id.editViewBrother);
                EditText editViewSister = (EditText) parent.findViewById(R.id.editViewSister);
                siblingJson2.put("sibling_id", ((EditText) parent.findViewById(R.id.edit_sibling_id)).getText().toString().trim());
                if (editViewBrother.getVisibility() == View.VISIBLE) {
                    siblingJson2.put("brother", editViewBrother.getText().toString().trim());
                } else {
                    siblingJson2.put("brother", "");
                }
                if (editViewSister.getVisibility() == View.VISIBLE) {
                    siblingJson2.put("sister", editViewSister.getText().toString().trim());
                } else {
                    siblingJson2.put("sister", "");
                }
                this.jsonArr_sibling.put(siblingJson2);
            }
            Log.i("Sibling :", this.jsonArr_sibling.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void on_ProfileSuccess(JSONObject response) {
        Log.d("~~~ Updated ~~~~>", "" + response.toString());
        try {
            if (this.btn_save.getText().toString().trim().equalsIgnoreCase("save")) {
                Toast.makeText(getApplicationContext(), response.getString("message").toString(), Toast.LENGTH_SHORT).show();
                if (response.getString("success").toString().trim().equalsIgnoreCase("1")) {
                }
            } else {
                Toast.makeText(getApplicationContext(), response.getString("message").toString(), Toast.LENGTH_LONG).show();
                this.pref.setStringValue(Constant.profile_id, response.getString("profile_id").toString().trim());
                if (response.getString("success").toString().toString().trim().equalsIgnoreCase("1")) {
                }
            }
            if (response.getString("success").toString().trim().equalsIgnoreCase("1")) {
//                showBackupConfirmation();
//                displayMessage(response.getString("message").toString());
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint({"InflateParams"})
    public void on_GeneralSuccess(JSONObject response, int responseCode) {
        try {
            JSONObject json1 = response.getJSONObject("profile");
            JSONObject json = json1.getJSONArray("profile").getJSONObject(0);
            this.edit_fullname.setText(json.getString("p_full_name"));
            this.edit_address.setText(json.getString("p_address"));
            this.edit_phone.setText(json.getString("p_phone"));
            this.edit_mobile.setText(json.getString("p_mobile"));
            this.edit_birthplace.setText(json.getString("p_birth_place"));
            this.edit_current_occupation.setText(json.getString("p_current_occup"));
            this.edit_sin_ssn.setText(json.getString("p_ssn"));
            this.edit_birth_mother.setText(json.getString("p_birth_mother"));
            this.edit_birth_Father.setText(json.getString("p_birth_father"));
            this.edit_step_mother.setText(json.getString("p_step_mother"));
            this.edit_step_father.setText(json.getString("p_step_father"));
            this.edit_SpousePartnerName.setText(json.getString("p_partner_name"));
            this.edit_CommonLaw.setText(json.getString("p_partner_law"));
            JSONArray sibling = response.getJSONObject("sibling").getJSONArray("sibling");
            if (sibling.length() <= 1) {
                ArrayList<String> customFiled = new ArrayList<>();
                customFiled.add(getResources().getString(R.string.hint_brother));
                customFiled.add(getResources().getString(R.string.hint_sister));
                addSiblingLayout(this.siblingCount, customFiled, 3);
                this.siblingCount++;
            } else {
                for (int i = 0; i < sibling.length(); i++) {
                    JSONObject siblingJson2 = sibling.getJSONObject(i);
                    View view = this.linf.inflate(R.layout.sibling_layout, null);
                    ImageView removeSiblingIcon = (ImageView) view.findViewById(R.id.removeSiblingIcon);
                    removeSiblingIcon.setTag(siblingJson2.getString("sibling_id"));
                    final View view2 = view;
                    final ImageView imageView = removeSiblingIcon;
                    removeSiblingIcon.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            ProfileMenu.this.add_sibling_layout.removeView(view2);
                            if (ProfileMenu.this.delete_sibling.equalsIgnoreCase("")) {
                                ProfileMenu.this.delete_sibling = ProfileMenu.this.delete_sibling.concat(imageView.getTag().toString());
                                return;
                            }
                            ProfileMenu.this.delete_sibling = ProfileMenu.this.delete_sibling.concat("," + imageView.getTag().toString());
                        }
                    });
                    if (i == 0) {
                        removeSiblingIcon.setVisibility(View.GONE);
                    }
                    EditText editViewBrother = (EditText) view.findViewById(R.id.editViewBrother);
                    EditText editViewSister = (EditText) view.findViewById(R.id.editViewSister);
                    ((EditText) view.findViewById(R.id.edit_sibling_id)).setText(siblingJson2.getString("sibling_id"));
                    if (!siblingJson2.getString("brother").toString().equalsIgnoreCase("")) {
                        editViewBrother.setVisibility(View.VISIBLE);
                        editViewBrother.setText(siblingJson2.getString("brother"));
                    } else {
                        editViewBrother.setVisibility(View.GONE);
                    }
                    if (!siblingJson2.getString("sister").toString().equalsIgnoreCase("")) {
                        editViewSister.setVisibility(View.VISIBLE);
                        editViewSister.setText(siblingJson2.getString("sister").toString());
                    } else {
                        editViewSister.setVisibility(View.GONE);
                    }
                    if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                        editViewSister.setEnabled(false);
                        editViewBrother.setEnabled(false);
                    }
                    this.add_sibling_layout.addView(view);
                    this.siblingCount++;
                }
            }
            JSONArray childrenJsonArray = response.getJSONObject("children").getJSONArray("children");
            if (childrenJsonArray.length() <= 1) {
                ADDParentToLayout(true);
            } else {
                for (int j = 0; j < childrenJsonArray.length(); j++) {
                    View view3 = this.linf.inflate(R.layout.layout_parent, null);
                    JSONObject children = childrenJsonArray.getJSONObject(j);
                    ((EditText) view3.findViewById(R.id.edit_children_id)).setText(children.getString("children_id"));
                    ImageView removeChildIcon = (ImageView) view3.findViewById(R.id.removeChildIcon);
                    removeChildIcon.setTag(children.getString("children_id"));
                    final View view4 = view3;
                    final ImageView imageView2 = removeChildIcon;
                    removeChildIcon.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            ProfileMenu.this.addChildresnLayout.removeView(view4);
                            if (ProfileMenu.this.delete_children.equalsIgnoreCase("")) {
                                ProfileMenu.this.delete_children = ProfileMenu.this.delete_children.concat(imageView2.getTag().toString());
                                return;
                            }
                            ProfileMenu.this.delete_children = ProfileMenu.this.delete_children.concat("," + imageView2.getTag().toString());
                        }
                    });
                    if (j == 0) {
                        removeChildIcon.setVisibility(View.GONE);
                        this.childrenCount++;
                    }
                    EditText childname = (EditText) view3.findViewById(R.id.et);
                    childname.setText(children.getString("child_name"));
                    ImageView childImage = (ImageView) view3.findViewById(R.id.iv);
                    childImage.setVisibility(View.GONE);
                    LinearLayout layout_images = (LinearLayout) view3.findViewById(R.id.layout_images);
                    if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                        childname.setEnabled(false);
                        childImage.setEnabled(false);
                        removeChildIcon.setVisibility(View.GONE);
                    }
                    JSONArray childImages = children.getJSONArray("children_images");
                    Log.e("Child Images", childImages.toString());
                    for (int i2 = 0; i2 < childImages.length(); i2++) {
                        JSONObject childdetail = childImages.getJSONObject(i2);
                        View viewimages = this.linf.inflate(R.layout.layout_images, null);
                        ImageView iv = (ImageView) viewimages.findViewById(R.id.iv);
                        iv.setTag(childdetail.getString("image_id"));
                        UrlImageViewHelper.setUrlDrawable(iv, childdetail.getString("image").toString(), (int) R.drawable.img);
                        Button btn_remove = (Button) viewimages.findViewById(R.id.btn_remove);
                        btn_remove.setTag(childdetail.getString("image_id"));
                        btn_remove.setVisibility(View.VISIBLE);
                        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                            btn_remove.setVisibility(View.GONE);
                            iv.setEnabled(false);
                        }
                        final View view5 = viewimages;
                        final ImageView imageView3 = iv;
                        btn_remove.setOnClickListener(new OnClickListener() {
                            public void onClick(View v) {
                                ((ViewGroup) view5.getParent()).removeView(view5);
                                if (!imageView3.getTag().toString().equalsIgnoreCase("image")) {
                                    if (ProfileMenu.this.delete_images.equalsIgnoreCase("")) {
                                        ProfileMenu.this.delete_images = ProfileMenu.this.delete_images.concat(imageView3.getTag().toString());
                                    } else {
                                        ProfileMenu.this.delete_images = ProfileMenu.this.delete_images.concat("," + imageView3.getTag().toString());
                                    }
                                }
                                Log.i("delete images id", ProfileMenu.this.delete_images);
                            }
                        });
                        final View view6 = view3;
                        final ImageView imageView4 = iv;
                        iv.setOnClickListener(new OnClickListener() {
                            public void onClick(View v) {
                                ProfileMenu.this.current_view = view6;
                                ProfileMenu.this.current_imageview = imageView4;
                                ProfileMenu.this.current_imageview.setContentDescription("");
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction("android.intent.action.GET_CONTENT");
                                ProfileMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
                            }
                        });
                        layout_images.addView(viewimages);
                    }
                    child_more_images(layout_images, false);
                    this.addChildresnLayout.addView(view3);
                    this.child_images_count++;
                }
            }
            JSONArray pet = response.getJSONObject("pet").getJSONArray("pet");
            if (pet.length() <= 1) {
                addPetSLayout(this.petlayoutCount);
                this.petlayoutCount++;
                return;
            }
            for (int i3 = 0; i3 < pet.length(); i3++) {
                JSONObject petJson = pet.getJSONObject(i3);
                View view7 = this.linf.inflate(R.layout.layout_pets, null);
                ImageView removepetIcon = (ImageView) view7.findViewById(R.id.removepetIcon);
                removepetIcon.setTag(petJson.getString("pet_id"));
                if (i3 == 0) {
                    removepetIcon.setVisibility(View.GONE);
                    this.petlayoutCount++;
                }
                final View view8 = view7;
                final ImageView imageView5 = removepetIcon;
                removepetIcon.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        ProfileMenu.this.petLayouts.removeView(view8);
                        if (ProfileMenu.this.delete_pet.equalsIgnoreCase("")) {
                            ProfileMenu.this.delete_pet = ProfileMenu.this.delete_pet.concat(imageView5.getTag().toString());
                            return;
                        }
                        ProfileMenu.this.delete_pet = ProfileMenu.this.delete_pet.concat("," + imageView5.getTag().toString());
                    }
                });
                ((EditText) view7.findViewById(R.id.edit_petid)).setText(petJson.getString("pet_id"));
                EditText edit_petname = (EditText) view7.findViewById(R.id.edit_petname);
                edit_petname.setText(petJson.getString("p_name"));
                EditText edit_petType = (EditText) view7.findViewById(R.id.edit_petType);
                edit_petType.setText(petJson.getString("p_type"));
                EditText edit_petDateOfBirth = (EditText) view7.findViewById(R.id.edit_petDateOfBirth);
                edit_petDateOfBirth.setText(petJson.getString("p_dob"));
                EditText edit_petVeterinarian = (EditText) view7.findViewById(R.id.edit_petVeterinarian);
                edit_petVeterinarian.setText(petJson.getString("p_veterinarian"));
                EditText edit_petSpecialneeds = (EditText) view7.findViewById(R.id.edit_petSpecialneeds);
                edit_petSpecialneeds.setText(petJson.getString("p_sepcial_need"));
                if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                    edit_petname.setEnabled(false);
                    edit_petType.setEnabled(false);
                    edit_petDateOfBirth.setEnabled(false);
                    edit_petVeterinarian.setEnabled(false);
                    edit_petSpecialneeds.setEnabled(false);
                }
                this.petLayouts.addView(view7);
            }
            JSONObject relationship = json.getJSONObject("relationship");
            this.relationship_id = relationship.getString("relationship_id").toString();
            UrlImageViewHelper.setUrlDrawable(this.imgPassport, relationship.getString("s_passport").toString(), (int) R.drawable.img);
            UrlImageViewHelper.setUrlDrawable(this.imgBirthCertificate, relationship.getString("s_birth_certi").toString(), (int) R.drawable.img);
            UrlImageViewHelper.setUrlDrawable(this.imgSIN_SSN, relationship.getString("s_ssn").toString(), (int) R.drawable.img);
            UrlImageViewHelper.setUrlDrawable(this.imgImmigrationPapers, relationship.getString("s_citizen_paper").toString(), (int) R.drawable.img);
            UrlImageViewHelper.setUrlDrawable(this.imgWillPowerAttorney, relationship.getString("s_power_attorney").toString(), (int) R.drawable.img);
            UrlImageViewHelper.setUrlDrawable(this.imgTrust, relationship.getString("s_trust").toString(), (int) R.drawable.img);
            UrlImageViewHelper.setUrlDrawable(this.imgWarVeteranRecords, relationship.getString("s_war_record").toString(), (int) R.drawable.img);
            UrlImageViewHelper.setUrlDrawable(this.imgOrganDonationRecords, relationship.getString("s_donation_rec").toString(), (int) R.drawable.img);
            UrlImageViewHelper.setUrlDrawable(this.imgMarriageCertificate, relationship.getString("m_marriege_certi").toString(), (int) R.drawable.img);
            UrlImageViewHelper.setUrlDrawable(this.imgPreMarriageAgreements, relationship.getString("m_marriege_agree").toString(), (int) R.drawable.img);
            UrlImageViewHelper.setUrlDrawable(this.imgMaintenanceRecords, relationship.getString("d_maintenance_rec").toString(), (int) R.drawable.img);
            UrlImageViewHelper.setUrlDrawable(this.imgAlimony, relationship.getString("d_alimony").toString(), (int) R.drawable.img);
            UrlImageViewHelper.setUrlDrawable(this.imgChildCustodyRecords, relationship.getString("d_child_custody").toString(), (int) R.drawable.img);
            UrlImageViewHelper.setUrlDrawable(this.imgDivorceDecreeCertificate, relationship.getString("d_divorce_certi").toString(), (int) R.drawable.img);
            UrlImageViewHelper.setUrlDrawable(this.imgSeparationAgreement, relationship.getString("d_sepration_agree").toString(), (int) R.drawable.img);
            UrlImageViewHelper.setUrlDrawable(this.imgDeathCertificate, relationship.getString("w_death_certi").toString(), (int) R.drawable.img);
            UrlImageViewHelper.setUrlDrawable(this.imgWill, relationship.getString("w_will").toString(), (int) R.drawable.img);

        } catch (Exception e) {
            ADDParentToLayout(true);
            ArrayList<String> customFiled2 = new ArrayList<>();
            customFiled2.add(getResources().getString(R.string.hint_brother));
            customFiled2.add(getResources().getString(R.string.hint_sister));
            addSiblingLayout(this.siblingCount, customFiled2, 3);
            addPetSLayout(this.petlayoutCount);
            e.printStackTrace();
        }
    }

    private void showBackupConfirmation() {
        new AlertDialog.Builder(this).setTitle((CharSequence) "Backup Plan").setMessage((CharSequence) "Do you want to synchronize your data on Cloud?").setPositiveButton("17039379", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ProfileMenu.this.startActivity(new Intent(ProfileMenu.this, SaveProfileToGoogleDrive.class));
            }
        }).setNegativeButton("17039369", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ProfileMenu.this.finish();
            }
        }).show();
    }
}