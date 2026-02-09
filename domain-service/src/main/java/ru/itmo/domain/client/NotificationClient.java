package ru.itmo.domain.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.itmo.common.notification.NotificationType;
import ru.itmo.common.notification.SendNotificationRequest;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class NotificationClient {

    private final RestTemplate restTemplate;
    private final HttpServletRequest httpServletRequest;
    private final String notificationServiceUrl;

    public NotificationClient(RestTemplate restTemplate,
                              HttpServletRequest httpServletRequest,
                              @Value("${services.notification.url}") String notificationServiceUrl) {
        this.restTemplate ${DB_USER:***REMOVED***} restTemplate;
        this.httpServletRequest ${DB_USER:***REMOVED***} httpServletRequest;
        this.notificationServiceUrl ${DB_USER:***REMOVED***} notificationServiceUrl;
    }

    public void sendDomainsActivated(UUID userId, List<String> domains, String expiresAt) {
        try {
            SendNotificationRequest request ${DB_USER:***REMOVED***} new SendNotificationRequest();
            request.setUserId(userId);
            request.setType(NotificationType.DOMAIN_ACTIVATED);
            request.setSubject("Активировано доменов: " + domains.size());
            request.setParameters(Map.of(
                    "domains", domains,
                    "expiresAt", expiresAt
            ));

            String jwtToken ${DB_USER:***REMOVED***} extractJwtToken();
            if (jwtToken ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
                log.warn("No JWT token found, skipping notification for domains: {}", domains);
                return;
            }

            HttpHeaders headers ${DB_USER:***REMOVED***} new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(jwtToken);

            HttpEntity<SendNotificationRequest> entity ${DB_USER:***REMOVED***} new HttpEntity<>(request, headers);

            String url ${DB_USER:***REMOVED***} notificationServiceUrl + "/notifications/send";
            restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
            log.info("Domain activation notification sent for {} domains", domains.size());
        } catch (Exception e) {
            log.warn("Failed to send domain activation notification: {}", e.getMessage());
        }
    }

    public void sendDomainsRenewed(UUID userId, Map<String, String> domainsWithExpiry) {
        try {
            SendNotificationRequest request ${DB_USER:***REMOVED***} new SendNotificationRequest();
            request.setUserId(userId);
            request.setType(NotificationType.DOMAIN_RENEWED);
            request.setSubject("Продлено доменов: " + domainsWithExpiry.size());
            request.setParameters(Map.of("domains", domainsWithExpiry));

            String jwtToken ${DB_USER:***REMOVED***} extractJwtToken();
            if (jwtToken ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
                log.warn("No JWT token found, skipping renewal notification");
                return;
            }

            HttpHeaders headers ${DB_USER:***REMOVED***} new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(jwtToken);

            HttpEntity<SendNotificationRequest> entity ${DB_USER:***REMOVED***} new HttpEntity<>(request, headers);

            String url ${DB_USER:***REMOVED***} notificationServiceUrl + "/notifications/send";
            restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
            log.info("Domain renewal notification sent for {} domains", domainsWithExpiry.size());
        } catch (Exception e) {
            log.warn("Failed to send domain renewal notification: {}", e.getMessage());
        }
    }

    private String extractJwtToken() {
        String authHeader ${DB_USER:***REMOVED***} httpServletRequest.getHeader("Authorization");
        if (authHeader !${DB_USER:***REMOVED***} null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
