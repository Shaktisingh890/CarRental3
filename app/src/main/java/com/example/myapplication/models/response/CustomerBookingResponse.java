package com.example.myapplication.models.response;

public class CustomerBookingResponse {
    private String _id;
    private String customerId;
    private Car carId;  // Nested Car object
    private String startDate;
    private String endDate;
    private int durationInDays;
    private int totalAmount;
    private String paymentStatus;
    private String status;
    private int penalties;
    private String createdAt;
    private String updatedAt;

    // Getter methods for each field
    public String get_id() {
        return _id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public Car getCarId() {
        return carId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public int getDurationInDays() {
        return durationInDays;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getStatus() {
        return status;
    }

    public int getPenalties() {
        return penalties;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
