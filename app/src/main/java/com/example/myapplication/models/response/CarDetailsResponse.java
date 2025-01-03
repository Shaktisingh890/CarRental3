package com.example.myapplication.models.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CarDetailsResponse {

    @SerializedName("statusCode")
    private int statusCode;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<Car> data;

    @SerializedName("success")
    private boolean success;

    // Getters and Setters
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Car> getData() {
        return data;
    }

    public void setData(List<Car> data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class Car implements Parcelable {

        @SerializedName("_id")
        private String id;

        @SerializedName("brand")
        private String brand;

        @SerializedName("model")
        private String model;

        @SerializedName("year")
        private int year;

        @SerializedName("seats")
        private int seats;

        @SerializedName("fuelType")
        private String fuelType;

        @SerializedName("description")
        private String description;

        @SerializedName("pricePerDay")
        private int pricePerDay;

        @SerializedName("milage")
        private int milage;

        @SerializedName("color")
        private String color;

        @SerializedName("availabilityStatus")
        private String availabilityStatus;

        @SerializedName("features")
        private List<String> features;

        @SerializedName("images")
        private List<String> images;

        @SerializedName("partnerId")
        private String partnerId;

        @SerializedName("category")
        private String category;

        @SerializedName("subCategory")
        private String subCategory;

        @SerializedName("bookings")
        private List<Object> bookings;

        @SerializedName("createdAt")
        private String createdAt;

        @SerializedName("updatedAt")
        private String updatedAt;

        @SerializedName("__v")
        private int version;

        // Parcelable implementation
        protected Car(Parcel in) {
            id = in.readString();
            brand = in.readString();
            model = in.readString();
            year = in.readInt();
            seats = in.readInt();
            fuelType = in.readString();
            description = in.readString();
            pricePerDay = in.readInt();
            milage = in.readInt();
            color = in.readString();
            availabilityStatus = in.readString();
            features = in.createStringArrayList();
            images = in.createStringArrayList();
            partnerId = in.readString();
            category = in.readString();
            subCategory = in.readString();
            bookings = in.readArrayList(Object.class.getClassLoader());
            createdAt = in.readString();
            updatedAt = in.readString();
            version = in.readInt();
        }

        public static final Creator<Car> CREATOR = new Creator<Car>() {
            @Override
            public Car createFromParcel(Parcel in) {
                return new Car(in);
            }

            @Override
            public Car[] newArray(int size) {
                return new Car[size];
            }
        };

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(brand);
            dest.writeString(model);
            dest.writeInt(year);
            dest.writeInt(seats);
            dest.writeString(fuelType);
            dest.writeString(description);
            dest.writeInt(pricePerDay);
            dest.writeInt(milage);
            dest.writeString(color);
            dest.writeString(availabilityStatus);
            dest.writeStringList(features);
            dest.writeStringList(images);
            dest.writeString(partnerId);
            dest.writeString(category);
            dest.writeString(subCategory);
            dest.writeList(bookings);
            dest.writeString(createdAt);
            dest.writeString(updatedAt);
            dest.writeInt(version);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getSeats() {
            return seats;
        }

        public void setSeats(int seats) {
            this.seats = seats;
        }

        public String getFuelType() {
            return fuelType;
        }

        public String getDescription(){return description;}

        public void setFuelType(String fuelType) {
            this.fuelType = fuelType;
        }

        public void setDescription(String description){this.description=description;}

        public int getPricePerDay() {
            return pricePerDay;
        }

        public void setPricePerDay(int pricePerDay) {
            this.pricePerDay = pricePerDay;
        }

        public int getMilage() {
            return milage;
        }

        public void setMilage(int milage) {
            this.milage = milage;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getAvailabilityStatus() {
            return availabilityStatus;
        }

        public void setAvailabilityStatus(String availabilityStatus) {
            this.availabilityStatus = availabilityStatus;
        }

        public List<String> getFeatures() {
            return features;
        }

        public void setFeatures(List<String> features) {
            this.features = features;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public String getPartnerId() {
            return partnerId;
        }

        public void setPartnerId(String partnerId) {
            this.partnerId = partnerId;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getSubCategory() {
            return subCategory;
        }

        public void setSubCategory(String subCategory) {
            this.subCategory = subCategory;
        }

        public List<Object> getBookings() {
            return bookings;
        }

        public void setBookings(List<Object> bookings) {
            this.bookings = bookings;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }
    }
}
