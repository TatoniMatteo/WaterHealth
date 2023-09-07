package com.tatonimatteo.waterhealth.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.util.Pair;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.tatonimatteo.waterhealth.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateRangePicker extends LinearLayout {

    private final MutableLiveData<DateRange> dateRange = new MutableLiveData<>();
    private FragmentManager fragmentManager;
    private TextView text;
    private ImageView icon;
    private SimpleDateFormat formatter;

    public DateRangePicker(Context context) {
        super(context);
        init();
    }

    public DateRangePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DateRangePicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.date_range_picker, this);

        text = findViewById(R.id.rangeText);
        icon = findViewById(R.id.rangeIcon);

        text.setOnClickListener(v -> showDatePickerDialog());
        icon.setOnClickListener(v -> showDatePickerDialog());

        formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        DateRange today = new DateRange(System.currentTimeMillis(), System.currentTimeMillis());
        dateRange.postValue(today);
        text.setText(String.format("%s - %s", formatter.format(today.getStartDate()), formatter.format(today.getEndDate())));
    }

    private void showDatePickerDialog() {
        MaterialDatePicker<Pair<Long, Long>> picker = MaterialDatePicker
                .Builder
                .dateRangePicker()
                .setTitleText(R.string.scegli_un_range_di_tempo)
                .setSelection(new Pair<>(MaterialDatePicker.todayInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds()))
                .build();

        picker.show(fragmentManager, "picker");

        picker.addOnPositiveButtonClickListener(selection -> {
            DateRange dateRange = new DateRange(selection.first, selection.second);
            this.dateRange.postValue(dateRange);
            text.setText(String.format("%s - %s", formatter.format(dateRange.getStartDate()), formatter.format(dateRange.getEndDate())));
        });

        picker.addOnNegativeButtonClickListener(view -> picker.dismiss());

    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public LiveData<DateRange> getDateRange() {
        return dateRange;
    }

}