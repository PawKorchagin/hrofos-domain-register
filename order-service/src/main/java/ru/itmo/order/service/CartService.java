package ru.itmo.order.service;

import java.util.List;
import java.util.UUID;

public interface CartService {

    void addToCart(UUID userId, String l3Domain);

    List<String> getCartByUserId(UUID userId);
}
