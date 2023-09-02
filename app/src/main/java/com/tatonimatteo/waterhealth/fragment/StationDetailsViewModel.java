package com.tatonimatteo.waterhealth.fragment;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tatonimatteo.waterhealth.api.controller.StationController;
import com.tatonimatteo.waterhealth.api.exception.DataException;
import com.tatonimatteo.waterhealth.configuration.AppConfiguration;
import com.tatonimatteo.waterhealth.entity.Station;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StationDetailsViewModel extends ViewModel {

    private final MutableLiveData<Throwable> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<Station> station = new MutableLiveData<>();
    private final StationController stationController;

    public StationDetailsViewModel() {
        stationController = AppConfiguration.getInstance().getStationController();
    }

    public LiveData<Station> getSelectedStation() {
        return station;
    }

    public void setSelectedStation(long stationId) {
        isLoading.postValue(true);
        stationController.getStationById(stationId, new Callback<Station>() {
            @Override
            public void onResponse(@NonNull Call<Station> call, @NonNull Response<Station> response) {
                isLoading.postValue(false);

                if (response.isSuccessful()) {
                    station.postValue(response.body());
                } else {
                    error.postValue(new DataException("Errore nel recupero dei dati della stazione."));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Station> call, @NonNull Throwable t) {
                isLoading.postValue(false);
                error.postValue(t);
            }
        });
    }

    public LiveData<Throwable> getError() {
        return error;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}
