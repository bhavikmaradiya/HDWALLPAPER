package com.collabcreations.hdwallpaper.Interface;

import com.collabcreations.hdwallpaper.Modal.User;

public interface UserFetchListener {
    void onUserFetchComplete(boolean isSuccessful, User user, int responseCode);
}
