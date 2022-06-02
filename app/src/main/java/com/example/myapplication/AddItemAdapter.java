package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AddItemAdapter extends RecyclerView.Adapter<com.example.myapplication.AddItemAdapter.MyHolder>{
    private ArrayList<Item>itemList;
    private String username;

    public AddItemAdapter(ArrayList<Item> itemList,String username){
        this.username=username;
        this.itemList=itemList;
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        private TextView itemTxt;
        private ImageButton btn;
        public MyHolder(final View view){
            super(view);
            itemTxt=view.findViewById(R.id.textView2);
            btn=view.findViewById(R.id.button);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
    public AddItemAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        View itemView=LayoutInflater.from(parent.getContext()).inflate(R.layout.itemforadditemrecycler,parent,false);
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddItemAdapter.MyHolder holder,int position){
        String name=itemList.get(position).getName();
        holder.itemTxt.setText(name);
    }

    @Override
    public int getItemCount(){return itemList.size();}

    public void addItem(Item item,int position){
        itemList.add(position,item);
        notifyItemInserted(position);
    }
}
