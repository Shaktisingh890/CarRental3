package com.example.myapplication.models.response;



import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllDriversResponse {
    @SerializedName("statusCode")
    private int statusCode;

    @SerializedName("data")
    private List<Driver> data;

    @SerializedName("message")
    private String message;

    public int getStatusCode() {
        return statusCode;
    }

    public List<Driver> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return statusCode == 200;
    }
}

