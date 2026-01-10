package ru.itmo.domainorder.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.common.dto.ApiResponse;
import ru.itmo.domainorder.order.dto.CreateOrderRequest;
import ru.itmo.domainorder.order.dto.OrderResponse;
import ru.itmo.domainorder.order.enumeration.OrderStatus;
import ru.itmo.domainorder.order.service.OrderService;
import ru.itmo.domainorder.util.SecurityUtil;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> getOrders(Pageable pageable) {
        UUID userId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        if (userId ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(new ru.itmo.common.dto.ApiError("UNAUTHORIZED", "User not authenticated")));
        }
        Page<OrderResponse> orders ${DB_USER:***REMOVED***} orderService.getOrdersByUserId(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable UUID id) {
        UUID userId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        if (userId ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(new ru.itmo.common.dto.ApiError("UNAUTHORIZED", "User not authenticated")));
        }
        OrderResponse order ${DB_USER:***REMOVED***} orderService.getOrderById(id, userId);
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @Valid @RequestBody CreateOrderRequest request) {
        UUID userId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        if (userId ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(new ru.itmo.common.dto.ApiError("UNAUTHORIZED", "User not authenticated")));
        }
        OrderResponse order ${DB_USER:***REMOVED***} orderService.createOrder(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(order));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @PathVariable UUID id,
            @RequestParam OrderStatus status) {
        OrderResponse order ${DB_USER:***REMOVED***} orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(@PathVariable UUID id) {
        OrderResponse order ${DB_USER:***REMOVED***} orderService.cancelOrder(id);
        return ResponseEntity.ok(ApiResponse.success(order));
    }
}
