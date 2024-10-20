package com.example.shopandroid.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kotlin.random.Random;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.shopandroid.R;
import com.example.shopandroid.activities.BottomNavigationActivity;
import com.example.shopandroid.adapters.CatalogProductAdapter;
import com.example.shopandroid.models.JSONObjects.Product;
import com.example.shopandroid.services.implementations.ProductService;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.progressindicator.CircularProgressIndicator;

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
    private CatalogProductAdapter cataloproductgProductAdapter;

    private ArrayList<Product> List;
    private MaterialToolbar topAppBar;

    private CircularProgressIndicator _catalogLoadingIndicator;
    private static final int RECYCLER_VIEW_COLUMN_COUNT = 3;
    private ProductService productService;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_catalog, container, false);
//
//        if (getActivity() instanceof BottomNavigationActivity) {
//            BottomNavigationActivity activity = (BottomNavigationActivity) getActivity();
//            MenuItem cartItem = activity.getCartItem(); // Use the method to get the MenuItem
//
//            // Call the method to update the badge
//            if (cartItem != null) {
//                //activity.addCartBadge(cartItem, 44); // Pass the MenuItem and count
//            } else {
//                Log.e("FragmentA", "MenuItem for cart not found.");
//            }
//        }
        init(view);
        populateProducts(view);
        populateCartItems(view);
        events(view);
        start(view);
        return view;
    }

    private void populateCartItems(View view) {
        topAppBar = view.findViewById(R.id.topAppBar);
    }

    private void populateProducts(View view) {

        productService = new ProductService(this);

        rvCatalogCatalog = view.findViewById(R.id.rvCatalogCatalog);
        productService.getProductsCatalog(rvCatalogCatalog,0,_catalogLoadingIndicator);
        
        
    }

    private void init(View view) {
        _catalogLoadingIndicator = view.findViewById(R.id.catalogLoadingIndicator);
        


    }
    private void events(View view) {

    }

    private void start(View view) {

    }




}