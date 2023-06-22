package com.tatonimatteo.waterhealth.api.maps;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.Locale;

public class MapAPI {

    private final Geocoder geocoder;

    public MapAPI(Context context) {
        this.geocoder = new Geocoder(context, Locale.getDefault());
    }

    public String getLocationName(double lat, double lng) {
        //COORDINATE L'AQUILA
        //lat=42.348800;
        //lng=13.398154;

        try {
            Address address = geocoder.getFromLocation(lat, lng, 1).get(0);
            return address.getLocality() + " (" + address.getAdminArea() + " - " + address.getCountryName() + ")";
        } catch (IOException e) {
            return "Posizione sconosciuta";
        }
    }
}
