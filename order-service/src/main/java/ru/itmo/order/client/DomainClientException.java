package ru.itmo.order.client;

public class DomainClientException extends RuntimeException {

    public DomainClientException(String message) {
        super(message);
    }

    public DomainClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
