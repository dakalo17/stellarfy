package com.example.shopandroid.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopandroid.R;
import com.example.shopandroid.activities.BottomNavigationActivity;
import com.example.shopandroid.models.JSONObjects.CartItem;
import com.example.shopandroid.models.JSONObjects.Product;
import com.example.shopandroid.services.implementations.CartItemService;
import com.example.shopandroid.services.session.CartItemsSessionManagement;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.badge.ExperimentalBadgeUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.Objects;

import static com.example.shopandroid.utilities.Misc.SINGLE_PRODUCT_KEY;

public class SingleProductFragment extends Fragment implements MenuProvider {


    private Button btnAddToCartView;
    private ImageView imgProductView;
    private TextView tvTitleProductView;
    private TextView tvProductPriceView;
    private TextView tvDescriptionProductView;
    private CartItemService _cartItemService;

    private TextView tvCartItemsCount;



    @Override
    public void onStart() {
        super.onStart();

        //requireActivity().addMenuProvider((BottomNavigationActivity)requireActivity(), getViewLifecycleOwner());
    }

    @Override
    public void onStop(){
        super.onStop();
   //     requireActivity().removeMenuProvider((BottomNavigationActivity)requireActivity());
    }

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


    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {

        menuInflater.inflate(R.menu.mn_top_navigation,menu);


        CartItemsSessionManagement cartSess = new CartItemsSessionManagement(requireContext(),false);
        var cart = cartSess.isValidSessionReturn();

        MenuItem cartBadgeItem = menu.findItem(R.id.iCartWithBadge);

        if(cart.second) {
            int sum =0;
            for(var item :cart.first){
                sum+=item.quantity;
            }

         //   updateCartBadge(cartBadgeItem,sum);

        }

    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        return id == R.id.iCartWithBadge;
    }


    @OptIn(markerClass = ExperimentalBadgeUtils.class)
    public void updateCartBadge(MenuItem cartItem,int count){



        BadgeDrawable badgeDrawable = BadgeDrawable.create(requireContext());
        View actionView = cartItem.getActionView();



        if(!(count > 0)) {
            if(actionView != null) {
                ImageView cartIcon = actionView.findViewById(R.id.imgCartIcon);
                BadgeUtils.detachBadgeDrawable(badgeDrawable,cartIcon);
            }

            return;
        }



        // Create a BadgeDrawable instance

        badgeDrawable.setNumber(count); // Set the cart count
        badgeDrawable.setVisible(true); // Ensure the badge is visible
        badgeDrawable.setHorizontalOffset(55); // Adjust horizontal offset as needed
        badgeDrawable.setVerticalOffset(17);
        badgeDrawable.setBadgeGravity(BadgeDrawable.TOP_END);


        if (actionView != null) {
            // Attach the badge to the ImageView in the action layout

            ImageView cartIcon = actionView.findViewById(R.id.imgCartIcon);

            BadgeUtils.detachBadgeDrawable(badgeDrawable,cartIcon);
            BadgeUtils.attachBadgeDrawable(badgeDrawable, cartIcon);
        } else {
            //  Attach the badge to the MenuItem icon
            //BadgeUtils.attachBadgeDrawable(badgeDrawable, findViewById(R.id.iCartWithBadge), null);
        }


    }
    private void init(View view) {
        btnAddToCartView = view.findViewById(R.id.btnAddToCartView);
        imgProductView = view.findViewById(R.id.imgProductView);
        tvTitleProductView = view.findViewById(R.id.tvTitleProductView);
        tvProductPriceView = view.findViewById(R.id.tvProductPriceView);
        tvDescriptionProductView = view.findViewById(R.id.tvDescriptionProductView);

        tvCartItemsCount = view.findViewById(R.id.tvCartItemsCount);
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



    private void events(View view) {
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

            //lastly update counter
            BottomNavigationActivity activity = (BottomNavigationActivity) getActivity();


            var countItems =countCartItems();
//            if(countItems > 0 ){
//                tvCartItemsCount.setVisibility(View.VISIBLE);
//                if(countItems > 99)
//                    tvCartItemsCount.setText(MessageFormat.format("{0}+", countItems));
//
//            }else{
//                tvCartItemsCount.setVisibility(View.GONE);
//            }

//            if(activity !=null)
//                activity.updateBadge(tvCartItemsCount,countItems);
        });
    }

    private int countCartItems() {
        CartItemsSessionManagement cartItemsSessionManagement =
                new CartItemsSessionManagement(requireActivity().getApplicationContext(),false);

        var items = cartItemsSessionManagement.isValidSessionReturn();
        if(items.second){
            return items.first.size();
        }
        return 0;
    }

    private Product getProduct() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return null;
        }

        Serializable getProduct = bundle.getSerializable(SINGLE_PRODUCT_KEY);

        return getProduct instanceof Product ? (Product) getProduct : null;

    }


}