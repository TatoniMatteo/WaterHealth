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
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tatonimatteo.waterhealth.R;
import com.tatonimatteo.waterhealth.entity.Sensor;
import com.tatonimatteo.waterhealth.fragment.StationDetailsViewModel;

import java.util.Comparator;

public class StationInfo extends Fragment implements OnMapReadyCallback {

    private StationDetailsViewModel viewModel;
    private TextView name;
    private TextView phone;
    private TextView sensors;
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
        phone = view.findViewById(R.id.phoneLabel);
        sensors = view.findViewById(R.id.sensorLabel);
        position = view.findViewById(R.id.positionLabel);
        mapView = view.findViewById(R.id.mapView);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        viewModel.getSelectedStation().observe(getViewLifecycleOwner(), station -> {
            if (station != null) {
                name.setText(station.getName());
                phone.setText(station.getPhone());
                position.setText(String.format("%s\n%s,%s", station.getLocality(), station.getAdminArea(), station.getCountry()));
            }
        });

        viewModel.getStationSensors().observe(getViewLifecycleOwner(), sensors -> {
            StringBuilder stringBuilder = new StringBuilder();
            sensors.sort(Comparator.comparing(s -> s.getSensorType().getName()));
            for (Sensor sensor : sensors) {
                stringBuilder.append("•    ").append(sensor.getSensorType().getName()).append("\n");
            }
            this.sensors.setText(stringBuilder.toString());
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        viewModel.getSelectedStation().observe(getViewLifecycleOwner(), station -> {
            if (station != null) {
                LatLng location = new LatLng(station.getLatitude(), station.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions().position(location);
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