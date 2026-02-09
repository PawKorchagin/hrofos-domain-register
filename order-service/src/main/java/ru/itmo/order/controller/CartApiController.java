package ru.itmo.order.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.order.generated.api.CartApi;
import ru.itmo.order.service.CartService;

import java.util.List;
import java.util.UUID;

@RestController
@org.springframework.web.bind.annotation.RequestMapping("${openapi.orderService.base-path:/orders}")
public class CartApiController implements CartApi {

    private final CartService cartService;

    public CartApiController(CartService cartService) {
        this.cartService ${DB_USER:***REMOVED***} cartService;
    }

    @Override
    public ResponseEntity<List<String>> getCartMe() {
        Authentication auth ${DB_USER:***REMOVED***} org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || !(auth.getPrincipal() instanceof UUID)) {
            return ResponseEntity.status(401).build();
        }
        UUID userId ${DB_USER:***REMOVED***} (UUID) auth.getPrincipal();
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @Override
    public ResponseEntity<Void> addToCart(String l3Domain) {
        Authentication auth ${DB_USER:***REMOVED***} org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || !(auth.getPrincipal() instanceof UUID)) {
            return ResponseEntity.status(401).build();
        }
        UUID userId ${DB_USER:***REMOVED***} (UUID) auth.getPrincipal();
        cartService.addToCart(userId, l3Domain);
        return ResponseEntity.status(201).build();
    }
}
