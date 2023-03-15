package com.example.shopandroid.models.JSONObjects;

import com.google.gson.annotations.SerializedName;

public class CartItem {
    @SerializedName("id")
    public int Id ;
    @SerializedName("fk_Order_Id")
    public int Fk_Order_Id ;
    @SerializedName("fk_Product_Id")
    public int Fk_Product_Id ;
    @SerializedName("quantity")
    public int Quantity ;
    @SerializedName("price")
    public double Price;
}
