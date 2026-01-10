package ru.itmo.domainorder.dns.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateCnameRecordRequest {
    @NotNull(message ${DB_USER:***REMOVED***} "Domain ID is required")
    private UUID domainId;

    @NotBlank(message ${DB_USER:***REMOVED***} "Name is required")
    private String name;

    @NotBlank(message ${DB_USER:***REMOVED***} "Canonical name is required")
    private String canonicalName;

    @NotNull(message ${DB_USER:***REMOVED***} "TTL is required")
    private Integer ttl;
}
