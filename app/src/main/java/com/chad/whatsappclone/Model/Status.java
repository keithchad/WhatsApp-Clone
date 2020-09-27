package com.chad.whatsappclone.Model;

public class Status {

    private int profileImage;
    private String userName;
    private String timeAgo;

    public Status() {
    }

    public Status(int profileImage, String userName,  String timeAgo) {
        this.profileImage = profileImage;
        this.userName = userName;
        this.timeAgo = timeAgo;
    }

    public int getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(int profileImage) {
        this.profileImage = profileImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }
}
