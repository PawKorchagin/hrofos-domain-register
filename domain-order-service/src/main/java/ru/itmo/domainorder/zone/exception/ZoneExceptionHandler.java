package ru.itmo.domainorder.zone.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.itmo.common.dto.ApiResponse;
import ru.itmo.common.dto.ApiError;

@RestControllerAdvice
public class ZoneExceptionHandler {
    @ExceptionHandler(ZoneNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleZoneNotFound(ZoneNotFoundException ex) {
        ApiError error ${DB_USER:***REMOVED***} new ApiError("ZONE_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(error));
    }

    @ExceptionHandler(ZoneAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleZoneAlreadyExists(ZoneAlreadyExistsException ex) {
        ApiError error ${DB_USER:***REMOVED***} new ApiError("ZONE_ALREADY_EXISTS", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(error));
    }

    @ExceptionHandler(ZoneHasDomainsException.class)
    public ResponseEntity<ApiResponse<Void>> handleZoneHasDomains(ZoneHasDomainsException ex) {
        ApiError error ${DB_USER:***REMOVED***} new ApiError("ZONE_HAS_DOMAINS", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(error));
    }
}
