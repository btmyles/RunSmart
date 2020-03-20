package com.cs2063.runsmart.ui.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cs2063.runsmart.LineLayerActivity;
import com.cs2063.runsmart.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoryDetailActivity extends AppCompatActivity {
    private final String TAG = "HistoryDetailActivity.java";
    private static DecimalFormat fmt= new DecimalFormat("######.##");
    SimpleDateFormat dayFmt = new SimpleDateFormat("yyyy/MM/dd ");

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
        double avg_pace = intent.getDoubleExtra("AVG_PACE", 0);


        TextView textStart = findViewById(R.id.value_start);
        textStart.setText((formatTime(startTime).replace("a.m", "AM").replace("p.m.","PM")));
        TextView textEnd = findViewById(R.id.value_end);
        textEnd.setText(formatTime(endTime).replace("a.m", "AM").replace("p.m.","PM"));
        TextView textDuration = findViewById(R.id.value_duration);
        textDuration.setText(formatDuration(duration));
        TextView textDistance = findViewById(R.id.value_distance);
        textDistance.setText(fmt.format(distance)+" km");
        TextView textAvgPace = findViewById(R.id.value_avg_pace);
        textAvgPace.setText(fmt.format((avg_pace/3.6))+" m/s");
        // Eventually the notes section will be added here

        ImageButton mapButton = findViewById(R.id.button_map);
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

        ImageButton deleteButton = findViewById(R.id.button_prev);
        deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
        });


        // This should be
        getSupportActionBar().setTitle((dayFmt.format(startTime)));
    }
    String formatDuration(long duration) {
        long second = (duration / 1000) % 60;
        long minute = (duration / (1000 * 60)) % 60;
        long hour = ((duration / (1000 * 60 * 60)) % 24);
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }
    String formatTime(long duration) {
        long second = (duration / 1000) % 60;
        long minute = (duration / (1000 * 60)) % 60;
        long hour = ((duration / (1000 * 60 * 60)) % 24 - 4) % 24;

        return stdFmt(String.format("%02d:%02d:%02d", hour, minute, second));
    }

    String stdFmt(String militaryFmt){
        Date date;
        String output = null;
        DateFormat df = new SimpleDateFormat("H:mm:ss");
        DateFormat outputformat = new SimpleDateFormat("h:mm:ss aa");
        try{
            date= df.parse(militaryFmt);
            output = outputformat.format(date);
        }catch(ParseException pe){
            pe.printStackTrace();
        }
        return output;
    }

}
