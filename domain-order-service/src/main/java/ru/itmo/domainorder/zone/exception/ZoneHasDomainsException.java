package ru.itmo.domainorder.zone.exception;

public class ZoneHasDomainsException extends RuntimeException {
    public ZoneHasDomainsException(String message) {
        super(message);
    }
}
