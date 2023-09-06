package com.tatonimatteo.waterhealth.fragment;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tatonimatteo.waterhealth.api.controller.RecordController;
import com.tatonimatteo.waterhealth.api.controller.SensorController;
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
    private final MutableLiveData<List<Sensor>> sensors = new MutableLiveData<>();
    private final MutableLiveData<List<Triple<Sensor, Record, Boolean>>> currentRecords = new MutableLiveData<>();
    private final StationController stationController;
    private final SensorController sensorController;
    private final RecordController recordController;

    public StationDetailsViewModel() {
        stationController = AppConfiguration.getInstance().getStationController();
        sensorController = AppConfiguration.getInstance().getSensorController();
        recordController = AppConfiguration.getInstance().getRecordController();
    }

    public LiveData<Station> getSelectedStation() {
        return station;
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
                if (response.isSuccessful()) {
                    stationFuture.complete(response.body());
                } else {
                    stationFuture.completeExceptionally(new DataException("Errore nel recupero dei dati della stazione."));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Station> call, @NonNull Throwable t) {
                stationFuture.completeExceptionally(t);
            }
        });

        sensorController.getSensorByStation(stationId, new Callback<List<Sensor>>() {
            @Override
            public void onResponse(@NonNull Call<List<Sensor>> call, @NonNull Response<List<Sensor>> response) {
                if (response.isSuccessful()) {
                    sensorsFuture.complete(response.body());
                } else {
                    sensorsFuture.completeExceptionally(new DataException("Errore nel recupero dei dati della stazione."));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Sensor>> call, @NonNull Throwable t) {
                sensorsFuture.completeExceptionally(t);
            }
        });

        sensorController.getSensorByStation(stationId, new Callback<List<Sensor>>() {
            @Override
            public void onResponse(@NonNull Call<List<Sensor>> call, @NonNull Response<List<Sensor>> response) {
                if (response.isSuccessful()) {
                    sensorsFuture.complete(response.body());
                } else {
                    sensorsFuture.completeExceptionally(new DataException("Errore nel recupero dei sensori della stazione."));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Sensor>> call, @NonNull Throwable t) {
                sensorsFuture.completeExceptionally(t);
            }
        });

        recordController.getCurrentData(stationId, new Callback<List<Record>>() {
            @Override
            public void onResponse(@NonNull Call<List<Record>> call, @NonNull Response<List<Record>> response) {
                if (response.isSuccessful()) {
                    recordsFuture.complete(response.body());
                } else {
                    recordsFuture.completeExceptionally(new DataException("Errore nel recupero dei record reventi!"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Record>> call, @NonNull Throwable t) {
                recordsFuture.completeExceptionally(t);
            }
        });

        recordController.getCurrentOutOfRange(stationId, new Callback<List<Record>>() {
            @Override
            public void onResponse(@NonNull Call<List<Record>> call, @NonNull Response<List<Record>> response) {
                if (response.isSuccessful()) {
                    outOfRangeFuture.complete(response.body());
                } else {
                    outOfRangeFuture.completeExceptionally(new DataException("Errore nel recupero dei record reventi (out of range)!"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Record>> call, @NonNull Throwable t) {
                outOfRangeFuture.completeExceptionally(t);
            }
        });

        CompletableFuture<Void> firstPage = CompletableFuture.allOf(stationFuture, sensorsFuture);
        CompletableFuture<Void> liveData = CompletableFuture.allOf(recordsFuture, outOfRangeFuture, sensorsFuture);

        firstPage.exceptionally(throwable -> {
            error.postValue(throwable);
            return null;
        });

        liveData.exceptionally(throwable -> {
            error.postValue(throwable);
            return null;
        });

        firstPage.thenRun(() -> {
            if (!firstPage.isCompletedExceptionally()) {
                try {
                    station.postValue(stationFuture.get());
                    sensors.postValue(sensorsFuture.get());
                    isLoading.setValue(false);
                } catch (ExecutionException | InterruptedException e) {
                    error.postValue(e);
                }
            }
        });

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

    public LiveData<List<Triple<Sensor, Record, Boolean>>> getLiveData() {
        return currentRecords;
    }

    public LiveData<List<Sensor>> getStationSensors() {
        return sensors;
    }

    public LiveData<Throwable> getError() {
        return error;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}
