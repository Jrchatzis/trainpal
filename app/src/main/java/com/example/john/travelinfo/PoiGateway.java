package com.example.john.travelinfo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PoiGateway extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_gateway);

        Button nextButton = findViewById(R.id.buttonToPark);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(PoiGateway.this,PoiPark.class));
            }
        });

        Button previousButton = findViewById(R.id.buttonBackHaymarket);
        previousButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(PoiGateway.this,PoiHaymarket.class));
            }
        });

        TextView food =(TextView)findViewById(R.id.GatewayFood);
        food.setClickable(true);
        food.setMovementMethod(LinkMovementMethod.getInstance());
        String foodlink = "<a href='https://foursquare.com/explore?mode=url&ne=55.945042%2C-3.299772&q=Food&sw=55.934142%2C-3.325993'>Food</a>";
        food.setText(Html.fromHtml(foodlink,0));

        TextView coffee =(TextView)findViewById(R.id.GatewayCoffee);
        coffee.setClickable(true);
        coffee.setMovementMethod(LinkMovementMethod.getInstance());
        String coffeelink = "<a href='https://foursquare.com/explore?mode=url&ne=55.945042%2C-3.299772&q=coffee&sw=55.934142%2C-3.325993'>Coffee</a>";
        coffee.setText(Html.fromHtml(coffeelink,0));

        TextView shops =(TextView)findViewById(R.id.GatewayShops);
        shops.setClickable(true);
        shops.setMovementMethod(LinkMovementMethod.getInstance());
        String shopslink = "<a href='https://foursquare.com/explore?mode=url&ne=55.945042%2C-3.299772&q=shops&sw=55.934142%2C-3.325993'>Shops</a>";
        shops.setText(Html.fromHtml(shopslink,0));

        TextView fun =(TextView)findViewById(R.id.GatewayFun);
        fun.setClickable(true);
        fun.setMovementMethod(LinkMovementMethod.getInstance());
        String funlink = "<a href='https://foursquare.com/explore?mode=url&ne=55.945042%2C-3.299772&q=Fun&sw=55.934142%2C-3.325993'>Fun</a>";
        fun.setText(Html.fromHtml(funlink,0));

        TextView nightlife =(TextView)findViewById(R.id.GatewayNighlife);
        nightlife.setClickable(true);
        nightlife.setMovementMethod(LinkMovementMethod.getInstance());
        String nightlifelink = "<a href='https://foursquare.com/explore?mode=url&ne=55.945042%2C-3.299772&q=Nightlife&sw=55.934142%2C-3.325993'>Nightlife</a>";
        nightlife.setText(Html.fromHtml(nightlifelink,0));
    }
}
