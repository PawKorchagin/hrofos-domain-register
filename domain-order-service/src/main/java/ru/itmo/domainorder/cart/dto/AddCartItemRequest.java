package ru.itmo.domainorder.cart.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.itmo.domainorder.common.enumeration.ItemAction;
import ru.itmo.domainorder.common.enumeration.ItemTerm;

import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddCartItemRequest {
    @NotNull(message ${DB_USER:***REMOVED***} "Action is required")
    private ItemAction action;

    @NotNull(message ${DB_USER:***REMOVED***} "Term is required")
    private ItemTerm term;

    @NotBlank(message ${DB_USER:***REMOVED***} "FQDN is required")
    private String fqdn;

    @NotNull(message ${DB_USER:***REMOVED***} "Price is required")
    @Positive(message ${DB_USER:***REMOVED***} "Price must be positive")
    private BigInteger price;
}
