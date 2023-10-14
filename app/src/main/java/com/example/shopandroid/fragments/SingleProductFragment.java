package com.example.shopandroid.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.example.shopandroid.services.endpoints.ICartItemEndpoint;
import com.example.shopandroid.services.implementations.CartItemService;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.Objects;

import static com.example.shopandroid.utilities.Misc.SINGLE_PRODUCT_KEY;

public class SingleProductFragment extends Fragment {


    private Button btnAddToCartView;
    private ImageView imgProductView;
    private TextView tvTitleProductView;
    private TextView tvProductPriceView;
    private TextView tvDescriptionProductView;
    private CartItemService _cartItemService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_single_product, container, false);

        init(view);
        events();
        start(view);
        return view;
    }
    private void init(View view) {
        btnAddToCartView = view.findViewById(R.id.btnAddToCartView);
        imgProductView = view.findViewById(R.id.imgProductView);
        tvTitleProductView = view.findViewById(R.id.tvTitleProductView);
        tvProductPriceView = view.findViewById(R.id.tvProductPriceView);
        tvDescriptionProductView = view.findViewById(R.id.tvDescriptionProductView);

        _cartItemService = new CartItemService(getContext());
    }

    private void start(View view) {
        Bundle bundle = getArguments();
        if(bundle == null){
            return;
        }

        Serializable getProduct = bundle.getSerializable(SINGLE_PRODUCT_KEY);

        if(getProduct instanceof Product){
            Product product = (Product)getProduct;



            tvTitleProductView.setText(product.name);
            tvProductPriceView.setText("R ".concat(String.valueOf(product.price)));
            tvDescriptionProductView.setText(product.description);

            //fetch from cache first
            Picasso.get().
                    load(product.imageLink).
                    error(R.drawable.ic_catalogbg).
                   fetch(new Callback() {
                       @Override
                       public void onSuccess() {
                           Picasso.get().
                                   load(product.imageLink).
                                   error(R.drawable.ic_catalogbg).
                                   into(imgProductView, new Callback() {
                                       @Override
                                       public void onSuccess() {

                                       }

                                       @Override
                                       public void onError(Exception e) {
                                           Log.e("Products catalog", Objects.requireNonNull(e.getLocalizedMessage()));
                                           Toast.makeText(getContext(), "Error ->"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                                       }
                                   });
                       }

                       @Override
                       public void onError(Exception e) {

                       }
                   });
        }

    }



    private void events() {
        btnAddToCartView.setOnClickListener(e->{
            //Todo add to cart
            var product = getProduct();

            //if there's nothing return
            if(product == null) return;

            var obj = new CartItem();


            obj.Fk_Product_Id = product.id;
            obj.Fk_Order_Id =-1;
            obj.Price = product.price;
            obj.Quantity = 1;


            _cartItemService.postCartItem(obj,false);
        });
    }

    private Product getProduct() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return null;
        }

        Serializable getProduct = bundle.getSerializable(SINGLE_PRODUCT_KEY);
        Product product = null;
        if (getProduct instanceof Product)
            product = (Product) getProduct;

        return product;
    }
}