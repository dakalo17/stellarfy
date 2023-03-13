package com.example.shopandroid.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shopandroid.R;


public class CartFragment extends Fragment {


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
    }

    private void start(View view) {
        
    }

    private void events(View view) {
        
        
    }
}