package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.databinding.ActivityAddItemBinding;
import com.example.myapplication.ui.gallery.GalleryFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddItem extends AppCompatActivity {
    private EditText amount;
    private TextView itemName;
    private Button add,back;
    private String value,username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        amount = (EditText) findViewById(R.id.enterAmount);
        //add euro sign when user focus on the amount field
        amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    amount.setText("$");
                    amount.setSelection(1);
                } else {
                    amount.setText("");
                }
            }
        });
        itemName = (TextView) findViewById(R.id.itemName);
        value=username=null;
        if(savedInstanceState==null) {
            Bundle extras = this.getIntent().getExtras();
            if (extras != null) {
                value = extras.getString("itemName");
                username = extras.getString("username");
            }
        }
        if(value!=null){
        itemName.setText(value);
        }

        add = (Button) findViewById(R.id.confirmAmountBtn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amountValue =(amount.getText().toString().contains("$"))?amount.getText().toString().substring(1) : amount.getText().toString();

                if(amountValue.equals("")){
                    amountValue="0";
                }
                MyDBHandler dbHandler = new MyDBHandler(view.getContext(), null, null, 1);
                if(username!=null) {
                    dbHandler.addItemToAccount(username,new Item(value),Integer.parseInt(amountValue),getDate());
                }
                finish();
//                Intent i=new Intent(view.getContext(),Main2Activity.class);
//                i.putExtra("userName",username);
//                startActivity(i);



            }
        });
        back = (Button) findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }



    private String getDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String currentDate=dateFormat.format(new Date());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss", Locale.getDefault());
        String currentTime=timeFormat.format(new Date());
        return "date"+currentDate+"time"+currentTime;
    }
}

