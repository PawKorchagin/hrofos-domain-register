package ru.itmo.domainorder.zone.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateZoneRequest {
    @NotBlank(message ${DB_USER:***REMOVED***} "Zone name is required")
    private String name;

    @NotNull(message ${DB_USER:***REMOVED***} "Price is required")
    @Positive(message ${DB_USER:***REMOVED***} "Price must be positive")
    private BigInteger price;
}
