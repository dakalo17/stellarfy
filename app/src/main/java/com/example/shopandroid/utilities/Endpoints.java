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



    public static final String BASE_URL="https://f5e9-197-90-236-105.ngrok.io";
    public static final String B1ASE_URL="https://localhost:7159";

}
