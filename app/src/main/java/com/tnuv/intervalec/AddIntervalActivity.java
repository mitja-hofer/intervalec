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
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Locale;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import androidx.appcompat.app.AppCompatActivity;

public class AddIntervalActivity extends AppCompatActivity {
    // https://stackoverflow.com/questions/7145606/how-do-you-save-store-objects-in-sharedpreferences-on-android
    // see example of adding new interval in SelectIntervalActivity
    EditText actTimeInputMinutes, actTimeInputSeconds, restTimeInputMinutes,restTimeInputSeconds, inputIntervalName, repsInput;
    Program program;
    int programIndex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_interval);

        Intent intent = getIntent();
        programIndex = intent.getIntExtra("programIndex", 0);

        actTimeInputMinutes = findViewById(R.id. active_time_input_minutes ) ;
        actTimeInputSeconds = findViewById(R.id. active_time_input_seconds ) ;
        restTimeInputMinutes = findViewById(R.id. rest_time_input_minutes ) ;
        restTimeInputSeconds = findViewById(R.id. rest_time_input_seconds ) ;
        inputIntervalName = findViewById(R.id.interval_name_input);
        repsInput = findViewById(R.id.reps_input);

        actTimeInputMinutes.setFilters( new InputFilter[]{ new MinMaxFilter( "0" , "120" )}) ;
        actTimeInputSeconds.setFilters( new InputFilter[]{ new MinMaxFilter( "0" , "59" )}) ;
        restTimeInputMinutes.setFilters( new InputFilter[]{ new MinMaxFilter( "0" , "120" )}) ;
        restTimeInputSeconds.setFilters( new InputFilter[]{ new MinMaxFilter( "0" , "59" )}) ;


        SharedPreferences prefs = getSharedPreferences("intervalec", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("intervalec_programs", "[]");
        Program[] programs = gson.fromJson(json, Program[].class);
        program = programs[programIndex];
        ((TextView) findViewById(R.id.program_name)).setText(program.name);

    }

    public void finishAddIntervalActivity(View v) {
        AddIntervalActivity.this.finish();
    }

    public void clearInterval(View v){
        actTimeInputMinutes.setText("");
        actTimeInputSeconds.setText("");
        restTimeInputMinutes.setText("");
        restTimeInputSeconds.setText("");
        repsInput.setText("");
        inputIntervalName.setText("");
    }

    public void addInterval(View v){

        int actInputMinutes;
        int actInputSeconds;
        int rstInputMinutes;
        int rstInputSeconds;
        int activeSeconds;
        int restSeconds;
        int reps;

        if (actTimeInputMinutes.getText().toString().equals("")){
            actInputMinutes = 0;
        }else{
            actInputMinutes = Integer.parseInt(actTimeInputMinutes.getText().toString());
        }

        if (actTimeInputSeconds.getText().toString().equals("")){
            actInputSeconds = 0;
        }else{
            actInputSeconds = Integer.parseInt(actTimeInputSeconds.getText().toString());
        }

        if (restTimeInputMinutes.getText().toString().equals("")){
            rstInputMinutes = 0;
        }else{
            rstInputMinutes = Integer.parseInt(restTimeInputMinutes.getText().toString());
        }

        if (restTimeInputSeconds.getText().toString().equals("")){
            rstInputSeconds = 0;
        }else{
            rstInputSeconds = Integer.parseInt(restTimeInputSeconds.getText().toString());
        }

        if (repsInput.getText().toString().equals("")){
            reps = 0;
        }else{
            reps = Integer.parseInt(repsInput.getText().toString());
        }


        String intervalName =  inputIntervalName.getText().toString();

        activeSeconds = actInputMinutes*60 + actInputSeconds;
        restSeconds = rstInputMinutes*60 + rstInputSeconds;

        Interval newInterval = new Interval(intervalName,activeSeconds,restSeconds,reps);

        Gson gson = new Gson();
        SharedPreferences prefs = getSharedPreferences("intervalec", Context.MODE_PRIVATE);
        String json = prefs.getString("intervalec_programs", "[]");
        Program[] programs = gson.fromJson(json, Program[].class);

        List<Program> programsList = new ArrayList<>(Arrays.asList(programs));
        Program currentProgram = programsList.get(programIndex);

        List<Interval> intervalsList = new ArrayList<>(Arrays.asList(currentProgram.intervals));
        intervalsList.add(newInterval);

        Interval[] newIntervalList = new Interval[intervalsList.size()];
        intervalsList.toArray( newIntervalList );

        currentProgram.intervals = newIntervalList;
        programsList.add(currentProgram);

        Program[] updatedProgramsList = new Program[programsList.size()];
        programsList.toArray(updatedProgramsList);

        SharedPreferences.Editor prefsEditor = prefs.edit();
        String updatedProgramJson = gson.toJson(updatedProgramsList);
        prefsEditor.putString("intervalec_programs", updatedProgramJson);
        prefsEditor.apply();

        Intent intent = new Intent(AddIntervalActivity.this, SelectIntervalActivity.class);
        intent.putExtra("programIndex", programIndex);
        startActivity(intent);

    }

}
