package com.tatonimatteo.waterhealth.fragment.stations.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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

public class StationMapsFragment extends Fragment implements OnMapReadyCallback {

    private final List<Station> stationList = new ArrayList<>();
    private NavController navController;
    private boolean isMapReady = false;
    private GoogleMap googleMap;

    private StationsViewModel stationsViewModel;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        stationsViewModel = new ViewModelProvider(requireActivity()).get(StationsViewModel.class);
        return inflater.inflate(R.layout.stations_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        stationsViewModel.getStations().observe(getViewLifecycleOwner(), stations -> {
            stationList.clear();
            stationList.addAll(stations);
            if (isMapReady) {
                updateMapMarkers();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        isMapReady = true;
        googleMap = map;
        updateMapMarkers();
    }

    private void updateMapMarkers() {
        if (isMapReady && getActivity() != null && googleMap != null && !stationList.isEmpty()) {
            googleMap.clear();
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

            for (Station station : stationList) {
                LatLng position = new LatLng(station.getLatitude(), station.getLongitude());
                boundsBuilder.include(position);

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(position)
                        .title(station.getName())
                        .snippet(station.getLocationName());

                Marker marker = googleMap.addMarker(markerOptions);
                if (marker != null) {
                    marker.setTag(station.getId());
                }
            }

            LatLngBounds bounds = boundsBuilder.build();
            int padding = 100;

            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));

            googleMap.setOnInfoWindowClickListener(marker -> {
                Long stationId = (Long) marker.getTag();
                if (stationId != null) {
                    Bundle bundle = new Bundle();
                    bundle.putLong("stationId", stationId);
                    navController.navigate(R.id.action_stations_to_stationDetails, bundle);
                }
            });
        }
    }
}
