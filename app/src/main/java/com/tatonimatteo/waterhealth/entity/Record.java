package com.tatonimatteo.waterhealth.entity;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;

public class Record {
    private final long id;
    private final LocalDateTime dateTime;
    private final long stationId;
    private final long sensorId;
    private final Double value;

    public Record(long id, LocalDateTime dateTime, long stationId, long sensorId, Double value) {
        this.id = id;
        this.dateTime = dateTime;
        this.stationId = stationId;
        this.sensorId = sensorId;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public long getStationId() {
        return stationId;
    }

    public long getSensorId() {
        return sensorId;
    }

    public Double getValue() {
        return value;
    }

    @NonNull
    @Override
    public String toString() {
        return getDateTime() + " - " + (stationId * 1000 + sensorId) + ": " + value;
    }
}
