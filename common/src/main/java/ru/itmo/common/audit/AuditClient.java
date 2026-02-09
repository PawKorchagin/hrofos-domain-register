package ru.itmo.common.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

public class AuditClient {

    private static final Logger log ${DB_USER:***REMOVED***} LoggerFactory.getLogger(AuditClient.class);

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public AuditClient(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate ${DB_USER:***REMOVED***} restTemplate;
        this.baseUrl ${DB_USER:***REMOVED***} baseUrl;
    }

    public void log(String description, UUID userId) {
        try {
            String url ${DB_USER:***REMOVED***} baseUrl + "/audit/events";

            HttpHeaders headers ${DB_USER:***REMOVED***} new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> body ${DB_USER:***REMOVED***} new java.util.HashMap<>();
            body.put("description", description);
            if (userId !${DB_USER:***REMOVED***} null) {
                body.put("userId", userId.toString());
            }

            HttpEntity<Map<String, String>> entity ${DB_USER:***REMOVED***} new HttpEntity<>(body, headers);
            restTemplate.postForEntity(url, entity, Void.class);
        } catch (Exception e) {
            log.warn("Failed to send audit event: {}", e.getMessage());
        }
    }

    public void log(String description) {
        log(description, null);
    }
}
