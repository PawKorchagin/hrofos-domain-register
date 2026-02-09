package ru.itmo.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.order.entity.Cart;
import ru.itmo.order.entity.CartId;

import java.util.List;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, CartId> {

    boolean existsByUserIdAndL3Domain(UUID userId, String l3Domain);

    List<Cart> findByUserIdOrderByL3Domain(UUID userId);
}
