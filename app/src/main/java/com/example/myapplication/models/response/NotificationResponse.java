package com.example.myapplication.models.response;



import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationResponse {
    private int statusCode;
    private List<Notification> data; // The 'data' array
    private String message;
    private boolean success;

    // Getters and setters
    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }

    public List<Notification> getData() { return data; }
    public void setData(List<Notification> data) { this.data = data; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
}
