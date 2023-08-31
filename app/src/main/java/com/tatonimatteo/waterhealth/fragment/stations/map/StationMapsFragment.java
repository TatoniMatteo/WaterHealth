package com.tatonimatteo.waterhealth.fragment.stations.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tatonimatteo.waterhealth.R;
import com.tatonimatteo.waterhealth.entity.Station;
import com.tatonimatteo.waterhealth.fragment.StationsViewModel;

import java.util.ArrayList;
import java.util.List;

public class StationMapsFragment extends Fragment {

    private final List<Station> stationList = new ArrayList<>();
    private boolean isMapReady = false; // Indica se la mappa è pronta
    private final OnMapReadyCallback callback = googleMap -> {
        isMapReady = true; // La mappa è pronta
        updateMapMarkers(); // Aggiorna i marker sulla mappa
    };


    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        StationsViewModel stationsViewModel = new ViewModelProvider(this).get(StationsViewModel.class);
        stationsViewModel.getStations().observe(getViewLifecycleOwner(), stations -> {
            stationList.clear();
            stationList.addAll(stations);
            if (isMapReady) {
                updateMapMarkers(); // Se la mappa è pronta, aggiorna i marker
            }
        });
        return inflater.inflate(R.layout.stations_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private void updateMapMarkers() {
        if (getActivity() == null || stationList.isEmpty()) {
            return;
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(googleMap -> {
                googleMap.clear(); // Rimuove tutti i marker precedenti
                LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder(); // Creo un builder per il bounding box

                for (Station station : stationList) {
                    LatLng position = new LatLng(station.getLatitude(), station.getLongitude());
                    boundsBuilder.include(position); // Aggiungo la posizione del marker al bounding box

                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(position)
                            .title(station.getName())
                            .snippet(station.getLocationName());

                    Marker marker = googleMap.addMarker(markerOptions);
                    assert marker != null;
                    marker.setTag(station);
                }

                LatLngBounds bounds = boundsBuilder.build(); // Costruisco il bounding box
                int padding = 100; // Padding intorno al bounding box in pixel

                // Imposto la mappa sul bounding box con il livello di zoom appropriato
                googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));

                // Listener per il click sulla finestra di informazioni del marker
                googleMap.setOnInfoWindowClickListener(marker -> {
                    Station station = (Station) marker.getTag();
                    if (station != null) {
                        //TODO: mostra dettaglio stazione
                    }
                });
            });
        }
    }
}