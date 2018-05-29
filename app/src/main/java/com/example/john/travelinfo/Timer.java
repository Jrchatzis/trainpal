package com.example.john.travelinfo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.testSoap.wcf.*;
import java.util.List;
import java.util.ArrayList;


public class Timer extends AppCompatActivity {
    ListView listView;
    TextView textView;
    String[] listItem;
    TrainStationInfo selectionDep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        //Back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Get selections from MainActivity activity
        Bundle recDataDep = getIntent().getExtras();
        selectionDep = (TrainStationInfo)recDataDep.get("departure");

        Bundle recDataArr = getIntent().getExtras();
        String selectionArr = recDataArr.getString("arrival");

        Bundle recDataTime = getIntent().getExtras();
        String selectionTime = recDataTime.getString("time");

        String src;
        public static void ExtractTrainInfo(){
            for (TrainStationInfo station : TrainStationInfo.values()){
                if
            }
        }
        //Get data from database
        @Override
        public void getAvailableTrains() {
            DAADeparturesBoardWithDetails request = new DAADeparturesBoardWithDetails();
            request.numRows = 10;
            String departureCode = selectionDep.name();
            DAAStationBoardWithDetails_2 result = new DAALDBServiceSoap12().GetArrBoardWithDetails(...);
            result.trainServices
            String destination = destination;
            List<TrainService> KeepList = new ArrayList<>();
            for (DAAServiceItemWithCallingPoints_2 trainService : result.trainServices) {
                for (CallingPoint point:trainService.subsequentCallingPoints){
                    if (point.crs.equals(destination)) {
                        keepList.add(trainService);
                        break;
                    }
                }
            }
        }
        //List creation
        listView=(ListView)findViewById(R.id.Schedules);
        textView=(TextView)findViewById(R.id.trainSchedulesText);
        listItem = getResources().getStringArray(R.array.Schedules);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, listItem);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj = listView.getAdapter().getItem(position);
                String value = obj.toString();
                Intent selection = new Intent(view.getContext(),WaitingLobby.class);
                selection.putExtra("Time",value);
                startActivity(selection);
            }
        });
    }

}


