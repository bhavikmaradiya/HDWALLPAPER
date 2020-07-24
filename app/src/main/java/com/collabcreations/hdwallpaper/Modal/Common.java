package com.collabcreations.hdwallpaper.Modal;

import android.content.Context;
import android.net.ConnectivityManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

public class Common {
    public static final String USER = "users";

    public static void saveUser(Context context, User user) {
        context.getSharedPreferences(USER, Context.MODE_PRIVATE).edit().putString(USER, user.toJson()).apply();
    }

    public static boolean isLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public static User getUser(Context context) {
        String user = context.getSharedPreferences(USER, Context.MODE_PRIVATE).getString(USER, null);
        if (user != null) {
            return new Gson().fromJson(user, User.class);
        } else {
            return null;
        }
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        } else {
            return false;
        }
    }
}
