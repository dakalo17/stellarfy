package com.example.shopandroid.services.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.shopandroid.models.JSONObjects.User;
import com.google.gson.Gson;

public class UserSessionManagement {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    
    private static final String SHARED_PRE_NAME="user_session";
    private static final String SESSION_KEY="session_user_key";
    
    private static final String DEFAULT_ROLE="Invalid";
    private static final String DEFAULT_JSON_STRING="{}";
    private static final int DEFAULT_ID = -1;
    
    private final Gson _gson = new Gson();
    
    public UserSessionManagement(Context context, boolean firstRun){
        sharedPreferences = context.getSharedPreferences(SHARED_PRE_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if(firstRun)
            removeSession();
    }

    public void saveSession(User user){
        String json = _gson.toJson(user);
        editor.putString(SESSION_KEY,json).commit();
    }

    public boolean editSession(User user){
        String json = _gson.toJson(user);
        return editor.putString(SESSION_KEY,json).commit();
    }

    public User getSession(){
        String json = sharedPreferences.getString(SESSION_KEY,DEFAULT_JSON_STRING);
        User user = _gson.fromJson(json,User.class);
        return user;
    }
    public void removeSession() {
        editor.putString(SESSION_KEY,DEFAULT_JSON_STRING).commit();
    }

    public boolean isValidSession(){
        User user = getSession();
        return !user.isNull();
    }
}
