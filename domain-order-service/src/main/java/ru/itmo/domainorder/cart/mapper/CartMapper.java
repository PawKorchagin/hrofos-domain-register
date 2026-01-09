package ru.itmo.domainorder.cart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.itmo.domainorder.cart.entity.Cart;
import ru.itmo.domainorder.cart.entity.CartItem;
import ru.itmo.domainorder.cart.dto.AddCartItemRequest;
import ru.itmo.domainorder.cart.dto.CartItemResponse;
import ru.itmo.domainorder.cart.dto.CartResponse;

import java.util.List;

@Mapper(componentModel ${DB_USER:***REMOVED***} "spring")
public interface CartMapper {
    @Mapping(target ${DB_USER:***REMOVED***} "id", ignore ${DB_USER:***REMOVED***} true)
    @Mapping(target ${DB_USER:***REMOVED***} "cartId", ignore ${DB_USER:***REMOVED***} true)
    @Mapping(target ${DB_USER:***REMOVED***} "createdAt", ignore ${DB_USER:***REMOVED***} true)
    @Mapping(target ${DB_USER:***REMOVED***} "updatedAt", ignore ${DB_USER:***REMOVED***} true)
    CartItem toEntity(AddCartItemRequest request);

    CartItemResponse toResponse(CartItem item);

    List<CartItemResponse> toResponseList(List<CartItem> items);

    @Mapping(target ${DB_USER:***REMOVED***} "items", source ${DB_USER:***REMOVED***} "items")
    CartResponse toResponse(Cart cart, List<CartItemResponse> items);
}
