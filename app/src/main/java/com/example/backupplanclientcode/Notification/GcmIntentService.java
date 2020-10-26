package com.example.backupplanclientcode.Notification;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.internal.view.SupportMenu;

import com.example.backupplanclientcode.Constant.Constant;
import com.example.backupplanclientcode.MenuListActivity;
import com.example.backupplanclientcode.R;
import com.example.backupplanclientcode.loginActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.plus.PlusShare;
import java.util.Random;

public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    public static final String TAG = "GCM Demo";
    static int count = 1;
    Notification.Builder builder;
    Context context;
    private NotificationManager mNotificationManager;

    public GcmIntentService() {
        super("PUSH NOTIFICATION");
    }

    /* access modifiers changed from: protected */
    public void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        String msg = intent.getStringExtra("message");
        String messageType = GoogleCloudMessaging.getInstance(this).getMessageType(intent);
        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString(), "");
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " + extras.toString(), "");
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                for (int i = 0; i < 5; i++) {
                    Log.i(TAG, "Working... " + (i + 1) + "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                }
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                sendNotification(msg, intent.getStringExtra("push_title"));
            }
            GcmBroadcastReceiver.completeWakefulIntent(getApplicationContext(), intent);
        }
    }

    private void sendNotification(String msg, String title) {
        PendingIntent contentIntent;
        this.mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        SharedPreferences setting = getSharedPreferences(Constant.PrefName, 0);
        if (setting.getBoolean(Constant.isLogin, false) || setting.getBoolean(Constant.isGuestLogin, false)) {
            Intent myintent = new Intent(this, MenuListActivity.class);
            myintent.putExtra("message", msg);
            myintent.putExtra(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_TITLE, title);
            myintent.setAction("android.intent.action.MAIN");
            contentIntent = PendingIntent.getActivity(this, 0, myintent, PendingIntent.FLAG_ONE_SHOT);
        } else {
            Intent myintent2 = new Intent(this, loginActivity.class);
            myintent2.putExtra("message", msg);
            myintent2.putExtra(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_TITLE, title);
            myintent2.setAction("android.intent.action.MAIN");
            contentIntent = PendingIntent.getActivity(this, 0, myintent2, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        Notification.Builder mBuilder = new Notification.Builder(this).setSmallIcon(R.drawable.ic_launcher).setContentTitle(getResources().getString(R.string.app_name)).setStyle(new Notification.BigTextStyle().bigText(msg)).setContentText(msg);
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(19);
        mBuilder.setLights(SupportMenu.CATEGORY_MASK, 3000, 3000);
        mBuilder.setAutoCancel(true);
        this.mNotificationManager.notify(new Random().nextInt(8999) + 1000, mBuilder.build());
    }
}