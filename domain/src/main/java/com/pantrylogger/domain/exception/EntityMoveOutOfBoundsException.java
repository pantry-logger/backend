package com.pantrylogger.domain.exception;

public class EntityMoveOutOfBoundsException extends RuntimeException {
    public EntityMoveOutOfBoundsException(String message) {
        super(message);
    }
}