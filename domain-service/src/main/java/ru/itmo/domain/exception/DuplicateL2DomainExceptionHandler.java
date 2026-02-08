package ru.itmo.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class DuplicateL2DomainExceptionHandler {

    private static final String MESSAGE ${DB_USER:***REMOVED***} "Такой домен уже существует";

    @ExceptionHandler(DuplicateL2DomainException.class)
    public ResponseEntity<List<String>> handleDuplicateL2Domain(DuplicateL2DomainException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(List.of(MESSAGE));
    }

    @ExceptionHandler(L2DomainNotFoundException.class)
    public ResponseEntity<List<String>> handleL2DomainNotFound(L2DomainNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(List.of(ex.getMessage()));
    }
}
