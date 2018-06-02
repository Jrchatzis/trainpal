package com.example.john.travelinfo;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelinfo.ldbws.DAAAccessToken;
import com.example.travelinfo.ldbws.DAAEnums;
import com.example.travelinfo.ldbws.DAALDBServiceSoap12;
import com.example.travelinfo.ldbws.DAAServiceDetails;
import com.example.travelinfo.ldbws.DAAStationBoardWithDetails_2;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.stream.Collectors;

public class WaitingLobbyActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private TrainService selectedTrain;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    public static final String ACCESS_TOKEN = "b57a223e-9ab3-4a91-977d-7071e0434a16";

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
                        Intent goToPreferences = new Intent(WaitingLobbyActivity.this, MainActivity.class);
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

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Bundle bundle = getIntent().getExtras();
        selectedTrain = TrainService.class.cast(bundle.get("selectedTrain"));
        //TODO:Giving value to text
        TextView sta = findViewById(R.id.selectedTime);
        sta.setText(selectedTrain.getSta().toString());

        Chronometer countdown = findViewById(R.id.countdown);
        countdown.setCountDown(true);
        countdown.setBase(SystemClock.elapsedRealtime() + millisUntilArrival());
        countdown.start();

        //trial
        TextView eta = findViewById(R.id.estimatedTime);
        eta.setText(selectedTrain.getEta().toString());

    }

    protected long millisUntilArrival() {
        return ChronoUnit.MILLIS.between(LocalTime.now(), selectedTrain.getSta().getTime());
    }

    public String getEstimatedTime(TrainService estimatedTime){
        DAALDBServiceSoap12 soapClient = new DAALDBServiceSoap12();
        DAAAccessToken accessToken = new DAAAccessToken();
        accessToken.TokenValue = ACCESS_TOKEN;
        String serviceId = trainService.getId();
        try {
            DAAServiceDetails soapResponse = soapClient.GetServiceDetails(serviceId, accessToken);
            if (soapResponse.trainServices == null) {
                return null;
            }

            return ;

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }


    }
}

