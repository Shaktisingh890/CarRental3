package com.example.myapplication.models.response;

public class Ride {
    private String date;
    private String price;
    private String pickupLocation;
    private String dropLocation;
    private String status;

    public Ride(String date, String price, String pickupLocation, String dropLocation, String status) {
        this.date = date;
        this.price = price;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public String getPrice() {
        return price;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public String getDropLocation() {
        return dropLocation;
    }

    public String getStatus() {
        return status;
    }
}
