package com.tatonimatteo.waterhealth.api.exception;

public class DataException extends Exception {
    private final RetryCallback retryCallback;

    public DataException(RetryCallback retryCallback) {
        this.retryCallback = retryCallback;
    }

    public DataException(String message, RetryCallback retryCallback) {
        super("Data Exception:" + message);
        this.retryCallback = retryCallback;
    }

    public DataException(String message, Throwable cause, RetryCallback retryCallback) {
        super("Data Exception:" + message, cause);
        this.retryCallback = retryCallback;
    }

    public DataException(Throwable cause, RetryCallback retryCallback) {
        super(cause);
        this.retryCallback = retryCallback;
    }

    public DataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, RetryCallback retryCallback) {
        super("Data Exception:" + message, cause, enableSuppression, writableStackTrace);
        this.retryCallback = retryCallback;
    }

    public void retry() {
        if (retryCallback != null) {
            retryCallback.onRetry();
        }
    }
}
