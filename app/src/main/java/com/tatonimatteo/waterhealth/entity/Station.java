package com.tatonimatteo.waterhealth.entity;

import androidx.annotation.NonNull;

import com.tatonimatteo.waterhealth.configuration.AppConfiguration;

public class Station {
    private long id;
    private String name;
    private String phone;
    private double latitude;
    private double longitude;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @NonNull
    @Override
    public String toString() {
        return "Station " + id + " - " + name;
    }

    public String getLocationName() {
        return AppConfiguration
                .getInstance()
                .getMapAPI()
                .getLocationName(latitude, longitude);

    }
}
