package com.example.john.travelinfo;



import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class Timer extends AppCompatActivity {
    ListView listView;
    TextView textView;
    String[] listItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        //Back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //List creation
        listView=(ListView)findViewById(R.id.Schedules);
        textView=(TextView)findViewById(R.id.textView);
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


