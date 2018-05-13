package com.saami.realestate.model;

/**
 * Created by sasiddi on 5/3/17.
 */
public class Address {
    private String address;
    private String city;
    private String state;
    private Long zip;    //fetched from zillow

    public String getAddress() {
        return address;
    }

    public Address setAddress(String address) {
        this.address = address;
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

    @Override
    public String toString() {
        return "Address{" +
                "address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip=" + zip +
                '}';
    }

    public Address setZip(Long zip) {
        this.zip = zip;
        return this;
    }
}
