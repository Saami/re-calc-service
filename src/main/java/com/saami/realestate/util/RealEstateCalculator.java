package com.saami.realestate.util;

/**
 * Created by sasiddi on 5/4/17.
 */
public class RealEstateCalculator {

    private static final double INTEREST_RATE = 5.00d;
    private static final int TERM_IN_YEARS = 30;
    private static final double DOWN_PAYMENT_PERCENT = 20;
    private static final double MANAGEMENT_FEE_PERCENT = 8;
    private static final double WEAR_TEAR_FACTOR = 20;

    public static double calculateMonthlyPayment(double loanAmount) {

        double interestRate = INTEREST_RATE / 100.0;

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

    public static double calculateAnnualTax(double taxRate, double homePrice) {
        taxRate = taxRate / 100;
        return homePrice * taxRate;
    }

    public static double calculateDownPayment(double homePrice) {
        return homePrice * (DOWN_PAYMENT_PERCENT /100d);
    }

    public static double calculateMonthlyManagementFees(double rent) {
        return rent * (MANAGEMENT_FEE_PERCENT /100d);
    }

    public static double calculateMonthlWearTear(double rent) {
        return rent * (WEAR_TEAR_FACTOR /100d);
    }

}
