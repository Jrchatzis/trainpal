package com.example.john.travelinfo;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.travelinfo.ldbws.*;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class AvailableServicesActivity extends AppCompatActivity {

    public static final String ACCESS_TOKEN = "b57a223e-9ab3-4a91-977d-7071e0434a16";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_timer);
        //Back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Get selections from previous activity
        Bundle bundle = getIntent().getExtras();
        TrainStationInfo departure = (TrainStationInfo)bundle.get("departure");
        TrainStationInfo destination = (TrainStationInfo)bundle.get("destination");
        TimeString timeString = (TimeString)bundle.get("timeString");

        List<TrainService> trainServices = getAvailableTrains(departure, destination, timeString);

        //List creation for the selected timeString
        final ListView listView = findViewById(R.id.Schedules);
        final ArrayAdapter<TrainService> adapter = new ArrayAdapter<TrainService>(this, android.R.layout.simple_list_item_1, android.R.id.text1, trainServices);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
                TrainService selectedTrain = adapter.getItem(position);
                Intent intent = new Intent(view.getContext(), WaitingLobbyActivity.class);
                intent.putExtra("selectedTrain", selectedTrain);
                startActivity(intent);
            });
    }

    public List<TrainService> getAvailableTrains(TrainStationInfo departure, TrainStationInfo destination, TimeString timeString){
        DAALDBServiceSoap12 soapClient = new DAALDBServiceSoap12();
        int offset = 0;
        LocalTime now = LocalTime.now();
        if (timeString.getTime().isAfter(now)) {
            offset = (int)ChronoUnit.MINUTES.between(now, timeString.getTime());
        }
        DAAAccessToken accessToken = new DAAAccessToken();
        accessToken.TokenValue = ACCESS_TOKEN;
        try {
            DAAStationBoardWithDetails_2 soapResponse = soapClient.GetArrBoardWithDetails(10, departure.name(), destination.name(), DAAEnums.FilterType.to, offset, 120, accessToken);
            if (soapResponse.trainServices == null) {
                return Collections.emptyList();
            }

            return soapResponse.trainServices.stream()
                    .map( serviceItem -> {
                        TrainService trainService = new TrainService();
                        trainService.setId(serviceItem.serviceID);
                        trainService.setSta(new TimeString(serviceItem.sta));
                        trainService.setEta(new TimeString(serviceItem.eta));
                        return trainService;
                    })
                    .collect(Collectors.toList());


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(AvailableServicesActivity.this, "Couldn't find any results for your preferences. Please change them on the previous screen.", Toast.LENGTH_LONG).show();
            return Collections.emptyList();
        }
    }

}


