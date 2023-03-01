package com.example.shopandroid.services.implementations;

import androidx.appcompat.app.AppCompatActivity;

public class UserService<T> extends BaseService<T>{

    public UserService(AppCompatActivity activity, Class<T> classObj) {
        super(activity, classObj);
    }
}
