package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class CreateAccount extends AppCompatActivity {
    private EditText userIn,emailIn,passIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        userIn=findViewById(R.id.signUpUsernameIn);
        emailIn=findViewById(R.id.signUpEmailIn);
        passIn=findViewById(R.id.signUpPassIn);
    }
    public void getSignUpData(View view){
        System.out.println("signupclickedcreateacc");
        boolean n1=true,n2=true,n3=true;
        if(!isEmpty(userIn)&&!isEmpty(emailIn)&&!isEmpty(passIn)){
            Toast.makeText(getApplicationContext(),userIn.getText()+" "+emailIn.getText()+" "+passIn.getText(),Toast.LENGTH_SHORT).show();
            if(!checkUsernameType(userIn.getText().toString())){
                Toast.makeText(getApplicationContext(),"Username must have a minimum length of 3",Toast.LENGTH_SHORT).show();
                n1=false;
            }else if(!checkUsernameExists(userIn.getText().toString())){
                Toast.makeText(getApplicationContext(),"Username already taken",Toast.LENGTH_SHORT).show();
                n1=false;
            }
            else if(!checkEmail(emailIn.getText().toString())){
                Toast.makeText(getApplicationContext(),"Invalid e-mail",Toast.LENGTH_SHORT).show();
                n2=false;

            }else if(!checkPass(passIn.getText().toString())){
                Toast.makeText(getApplicationContext(),"Password must contain one upper, one lower, one special and at least 8 characters",Toast.LENGTH_SHORT).show();
                n3=false;
            }
            System.out.println(passIn.getText());
        }else{
            n1=n2=n3=false;
            Toast.makeText(getApplicationContext(),"Please fill out all the fields",Toast.LENGTH_SHORT).show();
        }
        if(n1&&n2&&n3){
            MyDBHandler db=new MyDBHandler(this,null,null,1);
            db.addAccount(new Account(userIn.getText().toString(),emailIn.getText().toString(),passIn.getText().toString()));
            Toast.makeText(getApplicationContext(),"Welcome!",Toast.LENGTH_SHORT).show();
            Intent i=new Intent(this,Main2Activity.class);
            CharSequence userName=this.userIn.getText();
            CharSequence pass=passIn.getText();
            i.putExtra("userName",userName);
            i.putExtra("pass",pass);
            startActivity(i);
        }

    }
    public boolean checkUsernameType(String username){
        if(username.matches("^[a-zA-Z0-9]{3,20}$")){
            return true;
        }
        return false;
    }
    public boolean checkUsernameExists(String username){
        MyDBHandler db=new MyDBHandler(this,null,null,1);
        if(db.findAccount(username)==null)
            return true;
        return false;
    }
    public boolean checkEmail(String email){
        if(email.matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")){
            return true;
        }
        return false;

    }
    public boolean checkPass(String pass){
        if(pass.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$")){
            return true;
        }
        return false;
    }
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}