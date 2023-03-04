package com.example.shopandroid.models.JSONObjects;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    public int id;
    @SerializedName("firstname")
    public String firstname;
    @SerializedName("lastname")
    public String lastname;
    @SerializedName("username")
    public String username;
    @SerializedName("password")
    public String password;


}
