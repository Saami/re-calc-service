package com.saami.realestate.model.zillow;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdatedDetailsResponse extends ZillowResponse {


    private static final Logger LOG = LoggerFactory.getLogger(UpdatedDetailsResponse.class);

    private JSONObject updatedDetailsJson = new JSONObject();

    public UpdatedDetailsResponse(JSONObject response) {
        updatedDetailsJson = response;
        setData(updatedDetailsJson);
    }

    @Override
    void setData(JSONObject zestimateJson) {
        if (zestimateJson != null) {
            JSONObject searchResult = zestimateJson.optJSONObject("UpdatedPropertyDetails:updatedPropertyDetails");
            if (searchResult != null) {
                    super.data = searchResult;
                }
        }
    }

    public Double getListPrice() {
        if (data != null) {
            JSONObject price = data.optJSONObject("price");
            if (price != null) {
                return price.optDouble("content");
            }
        }
        return 0d;
    }
}
