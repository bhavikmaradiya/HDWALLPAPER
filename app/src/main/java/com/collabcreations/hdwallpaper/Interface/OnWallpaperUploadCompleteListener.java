package com.collabcreations.hdwallpaper.Interface;

public interface OnWallpaperUploadCompleteListener {
    void onUploadComplete(boolean isSuccess);

    void onFail();

    void onAdultContentFound();
}
