package com.tatonimatteo.waterhealth.fragment.stations.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tatonimatteo.waterhealth.R;
import com.tatonimatteo.waterhealth.entity.Station;

import java.util.List;

public class MyStationRecyclerViewAdapter extends RecyclerView.Adapter<MyStationRecyclerViewAdapter.ViewHolder> {

    private List<Station> stationList;

    public MyStationRecyclerViewAdapter(List<Station> stationList) {
        this.stationList = stationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_station_list_item, parent, false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Station station = stationList.get(position);
        holder.stationName.setText(station.getName());
        holder.stationLocation.setText(station.getLocationName());
    }

    @Override
    public int getItemCount() {
        return stationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView stationName;
        public final TextView stationLocation;

        public ViewHolder(View itemView) {
            super(itemView);
            stationName = itemView.findViewById(R.id.station_name);
            stationLocation = itemView.findViewById(R.id.station_location);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + stationName.getText() + "'";
        }
    }
}