package com.saami.realestate.model.zillow;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZillowSearchResponse extends ZillowResponse {
    private static final Logger LOG = LoggerFactory.getLogger(ZillowSearchResponse.class);

    private JSONObject searchJson = new JSONObject();

    public ZillowSearchResponse(JSONObject zillowSearchResponse) {
        searchJson = zillowSearchResponse;
        setData(searchJson);
    }

    @Override
    void setData(JSONObject searchJson) {
        if (searchJson != null) {
            JSONObject searchResults = searchJson.optJSONObject("SearchResults:searchresults");
            if (searchResults != null) {
                JSONObject response = searchResults.optJSONObject("response");
                if (response != null) {
                    JSONObject results = response.optJSONObject("results");
                    if (results != null) {
                        JSONObject result = results.optJSONObject("result");
                        if (result != null) {
                            super.data = result;
                        } else if (results.optJSONArray("result") != null && results.optJSONArray("result").length() > 0) {
                            try {
                                super.data = results.optJSONArray("result").getJSONObject(0);
                            } catch (JSONException e) {
                                LOG.error("Error getting result object from ZillowSearchResponse", e);
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }


}

