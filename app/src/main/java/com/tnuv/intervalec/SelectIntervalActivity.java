package com.tnuv.intervalec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SelectIntervalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_interval);
    }

    public void startActiveIntervalActivity(View v) {
        Intent intent = new Intent(SelectIntervalActivity.this, ActiveIntervalActivity.class);
        startActivity(intent);
    }

    public void finishSelectIntervalActivity(View v) {
        SelectIntervalActivity.this.finish();
    }
}