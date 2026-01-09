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
public class NotificationPreferencesResponse {
    private List<ExpiryAlertPreference> expiryAlerts;
    private boolean orderNotificationsEnabled;
    private boolean paymentNotificationsEnabled;
    private boolean domainActivationNotificationsEnabled;
}
