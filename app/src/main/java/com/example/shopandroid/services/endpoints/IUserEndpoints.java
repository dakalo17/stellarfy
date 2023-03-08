package com.example.shopandroid.services.endpoints;

import com.example.shopandroid.models.JSONObjects.User;
import com.example.shopandroid.models.jwt.JwtRefresh;
import com.example.shopandroid.utilities.Endpoints;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IUserEndpoints extends IRefreshToken{
    @Headers("Content-Type: application/json")
    @POST(Endpoints.LOGIN_URI)
    Call<JwtRefresh> login(@Header("Authorization") String authorization, @Body User user);



}
