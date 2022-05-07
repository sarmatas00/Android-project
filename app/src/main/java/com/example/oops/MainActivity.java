package com.example.oops;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView navigationView;
    firstFragment firstFragment = new firstFragment();
    secFragment secondFragment = new secFragment();
    thirdFragment thirdFragment = new thirdFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView=findViewById(R.id.bottomNavigationView);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,firstFragment).commit();

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id= item.getItemId();
                switch (id){
                    case R.id.firstFragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, firstFragment).commit();
                        return true;
                    case R.id.secFragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, secondFragment).commit();
                        return true;
                    case R.id.thirdFragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,thirdFragment).commit();
                        return true;

                }

                return false;
            }
        });









    }


}