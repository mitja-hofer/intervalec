package com.tnuv.intervalec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SelectProgramActivity extends AppCompatActivity {
    private ListView lv;
    List<Map<String, String>> programsMap = new ArrayList<Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_program);
        //get all saved intervals
        // https://stackoverflow.com/questions/7145606/how-do-you-save-store-objects-in-sharedpreferences-on-android

        SharedPreferences prefs = getSharedPreferences("intervalec", Context.MODE_PRIVATE); // name should be unique across all apps
        Gson gson = new Gson();

        if (!prefs.contains("intervalec_programs")) {
            Interval[] defaultIntervals = {new Interval("prvi", 5, 3, 5)};
            Program[] defaultPrograms = {new Program("program", defaultIntervals)};
            SharedPreferences.Editor prefsEditor = prefs.edit();
            String programJson = gson.toJson(defaultPrograms);
            prefsEditor.putString("intervalec_programs", programJson);
            prefsEditor.commit();
        }

        String json = prefs.getString("intervalec_programs", "[]");
        Program[] programs = gson.fromJson(json, Program[].class);

        // TODO: remove example of adding a new interval/program
        if (programs.length == 2) {
            List<Program> programsList = new ArrayList<Program>(Arrays.asList(programs));
            Interval[] newIntervals = {
                    new Interval("warm up", 3, 0, 0),
                    new Interval("run", 3, 3, 3),
            };
            Program newProgram = new Program("run", newIntervals);
            programsList.add(newProgram);
            Program[] newPrograms = new Program[ programsList.size() ];
            programsList.toArray( newPrograms );
            SharedPreferences.Editor prefsEditor = prefs.edit();
            String programJson = gson.toJson(newPrograms);
            prefsEditor.putString("intervalec_programs", programJson);
            prefsEditor.commit();

            json = prefs.getString("intervalec_programs", "[]");
            programs = gson.fromJson(json, Program[].class);
        }


        for (int i = 0; i < programs.length; i++) {
            programsMap.add(programs[i].parseToHashMap());
        }
        lv = findViewById(R.id.program_list);
        lv.setOnItemClickListener((adapterView, view, i, l) -> startSelectIntervalActivity(view, i));
    }

    @Override
    protected void onStart(){
        super.onStart();

        SimpleAdapter adapter = new SimpleAdapter(this,
                programsMap,
                R.layout.program_list_item,
                new String[] {"name", "num_intervals"},
                new int[] {R.id.program_list_name, R.id.num_intervals}
        );

        lv.setAdapter(adapter);
    }

    public void startSelectIntervalActivity(View v, int i) {
        Intent intent = new Intent(SelectProgramActivity.this, SelectIntervalActivity.class);
        intent.putExtra("programIndex", i);
        startActivity(intent);
    }

    public void startAddProgramActivity(View v) {
        Intent intent = new Intent(SelectProgramActivity.this, AddProgramActivity.class);
        startActivity(intent);
    }

    public void finishSelectProgramActivity(View v) {
        SelectProgramActivity.this.finish();
    }
}