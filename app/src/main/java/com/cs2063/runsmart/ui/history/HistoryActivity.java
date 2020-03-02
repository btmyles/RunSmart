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
        setContentView(R.layout.history_item_detail);
        json = MainActivity.jsonUtils;
        json.getHistoryData(); //change this, need to get specific part of arraylist
        Intent intent = getIntent();
        long startTime = intent.getLongExtra("START_TIME", 0);
        long endTime = intent.getLongExtra("END_TIME", 0);
        double[] longitude = intent.getDoubleArrayExtra("LONGITUDE");
        double[] latitude = intent.getDoubleArrayExtra("LATITUDE");
        /*
        TextView textView =findViewById(R.id.text_start);
        textView.setText("Longitute: " + longitude + "\nLatitude: " + latitude);
        textView.setMovementMethod(new ScrollingMovementMethod());

         */

        getSupportActionBar().setTitle(Long.toString(startTime));
    }

}
