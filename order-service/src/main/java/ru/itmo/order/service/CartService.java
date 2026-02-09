package ru.itmo.order.service;

import ru.itmo.order.generated.model.CartResponse;
import java.util.List;
import java.util.UUID;

public interface CartService {

    void addToCart(UUID userId, String l3Domain);

    CartResponse getCartByUserId(UUID userId);

    List<String> checkout(UUID userId, String period, String jwtToken);
}
