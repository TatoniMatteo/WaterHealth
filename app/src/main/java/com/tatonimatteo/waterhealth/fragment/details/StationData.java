package com.tatonimatteo.waterhealth.fragment.details;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.tatonimatteo.waterhealth.R;
import com.tatonimatteo.waterhealth.entity.Record;
import com.tatonimatteo.waterhealth.entity.Sensor;
import com.tatonimatteo.waterhealth.fragment.StationDetailsViewModel;
import com.tatonimatteo.waterhealth.view.DateAxisValueFormatter;
import com.tatonimatteo.waterhealth.view.DateRangePicker;
import com.tatonimatteo.waterhealth.view.LiveDataItem;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import kotlin.Triple;

public class StationData extends Fragment {

    private LinearLayout liveDataContainer;
    private StationDetailsViewModel viewModel;
    private TextView errorText;
    private ImageView errorIcon;
    private LineChart lineChart;
    private ChipGroup chipGroup;
    private List<Long> filters;
    private RecyclerView recyclerView;
    private RecordRecyclerViewAdapter adapter;
    private Map<Sensor, List<Record>> recordsInDateRange;
    private List<Pair<Sensor, Record>> recordList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(StationDetailsViewModel.class);
        return inflater.inflate(R.layout.station_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);

        DateRangePicker picker = view.findViewById(R.id.datePicker);
        picker.setFragmentManager(this.getChildFragmentManager());

        setupLineChart();
        observeDataChanges(picker);

