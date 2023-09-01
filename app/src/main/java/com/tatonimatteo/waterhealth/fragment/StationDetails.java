package com.tatonimatteo.waterhealth.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.tatonimatteo.waterhealth.R;

public class StationDetails extends Fragment {

    private StationDetailsViewModel mViewModel;
    private TextView station;

    public static StationDetails newInstance() {
        return new StationDetails();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(StationDetailsViewModel.class);
        return inflater.inflate(R.layout.station_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        station = view.findViewById(R.id.station);
        Bundle bundle = getArguments();
        if (bundle != null) {
            long stationId = bundle.getLong("stationId");
            station.setText(String.valueOf(stationId));
        } else {
            station.setText("Non è arrivato un cazzo");
        }

    }
}