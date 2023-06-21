package com.tatonimatteo.waterhealth.configuration;

public interface LoginCallback {
    void onSuccess();
    void onFailure(String errorMessage);
}
