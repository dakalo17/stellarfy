package com.example.shopandroid.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shopandroid.R;
import com.example.shopandroid.services.implementations.CartItemService;


public class CartFragment extends Fragment {


    private RecyclerView rvCartProducts;
    private CartItemService _services;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_cart, container, false);

        populateCart(view);
        init(view);
        events(view);
        start(view);
        
        return view;
    }

    private void populateCart(View view){
        _services = new CartItemService(this);

        rvCartProducts = view.findViewById(R.id.rvCartProducts);

        _services.getCartItems(rvCartProducts,0);
    }
    private void init(View view) {

    }

    private void start(View view) {
        
    }

    private void events(View view) {
        
        
    }
}