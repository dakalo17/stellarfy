package com.example.shopandroid.services.implementations;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;

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
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ConnectionPool;
import retrofit2.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.shopandroid.utilities.Endpoints.BASE_URL;

public abstract class BaseService<T> {

    //protected AppCompatActivity _activity;
    protected T api;
    public OkHttpClient.Builder okHttpClientBuilder;
    protected Retrofit retrofit;
    protected OkHttpClient httpClient;
    protected Context context;
    public BaseService(Context context,Class<T> classObj){

        //_activity = activity;
        this.context =context;
         Retrofit.Builder builder = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());


         UserSessionManagement userSessionManagement = new UserSessionManagement(context,false);

         //if user is not logged in ,then there is no need at all for client side...
        // to validate/refresh tokens

       okHttpClientBuilder =
                new OkHttpClient()
                    .newBuilder()
                    .connectTimeout(40, TimeUnit.SECONDS)
                    .readTimeout(40, TimeUnit.SECONDS)
                    .writeTimeout(40, TimeUnit.SECONDS)
                    .connectionPool(new ConnectionPool(5, 10, TimeUnit.MINUTES))
                        .callTimeout(1,TimeUnit.MINUTES)

       ;

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
                             .header("Content-Type","application/json")
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
                                     new RefreshTokenSessionManagement(context,false);

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
            okHttpClientBuilder.addInterceptor(authorization);
         }

         Interceptor retryInterceptor =new Interceptor() {
             private int retryCount;
             private final int MAX_RETRIES = 10;
             @NonNull
             @Override
             public Response intercept(@NonNull Chain chain) throws IOException {
                 
                 Request request = chain.request();
                 Response response = chain.proceed(request);

                 while (response.code() == 503 && retryCount < MAX_RETRIES) {
                     retryCount++;
                     response = chain.proceed(request);
                 }

                 return response;
             }
         };

         okHttpClientBuilder.addInterceptor(retryInterceptor);
        httpClient = okHttpClientBuilder.build();
        builder.client(httpClient);
        retrofit = builder.build();
        
        api = retrofit.create(classObj);

    }
    private void RefreshToken(JwtRefresh jwtTokenRefreshToken){

        //re create the 2,so that there is no interceptor for them
        Retrofit tempRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IRefreshToken tempApi = tempRetrofit.create(IRefreshToken.class);

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
        new UserSessionManagement(context,true).
                saveSession(loggedUser);
        return jwtRefreshRes;
    }

/**
 *  @deprecated use the synchronous version since a jwt token needs to be refreshed before running your request
 *  {@code new RefreshToken(JwtRefresh jwtTokenRefreshToken)}
 * */
    @Deprecated
    private void RefreshTokenAsync(JwtRefresh jwtTokenRefreshToken){

        //to make sure something is put instead of null
        Retrofit tempRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IRefreshToken tempApi = tempRetrofit.create(IRefreshToken.class);

        Call<JwtRefresh> call = tempApi.refreshToken(jwtTokenRefreshToken != null ? jwtTokenRefreshToken : new JwtRefresh());

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

}
