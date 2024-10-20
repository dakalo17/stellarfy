package com.example.shopandroid.services.implementations;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.shopandroid.adapters.CatalogProductAdapter;
import com.example.shopandroid.models.JSONObjects.Product;
import com.example.shopandroid.services.endpoints.IProductEndpoint;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductService extends BaseService<IProductEndpoint>{
    private static final int RECYCLER_VIEW_COLUMN_COUNT = 3;
    private static final String TAG = "Product Service";

    private final Fragment _fragment;
    public ProductService(Fragment fragment) {
        super(fragment.requireContext(),IProductEndpoint.class);
        _fragment = fragment;
    }

    public void getProduct(int id){
        Call<Product> call = api.getProduct(id);

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {

            }

            @Override
            public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                Log.e(TAG, Objects.requireNonNull(t.getLocalizedMessage()));
                Toast.makeText(_fragment.getContext(),"getProduct id = "+ t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getProduct(String productName){
        Call<Product> call = api.getProduct(productName);

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {

            }

            @Override
            public void onFailure(@NonNull Call<Product> call, Throwable t) {
                Log.e(TAG, Objects.requireNonNull(t.getLocalizedMessage()));
                Toast.makeText(_fragment.getContext(),"getProduct name = "+ t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getProductsCatalog(RecyclerView rvCatalogCatalog, int retries, CircularProgressIndicator catalogLoadingIndicator){

        if(retries >= 10)return;
        Call<ArrayList<Product>> call = api.getProduct();


        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {


                if (!response.isSuccessful()) return;
                /* TODO resolve crash */
                GridLayoutManager gridLayoutManager =
                        new GridLayoutManager(_fragment
                                .requireActivity()
                                .getApplicationContext(),
                                RECYCLER_VIEW_COLUMN_COUNT);

                ArrayList<Product> products = response.body();
                //logic for loading indicator

                //basically get their initial state but ensure they are not null
                var isRvCatalogGone = rvCatalogCatalog != null && rvCatalogCatalog.getVisibility() == View.GONE;
                var isCatalogLoadIndVisible = catalogLoadingIndicator != null && catalogLoadingIndicator.getVisibility() == View.VISIBLE;

                if(isRvCatalogGone && products != null && !products.isEmpty()){
                    rvCatalogCatalog.setVisibility(View.VISIBLE);
                    if(isCatalogLoadIndVisible){
                        catalogLoadingIndicator.setVisibility(View.GONE);
                    }
                }
//
//                if(isCatalogLoadIndVisible && products != null && products.isEmpty() &&
//                        (rvCatalogCatalog != null && rvCatalogCatalog.getVisibility() == View.GONE)){
//                    catalogLoadingIndicator.setVisibility(View.VISIBLE);
//                }

                CatalogProductAdapter catalogProductAdapter =
                        new CatalogProductAdapter(products, _fragment.getContext(), _fragment.getParentFragmentManager());
                rvCatalogCatalog.setLayoutManager(gridLayoutManager);
                rvCatalogCatalog.setAdapter(catalogProductAdapter);

            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Product>> call, @NonNull Throwable t) {
                Log.e(TAG, Objects.requireNonNull(t.getLocalizedMessage()));
                Toast.makeText(_fragment.getContext(), "getProducts = " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();


                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    getProducts(rvCatalogCatalog, retries + 1);
                }, 2000);
                ;
            }
        });
    }
    public void getProducts(RecyclerView rvCatalogCatalog,int retries){

        if(retries >= 10)return;
        Call<ArrayList<Product>> call = api.getProduct();


        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {


                if (!response.isSuccessful()) return;
                /* TODO resolve crash */
                GridLayoutManager gridLayoutManager =
                        new GridLayoutManager(_fragment
                                .requireActivity()
                                .getApplicationContext(),
                                RECYCLER_VIEW_COLUMN_COUNT);

                ArrayList<Product> products = response.body();
                //logic for loading indicator




                CatalogProductAdapter catalogProductAdapter =
                        new CatalogProductAdapter(products, _fragment.getContext(), _fragment.getParentFragmentManager());
                rvCatalogCatalog.setLayoutManager(gridLayoutManager);
                rvCatalogCatalog.setAdapter(catalogProductAdapter);

            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Product>> call, @NonNull Throwable t) {
                Log.e(TAG, Objects.requireNonNull(t.getLocalizedMessage()));
                Toast.makeText(_fragment.getContext(), "getProducts = " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();


                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    getProducts(rvCatalogCatalog, retries + 1);
                }, 2000);
                ;
            }
        });
    }

}
