package ru.itmo.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNotificationPreferencesRequest {
    private List<ExpiryAlertPreference> expiryAlerts;
    private Boolean orderNotificationsEnabled;
    private Boolean paymentNotificationsEnabled;
    private Boolean domainActivationNotificationsEnabled;
}
