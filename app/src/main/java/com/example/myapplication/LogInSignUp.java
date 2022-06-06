package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;


//LogInSignUp activity for logging in and signing up
public class LogInSignUp extends AppCompatActivity {
    private Button logInBtn,signUpBtn;
    private EditText username,password,email;
    private MyDBHandler db;

    //finds views which are the username/pass editTexts
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_sign_up);
        logInBtn=(Button) findViewById(R.id.buttonLogIn);
        signUpBtn=(Button)findViewById(R.id.button2);
        username= findViewById(R.id.editTextUsername);
        password= findViewById(R.id.editTextPassword);
        Bundle extras=getIntent().getExtras();
        db=new MyDBHandler(this,null,null,1);
        //add demo accounts when db is created
        seed();
        if(extras!=null){
            username.setText(extras.getCharSequence("newName"));
        }




    }
    @Override
    public void onBackPressed(){

    }


    //When log in button is clicked
    public void logClicked(View view) {


        //Looking in database for username with account and checking if pass is matching given one
        Account acc=db.findAccount(username.getText().toString());
        if(acc==null) {             //if account doesn't exist
            Toast.makeText(getApplicationContext(), "There isn't an account with this username", Toast.LENGTH_SHORT).show();
            return;
        }else if(!password.getText().toString().equals(acc.getPassword())) {        //if password doesn't match
            Toast.makeText(getApplicationContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
            return;
        }
        //Account found begins main activity
        Intent i=new Intent(this,Main2Activity.class);
        String userName=this.username.getText().toString();
        String pass=password.getText().toString();
        i.putExtra("userName",userName);
        i.putExtra("pass",pass);
        startActivity(i);
    }

    //Sign up button begins CreateAccount activity
    public void signUpClicked(View view) {
        Intent i=new Intent(this,CreateAccount.class);
        startActivity(i);
    }

    //Inserts demo account into the database
    public void seed() {
        Account account = new Account();
        account.setUsername("admin");
        account.setEmail("admin@admin.com");
        account.setPassword("12345");
        db.addAccount(account);
        db.deleteItemFromUser("admin","chicken");
        db.deleteItemFromUser("admin","balls");
        db.deleteItemFromUser("admin","curry");
        db.deleteItemFromUser("admin","dicks");

        db.addItem(new Item("chicken"));
            db.addItem(new Item("dicks"));
            db.addItem(new Item("balls"));
            db.addItem(new Item("curry"));


    }
}