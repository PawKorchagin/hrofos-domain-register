package ru.itmo.scheduler.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.itmo.common.notification.NotificationType;
import ru.itmo.common.notification.SendNotificationRequest;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class NotificationServiceClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public NotificationServiceClient(RestTemplate restTemplate,
                                     @Value("${services.notification.url}") String baseUrl) {
        this.restTemplate ${DB_USER:***REMOVED***} restTemplate;
        this.baseUrl ${DB_USER:***REMOVED***} baseUrl;
    }

    public void sendExpiringNotification(String recipientEmail, UUID userId,
                                         String domainName, String expiresAt, int daysLeft,
                                         String jwtToken) {
        try {
            SendNotificationRequest request ${DB_USER:***REMOVED***} new SendNotificationRequest();
            request.setUserId(userId);
            request.setType(NotificationType.DOMAIN_EXPIRING_SOON);
            request.setSubject("Домен " + domainName + " истекает через " + daysLeft + " дн.");
            request.setRecipientEmail(recipientEmail);
            request.setParameters(Map.of(
                    "domainName", domainName,
                    "expiresAt", expiresAt,
                    "daysLeft", String.valueOf(daysLeft)
            ));

            HttpHeaders headers ${DB_USER:***REMOVED***} new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(jwtToken);

            HttpEntity<SendNotificationRequest> entity ${DB_USER:***REMOVED***} new HttpEntity<>(request, headers);

            String url ${DB_USER:***REMOVED***} baseUrl + "/notifications/send";
            restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
            log.info("Expiring notification sent: domain${DB_USER:***REMOVED***}{}, email${DB_USER:***REMOVED***}{}, daysLeft${DB_USER:***REMOVED***}{}", domainName, recipientEmail, daysLeft);
        } catch (Exception e) {
            log.warn("Failed to send expiring notification for {}: {}", domainName, e.getMessage());
        }
    }
}
