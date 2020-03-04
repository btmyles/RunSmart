package com.cs2063.runsmart;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.cs2063.runsmart.util.JsonUtils;
import com.cs2063.runsmart.util.LocationUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    public static JsonUtils jsonUtils;
    public static LocationUtils locationUtils;
    private final String TAG = "MainActivity.java";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_run, R.id.navigation_history, R.id.navigation_articles)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        createHistoryDirectory("history");

        Log.i(TAG, "Starting JSON Utils");
        jsonUtils = new JsonUtils(this);
        Log.i(TAG, "Done with JSON Utils");

        //LocationUtils
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationUtils locationUtils = new LocationUtils(locationManager);

    }

    private void createHistoryDirectory(String filename) {
        Context context = getApplicationContext();

        // This is not actually creating the required directory

        File folder = getFilesDir();
        File f = new File(folder, filename);
        Log.i("Main", f.getAbsolutePath());

        if (!f.exists()) {
            Log.i("Main", "File does not exist");
            if (!f.mkdir()) {
                Toast.makeText(context, "Unable to create folder: " + filename, Toast.LENGTH_SHORT).show();
            }
        }
        String[] arr = context.fileList();
        for (int i=0; i<arr.length; i++) {
            Log.i("main", arr[i]);
        }

        try {
            File outputFile = new File(f, "test.json");
            FileWriter writer = new FileWriter(outputFile);
            writer.append("Test file contents");
            Log.i("Main", "test.json has been written - File list below");
            String[] filelist = context.fileList();
            for (int i=0; i<arr.length; i++) {
                Log.i("main", arr[i]);
            }
            writer.flush();
            writer.close();
        }
        catch (IOException e) {
            Log.i("Main", e.toString());

        }

        try {
            File outputFile = new File(f, "test.json");
            FileInputStream is = new FileInputStream(outputFile);
            int content;
            Log.i("MAIN", "Starting to read from test.json");
            while ((content=is.read()) != -1) {
                Log.i("MAIN", Character.toString((char) content));
            }
            is.close();
        }
        catch (IOException e) {
            Log.i("Main", e.toString());
        }

        // create dummy file and write to history folder
        // history should show an empty list  if nothing is in the FS
        // consider sqlite
        //make read operation very efficient

    }

}
