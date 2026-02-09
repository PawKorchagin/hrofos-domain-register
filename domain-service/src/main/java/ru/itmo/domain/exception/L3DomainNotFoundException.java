package ru.itmo.domain.exception;

public class L3DomainNotFoundException extends RuntimeException {

    public L3DomainNotFoundException(String name) {
        super("L3 domain not found: " + name);
    }
}
