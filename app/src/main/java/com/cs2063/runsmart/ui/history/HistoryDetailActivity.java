package com.cs2063.runsmart.ui.history;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cs2063.runsmart.LineLayerActivity;
import com.cs2063.runsmart.MainActivity;
import com.cs2063.runsmart.R;
import com.cs2063.runsmart.model.HistoryData;
import com.cs2063.runsmart.util.JsonUtils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class HistoryDetailActivity extends AppCompatActivity {
    private final String TAG = "HistoryDetailActivity.java";
    private static DecimalFormat fmt= new DecimalFormat("######.##");
    SimpleDateFormat dayFmt = new SimpleDateFormat("yyyy/MM/dd ");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_item_detail);

        final Intent intent = getIntent();
        final long startTime = intent.getLongExtra("START_TIME", 0);
        final long endTime = intent.getLongExtra("END_TIME", 0);
        long duration = intent.getLongExtra("DURATION", 0);
        double distance = intent.getDoubleExtra("DISTANCE", 0);
        final double[] longitude = intent.getDoubleArrayExtra("LONGITUDE");
        final double[] latitude = intent.getDoubleArrayExtra("LATITUDE");
        double avg_pace = intent.getDoubleExtra("AVG_PACE", 0);
        String notes = intent.getStringExtra("NOTES");

        TextView textStart = findViewById(R.id.value_start);
        textStart.setText((formatTime(startTime).replace("a.m", "AM").replace("p.m.","PM")));
        TextView textEnd = findViewById(R.id.value_end);
        textEnd.setText(formatTime(endTime).replace("a.m", "AM").replace("p.m.","PM"));
        TextView textDuration = findViewById(R.id.value_duration);
        textDuration.setText(formatDuration(duration));
        TextView textDistance = findViewById(R.id.value_distance);
        textDistance.setText(fmt.format(distance)+" km");
        TextView textAvgPace = findViewById(R.id.value_avg_pace);
        textAvgPace.setText((formatAvgPace((long) avg_pace))+"/km");
        final TextView textNotes = findViewById(R.id.edit_text);
        textNotes.setText(notes);

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

        getSupportActionBar().setTitle((dayFmt.format(startTime)));
    }

    @Override
    protected void onPause() {
        super.onPause();

        final Intent intent = getIntent();
        String original = intent.getStringExtra("NOTES");
        TextView textNotes = findViewById(R.id.edit_text);
        String notes = textNotes.getText().toString();
        if(notes != null && original != null && original.compareTo(notes) != 0) {
            ArrayList<HistoryData> mHistoryDataList = MainActivity.jsonUtils.getHistoryData();
            HistoryData current = null;
            for (int i = 0; i < mHistoryDataList.size(); i++) {
                if (mHistoryDataList.get(i).getStartTime() == intent.getLongExtra("START_TIME", 0)) {
                    current = mHistoryDataList.get(i);
                }
            }
            MainActivity.jsonUtils.updateNotes(getApplicationContext(), current, notes);
            textNotes.setText(notes);
            intent.putExtra("NOTES", notes);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Intent intent = getIntent();
        TextView textNotes = findViewById(R.id.edit_text);
        String notes = null;
        ArrayList<HistoryData> mHistoryDataList = MainActivity.jsonUtils.getHistoryData();
        HistoryData current = null;
        for (int i = 0; i < mHistoryDataList.size(); i++) {
            if (mHistoryDataList.get(i).getStartTime() == intent.getLongExtra("START_TIME", 0)) {
                current = mHistoryDataList.get(i);
                notes = current.getNotes();
                break;
            }
        }
        if(notes != null) {
            textNotes.setText(notes);
            intent.putExtra("NOTES", notes);
        }
    }

    String formatDuration(long duration) {
        long second = (duration / 1000) % 60;
        long minute = (duration / (1000 * 60)) % 60;
        long hour = ((duration / (1000 * 60 * 60)) % 24);
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    String formatAvgPace(long pace) {
        long second = (pace / 1000) % 60;
        long minute = (pace / (1000 * 60)) % 60;
        return String.format("%02d:%02d", minute, second);
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
        outputformat.setTimeZone(TimeZone.getTimeZone("GMT-3"));
        try{
            date= df.parse(militaryFmt);
            output = outputformat.format(date);
        }catch(ParseException pe){
            pe.printStackTrace();
        }
        return output;
    }

}
