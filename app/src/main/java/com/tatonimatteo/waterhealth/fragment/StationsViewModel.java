package com.tatonimatteo.waterhealth.fragment;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.tatonimatteo.waterhealth.api.controller.StationController;
import com.tatonimatteo.waterhealth.api.exception.DataException;
import com.tatonimatteo.waterhealth.configuration.AppConfiguration;
import com.tatonimatteo.waterhealth.entity.Station;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StationsViewModel extends ViewModel {

    private final MutableLiveData<Throwable> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final StationController stationController;
    private LiveData<List<Station>> stations;

    public StationsViewModel() {
        stationController = AppConfiguration.getInstance().getStationController();
        stations = Transformations.map(fetchStations(), Response::body);
    }

    public LiveData<List<Station>> getStations() {
        return stations;
    }

    public LiveData<Throwable> getError() {
        return error;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void refreshStations() {
        stations = Transformations.map(fetchStations(), Response::body);
    }

    private LiveData<Response<List<Station>>> fetchStations() {
        MutableLiveData<Response<List<Station>>> responseLiveData = new MutableLiveData<>();
        isLoading.postValue(true);

        stationController.getAllStation(new Callback<List<Station>>() {
            @Override
            public void onResponse(@NonNull Call<List<Station>> call, @NonNull Response<List<Station>> response) {
                isLoading.postValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    responseLiveData.postValue(response);
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

        return responseLiveData;
    }
}
