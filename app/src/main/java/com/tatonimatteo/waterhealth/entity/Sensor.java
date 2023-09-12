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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sensor sensor = (Sensor) o;
        return id.equals(sensor.id);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + sensorType.hashCode();
        result = 31 * result + (int) (sensorId ^ (sensorId >>> 32));
        result = 31 * result + (int) (stationId ^ (stationId >>> 32));
        result = 31 * result + unit.hashCode();
        result = 31 * result + decimals;
        return result;
    }
}
