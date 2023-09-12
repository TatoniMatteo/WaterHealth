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


public class StationRecyclerViewAdapter extends RecyclerView.Adapter<StationRecyclerViewAdapter.ViewHolder> {

    private OnItemClickListener listener;
    private List<Station> stationList;

    public StationRecyclerViewAdapter(List<Station> stationList) {
        this.stationList = stationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stations_list_item, parent, false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Station station = stationList.get(position);
        holder.stationName.setText(station.getName());
        holder.stationLocation.setText(station.getLocationName());
        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stationList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView stationName;
        public final TextView stationLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            stationName = itemView.findViewById(R.id.list_station_name);
            stationLocation = itemView.findViewById(R.id.list_station_location);
        }


        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + stationName.getText() + "'";
        }
    }
}
