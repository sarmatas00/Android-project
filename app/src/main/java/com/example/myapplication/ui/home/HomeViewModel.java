package com.example.myapplication.ui.home;

import android.graphics.Color;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.R;


import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;


public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;


    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
        System.out.println("HomeViewModelCreated");

    }

    public LiveData<String> getText() {
        return mText;
    }

}