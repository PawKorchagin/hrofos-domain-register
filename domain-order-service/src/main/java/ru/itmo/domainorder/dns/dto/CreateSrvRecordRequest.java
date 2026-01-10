package ru.itmo.domainorder.dns.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateSrvRecordRequest {
    @NotNull(message ${DB_USER:***REMOVED***} "Domain ID is required")
    private UUID domainId;

    @NotBlank(message ${DB_USER:***REMOVED***} "Service name is required")
    private String serviceName;

    @NotBlank(message ${DB_USER:***REMOVED***} "Target hostname is required")
    private String target;

    @NotNull(message ${DB_USER:***REMOVED***} "Priority is required")
    @Min(value ${DB_USER:***REMOVED***} 0, message ${DB_USER:***REMOVED***} "Priority must be non-negative")
    private Integer priority;

    @NotNull(message ${DB_USER:***REMOVED***} "Weight is required")
    @Min(value ${DB_USER:***REMOVED***} 0, message ${DB_USER:***REMOVED***} "Weight must be non-negative")
    @Max(value ${DB_USER:***REMOVED***} 65535, message ${DB_USER:***REMOVED***} "Weight must not exceed 65535")
    private Integer weight;

    @NotNull(message ${DB_USER:***REMOVED***} "Port is required")
    @Min(value ${DB_USER:***REMOVED***} 1, message ${DB_USER:***REMOVED***} "Port must be positive")
    @Max(value ${DB_USER:***REMOVED***} 65535, message ${DB_USER:***REMOVED***} "Port must not exceed 65535")
    private Integer port;

    @NotNull(message ${DB_USER:***REMOVED***} "TTL is required")
    private Integer ttl;
}
