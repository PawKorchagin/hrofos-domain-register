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
public class AuthClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String baseUrl;

    public AuthClient(RestTemplate restTemplate, ObjectMapper objectMapper,
                      @Value("${auth.client.base-url}") String baseUrl) {
        this.restTemplate ${DB_USER:***REMOVED***} restTemplate;
        this.objectMapper ${DB_USER:***REMOVED***} objectMapper;
        this.baseUrl ${DB_USER:***REMOVED***} baseUrl;
    }

    public long getUsersCount(String jwtToken) {
        String url ${DB_USER:***REMOVED***} UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/auth/stats/users-count")
                .toUriString();

        HttpHeaders headers ${DB_USER:***REMOVED***} new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);

        try {
            String body ${DB_USER:***REMOVED***} restTemplate.exchange(url, HttpMethod.GET,
                    new HttpEntity<>(headers), String.class).getBody();
            JsonNode node ${DB_USER:***REMOVED***} objectMapper.readTree(body);
            if (node !${DB_USER:***REMOVED***} null && node.has("data")) {
                return node.get("data").asLong();
            }
            throw new RuntimeException("Unexpected response from auth-service: " + body);
        } catch (Exception e) {
            log.error("Failed to get users count from auth-service", e);
            throw new RuntimeException("Failed to get users count from auth-service: " + e.getMessage(), e);
        }
    }
}
