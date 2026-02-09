package ru.itmo.scheduler.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class AuthServiceClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public AuthServiceClient(RestTemplate restTemplate,
                             @Value("${services.auth.url}") String baseUrl) {
        this.restTemplate ${DB_USER:***REMOVED***} restTemplate;
        this.baseUrl ${DB_USER:***REMOVED***} baseUrl;
    }

    public Map<String, String> getEmailsByUserIds(List<String> userIds, String jwtToken) {
        try {
            String url ${DB_USER:***REMOVED***} baseUrl + "/auth/internal/emails";

            HttpHeaders headers ${DB_USER:***REMOVED***} new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(jwtToken);

            HttpEntity<List<String>> entity ${DB_USER:***REMOVED***} new HttpEntity<>(userIds, headers);

            ResponseEntity<Map<String, String>> response ${DB_USER:***REMOVED***} restTemplate.exchange(
                    url, HttpMethod.POST, entity,
                    new ParameterizedTypeReference<>() {});

            return response.getBody() !${DB_USER:***REMOVED***} null ? response.getBody() : Collections.emptyMap();
        } catch (Exception e) {
            log.warn("Failed to get emails from auth-service: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }
}
