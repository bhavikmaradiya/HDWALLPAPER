package com.collabcreations.hdwallpaper.Modal;

import com.google.gson.Gson;

import java.io.Serializable;

public class User implements Serializable {
    private String uId;
    private String profileImage;
    private String emailAddress;
    private String userName;

    public User() {
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUsername() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String toJson() {
        return new Gson().toJson(this, User.class);
    }

    public User toUser() {
        return new Gson().fromJson(this.toJson(), User.class);
    }
}
