package ru.itmo.domainorder.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.domainorder.cart.entity.Cart;
import ru.itmo.domainorder.cart.entity.CartItem;
import ru.itmo.domainorder.cart.exception.CartItemNotFoundException;
import ru.itmo.domainorder.cart.exception.CartNotFoundException;
import ru.itmo.domainorder.cart.repository.CartItemRepository;
import ru.itmo.domainorder.cart.repository.CartRepository;
import ru.itmo.domainorder.domain.entity.Domain;
import ru.itmo.domainorder.domain.repository.DomainRepository;
import ru.itmo.domainorder.order.dto.CreateOrderRequest;
import ru.itmo.domainorder.order.dto.OrderItemResponse;
import ru.itmo.domainorder.order.dto.OrderResponse;
import ru.itmo.domainorder.order.entity.Order;
import ru.itmo.domainorder.order.entity.OrderItem;
import ru.itmo.domainorder.order.enumeration.OrderStatus;
import ru.itmo.domainorder.order.exception.OrderNotFoundException;
import ru.itmo.domainorder.order.mapper.OrderMapper;
import ru.itmo.domainorder.order.repository.OrderItemRepository;
import ru.itmo.domainorder.order.repository.OrderRepository;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final DomainRepository domainRepository;
    private final OrderMapper orderMapper;

    @Transactional(readOnly ${DB_USER:***REMOVED***} true)
    public Page<OrderResponse> getOrdersByUserId(UUID userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable)
                .map(order -> {
                    List<OrderItem> items ${DB_USER:***REMOVED***} orderItemRepository.findByOrderId(order.getId());
                    List<OrderItemResponse> itemResponses ${DB_USER:***REMOVED***} orderMapper.toResponseList(items);
                    return orderMapper.toResponse(order, itemResponses);
                });
    }

    @Transactional(readOnly ${DB_USER:***REMOVED***} true)
    public OrderResponse getOrderById(UUID id, UUID userId) {
        Order order ${DB_USER:***REMOVED***} orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        
        if (!order.getUserId().equals(userId)) {
            throw new OrderNotFoundException("Order not found with id: " + id);
        }
        
        List<OrderItem> items ${DB_USER:***REMOVED***} orderItemRepository.findByOrderId(order.getId());
        List<OrderItemResponse> itemResponses ${DB_USER:***REMOVED***} orderMapper.toResponseList(items);
        
        return orderMapper.toResponse(order, itemResponses);
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request, UUID userId) {
        Cart cart ${DB_USER:***REMOVED***} cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found for user: " + userId));

        List<CartItem> cartItems ${DB_USER:***REMOVED***} request.getCartItemIds().stream()
                .map(cartItemRepository::findById)
                .map(item -> item.orElseThrow(() -> new CartItemNotFoundException("Cart item not found")))
                .filter(item -> item.getCartId().equals(cart.getId()))
                .collect(Collectors.toList());

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("No valid cart items found");
        }

        BigInteger totalAmount ${DB_USER:***REMOVED***} cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigInteger.ZERO, BigInteger::add);

        Order order ${DB_USER:***REMOVED***} new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.created);
        order.setTotalAmount(totalAmount);
        order ${DB_USER:***REMOVED***} orderRepository.save(order);

        List<OrderItem> orderItems ${DB_USER:***REMOVED***} new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            Domain domain ${DB_USER:***REMOVED***} createOrGetDomain(cartItem, userId);
            
            OrderItem orderItem ${DB_USER:***REMOVED***} new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setAction(cartItem.getAction());
            orderItem.setTerm(cartItem.getTerm());
            orderItem.setDomainId(domain.getId());
            orderItem ${DB_USER:***REMOVED***} orderItemRepository.save(orderItem);
            orderItems.add(orderItem);

            cartItemRepository.delete(cartItem);
        }

        List<OrderItemResponse> itemResponses ${DB_USER:***REMOVED***} orderMapper.toResponseList(orderItems);
        return orderMapper.toResponse(order, itemResponses);
    }

    @Transactional
    public OrderResponse updateOrderStatus(UUID id, OrderStatus status) {
        Order order ${DB_USER:***REMOVED***} orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        
        order.setStatus(status);
        if (status ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} OrderStatus.paid) {
            order.setPaidAt(LocalDateTime.now());
        }
        order ${DB_USER:***REMOVED***} orderRepository.save(order);
        
        List<OrderItem> items ${DB_USER:***REMOVED***} orderItemRepository.findByOrderId(order.getId());
        List<OrderItemResponse> itemResponses ${DB_USER:***REMOVED***} orderMapper.toResponseList(items);
        
        return orderMapper.toResponse(order, itemResponses);
    }

    @Transactional
    public OrderResponse cancelOrder(UUID id) {
        return updateOrderStatus(id, OrderStatus.cancelled);
    }

    private Domain createOrGetDomain(CartItem cartItem, UUID userId) {
        return domainRepository.findByFqdn(cartItem.getFqdn())
                .orElseGet(() -> {
                    // TODO: Create domain if action is 'register', or throw exception if action is 'renew'
                    throw new IllegalArgumentException("Domain not found for FQDN: " + cartItem.getFqdn());
                });
    }
}
