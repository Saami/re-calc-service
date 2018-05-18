package com.saami.realestate.model.zillow;

import org.json.JSONObject;

public class ZestimateResonse extends ZillowResponse {

    private JSONObject zestimateJson = new JSONObject();

    public ZestimateResonse(JSONObject zestimateResponse) {
        zestimateJson = zestimateResponse;
        setData(zestimateJson);
    }

    @Override
    void setData(JSONObject zestimateJson) {
        if (zestimateJson != null) {
            JSONObject searchResult = zestimateJson.optJSONObject("Zestimate:zestimate");
            if (searchResult != null) {
                JSONObject response = searchResult.optJSONObject("response");
                if (response != null) {
                    super.data = response;
                }
            }
        }
    }

    public String getZpid() {
        String zpid = "";
        if (zestimateJson != null) {
            JSONObject searchResult = zestimateJson.optJSONObject("Zestimate:zestimate");
            if (searchResult != null) {
                JSONObject request = searchResult.optJSONObject("request");
                if (request != null) {
                    zpid = request.optString("zpid");
                }
            }
        }
        return zpid;
    }

}
