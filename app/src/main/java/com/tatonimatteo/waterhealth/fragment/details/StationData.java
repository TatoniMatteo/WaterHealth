package com.tatonimatteo.waterhealth.fragment.details;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.tatonimatteo.waterhealth.R;
import com.tatonimatteo.waterhealth.entity.Record;
import com.tatonimatteo.waterhealth.entity.Sensor;
import com.tatonimatteo.waterhealth.fragment.StationDetailsViewModel;
import com.tatonimatteo.waterhealth.view.DateRangePicker;
import com.tatonimatteo.waterhealth.view.LiveDataItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StationData extends Fragment {

    private LinearLayout liveDataContainer;
    private StationDetailsViewModel viewModel;

    private TextView errorText;
    private ImageView errorIcon;
    private DateRangePicker picker;

    private LineChart lineChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(StationDetailsViewModel.class);
        return inflater.inflate(R.layout.station_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        liveDataContainer = view.findViewById(R.id.liveDataContainer);
        errorText = view.findViewById(R.id.liveDataError);
        errorIcon = view.findViewById(R.id.liveDataWarning);
        picker = view.findViewById(R.id.datePicker);
        lineChart = view.findViewById(R.id.lineChart);

        viewModel.getLiveData().observe(getViewLifecycleOwner(), data -> {
            liveDataContainer.removeAllViews();
            errorIcon.setVisibility(View.INVISIBLE);
            if (data.isEmpty()) errorText.setVisibility(View.VISIBLE);
            else errorText.setVisibility(View.GONE);
            data.forEach(triple -> {
                LiveDataItem item = new LiveDataItem(requireContext());
                item.setComponent(
                        triple.getFirst().getSensorType().getName(),
                        triple.getFirst().getUnit(),
                        triple.getFirst().getDecimals(),
                        triple.getSecond().getValue(),
                        triple.getThird()
                );
                liveDataContainer.addView(item);
                if (triple.getThird()) errorIcon.setVisibility(View.VISIBLE);
            });
        });

        picker.setFragmentManager(this.getChildFragmentManager());
        picker.getDateRange().observe(getViewLifecycleOwner(), range -> {
            Map<Sensor, List<Record>> records = viewModel.getRecordsByDateRange(range.getStartDate(), range.getEndDate());

            List<LineData> lineData = new ArrayList<>();
            for (Map.Entry<Sensor, List<Record>> entry : records.entrySet()) {
                List<Entry> entries = new ArrayList<>();
                int count = 0;
                for (Record record : entry.getValue()) {
                    entries.add(new Entry(count++, record.getValue().floatValue()));
                }
                LineDataSet dataSet = new LineDataSet(entries, entry.getKey().getSensorType().getName());
                dataSet.setColor(Color.BLUE);
                dataSet.setCircleColor(Color.RED);
                dataSet.setLineWidth(2f);
                dataSet.setDrawValues(false);
                lineChart.setData(new LineData(dataSet));
                lineChart.invalidate();
            }
        });

    }
}