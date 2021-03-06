package com.saami.realestate.service.api;

import com.saami.realestate.model.zillow.DeepCompsResponse;
import com.saami.realestate.model.zillow.UpdatedDetailsResponse;
import com.saami.realestate.model.zillow.ZestimateResonse;
import com.saami.realestate.model.zillow.ZillowSearchResponse;

/**
 * Created by sasiddi on 5/1/17.
 */
public interface ZillowService {
    ZillowSearchResponse getZillowSearchData(String street, String city, String state);
    ZestimateResonse getZestimateData(String zId);
    DeepCompsResponse getDeepComps(String zId);
    UpdatedDetailsResponse getUpdatedDetails(String zId);
}
