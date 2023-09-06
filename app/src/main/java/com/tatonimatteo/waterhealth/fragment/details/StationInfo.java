package com.tatonimatteo.waterhealth.fragment.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tatonimatteo.waterhealth.R;
import com.tatonimatteo.waterhealth.fragment.StationDetailsViewModel;

public class StationInfo extends Fragment implements OnMapReadyCallback {

    private StationDetailsViewModel viewModel;
    private TextView name;
    private TextView position;
    private MapView mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(StationDetailsViewModel.class);
        return inflater.inflate(R.layout.station_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = view.findViewById(R.id.stationName);
        position = view.findViewById(R.id.positionLabel);
        mapView = view.findViewById(R.id.mapView);

        try {
            viewModel.getSelectedStation().observe(getViewLifecycleOwner(), station -> {
                if (station != null) {
                    name.setText(station.getName());
                    position.setText(String.format("%s\n%s", station.getCountry(), station.getRegion()));
                }
            });

            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            MapsInitializer.initialize(requireContext());
            mapView.getMapAsync(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        viewModel.getSelectedStation().observe(getViewLifecycleOwner(), station -> {
            if (station != null) {
                name.setText(station.getName());
                LatLng location = new LatLng(station.getLatitude(), station.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(location);
                googleMap.addMarker(markerOptions);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15f));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}