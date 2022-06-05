package com.tnuv.intervalec;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Bundle ;
import android.text.Editable;
import android.text.InputFilter ;
import android.view.View;
import android.widget.EditText ;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class AddProgramActivity extends AppCompatActivity {

    EditText programNameInput;
    int programIndex;
    Program program;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_program);

        programNameInput = findViewById(R.id.program_name_input);

    }

    public void addProgram(View v){
        String programName = String.valueOf(programNameInput.getText());
        SharedPreferences prefs = getSharedPreferences("intervalec", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("intervalec_programs", "[]");
        Program[] programs = gson.fromJson(json, Program[].class);

        List<Program> programsList = new ArrayList<Program>(Arrays.asList(programs));
        Interval[] emptyInterval = new Interval[0];
        Program newProgram = new Program(programName,emptyInterval);
        programsList.add(newProgram);
        Program[] newPrograms = new Program[ programsList.size() ];
        programsList.toArray( newPrograms );
        SharedPreferences.Editor prefsEditor = prefs.edit();
        String programJson = gson.toJson(newPrograms);
        prefsEditor.putString("intervalec_programs", programJson);
        prefsEditor.apply();

        Intent intent = new Intent(AddProgramActivity.this, SelectProgramActivity.class);
        startActivity(intent);
    }

    public void clearProgram(View v){
        programNameInput.setText("");
    }

    public void finishAddProgramActivity(View v) {
        AddProgramActivity.this.finish();
    }
}