package ru.itmo.order.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.order.generated.api.CartApi;
import ru.itmo.order.generated.model.CartResponse;
import ru.itmo.order.generated.model.CheckoutRequest;
import ru.itmo.order.service.CartService;

import java.util.List;
import java.util.UUID;

@RestController
@org.springframework.web.bind.annotation.RequestMapping("${openapi.orderService.base-path:/orders}")
public class CartApiController implements CartApi {

    private final CartService cartService;
    private final HttpServletRequest httpServletRequest;

    public CartApiController(CartService cartService, HttpServletRequest httpServletRequest) {
        this.cartService ${DB_USER:***REMOVED***} cartService;
        this.httpServletRequest ${DB_USER:***REMOVED***} httpServletRequest;
    }

    @Override
    public ResponseEntity<CartResponse> getCartMe() {
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

    @Override
    public ResponseEntity<List<String>> checkout(CheckoutRequest checkoutRequest) {
        Authentication auth ${DB_USER:***REMOVED***} org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || !(auth.getPrincipal() instanceof UUID)) {
            return ResponseEntity.status(401).build();
        }
        UUID userId ${DB_USER:***REMOVED***} (UUID) auth.getPrincipal();

        String authHeader ${DB_USER:***REMOVED***} httpServletRequest.getHeader("Authorization");
        String jwtToken ${DB_USER:***REMOVED***} authHeader !${DB_USER:***REMOVED***} null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;
        if (jwtToken ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            return ResponseEntity.status(401).build();
        }

        String period ${DB_USER:***REMOVED***} checkoutRequest.getPeriod().getValue();
        List<String> createdDomains ${DB_USER:***REMOVED***} cartService.checkout(userId, period, jwtToken);
        return ResponseEntity.ok(createdDomains);
    }
}
