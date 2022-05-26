package com.example.myapplication.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.Main2Activity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentHomeBinding;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private  PieChart pieChart;
    private  TextView stat1,stat2,stat3,stat4;
    private View view1,view2,view3,view4;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        System.out.println(root);
        System.out.println("HomeFragmentOnCreateView");


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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setData(view);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //here the data displayed in home page are made
    /*TODO get actual data from user (user items) calculate the 4 items user spend most money on and display 1)in the pie chart 2)in the text
       views stat1,..,stat4 and match colors of pie with view colors (view1,...,view4)

    */
    public  void setData(View view)
    {
        System.out.println("called");

        pieChart=view.findViewById(R.id.piechart);
        // Set the percentage of language used


        // Set the data and color to the pie chart
        pieChart.addPieSlice(
                new PieModel(
                        "stat1",
                        40,
                        Color.parseColor("#FFA726")));
        pieChart.addPieSlice(
                new PieModel(
                        "stat2",
                        30,
                        Color.parseColor("#66BB6A")));
        pieChart.addPieSlice(
                new PieModel(
                        "stat3",
                        40,
                        Color.parseColor("#EF5350")));
        pieChart.addPieSlice(
                new PieModel(
                        "stat4",
                        20,
                        Color.parseColor("#29B6F6")));

        // To animate the pie chart
        pieChart.startAnimation();
        /*
        stat1=getView().findViewById(R.id.stat1);
        stat2=getView().findViewById(R.id.stat2);
        stat3=getView().findViewById(R.id.stat3);
        stat4=getView().findViewById(R.id.stat4);
        stat1.setText("DICKS");
        stat2.setText("BALLS");
        stat3.setText("URMOM");
        stat4.setText("ISGAY");

         */
    }

}