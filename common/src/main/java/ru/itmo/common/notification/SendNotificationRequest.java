package ru.itmo.common.notification;

import jakarta.validation.constraints.NotNull;
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
    @NotNull(message ${DB_USER:***REMOVED***} "User ID is required")
    private UUID userId;
    
    @NotNull(message ${DB_USER:***REMOVED***} "Notification type is required")
    private NotificationType type;
    
    private String subject;
    private String template;
    private Map<String, Object> parameters;

    /** Если указан — письмо отправляется на этот email (для системных уведомлений). */
    private String recipientEmail;
}
