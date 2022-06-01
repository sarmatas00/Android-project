package com.example.myapplication.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

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

//This is the add Item option from navigation menu now its empty
/*TODO create layout with recycler view that contains all items from database and a layout to allow user to add one of the items to his collection
   also create layout that allows user to create a new item and add it to database
 */
public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private RecyclerView recyclerView;
    private static ArrayList<Item> itemList;
    private EditText itemName;
    private Button addItem;
    private MyDBHandler db;
    private ItemAdapter adapter;
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
        addItem=(Button) view.findViewById(R.id.buttonNewItem);
        addItem.setOnClickListener(myListener);
        itemList=new ArrayList<>();


        //get users items from db
        if(getActivity()!=null) {
            HomeViewModel slideshowViewModel =
                    new ViewModelProvider(getActivity()).get(HomeViewModel.class);
            slideshowViewModel.getText().observe(getViewLifecycleOwner(), s -> {
                db = new MyDBHandler(getActivity(), null, null, 1);
                itemList=db.findUserItems(s);
                username=s;
                setAdapter(recyclerView);
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

            String newItemName=itemName.getText().toString();
            itemName.setText("");
            if(newItemName!="") {
                Item newItem = new Item(newItemName);
                //get new item name from user and set total amount=0 =>starting amount
                if (db.addItem(newItem)) {
                    db.addItemToAccount(username, newItem, 0);
                    itemList.add(newItem);
                }
                adapter.addItem(newItem, adapter.getItemCount());
                adapter.notifyItemInserted(adapter.getItemCount());
            }
            if(itemList.size()>0) {
                TextView alert = (getActivity()).findViewById(R.id.noItemsAlert);
                alert.setVisibility(View.INVISIBLE);
            }
        }
    };

    //Standard code for Recycler creation
    private void setAdapter(RecyclerView recyclerView){
        adapter=new ItemAdapter(itemList,username);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}