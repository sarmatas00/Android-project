package com.example.myapplication;

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
    private ArrayList<Item> itemList;
    private String username;
//We pass the Array list of items we want displayed
    public ItemAdapter(ArrayList<Item> itemList,String username){
        this.username=username;
        this.itemList=itemList;
        System.out.println("HHHHHHHHHHHHHHHHH"+username);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView itemTxt;
        private ImageButton btn;
        public MyViewHolder(final View view){
            super(view);
            itemTxt=view.findViewById(R.id.listItemTitle);
            btn=view.findViewById(R.id.button);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(view.getContext(), AddItem.class);
                    String itemName=itemList.get(getAdapterPosition()).getName();
                    Bundle extras=new Bundle();
                    extras.putString("itemName",itemName);
                    extras.putString("username",username);
                    intent.putExtras(extras);
                    view.getContext().startActivity(intent);
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

    //We display the data in the elements of a recycler view item
    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.MyViewHolder holder, int position) {
        String name=itemList.get(position).getName();
        holder.itemTxt.setText(name);


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void addItem(Item item,int position){
        itemList.add(position,item);
        notifyItemInserted(position);
    }
}
