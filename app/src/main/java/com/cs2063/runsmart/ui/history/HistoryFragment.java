package com.cs2063.runsmart.ui.history;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.cs2063.runsmart.R;
import com.cs2063.runsmart.model.HistoryData;
import com.cs2063.runsmart.util.JsonUtils;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private static final String TAG = "HistoryFragment.java";
    private HistoryViewModel historyViewModel;

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

        TextView startTime = (TextView) root.findViewById(R.id.text_start);
        TextView endTime = (TextView) root.findViewById(R.id.text_end);
        TextView latitude = (TextView) root.findViewById(R.id.text_latitude);
        TextView longitude = (TextView) root.findViewById(R.id.text_longitude);
        TextView dataStart = (TextView) root.findViewById(R.id.data_start);
        TextView dataEnd = (TextView) root.findViewById(R.id.data_end);
        TextView dataLatitude = (TextView) root.findViewById(R.id.data_latitude);
        TextView dataLongitude = (TextView) root.findViewById(R.id.data_longitude);

        Context context = getContext();

        Log.i(TAG, "Starting JSON Utils");
        JsonUtils jsonUtils = new JsonUtils(context);
        Log.i(TAG, "Done with JSON Utils");

        ArrayList<HistoryData> mDataset = jsonUtils.getHistoryData();

        HistoryData history = mDataset.get(0);


        startTime.setText("TESTING STRING");
        dataStart.setText(Long.toString(history.getStartTime()));


        return root;
    }
}