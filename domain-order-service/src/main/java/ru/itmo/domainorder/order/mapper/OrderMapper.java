package ru.itmo.domainorder.order.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.itmo.domainorder.order.entity.Order;
import ru.itmo.domainorder.order.entity.OrderItem;
import ru.itmo.domainorder.order.dto.OrderItemResponse;
import ru.itmo.domainorder.order.dto.OrderResponse;

import java.util.List;

@Mapper(componentModel ${DB_USER:***REMOVED***} "spring")
public interface OrderMapper {
    OrderItemResponse toResponse(OrderItem item);

    List<OrderItemResponse> toResponseList(List<OrderItem> items);

    @Mapping(target ${DB_USER:***REMOVED***} "items", source ${DB_USER:***REMOVED***} "items")
    OrderResponse toResponse(Order order, List<OrderItemResponse> items);
}
