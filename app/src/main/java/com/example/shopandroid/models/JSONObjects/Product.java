package com.example.shopandroid.models.JSONObjects;

import java.io.Serializable;

public class Product implements Serializable {
    @SuppressWarnings("id")
    public int id;
    @SuppressWarnings("name")
    public String name;
    @SuppressWarnings("price")
    public double price ;
    @SuppressWarnings("specialPrice")
    public double specialPrice ;
    @SuppressWarnings("description")
    public String description;
    @SuppressWarnings("imageLink")
    public String imageLink ;
    @SuppressWarnings("quantity")
    public int quantity;

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
