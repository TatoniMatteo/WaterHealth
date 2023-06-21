package com.tatonimatteo.waterhealth.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.tatonimatteo.waterhealth.R;
import com.tatonimatteo.waterhealth.fragment.stations.grid.StationGridFragment;
import com.tatonimatteo.waterhealth.fragment.stations.list.StationListFragment;
import com.tatonimatteo.waterhealth.fragment.stations.map.StationMapsFragment;

public class Stations extends Fragment {

    private StationsViewModel mViewModel;

    public static Stations newInstance() {
        return new Stations();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(StationsViewModel.class);
        return inflater.inflate(R.layout.fragment_stations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageButton listButton = view.findViewById(R.id.listButton);
        ImageButton gridButton = view.findViewById(R.id.gridButton);
        ImageButton mapButton = view.findViewById(R.id.mapButton);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        StationListFragment list = new StationListFragment();
        StationGridFragment grid = new StationGridFragment();
        StationMapsFragment map = new StationMapsFragment();

        fragmentManager.beginTransaction().add(R.id.listContainer, list).commit();

        listButton.setOnClickListener((view1) -> {
            fragmentManager.beginTransaction().replace(R.id.listContainer, list).commit();
        });

        gridButton.setOnClickListener((view1) -> {
            fragmentManager.beginTransaction().replace(R.id.listContainer, grid).commit();
        });

        mapButton.setOnClickListener((view1) -> {
            fragmentManager.beginTransaction().replace(R.id.listContainer, map).commit();
        });
    }
}