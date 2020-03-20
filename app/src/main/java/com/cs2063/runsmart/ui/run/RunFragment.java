package com.cs2063.runsmart.ui.run;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.cs2063.runsmart.LocationService;
import com.cs2063.runsmart.MainActivity;
import com.cs2063.runsmart.R;
import com.cs2063.runsmart.model.HistoryData;
import com.cs2063.runsmart.ui.history.HistoryDetailActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class RunFragment extends Fragment {

    private final String TAG = "RunFragment";
    private static Context c;

    SharedPreferences sharedPreferences;
    private static final String MyPREFERENCES = "MyPrefs" ;
    private static final String ButtonText = "buttonKey";
    private static final String ChronometerBase = "baseKey";

    private static Button runButton;
    private static Chronometer chronometer;

    private static long starttime;
    private long endtime;
    private static ArrayList<Double> latitudeList;
    private static ArrayList<Double> longitudeList;
    private double[] latitudeArray;
    private double[] longitudeArray;

    HistoryData historyData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_run, container, false);
        c = getActivity().getApplicationContext();

        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        runButton = root.findViewById(R.id.start_run);
        runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runButtonPressed();
            }
        });

        chronometer = root.findViewById(R.id.chronometer);
        enableChronometer(SystemClock.elapsedRealtime());

        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        return root;
    }

    private void enableChronometer(long base) {
        chronometer.setFormat("00:%s");
        chronometer.setBase(base);

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                int h   = (int)(time /3600000);
                int m = (int)(time - h*3600000)/60000;
                int s= (int)(time - h*3600000- m*60000)/1000 ;
                String hh = h < 10 ? "0"+h: h+"";
                String mm = m < 10 ? "0"+m: m+"";
                String ss = s < 10 ? "0"+s: s+"";
                chronometer.setText(hh+":"+mm+":"+ss);
            }
        });
    }

    private void runButtonPressed() {

        Log.i(TAG, "Run button pressed. Button text = " + runButton.getText());

        if (runButton.getText().equals(getResources().getString(R.string.endrun_text)))
            endRun();
        else if(runButton.getText().equals(getResources().getString(R.string.loadingrun_text)))
            cancelRun();
        else
            startRun();
    }

    private void cancelRun() {
        // In development: foreground service
        Intent locationIntent = new Intent(getActivity().getApplicationContext(), LocationService.class);
        getActivity().getApplicationContext().stopService(locationIntent);

        runButton.setText(R.string.startrun_text);
        runButton.setBackgroundResource(R.drawable.button_bg_round_blue);

        Log.i(TAG, "Cancel Run");
    }

    private void startRun() {
        Log.i(TAG, "Start run");
        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        else if (!LocationService.isEnabled(getActivity().getApplicationContext())) {
            showLocationAlert();
            return;
        }

        // Foreground service
        Intent locationIntent = new Intent(getActivity().getApplicationContext(), LocationService.class);
        getActivity().getApplicationContext().startService(locationIntent);

        // Initialize coordinate lists
        latitudeList = new ArrayList<Double>();
        longitudeList = new ArrayList<Double>();

        Toast.makeText(getActivity(), "Setting up GPS", Toast.LENGTH_SHORT).show();

        //Turn Yellow
        runButton.setText(R.string.loadingrun_text);
        runButton.setBackgroundResource(R.drawable.button_bg_round_yellow);
    }


    private void endRun() {
        // foreground service
        Intent locationIntent = new Intent(getActivity().getApplicationContext(), LocationService.class);
        getActivity().getApplicationContext().stopService(locationIntent);

        // Get data from run
        endtime = Calendar.getInstance().getTimeInMillis();
        latitudeArray = list2double(latitudeList);
        longitudeArray = list2double(longitudeList);
        historyData = new HistoryData.Builder(starttime, endtime, latitudeArray, longitudeArray).build();

        // add this data to the JSON file
        MainActivity.jsonUtils.toJSon(getActivity(), historyData);

        runButton.setText(R.string.startrun_text);
        runButton.setBackgroundResource(R.drawable.button_bg_round_blue);

        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.stop();

        Log.i(TAG, "End time = " + endtime);

        // Start map activity
        Intent intent = new Intent(getActivity().getApplicationContext(), HistoryDetailActivity.class);
        intent.putExtra("START_TIME", historyData.getStartTime());
        intent.putExtra("END_TIME", historyData.getEndTime());
        intent.putExtra("DURATION", historyData.getDuration());
        intent.putExtra("DISTANCE", historyData.getDistance());
        intent.putExtra("LONGITUDE", historyData.getLongitude());
        intent.putExtra("LATITUDE", historyData.getLatitude());
        intent.putExtra("AVG_PACE", historyData.getAvgPace());
        startActivity(intent);
    }

    private void resumeRun(String buttonText, long chronometerBase) {
        Log.i(TAG, "resumeRun");
        if (buttonText.equals(getString(R.string.endrun_text))) {
            // Turn red
            runButton.setText(R.string.endrun_text);
            runButton.setBackgroundResource(R.drawable.button_bg_round_red);
            // Resume chronometer
            enableChronometer(chronometerBase);
            chronometer.start();
        }
        else {
            //Turn Yellow
            runButton.setText(R.string.loadingrun_text);
            runButton.setBackgroundResource((R.drawable.button_bg_round_yellow));
        }
    }

    private void showLocationAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Enable Location")
                .setMessage("Your Location Settings is turned off. Please enable location to use this feature")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent locSettingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(locSettingsIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    public static void addCoordinates(double latitude, double longitude) {
        latitudeList.add(latitude);
        longitudeList.add(longitude);


        if (runButton.getText().equals(c.getString(R.string.loadingrun_text))) {

            runButton.setText(R.string.endrun_text);
            runButton.setBackgroundResource((R.drawable.button_bg_round_red));

            starttime = Calendar.getInstance().getTimeInMillis();
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
        }
    }

    private double[] list2double(ArrayList<Double> list) {
        Double[] array = new Double[list.size()];
        double[] array2 = new double[list.size()];
        list.toArray(array);
        for (int i = 0; i < list.size(); i++) {
            array2[i] = array[i];
        }
        return array2;
    }

    @Override
    public void onPause() {
        super.onPause();
        String text = runButton.getText().toString();
        long base = chronometer.getBase();

        // Do shared preferences here
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ButtonText, text);
        editor.putLong(ChronometerBase, base);
        editor.commit();
        Log.i(TAG, "onPause: buttonText = " + text);
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
        String prefString = sharedPreferences.getString(ButtonText, "None");
        long prefBase = sharedPreferences.getLong(ChronometerBase, 0);
        if (prefString.equals(getString(R.string.endrun_text)) || prefString.equals(getString(R.string.loadingrun_text))) {
            Log.i(TAG, "resuming run");
            resumeRun(prefString, prefBase);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }
}

