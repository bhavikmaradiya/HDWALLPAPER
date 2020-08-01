package com.collabcreations.hdwallpaper.Tasks;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.collabcreations.hdwallpaper.Interface.WallpapersFetchListener;
import com.collabcreations.hdwallpaper.Modal.Common;
import com.collabcreations.hdwallpaper.Modal.Tag;
import com.collabcreations.hdwallpaper.Modal.Wallpaper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GetWallpapersByTagTask extends AsyncTask<Void, Void, Void> {
    private WallpapersFetchListener listener;
    private Tag myTag;
    private DatabaseReference wallpaperRef;
    private List<Wallpaper> wallpapers;

    public GetWallpapersByTagTask(Tag tag, WallpapersFetchListener listener) {
        this.listener = listener;
        this.myTag = tag;
        wallpapers = new ArrayList<>();
        wallpaperRef = FirebaseDatabase.getInstance().getReference(Common.WALLPAPER_REFERENCE);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        wallpaperRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Wallpaper wallpaper = dataSnapshot.getValue(Wallpaper.class);
                    if (wallpaper.getTags() != null) {
                        for (Tag tag : wallpaper.getTags()) {
                            if (myTag.getTagId().equals(tag.getTagId())) {
                                if (!wallpapers.contains(wallpaper)) {
                                    wallpapers.add(wallpaper);
                                    if (listener != null) listener.onNewWallpaperFound(wallpaper);
                                    break;
                                }
                            }
                        }
                    }
                }
                if (listener != null) listener.onWallpaperResult(wallpapers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return null;
    }
}
