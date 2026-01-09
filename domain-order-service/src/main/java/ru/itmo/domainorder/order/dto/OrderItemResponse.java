package ru.itmo.domainorder.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.itmo.domainorder.common.enumeration.ItemAction;
import ru.itmo.domainorder.common.enumeration.ItemTerm;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
    private UUID id;
    private ItemAction action;
    private ItemTerm term;
    private UUID domainId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
