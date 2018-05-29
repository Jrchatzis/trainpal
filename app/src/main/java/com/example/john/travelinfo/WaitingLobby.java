package com.example.john.travelinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class WaitingLobby extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

    {
        mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        mTextMessage.setText("Home");
                        return true;
                    case R.id.navigation_preference:
                        mTextMessage.setText("Profile");
                        Intent goToPreferences = new Intent(WaitingLobby.this, MainActivity.class);
                        startActivity(goToPreferences);
                        return true;
                    case R.id.navigation_schedule:
                        mTextMessage.setText("Schedule");
                        return true;
                    case R.id.navigation_about:
                        mTextMessage.setText("About");
                        return true;
                }
                return false;
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_lobby);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Bundle recData = getIntent().getExtras();
        String selection = recData.getString("Time");

        TextView textView = (TextView)findViewById(R.id.selectedTime);
        textView.setText(selection);

        String transformedSelection = selection.replace(":","");
        int tranSelectionToUse = Integer.parseInt(transformedSelection);

        SimpleDateFormat realTime= new SimpleDateFormat("HH:mm", Locale.getDefault());
        String instantTime = realTime.format(new Date());
        String instantTimeNew=selection.replace(":","");

        TextView textViewTime = (TextView)findViewById(R.id.remainingTime);
        boolean interruption = true;

    }
}

