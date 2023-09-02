package com.tatonimatteo.waterhealth.fragment.stations.list;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tatonimatteo.waterhealth.R;
import com.tatonimatteo.waterhealth.entity.Station;
import com.tatonimatteo.waterhealth.fragment.StationsViewModel;

import java.util.ArrayList;
import java.util.List;

public class StationListFragment extends Fragment {

    private List<Station> stationList;
    private StationsViewModel stationsViewModel;
    private MyStationRecyclerViewAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stationList = new ArrayList<>();
        stationsViewModel = new ViewModelProvider(this).get(StationsViewModel.class);
    }

    @Override
    @SuppressLint("NotifyDataSetChanged")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stations_list, container, false);

        RecyclerView recyclerViewStations = (RecyclerView) view;
        recyclerViewStations.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new MyStationRecyclerViewAdapter(stationList);
        recyclerViewStations.setAdapter(adapter);

        stationsViewModel.getStations().observe(getViewLifecycleOwner(), stations -> {
            stationList.clear();
            stationList.addAll(stations);
            adapter.notifyDataSetChanged();
        });

        stationsViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle(getString(R.string.data_error))
                        .setMessage(error.getMessage())
                        .setPositiveButton(getString(R.string.retry), (dialog, which) -> stationsViewModel.refreshStations())
                        .setNegativeButton(getString(R.string.exit), (dialog, which) -> requireActivity().finishAffinity())
                        .setCancelable(false)
                        .show();
            }
        });
        return view;
    }
}
