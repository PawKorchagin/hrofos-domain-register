package ru.itmo.domainorder.zone;

public class ZoneHasDomainsException extends RuntimeException {
    public ZoneHasDomainsException(String message) {
        super(message);
    }
}
