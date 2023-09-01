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
import com.tatonimatteo.waterhealth.api.exception.DataException;
import com.tatonimatteo.waterhealth.entity.Station;
import com.tatonimatteo.waterhealth.fragment.StationsViewModel;

import java.util.ArrayList;
import java.util.List;

public class StationListFragment extends Fragment {

    private final List<Station> stationList;

    private StationsViewModel stationsViewModel;
    private MyStationRecyclerViewAdapter adapter;

    public StationListFragment() {
        stationList = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stations_list, container, false);
        stationsViewModel = new ViewModelProvider(this).get(StationsViewModel.class);

        RecyclerView recyclerViewStations = (RecyclerView) view;
        recyclerViewStations.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new MyStationRecyclerViewAdapter(stationList);
        recyclerViewStations.setAdapter(adapter);

        getData();
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getData() {
        try {
            stationsViewModel.getStations().observe(getViewLifecycleOwner(), stations -> {
                stationList.clear();
                stationList.addAll(stations);
                adapter.notifyDataSetChanged();
            });
        } catch (DataException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Impossibile scaricare i dati!")
                    .setMessage(e.getMessage())
                    .setPositiveButton("Riprova", (dialog, which) -> getData())
                    .setNegativeButton("Esci", (dialog, which) -> requireActivity().finishAffinity())
                    .setCancelable(false)
                    .show();
        }
    }
}