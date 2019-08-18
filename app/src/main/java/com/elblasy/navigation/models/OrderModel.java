package com.elblasy.navigation.models;

public class OrderModel {

    private String details;
    private boolean active;

    public OrderModel() {
    }

    public OrderModel(String details, boolean active) {
        this.details = details;
        this.active = active;
    }

    public String getDetails() {
        return details;
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
