package ru.itmo.domain.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix ${DB_USER:***REMOVED***} "exdns.client")
public class ExdnsClientProperties {

    private String baseUrl ${DB_USER:***REMOVED***} "http://localhost:8000";
    private String apiToken ${DB_USER:***REMOVED***} "changeme";

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl ${DB_USER:***REMOVED***} baseUrl;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken ${DB_USER:***REMOVED***} apiToken;
    }
}
