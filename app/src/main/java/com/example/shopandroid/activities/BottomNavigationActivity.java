package com.example.shopandroid.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



import com.example.shopandroid.R;
import com.example.shopandroid.fragments.CartFragment;
import com.example.shopandroid.fragments.CatalogFragment;
import com.example.shopandroid.models.JSONObjects.Product;
import com.example.shopandroid.models.jwt.RefreshToken;
import com.example.shopandroid.services.implementations.CartItemService;
import com.example.shopandroid.services.session.CartItemsSessionManagement;
import com.example.shopandroid.services.session.RefreshTokenSessionManagement;
import com.example.shopandroid.services.session.UserSessionManagement;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.badge.ExperimentalBadgeUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@OptIn(markerClass = ExperimentalBadgeUtils.class)

public class BottomNavigationActivity extends AppCompatActivity implements MenuProvider {

    private BottomNavigationView bottom_navigation;
    private final FragmentManager _fragmentManager =getSupportFragmentManager();
    private Fragment _fragment=null;
    private CartItemService _cartItemService;
    private TextView tvCartItemsCount;

    private Toolbar topAppBar;
    private RecyclerView _rvCartItems;
    private TextView tvEmptyCart;
    private RelativeLayout rlEmptyCart;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        Toolbar mToolbar =  findViewById(R.id.topAppBar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        addMenuProvider(this,this, Lifecycle.State.RESUMED);

        init();
        checkSession();
        //navigateToCatalog();
        populateCartItems();
        setHomeFragment();

        events();
    }

    private void populateCartItems() {
       // _rvCartItems.
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

        _cartItemService.
                getCartItems(topAppBar.getMenu().findItem(R.id.iCartWithBadge),this,_rvCartItems,rlEmptyCart);

    }
    private void setHomeFragment() {
        //Todo check user roles
        Fragment fragment = new CatalogFragment();

        _fragmentManager.beginTransaction().replace(R.id.flMain, fragment).commit();
    }

    private void init(){
        bottom_navigation = findViewById(R.id.bottom_navigation);
        _rvCartItems = findViewById(R.id.rvCartItems);
        tvCartItemsCount = findViewById(R.id.tvCartItemsCount);
        topAppBar = findViewById(R.id.topAppBar);


        CartFragment cartFragment = new CartFragment();

        FragmentManager manager = getSupportFragmentManager();
        CartFragment fragment = (CartFragment) manager.findFragmentByTag("CartFragment");
        FragmentTransaction transaction =
                manager.beginTransaction()
                        .add(R.id.flMain,cartFragment,"CartFragment");
        transaction.commit();



         if(fragment != null && fragment.getView() != null){

            rlEmptyCart = fragment.getView().findViewById(R.id.rlEmptyCart);

        }


        //tvEmptyCart= findViewById(R.id.tvEmptyCart);
        //setSupportActionBar(topAppBar);
    }

    private void events() {
        bottom_navigation.setOnItemSelectedListener(item -> {


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
                    return false;
                }



            }
            if(_fragment !=null){
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                fragmentTransaction
                        .replace(R.id.flMain,_fragment)
                        .addToBackStack("")
                        .commit();
            }
            return true;
        });
        //topAppBar.inflateMenu(R.menu.mn_top_navigation);
        topAppBar.setOnMenuItemClickListener(item -> {


            switch(item.getItemId()){

                case R.id.iCartWithBadge:{
                    _fragment = new CartFragment();
                }break;
                default:{
                    return false;
                }



            }
            if(_fragment !=null){
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                fragmentTransaction
                        .replace(R.id.flMain,_fragment)
                        .addToBackStack("")
                        .commit();
            }
            return true;
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




    public void navigateToCatalog(){
        invalidateOptionsMenu();

        setHomeFragment();
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {

        menuInflater.inflate(R.menu.mn_top_navigation, menu);


        MenuItem cartItem = menu.findItem(R.id.iCartWithBadge);

        if(cartItem != null) {
            //     menuUi.
            //      menu.removeItem(R.menu.mn_top_navigation);
        }




        // Add a badge to the cart item
        int count = countCartItems();

        addCartBadge(cartItem,count);



    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        return true;
    }


    private int countCartItems() {
        CartItemsSessionManagement cartItemsSessionManagement =
                new CartItemsSessionManagement(getApplicationContext(),false);

        var items = cartItemsSessionManagement.isValidSessionReturn();

        if(items.first == null) return 0;
        int count = 0;
        for (Product item:items.first) {
            count += item.quantity ;
        }


        return count;
    }

    public void addCartBadge(@Nullable MenuItem cartItem, int count){
        BadgeDrawable badgeDrawable = BadgeDrawable.create(this);

        View actionView = cartItem != null ? cartItem.getActionView() : null;



        if(!(count > 0)) {
            if(actionView != null) {
                ImageView cartIcon = actionView.findViewById(R.id.imgCartIcon);
                BadgeUtils.detachBadgeDrawable(badgeDrawable,cartIcon);
            }

            return;
        }

        // Create a BadgeDrawable instance

        badgeDrawable.setNumber(count); // Set the cart count
        badgeDrawable.setVisible(true); // Ensure the badge is visible
        badgeDrawable.setHorizontalOffset(55); // Adjust horizontal offset as needed
        badgeDrawable.setVerticalOffset(17);
        badgeDrawable.setBadgeGravity(BadgeDrawable.TOP_END);


        if (actionView != null) {

            // Attach the badge to the ImageView in the action layout
            ImageView cartIcon = actionView.findViewById(R.id.imgCartIcon);


          //  BadgeUtils.detachBadgeDrawable(badgeDrawable,cartIcon);
            BadgeUtils.attachBadgeDrawable(badgeDrawable, cartIcon);
        } else {
            //  Attach the badge to the MenuItem icon
            BadgeUtils.attachBadgeDrawable(badgeDrawable, findViewById(R.id.iCartWithBadge), null);

        }

//
//        if (cartItem.getActionView() != null) {
//            // Custom layout scenario: Find the ImageView inside the custom action layout and attach the badge to it
//            ImageView cartIcon = cartItem.getActionView().findViewById(R.id.imgCartIcon);
//            BadgeUtils.attachBadgeDrawable(badgeDrawable, cartIcon);
//        } else {
//            // No custom layout: Attach the badge to the menu item's icon (fallback to default icon)
//            BadgeUtils.attachBadgeDrawable(badgeDrawable, findViewById(R.id.iCartWithBadge), null);
//        }

    }
    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
//
//    public MenuItem getCartItem() {
//        return menu.findItem(R.id.iCartWithBadge);
//    }

    private void logout(){
        var userSession = new UserSessionManagement(getApplicationContext(),false);

        if(userSession.isValidSession()){
            userSession.removeSession();
            startActivity(new Intent(BottomNavigationActivity.this, LoginActivity.class));

        }
    }


}