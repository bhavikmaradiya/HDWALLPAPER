package com.collabcreations.hdwallpaper.Tasks;

import android.os.AsyncTask;

import com.collabcreations.hdwallpaper.Interface.DeleteWallpaperListener;
import com.collabcreations.hdwallpaper.Modal.Common;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DeleteWallpaperTask extends AsyncTask<Void, Void, Void> {
    private DeleteWallpaperListener listener;
    private DatabaseReference wallpaperRef;

    public DeleteWallpaperTask(String wallpaperId, DeleteWallpaperListener listener) {
        this.listener = listener;
        this.wallpaperRef = FirebaseDatabase.getInstance().getReference(Common.WALLPAPER_REFERENCE).child(wallpaperId);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        wallpaperRef.removeValue().addOnCompleteListener(task -> {
            if (listener != null) listener.onDeleteComplete(task.isSuccessful());
        });
        return null;
    }
}
