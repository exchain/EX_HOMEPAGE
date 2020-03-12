package com.finger.tools.util;

public class RunningTime
{
    private String comment = "";
    private long startTime = 0;
    private long stopTime = 0;

    public RunningTime(String arg) {
        startTime = 0;
        stopTime = 0;
        if (arg == null || "".equals(arg)) comment = "";
        else comment = "["+ arg +"]";
    }

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public void stop() {
        stopTime = System.currentTimeMillis();
    }

    public String toPrinting() {
        return (comment +" Running time (millis) = "+ (stopTime - startTime));
    }

}