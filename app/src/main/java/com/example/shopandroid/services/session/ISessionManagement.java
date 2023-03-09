package com.example.shopandroid.services.session;


public interface ISessionManagement<T> {
    void saveSession(T obj);
    boolean editSession(T obj);
    T getSession();
    boolean isValidSession();
    void removeSession();
}
