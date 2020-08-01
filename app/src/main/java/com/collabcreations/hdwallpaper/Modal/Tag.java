package com.collabcreations.hdwallpaper.Modal;

import java.util.Calendar;
import java.util.UUID;

public class Tag {
    private String tagId;
    private String tag;
    private int searchCount = 0;

    public Tag(String tag) {
        this.tag = tag;
        tagId = generateTagId();
    }

    public String getTagId() {
        if (tagId == null) {
            tagId = generateTagId();
        }
        return tagId;
    }

    public int getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(int searchCount) {
        this.searchCount = searchCount;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    private String generateTagId() {
        return "tag" + UUID.randomUUID().toString().substring(0, 4) + Common.firebaseUserToUser().getuId().substring(0, 2) + Calendar.getInstance().getTime().toString().substring(0, 3);
    }
}
