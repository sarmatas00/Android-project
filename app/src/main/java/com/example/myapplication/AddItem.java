package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//AddItem activity for adding a new amount for a particular item
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
        amount.setOnFocusChangeListener(onFocusListener);
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
        add.setOnClickListener(onAddListener);
        back = (Button) findViewById(R.id.backBtn);
        back.setOnClickListener(onBackListener);

    }

    private View.OnFocusChangeListener onFocusListener=new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {                     //add dollar sign when user focus on the amount field
                amount.setText("$");
                amount.setSelection(1);         //set cursor to the second position
            }
        }
    };

    private View.OnClickListener onAddListener=new View.OnClickListener() {         //add the new amount to the database
        @Override
        public void onClick(View view) {

            //extract dollar value from amount if the user did not remove it by himself already
            String amountValue =(amount.getText().toString().contains("$"))?amount.getText().toString().substring(1) : amount.getText().toString();

            if(amountValue.equals("")){                                //if the amount is empty it means its 0
                amountValue="0";
            }
            MyDBHandler dbHandler = new MyDBHandler(view.getContext(), null, null, 1);
            if(username!=null) {
                dbHandler.addItemToAccount(username,new Item(value),Double.parseDouble(amountValue),getDate());
            }
            finish();                                       //return to the previous fragment

        }
    };

    private View.OnClickListener onBackListener=new View.OnClickListener() {        //return to the previous fragment
        @Override
        public void onClick(View view) {
            finish();
        }
    };



    private String getDate(){                       //get the current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String currentDate=dateFormat.format(new Date());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss", Locale.getDefault());
        String currentTime=timeFormat.format(new Date());
        return "date"+currentDate+"time"+currentTime;
    }
}

