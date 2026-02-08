package ru.itmo.domain.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class ExdnsClient {

    private static final String ZONES_PATH ${DB_USER:***REMOVED***} "/zones/{name}";

    private final RestTemplate restTemplate;
    private final ExdnsClientProperties properties;
    private final ObjectMapper objectMapper;

    public ExdnsClient(RestTemplate restTemplate, ExdnsClientProperties properties, ObjectMapper objectMapper) {
        this.restTemplate ${DB_USER:***REMOVED***} restTemplate;
        this.properties ${DB_USER:***REMOVED***} properties;
        this.objectMapper ${DB_USER:***REMOVED***} objectMapper;
    }

    public void createZone(String l2Domain, JsonNode body) {
        exchange(l2Domain, HttpMethod.POST, body);
    }

    public void getZone(String l2Domain) {
        exchange(l2Domain, HttpMethod.GET, null);
    }

    public JsonNode getZoneBody(String l2Domain) {
        String url ${DB_USER:***REMOVED***} UriComponentsBuilder.fromHttpUrl(properties.getBaseUrl())
                .path(ZONES_PATH)
                .buildAndExpand(l2Domain)
                .toUriString();

        HttpHeaders headers ${DB_USER:***REMOVED***} new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authentication", "Bearer " + properties.getApiToken());

        try {
            JsonNode responseBody ${DB_USER:***REMOVED***} restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), JsonNode.class).getBody();
            failIfError(responseBody);
            return responseBody;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            String errorMessage ${DB_USER:***REMOVED***} parseErrorFromBody(e.getResponseBodyAsString());
            throw new ExdnsClientException(errorMessage, e);
        }
    }

    public void replaceZone(String l2Domain, JsonNode body) {
        exchange(l2Domain, HttpMethod.PUT, body);
    }

    public void deleteZone(String l2Domain) {
        exchange(l2Domain, HttpMethod.DELETE, null);
    }

    private void exchange(String l2Domain, HttpMethod method, JsonNode body) {
        String url ${DB_USER:***REMOVED***} UriComponentsBuilder.fromHttpUrl(properties.getBaseUrl())
                .path(ZONES_PATH)
                .buildAndExpand(l2Domain)
                .toUriString();

        HttpHeaders headers ${DB_USER:***REMOVED***} new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authentication", "Bearer " + properties.getApiToken());

        HttpEntity<JsonNode> entity ${DB_USER:***REMOVED***} body !${DB_USER:***REMOVED***} null
                ? new HttpEntity<>(body, headers)
                : new HttpEntity<>(headers);

        try {
            JsonNode responseBody ${DB_USER:***REMOVED***} restTemplate.exchange(url, method, entity, JsonNode.class).getBody();
            failIfError(responseBody);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            String errorMessage ${DB_USER:***REMOVED***} parseErrorFromBody(e.getResponseBodyAsString());
            throw new ExdnsClientException(errorMessage, e);
        }
    }

    private void failIfError(JsonNode body) {
        if (body !${DB_USER:***REMOVED***} null && body.has("error")) {
            throw new ExdnsClientException(body.get("error").asText());
        }
    }

    private String parseErrorFromBody(String responseBody) {
        if (responseBody ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || responseBody.isBlank()) {
            return "Unknown error";
        }
        try {
            JsonNode node ${DB_USER:***REMOVED***} objectMapper.readTree(responseBody);
            if (node !${DB_USER:***REMOVED***} null && node.has("error")) {
                return node.get("error").asText();
            }
        } catch (Exception ignored) {
        }
        return responseBody;
    }
}
