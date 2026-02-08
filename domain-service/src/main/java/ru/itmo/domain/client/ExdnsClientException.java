package ru.itmo.domain.client;

public class ExdnsClientException extends RuntimeException {

    public ExdnsClientException(String message) {
        super(message);
    }

    public ExdnsClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
