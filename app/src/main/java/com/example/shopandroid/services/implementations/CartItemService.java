package com.example.shopandroid.services.implementations;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.shopandroid.models.JSONObjects.AbstractResponse;
import com.example.shopandroid.models.JSONObjects.CartItem;
import com.example.shopandroid.services.endpoints.ICartItemEndpoint;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartItemService extends BaseService<ICartItemEndpoint> {
    private static final String TAG = "Cart Item Service";
    private Context _context;
    public CartItemService(Context context) {
        super(context, ICartItemEndpoint.class);
        _context = context;
    }


    public void getCartItems(){
        Call<ArrayList<CartItem>> call = api.getCartItems();
    }

    public void postCartItem(CartItem cartItem){
        Call<AbstractResponse> call =api.postCartItem(cartItem);

        call.enqueue(new Callback<AbstractResponse>() {
            @Override
            public void onResponse(@NonNull Call<AbstractResponse> call, @NonNull Response<AbstractResponse> response) {
                if(!response.isSuccessful())return;

                AbstractResponse res = response.body();


                Toast.makeText(_context,res!=null?res.response:"",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(@NonNull Call<AbstractResponse> call, @NonNull Throwable t) {
                Log.e(TAG, Objects.requireNonNull(t.getLocalizedMessage()));
            }
        });
    }

    public void getCartItem(){
        Call<CartItem> call = api.getCartItem();
    }

    public void putCartItem(int cartId,CartItem cartItem){
        Call<AbstractResponse> call = api.putCartItem(cartId,cartItem);
    }
}
