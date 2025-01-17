package com.example.myapplication.models.response;

import com.google.gson.annotations.SerializedName;

public class BookingDetailsResponse {

    private int statusCode;
    private BookingDetails data;
    private String message;
    private boolean success;

    // Getters and Setters
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public BookingDetails getData() {
        return data;
    }

    public void setData(BookingDetails data) {
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

    // Inner class for BookingDetails
    public static class BookingDetails {

        @SerializedName("_id")
        private String id;

        @SerializedName("customerId")
        private String customerId;

        @SerializedName("carId")
        private String carId;

        @SerializedName("partnerId")
        private String partnerId;

        @SerializedName("pickupLocation")
        private String pickupLocation;

        @SerializedName("dropoffLocation")
        private String dropoffLocation;

        @SerializedName("startDate")
        private String startDate;

        @SerializedName("endDate")
        private String endDate;

        @SerializedName("durationInDays")
        private int durationInDays;

        @SerializedName("totalAmount")
        private double totalAmount;

        @SerializedName("paymentStatus")
        private String paymentStatus;

        @SerializedName("status")
        private String status;

        @SerializedName("penalties")
        private int penalties;

        @SerializedName("partnerStatus")
        private String partnerStatus;

        @SerializedName("driverStatus")
        private String driverStatus;

        @SerializedName("carModel")
        private String carModel;

        @SerializedName("carName")
        private String carName;

        @SerializedName("pricePerDay")
        private double pricePerDay;

        @SerializedName("cName")
        private String cname;

        @SerializedName("cPhone")
        private String cphone;

        @SerializedName("cImage")
        private String cimage;

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getCarId() {
            return carId;
        }

        public void setCarId(String carId) {
            this.carId = carId;
        }

        public String getPartnerId() {
            return partnerId;
        }

        public void setPartnerId(String partnerId) {
            this.partnerId = partnerId;
        }

        public String getPickupLocation() {
            return pickupLocation;
        }

        public void setPickupLocation(String pickupLocation) {
            this.pickupLocation = pickupLocation;
        }

        public String getDropoffLocation() {
            return dropoffLocation;
        }

        public void setDropoffLocation(String dropoffLocation) {
            this.dropoffLocation = dropoffLocation;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public int getDurationInDays() {
            return durationInDays;
        }

        public void setDurationInDays(int durationInDays) {
            this.durationInDays = durationInDays;
        }

        public double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getPenalties() {
            return penalties;
        }

        public void setPenalties(int penalties) {
            this.penalties = penalties;
        }

        public String getPartnerStatus() {
            return partnerStatus;
        }

        public void setPartnerStatus(String partnerStatus) {
            this.partnerStatus = partnerStatus;
        }

        public String getDriverStatus() {
            return driverStatus;
        }

        public void setDriverStatus(String driverStatus) {
            this.driverStatus = driverStatus;
        }

        public String getCarModel() {
            return carModel;
        }

        public void setCarModel(String carModel) {
            this.carModel = carModel;
        }

        public String getCarName() {
            return carName;
        }

        public void setCarName(String carName) {
            this.carName = carName;
        }

        public double getPricePerDay() {
            return pricePerDay;
        }

        public void setPricePerDay(double pricePerDay) {
            this.pricePerDay = pricePerDay;
        }

        public String getCname() {
            return cname;
        }

        public void setCname(String cname) {
            this.cname = cname;
        }

        public String getCphone() {
            return cphone;
        }

        public void setCphone(String cphone) {
            this.cphone = cphone;
        }

        public String getCimage() {
            return cimage;
        }

        public void setCimage(String cimage) {
            this.cimage = cimage;
        }
    }
}
