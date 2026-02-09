package ru.itmo.domain.exception;

public class DnsRecordNameMismatchException extends RuntimeException {

    public DnsRecordNameMismatchException(String pathName, String bodyName) {
        super("name in path (" + pathName + ") must match name in body (" + bodyName + ")");
    }
}
