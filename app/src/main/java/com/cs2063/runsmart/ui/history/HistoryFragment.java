package com.cs2063.runsmart.ui.history;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.cs2063.runsmart.MainActivity;
import com.cs2063.runsmart.R;
import com.cs2063.runsmart.model.HistoryData;
import com.cs2063.runsmart.util.JsonUtils;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class HistoryFragment extends Fragment {

    private static final String TAG = "HistoryFragment.java";
    private ImageView deleteIcon;
    private static DecimalFormat fmt= new DecimalFormat("######.##");
    SimpleDateFormat dayFmt = new SimpleDateFormat("yyyy/MM/dd ");



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
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
            private TextView start;
            private TextView distance;
            private TextView duration;
            private TextView clickView;
            private ViewHolder(View v) {
                super(v);
                clickView=v.findViewById(R.id.clickView);
                start = v.findViewById(R.id.history_item_start);
                distance = v.findViewById(R.id.history_item_distance);
                duration = v.findViewById(R.id.history_item_duration);
                deleteIcon=v.findViewById(R.id.item_delete);
                deleteIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked
                                        final HistoryData currentHistory = mDataset.get(getAdapterPosition());
                                        removeAt(getAdapterPosition());

                                        // Delete from JSON
                                        Boolean deleteSuccess = MainActivity.jsonUtils.delete(getActivity().getApplicationContext(), currentHistory);

                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                    }
                });
            }
        }

        // The inflate method of the LayoutInflater class can be used to obtain the
        // View object corresponding to an XML layout resource file. Here
        // onCreateViewHolder inflates the TextView corresponding to geodata_list_content.xml
        // and uses it to instantiate a ViewHolder.

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.geodata_list_content, parent, false);
            return new ViewHolder(v);
        }


        // onBindViewHolder binds a ViewHolder to the data at the specified position in mDataset
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            //  Get the Course at index position in mDataSet
            final HistoryData currentHistory = mDataset.get(position);

            //  Set the TextView in the ViewHolder (holder) to be the title
            holder.start.setText(dayFmt.format(currentHistory.getStartTime())+"- "+formatTime(currentHistory.getStartTime()).replace("a.m", "AM").replace("p.m.","PM"));
            holder.distance.setText(fmt.format(currentHistory.getDistance())+" km");
            holder.duration.setText(formatDuration(currentHistory.getDuration()));

            final Intent intent = new Intent(getActivity().getApplicationContext(), HistoryDetailActivity.class);
            intent.putExtra("START_TIME", currentHistory.getStartTime());
            intent.putExtra("END_TIME", currentHistory.getEndTime());
            intent.putExtra("DURATION", currentHistory.getDuration());
            intent.putExtra("DISTANCE", currentHistory.getDistance());
            intent.putExtra("LONGITUDE", currentHistory.getLongitude());
            intent.putExtra("LATITUDE", currentHistory.getLatitude());
            intent.putExtra("AVG_PACE", currentHistory.getAvgPace());
            intent.putExtra("NOTES", currentHistory.getNotes());

            //  Set the onClickListener for the TextView in the ViewHolder (holder) such that when it is clicked, it creates an explicit intent to launch DetailActivity
            holder.start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){

                    startActivity(intent);
                }
            });
            holder.duration.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){

                    startActivity(intent);
                }
            });
            holder.distance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){

                    startActivity(intent);
                }
            });
            holder.clickView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){

                    startActivity(intent);
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
    String formatDuration(long duration) {
        long second = (duration / 1000) % 60;
        long minute = (duration / (1000 * 60)) % 60;
        long hour = ((duration / (1000 * 60 * 60)) % 24);
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }


    String formatTime(long duration) {
        long second = (duration / 1000) % 60;
        long minute = (duration / (1000 * 60)) % 60;
        long hour = ((duration / (1000 * 60 * 60)) % 24 - 4) % 24;

        return stdFmt(String.format("%02d:%02d:%02d", hour, minute, second));
    }
    String stdFmt(String militaryFmt){
        DateFormat df = new SimpleDateFormat("H:mm:ss");
        DateFormat outputformat = new SimpleDateFormat("h:mm:ss aa");
        outputformat.setTimeZone(TimeZone.getTimeZone("GMT-3"));
        Date date;
        String output = null;
        try{
            date= df.parse(militaryFmt);
            output = outputformat.format(date);
        }catch(ParseException pe){
            pe.printStackTrace();
        }
        return output;
    }
}
