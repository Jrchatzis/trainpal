package com.example.john.travelinfo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PoiPark extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_park);

        //Button leading Edinburgh Gateway screen
        Button previousButton = findViewById(R.id.buttonBackGateway);
        previousButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(PoiPark.this,PoiGateway.class));
            }
        });
        //Button leading to home screen
        Button homeButton = findViewById(R.id.buttonToStart);
        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(PoiPark.this,HomeActivity.class));
            }
        });

        //Hypertext leading to points of interest concerning food
        TextView food =(TextView)findViewById(R.id.ParkFood);
        food.setClickable(true);
        food.setMovementMethod(LinkMovementMethod.getInstance());
        String foodlink = "<a href='https://foursquare.com/explore?mode=url&ne=55.932051%2C-3.291317&q=Food&sw=55.921147%2C-3.317538'>Food</a>";
        food.setText(Html.fromHtml(foodlink,0));

        //Hypertext leading to points of interest concerning coffee
        TextView coffee =(TextView)findViewById(R.id.ParkCoffee);
        coffee.setClickable(true);
        coffee.setMovementMethod(LinkMovementMethod.getInstance());
        String coffeelink = "<a href='https://foursquare.com/explore?mode=url&ne=55.932051%2C-3.291317&q=coffee&sw=55.921147%2C-3.317538'>Coffee</a>";
        coffee.setText(Html.fromHtml(coffeelink,0));

        //Hypertext leading to points of interest concerning shops
        TextView shops =(TextView)findViewById(R.id.ParkShops);
        shops.setClickable(true);
        shops.setMovementMethod(LinkMovementMethod.getInstance());
        String shopslink = "<a href='https://foursquare.com/explore?mode=url&ne=55.932051%2C-3.291317&q=shops&sw=55.921147%2C-3.317538'>Shops</a>";
        shops.setText(Html.fromHtml(shopslink,0));

        //Hypertext leading to points of interest concerning fun places
        TextView fun =(TextView)findViewById(R.id.ParkFun);
        fun.setClickable(true);
        fun.setMovementMethod(LinkMovementMethod.getInstance());
        String funlink = "<a href='https://foursquare.com/explore?mode=url&ne=55.932051%2C-3.291317&q=Fun&sw=55.921147%2C-3.317538'>Fun</a>";
        fun.setText(Html.fromHtml(funlink,0));

        //Hypertext leading to points of interest concerning nightlife
        TextView nightlife =(TextView)findViewById(R.id.ParkNightlife);
        nightlife.setClickable(true);
        nightlife.setMovementMethod(LinkMovementMethod.getInstance());
        String nightlifelink = "<a href='https://foursquare.com/explore?mode=url&ne=55.932051%2C-3.291317&q=Nightlife&sw=55.921147%2C-3.317538'>Nightlife</a>";
        nightlife.setText(Html.fromHtml(nightlifelink,0));
    }
}
