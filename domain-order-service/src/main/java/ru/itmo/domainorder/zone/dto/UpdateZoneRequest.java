package ru.itmo.domainorder.zone.dto;

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
public class UpdateZoneRequest {
    @Positive(message ${DB_USER:***REMOVED***} "Price must be positive")
    private BigInteger price;
}
