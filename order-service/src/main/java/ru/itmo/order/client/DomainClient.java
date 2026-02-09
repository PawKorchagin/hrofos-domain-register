package ru.itmo.order.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
public class DomainClient {

    private static final String USER_DOMAINS_PATH ${DB_USER:***REMOVED***} "/domains/userDomains";

    private final RestTemplate restTemplate;
    private final DomainClientProperties properties;
    private final ObjectMapper objectMapper;

    public DomainClient(RestTemplate restTemplate, DomainClientProperties properties, ObjectMapper objectMapper) {
        this.restTemplate ${DB_USER:***REMOVED***} restTemplate;
        this.properties ${DB_USER:***REMOVED***} properties;
        this.objectMapper ${DB_USER:***REMOVED***} objectMapper;
    }

    public List<String> createUserDomains(List<String> l3Domains, String jwtToken) {
        String url ${DB_USER:***REMOVED***} UriComponentsBuilder.fromHttpUrl(properties.getBaseUrl())
                .path(USER_DOMAINS_PATH)
                .toUriString();

        HttpHeaders headers ${DB_USER:***REMOVED***} new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + jwtToken);

        try {
            HttpEntity<List<String>> entity ${DB_USER:***REMOVED***} new HttpEntity<>(l3Domains, headers);
            List<String> response ${DB_USER:***REMOVED***} restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<List<String>>() {}
            ).getBody();
            return response !${DB_USER:***REMOVED***} null ? response : List.of();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            String errorMessage ${DB_USER:***REMOVED***} parseErrorFromBody(e.getResponseBodyAsString());
            throw new DomainClientException("Failed to create user domains: " + errorMessage, e);
        }
    }

    private String parseErrorFromBody(String responseBody) {
        if (responseBody ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || responseBody.isBlank()) {
            return "Unknown error";
        }
        try {
            ObjectMapper mapper ${DB_USER:***REMOVED***} new ObjectMapper();
            var node ${DB_USER:***REMOVED***} mapper.readTree(responseBody);
            if (node !${DB_USER:***REMOVED***} null && node.isArray() && node.size() > 0) {
                return node.get(0).asText();
            }
            if (node !${DB_USER:***REMOVED***} null && node.has("message")) {
                return node.get("message").asText();
            }
        } catch (Exception ignored) {
        }
        return responseBody;
    }
}
