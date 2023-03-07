package com.example.shopandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopandroid.activities.LoginActivity;
import com.example.shopandroid.services.endpoints.ITestEndpoints;
import com.example.shopandroid.services.implementations.TestService;
import com.example.shopandroid.services.session.UserSessionManagement;

public class HomeActivity extends AppCompatActivity {

    private TextView textView;
    TestService<ITestEndpoints> _testService;
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
        _testService = new TestService<>(this,ITestEndpoints.class);
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