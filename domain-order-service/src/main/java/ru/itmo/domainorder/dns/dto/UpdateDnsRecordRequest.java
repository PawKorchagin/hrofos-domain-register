package ru.itmo.domainorder.dns.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateDnsRecordRequest {
    @NotBlank(message ${DB_USER:***REMOVED***} "Value is required")
    private String value;

    @NotNull(message ${DB_USER:***REMOVED***} "TTL is required")
    private Integer ttl;

    private Integer priority;
    private Integer weight;
    private Integer port;
    private String flags;
    private String tag;
}
