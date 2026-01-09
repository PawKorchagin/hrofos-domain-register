package ru.itmo.domainorder.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.itmo.domainorder.common.enumeration.ItemAction;
import ru.itmo.domainorder.common.enumeration.ItemTerm;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
    private UUID id;
    private ItemAction action;
    private ItemTerm term;
    private String fqdn;
    private BigInteger price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
