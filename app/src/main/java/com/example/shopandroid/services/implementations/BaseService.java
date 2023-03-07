package com.example.shopandroid.services.implementations;

import com.example.shopandroid.services.session.UserSessionManagement;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.shopandroid.utilities.Endpoints.BASE_URL;

public abstract class BaseService<T> {

    protected AppCompatActivity _activity;
    protected T api;
    protected Retrofit retrofit;

    protected OkHttpClient httpClient;
    public BaseService(AppCompatActivity activity, Class<T> classObj){

        _activity = activity;
         Retrofit.Builder builder = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());


         UserSessionManagement userSessionManagement = new UserSessionManagement(_activity.getApplicationContext(),false);

         if(userSessionManagement.isValidSession()) {

             Interceptor authorization = new Interceptor() {
                 @NonNull
                 @Override
                 public Response intercept(@NonNull Chain chain) throws IOException {
                     Request req = chain.request();
                     Request.Builder reqBuilder = req
                             .newBuilder()
                             .header("Authorization", "Bearer " + userSessionManagement.getSession().jwtToken)
                             .method(req.method(), req.body());

                     return chain.proceed(reqBuilder.build());
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

}
