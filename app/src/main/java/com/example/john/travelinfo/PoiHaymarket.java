package com.example.john.travelinfo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PoiHaymarket extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_haymarket);

        Button nextButton = findViewById(R.id.buttonToGateway);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(PoiHaymarket.this,PoiGateway.class));
            }
        });

        Button previousButton = findViewById(R.id.buttonBackWaverley);
        previousButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(PoiHaymarket.this,PoiWaverley.class));
            }
        });
    }
}
