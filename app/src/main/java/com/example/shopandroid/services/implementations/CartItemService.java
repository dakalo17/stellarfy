package com.example.shopandroid.services.implementations;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.shopandroid.adapters.CartProductAdapter;
import com.example.shopandroid.models.JSONObjects.AbstractResponse;
import com.example.shopandroid.models.JSONObjects.CartItem;
import com.example.shopandroid.models.JSONObjects.CartProduct;
import com.example.shopandroid.services.endpoints.ICartItemEndpoint;
import com.example.shopandroid.services.session.UserSessionManagement;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartItemService extends BaseService<ICartItemEndpoint> {
    private static final String TAG ="CartItemService";
    private Fragment _fragment;
    private final UserSessionManagement _userSession ;
    public CartItemService( Fragment fragment) {
        super(fragment.requireContext(), ICartItemEndpoint.class);
        _fragment = fragment;
        _userSession =
                new UserSessionManagement(_fragment.requireContext(),false);

    }



    public void getCartItems(RecyclerView rvCartProducts,int retry){

        if(retry == 10)return;




        Call<ArrayList<CartProduct>> call =
                api.getCartItems(_userSession.getSession().cartId);


        call.enqueue(new Callback<ArrayList<CartProduct>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<CartProduct>> call, @NonNull Response<ArrayList<CartProduct>> response) {

                if(!response.isSuccessful())
                    if(response.raw().code() == 401) {
                        getCartItems(rvCartProducts,retry+1);
                        return;
                    }

                ArrayList<CartProduct> res = response.body();


                LinearLayoutManager linearLayoutManager=
                        new LinearLayoutManager(_fragment.requireContext());


                CartProductAdapter cartItemsAdapter =
                        new CartProductAdapter(res, _fragment);

                rvCartProducts.setLayoutManager(linearLayoutManager);
                rvCartProducts.setAdapter(cartItemsAdapter);

            }

            @Override
            public void onFailure(Call<ArrayList<CartProduct>> call, Throwable t) {
                Log.e(TAG,t.getLocalizedMessage() != null?t.getLocalizedMessage():"null" );
                getCartItems(rvCartProducts,retry+1);
            }
        });

    }

    //add item to cart
    public void postCartItem(CartItem cartItem,int count){

        if(count == 10) return;

        Call<AbstractResponse> call =api.postCartItem(cartItem);

        call.enqueue(new Callback<AbstractResponse>() {
            @Override
            public void onResponse(Call<AbstractResponse> call, Response<AbstractResponse> response) {

                if(!response.isSuccessful())
                    if(response.raw().code() == 401) {
                        postCartItem(cartItem, count + 1);
                        return;
                    }


                AbstractResponse res = response.body();
                if(res == null) return;
                Toast.makeText(_fragment.getContext(), res.response +" from cart", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<AbstractResponse> call, Throwable t) {
                Log.e(TAG,t.getLocalizedMessage() != null?t.getLocalizedMessage():"null" );
                new Handler(Looper.myLooper()).postDelayed(
                        () ->postCartItem(cartItem,count+1),2000);
            }
        });
    }

    public void getCartItem(){
        Call<CartItem> call = api.getCartItem();

        call.enqueue(new Callback<CartItem>() {
            @Override
            public void onResponse(Call<CartItem> call, Response<CartItem> response) {

            }

            @Override
            public void onFailure(Call<CartItem> call, Throwable t) {
                Log.e(TAG,t.getLocalizedMessage() != null?t.getLocalizedMessage():"null" );
            }
        });
    }

    public void putCartItem(CartItem cartItem, int retry, TextInputLayout tvQuantityCartItemContainer){

        if(retry == 10)return;
        Call<AbstractResponse> call = api.putCartItem(cartItem);


        call.enqueue(new Callback<AbstractResponse>() {
            @Override
            public void onResponse(Call<AbstractResponse> call, Response<AbstractResponse> response) {
                if(!response.isSuccessful())
                    if(response.raw().code() == 401) {
                        putCartItem(cartItem, retry + 1, tvQuantityCartItemContainer);
                        return;
                    }


                AbstractResponse res = response.body();
                if(res == null) return;
                Toast.makeText(_fragment.getContext(), res.response +" from cart", Toast.LENGTH_LONG).show();
                if(!res.response.equals("0")) {
                    tvQuantityCartItemContainer.setHint(""+cartItem.Quantity);

                }
            }

            @Override
            public void onFailure(Call<AbstractResponse> call, Throwable t) {
                Log.e(TAG,t.getLocalizedMessage() != null?t.getLocalizedMessage():"null" );
            }
        });
    }
}
