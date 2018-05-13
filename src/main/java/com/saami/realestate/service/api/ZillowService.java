package com.saami.realestate.service.api;

import com.saami.realestate.model.ZillowData;

/**
 * Created by sasiddi on 5/1/17.
 */
public interface ZillowService {
    ZillowData getZillowSearchData(String address, String city, String state);
    ZillowData getZestimateData(String zId);
}
