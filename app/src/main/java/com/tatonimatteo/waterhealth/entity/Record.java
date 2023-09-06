package com.tatonimatteo.waterhealth.entity;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Record record = (Record) o;

        if (id != record.id) return false;
        if (stationId != record.stationId) return false;
        if (sensorId != record.sensorId) return false;
        if (!Objects.equals(dateTime, record.dateTime))
            return false;
        return Objects.equals(value, record.value);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        result = 31 * result + (int) (stationId ^ (stationId >>> 32));
        result = 31 * result + (int) (sensorId ^ (sensorId >>> 32));
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
