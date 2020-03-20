package com.cs2063.runsmart.ui.articles;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cs2063.runsmart.R;

import java.net.URL;

public class ArticlesFragment extends Fragment {
    private String TAG = "ArticlesFragment.java";
    private URL[] links = new URL[6];
    private String[] titles = new String[6];

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        Log.i(TAG, "onCreateView");
        View root = inflater.inflate(R.layout.fragment_articles, container, false);
        try{
            Log.i(TAG, "In Try Statement");
            links[0] = new URL(getString(R.string.url_run1));
            titles[0] = getString(R.string.url1);
            links[1] = new URL(getString(R.string.url_run2));
            titles[1] = getString(R.string.url2);
            links[2] = new URL(getString(R.string.url_fitness1));
            titles[2] = getString(R.string.url3);
            links[3] = new URL(getString(R.string.url_fitness2));
            titles[3] = getString(R.string.url4);
            links[4] = new URL(getString(R.string.url_food1));
            titles[4] = getString(R.string.url5);
            links[5] = new URL(getString(R.string.url_food2));
            titles[5] = getString(R.string.url6);
            Log.i(TAG, "In End Of Try Statement");
        }
        catch(Exception e)
        {
            Log.i(TAG, "Catching Exception");
            Toast.makeText(getActivity().getApplicationContext(), "Error getting URLs", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Error getting URLs in ArticlesFragment");
        }

        Log.i(TAG, "Creating RecyclerView");
        RecyclerView recyclerView = root.findViewById(R.id.articles_recycler);
        Log.i(TAG, "Creating Layout Manager");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        Log.i(TAG, "Setting Layout Manager");
        recyclerView.setLayoutManager(layoutManager);
        Log.i(TAG, "Passing links to new ArticlesFragment.MyAdapter");
        ArticlesFragment.MyAdapter mAdapter = new ArticlesFragment.MyAdapter(links, titles);
        Log.i(TAG, "Setting Adapter");
        recyclerView.setAdapter(mAdapter);
        Log.i(TAG, "Setting Animator");
        try {
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            Log.i(TAG, "Finished Setting Animator");
            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        }
        catch(Exception e)
        {
            Log.i(TAG, "Catching Animator Error");
            Toast.makeText(getActivity().getApplicationContext(), "Error setting Animator", Toast.LENGTH_LONG).show();
            Log.e(TAG, e.getStackTrace().toString());
        }

        Log.i(TAG, "Returning root");
        return root;
    }

    public class MyAdapter extends RecyclerView.Adapter<ArticlesFragment.MyAdapter.ViewHolder> {
        private final URL[] mDataset;
        private final String[] mTitles;

        private MyAdapter(URL[] urls, String[] titles) {
            Log.i(TAG, "Creating mDataset in MyAdapter");
            mDataset = urls;
            mTitles = titles;
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
        public ArticlesFragment.MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                       int viewType) {
            Log.i(TAG, "In onCreateViewHolder");
            TextView v = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.url_list_content, parent, false);
            Log.i(TAG, "Creating new MyAdapter.ViewHolder");
            return new ArticlesFragment.MyAdapter.ViewHolder(v);
        }

        // onBindViewHolder binds a ViewHolder to the data at the specified
        // position in mDataset
        @Override
        public void onBindViewHolder(ArticlesFragment.MyAdapter.ViewHolder holder, int position) {

            //  Get the Course at index position in mDataSet
            //  (Hint: you might need to declare this variable as final.)
            Log.i(TAG, "Getting Current URL to open in Browser");
            final URL currentURL = mDataset[position];

            //  Set the TextView in the ViewHolder (holder) to be the title
            holder.mTextView.setText(mTitles[position]);

            //  Set the onClickListener for the TextView in the ViewHolder (holder) such
            //  that when it is clicked, it creates an explicit intent to launch DetailActivity
            //  HINT: You will need to put two extra pieces of information in this intent:
            //      The Course title and it's description
            Log.i(TAG, "Setting OnClickListener");
            holder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    // We should send every part of the historydata object
                    Log.i(TAG, "In onClick of setOnClickListener");
                    Log.i(TAG, "Parsing URI: " + currentURL.toString());
                    Uri uri = Uri.parse(currentURL.toString());
                    Log.i(TAG, "Creating intent to open web browser");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    Log.i(TAG, "Starting Intent, should open Web Browser");
                    startActivity(Intent.createChooser(intent, "Browse with"));
                    Log.i(TAG, "If you see this, the browser probably did not open!!!");
                }
            });
        }

        @Override
        public int getItemCount() {
            Log.i(TAG, "Getting Item Count (" + mDataset.length + ")");
            return mDataset.length;
        }
    }
}