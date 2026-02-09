package ru.itmo.domain.exception;

public class DnsRecordNotFoundException extends RuntimeException {

    public DnsRecordNotFoundException(Long id) {
        super("DNS record not found: " + id);
    }
}
