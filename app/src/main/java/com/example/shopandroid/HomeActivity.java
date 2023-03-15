package com.example.shopandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopandroid.activities.LoginActivity;
import com.example.shopandroid.models.jwt.JwtRefresh;
import com.example.shopandroid.services.endpoints.ITestEndpoints;
import com.example.shopandroid.services.implementations.TestService;
import com.example.shopandroid.services.session.RefreshTokenSessionManagement;
import com.example.shopandroid.services.session.UserSessionManagement;

public class HomeActivity extends AppCompatActivity {

    private TextView textView;
    TestService _testService;
    private Button btnClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(!new UserSessionManagement(getApplicationContext(),false).isValidSession()) {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        }

        init();
        events();
        start();
    }
    private void init(){
        btnClick = findViewById(R.id.btnClick);
        textView = findViewById(R.id.textView);

        JwtRefresh jwtRefresh = null;
        RefreshTokenSessionManagement refreshTokenSession = new RefreshTokenSessionManagement(getApplicationContext(),false);
        if(refreshTokenSession.isValidSession()) {
            jwtRefresh = new JwtRefresh();
            jwtRefresh.refreshToken = refreshTokenSession.getSession();
            jwtRefresh.token = null;
        }

        _testService = new TestService(getApplicationContext());
    }
    private void events() {
        btnClick.setOnClickListener(e->{
            //try refresh
            //startActivity(new Intent(HomeActivity.this, HomeActivity.class));
            _testService.getTest(textView);
        });
    }



    private void start(){
        //refresh token???

    }
}