package com.tatonimatteo.waterhealth.api.maps;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

public class MapAPI {

    private final Geocoder geocoder;

    public MapAPI(Context context) {
        this.geocoder = new Geocoder(context, Locale.getDefault());
    }

    public Address getLocation(double lat, double lng) {
        try {
            return Objects.requireNonNull(geocoder.getFromLocation(lat, lng, 1)).get(0);
        } catch (IOException e) {
            return null;
        }
    }
}
