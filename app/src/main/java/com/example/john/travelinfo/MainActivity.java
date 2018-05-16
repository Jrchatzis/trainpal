package com.example.john.travelinfo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //1st spinner - Starting station
        Spinner spinnerFirst = (Spinner) findViewById(R.id.planets_spinner2);

        ArrayAdapter<String> firstAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Start));

        firstAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFirst.setAdapter(firstAdapter);

        //2nd spinner - Ending station
        Spinner spinnerSecond = (Spinner) findViewById(R.id.planets_spinner);

        ArrayAdapter<String> secondAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.End));

        secondAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSecond.setAdapter(secondAdapter);

        //3nd spinner - Starting time
        Spinner spinnerThird = (Spinner) findViewById(R.id.planets_spinner3);

        ArrayAdapter<String> thirdAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.StartingTime));

        thirdAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerThird.setAdapter(thirdAdapter);

        spinnerThird.setSelection(0);

        //4th spinner - Ending time
        Spinner spinnerFourth = (Spinner) findViewById(R.id.planets_spinner4);

        ArrayAdapter<String> fourthAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.EndingTime));

        fourthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFourth.setAdapter(fourthAdapter);

        //Proceed button
        Button advanceToTimeTables = (Button) findViewById(R.id.button_proceed);
        advanceToTimeTables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Timer.class);
                startActivity(intent);
            }
        });
    }
}
