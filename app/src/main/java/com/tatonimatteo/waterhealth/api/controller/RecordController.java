package com.tatonimatteo.waterhealth.api.controller;

import androidx.annotation.NonNull;

import com.tatonimatteo.waterhealth.api.HttpManager;
import com.tatonimatteo.waterhealth.entity.Record;

import java.sql.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class RecordController {

    private final HttpManager httpManager;

    public RecordController(HttpManager httpManager) {
        this.httpManager = httpManager;
    }

    public void getRecordByStationAndDate(
            long stationID,
            @NonNull Date startDate,
            @NonNull Date endDate,
            Callback<List<Record>> callback
    ) {
        Call<List<Record>> call = httpManager.getHttpService().getRecordByStationAndDate(stationID, startDate.toString(), endDate.toString());
        call.enqueue(callback);
    }

    public void getRecordBySensorAndDate(
            long stationID,
            long sensorID,
            @NonNull Date startDate,
            @NonNull Date endDate,
            Callback<List<Record>> callback
    ) {
        long id = (stationID * 1000) + sensorID;
        Call<List<Record>> call = httpManager.getHttpService().getRecordBySensorAndDate(id, startDate.toString(), endDate.toString());
        call.enqueue(callback);
    }
}
