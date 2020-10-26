package com.example.backupplanclientcode.Notification;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.stats.GCoreWakefulBroadcastReceiver;

public class GcmBroadcastReceiver extends GCoreWakefulBroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        startWakefulService(context, intent.setComponent(new ComponentName(context.getPackageName(), GcmIntentService.class.getName())));
//        setResultCode(-1);
    }
}
