package com.example.john.travelinfo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PoiWaverley extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_waverley);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Button leading Haymarket screen
        Button nextButton = findViewById(R.id.buttonToHaymarket);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(PoiWaverley.this,PoiHaymarket.class));
            }
        });

        //Hypertext leading to points of interest concerning food
        TextView food =(TextView)findViewById(R.id.WaverleyFood);
        food.setClickable(true);
        food.setMovementMethod(LinkMovementMethod.getInstance());
        String foodlink = "<a href='https://foursquare.com/explore?mode=url&near=Waverley%20Station%2C%20Edinburgh%2C%20United%20Kingdom&nearGeoId=79804&q=food'>Food</a>";
        food.setText(Html.fromHtml(foodlink,0));

        //Hypertext leading to points of interest concerning coffee
        TextView coffee =(TextView)findViewById(R.id.WaverleyCoffee);
        coffee.setClickable(true);
        coffee.setMovementMethod(LinkMovementMethod.getInstance());
        String coffeelink = "<a href='https://foursquare.com/explore?mode=url&near=Waverley%20Station%2C%20Edinburgh%2C%20United%20Kingdom&nearGeoId=79804&q=coffee'>Coffee</a>";
        coffee.setText(Html.fromHtml(coffeelink,0));

        //Hypertext leading to points of interest concerning shops
        TextView shops =(TextView)findViewById(R.id.WaverleyShops);
        shops.setClickable(true);
        shops.setMovementMethod(LinkMovementMethod.getInstance());
        String shopslink = "<a href='https://foursquare.com/explore?mode=url&near=Waverley%20Station%2C%20Edinburgh%2C%20United%20Kingdom&nearGeoId=79804&q=shops'>Shops</a>";
        shops.setText(Html.fromHtml(shopslink,0));

        //Hypertext leading to points of interest concerning fun places
        TextView fun =(TextView)findViewById(R.id.WaverleyFun);
        fun.setClickable(true);
        fun.setMovementMethod(LinkMovementMethod.getInstance());
        String funlink = "<a href='https://foursquare.com/explore?mode=url&near=Waverley%20Station%2C%20Edinburgh%2C%20United%20Kingdom&nearGeoId=79804&q=Fun'>Fun</a>";
        fun.setText(Html.fromHtml(funlink,0));

        //Hypertext leading to points of interest concerning nightlife
        TextView nightlife =(TextView)findViewById(R.id.WaverleyNightlife);
        nightlife.setClickable(true);
        nightlife.setMovementMethod(LinkMovementMethod.getInstance());
        String nightlifelink = "<a href='https://foursquare.com/explore?mode=url&near=Waverley%20Station%2C%20Edinburgh%2C%20United%20Kingdom&nearGeoId=79804&q=Nightlife'>Nightlife</a>";
        nightlife.setText(Html.fromHtml(nightlifelink,0));
    }

    //Button leading to the previous screen of the current one
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
