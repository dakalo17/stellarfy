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
import com.example.shopandroid.models.JSONObjects.Product;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

import static com.example.shopandroid.utilities.Misc.SINGLE_PRODUCT_KEY;

public class SingleProductFragment extends Fragment {


    private Button btnAddToCartView;
    private ImageView imgProductView;
    private TextView tvTitleProductView;
    private TextView tvProductPriceView;
    private TextView tvDescriptionProductView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_single_product, container, false);

        init(view);
        events(view);
        start(view);
        return view;
    }
    private void init(View view) {
        btnAddToCartView = view.findViewById(R.id.btnAddToCartView);
        imgProductView = view.findViewById(R.id.imgProductView);
        tvTitleProductView = view.findViewById(R.id.tvTitleProductView);
        tvProductPriceView = view.findViewById(R.id.tvProductPriceView);
        tvDescriptionProductView = view.findViewById(R.id.tvDescriptionProductView);
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
            tvProductPriceView.setText("R "+product.price);
            tvDescriptionProductView.setText(product.description);

            Picasso.get().load(product.imageLink).
                    error(R.drawable.ic_catalogbg).
                    into(imgProductView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("Products catalog",e.getLocalizedMessage());
                            Toast.makeText(getContext(), "Error ->"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
        }

    }



    private void events(View view) {
        btnAddToCartView.setOnClickListener(e->{
            //Todo add to cart
        });
    }


}