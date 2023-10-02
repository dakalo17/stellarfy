package com.example.shopandroid.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shopandroid.R;
import com.example.shopandroid.adapters.CartItemsAdapter;
import com.example.shopandroid.models.JSONObjects.CartItem;
import com.example.shopandroid.models.JSONObjects.Product;
import com.example.shopandroid.services.endpoints.ICartItemEndpoint;
import com.example.shopandroid.services.implementations.CartItemService;
import com.example.shopandroid.services.session.CartItemsSessionManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CartFragment extends Fragment {

    private RecyclerView rvCartItems;
    private CartItemService _service;
    private static final int RECYCLER_VIEW_COLUMN_COUNT = 1;

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
        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(
                        requireActivity().
                        getApplicationContext(),
                        RECYCLER_VIEW_COLUMN_COUNT);



        var cartItemSession = new CartItemsSessionManagement(requireActivity().getApplicationContext(),false);
        var isValidSession = cartItemSession.isValidSessionReturn();

        if(!isValidSession.second)return;

        CartItemsAdapter cartItemsAdapter =
                new CartItemsAdapter((ArrayList<Product>) isValidSession.first,requireContext());

        rvCartItems.setLayoutManager(gridLayoutManager);
        rvCartItems.setAdapter(cartItemsAdapter);

    }

    private void events(View view) {
        
        
    }
}