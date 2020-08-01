package com.collabcreations.hdwallpaper.Interface;

public interface WallpaperUploadListener {
    void onUploadComplete(boolean isSuccess);
    void onFail();

    void onAdultContentFound();
}
