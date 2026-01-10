package ru.itmo.auth.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.itmo.common.dto.ApiResponse;
import ru.itmo.common.dto.ApiError;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmailAlreadyExists(EmailAlreadyExistsException e) {
        log.error("Email already exists: {}", e.getMessage());
        ApiError error ${DB_USER:***REMOVED***} new ApiError("EMAIL_ALREADY_EXISTS", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error(error));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserNotFound(UserNotFoundException e) {
        log.error("User not found: {}", e.getMessage());
        ApiError error ${DB_USER:***REMOVED***} new ApiError("USER_NOT_FOUND", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(error));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalState(IllegalStateException e) {
        log.error("Illegal state: {}", e.getMessage());
        ApiError error ${DB_USER:***REMOVED***} new ApiError("ILLEGAL_STATE", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(error));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidCredentials(InvalidCredentialsException e) {
        log.error("Invalid credentials: {}", e.getMessage());
        ApiError error ${DB_USER:***REMOVED***} new ApiError("INVALID_CREDENTIALS", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(error));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidToken(InvalidTokenException e) {
        log.error("Invalid token: {}", e.getMessage());
        ApiError error ${DB_USER:***REMOVED***} new ApiError("INVALID_TOKEN", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(error));
    }

    @ExceptionHandler(TOTPRequiredException.class)
    public ResponseEntity<ApiResponse<Void>> handleTotpRequired(TOTPRequiredException e) {
        log.error("TOTP required: {}", e.getMessage());
        ApiError error ${DB_USER:***REMOVED***} new ApiError("TOTP_REQUIRED", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(error));
    }

    @ExceptionHandler(InvalidTOTPException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidTotp(InvalidTOTPException e) {
        log.error("Invalid TOTP: {}", e.getMessage());
        ApiError error ${DB_USER:***REMOVED***} new ApiError("INVALID_TOTP", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(error));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception e) {
        log.error("Unexpected error: ", e);
        ApiError error ${DB_USER:***REMOVED***} new ApiError("INTERNAL_ERROR", "An unexpected error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(error));
    }
}
