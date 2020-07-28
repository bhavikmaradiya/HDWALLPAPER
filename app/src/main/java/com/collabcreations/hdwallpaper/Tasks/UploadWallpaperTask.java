package com.collabcreations.hdwallpaper.Tasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.collabcreations.hdwallpaper.Interface.OnWallpaperUploadCompleteListener;
import com.collabcreations.hdwallpaper.Modal.Common;
import com.collabcreations.hdwallpaper.Modal.Wallpaper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageSource;
import com.google.cloud.vision.v1.SafeSearchAnnotation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UploadWallpaperTask extends AsyncTask<Void, Void, Void> {
    private Context context;
    private Wallpaper wallpaper;
    private DatabaseReference wallpaperRef;
    private StorageReference originalImageRef, thumbImageRef;
    private Uri file;
    private OnWallpaperUploadCompleteListener onWallpaperUploadCompleteListener;

    public UploadWallpaperTask(Context context, Wallpaper wallpaper, Uri uri, OnWallpaperUploadCompleteListener onWallpaperUploadCompleteListener) {
        this.context = context;
        this.file = uri;
        this.wallpaper = wallpaper;
        this.wallpaper.setWallpaperId(UUID.randomUUID().toString());
        this.wallpaperRef = FirebaseDatabase.getInstance().getReference(Common.WALLPAPER_REFERENCE);
        this.originalImageRef = FirebaseStorage.getInstance().getReference(Common.WALLPAPER_REFERENCE).child(Common.ORIGINAL_IMAGE).child(this.wallpaper.getWallpaperId() + "." + Common.getFileExtension());
        this.thumbImageRef = FirebaseStorage.getInstance().getReference(Common.WALLPAPER_REFERENCE).child(Common.THUMB_IMAGE).child(this.wallpaper.getWallpaperId() + "." + Common.getFileExtension());
        this.onWallpaperUploadCompleteListener = onWallpaperUploadCompleteListener;
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

    @Override
    protected Void doInBackground(Void... voids) {
        originalImageRef.putFile(file).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (!task.isSuccessful()) {
                    if (onWallpaperUploadCompleteListener != null) {
                        onWallpaperUploadCompleteListener.onFail();
                    }
                } else {

                }
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
