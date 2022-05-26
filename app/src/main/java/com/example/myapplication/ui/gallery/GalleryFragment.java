package com.example.myapplication.ui.gallery;

import com.example.myapplication.ItemAdapter;
import com.example.myapplication.Main2Activity;
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



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

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
        RecyclerView rec=view.findViewById(R.id.recycle);
        System.out.println(rec);
        //getActivity so we can find the username
        Main2Activity activity=(Main2Activity)getActivity();
        //HERE WE HAVE USERNAME FOR DATA ACCESING U CAN PASS OTHER DATA SAME WAY
        System.out.println("We be in fragment gallery "+activity.getUsername());
        //seeds some data
        enterData();
        //pass recycler to setAdapter, which creates the recycler view with the itemList arrayList
        setAdapter(rec);


    }

    //seeds db with premade items
    //TODO grab user items from table USER_HAS_ITEMS and add them to the arraylist, since it currently displays random items and not user items
    private void enterData(){
        itemList=new ArrayList<>();
        itemList.add(new Item("chicken",2));
        itemList.add(new Item("dicks",2));
        itemList.add(new Item("balls",2));
        itemList.add(new Item("curry",2));

    }
    //Standard code for Recycler creation
    private void setAdapter(RecyclerView recyclerView){
        ItemAdapter adapter=new ItemAdapter(itemList);
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