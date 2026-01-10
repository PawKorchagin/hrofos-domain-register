package ru.itmo.domainorder.dns.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExdnsClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${exdns.url:http://localhost:8095}")
    private String exdnsUrl;

    @Value("${exdns.api-token:changeme}")
    private String apiToken;

    public ExdnsZone getZone(String domainName) {
        try {
            HttpHeaders headers ${DB_USER:***REMOVED***} createHeaders();
            HttpEntity<Void> entity ${DB_USER:***REMOVED***} new HttpEntity<>(headers);

            String url ${DB_USER:***REMOVED***} exdnsUrl + "/zones/" + domainName;
            ResponseEntity<String> response ${DB_USER:***REMOVED***} restTemplate.exchange(
                    url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} HttpStatus.OK) {
                return objectMapper.readValue(response.getBody(), ExdnsZone.class);
            } else if (response.getStatusCode() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} HttpStatus.NOT_FOUND) {
                return null;
            } else {
                log.error("Failed to get zone {}: {}", domainName, response.getStatusCode());
                throw new RuntimeException("Failed to get zone: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error getting zone {}: {}", domainName, e.getMessage(), e);
            throw new RuntimeException("Error getting zone: " + e.getMessage(), e);
        }
    }

    public void createOrUpdateZone(String domainName, ExdnsZone zone) {
        try {
            HttpHeaders headers ${DB_USER:***REMOVED***} createHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String zoneJson ${DB_USER:***REMOVED***} objectMapper.writeValueAsString(zone);
            HttpEntity<String> entity ${DB_USER:***REMOVED***} new HttpEntity<>(zoneJson, headers);

            String url ${DB_USER:***REMOVED***} exdnsUrl + "/zones/" + domainName;
            ResponseEntity<String> response ${DB_USER:***REMOVED***} restTemplate.exchange(
                    url, HttpMethod.PUT, entity, String.class);

            if (response.getStatusCode() !${DB_USER:***REMOVED***} HttpStatus.OK && response.getStatusCode() !${DB_USER:***REMOVED***} HttpStatus.CREATED) {
                log.error("Failed to create/update zone {}: {}", domainName, response.getStatusCode());
                throw new RuntimeException("Failed to create/update zone: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error creating/updating zone {}: {}", domainName, e.getMessage(), e);
            throw new RuntimeException("Error creating/updating zone: " + e.getMessage(), e);
        }
    }

    public void deleteZone(String domainName) {
        try {
            HttpHeaders headers ${DB_USER:***REMOVED***} createHeaders();
            HttpEntity<Void> entity ${DB_USER:***REMOVED***} new HttpEntity<>(headers);

            String url ${DB_USER:***REMOVED***} exdnsUrl + "/zones/" + domainName;
            ResponseEntity<String> response ${DB_USER:***REMOVED***} restTemplate.exchange(
                    url, HttpMethod.DELETE, entity, String.class);

            if (response.getStatusCode() !${DB_USER:***REMOVED***} HttpStatus.OK) {
                if (response.getStatusCode() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} HttpStatus.NOT_FOUND) {
                    log.warn("Zone {} not found for deletion", domainName);
                    return;
                }
                log.error("Failed to delete zone {}: {}", domainName, response.getStatusCode());
                throw new RuntimeException("Failed to delete zone: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error deleting zone {}: {}", domainName, e.getMessage(), e);
            throw new RuntimeException("Error deleting zone: " + e.getMessage(), e);
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers ${DB_USER:***REMOVED***} new HttpHeaders();
        headers.set("Authentication", "Bearer " + apiToken);
        return headers;
    }

    public static class ExdnsZone {
        private String name;
        private Integer version;
        private List<ExdnsRecord> records ${DB_USER:***REMOVED***} new ArrayList<>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name ${DB_USER:***REMOVED***} name;
        }

        public Integer getVersion() {
            return version;
        }

        public void setVersion(Integer version) {
            this.version ${DB_USER:***REMOVED***} version;
        }

        public List<ExdnsRecord> getRecords() {
            return records;
        }

        public void setRecords(List<ExdnsRecord> records) {
            this.records ${DB_USER:***REMOVED***} records;
        }
    }

    public static class ExdnsRecord {
        private String name;
        private String type;
        private Integer ttl;
        private Object data;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name ${DB_USER:***REMOVED***} name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type ${DB_USER:***REMOVED***} type;
        }

        public Integer getTtl() {
            return ttl;
        }

        public void setTtl(Integer ttl) {
            this.ttl ${DB_USER:***REMOVED***} ttl;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data ${DB_USER:***REMOVED***} data;
        }
    }
}
