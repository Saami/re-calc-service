package com.saami.realestate.model;

import java.util.Date;

/**
 * Created by sasiddi on 5/3/17.
 */
public class Listing {
    private String date;
    private Address address;
    private Double price;
    private String status;
    private ZillowData zillowData;

    public String getDate() {
        return date;
    }

    public Listing setDate(String date) {
        this.date = date;
        return this;
    }

    public Address getAddress() {
        return address;
    }

    public Listing setAddress(Address address) {
        this.address = address;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public Listing setPrice(Double price) {
        this.price = price;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Listing setStatus(String status) {
        this.status = status;
        return this;
    }

    public ZillowData getZillowData() {
        return zillowData;
    }

    public Listing setZillowData(ZillowData zillowData) {
        this.zillowData = zillowData;
        return this;
    }




}
