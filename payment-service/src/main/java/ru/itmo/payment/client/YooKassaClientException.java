package ru.itmo.payment.client;

public class YooKassaClientException extends RuntimeException {
    public YooKassaClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
