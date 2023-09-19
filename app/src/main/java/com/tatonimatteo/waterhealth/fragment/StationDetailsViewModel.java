package com.tatonimatteo.waterhealth.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tatonimatteo.waterhealth.configuration.AppConfiguration;
import com.tatonimatteo.waterhealth.entity.Record;
import com.tatonimatteo.waterhealth.entity.Sensor;
import com.tatonimatteo.waterhealth.entity.Station;
import com.tatonimatteo.waterhealth.repository.RecordRepository;
import com.tatonimatteo.waterhealth.repository.SensorRepository;
import com.tatonimatteo.waterhealth.repository.StationRepository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kotlin.Triple;

public class StationDetailsViewModel extends ViewModel {

    private final StationRepository stationRepository;
    private final SensorRepository sensorRepository;
    private final RecordRepository recordRepository;
    private final MediatorLiveData<Boolean> loadingLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<Throwable> errorLiveData = new MediatorLiveData<>();
    private final MutableLiveData<List<Long>> sensorFilter = new MutableLiveData<>();
    private final boolean[] isLoadingArray = {false, false, false};

    public StationDetailsViewModel() {
        stationRepository = AppConfiguration.getInstance().getStationRepository();
        sensorRepository = AppConfiguration.getInstance().getSensorRepository();
        recordRepository = AppConfiguration.getInstance().getRecordRepository();

        stationRepository.isLoading().observeForever(v -> setLoadingLiveData(0, v));
        sensorRepository.isLoading().observeForever(v -> setLoadingLiveData(1, v));
        recordRepository.isLoading().observeForever(v -> setLoadingLiveData(2, v));
    }

    private void setLoadingLiveData(int repository, boolean value) {
        isLoadingArray[repository] = value;

        boolean someoneIsLoading = isLoadingArray[0] || isLoadingArray[1] || isLoadingArray[2];
        Boolean loadingLiveDataValue = loadingLiveData.getValue();

        if (loadingLiveDataValue == null || someoneIsLoading != loadingLiveDataValue)
            loadingLiveData.setValue(someoneIsLoading);
    }

    public LiveData<Station> getSelectedStation() {
        return stationRepository.getSelectedStation();
    }

    public void setSelectedStation(long stationId) {
        stationRepository.loadSelectedStation(stationId);
        sensorRepository.loadStationSensors(stationId);
        recordRepository.loadCurrentRecords(stationId);
    }

    public LiveData<List<Sensor>> getStationSensors() {
        return sensorRepository.getStationSensors();
    }

    public LiveData<List<Triple<Sensor, Record, Boolean>>> getLiveData() {
        return recordRepository.getCurrentRecords();
    }

    public LiveData<Throwable> getError() {
        return errorLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return loadingLiveData;
    }

    public LiveData<Map<Sensor, List<Record>>> getRecordsByDateRange(Date startDate, Date endDate) {
        return recordRepository.getRecordsByDateRange(startDate, endDate);
    }

    public void setCheckedSensorFilter(long id, boolean isChecked) {
        List<Long> currentFilters = sensorFilter.getValue();
        if (currentFilters == null) {
            currentFilters = new ArrayList<>();
        }

        if (isChecked) {
            if (!currentFilters.contains(id)) {
                currentFilters.add(id);
            }
        } else {
            currentFilters.remove(id);
        }

        sensorFilter.setValue(currentFilters);
    }


    public LiveData<List<Long>> getSensorFilter() {
        return sensorFilter;
    }
}
