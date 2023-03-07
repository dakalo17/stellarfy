package com.example.shopandroid.models.jwt;

import com.google.gson.annotations.SerializedName;

public class RefreshToken {
    @SerializedName("fkUserId")
    public int fkUserId;
    @SerializedName("rToken")
    public String rToken;
    @SerializedName("key")
    public String key;
    @SerializedName("expiringDate")
    public String expiringDate;

    public String expiringDate_Date;
}
