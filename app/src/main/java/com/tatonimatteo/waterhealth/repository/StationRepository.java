package com.tatonimatteo.waterhealth.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tatonimatteo.waterhealth.api.controller.StationController;
import com.tatonimatteo.waterhealth.api.exception.DataException;
import com.tatonimatteo.waterhealth.entity.Station;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StationRepository implements Repository {

    private final MutableLiveData<Station> selectedStation = new MutableLiveData<>();
    private final MutableLiveData<Throwable> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final StationController stationController;
    private final MutableLiveData<List<Station>> stations;

    public StationRepository(StationController stationController) {
        this.stationController = stationController;
        stations = new MutableLiveData<>();
    }

    @Override
    public LiveData<Throwable> getError() {
        return error;
    }

    @Override
    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

    public LiveData<Station> getSelectedStation() {
        return selectedStation;
    }

    public LiveData<List<Station>> getStations() {
        return stations;
    }

    public void loadStations() {
        fetchStations();
    }

    public void loadSelectedStation(long stationId) {
        if (stations.getValue() == null) {
            loadStations();
        }
        selectedStation.postValue(stations
                .getValue()
                .stream()
                .filter(station -> station.getId() == stationId)
                .findFirst()
                .orElse(null)
        );
    }

    private void fetchStations() {
        isLoading.postValue(true);
        stationController.getAllStation(new Callback<List<Station>>() {
            @Override
            public void onResponse(@NonNull Call<List<Station>> call, @NonNull Response<List<Station>> response) {
                isLoading.postValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    stations.postValue(response.body());
                    error.postValue(null);
                } else {
                    error.postValue(new DataException("Errore nel recupero dei dati delle stazioni."));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Station>> call, @NonNull Throwable t) {
                isLoading.postValue(false);
                error.postValue(t);
            }
        });
    }
}
