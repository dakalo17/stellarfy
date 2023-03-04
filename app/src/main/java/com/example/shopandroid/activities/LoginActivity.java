package com.example.shopandroid.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;

import com.example.shopandroid.R;

public class LoginActivity extends AppCompatActivity {


    private ImageButton imgBtnForgot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        start();
    }

    private void init(){
        imgBtnForgot = findViewById(R.id.imgBtnForgot);
    }
    private void start() {

        imgBtnForgot.setOnClickListener(e->{
            imgBtnForgot.
        });
    }
}