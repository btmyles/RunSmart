package com.cs2063.runsmart.ui.history;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.cs2063.runsmart.R;
import com.cs2063.runsmart.Tools;
import com.cs2063.runsmart.model.HistoryData;
import com.cs2063.runsmart.util.JsonUtils;

import java.util.ArrayList;
import java.util.Random;

public class HistoryFragment extends Fragment {

    private static final String TAG = "HistoryFragment.java";
    private HistoryViewModel historyViewModel;

    TextView startTime;
    TextView endTime;
    TextView latitude;
    TextView longitude;
    TextView dataStart;
    TextView dataEnd;
    TextView dataLatitude;
    TextView dataLongitude;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        historyViewModel =
                ViewModelProviders.of(this).get(HistoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_history, container, false);
        final TextView textView = root.findViewById(R.id.text_history);
        historyViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        // Temporary textviews for testing the ability to read JSON file.
        startTime = (TextView) root.findViewById(R.id.text_start);
        endTime = (TextView) root.findViewById(R.id.text_end);
        latitude = (TextView) root.findViewById(R.id.text_latitude);
        longitude = (TextView) root.findViewById(R.id.text_longitude);
        dataStart = (TextView) root.findViewById(R.id.data_start);
        dataEnd = (TextView) root.findViewById(R.id.data_end);
        dataLatitude = (TextView) root.findViewById(R.id.data_latitude);
        dataLongitude = (TextView) root.findViewById(R.id.data_longitude);

        final Context context = getContext();

        // This should read the Json file in assets directory, copy it to
        // local storage, and parse the data into the historyList variable within jsonUtils
        Log.i(TAG, "Starting JSON Utils");
        final JsonUtils jsonUtils = new JsonUtils(context);
        Log.i(TAG, "Done with JSON Utils");
        try {
            ArrayList<HistoryData> mDataset = jsonUtils.getHistoryData();
            Log.i(TAG, "Created dataset arraylist. Accessing mDataset.get(0)");
            HistoryData history = mDataset.get(0);
            Log.i(TAG, "Successfully accessed first element of dataset");
            dataStart.setText(Long.toString(history.getStartTime()));
        }
        catch(Exception e)
        {
            Log.e(TAG, "Caught Exception.", e);
            Tools.exceptionToast(context, "Error: " + e.getMessage());
        }

        //Random setup
        Random rand = new Random();
        double[] testLat = {1.5, 1.6};
        double[] testLon = {2.5, 2.6};

        // This is what should happen at the end of a run:
        // A new HistoryData object is initialized with the data from the run
        // The new HistoryData object should have its attributes calculated
        // At some point the new HistoryData should be written to local storage in History.json
        final HistoryData testHistory = new HistoryData.Builder(rand.nextLong(), rand.nextLong(), testLat, testLon).build();
        testHistory.deriveAttributes();

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonUtils.toJSon(context, testHistory);
            }
        });


        return root;
    }
}