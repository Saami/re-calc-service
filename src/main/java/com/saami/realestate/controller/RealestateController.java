package com.saami.realestate.controller;

import com.saami.realestate.enums.City;
import com.saami.realestate.model.ZillowData;
import com.saami.realestate.service.api.PropertyReportService;
import com.saami.realestate.service.api.ZillowService;
import com.saami.realestate.util.RealEstateCalculator;
import com.saami.realestate.util.RealEstateConstants;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import static com.saami.realestate.util.RealEstateUtils.formatPrice;

/**
 * Created by sasiddi on 5/1/17.
 */
@RestController
public class RealestateController {

    private final static Logger LOG = LoggerFactory.getLogger(RealestateController.class);

    @Autowired
    PropertyReportService propertyReportService;

    @Autowired
    ZillowService zillowService;

    @RequestMapping(path = "/report", method = RequestMethod.GET)
    public void getCharlotteProperties(HttpServletResponse response) {
        try {
            String csvFileName = "report.csv";

            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"",
                    csvFileName);
            response.setHeader(headerKey, headerValue);
            response.setContentType("text/csv; charset=UTF-8");

            ServletOutputStream outputStream = response.getOutputStream();

            String csvString = propertyReportService.generateReport();
            byte[] bytes = csvString.getBytes("UTF-8");
            outputStream.write(bytes);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @CrossOrigin
    @RequestMapping(path = "/property/search", method = RequestMethod.GET)
    public String getZillowSearchResult(@RequestParam(value = "address", required = true) String addressParam, @RequestParam(value = "city", required = true) String city,
                                  @RequestParam(value = "state", required = true) String state, @RequestParam(value = "zip", required = false) Long zip,
                                  @RequestParam(value = "price", required = false) Double priceArg, @RequestParam(value = "rent", required = false) Double rentArg) {
        LOG.info("hitting the /property/search endpoint");

        ZillowData zillowData = zillowService.getZillowSearchData(addressParam, city, state);

        Double rent = rentArg != null ? rentArg : zillowData.getRentZestimate();
        Double price = priceArg != null ? priceArg : zillowData.getZestimate();
        return getPropertyData(zillowData, price, rent).toString();
    }

    @CrossOrigin
    @RequestMapping(path = "/property/{zpId}", method = RequestMethod.GET)
    public String getZillowPropertyData(@PathVariable(value = "zpId", required = true) String zpId,
                                  @RequestParam(value = "price", required = false) Double priceArg, @RequestParam(value = "rentArg", required = false) Double rentArg) {
        LOG.info(String.format("hitting the /property/%s endpoint", zpId));

        ZillowData zillowData = zillowService.getZestimateData(zpId);
        Double rent = rentArg != null ? rentArg : zillowData.getRentZestimate();
        Double price = priceArg != null ? priceArg : zillowData.getZestimate();
        return getPropertyData(zillowData, price, rent).toString();
    }

    @CrossOrigin
    @RequestMapping(path = "/property/search/{address}", method = RequestMethod.GET)
    public String getZillowSearchResult(@PathVariable(value = "address", required = true) String addressParam){
        LOG.info("hitting the /property/search endpoint");

        addressParam = addressParam
                .replace('/', ' ')
                .replace('-', ' ')
                .replace('.', ' ')
                .replace('_', ' ');
        String[] addressArray = addressParam.split(",");

        String address = addressArray[0].trim();
        String city = addressArray[1].trim();
        String state = addressArray[2].trim().substring(0,2);

        ZillowData zillowData = zillowService.getZillowSearchData(address, city, state);
        return getPropertyData(zillowData, zillowData.getZestimate(), zillowData.getRentZestimate()).toString();

    }


    private JSONObject getPropertyData(ZillowData zillowData, Double price, Double rent) {

        double management = RealEstateCalculator.calculateMonthlyManagementFees(rent);
        double tax = RealEstateCalculator.calculateAnnualTax(getCityTaxRate(zillowData.getAddress().getCity()), price / 12);
        double insurance = City.CHARLOTTE.getHomeInsurance() /12;
        double mortgage = RealEstateCalculator.calculateMonthlyPayment(price - RealEstateCalculator.calculateDownPayment(price));
        double wearTear = RealEstateCalculator.calculateMonthlWearTear(rent);
        double expense  = management+ tax + insurance + mortgage + wearTear;
        double cashFlow = rent - expense;

        JSONObject result = new JSONObject();
        try {
            result.put("address", zillowData.getAddress().getAddress());
            result.put("city", zillowData.getAddress().getCity());
            result.put("state", zillowData.getAddress().getState());
            result.put("zip", zillowData.getAddress().getZip());
            result.put("zestimate", formatPrice(price));
            result.put("rent", formatPrice(rent));
            result.put("management", formatPrice(management));
            result.put("tax", formatPrice(tax));
            result.put("insurance", formatPrice(insurance));
            result.put("mortgage", formatPrice(mortgage));
            result.put("wearTear", formatPrice(wearTear));
            result.put("cashFlow", formatPrice(rent - expense));
            result.put("estimatedReturn", String.valueOf( Math.floor(((cashFlow * 12d) / (RealEstateCalculator.calculateDownPayment(price) + 2500d) * 100) * 100) /100) + "%");

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
