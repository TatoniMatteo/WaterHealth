package com.tatonimatteo.waterhealth.fragment.stations.list;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tatonimatteo.waterhealth.R;
import com.tatonimatteo.waterhealth.entity.Station;
import com.tatonimatteo.waterhealth.fragment.StationsViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class StationListFragment extends Fragment {

    private List<Station> stationList;

    public StationListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_station_list, container, false);

        Context context = view.getContext();
        RecyclerView recyclerViewStations = (RecyclerView) view;
        recyclerViewStations.setLayoutManager(new LinearLayoutManager(context));

        stationList = new ArrayList<>();
        MyStationRecyclerViewAdapter stationAdapter = new MyStationRecyclerViewAdapter(stationList);
        recyclerViewStations.setAdapter(stationAdapter);
        StationsViewModel stationsViewModel = new ViewModelProvider(this).get(StationsViewModel.class);
        stationsViewModel.getStations().observe(getViewLifecycleOwner(), stations -> {
            stationList.clear();
            stationList.addAll(stations);
            stationAdapter.notifyDataSetChanged();
        });

        return view;
    }
}