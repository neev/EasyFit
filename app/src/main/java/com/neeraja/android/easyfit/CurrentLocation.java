package com.neeraja.android.easyfit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class CurrentLocation extends AppCompatActivity{
    private TextView locationText;
    private TextView addressText;

    double longitude ;
    double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location);

        locationText = (TextView) findViewById(R.id.location);
        addressText = (TextView) findViewById(R.id.address);


    }



    public void callBackDataFromAsyncTask(String address) {
        addressText.setText(address);
    }

}

