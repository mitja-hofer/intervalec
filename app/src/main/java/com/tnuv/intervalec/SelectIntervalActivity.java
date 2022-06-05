package com.tnuv.intervalec;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SelectIntervalActivity extends AppCompatActivity {
    private ListView lv;
    int programIndex;
    Program program;
    Button btn_start;
    Toast noIntervals;
    List<Map<String, String>> intervalsMap = new ArrayList<Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_interval);

        Intent intent = getIntent();
        programIndex = intent.getIntExtra("programIndex", 0);
        SharedPreferences prefs = getSharedPreferences("intervalec", Context.MODE_PRIVATE); // name should be unique across all apps
        Gson gson = new Gson();
        String json = prefs.getString("intervalec_programs", "[]");
        Program[] programs = gson.fromJson(json, Program[].class);
        program = programs[programIndex];
        ((TextView) findViewById(R.id.interval_list_program_name)).setText(program.name);
        btn_start = findViewById(R.id.btn_start_interval);
        noIntervals = Toast.makeText(getApplicationContext(),"Please add intervals.",Toast.LENGTH_SHORT);

        //get all saved intervals
        // https://stackoverflow.com/questions/7145606/how-do-you-save-store-objects-in-sharedpreferences-on-android

        /*
        if (!prefs.contains("intervalec_intervals")) {
            Interval[] defaultInterval = {new Interval("prvi", 5, 3, 5)};
            SharedPreferences.Editor prefsEditor = prefs.edit();
            String intervalJson = gson.toJson(defaultInterval);
            prefsEditor.putString("intervalec_intervals", intervalJson);
            prefsEditor.commit();
        }
         */

        /*
        // TODO: remove example of adding a new interval
        if (intervals.length == 1) {
            List<Interval> intervalsList = new ArrayList<Interval>(Arrays.asList(intervals));
            Interval newInterval = new Interval("drugi", 3, 3, 3);
            intervalsList.add(newInterval);
            Interval[] newIntervals = new Interval[ intervalsList.size() ];
            intervalsList.toArray( newIntervals );
            SharedPreferences.Editor prefsEditor = prefs.edit();
            String intervalJson = gson.toJson(newIntervals);
            prefsEditor.putString("intervalec_intervals", intervalJson);
            prefsEditor.commit();

            json = prefs.getString("intervalec_intervals", "[]");
            intervals = gson.fromJson(json, Interval[].class);
        }
        */


        for (int i = 0; i < program.intervals.length; i++) {
            intervalsMap.add(program.intervals[i].parseToHashMap());
        }
        lv = findViewById(R.id.interval_list);
        /*
        lv.setOnItemClickListener((adapterView, view, i, l) -> editIntervalActivity(view, programIndex));
         */
    }

    @Override
    protected void onStart(){
        super.onStart();

        SimpleAdapter adapter = new SimpleAdapter(this,
                intervalsMap,
                R.layout.interval_list_item,
                new String[] {"name", "activeSeconds", "restSeconds", "reps"},
                new int[] {R.id.interval_list_name, R.id.activeSeconds, R.id.restSeconds, R.id.reps}
        );

        lv.setAdapter(adapter);
    }

    public void startActiveProgramActivity(View v) {
        if (program.intervals.length == 0){
            btn_start.setVisibility(View.INVISIBLE);
            noIntervals.show();
        }else {
            Intent intent = new Intent(SelectIntervalActivity.this, ActiveProgramActivity.class);
            intent.putExtra("programIndex", programIndex);
            startActivity(intent);
        }
    }

    public void startAddIntervalActivity(View v) {
        Intent intent = new Intent(SelectIntervalActivity.this, AddIntervalActivity.class);
        intent.putExtra("programIndex", programIndex);
        startActivity(intent);
    }

    public void finishSelectIntervalActivity(View v) {
        SelectIntervalActivity.this.finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SelectIntervalActivity.this, SelectProgramActivity.class);
        startActivity(intent);
    }

}