package com.saami.realestate.enums;

/**
 * Created by sasiddi on 5/4/17.
 */
public enum City {
    AUSTIN(1.973d, 800d),
    ATLANTA(1.10d, 800d),
    CHARLOTTE(1.164d, 800d),
    COLUMBUS(2.029, 800d),
    DALLAS(2.173, 800d),
    DURHAM(1.22, 800d),
    INDIANAPOLIS(1.060, 800d),
    RALEIGH(0.87, 800d),
    SAN_ANTONIO(2.097, 800d);


    private double taxRate;
    private double homeInsurance;

    City(double taxRate, double homeInsurance) {
        this.taxRate = taxRate;
        this.homeInsurance = homeInsurance;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public double getHomeInsurance() {
        return homeInsurance;
    }
}
