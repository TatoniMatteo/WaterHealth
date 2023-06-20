package com.tatonimatteo.waterhealth.entity;

import androidx.annotation.NonNull;

public class Station {
    private long id;
    private String name;
    private String phone;
    private String latitude;
    private String longitude;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    @NonNull
    @Override
    public String toString() {
        return "Station " + id + " - " + name;
    }
}
