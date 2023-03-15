package com.example.shopandroid.services.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.shopandroid.models.jwt.RefreshToken;
import com.google.gson.Gson;

public class RefreshTokenSessionManagement implements ISessionManagement<RefreshToken>{
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String SHARED_PRE_NAME="refresh_token_session";
    private static final String SESSION_KEY="session_refresh_token_key";
    private static final String DEFAULT_JSON_STRING="";

    private final Gson _gson = new Gson();

    public RefreshTokenSessionManagement(Context context, boolean firstRun){
        sharedPreferences = context.getSharedPreferences(SHARED_PRE_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if(firstRun)
            removeSession();
    }
    @Override
    public void saveSession(RefreshToken refreshToken){
        String json = _gson.toJson(refreshToken);
        editor.putString(SESSION_KEY,json).commit();
    }
    @Override
    public boolean editSession(RefreshToken refreshToken){
        String json = _gson.toJson(refreshToken);
        return editor.putString(SESSION_KEY,json).commit();
    }
    @Override
    public RefreshToken getSession(){
        String json = sharedPreferences.getString(SESSION_KEY,DEFAULT_JSON_STRING);


        return _gson.fromJson(json,RefreshToken.class);
    }

    @Override
    public boolean isValidSession() {
        RefreshToken refreshToken = getSession();
        return refreshToken !=null && !refreshToken.isNull();
    }
    @Override
    public void removeSession() {
        editor.putString(SESSION_KEY,DEFAULT_JSON_STRING).commit();
    }

}
