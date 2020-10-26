package com.example.backupplanclientcode.Bugsense;

import android.content.Context;

import com.splunk.mint.Mint;

/* renamed from: com.backupplan.app.Bugsense.Bugsense */
public class Bugsense {
    String key = "aac26fa0";

    public void startBugsense(Context ctx) {
        Mint.initAndStartSession(ctx, this.key);
    }
}
