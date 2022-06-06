package com.tnuv.intervalec;

import java.util.HashMap;

public class Interval {
    public String name;
    public int reps, activeSeconds, restSeconds;

    public Interval(String name, int activeSeconds, int restSeconds, int reps) {
        this.name = name;
        this.activeSeconds = activeSeconds;
        this.restSeconds = restSeconds;
        this.reps = reps;
    }

    public HashMap<String, String> parseToHashMap(){
        // tmp hash map for single contact
        HashMap<String, String> intervalMap = new HashMap<String, String>();

        // adding each child node to HashMap key => value
        intervalMap.put("name", this.name);
        intervalMap.put("activeSeconds", "Active: " + Util.millisToMinSec(this.activeSeconds * 1000));
        intervalMap.put("restSeconds", "Rest: " + Util.millisToMinSec(this.restSeconds * 1000));
        intervalMap.put("reps", "Reps: " + Integer.toString(this.reps));
        return intervalMap;
    }
}

