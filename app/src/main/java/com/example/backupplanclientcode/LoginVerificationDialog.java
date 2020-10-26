package com.example.backupplanclientcode;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.backupplanclientcode.Utility.PendingTask;
import com.example.backupplanclientcode.Utility.PendingTask;

public class LoginVerificationDialog extends DialogFragment {
    /* access modifiers changed from: private */
    public static PendingTask mPendingTask;
    private Button btnVerify;
    /* access modifiers changed from: private */
    public EditText etVerificationCode;

    public static LoginVerificationDialog newInstance(PendingTask pendingTask) {
        mPendingTask = pendingTask;
        return new LoginVerificationDialog();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.dialog_theme);
    }

    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            d.getWindow().setLayout(-1, -1);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_login_verification, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.etVerificationCode = (EditText) view.findViewById(R.id.txt_pin);
        this.btnVerify = (Button) view.findViewById(R.id.btn_verify);
        view.findViewById(R.id.tv_cancel).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                LoginVerificationDialog.this.getDialog().dismiss();
            }
        });
        this.btnVerify.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (LoginVerificationDialog.this.etVerificationCode.getText().toString().length() >= 4) {
                    LoginVerificationDialog.mPendingTask.onCompleteTask(LoginVerificationDialog.this.etVerificationCode.getText().toString());
                    LoginVerificationDialog.this.getDialog().dismiss();
                    return;
                }
                Toast.makeText(LoginVerificationDialog.this.getActivity(), "Please enter a valid Verification Code.", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}