package com.tatonimatteo.waterhealth.view;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateAxisValueFormatter extends ValueFormatter {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        long millis = (long) value;
        Date date = new Date(millis);
        return dateFormat.format(date);
    }
}

