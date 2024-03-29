package com.example.shopandroid.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.shopandroid.HomeActivity;
import com.example.shopandroid.R;
import com.example.shopandroid.fragments.CartFragment;
import com.example.shopandroid.fragments.CatalogFragment;
import com.example.shopandroid.models.jwt.RefreshToken;
import com.example.shopandroid.services.implementations.CartItemService;
import com.example.shopandroid.services.session.CartItemsSessionManagement;
import com.example.shopandroid.services.session.RefreshTokenSessionManagement;
import com.example.shopandroid.services.session.UserSessionManagement;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;


public class BottomNavigationActivity extends AppCompatActivity {

    private BottomNavigationView bottom_navigation;
    private final FragmentManager _fragmentManager =getSupportFragmentManager();
    private Fragment _fragment=null;
    private CartItemService _cartItemService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        checkSession();
        setHomeFragment();
        init();
        events();
    }

    private void checkSession() {



        var userSession = new UserSessionManagement(getApplicationContext(), false);
        var resSession = new RefreshTokenSessionManagement(getApplicationContext(),false);

        if(userSession.isValidSession() && resSession.isValidSession()){

            var getResSession = resSession.getSession();

            if(tokenExpired(getResSession)){
                userSession.removeSession();
                startActivity(new Intent(BottomNavigationActivity.this, LoginActivity.class));
            }

        }else{
            userSession.removeSession();
            resSession.removeSession();
            startActivity(new Intent(BottomNavigationActivity.this, LoginActivity.class));

        }
        pullCarts();
    }

    private void pullCarts(){

        _cartItemService= new CartItemService(getApplicationContext());

        _cartItemService.getCartItems();
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
                    _fragment = new CatalogFragment();
                }break;
                case R.id.iCartBotNav:{
                    _fragment = new CartFragment();
                }break;
                case R.id.iProfileBotNav:{

                logout();
                }break;

                default:{

                }



            }
            if(_fragment !=null){
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                fragmentTransaction
                        .replace(R.id.flMain,_fragment)
                        .addToBackStack("")
                        .commit();
            }

        });

    }

    private boolean tokenExpired(RefreshToken getSession){
        LocalDateTime localDateTime ;


        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_INSTANT;
        Instant instant = Instant.from(dateTimeFormatter.parse(getSession.expiringDate));
        ZoneId zoneId = ZoneId.systemDefault();
        localDateTime = instant.atZone(zoneId).toLocalDateTime();



        int compare = localDateTime.compareTo(LocalDateTime.now());

        return compare < 0;
    }

    private void logout(){
        var userSession = new UserSessionManagement(getApplicationContext(),false);

        if(userSession.isValidSession()){
            userSession.removeSession();
            startActivity(new Intent(BottomNavigationActivity.this, LoginActivity.class));

        }
    }
    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}