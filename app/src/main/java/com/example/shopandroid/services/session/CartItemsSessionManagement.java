package com.example.shopandroid.services.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.example.shopandroid.models.JSONObjects.CartItem;
import com.example.shopandroid.models.JSONObjects.Product;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Iterator;
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

    public int getQuantity (int productId){
        int qty=0;
        String json = sharedPreferences.getString(SESSION_KEY,DEFAULT_JSON_STRING);

        JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();
        for (JsonElement elem: jsonArray) {
            JsonObject jsonObject = elem.getAsJsonObject();
            if(jsonObject.get("id").getAsString().equals(String.valueOf(productId))){

                qty = jsonObject.get("quantity").getAsInt();
                break;
            }
        }
        return qty;
    }
    public boolean deleteCartItem(int productIndex){
        String json = sharedPreferences.getString(SESSION_KEY,DEFAULT_JSON_STRING);

        JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();
        jsonArray.remove(productIndex);

        return editor.putString(SESSION_KEY,jsonArray.toString()).commit();

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

                //if its the same amount just return the function
                if(currQuantity != obj.quantity)
                    cartJSONObj.addProperty("quantity",obj.quantity);

                insert = false;
                break;
            }
        }

        //if not found to update -> insert
        if(insert){
            var newProduct = getJsonObject(obj);

            jsonArray.add(newProduct);
        }

        return editor.putString(SESSION_KEY,jsonArray.toString()).commit();
    }

    public boolean updateProductQuantity(int productId,int quantity,boolean isIncrement) {
        String json = sharedPreferences.getString(SESSION_KEY,DEFAULT_JSON_STRING);

        JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();

        //find and edit

        for(JsonElement element: jsonArray){
            JsonObject cartJSONObj =element.getAsJsonObject();
            if(cartJSONObj.get("id").getAsString().equals(String.valueOf(productId))){
                int currQuantity = cartJSONObj.get("quantity").getAsInt();

                if(isIncrement)
                        cartJSONObj.addProperty("quantity", quantity+currQuantity);
                else if (currQuantity != quantity)
                    cartJSONObj.addProperty("quantity", quantity);

                break;
            }
        }


        return editor.putString(SESSION_KEY,jsonArray.toString()).commit();
    }


    @NonNull
    private static JsonObject getJsonObject(Product obj) {
        var newProduct = new JsonObject();

        newProduct.addProperty("id", obj.id);
        newProduct.addProperty("name", obj.name);
        newProduct.addProperty("quantity", obj.quantity);
        newProduct.addProperty("price", obj.price);
        newProduct.addProperty("description", obj.description);
        newProduct.addProperty("price", obj.price);
        newProduct.addProperty("specialPrice", obj.specialPrice);
        newProduct.addProperty("imageLink", obj.imageLink);
        return newProduct;
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
        editor.putString(SESSION_KEY,DEFAULT_JSON_STRING).commit();
    }

    public boolean deleteItem(int productId) {
        String json = sharedPreferences.getString(SESSION_KEY,DEFAULT_JSON_STRING);

        //all items from cart
        JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();

        //find and edit

        JsonObject cartJSONObj = null;
        for (JsonElement element : jsonArray) {
            cartJSONObj = element.getAsJsonObject();

            if (!cartJSONObj.get("id").getAsString().equals(String.valueOf(productId)))
                break;

        }
        if(cartJSONObj != null)
            jsonArray.remove(cartJSONObj);


        return editor.putString(SESSION_KEY,jsonArray.toString()).commit();
    }
}
