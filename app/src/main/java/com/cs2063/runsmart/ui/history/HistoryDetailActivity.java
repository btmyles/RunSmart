package com.cs2063.runsmart.ui.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cs2063.runsmart.LineLayerActivity;
import com.cs2063.runsmart.R;
import com.cs2063.runsmart.util.JsonUtils;

public class HistoryDetailActivity extends AppCompatActivity {
    private final String TAG = "HistoryDetailActivity.java";
    private JsonUtils json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_item_detail);

        Intent intent = getIntent();
        long startTime = intent.getLongExtra("START_TIME", 0);
        long endTime = intent.getLongExtra("END_TIME", 0);
        long duration = intent.getLongExtra("DURATION", 0);
        double distance = intent.getDoubleExtra("DISTANCE", 0);
        final double[] longitude = intent.getDoubleArrayExtra("LONGITUDE");
        final double[] latitude = intent.getDoubleArrayExtra("LATITUDE");
        long avg_pace = intent.getLongExtra("AVG_PACE", 0);

        TextView textStart = findViewById(R.id.value_start);
        textStart.setText(Long.toString(startTime));
        // This line was at the end of each section for the textviews. Might be necessary when we start scrolling
        //textStart.setMovementMethod(new ScrollingMovementMethod());
        TextView textEnd = findViewById(R.id.value_end);
        textEnd.setText(Long.toString(endTime));
        TextView textDuration = findViewById(R.id.value_duration);
        textDuration.setText(Long.toString(duration));
        TextView textDistance = findViewById(R.id.value_distance);
        textDistance.setText(Double.toString(distance));
        TextView textAvgPace = findViewById(R.id.value_avg_pace);
        textAvgPace.setText(Double.toString(avg_pace));
        // Eventually the notes section will be added here

        Button mapButton = findViewById(R.id.button_map);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start map activity
                Intent intent = new Intent(getApplicationContext(), LineLayerActivity.class);
                intent.putExtra("LATITUDE", latitude);
                intent.putExtra("LONGITUDE", longitude);
                startActivity(intent);
            }
        });

        // This should be
        getSupportActionBar().setTitle(Long.toString(startTime));
    }

}
