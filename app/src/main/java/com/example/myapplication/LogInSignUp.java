package com.example.myapplication;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
//TODO make landscape layout not trash
//works fine
public class LogInSignUp extends AppCompatActivity {
    private Button logInBtn,signUpBtn;
    private EditText username,password,email;

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
        if(extras!=null){
            username.setText(extras.getCharSequence("newName"));
        }




    }
    @Override
    public void onBackPressed(){

    }


    //When log in button is clicked
    public void logClicked(View view) {

        System.out.println("loginclicked");

        //Looking in database for username with account and checking if pass is matching given one
        MyDBHandler db=new MyDBHandler(this,null,null,1);
        Account acc=db.findAccount(username.getText().toString());
        if(acc==null) {
            Toast.makeText(getApplicationContext(), "There isn't an account with this username", Toast.LENGTH_SHORT).show();
            return;
        }else if(!password.getText().toString().equals(acc.getPassword())) {
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
        System.out.println("signupclicked");
        Intent i=new Intent(this,CreateAccount.class);
        startActivity(i);
    }
}