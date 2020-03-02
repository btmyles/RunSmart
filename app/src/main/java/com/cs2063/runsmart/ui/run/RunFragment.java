package com.cs2063.runsmart.ui.run;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.cs2063.runsmart.LineLayerActivity;
import com.cs2063.runsmart.MainActivity;
import com.cs2063.runsmart.R;
import com.cs2063.runsmart.model.HistoryData;
import com.cs2063.runsmart.util.LocationUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

public class RunFragment extends Fragment {

    private final String TAG = "RunFragment";

    private RunViewModel runViewModel;
    private Button runButton;

    private LocationManager locationManager;
    private LocationUtils locationUtils;

    private long starttime;
    private long endtime;
    private static ArrayList<Double> latitudeList;
    private static ArrayList<Double> longitudeList;
    private double[] latitudeArray;
    private double[] longitudeArray;

    HistoryData historyData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        runViewModel =
                ViewModelProviders.of(this).get(RunViewModel.class);
        View root = inflater.inflate(R.layout.fragment_run, container, false);
        final TextView textView = root.findViewById(R.id.text_run);
        runViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        runButton = root.findViewById(R.id.start_run);
        runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRun();
            }
        });

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationUtils = new LocationUtils(locationManager);

        return root;
    }

    private void startRun() {
        // Check if location is enabled
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showLocationAlert();
            return;
        }
        if (runButton.getText().equals(getResources().getString(R.string.endrun_text))) {
            locationUtils.shutoff();

            // Get data from run
            endtime = Calendar.getInstance().getTimeInMillis();
            latitudeArray = list2double(latitudeList);
            longitudeArray = list2double(longitudeList);
            historyData = new HistoryData.Builder(starttime, endtime, latitudeArray, longitudeArray).build();

            // add this data to the JSON file
            MainActivity.jsonUtils.toJSon(getActivity(), historyData);

            runButton.setText(R.string.startrun_text);
            runButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_bg_round));
            Log.i(TAG, "End time = " + endtime);

            // Start map activity
            Intent intent = new Intent(getActivity().getApplicationContext(), LineLayerActivity.class);
            intent.putExtra("LATITUDE", historyData.getLatitude());
            intent.putExtra("LONGITUDE", historyData.getLongitude());
            startActivity(intent);
        } else {
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                showPermissionAlert();
                return;
            }
            locationUtils.turnon(getActivity());

            // Initialize coordinate lists
            latitudeList = new ArrayList<Double>();
            longitudeList = new ArrayList<Double>();
            starttime = Calendar.getInstance().getTimeInMillis();

            Toast.makeText(getActivity(), "Run started", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Start time = " + starttime);

            runButton.setText(R.string.endrun_text);
            runButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_bg_round_2));
        }
    }

    private void showPermissionAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Permission not enabled")
                .setMessage("Location is not allowed. Please adjust your permission settings for RunSmart")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
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
    }

    private double[] list2double(ArrayList<Double> list) {
        Double[] array = new Double[list.size()];
        double[] array2 = new double[list.size()];
        list.toArray(array);
        for (int i=0; i<list.size(); i++) {
            array2[i] = array[i];
        }
        return array2;
    }
}