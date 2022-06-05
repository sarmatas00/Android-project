package com.example.myapplication.ui.slideshow;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.AddItem;
import com.example.myapplication.AddItemAdapter;
import com.example.myapplication.HomeViewModel;
import com.example.myapplication.Item;
import com.example.myapplication.MyDBHandler;
import com.example.myapplication.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Button;
import android.widget.Toast;
import com.example.myapplication.databinding.FragmentSlideshowBinding;
import java.util.ArrayList;
import java.util.Locale;

//Slideshow fragment for "Add item" navigation drawer option
public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private RecyclerView recyclerView;
    private static ArrayList<Item> itemList;
    private EditText itemName;
    private SearchView searchQuery;
    private Button addItem;
    private MyDBHandler db;
    private AddItemAdapter adapter;
    private String username;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.recycle);
        itemName=(EditText) view.findViewById(R.id.edit1);
        searchQuery=(SearchView) view.findViewById(R.id.searchView);
        searchQuery.clearFocus();                                           //Clear focus from search bar
        searchQuery.setOnQueryTextListener(searchListener);                 //Set listener for search bar
        addItem=(Button) view.findViewById(R.id.buttonNewItem);
        addItem.setOnClickListener(myListener);                             //Set listener for add item button

        itemList=new ArrayList<>();


        //get users items from db
        if(getActivity()!=null) {
            HomeViewModel slideshowViewModel =
                    new ViewModelProvider(getActivity()).get(HomeViewModel.class);
            slideshowViewModel.getText().observe(getViewLifecycleOwner(), s -> {
                db = new MyDBHandler(getActivity(), null, null, 1);
                itemList=db.findAllItems();
                username=s;
                setAdapter(recyclerView,itemList);                          //Set adapter for recycler view
                if(itemList.size()<=0) {                                    //If no items in db display message
                    TextView alert = view.findViewById(R.id.noItemsAlert);
                    alert.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Attaching OnClick listener to addItem button where we get new item name from user and add it to database
    private View.OnClickListener myListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String newItemName=itemName.getText().toString();           //Get new item name from user
            itemName.setText("");                                       //Clear item name field
            if(itemList.size()>0) {                                     //If items found hide alert message
                TextView alert = (getActivity()).findViewById(R.id.noItemsAlert);
                alert.setVisibility(View.INVISIBLE);
            }
            if(!newItemName.equals("")) {                               //If item name not empty add item to db
                Item newItem = new Item(newItemName);

                db.addItem(newItem);                                    //register new item to db

                adapter.addItem(newItem, adapter.getItemCount());           //Add new item to adapter
                adapter.notifyItemInserted(adapter.getItemCount());         //Notify adapter that new item added


                Intent intent = new Intent(v.getContext(), AddItem.class);      //Open add item activity
                String itemName = newItemName;                                  //Pass item name to add item activity
                Bundle extras = new Bundle();
                extras.putString("itemName", itemName);
                extras.putString("username", username);                         //Pass username to add item activity
                intent.putExtras(extras);
                v.getContext().startActivity(intent);
            }
            else{
                Toast.makeText(getActivity(), "Please enter a valid item name", Toast.LENGTH_SHORT).show();             //Display error message if item name is empty
            }
        }
    };


    //Attaching OnClick listener to search bar where we get search query from user and filter items
    private SearchView.OnQueryTextListener searchListener=new SearchView.OnQueryTextListener(){

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {          //Filter items based on search query
            ArrayList<Item>filtered=new ArrayList<>();

            if(newText.trim().length() != 0){                       //If search query not empty
                for(Item x:itemList){
                    if(x.getName().toLowerCase(Locale.ROOT).contains(newText.toLowerCase(Locale.ROOT)))     //If item name contains search query
                        filtered.add(x);
                }
                setAdapter(recyclerView,filtered);          //Set filtered items to recycler view
            }else{
                setAdapter(recyclerView,itemList);          //else set all items to recycler view
            }
            return true;
        }
    };


    //Standard code for Recycler creation
    private void setAdapter(RecyclerView recyclerView,ArrayList<Item> list){
        adapter=new AddItemAdapter(list,username);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }



    @Override
    public void onResume() {                    //Refresh recycler when fragment is resumed (when user returns to fragment from add item activity)
        super.onResume();
        itemList=db.findAllItems();
        adapter=new AddItemAdapter(itemList,username);
        recyclerView.setAdapter(adapter);

    }
}