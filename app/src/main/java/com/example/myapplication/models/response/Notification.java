package com.example.myapplication.models.response;

public class Notification {
    private String id; // Unique ID for the notification
    private String title;
    private String body;  // Body of the notification
    private boolean isRead;  // Status to check if the notification has been read

    private String bookingId;

    // Constructor
    public Notification(String id, String title, String body, boolean isRead,String bookingId) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.isRead = isRead;
        this.bookingId=bookingId;

    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public boolean isRead() {
        return isRead;
    }



    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void setBookingId(String bookingId){this.bookingId=bookingId
    ;}

    public String getBookingId(){return bookingId;}

    public void setBody(String body) {
        this.body = body;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }



    // Method to toggle read status
    public void markAsRead() {
        this.isRead = true;
    }

    public void markAsUnread() {
        this.isRead = false;
    }
}
