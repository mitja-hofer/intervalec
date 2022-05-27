package com.tnuv.intervalec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;


public class ActiveProgramActivity extends AppCompatActivity {

    long millisLeft;
    TextView mTextView;
    CountDownTimer timer;
    int activeSeconds, restSeconds, reps, repsLeft, programIndex, intervalIndex = 0;
    Program program;
    boolean isActive;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_interval);
        Button btn = (Button) findViewById(R.id.btn_toggle_timer);
        btn.setText(R.string.btn_pause_timer);
        btn.setOnClickListener(v -> pauseTimer(v));

        Intent intent = getIntent();
        programIndex = intent.getIntExtra("programIndex", 0);
        SharedPreferences prefs = getSharedPreferences("intervalec", Context.MODE_PRIVATE); // name should be unique across all apps
        Gson gson = new Gson();
        String json = prefs.getString("intervalec_programs", "[]");
        Program[] programs = gson.fromJson(json, Program[].class);
        program = programs[programIndex];
        ((TextView) findViewById(R.id.program_name)).setText(program.name);
        startInterval();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timer != null) {
            timer.cancel();
        }
    }

    public void startSelectIntervalActivity(View v) {
        Intent intent = new Intent(ActiveProgramActivity.this, SelectIntervalActivity.class);
        startActivity(intent);
    }

    public void startInterval() {
        TextView mTextView = findViewById(R.id.timer_text);
        Interval interval = program.intervals[intervalIndex];
        activeSeconds = interval.activeSeconds;
        restSeconds = interval.restSeconds;
        reps = interval.reps;
        repsLeft = reps;
        mTextView.setText(Util.millisToMinSec(activeSeconds*1000));
        ((TextView) findViewById(R.id.active_active_status)).setText(Util.millisToMinSec(activeSeconds*1000));
        ((TextView) findViewById(R.id.active_rest_status)).setText(Util.millisToMinSec(restSeconds*1000));
        ((TextView) findViewById(R.id.active_reps_status)).setText(Integer.toString(reps));
        ((TextView) findViewById(R.id.interval_name)).setText(interval.name);
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
                    intervalIndex++;
                    if (intervalIndex > program.intervals.length){
                        mTextView.setText("done!");
                    } else {
                        startInterval();
                    }
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
