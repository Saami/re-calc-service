package com.saami.realestate.model;

import java.util.ArrayList;
import java.util.List;

public class Property {

    private String zpid;
    private Address address;
    private Double listPrice;
    private Double estimatedPrice;
    private Double estimatedRent;
    private Double taxes;
    private List<String> comparableUrls = new ArrayList<String>();

    public String getZpid() {
        return zpid;
    }

    public Property setZpid(String zpid) {
        this.zpid = zpid;
        return this;
    }

    public Address getAddress() {
        return address;
    }

    public Property setAddress(Address address) {
        this.address = address;
        return this;
    }

    public Double getListPrice() {
        return listPrice;
    }

    public Property setListPrice(Double listPrice) {
        this.listPrice = listPrice;
        return this;
    }

    public Double getEstimatedPrice() {
        return estimatedPrice;
    }

    public Property setEstimatedPrice(Double estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
        return this;
    }

    public Double getEstimatedRent() {
        return estimatedRent;
    }

    public Property setEstimatedRent(Double estimatedRent) {
        this.estimatedRent = estimatedRent;
        return this;
    }

    public Double getTaxes() {
        return taxes;
    }

    public Property setTaxes(Double taxes) {
        this.taxes = taxes;
        return this;
    }

    public List<String> getComparableUrls() {
        return comparableUrls;
    }

    public Property setComparableUrls(List<String> comparableUrls) {
        this.comparableUrls = comparableUrls;
        return this;
    }
}
