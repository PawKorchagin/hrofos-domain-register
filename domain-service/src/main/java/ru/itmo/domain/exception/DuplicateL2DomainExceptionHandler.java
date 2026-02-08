package ru.itmo.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<String> handleL2DomainNotFound(L2DomainNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors ${DB_USER:***REMOVED***} ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + (e.getDefaultMessage() !${DB_USER:***REMOVED***} null ? e.getDefaultMessage() : e.getCode()))
                .collect(Collectors.toList());
        if (errors.isEmpty()) {
            errors ${DB_USER:***REMOVED***} ex.getBindingResult().getGlobalErrors().stream()
                    .map(e -> e.getDefaultMessage() !${DB_USER:***REMOVED***} null ? e.getDefaultMessage() : e.getCode())
                    .collect(Collectors.toList());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<List<String>> handleMethodValidation(HandlerMethodValidationException ex) {
        List<String> errors ${DB_USER:***REMOVED***} ex.getAllValidationResults().stream()
                .flatMap(r -> r.getResolvableErrors().stream())
                .map(err -> err.getDefaultMessage() !${DB_USER:***REMOVED***} null ? err.getDefaultMessage() : (err.getCodes() !${DB_USER:***REMOVED***} null && err.getCodes().length > 0 ? err.getCodes()[0] : "validation failed"))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<List<String>> handleMessageNotReadable(org.springframework.http.converter.HttpMessageNotReadableException ex) {
        String message ${DB_USER:***REMOVED***} ex.getMostSpecificCause() !${DB_USER:***REMOVED***} null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        if (message ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            message ${DB_USER:***REMOVED***} "Invalid request body";
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(List.of(message));
    }
}
