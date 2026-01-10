package ru.itmo.domainorder.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.domainorder.cart.dto.AddCartItemRequest;
import ru.itmo.domainorder.cart.dto.CartItemResponse;
import ru.itmo.domainorder.cart.dto.CartResponse;
import ru.itmo.domainorder.cart.entity.Cart;
import ru.itmo.domainorder.cart.entity.CartItem;
import ru.itmo.domainorder.cart.exception.CartItemAlreadyExistsException;
import ru.itmo.domainorder.cart.exception.CartItemNotFoundException;
import ru.itmo.domainorder.cart.exception.CartNotFoundException;
import ru.itmo.domainorder.cart.mapper.CartMapper;
import ru.itmo.domainorder.cart.repository.CartItemRepository;
import ru.itmo.domainorder.cart.repository.CartRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;

    @Transactional
    public CartResponse getCartByUserId(UUID userId) {
        Cart cart ${DB_USER:***REMOVED***} cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart ${DB_USER:***REMOVED***} new Cart();
                    newCart.setUserId(userId);
                    return cartRepository.save(newCart);
                });
        
        List<CartItem> items ${DB_USER:***REMOVED***} cartItemRepository.findByCartId(cart.getId());
        List<CartItemResponse> itemResponses ${DB_USER:***REMOVED***} cartMapper.toResponseList(items);
        
        return cartMapper.toResponse(cart, itemResponses);
    }

    @Transactional
    public CartResponse getOrCreateCart(UUID userId) {
        Cart cart ${DB_USER:***REMOVED***} cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart ${DB_USER:***REMOVED***} new Cart();
                    newCart.setUserId(userId);
                    return cartRepository.save(newCart);
                });
        
        List<CartItem> items ${DB_USER:***REMOVED***} cartItemRepository.findByCartId(cart.getId());
        List<CartItemResponse> itemResponses ${DB_USER:***REMOVED***} cartMapper.toResponseList(items);
        
        return cartMapper.toResponse(cart, itemResponses);
    }

    @Transactional
    public CartItemResponse addItem(UUID userId, AddCartItemRequest request) {
        Cart cart ${DB_USER:***REMOVED***} getOrCreateCartEntity(userId);
        
        if (cartItemRepository.existsByCartIdAndFqdnAndActionAndTerm(
                cart.getId(), request.getFqdn(), request.getAction(), request.getTerm())) {
            throw new CartItemAlreadyExistsException(
                    "Item already exists in cart: " + request.getFqdn() + " " + request.getAction() + " " + request.getTerm());
        }
        
        CartItem item ${DB_USER:***REMOVED***} cartMapper.toEntity(request);
        item.setCartId(cart.getId());
        item ${DB_USER:***REMOVED***} cartItemRepository.save(item);
        
        return cartMapper.toResponse(item);
    }

    @Transactional
    public void removeItem(UUID userId, UUID itemId) {
        Cart cart ${DB_USER:***REMOVED***} cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found for user: " + userId));
        
        CartItem item ${DB_USER:***REMOVED***} cartItemRepository.findById(itemId)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found: " + itemId));
        
        if (!item.getCartId().equals(cart.getId())) {
            throw new CartItemNotFoundException("Cart item does not belong to user's cart");
        }
        
        cartItemRepository.delete(item);
    }

    @Transactional
    public void clearCart(UUID userId) {
        Cart cart ${DB_USER:***REMOVED***} cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found for user: " + userId));
        
        cartItemRepository.deleteByCartId(cart.getId());
    }

    private Cart getOrCreateCartEntity(UUID userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart ${DB_USER:***REMOVED***} new Cart();
                    newCart.setUserId(userId);
                    return cartRepository.save(newCart);
                });
    }
}
