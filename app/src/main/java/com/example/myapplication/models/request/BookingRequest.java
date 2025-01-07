package com.example.myapplication.models.request;

public class BookingRequest {
    private String pickUpLocation;
    private String returnLocation;
    private String pickUpDateTime;
    private String returnDateTime;
    private String partnerId;
    private String carId;
    private boolean isDriverRequired;
    private long durationInHours;

    private int totalRent;

    public BookingRequest(String pickUpLocation, String returnLocation, String pickUpDateTime,
                          String returnDateTime, String partnerId, String carId, boolean isDriverRequired, long durationInHours,int totalRent) {
        this.pickUpLocation = pickUpLocation;
        this.returnLocation = returnLocation;
        this.pickUpDateTime = pickUpDateTime;
        this.returnDateTime = returnDateTime;
        this.partnerId = partnerId;
        this.carId = carId;
        this.isDriverRequired = isDriverRequired;
        this.durationInHours = durationInHours;
        this.totalRent=totalRent;
    }

    // Getters and setters (optional, if needed)
}
