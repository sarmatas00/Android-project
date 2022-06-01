package com.example.myapplication.ui.gallery;

import com.example.myapplication.HomeViewModel;
import com.example.myapplication.ItemAdapter;
import com.example.myapplication.Main2Activity;
import com.example.myapplication.MyDBHandler;
import com.example.myapplication.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private static ArrayList<Item> itemList;
    private RecyclerView recyclerView;
    private MyDBHandler db;
    private String username;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        System.out.println("GalleryFragmentOnCreateView");
        return root;
    }

    //Runs after every view is created and you have access to all the elements
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //finds the recycler from layout
        RecyclerView rec=view.findViewById(R.id.recycler);
        System.out.println(rec);
        db = new MyDBHandler(getActivity(), null, null, 1);

        //get users items from db
        if(getActivity()!=null) {
            HomeViewModel galleryViewModel =
                    new ViewModelProvider(getActivity()).get(HomeViewModel.class);
            galleryViewModel.getText().observe(getViewLifecycleOwner(), s -> {
                username=s;
                itemList=new ArrayList<>();
                itemList=db.findUserItems(s);

                //find all items from db
                itemList=new ArrayList<>();
                itemList=db.findAllItems();
                //pass recycler to setAdapter, which creates the recycler view with the itemList arrayList
                setAdapter(rec);
            });
        }



    }

    //seeds db with premade items
    private void enterData(){
            db.addItem(new Item("chicken"));
            db.addItem(new Item("pork"));
            db.addItem(new Item("beef"));
            db.addItem(new Item("poutsaki"));

    }



    //Standard code for Recycler creation
    private void setAdapter(RecyclerView recyclerView){
        ItemAdapter adapter=new ItemAdapter(itemList,username);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}