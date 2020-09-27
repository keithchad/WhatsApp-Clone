package com.chad.whatsappclone.Model;

public class Status {

    private String profileImage;
    private String statusImage;
    private String userName;
    private String timeAgo;

    public Status() {
    }

    public Status(String profileImage, String statusImage, String userName, String timeAgo) {
        this.profileImage = profileImage;
        this.statusImage = statusImage;
        this.userName = userName;
        this.timeAgo = timeAgo;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getStatusImage() {
        return statusImage;
    }

    public void setStatusImage(String statusImage) {
        this.statusImage = statusImage;
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
