package ru.itmo.domainorder.dns.exception;

public class DnsRecordNotFoundException extends RuntimeException {
    public DnsRecordNotFoundException(String message) {
        super(message);
    }
}
