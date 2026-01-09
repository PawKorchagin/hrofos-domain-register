package ru.itmo.domainorder.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDomainRequest {
    private LocalDateTime expiresAt;
    private LocalDateTime activatedAt;
}
