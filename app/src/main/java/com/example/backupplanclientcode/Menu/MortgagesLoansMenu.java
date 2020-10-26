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

public class MortgagesLoansMenu extends Activity implements OnClickListener, ResponseListerProfile, ResponseListener_General {
    private static final int SELECT_PICTURE = 1;
    JSONArray LoanJsonArray;
    LinearLayout LoansLayout;
    JSONArray MortgageJsonArray;
    LinearLayout MortgagesLayout;
    TextView actionBarTittle;
    ImageView addLoanIcon;
    ImageView addMortgageIcon;
    Button btn_back;
    Button btn_save;
    CompressImage compress;
    ConnectionDetector connection;
    ImageView currentImageVew;
    String delete_loan = "";
    String delete_mortgage = "";
    ArrayList<HashMap<String, String>> list_images;
    JSONObject loan_mortgage_data;
    SettingPreference pref;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loan_mortgage_layout);
        this.pref = new SettingPreference(getApplicationContext());
        this.compress = new CompressImage(getApplicationContext());
        this.connection = new ConnectionDetector(getApplicationContext());
        new Bugsense().startBugsense(getApplicationContext());
        findViewId();
        check_Save_Edit();
    }

    private void check_Save_Edit() {
        if (this.pref.getStringValue(Constant.MortgageLoansFlag, "").equalsIgnoreCase("1")) {
            this.btn_save.setText("Edit");
            if (!this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                this.actionBarTittle.setText("Edit " + getResources().getString(R.string.menu_Mortgage_loan));
            }
            if (this.connection.isConnectingToInternet()) {
                try {
                    JSONObject nameValuePair = new JSONObject();
                    nameValuePair.put("user_id", this.pref.getStringValue(Constant.user_id, ""));
                    nameValuePair.put("token", this.pref.getStringValue(Constant.jwttoken, ""));
                    new GeneralTask(this, ServiceUrl.get_loan_mortgage_detail, nameValuePair, 2, "post").execute(new Void[0]);
                } catch (Exception e) {
                }
                return;
            }
            displayMessage(getResources().getString(R.string.connectionFailMessage));
            return;
        }
        this.actionBarTittle.setText(getResources().getString(R.string.menu_Mortgage_loan));
        addLoanLayout();
        addMortgageLayout();
    }

    private void findViewId() {
        this.btn_save = (Button) findViewById(R.id.btn_save);
        this.btn_back = (Button) findViewById(R.id.btn_back);
        this.btn_save.setOnClickListener(this);
        this.btn_back.setOnClickListener(this);
        this.actionBarTittle = (TextView) findViewById(R.id.actionBarTittle);
        this.addMortgageIcon = (ImageView) findViewById(R.id.addMortgageIcon);
        this.addLoanIcon = (ImageView) findViewById(R.id.addLoanIcon);
        this.addMortgageIcon.setOnClickListener(this);
        this.addLoanIcon.setOnClickListener(this);
        this.MortgagesLayout = (LinearLayout) findViewById(R.id.MortgagesLayout);
        this.LoansLayout = (LinearLayout) findViewById(R.id.LoansLayout);
        this.list_images = new ArrayList<>();
        this.list_images.clear();
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            setControlEnable();
        }
    }

    private void setControlEnable() {
        this.actionBarTittle.setText(getResources().getString(R.string.menu_Mortgage_loan));
        this.btn_save.setVisibility(View.GONE);
        this.addMortgageIcon.setEnabled(false);
        this.addLoanIcon.setEnabled(false);
    }

    private void prepareSendJson() {
        try {
            this.loan_mortgage_data = new JSONObject();
            this.loan_mortgage_data.put("user_id", this.pref.getStringValue(Constant.user_id, ""));
            this.loan_mortgage_data.put("delete_loan", this.delete_loan);
            this.loan_mortgage_data.put("delete_mortgage", this.delete_mortgage);
            this.loan_mortgage_data.put("mortgage", this.MortgageJsonArray);
            this.loan_mortgage_data.put("loan", this.LoanJsonArray);
            JSONObject sendJson = new JSONObject();
            sendJson.put("loan_mortgage_data", this.loan_mortgage_data);
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

            Log.e("Sending json object", sendJson.toString());
            Log.i("list of images", this.list_images.toString());
            if (!this.connection.isConnectingToInternet()) {
                displayMessage(getResources().getString(R.string.connectionFailMessage));
            } else if (this.btn_save.getText().toString().trim().equalsIgnoreCase("edit")) {
                new SaveProfileAsytask(this, ServiceUrl.edit_loan_mortgage, nameValuePairs).execute(new Void[0]);
            } else {
                new SaveProfileAsytask(this, ServiceUrl.save_loan_mortgage, nameValuePairs).execute(new Void[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepareMortgageJson() {
        try {
            this.MortgageJsonArray = new JSONArray();
            for (int i = 0; i < this.MortgagesLayout.getChildCount(); i++) {
                JSONObject jsonbj = new JSONObject();
                ViewGroup view = (ViewGroup) this.MortgagesLayout.getChildAt(i);
                EditText edit_Name = (EditText) view.findViewById(R.id.edit_Name);
                EditText edit_Address = (EditText) view.findViewById(R.id.edit_Address);
                EditText edit_Register1 = (EditText) view.findViewById(R.id.edit_Register1);
                EditText edit_Register2 = (EditText) view.findViewById(R.id.edit_Register2);
                EditText edit_Register3 = (EditText) view.findViewById(R.id.edit_Register3);
                EditText edit_DateOfPurchase = (EditText) view.findViewById(R.id.edit_DateOfPurchase);
                EditText edit_Price = (EditText) view.findViewById(R.id.edit_Price);
                ImageView img_mortgages = (ImageView) view.findViewById(R.id.img_mortgages);
                jsonbj.put("mortgages_id", ((EditText) view.findViewById(R.id.edit_MortgagesId)).getText().toString().trim());
                jsonbj.put("m_name", edit_Name.getText().toString().trim());
                jsonbj.put("m_address", edit_Address.getText().toString().trim());
                jsonbj.put("m_owner1", edit_Register1.getText().toString().trim());
                jsonbj.put("m_owner2", edit_Register2.getText().toString().trim());
                jsonbj.put("m_owner3", edit_Register3.getText().toString().trim());
                jsonbj.put("m_date_purchase", edit_DateOfPurchase.getText().toString().trim());
                jsonbj.put("m_price", edit_Price.getText().toString().trim());
                if (img_mortgages.getContentDescription().toString().isEmpty()) {
                    jsonbj.put("m_photo", "");
                } else {
                    jsonbj.put("m_photo", "m_photo" + i);
                    HashMap<String, String> item_map = new HashMap<>();
                    item_map.put("image_path", img_mortgages.getContentDescription().toString());
                    item_map.put("image_name", "m_photo" + i);
                    this.list_images.add(item_map);
                }
                this.MortgageJsonArray.put(jsonbj);
            }
            Log.i("list_images", this.list_images.toString());
            Log.i("MortgageJsonArray", this.MortgageJsonArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prePareLoanJson() {
        try {
            this.LoanJsonArray = new JSONArray();
            for (int i = 0; i < this.LoansLayout.getChildCount(); i++) {
                JSONObject jsonbj = new JSONObject();
                ViewGroup view = (ViewGroup) this.LoansLayout.getChildAt(i);
                EditText edit_Type = (EditText) view.findViewById(R.id.edit_Type);
                EditText edit_institution = (EditText) view.findViewById(R.id.edit_institution);
                EditText edit_Amount = (EditText) view.findViewById(R.id.edit_Amount);
                EditText edit_Payment = (EditText) view.findViewById(R.id.edit_Payment);
                EditText edit_Date = (EditText) view.findViewById(R.id.edit_Date);
                jsonbj.put("loan_id", ((EditText) view.findViewById(R.id.edit_loanId)).getText().toString().trim());
                jsonbj.put("l_type", edit_Type.getText().toString().trim());
                jsonbj.put("l_institution", edit_institution.getText().toString().trim());
                jsonbj.put("l_amount", edit_Amount.getText().toString().trim());
                jsonbj.put("l_payment", edit_Payment.getText().toString().trim());
                jsonbj.put("l_completion_date", edit_Date.getText().toString().trim());
                ImageView img_loan = (ImageView) view.findViewById(R.id.img_loan);
                if (img_loan.getContentDescription().toString().isEmpty()) {
                    jsonbj.put("l_photo", "");
                } else {
                    jsonbj.put("l_photo", "l_photo" + i);
                    HashMap<String, String> item_map = new HashMap<>();
                    item_map.put("image_path", img_loan.getContentDescription().toString());
                    item_map.put("image_name", "l_photo" + i);
                    this.list_images.add(item_map);
                }
                this.LoanJsonArray.put(jsonbj);
            }
            Log.i("list_images", this.list_images.toString());
            Log.i("walletJsonArray", this.LoanJsonArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint({"InflateParams"})
    private void addMortgageLayout() {
        final View MortgageView = LayoutInflater.from(this).inflate(R.layout.layout_mortgages, null);
        ImageView removeIcon = (ImageView) MortgageView.findViewById(R.id.removeIcon);
        removeIcon.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MortgagesLoansMenu.this.MortgagesLayout.removeView(MortgageView);
            }
        });
        final ImageView img_mortgages = (ImageView) MortgageView.findViewById(R.id.img_mortgages);
        img_mortgages.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MortgagesLoansMenu.this.currentImageVew = img_mortgages;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                MortgagesLoansMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        EditText edit_MortgagesId = (EditText) MortgageView.findViewById(R.id.edit_MortgagesId);
        EditText edit_Name = (EditText) MortgageView.findViewById(R.id.edit_Name);
        EditText edit_Address = (EditText) MortgageView.findViewById(R.id.edit_Address);
        EditText edit_Register1 = (EditText) MortgageView.findViewById(R.id.edit_Register1);
        EditText edit_Register2 = (EditText) MortgageView.findViewById(R.id.edit_Register2);
        EditText edit_Register3 = (EditText) MortgageView.findViewById(R.id.edit_Register3);
        EditText edit_DateOfPurchase = (EditText) MortgageView.findViewById(R.id.edit_DateOfPurchase);
        EditText edit_Price = (EditText) MortgageView.findViewById(R.id.edit_Price);
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            removeIcon.setVisibility(View.GONE);
            img_mortgages.setEnabled(false);
            edit_MortgagesId.setEnabled(false);
            edit_Name.setEnabled(false);
            edit_Address.setEnabled(false);
            edit_Register1.setEnabled(false);
            edit_Register2.setEnabled(false);
            edit_Register3.setEnabled(false);
            edit_DateOfPurchase.setEnabled(false);
            edit_Price.setEnabled(false);
        }
        this.MortgagesLayout.addView(MortgageView);
    }

    @SuppressLint({"InflateParams"})
    private void addLoanLayout() {
        final View LoanView = LayoutInflater.from(this).inflate(R.layout.layout_loans, null);
        ImageView removeIcon = (ImageView) LoanView.findViewById(R.id.removeIcon);
        removeIcon.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MortgagesLoansMenu.this.LoansLayout.removeView(LoanView);
            }
        });
        final ImageView img_loan = (ImageView) LoanView.findViewById(R.id.img_loan);
        img_loan.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MortgagesLoansMenu.this.currentImageVew = img_loan;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                MortgagesLoansMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        EditText edit_loanId = (EditText) LoanView.findViewById(R.id.edit_loanId);
        EditText edit_Type = (EditText) LoanView.findViewById(R.id.edit_Type);
        EditText edit_institution = (EditText) LoanView.findViewById(R.id.edit_institution);
        EditText edit_Amount = (EditText) LoanView.findViewById(R.id.edit_Amount);
        EditText edit_Payment = (EditText) LoanView.findViewById(R.id.edit_Payment);
        EditText edit_Date = (EditText) LoanView.findViewById(R.id.edit_Date);
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            edit_loanId.setEnabled(false);
            img_loan.setEnabled(false);
            removeIcon.setVisibility(View.GONE);
            edit_Type.setEnabled(false);
            edit_institution.setEnabled(false);
            edit_Amount.setEnabled(false);
            edit_Payment.setEnabled(false);
            edit_Date.setEnabled(false);
        }
        this.LoansLayout.addView(LoanView);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save /*2131558589*/:
                prepareMortgageJson();
                prePareLoanJson();
                prepareSendJson();
                return;
            case R.id.btn_back /*2131558590*/:
                finish();
                return;
            case R.id.addMortgageIcon /*2131558863*/:
                addMortgageLayout();
                return;
            case R.id.addLoanIcon /*2131558865*/:
                addLoanLayout();
                return;
            default:
                return;
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
    }

    public void on_GeneralSuccess(JSONObject response, int responseCode) {
        if (response == null) {
            addLoanLayout();
            addMortgageLayout();
            return;
        }
        show_Mortgage(response);
        show_Loan(response);
    }

    @SuppressLint({"InflateParams"})
    private void show_Mortgage(JSONObject response) {
        try {
            JSONArray mortgageArray = response.getJSONObject("loan_mortgage").getJSONArray("mortgage");
            for (int i = 0; i < mortgageArray.length(); i++) {
                JSONObject json = mortgageArray.getJSONObject(i);
                final View MortgageView = LayoutInflater.from(this).inflate(R.layout.layout_mortgages, null);
                EditText edit_MortgagesId = (EditText) MortgageView.findViewById(R.id.edit_MortgagesId);
                EditText edit_Name = (EditText) MortgageView.findViewById(R.id.edit_Name);
                EditText edit_Address = (EditText) MortgageView.findViewById(R.id.edit_Address);
                EditText edit_Register1 = (EditText) MortgageView.findViewById(R.id.edit_Register1);
                EditText edit_Register2 = (EditText) MortgageView.findViewById(R.id.edit_Register2);
                EditText edit_Register3 = (EditText) MortgageView.findViewById(R.id.edit_Register3);
                EditText edit_DateOfPurchase = (EditText) MortgageView.findViewById(R.id.edit_DateOfPurchase);
                EditText edit_Price = (EditText) MortgageView.findViewById(R.id.edit_Price);
                edit_MortgagesId.setText(json.getString("mortgages_id").toString().trim());
                edit_Name.setText(json.getString("m_name").toString().trim());
                edit_Address.setText(json.getString("m_address").toString().trim());
                edit_Register1.setText(json.getString("m_owner1").toString().trim());
                edit_Register2.setText(json.getString("m_owner2").toString().trim());
                edit_Register3.setText(json.getString("m_owner3").toString().trim());
                edit_DateOfPurchase.setText(json.getString("m_date_purchase").toString().trim());
                edit_Price.setText(json.getString("m_price").toString().trim());
                ImageView removeIcon = (ImageView) MortgageView.findViewById(R.id.removeIcon);
                removeIcon.setTag(json.getString("mortgages_id").toString().trim());
                final ImageView imageView = removeIcon;
                removeIcon.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        MortgagesLoansMenu.this.MortgagesLayout.removeView(MortgageView);
                        if (MortgagesLoansMenu.this.delete_mortgage.equalsIgnoreCase("")) {
                            MortgagesLoansMenu.this.delete_mortgage = MortgagesLoansMenu.this.delete_mortgage.concat(imageView.getTag().toString());
                            return;
                        }
                        MortgagesLoansMenu.this.delete_mortgage = MortgagesLoansMenu.this.delete_mortgage.concat("," + imageView.getTag().toString());
                    }
                });
                final ImageView img_mortgages = (ImageView) MortgageView.findViewById(R.id.img_mortgages);
                UrlImageViewHelper.setUrlDrawable(img_mortgages, json.getString("m_photo").toString().trim(), (int) R.drawable.img);
                img_mortgages.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        MortgagesLoansMenu.this.currentImageVew = img_mortgages;
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction("android.intent.action.GET_CONTENT");
                        MortgagesLoansMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                    }
                });
                if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                    removeIcon.setVisibility(View.GONE);
                    img_mortgages.setEnabled(false);
                    edit_MortgagesId.setEnabled(false);
                    edit_Name.setEnabled(false);
                    edit_Address.setEnabled(false);
                    edit_Register1.setEnabled(false);
                    edit_Register2.setEnabled(false);
                    edit_Register3.setEnabled(false);
                    edit_DateOfPurchase.setEnabled(false);
                    edit_Price.setEnabled(false);
                }
                this.MortgagesLayout.addView(MortgageView);
            }
        } catch (Exception e) {
            addMortgageLayout();
            e.printStackTrace();
        }
    }

    @SuppressLint({"InflateParams"})
    private void show_Loan(JSONObject response) {
        try {
            JSONArray loanArray = response.getJSONObject("loan_mortgage").getJSONArray("loan_mortgage_data");
            for (int i = 0; i < loanArray.length(); i++) {
                JSONObject json = loanArray.getJSONObject(i);
                final View loanView = LayoutInflater.from(this).inflate(R.layout.layout_loans, null);
                EditText edit_Type = (EditText) loanView.findViewById(R.id.edit_Type);
                EditText edit_institution = (EditText) loanView.findViewById(R.id.edit_institution);
                EditText edit_Amount = (EditText) loanView.findViewById(R.id.edit_Amount);
                EditText edit_Payment = (EditText) loanView.findViewById(R.id.edit_Payment);
                EditText edit_Date = (EditText) loanView.findViewById(R.id.edit_Date);
                ((EditText) loanView.findViewById(R.id.edit_loanId)).setText(json.getString("loan_id").toString().trim());
                edit_Type.setText(json.getString("l_type").toString().trim());
                edit_institution.setText(json.getString("l_institution").toString().trim());
                edit_Amount.setText(json.getString("l_amount").toString().trim());
                edit_Payment.setText(json.getString("l_payment").toString().trim());
                edit_Date.setText(json.getString("l_completion_date").toString().trim());
                ImageView removeIcon = (ImageView) loanView.findViewById(R.id.removeIcon);
                removeIcon.setTag(json.getString("loan_id").toString().trim());
                final ImageView imageView = removeIcon;
                removeIcon.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        MortgagesLoansMenu.this.LoansLayout.removeView(loanView);
                        if (MortgagesLoansMenu.this.delete_loan.equalsIgnoreCase("")) {
                            MortgagesLoansMenu.this.delete_loan = MortgagesLoansMenu.this.delete_loan.concat(imageView.getTag().toString());
                            return;
                        }
                        MortgagesLoansMenu.this.delete_loan = MortgagesLoansMenu.this.delete_loan.concat("," + imageView.getTag().toString());
                    }
                });
                final ImageView img_loan = (ImageView) loanView.findViewById(R.id.img_loan);
                UrlImageViewHelper.setUrlDrawable(img_loan, json.getString("l_photo").toString().trim(), (int) R.drawable.img);
                img_loan.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        MortgagesLoansMenu.this.currentImageVew = img_loan;
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction("android.intent.action.GET_CONTENT");
                        MortgagesLoansMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                    }
                });
                if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                    img_loan.setEnabled(false);
                    removeIcon.setVisibility(View.GONE);
                    edit_Type.setEnabled(false);
                    edit_institution.setEnabled(false);
                    edit_Amount.setEnabled(false);
                    edit_Payment.setEnabled(false);
                    edit_Date.setEnabled(false);
                }
                this.LoansLayout.addView(loanView);
            }
        } catch (Exception e) {
            addLoanLayout();
            e.printStackTrace();
        }
    }

    public void on_ProfileSuccess(JSONObject response) {
        try {
            if (response.has("message")) {
                displayMessage(response.getString("message").toString());
            }
            if (response.has("success") && response.getString("success").toString().trim().equalsIgnoreCase("1")) {
                this.pref.setStringValue(Constant.MortgageLoansFlag, response.getString("success").toString());
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}