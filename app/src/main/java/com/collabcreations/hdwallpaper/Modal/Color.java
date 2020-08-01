package com.collabcreations.hdwallpaper.Modal;

public class Color {
    private String stringValue;
    private int intValue;

    public Color() {
    }

    public Color(String stringValue, int intValue) {
        this.stringValue = stringValue;
        this.intValue = intValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public String getStringValue() {
        return stringValue;
    }
}
