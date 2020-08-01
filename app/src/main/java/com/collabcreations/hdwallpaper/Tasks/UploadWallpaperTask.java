package com.collabcreations.hdwallpaper.Tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.palette.graphics.Palette;

import com.collabcreations.hdwallpaper.Interface.WallpaperUploadListener;
import com.collabcreations.hdwallpaper.Modal.Color;
import com.collabcreations.hdwallpaper.Modal.Common;
import com.collabcreations.hdwallpaper.Modal.Wallpaper;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageSource;
import com.google.cloud.vision.v1.SafeSearchAnnotation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UploadWallpaperTask extends AsyncTask<Void, Void, Void> {
    private Context context;
    private Wallpaper wallpaper;
    Bitmap bitmap;
    private DatabaseReference wallpaperRef;
    private StorageReference originalImageRef, thumbImageRef;
    private Uri file;
    private UploadTask thumbTask, imageTask;
    private WallpaperUploadListener wallpaperUploadListener;

    public UploadWallpaperTask(Context context, Wallpaper wallpaper, Uri uri, WallpaperUploadListener wallpaperUploadListener) {
        this.context = context;
        this.file = uri;
        this.wallpaper = wallpaper;
        this.wallpaperRef = FirebaseDatabase.getInstance().getReference(Common.WALLPAPER_REFERENCE).child(this.wallpaper.getWallpaperId());
        this.originalImageRef = FirebaseStorage.getInstance().getReference(Common.WALLPAPER_REFERENCE).child(Common.ORIGINAL_IMAGE).child(this.wallpaper.getWallpaperId() + "." + Common.getFileExtension());
        this.thumbImageRef = FirebaseStorage.getInstance().getReference(Common.WALLPAPER_REFERENCE).child(Common.THUMB_IMAGE).child(this.wallpaper.getWallpaperId() + "." + Common.getFileExtension());
        this.wallpaperUploadListener = wallpaperUploadListener;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void detectSafeSearchGcs(String gcsPath) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ImageSource imgSource = ImageSource.newBuilder().setGcsImageUri(gcsPath).build();
        Image img = Image.newBuilder().setSource(imgSource).build();
        Feature feat = Feature.newBuilder().setType(Type.SAFE_SEARCH_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.getError().getMessage());
                    return;
                }

                // For full list of available annotations, see http://g.co/cloud/vision/docs
                SafeSearchAnnotation annotation = res.getSafeSearchAnnotation();
                System.out.format(
                        "adult: %s%nmedical: %s%nspoofed: %s%nviolence: %s%nracy: %s%n",
                        annotation.getAdult(),
                        annotation.getMedical(),
                        annotation.getSpoof(),
                        annotation.getViolence(),
                        annotation.getRacy());
            }
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    private void loadBitmap() {
        if (bitmap == null) {
            Picasso.get().load(file).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap resource, Picasso.LoadedFrom from) {
                    bitmap = resource;
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), file);
            if (bitmap == null) loadBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byte[] data = baos.toByteArray();
            thumbTask = thumbImageRef.putBytes(data);
            thumbTask.addOnCompleteListener(task -> {
                if (wallpaperUploadListener != null && !task.isSuccessful()) {
                    wallpaperUploadListener.onFail();
                    wallpaperUploadListener = null;
                    cancel(true);
                }

                if (task.isSuccessful()) {
                    thumbImageRef.getDownloadUrl().addOnSuccessListener(uri -> wallpaper.setThumbnail(uri.toString()));
                    imageTask = originalImageRef.putFile(file);
                    imageTask.addOnCompleteListener(imagetask -> {
                        if (wallpaperUploadListener != null && !imagetask.isSuccessful()) {
                            wallpaperUploadListener.onFail();
                            wallpaperUploadListener = null;
                            cancel(true);
                        }

                        if (imagetask.isSuccessful()) {
                            originalImageRef.getDownloadUrl().addOnSuccessListener(uri -> wallpaper.setOriginalImage(uri.toString()));
                            getColors();
                            wallpaper.setTimeInMillis(System.currentTimeMillis());
                            wallpaperRef.setValue(wallpaper).addOnCompleteListener(wallpaperTask -> {
                                if (wallpaperUploadListener != null) {
                                    wallpaperUploadListener.onUploadComplete(wallpaperTask.isSuccessful());
                                }
                            });
                        }
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void getColors() {
        if (bitmap == null) loadBitmap();
        Palette.from(bitmap)
                .generate(palette -> {
                    int defaultValue = 0x000000;
                    List<Color> colors = new ArrayList<>();
                    colors.add(new Color(String.valueOf(palette.getVibrantColor(defaultValue)), palette.getVibrantColor(defaultValue)));
                    colors.add(new Color(String.valueOf(palette.getDarkVibrantColor(defaultValue)), palette.getDarkVibrantColor(defaultValue)));
                    colors.add(new Color(String.valueOf(palette.getLightVibrantColor(defaultValue)), palette.getLightVibrantColor(defaultValue)));
                    colors.add(new Color(String.valueOf(palette.getMutedColor(defaultValue)), palette.getMutedColor(defaultValue)));
                    colors.add(new Color(String.valueOf(palette.getDarkMutedColor(defaultValue)), palette.getDarkMutedColor(defaultValue)));
                    colors.add(new Color(String.valueOf(palette.getLightMutedColor(defaultValue)), palette.getLightMutedColor(defaultValue)));
                    wallpaper.setColors(colors);

                    FirebaseDatabase.getInstance().getReference(Common.COLOR_REFERENCE)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (Color color : colors) {
                                        if (!snapshot.hasChild(color.getStringValue())) {
                                            FirebaseDatabase.getInstance().getReference(Common.COLOR_REFERENCE)
                                                    .child(color.getStringValue()).setValue(color);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                });

    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
