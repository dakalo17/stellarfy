package com.example.shopandroid.adapters;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopandroid.R;
import com.example.shopandroid.fragments.SingleProductFragment;
import com.example.shopandroid.models.JSONObjects.Product;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.shopandroid.utilities.Misc.SINGLE_PRODUCT_KEY;

public class CatalogProductAdapter extends RecyclerView.Adapter<CatalogProductAdapter.ProductViewHolder> {

    private Context _context;
    private ArrayList<Product> _productList;

    private SingleProductFragment _singleProductFragment;
    private FragmentManager _fragmentManager;
    public CatalogProductAdapter(ArrayList<Product> productList,Context context,FragmentManager fragmentManager) {
        _context = context;
        _productList = productList;
        _fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_product_item,parent,false);

        return new CatalogProductAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        holder.tvTitleProductSingleItem.setText( _productList.get(position).name);
        holder.tvProductPriceSingleItem.setText("R "+_productList.get(position).price);

        //caches the image in the main memory
        Picasso picasso = Picasso.get();
        picasso.setIndicatorsEnabled(false);
        final int finalPosition = position;
        picasso.load(_productList.get(position).imageLink).
                error(R.drawable.ic_catalogbg).

                fetch( new Callback() {
                    @Override
                    public void onSuccess() {

                        picasso.load(_productList.get(finalPosition).imageLink).
                                error(R.drawable.ic_catalogbg).
                                into(holder.imgProductSingleItem, new Callback() {
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


        holder.mvcProductSingleItem.setOnClickListener(e->{
            Bundle bundle = new Bundle();

            bundle.putSerializable(SINGLE_PRODUCT_KEY,_productList.get(position));

            _singleProductFragment = new SingleProductFragment();
            _singleProductFragment.setArguments(bundle);


            FragmentTransaction fragmentTransaction = _fragmentManager.beginTransaction();



            fragmentTransaction
                    .replace(R.id.flMain,_singleProductFragment)
                    .addToBackStack("")
                    .commit();

        });


    }

    @Override
    public int getItemCount() {
        return _productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgProductSingleItem;
        public TextView tvTitleProductSingleItem;
        public TextView tvProductPriceSingleItem;

        public MaterialCardView mvcProductSingleItem;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProductSingleItem = itemView.findViewById(R.id.imgProductSingleItem);
            tvTitleProductSingleItem = itemView.findViewById(R.id.tvTitleProductSingleItem);
            tvProductPriceSingleItem = itemView.findViewById(R.id.tvProductPriceSingleItem);
            mvcProductSingleItem = itemView.findViewById(R.id.mvcProductSingleItem);
        }
    }
}
