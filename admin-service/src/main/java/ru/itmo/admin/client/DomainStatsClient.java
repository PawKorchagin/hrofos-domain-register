package ru.itmo.admin.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Slf4j
public class DomainStatsClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String baseUrl;

    public DomainStatsClient(RestTemplate restTemplate, ObjectMapper objectMapper,
                             @Value("${domain.client.base-url}") String baseUrl) {
        this.restTemplate ${DB_USER:***REMOVED***} restTemplate;
        this.objectMapper ${DB_USER:***REMOVED***} objectMapper;
        this.baseUrl ${DB_USER:***REMOVED***} baseUrl;
    }

    public DomainStats getDomainStats(String jwtToken) {
        String url ${DB_USER:***REMOVED***} UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/domains/stats")
                .toUriString();

        HttpHeaders headers ${DB_USER:***REMOVED***} new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);

        try {
            String body ${DB_USER:***REMOVED***} restTemplate.exchange(url, HttpMethod.GET,
                    new HttpEntity<>(headers), String.class).getBody();
            JsonNode node ${DB_USER:***REMOVED***} objectMapper.readTree(body);
            if (node !${DB_USER:***REMOVED***} null) {
                long activeUsers ${DB_USER:***REMOVED***} node.has("activeUsersCount") ? node.get("activeUsersCount").asLong() : 0;
                long registeredDomains ${DB_USER:***REMOVED***} node.has("registeredDomainsCount") ? node.get("registeredDomainsCount").asLong() : 0;
                return new DomainStats(activeUsers, registeredDomains);
            }
            throw new RuntimeException("Unexpected response from domain-service: " + body);
        } catch (Exception e) {
            log.error("Failed to get domain stats from domain-service", e);
            throw new RuntimeException("Failed to get domain stats from domain-service: " + e.getMessage(), e);
        }
    }

    public record DomainStats(long activeUsersCount, long registeredDomainsCount) {}
}
