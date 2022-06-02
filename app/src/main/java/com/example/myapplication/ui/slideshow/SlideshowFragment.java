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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.ItemAdapter;
import com.example.myapplication.databinding.FragmentSlideshowBinding;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

//This is the add Item option from navigation menu now its empty
/*TODO create layout with recycler view that contains all items from database and a layout to allow user to add one of the items to his collection
   also create layout that allows user to create a new item and add it to database
 */
public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private RecyclerView recyclerView;
    private static ArrayList<Item> itemList;
    private EditText itemName,searchQuery;
    private Button addItem,searchItem;
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
        searchQuery=(EditText)view.findViewById(R.id.textView3);
        addItem=(Button) view.findViewById(R.id.buttonNewItem);
        addItem.setOnClickListener(myListener);
        searchItem=(Button)view.findViewById(R.id.button6);
        searchItem.setOnClickListener(searchListener);
        itemList=new ArrayList<>();


        //get users items from db
        if(getActivity()!=null) {
            HomeViewModel slideshowViewModel =
                    new ViewModelProvider(getActivity()).get(HomeViewModel.class);
            slideshowViewModel.getText().observe(getViewLifecycleOwner(), s -> {
                db = new MyDBHandler(getActivity(), null, null, 1);
                itemList=db.findAllItems();
                username=s;
                setAdapter(recyclerView,itemList);
                if(itemList.size()<=0) {
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

            System.out.println("+ clicked");
            String newItemName=itemName.getText().toString();
            itemName.setText("");
            if(newItemName!="") {
                Item newItem = new Item(newItemName);
                //get new item name from user and set total amount=0 =>starting amount
                if (db.addItem(newItem)) {
                    db.addItemToAccount(username, newItem, 0);
                }
                adapter.addItem(newItem, adapter.getItemCount());
                adapter.notifyItemInserted(adapter.getItemCount());
            }
            if(itemList.size()>0) {
                TextView alert = (getActivity()).findViewById(R.id.noItemsAlert);
                alert.setVisibility(View.INVISIBLE);
            }
            Intent intent = new Intent(v.getContext(), AddItem.class);
            String itemName=newItemName;
            Bundle extras=new Bundle();
            extras.putString("itemName",itemName);
            extras.putString("username",username);
            intent.putExtras(extras);
            v.getContext().startActivity(intent);
        }
    };
    private View.OnClickListener searchListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ArrayList<Item>filtered=new ArrayList<>();
            System.out.println(searchQuery.getText());
            if(searchQuery.getText().toString().trim().length() != 0){
                for(Item x:itemList){
                    if(x.getName().toLowerCase(Locale.ROOT).contains(searchQuery.getText().toString().toLowerCase(Locale.ROOT)))
                        filtered.add(x);
                }
                setAdapter(recyclerView,filtered);
            }else{
                setAdapter(recyclerView,itemList);
            }
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
}