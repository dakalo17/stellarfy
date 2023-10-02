package com.example.shopandroid.services.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

import com.example.shopandroid.models.JSONObjects.Product;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class CartItemsSessionManagement implements ISessionManagement<List<Product>> {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    private static final String SHARED_PRE_NAME="cart_session";
    private static final String SESSION_KEY="session_cart_key";
    private static final String DEFAULT_JSON_STRING="";
    private final Gson _gson = new Gson();

    public CartItemsSessionManagement(Context context,boolean firstRun){
        sharedPreferences = context.getSharedPreferences(SHARED_PRE_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if(firstRun)
            removeSession();
    }
    @Override
    public void saveSession(List<Product> obj) {
        String json = _gson.toJson(obj);
        editor.putString(SESSION_KEY,json).commit();
    }

    @Override
    public boolean editSession(List<Product> obj) {
        String json = _gson.toJson(obj);
        return editor.putString(SESSION_KEY,json).commit();
    }
    public boolean editSessionJSON(Product obj) {
        String json = sharedPreferences.getString(SESSION_KEY,DEFAULT_JSON_STRING);

        JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();

        //find and edit
        boolean insert = true;
        for(JsonElement element: jsonArray){
            JsonObject cartJSONObj =element.getAsJsonObject();
            if(cartJSONObj.get("id").getAsString().equals(String.valueOf(obj.id))){
                int currQuantity = cartJSONObj.get("quantity").getAsInt();
                cartJSONObj.addProperty("quantity",currQuantity+obj.quantity);
                insert = false;
                break;
            }
        }

        //if not found to update -> insert
        if(insert){
            JsonObject newProduct = new JsonObject();

            newProduct.addProperty("name",obj.name);
            newProduct.addProperty("quantity",obj.quantity);
            newProduct.addProperty("price",obj.price);
            newProduct.addProperty("description",obj.description);
            newProduct.addProperty("price",obj.price);
            newProduct.addProperty("specialPrice",obj.specialPrice);
            newProduct.addProperty("imageLink",obj.imageLink);

            jsonArray.add(newProduct);
        }

        return editor.putString(SESSION_KEY,jsonArray.toString()).commit();
    }
    @Override
    public List<Product> getSession() {
        String json = sharedPreferences.getString(SESSION_KEY,DEFAULT_JSON_STRING);

        return _gson.fromJson(json, new TypeToken<List<Product>>(){}.getType());
    }

    @Override
    public boolean isValidSession() {
        var session = getSession();
        return session != null;
    }

    public Pair<List<Product>,Boolean> isValidSessionReturn(){
        var session = getSession();


        return new Pair<>(session,session !=null);
    }

    @Override
    public void removeSession() {

    }
}
