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

import java.time.LocalTime;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class MainActivity extends AppCompatActivity {

    TrainStationInfo destination;
    TrainStationInfo departure;
    DepartureTime departureTime;


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
        final Spinner destinationSpinner = (Spinner) findViewById(R.id.goingSpinner);
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

        //Departure time
        final Spinner departureTimeSpinner = (Spinner)findViewById(R.id.beforeSpinner);
        DepartureTimeSupplier departureTimeSupplier = new DepartureTimeSupplier(30);
        List<DepartureTime> departureTimes = Stream.generate(departureTimeSupplier).limit(4).collect(Collectors.toList());

        ArrayAdapter<DepartureTime> departureTimeAdapter = new ArrayAdapter<DepartureTime>(MainActivity.this, android.R.layout.simple_list_item_1, departureTimes);
        departureTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departureTimeSpinner.setAdapter(departureTimeAdapter);
        departureTimeSpinner.setSelection(0);
        departureTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                departureTime = (DepartureTime)departureTimeSpinner.getAdapter().getItem(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // no-op
            }
        });

        //Proceed button
        Button proceedButton = (Button) findViewById(R.id.buttonProceed);
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dep = TrainStationInfo.class.cast(departureSpinner.getSelectedItem()).name();
                String dest = TrainStationInfo.class.cast(destinationSpinner.getSelectedItem()).name();
                if (!dep.equals(dest)){
                    Intent intent = new Intent(MainActivity.this, Timer.class);
                    intent.putExtra("departureTime", departureTime);
                    intent.putExtra("destination", destination);
                    intent.putExtra("departure", departure);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "You can't have the same departure and arrival location. Please change your selection", Toast.LENGTH_SHORT).show();
                }
        }
    });
}
}