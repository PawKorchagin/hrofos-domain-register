package ru.itmo.domainorder.order.enumeration;

public enum OrderStatus {
    created,
    pending_payment,
    paid,
    cancelled,
    failed
}
