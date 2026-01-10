package ru.itmo.domainorder.cart.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.common.dto.ApiResponse;
import ru.itmo.domainorder.cart.dto.AddCartItemRequest;
import ru.itmo.domainorder.cart.dto.CartItemResponse;
import ru.itmo.domainorder.cart.dto.CartResponse;
import ru.itmo.domainorder.cart.service.CartService;
import ru.itmo.domainorder.util.SecurityUtil;

import java.util.UUID;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> getCart() {
        UUID userId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        if (userId ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(new ru.itmo.common.dto.ApiError("UNAUTHORIZED", "User not authenticated")));
        }
        CartResponse cart ${DB_USER:***REMOVED***} cartService.getCartByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(cart));
    }

    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartItemResponse>> addItem(
            @Valid @RequestBody AddCartItemRequest request) {
        UUID userId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        if (userId ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(new ru.itmo.common.dto.ApiError("UNAUTHORIZED", "User not authenticated")));
        }
        CartItemResponse item ${DB_USER:***REMOVED***} cartService.addItem(userId, request);
        return ResponseEntity.ok(ApiResponse.success(item));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse<Void>> removeItem(@PathVariable UUID itemId) {
        UUID userId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        if (userId ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(new ru.itmo.common.dto.ApiError("UNAUTHORIZED", "User not authenticated")));
        }
        cartService.removeItem(userId, itemId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> clearCart() {
        UUID userId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        if (userId ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(new ru.itmo.common.dto.ApiError("UNAUTHORIZED", "User not authenticated")));
        }
        cartService.clearCart(userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
