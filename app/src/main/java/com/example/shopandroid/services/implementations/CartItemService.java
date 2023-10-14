package com.example.shopandroid.services.implementations;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.shopandroid.models.JSONObjects.AbstractResponse;
import com.example.shopandroid.models.JSONObjects.CartItem;
import com.example.shopandroid.models.JSONObjects.Product;
import com.example.shopandroid.services.endpoints.ICartItemEndpoint;
import com.example.shopandroid.services.session.CartItemsSessionManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartItemService extends BaseService<ICartItemEndpoint> {
    private static final String TAG = "Cart Item Service";
    private final Context _context;
    public CartItemService(Context context) {
        super(context, ICartItemEndpoint.class);
        _context = context;
    }


    public void getCartItems(){
        Call<ArrayList<Product>> call = api.getCartItems();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Product>> call, @NonNull Response<ArrayList<Product>> response) {
                if(!response.isSuccessful())return;

                var res = response.body();

                    if(res == null)return;

                //add to the session/cache as NEW
                var cartSession = new CartItemsSessionManagement(_context,true);
                cartSession.saveSession(res);

            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Product>> call, @NonNull Throwable t) {
                Log.e(TAG, Objects.requireNonNull(t.getLocalizedMessage()));
            }
        });
    }

    public void postCartItem(CartItem cartItem,boolean isUpdate){
        Call<Product> call =api.postCartItem(cartItem,isUpdate);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                if(!response.isSuccessful())return;

                Product product = response.body();
                if(product == null)return;

                var cartSession = new CartItemsSessionManagement(_context,false);
                var sessionValidity = cartSession.isValidSessionReturn();
                if(sessionValidity.second) {
                    //update the quantity
                    product.quantity = cartItem.Quantity;

                    if (!cartSession.editSessionJSON(product))
                        Toast.makeText(_context, "Cart Item ERROR NOT UPDATED", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(_context, "Y", Toast.LENGTH_LONG).show();
                }else
                    Toast.makeText(_context, "CART CACHE IS INVALID", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
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
