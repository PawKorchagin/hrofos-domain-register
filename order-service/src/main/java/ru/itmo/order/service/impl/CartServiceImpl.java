package ru.itmo.order.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.order.client.DomainClient;
import ru.itmo.order.entity.Cart;
import ru.itmo.order.generated.model.CartResponse;
import ru.itmo.order.repository.CartRepository;
import ru.itmo.order.service.CartService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final DomainClient domainClient;

    @Value("${domain.monthly-price:200}")
    private int monthlyPrice;

    @Value("${domain.yearly-discount:0.7}")
    private double yearlyDiscount;

    public CartServiceImpl(CartRepository cartRepository, DomainClient domainClient) {
        this.cartRepository ${DB_USER:***REMOVED***} cartRepository;
        this.domainClient ${DB_USER:***REMOVED***} domainClient;
    }

    @Override
    @Transactional
    public void addToCart(UUID userId, String l3Domain) {
        String trimmed ${DB_USER:***REMOVED***} l3Domain ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null ? null : l3Domain.trim();
        if (trimmed ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || trimmed.isBlank()) {
            throw new IllegalArgumentException("l3Domain must not be blank");
        }
        if (cartRepository.existsByUserIdAndL3Domain(userId, trimmed)) {
            return;
        }
        Cart cart ${DB_USER:***REMOVED***} new Cart();
        cart.setUserId(userId);
        cart.setL3Domain(trimmed);
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public List<String> checkout(UUID userId, String jwtToken) {
        List<Cart> cartItems ${DB_USER:***REMOVED***} cartRepository.findByUserIdOrderByL3Domain(userId);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        List<String> l3Domains ${DB_USER:***REMOVED***} cartItems.stream()
                .map(Cart::getL3Domain)
                .collect(Collectors.toList());

        // Send domains to domain-service for registration
        List<String> createdDomains ${DB_USER:***REMOVED***} domainClient.createUserDomains(l3Domains, jwtToken);

        // Clear cart after successful registration
        cartRepository.deleteByUserId(userId);

        return createdDomains;
    }

    @Override
    public CartResponse getCartByUserId(UUID userId) {
        List<String> l3Domains ${DB_USER:***REMOVED***} cartRepository.findByUserIdOrderByL3Domain(userId).stream()
                .map(Cart::getL3Domain)
                .collect(Collectors.toList());

        int domainCount ${DB_USER:***REMOVED***} l3Domains.size();
        int totalMonthlyPrice ${DB_USER:***REMOVED***} domainCount * monthlyPrice;
        int totalYearlyPrice ${DB_USER:***REMOVED***} (int) Math.round(totalMonthlyPrice * 12 * yearlyDiscount);

        CartResponse response ${DB_USER:***REMOVED***} new CartResponse();
        response.setTotalMonthlyPrice(totalMonthlyPrice);
        response.setTotalYearlyPrice(totalYearlyPrice);
        response.setL3Domains(l3Domains);
        return response;
    }
}
