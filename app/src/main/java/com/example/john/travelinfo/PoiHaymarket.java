package com.example.john.travelinfo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PoiHaymarket extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_haymarket);

        //Button leading Edinburgh Gateway screen
        Button nextButton = findViewById(R.id.buttonToGateway);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(PoiHaymarket.this,PoiGateway.class));
            }
        });

        //Button leading to waverley screen
        Button previousButton = findViewById(R.id.buttonBackWaverley);
        previousButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(PoiHaymarket.this,PoiWaverley.class));
            }
        });

        //Hypertext leading to points of interest concerning food
        TextView food =(TextView)findViewById(R.id.HaymarketFood);
        food.setClickable(true);
        food.setMovementMethod(LinkMovementMethod.getInstance());
        String foodlink = "<a href='https://foursquare.com/explore?mode=url&near=Haymarket%2C%20Edinburgh%2C%20United%20Kingdom&nearGeoId=79815&q=Food'>Food</a>";
        food.setText(Html.fromHtml(foodlink,0));

        //Hypertext leading to points of interest concerning coffee
        TextView coffee =(TextView)findViewById(R.id.HaymarketCoffee);
        coffee.setClickable(true);
        coffee.setMovementMethod(LinkMovementMethod.getInstance());
        String coffeelink = "<a href='https://foursquare.com/explore?mode=url&near=Haymarket%2C%20Edinburgh%2C%20United%20Kingdom&nearGeoId=79815&q=Coffee'>Coffee</a>";
        coffee.setText(Html.fromHtml(coffeelink,0));

        //Hypertext leading to points of interest concerning shops
        TextView shops =(TextView)findViewById(R.id.HaymarketShops);
        shops.setClickable(true);
        shops.setMovementMethod(LinkMovementMethod.getInstance());
        String shopslink = "<a href='https://foursquare.com/explore?mode=url&near=Haymarket%2C%20Edinburgh%2C%20United%20Kingdom&nearGeoId=79815&q=shops'>Shops</a>";
        shops.setText(Html.fromHtml(shopslink,0));

        //Hypertext leading to points of interest concerning fun places
        TextView fun =(TextView)findViewById(R.id.HaymarketFun);
        fun.setClickable(true);
        fun.setMovementMethod(LinkMovementMethod.getInstance());
        String funlink = "<a href='https://foursquare.com/explore?mode=url&near=Haymarket%2C%20Edinburgh%2C%20United%20Kingdom&nearGeoId=79815&q=Fun'>Fun</a>";
        fun.setText(Html.fromHtml(funlink,0));

        //Hypertext leading to points of interest concerning nightlife
        TextView nightlife =(TextView)findViewById(R.id.HaymarketNighlife);
        nightlife.setClickable(true);
        nightlife.setMovementMethod(LinkMovementMethod.getInstance());
        String nightlifelink = "<a href='https://foursquare.com/explore?mode=url&near=Haymarket%2C%20Edinburgh%2C%20United%20Kingdom&nearGeoId=79815&q=Nightlife'>Nightlife</a>";
        nightlife.setText(Html.fromHtml(nightlifelink,0));

    }
}
