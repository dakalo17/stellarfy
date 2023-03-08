package com.example.shopandroid.services.implementations;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.shopandroid.MainActivity;
import com.example.shopandroid.activities.LoginActivity;
import com.example.shopandroid.models.JSONObjects.User;
import com.example.shopandroid.models.jwt.JwtRefresh;
import com.example.shopandroid.services.DecodeToken;
import com.example.shopandroid.services.endpoints.IRefreshToken;
import com.example.shopandroid.services.endpoints.IUserEndpoints;
import com.example.shopandroid.services.session.RefreshTokenSessionManagement;
import com.example.shopandroid.services.session.UserSessionManagement;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService<T extends IUserEndpoints> extends BaseService<T>{


    public UserService(AppCompatActivity activity, Class<T> classObj) {
        super(activity, classObj);
        _activity=activity;
    }


    public void Login(User user,User userRetrieved){
        Call<JwtRefresh> call = api.login("Bearer ",user);

        call.enqueue(new Callback<JwtRefresh>() {
            @Override
            public void onResponse(Call<JwtRefresh> call, Response<JwtRefresh> response) {
                if(!response.isSuccessful())return;

                JwtRefresh jwtPair = response.body();

                if(jwtPair==null) return ;

                //decode jwt token - no need to verify it - then put to session
                User loggedUser = DecodeToken.DecodeUserClaims(jwtPair.token);
                UserSessionManagement userSessionManagement =
                        new UserSessionManagement(_activity.getApplicationContext(), true);
                userSessionManagement.saveSession(loggedUser);
                //save refresh token to session,ie local storage of device
                ///Todo
                new RefreshTokenSessionManagement(_activity.getApplicationContext(),true).
                        saveSession(jwtPair.refreshToken);

                //copy
                //userRetrieved.copy(loggedUser);
                if(userSessionManagement.isValidSession()){
                    _activity.startActivity(new Intent(_activity.getApplicationContext(), LoginActivity.class));
                }
            }


            @Override
            public void onFailure(Call<JwtRefresh> call, Throwable t) {
                Log.e("UserService",t.getLocalizedMessage());

            }
        });

    }

}
