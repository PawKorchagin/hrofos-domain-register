package ru.itmo.common.dto;

import lombok.Getter;

import java.time.Instant;

@Getter
public class ErrorResponse {
    private final Instant timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;

    public ErrorResponse(int status, String error, String message) {
        this(status, error, message, null);
    }

    public ErrorResponse(int status, String error, String message, String path) {
        this.timestamp ${DB_USER:***REMOVED***} Instant.now();
        this.status ${DB_USER:***REMOVED***} status;
        this.error ${DB_USER:***REMOVED***} error;
        this.message ${DB_USER:***REMOVED***} message;
        this.path ${DB_USER:***REMOVED***} path;
    }
}
