package com.saami.realestate.model;

/**
 * Created by sasiddi on 5/3/17.
 */
public class Address {
    private String street;
    private String city;
    private String state;
    private Long zip;    //fetched from zillow
    private double latitude;
    private double longitude;

    public String getStreet() {
        return street;
    }

    public Address setStreet(String street) {
        this.street = street;
        return this;
    }

    public String getCity() {
        return city;
    }

    public Address setCity(String city) {
        this.city = city;
        return this;
    }

    public String getState() {
        return state;
    }

    public Address setState(String state) {
        this.state = state;
        return this;
    }

    public Long getZip() {
        return zip;
    }

    public Address setZip(Long zip) {
        this.zip = zip;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public Address setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public Address setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }
}
