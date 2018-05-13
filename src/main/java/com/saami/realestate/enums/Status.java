package com.saami.realestate.enums;

/**
 * Created by sasiddi on 5/3/17.
 */
public enum Status {
    ACTIVE("ACT"),
    TEMP("TEMP");

    private String code;

    Status(String code) {
        this.code = code;
    }
}
