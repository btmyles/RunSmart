package com.cs2063.runsmart.util;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.cs2063.runsmart.ui.run.RunFragment;

public class LocationUtils {

    private LocationManager locationManager;
    private final String TAG = "LocationUtils";

    public LocationUtils(LocationManager locationManager) {
        this.locationManager = locationManager;

    }

    public void shutoff() {
        locationManager.removeUpdates(locationListenerGPS);
    }

    public void turnon(Context context) {
        // This statement is already being checked before calling the turnon method. No operations are needed here.
        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5 * 1000, 0, locationListenerGPS);
    }

    private final LocationListener locationListenerGPS = new LocationListener() {
        public void onLocationChanged(Location location) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            // TODO: return the location data to RunFragment or find another way to
            //  incrementally store this data until the run is over.
            RunFragment.addCoordinates(latitude, longitude);
            Log.i(TAG, "Location updated: " + latitude + " : " + longitude);
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    //Here we just pass the data along to wherever it should be used.
//                    longitudeValue.setText(longitude + "");
//                    latitudeValue.setText(latitude + "");
//                    Toast.makeText(MainActivity.this, "Network Provider update", Toast.LENGTH_SHORT).show();
//                }
//            });
        }
        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }
        @Override
        public void onProviderEnabled(String s) {
        }
        @Override
        public void onProviderDisabled(String s) {
        }
    };
}
