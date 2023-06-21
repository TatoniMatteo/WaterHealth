package com.tatonimatteo.waterhealth;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.tatonimatteo.waterhealth.configuration.AppConfiguration;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppConfiguration.createConfiguration(this);
    }
}