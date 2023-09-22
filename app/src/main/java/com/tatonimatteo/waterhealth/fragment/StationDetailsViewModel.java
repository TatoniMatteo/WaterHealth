package com.tatonimatteo.waterhealth.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tatonimatteo.waterhealth.api.exception.DataException;
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

    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<DataException> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Long>> sensorFilter = new MutableLiveData<>();

    public StationDetailsViewModel() {
        AppConfiguration appConfig = AppConfiguration.getInstance();
        stationRepository = appConfig.getStationRepository();
        sensorRepository = appConfig.getSensorRepository();
        recordRepository = appConfig.getRecordRepository();

        setupLoadingObservations();
    }

    private void setupLoadingObservations() {
        stationRepository.isLoading().observeForever(b -> setLoadingLiveData());
        sensorRepository.isLoading().observeForever(b -> setLoadingLiveData());
        recordRepository.isLoading().observeForever(b -> setLoadingLiveData());

        stationRepository.getError().observeForever(errorLiveData::setValue);
        sensorRepository.getError().observeForever(errorLiveData::setValue);
        recordRepository.getError().observeForever(errorLiveData::setValue);
    }

    private void setLoadingLiveData() {
        boolean[] isLoadingArray = {
                Boolean.TRUE.equals(stationRepository.isLoading().getValue()),
                Boolean.TRUE.equals(sensorRepository.isLoading().getValue()),
                Boolean.TRUE.equals(recordRepository.isLoading().getValue())};
        boolean someoneIsLoading = isLoadingArray[0] || isLoadingArray[1] || isLoadingArray[2];
        Boolean loadingLiveDataValue = loadingLiveData.getValue();

        if (loadingLiveDataValue == null || someoneIsLoading != loadingLiveDataValue) {
            loadingLiveData.setValue(someoneIsLoading);
        }
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

    public LiveData<DataException> getError() {
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
