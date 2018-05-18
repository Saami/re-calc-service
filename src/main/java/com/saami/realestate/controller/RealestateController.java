package com.saami.realestate.controller;

import com.saami.realestate.enums.City;
import com.saami.realestate.model.Property;
import com.saami.realestate.service.api.PropertyService;
import com.saami.realestate.util.RealEstateCalculator;
import com.saami.realestate.util.RealEstateConstants;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


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
                                  @RequestParam(value = "price", required = false) Double priceArg, @RequestParam(value = "rent", required = false) Double rentArg) {
        LOG.info(String.format("hitting the /property/%s endpoint", zpId));
        Property property = null;
        try {
            property = propertyService.getPropertyByZpid(zpId);
            Double rent = rentArg != null ? rentArg : property.getEstimatedRent();
            Double price = priceArg != null ? priceArg : property.getEstimatedPrice();
            return getPropertyData(property, price, rent).toString();
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
            return getPropertyData(property, property.getEstimatedPrice(), property.getEstimatedRent()).toString();

        } catch(Exception e) {
            LOG.error(String.format("failed to get search result for address: %s. Reason: %s", addressParam, e.toString()));
            return ("Failed to retrieve results");
        }

    }


    private JSONObject getPropertyData(Property property, Double price, Double rent) {

        double management = RealEstateCalculator.calculateMonthlyManagementFees(rent);
        double tax = RealEstateCalculator.calculateAnnualTax(getCityTaxRate(property.getAddress().getCity()), price / 12);
        double insurance = City.CHARLOTTE.getHomeInsurance() /12;
        double mortgage = RealEstateCalculator.calculateMonthlyPayment(price - RealEstateCalculator.calculateDownPayment(price));
        double wearTear = RealEstateCalculator.calculateMonthlWearTear(rent);
        double expense  = management+ tax + insurance + mortgage + wearTear;
        double cashFlow = rent - expense;

        JSONObject result = new JSONObject();
        try {
            result.put("zpid", property.getZpid());
            result.put("address", property.getAddress().getStreet());
            result.put("city", property.getAddress().getCity());
            result.put("state", property.getAddress().getState());
            result.put("zip", property.getAddress().getZip());
            result.put("zestimate", formatPrice(price));
            result.put("rent", formatPrice(rent));
            result.put("management", formatPrice(management));
            result.put("tax", formatPrice(tax));
            result.put("insurance", formatPrice(insurance));
            result.put("mortgage", formatPrice(mortgage));
            result.put("wearTear", formatPrice(wearTear));
            result.put("cashFlow", formatPrice(rent - expense));
            result.put("estimatedReturn", String.valueOf( Math.floor(((cashFlow * 12d) / (RealEstateCalculator.calculateDownPayment(price) + 2500d) * 100) * 100) /100));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;

    }

    private Double getCityTaxRate(String cityParam) {
        Double taxRate;
        if (!cityParam.isEmpty()) {
            try {
               City city = City.valueOf(cityParam.toUpperCase());
               return city.getTaxRate();
            } catch (Exception e) {
                return RealEstateConstants.DEFAULT_TAX_RTE;
            }
        }
        return RealEstateConstants.DEFAULT_TAX_RTE;
    }



}
