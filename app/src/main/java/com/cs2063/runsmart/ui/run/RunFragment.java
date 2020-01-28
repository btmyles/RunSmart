package com.cs2063.runsmart.ui.run;

import android.os.Bundle;
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

public class RunFragment extends Fragment {

    private RunViewModel runViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        runViewModel =
                ViewModelProviders.of(this).get(RunViewModel.class);
        View root = inflater.inflate(R.layout.fragment_run, container, false);
        final TextView textView = root.findViewById(R.id.text_run);
        runViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}