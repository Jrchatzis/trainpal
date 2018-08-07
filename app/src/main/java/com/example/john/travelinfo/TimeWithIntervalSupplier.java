package com.example.john.travelinfo;

import java.time.LocalTime;
import java.util.function.Supplier;

/**
 *
 */
//Add a standard time to local time of the device
public class TimeWithIntervalSupplier implements Supplier<TimeString> {

    private TimeString next = new TimeString(LocalTime.now());
    private long interval;

    TimeWithIntervalSupplier(long interval) {
        this.interval = interval;
    }

    @Override
    public TimeString get() {
        TimeString result = next;
        next = new TimeString(next.getTime().plusMinutes(interval));
        return result;
    }

}
