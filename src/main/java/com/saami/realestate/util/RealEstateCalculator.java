package com.saami.realestate.util;

/**
 * Created by sasiddi on 5/4/17.
 */
public class RealEstateCalculator {


    private static final int TERM_IN_YEARS = 30;



    public static double calculateMonthlyPayment(double loanAmount, double interestRateParam) {

        double interestRate = interestRateParam / 100.0;

        double monthlyRate = interestRate / 12.0;

        // The length of the term in months
        // is the number of years times 12

        int termInMonths = TERM_IN_YEARS * 12;

        // Calculate the monthly payment
        // Typically this formula is provided so
        // we won't go into the details

        // The Math.pow() method is used calculate values raised to a power

        double monthlyPayment =
                (loanAmount*monthlyRate) /
                        (1-Math.pow(1+monthlyRate, -termInMonths));

        return monthlyPayment;
    }

    public static double calculateMontylyTax(double taxRate, double homePrice) {
        taxRate = taxRate / 100;
        double annualTax = homePrice * taxRate;
        return annualTax / 12;
    }

    public static double calculateDownPayment(double homePrice, double downPaymentPercent) {
        return homePrice * (downPaymentPercent /100d);
    }

    public static double calculateMonthlyManagementFees(double rent, double managementFees) {
        return rent * (managementFees /100d);
    }

    public static double calculateMonthlyWearTear(double rent, double wearAndTearRate) {
        return rent * (wearAndTearRate /100d);
    }

}
