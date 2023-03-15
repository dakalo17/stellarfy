package com.example.shopandroid.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    private static final String REGEX_EMAIL= "^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";



    public static boolean validEmail(String email){
        Pattern pattern = Pattern.compile(REGEX_EMAIL);
        Matcher matcher = pattern.matcher(email);

        return matcher.find();
    }


}
