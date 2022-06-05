package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


//Logout fragment for logging out of the app
public class LogOut extends Fragment {


    private Button btnLogOut,btnGoBack;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_out, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnLogOut=view.findViewById(R.id.button4);
        btnGoBack=view.findViewById(R.id.button5);

        btnLogOut.setOnClickListener(new View.OnClickListener() {           //on button click start Login/Signup activity
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),LogInSignUp.class);
                startActivity(i);
            }
        });
        btnGoBack.setOnClickListener(new View.OnClickListener() {           //on Go Back button click return to Main activity
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }
}