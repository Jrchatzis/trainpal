package com.example.john.travelinfo;

import android.app.Application;
import android.os.SystemClock;

public class SplashDelayActivity extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SystemClock.sleep(3000);
    }
}
