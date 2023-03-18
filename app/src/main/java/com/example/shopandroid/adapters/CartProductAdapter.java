package com.example.shopandroid.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopandroid.R;
import com.example.shopandroid.models.JSONObjects.CartItem;
import com.example.shopandroid.models.JSONObjects.CartProduct;
import com.example.shopandroid.models.JSONObjects.Product;
import com.example.shopandroid.services.implementations.CartItemService;
import com.example.shopandroid.services.session.UserSessionManagement;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.CartProductViewHolder> {

    private final ArrayList<CartProduct> _products ;
    private final Fragment _fragment;
    private final CartItemService _carItemService;
    private final UserSessionManagement _userSessionManagement;

    public CartProductAdapter(ArrayList<CartProduct> products, Fragment fragment) {

        _products = products;

        _fragment = fragment;
        _carItemService = new CartItemService(fragment);
        _userSessionManagement = new UserSessionManagement(fragment.requireContext(),false);
    }

    @NonNull
    @Override
    public CartProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_cart_product_item,parent,false);

        return new CartProductAdapter.CartProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartProductViewHolder holder, int position) {

        if(_products == null) return;

        holder.tvProductTitleCartItem.setText(_products.get(position).name);
        holder.tvProductPriceCartItem.setText("R "+_products.get(position).price);
        holder.tvQuantityCartItemContainer.setHint(_products.get(position).quantity+"");



        holder.btnQuantityCartItem.setOnClickListener(e->{
            //Todo update quantity

            CartProduct cartProduct = _products.get(position);
            CartItem cartItem = new CartItem();

            cartItem.Fk_Order_Id = _userSessionManagement.getSession().cartId;

            cartItem.Id = cartProduct.cart_product_id;
            cartItem.Price = cartProduct.price;
            cartItem.Quantity = Integer.parseInt(holder.tvQuantityCartItem.getText().toString());;

            _carItemService.putCartItem(cartItem,0,holder.tvQuantityCartItemContainer);


        });
        final int finalPosition = position;
        Picasso.get().load(_products.get(position).image_link).
                error(R.drawable.ic_catalogbg).

                fetch( new Callback() {
                    @Override
                    public void onSuccess() {


                        Picasso.get().load(_products.get(finalPosition).image_link).
                                error(R.drawable.ic_catalogbg).
                                into(holder.imgProductCartItem, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Log.e("Products catalog",e.getLocalizedMessage());
                                        Toast.makeText(_fragment.requireContext(), "Error ->"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("Products catalog",e.getLocalizedMessage());
                        Toast.makeText(_fragment.requireContext(), "Error ->"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public int getItemCount() {
        if(_products == null)
            return 1;
        return _products.size();
    }


    public static class CartProductViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgProductCartItem;
        public TextView tvProductTitleCartItem;
        public TextView tvProductPriceCartItem;

        public TextInputLayout tvQuantityCartItemContainer;
        public TextInputEditText tvQuantityCartItem;
        public Button btnQuantityCartItem;

        public CartProductViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProductCartItem = itemView.findViewById(R.id.imgProductCartItem);
            tvProductTitleCartItem = itemView.findViewById(R.id.tvProductTitleCartItem);
            tvProductPriceCartItem = itemView.findViewById(R.id.tvProductPriceCartItem);
            tvQuantityCartItemContainer = itemView.findViewById(R.id.tvQuantityCartItemContainer);
            tvQuantityCartItem = itemView.findViewById(R.id.tvQuantityCartItem);
            btnQuantityCartItem = itemView.findViewById(R.id.btnQuantityCartItem);
        }
    }
}
