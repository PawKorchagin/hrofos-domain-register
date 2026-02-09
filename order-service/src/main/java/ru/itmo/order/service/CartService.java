package ru.itmo.order.service;

import ru.itmo.order.generated.model.CartResponse;
import java.util.UUID;

public interface CartService {

    void addToCart(UUID userId, String l3Domain);

    CartResponse getCartByUserId(UUID userId);
}
