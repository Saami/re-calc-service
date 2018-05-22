package com.saami.realestate.model.zillow;

import org.json.JSONObject;

public class DeepCompsResponse extends ZillowResponse {

    private JSONObject deepCompsResponse = new JSONObject();

    public DeepCompsResponse(JSONObject response) {
        deepCompsResponse = response;
        setData(deepCompsResponse);
    }

    @Override
    void setData(JSONObject deepCompsResponse) {
        if (deepCompsResponse != null) {
            JSONObject searchResult = deepCompsResponse.optJSONObject("Zestimate:zestimate");
            if (searchResult != null) {
                JSONObject response = searchResult.optJSONObject("response");
                if (response != null) {
                    super.data = response;
                }
            }
        }
    }
}
