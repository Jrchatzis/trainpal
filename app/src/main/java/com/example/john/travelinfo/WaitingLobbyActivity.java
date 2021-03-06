package com.example.john.travelinfo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.john.config.TrainStationInfo;
import com.example.travelinfo.ldbws.DAAAccessToken;
import com.example.travelinfo.ldbws.DAALDBServiceSoap12;
import com.example.travelinfo.ldbws.DAAServiceDetails;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;

public class WaitingLobbyActivity extends AppCompatActivity {

    private Timer timer;
    private TextView mTextMessage;
    private TrainService selectedTrain;
    private TrainStationInfo departure;
    private TrainStationInfo destination;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    public static final String ACCESS_TOKEN = "b57a223e-9ab3-4a91-977d-7071e0434a16";
    //public static final String ACCESS_TOKEN = "c894167b-8296-4071-8797-e3fa421f8ff6";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_lobby);

        mTextMessage = findViewById(R.id.message);

        //Get data from previous screen
        Bundle bundle = getIntent().getExtras();
        selectedTrain = TrainService.class.cast(bundle.get("selectedTrain"));
        departure = (TrainStationInfo)bundle.get("departure");
        destination = (TrainStationInfo)bundle.get("destination");
        TextView sta = findViewById(R.id.selectedTime);
        sta.setText(selectedTrain.getSta().toString());

        //Chronometer used to countdown until the arrival of the train
        Chronometer countdown = findViewById(R.id.countdown);
        countdown.setCountDown(true);
        countdown.setBase(SystemClock.elapsedRealtime() + millisUntilArrival());
        countdown.start();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateService();
            }
        }, 0L, 600L);

        //Set action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    //Go back
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    protected long millisUntilArrival() {
        return ChronoUnit.MILLIS.between(LocalTime.now(), selectedTrain.getSta().getTime());
    }

    //Update the information provided to the user
    private void updateService() {

        try {
            TrainService serviceUpdate = getTrainServiceUpdate();
            runOnUiThread(()->{
                TextView eta = findViewById(R.id.estimatedTime);
                //Alert dialog appeared in the event of train's arrival on time
                if (serviceUpdate.isArrived()) {
                    timer.cancel();
                    eta.setText("Arrived: " + serviceUpdate.getAta());
                    AlertDialog.Builder builderDelay = new AlertDialog.Builder(WaitingLobbyActivity.this);
                    builderDelay.setCancelable(true);
                    builderDelay.setTitle("Train update");
                    builderDelay.setMessage("Your train has just arrived!");
                    builderDelay.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    moveTaskToBack(true);
                                }
                            });
                }
                //In the event of train's delay or cancellation
                else if (serviceUpdate.isDelayed()) {
                    timer.cancel();
                    eta.setText("Delayed: " + serviceUpdate.getDelayReason());

                    AlertDialog.Builder builderDelay = new AlertDialog.Builder(WaitingLobbyActivity.this);
                    builderDelay.setCancelable(true);
                    builderDelay.setTitle("Train delay update");
                    builderDelay.setMessage(serviceUpdate.getDelayReason()+"."+"\nWhat would you like to do?");
                    builderDelay.setPositiveButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    moveTaskToBack(true);
                                }
                            });
                    builderDelay.setNeutralButton("Reroute",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(WaitingLobbyActivity.this, RouteAnalysisActivity.class);
                                    intent.putExtra("destination", destination);
                                    intent.putExtra("departure", departure);
                                    startActivity(intent);
                                }
                            });
                    builderDelay.setNegativeButton("Stay",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    builderDelay.create().show();

                   //Set a vibrator for the application in the event of train delay or arrival
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    eta.setText(serviceUpdate.getEta());
                }

            });

            Log.d(getClass().getName(),"eta updated");
        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(()-> {
                Toast.makeText(this, "There was an error contacting train service", Toast.LENGTH_LONG).show();
            });
        }
    }

    //Access the train service and refresh data
    private TrainService getTrainServiceUpdate() throws Exception {
        if (selectedTrain.getId().equals("dummy")) {
            return selectedTrain;
        }
        DAALDBServiceSoap12 soapClient = new DAALDBServiceSoap12();
        DAAAccessToken accessToken = new DAAAccessToken();
        accessToken.TokenValue = ACCESS_TOKEN;
        DAAServiceDetails soapResponse = soapClient.GetServiceDetails(selectedTrain.getId(), accessToken);
        TrainService result = new TrainService();
        result.setAta(soapResponse.ata);
        result.setEta(soapResponse.eta);
        result.setDelayReason(soapResponse.delayReason);
        result.setDelayReason("delayed");
        result.setId(selectedTrain.getId());
        result.setSta(new TimeString(soapResponse.sta));
        return result;

    }
}

