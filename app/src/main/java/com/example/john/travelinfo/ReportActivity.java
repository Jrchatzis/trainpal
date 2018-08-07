package com.example.john.travelinfo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ReportActivity extends AppCompatActivity {

    EditText emailSubject, emailMessage;

    Button emailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        //Default text of fields
        String email = "jrchatzis@gmail.com";
        emailSubject = (EditText) findViewById(R.id.emailSubject);
        emailMessage = (EditText) findViewById(R.id.emailMessage);
        emailButton = (Button) findViewById(R.id.emailButton);

        //Enable button to pass the information written in the fields to the wanted mail service
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Subject = emailSubject.getText().toString();
                String Message = emailMessage.getText().toString();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                intent.putExtra(Intent.EXTRA_SUBJECT, Subject);
                intent.putExtra(Intent.EXTRA_TEXT, Message);

                intent.setType("message/rfc822");

                startActivity(Intent.createChooser(intent, "Select Email app"));
            }
        }
        );

        //Create actionbars
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    //Back button on action bar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
