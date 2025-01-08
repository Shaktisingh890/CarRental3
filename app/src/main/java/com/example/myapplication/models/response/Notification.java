package com.example.myapplication.models.response;

public class Notification {
    private String title;
    private String subtitle;
    private String time;

    // Constructor
    public Notification(String title, String subtitle, String time) {
        this.title = title;
        this.subtitle = subtitle;
        this.time = time;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getTime() {
        return time;
    }
}
