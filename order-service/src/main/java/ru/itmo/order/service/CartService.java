package ru.itmo.order.service;

import ru.itmo.order.generated.model.CartResponse;
import ru.itmo.order.generated.model.PaymentLinkResponse;

import java.util.UUID;

public interface CartService {

    void addToCart(UUID userId, String l3Domain);

    CartResponse getCartByUserId(UUID userId);

    PaymentLinkResponse checkout(UUID userId, String period, String jwtToken);
}
