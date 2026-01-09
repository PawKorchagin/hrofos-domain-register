package ru.itmo.domainorder.zone.exception;

public class ZoneAlreadyExistsException extends RuntimeException {
    public ZoneAlreadyExistsException(String message) {
        super(message);
    }
}
