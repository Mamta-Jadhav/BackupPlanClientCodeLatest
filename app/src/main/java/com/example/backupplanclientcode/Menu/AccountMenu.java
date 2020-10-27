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

public class AccountMenu extends Activity implements OnClickListener, ResponseListerProfile, ResponseListener_General {
    private static final int SELECT_PICTURE = 1;
    JSONObject accountJson;
    JSONArray accountJsonArray;
    LinearLayout accountLayout;
    TextView actionBarTittle;
    ImageView addAccountIcon;
    ImageView addCreditCardIcon;
    ImageView addLineOfCreditIcon;
    ImageView addWalletIcon;
    Button btn_back;
    Button btn_save;
    CompressImage compress;
    ConnectionDetector connection;
    LinearLayout creditCardLayout;
    JSONArray creditCardsJsonArray;
    ImageView currentImageVew;
    String delete_account = "";
    String delete_credit_card = "";
    String delete_line_of_credit = "";
    String delete_wallet = "";
    JSONArray lineOfCreditJsonArray;
    LinearLayout lineOfCreditsLayout;
    ArrayList<HashMap<String, String>> list_images;
    LinearLayout main_container;
    SettingPreference pref;
    JSONArray walletJsonArray;
    LinearLayout walletLayout;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_menu_layout);
        intilization();
        findViewIds();
        checkAlredySaveAccount();
    }

    private void intilization() {
        this.pref = new SettingPreference(getApplicationContext());
        this.compress = new CompressImage(getApplicationContext());
        this.connection = new ConnectionDetector(getApplicationContext());
        new Bugsense().startBugsense(getApplicationContext());
    }

    private void checkAlredySaveAccount() {
        if (this.pref.getStringValue(Constant.accountFlag, "").equalsIgnoreCase("1")) {
            this.btn_save.setText("Edit");
            if (!this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                this.actionBarTittle.setText("Edit " + getResources().getString(R.string.menu_account));
            }
            if (this.connection.isConnectingToInternet()) {
                try {
                    JSONObject nameValuePair = new JSONObject();
                    nameValuePair.put("user_id", this.pref.getStringValue(Constant.user_id, ""));//2
                    nameValuePair.put("token", this.pref.getStringValue(Constant.jwttoken, ""));
                    new GeneralTask(this, ServiceUrl.get_account_detail, nameValuePair, 2, "post").execute(new Void[0]);
                } catch (Exception e) {
                }
                return;
            }
            displayMessage(getResources().getString(R.string.connectionFailMessage));
            addAccountLayout();
            addLineOfCreditsLayout();
            addCreditCardslayout();
            addWalletLayout();
            return;
        }
        this.actionBarTittle.setText(getResources().getString(R.string.menu_account));
        addAccountLayout();
        addLineOfCreditsLayout();
        addCreditCardslayout();
        addWalletLayout();
    }

    private void findViewIds() {
        this.actionBarTittle = (TextView) findViewById(R.id.actionBarTittle);
        this.list_images = new ArrayList<>();
        this.list_images.clear();
        this.accountJsonArray = new JSONArray();
        this.lineOfCreditJsonArray = new JSONArray();
        this.creditCardsJsonArray = new JSONArray();
        this.walletJsonArray = new JSONArray();
        this.accountJson = new JSONObject();
        this.main_container = (LinearLayout) findViewById(R.id.main_container);
        this.accountLayout = (LinearLayout) findViewById(R.id.accountLayout);
        this.lineOfCreditsLayout = (LinearLayout) findViewById(R.id.lineOfCreditsLayout);
        this.creditCardLayout = (LinearLayout) findViewById(R.id.creditCardLayout);
        this.walletLayout = (LinearLayout) findViewById(R.id.walletLayout);
        this.addAccountIcon = (ImageView) findViewById(R.id.addAccountIcon);
        this.addAccountIcon.setOnClickListener(this);
        this.addLineOfCreditIcon = (ImageView) findViewById(R.id.addLineOfCreditIcon);
        this.addLineOfCreditIcon.setOnClickListener(this);
        this.addCreditCardIcon = (ImageView) findViewById(R.id.addCreditCardIcon);
        this.addCreditCardIcon.setOnClickListener(this);
        this.addWalletIcon = (ImageView) findViewById(R.id.addWalletIcon);
        this.addWalletIcon.setOnClickListener(this);
        this.btn_save = (Button) findViewById(R.id.btn_save);
        this.btn_save.setOnClickListener(this);
        this.btn_back = (Button) findViewById(R.id.btn_back);
        this.btn_back.setOnClickListener(this);
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            setEnableControl();
        }
    }

    private void setEnableControl() {
        this.actionBarTittle.setText(getResources().getString(R.string.menu_account));
        this.addAccountIcon.setEnabled(false);
        this.addLineOfCreditIcon.setEnabled(false);
        this.addCreditCardIcon.setEnabled(false);
        this.addWalletIcon.setEnabled(false);
        this.btn_save.setVisibility(View.GONE);
    }

    @SuppressLint({"InflateParams"})
    private void addWalletLayout() {
        final View walletView = LayoutInflater.from(this).inflate(R.layout.layout_wallet, null);
        ImageView removeWalletIcon = (ImageView) walletView.findViewById(R.id.removeWalletIcon);
        removeWalletIcon.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AccountMenu.this.walletLayout.removeView(walletView);
            }
        });
        final ImageView imgWallet = (ImageView) walletView.findViewById(R.id.imgWallet);
        imgWallet.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AccountMenu.this.currentImageVew = imgWallet;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                AccountMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        EditText edit_Card = (EditText) walletView.findViewById(R.id.edit_Card);
        EditText edit_Number = (EditText) walletView.findViewById(R.id.edit_Number);
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            edit_Card.setEnabled(false);
            edit_Number.setEnabled(false);
            imgWallet.setEnabled(false);
            removeWalletIcon.setVisibility(View.GONE);
        }
        this.walletLayout.addView(walletView);
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
    private void addCreditCardslayout() {
        final View creditCardView = LayoutInflater.from(this).inflate(R.layout.layout_credits_cards, null);
        ImageView removeCreditCards = (ImageView) creditCardView.findViewById(R.id.removeCreditCards);
        removeCreditCards.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AccountMenu.this.creditCardLayout.removeView(creditCardView);
            }
        });
        EditText edit_Type = (EditText) creditCardView.findViewById(R.id.edit_Type);
        EditText edit_Number = (EditText) creditCardView.findViewById(R.id.edit_Number);
        EditText edit_Institution = (EditText) creditCardView.findViewById(R.id.edit_Institution);
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            edit_Type.setEnabled(false);
            edit_Number.setEnabled(false);
            edit_Institution.setEnabled(false);
            removeCreditCards.setVisibility(View.GONE);
        }
        this.creditCardLayout.addView(creditCardView);
    }

    @SuppressLint({"InflateParams"})
    private void addLineOfCreditsLayout() {
        final View view = LayoutInflater.from(this).inflate(R.layout.layout_line_of_credits, null);
        ImageView removeLineofCreditIcon = (ImageView) view.findViewById(R.id.removeLineofCreditIcon);
        removeLineofCreditIcon.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AccountMenu.this.lineOfCreditsLayout.removeView(view);
            }
        });
        EditText edit_institution = (EditText) view.findViewById(R.id.edit_institution);
        EditText edit_location = (EditText) view.findViewById(R.id.edit_location);
        EditText edit_amount = (EditText) view.findViewById(R.id.edit_amount);
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            edit_institution.setEnabled(false);
            edit_location.setEnabled(false);
            edit_amount.setEnabled(false);
            removeLineofCreditIcon.setVisibility(View.GONE);
        }
        this.lineOfCreditsLayout.addView(view);
    }

    @SuppressLint({"InflateParams"})
    private void addAccountLayout() {
        final View accountView = LayoutInflater.from(this).inflate(R.layout.layout_account, null);
        final ImageView img_Account = (ImageView) accountView.findViewById(R.id.img_Account);
        img_Account.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AccountMenu.this.currentImageVew = img_Account;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                AccountMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        EditText edit_accType = (EditText) accountView.findViewById(R.id.edit_accType);
        EditText edit_accOwner = (EditText) accountView.findViewById(R.id.edit_accOwner);
        EditText edit_accLocation = (EditText) accountView.findViewById(R.id.edit_accLocation);
        ImageView removerAccountIcon = (ImageView) accountView.findViewById(R.id.removerAccountIcon);
        removerAccountIcon.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AccountMenu.this.accountLayout.removeView(accountView);
            }
        });
        ToggleButton yesNoAccount = (ToggleButton) accountView.findViewById(R.id.yesNoAccount);
        yesNoAccount.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    img_Account.setVisibility(View.VISIBLE);
                } else {
                    img_Account.setVisibility(View.GONE);
                }
            }
        });
        if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
            edit_accType.setEnabled(false);
            edit_accOwner.setEnabled(false);
            edit_accLocation.setEnabled(false);
            yesNoAccount.setEnabled(false);
            removerAccountIcon.setVisibility(View.GONE);
            img_Account.setEnabled(false);
        }
        this.accountLayout.addView(accountView);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addAccountIcon /*2131558574*/:
                addAccountLayout();
                return;
            case R.id.addLineOfCreditIcon /*2131558576*/:
                addLineOfCreditsLayout();
                return;
            case R.id.addCreditCardIcon /*2131558578*/:
                addCreditCardslayout();
                return;
            case R.id.addWalletIcon /*2131558580*/:
                addWalletLayout();
                return;
            case R.id.btn_save /*2131558589*/:
                PrepareAccountJson();
                PrepareLineOfCreditJson();
                PrePareCreditCardJson();
                PrePareWalletJson();
                PrePareToSendJson();
                return;
            case R.id.btn_back /*2131558590*/:
                finish();
                return;
            default:
                return;
        }
    }

    private void PrePareToSendJson() {
        try {
            this.accountJson = new JSONObject();
            this.accountJson.putOpt("user_id", this.pref.getStringValue(Constant.user_id, ""));
            this.accountJson.putOpt("delete_account", this.delete_account);
            this.accountJson.putOpt("delete_credit_card", this.delete_credit_card);
            this.accountJson.putOpt("delete_line_of_credit", this.delete_line_of_credit);
            this.accountJson.putOpt("delete_wallet", this.delete_wallet);
            this.accountJson.putOpt("account", this.accountJsonArray);
            this.accountJson.putOpt("line_of_credit", this.lineOfCreditJsonArray);
            this.accountJson.putOpt("credit_card", this.creditCardsJsonArray);
            this.accountJson.putOpt("wallet", this.walletJsonArray);
            Log.e("account JSON :", this.accountJson.toString());
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            JSONObject sendJson = new JSONObject();
            sendJson.put("account_data", this.accountJson);
            entity.addPart("json_data", new StringBody(sendJson.toString()));
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("json_data", sendJson.toString()));
            for (int i = 0; i < this.list_images.size(); i++) {
                entity.addPart((String) ((HashMap) this.list_images.get(i)).get("image_name"), new FileBody(new File((String) ((HashMap) this.list_images.get(i)).get("image_path"))));
                Log.i("file parameter", ((String) ((HashMap) this.list_images.get(i)).get("image_name")).toString());
                Log.i("file path", ((String) ((HashMap) this.list_images.get(i)).get("image_path")).toString());
            }
            Log.i("list of images", this.list_images.toString());
            Log.e("Sending json object", sendJson.toString());
            if (!this.connection.isConnectingToInternet()) {
                displayMessage(getResources().getString(R.string.connectionFailMessage));
            } else if (this.btn_save.getText().toString().trim().equalsIgnoreCase("edit")) {
                new SaveProfileAsytask(this, ServiceUrl.edit_account, entity).execute(new Void[0]);
            } else {
                new SaveProfileAsytask(this, ServiceUrl.save_account, entity).execute(new Void[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void PrePareWalletJson() {
        try {
            this.walletJsonArray = new JSONArray();
            for (int i = 0; i < this.walletLayout.getChildCount(); i++) {
                JSONObject jsonbj = new JSONObject();
                ViewGroup view = (ViewGroup) this.walletLayout.getChildAt(i);
                EditText edit_Card = (EditText) view.findViewById(R.id.edit_Card);
                EditText edit_Number = (EditText) view.findViewById(R.id.edit_Number);
                ImageView imgWallet = (ImageView) view.findViewById(R.id.imgWallet);
                jsonbj.put("wallet_id", ((EditText) view.findViewById(R.id.edit_walltet_id)).getText().toString().trim());
                jsonbj.put("w_card", edit_Card.getText().toString().trim());
                jsonbj.put("w_number", edit_Number.getText().toString().trim());
                if (imgWallet.getContentDescription().toString().isEmpty()) {
                    jsonbj.put("w_photo", "");
                } else {
                    jsonbj.put("w_photo", "image" + i);
                    HashMap<String, String> item_map = new HashMap<>();
                    item_map.put("image_path", imgWallet.getContentDescription().toString());
                    item_map.put("image_name", "image" + i);
                    this.list_images.add(item_map);
                }
                this.walletJsonArray.put(jsonbj);
            }
            Log.i("list_images", this.list_images.toString());
            Log.i("Json Array :", this.walletJsonArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void PrePareCreditCardJson() {
        try {
            this.creditCardsJsonArray = new JSONArray();
            for (int i = 0; i < this.creditCardLayout.getChildCount(); i++) {
                ViewGroup view = (ViewGroup) this.creditCardLayout.getChildAt(i);
                EditText edit_Type = (EditText) view.findViewById(R.id.edit_Type);
                EditText edit_Number = (EditText) view.findViewById(R.id.edit_Number);
                EditText edit_Institution = (EditText) view.findViewById(R.id.edit_Institution);
                EditText edit_credit_card_id = (EditText) view.findViewById(R.id.edit_credit_card_id);
                JSONObject jsonbj = new JSONObject();
                jsonbj.put("credit_card_id", edit_credit_card_id.getText().toString().trim());
                jsonbj.put("cc_type", edit_Type.getText().toString().trim());
                jsonbj.put("cc_number", edit_Number.getText().toString().trim());
                jsonbj.put("cc_institution", edit_Institution.getText().toString().trim());
                this.creditCardsJsonArray.put(jsonbj);
            }
            Log.i("Json Array :", this.creditCardsJsonArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void PrepareLineOfCreditJson() {
        try {
            this.lineOfCreditJsonArray = new JSONArray();
            for (int i = 0; i < this.lineOfCreditsLayout.getChildCount(); i++) {
                ViewGroup view = (ViewGroup) this.lineOfCreditsLayout.getChildAt(i);
                EditText edit_institution = (EditText) view.findViewById(R.id.edit_institution);
                EditText edit_location = (EditText) view.findViewById(R.id.edit_location);
                EditText edit_amount = (EditText) view.findViewById(R.id.edit_amount);
                EditText edit_line_of_credit_id = (EditText) view.findViewById(R.id.edit_line_of_credit_id);
                JSONObject jsonbj = new JSONObject();
                jsonbj.put("line_of_credit_id", edit_line_of_credit_id.getText().toString().trim());
                jsonbj.put("lc_institution", edit_institution.getText().toString().trim());
                jsonbj.put("lc_location", edit_location.getText().toString().trim());
                jsonbj.put("lc_amount", edit_amount.getText().toString().trim());
                this.lineOfCreditJsonArray.put(jsonbj);
            }
            Log.i("Json Array :", this.lineOfCreditJsonArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void PrepareAccountJson() {
        try {
            this.accountJsonArray = new JSONArray();
            for (int i = 0; i < this.accountLayout.getChildCount(); i++) {
                JSONObject jsonbj = new JSONObject();
                ViewGroup view = (ViewGroup) this.accountLayout.getChildAt(i);
                EditText edit_accType = (EditText) view.findViewById(R.id.edit_accType);
                EditText edit_accOwner = (EditText) view.findViewById(R.id.edit_accOwner);
                EditText edit_accLocation = (EditText) view.findViewById(R.id.edit_accLocation);
                EditText edit_account_id = (EditText) view.findViewById(R.id.edit_account_id);
                ToggleButton yesNoAccount = (ToggleButton) view.findViewById(R.id.yesNoAccount);
                ImageView img_Account = (ImageView) view.findViewById(R.id.img_Account);
                if (img_Account.getContentDescription().toString().isEmpty()) {
                    jsonbj.put("ac_photo", "");
                } else {
                    jsonbj.put("ac_photo", "ac_photo" + i);
                    HashMap<String, String> item_map = new HashMap<>();
                    item_map.put("image_path", img_Account.getContentDescription().toString());
                    item_map.put("image_name", "ac_photo" + i);
                    this.list_images.add(item_map);
                }
                jsonbj.put("account_id", edit_account_id.getText().toString().trim());
                jsonbj.put("ac_type", edit_accType.getText().toString().trim());
                jsonbj.put("ac_owner", edit_accOwner.getText().toString().trim());
                jsonbj.put("ac_location", edit_accLocation.getText().toString().trim());
                if (yesNoAccount.isChecked()) {
                    jsonbj.put("ac_is_photo", "1");
                } else {
                    jsonbj.put("ac_is_photo", "0");
                }
                this.accountJsonArray.put(jsonbj);
            }
            Log.i("Account Json Array :", this.accountJsonArray.toString());
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
                this.pref.setStringValue(Constant.accountFlag, response.getString("success").toString());
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void on_GeneralSuccess(JSONObject response, int responseCode) {
        if (response == null) {
            addAccountLayout();
            addLineOfCreditsLayout();
            addCreditCardslayout();
            addWalletLayout();
        } else if (responseCode == 2) {
            show_account_Detail(response);
        }
    }

    private void show_account_Detail(JSONObject response) {
        show_account(response);
        show_line_of_credits(response);
        show_Credits(response);
        show_Walltes(response);
    }

    @SuppressLint({"InflateParams"})
    private void show_Walltes(JSONObject response) {
        try {
            JSONArray wallet = response.getJSONObject("wallet").getJSONArray("wallet");
            if (wallet.length() < 1) {
                addWalletLayout();
                return;
            }
            for (int i = 0; i < wallet.length(); i++) {
                JSONObject json = wallet.getJSONObject(i);
                final View walletView = LayoutInflater.from(this).inflate(R.layout.layout_wallet, null);
                final ImageView removeWalletIcon = (ImageView) walletView.findViewById(R.id.removeWalletIcon);
                removeWalletIcon.setTag(json.getString("wallet_id").toString().trim());
                removeWalletIcon.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AccountMenu.this.walletLayout.removeView(walletView);
                        if (AccountMenu.this.delete_wallet.equalsIgnoreCase("")) {
                            AccountMenu.this.delete_wallet = AccountMenu.this.delete_wallet.concat(removeWalletIcon.getTag().toString());
                            return;
                        }
                        AccountMenu.this.delete_wallet = AccountMenu.this.delete_wallet.concat("," + removeWalletIcon.getTag().toString());
                    }
                });
                ((EditText) walletView.findViewById(R.id.edit_walltet_id)).setText(json.getString("wallet_id").toString().trim());
                EditText edit_Card = (EditText) walletView.findViewById(R.id.edit_Card);
                edit_Card.setText(json.getString("w_card").toString().trim());
                EditText edit_Number = (EditText) walletView.findViewById(R.id.edit_Number);
                edit_Number.setText(json.getString("w_number").toString().trim());
                final ImageView imgWallet = (ImageView) walletView.findViewById(R.id.imgWallet);
                imgWallet.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AccountMenu.this.currentImageVew = imgWallet;
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction("android.intent.action.GET_CONTENT");
                        AccountMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                    }
                });
                UrlImageViewHelper.setUrlDrawable(imgWallet, json.getString("w_photo").toString().trim(), (int) R.drawable.img);
                if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                    edit_Card.setEnabled(false);
                    edit_Number.setEnabled(false);
                    imgWallet.setEnabled(false);
                    removeWalletIcon.setVisibility(View.GONE);
                }
                this.walletLayout.addView(walletView);
            }
        } catch (Exception e) {
            addWalletLayout();
            e.printStackTrace();
        }
    }

    @SuppressLint({"InflateParams"})
    private void show_Credits(JSONObject response) {
        try {
            JSONArray credit_card = response.getJSONObject("credit_card").getJSONArray("credit_card");
            if (credit_card.length() < 1) {
                addCreditCardslayout();
                return;
            }
            for (int i = 0; i < credit_card.length(); i++) {
                JSONObject json = credit_card.getJSONObject(i);
                final View creditCaerdView = LayoutInflater.from(this).inflate(R.layout.layout_credits_cards, null);
                final ImageView removeCreditCards = (ImageView) creditCaerdView.findViewById(R.id.removeCreditCards);
                removeCreditCards.setTag(json.getString("credit_card_id").toString().trim());
                removeCreditCards.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AccountMenu.this.creditCardLayout.removeView(creditCaerdView);
                        if (AccountMenu.this.delete_credit_card.equalsIgnoreCase("")) {
                            AccountMenu.this.delete_credit_card = AccountMenu.this.delete_credit_card.concat(removeCreditCards.getTag().toString());
                            return;
                        }
                        AccountMenu.this.delete_credit_card = AccountMenu.this.delete_credit_card.concat("," + removeCreditCards.getTag().toString());
                    }
                });
                ((EditText) creditCaerdView.findViewById(R.id.edit_credit_card_id)).setText(json.getString("credit_card_id").toString().trim());
                EditText edit_Type = (EditText) creditCaerdView.findViewById(R.id.edit_Type);
                edit_Type.setText(json.getString("cc_type").toString().trim());
                EditText edit_Number = (EditText) creditCaerdView.findViewById(R.id.edit_Number);
                edit_Number.setText(json.getString("cc_number").toString().trim());
                EditText edit_Institution = (EditText) creditCaerdView.findViewById(R.id.edit_Institution);
                edit_Institution.setText(json.getString("cc_institution").toString().trim());
                if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                    edit_Type.setEnabled(false);
                    edit_Number.setEnabled(false);
                    edit_Institution.setEnabled(false);
                    removeCreditCards.setVisibility(View.GONE);
                }
                this.creditCardLayout.addView(creditCaerdView);
            }
        } catch (Exception e) {
            addCreditCardslayout();
            e.printStackTrace();
        }
    }

    @SuppressLint({"InflateParams"})
    private void show_line_of_credits(JSONObject response) {
        try {
            JSONArray line_of_credit = response.getJSONObject("line_of_credit").getJSONArray("line_of_credit");
            if (line_of_credit.length() < 1) {
                addLineOfCreditsLayout();
                return;
            }
            for (int i = 0; i < line_of_credit.length(); i++) {
                JSONObject json = line_of_credit.getJSONObject(i);
                final View lineOfCreditsView = LayoutInflater.from(this).inflate(R.layout.layout_line_of_credits, null);
                final ImageView removeLineofCreditIcon = (ImageView) lineOfCreditsView.findViewById(R.id.removeLineofCreditIcon);
                removeLineofCreditIcon.setTag(json.getString("line_of_credit_id").toString().trim());
                removeLineofCreditIcon.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AccountMenu.this.lineOfCreditsLayout.removeView(lineOfCreditsView);
                        if (AccountMenu.this.delete_line_of_credit.equalsIgnoreCase("")) {
                            AccountMenu.this.delete_line_of_credit = AccountMenu.this.delete_line_of_credit.concat(removeLineofCreditIcon.getTag().toString());
                            return;
                        }
                        AccountMenu.this.delete_line_of_credit = AccountMenu.this.delete_line_of_credit.concat("," + removeLineofCreditIcon.getTag().toString());
                    }
                });
                ((EditText) lineOfCreditsView.findViewById(R.id.edit_line_of_credit_id)).setText(json.getString("line_of_credit_id").toString().trim());
                EditText edit_institution = (EditText) lineOfCreditsView.findViewById(R.id.edit_institution);
                edit_institution.setText(json.getString("lc_institution").toString().trim());
                EditText edit_location = (EditText) lineOfCreditsView.findViewById(R.id.edit_location);
                edit_location.setText(json.getString("lc_location").toString().trim());
                EditText edit_amount = (EditText) lineOfCreditsView.findViewById(R.id.edit_amount);
                edit_amount.setText(json.getString("lc_amount").toString().trim());
                if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                    edit_institution.setEnabled(false);
                    edit_location.setEnabled(false);
                    edit_amount.setEnabled(false);
                    removeLineofCreditIcon.setVisibility(View.GONE);
                }
                this.lineOfCreditsLayout.addView(lineOfCreditsView);
            }
        } catch (Exception e) {
            addLineOfCreditsLayout();
            e.printStackTrace();
        }
    }

    @SuppressLint({"InflateParams"})
    private void show_account(JSONObject response) {
        try {
            JSONArray accountDetail = response.getJSONObject("account").getJSONArray("account");
            if (accountDetail.length() < 1) {
                addAccountLayout();
                return;
            }
            for (int i = 0; i < accountDetail.length(); i++) {
                JSONObject json = accountDetail.getJSONObject(i);
                final View accountView = LayoutInflater.from(this).inflate(R.layout.layout_account, null);
                final ImageView removerAccountIcon = (ImageView) accountView.findViewById(R.id.removerAccountIcon);
                final ImageView img_Account = (ImageView) accountView.findViewById(R.id.img_Account);
                img_Account.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AccountMenu.this.currentImageVew = img_Account;
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction("android.intent.action.GET_CONTENT");
                        AccountMenu.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                    }
                });
                UrlImageViewHelper.setUrlDrawable(img_Account, json.getString("ac_photo").toString().trim(), (int) R.drawable.img);
                removerAccountIcon.setTag(json.getString("account_id").toString().trim());
                removerAccountIcon.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AccountMenu.this.accountLayout.removeView(accountView);
                        if (AccountMenu.this.delete_account.equalsIgnoreCase("")) {
                            AccountMenu.this.delete_account = AccountMenu.this.delete_account.concat(removerAccountIcon.getTag().toString());
                            return;
                        }
                        AccountMenu.this.delete_account = AccountMenu.this.delete_account.concat("," + removerAccountIcon.getTag().toString());
                    }
                });
                ((EditText) accountView.findViewById(R.id.edit_account_id)).setText(json.getString("account_id").toString().trim());
                EditText edit_accType = (EditText) accountView.findViewById(R.id.edit_accType);
                edit_accType.setText(json.getString("ac_type").toString().trim());
                EditText edit_accOwner = (EditText) accountView.findViewById(R.id.edit_accOwner);
                edit_accOwner.setText(json.getString("ac_owner").toString().trim());
                EditText edit_accLocation = (EditText) accountView.findViewById(R.id.edit_accLocation);
                edit_accLocation.setText(json.getString("ac_location").toString().trim());
                ToggleButton yesNoAccount = (ToggleButton) accountView.findViewById(R.id.yesNoAccount);
                if (json.getString("ac_is_photo").toString().trim().equalsIgnoreCase("1")) {
                    yesNoAccount.setChecked(true);
                } else {
                    yesNoAccount.setChecked(false);
                }
                if (this.pref.getBooleanValue(Constant.isGuestLogin, false)) {
                    edit_accType.setEnabled(false);
                    edit_accOwner.setEnabled(false);
                    edit_accLocation.setEnabled(false);
                    yesNoAccount.setEnabled(false);
                    removerAccountIcon.setVisibility(View.GONE);
                    img_Account.setEnabled(false);
                }
                this.accountLayout.addView(accountView);
            }
        } catch (Exception e) {
            addAccountLayout();
            e.printStackTrace();
        }
    }

    private void displayMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
