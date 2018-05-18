package com.saami.realestate.service.api;

import com.saami.realestate.model.Property;

public interface PropertyService {
    Property getPropertyByAddress(String street, String city, String state);
    Property getPropertyByZpid(String zpid);
}
