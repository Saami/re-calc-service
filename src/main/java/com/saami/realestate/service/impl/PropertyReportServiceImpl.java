package com.saami.realestate.service.impl;

import com.csvreader.CsvWriter;
import com.saami.realestate.enums.City;
import com.saami.realestate.model.Address;
import com.saami.realestate.model.Listing;
import com.saami.realestate.model.ZillowData;
import com.saami.realestate.service.api.MLSDataService;
import com.saami.realestate.service.api.PropertyReportService;
import com.saami.realestate.service.api.ZillowService;
import com.saami.realestate.util.RealEstateCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

import static com.saami.realestate.util.RealEstateUtils.formatPrice;

/**
 * Created by sasiddi on 5/4/17.
 */
@Service
public class PropertyReportServiceImpl implements PropertyReportService {
    private static final Logger LOG = LoggerFactory.getLogger(ZillowServiceImpl.class);

    @Autowired
    MLSDataService mlsDataService;

    @Autowired
    ZillowService zillowService;

    private static final String[] HEADERS = {"Address", "City", "Zip", "Price", "Down Payment", "Estimated Rent", "Mortgage/Taxes/Insurance", "Management",  "Cash Flow", "Estimated Return"};

    @Override
    public String generateReport() {
//        Long startTime = System.currentTimeMillis();
//
//        List<Listing> mlsListings = mlsDataService.getMLSData();
//
//        for (Listing listing : mlsListings) {
//            Address address = listing.getAddress();
//            ZillowData zillowData = zillowService.getZillowSearchData(address.getStreet(),address.getCity(), address.getState());
//
//            if (zillowData == null) {
//                LOG.info(String.format("Zillow data not found for address:%s, %s, %s", address.getStreet(), address.getCity(), address.getStreet()));
//                continue;
//            }
//
//            address.setZip(zillowData.getAddress().getZip());
//            listing.setZillowData(zillowData);
//        }
//
//        return generateCsvReport(mlsListings);
        return "disabled";


    }

    private String generateCsvReport(List<Listing> listings) {
        Long startTime = System.currentTimeMillis();
        try {
            final StringWriter stringWriter = new StringWriter();
            final CsvWriter csvWriter = new CsvWriter(stringWriter, ',');
            csvWriter.writeRecord(HEADERS);

            for (Listing listing : listings) {
                csvWriter.write(listing.getAddress().getStreet());
                csvWriter.write(listing.getAddress().getCity());
                csvWriter.write(listing.getAddress().getZip().toString());
                csvWriter.write(formatPrice(listing.getPrice()));
                csvWriter.write(formatPrice(RealEstateCalculator.calculateDownPayment(listing.getPrice())));
                double rent = listing.getZillowData().getRentZestimate();
                csvWriter.write(formatPrice(rent));

                //

                double management = RealEstateCalculator.calculateMonthlyManagementFees(listing.getZillowData().getRentZestimate());
                double tax = RealEstateCalculator.calculateAnnualTax(City.valueOf(listing.getAddress().getCity().toUpperCase()).getTaxRate(), listing.getPrice()) / 12;
                double insurance = City.valueOf(listing.getAddress().getCity().toUpperCase()).getHomeInsurance()  /12;
                double mortgage = RealEstateCalculator.calculateMonthlyPayment(listing.getPrice() - RealEstateCalculator.calculateDownPayment(listing.getPrice()));
                double wearTear = RealEstateCalculator.calculateMonthlWearTear(rent);
                double expense  = management+ tax + insurance + mortgage + wearTear;
                double cashFlow = rent - expense;

                csvWriter.write(formatPrice(mortgage + tax + insurance));
                csvWriter.write(formatPrice(management));
                csvWriter.write(formatPrice(cashFlow));
                csvWriter.write(String.valueOf((cashFlow * 12d) / (RealEstateCalculator.calculateDownPayment(listing.getPrice()) + 2500d)));
                csvWriter.endRecord();

            }
            csvWriter.close();
           return stringWriter.toString();


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    private double getMonthlyCashFlow(Listing listing) {

        double rent = listing.getZillowData().getRentZestimate();
        double management = RealEstateCalculator.calculateMonthlyManagementFees(listing.getZillowData().getRentZestimate());
        double tax = RealEstateCalculator.calculateAnnualTax(City.CHARLOTTE.getTaxRate(), listing.getPrice()) / 12;
        double insurance = City.CHARLOTTE.getHomeInsurance() /12;
        double mortgage = RealEstateCalculator.calculateMonthlyPayment(listing.getPrice() - RealEstateCalculator.calculateDownPayment(listing.getPrice()));
        double wearTear = RealEstateCalculator.calculateMonthlWearTear(rent);

        double expense  = management+ tax + insurance + mortgage + wearTear;
        return rent - expense;

    }

}
