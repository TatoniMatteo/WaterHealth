package com.tatonimatteo.waterhealth.configuration;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
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
    private final StationController stationController;
    private final SensorController sensorController;
    private final RecordController recordController;
    private final StationRepository stationRepository;
    private final SensorRepository sensorRepository;
    private final RecordRepository recordRepository;
    private final MapAPI mapAPI;

    private AppConfiguration(Context context) {
        HttpManager httpManager = new HttpManager();
        this.authController = new AuthController(httpManager);
        this.stationController = new StationController(httpManager);
        this.sensorController = new SensorController(httpManager);
        this.recordController = new RecordController(httpManager);
        this.stationRepository = new StationRepository(this.stationController);
        this.sensorRepository = new SensorRepository(this.sensorController);
        this.recordRepository = new RecordRepository(this.recordController);
        this.mapAPI = new MapAPI(context.getApplicationContext());
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful())
                        Log.w("Firebase", "Fetching FCM registration token failed", task.getException());
                    else {
                        String msg = "Fetching FCM registration token success " + task.getResult();
                        Log.d("Firebase", msg);
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    }
                });
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
