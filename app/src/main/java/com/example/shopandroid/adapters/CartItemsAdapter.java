package com.example.shopandroid.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopandroid.R;
import com.example.shopandroid.activities.BottomNavigationActivity;
import com.example.shopandroid.models.JSONObjects.CartItem;
import com.example.shopandroid.models.JSONObjects.Product;
import com.example.shopandroid.services.implementations.CartItemService;
import com.example.shopandroid.services.session.CartItemsSessionManagement;
import com.google.android.material.behavior.SwipeDismissBehavior;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.CartItemViewHolder> {

    private final List<Product> _products ;
    private final Context _context;
    private final RelativeLayout _rlEmptyCart;
    private final RecyclerView _rvCartItems;
    private CartItemService _service;

    private FragmentActivity _fragmentActivity;

    public CartItemsAdapter(List<Product> products, Context context, RelativeLayout rlEmptyCart,
                            RecyclerView rvCartItems,FragmentActivity fragmentActivity) {

        _fragmentActivity = fragmentActivity;

        _products = products;

        _context = context;
        _rlEmptyCart = rlEmptyCart;
        _rvCartItems = rvCartItems;
        _service = new CartItemService(_context);

    }
    public void removeProductFromCart(int position){
        _products.remove(position);
        notifyItemRemoved(position);
    }
    public void updateData(List<Product> products) {
        _products.clear();
        _products.addAll(products);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_cart_product_item,parent,false);

        return new CartItemsAdapter.CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {

        //don't display if qty of product is 0
        if(_products.get(position).quantity == 0){

            holder.imgProductCartItem.setVisibility(View.GONE);
            holder.tvProductTitleCartItem.setVisibility(View.GONE);
            holder.tvProductPriceCartItem.setVisibility(View.GONE);
            holder.tvQuantityCartItemContainer.setVisibility(View.GONE);
            holder.tvQuantityCartItem.setVisibility(View.GONE);
            holder.btnUpdateQuantity.setVisibility(View.GONE);
            holder.clCart.setVisibility(View.GONE);
            holder.mcvItem.setVisibility(View.GONE);
            holder.imgSwipeDelete.setVisibility(View.GONE);


            return;
        }
        try {
            holder.tvProductTitleCartItem.setText(_products.get(position).name);
            holder.tvProductPriceCartItem.setText("R ".concat(
                    String.valueOf(_products.get(position).price*_products.get(position).quantity) ));
            holder.tvQuantityCartItem.setText(String.valueOf(_products.get(position).quantity));


            holder.btnUpdateQuantity.setOnClickListener(e->{
                _fragmentActivity.invalidateOptionsMenu();
                var cartItem = new CartItem();

                cartItem.Quantity =
                        Integer.parseInt( Objects.requireNonNull(holder.tvQuantityCartItem.getText()).toString());
                cartItem.Price = _products.get(position).price;
                cartItem.Fk_Product_Id = _products.get(position).id;
                cartItem.Fk_Order_Id = -1;



                //indicate that you are not inc or dec but replacing , eg. quantity = 8 not qu = 8 + 8
                _service.postCartItemDelete(cartItem,true,position,this);
                var cartSession = new CartItemsSessionManagement(_context,false);
                var obj = cartSession.getSession();
            });

            //holder.clCart;

            makeSwipeFunc(holder,_products.get(position));

            final int finalPosition = position;
            Picasso.get().load(_products.get(position).imageLink).
                    error(R.drawable.ic_catalogbg).
                    fetch(new Callback() {
                        @Override
                        public void onSuccess() {


                            Picasso.get().load(_products.get(finalPosition).imageLink).
                                    error(R.drawable.ic_catalogbg).
                                    into(holder.imgProductCartItem, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            Log.e("Products catalog", Objects.requireNonNull(e.getLocalizedMessage()));
                                            Toast.makeText(_context, "Error ->" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    });
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("Products catalog", e.getLocalizedMessage());
                            Toast.makeText(_context, "Error ->" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
        }catch(Exception e){
            e.getLocalizedMessage();
        }
    }

    public void deleteItemFromCart(int position) {
        CartItemsSessionManagement sessionManagement = new CartItemsSessionManagement(_context,false);

        var cart = sessionManagement.isValidSessionReturn();
        if(cart.second){
            if(sessionManagement.deleteCartItem(position))
            {
                removeProductFromCart(position);
                //success ?
//                ArrayList<Product> prs = new ArrayList<>(2);
//                prs.add(cart.first.get(1));
//                updateData(prs);
            }

        }

    }

    private void makeSwipeFunc(@NonNull CartItemViewHolder holder,Product product) {
        SwipeDismissBehavior<MaterialCardView> swipeDismissBehavior = new SwipeDismissBehavior<>();
        swipeDismissBehavior.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_END_TO_START);

        swipeDismissBehavior.setListener(new SwipeDismissBehavior.OnDismissListener() {
            @Override
            public void onDismiss(View view) {
                holder.mcvItem.setVisibility(View.GONE);
                holder.imgSwipeDelete.setVisibility(View.GONE);
                //delete the item
                Toast.makeText(_context, "Deleted", Toast.LENGTH_SHORT).show();

                product.quantity = 0;
                _service.deleteCartItem(product.id,holder,_rlEmptyCart,_rvCartItems);


            }

            @Override
            public void onDragStateChanged(int state) {
                //holder.mcvItem.setVisibility(View.VISIBLE);
                if(state == SwipeDismissBehavior.STATE_DRAGGING) {
                    holder.imgSwipeDelete.setVisibility(View.VISIBLE);

                } else if (state == SwipeDismissBehavior.STATE_IDLE) {
                    holder.imgSwipeDelete.setVisibility(View.GONE);

                } else if(state == SwipeDismissBehavior.STATE_SETTLING) {
                    
                    holder.mcvItem.setVisibility(View.VISIBLE);

                }
            }


        });
        CoordinatorLayout.LayoutParams params =
                (CoordinatorLayout.LayoutParams) holder.mcvItem.getLayoutParams();
        params.setBehavior(swipeDismissBehavior);
    }

    @Override
    public int getItemCount() {
        return _products.size();
    }


    public static class CartItemViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgProductCartItem;
        public TextView tvProductTitleCartItem;
        public TextView tvProductPriceCartItem;

        public TextInputLayout tvQuantityCartItemContainer;
        public TextInputEditText tvQuantityCartItem;

        public Button btnUpdateQuantity;

        public CoordinatorLayout clCart;
        public MaterialCardView mcvItem;
        public ImageView imgSwipeDelete;

        public BottomNavigationActivity bottomNavigationActivity;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProductCartItem = itemView.findViewById(R.id.imgProductCartItem);
            tvProductTitleCartItem = itemView.findViewById(R.id.tvProductTitleCartItem);
            tvProductPriceCartItem = itemView.findViewById(R.id.tvProductPriceCartItem);
            tvQuantityCartItemContainer = itemView.findViewById(R.id.tvQuantityCartItemContainer);
            tvQuantityCartItem = itemView.findViewById(R.id.tvQuantityCartItem);
            btnUpdateQuantity = itemView.findViewById(R.id.btnUpdateQuantity);
            clCart = itemView.findViewById(R.id.clCart);
            mcvItem = itemView.findViewById(R.id.mcvItem);
            imgSwipeDelete = itemView.findViewById(R.id.imgSwipeDelete);

           // bottomNavigationActivity = itemView.findViewById(R.id.botNavSingleProduct);
        }
    }
}
