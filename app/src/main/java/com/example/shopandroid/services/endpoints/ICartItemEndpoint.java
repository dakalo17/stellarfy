package com.example.shopandroid.services.endpoints;

import com.example.shopandroid.models.JSONObjects.AbstractResponse;
import com.example.shopandroid.models.JSONObjects.CartItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ICartItemEndpoint {
    @POST(" ")
    Call<AbstractResponse> postCartItem(@Body CartItem cartItem) ;

    @GET(" ")
    Call<ArrayList<CartItem>> getCartItems();

    @GET(" ")
    Call<CartItem> getCartItem();

    @PUT(" ")
    Call<AbstractResponse> putCartItem(@Path("id") int id,@Body CartItem cartItem);
}
