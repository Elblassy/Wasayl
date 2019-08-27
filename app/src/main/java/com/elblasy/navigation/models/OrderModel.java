package com.elblasy.navigation.models;

public class OrderModel {

    private boolean active;
    private String details, placeName, userName, address, token, phoneNumber;

    public OrderModel() {
    }

    public OrderModel(boolean active, String details, String address, String placeName,
                      String userName, String token, String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.active = active;
        this.details = details;
        this.address = address;
        this.token = token;
        this.placeName = placeName;
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDetails() {
        return details;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
