package com.saami.realestate.controller;

import com.saami.realestate.enums.City;
import com.saami.realestate.model.Property;
import com.saami.realestate.model.RequestParams;
import com.saami.realestate.service.api.PropertyService;
import com.saami.realestate.util.RealEstateCalculator;
import com.saami.realestate.util.RealEstateConstants;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.text.DecimalFormat;

import static com.saami.realestate.util.RealEstateConstants.*;
import static com.saami.realestate.util.RealEstateUtils.formatPrice;

/**
 * Created by sasiddi on 5/1/17.
 */
@RestController
public class RealestateController {

    private final static Logger LOG = LoggerFactory.getLogger(RealestateController.class);

    @Autowired
    PropertyService propertyService;

    @CrossOrigin
    @RequestMapping(path = "/property/{zpId}", method = RequestMethod.GET)
    public String getZillowPropertyData(@PathVariable(value = "zpId", required = true) String zpId,
                                        @RequestParam(value = "price", required = false) Double priceArg,
                                        @RequestParam(value = "rent", required = false) Double rentArg,
                                        @RequestParam(value = "hoa", required = false) Double hoa,
                                        @RequestParam(value = "taxRate", required = false) Double taxRate,
                                        @RequestParam(value = "wTPercent", required = false) Double wTPercent,
                                        @RequestParam(value = "pMPercent", required = false) Double pmRate,
                                        @RequestParam(value = "iR", required = false) Double interestRate,
                                        @RequestParam(value = "downPercent", required = false) Double downPercent,
                                        @RequestParam(value = "insurance", required = false) Double insurance,
                                        @RequestParam(value = "closingCosts", required = false) Double closingCosts
                                        ) {
        LOG.info(String.format("hitting the /property/%s endpoint", zpId));
        Property property = null;
        RequestParams optionalParams = new RequestParams();
        try {
            property = propertyService.getPropertyByZpid(zpId);
            optionalParams
                    .setPrice(priceArg == null ? -1 : priceArg )
                    .setRent(rentArg == null ? -1 : rentArg)
                    .setHoa(hoa == null ? -1 : hoa)
                    .setDownPaymentPercent(downPercent == null? -1 : downPercent)
                    .setTaxRate(taxRate == null ? -1 : taxRate)
                    .setWearAndPercent(wTPercent == null ? -1 : wTPercent )
                    .setPropertyManagementRate(pmRate == null ? -1 : pmRate )
                    .setInterestRate(interestRate == null ? -1 : interestRate )
                    .setInsurance(insurance == null ? -1 : insurance )
                    .setClosingCosts(closingCosts == null ? -1 : closingCosts);

            return getPropertyData(property, optionalParams).toString();
        } catch (Exception e) {
            LOG.error(String.format("failed to get search result for zpId: %s. Reason: %s", zpId, e.toString()));
            return ("Failed to retrieve results");
        }
    }

    @CrossOrigin
    @RequestMapping(path = "/property/search/{address}", method = RequestMethod.GET)
    public String getZillowSearchResult(@PathVariable(value = "address", required = true) String addressParam){
        LOG.info("hitting the /property/search endpoint");
        Property property = null;
        RequestParams requestParams = new RequestParams();
        try {
            addressParam = addressParam
                    .replace('/', ' ')
                    .replace('-', ' ')
                    .replace('.', ' ')
                    .replace('_', ' ');
            String[] addressArray = addressParam.split(",");

            String address = addressArray[0].trim();
            String city = addressArray[1].trim();
            String state = addressArray[2].trim().substring(0,2);

            property = propertyService.getPropertyByAddress(address, city, state);
            return getPropertyData(property, requestParams).toString();

            //address https://www.zillow.com/homes/1420-Rollingwood-Drive_Charlotte_NC_rb/

        } catch(Exception e) {
            LOG.error(String.format("failed to get search result for address: %s. Reason: %s", addressParam, e.toString()));
            return ("Failed to retrieve results");
        }

    }


    private JSONObject getPropertyData(Property property, RequestParams params) {

        double price = params.getPrice() > 0d ? params.getPrice() : property.getListPrice() > 0d ? property.getListPrice() : property.getEstimatedPrice();
        double rent = params.getRent() >= 0d ? params.getRent() : property.getEstimatedRent();
        double managementFeeRate = params.getPropertyManagementRate() >= 0d ? params.getPropertyManagementRate() : DEFAULT_MANAGEMENT_FEE_PERCENT;
        double propertyTaxRate = params.getTaxRate() >= 0d ? params.getTaxRate() : getCityTaxRate(property.getAddress().getCity());
        double interestRate = params.getInterestRate() >= 0d ? params.getInterestRate() : DEFAULT_INTEREST_RATE;
        double wearAndTearRate = params.getWearAndPercent() >= 0d ? params.getWearAndPercent() : DEFAULT_WEAR_AND_TEAR_PERCENT;
        double insuranceFee = params.getInsurance() >= 0d ? params.getInsurance() : City.CHARLOTTE.getHomeInsurance() /12;
        double downPaymentPercent = params.getDownPaymentPercent() >= 0d ? params.getDownPaymentPercent() : DEFAULT_DOWN_PAYMENT_PERCENT;

        double downPayment = RealEstateCalculator.calculateDownPayment(price, downPaymentPercent);
        double management = RealEstateCalculator.calculateMonthlyManagementFees(rent, managementFeeRate);
        double tax = RealEstateCalculator.calculateMontylyTax(propertyTaxRate, price);
        double mortgage = RealEstateCalculator.calculateMonthlyPayment(price - downPayment, interestRate);
        double wearTear = RealEstateCalculator.calculateMonthlyWearTear(rent, wearAndTearRate);
        double expense  = management+ tax + insuranceFee + mortgage + wearTear + params.getHoa();
        double cashFlow = rent - expense;
        double cashToClose = downPayment + params.getClosingCosts();

        DecimalFormat df = new DecimalFormat("#.##");

        JSONObject result = new JSONObject();
        try {
            result.put("zpid", property.getZpid());
            result.put("address", property.getAddress().getStreet());
            result.put("city", property.getAddress().getCity());
            result.put("state", property.getAddress().getState());
            result.put("lat", property.getAddress().getLatitude());
            result.put("long", property.getAddress().getLongitude());
            result.put("zip", property.getAddress().getZip());
            result.put("listPrice", property.getListPrice());
            result.put("zestimate", price);
            result.put("rent", rent);
            result.put("management", df.format(management));
            result.put("tax", df.format(tax));
            result.put("taxRate", propertyTaxRate);
            result.put("insurance", df.format(insuranceFee));
            result.put("downPayment",  df.format(downPayment));

            result.put("mortgage", df.format(mortgage));
            result.put("wearTear", df.format(wearTear));
            result.put("cashFlow", formatPrice(rent - expense));
            result.put("estimatedReturn", String.valueOf(Math.floor(((cashFlow * 12d) / cashToClose * 100) * 100) /100));
            result.put("cashToClose", df.format(cashToClose));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;

    }

    private Double getCityTaxRate(String cityParam) {
        if (!cityParam.isEmpty()) {
            try {
                cityParam = cityParam.replace(' ', '_');
               City city = City.valueOf(cityParam.toUpperCase());
               return city.getTaxRate();
            } catch (Exception e) {
                return RealEstateConstants.DEFAULT_TAX_RATE;
            }
        }
        return RealEstateConstants.DEFAULT_TAX_RATE;
    }



}
