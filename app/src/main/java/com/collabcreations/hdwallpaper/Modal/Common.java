package com.collabcreations.hdwallpaper.Modal;

import android.content.Context;
import android.net.ConnectivityManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

public class Common {
    public static final String USER_REFERENCE = "users";
    public static final String WALLPAPER_REFERENCE = "wallpapers";
    public static final String ORIGINAL_IMAGE = "original";
    public static final String THUMB_IMAGE = "thumbnail";

    public static void saveUser(Context context, User user) {
        context.getSharedPreferences(USER_REFERENCE, Context.MODE_PRIVATE).edit().putString(USER_REFERENCE, user.toJson()).apply();
    }

    public static User getLoggedInUser() {
        return firebaseUserToUser();
    }

    public static User firebaseUserToUser() {
        User user = null;
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (isLoggedIn()) {
            user = new User();
            user.setEmailAddress(firebaseUser.getEmail());
            if (firebaseUser.getPhotoUrl() != null) {
                user.setProfileImage(firebaseUser.getPhotoUrl().toString());
            }
            user.setUserName(firebaseUser.getDisplayName());
            user.setuId(firebaseUser.getUid());
        }
        return user;
    }

    public static boolean isLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public static User getUser(Context context) {
        String user = context.getSharedPreferences(USER_REFERENCE, Context.MODE_PRIVATE).getString(USER_REFERENCE, null);
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

    public static String getFileExtension() {
        return null;
    }
}
