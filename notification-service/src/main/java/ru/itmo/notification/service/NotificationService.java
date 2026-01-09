package ru.itmo.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.itmo.common.notification.NotificationType;
import ru.itmo.common.notification.SendNotificationRequest;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final EmailService emailService;
    private final RestTemplate restTemplate;
    
    @Value("${services.auth.url}")
    private String authServiceUrl;

    public void sendNotification(SendNotificationRequest request) {
        String userEmail ${DB_USER:***REMOVED***} getUserEmail(request.getUserId());
        
        if (userEmail ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || userEmail.isEmpty()) {
            log.warn("User email not found for userId: {}", request.getUserId());
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

    private String getUserEmail(UUID userId) {
        try {
            String url ${DB_USER:***REMOVED***} authServiceUrl + "/users/" + userId + "/email";
            String email ${DB_USER:***REMOVED***} restTemplate.getForObject(url, String.class);
            log.debug("Retrieved email for userId {}: {}", userId, email);
            return email;
        } catch (Exception e) {
            log.error("Failed to get user email for userId: {}", userId, e);
            return null;
        }
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
