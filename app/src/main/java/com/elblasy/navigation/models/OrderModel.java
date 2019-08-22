package com.elblasy.navigation.models;

public class OrderModel {

    private String details;
    private String address;
    private boolean active;
    private String token;

    public OrderModel() {
    }

    public OrderModel(String details, boolean active,String address,String token) {
        this.details = details;
        this.active = active;
        this.address = address;
        this.token = token;
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
