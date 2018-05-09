package com.yugoo.gis.common.exception;

/**
 * Created by nihao on 18/5/9.
 */
public class GisRuntimeException extends RuntimeException {
    public GisRuntimeException() {
    }

    public GisRuntimeException(String message) {
        super(message);
    }

    public GisRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public GisRuntimeException(Throwable cause) {
        super(cause);
    }

    public GisRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
