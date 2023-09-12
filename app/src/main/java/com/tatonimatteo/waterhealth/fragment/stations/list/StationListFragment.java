package com.tatonimatteo.waterhealth.fragment.stations.list;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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
    private StationRecyclerViewAdapter adapter;
    private NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stationList = new ArrayList<>();
        stationsViewModel = new ViewModelProvider(requireActivity()).get(StationsViewModel.class);
    }

    @Override
    @SuppressLint("NotifyDataSetChanged")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stations_list, container, false);
        navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);

        RecyclerView recyclerViewStations = (RecyclerView) view;
        recyclerViewStations.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new StationRecyclerViewAdapter(stationList);
        recyclerViewStations.setAdapter(adapter);

        stationsViewModel.getStations().observe(getViewLifecycleOwner(), stations -> {
            stationList.clear();
            stationList.addAll(stations);
            adapter.notifyDataSetChanged();
        });

        adapter.setOnItemClickListener(position -> {
            Bundle bundle = new Bundle();
            bundle.putLong("stationId", stationList.get(position).getId());
            navController.navigate(R.id.action_stations_to_stationDetails, bundle);
        });

        return view;
    }
}
