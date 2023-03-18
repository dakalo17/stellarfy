package com.example.shopandroid.models.JSONObjects;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class User {
    @SerializedName("id")
    public int id;
    @SerializedName("firstname")
    public String firstname;
    @SerializedName("lastname")
    public String lastname;
    @SerializedName("email")
    public String email;
    @SerializedName("password")
    public String password;
    //@SerializedName("role")
    public int role;
    public String jwtToken;

    public int cartId;
    public User(){}
    public User(int id, String firstname, String lastname, String email, int role,int cartId) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;

        this.role = role;
        this.cartId = cartId;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void copy(int id, String firstname, String lastname, String email,String password, int role,int cartId){

        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.role = role;
        this.cartId = cartId;

    }
    public void copy(User user){

        if(user == null)return;
        this.id = user.id;
        this.firstname = user.firstname;
        this.lastname = user.lastname;
        this.email = user.email;
        this.password = user.password;
        this.role = user.role;
        this.cartId = user.cartId;

    }


    public boolean isNull(){
        return
//                (firstname == null || firstname.equals("")) &&
//                (lastname == null || lastname.equals("")) &&
                (email == null || email.equals("")) ||
                (jwtToken == null || Objects.equals(jwtToken, ""));
    }

}
