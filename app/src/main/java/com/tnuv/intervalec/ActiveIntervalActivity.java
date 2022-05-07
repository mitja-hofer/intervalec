package com.tnuv.intervalec;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ActiveIntervalActivity extends AppCompatActivity {

    long millisLeft;
    TextView mTextView;
    CountDownTimer timer;
    int activeSeconds, restSeconds, reps, repsLeft;
    boolean isActive;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_interval);
        Button btn = (Button) findViewById(R.id.btn_toggle_timer);
        TextView mTextView = findViewById(R.id.timer_text);
        btn.setText(R.string.btn_pause_timer);
        btn.setOnClickListener(v -> pauseTimer(v));
        Intent intent = getIntent();
        activeSeconds = intent.getIntExtra("activeSeconds", 0);
        restSeconds = intent.getIntExtra("restSeconds", 0);
        reps = intent.getIntExtra("reps", 0);
        mTextView.setText(Util.millisToMinSec(activeSeconds*1000));
        ((TextView) findViewById(R.id.active_active_status)).setText(Util.millisToMinSec(activeSeconds*1000));
        ((TextView) findViewById(R.id.active_rest_status)).setText(Util.millisToMinSec(restSeconds*1000));
        ((TextView) findViewById(R.id.active_reps_status)).setText(Integer.toString(reps));

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timer != null) {
            timer.cancel();
        }
    }

    public void startSelectIntervalActivity(View v) {
        Intent intent = new Intent(ActiveIntervalActivity.this, SelectIntervalActivity.class);
        startActivity(intent);
    }

    public void startInterval(View v) {
        repsLeft = reps;
        startActive();
    }

    private void startActive() {
        isActive = true;
        View v = findViewById(R.id.active_main);
        v.setBackgroundColor(Color.GREEN);
        startTimer(activeSeconds * 1000);
    }

    private void startRest(){
        isActive = false;
        View v = findViewById(R.id.active_main);
        v.setBackgroundColor(Color.RED);
        startTimer(restSeconds * 1000);
    }


    private void startTimer(long millis) {
        TextView mTextView = findViewById(R.id.timer_text);
        if (timer != null) {
            timer.cancel();
        }
        timer = new CountDownTimer(millis, 1000) {
            public void onTick(long millisUntilFinished) {
                millisLeft = millisUntilFinished;
                mTextView.setText(Util.millisToMinSec(millisUntilFinished));
            }
            public void onFinish() {
                if (repsLeft > 0) {
                    if (isActive) {
                        //TODO: play sound
                        startRest();
                    } else {
                        repsLeft--;
                        ((TextView) findViewById(R.id.active_reps_status)).setText(Integer.toString(repsLeft));
                        //TODO: play sound
                        startActive();
                    }
                } else {
                    //TODO: play sound
                    mTextView.setText("done!");
                }
            }
        };
        timer.start();
    }

    public void pauseTimer(View v) {
        timer.cancel();
        Button btn = (Button) v;
        btn.setText(R.string.btn_resume_timer);
        btn.setOnClickListener(vv -> resumeTimer(vv));
    }

    public void resumeTimer(View v) {
        startTimer(millisLeft);
        Button btn = (Button) v;
        btn.setText(R.string.btn_pause_timer);
        btn.setOnClickListener(vv -> pauseTimer(vv));
    }


}
