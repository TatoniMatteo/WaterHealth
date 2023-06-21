package com.tatonimatteo.waterhealth.api.maps;

import android.content.Context;
import android.location.Geocoder;

import java.io.IOException;
import java.util.Locale;

public class MapAPI {

    private final Geocoder geocoder;

    public MapAPI(Context context) {
        this.geocoder = new Geocoder(context, Locale.getDefault());
    }

    public String getLocationName(double lat, double lng) {
        try {
            return geocoder.getFromLocation(lat, lng, 1).get(0).getLocality();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
