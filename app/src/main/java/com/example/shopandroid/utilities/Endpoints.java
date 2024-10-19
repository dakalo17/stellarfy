package com.example.shopandroid.utilities;

public class Endpoints {


    public static final String LOGIN_URI="api/User/Login";
    public static final String SIGNUP_URI="api/User/SignUp";

    public static final String REFRESH_TOKEN_URI="api/User/RefreshToken";
    private static final String PRODUCT_CONTROLLER ="api/Product";
    public static final String GET_PRODUCTS = PRODUCT_CONTROLLER+"/GetProducts";
    public static final String GET_PRODUCT_NAME = PRODUCT_CONTROLLER+"/GetProduct/{productName}";
    public static final String GET_PRODUCT_ID = PRODUCT_CONTROLLER+"GetProduct/{Id}";
    public static final String POST_PRODUCT = PRODUCT_CONTROLLER+"PostProduct";


    public static final String CART_PRODUCT_CONTROLLER="api/CartItem";
    public static final String POST_CART_PRODUCT = CART_PRODUCT_CONTROLLER+"/PostCartItem";
    public static final String GET_CART_PRODUCTS = CART_PRODUCT_CONTROLLER+"/GetCartItems";
    public static final String DELETE_CART_PRODUCT = CART_PRODUCT_CONTROLLER + "/DeleteCartItem/{productId}";


    public static final String BASE_URL="https://stable-frankly-marmoset.ngrok-free.app";
    public static final String LOCAL_URL="https://6c261gw7-7159.inc1.devtunnels.ms";

}
