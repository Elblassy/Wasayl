package com.elblasy.navigation.models;

public class User {

    private String username, mobile,country,city,userType;

    public User() {
    }


    public User(String username, String mobile, String country, String city, String userType) {
        this.username = username;
        this.mobile = mobile;
        this.country = country;
        this.city = city;
        this.userType = userType;
    }

    //Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public  String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}

