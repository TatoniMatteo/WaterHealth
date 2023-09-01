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

    private final LiveData<List<Station>> stationsLiveData;
    private final StationController stationController;
    private Throwable error = null;

    public StationsViewModel() {
        stationController = AppConfiguration.getInstance().getStationController();
        stationsLiveData = Transformations.map(fetchStations(), Response::body);
    }

    public LiveData<List<Station>> getStations() throws DataException {
        if (error != null) throw new DataException(error);
        return stationsLiveData;
    }

    private LiveData<Response<List<Station>>> fetchStations() {
        MutableLiveData<Response<List<Station>>> responseLiveData = new MutableLiveData<>();
        stationController.getAllStation(new Callback<List<Station>>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<Station>> call,
                    @NonNull Response<List<Station>> response
            ) {
                responseLiveData.postValue(response);
                error = null;
            }

            @Override
            public void onFailure(
                    @NonNull Call<List<Station>> call,
                    @NonNull Throwable t
            ) {
                error = t;
            }
        });
        return responseLiveData;
    }
}
