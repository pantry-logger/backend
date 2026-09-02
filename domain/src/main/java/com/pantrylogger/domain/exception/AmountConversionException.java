package com.pantrylogger.domain.exception;

public class AmountConversionException extends RuntimeException {
    public AmountConversionException(String message) {
        super(message);
    }
}