package com.example.shopandroid.services.endpoints;

import com.example.shopandroid.models.JSONObjects.AbstractResponse;
import com.example.shopandroid.models.JSONObjects.CartItem;
import com.example.shopandroid.models.JSONObjects.CartProduct;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import static com.example.shopandroid.utilities.Endpoints.*;


public interface ICartItemEndpoint {
    @POST(POST_CART_ITEM)
    Call<AbstractResponse> postCartItem(@Body CartItem cartItem) ;

    @GET(GET_CART_ITEMS)
    Call<ArrayList<CartProduct>> getCartItems(@Path("cartId")int cartId);

    @GET(GET_CART_ITEM)
    Call<CartItem> getCartItem();


    @PUT(PUT_CART_ITEM)
    Call<AbstractResponse> putCartItem(@Body CartItem cartItem);
}
