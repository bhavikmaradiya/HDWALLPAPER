package com.collabcreations.hdwallpaper.Modal;

import java.io.Serializable;
import java.util.Calendar;
import java.util.UUID;

public class Category implements Serializable {
    private String categoryId;
    private String categoryName;
    private String categoryThumb;

    public Category() {
        this.categoryId = generateCategoryId();
    }


    public String getCategoryId() {
        if (categoryId == null) {
            categoryId = generateCategoryId();
        }
        return categoryId;
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

    private String generateCategoryId() {
        return "cat" + UUID.randomUUID().toString().substring(0, 2) + Calendar.getInstance().getTime().toString().substring(0, 3);
    }

    public void setCategoryThumb(String categoryThumb) {
        this.categoryThumb = categoryThumb;
    }
}
