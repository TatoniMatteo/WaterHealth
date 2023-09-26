package com.tatonimatteo.waterhealth.entity;

import android.location.Address;

import androidx.annotation.NonNull;

import com.tatonimatteo.waterhealth.configuration.AppConfiguration;

public class Station {
    private long id;
    private String name;
    private String phone;
    private double latitude;
    private double longitude;
    private Address address;

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

    public String getLocationNameFormatted() {
        if (address == null) setAddress();
        if (address == null) return "Impossibile recuperare il nome della posizione";
        return String.format(
                "%s (%s, %s)",
                address.getLocality(),
                address.getAdminArea(),
                address.getCountryName());
    }

    public String getCountry() {
        if (address == null) setAddress();
        if (address == null) return "Impossibile recuperare il nome della posizione";
        return address.getCountryName();
    }

    public String getLocality() {
        if (address == null) setAddress();
        if (address == null) return "Impossibile recuperare il nome della posizione";
        return address.getLocality();
    }

    public String getAdminArea() {
        if (address == null) setAddress();
        if (address == null) return "Impossibile recuperare il nome della posizione";
        return address.getAdminArea();
    }

    private void setAddress() {
        address = AppConfiguration
                .getInstance()
                .getMapAPI()
                .getLocation(latitude, longitude);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return id == station.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
