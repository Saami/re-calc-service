package com.saami.realestate.util;

import java.text.NumberFormat;
import java.util.Locale;

public class RealEstateUtils {

    public static String formatPrice(Double price) {
        boolean isNegative = price < 0.0;
        NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US);
        if (isNegative) {
            return "-" + n.format(price).replace('(', ' ').replace(')', ' ').trim();
        }

        return n.format(price);
    }

}
