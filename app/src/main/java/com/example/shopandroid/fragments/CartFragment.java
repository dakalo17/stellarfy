package com.example.shopandroid.fragments;

import static androidx.core.app.ActivityCompat.invalidateOptionsMenu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.shopandroid.R;
import com.example.shopandroid.adapters.CartItemsAdapter;
import com.example.shopandroid.services.implementations.CartItemService;
import com.example.shopandroid.services.session.CartItemsSessionManagement;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.badge.ExperimentalBadgeUtils;
import com.google.android.material.loadingindicator.LoadingIndicator;
import com.google.android.material.progressindicator.CircularProgressIndicator;


public class CartFragment extends Fragment implements MenuProvider {

    private RecyclerView rvCartItems;
    private CartItemService _service;
    private CircularProgressIndicator _cartLoadingIndicator;
    private static final int RECYCLER_VIEW_COLUMN_COUNT = 1;
    private View thisView;


    private RelativeLayout rlEmptyCart;
    private MenuItem _iCartWithBadge;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_cart, container, false);

        requireActivity().addMenuProvider(this,getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        init(view);
        events(view);

        thisView = view;

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void init(View view) {
        _service = new CartItemService(getContext());
        rvCartItems = view.findViewById(R.id.rvCartItems);
        _cartLoadingIndicator = view.findViewById(R.id.cartLoadingIndicator);



        rlEmptyCart = view.findViewById(R.id.rlEmptyCart);

    }


    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        //menuInflater.inflate(R.menu.mn_top_navigation,menu);
        _iCartWithBadge = menu.findItem(R.id.iCartWithBadge);
        start(thisView);
        //requireActivity().invalidateOptionsMenu();
        CartItemsSessionManagement cartSess = new CartItemsSessionManagement(requireContext(),false);
        var cart = cartSess.isValidSessionReturn();

        //MenuItem cartBadgeItem = menu.findItem(R.id.iCartWithBadge);

        if(cart.second) {
            int sum =0;
            for(var item :cart.first){
                sum+=item.quantity;
            }

              updateCartBadge(null/*cartBadgeItem*/,sum);

        }

    }

    @Override
    public void onPrepareMenu(@NonNull Menu menu) {
        MenuProvider.super.onPrepareMenu(menu);


    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        return id == R.id.iCartWithBadge;
    }




    @OptIn(markerClass = ExperimentalBadgeUtils.class)
    public void updateCartBadge(@Nullable MenuItem cartItem, int count){



        BadgeDrawable badgeDrawable = BadgeDrawable.create(requireContext());
        View actionView = cartItem != null ? cartItem.getActionView() : null;



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

            //ImageView cartIcon = actionView.findViewById(R.id.imgCartIcon);
            //   BadgeUtils.attachBadgeDrawable(badgeDrawable, cartIcon);
        } else {
            //  Attach the badge to the MenuItem icon
            BadgeUtils.detachBadgeDrawable(badgeDrawable,requireActivity().findViewById(R.id.iCartWithBadge));
            BadgeUtils.attachBadgeDrawable(badgeDrawable,
                    requireActivity().findViewById(R.id.iCartWithBadge), null);
        }


    }



    private void start(View view) {
        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(
                        requireActivity().
                        getApplicationContext(),
                        RECYCLER_VIEW_COLUMN_COUNT);

        // pull cart items

        _service.getCartItemsMain(rvCartItems,rlEmptyCart,_cartLoadingIndicator);

        var cartItemSession =
                new CartItemsSessionManagement(requireActivity().getApplicationContext(),false);
        var cart = cartItemSession.isValidSessionReturn();

//        if(!cart.second)return;
//
//        if(cart.first.isEmpty()){
//            rlEmptyCart.setVisibility(View.GONE);
//            rvCartItems.setVisibility(View.GONE);
//            return;
//        }
//        //_cartLoadingIndicator.setVisibility(View.GONE);


        CartItemsAdapter cartItemsAdapter =
                new CartItemsAdapter(cart.first,requireContext(),
                        rlEmptyCart, rvCartItems,requireActivity(),_iCartWithBadge);

        cartItemsAdapter.updateData(cartItemSession.getSession());

        rvCartItems.setLayoutManager(gridLayoutManager);
        rvCartItems.setAdapter(cartItemsAdapter);
    }

    private void events(View view) {
        
        
    }
}