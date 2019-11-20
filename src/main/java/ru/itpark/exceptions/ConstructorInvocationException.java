package ru.itpark.exceptions;

public class ConstructorInvocationException extends RuntimeException {
    public ConstructorInvocationException() {
    }

    public ConstructorInvocationException(String message) {
        super(message);
    }

    public ConstructorInvocationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConstructorInvocationException(Throwable cause) {
        super(cause);
    }

    public ConstructorInvocationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
