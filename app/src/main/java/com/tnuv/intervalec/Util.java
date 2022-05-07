package com.tnuv.intervalec;

public class Util {
    public static String millisToMinSec(long millis) {
        long sec = millis / 1000;
        long min = (long) (sec / 60);
        sec = (long) sec % 60;
        if (sec < 10) {
            return min + ":0" + sec;
        } else {
            return min + ":" + sec;
        }
    }
}
