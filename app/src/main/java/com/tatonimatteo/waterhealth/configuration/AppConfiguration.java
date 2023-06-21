package com.tatonimatteo.waterhealth.configuration;

import android.content.Context;

import com.tatonimatteo.waterhealth.api.HttpManager;
import com.tatonimatteo.waterhealth.api.controller.RecordController;
import com.tatonimatteo.waterhealth.api.controller.SensorController;
import com.tatonimatteo.waterhealth.api.controller.StationController;
import com.tatonimatteo.waterhealth.api.maps.MapAPI;
import com.tatonimatteo.waterhealth.api.security.AuthController;

public class AppConfiguration {
    private static AppConfiguration instance;
    private final AuthController authController;
    private final StationController stationController;
    private final SensorController sensorController;
    private final RecordController recordController;

    private final MapAPI mapAPI;

    public AppConfiguration(Context context) {
        HttpManager httpManager = new HttpManager();
        this.authController = new AuthController(httpManager);
        this.stationController = new StationController(httpManager);
        this.sensorController = new SensorController(httpManager);
        this.recordController = new RecordController(httpManager);
        this.mapAPI = new MapAPI(context);
    }

    public static AppConfiguration getInstance() {
        return instance;
    }

    public static void createConfiguration(Context context) {
        if (instance == null) instance = new AppConfiguration(context);
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
