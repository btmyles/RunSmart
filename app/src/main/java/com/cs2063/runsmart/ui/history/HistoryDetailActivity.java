package com.cs2063.runsmart.ui.history;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cs2063.runsmart.LineLayerActivity;
import com.cs2063.runsmart.MainActivity;
import com.cs2063.runsmart.R;
import com.cs2063.runsmart.util.JsonUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
        textStart.setText((formatTime(startTime)));
        // This line was at the end of each section for the textviews. Might be necessary when we start scrolling
        //textStart.setMovementMethod(new ScrollingMovementMethod());
        TextView textEnd = findViewById(R.id.value_end);
        textEnd.setText(formatTime(endTime));
        TextView textDuration = findViewById(R.id.value_duration);
        textDuration.setText(formatDuration(duration));
        TextView textDistance = findViewById(R.id.value_distance);
        textDistance.setText(Double.toString(distance));
        TextView textAvgPace = findViewById(R.id.value_avg_pace);
        textAvgPace.setText(Double.toString(avg_pace));
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

        ImageButton deleteButton = findViewById(R.id.button_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
        });


        // This should be
        getSupportActionBar().setTitle((formatTime(startTime)));
    }
    String formatDuration(long duration) {
        long second = (duration / 1000) % 60;
        long minute = (duration / (1000 * 60)) % 60;
        long hour = ((duration / (1000 * 60 * 60)) % 24);
        //long hour= TimeUnit.MILLISECONDS.toSeconds(duration)%24;
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }
    String formatTime(long duration) {
        long second = (duration / 1000) % 60;
        long minute = (duration / (1000 * 60)) % 60;
        long hour = ((duration / (1000 * 60 * 60)) % 24 - 4) % 24;

        return stdFmt(String.format("%02d:%02d:%02d", hour, minute, second));
    }

    String stdFmt(String militaryFmt){
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        //Date/time pattern of desired output date
        DateFormat outputformat = new SimpleDateFormat("hh:mm:ss aa");
        Date date = null;
        String output = null;
        try{
            //Conversion of input String to date
            date= df.parse(militaryFmt);
            //old date format to new date format
            output = outputformat.format(date);
            System.out.println(output);
        }catch(ParseException pe){
            pe.printStackTrace();
        }
        return output;
    }

}
