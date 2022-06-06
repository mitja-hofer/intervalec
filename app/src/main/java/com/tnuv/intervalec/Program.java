package com.tnuv.intervalec;

import java.util.HashMap;

public class Program {
    public String name;
    public Interval[] intervals;
    public Program(String name, Interval[] intervals){
        this.name = name;
        this.intervals = intervals;
    }

    public HashMap<String, String> parseToHashMap(){
        // tmp hash map for single contact
        HashMap<String, String> programMap = new HashMap<String, String>();

        // adding each child node to HashMap key => value
        programMap.put("name", this.name);
        programMap.put("num_intervals", Integer.toString(this.intervals.length));
        return programMap;
    }
}
