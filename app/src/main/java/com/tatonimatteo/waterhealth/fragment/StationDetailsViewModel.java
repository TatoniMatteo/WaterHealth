package com.tatonimatteo.waterhealth.fragment;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tatonimatteo.waterhealth.api.controller.StationController;
import com.tatonimatteo.waterhealth.api.exception.DataException;
import com.tatonimatteo.waterhealth.configuration.AppConfiguration;
import com.tatonimatteo.waterhealth.entity.Record;
import com.tatonimatteo.waterhealth.entity.Sensor;
import com.tatonimatteo.waterhealth.entity.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import kotlin.Triple;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StationDetailsViewModel extends ViewModel {

    private final MutableLiveData<Throwable> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<Station> station = new MutableLiveData<>();
    private final MutableLiveData<Sensor> sensors = new MutableLiveData<>();
    private final MutableLiveData<List<Triple<Sensor, Record, Boolean>>> currentRecords = new MutableLiveData<>();
    private final StationController stationController;

    public StationDetailsViewModel() {
        stationController = AppConfiguration.getInstance().getStationController();
    }

    public LiveData<Station> getSelectedStation() {
        return station;
    }

    public LiveData<List<Triple<Sensor, Record, Boolean>>> getLiveData() {
        return currentRecords;
    }


    public void setSelectedStation(long stationId) {
        isLoading.postValue(true);

        CompletableFuture<Station> stationFuture = new CompletableFuture<>();
        CompletableFuture<List<Sensor>> sensorsFuture = new CompletableFuture<>();
        CompletableFuture<List<Record>> recordsFuture = new CompletableFuture<>();
        CompletableFuture<List<Record>> outOfRangeFuture = new CompletableFuture<>();

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

        CompletableFuture<Void> firstPage = CompletableFuture.allOf(stationFuture, sensorsFuture);
        CompletableFuture<Void> liveData = CompletableFuture.allOf(recordsFuture, outOfRangeFuture, sensorsFuture);

        liveData.thenRun(() -> {
            if (!liveData.isCompletedExceptionally()) {
                try {
                    List<Record> records = recordsFuture.get();
                    List<Record> outOfRangeRecords = outOfRangeFuture.get();

                    Map<Long, Sensor> sensorMap = Objects.requireNonNull(sensorsFuture.get())
                            .stream()
                            .collect(Collectors.toMap(Sensor::getId, sensor -> sensor));

                    List<Triple<Sensor, Record, Boolean>> result = new ArrayList<>();

                    for (Record record : records) {
                        result.add(new Triple<>(sensorMap.get(record.getSensorId()), record, outOfRangeRecords.contains(record)));
                    }
                    currentRecords.postValue(result);
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public LiveData<Sensor> getStationSensors() {
        return sensors;
    }

    public LiveData<Throwable> getError() {
        return error;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}
