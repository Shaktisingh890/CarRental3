package com.example.myapplication.models.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class CustomerBookingResponse implements Parcelable {
    private boolean success;
    private String message;
    private List<BookingData> data;

    public CustomerBookingResponse(boolean success, String message, List<BookingData> data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    protected CustomerBookingResponse(Parcel in) {
        success = in.readByte() != 0;
        message = in.readString();
        data = in.createTypedArrayList(BookingData.CREATOR);
    }

    public static final Creator<CustomerBookingResponse> CREATOR = new Creator<CustomerBookingResponse>() {
        @Override
        public CustomerBookingResponse createFromParcel(Parcel in) {
            return new CustomerBookingResponse(in);
        }

        @Override
        public CustomerBookingResponse[] newArray(int size) {
            return new CustomerBookingResponse[size];
        }
    };

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<BookingData> getData() {
        return data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (success ? 1 : 0));
        parcel.writeString(message);
        parcel.writeTypedList(data);
    }

    // BookingData class
    public static class BookingData implements Parcelable {
        private String _id;
        private String customerId;
        private Car carId;
        private String partnerId;
        private String pickupLocation;
        private String dropoffLocation;
        private String startDate;
        private String endDate;
        private int durationInDays;
        private double totalAmount;
        private String paymentStatus;
        private String status;
        private int penalties;
        private String partnerStatus;
        private String driverStatus;
        private String createdAt;
        private String updatedAt;
        private Driver driverId;

        public BookingData(String _id, String customerId, Car carId, String partnerId, String pickupLocation, String dropoffLocation,
                           String startDate, String endDate, int durationInDays, double totalAmount, String paymentStatus,
                           String status, int penalties, String partnerStatus, String driverStatus, String createdAt,
                           String updatedAt, Driver driverId) {
            this._id = _id;
            this.customerId = customerId;
            this.carId = carId;
            this.partnerId = partnerId;
            this.pickupLocation = pickupLocation;
            this.dropoffLocation = dropoffLocation;
            this.startDate = startDate;
            this.endDate = endDate;
            this.durationInDays = durationInDays;
            this.totalAmount = totalAmount;
            this.paymentStatus = paymentStatus;
            this.status = status;
            this.penalties = penalties;
            this.partnerStatus = partnerStatus;
            this.driverStatus = driverStatus;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.driverId = driverId;
        }

        protected BookingData(Parcel in) {
            _id = in.readString();
            customerId = in.readString();
            carId = in.readParcelable(Car.class.getClassLoader());
            partnerId = in.readString();
            pickupLocation = in.readString();
            dropoffLocation = in.readString();
            startDate = in.readString();
            endDate = in.readString();
            durationInDays = in.readInt();
            totalAmount = in.readDouble();
            paymentStatus = in.readString();
            status = in.readString();
            penalties = in.readInt();
            partnerStatus = in.readString();
            driverStatus = in.readString();
            createdAt = in.readString();
            updatedAt = in.readString();
            driverId = in.readParcelable(Driver.class.getClassLoader());
        }

        public static final Creator<BookingData> CREATOR = new Creator<BookingData>() {
            @Override
            public BookingData createFromParcel(Parcel in) {
                return new BookingData(in);
            }

            @Override
            public BookingData[] newArray(int size) {
                return new BookingData[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(_id);
            parcel.writeString(customerId);
            parcel.writeParcelable(carId, i);
            parcel.writeString(partnerId);
            parcel.writeString(pickupLocation);
            parcel.writeString(dropoffLocation);
            parcel.writeString(startDate);
            parcel.writeString(endDate);
            parcel.writeInt(durationInDays);
            parcel.writeDouble(totalAmount);
            parcel.writeString(paymentStatus);
            parcel.writeString(status);
            parcel.writeInt(penalties);
            parcel.writeString(partnerStatus);
            parcel.writeString(driverStatus);
            parcel.writeString(createdAt);
            parcel.writeString(updatedAt);
            parcel.writeParcelable(driverId, i);
        }

        // Getters and Setters
        public String getId() {
            return _id;
        }

        public void setId(String _id) {
            this._id = _id;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public Car getCarId() {
            return carId;
        }

        public void setCarId(Car carId) {
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

        public Driver getDriverId() {
            return driverId;
        }

        public void setDriverId(Driver driverId) {
            this.driverId = driverId;
        }
    }


    // Driver class
    public static class Driver implements Parcelable {
        private String _id;
        private String fullName;
        private String email;
        private String phoneNumber;

        public Driver(String _id, String fullName, String email, String phoneNumber) {
            this._id = _id;
            this.fullName = fullName;
            this.email = email;
            this.phoneNumber = phoneNumber;
        }

        protected Driver(Parcel in) {
            _id = in.readString();
            fullName = in.readString();
            email = in.readString();
            phoneNumber = in.readString();
        }

        public static final Creator<Driver> CREATOR = new Creator<Driver>() {
            @Override
            public Driver createFromParcel(Parcel in) {
                return new Driver(in);
            }

            @Override
            public Driver[] newArray(int size) {
                return new Driver[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(_id);
            parcel.writeString(fullName);
            parcel.writeString(email);
            parcel.writeString(phoneNumber);
        }

        // Getters
        public String getId() {
            return _id;
        }

        public void setId(String _id) {
            this._id = _id;
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
    }

    // Car class
    public static class Car implements Parcelable {
        private Location location;
        private List<String> images;
        private String brand;
        private String model;
        private int year;
        private int seats;
        private String fuelType;
        private double pricePerDay;
        private String color;
        private List<String> features;
        private String availabilityStatus;

        public Car(Location location, List<String> images, String brand, String model, int year, int seats, String fuelType, double pricePerDay, String color, List<String> features, String availabilityStatus) {
            this.location = location;
            this.images = images;
            this.brand = brand;
            this.model = model;
            this.year = year;
            this.seats = seats;
            this.fuelType = fuelType;
            this.pricePerDay = pricePerDay;
            this.color = color;
            this.features = features;
            this.availabilityStatus = availabilityStatus;
        }

        protected Car(Parcel in) {
            location = in.readParcelable(Location.class.getClassLoader());
            images = in.createStringArrayList();
            brand = in.readString();
            model = in.readString();
            year = in.readInt();
            seats = in.readInt();
            fuelType = in.readString();
            pricePerDay = in.readDouble();
            color = in.readString();
            features = in.createStringArrayList();
            availabilityStatus = in.readString();
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
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeParcelable(location, i);
            parcel.writeStringList(images);
            parcel.writeString(brand);
            parcel.writeString(model);
            parcel.writeInt(year);
            parcel.writeInt(seats);
            parcel.writeString(fuelType);
            parcel.writeDouble(pricePerDay);
            parcel.writeString(color);
            parcel.writeStringList(features);
            parcel.writeString(availabilityStatus);
        }

        // Getter and Setter methods

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
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

        public void setFuelType(String fuelType) {
            this.fuelType = fuelType;
        }

        public double getPricePerDay() {
            return pricePerDay;
        }

        public void setPricePerDay(double pricePerDay) {
            this.pricePerDay = pricePerDay;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public List<String> getFeatures() {
            return features;
        }

        public void setFeatures(List<String> features) {
            this.features = features;
        }

        public String getAvailabilityStatus() {
            return availabilityStatus;
        }

        public void setAvailabilityStatus(String availabilityStatus) {
            this.availabilityStatus = availabilityStatus;
        }
    }

    // Location class (unchanged)
    public static class Location implements Parcelable {
        private String type;
        private List<Double> coordinates;

        public Location(String type, List<Double> coordinates) {
            this.type = type;
            this.coordinates = coordinates;
        }

        protected Location(Parcel in) {
            type = in.readString();
            coordinates = in.readArrayList(Double.class.getClassLoader());
        }

        public static final Creator<Location> CREATOR = new Creator<Location>() {
            @Override
            public Location createFromParcel(Parcel in) {
                return new Location(in);
            }

            @Override
            public Location[] newArray(int size) {
                return new Location[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(type);
            parcel.writeList(coordinates);
        }
    }
}
