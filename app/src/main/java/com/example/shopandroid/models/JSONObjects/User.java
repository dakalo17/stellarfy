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
    public User(){}
    public User(int id, String firstname, String lastname, String email, int role) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;

        this.role = role;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void copy(int id, String firstname, String lastname, String email,String password, int role){

        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.role = role;
    }
    public void copy(User user){

        if(user == null)return;
        this.id = user.id;
        this.firstname = user.firstname;
        this.lastname = user.lastname;
        this.email = user.email;
        this.password = user.password;
        this.role = user.role;
    }


    public boolean isNull(){
        return
//                (firstname == null || firstname.equals("")) &&
//                (lastname == null || lastname.equals("")) &&
                (email == null || email.equals("")) ||
                (jwtToken == null || Objects.equals(jwtToken, ""));
    }

}
