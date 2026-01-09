package ru.itmo.common.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendNotificationRequest {
    private UUID userId;
    private NotificationType type;
    private String subject;
    private String template;
    private Map<String, Object> parameters;
}
