package com.tatonimatteo.waterhealth.configuration;

import android.content.Context;

import com.tatonimatteo.waterhealth.api.HttpManager;
import com.tatonimatteo.waterhealth.api.controller.RecordController;
import com.tatonimatteo.waterhealth.api.controller.SensorController;
import com.tatonimatteo.waterhealth.api.controller.StationController;
import com.tatonimatteo.waterhealth.api.maps.MapAPI;
import com.tatonimatteo.waterhealth.api.security.AuthController;

public class AppConfiguration {
    private static volatile AppConfiguration instance;
    private final AuthController authController;
    private final StationController stationController;
    private final SensorController sensorController;
    private final RecordController recordController;
    private final MapAPI mapAPI;

    private AppConfiguration(Context context) {
        HttpManager httpManager = new HttpManager();
        this.authController = new AuthController(httpManager);
        this.stationController = new StationController(httpManager);
        this.sensorController = new SensorController(httpManager);
        this.recordController = new RecordController(httpManager);
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

    public StationController getStationController() {
        return stationController;
    }

    public SensorController getSensorController() {
        return sensorController;
    }

    public RecordController getRecordController() {
        return recordController;
    }

    public MapAPI getMapAPI() {
        return mapAPI;
    }
}
