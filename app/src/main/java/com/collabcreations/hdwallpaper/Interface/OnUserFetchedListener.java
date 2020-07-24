package com.collabcreations.hdwallpaper.Interface;

import com.collabcreations.hdwallpaper.Modal.User;

public interface OnUserFetchedListener {
    void onUserFetchComplete(boolean isSuccessful, User user, int responseCode);
}
