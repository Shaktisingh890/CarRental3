package com.example.myapplication.models.request;

public class
LoginRequest {
    private String email;
    private String password;

    private String deviceToken;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;

    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public String getDeviceToken(){return  deviceToken;}
    public void setDeviceToken(String deviceToken){ this.deviceToken=deviceToken;}


    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
