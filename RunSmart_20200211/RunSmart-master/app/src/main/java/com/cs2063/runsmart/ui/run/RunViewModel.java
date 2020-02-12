package com.cs2063.runsmart.ui.run;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RunViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RunViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("We need a 'Start Run' activity");
    }

    public LiveData<String> getText() {
        return mText;
    }
}