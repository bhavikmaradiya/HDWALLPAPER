package com.collabcreations.hdwallpaper.Modal;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class Wallpaper implements Serializable {
    private String wallpaperId;
    private String originalImage;
    private String thumbnail;
    private String categoryId;
    private List<Color> colors;
    private String uploadedBy = Common.firebaseUserToUser().getuId();
    private long viewCount = 0;
    private long downloadCount = 0;
    private long timeInMillis;
    private List<Tag> tags;

    public Wallpaper() {
        wallpaperId = generateWallpaperId();
    }

    public String getWallpaperId() {
        if (wallpaperId == null) {
            wallpaperId = generateWallpaperId();
        }
        return wallpaperId;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    public void setWallpaperId(String wallpaperId) {
        this.wallpaperId = wallpaperId;
    }

    public String getOriginalImage() {
        return originalImage;
    }

    public void setOriginalImage(String originalImage) {
        this.originalImage = originalImage;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public List<Color> getColors() {
        return colors;
    }

    public void setColors(List<Color> colors) {
        this.colors = colors;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public long getViewCount() {
        return viewCount;
    }

    public long getDownloadCount() {
        return downloadCount;
    }

    private String generateWallpaperId() {
        return "wall" + UUID.randomUUID().toString().substring(0, 3) + Common.firebaseUserToUser().getuId().substring(0, 5)+ Calendar.getInstance().getTime().toString().substring(0, 3);
    }

    public String toJson() {
        return new Gson().toJson(this, Wallpaper.class);
    }

}
