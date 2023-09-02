package com.tatonimatteo.waterhealth.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tatonimatteo.waterhealth.api.controller.StationController;
import com.tatonimatteo.waterhealth.configuration.AppConfiguration;
import com.tatonimatteo.waterhealth.entity.Station;

public class StationDetailsViewModel extends ViewModel {
    private final LiveData<Station> stationLiveData;
    private final StationController stationController;

    public StationDetailsViewModel() {
        stationController = AppConfiguration.getInstance().getStationController();
        stationLiveData = new MutableLiveData<>();
    }
}