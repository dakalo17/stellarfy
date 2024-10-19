package com.example.shopandroid.services.implementations;

import android.content.Context;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopandroid.activities.BottomNavigationActivity;
import com.example.shopandroid.activities.LoginActivity;
import com.example.shopandroid.adapters.CartItemsAdapter;
import com.example.shopandroid.models.JSONObjects.AbstractResponse;
import com.example.shopandroid.models.JSONObjects.CartItem;
import com.example.shopandroid.models.JSONObjects.Product;
import com.example.shopandroid.services.endpoints.ICartItemEndpoint;
import com.example.shopandroid.services.session.CartItemsSessionManagement;
import com.google.android.material.progressindicator.CircularProgressIndicator;

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

    public void getCartItemsMain(RecyclerView rvCartItems, RelativeLayout rlEmptyCart,
                                 CircularProgressIndicator cartLoadingIndicator){
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




                var countItems =res.size();
                boolean EmptyCartIsVISIBLE =rlEmptyCart != null && rlEmptyCart.getVisibility() == View.VISIBLE;
                boolean CartItemsIsGONE = rvCartItems != null && rvCartItems.getVisibility() == View.GONE;

                if(countItems > 0 ) {
                    cartLoadingIndicator.setVisibility(View.GONE);
                    if (CartItemsIsGONE) {
                        rvCartItems.setVisibility(View.VISIBLE);
                        if (EmptyCartIsVISIBLE)
                            rlEmptyCart.setVisibility(View.GONE);
                    }

                }else if (rlEmptyCart != null) {
                    rlEmptyCart.setVisibility(View.VISIBLE);
                    if(rvCartItems != null)
                        rvCartItems.setVisibility(View.GONE);
                }else {

                    cartLoadingIndicator.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Product>> call, @NonNull Throwable t) {
                Log.e(TAG, Objects.requireNonNull(t.getLocalizedMessage()));
            }
        });
    }
    public void getCartItems(@Nullable MenuItem menuItem, @Nullable AppCompatActivity activity, RecyclerView rvCartItems, RelativeLayout rlEmptyCart){
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




                var countItems =getCountItems(cartSession.getSession());

                if(countItems > 0 ) {
                    boolean showEmptyCart =rlEmptyCart != null && rlEmptyCart.getVisibility() == View.VISIBLE;
                    boolean showCartItems = rvCartItems != null && rvCartItems.getVisibility() == View.VISIBLE;
                    if (showCartItems && !showEmptyCart)
                        rvCartItems.setVisibility(View.VISIBLE);
                    if(showEmptyCart && !showCartItems )
                        rlEmptyCart.setVisibility(View.VISIBLE);
                }

                if(activity instanceof BottomNavigationActivity) {

                    BottomNavigationActivity bactivity = (BottomNavigationActivity) activity;

                  //  bactivity.addCartBadge(menuItem,countItems);
                }else if(activity instanceof LoginActivity){
                    LoginActivity lactivity = (LoginActivity)activity;

              //      lactivity.addCartBadge(menuItem,countItems);

                }else if (activity == null){
                    //a fragment

                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Product>> call, @NonNull Throwable t) {
                Log.e(TAG, Objects.requireNonNull(t.getLocalizedMessage()));
            }
        });
    }

    private int getCountItems(List<Product> products) {

        int sum = 0;
        for(var product : products){
            sum += product.quantity;
        }
        return sum;
    }

    public void postCartItem(CartItem cartItem, boolean isUpdate){
        Call<Product> call =api.postCartItem(cartItem,isUpdate);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                if(!response.isSuccessful())return;

                Product product = response.body();
                if(product == null)return;
                // if deleted
                var cartSession = new CartItemsSessionManagement(_context,false);
                var sessionValidity = cartSession.isValidSessionReturn();
                if(sessionValidity.second) {
                    //update the quantity
                    //product.quantity = cartItem.Quantity;

                    //
                    var items = cartSession.getSession();
                    boolean updated = false;
                    for (int i = 0; i < items.size(); i++) {
                        if(items.get(i).id == product.id){
                            items.get(i).quantity = cartItem.Quantity;
                            updated = true;
                            cartSession.saveSession(items);
                            break;
                        }else{
                            //Updating quantity
                            if (cartSession.editSessionJSON(product)){
                                updated = true;

                                if(product.quantity == 0)
                                    Toast.makeText(_context, "Cart Item deleted", Toast.LENGTH_LONG).show();

                                break;

                            }
                        }
                    }

                    //HERE IS THE PROBLEM
                    //cartSession.editSessionJSON(product)
                    if (!updated)
                        Toast.makeText(_context, "Cart Item ERROR NOT UPDATED", Toast.LENGTH_LONG).show();
                    else {
                        Toast.makeText(_context, "Cart Item quantity updated", Toast.LENGTH_LONG).show();
                    }

                }else
                    Toast.makeText(_context, "CART CACHE IS INVALID", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                Log.e(TAG, Objects.requireNonNull(t.getLocalizedMessage()));
            }
        });
    }
    public void postCartItemDelete(CartItem cartItem, boolean isUpdate,int position,CartItemsAdapter adapter){
        Call<Product> call =api.postCartItem(cartItem,isUpdate);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                if(!response.isSuccessful())return;

                Product product = response.body();
                if(product == null)return;
                // if deleted

                adapter.deleteItemFromCart(position);
                var cartSession = new CartItemsSessionManagement(_context,false);
                var sessionValidity = cartSession.isValidSessionReturn();
                if(sessionValidity.second) {
                    //update the quantity
                    //product.quantity = cartItem.Quantity;

                    //
                    var items = cartSession.getSession();
                    boolean updated = false;
                    for (int i = 0; i < items.size(); i++) {
                        if(items.get(i).id == product.id){
                            items.get(i).quantity = cartItem.Quantity;
                            updated = true;
                            cartSession.saveSession(items);
                            break;
                        }else{
                            //Updating quantity
                            if (cartSession.editSessionJSON(product)){
                                updated = true;

                                if(product.quantity == 0)
                                    Toast.makeText(_context, "Cart Item deleted", Toast.LENGTH_LONG).show();

                                break;

                            }
                        }
                    }

                    //HERE IS THE PROBLEM
                    //cartSession.editSessionJSON(product)
                    if (!updated)
                        Toast.makeText(_context, "Cart Item ERROR NOT UPDATED", Toast.LENGTH_LONG).show();
                    else {
                        Toast.makeText(_context, "Cart Item quantity updated", Toast.LENGTH_LONG).show();
                    }

                }else
                    Toast.makeText(_context, "CART CACHE IS INVALID", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                Log.e(TAG, Objects.requireNonNull(t.getLocalizedMessage()));
            }
        });
    }
    public void deleteCartItem(int productId, CartItemsAdapter.CartItemViewHolder holder, RelativeLayout rlEmptyCart, RecyclerView rvCartItems){
        Call<AbstractResponse> call = api.deleteCartItem(productId);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<AbstractResponse> call, @NonNull Response<AbstractResponse> response) {
                if(!response.isSuccessful())return;

                var res = response.body();
                //making sure exactly 1 row is affected
                if(res != null && Integer.parseInt(res.response) == 1){
                    //deleted
                    //swipe away
                    holder.mcvItem.setVisibility(View.GONE);
                    holder.imgSwipeDelete.setVisibility(View.GONE);

                    CartItemsSessionManagement sessionManagement = new CartItemsSessionManagement(_context,false);

                    sessionManagement.deleteItem(productId);

                    if(sessionManagement.getSession().isEmpty()){
                        rlEmptyCart.setVisibility(View.VISIBLE);
                    }else{
                        rvCartItems.setVisibility(View.VISIBLE);
                    }
                }
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
