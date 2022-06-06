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
        //seed "admin" account
        Account account = new Account();
        account.setUsername("admin");
        account.setEmail("admin@admin.com");
        account.setPassword("12345");
        db.addAccount(account);

        //seed items ready to use by everyone
        db.addItem(new Item("chicken"));
        db.addItem(new Item("chicken nuggets"));
        db.addItem(new Item("beef steak"));
        db.addItem(new Item("beef ribs"));
        db.addItem(new Item("pork chops"));
        db.addItem(new Item("vegan burger"));
        db.addItem(new Item("gouda cheese"));
        db.addItem(new Item("feta cheese"));
        db.addItem(new Item("water 1.5lt"));
        db.addItem(new Item("water 6pack"));
        db.addItem(new Item("coke 1.5lt"));
        db.addItem(new Item("coke light 1lt"));
        db.addItem(new Item("monster energy blue"));
        db.addItem(new Item("croissant"));
        db.addItem(new Item("snickers"));
        db.addItem(new Item("ben n jerrys ice cream"));
        db.addItem(new Item("milk 1lt"));
        db.addItem(new Item("peanut butter"));
        db.addItem(new Item("protein powder 2.5kg"));
        db.addItem(new Item("honey"));
        db.addItem(new Item("tomatoes"));
        db.addItem(new Item("onion"));
        db.addItem(new Item("potatoes"));
        db.addItem(new Item("carrots"));
        db.addItem(new Item("apples"));
        db.addItem(new Item("bananas"));
        db.addItem(new Item("oranges"));
        db.addItem(new Item("grapes"));
        db.addItem(new Item("batteries"));
        db.addItem(new Item("detergent"));
        db.addItem(new Item("paper towels"));
        db.addItem(new Item("toilet paper"));
        db.addItem(new Item("shampoo ultrex"));
        db.addItem(new Item("dentist"));
        db.addItem(new Item("cigarettes"));

        //seed items to admin in different dates
        db.addItemToAccount("admin",new Item("chicken"),7.8,"date20210606time123451");
        db.addItemToAccount("admin",new Item("pork chops"),2.65,"date20210607time12342");
        db.addItemToAccount("admin",new Item("cigarettes"),13.9,"date20210608time123453");
        db.addItemToAccount("admin",new Item("dentist"),26.0,"date20220512time123454");
        db.addItemToAccount("admin",new Item("vegan burger"),17.9,"date20220513time123455");
        db.addItemToAccount("admin",new Item("water 6pack"),1.6,"date20220517time123456");
        db.addItemToAccount("admin",new Item("paper towel"),12.2,"date20220603time123457");
        db.addItemToAccount("admin",new Item("tomatoes"),3.45,"date20220604time123458");
        db.addItemToAccount("admin",new Item("beef steak"),19.0,"date20220605time123459");
        db.addItemToAccount("admin",new Item("grapes"),8.0,"date20220606time123459");
        db.addItemToAccount("admin",new Item("gouda cheese"),7.5,"date20220606time123459");
        db.addItemToAccount("admin",new Item("coke light 1.5lt"),28.0,"date20220606time123459");


    }
}