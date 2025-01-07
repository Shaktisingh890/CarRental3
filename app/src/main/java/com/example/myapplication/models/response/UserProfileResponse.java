package com.example.myapplication.models.response;

// This is the main UserProfile class that will contain the User data
import com.google.gson.annotations.SerializedName;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class
UserProfileResponse {

    @SerializedName("statusCode")
    private int statusCode;

    @SerializedName("data")
    private Data data;

    @SerializedName("message")
    private String message;

    @SerializedName("success")
    private boolean success;

    // Getters and Setters
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

    public static class Data {

        @SerializedName("paymentDetails")
        private PaymentDetails paymentDetails;

        @SerializedName("bussinessinfo")
        private BusinessInfo businessInfo;

        @SerializedName("_id")
        private String id;

        @SerializedName("fullName")
        private String fullName;

        @SerializedName("email")
        private String email;

        @SerializedName("phoneNumber")
        private String phoneNumber;

        @SerializedName("address")
        private String address;

        @SerializedName("fleet")
        private List<Object> fleet;

        @SerializedName("drivers")
        private List<Object> drivers;

        @SerializedName("imgUrl")
        private String imgUrl;

        @SerializedName("refreshToken")
        private String refreshToken;

        @SerializedName("termsAccepted")
        private boolean termsAccepted;

        @SerializedName("__v")
        private int version;

        @SerializedName("deviceTokens")
        private List<String> deviceTokens;

        // Getters and Setters
        public PaymentDetails getPaymentDetails() {
            return paymentDetails;
        }

        public void setPaymentDetails(PaymentDetails paymentDetails) {
            this.paymentDetails = paymentDetails;
        }

        public BusinessInfo getBusinessInfo() {
            return businessInfo;
        }

        public void setBusinessInfo(BusinessInfo businessInfo) {
            this.businessInfo = businessInfo;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public List<Object> getFleet() {
            return fleet;
        }

        public void setFleet(List<Object> fleet) {
            this.fleet = fleet;
        }

        public List<Object> getDrivers() {
            return drivers;
        }

        public void setDrivers(List<Object> drivers) {
            this.drivers = drivers;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public boolean isTermsAccepted() {
            return termsAccepted;
        }

        public void setTermsAccepted(boolean termsAccepted) {
            this.termsAccepted = termsAccepted;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public List<String> getDeviceTokens() {
            return deviceTokens;
        }

        public void setDeviceTokens(List<String> deviceTokens) {
            this.deviceTokens = deviceTokens;
        }
    }

    public static class PaymentDetails {

        @SerializedName("accountNumber")
        private String accountNumber;

        @SerializedName("upi_id")
        private String upiId;

        // Getters and Setters
        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getUpiId() {
            return upiId;
        }

        public void setUpiId(String upiId) {
            this.upiId = upiId;
        }
    }

    public static class BusinessInfo {

        @SerializedName("company_add")
        private String companyAddress;

        @SerializedName("company_name")
        private String companyName;

        @SerializedName("service_area")
        private String serviceArea;

        // Getters and Setters
        public String getCompanyAddress() {
            return companyAddress;
        }

        public void setCompanyAddress(String companyAddress) {
            this.companyAddress = companyAddress;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getServiceArea() {
            return serviceArea;
        }

        public void setServiceArea(String serviceArea) {
            this.serviceArea = serviceArea;
        }
    }
}
