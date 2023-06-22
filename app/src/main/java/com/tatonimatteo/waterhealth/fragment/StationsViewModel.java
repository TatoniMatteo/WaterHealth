package com.tatonimatteo.waterhealth.fragment;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tatonimatteo.waterhealth.api.controller.StationController;
import com.tatonimatteo.waterhealth.configuration.AppConfiguration;
import com.tatonimatteo.waterhealth.entity.Station;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StationsViewModel extends ViewModel {

    private final MutableLiveData<List<Station>> stationsLiveData;
    private final StationController stationController;

    public StationsViewModel() {
        stationsLiveData = new MutableLiveData<>();
        stationController = AppConfiguration.getInstance().getStationController();
    }

    public LiveData<List<Station>> getStations() {
        stationController.getAllStation(new Callback<List<Station>>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<Station>> call,
                    @NonNull Response<List<Station>> response
            ) {
                stationsLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(
                    @NonNull Call<List<Station>> call,
                    @NonNull Throwable t
            ) {

            }
        });
        return stationsLiveData;
    }
}