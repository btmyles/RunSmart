package com.cs2063.runsmart.ui.history;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HistoryFragment extends Fragment {

    private static final String TAG = "HistoryFragment.java";
    private HistoryViewModel historyViewModel;

    private ArrayList<HistoryData> mHistoryDataList;
    private JsonUtils jutils;
    private RecyclerView.LayoutManager layoutManager;
    private MyAdapter mAdapter;
    private RecyclerView view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //historyViewModel =
        //        ViewModelProviders.of(this).get(HistoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_history, container, false);
        jutils = MainActivity.jsonUtils;
        mHistoryDataList = jutils.getHistoryData();
        view = (RecyclerView) root.findViewById(R.id.geodata_list);
        layoutManager = new LinearLayoutManager(getActivity());
        view.setLayoutManager(layoutManager);
        mAdapter = new MyAdapter(mHistoryDataList);
        view.setAdapter(mAdapter);
        view.setItemAnimator(new DefaultItemAnimator());
        //setupRecyclerView(view);
        //final TextView textView = root.findViewById(R.id.text_history);
        /*
        historyViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        // Is this the right way to get context to be used for application storage??
        //final Context context = getContext();

        // This should read the Json file in assets directory, copy it to
        // local storage, and parse the data into the historyList variable within jsonUtils

        //ArrayList<HistoryData> mDataset = MainActivity.jsonUtils.getHistoryData();

        // This is what should happen at the end of a run:
        // A new HistoryData object is initialized with the data from the run
        // The new HistoryData object should have its attributes calculated
        // At some point the new HistoryData should be written to local storage in History.json




        return root;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private final ArrayList<HistoryData> mDataset;

        public MyAdapter(ArrayList<HistoryData> myDataset) {
            mDataset = myDataset;
        }

        // ViewHolder represents an individual item to display. In this case
        // it will just be a single TextView (displaying the title of a course)
        // but RecyclerView gives us the flexibility to do more complex things
        // (e.g., display an image and some text).
        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextView;
            public ViewHolder(TextView v) {
                super(v);
                mTextView = v;
            }
        }

        // The inflate method of the LayoutInflater class can be used to obtain the
        // View object corresponding to an XML layout resource file. Here
        // onCreateViewHolder inflates the TextView corresponding to item_layout.xml
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
            // TODO 3
            //  Get the Course at index position in mDataSet
            //  (Hint: you might need to declare this variable as final.)
            final HistoryData currentHistory = mDataset.get(position);
            // TODO 4
            //  Set the TextView in the ViewHolder (holder) to be the title for this Course
            holder.mTextView.setText(currentHistory.getStartTime() + "");
            // TODO 5
            //  Set the onClickListener for the TextView in the ViewHolder (holder) such
            //  that when it is clicked, it creates an explicit intent to launch DetailActivity
            //  HINT: You will need to put two extra pieces of information in this intent:
            //      The Course title and it's description
            holder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(getActivity().getApplicationContext(), HistoryActivity.class);
                    String hisStart = currentHistory.getStartTime() + "";
                    String hisLong = currentHistory.getLongitude() + "";
                    String hisLat = currentHistory.getLatitude() + "";
                    intent.putExtra("START_TIME", hisStart);
                    intent.putExtra("LONGITUDE", hisLong);
                    intent.putExtra("LATITUDE", hisLat);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }
    /*
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(mHistoryDataList));
    }
    */


    /*
    private class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<HistoryData> mValues;

        public SimpleItemRecyclerViewAdapter(List<HistoryData> data) {
            mValues = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.geodata_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mHistoryData = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).getLongitude().toString());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /**
                     * Setting the data to be sent to the Detail portion of the template.
                     * Here, we send the title, longitude, and latitude of the Earthquake
                     * that was clicked in the RecyclerView. The Detail Activity/Fragment
                     * will then display this information. Condition check is whether we
                     * are twoPane on a Tablet, which varies how we pass arguments to the
                     * participating activity/fragment.

                    long startTimeLong = holder.mHistoryData.getStartTime();
                    String lng = holder.mHistoryData.getLongitude().toString();
                    String lat = holder.mHistoryData.getLatitude().toString();

                    //is this context fine?
                    Intent intent = new Intent(getContext(), HistoryActivity.class);
                    //set the intent's key value pairs
                    intent.putExtra(startTime, startTimeLong);
                    intent.putExtra(GeoDataDetailFragment.LNG, lng);
                    intent.putExtra(GeoDataDetailFragment.LAT, lat);
                    startActivity(intent);


                }
            });
        }
        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public HistoryData mHistoryData;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = view.findViewById(R.id.id);
                mContentView = view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
    */
}