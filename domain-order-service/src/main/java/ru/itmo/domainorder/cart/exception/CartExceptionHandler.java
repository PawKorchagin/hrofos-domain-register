package ru.itmo.domainorder.cart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.itmo.common.dto.ApiResponse;
import ru.itmo.common.dto.ApiError;

@RestControllerAdvice
public class CartExceptionHandler {
    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleCartNotFound(CartNotFoundException ex) {
        ApiError error ${DB_USER:***REMOVED***} new ApiError("CART_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(error));
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleCartItemNotFound(CartItemNotFoundException ex) {
        ApiError error ${DB_USER:***REMOVED***} new ApiError("CART_ITEM_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(error));
    }

    @ExceptionHandler(CartItemAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleCartItemAlreadyExists(CartItemAlreadyExistsException ex) {
        ApiError error ${DB_USER:***REMOVED***} new ApiError("CART_ITEM_ALREADY_EXISTS", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(error));
    }
}
