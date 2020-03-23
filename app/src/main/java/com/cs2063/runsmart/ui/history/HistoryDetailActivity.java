package com.cs2063.runsmart.ui.history;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

        final Intent intent = getIntent();
        long startTime = intent.getLongExtra("START_TIME", 0);
        long endTime = intent.getLongExtra("END_TIME", 0);
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
        textAvgPace.setText(fmt.format((avg_pace/3.6))+" m/s");
        Log.i(TAG, "onCreate - getting notes view by id");
        final TextView textNotes = findViewById(R.id.edit_text);
        textNotes.setText(notes);
        textNotes.addTextChangedListener(new TextWatcher() {
            boolean _ignore = false;
            String newNotes = "";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (_ignore)
                    return;

                _ignore = true; // prevent infinite loop
                newNotes = textNotes.getText().toString();
                textNotes.setText(newNotes);
                //needs to save the newNotes back to json
                intent.putExtra("NOTES", newNotes);
                Log.i(TAG, "TextWatcher - afterTextChanged: " + newNotes);
                _ignore = false; //TextWatcher starts to listen again.
            }
        });

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
