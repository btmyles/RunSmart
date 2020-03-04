package com.cs2063.runsmart.ui.history;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.cs2063.runsmart.MainActivity;
import com.cs2063.runsmart.R;
import com.cs2063.runsmart.model.HistoryData;
import com.cs2063.runsmart.util.JsonUtils;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class HistoryFragment extends Fragment {

    private static final String TAG = "HistoryFragment.java";
    private ImageView deleteIcon;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        Log.i(TAG, "running onCreateView()");
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        JsonUtils jutils = MainActivity.jsonUtils;
        ArrayList<HistoryData> mHistoryDataList = jutils.getHistoryData();

        // RecyclerView setup
        RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        MyAdapter mAdapter = new MyAdapter(mHistoryDataList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        Log.i(TAG, "finishing onCreateView()");

        return root;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private final ArrayList<HistoryData> mDataset;
        private final Date date = new Date();

        private MyAdapter(ArrayList<HistoryData> myDataset) {
            mDataset = myDataset;
        }

        // ViewHolder represents an individual item to display. In this case
        // it will just be a single TextView (displaying the title of a course)
        // but RecyclerView gives us the flexibility to do more complex things
        // (e.g., display an image and some text).
        private class ViewHolder extends RecyclerView.ViewHolder {
            private RelativeLayout relativeLayout;
            private TextView start;
            private TextView distance;
            private TextView duration;
            private ViewHolder(View v) {
                super(v);
                start = v.findViewById(R.id.history_item_start);
                distance = v.findViewById(R.id.history_item_distance);
                duration = v.findViewById(R.id.history_item_duration);
                deleteIcon=v.findViewById(R.id.item_delete);
                relativeLayout = v.findViewById(R.id.history_item_layout);
            }
        }

        // The inflate method of the LayoutInflater class can be used to obtain the
        // View object corresponding to an XML layout resource file. Here
        // onCreateViewHolder inflates the TextView corresponding to geodata_list_content.xml
        // and uses it to instantiate a ViewHolder.
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.geodata_list_content, parent, false);

            return new ViewHolder(v);
        }

        // onBindViewHolder binds a ViewHolder to the data at the specified
        // position in mDataset
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            //  Get the Course at index position in mDataSet
            //  (Hint: you might need to declare this variable as final.)
            final HistoryData currentHistory = mDataset.get(position);

            date.setTime(currentHistory.getStartTime());
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateFormatted = formatter.format(date);

            long durationInMillis = currentHistory.getDuration();
            long second = (durationInMillis / 1000) % 60;
            long minute = (durationInMillis / (1000 * 60)) % 60;
            long hour = (durationInMillis / (1000 * 60 * 60)) % 24;
            String durationFormatted = String.format("%02d:%02d:%02d", hour, minute, second);

            //  Set the TextView in the ViewHolder (holder) to be the title
            holder.start.setText(dateFormatted);
            holder.distance.setText(Double.toString(currentHistory.getDistance()));
            holder.duration.setText(durationFormatted);

            //  Set the onClickListener for the TextView in the ViewHolder (holder) such
            //  that when it is clicked, it creates an explicit intent to launch DetailActivity
            holder.start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    // We should send every part of the historydata object
                    Intent intent = new Intent(getActivity().getApplicationContext(), HistoryDetailActivity.class);
                    intent.putExtra("START_TIME", currentHistory.getStartTime());
                    intent.putExtra("END_TIME", currentHistory.getEndTime());
                    intent.putExtra("DURATION", currentHistory.getDuration());
                    intent.putExtra("DISTANCE", currentHistory.getDistance());
                    intent.putExtra("LONGITUDE", currentHistory.getLongitude());
                    intent.putExtra("LATITUDE", currentHistory.getLatitude());
                    intent.putExtra("AVG_PACE", currentHistory.getAvgPace());
                    startActivity(intent);
                }
            });

            // Set onClickListener so that when the delete button is clicked the row is removed
            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeAt(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        public void removeAt(int position) {
            mDataset.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mDataset.size());
        }



    }
}