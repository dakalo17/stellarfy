package com.example.shopandroid.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.shopandroid.R;
import com.example.shopandroid.fragments.CatalogFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class BottomNavigationActivity extends AppCompatActivity {

    private BottomNavigationView bottom_navigation;
    private final FragmentManager _fragmentManager =getSupportFragmentManager();
    private Fragment _fragment=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        setHomeFragment();
        init();
        events();
    }

    private void setHomeFragment() {
        //Todo check user roles
        Fragment fragment = new CatalogFragment();

        _fragmentManager.beginTransaction().replace(R.id.flMain, fragment).commit();
    }

    private void init(){
        bottom_navigation = findViewById(R.id.bottom_navigation);


    }

    private void events() {
        bottom_navigation.setOnItemReselectedListener(item -> {
            switch(item.getItemId()){
                case R.id.iHomeBotNav:{

                }break;

                default:{

                }

            }


        });

    }


}