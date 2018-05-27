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


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //1st spinner - Starting station
        final Spinner spinnerFirst = (Spinner) findViewById(R.id.startingSpinner);

        ArrayAdapter<String> firstAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Start));

        firstAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFirst.setAdapter(firstAdapter);
        spinnerFirst.setSelection(0);

        //Keep selection
        final String startingLocation = spinnerFirst.getSelectedItem().toString();

        spinnerFirst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj = spinnerFirst.getAdapter().getItem(position);
                String value = obj.toString();
                Intent selection = new Intent(view.getContext(),WaitingLobby.class);
                selection.putExtra("departure",value);
            }
        });

        //2nd spinner - Ending station
        final Spinner spinnerSecond = (Spinner) findViewById(R.id.goingSpinner);

        ArrayAdapter<String> secondAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.End));

        secondAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSecond.setAdapter(secondAdapter);
        spinnerSecond.setSelection(1);

        //Keep selection
        final String endingLocation = spinnerSecond.getSelectedItem().toString();

        spinnerSecond.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj = spinnerSecond.getAdapter().getItem(position);
                String value = obj.toString();
                Intent selection = new Intent(view.getContext(),WaitingLobby.class);
                selection.putExtra("arrival",value);
            }
        });

        //3nd spinner - Starting time
        final Spinner spinnerThird = (Spinner) findViewById(R.id.beforeSpinner);

        ArrayAdapter<String> thirdAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.StartingTime));

        thirdAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerThird.setAdapter(thirdAdapter);

        spinnerThird.setSelection(0);

        //Keep selection
        final String departureTime = spinnerFirst.getSelectedItem().toString();

        spinnerThird.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj = spinnerThird.getAdapter().getItem(position);
                String value = obj.toString();
                Intent selection = new Intent(view.getContext(),WaitingLobby.class);
                selection.putExtra("time",value);
            }
        });

        //Proceed button
        Button advanceToTimeTables = (Button) findViewById(R.id.buttonProceed);
        advanceToTimeTables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!startingLocation.equals(endingLocation)){
                    Intent intent = new Intent(MainActivity.this, Timer.class);
                    startActivity(intent);
                } else if(startingLocation.equals(endingLocation)){
                    Toast.makeText(MainActivity.this, "You can't have the same departure and arrival location. Please change your selection", Toast.LENGTH_SHORT).show();
                }
        }
    });
}
}