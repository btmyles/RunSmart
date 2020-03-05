package com.cs2063.runsmart;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.cs2063.runsmart.ui.run.RunFragment;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class LocationService extends Service {
    public static LocationManager locationManager;
    public MyLocationListener listener;


    @Override
    public void onCreate() {
        super.onCreate();
        // This idea comes from this site
        // https://hackernoon.com/android-location-tracking-with-a-service-80940218f561
        startForeground(12345678, getNotification());
    }

    @Override
    public void onStart(Intent intent, int startId) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, listener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        locationManager.removeUpdates(listener);
    }
/* I dont think this is ever used, but leaving it here just in case - Ben
    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }
*/
    private Notification getNotification() {
        NotificationChannel channel = new NotificationChannel(
                "channel_01",
                "My Channel",
                NotificationManager.IMPORTANCE_DEFAULT
        );

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        Notification.Builder builder = new Notification.Builder(getApplicationContext(), "channel_01");

        return builder.build();
    }

    public class MyLocationListener implements LocationListener {

        public void onLocationChanged(final Location loc) {
            double longitude = loc.getLongitude();
            double latitude = loc.getLatitude();
            RunFragment.addCoordinates(latitude, longitude);
            Log.i(TAG, "Location updated: " + latitude + " : " + longitude);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        }


        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }
    }

    public static Boolean isEnabled(Context c) {
        LocationManager testManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
        return testManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}