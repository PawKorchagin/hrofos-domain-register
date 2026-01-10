package ru.itmo.domainorder.dns.exception;

public class DomainAccessDeniedException extends RuntimeException {
    public DomainAccessDeniedException(String message) {
        super(message);
    }
}
