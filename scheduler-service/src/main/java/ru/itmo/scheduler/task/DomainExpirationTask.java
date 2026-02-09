package ru.itmo.scheduler.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.itmo.scheduler.client.AuthServiceClient;
import ru.itmo.scheduler.client.DomainServiceClient;
import ru.itmo.scheduler.client.NotificationServiceClient;
import ru.itmo.scheduler.config.JwtTokenProvider;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DomainExpirationTask {

    private final DomainServiceClient domainServiceClient;
    private final AuthServiceClient authServiceClient;
    private final NotificationServiceClient notificationServiceClient;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${scheduler.expiring-notifications.days-before:30,14,7,3,1}")
    private String daysBefore;

    /**
     * Каждый день в 09:00 проверяет домены, истекающие через 30, 14, 7, 3, 1 дней,
     * и отправляет email-уведомления владельцам.
     */
    @Scheduled(cron ${DB_USER:***REMOVED***} "${scheduler.expiring-notifications.cron}")
    public void sendExpiringNotifications() {
        log.info("Starting expiring domain notifications check...");

        String token ${DB_USER:***REMOVED***} jwtTokenProvider.generateSystemToken();
        int[] thresholds ${DB_USER:***REMOVED***} parseThresholds(daysBefore);

        for (int days : thresholds) {
            processThreshold(days, token);
        }

        log.info("Expiring domain notifications check completed.");
    }

    /**
     * Каждый день в 03:00 удаляет просроченные домены.
     */
    @Scheduled(cron ${DB_USER:***REMOVED***} "${scheduler.expired-cleanup.cron}")
    public void cleanupExpiredDomains() {
        log.info("Starting expired domain cleanup...");

        String token ${DB_USER:***REMOVED***} jwtTokenProvider.generateSystemToken();
        long deleted ${DB_USER:***REMOVED***} domainServiceClient.deleteExpiredDomains(token);

        log.info("Expired domain cleanup completed. Deleted: {} domains.", deleted);
    }

    private void processThreshold(int days, String token) {
        List<Map<String, Object>> expiringDomains ${DB_USER:***REMOVED***} domainServiceClient.getExpiringDomains(days, token);

        if (expiringDomains.isEmpty()) {
            log.debug("No domains expiring in {} days.", days);
            return;
        }

        log.info("Found {} domains expiring in {} days.", expiringDomains.size(), days);

        // Собираем уникальные userId
        Set<String> userIds ${DB_USER:***REMOVED***} expiringDomains.stream()
                .map(d -> String.valueOf(d.get("userId")))
                .filter(id -> id !${DB_USER:***REMOVED***} null && !id.equals("null"))
                .collect(Collectors.toSet());

        if (userIds.isEmpty()) {
            return;
        }

        // Получаем email-адреса пользователей
        Map<String, String> emailMap ${DB_USER:***REMOVED***} authServiceClient.getEmailsByUserIds(new ArrayList<>(userIds), token);

        // Отправляем уведомления
        for (Map<String, Object> domain : expiringDomains) {
            String userId ${DB_USER:***REMOVED***} String.valueOf(domain.get("userId"));
            String domainName ${DB_USER:***REMOVED***} String.valueOf(domain.get("domainName"));
            String finishedAt ${DB_USER:***REMOVED***} String.valueOf(domain.get("finishedAt"));
            String email ${DB_USER:***REMOVED***} emailMap.get(userId);

            if (email ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
                log.warn("No email found for userId${DB_USER:***REMOVED***}{}, skipping domain${DB_USER:***REMOVED***}{}", userId, domainName);
                continue;
            }

            notificationServiceClient.sendExpiringNotification(
                    email,
                    UUID.fromString(userId),
                    domainName,
                    finishedAt,
                    days,
                    token
            );
        }
    }

    private int[] parseThresholds(String value) {
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .mapToInt(Integer::parseInt)
                .toArray();
    }
}
