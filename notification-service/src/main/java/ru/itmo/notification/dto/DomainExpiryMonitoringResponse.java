package ru.itmo.notification.dto;

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
public class DomainExpiryMonitoringResponse {
    private UUID domainId;
    private String fqdn;
    private LocalDateTime expiresAt;
    private LocalDateTime activatedAt;
    private Integer daysUntilExpiry;
    private boolean alertSent;
    private LocalDateTime lastAlertSentAt;
}
