package com.tatonimatteo.waterhealth.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tatonimatteo.waterhealth.api.controller.SensorController;
import com.tatonimatteo.waterhealth.api.exception.DataException;
import com.tatonimatteo.waterhealth.entity.Sensor;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SensorRepository implements Repository {

    private final MutableLiveData<List<Sensor>> stationSensors = new MutableLiveData<>();
    private final SensorController sensorController;
    private final MutableLiveData<DataException> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public SensorRepository(SensorController sensorController) {
        this.sensorController = sensorController;
    }

    @Override
    public LiveData<DataException> getError() {
        return error;
    }

    @Override
    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

    public LiveData<List<Sensor>> getStationSensors() {
        return stationSensors;
    }

    public void loadStationSensors(long stationId) {
        isLoading.setValue(true);
        sensorController.getSensorByStation(stationId, new Callback<List<Sensor>>() {
            @Override
            public void onResponse(@NonNull Call<List<Sensor>> call, @NonNull Response<List<Sensor>> response) {
                isLoading.postValue(false);
                if (response.isSuccessful()) {
                    stationSensors.postValue(response.body());
                } else {
                    error.postValue(new DataException("impossibile caricare i sensori delal stazione con ID: " + stationId, () -> loadStationSensors(stationId)));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Sensor>> call, @NonNull Throwable t) {
                isLoading.postValue(false);
                error.postValue(new DataException(t, () -> loadStationSensors(stationId)));
            }
        });
    }
}

