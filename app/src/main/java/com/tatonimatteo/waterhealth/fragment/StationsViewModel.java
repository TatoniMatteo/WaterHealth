package com.tatonimatteo.waterhealth.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.tatonimatteo.waterhealth.api.exception.DataException;
import com.tatonimatteo.waterhealth.configuration.AppConfiguration;
import com.tatonimatteo.waterhealth.entity.Station;
import com.tatonimatteo.waterhealth.repository.StationRepository;

import java.util.List;

public class StationsViewModel extends ViewModel {

    private final StationRepository stationRepository;

    public StationsViewModel() {
        stationRepository = AppConfiguration.getInstance().getStationRepository();
        stationRepository.loadStations();
    }

    public LiveData<List<Station>> getStations() {
        return stationRepository.getStations();
    }

    public LiveData<DataException> getError() {
        return stationRepository.getError();
    }

    public LiveData<Boolean> getIsLoading() {
        return stationRepository.isLoading();
    }

    public void refreshStations() {
        stationRepository.loadStations();
    }
}
