package com.example.john.travelinfo;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.travelinfo.ldbws.*;

import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.MINUTES;


public class Timer extends AppCompatActivity {

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
        DepartureTime departureTime = (DepartureTime)bundle.get("departureTime");

        List<AvailableTrain> availableTrains = getAvailableTrains(departure, destination, departureTime);

        //List creation for the selected time
        final ListView listView = (ListView)findViewById(R.id.Schedules);
        final ArrayAdapter<AvailableTrain> adapter = new ArrayAdapter<AvailableTrain>(this, android.R.layout.simple_list_item_1, android.R.id.text1, availableTrains);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AvailableTrain selectedTrain = (AvailableTrain)adapter.getItem(position);
                Intent selection = new Intent(view.getContext(), WaitingLobby.class);
                selection.putExtra("selectedTrain", selectedTrain);
                startActivity(selection);
            }
        });
    }

    public List<AvailableTrain> getAvailableTrains(TrainStationInfo departure, TrainStationInfo destination, DepartureTime departureTime){
        DAALDBServiceSoap12 soapClient = new DAALDBServiceSoap12();
        int offset = 0;
        LocalTime now = LocalTime.now();
        if (departureTime.getTime().isAfter(now)) {
            offset = (int)ChronoUnit.MINUTES.between(now, departureTime.getTime());
        }
        DAAAccessToken accessToken = new DAAAccessToken();
        accessToken.TokenValue = "b57a223e-9ab3-4a91-977d-7071e0434a16";
        try {
            DAAStationBoardWithDetails_2 soapResponse = soapClient.GetArrBoardWithDetails(10, departure.name(), destination.name(), DAAEnums.FilterType.to, offset, 120, accessToken);

            return soapResponse.trainServices.stream()
                    .map( serviceItem -> {
                        AvailableTrain availableTrain = new AvailableTrain();
                        availableTrain.setId(serviceItem.serviceID);
                        availableTrain.setSta(serviceItem.sta);
                        availableTrain.setEta(serviceItem.eta);
                        return availableTrain;
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}


