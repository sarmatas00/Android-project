package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
//ItemAdapter for recycler view in My Items page
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
    private ArrayList<EnchancedItem> itemList,valuesList;
    private String username;
//We pass the Array list of items we want displayed
    public ItemAdapter(ArrayList<EnchancedItem> itemList,String username){
        this.username=username;
        this.itemList=itemList;


    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView itemTxt,totalSpentOnItem;
        private ImageButton btn,btnX;
        public MyViewHolder(final View view){
            super(view);
            itemTxt=view.findViewById(R.id.textView2);
            totalSpentOnItem=view.findViewById(R.id.listItemTitle);
            btn=view.findViewById(R.id.button);
            btnX=view.findViewById(R.id.imageButton);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {                   //on button click start AddItem activity and pass item name and username
                    Intent intent = new Intent(view.getContext(), AddItem.class);
                    String itemName=itemList.get(getAdapterPosition()).getName();
                    Bundle extras=new Bundle();
                    extras.putString("itemName",itemName);
                    extras.putString("username",username);
                    intent.putExtras(extras);
                    view.getContext().startActivity(intent);
                }
            });
            btnX.setOnClickListener(new View.OnClickListener() {            //on button click delete item from database
                @Override
                public void onClick(View view) {
                    String itemName=itemList.get(getAdapterPosition()).getName();       //get item name
                    MyDBHandler db=new MyDBHandler(view.getContext(), null,null,1);
                    db.deleteItemFromUser(username,itemName);
                    Activity main= (Activity) view.getContext();
                    main.onBackPressed();                               //go back to main activity

                }
            });
        }

    }


    @NonNull
    @Override
    public ItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.itemviewforrecycler,parent,false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.MyViewHolder holder, int position) {      //bind the data to the view
        String name=itemList.get(position).getName();
        Double value=itemList.get(position).getValue();
        holder.itemTxt.setText(name);                               //set item name to recycler view
        holder.totalSpentOnItem.setText(String.format("%.1f",value)+"$");      //set total spent on item to view



    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void addItem(EnchancedItem item,int position){           //add item to the list and notify the adapter to update the view
        itemList.add(position,item);
        notifyItemInserted(position);
    }
}
