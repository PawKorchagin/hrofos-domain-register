package ru.itmo.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.itmo.common.notification.NotificationType;
import ru.itmo.common.notification.SendNotificationRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final EmailService emailService;

    public void sendNotification(SendNotificationRequest request, String userEmail) {
        if (userEmail ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || userEmail.isEmpty()) {
            log.warn("User email is null or empty");
            throw new IllegalArgumentException("User email not found");
        }

        String subject ${DB_USER:***REMOVED***} request.getSubject() !${DB_USER:***REMOVED***} null 
            ? request.getSubject() 
            : getDefaultSubject(request.getType());

        emailService.sendNotification(
            userEmail,
            request.getType(),
            subject,
            request.getParameters()
        );
    }

    private String getDefaultSubject(NotificationType type) {
        return switch (type) {
            case ORDER_CREATED -> "Заказ создан";
            case PAYMENT_APPROVED -> "Платеж успешно обработан";
            case DOMAIN_ACTIVATED -> "Домен активирован";
            case DOMAIN_EXPIRING_SOON -> "Напоминание: срок действия домена истекает";
            case DOMAIN_EXPIRED -> "Срок действия домена истек";
            case EMAIL_VERIFICATION -> "Подтверждение email адреса";
        };
    }
}
