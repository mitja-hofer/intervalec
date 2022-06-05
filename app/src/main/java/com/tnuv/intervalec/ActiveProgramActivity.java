package com.tnuv.intervalec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import java.util.Locale;


public class ActiveProgramActivity extends AppCompatActivity {

    long millisLeft;
    TextView mTextView;
    CountDownTimer timer;
    int repsLeft, programIndex, intervalIndex = 0;
    Program program;
    Interval interval;
    boolean isActive;
    TextToSpeech tts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_interval);
        Button btn = (Button) findViewById(R.id.btn_toggle_timer);
        btn.setText(R.string.btn_pause_timer);
        btn.setOnClickListener(v -> pauseTimer(v));

        Intent intent = getIntent();
        programIndex = intent.getIntExtra("programIndex", 0);
        SharedPreferences prefs = getSharedPreferences("intervalec", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("intervalec_programs", "[]");
        Program[] programs = gson.fromJson(json, Program[].class);
        program = programs[programIndex];
        ((TextView) findViewById(R.id.program_name)).setText(program.name);

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                }
                if(i == TextToSpeech.SUCCESS) {
                    startInterval();
                }
            }
        });
        if (Build.FINGERPRINT.contains("generic")) {
            startInterval();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
        }
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    public void startSelectIntervalActivity(View v) {
        Intent intent = new Intent(ActiveProgramActivity.this, SelectIntervalActivity.class);
        startActivity(intent);
    }

    public void startInterval() {
        TextView mTextView = findViewById(R.id.timer_text);
        interval = program.intervals[intervalIndex];
        repsLeft = interval.reps;
        mTextView.setText(Util.millisToMinSec(interval.activeSeconds*1000));
        ((TextView) findViewById(R.id.active_active_status)).setText(Util.millisToMinSec(interval.activeSeconds*1000));
        ((TextView) findViewById(R.id.active_rest_status)).setText(Util.millisToMinSec(interval.restSeconds*1000));
        ((TextView) findViewById(R.id.active_reps_status)).setText(Integer.toString(interval.reps));
        ((TextView) findViewById(R.id.interval_name)).setText(interval.name);
        startActive();
    }

    private void startActive() {
        isActive = true;
        View v = findViewById(R.id.active_main);
        v.setBackgroundColor(Color.GREEN);
        if (repsLeft == interval.reps) {
            tts.speak(interval.name, TextToSpeech.QUEUE_FLUSH, null);
        } else {
            tts.speak("active", TextToSpeech.QUEUE_FLUSH, null);
        }
        startTimer(interval.activeSeconds * 1000);
    }

    private void startRest(){
        isActive = false;
        View v = findViewById(R.id.active_main);
        v.setBackgroundColor(Color.RED);
        tts.speak("rest", TextToSpeech.QUEUE_FLUSH, null);
        startTimer(interval.restSeconds * 1000);
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
                        startRest();
                    } else {
                        repsLeft--;
                        ((TextView) findViewById(R.id.active_reps_status)).setText(Integer.toString(repsLeft));
                        startActive();
                    }
                } else {
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
