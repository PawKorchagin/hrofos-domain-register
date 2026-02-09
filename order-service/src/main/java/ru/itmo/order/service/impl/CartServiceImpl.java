package ru.itmo.order.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.common.audit.AuditClient;
import ru.itmo.order.client.PaymentClient;
import ru.itmo.order.client.PaymentCreateRequest;
import ru.itmo.order.client.PaymentCreateResponse;
import ru.itmo.order.entity.Cart;
import ru.itmo.order.generated.model.CartResponse;
import ru.itmo.order.generated.model.PaymentLinkResponse;
import ru.itmo.order.repository.CartRepository;
import ru.itmo.order.service.CartService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final PaymentClient paymentClient;
    private final AuditClient auditClient;

    @Value("${domain.monthly-price:200}")
    private int monthlyPrice;

    @Value("${domain.yearly-discount:0.7}")
    private double yearlyDiscount;

    public CartServiceImpl(CartRepository cartRepository, PaymentClient paymentClient, AuditClient auditClient) {
        this.cartRepository ${DB_USER:***REMOVED***} cartRepository;
        this.paymentClient ${DB_USER:***REMOVED***} paymentClient;
        this.auditClient ${DB_USER:***REMOVED***} auditClient;
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
    public PaymentLinkResponse checkout(UUID userId, String period, String jwtToken) {
        List<Cart> cartItems ${DB_USER:***REMOVED***} cartRepository.findByUserIdOrderByL3Domain(userId);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        List<String> l3Domains ${DB_USER:***REMOVED***} cartItems.stream()
                .map(Cart::getL3Domain)
                .collect(Collectors.toList());

        int domainCount ${DB_USER:***REMOVED***} l3Domains.size();
        int totalMonthlyPrice ${DB_USER:***REMOVED***} domainCount * monthlyPrice;
        int amountInRubles ${DB_USER:***REMOVED***} "YEAR".equalsIgnoreCase(period)
                ? (int) Math.round(totalMonthlyPrice * 12 * yearlyDiscount)
                : totalMonthlyPrice;
        int amount ${DB_USER:***REMOVED***} amountInRubles * 100;

        PaymentCreateRequest paymentRequest ${DB_USER:***REMOVED***} new PaymentCreateRequest(
                l3Domains,
                period,
                amount,
                "RUB",
                "Domain payment"
        );
        PaymentCreateResponse paymentResponse ${DB_USER:***REMOVED***} paymentClient.createPayment(paymentRequest, jwtToken);
        if (paymentResponse ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || paymentResponse.getPaymentUrl() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            throw new IllegalStateException("Failed to create payment");
        }

        cartRepository.deleteByUserId(userId);

        auditClient.log("Payment initiated: " + paymentResponse.getPaymentId() + " (period${DB_USER:***REMOVED***}" + period + ")", userId);

        PaymentLinkResponse response ${DB_USER:***REMOVED***} new PaymentLinkResponse();
        response.setPaymentId(paymentResponse.getPaymentId());
        response.setPaymentUrl(paymentResponse.getPaymentUrl());
        response.setAmount(paymentResponse.getAmount());
        response.setCurrency(paymentResponse.getCurrency());
        response.setStatus(paymentResponse.getStatus());
        return response;
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
