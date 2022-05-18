package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
    private ArrayList<Item> itemList;

    public ItemAdapter(ArrayList<Item> itemList){
        this.itemList=itemList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView itemTxt;
        public MyViewHolder(final View view){
            super(view);
            itemTxt=view.findViewById(R.id.listItemTitle);

        }
    }

    @NonNull
    @Override
    public ItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.itemviewforrecycler,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.MyViewHolder holder, int position) {
        String name=itemList.get(position).getName();
        holder.itemTxt.setText(name);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
