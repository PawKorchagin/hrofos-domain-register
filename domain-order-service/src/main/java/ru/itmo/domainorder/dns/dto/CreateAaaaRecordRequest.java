package ru.itmo.domainorder.dns.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateAaaaRecordRequest {
    @NotNull(message ${DB_USER:***REMOVED***} "Domain ID is required")
    private UUID domainId;

    @NotBlank(message ${DB_USER:***REMOVED***} "Name is required")
    private String name;

    @NotBlank(message ${DB_USER:***REMOVED***} "IPv6 address is required")
    @Pattern(regexp ${DB_USER:***REMOVED***} "^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$|^::1$|^::$",
            message ${DB_USER:***REMOVED***} "Invalid IPv6 address format")
    private String ipv6Address;

    @NotNull(message ${DB_USER:***REMOVED***} "TTL is required")
    private Integer ttl;
}
