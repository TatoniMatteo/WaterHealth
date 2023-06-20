package com.tatonimatteo.waterhealth.entity;

import androidx.annotation.NonNull;

public class Sensor {

    private Long id;

    private SensorType sensorType;

    private long sensorId;

    private long stationId;

    private String unit;

    private int decimals;

    public Long getId() {
        return id;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public long getSensorId() {
        return sensorId;
    }

    public long getStationId() {
        return stationId;
    }

    public String getUnit() {
        return unit;
    }

    public int getDecimals() {
        return decimals;
    }

    @NonNull
    @Override
    public String toString() {
        return "Sensor " + id + " - " + sensorType.getName();
    }
}
