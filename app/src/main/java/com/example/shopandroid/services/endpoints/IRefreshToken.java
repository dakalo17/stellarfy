package com.example.shopandroid.services.endpoints;

import com.example.shopandroid.models.jwt.JwtRefresh;
import com.example.shopandroid.utilities.Endpoints;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IRefreshToken {
    @Headers("Content-Type: application/json")
    @POST(Endpoints.REFRESH_TOKEN_URI)
    Call<JwtRefresh> refreshToken(@Body JwtRefresh jwtRefresh);
}
