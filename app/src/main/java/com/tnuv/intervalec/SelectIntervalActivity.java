package com.tnuv.intervalec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SelectIntervalActivity extends AppCompatActivity {
    private ListView lv;
    List<Map<String, String>> intervalsMap = new ArrayList<Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_interval);
        //get all saved intervals
        // https://stackoverflow.com/questions/7145606/how-do-you-save-store-objects-in-sharedpreferences-on-android
        SharedPreferences prefs = getSharedPreferences("intervalec_intervals", Context.MODE_PRIVATE); // name should be unique across all apps
        // TODO: load from file
        // Interval[]  = prefs.getString(kljucStevec, "[]");
        /*
        Gson gson = new Gson();
        String json = prefs.getString("intervals", "");
        Interval[] intervals = gson.fromJson(json, Interval[].class);
         */
        Interval[] intervals = {
                new Interval("prvi", 5, 3, 5),
                new Interval("drugi", 3, 3, 3)
        };


        for (int i = 0; i < intervals.length; i++) {
            intervalsMap.add(intervals[i].parseToHashMap());
        }
        lv = findViewById(R.id.list);
        lv.setOnItemClickListener((adapterView, view, i, l) -> {
            startActiveIntervalActivity(view, intervals[i]);
        });
    }

    @Override
    protected void onStart(){
        super.onStart();

        SimpleAdapter adapter = new SimpleAdapter(this,
                intervalsMap,
                R.layout.list_item,
                new String[] {"name", "activeSeconds", "restSeconds", "reps"},
                new int[] {R.id.name, R.id.activeSeconds, R.id.restSeconds, R.id.reps}
        );

        lv.setAdapter(adapter);
    }

    public void startActiveIntervalActivity(View v, Interval interval) {
        Intent intent = new Intent(SelectIntervalActivity.this, ActiveIntervalActivity.class);
        intent.putExtra("activeSeconds", interval.activeSeconds);
        intent.putExtra("restSeconds", interval.restSeconds);
        intent.putExtra("reps", interval.reps);
        startActivity(intent);
    }

    public void startAddIntervalActivity(View v) {
        Intent intent = new Intent(SelectIntervalActivity.this, AddIntervalActivity.class);
        startActivity(intent);
    }

    public void finishSelectIntervalActivity(View v) {
        SelectIntervalActivity.this.finish();
    }
}