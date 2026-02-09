package ru.itmo.domain.exception;

public class ForbiddenWordException extends RuntimeException {

    public ForbiddenWordException(String word) {
        super("Domain name contains forbidden word: " + word);
    }
}
