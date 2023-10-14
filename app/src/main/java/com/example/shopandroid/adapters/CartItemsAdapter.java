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
import com.example.shopandroid.models.JSONObjects.Product;
import com.example.shopandroid.services.implementations.CartItemService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.CartItemViewHolder> {

    private final ArrayList<Product> _products ;
    private final Context _context;
    private CartItemService _service;

    public CartItemsAdapter(ArrayList<Product> products, Context context) {

        _products = products;

        _context = context;
        _service = new CartItemService(_context);
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_cart_product_item,parent,false);

        return new CartItemsAdapter.CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {

    try {
        holder.tvProductTitleCartItem.setText(_products.get(position).name);
        holder.tvProductPriceCartItem.setText("R ".concat(
                String.valueOf(_products.get(position).price*_products.get(position).quantity) ));
        holder.tvQuantityCartItem.setText(String.valueOf(_products.get(position).quantity));


        holder.btnUpdateQuantity.setOnClickListener(e->{
            //
            var cartItem = new CartItem();

            cartItem.Quantity =Integer.parseInt( Objects.requireNonNull(holder.tvQuantityCartItem.getText()).toString());
            cartItem.Price = _products.get(position).price;
            cartItem.Fk_Product_Id = _products.get(position).id;
            cartItem.Fk_Order_Id = -1;



            _service.postCartItem(cartItem,true);
        });

        final int finalPosition = position;
        Picasso.get().load(_products.get(position).imageLink).
                error(R.drawable.ic_catalogbg).
                fetch(new Callback() {
                    @Override
                    public void onSuccess() {


                        Picasso.get().load(_products.get(finalPosition).imageLink).
                                error(R.drawable.ic_catalogbg).
                                into(holder.imgProductCartItem, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Log.e("Products catalog", Objects.requireNonNull(e.getLocalizedMessage()));
                                        Toast.makeText(_context, "Error ->" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("Products catalog", e.getLocalizedMessage());
                        Toast.makeText(_context, "Error ->" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }catch(Exception e){
        e.getLocalizedMessage();
    }
    }

    @Override
    public int getItemCount() {
        return _products.size();
    }


    public static class CartItemViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgProductCartItem;
        public TextView tvProductTitleCartItem;
        public TextView tvProductPriceCartItem;

        public TextInputLayout tvQuantityCartItemContainer;
        public TextInputEditText tvQuantityCartItem;

        public Button btnUpdateQuantity;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProductCartItem = itemView.findViewById(R.id.imgProductCartItem);
            tvProductTitleCartItem = itemView.findViewById(R.id.tvProductTitleCartItem);
            tvProductPriceCartItem = itemView.findViewById(R.id.tvProductPriceCartItem);
            tvQuantityCartItemContainer = itemView.findViewById(R.id.tvQuantityCartItemContainer);
            tvQuantityCartItem = itemView.findViewById(R.id.tvQuantityCartItem);
            btnUpdateQuantity = itemView.findViewById(R.id.btnUpdateQuantity);
        }
    }
}
