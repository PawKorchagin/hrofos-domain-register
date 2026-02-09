package ru.itmo.payment.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class YooKassaClient {

    private static final Logger log ${DB_USER:***REMOVED***} LoggerFactory.getLogger(YooKassaClient.class);
    private static final String BASE_URL ${DB_USER:***REMOVED***} "https://api.yookassa.ru/v3";
    private static final String PAYMENTS_PATH ${DB_USER:***REMOVED***} "/payments";

    private final RestTemplate restTemplate;
    private final YooKassaClientProperties properties;
    private final ObjectMapper objectMapper;

    public YooKassaClient(RestTemplate restTemplate, YooKassaClientProperties properties, ObjectMapper objectMapper) {
        this.restTemplate ${DB_USER:***REMOVED***} restTemplate;
        this.properties ${DB_USER:***REMOVED***} properties;
        this.objectMapper ${DB_USER:***REMOVED***} objectMapper;
    }

    public YooKassaCreateResponse createPayment(String orderId, int amount, String currency, String description, String returnUrl) {
        String url ${DB_USER:***REMOVED***} UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path(PAYMENTS_PATH)
                .toUriString();

        if (properties.getShopId() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || properties.getShopId().isBlank()) {
            throw new YooKassaClientException("Shop ID is required", null);
        }
        if (properties.getSecretKey() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || properties.getSecretKey().isBlank()) {
            throw new YooKassaClientException("Secret key is required", null);
        }

        Map<String, Object> requestBody ${DB_USER:***REMOVED***} new LinkedHashMap<>();
        requestBody.put("amount", Map.of(
                "value", String.format("%.2f", amount / 100.0),
                "currency", currency !${DB_USER:***REMOVED***} null ? currency : "RUB"
        ));
        requestBody.put("confirmation", Map.of(
                "type", "redirect",
                "return_url", returnUrl !${DB_USER:***REMOVED***} null ? returnUrl : properties.getReturnUrl()
        ));
        requestBody.put("capture", true);
        requestBody.put("description", description);
        requestBody.put("metadata", Map.of("orderId", orderId));

        HttpHeaders headers ${DB_USER:***REMOVED***} createAuthHeaders();
        headers.set("Idempotence-Key", orderId);

        try {
            log.info("YooKassa createPayment request url${DB_USER:***REMOVED***}{} body${DB_USER:***REMOVED***}{}", url, requestBody);
            HttpEntity<Map<String, Object>> entity ${DB_USER:***REMOVED***} new HttpEntity<>(requestBody, headers);
            String responseBody ${DB_USER:***REMOVED***} restTemplate.postForObject(url, entity, String.class);
            log.info("YooKassa createPayment response body${DB_USER:***REMOVED***}{}", responseBody);
            return parseCreateResponse(responseBody);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.warn("YooKassa createPayment error status${DB_USER:***REMOVED***}{} body${DB_USER:***REMOVED***}{}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new YooKassaClientException("Failed to create payment: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.warn("YooKassa createPayment error: {}", e.getMessage());
            throw new YooKassaClientException("Failed to create payment", e);
        }
    }

    public YooKassaPaymentInfo getPaymentInfo(String paymentId) {
        String url ${DB_USER:***REMOVED***} UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path(PAYMENTS_PATH)
                .path("/")
                .path(paymentId)
                .toUriString();

        HttpHeaders headers ${DB_USER:***REMOVED***} createAuthHeaders();

        try {
            log.info("YooKassa getPaymentInfo request url${DB_USER:***REMOVED***}{}", url);
            HttpEntity<Void> entity ${DB_USER:***REMOVED***} new HttpEntity<>(headers);
            String responseBody ${DB_USER:***REMOVED***} restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
            log.info("YooKassa getPaymentInfo response body${DB_USER:***REMOVED***}{}", responseBody);
            return parsePaymentInfo(responseBody);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.warn("YooKassa getPaymentInfo error status${DB_USER:***REMOVED***}{} body${DB_USER:***REMOVED***}{}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new YooKassaClientException("Failed to get payment info: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.warn("YooKassa getPaymentInfo error: {}", e.getMessage());
            throw new YooKassaClientException("Failed to get payment info", e);
        }
    }

    private HttpHeaders createAuthHeaders() {
        HttpHeaders headers ${DB_USER:***REMOVED***} new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String auth ${DB_USER:***REMOVED***} properties.getShopId() + ":" + properties.getSecretKey();
        String encodedAuth ${DB_USER:***REMOVED***} Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        headers.set("Authorization", "Basic " + encodedAuth);
        return headers;
    }

    private YooKassaCreateResponse parseCreateResponse(String responseBody) {
        if (responseBody ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || responseBody.isBlank()) {
            return new YooKassaCreateResponse(null, null);
        }
        try {
            JsonNode node ${DB_USER:***REMOVED***} objectMapper.readTree(responseBody);
            String paymentId ${DB_USER:***REMOVED***} node.path("id").asText(null);
            JsonNode confirmation ${DB_USER:***REMOVED***} node.path("confirmation");
            String confirmationUrl ${DB_USER:***REMOVED***} confirmation.path("confirmation_url").asText(null);
            return new YooKassaCreateResponse(paymentId, confirmationUrl);
        } catch (Exception e) {
            log.warn("Failed to parse YooKassa create response: {}", e.getMessage());
            return new YooKassaCreateResponse(null, null);
        }
    }

    private YooKassaPaymentInfo parsePaymentInfo(String responseBody) {
        if (responseBody ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || responseBody.isBlank()) {
            log.warn("YooKassa payment info response is null or blank");
            return new YooKassaPaymentInfo(null);
        }
        try {
            JsonNode node ${DB_USER:***REMOVED***} objectMapper.readTree(responseBody);
            String status ${DB_USER:***REMOVED***} node.path("status").asText(null);
            log.info("Parsed YooKassa payment status: {}", status);
            return new YooKassaPaymentInfo(status);
        } catch (Exception e) {
            log.warn("Failed to parse YooKassa payment info: {}, response body: {}", e.getMessage(), responseBody);
            return new YooKassaPaymentInfo(null);
        }
    }

    public static class YooKassaCreateResponse {
        private final String paymentId;
        private final String confirmationUrl;

        public YooKassaCreateResponse(String paymentId, String confirmationUrl) {
            this.paymentId ${DB_USER:***REMOVED***} paymentId;
            this.confirmationUrl ${DB_USER:***REMOVED***} confirmationUrl;
        }

        public String getPaymentId() {
            return paymentId;
        }

        public String getConfirmationUrl() {
            return confirmationUrl;
        }
    }

    public static class YooKassaPaymentInfo {
        private final String status;

        public YooKassaPaymentInfo(String status) {
            this.status ${DB_USER:***REMOVED***} status;
        }

        public String getStatus() {
            return status;
        }
    }
}
