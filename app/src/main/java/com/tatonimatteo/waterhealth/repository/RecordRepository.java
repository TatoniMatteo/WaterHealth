package com.tatonimatteo.waterhealth.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tatonimatteo.waterhealth.api.controller.RecordController;
import com.tatonimatteo.waterhealth.api.exception.DataException;
import com.tatonimatteo.waterhealth.configuration.AppConfiguration;
import com.tatonimatteo.waterhealth.entity.Record;
import com.tatonimatteo.waterhealth.entity.Sensor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import kotlin.Triple;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordRepository implements Repository {

    private final MutableLiveData<List<Triple<Sensor, Record, Boolean>>> currentRecords = new MutableLiveData<>();
    private final MutableLiveData<Throwable> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final RecordController recordController;

    public RecordRepository(RecordController recordController) {
        this.recordController = recordController;
    }

    @Override
    public LiveData<Throwable> getError() {
        return error;
    }

    @Override
    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

    public LiveData<List<Triple<Sensor, Record, Boolean>>> getCurrentRecords() {
        return currentRecords;
    }

    public void loadCurrentRecords(long stationId) {
        isLoading.setValue(true);
        CompletableFuture<List<Record>> currentDataFuture = new CompletableFuture<>();
        CompletableFuture<List<Record>> currentOutOfRangeFuture = new CompletableFuture<>();
        CompletableFuture<List<Sensor>> sensorListFuture = new CompletableFuture<>();

        recordController.getCurrentData(stationId, new Callback<List<Record>>() {
            @Override
            public void onResponse(@NonNull Call<List<Record>> call, @NonNull Response<List<Record>> response) {
                if (response.isSuccessful()) {
                    currentDataFuture.complete(response.body());
                } else {
                    currentDataFuture.completeExceptionally(new Exception("Failed to fetch current data"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Record>> call, @NonNull Throwable t) {
                currentDataFuture.completeExceptionally(t);
            }
        });

        recordController.getCurrentOutOfRange(stationId, new Callback<List<Record>>() {
            @Override
            public void onResponse(@NonNull Call<List<Record>> call, @NonNull Response<List<Record>> response) {
                if (response.isSuccessful()) {
                    currentOutOfRangeFuture.complete(response.body());
                } else {
                    currentOutOfRangeFuture.completeExceptionally(new Exception("Failed to fetch current out-of-range data"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Record>> call, @NonNull Throwable t) {
                currentOutOfRangeFuture.completeExceptionally(t);
            }
        });


        LiveData<List<Sensor>> sensorLiveData = AppConfiguration.getInstance().getSensorRepository().getStationSensors();
        sensorLiveData.observeForever(sensors -> {
            if (sensors != null) {
                sensorListFuture.complete(sensors);
            }
        });

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(currentDataFuture, currentOutOfRangeFuture, sensorListFuture);

        combinedFuture.whenComplete((result, throwable) -> {
            if (throwable == null) {
                List<Record> currentDataRecords = currentDataFuture.join();
                List<Record> currentOutOfRangeRecords = currentOutOfRangeFuture.join();
                List<Sensor> sensors = sensorListFuture.join();

                List<Triple<Sensor, Record, Boolean>> resultRecords = new ArrayList<>();

                for (Record currentDataRecord : currentDataRecords) {
                    boolean isOutOfRange = currentOutOfRangeRecords.contains(currentDataRecord);
                    resultRecords.add(new Triple<>(sensors.stream().filter(sensor -> sensor.getId() == currentDataRecord.getSensorId()).findFirst().orElse(null), currentDataRecord, isOutOfRange));
                }

                currentRecords.postValue(resultRecords);
            } else {
                error.postValue(throwable);
            }

            isLoading.postValue(false);
        });

    }


    public LiveData<Map<Sensor, List<Record>>> getRecordsByDateRange(Date startDate, Date endDate) {
        isLoading.setValue(true);
        MutableLiveData<Map<Sensor, List<Record>>> recordsLiveData = new MutableLiveData<>();
        List<Sensor> sensors = AppConfiguration.getInstance().getSensorRepository().getStationSensors().getValue();

        if (sensors != null) {
            Map<Sensor, List<Record>> sensorRecordMap = new TreeMap<>(Comparator.comparing(sensor -> sensor.getSensorType().getName()));
            AtomicInteger remainingRequests = new AtomicInteger(sensors.size());

            for (Sensor sensor : sensors) {
                recordController.getRecordBySensorAndDate(sensor.getId(), startDate, endDate, new Callback<List<Record>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Record>> call, @NonNull Response<List<Record>> response) {
                        if (response.isSuccessful()) {
                            List<Record> records = response.body();
                            sensorRecordMap.put(sensor, records);

                            if (remainingRequests.decrementAndGet() == 0) {
                                recordsLiveData.postValue(sensorRecordMap);
                            }
                        } else {
                            isLoading.postValue(false);
                            error.postValue(new DataException("Impossibile caricare i dati del sensore: " + sensor.getSensorType().getName()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Record>> call, @NonNull Throwable t) {
                        isLoading.postValue(false);
                        error.postValue(t);
                    }
                });
            }
        }
        isLoading.postValue(false);
        return recordsLiveData;
    }

}
