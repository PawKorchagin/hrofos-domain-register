package ru.itmo.payment.client;

public class DomainClientException extends RuntimeException {

    public DomainClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
