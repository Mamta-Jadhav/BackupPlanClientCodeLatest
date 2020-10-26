package com.example.backupplanclientcode.Menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.backupplanclientcode.Asyntask.GeneralTask.ResponseListener_General;
import com.example.backupplanclientcode.Asyntask.SaveProfileAsytask.ResponseListerProfile;
import com.example.backupplanclientcode.Bugsense.Bugsense;
import com.example.backupplanclientcode.ConnectionDetector;
import com.example.backupplanclientcode.Preference.SettingPreference;
import com.example.backupplanclientcode.R;
import com.example.backupplanclientcode.Utility.CompressImage;
import org.json.JSONArray;
import org.json.JSONObject;

public class InternetMenuActivityCopy extends Activity implements OnClickListener, ResponseListerProfile, ResponseListener_General {
    private static final int SELECT_PICTURE = 1;
    JSONArray BlogJsonArray;
    JSONArray FacebookjsonArray;
    int InstagCount = 0;
    JSONArray InstagramJsonArray;
    int LinkedinCount = 0;
    JSONArray LinkedinJsonArray;
    JSONArray ServeJsonArray;
    JSONArray TwitterJsonArray;
    JSONArray WebsiteJsonArray;
    TextView actionBarTittle;
    ImageView addBlog;
    ImageView addEmail;
    ImageView addFacebook;
    ImageView addInstagram;
    ImageView addLinkedin;
    ImageView addServer;
    ImageView addTwitter;
    ImageView addWebsite;
    int blogCount = 0;
    Button btn_back;
    Button btn_remove;
    Button btn_save;
    CompressImage compress;
    ConnectionDetector connection;
    ImageView currentImageVew;
    String delete_blog = "";
    String delete_cpanel = "";
    String delete_email = "";
    String delete_facebook = "";
    String delete_instagram = "";
    String delete_linkedin = "";
    String delete_twitter = "";
    String delete_website = "";
    EditText edit_ComPassword;
    EditText edit_comLocation;
    EditText edit_compId;
    int emailCount = 0;
    JSONArray emailJsonArray;
    int facebookCount = 0;

    /* renamed from: iv */
    ImageView f40iv;
    LinearLayout layoutBlog;
    LinearLayout layoutInstagram;
    LinearLayout layoutLinkedin;
    LinearLayout layoutServer;
    LinearLayout layoutWebsite;
    LinearLayout layout_Email;
    LinearLayout layout_Facebook;
    LinearLayout layout_Twitter;
    SettingPreference pref;
    int serverCount = 0;
    int twitterCount = 0;
    int websiteCount = 0;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_internet);
        this.pref = new SettingPreference(getApplicationContext());
        this.compress = new CompressImage(getApplicationContext());
        this.connection = new ConnectionDetector(getApplicationContext());
        new Bugsense().startBugsense(getApplicationContext());
    }

    public void onClick(View v) {
    }

    public void on_GeneralSuccess(JSONObject response, int responseCode) {
    }

    public void on_ProfileSuccess(JSONObject response) {
    }
}
