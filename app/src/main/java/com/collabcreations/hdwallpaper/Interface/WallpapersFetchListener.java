package com.collabcreations.hdwallpaper.Interface;

import com.collabcreations.hdwallpaper.Modal.Wallpaper;

import java.util.List;

public interface WallpapersFetchListener {
    void onWallpaperResult(List<Wallpaper> wallpapers);
    void onNewWallpaperFound(Wallpaper wallpaper);
}
