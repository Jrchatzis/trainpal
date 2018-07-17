package com.example.john.travelinfo;

import android.app.Application;
import android.os.SystemClock;

//Delay starting screen for 3 seconds
public class SplashDelayActivity extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SystemClock.sleep(3000);
    }
}
