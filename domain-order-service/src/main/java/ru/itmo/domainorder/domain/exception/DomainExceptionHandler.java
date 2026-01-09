package ru.itmo.domainorder.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.itmo.common.dto.ApiResponse;
import ru.itmo.common.dto.ApiError;
import ru.itmo.domainorder.zone.exception.ZoneNotFoundException;

@RestControllerAdvice
public class DomainExceptionHandler {
    @ExceptionHandler(DomainNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleDomainNotFound(DomainNotFoundException ex) {
        ApiError error ${DB_USER:***REMOVED***} new ApiError("DOMAIN_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(error));
    }

    @ExceptionHandler(DomainAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleDomainAlreadyExists(DomainAlreadyExistsException ex) {
        ApiError error ${DB_USER:***REMOVED***} new ApiError("DOMAIN_ALREADY_EXISTS", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(error));
    }

    @ExceptionHandler(ZoneNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleZoneNotFound(ZoneNotFoundException ex) {
        ApiError error ${DB_USER:***REMOVED***} new ApiError("ZONE_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(error));
    }
}
