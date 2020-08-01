package com.collabcreations.hdwallpaper.Tasks;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.collabcreations.hdwallpaper.Interface.WallpapersFetchListener;
import com.collabcreations.hdwallpaper.Modal.Category;
import com.collabcreations.hdwallpaper.Modal.Common;
import com.collabcreations.hdwallpaper.Modal.Wallpaper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GetWallpapersByCategoryTask extends AsyncTask<Void, Void, Void> {
    private WallpapersFetchListener listener;
    private List<Wallpaper> wallpapers;
    private Category category;

    public GetWallpapersByCategoryTask(Category category, WallpapersFetchListener wallpapersFetchListener) {
        listener = wallpapersFetchListener;
        wallpapers = new ArrayList<>();
        this.category = category;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        FirebaseDatabase.getInstance().getReference(Common.WALLPAPER_REFERENCE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Wallpaper wallpaper = dataSnapshot.getValue(Wallpaper.class);
                    if (wallpaper.getCategoryId().equals(category.getCategoryId())) {
                        wallpapers.add(wallpaper);
                        if (listener != null) listener.onNewWallpaperFound(wallpaper);
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
