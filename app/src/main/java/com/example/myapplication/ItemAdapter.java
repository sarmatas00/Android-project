package com.example.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ui.gallery.GalleryFragment;

import java.util.ArrayList;
//ItemAdapter for recycler view in My Items page
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
    private ArrayList<EnchancedItem> itemList,valuesList;
    private String username;
//We pass the Array list of items we want displayed
    public ItemAdapter(ArrayList<EnchancedItem> itemList,String username){
        this.username=username;
        this.itemList=itemList;
        System.out.println("HHHHHHHHHHHHHHHHH"+username);

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
            btnX.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println(view.getContext());
                    String itemName=itemList.get(getAdapterPosition()).getName();
                    System.out.println(view.getContext()+" "+username+" "+itemName);
                    MyDBHandler db=new MyDBHandler(view.getContext(), null,null,1);
                    db.deleteItemFromUser(username,itemName);
                    Activity main= (Activity) view.getContext();
                    main.onBackPressed();
                    //GalleryFragment curr=((GalleryFragment) getFragmentManager().findFragmentById(R.id.nav_gallery);

                }
            });
        }
        /* TODO display value
        public double getTotalSpent(String username,String item){
            MyDBHandler db=new MyDBHandler(,null,null,1);
            return db.findTotalSpentOnItem(username,item);
        }

         */

    }


    @NonNull
    @Override
    public ItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.itemviewforrecycler,parent,false);
        return new MyViewHolder(itemView);
    }

    //We display the data in the elements of a recycler view item
    //TODO display value
    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.MyViewHolder holder, int position) {
        String name=itemList.get(position).getName();
        Double value=itemList.get(position).getValue();
        holder.itemTxt.setText(name);
        holder.totalSpentOnItem.setText(value.toString());



    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void addItem(EnchancedItem item,int position){
        itemList.add(position,item);
        notifyItemInserted(position);
    }
}
