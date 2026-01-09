package ru.itmo.notification.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.itmo.common.dto.ApiError;
import ru.itmo.common.dto.ApiResponse;
import ru.itmo.notification.service.EmailSendingException;

@RestControllerAdvice
@Slf4j
public class EmailSendingExceptionHandler {

    @ExceptionHandler(EmailSendingException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmailSendingException(EmailSendingException e) {
        log.error("Email sending failed", e);
        ApiError error ${DB_USER:***REMOVED***} new ApiError(
            "EMAIL_SENDING_FAILED",
            "Failed to send email: " + e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(error));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Invalid request", e);
        ApiError error ${DB_USER:***REMOVED***} new ApiError(
            "INVALID_REQUEST",
            e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(error));
    }
}
