package com.tatonimatteo.waterhealth.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tatonimatteo.waterhealth.R;

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

    public LiveDataItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        name = findViewById(R.id.dataName);
        value = findViewById(R.id.dataValue);
    }

    public void setName(String name) {
        this.name.setText(String.format("%s:", name));
    }

    public void setValue(String unit, Double decimal, Double value, boolean error) {
        this.value.setText(String.format("%.0" + decimal.intValue() + "f %s:", unit, value));
        if (error) {
            this.value.setTextColor(Color.RED);
        }
    }

    public void setComponent(String name, String unit, Double decimal, Double value, boolean error) {
        setName(name);
        setValue(unit, decimal, value, error);
    }


}