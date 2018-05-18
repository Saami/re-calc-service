package com.saami.realestate.service.impl;

import com.saami.realestate.model.Property;
import com.saami.realestate.model.zillow.ZestimateResonse;
import com.saami.realestate.model.zillow.ZillowSearchResponse;
import com.saami.realestate.service.api.PropertyService;
import com.saami.realestate.service.api.ZillowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class PropertyServiceImpl implements PropertyService {
    private static final Logger LOG = LoggerFactory.getLogger(PropertyServiceImpl.class);

    @Autowired
    ZillowService zillowService;

    @Override
    public Property getPropertyByAddress(String street, String city, String state) {
        if (StringUtils.isEmpty(street) || StringUtils.isEmpty(city) || StringUtils.isEmpty(state)) {
            throw new IllegalArgumentException("Address, City and State are required to getPropertyByAddress");
        }

        ZillowSearchResponse zillowSearchResponse = zillowService.getZillowSearchData(street, city, state);
        Property property = new Property()
                .setZpid(zillowSearchResponse.getZpid())
                .setAddress(zillowSearchResponse.getAddress())
                .setEstimatedPrice(zillowSearchResponse.getZestimate())
                .setEstimatedRent(zillowSearchResponse.getRentEstimate());

        return property;
    }

    @Override
    public Property getPropertyByZpid(String zpid) {
        if (StringUtils.isEmpty(zpid)) {
            throw new IllegalArgumentException("zipd is required to getPropertyByZpid");
        }

        ZestimateResonse zestimateResonse = zillowService.getZestimateData(zpid);
        Property property = new Property()
                .setZpid(zestimateResonse.getZpid())
                .setAddress(zestimateResonse.getAddress())
                .setEstimatedPrice(zestimateResonse.getZestimate())
                .setEstimatedRent(zestimateResonse.getRentEstimate());

        return property;
    }
}
