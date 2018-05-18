package com.saami.realestate.model.zillow;

import com.saami.realestate.model.Address;
import org.json.JSONObject;

public abstract class ZillowResponse {

    protected JSONObject data;

    abstract void setData(JSONObject responseJson);

    public String getZpid() {
        String zpid = "";
        if (data != null) {
            zpid = data.optString("zpid");
        }
        return zpid;
    }

    public Address getAddress() {
        if (data != null) {
            JSONObject addressJson = data.optJSONObject("address");
            if (addressJson != null) {
                return new Address()
                        .setStreet(addressJson.optString("street"))
                        .setCity(addressJson.optString("city"))
                        .setState(addressJson.optString("state"))
                        .setZip(addressJson.optLong("zipcode"))
                        .setLatitude(addressJson.optDouble("latitude"))
                        .setLongitude(addressJson.optDouble("longitude"));
            }
        }
        return null;
    }

    public Double getRentEstimate() {
        if (data != null) {
            JSONObject rentZestimate = data.optJSONObject("rentzestimate");
            if (rentZestimate != null) {
                JSONObject amountJson = rentZestimate.optJSONObject("amount");
                if (amountJson != null) {
                    return amountJson.optDouble("content");
                }
            }
        }
        return 0d;
    }

    public Double getZestimate() {
        if (data != null) {
            JSONObject zestimate = data.optJSONObject("zestimate");
            if (zestimate != null) {
                JSONObject amountJson = zestimate.optJSONObject("amount");
                if (amountJson != null) {
                    return amountJson.optDouble("content");
                }
            }
        }
        return 0d;
    }


}
