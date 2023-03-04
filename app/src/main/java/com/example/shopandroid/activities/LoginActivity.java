package com.example.shopandroid.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.shopandroid.R;

public class LoginActivity extends AppCompatActivity {


    private ImageButton imgBtnForgot;
    private ImageButton imgBtnNotSignedUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        start();
    }

    private void init(){
        imgBtnForgot = findViewById(R.id.imgBtnForgot);
        imgBtnNotSignedUp = findViewById(R.id.imgBtnNotSignedUp);
    }
    private void start() {

        imgBtnForgot.setOnClickListener(e->{
            ///Todo
            Toast.makeText(getApplicationContext(), "Forgot password!!!!", Toast.LENGTH_SHORT).show();
        });

        imgBtnNotSignedUp.setOnClickListener(e->{
            //startActivity(new Intent(getApplicationContext(),));
            Toast.makeText(getApplicationContext(), "Sign up!!!!", Toast.LENGTH_SHORT).show();
        });

    }
}