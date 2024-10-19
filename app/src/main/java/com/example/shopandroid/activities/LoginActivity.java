package com.example.shopandroid.activities;

import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.shopandroid.R;
import com.example.shopandroid.models.JSONObjects.User;
import com.example.shopandroid.services.implementations.CartItemService;
import com.example.shopandroid.services.implementations.UserService;
import com.example.shopandroid.services.session.CartItemsSessionManagement;
import com.example.shopandroid.services.session.RefreshTokenSessionManagement;
import com.example.shopandroid.services.session.UserSessionManagement;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.badge.ExperimentalBadgeUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
@OptIn(markerClass = ExperimentalBadgeUtils.class)
public class LoginActivity extends AppCompatActivity {


    private Button btnLogin;
    private ImageButton imgBtnForgot;
    private ImageButton imgBtnNotSignedUp;

        private TextInputEditText tvUsername;
    private TextInputLayout tvUsernameContainer;

    private TextInputEditText tvPassword;
    private TextInputLayout tvPasswordContainer;

    private UserService _userService;
    private UserSessionManagement _userSession;

    private CartItemService _cartItemService;



    public MaterialToolbar topAppBar;
    private TextView tvCartItemsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        _userSession =new UserSessionManagement(getApplicationContext(),false);
        if(_userSession.isValidSession()) {
            startActivity(new Intent(LoginActivity.this, BottomNavigationActivity.class));
            //remove any carts session
            new CartItemsSessionManagement(getApplicationContext(),true);
            pullCarts();
        }
        init();
        start();
    }

    private void init(){
        btnLogin = findViewById(R.id.btnLogin);
        imgBtnForgot = findViewById(R.id.imgBtnForgot);
        imgBtnNotSignedUp = findViewById(R.id.imgBtnNotSignedUp);

        tvUsername = findViewById(R.id.tvUsername);
        tvUsernameContainer = findViewById(R.id.tvUsernameContainer);
        tvPassword = findViewById(R.id.tvPassword);
        tvPasswordContainer = findViewById(R.id.tvPasswordContainer);

        tvCartItemsCount = findViewById(R.id.tvCartItemsCount);

        topAppBar = findViewById(R.id.topAppBar);

        RefreshTokenSessionManagement refreshTokenSession =
                new RefreshTokenSessionManagement(getApplicationContext(),false);


        _userService = new UserService(this);

    }
    private void start() {

        btnLogin.setOnClickListener(e->{
            User userRetrieve = new User();

            _userService.Login(new User(Objects.requireNonNull(tvUsername.getText(),"username must ! null").toString(),
                            Objects.requireNonNull(tvPassword.getText(),"password must ! null").toString())
                    ,userRetrieve);
        });

        imgBtnForgot.setOnClickListener(e->{
            ///Todo
            Toast.makeText(getApplicationContext(), "Forgot password!!!!", Toast.LENGTH_SHORT).show();
        });

        imgBtnNotSignedUp.setOnClickListener(e->{
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        });

    }

    private void pullCarts(){
        _cartItemService= new CartItemService(getApplicationContext());

        //_cartItemService.getCartItems(topAppBar.getMenu().findItem(R.id.iCartWithBadge),this, _rvCartItems);

    }

    public void  addCartBadge(MenuItem cartItem, int count){
        BadgeDrawable badgeDrawable = BadgeDrawable.create(this);
        View actionView = cartItem.getActionView();

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
            BadgeUtils.attachBadgeDrawable(badgeDrawable, cartIcon);
        } else {
            // Fallback: Attach the badge to the MenuItem icon
            //BadgeUtils.attachBadgeDrawable(badgeDrawable, findViewById(R.id.iCartWithBadge), null);
        }


    }
}