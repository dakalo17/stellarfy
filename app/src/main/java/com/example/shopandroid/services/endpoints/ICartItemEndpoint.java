package com.example.shopandroid.services.endpoints;

import static com.example.shopandroid.utilities.Endpoints.DELETE_CART_PRODUCT;
import static com.example.shopandroid.utilities.Endpoints.GET_CART_PRODUCTS;
import static com.example.shopandroid.utilities.Endpoints.POST_CART_PRODUCT;

import com.example.shopandroid.models.JSONObjects.AbstractResponse;
import com.example.shopandroid.models.JSONObjects.CartItem;
import com.example.shopandroid.models.JSONObjects.Product;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ICartItemEndpoint {
    @POST(POST_CART_PRODUCT)
    Call<Product> postCartItem(@Body CartItem cartItem,@Query("isUpdate") boolean isUpdate) ;

    @GET(GET_CART_PRODUCTS)
    Call<ArrayList<Product>> getCartItems();

    //it doesn't really delete but flags as deleted
    @DELETE(DELETE_CART_PRODUCT)
    Call<AbstractResponse> deleteCartItem(@Path("productId") int productId);

    @GET(" ")
    Call<CartItem> getCartItem();

    @PUT(" ")
    Call<AbstractResponse> putCartItem(@Path("id") int id,@Body CartItem cartItem);
}
