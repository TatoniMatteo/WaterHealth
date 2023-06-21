package com.tatonimatteo.waterhealth.api.controller;

import com.tatonimatteo.waterhealth.api.HttpManager;
import com.tatonimatteo.waterhealth.entity.Sensor;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class SensorController {

    private final HttpManager httpManager;

    public SensorController(HttpManager httpManager) {
        this.httpManager = httpManager;
    }

    public void getAllSensor(Callback<List<Sensor>> callback) {
        Call<List<Sensor>> call = httpManager.getHttpService().getAllSensor();
        call.enqueue(callback);
    }

    public void getSensorByStation(
            long stationID,
            Callback<List<Sensor>> callback
    ) {
        Call<List<Sensor>> call = httpManager.getHttpService().getSensorByStation(stationID);
        call.enqueue(callback);
    }

}
