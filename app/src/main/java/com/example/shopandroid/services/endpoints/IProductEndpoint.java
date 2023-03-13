package com.example.shopandroid.services.endpoints;

import com.example.shopandroid.models.JSONObjects.Product;
import com.example.shopandroid.utilities.Endpoints;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import static com.example.shopandroid.utilities.Endpoints.GET_PRODUCTS;
import static com.example.shopandroid.utilities.Endpoints.GET_PRODUCT_ID;
import static com.example.shopandroid.utilities.Endpoints.GET_PRODUCT_NAME;

public interface IProductEndpoint {
    @GET(GET_PRODUCTS)
    Call<ArrayList<Product>>getProduct();

    @GET(GET_PRODUCT_ID)
    Call<Product> getProduct(@Path("id") int id);

    @GET(GET_PRODUCT_NAME)
    Call<Product> getProduct(@Path("productName") String productName);
}
