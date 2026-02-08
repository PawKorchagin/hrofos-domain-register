package ru.itmo.domain.exception;

public class DuplicateL2DomainException extends RuntimeException {

    public DuplicateL2DomainException(String name) {
        super("L2 domain already exists: " + name);
    }
}
