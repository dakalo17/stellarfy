package com.example.shopandroid.services.implementations;

import android.util.Log;

import com.example.shopandroid.models.JSONObjects.User;
import com.example.shopandroid.models.jwt.JwtRefresh;
import com.example.shopandroid.services.DecodeToken;
import com.example.shopandroid.services.endpoints.IRefreshToken;
import com.example.shopandroid.services.endpoints.IUserEndpoints;
import com.example.shopandroid.services.session.RefreshTokenSessionManagement;
import com.example.shopandroid.services.session.UserSessionManagement;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Optional;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.shopandroid.utilities.Endpoints.BASE_URL;

public abstract class BaseService<T extends IRefreshToken> {

    protected AppCompatActivity _activity;
    protected T api;
    protected Retrofit retrofit;
    private Class<T> _classObj;
    protected OkHttpClient httpClient;
    public BaseService(AppCompatActivity activity, Class<T> classObj){

        _classObj = classObj;
        _activity = activity;
         Retrofit.Builder builder = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());


         UserSessionManagement userSessionManagement = new UserSessionManagement(_activity.getApplicationContext(),false);

         //if user is not logged in ,then there is no need at all for client side...
        // to validate/refresh tokens
         if(userSessionManagement.isValidSession()) {



             Interceptor authorization = new Interceptor() {

                 @NonNull
                 @Override
                 public Response intercept(@NonNull Chain chain) throws IOException {

                     final User user = userSessionManagement.getSession();
                     //receives request data of a service
                     Request req = chain.request();

                     //attaches a header so to try to prevent 401/unauthorized response
                     Request.Builder reqBuilder = req
                             .newBuilder()
                             .header("Authorization", "Bearer " + user.jwtToken)
                             .method(req.method(), req.body());


                     // propagate the request ->
                     Response res = chain.proceed(reqBuilder.build());


                     //if its unAuthorized -> refresh the token

                     if(res.code() == 401){
                         //makes sure that tasks finish before they continue
                         synchronized (this){
                             JwtRefresh jwtRefresh = null;
                             RefreshTokenSessionManagement refreshTokenSession =
                                     new RefreshTokenSessionManagement(_activity.getApplicationContext(),false);

                             //retrieve the current expired access token for server to extract
                             if(refreshTokenSession.isValidSession()) {
                                 jwtRefresh = new JwtRefresh();
                                 jwtRefresh.refreshToken = refreshTokenSession.getSession();
                                 jwtRefresh.token = user.jwtToken;
                             }

                             RefreshToken(jwtRefresh);

                         }
                     }
                     return res;
                 }
             };

             httpClient = new OkHttpClient()
                     .newBuilder()
                     .addInterceptor(authorization)
                     .build();

             builder.client(httpClient);
         }

        retrofit = builder.build();
        
        api = retrofit.create(classObj);
    }

    @Deprecated
    private void RefreshTokenAsync(JwtRefresh jwtTokenRefreshToken){

        //to make sure something is put instead of null
        Retrofit tempRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        T tempApi = tempRetrofit.create(_classObj);

        Call<JwtRefresh> call = tempApi.refreshToken(jwtTokenRefreshToken != null ? jwtTokenRefreshToken : new JwtRefresh());
//        retrofit2.Response<JwtRefresh> execute = null;
//        try {
//            execute = call.execute();
//        }catch (IOException ex){
//            Log.e("",ex.getLocalizedMessage());
//        }
//        if(execute != null && execute.isSuccessful())
//            return execute.body();
//
//        return null;


//        Call<T> call = nul;
        call.enqueue(new Callback<JwtRefresh>() {
            @Override
            public void onResponse(@NonNull Call<JwtRefresh> call, @NonNull retrofit2.Response<JwtRefresh> response) {
                saveToken(response);
            }

            @Override
            public void onFailure(Call<JwtRefresh> call, Throwable t) {
                Log.e("UserService",t.getLocalizedMessage());
            }
        });
    }
    private void RefreshToken(JwtRefresh jwtTokenRefreshToken){

        //re create the 2,so that there is no interceptor for them
        Retrofit tempRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        T tempApi = tempRetrofit.create(_classObj);

        //to make sure something is put instead of null
        Call<JwtRefresh> call = tempApi.refreshToken(jwtTokenRefreshToken != null ? jwtTokenRefreshToken : new JwtRefresh());

        retrofit2.Response<JwtRefresh> res ;

        try {
            //execute the request synchronously since
            res =  call.execute();
            //save token to shared pref
            JwtRefresh jwtRefreshRes  = saveToken(res);

            Log.e("Received token -> ",jwtRefreshRes != null ? jwtRefreshRes.token:"");

        } catch (IOException e) {
            Log.e("UserService",e.getLocalizedMessage());
        }


    }

    private JwtRefresh saveToken(retrofit2.Response<JwtRefresh> response){

        if(!response.isSuccessful())return null;

        JwtRefresh jwtRefreshRes = response.body();

        if(jwtRefreshRes == null) return null;

        User loggedUser = DecodeToken.DecodeUserClaims(jwtRefreshRes.token);
        new UserSessionManagement(_activity.getApplicationContext(),true).
                saveSession(loggedUser);
        return jwtRefreshRes;
    }
}
