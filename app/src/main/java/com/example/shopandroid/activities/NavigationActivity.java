package com.example.shopandroid.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.shopandroid.R;
import com.google.android.material.appbar.MaterialToolbar;

public class NavigationActivity extends AppCompatActivity {

    private MaterialToolbar tbBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
    }
}