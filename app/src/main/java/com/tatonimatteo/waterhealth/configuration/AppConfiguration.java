package com.tatonimatteo.waterhealth.configuration;

import android.content.Context;

import com.tatonimatteo.waterhealth.api.HttpManager;
import com.tatonimatteo.waterhealth.api.controller.RecordController;
import com.tatonimatteo.waterhealth.api.controller.SensorController;
import com.tatonimatteo.waterhealth.api.controller.StationController;
import com.tatonimatteo.waterhealth.api.maps.MapAPI;
import com.tatonimatteo.waterhealth.api.security.AuthController;
import com.tatonimatteo.waterhealth.repository.RecordRepository;
import com.tatonimatteo.waterhealth.repository.SensorRepository;
import com.tatonimatteo.waterhealth.repository.StationRepository;

public class AppConfiguration {
    private static volatile AppConfiguration instance;
    private final AuthController authController;
    private final StationRepository stationRepository;
    private final SensorRepository sensorRepository;
    private final RecordRepository recordRepository;
    private final MapAPI mapAPI;

    private AppConfiguration(Context context) {
        HttpManager httpManager = new HttpManager();
        this.authController = new AuthController(httpManager);
        this.stationRepository = new StationRepository(new StationController(httpManager));
        this.sensorRepository = new SensorRepository(new SensorController(httpManager));
        this.recordRepository = new RecordRepository(new RecordController(httpManager));
        this.mapAPI = new MapAPI(context.getApplicationContext());
    }

    public static void init(Context context) {
        if (instance == null) {
            synchronized (AppConfiguration.class) {
                if (instance == null) {
                    instance = new AppConfiguration(context);
                }
            }
        }
    }

    public static AppConfiguration getInstance() throws IllegalStateException {
        if (instance == null) {
            throw new IllegalStateException("AppConfiguration is not initialized. Call init() first.");
        }
        return instance;
    }

    public AuthController getAuthController() {
        return authController;
    }

    public StationRepository getStationRepository() {
        return stationRepository;
    }

    public SensorRepository getSensorRepository() {
        return sensorRepository;
    }

    public RecordRepository getRecordRepository() {
        return recordRepository;
    }

    public MapAPI getMapAPI() {
        return mapAPI;
    }
}
