package com.example.backupplanclientcode;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.example.backupplanclientcode.HttpLoader.HttpLoader;

public class TextViewerActivity extends Activity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_viewer);
        ((TextView) findViewById(R.id.tv)).setText(getIntent().getStringExtra(HttpLoader.FILE_TYPE_TEXT));
    }
}
