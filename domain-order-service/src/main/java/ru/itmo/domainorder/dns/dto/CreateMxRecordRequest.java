package ru.itmo.domainorder.dns.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateMxRecordRequest {
    @NotNull(message ${DB_USER:***REMOVED***} "Domain ID is required")
    private UUID domainId;

    @NotBlank(message ${DB_USER:***REMOVED***} "Name is required")
    private String name;

    @NotBlank(message ${DB_USER:***REMOVED***} "Mail exchange hostname is required")
    private String mailExchange;

    @NotNull(message ${DB_USER:***REMOVED***} "Priority is required")
    @Min(value ${DB_USER:***REMOVED***} 0, message ${DB_USER:***REMOVED***} "Priority must be non-negative")
    private Integer priority;

    @NotNull(message ${DB_USER:***REMOVED***} "TTL is required")
    private Integer ttl;
}
