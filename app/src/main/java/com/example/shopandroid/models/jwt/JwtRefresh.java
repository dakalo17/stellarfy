package com.example.shopandroid.models.jwt;

import com.google.gson.annotations.SerializedName;

public class JwtRefresh {
    @SerializedName("token")
    public String token;
    @SerializedName("refreshToken")
    public RefreshToken refreshToken;
}
