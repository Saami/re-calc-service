package com.saami.realestate.model;

public class RequestParams {
    private double price;
    private double rent;
    private double hoa;
    private double downPaymentPercent;
    private double taxRate;
    private double wearAndPercent;
    private double propertyManagementRate;
    private double interestRate;
    private double insurance;
    private double closingCosts;

    public double getPrice() {
        return price;
    }

    public RequestParams setPrice(double price) {
        this.price = price;
        return this;
    }

    public double getRent() {
        return rent;
    }

    public RequestParams setRent(double rent) {
        this.rent = rent;
        return this;
    }

    public double getHoa() {
        return hoa;
    }

    public RequestParams setHoa(double hoa) {
        this.hoa = hoa;
        return this;
    }

    public double getDownPaymentPercent() {
        return downPaymentPercent;
    }

    public RequestParams setDownPaymentPercent(double downPaymentPercent) {
        this.downPaymentPercent = downPaymentPercent;
        return this;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public RequestParams setTaxRate(double taxRate) {
        this.taxRate = taxRate;
        return this;
    }

    public double getWearAndPercent() {
        return wearAndPercent;
    }

    public RequestParams setWearAndPercent(double wearAndPercent) {
        this.wearAndPercent = wearAndPercent;
        return this;
    }

    public double getPropertyManagementRate() {
        return propertyManagementRate;
    }

    public RequestParams setPropertyManagementRate(double propertyManagementRate) {
        this.propertyManagementRate = propertyManagementRate;
        return this;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public RequestParams setInterestRate(double interestRate) {
        this.interestRate = interestRate;
        return this;
    }

    public double getInsurance() {
        return insurance;
    }

    public RequestParams setInsurance(double insurance) {
        this.insurance = insurance;
        return this;
    }

    public double getClosingCosts() {
        return closingCosts;
    }

    public RequestParams setClosingCosts(double closingCosts) {
        this.closingCosts = closingCosts;
        return this;
    }
}
