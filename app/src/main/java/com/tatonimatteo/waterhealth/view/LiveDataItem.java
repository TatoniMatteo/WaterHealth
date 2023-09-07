package com.tatonimatteo.waterhealth.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tatonimatteo.waterhealth.R;

import java.util.Locale;

public class LiveDataItem extends LinearLayout {

    private TextView name;
    private TextView value;

    public LiveDataItem(Context context) {
        super(context);
        init();
    }

    public LiveDataItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveDataItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.live_data_item, this);
        name = findViewById(R.id.dataName);
        value = findViewById(R.id.dataValue);
    }

    public void setName(String name) {
        this.name.setText(String.format("%s:", name));
    }

    public void setValue(String unit, int decimal, Double value, boolean error) {
        this.value.setText(String.format(Locale.getDefault(), "%.0" + decimal + "f %s:", value, unit));
        if (error) {
            this.value.setTextColor(Color.RED);
        }
    }

    public void setComponent(String name, String unit, int decimal, Double value, boolean error) {
        setName(name);
        setValue(unit, decimal, value, error);
    }


}