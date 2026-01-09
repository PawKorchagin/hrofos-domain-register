package ru.itmo.domainorder.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DomainSearchResult {
    private String fqdn;
    private Boolean free;
    private java.math.BigInteger price;
}
