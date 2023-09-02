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

    private final MutableLiveData<Throwable> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoadingLiveData = new MutableLiveData<>();
    private LiveData<List<Station>> stationsLiveData;
    private final StationController stationController;

    public StationsViewModel() {
        stationController = AppConfiguration.getInstance().getStationController();
        stationsLiveData = Transformations.map(fetchStations(), Response::body);
    }

    public LiveData<List<Station>> getStations() {
        return stationsLiveData;
    }

    public LiveData<Throwable> getError() {
        return errorLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoadingLiveData;
    }

    public void refreshStations() {
        stationsLiveData = Transformations.map(fetchStations(), Response::body);
    }

    private LiveData<Response<List<Station>>> fetchStations() {
        MutableLiveData<Response<List<Station>>> responseLiveData = new MutableLiveData<>();
        isLoadingLiveData.postValue(true); // Mostra un indicatore di caricamento

        stationController.getAllStation(new Callback<List<Station>>() {
            @Override
            public void onResponse(@NonNull Call<List<Station>> call, @NonNull Response<List<Station>> response) {
                isLoadingLiveData.postValue(false); // Nascondi l'indicatore di caricamento

                if (response.isSuccessful() && response.body() != null) {
                    if (validateData(response.body())) {
                        responseLiveData.postValue(response);
                        errorLiveData.postValue(null);
                    } else {
                        errorLiveData.postValue(new DataException("Dati non validi"));
                    }
                } else {
                    errorLiveData.postValue(new DataException("Risposta API non riuscita"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Station>> call, @NonNull Throwable t) {
                isLoadingLiveData.postValue(false); // Nascondi l'indicatore di caricamento
                errorLiveData.postValue(t);
            }
        });

        return responseLiveData;
    }

    private boolean validateData(List<Station> stations) {
        // Implementa qui la logica di validazione dei dati, se necessario
        // Restituisci true se i dati sono validi, altrimenti false
        return true;
    }
}
