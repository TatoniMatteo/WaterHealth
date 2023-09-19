package com.tatonimatteo.waterhealth.api.exception;

@FunctionalInterface
public interface RetryCallback {
    void onRetry();
}

