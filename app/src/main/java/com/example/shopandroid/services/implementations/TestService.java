package com.example.shopandroid.services.implementations;

import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopandroid.HomeActivity;
import com.example.shopandroid.activities.LoginActivity;
import com.example.shopandroid.activities.NavigationActivity;
import com.example.shopandroid.models.JSONObjects.AbstractResponse;
import com.example.shopandroid.services.endpoints.ITestEndpoints;
import com.example.shopandroid.services.session.UserSessionManagement;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestService<T extends ITestEndpoints> extends BaseService<T> {
    public TestService(AppCompatActivity activity, Class<T> classObj) {
        super(activity, classObj);
    }

    public void getTest(TextView textView){
        Call<AbstractResponse> call = api.getTest();

        call.enqueue(new Callback<AbstractResponse>() {
            @Override
            public void onResponse(Call<AbstractResponse> call, Response<AbstractResponse> response) {

                int code = response.raw().code();

                if(code == 401){
                    //then logg out
                    new UserSessionManagement(_activity.getApplicationContext(),false).
                            removeSession();
                    _activity.startActivity(new Intent(_activity, HomeActivity.class));

                }
                if(!response.isSuccessful())return;
                AbstractResponse res = response.body();

                final String pre = textView.getText().toString();
                textView.setText(res == null ? pre: res.response);
                _activity.startActivity(new Intent(_activity, NavigationActivity.class));

            }

            @Override
            public void onFailure(Call<AbstractResponse> call, Throwable t) {
                Log.e("Test Service",t.getLocalizedMessage());
                Toast.makeText(_activity.getApplicationContext(), "Error...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
