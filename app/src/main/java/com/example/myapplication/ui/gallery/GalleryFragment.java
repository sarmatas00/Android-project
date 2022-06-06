package com.example.myapplication.ui.gallery;

import com.example.myapplication.EnchancedItem;
import com.example.myapplication.HomeViewModel;
import com.example.myapplication.ItemAdapter;
import com.example.myapplication.Main2Activity;
import com.example.myapplication.MyDBHandler;
import com.example.myapplication.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.Item;
import com.example.myapplication.databinding.FragmentGalleryBinding;

import java.util.ArrayList;
//GaleryFragment for My Items navigation drawer option
public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private static ArrayList<EnchancedItem> itemList;
    private RecyclerView rec;
    private MyDBHandler db;
    private String username;
    private ItemAdapter adapter;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    //Runs after every view is created and you have access to all the elements
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //finds the recycler view from layout
        rec=view.findViewById(R.id.recycler);
        db = new MyDBHandler(getActivity(), null, null, 1);             //initiates db handler

        //use the view model to get the username from the main activity
        if(getActivity()!=null) {
            HomeViewModel galleryViewModel =
                    new ViewModelProvider(getActivity()).get(HomeViewModel.class);
            galleryViewModel.getText().observe(getViewLifecycleOwner(), s -> {
                username=s;
                itemList=new ArrayList<>();             //initialize item list
                itemList=db.findUserData(username);      //get all items for this user from db

                TextView alert = view.findViewById(R.id.noItemsAlert2);     //finds the alert text view
                if(itemList.size()<=0) {
                    alert.setVisibility(View.VISIBLE);              //if no items found for user, show alert
                }else{
                    alert.setVisibility(View.INVISIBLE);            //otherwise hide alert
                }
                //pass recycler to setAdapter, which creates the recycler view with the itemList arrayList
                setAdapter(rec);
            });
        }
    }

    //creates the adapter for the recycler view
    private void setAdapter(RecyclerView recyclerView){
        adapter=new ItemAdapter(itemList,username);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onDestroyView() {       //when fragment is destroyed, remove the binding
        super.onDestroyView();
        binding = null;
    }

    //when fragment is resumed, which happens after returning from AddItem activity
    //reload the items for this user in order to update the recycler view with the new data
    @Override
    public void onResume() {
        super.onResume();
        itemList=db.findUserData(username);
        adapter=new ItemAdapter(itemList,username);
        rec.setAdapter(adapter);
    }
}