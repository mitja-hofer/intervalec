package com.tnuv.intervalec;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;


public class ActiveIntervalActivity extends AppCompatActivity {

    long millisLeft;
    TextView mTextView;
    CountDownTimer timer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_interval);
    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }

    public void startSelectIntervalActivity(View v) {
        Intent intent = new Intent(ActiveIntervalActivity.this, SelectIntervalActivity.class);
        startActivity(intent);
    }

    public void startCountdown(View v) {
        timerStart(30000);
    }

    private void timerStart(long millis) {
        TextView mTextView = findViewById(R.id.timer_text);
        if (timer != null) {
            timer.cancel();
        }
        timer = new CountDownTimer(millis, 1000) {
            public void onTick(long millisUntilFinished) {
                millisLeft = millisUntilFinished;
                mTextView.setText(millisToMinSec(millisUntilFinished));
            }
            public void onFinish() {
                mTextView.setText("done!");
            }
        };
        timer.start();
    }

    public void pauseTimer(View v) {
        timer.cancel();
    }

    public void resumeTimer(View v) {
        timerStart(millisLeft);
    }

    private String millisToMinSec(long millis) {
        long sec = millis / 1000;
        long min = (long) (sec / 60);
        sec = (long) sec % 60;
        return min + ":" + sec;
    }

}
