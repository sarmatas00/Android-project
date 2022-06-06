package com.example.myapplication.ui.home;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.HomeViewModel;
import com.example.myapplication.Item;
import com.example.myapplication.ItemAdapter;
import com.example.myapplication.Main2Activity;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

//home fragment for "Home" navigation drawer option
public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private FragmentHomeBinding binding;
    private  PieChart pieChart;
    private String username;
    private MyDBHandler db;
    private Map<String,String> map;
    private Spinner spinner;
    HomeViewModel homeViewModel;


    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();          //get the root view of the fragment => mainActivity
        db= new MyDBHandler(getActivity(), null, null, 1);      //create a new database handler
        map=new HashMap<>();                //create a new hashmap to hold the items and their amounts


        spinner=root.findViewById(R.id.spinner);        //get the spinner/dropdown from the layout
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.dropdown));        //create an array adapter to hold the dropdown items
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);        //set the dropdown view to the dropdown item
        spinner.setAdapter(adapter);            //set the adapter to the spinner and populate it with the dropdown items
        spinner.setOnItemSelectedListener(this);            //set the spinner to listen for item selection
        spinner.setBackground(getContext().getResources().getDrawable(R.drawable.spinner_dropdown));                //set a custom border drawable to the spinner



        // This callback will only be called when MyFragment is at least Started.
        //This de-activates back button when user is in home page
            OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
                @Override
                public void handleOnBackPressed() {
                }
            };
            requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return root;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pieChart = view.findViewById(R.id.piechart);            //get the pie chart from the layout

        //get connected user's username from activity
        if(getActivity()!=null) {
            homeViewModel =
                    new ViewModelProvider(getActivity()).get(HomeViewModel.class);
            obtainChartData("total");       //get the chart data for the total amount spent which is the default
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {     //when an item is selected in the dropdown
        String item = parent.getItemAtPosition(position).toString();        //get the option selected name
        ConstraintLayout layout=(ConstraintLayout) (getView()).findViewById(R.id.dataSet);      //get the layout to display the data below the piechart
        layout.removeAllViews();                                                             //if there are any views displayed before, remove them to put the new ones in


        switch (item){                                         //switch to the selected option
            case "Annual Expenses":
                obtainChartData("annual");                   //get the chart data for the annual amount spent
                break;
            case "Total Expenses":
                obtainChartData("total");                   //get the chart data for the total amount spent
                break;
            case "Monthly Expenses":
                obtainChartData("month");                       //get the chart data for the monthly amount spent
                break;
            case "Daily Expenses":
                obtainChartData("day");                         //get the chart data for the daily amount spent
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void obtainChartData(String usecase){                           //get the chart data for the selected option
        homeViewModel.getText().observe(getViewLifecycleOwner(), s -> {         //obtain username from the viewmodel

            map=db.findUserDataForChart(s,usecase);                         //get the chart data from the database

            //sort hashmap by value and put the results in a linked hashmap
            //we need to use a linked hashmap to preserve the order of the items in the map
            //and it has to be sorted in order to display the items in order to the user
            Map<String,String> sortedMap=map.entrySet().stream()
                    .sorted((k1, k2) -> Float.compare(Float.valueOf(k1.getValue()),Float.valueOf(k2.getValue()))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (k1, k2) -> k1, LinkedHashMap::new));
            setupPieChart();                                //setup the pie chart
            loadPieChartData(sortedMap);                    //load the data into the pie chart
        });
    }



    private void setupPieChart() {                          //setup the pie chart
        pieChart.setDrawHoleEnabled(true);                  //enable the hole in the pie chart
        pieChart.setUsePercentValues(true);                 //set the values to be displayed as percentages
        pieChart.setEntryLabelTextSize(12);                 //set the size of the labels
        pieChart.setEntryLabelColor(Color.BLACK);           //set the color of the labels
        pieChart.setCenterText("Spending by Category");     //set the text in the center of the pie chart
        pieChart.setCenterTextSize(24);                     //set the size of the text in the center
        pieChart.getDescription().setEnabled(false);        //disable the description of the pie chart

        Legend l = pieChart.getLegend();                    //get the legend of the pie chart
        l.setEnabled(false);                                //disable the legend of the pie chart

    }

    private float getPercentage(float value,float total){                       //get the percentage of each value out of the total (top 5 item values)
        BigDecimal number= new BigDecimal(Float.toString(value/total));
        return number.floatValue();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadPieChartData(Map<String,String> sortedMap) {               //load the data into the pie chart

        Set<String> setKeys= sortedMap.keySet();                                //get the keys of the map = item names
        List<String> keys=new ArrayList<String>(setKeys);                           //convert the set of keys to a list
        ListIterator<String> it=keys.listIterator(keys.size());                 //create a list iterator to iterate through the list of keys

        ArrayList<PieEntry> pieEntries = new ArrayList<>();                     //create an array list to store the pie chart entries
        ArrayList<Integer> colors = new ArrayList<>();                          //create an array list to store the colors to be used in the chart
        if(keys.size()<=0){                                                 //if there are no items for this user, display an empty pie chart with a message and a single color
            pieEntries.add(new PieEntry(1f,"No Data"));
            colors.add(Color.parseColor("#6200EE"));
        }
        for (int color: ColorTemplate.VORDIPLOM_COLORS) {                   //seed the colors to the array list
            colors.add(color);
        }
        PieDataSet dataSet = new PieDataSet(pieEntries,"Expenses");     //create a pie data set with the pie entries and the name of the data set




        float total=0;                                       //total amount of first 5 items
        if(map.size()<=5){                                      //if there are less than 5 items, get the total amount of all the items
            while(it.hasPrevious()){
                total+=Float.parseFloat((String) map.get(it.previous()));
            }
        }else{
            int i=0;
            while(it.hasPrevious()){
                if(i>=5){                                   //if there are more than 5 items, get the total amount of the first 5 items
                    break;
                }
                float currValue=Float.parseFloat((String) map.get(it.previous()));
                total+=currValue;
                i++;
                if(currValue==0){                           //if the value is 0, it wont be displayed at all
                    break;
                }
            }
        }

        it=keys.listIterator(keys.size());                  //reset the iterator to iterate through the list of keys
        int i=0;
        ConstraintLayout layout=(ConstraintLayout) (getView()).findViewById(R.id.dataSet);      //get the layout to display the data below the piechart


        int previous=(getView()).findViewById(R.id.homeDataContainer).getId();                  //get the id of the previous view
        while (it.hasPrevious()){                                                                   //iterate through the list of keys in reverse order
            String item=  it.previous();
            float currValue=Float.parseFloat(map.get(item));                                    //get the amount of the current item
            if(currValue==0){                                                                   //if the value is 0, it wont be displayed at all
                break;
            }
            if(i<5){                                                                            //only add the 5 first items to the chart
                pieEntries.add(new PieEntry(getPercentage(currValue,total), item));
                i++;
            }else{i++;}
                TextView stat=(new TextView(getActivity()));                                    //create a text view to display the amount of the current item
                ConstraintLayout.LayoutParams params=new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);     //create a layout params to set the text view
                int topToBottom=previous;                   //set the top to bottom constraint to the previous view
                params.leftToLeft=layout.getId();
                params.topToBottom=topToBottom;
                params.rightToRight=layout.getId();
                params.bottomMargin=10;
                previous=View.generateViewId();                                    //generate new id for the new view and set the previous view to the current view
                stat.setId(previous);
                stat.setLayoutParams(params);
                stat.setText(item+" : "+currValue+"$");                             //set the text of the text view
                stat.setTextSize(20);
                stat.setShadowLayer(0.01f,-2,2,getResources().getColor(R.color.black));
                if(i<=5){                                                           //set the text colors of the views based on the colors in the chart
                    stat.setTextColor(colors.get(i-1));
                }else{

                    stat.setTextColor(getResources().getColor(R.color.teal_200));               //for the rest of the views set a standard color for all of them
                }
                stat.setBackgroundColor(Color.WHITE);

                layout.addView(stat);

        }


        dataSet.setColors(colors);                                  //set the colors to the data set

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);                                   //enable the values to be displayed
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();                                    //refresh the chart

        pieChart.animateY(1400, Easing.EaseInOutQuad);      //animate the chart
    }


    @Override
    public void onPause() {
        super.onPause();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    private String getDate(){                   //get the current date in particular format for use in the database
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String currentDate=dateFormat.format(new Date());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss", Locale.getDefault());
        String currentTime=timeFormat.format(new Date());
        return "date"+currentDate+"time"+currentTime;
    }


}