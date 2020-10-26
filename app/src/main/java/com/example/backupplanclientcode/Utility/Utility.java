package com.example.backupplanclientcode.Utility;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import com.example.backupplanclientcode.Constant.Constant;
import com.example.backupplanclientcode.Preference.SettingPreference;

import java.io.ByteArrayOutputStream;

/* renamed from: com.backupplan.app.Utility.Utility */
public class Utility {
    public static boolean isSubscriptionValid(SettingPreference pref) {
        if (!isFreeSubscription(pref) && !isSubscriptionExpired(pref)) {
            return true;
        }
        return false;
    }

    public static boolean isFreeSubscription(SettingPreference pref) {
        return pref.getStringValue(Constant.subscription, "free").equals("free");
    }

    public static boolean isSubscriptionExpired(SettingPreference pref) {
        return pref.getBooleanValue(Constant.subscription_expired, true);
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    public static Bitmap getBitmapFromPath(String photoPath) {
        Options options = new Options();
        options.inPreferredConfig = Config.ARGB_8888;
        return BitmapFactory.decodeFile(photoPath, options);
    }
}
