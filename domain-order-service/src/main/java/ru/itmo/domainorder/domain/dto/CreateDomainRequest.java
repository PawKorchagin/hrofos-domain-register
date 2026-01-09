package ru.itmo.domainorder.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDomainRequest {
    @NotBlank(message ${DB_USER:***REMOVED***} "FQDN is required")
    private String fqdn;

    @NotNull(message ${DB_USER:***REMOVED***} "Zone ID is required")
    private UUID zoneId;

    @NotNull(message ${DB_USER:***REMOVED***} "Expires at is required")
    private LocalDateTime expiresAt;
}
