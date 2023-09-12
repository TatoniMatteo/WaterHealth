package com.tatonimatteo.waterhealth.fragment.details;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.tatonimatteo.waterhealth.R;
import com.tatonimatteo.waterhealth.entity.Record;
import com.tatonimatteo.waterhealth.entity.Sensor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RecordRecyclerViewAdapter extends RecyclerView.Adapter<RecordRecyclerViewAdapter.ViewHolder> {
    private List<Pair<Sensor, Record>> recordList;

    public RecordRecyclerViewAdapter(Map<Sensor, List<Record>> recordMap) {
        recordList = new ArrayList<>();
        for (Map.Entry<Sensor, List<Record>> entry : recordMap.entrySet()) {
            Sensor sensor = entry.getKey();
            List<Record> records = entry.getValue();
            for (Record record : records) {
                recordList.add(new Pair<>(sensor, record));
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pair<Sensor, Record> pair = recordList.get(position);
        Sensor sensor = pair.first;
        Record record = pair.second;

        holder.recordType.setText(sensor.getSensorType().getName());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy\nHH:mm", Locale.getDefault());
        holder.recordTime.setText(sdf.format(record.getDateTime()));

        String valueFormat = String.format(Locale.getDefault(), "%.0" + sensor.getDecimals() + "f %s", record.getValue(), sensor.getUnit());
        holder.recordValue.setText(valueFormat);
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView recordType;
        public final TextView recordTime;
        public final TextView recordValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recordType = itemView.findViewById(R.id.recordType);
            recordTime = itemView.findViewById(R.id.recordTime);
            recordValue = itemView.findViewById(R.id.recordValue);
        }
    }
}
