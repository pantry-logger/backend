package com.pantrylogger.restapi;

public record ErrorResponse(
        String message,
        int status) {
}