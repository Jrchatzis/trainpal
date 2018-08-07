package com.example.john.travelinfo;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class HomeActivity extends AppCompatActivity  implements View.OnClickListener{
    private CardView trainCard, stationCard, aboutCard, guideCard, reportCard, offCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Define each card of the menu
        trainCard = (CardView) findViewById(R.id.trainCard);
        stationCard = (CardView) findViewById(R.id.stationCard);
        aboutCard = (CardView) findViewById(R.id.aboutCard);
        guideCard = (CardView) findViewById(R.id.legendCard);
        reportCard = (CardView) findViewById(R.id.reportCard);
        offCard = (CardView) findViewById(R.id.offCard);

        //add click listener to each card
        trainCard.setOnClickListener(this);
        stationCard.setOnClickListener(this);
        aboutCard.setOnClickListener(this);
        guideCard.setOnClickListener(this);
        reportCard.setOnClickListener(this);
        offCard.setOnClickListener(this);
    }
    //Selection of different screens from the home screen
    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()) {
            case R.id.trainCard : i = new Intent(this,MainActivity.class);
                startActivity(i);
                break;
            case R.id.offCard :
                //Alert dialog ensuring the user wants to exit the application
                AlertDialog.Builder builderDelay = new AlertDialog.Builder(HomeActivity.this);
                builderDelay.setCancelable(true);
                builderDelay.setTitle("Trainpal message");
                builderDelay.setMessage("Are you sure you want to leave Trainpal?");
                builderDelay.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                moveTaskToBack(true);
                            }
                        });
                builderDelay.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builderDelay.create().show();
                break;
            case R.id.reportCard : i = new Intent(this,ReportActivity.class);
                startActivity(i);
                break;
            case R.id.aboutCard : i = new Intent(this,InfoActivity.class);
                startActivity(i);
                break;
            case R.id.stationCard : i = new Intent(this,PoiWaverley.class);
                startActivity(i);
                break;
            case R.id.legendCard : i = new Intent (this, LegendActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }
}
