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
public class DomainServiceClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public DomainServiceClient(RestTemplate restTemplate,
                               @Value("${services.domain.url}") String baseUrl) {
        this.restTemplate ${DB_USER:***REMOVED***} restTemplate;
        this.baseUrl ${DB_USER:***REMOVED***} baseUrl;
    }

    public List<Map<String, Object>> getExpiringDomains(int days, String jwtToken) {
        try {
            String url ${DB_USER:***REMOVED***} baseUrl + "/domains/userDomains/expiring?days${DB_USER:***REMOVED***}" + days;

            HttpHeaders headers ${DB_USER:***REMOVED***} new HttpHeaders();
            headers.setBearerAuth(jwtToken);

            ResponseEntity<List<Map<String, Object>>> response ${DB_USER:***REMOVED***} restTemplate.exchange(
                    url, HttpMethod.GET, new HttpEntity<>(headers),
                    new ParameterizedTypeReference<>() {});

            return response.getBody() !${DB_USER:***REMOVED***} null ? response.getBody() : Collections.emptyList();
        } catch (Exception e) {
            log.warn("Failed to get expiring domains (days${DB_USER:***REMOVED***}{}): {}", days, e.getMessage());
            return Collections.emptyList();
        }
    }

    public long deleteExpiredDomains(String jwtToken) {
        try {
            String url ${DB_USER:***REMOVED***} baseUrl + "/domains/userDomains/expired";

            HttpHeaders headers ${DB_USER:***REMOVED***} new HttpHeaders();
            headers.setBearerAuth(jwtToken);

            ResponseEntity<Long> response ${DB_USER:***REMOVED***} restTemplate.exchange(
                    url, HttpMethod.DELETE, new HttpEntity<>(headers), Long.class);

            return response.getBody() !${DB_USER:***REMOVED***} null ? response.getBody() : 0;
        } catch (Exception e) {
            log.warn("Failed to delete expired domains: {}", e.getMessage());
            return 0;
        }
    }
}
