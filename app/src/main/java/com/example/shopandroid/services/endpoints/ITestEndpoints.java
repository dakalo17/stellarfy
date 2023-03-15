package com.example.shopandroid.services.endpoints;

import com.example.shopandroid.models.JSONObjects.AbstractResponse;
import com.example.shopandroid.utilities.Endpoints;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ITestEndpoints {

    @GET("api/WeatherForecast/GetTest")
    Call<AbstractResponse> getTest();
}
