package com.example.john.travelinfo;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class TimeString implements Serializable {

    private LocalTime time;
    private static DateTimeFormatter formatter() {
        return DateTimeFormatter.ofPattern("HH:mm");
    }

    TimeString(String time) {
        this.time = LocalTime.parse(time, formatter());
    }

    TimeString(LocalTime time) {
        this.time = time;
    }

    public LocalTime getTime() {
        return time;
    }

    @Override
    public String toString() {
        return time.format(formatter());
    }
}
