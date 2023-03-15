package com.example.shopandroid.services.implementations;

import android.content.Context;

import com.example.shopandroid.models.JSONObjects.AbstractResponse;
import com.example.shopandroid.models.JSONObjects.CartItem;
import com.example.shopandroid.services.endpoints.ICartItemEndpoint;

import java.util.ArrayList;

import retrofit2.Call;

public class CartItemService extends BaseService<ICartItemEndpoint> {
    public CartItemService(Context context, Class<ICartItemEndpoint> classObj) {
        super(context, classObj);
    }


    public void getCartItems(){
        Call<ArrayList<CartItem>> call = api.getCartItems();
    }

    public void postCartItem(CartItem cartItem){
        Call<AbstractResponse> call =api.postCartItem(cartItem);
    }

    public void getCartItem(){
        Call<CartItem> call = api.getCartItem();
    }

    public void putCartItem(int cartId,CartItem cartItem){
        Call<AbstractResponse> call = api.putCartItem(cartId,cartItem);
    }
}
