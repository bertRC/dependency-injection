package ru.itpark.exceptions;

public class MissingComponentsException extends RuntimeException {
    public MissingComponentsException() {
    }

    public MissingComponentsException(String message) {
        super(message);
    }

    public MissingComponentsException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingComponentsException(Throwable cause) {
        super(cause);
    }

    public MissingComponentsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
