package com.example.pixelstore_an.utils;

import android.util.Patterns;

public class Validator {

    public static boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
}