        viewModel.getStationSensors().observe(getViewLifecycleOwner(), this::setChips);
        viewModel.getLiveData().observe(getViewLifecycleOwner(), this::updateLiveData);
    }

    private void initializeViews(View view) {
        liveDataContainer = view.findViewById(R.id.liveDataContainer);
        errorText = view.findViewById(R.id.liveDataError);
        errorIcon = view.findViewById(R.id.liveDataWarning);
        lineChart = view.findViewById(R.id.lineChart);
        chipGroup = view.findViewById(R.id.chipContainer);
        recyclerView = view.findViewById(R.id.recordList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recordList = new ArrayList<>();
        adapter = new RecordRecyclerViewAdapter(recordList);
        recyclerView.setAdapter(adapter);
    }

    private void setupLineChart() {
        TypedValue typedValue = new TypedValue();
        requireActivity().getTheme().resolveAttribute(android.R.attr.textColor, typedValue, true);
        int color = typedValue.data;

        lineChart.getDescription().setEnabled(false);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineColor(color);
        xAxis.setTextColor(color);
        xAxis.setValueFormatter(new DateAxisValueFormatter());


        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisLineColor(color);
        leftAxis.setTextColor(color);

        lineChart.getAxisRight().setDrawLabels(false);

        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextColor(color);

        lineChart.setNoDataText(getString(R.string.no_data_available));
    }

    private void observeDataChanges(DateRangePicker picker) {
        Observer<Map<Sensor, List<Record>>> recordsObserver = data -> {
            recordsInDateRange = data;
            updateRecyclerView();
            if (filters != null && (filters.size() > 0 && filters.size() <= 3) && recordsInDateRange != null)
                drawChart(data, filters);
        };

        picker.getDateRange().observe(getViewLifecycleOwner(), range ->
                viewModel.getRecordsByDateRange(range.getStartDate(), range.getEndDate())
                        .observe(getViewLifecycleOwner(), recordsObserver)
        );

        viewModel.getSensorFilter().observe(getViewLifecycleOwner(), filterList -> {
            filters = filterList;
            updateRecyclerView();
            if (filters != null && (filters.size() > 0 && filters.size() <= 3) && recordsInDateRange != null) {
                lineChart.setVisibility(View.VISIBLE);
                drawChart(recordsInDateRange, filterList);
            } else {
                lineChart.setVisibility(View.GONE);
            }
        });
    }

    private void setChips(List<Sensor> sensors) {
        chipGroup.removeAllViews();
        sensors.sort(Comparator.comparing(sensor -> sensor.getSensorType().getName()));
        for (Sensor s : sensors) {
            Chip chip = createSensorChip(requireContext(), s);
            chip.setOnCheckedChangeListener((compoundButton, checked) -> viewModel.setCheckedSensorFilter(s.getId(), checked));
            chipGroup.addView(chip);
        }
    }

    private Chip createSensorChip(Context context, Sensor sensor) {
        Chip chip = new Chip(context);
        chip.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel()
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED, 60)
                .build();
        chip.setCheckable(true);
        chip.setMinWidth(70);
        chip.setText(sensor.getSensorType().getName());
        chip.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        chip.setShapeAppearanceModel(shapeAppearanceModel);
        chip.setChipIconResource(R.drawable.sensor);
        chip.setChipIconVisible(true);
        chip.setChipStrokeWidth(2);
        return chip;
    }

    private void drawChart(Map<Sensor, List<Record>> records, List<Long> filters) {
        if (thereIsNoData(records)) {
            lineChart.setNoDataText(getString(R.string.no_data_available));
            lineChart.invalidate();
        } else {

            List<ILineDataSet> dataSets = new ArrayList<>();
            for (Map.Entry<Sensor, List<Record>> entry : records.entrySet()) {
                if (!filters.contains(entry.getKey().getId())) continue;
                ArrayList<Entry> entries = new ArrayList<>();
                for (Record record : entry.getValue()) {
                    entries.add(new Entry(
                            record.getDateTime()
                                    .toInstant(ZoneOffset.UTC)
                                    .toEpochMilli(),
                            record.getValue().floatValue())
                    );
                }
                LineDataSet dataSet = createLineDataSet(entries, entry.getKey());
                dataSets.add(dataSet);

                LineData lineData = new LineData(dataSets);
                lineChart.setData(lineData);
                lineChart.animateX(3000, Easing.Linear);
                lineChart.invalidate();
            }
        }
    }

    private LineDataSet createLineDataSet(ArrayList<Entry> entries, Sensor sensor) {
        LineDataSet dataSet = new LineDataSet(entries, String.format("%s (%s)", sensor.getSensorType().getName(), sensor.getUnit()));
        dataSet.setColor(generateRandomColor());
        dataSet.setLineWidth(2f);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);
        return dataSet;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateRecyclerView() {
        recordList.clear();
        recordList.addAll(recordToList());
        adapter.notifyDataSetChanged();
        int itemCount = adapter.getItemCount();
        int desiredHeight = itemCount <= 10 ? ViewGroup.LayoutParams.WRAP_CONTENT : getResources().getDimensionPixelSize(R.dimen.minHeightRecyclerView);

        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
        layoutParams.height = desiredHeight;
        recyclerView.setLayoutParams(layoutParams);
    }

    private void updateLiveData(List<Triple<Sensor, Record, Boolean>> data) {
        liveDataContainer.removeAllViews();
        errorIcon.setVisibility(View.INVISIBLE);
        errorText.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);
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
    }

    private int generateRandomColor() {
        Random random = new Random();
        return Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    private boolean thereIsNoData(Map<Sensor, List<Record>> map) {
        return map.values().stream().allMatch(List::isEmpty);
    }

    private List<Pair<Sensor, Record>> recordToList() {
        List<Pair<Sensor, Record>> list = new ArrayList<>();
        for (Map.Entry<Sensor, List<Record>> entry : recordsInDateRange.entrySet()) {
            Sensor sensor = entry.getKey();
            if (filters == null || filters.isEmpty() || filters.contains(sensor.getId())) {
                List<Record> records = entry.getValue();
                for (Record record : records) {
                    list.add(new Pair<>(sensor, record));
                }
            }
        }

        list.sort((pair1, pair2) -> {
            LocalDateTime dateTime1 = pair1.second.getDateTime();
            LocalDateTime dateTime2 = pair2.second.getDateTime();
            return dateTime1.compareTo(dateTime2);
        });

        return list;
    }
}
