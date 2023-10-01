package com.example.shopandroid.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopandroid.R;
import com.example.shopandroid.models.JSONObjects.Product;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.CartItemViewHolder> {

    private final ArrayList<Product> _products ;
    private final Context _context;

    public CartItemsAdapter(ArrayList<Product> products, Context context) {

        _products = products;

        _context = context;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_cart,parent,false);

        return new CartItemsAdapter.CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {


        holder.tvProductTitleCartItem.setText(_products.get(position).name);
        holder.tvProductPriceCartItem.setText("R "+_products.get(position).price);


        final int finalPosition = position;
        Picasso.get().load(_products.get(position).imageLink).
                error(R.drawable.ic_catalogbg).

                fetch( new Callback() {
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
                                        Log.e("Products catalog",e.getLocalizedMessage());
                                        Toast.makeText(_context, "Error ->"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("Products catalog",e.getLocalizedMessage());
                        Toast.makeText(_context, "Error ->"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
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

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProductCartItem = itemView.findViewById(R.id.imgProductCartItem);
            tvProductTitleCartItem = itemView.findViewById(R.id.tvProductTitleCartItem);
            tvProductPriceCartItem = itemView.findViewById(R.id.tvProductPriceCartItem);
            tvQuantityCartItemContainer = itemView.findViewById(R.id.tvQuantityCartItemContainer);
            tvQuantityCartItem = itemView.findViewById(R.id.tvQuantityCartItem);
        }
    }
}
