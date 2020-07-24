package com.collabcreations.hdwallpaper.Modal;

import java.io.Serializable;

public class Category implements Serializable {
    private String categoryId;
    private String categoryName;
    private String categoryThumb;

    public Category() {
    }

    public Category(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryThumb() {
        return categoryThumb;
    }

    public void setCategoryThumb(String categoryThumb) {
        this.categoryThumb = categoryThumb;
    }
}
