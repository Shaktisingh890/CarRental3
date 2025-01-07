package com.example.myapplication.models.response;

import android.os.Parcel;
import android.os.Parcelable;

public class BookingResponse implements Parcelable {
    private boolean success;
    private String message;
    private Data data;

    // Nested Data class to represent the "data" object in the JSON
    public static class Data implements Parcelable {
        private String customerId;
        private String carId;
        private String partnerId;
        private String dropoffLocation;

        private String pickupLocation;
        private String startDate;
        private String endDate;
        private int durationInDays;
        private int totalAmount;
        private String driverStatus;

        private String paymentStatus;

        private String partnertStatus;



        private String _id;
        private CarData carData;

        // Nested CarData class to represent "carData" object in the JSON
        public static class CarData implements Parcelable {
            private String brand;
            private String model;
            private int pricePerDay;

            // Constructor
            public CarData() {}

            // Parcelable implementation for CarData
            protected CarData(Parcel in) {
                brand = in.readString();
                model = in.readString();
                pricePerDay = in.readInt();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(brand);
                dest.writeString(model);
                dest.writeInt(pricePerDay);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<CarData> CREATOR = new Creator<CarData>() {
                @Override
                public CarData createFromParcel(Parcel in) {
                    return new CarData(in);
                }

                @Override
                public CarData[] newArray(int size) {
                    return new CarData[size];
                }
            };

            // Getters and Setters
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

            public int getPricePerDay() {
                return pricePerDay;
            }

            public void setPricePerDay(int pricePerDay) {
                this.pricePerDay = pricePerDay;
            }
        }

        // Constructor
        public Data() {}

        // Parcelable implementation for Data
        protected Data(Parcel in) {
            customerId = in.readString();
            carId = in.readString();
            partnerId = in.readString();
            dropoffLocation = in.readString();
            pickupLocation = in.readString();
            startDate = in.readString();
            endDate = in.readString();
            durationInDays = in.readInt();
            totalAmount = in.readInt();
            driverStatus = in.readString();
            paymentStatus = in.readString();
            partnertStatus = in.readString();

            _id = in.readString();
            carData = in.readParcelable(CarData.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(customerId);
            dest.writeString(carId);
            dest.writeString(partnerId);
            dest.writeString(dropoffLocation);
            dest.writeString(pickupLocation);
            dest.writeString(startDate);
            dest.writeString(endDate);
            dest.writeInt(durationInDays);
            dest.writeInt(totalAmount);
            dest.writeString(driverStatus);
            dest.writeString(paymentStatus);
            dest.writeString(partnertStatus);
            dest.writeString(_id);
            dest.writeParcelable(carData, flags);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Data> CREATOR = new Creator<Data>() {
            @Override
            public Data createFromParcel(Parcel in) {
                return new Data(in);
            }

            @Override
            public Data[] newArray(int size) {
                return new Data[size];
            }
        };

        // Getters and Setters
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

        public String getDropoffLocation() {
            return dropoffLocation;
        }

        public void setDropoffLocation(String dropoffLocation) {
            this.dropoffLocation = dropoffLocation;
        }


        public void setPickupLocation(String pickupLocation){this.pickupLocation=pickupLocation;}
        public String getPickupLocation(){return  pickupLocation;}
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

        public int getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(int totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getDriverStatus() {
            return driverStatus;
        }

        public void setDriverStatus(String driverStatus) {
            this.driverStatus = driverStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public String getPartnertStatus(){ return partnertStatus;}


        public void setPartnerStatus(String partnertStatus) {
            this.partnertStatus = partnertStatus;
        }

        public String getPaymentStatus(){ return paymentStatus;}



        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public CarData getCarData() {
            return carData;
        }

        public void setCarData(CarData carData) {
            this.carData = carData;
        }
    }

    // Constructor
    public BookingResponse() {}

    // Parcelable implementation for BookingResponse
    protected BookingResponse(Parcel in) {
        success = in.readByte() != 0;
        message = in.readString();
        data = in.readParcelable(Data.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (success ? 1 : 0));
        dest.writeString(message);
        dest.writeParcelable(data, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BookingResponse> CREATOR = new Creator<BookingResponse>() {
        @Override
        public BookingResponse createFromParcel(Parcel in) {
            return new BookingResponse(in);
        }

        @Override
        public BookingResponse[] newArray(int size) {
            return new BookingResponse[size];
        }
    };

    // Getters and Setters for BookingResponse
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
