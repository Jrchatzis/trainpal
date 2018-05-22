package com.example.john.travelinfo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //1st spinner - Starting station
        Spinner spinnerFirst = (Spinner) findViewById(R.id.startingSpinner);

        ArrayAdapter<String> firstAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Start));

        firstAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFirst.setAdapter(firstAdapter);

        spinnerFirst.setSelection(0);

        //Keep selection
        final String startingLocation = spinnerFirst.getSelectedItem().toString();

        //2nd spinner - Ending station
        Spinner spinnerSecond = (Spinner) findViewById(R.id.goingSpinner);

        ArrayAdapter<String> secondAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.End));

        secondAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSecond.setAdapter(secondAdapter);
        spinnerSecond.setSelection(1);

        //Keep selection
        final String endingLocation = spinnerFirst.getSelectedItem().toString();

        //3nd spinner - Starting time
        Spinner spinnerThird = (Spinner) findViewById(R.id.beforeSpinner);

        ArrayAdapter<String> thirdAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.StartingTime));

        thirdAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerThird.setAdapter(thirdAdapter);

        spinnerThird.setSelection(0);

        //Keep selection
        final String departureTime = spinnerFirst.getSelectedItem().toString();

        //4th spinner - Ending time
        Spinner spinnerFourth = (Spinner) findViewById(R.id.afterSpinner);

        ArrayAdapter<String> fourthAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.EndingTime));

        fourthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFourth.setAdapter(fourthAdapter);

        spinnerFourth.setSelection(1);

        //Keep selection
        final String arrivalTime = spinnerFirst.getSelectedItem().toString();


        //Proceed button
        Button advanceToTimeTables = (Button) findViewById(R.id.buttonProceed);
        advanceToTimeTables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //if ((!startingLocation.equals(endingLocation)) && (!departureTime.equals(arrivalTime))){
                    Intent intent = new Intent(MainActivity.this, Timer.class);
                    startActivity(intent);
                //} else if((startingLocation.equals(endingLocation))&&(!departureTime.equals(arrivalTime))){
                   // Toast.makeText(MainActivity.this, "You can't have the same departure and arrival location. Please change your selection", Toast.LENGTH_SHORT).show();
                //} else if ((departureTime.equals(arrivalTime)) && (!startingLocation.equals(endingLocation))){
                    //Toast.makeText(MainActivity.this, "You can't have the same departure and arrival time. Please change your selection", Toast.LENGTH_SHORT).show();
                //}
        }
    });
}
}