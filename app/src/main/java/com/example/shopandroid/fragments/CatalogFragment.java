package com.example.shopandroid.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kotlin.random.Random;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shopandroid.R;
import com.example.shopandroid.adapters.CatalogProductAdapter;
import com.example.shopandroid.models.JSONObjects.Product;
import com.example.shopandroid.services.implementations.ProductService;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CatalogFragment} factory method to
 * create an instance of this fragment.
 */
public class CatalogFragment extends Fragment {


    private RecyclerView rvCatalogCatalog;
    private GridLayoutManager gridLayoutManager;
    private CatalogProductAdapter catalogProductAdapter;

    private ArrayList<Product> productList;

    private static final int RECYCLER_VIEW_COLUMN_COUNT = 3;
    private ProductService productService;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_catalog, container, false);

        populateProducts(view);
        init(view);
        events(view);
        start(view);
        return view;
    }

    private void populateProducts(View view) {

        productService = new ProductService(this);

        rvCatalogCatalog = view.findViewById(R.id.rvCatalogCatalog);
        productService.getProducts(rvCatalogCatalog,0);
        
        
    }

    private void init(View view) {

        


    }
    private void events(View view) {

    }

    private void start(View view) {

    }




}