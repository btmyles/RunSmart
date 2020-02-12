package com.cs2063.runsmart.ui.articles;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ArticlesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ArticlesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is articles fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}