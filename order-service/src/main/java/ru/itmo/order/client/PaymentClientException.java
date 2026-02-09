package ru.itmo.order.client;

public class PaymentClientException extends RuntimeException {

    public PaymentClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
