package com.elblasy.navigation.models;

public class DriverModel {

    private String name, message;
    private int rat;

    public DriverModel() {
    }

    public DriverModel(String name, String message, int rat) {
        this.name = name;
        this.message = message;
        this.rat = rat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRat() {
        return rat;
    }

    public void setRat(int rat) {
        this.rat = rat;
    }
}
