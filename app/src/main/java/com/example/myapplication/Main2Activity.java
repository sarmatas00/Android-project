package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityMain2Binding;

//Main activity contains all the menu option fragments inside it, gets called from log-in and sign-up
public class Main2Activity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private String username,email;
    private ActivityMain2Binding binding;
    private TextView navUser,navEm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent=getIntent();
        username=intent.getStringExtra("userName");
        super.onCreate(savedInstanceState);

        HomeViewModel viewModel=new ViewModelProvider(this).get(HomeViewModel.class);
        viewModel.setText(username);


        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain2.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //Here we add our menu options
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,R.id.nav_logout)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main2);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        System.out.println("username "+username);

        seed();




    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        navUser=(TextView) findViewById(R.id.navUsername);
        navEm=(TextView) findViewById(R.id.navEmail);
        navUser.setText(username);
        MyDBHandler db=new MyDBHandler(this,null,null,1);
        Account acc=db.findAccount(username);
        email=acc.getEmail();
        navEm.setText(email);
        System.out.println("navuserview "+navUser);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main2);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    //seeds database
    //TODO seed properly and use the data on addItems menu
    public  void seed(){
        MyDBHandler db=new MyDBHandler(this,null,null,1);
        db.addItem(new Item("chicken"));
        db.addItem(new Item("dicks"));
        db.addItem(new Item("balls"));
        db.addItem(new Item("curry"));
    }

    public String getUsername(){
        return username;
    }





}