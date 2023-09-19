package com.tatonimatteo.waterhealth.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.tatonimatteo.waterhealth.R;

public class Loader extends ConstraintLayout {

    public Loader(Context context) {
        super(context);
        init();
    }

    public Loader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Loader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.loader, this);
    }
}