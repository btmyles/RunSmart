package com.cs2063.runsmart.ui.history;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HistoryFragment extends Fragment {

    private static final String TAG = "HistoryFragment.java";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

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

        private MyAdapter(ArrayList<HistoryData> myDataset) {
            mDataset = myDataset;
        }

        // ViewHolder represents an individual item to display. In this case
        // it will just be a single TextView (displaying the title of a course)
        // but RecyclerView gives us the flexibility to do more complex things
        // (e.g., display an image and some text).
        private class ViewHolder extends RecyclerView.ViewHolder {
            private TextView mTextView;
            private ViewHolder(TextView v) {
                super(v);
                mTextView = v;
            }
        }

        // The inflate method of the LayoutInflater class can be used to obtain the
        // View object corresponding to an XML layout resource file. Here
        // onCreateViewHolder inflates the TextView corresponding to geodata_list_content.xml
        // and uses it to instantiate a ViewHolder.
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            TextView v = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.geodata_list_content, parent, false);

            return new ViewHolder(v);
        }

        // onBindViewHolder binds a ViewHolder to the data at the specified
        // position in mDataset
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            //  Get the Course at index position in mDataSet
            //  (Hint: you might need to declare this variable as final.)
            final HistoryData currentHistory = mDataset.get(position);

            //  Set the TextView in the ViewHolder (holder) to be the title
            holder.mTextView.setText(Long.toString(currentHistory.getStartTime()));

            //  Set the onClickListener for the TextView in the ViewHolder (holder) such
            //  that when it is clicked, it creates an explicit intent to launch DetailActivity
            //  HINT: You will need to put two extra pieces of information in this intent:
            //      The Course title and it's description
            holder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    // We should send every part of the historydata object
                    Intent intent = new Intent(getActivity().getApplicationContext(), HistoryActivity.class);
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
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }
}