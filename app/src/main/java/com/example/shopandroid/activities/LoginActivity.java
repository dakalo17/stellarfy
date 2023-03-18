package com.example.shopandroid.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.shopandroid.HomeActivity;
import com.example.shopandroid.R;
import com.example.shopandroid.fragments.CatalogFragment;
import com.example.shopandroid.models.JSONObjects.User;
import com.example.shopandroid.models.jwt.JwtRefresh;
import com.example.shopandroid.models.jwt.RefreshToken;
import com.example.shopandroid.services.endpoints.IUserEndpoints;
import com.example.shopandroid.services.implementations.UserService;
import com.example.shopandroid.services.session.RefreshTokenSessionManagement;
import com.example.shopandroid.services.session.UserSessionManagement;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        _userSession =new UserSessionManagement(getApplicationContext(),false);
        if(_userSession.isValidSession()) {
            startActivity(new Intent(LoginActivity.this, BottomNavigationActivity.class));
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
}