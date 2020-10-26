package com.example.backupplanclientcode.Utility;

import android.text.TextUtils;
import android.util.Patterns;
import java.util.regex.Pattern;

public class Validation {
    public boolean IsValidEmail(String email2) {
        return Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$").matcher(email2).matches();
    }

    public boolean IsValidPhoneNumber(String phone) {
        if (!TextUtils.isEmpty(phone)) {
            return Patterns.PHONE.matcher(phone).matches();
        }
        return false;
    }
}
