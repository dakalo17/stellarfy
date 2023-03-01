package com.example.shopandroid.services.implementations;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.shopandroid.utilities.Endpoints.BASE_URL;

public abstract class BaseService<T> {

    protected AppCompatActivity activity;
    protected T api;
    protected Retrofit retrofit;

    public BaseService(AppCompatActivity activity, Class<T> classObj){

        retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(classObj);
    }

}
