package ru.itmo.domainorder.dns.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.itmo.common.dto.ApiResponse;
import ru.itmo.common.dto.ApiError;

@RestControllerAdvice
public class DnsExceptionHandler {
    @ExceptionHandler(DnsRecordNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleDnsRecordNotFound(DnsRecordNotFoundException ex) {
        ApiError error ${DB_USER:***REMOVED***} new ApiError("DNS_RECORD_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(error));
    }

    @ExceptionHandler(DomainAccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleDomainAccessDenied(DomainAccessDeniedException ex) {
        ApiError error ${DB_USER:***REMOVED***} new ApiError("DOMAIN_ACCESS_DENIED", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(error));
    }
}
