package com.example.shopandroid.models.JSONObjects;

import com.google.gson.annotations.SerializedName;

public class CartProduct {
    @SerializedName("cart_product_id")
    public int cart_product_id;
    @SerializedName("name")
    public String name;
    @SerializedName("price")
    public double price;
    @SerializedName("image_link")
    public String image_link;
    @SerializedName("special_price")
    public double special_price;
    @SerializedName("quantity")
    public int quantity;
}
