package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.databinding.ActivityAddItemBinding;

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
                String amountValue = amount.getText().toString();
                MyDBHandler dbHandler = new MyDBHandler(view.getContext(), null, null, 1);
                if(username!=null) {
                    dbHandler.addItemToAccount(username,new Item(value),Integer.parseInt(amountValue));
                }
                finish();
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
}

