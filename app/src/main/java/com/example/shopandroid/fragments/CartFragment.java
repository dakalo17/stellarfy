package com.example.shopandroid.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shopandroid.R;
import com.example.shopandroid.services.endpoints.ICartItemEndpoint;
import com.example.shopandroid.services.implementations.CartItemService;


public class CartFragment extends Fragment {

    private RecyclerView rvCartItems;
    private CartItemService _service;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_cart, container, false);
        
        init(view);
        events(view);
        start(view);
        
        return view;
    }

    private void init(View view) {
        _service = new CartItemService(getContext());
        rvCartItems = view.findViewById(R.id.rvCartItems);

    }

    private void start(View view) {
        
    }

    private void events(View view) {
        
        
    }
}