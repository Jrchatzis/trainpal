package com.example.john.travelinfo;



import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.Menu;
import android.widget.ListView;

import java.util.List;


public class Timer extends AppCompatActivity {

    ListView listView;

    String[] mobileArray = {"Android", "IPhone", "WindowsMobile", "Blackberry",
            "WebOS", "Ubuntu", "Windows7", "Max OS X"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        //Back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_timer, mobileArray);

        ListView listView = (ListView) findViewById(R.id.Schedules);
        listView.setAdapter(adapter);
    }
}


