package com.tatonimatteo.waterhealth.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.tatonimatteo.waterhealth.R;

public class DateRangePicker extends LinearLayout {
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
    }
}