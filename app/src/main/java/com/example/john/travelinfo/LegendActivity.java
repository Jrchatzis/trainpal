package com.example.john.travelinfo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LegendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legend);

        //Set action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    //Go back
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
