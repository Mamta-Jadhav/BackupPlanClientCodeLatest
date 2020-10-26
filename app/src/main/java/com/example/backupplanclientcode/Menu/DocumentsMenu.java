package com.example.backupplanclientcode.Menu;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class DocumentsMenu extends Activity implements OnClickListener, ResponseListener_General, ResponseListerProfile {
    private static final int SELECT_PICTURE = 1;
    TextView actionBarTittle;
    Button btn_back;
    Button btn_save;
    CompressImage compress;
    ConnectionDetector connection;
    ImageView currentImageVew;
    JSONObject document_data;
    EditText edit_3rdparty;
    EditText edit_PROF_SAFE;
    EditText edit_ketLocationBox;
    EditText edit_registrationRecord;
    ImageView img_ALIMONY;
    ImageView img_CEMETERY;
    ImageView img_CITIZENSHIP_PAPERS;
    ImageView img_CUSTODY_ORDERS;
    ImageView img_DIVORCE_DECREE;
    ImageView img_FUNERAL_ARRANGEMENTS;
    ImageView img_IMMIGRATION_PAPERS;
    ImageView img_MAINTENANCE;
    ImageView img_MARRIAGE_CERTIFICATE;
    ImageView img_NEXUS;
    ImageView img_PASSPORTS;
    ImageView img_PRE_MARRIAGE_AGREEMENTS;
    ImageView img_PROOF_SAFE;
    ImageView img_RegistrationRecord;
    ImageView img_SEPARATION_AGREEMENT;
    ImageView img_SOCIAL_SECURITY;
    ImageView img_WAR_VETERAN_RECORDS;
    ImageView img_YOUR_KEYS;
    ImageView img_YOUR_WALLTE_COPY;
    ImageView img_brith_certificate;
    SettingPreference pref;
    ToggleButton yesNoFUNERAL_ARRANGEMENTS;
    ToggleButton yesNo_ALIMONY;
    ToggleButton yesNo_BirthCertificate;
    ToggleButton yesNo_CEMETERY;
    ToggleButton yesNo_CITIZENSHIP_PAPERS;
    ToggleButton yesNo_CUSTODY_ORDERS;
    ToggleButton yesNo_DIVORCE_DECREE;
    ToggleButton yesNo_IMMIGRATION_PAPERS;
    ToggleButton yesNo_MAINTENANCE;
    ToggleButton yesNo_MARRIAGE_CERTIFICATE;
    ToggleButton yesNo_NEXUS;
    ToggleButton yesNo_PASSPORTS;
    ToggleButton yesNo_PRE_MARRIAGE_AGREEMENTS;
    ToggleButton yesNo_SEPARATION_AGREEMENT;
    ToggleButton yesNo_SOCIAL_SECURITY;
    ToggleButton yesNo_WAR_VETERAN_RECORDS;
    ToggleButton yesNo_YOUR_KEYS;
    ToggleButton yesNo_YOUR_WALLTE_COPY;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_documents);
        this.pref = new SettingPreference(getApplicationContext());
        this.compress = new CompressImage(getApplicationContext());
        this.connection = new ConnectionDetector(getApplicationContext());
        new Bugsense().startBugsense(getApplicationContext());
        findViewids();
        findToggleButton();
        findImageView();
        check_documentAlredy();
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            setEnableControl();
        }
    }

    private void setEnableControl() {
        this.actionBarTittle.setText(getResources().getString(R.string.menu_documents));
        this.btn_save.setVisibility(View.GONE);
        this.img_brith_certificate.setEnabled(false);
        this.img_SOCIAL_SECURITY.setEnabled(false);
        this.img_MARRIAGE_CERTIFICATE.setEnabled(false);
        this.img_PASSPORTS.setEnabled(false);
        this.img_PRE_MARRIAGE_AGREEMENTS.setEnabled(false);
        this.img_NEXUS.setEnabled(false);
        this.img_CITIZENSHIP_PAPERS.setEnabled(false);
        this.img_ALIMONY.setEnabled(false);
        this.img_MAINTENANCE.setEnabled(false);
        this.img_CUSTODY_ORDERS.setEnabled(false);
        this.img_IMMIGRATION_PAPERS.setEnabled(false);
        this.img_DIVORCE_DECREE.setEnabled(false);
        this.img_SEPARATION_AGREEMENT.setEnabled(false);
        this.img_CEMETERY.setEnabled(false);
        this.img_FUNERAL_ARRANGEMENTS.setEnabled(false);
        this.img_WAR_VETERAN_RECORDS.setEnabled(false);
        this.img_YOUR_WALLTE_COPY.setEnabled(false);
        this.img_YOUR_KEYS.setEnabled(false);
        this.img_RegistrationRecord.setEnabled(false);
        this.img_PROOF_SAFE.setEnabled(false);
        this.yesNo_BirthCertificate.setEnabled(false);
        this.yesNo_SOCIAL_SECURITY.setEnabled(false);
        this.yesNo_MARRIAGE_CERTIFICATE.setEnabled(false);
        this.yesNo_PASSPORTS.setEnabled(false);
        this.yesNo_PRE_MARRIAGE_AGREEMENTS.setEnabled(false);
        this.yesNo_NEXUS.setEnabled(false);
        this.yesNo_CITIZENSHIP_PAPERS.setEnabled(false);
        this.yesNo_ALIMONY.setEnabled(false);
        this.yesNo_MAINTENANCE.setEnabled(false);
        this.yesNo_CUSTODY_ORDERS.setEnabled(false);
        this.yesNo_IMMIGRATION_PAPERS.setEnabled(false);
        this.yesNo_DIVORCE_DECREE.setEnabled(false);
        this.yesNo_SEPARATION_AGREEMENT.setEnabled(false);
        this.yesNo_CEMETERY.setEnabled(false);
        this.yesNoFUNERAL_ARRANGEMENTS.setEnabled(false);
        this.yesNo_WAR_VETERAN_RECORDS.setEnabled(false);
        this.yesNo_YOUR_WALLTE_COPY.setEnabled(false);
        this.yesNo_YOUR_KEYS.setEnabled(false);
        this.edit_ketLocationBox.setEnabled(false);
        this.edit_registrationRecord.setEnabled(false);
        this.edit_3rdparty.setEnabled(false);
        this.edit_PROF_SAFE.setEnabled(false);
    }

    private void check_documentAlredy() {
        Log.d("test", "Doc Id : " + pref.getStringValue(Constant.document_id, ""));
        if (!this.connection.isConnectingToInternet()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.connectionFailMessage), Toast.LENGTH_SHORT).show();
        } else if (!this.pref.getStringValue(Constant.document_id, "").equalsIgnoreCase("") && !this.pref.getStringValue(Constant.document_id, "").equalsIgnoreCase("0")) {
            this.btn_save.setText("Edit");
            if (!this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                this.actionBarTittle.setText("Edit " + getResources().getString(R.string.menu_documents));
            }
            try {
                JSONObject nameValuePair = new JSONObject();
                nameValuePair.put("user_id", this.pref.getStringValue(Constant.user_id, ""));
                nameValuePair.put("document_id", this.pref.getStringValue(Constant.document_id, ""));
                nameValuePair.put("token", this.pref.getStringValue(Constant.jwttoken, ""));
                new GeneralTask(this, ServiceUrl.get_document_detail, nameValuePair, 1, "post").execute(new Void[0]);
            } catch (Exception e) {

            }
        } else {
            this.actionBarTittle.setText(getResources().getString(R.string.menu_documents));
        }
    }

    private void findToggleButton() {
        this.yesNo_BirthCertificate = (ToggleButton) findViewById(R.id.yesNo_BirthCertificate);
        this.yesNo_BirthCertificate.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DocumentsMenu.this.img_brith_certificate.setVisibility(View.VISIBLE);
                } else {
                    DocumentsMenu.this.img_brith_certificate.setVisibility(View.GONE);
                }
            }
        });
        this.yesNo_SOCIAL_SECURITY = (ToggleButton) findViewById(R.id.yesNo_SOCIAL_SECURITY);
        this.yesNo_SOCIAL_SECURITY.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DocumentsMenu.this.img_SOCIAL_SECURITY.setVisibility(View.VISIBLE);
                } else {
                    DocumentsMenu.this.img_SOCIAL_SECURITY.setVisibility(View.GONE);
                }
            }
        });
        this.yesNo_MARRIAGE_CERTIFICATE = (ToggleButton) findViewById(R.id.yesNo_MARRIAGE_CERTIFICATE);
        this.yesNo_MARRIAGE_CERTIFICATE.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DocumentsMenu.this.img_MARRIAGE_CERTIFICATE.setVisibility(View.VISIBLE);
                } else {
                    DocumentsMenu.this.img_MARRIAGE_CERTIFICATE.setVisibility(View.GONE);
                }
            }
        });
        this.yesNo_PASSPORTS = (ToggleButton) findViewById(R.id.yesNo_PASSPORTS);
        this.yesNo_PASSPORTS.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DocumentsMenu.this.img_PASSPORTS.setVisibility(View.VISIBLE);
                } else {
                    DocumentsMenu.this.img_PASSPORTS.setVisibility(View.GONE);
                }
            }
        });
        this.yesNo_PRE_MARRIAGE_AGREEMENTS = (ToggleButton) findViewById(R.id.yesNo_PRE_MARRIAGE_AGREEMENTS);
        this.yesNo_PRE_MARRIAGE_AGREEMENTS.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DocumentsMenu.this.img_PRE_MARRIAGE_AGREEMENTS.setVisibility(View.VISIBLE);
                } else {
                    DocumentsMenu.this.img_PRE_MARRIAGE_AGREEMENTS.setVisibility(View.GONE);
                }
            }
        });
        this.yesNo_NEXUS = (ToggleButton) findViewById(R.id.yesNo_NEXUS);
        this.yesNo_NEXUS.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DocumentsMenu.this.img_NEXUS.setVisibility(View.VISIBLE);
                } else {
                    DocumentsMenu.this.img_NEXUS.setVisibility(View.GONE);
                }
            }
        });
        this.yesNo_CITIZENSHIP_PAPERS = (ToggleButton) findViewById(R.id.yesNo_CITIZENSHIP_PAPERS);
        this.yesNo_CITIZENSHIP_PAPERS.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DocumentsMenu.this.img_CITIZENSHIP_PAPERS.setVisibility(View.VISIBLE);
                } else {
                    DocumentsMenu.this.img_CITIZENSHIP_PAPERS.setVisibility(View.GONE);
                }
            }
        });
        this.yesNo_ALIMONY = (ToggleButton) findViewById(R.id.yesNo_ALIMONY);
        this.yesNo_ALIMONY.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DocumentsMenu.this.img_ALIMONY.setVisibility(View.VISIBLE);
                } else {
                    DocumentsMenu.this.img_ALIMONY.setVisibility(View.GONE);
                }
            }
        });
        this.yesNo_MAINTENANCE = (ToggleButton) findViewById(R.id.yesNo_MAINTENANCE);
        this.yesNo_MAINTENANCE.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DocumentsMenu.this.img_MAINTENANCE.setVisibility(View.VISIBLE);
                } else {
                    DocumentsMenu.this.img_MAINTENANCE.setVisibility(View.GONE);
                }
            }
        });
        this.yesNo_CUSTODY_ORDERS = (ToggleButton) findViewById(R.id.yesNo_CUSTODY_ORDERS);
        this.yesNo_CUSTODY_ORDERS.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DocumentsMenu.this.img_CUSTODY_ORDERS.setVisibility(View.VISIBLE);
                } else {
                    DocumentsMenu.this.img_CUSTODY_ORDERS.setVisibility(View.GONE);
                }
            }
        });
        this.yesNo_IMMIGRATION_PAPERS = (ToggleButton) findViewById(R.id.yesNo_IMMIGRATION_PAPERS);
        this.yesNo_IMMIGRATION_PAPERS.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DocumentsMenu.this.img_IMMIGRATION_PAPERS.setVisibility(View.VISIBLE);
                } else {
                    DocumentsMenu.this.img_IMMIGRATION_PAPERS.setVisibility(View.GONE);
                }
            }
        });
        this.yesNo_DIVORCE_DECREE = (ToggleButton) findViewById(R.id.yesNo_DIVORCE_DECREE);
        this.yesNo_DIVORCE_DECREE.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DocumentsMenu.this.img_DIVORCE_DECREE.setVisibility(View.VISIBLE);
                } else {
                    DocumentsMenu.this.img_DIVORCE_DECREE.setVisibility(View.GONE);
                }
            }
        });
        this.yesNo_SEPARATION_AGREEMENT = (ToggleButton) findViewById(R.id.yesNo_SEPARATION_AGREEMENT);
        this.yesNo_SEPARATION_AGREEMENT.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DocumentsMenu.this.img_SEPARATION_AGREEMENT.setVisibility(View.VISIBLE);
                } else {
                    DocumentsMenu.this.img_SEPARATION_AGREEMENT.setVisibility(View.GONE);
                }
            }
        });
        this.yesNo_CEMETERY = (ToggleButton) findViewById(R.id.yesNo_CEMETERY);
        this.yesNo_CEMETERY.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DocumentsMenu.this.img_CEMETERY.setVisibility(View.VISIBLE);
                } else {
                    DocumentsMenu.this.img_CEMETERY.setVisibility(View.GONE);
                }
            }
        });
        this.yesNoFUNERAL_ARRANGEMENTS = (ToggleButton) findViewById(R.id.yesNoFUNERAL_ARRANGEMENTS);
        this.yesNoFUNERAL_ARRANGEMENTS.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DocumentsMenu.this.img_FUNERAL_ARRANGEMENTS.setVisibility(View.VISIBLE);
                } else {
                    DocumentsMenu.this.img_FUNERAL_ARRANGEMENTS.setVisibility(View.GONE);
                }
            }
        });
        this.yesNo_WAR_VETERAN_RECORDS = (ToggleButton) findViewById(R.id.yesNo_WAR_VETERAN_RECORDS);
        this.yesNo_WAR_VETERAN_RECORDS.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DocumentsMenu.this.img_WAR_VETERAN_RECORDS.setVisibility(View.VISIBLE);
                } else {
                    DocumentsMenu.this.img_WAR_VETERAN_RECORDS.setVisibility(View.GONE);
                }
            }
        });
        this.yesNo_YOUR_WALLTE_COPY = (ToggleButton) findViewById(R.id.yesNo_YOUR_WALLTE_COPY);
        this.yesNo_YOUR_WALLTE_COPY.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DocumentsMenu.this.img_YOUR_WALLTE_COPY.setVisibility(View.VISIBLE);
                } else {
                    DocumentsMenu.this.img_YOUR_WALLTE_COPY.setVisibility(View.GONE);
                }
            }
        });
        this.yesNo_YOUR_KEYS = (ToggleButton) findViewById(R.id.yesNo_YOUR_KEYS);
        this.yesNo_YOUR_KEYS.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DocumentsMenu.this.img_YOUR_KEYS.setVisibility(View.VISIBLE);
                } else {
                    DocumentsMenu.this.img_YOUR_KEYS.setVisibility(View.GONE);
                }
            }
        });
    }

    private void findImageView() {
        this.img_brith_certificate = (ImageView) findViewById(R.id.img_brith_certificate);
        this.img_brith_certificate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DocumentsMenu.this.currentImageVew = DocumentsMenu.this.img_brith_certificate;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                DocumentsMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        this.img_SOCIAL_SECURITY = (ImageView) findViewById(R.id.img_SOCIAL_SECURITY);
        this.img_SOCIAL_SECURITY.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DocumentsMenu.this.currentImageVew = DocumentsMenu.this.img_SOCIAL_SECURITY;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                DocumentsMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        this.img_MARRIAGE_CERTIFICATE = (ImageView) findViewById(R.id.img_MARRIAGE_CERTIFICATE);
        this.img_MARRIAGE_CERTIFICATE.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DocumentsMenu.this.currentImageVew = DocumentsMenu.this.img_MARRIAGE_CERTIFICATE;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                DocumentsMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        this.img_PASSPORTS = (ImageView) findViewById(R.id.img_PASSPORTS);
        this.img_PASSPORTS.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DocumentsMenu.this.currentImageVew = DocumentsMenu.this.img_PASSPORTS;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                DocumentsMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        this.img_PRE_MARRIAGE_AGREEMENTS = (ImageView) findViewById(R.id.img_PRE_MARRIAGE_AGREEMENTS);
        this.img_PRE_MARRIAGE_AGREEMENTS.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DocumentsMenu.this.currentImageVew = DocumentsMenu.this.img_PRE_MARRIAGE_AGREEMENTS;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                DocumentsMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        this.img_NEXUS = (ImageView) findViewById(R.id.img_NEXUS);
        this.img_NEXUS.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DocumentsMenu.this.currentImageVew = DocumentsMenu.this.img_NEXUS;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                DocumentsMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        this.img_CITIZENSHIP_PAPERS = (ImageView) findViewById(R.id.img_CITIZENSHIP_PAPERS);
        this.img_CITIZENSHIP_PAPERS.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DocumentsMenu.this.currentImageVew = DocumentsMenu.this.img_CITIZENSHIP_PAPERS;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                DocumentsMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        this.img_ALIMONY = (ImageView) findViewById(R.id.img_ALIMONY);
        this.img_ALIMONY.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DocumentsMenu.this.currentImageVew = DocumentsMenu.this.img_ALIMONY;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                DocumentsMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        this.img_MAINTENANCE = (ImageView) findViewById(R.id.img_MAINTENANCE);
        this.img_MAINTENANCE.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DocumentsMenu.this.currentImageVew = DocumentsMenu.this.img_MAINTENANCE;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                DocumentsMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        this.img_CUSTODY_ORDERS = (ImageView) findViewById(R.id.img_CUSTODY_ORDERS);
        this.img_CUSTODY_ORDERS.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DocumentsMenu.this.currentImageVew = DocumentsMenu.this.img_CUSTODY_ORDERS;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                DocumentsMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        this.img_IMMIGRATION_PAPERS = (ImageView) findViewById(R.id.img_IMMIGRATION_PAPERS);
        this.img_IMMIGRATION_PAPERS.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DocumentsMenu.this.currentImageVew = DocumentsMenu.this.img_IMMIGRATION_PAPERS;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                DocumentsMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        this.img_DIVORCE_DECREE = (ImageView) findViewById(R.id.img_DIVORCE_DECREE);
        this.img_DIVORCE_DECREE.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DocumentsMenu.this.currentImageVew = DocumentsMenu.this.img_DIVORCE_DECREE;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                DocumentsMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        this.img_SEPARATION_AGREEMENT = (ImageView) findViewById(R.id.img_SEPARATION_AGREEMENT);
        this.img_SEPARATION_AGREEMENT.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DocumentsMenu.this.currentImageVew = DocumentsMenu.this.img_SEPARATION_AGREEMENT;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                DocumentsMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        this.img_CEMETERY = (ImageView) findViewById(R.id.img_CEMETERY);
        this.img_CEMETERY.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DocumentsMenu.this.currentImageVew = DocumentsMenu.this.img_CEMETERY;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                DocumentsMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        this.img_FUNERAL_ARRANGEMENTS = (ImageView) findViewById(R.id.img_FUNERAL_ARRANGEMENTS);
        this.img_FUNERAL_ARRANGEMENTS.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DocumentsMenu.this.currentImageVew = DocumentsMenu.this.img_FUNERAL_ARRANGEMENTS;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                DocumentsMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        this.img_WAR_VETERAN_RECORDS = (ImageView) findViewById(R.id.img_WAR_VETERAN_RECORDS);
        this.img_WAR_VETERAN_RECORDS.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DocumentsMenu.this.currentImageVew = DocumentsMenu.this.img_WAR_VETERAN_RECORDS;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                DocumentsMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        this.img_YOUR_WALLTE_COPY = (ImageView) findViewById(R.id.img_YOUR_WALLTE_COPY);
        this.img_YOUR_WALLTE_COPY.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DocumentsMenu.this.currentImageVew = DocumentsMenu.this.img_YOUR_WALLTE_COPY;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                DocumentsMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        this.img_YOUR_KEYS = (ImageView) findViewById(R.id.img_YOUR_KEYS);
        this.img_YOUR_KEYS.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DocumentsMenu.this.currentImageVew = DocumentsMenu.this.img_YOUR_KEYS;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                DocumentsMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        this.img_RegistrationRecord = (ImageView) findViewById(R.id.img_RegistrationRecord);
        this.img_RegistrationRecord.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DocumentsMenu.this.currentImageVew = DocumentsMenu.this.img_RegistrationRecord;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                DocumentsMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        this.img_PROOF_SAFE = (ImageView) findViewById(R.id.img_PROOF_SAFE);
        this.img_PROOF_SAFE.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DocumentsMenu.this.currentImageVew = DocumentsMenu.this.img_PROOF_SAFE;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                DocumentsMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
    }

    private void findViewids() {
        this.btn_save = (Button) findViewById(R.id.btn_save);
        this.btn_back = (Button) findViewById(R.id.btn_back);
        this.btn_back.setOnClickListener(this);
        this.btn_save.setOnClickListener(this);
        this.edit_ketLocationBox = (EditText) findViewById(R.id.edit_ketLocationBox);
        this.edit_registrationRecord = (EditText) findViewById(R.id.edit_registrationRecord);
        this.edit_3rdparty = (EditText) findViewById(R.id.edit_3rdparty);
        this.edit_PROF_SAFE = (EditText) findViewById(R.id.edit_PROF_SAFE);
        this.actionBarTittle = (TextView) findViewById(R.id.actionBarTittle);
        this.document_data = new JSONObject();
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
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save /*2131558589*/:
                PrepareJson();
                return;
            case R.id.btn_back /*2131558590*/:
                finish();
                return;
            default:
                return;
        }
    }

    private void PrepareJson() {
        try {
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            this.document_data.put("user_id", this.pref.getStringValue(Constant.user_id, ""));
            if (this.yesNo_BirthCertificate.isChecked()) {
                this.document_data.put("is_birth_certi", "1");
                this.document_data.put("birth_certi", "birth_certi");
                if (!this.img_brith_certificate.getContentDescription().toString().equalsIgnoreCase("")) {
                    entity.addPart("birth_certi", new FileBody(new File(this.img_brith_certificate.getContentDescription().toString()), "image/jpeg"));
                    Log.i("img_brith_certificate", this.img_brith_certificate.getContentDescription().toString());
                }
            } else {
                this.document_data.put("is_birth_certi", "0");
                this.document_data.put("birth_certi", "");
            }
            if (this.yesNo_SOCIAL_SECURITY.isChecked()) {
                this.document_data.put("is_sin", "1");
                this.document_data.put("sin", "sin");
                if (!this.img_SOCIAL_SECURITY.getContentDescription().toString().equalsIgnoreCase("")) {
                    entity.addPart("sin", new FileBody(new File(this.img_SOCIAL_SECURITY.getContentDescription().toString()), "image/jpeg"));
                    Log.i("img_SOCIAL_SECURITY", this.img_SOCIAL_SECURITY.getContentDescription().toString());
                }
            } else {
                this.document_data.put("is_sin", "0");
                this.document_data.put("sin", "");
            }
            if (this.yesNo_MARRIAGE_CERTIFICATE.isChecked()) {
                this.document_data.put("is_merr_certi", "1");
                this.document_data.put("merr_certi", "merr_certi");
                if (!this.img_MARRIAGE_CERTIFICATE.getContentDescription().toString().equalsIgnoreCase("")) {
                    entity.addPart("merr_certi", new FileBody(new File(this.img_MARRIAGE_CERTIFICATE.getContentDescription().toString()), "image/jpeg"));
                    Log.i("MARRIAGE_CRTFCATE", this.img_MARRIAGE_CERTIFICATE.getContentDescription().toString());
                }
            } else {
                this.document_data.put("is_merr_certi", "0");
                this.document_data.put("merr_certi", "");
            }
            if (this.yesNo_PASSPORTS.isChecked()) {
                this.document_data.put("is_passport", "1");
                this.document_data.put("passport", "passport");
                if (!this.img_PASSPORTS.getContentDescription().toString().equalsIgnoreCase("")) {
                    entity.addPart("passport", new FileBody(new File(this.img_PASSPORTS.getContentDescription().toString()), "image/jpeg"));
                    Log.i("img_PASSPORTS", this.img_PASSPORTS.getContentDescription().toString());
                }
            } else {
                this.document_data.put("is_passport", "0");
                this.document_data.put("passport", "");
            }
            if (this.yesNo_PRE_MARRIAGE_AGREEMENTS.isChecked()) {
                this.document_data.put("is_pre_merr_agree", "1");
                this.document_data.put("pre_merr_agree", "pre_merr_agree");
                if (!this.img_PRE_MARRIAGE_AGREEMENTS.getContentDescription().toString().equalsIgnoreCase("")) {
                    entity.addPart("pre_merr_agree", new FileBody(new File(this.img_PRE_MARRIAGE_AGREEMENTS.getContentDescription().toString()), "image/jpeg"));
                    Log.i("PRE_AGREEMENTS", this.img_PRE_MARRIAGE_AGREEMENTS.getContentDescription().toString());
                }
            } else {
                this.document_data.put("is_pre_merr_agree", "0");
                this.document_data.put("pre_merr_agree", "");
            }
            if (this.yesNo_NEXUS.isChecked()) {
                this.document_data.put("is_nexus", "1");
                this.document_data.put("nexus", "nexus");
                if (!this.img_NEXUS.getContentDescription().toString().equalsIgnoreCase("")) {
                    entity.addPart("nexus", new FileBody(new File(this.img_NEXUS.getContentDescription().toString()), "image/jpeg"));
                    Log.i("img_NEXUS", this.img_NEXUS.getContentDescription().toString());
                }
            } else {
                this.document_data.put("is_nexus", "0");
                this.document_data.put("nexus", "");
            }
            if (this.yesNo_CITIZENSHIP_PAPERS.isChecked()) {
                this.document_data.put("is_citizen_paper", "1");
                this.document_data.put("citizen_paper", "citizen_paper");
                if (!this.img_CITIZENSHIP_PAPERS.getContentDescription().toString().equalsIgnoreCase("")) {
                    entity.addPart("citizen_paper", new FileBody(new File(this.img_CITIZENSHIP_PAPERS.getContentDescription().toString()), "image/jpeg"));
                    Log.i("img_CITIZENSHIP_PAPERS", this.img_CITIZENSHIP_PAPERS.getContentDescription().toString());
                }
            } else {
                this.document_data.put("is_citizen_paper", "0");
                this.document_data.put("citizen_paper", "");
            }
            if (this.yesNo_ALIMONY.isChecked()) {
                this.document_data.put("is_alimony", "1");
                this.document_data.put("alimony", "alimony");
                if (!this.img_ALIMONY.getContentDescription().toString().equalsIgnoreCase("")) {
                    entity.addPart("alimony", new FileBody(new File(this.img_ALIMONY.getContentDescription().toString()), "image/jpeg"));
                    Log.i("img_ALIMONY", this.img_ALIMONY.getContentDescription().toString());
                }
            } else {
                this.document_data.put("is_alimony", "0");
                this.document_data.put("alimony", "");
            }
            if (this.yesNo_MAINTENANCE.isChecked()) {
                this.document_data.put("is_maintance", "1");
                this.document_data.put("maintance", "maintance");
                if (!this.img_MAINTENANCE.getContentDescription().toString().equalsIgnoreCase("")) {
                    entity.addPart("maintance", new FileBody(new File(this.img_MAINTENANCE.getContentDescription().toString()), "image/jpeg"));
                    Log.i("img_MAINTENANCE", this.img_MAINTENANCE.getContentDescription().toString());
                }
            } else {
                this.document_data.put("is_maintance", "0");
                this.document_data.put("maintance", "");
            }
            if (this.yesNo_CUSTODY_ORDERS.isChecked()) {
                this.document_data.put("is_custody_order", "1");
                this.document_data.put("custody_order", "custody_order");
                if (!this.img_CUSTODY_ORDERS.getContentDescription().toString().equalsIgnoreCase("")) {
                    entity.addPart("custody_order", new FileBody(new File(this.img_CUSTODY_ORDERS.getContentDescription().toString()), "image/jpeg"));
                    Log.i("img_CUSTODY_ORDERS", this.img_CUSTODY_ORDERS.getContentDescription().toString());
                }
            } else {
                this.document_data.put("is_custody_order", "0");
                this.document_data.put("custody_order", "");
            }
            if (this.yesNo_IMMIGRATION_PAPERS.isChecked()) {
                this.document_data.put("is_immig_paper", "1");
                this.document_data.put("immig_paper", "immig_paper");
                if (!this.img_IMMIGRATION_PAPERS.getContentDescription().toString().equalsIgnoreCase("")) {
                    entity.addPart("immig_paper", new FileBody(new File(this.img_IMMIGRATION_PAPERS.getContentDescription().toString()), "image/jpeg"));
                    Log.i("img_IMMIGRATION_PAPERS", this.img_IMMIGRATION_PAPERS.getContentDescription().toString());
                }
            } else {
                this.document_data.put("is_immig_paper", "0");
                this.document_data.put("immig_paper", "");
            }
            if (this.yesNo_DIVORCE_DECREE.isChecked()) {
                this.document_data.put("is_divorce_agree", "1");
                this.document_data.put("divorce_agree", "divorce_agree");
                if (!this.img_DIVORCE_DECREE.getContentDescription().toString().equalsIgnoreCase("")) {
                    entity.addPart("divorce_agree", new FileBody(new File(this.img_DIVORCE_DECREE.getContentDescription().toString()), "image/jpeg"));
                    Log.i("img_DIVORCE_DECREE", this.img_DIVORCE_DECREE.getContentDescription().toString());
                }
            } else {
                this.document_data.put("is_divorce_agree", "0");
                this.document_data.put("divorce_agree", "");
            }
            if (this.yesNo_SEPARATION_AGREEMENT.isChecked()) {
                this.document_data.put("is_sepration_agree", "1");
                this.document_data.put("sepration_agree", "sepration_agree");
                if (!this.img_SEPARATION_AGREEMENT.getContentDescription().toString().equalsIgnoreCase("")) {
                    entity.addPart("sepration_agree", new FileBody(new File(this.img_SEPARATION_AGREEMENT.getContentDescription().toString()), "image/jpeg"));
                    Log.i("SPRTION_AGREMENT", this.img_SEPARATION_AGREEMENT.getContentDescription().toString());
                }
            } else {
                this.document_data.put("is_sepration_agree", "0");
                this.document_data.put("sepration_agree", "");
            }
            if (this.yesNo_CEMETERY.isChecked()) {
                this.document_data.put("is_cemetery", "1");
                this.document_data.put("cemetery", "cemetery");
                if (!this.img_CEMETERY.getContentDescription().toString().equalsIgnoreCase("")) {
                    entity.addPart("cemetery", new FileBody(new File(this.img_CEMETERY.getContentDescription().toString()), "image/jpeg"));
                    Log.i("img_CEMETERY", this.img_CEMETERY.getContentDescription().toString());
                }
            } else {
                this.document_data.put("is_cemetery", "0");
                this.document_data.put("cemetery", "");
            }
            if (this.yesNoFUNERAL_ARRANGEMENTS.isChecked()) {
                this.document_data.put("is_pre_paid_fun_arra", "1");
                this.document_data.put("pre_paid_fun_arra", "pre_paid_fun_arra");
                if (!this.img_FUNERAL_ARRANGEMENTS.getContentDescription().toString().equalsIgnoreCase("")) {
                    entity.addPart("pre_paid_fun_arra", new FileBody(new File(this.img_FUNERAL_ARRANGEMENTS.getContentDescription().toString()), "image/jpeg"));
                    Log.i("FUNERAL_ARNGS", this.img_FUNERAL_ARRANGEMENTS.getContentDescription().toString());
                }
            } else {
                this.document_data.put("is_pre_paid_fun_arra", "0");
                this.document_data.put("pre_paid_fun_arra", "");
            }
            if (this.yesNo_WAR_VETERAN_RECORDS.isChecked()) {
                this.document_data.put("is_war_vet_rec", "1");
                this.document_data.put("war_vet_rec", "war_vet_rec");
                if (!this.img_WAR_VETERAN_RECORDS.getContentDescription().toString().equalsIgnoreCase("")) {
                    entity.addPart("war_vet_rec", new FileBody(new File(this.img_WAR_VETERAN_RECORDS.getContentDescription().toString()), "image/jpeg"));
                    Log.i("img_WAR_VETERAN_RECORDS", this.img_WAR_VETERAN_RECORDS.getContentDescription().toString());
                }
            } else {
                this.document_data.put("is_war_vet_rec", "0");
                this.document_data.put("war_vet_rec", "");
            }
            if (this.yesNo_YOUR_WALLTE_COPY.isChecked()) {
                this.document_data.put("is_wollet_card", "1");
                this.document_data.put("wollet_card", "wollet_card");
                if (!this.img_YOUR_WALLTE_COPY.getContentDescription().toString().equalsIgnoreCase("")) {
                    entity.addPart("wollet_card", new FileBody(new File(this.img_YOUR_WALLTE_COPY.getContentDescription().toString()), "image/jpeg"));
                    Log.i("img_YOUR_WALLTE_COPY", this.img_YOUR_WALLTE_COPY.getContentDescription().toString());
                }
            } else {
                this.document_data.put("is_wollet_card", "0");
                this.document_data.put("wollet_card", "");
            }
            if (this.yesNo_YOUR_KEYS.isChecked()) {
                this.document_data.put("is_take_pic", "1");
                this.document_data.put("take_pic", "take_pic");
                if (!this.img_YOUR_KEYS.getContentDescription().toString().equalsIgnoreCase("")) {
                    entity.addPart("take_pic", new FileBody(new File(this.img_YOUR_KEYS.getContentDescription().toString()), "image/jpeg"));
                    Log.i("img_YOUR_KEYS", this.img_YOUR_KEYS.getContentDescription().toString());
                }
            } else {
                this.document_data.put("is_take_pic", "0");
                this.document_data.put("take_pic", "");
            }
            this.document_data.put("sd_key_location", this.edit_ketLocationBox.getText().toString().trim());
            this.document_data.put("r_location", this.edit_registrationRecord.getText().toString().trim());
            this.document_data.put("r_party_access", this.edit_3rdparty.getText().toString().trim());
            if (!this.img_RegistrationRecord.getContentDescription().toString().equalsIgnoreCase("")) {
                entity.addPart("r_photo", new FileBody(new File(this.img_RegistrationRecord.getContentDescription().toString()), "image/jpeg"));
                Log.i("img_RegistrationRecord", this.img_RegistrationRecord.getContentDescription().toString());
                this.document_data.put("r_photo", "r_photo");
            } else {
                this.document_data.put("r_photo", "");
            }
            this.document_data.put("fp_location", this.edit_PROF_SAFE.getText().toString().trim());
            if (!this.img_PROOF_SAFE.getContentDescription().toString().equalsIgnoreCase("")) {
                this.document_data.put("fp_photo", "fp_photo");
                entity.addPart("fp_photo", new FileBody(new File(this.img_PROOF_SAFE.getContentDescription().toString()), "image/jpeg"));
                Log.i("img_PROOF_SAFE", this.img_PROOF_SAFE.getContentDescription().toString());
            } else {
                this.document_data.put("fp_photo", "");
            }
            JSONObject sendJsonOBj = new JSONObject();
            if (!this.connection.isConnectingToInternet()) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.connectionFailMessage), Toast.LENGTH_SHORT).show();
            } else if (this.btn_save.getText().toString().trim().equalsIgnoreCase("edit")) {
                this.document_data.put("document_id", this.pref.getStringValue(Constant.document_id, ""));
                sendJsonOBj.put("document_data", this.document_data);
                entity.addPart("json_data", new StringBody(sendJsonOBj.toString()));
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("json_data", sendJsonOBj.toString()));
                new SaveProfileAsytask(this, ServiceUrl.edit_document, nameValuePairs).execute(new Void[0]);
            } else {
                sendJsonOBj.put("document_data", this.document_data);
                entity.addPart("json_data", new StringBody(sendJsonOBj.toString()));
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("json_data", sendJsonOBj.toString()));
                new SaveProfileAsytask(this, ServiceUrl.save_document, nameValuePairs).execute(new Void[0]);
            }
            Log.e("SEND OBJ", sendJsonOBj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void on_ProfileSuccess(JSONObject response) {
        try {
            if (response.has("document_id")) {
                this.pref.setStringValue(Constant.document_id, response.getString("document_id"));
            }
            if (response.has("message")) {
                Toast.makeText(getApplicationContext(), response.getString("message").toString(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();
            }
            if (response.has("success") && response.getString("success").toString().trim().equalsIgnoreCase("1")) {
                this.pref.setStringValue(Constant.document_id, response.getString("document_id"));
                this.pref.setStringValue(Constant.document_flag,"1");
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void on_GeneralSuccess(JSONObject response, int responseCode) {
        if (responseCode == 1) {
            try {
                if (response.has("document_data")) {
                    JSONObject json = response.getJSONObject("document_data");
                    show_toggle_button(json);
                    show_images(json);
                    show_editTextValue(json);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void show_editTextValue(JSONObject json) {
        try {
            if (json.has("sd_key_location")) {
                this.edit_ketLocationBox.setText(json.getString("sd_key_location").toString().trim());
            }
            if (json.has("r_location")) {
                this.edit_registrationRecord.setText(json.getString("r_location").toString().trim());
            }
            if (json.has("r_party_access")) {
                this.edit_3rdparty.setText(json.getString("r_party_access").toString().trim());
            }
            if (json.has("fp_location")) {
                this.edit_PROF_SAFE.setText(json.getString("fp_location").toString().trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void show_images(JSONObject json) {
        try {
            if (json.has("birth_certi")) {
                UrlImageViewHelper.setUrlDrawable(this.img_brith_certificate, json.getString("birth_certi").toString().trim(), (int) R.drawable.img);
            }
            if (json.has("sin")) {
                UrlImageViewHelper.setUrlDrawable(this.img_SOCIAL_SECURITY, json.getString("sin").toString().trim(), (int) R.drawable.img);
            }
            if (json.has("merr_certi")) {
                UrlImageViewHelper.setUrlDrawable(this.img_MARRIAGE_CERTIFICATE, json.getString("merr_certi").toString().trim(), (int) R.drawable.img);
            }
            if (json.has("passport")) {
                UrlImageViewHelper.setUrlDrawable(this.img_PASSPORTS, json.getString("passport").toString().trim(), (int) R.drawable.img);
            }
            if (json.has("pre_merr_agree")) {
                UrlImageViewHelper.setUrlDrawable(this.img_PRE_MARRIAGE_AGREEMENTS, json.getString("pre_merr_agree").toString().trim(), (int) R.drawable.img);
            }
            if (json.has("nexus")) {
                UrlImageViewHelper.setUrlDrawable(this.img_NEXUS, json.getString("nexus").toString().trim(), (int) R.drawable.img);
            }
            if (json.has("citizen_paper")) {
                UrlImageViewHelper.setUrlDrawable(this.img_CITIZENSHIP_PAPERS, json.getString("citizen_paper").toString().trim(), (int) R.drawable.img);
            }
            if (json.has("alimony")) {
                UrlImageViewHelper.setUrlDrawable(this.img_ALIMONY, json.getString("alimony").toString().trim(), (int) R.drawable.img);
            }
            if (json.has("maintance")) {
                UrlImageViewHelper.setUrlDrawable(this.img_MAINTENANCE, json.getString("maintance").toString().trim(), (int) R.drawable.img);
            }
            if (json.has("custody_order")) {
                UrlImageViewHelper.setUrlDrawable(this.img_CUSTODY_ORDERS, json.getString("custody_order").toString().trim(), (int) R.drawable.img);
            }
            if (json.has("immig_paper")) {
                UrlImageViewHelper.setUrlDrawable(this.img_IMMIGRATION_PAPERS, json.getString("immig_paper").toString().trim(), (int) R.drawable.img);
            }
            if (json.has("divorce_agree")) {
                UrlImageViewHelper.setUrlDrawable(this.img_DIVORCE_DECREE, json.getString("divorce_agree").toString().trim(), (int) R.drawable.img);
            }
            if (json.has("sepration_agree")) {
                UrlImageViewHelper.setUrlDrawable(this.img_SEPARATION_AGREEMENT, json.getString("sepration_agree").toString().trim(), (int) R.drawable.img);
            }
            if (json.has("cemetery")) {
                UrlImageViewHelper.setUrlDrawable(this.img_CEMETERY, json.getString("cemetery").toString().trim(), (int) R.drawable.img);
            }
            if (json.has("pre_paid_fun_arra")) {
                UrlImageViewHelper.setUrlDrawable(this.img_FUNERAL_ARRANGEMENTS, json.getString("pre_paid_fun_arra").toString().trim(), (int) R.drawable.img);
            }
            if (json.has("war_vet_rec")) {
                UrlImageViewHelper.setUrlDrawable(this.img_WAR_VETERAN_RECORDS, json.getString("war_vet_rec").toString().trim(), (int) R.drawable.img);
            }
            if (json.has("wollet_card")) {
                UrlImageViewHelper.setUrlDrawable(this.img_YOUR_WALLTE_COPY, json.getString("wollet_card").toString().trim(), (int) R.drawable.img);
            }
            if (json.has("take_pic")) {
                UrlImageViewHelper.setUrlDrawable(this.img_YOUR_KEYS, json.getString("take_pic").toString().trim(), (int) R.drawable.img);
            }
            if (json.has("r_photo")) {
                UrlImageViewHelper.setUrlDrawable(this.img_RegistrationRecord, json.getString("r_photo").toString().trim(), (int) R.drawable.img);
            }
            if (json.has("fp_photo")) {
                UrlImageViewHelper.setUrlDrawable(this.img_PROOF_SAFE, json.getString("fp_photo").toString().trim(), (int) R.drawable.img);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void show_toggle_button(JSONObject json) {
        try {
            if (json.has("is_birth_certi")) {
                if (json.getString("is_birth_certi").toString().trim().equalsIgnoreCase("1")) {
                    this.yesNo_BirthCertificate.setChecked(true);
                } else {
                    this.yesNo_BirthCertificate.setChecked(false);
                }
            }
            if (json.has("is_sin")) {
                if (json.getString("is_sin").toString().trim().equalsIgnoreCase("1")) {
                    this.yesNo_SOCIAL_SECURITY.setChecked(true);
                } else {
                    this.yesNo_SOCIAL_SECURITY.setChecked(false);
                }
            }
            if (json.has("is_merr_certi")) {
                if (json.getString("is_merr_certi").toString().trim().equalsIgnoreCase("1")) {
                    this.yesNo_MARRIAGE_CERTIFICATE.setChecked(true);
                } else {
                    this.yesNo_MARRIAGE_CERTIFICATE.setChecked(false);
                }
            }
            if (json.has("is_passport")) {
                if (json.getString("is_passport").toString().trim().equalsIgnoreCase("1")) {
                    this.yesNo_PASSPORTS.setChecked(true);
                } else {
                    this.yesNo_PASSPORTS.setChecked(false);
                }
            }
            if (json.has("is_pre_merr_agree")) {
                if (json.getString("is_pre_merr_agree").toString().trim().equalsIgnoreCase("1")) {
                    this.yesNo_PRE_MARRIAGE_AGREEMENTS.setChecked(true);
                } else {
                    this.yesNo_PRE_MARRIAGE_AGREEMENTS.setChecked(false);
                }
            }
            if (json.has("is_nexus")) {
                if (json.getString("is_nexus").toString().trim().equalsIgnoreCase("1")) {
                    this.yesNo_NEXUS.setChecked(true);
                } else {
                    this.yesNo_NEXUS.setChecked(false);
                }
            }
            if (json.has("is_citizen_paper")) {
                if (json.getString("is_citizen_paper").toString().trim().equalsIgnoreCase("1")) {
                    this.yesNo_CITIZENSHIP_PAPERS.setChecked(true);
                } else {
                    this.yesNo_CITIZENSHIP_PAPERS.setChecked(false);
                }
            }
            if (json.has("is_alimony")) {
                if (json.getString("is_alimony").toString().trim().equalsIgnoreCase("1")) {
                    this.yesNo_ALIMONY.setChecked(true);
                } else {
                    this.yesNo_ALIMONY.setChecked(false);
                }
            }
            if (json.has("is_maintance")) {
                if (json.getString("is_maintance").toString().trim().equalsIgnoreCase("1")) {
                    this.yesNo_MAINTENANCE.setChecked(true);
                } else {
                    this.yesNo_MAINTENANCE.setChecked(false);
                }
            }
            if (json.has("is_custody_order")) {
                if (json.getString("is_custody_order").toString().trim().equalsIgnoreCase("1")) {
                    this.yesNo_CUSTODY_ORDERS.setChecked(true);
                } else {
                    this.yesNo_CUSTODY_ORDERS.setChecked(false);
                }
            }
            if (json.has("is_immig_paper")) {
                if (json.getString("is_immig_paper").toString().trim().equalsIgnoreCase("1")) {
                    this.yesNo_IMMIGRATION_PAPERS.setChecked(true);
                } else {
                    this.yesNo_IMMIGRATION_PAPERS.setChecked(false);
                }
            }
            if (json.has("is_divorce_agree")) {
                if (json.getString("is_divorce_agree").toString().trim().equalsIgnoreCase("1")) {
                    this.yesNo_DIVORCE_DECREE.setChecked(true);
                } else {
                    this.yesNo_DIVORCE_DECREE.setChecked(false);
                }
            }
            if (json.has("is_sepration_agree")) {
                if (json.getString("is_sepration_agree").toString().trim().equalsIgnoreCase("1")) {
                    this.yesNo_SEPARATION_AGREEMENT.setChecked(true);
                } else {
                    this.yesNo_SEPARATION_AGREEMENT.setChecked(false);
                }
            }
            if (json.has("is_cemetery")) {
                if (json.getString("is_cemetery").toString().trim().equalsIgnoreCase("1")) {
                    this.yesNo_CEMETERY.setChecked(true);
                } else {
                    this.yesNo_CEMETERY.setChecked(false);
                }
            }
            if (json.has("is_pre_paid_fun_arra")) {
                if (json.getString("is_pre_paid_fun_arra").toString().trim().equalsIgnoreCase("1")) {
                    this.yesNoFUNERAL_ARRANGEMENTS.setChecked(true);
                } else {
                    this.yesNoFUNERAL_ARRANGEMENTS.setChecked(false);
                }
            }
            if (json.has("is_war_vet_rec")) {
                if (json.getString("is_war_vet_rec").toString().trim().equalsIgnoreCase("1")) {
                    this.yesNo_WAR_VETERAN_RECORDS.setChecked(true);
                } else {
                    this.yesNo_WAR_VETERAN_RECORDS.setChecked(false);
                }
            }
            if (json.has("is_wollet_card")) {
                if (json.getString("is_wollet_card").toString().trim().equalsIgnoreCase("1")) {
                    this.yesNo_YOUR_WALLTE_COPY.setChecked(true);
                } else {
                    this.yesNo_YOUR_WALLTE_COPY.setChecked(false);
                }
            }
            if (!json.has("is_take_pic")) {
                return;
            }
            if (json.getString("is_take_pic").toString().trim().equalsIgnoreCase("1")) {
                this.yesNo_YOUR_KEYS.setChecked(true);
            } else {
                this.yesNo_YOUR_KEYS.setChecked(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}