package ru.itmo.domainorder.dns.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateCaaRecordRequest {
    @NotNull(message ${DB_USER:***REMOVED***} "Domain ID is required")
    private UUID domainId;

    @NotBlank(message ${DB_USER:***REMOVED***} "Name is required")
    private String name;

    @NotBlank(message ${DB_USER:***REMOVED***} "Flags are required")
    @Pattern(regexp ${DB_USER:***REMOVED***} "^[0-9]$", message ${DB_USER:***REMOVED***} "Flags must be a single digit (0-255)")
    private String flags;

    @NotBlank(message ${DB_USER:***REMOVED***} "Tag is required")
    @Pattern(regexp ${DB_USER:***REMOVED***} "^(issue|issuewild|iodef)$", message ${DB_USER:***REMOVED***} "Tag must be one of: issue, issuewild, iodef")
    private String tag;

    @NotBlank(message ${DB_USER:***REMOVED***} "Value is required")
    private String value;

    @NotNull(message ${DB_USER:***REMOVED***} "TTL is required")
    private Integer ttl;
}
