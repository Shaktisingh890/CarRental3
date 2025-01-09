package com.example.myapplication.models;

public class NotificationModel {
    private String title;
    private String message;
    private boolean isRead; // To track read/unread status

    public NotificationModel(String title, String message, boolean isRead) {
        this.title = title;
        this.message = message;
        this.isRead = isRead;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
