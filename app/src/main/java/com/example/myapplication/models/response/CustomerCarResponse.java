package com.example.myapplication.models.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class CustomerCarResponse implements Parcelable {

    private String id;
    private String brand;
    private String model;
    private int year;
    private int seats;
    private String fuelType;
    private int pricePerDay;
    private int milage;
    private String color;
    private String availabilityStatus;
    private List<String> features;
    private List<String> images;

    private String description;
    private String category;
    private String subCategory;

    // Partner details
    private String partnerId;
    private String partnerName;
    private String partnerEmail;
    private String partnerPhoneNumber;
    private String partnerAddress;

    // Pickup and dropoff locations
    private String pickupLocation;
    private String dropoffLocation;

    // Constructor
    protected CustomerCarResponse(Parcel in) {
        id = in.readString();
        brand = in.readString();
        model = in.readString();
        year = in.readInt();
        seats = in.readInt();
        fuelType = in.readString();
        pricePerDay = in.readInt();
        milage = in.readInt();
        color = in.readString();
        availabilityStatus = in.readString();
        features = in.createStringArrayList();
        images = in.createStringArrayList();
        description = in.readString();
        category = in.readString();
        subCategory = in.readString();

        partnerId = in.readString();
        partnerName = in.readString();
        partnerEmail = in.readString();
        partnerPhoneNumber = in.readString();
        partnerAddress = in.readString();

        pickupLocation = in.readString();
        dropoffLocation = in.readString();
    }

    public static final Creator<CustomerCarResponse> CREATOR = new Creator<CustomerCarResponse>() {
        @Override
        public CustomerCarResponse createFromParcel(Parcel in) {
            return new CustomerCarResponse(in);
        }

        @Override
        public CustomerCarResponse[] newArray(int size) {
            return new CustomerCarResponse[size];
        }
    };

    // Parcelable implementation
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(brand);
        dest.writeString(model);
        dest.writeInt(year);
        dest.writeInt(seats);
        dest.writeString(fuelType);
        dest.writeInt(pricePerDay);
        dest.writeInt(milage);
        dest.writeString(color);
        dest.writeString(availabilityStatus);
        dest.writeStringList(features);
        dest.writeStringList(images);
        dest.writeString(description);
        dest.writeString(category);
        dest.writeString(subCategory);

        dest.writeString(partnerId);
        dest.writeString(partnerName);
        dest.writeString(partnerEmail);
        dest.writeString(partnerPhoneNumber);
        dest.writeString(partnerAddress);

        dest.writeString(pickupLocation);
        dest.writeString(dropoffLocation);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public int getSeats() {
        return seats;
    }

    public String getFuelType() {
        return fuelType;
    }

    public int getPricePerDay() {
        return pricePerDay;
    }

    public int getMilage() {
        return milage;
    }

    public String getColor() {
        return color;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public List<String> getFeatures() {
        return features;
    }

    public List<String> getImages() {
        return images;
    }

    public String getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public String getPartnerEmail() {
        return partnerEmail;
    }

    public String getPartnerPhoneNumber() {
        return partnerPhoneNumber;
    }

    public String getPartnerAddress() {
        return partnerAddress;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public String getDropoffLocation() {
        return dropoffLocation;
    }
}
