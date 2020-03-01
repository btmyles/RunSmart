package com.cs2063.runsmart.ui.history;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cs2063.runsmart.MainActivity;
import com.cs2063.runsmart.R;
import com.cs2063.runsmart.util.JsonUtils;

public class HistoryActivity extends AppCompatActivity {
    private final String TAG = "HistoryActivity.java";
    private JsonUtils json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_history);
        json = MainActivity.jsonUtils;
        json.getHistoryData(); //change this, need to get specific part of arraylist
        Intent intent = getIntent();
        String startTime = intent.getStringExtra("START_TIME");
        String longitude = intent.getStringExtra("LONGITUTDE");
        String latitude = intent.getStringExtra("LATITUDE");
        /*
        TextView textView =findViewById(R.id.text_start);
        textView.setText("Longitute: " + longitude + "\nLatitude: " + latitude);
        textView.setMovementMethod(new ScrollingMovementMethod());

         */

        getSupportActionBar().setTitle(startTime);
    }

}
