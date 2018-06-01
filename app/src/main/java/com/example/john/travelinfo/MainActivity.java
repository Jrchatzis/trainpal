package com.example.john.travelinfo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class MainActivity extends AppCompatActivity {

    TrainStationInfo destination;
    TrainStationInfo departure;
    TimeString timeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // departing station
        final Spinner departureSpinner = Spinner.class.cast(findViewById(R.id.startingSpinner));
        ArrayAdapter<TrainStationInfo> departureAdapter = new ArrayAdapter<TrainStationInfo>(MainActivity.this, android.R.layout.simple_list_item_1, TrainStationInfo.values());
        departureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departureSpinner.setAdapter(departureAdapter);
        departureSpinner.setSelection(0);
        departureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                departure = (TrainStationInfo)departureSpinner.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // no-op
            }
        });

        // destination station
        final Spinner destinationSpinner = findViewById(R.id.goingSpinner);
        ArrayAdapter<TrainStationInfo> destinationAdapter = new ArrayAdapter<TrainStationInfo>(MainActivity.this, android.R.layout.simple_list_item_1, TrainStationInfo.values());
        destinationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinationSpinner.setAdapter(destinationAdapter);
        destinationSpinner.setSelection(1);
        destinationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                destination = (TrainStationInfo) destinationSpinner.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // no-op
            }
        });

        //Departure timeString
        final Spinner departureTimeSpinner = findViewById(R.id.beforeSpinner);
        TimeWithIntervalSupplier timeWithIntervalSupplier = new TimeWithIntervalSupplier(30);
        List<TimeString> timeStrings = Stream.generate(timeWithIntervalSupplier).limit(4).collect(Collectors.toList());

        ArrayAdapter<TimeString> departureTimeAdapter = new ArrayAdapter<TimeString>(MainActivity.this, android.R.layout.simple_list_item_1, timeStrings);
        departureTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departureTimeSpinner.setAdapter(departureTimeAdapter);
        departureTimeSpinner.setSelection(0);
        departureTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                timeString = (TimeString)departureTimeSpinner.getAdapter().getItem(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // no-op
            }
        });

        //Proceed button
        Button proceedButton = findViewById(R.id.buttonProceed);
        proceedButton.setOnClickListener(view -> {
                String dep = TrainStationInfo.class.cast(departureSpinner.getSelectedItem()).name();
                String dest = TrainStationInfo.class.cast(destinationSpinner.getSelectedItem()).name();
                if (!dep.equals(dest)) {
                    Intent intent = new Intent(MainActivity.this, AvailableServicesActivity.class);
                    intent.putExtra("timeString", timeString);
                    intent.putExtra("destination", destination);
                    intent.putExtra("departure", departure);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "You can't have the same departure and arrival selection.", Toast.LENGTH_LONG).show();
                }
        });
    }
}