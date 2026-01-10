package ru.itmo.domainorder.dns.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class NsDelegationRequest {
    @NotNull(message ${DB_USER:***REMOVED***} "NS servers list is required")
    @NotEmpty(message ${DB_USER:***REMOVED***} "At least one NS server is required")
    @Size(min ${DB_USER:***REMOVED***} 1, max ${DB_USER:***REMOVED***} 10, message ${DB_USER:***REMOVED***} "NS servers list must contain between 1 and 10 servers")
    private List<@jakarta.validation.constraints.NotBlank(message ${DB_USER:***REMOVED***} "NS server name cannot be blank") String> nsServers;
}
