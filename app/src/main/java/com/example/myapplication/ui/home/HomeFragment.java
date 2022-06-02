package com.example.myapplication.ui.home;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.HomeViewModel;
import com.example.myapplication.MyDBHandler;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentHomeBinding;


import org.eazegraph.lib.models.PieModel;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private  PieChart pieChart;
    private String username;
    private MyDBHandler db;
    private Map<String,String> map;


    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        db= new MyDBHandler(getActivity(), null, null, 1);
        map=new HashMap<>();





        // This callback will only be called when MyFragment is at least Started.
        //This de-activates back button when user is in home page
            OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
                @Override
                public void handleOnBackPressed() {
                    // Handle the back button event
                }
            };
            requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

            // The callback can be enabled or disabled here or in handleOnBackPressed()

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pieChart = view.findViewById(R.id.piechart);




        //get connected user's username from activity
        if(getActivity()!=null) {
            HomeViewModel homeViewModel =
                    new ViewModelProvider(getActivity()).get(HomeViewModel.class);

            homeViewModel.getText().observe(getViewLifecycleOwner(), s -> {
                this.username=s;

                map=db.findUserDataForChart(username);


                //sort hashmap by value
                Map<String,String> sortedMap=map.entrySet().stream()
                        .sorted((k1, k2) -> Float.compare(Float.valueOf(k1.getValue()),Float.valueOf(k2.getValue()))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (k1, k2) -> k1, LinkedHashMap::new));



                setupPieChart();
                loadPieChartData(sortedMap);


            });

        }





    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }




    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Spending by Category");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);
/*
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setTextSize(15);
        l.setDrawInside(false);
        l.setEnabled(true);
        */
    }

    private float getPercentage(float value,float total){
        BigDecimal number= new BigDecimal(Float.toString(value/total));
        return number.floatValue();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadPieChartData(Map<String,String> sortedMap) {

        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        Set<String> setKeys= sortedMap.keySet();
        List<String> keys=new ArrayList<String>(setKeys);

        ListIterator<String> it=keys.listIterator(keys.size());






        float total=0;
        if(map.size()<=5){
            while(it.hasPrevious()){
                total+=Float.parseFloat((String) map.get(it.previous()));
            }
        }else{
            int i=0;
            while(it.hasPrevious()){
                if(i>=5){
                    break;
                }
                float currValue=Float.parseFloat((String) map.get(it.previous()));
                total+=currValue;
                i++;
                if(currValue==0){
                    break;
                }
            }
        }

        it=keys.listIterator(keys.size());
        int i=0;
        ConstraintLayout layout=(ConstraintLayout) (getView()).findViewById(R.id.dataSet);

        int previous=(getView()).findViewById(R.id.homeDataContainer).getId();
        while (it.hasPrevious()){
            String item=  it.previous();
            float currValue=Float.parseFloat(map.get(item));
            if(currValue==0){
                break;
            }
            if(i<5){
                pieEntries.add(new PieEntry(getPercentage(currValue,total), item));
                i++;
            }
                TextView stat=(new TextView(getActivity()));
                ConstraintLayout.LayoutParams params=new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                int topToBottom=previous;
                params.leftToLeft=layout.getId();
                params.topToBottom=topToBottom;

                params.rightToRight=layout.getId();

                previous=View.generateViewId();
                stat.setId(previous);
                stat.setLayoutParams(params);
                stat.setText(item+" : "+currValue);
                stat.setTextSize(12);
                stat.setTextColor(Color.BLACK);
                stat.setBackgroundColor(0xff66ff66);
                layout.addView(stat);



        }







        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        System.out.println(pieEntries.size());
        PieDataSet dataSet = new PieDataSet(pieEntries,"Expenses");
        dataSet.setColors(colors);


        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }




}