package ru.itmo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDomainDetailedResponse {
    private Long id;
    private String fqdn;
    private String zoneName;
    private String activatedAt;
    private String expiresAt;
}
