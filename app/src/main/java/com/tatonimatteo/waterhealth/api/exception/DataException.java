package com.tatonimatteo.waterhealth.api.exception;

public class DataException extends Exception {

    public DataException() {
    }

    public DataException(String message) {
        super("Data Exception:" + message);
    }

    public DataException(String message, Throwable cause) {
        super("Data Exception:" + message, cause);
    }

    public DataException(Throwable cause) {
        super(cause);
    }

    public DataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super("Data Exception:" + message, cause, enableSuppression, writableStackTrace);
    }
}
