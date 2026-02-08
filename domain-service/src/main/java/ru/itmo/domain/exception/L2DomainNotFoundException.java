package ru.itmo.domain.exception;

public class L2DomainNotFoundException extends RuntimeException {

    public L2DomainNotFoundException(String name) {
        super("L2 domain not found: " + name);
    }
}
