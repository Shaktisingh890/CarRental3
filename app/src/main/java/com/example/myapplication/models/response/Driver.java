package com.example.myapplication.models.response;

import android.os.Parcel;
import android.os.Parcelable;

public class Driver implements Parcelable {
    private String _id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String licenseNumber;
    private String address;
    private String licenseExpiryDate;
    private String licenseFrontImgUrl;
    private String licenseBackImgUrl;
    private boolean availabilityStatus;
    private String imgUrl;

    // Constructor
    public Driver(String _id, String fullName, String email, String phoneNumber, String licenseNumber,
                  String address, String licenseExpiryDate, String licenseFrontImgUrl, String licenseBackImgUrl,
                  boolean availabilityStatus, String imgUrl) {
        this._id = _id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.licenseNumber = licenseNumber;
        this.address = address;
        this.licenseExpiryDate = licenseExpiryDate;
        this.licenseFrontImgUrl = licenseFrontImgUrl;
        this.licenseBackImgUrl = licenseBackImgUrl;
        this.availabilityStatus = availabilityStatus;
        this.imgUrl = imgUrl;
    }

    // Parcelable implementation
    protected Driver(Parcel in) {
        _id = in.readString();
        fullName = in.readString();
        email = in.readString();
        phoneNumber = in.readString();
        licenseNumber = in.readString();
        address = in.readString();
        licenseExpiryDate = in.readString();
        licenseFrontImgUrl = in.readString();
        licenseBackImgUrl = in.readString();
        availabilityStatus = in.readByte() != 0;
        imgUrl = in.readString();
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(fullName);
        dest.writeString(email);
        dest.writeString(phoneNumber);
        dest.writeString(licenseNumber);
        dest.writeString(address);
        dest.writeString(licenseExpiryDate);
        dest.writeString(licenseFrontImgUrl);
        dest.writeString(licenseBackImgUrl);
        dest.writeByte((byte) (availabilityStatus ? 1 : 0));
        dest.writeString(imgUrl);
    }

    // Getters and Setters
    public String getId() {
        return _id;
    }

    public void setId(String id) {
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

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLicenseExpiryDate() {
        return licenseExpiryDate;
    }

    public void setLicenseExpiryDate(String licenseExpiryDate) {
        this.licenseExpiryDate = licenseExpiryDate;
    }

    public String getLicenseFrontImgUrl() {
        return licenseFrontImgUrl;
    }

    public void setLicenseFrontImgUrl(String licenseFrontImgUrl) {
        this.licenseFrontImgUrl = licenseFrontImgUrl;
    }

    public String getLicenseBackImgUrl() {
        return licenseBackImgUrl;
    }

    public void setLicenseBackImgUrl(String licenseBackImgUrl) {
        this.licenseBackImgUrl = licenseBackImgUrl;
    }

    public boolean isAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(boolean availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
