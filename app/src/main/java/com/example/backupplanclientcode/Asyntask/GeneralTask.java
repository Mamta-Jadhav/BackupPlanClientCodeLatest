package com.example.backupplanclientcode.Asyntask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.backupplanclientcode.Constant.Constant;
import com.example.backupplanclientcode.Preference.SettingPreference;
import com.example.backupplanclientcode.R;
import com.google.api.client.json.Json;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class GeneralTask extends AsyncTask<Void, Void, JSONObject> {
    Activity context;
    int flag;

    /* renamed from: jo */
    JSONObject f34jo;
    List<NameValuePair> nameValuePair;
    JSONObject nameValuePairjson;
    /* renamed from: pd */
    ProgressDialog f35pd;
    SettingPreference pref;
    int responseCode;
    String url, method;

    public interface ResponseListener_General {
        void on_GeneralSuccess(JSONObject jSONObject, int i);
    }

    public GeneralTask(Activity mainActivity, String api, List<NameValuePair> nameValuePair2, int responseCode2) {
        this.context = mainActivity;
        this.url = api;
        this.nameValuePair = nameValuePair2;
        this.responseCode = responseCode2;
    }

    public GeneralTask(Activity mainActivity, String api, JSONObject nameValuePair2, int responseCode2, String method) {
        this.context = mainActivity;
        this.url = api;
        this.nameValuePairjson = nameValuePair2;
        this.responseCode = responseCode2;
        this.method = method;
        this.pref = new SettingPreference(mainActivity);
    }

    /* access modifiers changed from: protected */
    public JSONObject doInBackground(Void... params) {
        String str = "";
        try {

            if (method.equalsIgnoreCase("posta")) {
                HttpClient myClient = new DefaultHttpClient();
                HttpPost httpost = new HttpPost(this.url);

//                httpost.setHeader("Postman-Token",
//                        "<calculated when request is sent>");
//                httpost.setHeader("Content-Type",
//                        "multipart/form-data; boundary=<calculated when request is sent>");
////            httpost.setHeader("Content-Length",
////                    "<calculated when request is sent>");
//                httpost.setHeader("Host",
//                        "<calculated when request is sent>");
//                httpost.setHeader(HTTP.USER_AGENT,
//                        "PostmanRuntime/7.26.5");
//                httpost.setHeader("Accept",
//                        "*/*");
//                httpost.setHeader("Accept-Encoding",
//                        "gzip, deflate, br");
//                httpost.setHeader(HTTP.CONN_DIRECTIVE,
//                        "keep-alive");
                httpost.setHeader(HTTP.CONTENT_TYPE,
                        "multipart/form-data");
                httpost.setHeader("Accept",
                        "application/json");
                httpost.setHeader("Authorization",
                        "bearer "+this.pref.getStringValue(Constant.jwttoken, ""));

//                String json = new Gson().toJson(this.nameValuePairjson);
//            Gson gson = new GsonBuilder().create();
//            JsonArray myCustomArray = gson.toJsonTree(this.nameValuePairjson).getAsJsonArray();
//            JsonElement jsonObject = new JsonParser().parse(String.valueOf(myCustomArray));

                httpost.setEntity(new StringEntity(this.nameValuePairjson.toString(), HTTP.UTF_8));

                HttpResponse response = myClient.execute(httpost);
                this.f34jo = new JSONObject(EntityUtils.toString(response.getEntity()));
            } else  if (method.equalsIgnoreCase("post")) {
                HttpClient myClient = new DefaultHttpClient();
                HttpPost myConnection = new HttpPost(this.url);

                myConnection.setHeader(HTTP.CONTENT_TYPE,
                        "application/json");
                myConnection.setHeader("Accept",
                        "application/json");

//                String json = new Gson().toJson(this.nameValuePairjson);
//            Gson gson = new GsonBuilder().create();
//            JsonArray myCustomArray = gson.toJsonTree(this.nameValuePairjson).getAsJsonArray();
//            JsonElement jsonObject = new JsonParser().parse(String.valueOf(myCustomArray));

                myConnection.setEntity(new StringEntity(this.nameValuePairjson.toString(), HTTP.UTF_8));

                HttpResponse response = myClient.execute(myConnection);
                this.f34jo = new JSONObject(EntityUtils.toString(response.getEntity()));
            } else {

                HttpClient myClient = new DefaultHttpClient();

                HttpGet request = new HttpGet();
                URI website = new URI(this.url+"?token="+this.pref.getStringValue(Constant.jwttoken, ""));
                request.setURI(website);
                request.addHeader(HTTP.CONTENT_TYPE,
                        "application/json");
                request.addHeader("Accept",
                        "application/json");
//                request.addHeader("token",
//                        this.pref.getStringValue(Constant.jwttoken, ""));

                HttpResponse response = myClient.execute(request);
                this.f34jo = new JSONObject(EntityUtils.toString(response.getEntity()));
            }

//            this.f34jo = new JSONObject(String.valueOf(myClient.execute(myConnection)));
            Log.e("General Category.", "General Task");
        } catch (Exception e) {
            Log.d("test", e.getMessage());
        }
        return this.f34jo;
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"InlinedApi"})
    public void onPreExecute() {
        super.onPreExecute();
        this.f35pd = new ProgressDialog(this.context, 3);
        this.f35pd.setMessage(this.context.getResources().getString(R.string.txt_loading));
        this.f35pd.setCancelable(false);
        this.f35pd.show();
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(JSONObject result) {
        super.onPostExecute(result);
        try {
            if (this.f35pd.isShowing()) {
                this.f35pd.dismiss();
            }
            returnResult(this.f34jo, this.responseCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void returnResult(JSONObject response, int responseCode2) {
        ResponseListener_General mListener = (ResponseListener_General) this.context;
        if (mListener != null) {
            Log.d("Request", url);
            Log.d("Response", response.toString());
            mListener.on_GeneralSuccess(response, this.responseCode);
        }
    }
}