package com.example.shopandroid.services;

import com.auth0.android.jwt.JWT;
import com.example.shopandroid.models.JSONObjects.User;

public class DecodeToken {
    public static final String CLAIM_ID = "id";
    public static final String CLAIM_FN = "firstname";
    public static final String CLAIM_LN = "lastname";
    public static final String CLAIM_EMAIL = "email";
    public static final String CLAIM_ROLE = "role";
    public static final String CLAIM_CART_ID = "cartId";

    public static User DecodeUserClaims(String token){
        JWT jwt = new JWT(token);

//        if(jwt.isExpired(0)) {
//            return null;
//        }

        User user = null;
        try {
            int Id = jwt.getClaim(CLAIM_ID).asInt();
            String firstname = jwt.getClaim(CLAIM_FN).asString();
            String lastname = jwt.getClaim(CLAIM_LN).asString();
            String email = jwt.getClaim(CLAIM_EMAIL).asString();
            int role = jwt.getClaim(CLAIM_ROLE).asInt();
            int cartId = jwt.getClaim(CLAIM_CART_ID).asInt();

            user = new User(Id, firstname, lastname, email, role,cartId);

            user.jwtToken = token;
        }
        catch(NullPointerException ex){
            ex.printStackTrace();
        }

        return (user != null && user.isNull()) ? null : user;
    }
}
