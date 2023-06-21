package com.tatonimatteo.waterhealth.api.controller;

import com.tatonimatteo.waterhealth.api.HttpManager;
import com.tatonimatteo.waterhealth.entity.Station;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class StationController {

    private final HttpManager httpManager;

    public StationController(HttpManager httpManager) {
        this.httpManager = httpManager;
    }


    public void getAllStation(Callback<List<Station>> callback) {
        Call<List<Station>> call = httpManager.getHttpService().getAllStation();
        call.enqueue(callback);
    }
}
