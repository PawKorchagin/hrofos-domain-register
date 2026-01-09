package ru.itmo.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpiryAlertPreference {
    private UUID id;
    private Integer daysBefore;
    private boolean enabled;
}
