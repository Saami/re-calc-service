package com.saami.realestate.model;

/**
 * Created by sasiddi on 5/4/17.
 */
public class ZillowData  {

    private Address address;
    private String zpid;
    private double rentZestimate;
    private double zestimate;

    public Address getAddress() {
        return address;
    }

    public ZillowData setAddress(Address address) {
        this.address = address;
        return this;
    }

    public String getZpid() {
        return zpid;
    }

    public ZillowData setZpid(String zpid) {
        this.zpid = zpid;
        return this;
    }

    public double getRentZestimate() {
        return rentZestimate;
    }

    public ZillowData setRentZestimate(double rentZestimate) {
        this.rentZestimate = rentZestimate;
        return this;
    }

    public double getZestimate() {
        return zestimate;
    }

    public ZillowData setZestimate(double zestimate) {
        this.zestimate = zestimate;
        return this;
    }
}
