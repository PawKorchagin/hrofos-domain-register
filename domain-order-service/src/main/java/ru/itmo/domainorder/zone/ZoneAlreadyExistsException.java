package ru.itmo.domainorder.zone;

public class ZoneAlreadyExistsException extends RuntimeException {
    public ZoneAlreadyExistsException(String message) {
        super(message);
    }
}
