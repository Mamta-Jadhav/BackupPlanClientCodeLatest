package com.example.backupplanclientcode.Asyntask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.backupplanclientcode.Constant.Constant;
import com.example.backupplanclientcode.HttpLoader.HttpLoader;
import com.example.backupplanclientcode.Preference.SettingPreference;
import com.example.backupplanclientcode.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.List;

public class SaveProfileAsytask extends AsyncTask<Void, Void, JSONObject> {
    String Url = "";
    Activity context;
    SettingPreference pref;
    List<NameValuePair> entity;

    /* renamed from: jo */
    JSONObject f36jo;

    /* renamed from: pd */
    ProgressDialog f37pd;

    public interface ResponseListerProfile {
        void on_ProfileSuccess(JSONObject jSONObject);
    }

    public SaveProfileAsytask(Activity ctx, String url, MultipartEntity entity2) {
        this.context = ctx;
        this.Url = url;
//        this.entity = entity2;
        this.pref = new SettingPreference(ctx);
    }

    public SaveProfileAsytask(Activity ctx, String url, List<NameValuePair> entity2) {
        this.context = ctx;
        this.Url = url;
        this.entity = entity2;
        this.pref = new SettingPreference(ctx);
    }

    /* access modifiers changed from: protected */
    public JSONObject doInBackground(Void... params) {
        String str = "";
        try {
            HttpClient myClient = new DefaultHttpClient();
            HttpPost httpost = new HttpPost(this.Url + "?token=" + this.pref.getStringValue(Constant.jwttoken, ""));
//            httpost.setHeader("Postman-Token",
//                    "<calculated when request is sent>");
//            httpost.setHeader("Content-Type",
//                    "multipart/form-data; boundary=<calculated when request is sent>");
////            httpost.setHeader("Content-Length",
////                    "<calculated when request is sent>");
//            httpost.setHeader("Host",
//                    "<calculated when request is sent>");
//            httpost.setHeader(HTTP.USER_AGENT,
//                    "PostmanRuntime/7.26.5");
//            httpost.setHeader("Accept",
//                    "*/*");
//            httpost.setHeader("Accept-Encoding",
//                    "gzip, deflate, br");
//            httpost.setHeader(HTTP.CONN_DIRECTIVE,
//                    "keep-alive");
//            httpost.setHeader(HTTP.CONTENT_TYPE,
//                    "multipart/form-data");
//            httpost.setHeader("Accept",
//                    "application/json");
//            httpost.setHeader("Authorization",
//                    "bearer " + this.pref.getStringValue(Constant.jwttoken, ""));
            if (this.entity != null) {
//                httpost.setEntity(this.entity);
                httpost.setEntity(new UrlEncodedFormEntity(this.entity));
            }
//            String str2 = EntityUtils.toString(myClient.execute(httpost).getEntity(), HTTP.UTF_8);

            HttpResponse response = myClient.execute(httpost);
            Log.i("Response :", response.toString());
//            this.f36jo = new JSONObject(str2);
            this.f36jo = new JSONObject(EntityUtils.toString(response.getEntity()));
            Log.e("General Category.", "General Task");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"InlinedApi"})
    public void onPreExecute() {
        super.onPreExecute();
        this.f37pd = new ProgressDialog(this.context, 3);
        this.f37pd.setMessage(this.context.getResources().getString(R.string.txt_loading));
        this.f37pd.setCancelable(false);
        this.f37pd.show();
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(JSONObject result) {
        super.onPostExecute(result);
        this.f37pd.dismiss();
        returnResult(this.f36jo);
    }

    private void returnResult(JSONObject response) {
        ResponseListerProfile mListener = (ResponseListerProfile) this.context;
        if (mListener != null) {
            mListener.on_ProfileSuccess(response);
        }
    }
}