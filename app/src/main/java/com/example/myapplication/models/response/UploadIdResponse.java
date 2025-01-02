package com.example.myapplication.models.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

import java.util.List;

public class UploadIdResponse {
    private int statusCode;
    private Data data;
    private String message;
    private boolean success;

    // Getters and setters
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    // Inner class for Data
    public static class Data {
        private String idType;
        private String idNumber;
        private List<String> idImages;

        // Getters and setters
        public String getIdType() {
            return idType;
        }

        public void setIdType(String idType) {
            this.idType = idType;
        }

        public String getIdNumber() {
            return idNumber;
        }

        public void setIdNumber(String idNumber) {
            this.idNumber = idNumber;
        }

        public List<String> getIdImages() {
            return idImages;
        }

        public void setIdImages(List<String> idImages) {
            this.idImages = idImages;
        }
    }
}
