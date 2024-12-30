package com.example.myapplication.models.response;

import com.google.gson.annotations.SerializedName;

public class UploadIdResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private Object data; // Replace `Object` with a specific model if you expect structured data.

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}