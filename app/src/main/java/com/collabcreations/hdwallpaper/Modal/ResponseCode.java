package com.collabcreations.hdwallpaper.Modal;

public class ResponseCode {
    public static final int NO_USER_FOUND = 101;
    public static final int SUCCESS = 220;
    public static final int DATABASE_ERROR = 1005;

    public static String getMessage(int responseCode) {
        String message = "";
        switch (responseCode) {
            case NO_USER_FOUND:
                message = "No user found";
                break;
            case SUCCESS:
                message = "Success";
                break;
            case DATABASE_ERROR:
                message = "Error from server side";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + responseCode);
        }
        return message;
    }
}
